package ru.dmitrychinyaev.cafereserve.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitrychinyaev.cafereserve.configuration.TelegramBotConfiguration;

@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramBotConfiguration telegramBotConfiguration;
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
}
