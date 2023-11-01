package ru.dmitrychinyaev.cafereserve.repository;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Repository
public class Tables2PersonsRepository {
    private final List<ArrayList<LinkedList<ReservationRequest>>> arrayTime = new ArrayList<ArrayList<LinkedList<ReservationRequest>>>(8);

    public ArrayList<String> availableTime (ReservationRequest request){
        int requestedDate = dateConvertToElement(request.getDate());

        if(arrayTime.size()==0){
            initiateFirstArray();
        }

        if (arrayTime.get(requestedDate)==null){
            arrayTime.set(requestedDate, new ArrayList<>(8));
            initiateInArray(arrayTime.get(requestedDate));
        }

        ArrayList<LinkedList<ReservationRequest>> availableSlots = arrayTime.get(requestedDate);
        ArrayList<String> availableTime = new ArrayList<>(11);
        int time = 12;
        for (int i = 0; i < availableSlots.size(); i++) {
            LinkedList<ReservationRequest> requestLinkedList = availableSlots.get(i);
            if (requestLinkedList == null || requestLinkedList.size() < 3) {
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

    public int timeConvertToElement(String time){
        List<String> times = new ArrayList<>();
        int openingTime = 12;
        for (int i = 0; i <11; i++) {
            times.add(i,openingTime + ":00");
        }
        return times.indexOf(time);
    }
    
    public void initiateFirstArray(){
        for (int i = 0; i < 8; i++) {
            arrayTime.add(null);
        }
    }

    public void initiateInArray(ArrayList<LinkedList<ReservationRequest>> requestLinkedList){
        for (int i = 0; i < 11; i++) {
            requestLinkedList.add(null);
        }
    }

    public void putBooking(ReservationRequest requestToPut) {
        int date = dateConvertToElement(requestToPut.getDate());
        int time = timeConvertToElement(requestToPut.getTime());
        arrayTime.get(date).get(time).add(requestToPut);
    }
}
