package eu.kratochvil.jidlobot;

import eu.kratochvil.jidlobot.model.DailyMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DailyMenuController {

    @GetMapping("/dailymenu")
    public DailyMenu getDailyMenu() {
        // Here you can build and return your DailyMenu object.
        DailyMenu dailyMenu = new DailyMenu();
        dailyMenu.setMenu("Today's Special: Grilled Chicken with Vegetables");
        return dailyMenu;
    }
}