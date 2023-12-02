package ru.dmitrychinyaev.cafereserve.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BadDaysRepository {
    private final List<String> closedCafeDays = new LinkedList<>();
    public boolean ifExist(String date){
        return closedCafeDays.contains(date);
    }

    public void putDayClosing(String date){
        closedCafeDays.add(date);
    }

    public boolean deleteDate(String date){
        if(!ifExist(date)){
            return false;
        }
        return closedCafeDays.remove(date);
    }
}
