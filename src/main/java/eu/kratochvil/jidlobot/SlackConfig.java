package eu.kratochvil.jidlobot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    private final String botToken;
    private final String appToken;

    public SlackConfig(@Value("${slack.botToken}") String botToken,
                       @Value("${slack.appToken}") String appToken
    ) {
        this.botToken = botToken;
        this.appToken = appToken;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getAppToken() {
        return appToken;
    }
}