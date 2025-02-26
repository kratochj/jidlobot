package eu.kratochvil.jidlobot.slack;

import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import eu.kratochvil.jidlobot.config.ApplicationConfig;
import eu.kratochvil.jidlobot.model.DailyMenu;
import eu.kratochvil.jidlobot.parser.AlergensParser;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DailyMenuMessageBuilder {

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("cs", "CZ"));

    private final Clock clock;
    private final ApplicationConfig config;
    private final AlergensParser alergensParser;

    public DailyMenuMessageBuilder(Clock clock, ApplicationConfig config, AlergensParser alergensParser) {
        this.clock = clock;
        this.config = config;
        this.alergensParser = alergensParser;
    }

    public ChatPostMessageRequest getChatPostMessage(DailyMenu dailyMenu) {
        return ChatPostMessageRequest.builder()
                .blocks(buildFormattedMessage(dailyMenu))
                .text(buildPlainMessage(dailyMenu))
                .build();
    }

    public String buildPlainMessage(DailyMenu dailyMenu) {
        StringBuilder menuText = new StringBuilder("*Denní Menu v Jídlovicích*\n");

        addTextMenuItems(menuText, "Polévky", dailyMenu.getSoups());
        addTextMenuItems(menuText, "Hlavní jídla", dailyMenu.getDishesOfTheDay());
        addTextMenuItems(menuText, "Jídlovický speciál", dailyMenu.getSpecialDishes());

        return menuText.toString();
    }

    private <T extends DailyMenu.MenuItem> void addTextMenuItems(StringBuilder menuText, String heading, List<T> menuItems) {
        if (menuItems.isEmpty()) {
            return;
        }
        menuText.append("- *").append(heading).append("*:\n");
        for (DailyMenu.MenuItem dish : menuItems) {
            menuText.append(String.format("- %s  %s %s - %s\n",
                    StringUtils.defaultIfBlank(dish.getName(), ""),
                    StringUtils.defaultIfBlank(dish.getDescription(), ""),
                    alergensParser.parse(dish.getAllergens()),
                    formatPrice(dish.getPrice())));
        }
    }

    public List<LayoutBlock> buildFormattedMessage(DailyMenu dailyMenu) {
        List<LayoutBlock> blocks = new ArrayList<>();

        // Header
        blocks.add(SlackTextUtils.buildHeaderLayoutBlock(String.format("Menu v Jídlovicích dne %s", formatLocalDateToString(clock.instant()))));

        // Soups Section
        if (!dailyMenu.getSoups().isEmpty()) {
            blocks.addAll(addMenuItemsSection("Polévky:", dailyMenu.getSoups()));
        }

        // Main Dishes Section
        if (!dailyMenu.getDishesOfTheDay().isEmpty()) {
            blocks.addAll(addMenuItemsSection("Hlavní jídla:", dailyMenu.getDishesOfTheDay()));
        }

        // Specials Section
        if (!dailyMenu.getSpecialDishes().isEmpty()) {
            blocks.addAll(addMenuItemsSection("Jídlovický speciál:", dailyMenu.getSpecialDishes()));
        }

        return blocks;
    }

    @NotNull
    private String formatLocalDateToString(Instant instant) {
        LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.of(config.getTimeZone()));
        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private <T extends DailyMenu.MenuItem> List<LayoutBlock> addMenuItemsSection(String heading, List<T> menuItems) {
        List<LayoutBlock> blocks = new ArrayList<>();
        blocks.add(Blocks.divider());
        blocks.add(SlackTextUtils.buildTextLayoutBlock(String.format("*%s*", heading)));

        for (DailyMenu.MenuItem menuItem : menuItems) {
            String menuItemText;
            menuItemText = String.format("• %s %s %s - %s",
                    StringUtils.defaultIfBlank(menuItem.getName(), ""),
                    StringUtils.defaultIfBlank(menuItem.getDescription(), ""),
                    alergensParser.parse(menuItem.getAllergens()),
                    formatPrice(menuItem.getPrice()));
            blocks.add(SlackTextUtils.buildTextLayoutBlock(menuItemText));
        }
        return blocks;
    }

    private String formatPrice(double price) {
        return currencyFormatter.format(price);
    }
}
