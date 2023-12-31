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
                processThePhoneNumberFromCallback(chatId, update, messageText, username);
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

    private void askPersons(long chatId) throws TelegramApiException {
        execute(botServiceKeyboard.personsKeyboard(chatId));
    }
    private void askTime(long chatId, ArrayList<String> availableTime) throws TelegramApiException {
        execute(botServiceKeyboard.timeKeyboard(chatId, availableTime));
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
            if (botService.createRequest(makeRequestID(update), callbackData)) {
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
            ArrayList<String> availableTime = botService.findAvailableTime(makeRequestID(update));
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

    private void processThePhoneNumberFromCallback(long chatId, Update update, String messageText, String username) {
        if(botService.setNamePhoneToRequest(makeRequestID(update), messageText, username)) {
            if (botService.putRequest(makeRequestID(update), update.getMessage().getChat().getUserName())) {
                sendMessage(chatId, BotCommons.TEXT_SUCCESS_BOOKING);
            } else {
                sendMessage(chatId, BotCommons.TEXT_SOMETHING_WRONG);
            }
        }
    }
}
