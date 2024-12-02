package eu.kratochvil.jidlobot;

import eu.kratochvil.jidlobot.model.DailyMenu;
import eu.kratochvil.jidlobot.parser.DailyMenuParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DailyMenuParserTest {
    private static final Logger log = LoggerFactory.getLogger(DailyMenuParserTest.class);

    JsoupConnector mockConnector = null;
    DailyMenuParser parser = null;

    @BeforeEach
    void setUp() {
        mockConnector = mock(JsoupConnector.class);
        parser = new DailyMenuParser(mockConnector);
    }

    @Test
    void testParse001() throws Exception {
        checkMenu(mockConnector, parser, "menus/testMenu001.html");
    }

    @Test
    void testParse002() throws Exception {
        checkMenu(mockConnector, parser, "menus/testMenu002.html");
    }

    private void checkMenu(JsoupConnector mockConnector, DailyMenuParser parser, String testFilename) throws IOException {
        log.debug("Starting testParse...");
        URL resource = getClass().getClassLoader().getResource(testFilename);
        assertNotNull(resource, "testMenu.html resource not found");
        String menuUrl = resource.toString();

        // Create a Document object from the test HTML
        File input = new File(resource.getFile());
        Document testDocument = Jsoup.parse(input, "UTF-8");

        when(mockConnector.getDocument(anyString())).thenReturn(testDocument);

        DailyMenu menu = parser.parse(menuUrl);

        assertNotNull(menu);
        assertNotNull(menu.getSoups());
        assertEquals(2, menu.getSoups().size());
        assertNotNull(menu.getDishesOfTheDay());
        assertEquals(5, menu.getDishesOfTheDay().size());
        assertNotNull(menu.getSpecialDishes());
        assertEquals(1, menu.getSpecialDishes().size());

        for (int i = 0; i < menu.getSoups().size(); i++) {
            assertNotNull(menu.getSoups().get(i).getName());
            assertTrue(menu.getSoups().get(i).getPrice() > 0);
            assertNotNull(menu.getSoups().get(i).getAllergens());
        }
        for (DailyMenu.Dish dish : menu.getDishesOfTheDay()) {
            assertNotNull(dish.getName());
            assertTrue(dish.getPrice() > 0);
            assertNotNull(dish.getAllergens());
        }
        for (DailyMenu.Dish dish : menu.getSpecialDishes()) {
            assertNotNull(dish.getName());
            assertTrue(dish.getPrice() > 0);
            assertNotNull(dish.getAllergens());
        }
    }

}
