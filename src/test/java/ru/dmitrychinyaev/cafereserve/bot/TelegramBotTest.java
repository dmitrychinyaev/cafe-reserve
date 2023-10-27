package ru.dmitrychinyaev.cafereserve.bot;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TelegramBotTest {
    @Test
    void changeKey () {
        Map<String, Integer> playerMap = new HashMap<>();
        playerMap.put("Kai", 42);
        playerMap.put("Amanda", 88);
        playerMap.put("Tom", 200);
        playerMap.put("Eric", playerMap.remove("Kai"));

        assertFalse(playerMap.containsKey("Kai"));
        assertTrue(playerMap.containsKey("Eric"));
        assertEquals(42, playerMap.get("Eric"));
    }

    @Test
    void changeValue () {
        Map<String, Double> fruitMap = new HashMap<>();
        fruitMap.put("apple", 2.45);

        double newPrice = 3.22;
        Double applePrice = fruitMap.get("apple");

        Double oldValue = fruitMap.replace("apple", newPrice);

        Assertions.assertNotNull(oldValue);
        Assertions.assertEquals(oldValue, applePrice);
        Assertions.assertEquals(Double.valueOf(newPrice), fruitMap.get("apple"));
    }

    @Test
    void dateMinusDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        DateTime now = DateTime.now();
        String m = "30.10.2023";
        DateTime sixDaysBehind = DateTime.parse(m, formatter);

        long diff = Math.abs(Days.daysBetween(now, sixDaysBehind).getDays());
        assertEquals(2, diff);
    }
    @Test
    void dateConvertToElement() {
        List<String> dates = new ArrayList<>();
        DateTime dateTime = new DateTime();
        dates.add(0, dateTime.toString("dd"));
        for (int i = 1; i < 8; i++) {
            dates.add(i, dateTime
                    .plusDays(i)
                    .toString("dd"));
        }
        assertEquals(dates.size(), 8);
        assertEquals("27", dates.get(0));
        assertEquals(0, dates.indexOf("27"));
    }
}
