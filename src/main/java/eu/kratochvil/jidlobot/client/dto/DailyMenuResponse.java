package eu.kratochvil.jidlobot.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO mapping the JSON structure of the daily menu endpoint:
 * <br/>
 * {
 *   "available": false,
 *   "branch_id": 3,
 *   "menu_date": "2025-02-25",
 *   "date_modified": "...",
 *   "menu_items": [ { ... }, { ... } ]
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyMenuResponse {

    private boolean available;
    private int branch_id;
    private String menu_date;
    private String date_modified;
    private List<MenuItemWrapper> menu_items;

    // Getters and setters

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getMenu_date() {
        return menu_date;
    }

    public void setMenu_date(String menu_date) {
        this.menu_date = menu_date;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public List<MenuItemWrapper> getMenu_items() {
        return menu_items;
    }

    public void setMenu_items(List<MenuItemWrapper> menu_items) {
        this.menu_items = menu_items;
    }
}
