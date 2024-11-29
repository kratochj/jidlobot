package eu.kratochvil.jidlobot.parser;

import eu.kratochvil.jidlobot.model.DailyMenu;

public interface MenuItemCreator <T extends DailyMenu.MenuItem> {
    T create(String name, String description, String allergens, double price);
}
