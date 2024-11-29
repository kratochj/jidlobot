package eu.kratochvil.jidlobot;

import eu.kratochvil.jidlobot.model.DailyMenu;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DailyMenuParser {

    public static final Logger log = LoggerFactory.getLogger(DailyMenuParser.class);

    private final String menuUrl;

    public DailyMenuParser(@Value("${menu.url}") String menuUrl) {
        this.menuUrl = menuUrl;
    }

    @PostConstruct
    public void getResult() {
        parse();
    }

    public DailyMenu parse() {
        try {
            // Download and parse the webpage
            Document document = Jsoup.connect(menuUrl).get();

            DailyMenu menu = new DailyMenu();
            menu.setSoups(parseSoups(document));
            menu.setDishesOfTheDay(parseDishes(document));

            // Print the menu to verify
            log.debug("Soups:");
            menu.getSoups().forEach(soup -> log.debug(soup.toString()));

            log.debug("\nDishes of the Day:");
            menu.getDishesOfTheDay().forEach(dish -> log.debug(dish.toString()));

            return menu;
        } catch (IOException e) {
            log.error("Error fetching the daily menu: {}", e.getMessage());
            return null;
        }
    }

    private List<DailyMenu.Soup> parseSoups(Document document) {
        List<DailyMenu.Soup> soups = new ArrayList<>();
        Elements soupElements = document.select("p:contains(POLÉVKY)").nextAll();

        boolean isCzechLine = true; // Toggle to track Czech and English lines
        String czechText = "";     // Temporary storage for Czech text
        String allergens = "";     // Temporary storage for allergens

        for (Element element : soupElements) {
            String text = element.text().trim();

            // Stop parsing when "JÍDLA DNE" is encountered
            if (text.contains("JÍDLA DNE")) {
                break;
            }

            log.debug("Parsing line: {}", text);

            if (isCzechLine) {
                // Process Czech line: extract name and allergens
                String[] parts = text.split("\\("); // Split by parentheses for allergens
                czechText = capitalizeFirstLetter(parts[0].trim());
                allergens = (parts.length > 1) ? parts[1].replace(")", "").trim() : "";
            } else {
                // Process English line: extract price
                String priceText = text.replaceAll(".*\\|", "").replace("Kč", "").trim();
                double price = parsePrice(priceText);

                // Add the soup to the list
                if (!czechText.isEmpty() && price > 0) {
                    soups.add(new DailyMenu.Soup(czechText, "", allergens, price));
                }
            }

            // Toggle the flag to alternate between Czech and English lines
            isCzechLine = !isCzechLine;
        }
        return soups;
    }


    private List<DailyMenu.Dish> parseDishes(Document document) {
        List<DailyMenu.Dish> dishes = new ArrayList<>();
        Elements dishElements = document.select("h4:contains(JÍDLA DNE)").nextAll("p");

        boolean isCzechLine = true; // Toggle to track Czech and English lines
        String czechName = "";     // Temporary storage for Czech name
        String allergens = "";     // Temporary storage for allergens

        for (Element element : dishElements) {
            if (!element.tagName().equals("p") || element.text().trim().isEmpty()) {
                continue;
            }
            String text = element.text().trim();
            // Stop parsing when encountering the next section
            if (text.contains("JÍDLOVICKÝ SPECIÁL")) {
                break;
            }

            if (isCzechLine) {
                // Process Czech line: extract name and allergens
                String[] parts = text.split("\\("); // Split by parentheses for allergens
                czechName = capitalizeFirstLetter(parts[0].trim());
                allergens = (parts.length > 1) ? parts[1].replace(")", "").trim() : "";
            } else {
                // Process English line: extract English description and price
                int lastPipeIndex = text.lastIndexOf("|");
                String englishDescription = (lastPipeIndex > -1) ? text.substring(0, lastPipeIndex).trim() : text.trim();
                String[] splittedLine = text.split(" ");
                String priceText = splittedLine[splittedLine.length - 2];
                //String priceText = text.substring(text.lastIndexOf(' ')).trim();
                double price = parsePrice(priceText);

                // Add the dish to the list
                if (!czechName.isEmpty() && price > 0) {
                    dishes.add(new DailyMenu.Dish(czechName, englishDescription, allergens, price));
                }
            }

            // Toggle the flag to alternate between Czech and English lines
            isCzechLine = !isCzechLine;
        }
        return dishes;
    }

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String lowercase = input.toLowerCase();
        return lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
    }

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
}
