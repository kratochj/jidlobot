package eu.kratochvil.jidlobot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class DailyMenu {
    private List<Soup> soups;
    private List<Dish> dishesOfTheDay;
    private List<Dish> specialDishes;

    public List<Soup> getSoups() {
        return soups;
    }

    public void setSoups(List<Soup> soups) {
        this.soups = soups;
    }

    public List<Dish> getSpecialDishes() {
        return specialDishes;
    }

    public void setSpecialDishes(List<Dish> specialDish) {
        this.specialDishes = specialDish;
    }

    public List<Dish> getDishesOfTheDay() {
        return dishesOfTheDay;
    }

    public void setDishesOfTheDay(List<Dish> dishesOfTheDay) {
        this.dishesOfTheDay = dishesOfTheDay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("soups", soups)
                .append("dishesOfTheDay", dishesOfTheDay)
                .append("specialDishes", specialDishes)
                .toString();
    }

    public static class Soup implements MenuItem {
        private final String name;
        private final String description;
        private final String allergens;
        private final double price;

        public Soup(String name, String description, String allergens, double price) {
            this.name = name;
            this.description = description;
            this.allergens = allergens;
            this.price = price;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getAllergens() {
            return allergens;
        }

        @Override
        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("name", name)
                    .append("description", description)
                    .append("allergens", allergens)
                    .append("price", price)
                    .toString();
        }
    }

    public static class Dish implements MenuItem {
        private final String name;
        private final String description;
        private final String allergens;
        private final double price;

        public Dish(String name, String description, String allergens, double price) {
            this.name = name;
            this.description = description;
            this.allergens = allergens;
            this.price = price;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getAllergens() {
            return allergens;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("nameCz", name)
                    .append("description", description)
                    .append("allergens", allergens)
                    .append("price", price)
                    .toString();
        }
    }

    public interface MenuItem {
        String getName();

        String getAllergens();

        String getDescription();

        double getPrice();
    }
}
