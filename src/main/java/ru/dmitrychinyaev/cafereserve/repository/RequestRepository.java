package ru.dmitrychinyaev.cafereserve.repository;

import org.springframework.stereotype.Repository;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RequestRepository {
    private final Map<String, ReservationRequest> requestRepository = new HashMap<>();

    public ReservationRequest getRequest(String requestID){
        return requestRepository.get(requestID);
    }

    public boolean addRequest(String requestId, ReservationRequest reservationRequest){
        if (!requestRepository.containsKey(requestId)) {
            requestRepository.put(requestId,reservationRequest);
            return true;
        } else {
            requestRepository.remove(requestId);
            return false;
        }
    }

    public boolean setPersons(String requestID, String personsNumber) {
        ReservationRequest requestToChange = requestRepository.get(requestID);
        if (!requestToChange.getPersons().isEmpty()) {
            requestToChange.setPersons(personsNumber);
            requestRepository.replace(requestID, requestToChange);
            return true;
        }
        return false;
    }

    public void setTime(String requestID, String time) {
        ReservationRequest requestToChange = requestRepository.get(requestID);
        requestToChange.setTime(time);
        requestRepository.replace(requestID, requestToChange);
    }

    public void setNameAndPhone(String requestID, String phoneNumber, String name) {
        ReservationRequest requestToChange = requestRepository.get(requestID);
        requestToChange.setPhoneNumber(phoneNumber);
        requestToChange.setName(name);
        requestRepository.replace(requestID, requestToChange);
    }

    public void changeKey(String keyUpdate, String requestID) {
        requestRepository.put(keyUpdate, requestRepository.remove(requestID));
    }
}
