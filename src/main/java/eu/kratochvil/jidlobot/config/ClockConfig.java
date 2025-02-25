package eu.kratochvil.jidlobot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Configuration class for providing a {@link Clock} bean.
 *
 * This class defines a {@code Clock} bean configured with the time zone
 * "Europe/Prague". It can be used throughout the application for working
 * with time operations in a consistent and testable manner.
 */
@Configuration
public class ClockConfig {

    ApplicationConfig config;

    public ClockConfig(ApplicationConfig config) {
        this.config = config;
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(config.getTimeZone()));
    }
}
