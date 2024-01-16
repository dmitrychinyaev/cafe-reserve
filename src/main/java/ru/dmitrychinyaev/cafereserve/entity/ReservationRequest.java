package ru.dmitrychinyaev.cafereserve.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationRequest {

    private String name;
    private String date;
    private String persons;
    private String time;
    private String phoneNumber;
    private Long chatID;

    public ReservationRequest(String date) {
        this.date = date;
    }
    public ReservationRequest(String date, Long chatID) {
        this.date = date;
        this.chatID = chatID;
    }
    //TODO сделать проверку даты. Если число 01, а выбор даты произошел в конце месяца, то месяц будет тот же а не след

    public String successBooking(){
        return "Бронь на " + this.getName() + " на " + this.getDate()
                + " в " + this.getTime() + " создана";
    }

    public String getDataForAdmin(){
        return this.name + " " + this.phoneNumber + " " + this.persons + "чел" + " " + this.date
        + " " + this.time;
    }
}
