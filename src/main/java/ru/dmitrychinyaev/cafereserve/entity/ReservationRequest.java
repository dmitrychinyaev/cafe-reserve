package ru.dmitrychinyaev.cafereserve.entity;

import lombok.Data;

@Data
public class ReservationRequest {
    private String date;
    private String persons;
    private String time;
}
