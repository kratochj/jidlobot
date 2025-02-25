package eu.kratochvil.jidlobot.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A single item from "menu_items" in DailyMenuResponse.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuItemWrapper {

    private String date_modified;
    private int branch_id;
    private int meal_id;
    private String menu_date;
    private boolean is_from_main_menu;
    private Object meal_override; // We don't need the override in this example
    private Meal meal;

    // Getters and setters

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public String getMenu_date() {
        return menu_date;
    }

    public void setMenu_date(String menu_date) {
        this.menu_date = menu_date;
    }

    public boolean isIs_from_main_menu() {
        return is_from_main_menu;
    }

    public void setIs_from_main_menu(boolean is_from_main_menu) {
        this.is_from_main_menu = is_from_main_menu;
    }

    public Object getMeal_override() {
        return meal_override;
    }

    public void setMeal_override(Object meal_override) {
        this.meal_override = meal_override;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}
