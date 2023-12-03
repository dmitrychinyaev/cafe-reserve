package ru.dmitrychinyaev.cafereserve.service;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.dmitrychinyaev.cafereserve.entity.TelegramBotCommon;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramBotServiceKeyboard {
    public SendMessage dateKeyboard(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(TelegramBotCommon.TEXT_ASK_DATE);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        DateTime dateTime = new DateTime();
        for (int i = 0; i < 8; i++) {
            var button = new InlineKeyboardButton();
            String date = dateTime
                    .plusDays(i)
                    .toString(TelegramBotCommon.REGEX_DAY);
            button.setText(date);
            button.setCallbackData(date);
            rowInLine.add(button);
        }

        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        return message;
    }

    public SendMessage personsKeyboard(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(TelegramBotCommon.REGEX_ASK_PEOPLE);

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

    public SendMessage timeKeyboard(long chatId, ArrayList<String> availableTime) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(TelegramBotCommon.TEXT_ASK_TIME);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            var button1 = new InlineKeyboardButton();
            String time1 = availableTime.get(i);
            button1.setText(time1);
            button1.setCallbackData(time1);
            rowInLine1.add(button1);
        }
        rowsInLine.add(rowInLine1);

        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        if (availableTime.size() > 4) {
            for (int j = 4; j < 8; j++) {
                var button2 = new InlineKeyboardButton();
                String time2 = availableTime.get(j);
                button2.setText(time2);
                button2.setCallbackData(time2);
                rowInLine2.add(button2);
            }
        }
        rowsInLine.add(rowInLine2);

        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        if (availableTime.size() > 8) {
            for (int k = 8; k < availableTime.size(); k++) {
                var button3 = new InlineKeyboardButton();
                String time3 = availableTime.get(k);
                button3.setText(time3);
                button3.setCallbackData(time3);
                rowInLine3.add(button3);
            }
        }
        rowsInLine.add(rowInLine3);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        return message;
    }
}
