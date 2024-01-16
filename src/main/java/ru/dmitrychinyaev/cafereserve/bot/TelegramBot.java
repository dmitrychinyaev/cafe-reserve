package ru.dmitrychinyaev.cafereserve.bot;

import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitrychinyaev.cafereserve.configuration.TelegramBotConfiguration;
import ru.dmitrychinyaev.cafereserve.entity.BotCommons;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;
import ru.dmitrychinyaev.cafereserve.service.BotService;
import ru.dmitrychinyaev.cafereserve.service.BotServiceKeyboard;

import java.util.ArrayList;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final BotServiceKeyboard botServiceKeyboard;
    private final BotService botService;
    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getBotname();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfiguration.getToken();
    }
    //TODO описать как админ добавляет и удаляет даты не работающего кафе. И показывает список всех гостей на число
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String username = getUsername(update);

            if (Pattern.matches(BotCommons.COMMAND_START, messageText)) {
                sendMessage(chatId, String.format(BotCommons.TEXT_GREETING, username));
                try {
                    askDate(chatId);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (Pattern.matches(BotCommons.REGEX_PHONE_NUMBER, messageText)) {
                try {
                    processThePhoneNumberFromCallback(chatId, update, messageText, username);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (Pattern.matches("showID", messageText)) {
                sendMessage(chatId, String.valueOf(chatId));
            } else {
                sendMessage(chatId, BotCommons.TEXT_TRY_AGAIN);
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            try {
                if (Pattern.matches(BotCommons.REGEX_ASK_DATE, callbackData)) {
                    processTheDateFromCallback(callbackData, update);
                } else if (Pattern.matches(BotCommons.REGEX_ASK_PEOPLE, callbackData)) {
                    processThePersonsFromCallback(callbackData, update);
                } else if (Pattern.matches(BotCommons.REGEX_ASK_TIME, callbackData)) {
                    processTheTimeFromCallback(callbackData, update);
                } else if (Pattern.matches(BotCommons.REGEX_SUCCESSFUL_ADMIN_RESPONSE, callbackData) && update.getCallbackQuery().getMessage().getChatId()==942625769L) {
                    Long chatIDExtracted = extractChatID(callbackData);
                    sendMessage(chatIDExtracted, BotCommons.TEXT_SUCCESSFUL_BOOKING);
                    sendMessage(942625769L, BotCommons.TEXT_BOOKING_COMPLETED);
                } else if (Pattern.matches(BotCommons.REGEX_UNSUCCESSFUL_ADMIN_RESPONSE, callbackData) && update.getCallbackQuery().getMessage().getChatId()==942625769L) {
                    Long chatIDExtracted = extractChatID(callbackData);
                    sendMessage(chatIDExtracted, BotCommons.TEXT_UNSUCCESSFUL_BOOKING);
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void askDate(long chatId) throws TelegramApiException {
        execute(botServiceKeyboard.dateKeyboard(chatId));
    }
    //TODO ответ администатору после его ответа. Перенеси дату в отображении администратору. Добавить месяц. Добавить чтобы бронирование показывалось клиенту. Добавить информацию как писать номер телефона
    private void askPersons(long chatId) throws TelegramApiException {
        execute(botServiceKeyboard.personsKeyboard(chatId));
    }
    private void askTime(long chatId, ArrayList<String> availableTime) throws TelegramApiException {
        execute(botServiceKeyboard.timeKeyboard(chatId, availableTime));
    }
    private void sendRequestToAdmin(ReservationRequest request) throws TelegramApiException {
        execute(botServiceKeyboard.requestToAdmin(request));
    }

    private String makeRequestID(Update update){
        DateTime dateTime = new DateTime();
        String username;
        try {
            username = update.getCallbackQuery().getMessage().getChat().getUserName();
        } catch (NullPointerException e){
            username = update.getMessage().getChat().getUserName();
        }
        return username + dateTime.toString(BotCommons.REGEX_DAY_MONTH);
    }

    private String getUsername(Update update) {
        String username = update.getMessage().getChat().getFirstName();
        if (username == null || username.isEmpty()) {
            username = update.getMessage().getChat().getUserName();
        }
        return username;
    }

    private void processTheDateFromCallback(String callbackData, Update update) throws TelegramApiException {
        if (botService.checkTheDate(callbackData)) {
            if (botService.createRequest(makeRequestID(update), callbackData, update.getCallbackQuery().getMessage().getChatId())) {
                askPersons(update.getCallbackQuery().getMessage().getChatId());
            } else {
                sendMessage(update.getCallbackQuery().getMessage().getChatId(), BotCommons.TEXT_SOMETHING_WRONG);
            }
        } else {
            sendMessage(update.getCallbackQuery().getMessage().getChatId(), BotCommons.TEXT_BAD_DATE);
        }
    }

    private void processThePersonsFromCallback(String callbackData, Update update) throws TelegramApiException {
        if (botService.setPersonsToRequest(makeRequestID(update), callbackData)) {
            ArrayList<String> availableTime = botService.availableTimeTest(makeRequestID(update));
            askTime(update.getCallbackQuery().getMessage().getChatId(), availableTime);
        } else {
            sendMessage(update.getCallbackQuery().getMessage().getChatId(), BotCommons.TEXT_SOMETHING_WRONG);
        }
    }

    private void processTheTimeFromCallback(String callbackData, Update update) {
        if (botService.setTimeToRequest(makeRequestID(update), callbackData)) {
            sendMessage(update.getCallbackQuery().getMessage().getChatId(), BotCommons.TEXT_ASK_PHONE_NUMBER);
        } else {
            sendMessage(update.getCallbackQuery().getMessage().getChatId(), BotCommons.TEXT_SOMETHING_WRONG);
        }
    }

    private void processThePhoneNumberFromCallback(long chatId, Update update, String phoneNumber, String username) throws TelegramApiException {
        if(botService.setNamePhoneToRequest(makeRequestID(update), phoneNumber, username)) {
            sendMessage(chatId, BotCommons.TEXT_WAIT_FOR_CONFIRMATION);
            sendRequestToAdmin(botService.findRequest(makeRequestID(update)));
            botService.removeRequest(makeRequestID(update));
        }else {
            sendMessage(chatId, BotCommons.TEXT_SOMETHING_WRONG);
        }
    }

    private Long extractChatID(String callback){
        return Long.valueOf(callback.substring(3));
    }
}
