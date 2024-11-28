package eu.kratochvil.jidlobot;

import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import eu.kratochvil.jidlobot.model.DailyMenu;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DailyMenuMessageBuilder {

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("cs", "CZ"));

    public String buildPlainMessage(DailyMenu dailyMenu) {
        StringBuilder menuText = new StringBuilder("""
                *Denní Menu v Jídlovicích*
                
                - *Polévky*:
                """);

        for (DailyMenu.Soup soup : dailyMenu.getSoups()) {
            menuText.append(String.format("- %s (%s) - %s\n", soup.getName(), soup.getAllergens(), formatPrice(soup.getPrice())));
        }

        menuText.append("- *Hlavní jídla*:\n");
        for (DailyMenu.Dish dish : dailyMenu.getDishesOfTheDay()) {
            menuText.append(String.format("- %s (%s) - %s\n", dish.getNameCz(), dish.getAllergens(), formatPrice(dish.getPrice())));
        }

        return menuText.toString();
    }

    public List<LayoutBlock> buildFormattedMessage(DailyMenu dailyMenu) {
        List<LayoutBlock> blocks = new ArrayList<>();

        // Header
        blocks.add(Blocks.header(h -> h.text(plainText("Denní Menu v Jídlovicích"))));

        // Divider
        blocks.add(Blocks.divider());

        // Soups Section
        if (!dailyMenu.getSoups().isEmpty()) {
            blocks.add(Blocks.section(s -> s.text(markdownText("*Polévky:*"))));

            for (DailyMenu.Soup soup : dailyMenu.getSoups()) {
                String soupText = String.format("• %s (%s) - %s", soup.getName(), soup.getAllergens(), formatPrice(soup.getPrice()));
                blocks.add(Blocks.section(s -> s.text(markdownText(soupText))));
            }

            // Divider
            blocks.add(Blocks.divider());
        }

        // Main Dishes Section
        if (!dailyMenu.getDishesOfTheDay().isEmpty()) {
            blocks.add(Blocks.section(s -> s.text(markdownText("*Hlavní jídla:*"))));

            for (DailyMenu.Dish dish : dailyMenu.getDishesOfTheDay()) {
                String dishText = String.format("• %s (%s) - %s", dish.getNameCz(), dish.getAllergens(), formatPrice(dish.getPrice()));
                blocks.add(Blocks.section(s -> s.text(markdownText(dishText))));
            }
        }

        return blocks;
    }

    private MarkdownTextObject markdownText(String text) {
        return MarkdownTextObject.builder().text(text).build();
    }

    @SuppressWarnings("SameParameterValue")
    private PlainTextObject plainText(String text) {
        return PlainTextObject.builder().text(text).build();
    }

    private String formatPrice(double price) {
        return currencyFormatter.format(price);
    }
}
