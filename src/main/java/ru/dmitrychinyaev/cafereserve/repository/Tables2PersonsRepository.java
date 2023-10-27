package ru.dmitrychinyaev.cafereserve.repository;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Repository
public class Tables2PersonsRepository {
    private List<ArrayList<HashMap<Byte, ReservationRequest>>> arrayTime = new ArrayList<ArrayList<HashMap<Byte, ReservationRequest>>>(8);

    public ArrayList<String> availableTime (ReservationRequest request){
        int requestedDate = dateConvertToElement(request.getDate());

        if(arrayTime.size()==0){
            initiateFirstArray();
        }

        if (arrayTime.get(requestedDate)==null){
            arrayTime.set(requestedDate, new ArrayList<>(8));
            initiateInArray(arrayTime.get(requestedDate));
        }

        ArrayList<HashMap<Byte, ReservationRequest>> availableSlots = arrayTime.get(requestedDate);
        ArrayList<String> availableTime = new ArrayList<>(11);
        int time = 12;
        for (int i = 0; i < availableSlots.size(); i++) {
            HashMap<Byte,ReservationRequest> requestHashMap = availableSlots.get(i);
            if (requestHashMap==null || requestHashMap.size() < 3) {
                availableTime.add(time +":00");
            }
            time++;
        }
        return availableTime;
    }

    public int dateConvertToElement(String date){
        List<String> dates = new ArrayList<>();
        DateTime dateTime = new DateTime();
        dates.add(0,dateTime.toString("dd"));
        for (int i = 1; i < 8; i++) {
            dates.add(i,dateTime
                    .plusDays(i)
                    .toString("dd"));
        }
        return dates.indexOf(date);
    }
    
    public void initiateFirstArray(){
        for (int i = 0; i < 8; i++) {
            arrayTime.add(null);
        }
    }

    public void initiateInArray(ArrayList<HashMap<Byte, ReservationRequest>> hashMapArrayList){
        for (int i = 0; i < 11; i++) {
            hashMapArrayList.add(null);
        }
    }
}
