package ru.dmitrychinyaev.cafereserve.bot;

import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitrychinyaev.cafereserve.configuration.TelegramBotConfiguration;
import ru.dmitrychinyaev.cafereserve.entity.TelegramBotCommon;
import ru.dmitrychinyaev.cafereserve.service.TelegramBotService;
import ru.dmitrychinyaev.cafereserve.service.TelegramBotServiceKeyboard;

import java.util.ArrayList;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final TelegramBotServiceKeyboard telegramBotServiceKeyboard;
    private final TelegramBotService telegramBotService;
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
            String username = update.getMessage().getChat().getFirstName();

            if (messageText.equals(TelegramBotCommon.COMMAND_START)) {
                sendMessage(chatId, String.format(TelegramBotCommon.TEXT_GREETING, username));
                try {
                    askDate(chatId);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if(Pattern.matches(TelegramBotCommon.REGEX_PHONE_NUMBER, messageText)){
                if(username == null || username.equals("")){
                    username = update.getMessage().getChat().getUserName();
                }
                telegramBotService.setNamePhoneToRequest(makeRequestID(update),messageText, username);
                sendMessage(update.getMessage().getChatId(), "good!");
                sendMessage(update.getMessage().getChatId(),telegramBotService.findRequest(makeRequestID(update)).successBooking());
                telegramBotService.putRequest(makeRequestID(update), update.getMessage().getChat().getUserName());
            } else {
                //TODO Написать если ошибочный текст
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            if (Pattern.matches("\\d{2}", callbackData)){
                //TODO вынести в отдельные методы с проверкой, если кто-то уже ввел время и время стоит в запросе -> вернуть ошибку
                telegramBotService.createRequest(makeRequestID(update), callbackData);
                try {
                    askPersons(update.getCallbackQuery().getMessage().getChatId());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            //TODO Записать константы
            if (Pattern.matches("\\d", callbackData)){
                telegramBotService.setPersonsToRequest(makeRequestID(update),callbackData);
                ArrayList<String> availableTime = telegramBotService.findAvailableTime(makeRequestID(update));
                try {
                    askTime(update.getCallbackQuery().getMessage().getChatId(), availableTime);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if(Pattern.matches("\\d{2}:00", callbackData)){
                telegramBotService.setTimeToRequest(makeRequestID(update),callbackData);
                sendMessage(update.getCallbackQuery().getMessage().getChatId(), TelegramBotCommon.TEXT_ASK_PHONE_NUMBER);
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
        execute(telegramBotServiceKeyboard.dateKeyboard(chatId));
    }

    private void askPersons(long chatId) throws TelegramApiException {
        execute(telegramBotServiceKeyboard.personsKeyboard(chatId));
    }
    private void askTime(long chatId, ArrayList<String> availableTime) throws TelegramApiException {
        execute(telegramBotServiceKeyboard.timeKeyboard(chatId, availableTime));
    }

    private String makeRequestID(Update update){
        DateTime dateTime = new DateTime();
        String username;
        try {
            username = update.getCallbackQuery().getMessage().getChat().getUserName();
        } catch (NullPointerException e){
            username = update.getMessage().getChat().getUserName();
        }
        return username + dateTime.toString("dd.MM");
    }

}
