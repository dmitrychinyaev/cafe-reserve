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

    public ReservationRequest(String date) {
        this.date = date;
    }
}
