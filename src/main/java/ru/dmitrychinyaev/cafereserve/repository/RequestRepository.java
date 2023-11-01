package ru.dmitrychinyaev.cafereserve.repository;

import org.springframework.stereotype.Repository;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RequestRepository {
    private final Map<String, ReservationRequest> repository = new HashMap<>();

    public ReservationRequest getRequest(String requestID){
        return repository.get(requestID);
    }

    public void addRequest(String requestId, ReservationRequest reservationRequest){
        repository.put(requestId,reservationRequest);
    }

    public void setPersons(String requestID, String personsNumber){
        ReservationRequest requestToChange = repository.get(requestID);
        requestToChange.setPersons(personsNumber);
        repository.replace(requestID, requestToChange);
    }

    public void setTime(String requestID, String time) {
        ReservationRequest requestToChange = repository.get(requestID);
        requestToChange.setTime(time);
        repository.replace(requestID, requestToChange);
    }

    public void setNameAndPhone(String requestID, String phoneNumber, String name) {
        ReservationRequest requestToChange = repository.get(requestID);
        requestToChange.setPhoneNumber(phoneNumber);
        requestToChange.setName(name);
        repository.replace(requestID, requestToChange);
    }

    public void changeKey(String keyUpdate, String requestID) {
        repository.put(keyUpdate, repository.remove(requestID));
    }
}
