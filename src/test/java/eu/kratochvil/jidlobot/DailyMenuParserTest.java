package eu.kratochvil.jidlobot;

import eu.kratochvil.jidlobot.model.DailyMenu;
import eu.kratochvil.jidlobot.parser.DailyMenuParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DailyMenuParserTest {

    @Test
    void testParse() throws Exception {
        JsoupConnector mockConnector = mock(JsoupConnector.class);
        DailyMenuParser parser = new DailyMenuParser(mockConnector);

        // Load the test HTML from resources
        URL resource = getClass().getClassLoader().getResource("menus/testMenu001.html");
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
        assertEquals(6, menu.getDishesOfTheDay().size());

        for (int i = 0; i < menu.getSoups().size(); i++) {
            assertNotNull(menu.getSoups().get(i).getName());
            assertTrue(menu.getSoups().get(i).getPrice()>0);
            assertNotNull(menu.getSoups().get(i).getAllergens());
        }
    }

}
