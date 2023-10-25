package ru.dmitrychinyaev.cafereserve.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitrychinyaev.cafereserve.configuration.TelegramBotConfiguration;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;
import ru.dmitrychinyaev.cafereserve.entity.TelegramBotCommon;
import ru.dmitrychinyaev.cafereserve.service.TelegramBotService;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final TelegramBotService telegramBotService;
    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getBotname();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfiguration.getToken();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getChat().getUserName();
            switch (messageText) {
                case TelegramBotCommon.COMMAND_START -> {
                    ReservationRequest request = new ReservationRequest();
                    sendMessage(chatId, String.format(TelegramBotCommon.TEXT_GREETING, username));
                    try {
                        askDate(chatId);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            if (Pattern.matches("\\d{2}", callbackData)){
                try {
                    askPersons(update.getCallbackQuery().getMessage().getChatId());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            //long messageId = update.getCallbackQuery().getMessage().getMessageId();
            //long chatId = update.getCallbackQuery().getMessage().getChatId();
            //try {
            //    executeEditMessageText(callbackData, chatId, messageId);
            //} catch (TelegramApiException e) {
            //    throw new RuntimeException(e);
            //}
        }
    }

    private void executeEditMessageText(String text, long chatId, long messageId) throws TelegramApiException {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);
        execute(message);
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
        execute(telegramBotService.dateKeyboard(chatId));
    }

    private void askPersons(long chatId) throws TelegramApiException {
        execute(telegramBotService.personsKeyboard(chatId));
    }
    private void askTime(long chatId) throws TelegramApiException {
        execute(telegramBotService.timeKeyboard(chatId));
    }

}
