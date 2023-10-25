package ru.dmitrychinyaev.cafereserve.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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
}
