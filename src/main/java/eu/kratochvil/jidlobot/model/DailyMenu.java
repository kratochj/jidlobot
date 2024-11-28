package eu.kratochvil.jidlobot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class DailyMenu {
    private List<Soup> soups;
    private List<Dish> dishesOfTheDay;

    public List<Soup> getSoups() {
        return soups;
    }

    public void setSoups(List<Soup> soups) {
        this.soups = soups;
    }

    public List<Dish> getDishesOfTheDay() {
        return dishesOfTheDay;
    }

    public void setDishesOfTheDay(List<Dish> dishesOfTheDay) {
        this.dishesOfTheDay = dishesOfTheDay;
    }

    @Override
    public String toString() {
        return "DailyMenu{" +
                "soups=" + soups +
                ", dishesOfTheDay=" + dishesOfTheDay +
                '}';
    }

    public static class Soup {
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

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getAllergens() {
            return allergens;
        }

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

    public static class Dish {
        private final String nameCz;
        private final String nameEn;
        private final String allergens;
        private final double price;

        public Dish(String nameCz, String nameEn, String allergens, double price) {
            this.nameCz = nameCz;
            this.nameEn = nameEn;
            this.allergens = allergens;
            this.price = price;
        }

        public String getNameCz() {
            return nameCz;
        }

        public String getNameEn() {
            return nameEn;
        }

        public String getAllergens() {
            return allergens;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("nameCz", nameCz)
                    .append("nameEn", nameEn)
                    .append("allergens", allergens)
                    .append("price", price)
                    .toString();
        }
    }
}
