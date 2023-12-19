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
    //TODO сделать проверку даты. Если число 01, а выбор даты произошел в конце месяца, то месяц будет тот же а не след

    public String successBooking(){
        DateTime dateTime = new DateTime();
        return "Бронь на " + this.getName() + " на " + this.getDate() + dateTime.toString(".MM")
                + " в " + this.getTime() + " создана";
    }

    public String[] getArrayOfData(){
        return new String[]{this.name, this.phoneNumber, this.date, this.persons + "чел", this.time};
    }
}
