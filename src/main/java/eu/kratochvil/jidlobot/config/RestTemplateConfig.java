package eu.kratochvil.jidlobot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for providing a {@link RestTemplate} bean.
 *
 * This class defines and configures a {@code RestTemplate} bean that can be used
 * throughout the application for making HTTP requests. The {@code RestTemplate}
 * instance is centralized here to facilitate customization and consistent usage.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
