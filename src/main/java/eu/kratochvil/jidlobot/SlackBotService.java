package eu.kratochvil.jidlobot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.jakarta_socket_mode.SocketModeApp;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.AppMentionEvent;
import com.slack.api.model.event.MessageEvent;
import eu.kratochvil.jidlobot.client.JidloviceMenuClient;
import eu.kratochvil.jidlobot.config.ApplicationConfig;
import eu.kratochvil.jidlobot.config.SlackConfig;
import eu.kratochvil.jidlobot.model.DailyMenu;
import eu.kratochvil.jidlobot.slack.BotMessageBuilder;
import eu.kratochvil.jidlobot.slack.DailyMenuMessageBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

@Service
public class SlackBotService {
    public final static Logger log = LoggerFactory.getLogger(SlackBotService.class);

    private final DailyMenuMessageBuilder dailyMenuMessageBuilder;
    private final BotMessageBuilder botMessageBuilder;
    private final DailyMenuCache dailyMenuCache;
    private SocketModeApp socketModeApp = null;

    private final SlackConfig slackConfig;

    public SlackBotService(SlackConfig slackConfig,
                           DailyMenuMessageBuilder dailyMenuMessageBuilder,
                           BotMessageBuilder botMessageBuilder,
                           DailyMenuCache dailyMenuCache) {
        this.slackConfig = slackConfig;
        this.dailyMenuMessageBuilder = dailyMenuMessageBuilder;
        this.botMessageBuilder = botMessageBuilder;
        this.dailyMenuCache = dailyMenuCache;
    }

    @PostConstruct
    public void start() throws Exception {
        String botToken = slackConfig.getBotToken();
        AppConfig appConfig = AppConfig.builder().singleTeamBotToken(botToken).build();
        App app = new App(appConfig);
        app.config().setSigningSecret(null);

        app.event(MessageEvent.class, (req, ctx)
                -> processSlackEvent(true, req.getEvent().getText(), ctx));

        app.event(AppMentionEvent.class, (req, ctx)
                -> processSlackEvent(false, req.getEvent().getText(), ctx));

        socketModeApp = new SocketModeApp(slackConfig.getAppToken(), app);
        socketModeApp.startAsync();
    }

    private Response processSlackEvent(boolean isIm, String req, EventContext ctx) {
        try {
            // Retrieve the user ID of the sender
            String senderUserId = ctx.getRequestUserId();
            log.debug("Message received from user: {}", senderUserId);

            // Check if the message was sent by the bot itself
            if (senderUserId.equals(ctx.getBotUserId())) {
                log.info("Message ignored as it was sent by the bot itself.");
                return ctx.ack(); // Acknowledge the event without processing
            }

            ChatPostMessageRequest message = processCommand(isIm, req);
            message.setChannel(ctx.getChannelId());
            ChatPostMessageResponse responseMenu = ctx.client().chatPostMessage(message);
            if (!responseMenu.isOk()) {
                log.error("Error posting message: {}", responseMenu.getError());
            }
        } catch (IOException | SlackApiException e) {
            log.error("Exception while posting message: {}", e.getMessage());
        }
        return ctx.ack();
    }

    private @NotNull ChatPostMessageRequest processCommand(boolean isIm, String text) {
        // Remove the bot mention from the text
        String commandText = text.replaceAll("<@\\w+>", "").trim().toLowerCase();

        return switch (commandText) {
            case "menu" -> dailyMenuMessageBuilder.getChatPostMessage(dailyMenuCache.getMenu());
            case "help" -> botMessageBuilder.getChatPostHelpMessage(isIm);
            default -> botMessageBuilder.getChatPostGenericMessage(isIm);
        };
    }

    @PreDestroy
    public void stop() throws Exception {
        if (socketModeApp != null) {
            socketModeApp.stop(); // Gracefully stop the SocketModeApp
        }
    }
}
