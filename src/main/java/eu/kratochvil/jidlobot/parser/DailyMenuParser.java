package eu.kratochvil.jidlobot.parser;

import eu.kratochvil.jidlobot.JsoupConnector;
import eu.kratochvil.jidlobot.model.DailyMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DailyMenuParser is responsible for parsing daily menu information
 * from a given URL using an external JsoupConnector to extract HTML
 * documents. It extracts and processes sections of the document to
 * create a DailyMenu object containing soups and dishes of the day.
 */
@Component
public class DailyMenuParser {

    public static final Logger log = LoggerFactory.getLogger(DailyMenuParser.class);

    private final JsoupConnector jsoupConnector;

    public DailyMenuParser(JsoupConnector jsoupConnector) {
        this.jsoupConnector = jsoupConnector;
    }

    /**
     * Parses the daily menu from the provided URL.
     *
     * @param menuUrl the URL of the menu to parse. Must not be null.
     * @return a DailyMenu object containing soups and dishes of the day,
     *         or null if an error occurs during fetching or parsing.
     */
    public DailyMenu parse(@NotNull String menuUrl) {
        try {
            // Download and parse the webpage
            Document document = jsoupConnector.getDocument(menuUrl);

            DailyMenu menu = new DailyMenu();
            menu.setSoups(parseMenuItems(document, "p:contains(POLÉVKY)", "JÍDLA DNE", new SoupMenuItemCreator()));
            menu.setDishesOfTheDay(parseMenuItems(document, "h4:contains(JÍDLA DNE), p:contains(JÍDLA DNE)", "JÍDLOVICKÝ SPECIÁL", new DishMenuItemCreator()));
            menu.setSpecialDishes(parseMenuItems(document, "h4:contains(JÍDLOVICKÝ SPECIÁL), p:contains(JÍDLOVICKÝ SPECIÁL)", "JÍDLOVICKÉ STÁLICE", new DishMenuItemCreator()));

            // Print the menu to verify
            logMenuItems("Soups", menu.getSoups());
            logMenuItems("Dishes of the Day:", menu.getDishesOfTheDay());
            logMenuItems("Special Dish:", menu.getSpecialDishes());

            return menu;
        } catch (IOException e) {
            log.error("Error fetching the daily menu: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Parses menu items from a specified section of a document. 
     * The section is defined by a start selector and ends where a specified end text is encountered.
     *
     * @param document the HTML document to parse the menu items from.
     * @param sectionStartSelector the CSS selector indicating the start of the section to parse.
     * @param sectionEnd the marker text that indicates the end of the section.
     * @param itemCreator the creator object used to instantiate menu item objects of type T.
     * @return a list of menu items of type T parsed and created from the specified section of the document.
     */
    private <T extends DailyMenu.MenuItem> List<T> parseMenuItems(@NotNull Document document, @NotNull String sectionStartSelector, @Nullable String sectionEnd, @NotNull MenuItemCreator<T> itemCreator) {
        List<T> items = new ArrayList<>();

        Elements elements = document.select(sectionStartSelector).nextAll();
        boolean isCzechLine = true;
        String czechText = "";
        String allergens = "";

        for (Element element : elements) {
            String text = element.text().trim();
            if (sectionEnd != null && text.contains(sectionEnd)) break;

            if (!element.tagName().equals("p")) continue;

            if (isCzechLine) {
                String[] parts = text.split("\\(");
                czechText = capitalizeFirstLetter(parts[0].trim());
                allergens = parts.length > 1 ? parts[1].replace(")", "").trim() : "";
            } else {
                String priceText;
                if (text.contains("|")) {
                    priceText = text.replaceAll(".*\\|", "").replace("Kč", "").trim();
                } else {
                    if (text.contains("Kč")) {
                        priceText = text.substring(0, text.lastIndexOf(" ")).substring(text.lastIndexOf(" ", text.lastIndexOf(" ") - 1) + 1);
                    } else {
                        priceText = text.substring(text.lastIndexOf(" ") + 1);
                    }
                }
                double price = parsePrice(priceText);
                items.add(itemCreator.create(czechText, "", allergens, price));
            }
            isCzechLine = !isCzechLine;
        }
        return items;
    }

    /**
     * Capitalizes the first letter of the given input string while converting the rest of the string to lowercase.
     *
     * @param input the string whose first letter is to be capitalized. It must be non-null and can be an empty string.
     * @return a new string with the first letter capitalized and the rest in lowercase, or the original string if it is null or empty.
     */
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String lowercase = input.toLowerCase();
        return lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
    }

    /**
     * Parses the given price text to extract a numeric value.
     * The method removes any non-numeric characters except for
     * decimal separators (dot or comma) and then converts the
     * remaining string into a double.
     *
     * @param priceText the text from which to parse the price. It may
     *                  include currency symbols or other non-numeric
     *                  characters.
     * @return the parsed double value of the price, or 0.0 if the
     *         text cannot be parsed into a valid number.
     */
    private double parsePrice(String priceText) {
        try {
            // Remove non-numeric characters except the decimal separator (dot or comma)
            String numericPart = priceText.replaceAll("[^0-9,.]", "").replace(",", ".");
            return Double.parseDouble(numericPart);
        } catch (NumberFormatException e) {
            log.debug("Failed to parse price: {}", priceText);
            return 0.0; // Return a default value if parsing fails
        }
    }

    private <T extends DailyMenu.MenuItem> void logMenuItems(String title, List<T> menuItems) {
        if (!log.isDebugEnabled()) {
            return;
        }
        log.debug("{}:", title);
        menuItems.forEach(menuItem -> log.debug(menuItem.toString()));
    }
}
