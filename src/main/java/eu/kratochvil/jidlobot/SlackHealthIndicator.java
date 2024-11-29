package eu.kratochvil.jidlobot;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.auth.AuthTestResponse;
import eu.kratochvil.jidlobot.config.SlackConfig;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackHealthIndicator implements HealthIndicator {

    private final SlackConfig slackConfig;

    public SlackHealthIndicator(SlackConfig slackConfig) {
        this.slackConfig = slackConfig;
    }

    @Override
    public Health health() {
        boolean isHealthy = true;
        String statusMessage = "Operational";

        // Check token validity
        try {
            AuthTestResponse authTestResponse = Slack.getInstance().methods(slackConfig.getBotToken()).authTest(req -> req);
            if (!authTestResponse.isOk()) {
                isHealthy = false;
                statusMessage = "Invalid bot token: " + authTestResponse.getError();
            }
        } catch (IOException | SlackApiException e) {
            isHealthy = false;
            statusMessage = "Token validation failed: " + e.getMessage();
        }

        if (isHealthy) {
            return Health.up()
                    .withDetail("SlackBot", statusMessage)
                    .build();
        } else {
            return Health.down()
                    .withDetail("SlackBot", statusMessage)
                    .build();
        }
    }
}
