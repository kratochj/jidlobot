package eu.kratochvil.jidlobot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    private final String verificationToken;
    private final String signingSecret;
    private final String clientSecret;
    private final String clientId;
    private final String botToken;
    private final String appToken;

    public SlackConfig(@Value("${slack.verificationToken}") String verificationToken,
                       @Value("${slack.signingSecret}") String signingSecret,
                       @Value("${slack.clientId}") String clientId,
                       @Value("${slack.clientSecret}") String clientSecret,
                       @Value("${slack.botToken}") String botToken,
                       @Value("${slack.appToken}") String appToken
    ) {
        this.verificationToken = verificationToken;
        this.signingSecret = signingSecret;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.botToken = botToken;
        this.appToken = appToken;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public String getSigningSecret() {
        return signingSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getAppToken() {
        return appToken;
    }
}