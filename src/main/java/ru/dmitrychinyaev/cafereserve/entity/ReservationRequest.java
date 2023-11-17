package ru.dmitrychinyaev.cafereserve.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
public class ReservationRequest {

    private String name;
    private String date;
    private String persons;
    private String time;
    private String phoneNumber;

    public ReservationRequest(String date) {
        this.date = date;
    }

    public String successBooking(){
        DateTime dateTime = new DateTime();
        return "Бронь на " + this.getName() + " на " + this.getDate() + dateTime.toString(".MM")
                + " в " + this.getTime() + " создана";
    }
}
