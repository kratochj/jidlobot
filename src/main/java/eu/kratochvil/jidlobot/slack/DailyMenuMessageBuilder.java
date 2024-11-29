package eu.kratochvil.jidlobot.slack;

import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import eu.kratochvil.jidlobot.model.DailyMenu;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DailyMenuMessageBuilder {

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("cs", "CZ"));

    public ChatPostMessageRequest getChatPostMessage(DailyMenu dailyMenu) {
        return ChatPostMessageRequest.builder()
                .blocks(buildFormattedMessage(dailyMenu))
                .text(buildPlainMessage(dailyMenu))
                .build();
    }

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
        blocks.add(SlackTextUtils.buildHeaderLayoutBlock("Denní Menu v Jídlovicích"));

        // Divider
        blocks.add(Blocks.divider());

        // Soups Section
        if (!dailyMenu.getSoups().isEmpty()) {
            blocks.add(SlackTextUtils.buildTextLayoutBlock("*Polévky:*"));

            for (DailyMenu.Soup soup : dailyMenu.getSoups()) {
                String soupText = String.format("• %s (%s) - %s", soup.getName(), soup.getAllergens(), formatPrice(soup.getPrice()));
                blocks.add(SlackTextUtils.buildTextLayoutBlock(soupText));
            }

            // Divider
            blocks.add(Blocks.divider());
        }

        // Main Dishes Section
        if (!dailyMenu.getDishesOfTheDay().isEmpty()) {
            blocks.add(SlackTextUtils.buildTextLayoutBlock("*Hlavní jídla:*"));

            for (DailyMenu.Dish dish : dailyMenu.getDishesOfTheDay()) {
                String dishText = String.format("• %s (%s) - %s", dish.getNameCz(), dish.getAllergens(), formatPrice(dish.getPrice()));
                blocks.add(SlackTextUtils.buildTextLayoutBlock(dishText));
            }
        }

        return blocks;
    }

    private String formatPrice(double price) {
        return currencyFormatter.format(price);
    }
}
