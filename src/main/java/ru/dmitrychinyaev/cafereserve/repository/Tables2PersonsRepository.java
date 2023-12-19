package ru.dmitrychinyaev.cafereserve.repository;

import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;
import ru.dmitrychinyaev.cafereserve.utils.CSVUtils;
import ru.dmitrychinyaev.cafereserve.utils.MailSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class Tables2PersonsRepository {
    private final List<ArrayList<LinkedList<ReservationRequest>>> arrayTime = new ArrayList<ArrayList<LinkedList<ReservationRequest>>>(8);
    private final CSVUtils csvUtils;
    private final MailSender mailSender;
    private DateTime currentDate;
    @Scheduled(cron = "@hourly")
    private void checkDate(){
        if(currentDate==null){
            currentDate = new DateTime();
            return;
        }
        DateTime dateTime = new DateTime();
        if(!dateTime.equals(currentDate)){
           currentDate = dateTime;
           arrayTime.remove(0);
           arrayTime.add(null);
        }
    }
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
        for (LinkedList<ReservationRequest> requestLinkedList : availableSlots) {
            if (requestLinkedList == null || requestLinkedList.size() < 3) {
                availableTime.add(time + ":00");
            }
            time++;
        }
        return availableTime;
    }

    public int dateConvertToElement(String date){
        int dateInt = Integer.parseInt(date);
        int dateToCompare = Integer.parseInt(new DateTime().toString("dd"));
        return dateInt - dateToCompare;
    }

    public int timeConvertToElement(String time){
        int index = 0;
        int openingTime = 12;
        for (int i = 0; i <11; i++) {
            if(time.equals(openingTime + ":00")){
               index = i;
               break;
            }
            openingTime++;
        }
        return index;
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
        try {
            arrayTime.get(date).get(time).add(requestToPut);
        } catch (NullPointerException e) {
            arrayTime.get(date).add(time, new LinkedList<>());
            arrayTime.get(date).get(time).add(requestToPut);
        }
        csvUtils.write(requestToPut);
        mailSender.send(requestToPut.successBooking());
    }
}
