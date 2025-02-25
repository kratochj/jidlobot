package eu.kratochvil.jidlobot.client;

import eu.kratochvil.jidlobot.client.dto.DailyMenuResponse;
import eu.kratochvil.jidlobot.client.dto.Meal;
import eu.kratochvil.jidlobot.client.dto.MenuItemWrapper;
import eu.kratochvil.jidlobot.config.ApplicationConfig;
import eu.kratochvil.jidlobot.model.DailyMenu;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestClientTest(JidloviceMenuClient.class)
public class JidloviceMenuClientTest {

    @Autowired
    private JidloviceMenuClient jidloviceMenuClient;

    @MockitoBean
    private RestTemplate restTemplate;

    @MockitoBean
    private ApplicationConfig config;

    @Test
    public void testGetDailyMenu_withValidResponse() {
        var date = Instant.now();
        var localDate = date.atZone(ZoneId.of("Europe/Prague")).toLocalDate().toString();

        Mockito.when(config.getTimeZone()).thenReturn("Europe/Prague");
        Mockito.when(config.getUrl()).thenReturn("https://www.jidlovice.cz/api/v1/branch/3/menu/{date}?include_internal_tags=false");

        DailyMenuResponse response = new DailyMenuResponse();
        Meal meal = new Meal();
        meal.setCategory_id(1);
        meal.setName("Soup1");
        meal.setDescription("Delicious soup");
        meal.setPrice(55.0);

        MenuItemWrapper wrapper = new MenuItemWrapper();
        wrapper.setMeal(meal);

        response.setMenu_items(Collections.singletonList(wrapper));

        Mockito.when(restTemplate.getForObject(
                config.getUrl(),
                DailyMenuResponse.class,
                localDate
        )).thenReturn(response);

        DailyMenu dailyMenu = jidloviceMenuClient.getDailyMenu(date);

        assertEquals(1, dailyMenu.getSoups().size());
        assertEquals("Soup1", dailyMenu.getSoups().get(0).getName());
        assertEquals("Delicious soup", dailyMenu.getSoups().get(0).getDescription());
        assertEquals(55.0, dailyMenu.getSoups().get(0).getPrice());
    }

    @Test
    public void testGetDailyMenu_withEmptyResponse() {
        var date = Instant.now();
        var localDate = date.atZone(ZoneId.of("Europe/Prague")).toLocalDate().toString();

        Mockito.when(config.getTimeZone()).thenReturn("Europe/Prague");
        Mockito.when(config.getUrl()).thenReturn("https://www.jidlovice.cz/api/v1/branch/3/menu/{date}?include_internal_tags=false");

        DailyMenuResponse response = new DailyMenuResponse();
        response.setMenu_items(Collections.emptyList());

        Mockito.when(restTemplate.getForObject(
                config.getUrl(),
                DailyMenuResponse.class,
                localDate
        )).thenReturn(response);

        DailyMenu dailyMenu = jidloviceMenuClient.getDailyMenu(date);

        assertEquals(0, dailyMenu.getSoups().size());
        assertEquals(0, dailyMenu.getDishesOfTheDay().size());
        assertEquals(0, dailyMenu.getSpecialDishes().size());
    }

    @Test
    public void testGetDailyMenu_withNullResponse() {
        var date = Instant.now();
        var localDate = date.atZone(ZoneId.of("Europe/Prague")).toLocalDate().toString();

        Mockito.when(config.getTimeZone()).thenReturn("Europe/Prague");
        Mockito.when(config.getUrl()).thenReturn("https://www.jidlovice.cz/api/v1/branch/3/menu/{date}?include_internal_tags=false");

        Mockito.when(restTemplate.getForObject(
                config.getUrl(),
                DailyMenuResponse.class,
                localDate
        )).thenReturn(null);

        DailyMenu dailyMenu = jidloviceMenuClient.getDailyMenu(date);

        assertEquals(0, dailyMenu.getSoups().size());
        assertEquals(0, dailyMenu.getDishesOfTheDay().size());
        assertEquals(0, dailyMenu.getSpecialDishes().size());
    }

    @Test
    public void testGetDailyMenu_withMultipleMeals() {
        var date = Instant.now();
        var localDate = date.atZone(ZoneId.of("Europe/Prague")).toLocalDate().toString();

        Mockito.when(config.getTimeZone()).thenReturn("Europe/Prague");
        Mockito.when(config.getUrl()).thenReturn("https://www.jidlovice.cz/api/v1/branch/3/menu/{date}?include_internal_tags=false");

        DailyMenuResponse response = new DailyMenuResponse();

        Meal soupMeal = new Meal();
        soupMeal.setCategory_id(1);
        soupMeal.setName("Soup1");
        soupMeal.setDescription("Soup Description");
        soupMeal.setPrice(50.0);

        Meal dishMeal = new Meal();
        dishMeal.setCategory_id(2);
        dishMeal.setName("Dish1");
        dishMeal.setDescription("Dish Description");
        dishMeal.setPrice(120.0);

        Meal specialMeal = new Meal();
        specialMeal.setCategory_id(3);
        specialMeal.setName("Special1");
        specialMeal.setDescription("Special Description");
        specialMeal.setPrice(150.0);

        MenuItemWrapper soupWrapper = new MenuItemWrapper();
        soupWrapper.setMeal(soupMeal);

        MenuItemWrapper dishWrapper = new MenuItemWrapper();
        dishWrapper.setMeal(dishMeal);

        MenuItemWrapper specialWrapper = new MenuItemWrapper();
        specialWrapper.setMeal(specialMeal);

        response.setMenu_items(Arrays.asList(soupWrapper, dishWrapper, specialWrapper));

        Mockito.when(restTemplate.getForObject(
                config.getUrl(),
                DailyMenuResponse.class,
                localDate
        )).thenReturn(response);

        DailyMenu dailyMenu = jidloviceMenuClient.getDailyMenu(date);

        assertEquals(1, dailyMenu.getSoups().size());
        assertEquals("Soup1", dailyMenu.getSoups().get(0).getName());
        assertEquals(1, dailyMenu.getDishesOfTheDay().size());
        assertEquals("Dish1", dailyMenu.getDishesOfTheDay().get(0).getName());
        assertEquals(1, dailyMenu.getSpecialDishes().size());
        assertEquals("Special1", dailyMenu.getSpecialDishes().get(0).getName());
    }
}