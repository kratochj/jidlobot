package eu.kratochvil.jidlobot.parser;

import eu.kratochvil.jidlobot.model.DailyMenu;

public class SoupMenuItemCreator implements MenuItemCreator<DailyMenu.Soup> {

    public SoupMenuItemCreator() {
    }

    @Override
    public DailyMenu.Soup create(String name, String description, String allergens, double price) {
        return new DailyMenu.Soup(name, description, allergens, price);
    }
}
