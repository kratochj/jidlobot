package eu.kratochvil.jidlobot.parser;

import eu.kratochvil.jidlobot.model.DailyMenu;

public class DishMenuItemCreator implements MenuItemCreator<DailyMenu.Dish> {

    @Override
    public DailyMenu.Dish create(String name, String description, String allergens, double price) {
        return new DailyMenu.Dish(name, description, allergens, price);
    }
}
