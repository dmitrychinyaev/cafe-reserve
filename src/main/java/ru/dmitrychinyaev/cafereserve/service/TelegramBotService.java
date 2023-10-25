package ru.dmitrychinyaev.cafereserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;
import ru.dmitrychinyaev.cafereserve.repository.RequestRepository;

@Service
@RequiredArgsConstructor
public class TelegramBotService {
    private final RequestRepository requestRepository;
    public void createRequest(String requestName, String date){
        requestRepository.addRequest(requestName, new ReservationRequest(date));
    }
    public void setPersonsToRequest(String requestID, String persons){
        requestRepository.setPersons(requestID, persons);
    }
}
