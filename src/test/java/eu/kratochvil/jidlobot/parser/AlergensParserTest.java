package eu.kratochvil.jidlobot.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlergensParserTest {

    AlergensParser alergensParser;

    @BeforeEach
    void setUp() {
        alergensParser = new AlergensParser();
    }

    @Test
    void test9532() {
        Set<Integer> alergens = alergensParser.parse(9532L);
        Iterator<Integer> it = alergens.iterator();
        assertEquals(7, alergens.size());
        assertEquals(3, it.next().intValue());
        assertEquals(4, it.next().intValue());
        assertEquals(5, it.next().intValue());
        assertEquals(6, it.next().intValue());
        assertEquals(9, it.next().intValue());
        assertEquals(11, it.next().intValue());
    }
    @Test
    void test581() {
        Set<Integer> alergens = alergensParser.parse(581L);
        Iterator<Integer> it = alergens.iterator();
        assertEquals(4, alergens.size());
        assertEquals(1, it.next().intValue());
        assertEquals(3, it.next().intValue());
        assertEquals(7, it.next().intValue());
        assertEquals(10, it.next().intValue());
    }
    @Test
    void test9999() {
        Set<Integer> alergens = alergensParser.parse(9999L);
        Iterator<Integer> it = alergens.iterator();
        assertEquals(8, alergens.size());
        assertEquals(1, it.next().intValue());
        assertEquals(2, it.next().intValue());
        assertEquals(3, it.next().intValue());
        assertEquals(4, it.next().intValue());
        assertEquals(9, it.next().intValue());
        assertEquals(10, it.next().intValue());
        assertEquals(11, it.next().intValue());
        assertEquals(14, it.next().intValue());
    }
    @Test
    void testNull() {
        Set<Integer> alergens = alergensParser.parse(null);
        Iterator<Integer> it = alergens.iterator();
        assertEquals(0, alergens.size());
    }
    @Test
    void test0() {
        Set<Integer> alergens = alergensParser.parse(0L);
        Iterator<Integer> it = alergens.iterator();
        assertEquals(0, alergens.size());
    }

}