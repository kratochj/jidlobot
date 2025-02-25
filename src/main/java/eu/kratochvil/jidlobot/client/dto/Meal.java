package eu.kratochvil.jidlobot.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents the "meal" object inside each MenuItemWrapper.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meal {

    private String name;
    private String name_en;
    private String description;
    private String description_en;
    private double price;
    private Integer quantity;
    private int allergens;
    private boolean draft;
    private boolean permanent;
    private int id;
    private String date_modified;
    private int category_id;
    // tags not needed for now

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getAllergens() {
        return allergens;
    }

    public void setAllergens(int allergens) {
        this.allergens = allergens;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("price", price)
                .toString();
    }
}
