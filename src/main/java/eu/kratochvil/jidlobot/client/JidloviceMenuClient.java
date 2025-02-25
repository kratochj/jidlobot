package eu.kratochvil.jidlobot.client;

import eu.kratochvil.jidlobot.client.dto.DailyMenuResponse;
import eu.kratochvil.jidlobot.client.dto.Meal;
import eu.kratochvil.jidlobot.client.dto.MenuItemWrapper;
import eu.kratochvil.jidlobot.config.ApplicationConfig;
import eu.kratochvil.jidlobot.model.DailyMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class JidloviceMenuClient {
    private static final Logger log = LoggerFactory.getLogger(JidloviceMenuClient.class);

    private static final String DAILY_MENU_URL =
            "https://www.jidlovice.cz/api/v1/branch/3/menu/{date}?include_internal_tags=false";

    private final RestTemplate restTemplate;
    private final ApplicationConfig config;

    public JidloviceMenuClient(RestTemplate restTemplate, ApplicationConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    /**
     * Fetches the daily menu from Jídlovice and maps it into our DailyMenu model.
     */
    public DailyMenu getDailyMenu(Instant date) {
        LocalDate localDate = LocalDate.ofInstant(date, ZoneId.of(config.getTimeZone()));
        var dateStr = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("Reading daily menu from the website for date: {}", dateStr);

        DailyMenuResponse response = restTemplate.getForObject(
                config.getUrl(),
                DailyMenuResponse.class,
                dateStr
        );

        List<DailyMenu.Soup> soups = new ArrayList<>();
        List<DailyMenu.Dish> dishesOfTheDay = new ArrayList<>();
        List<DailyMenu.Dish> specialDishes = new ArrayList<>();

        if (response != null && response.getMenu_items() != null) {
            for (MenuItemWrapper item : response.getMenu_items()) {
                Meal currentMeal = item.getMeal();
                if (currentMeal == null) {
                    continue;
                }
                addMealToCategory(currentMeal, soups, dishesOfTheDay, specialDishes);
            }
        }
        DailyMenu dailyMenu = new DailyMenu();
        dailyMenu.setSoups(soups);
        dailyMenu.setDishesOfTheDay(dishesOfTheDay);
        dailyMenu.setSpecialDishes(specialDishes);
        return dailyMenu;
    }

    private void addMealToCategory(Meal meal,
                                   List<DailyMenu.Soup> soups,
                                   List<DailyMenu.Dish> dishesOfTheDay,
                                   List<DailyMenu.Dish> specialDishes) {
        int mealCategoryId = meal.getCategory_id();
        log.debug("Adding meal to category: {}", mealCategoryId);
        log.debug("  meal: {}", meal);
        switch (mealCategoryId) {
            case 1:
                soups.add(new DailyMenu.Soup(
                        meal.getName(),
                        meal.getDescription(),
                        null,
                        meal.getPrice()
                ));
                break;
            case 2:
                dishesOfTheDay.add(new DailyMenu.Dish(
                        meal.getName(),
                        meal.getDescription(),
                        null,
                        meal.getPrice()
                ));
                break;
            case 3:
                specialDishes.add(new DailyMenu.Dish(
                        meal.getName(),
                        meal.getDescription(),
                        null,
                        meal.getPrice()
                ));
                break;
            default:
                // Not needed for final DailyMenu
                break;
        }
    }
}

