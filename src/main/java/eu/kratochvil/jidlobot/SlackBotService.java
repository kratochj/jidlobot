package eu.kratochvil.jidlobot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.jakarta_socket_mode.SocketModeApp;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.event.AppMentionEvent;
import eu.kratochvil.jidlobot.model.DailyMenu;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SlackBotService {

    private final DailyMenuMessageBuilder dailyMenuMessageBuilder;
    private final DailyMenuParser dailyMenuParser;

    private final SlackConfig slackConfig;

    public SlackBotService(SlackConfig slackConfig, DailyMenuMessageBuilder dailyMenuMessageBuilder, DailyMenuParser dailyMenuParser) {
        this.slackConfig = slackConfig;
        this.dailyMenuMessageBuilder = dailyMenuMessageBuilder;
        this.dailyMenuParser = dailyMenuParser;
    }

    @PostConstruct
    public void start() throws Exception {
        String botToken = slackConfig.getBotToken();
        AppConfig appConfig = AppConfig.builder().singleTeamBotToken(botToken).build();
        App app = new App(appConfig);

        app.event(AppMentionEvent.class, (req, ctx) -> {

            String response = processCommand(req.getEvent().getText());
            if (response != null) {
                ctx.say(response);
                return ctx.ack();
            }

            DailyMenu dailyMenu = dailyMenuParser.parse();
            String channelId = req.getEvent().getChannel();
            List<LayoutBlock> blocks = dailyMenuMessageBuilder.buildFormattedMessage(dailyMenu);
            try {
                ChatPostMessageResponse responseMenu = ctx.client().chatPostMessage(r -> r
                        .channel(channelId)
                        .blocks(blocks)
                        .text(dailyMenuMessageBuilder.buildPlainMessage(dailyMenu))
                );
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

    private @Nullable String processCommand(String text) {
        // Remove the bot mention from the text
        String commandText = text.replaceAll("<@\\w+>", "").trim().toLowerCase();

        return switch (commandText) {
            case "menu" -> null;
            case "help" -> "Dostupné příkazy:\n- @jidlobot menu\n- @jidlobot help";
            default -> "Neznámý příkaz. Zadej '@jidlobot help' pro dostupné příkazy.";
        };
    }
}
