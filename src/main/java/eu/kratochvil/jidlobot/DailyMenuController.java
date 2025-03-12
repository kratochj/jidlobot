package eu.kratochvil.jidlobot;

import eu.kratochvil.jidlobot.model.DailyMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DailyMenuController {

    private final DailyMenuCache dailyMenuCache;

    public DailyMenuController(DailyMenuCache dailyMenuCache) {
        this.dailyMenuCache = dailyMenuCache;
    }

    @GetMapping("/dailymenu")
    public DailyMenu getDailyMenu() {
        return dailyMenuCache.getMenu();
    }
}