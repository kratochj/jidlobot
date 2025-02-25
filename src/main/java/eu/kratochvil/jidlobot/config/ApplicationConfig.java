package eu.kratochvil.jidlobot.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    public static final Logger log = LoggerFactory.getLogger(SlackConfig.class);

    private final String url;
    private final boolean cacheEnabled;
    private final long cacheForSeconds;
    private final String timeZone;

    public ApplicationConfig(@Value("${menu.url}") String url,
                             @Value("${menu.cache-enabled:true}") boolean cacheEnabled,
                             @Value("${menu.cache-for-seconds:600}") long cacheForSeconds,
                             @Value("${application.time-zone:Europe/Prague}") String timeZone) {
        this.url = url;
        this.cacheEnabled = cacheEnabled;
        this.cacheForSeconds = cacheForSeconds;
        this.timeZone = timeZone;

        log.info("App configuration loaded");
        log.info("  URL: '{}'", url);
        log.info("  Cache enabled: '{}'", cacheEnabled);
        log.info("  Cache for seconds: '{}'", cacheForSeconds);
        log.info("  Time zone: '{}'", timeZone);
    }

    @NotNull
    public String getUrl() {
        return url;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public long getCacheForSeconds() {
        return cacheForSeconds;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
