package eu.kratochvil.jidlobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    public static final Logger log = LoggerFactory.getLogger(SlackConfig.class);

    private final String botToken;
    private final String appToken;

    public SlackConfig(@Value("${slack.bot-token}") String botToken,
                       @Value("${slack.app-token}") String appToken
    ) {
        this.botToken = botToken;
        this.appToken = appToken;
        log.info("Slack configuration loaded");
        log.debug("  Bot token: '{}'", botToken);
        log.debug("  App token: '{}'", appToken);
    }

    public String getBotToken() {
        return botToken.trim();
    }

    public String getAppToken() {
        return appToken.trim();
    }
}