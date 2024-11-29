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

    public ApplicationConfig(@Value("${menu.url}") String url,
                             @Value("${menu.cache-enabled:true}") boolean cacheEnabled,
                             @Value("${menu.cache-for-seconds:600}") long cacheForSeconds) {
        this.url = url;
        this.cacheEnabled = cacheEnabled;
        this.cacheForSeconds = cacheForSeconds;

        log.info("App configuration loaded");
        log.info("  URL: '{}'", url);
        log.info("  Cache enabled: '{}'", cacheEnabled);
        log.info("  Cache for seconds: '{}'", cacheForSeconds);
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
}
