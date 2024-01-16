package ru.dmitrychinyaev.cafereserve.service;

import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import ru.dmitrychinyaev.cafereserve.entity.BotCommons;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;
import ru.dmitrychinyaev.cafereserve.repository.BadDaysRepository;
import ru.dmitrychinyaev.cafereserve.repository.RequestRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotService {
    private final RequestRepository requestRepository;
    private final BadDaysRepository badDaysRepository;

    public boolean createRequest(String requestName, String date, Long chatID){
        return requestRepository.addRequest(requestName, new ReservationRequest(date, chatID));
    }
    public boolean setPersonsToRequest(String requestID, String persons){
        return requestRepository.setPersons(requestID, persons);
    }
    //TODO Добавить в метод, который будет сравнивать дату с датой, кот добавил админ когда не раб кафе + создать реп сюда с добавлением и удалением даты

    public boolean setTimeToRequest(String requestID, String time) {
        return requestRepository.setTime(requestID,time);
    }

    public boolean setNamePhoneToRequest(String requestID, String phoneNumber, String name) {
        return requestRepository.setNameAndPhone(requestID,phoneNumber,name);
    }

    public ReservationRequest findRequest(String requestID) {
        return requestRepository.getRequest(requestID);
    }

    public boolean checkTheDate(String callbackData) {
        return badDaysRepository.ifExist(callbackData);
    }

    public ArrayList<String> availableTimeTest(String requestID){
        ArrayList<String> availableTime = new ArrayList<>();
        Optional<ReservationRequest> requestToFind = Optional.ofNullable(requestRepository.getRequest(requestID));
        if(requestToFind.isEmpty()){
            return null;
        }

        String date = requestToFind.get().getDate();
        DateTime dateTime = new DateTime();
        String dateToCompare = dateTime
                .toString(BotCommons.REGEX_DAY_MONTH);
        int time = Integer.parseInt(dateTime.toString(BotCommons.REGEX_HOUR));
        if (!(date.equals(dateToCompare) && time > BotCommons.OPENING_HOUR)) {
            time = BotCommons.OPENING_HOUR;
        } else {
            time++;
        }
        for (int i = time; i < BotCommons.CLOSING_HOUR; i++) {
            availableTime.add(time + ":00");
            time++;
        }
        return availableTime;
    }

    public void removeRequest(String request) {
        requestRepository.removeRequest(request);
    }
}
