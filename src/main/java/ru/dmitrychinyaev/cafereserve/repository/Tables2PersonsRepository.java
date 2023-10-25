package ru.dmitrychinyaev.cafereserve.repository;

import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tables2PersonsRepository {
    private List<ArrayList<HashMap<Byte, ReservationRequest>>> arrayTime = new ArrayList<ArrayList<HashMap<Byte,ReservationRequest>>>(10);

    public ReservationRequest q1 (){
        return arrayTime.get(0).get(0).get(1);
    }
}
