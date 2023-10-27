package ru.dmitrychinyaev.cafereserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;
import ru.dmitrychinyaev.cafereserve.repository.RequestRepository;
import ru.dmitrychinyaev.cafereserve.repository.Tables2PersonsRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramBotService {
    private final RequestRepository requestRepository;
    private final Tables2PersonsRepository tables2PersonsRepository;
    public void createRequest(String requestName, String date){
        requestRepository.addRequest(requestName, new ReservationRequest(date));
    }
    public void setPersonsToRequest(String requestID, String persons){
        requestRepository.setPersons(requestID, persons);
    }

    public ArrayList<String> findAvailableTime(String requestID) {
        ArrayList<String> availableTime = new ArrayList<>();
        Optional<ReservationRequest> requestToFind = Optional.ofNullable(requestRepository.getRequest(requestID));
        if(requestToFind.isEmpty()){
            return null;
        }
        int persons = Integer.parseInt(requestToFind.get().getPersons());
        if (persons < 3){
            availableTime = tables2PersonsRepository.availableTime(requestToFind.get());
        }
        return availableTime;
    }
}
