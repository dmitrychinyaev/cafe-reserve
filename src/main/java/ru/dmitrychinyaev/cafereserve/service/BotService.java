package ru.dmitrychinyaev.cafereserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;
import ru.dmitrychinyaev.cafereserve.repository.BadDaysRepository;
import ru.dmitrychinyaev.cafereserve.repository.RequestRepository;
import ru.dmitrychinyaev.cafereserve.repository.Tables2PersonsRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotService {
    private final RequestRepository requestRepository;
    private final Tables2PersonsRepository tables2PersonsRepository;
    private final BadDaysRepository badDaysRepository;

    public boolean createRequest(String requestName, String date){
        return requestRepository.addRequest(requestName, new ReservationRequest(date));
    }
    public boolean setPersonsToRequest(String requestID, String persons){
        return requestRepository.setPersons(requestID, persons);
    }
    //TODO Добавить в метод, который будет сравнивать дату с датой, кот добавил админ когда не раб кафе + создать реп сюда с добавлением и удалением даты
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

    public void setTimeToRequest(String requestID, String time) {
        requestRepository.setTime(requestID,time);
    }

    public void setNamePhoneToRequest(String requestID, String phoneNumber, String name) {
        requestRepository.setNameAndPhone(requestID,phoneNumber,name);
    }

    public ReservationRequest findRequest(String requestID) {
        return requestRepository.getRequest(requestID);
    }

    public boolean putRequest(String makeRequestID, String username) {
        ReservationRequest requestToPut = requestRepository.getRequest(makeRequestID);
        String keyUpdate = username + requestToPut.getDate();
        requestRepository.changeKey(keyUpdate, makeRequestID);
        tables2PersonsRepository.putBooking(requestToPut);
        return true;
    }

    public boolean checkTheDate(String callbackData) {
        return badDaysRepository.ifExist(callbackData);
    }
}
