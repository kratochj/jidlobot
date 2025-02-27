package eu.kratochvil.jidlobot.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * <p>
 * JidlobotMetrics is a component responsible for collecting and recording
 * metrics related to the usage of Jidlobot's menu functionalities.
 * </p><p>
 * It uses a MeterRegistry to define and manage counters for tracking actions
 * such as menu displays and menu downloads.
 * </p><p>
 * The metrics provided by this class can be used for monitoring and analyzing
 * how frequently menus are displayed or downloaded by users.
 * </p>
 */
@Component
public class JidlobotMetrics {

    private final Counter menuDisplayedCounter;
    private final Counter menuDownloadedCounter;

    public JidlobotMetrics(MeterRegistry meterRegistry) {
        menuDisplayedCounter = meterRegistry.counter("jidlobot_menu_displayed", "jidlobot", "menu", "user", "usage");
        menuDownloadedCounter = meterRegistry.counter("jidlobot_menu_downloaded", "jidlobot", "menu", "user", "usage");
    }

    /**
     * Increments the counter tracking the number of times a menu is displayed.
     * This method is used to record occurrences of menu displays as a metric
     * for monitoring user interaction with the Jidlobot interface.
     */
    public void recordMenuDisplayed() {
        menuDisplayedCounter.increment();
    }

    /**
     * Increments the counter tracking the number of times a menu is downloaded.
     * This method is used to record occurrences of menu downloads as a metric
     * for monitoring user interaction with the Jidlobot interface.
     */
    public void recordMenuDownloaded() {
        menuDownloadedCounter.increment();
    }
}

