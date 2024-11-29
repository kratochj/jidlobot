package eu.kratochvil.jidlobot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.jakarta_socket_mode.SocketModeApp;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.AppMentionEvent;
import eu.kratochvil.jidlobot.model.DailyMenu;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
public class SlackBotService {

    private final DailyMenuMessageBuilder dailyMenuMessageBuilder;
    private final DailyMenuParser dailyMenuParser;
    private final BotMessageBuilder botMessageBuilder;
    private final ApplicationConfig applicationConfig;

    private DailyMenu dailyMenu = null;
    private Instant dailyMenuLastUpdated = null;

    private final SlackConfig slackConfig;

    public SlackBotService(SlackConfig slackConfig,
                           DailyMenuMessageBuilder dailyMenuMessageBuilder,
                           DailyMenuParser dailyMenuParser,
                           BotMessageBuilder botMessageBuilder,
                           ApplicationConfig applicationConfig) {
        this.slackConfig = slackConfig;
        this.dailyMenuMessageBuilder = dailyMenuMessageBuilder;
        this.dailyMenuParser = dailyMenuParser;
        this.botMessageBuilder = botMessageBuilder;
        this.applicationConfig = applicationConfig;
    }

    @PostConstruct
    public void start() throws Exception {
        String botToken = slackConfig.getBotToken();
        AppConfig appConfig = AppConfig.builder().singleTeamBotToken(botToken).build();
        App app = new App(appConfig);

        app.event(AppMentionEvent.class, (req, ctx) -> {
            try {
                ChatPostMessageRequest message = processCommand(req.getEvent().getText());
                message.setChannel(ctx.getChannelId());
                ChatPostMessageResponse responseMenu = ctx.client().chatPostMessage(message);
                if (!responseMenu.isOk()) {
                    ctx.logger.error("Error posting message: {}", responseMenu.getError());
                }
            } catch (IOException | SlackApiException e) {
                ctx.logger.error("Exception while posting message: {}", e.getMessage());
            }
            return ctx.ack();
        });

        SocketModeApp socketModeApp = new SocketModeApp(slackConfig.getAppToken(), app);
        socketModeApp.start();
    }

    private DailyMenu getMenu() {
        if(!applicationConfig.isCacheEnabled() || dailyMenu == null || dailyMenuLastUpdated == null
                || dailyMenuLastUpdated.isBefore(Instant.now().minusSeconds(applicationConfig.getCacheForSeconds()))) {
            dailyMenu = dailyMenuParser.parse();
            dailyMenuLastUpdated = Instant.now();
        }
        return dailyMenu;
    }

    private @NotNull ChatPostMessageRequest processCommand(String text) {
        // Remove the bot mention from the text
        String commandText = text.replaceAll("<@\\w+>", "").trim().toLowerCase();

        return switch (commandText) {
            case "menu" -> dailyMenuMessageBuilder.getChatPostMessage(getMenu());
            case "help" -> botMessageBuilder.getChatPostHelpMessage();
            default -> botMessageBuilder.getChatPostGenericMessage();
        };
    }
}
