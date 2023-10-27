package ru.dmitrychinyaev.cafereserve.repository;

import org.springframework.stereotype.Repository;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RequestRepository {
    private Map<String, ReservationRequest> repository = new HashMap<>();

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
}
