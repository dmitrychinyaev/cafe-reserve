package ru.dmitrychinyaev.cafereserve.service;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramBotService {
    public SendMessage dateKeyboard(long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("На какое число Вы хотите забронировать стол?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        DateTime dateTime = new DateTime();
        for (int i = 0; i < 8; i++) {
            var button = new InlineKeyboardButton();
            String date = dateTime
                    .plusDays(i)
                    .toString("dd");
            button.setText(date);
            button.setCallbackData(date);
            rowInLine.add(button);
        }

        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        return message;
    }

    public SendMessage personsKeyboard(long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Какое количество человек?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            var button = new InlineKeyboardButton();
            button.setText(String.valueOf(i));
            button.setCallbackData(String.valueOf(i));
            rowInLine.add(button);
        }

        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        return message;
    }

    public SendMessage timeKeyboard(long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("На какое время?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        //DateTime dateTime = new DateTime();
        for (int i = 12; i < 22; i = i + 1) {
            var button = new InlineKeyboardButton();
        //    String date = dateTime
        //            .plusDays(i)
        //            .toString("dd");
            button.setText(i + ":00");
            button.setCallbackData(i + ":00");
            rowInLine.add(button);
        }

        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        return message;
    }


}
