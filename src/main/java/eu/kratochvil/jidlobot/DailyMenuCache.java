package eu.kratochvil.jidlobot;

import eu.kratochvil.jidlobot.client.JidloviceMenuClient;
import eu.kratochvil.jidlobot.config.ApplicationConfig;
import eu.kratochvil.jidlobot.metrics.JidlobotMetrics;
import eu.kratochvil.jidlobot.model.DailyMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

/**
 * <p>
 * Service responsible for caching and retrieving the daily menu.
 * </p><p>
 * DailyMenuCache interacts with the JidloviceMenuClient to fetch the daily menu
 * from an external source and caches the result for a configurable amount of time.
 * The cache is updated as needed based on the time elapsed or when caching is disabled.
 * </p>
 */
@Service
public class DailyMenuCache {
    private static final Logger log = LoggerFactory.getLogger(DailyMenuCache.class);

    private final JidloviceMenuClient jidloviceMenuClient;
    private final ApplicationConfig applicationConfig;
    private final Clock clock;
    private final JidlobotMetrics jidlobotMetrics;

    private DailyMenu dailyMenu = null;
    private Instant dailyMenuLastUpdated = null;

    public DailyMenuCache(JidloviceMenuClient jidloviceMenuClient, ApplicationConfig applicationConfig, Clock clock, JidlobotMetrics jidlobotMetrics) {
        this.jidloviceMenuClient = jidloviceMenuClient;
        this.applicationConfig = applicationConfig;
        this.clock = clock;
        this.jidlobotMetrics = jidlobotMetrics;
    }

    /**
     * Retrieves the daily menu, either from the cache or by fetching it from the external source.
     * If caching is enabled and the cache is valid, the cached menu is returned.
     * Otherwise, the menu is refreshed from the external source and cached for future use.
     *
     * @return the current daily menu
     */
    public DailyMenu getMenu() {
        if (!applicationConfig.isCacheEnabled() || dailyMenu == null || dailyMenuLastUpdated == null
                || dailyMenuLastUpdated.isBefore(Instant.now().minusSeconds(applicationConfig.getCacheForSeconds()))) {
            //dailyMenu = dailyMenuParser.parse(applicationConfig.getUrl());
            log.debug("Refreshing menu from the website");
            dailyMenu = jidloviceMenuClient.getDailyMenu(clock.instant());
            jidlobotMetrics.recordMenuDownloaded();
            dailyMenuLastUpdated = clock.instant();
        }
        log.debug("Sending menu to slack");
        return dailyMenu;
    }
}
