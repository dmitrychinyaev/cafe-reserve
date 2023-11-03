package ru.dmitrychinyaev.cafereserve.entity;

import org.springframework.stereotype.Component;

@Component
public class TelegramBotCommon {

    //bot commands
    public static final String COMMAND_START = "/start";

    //text

    public static final String TEXT_GREETING = "Hi, %s!";

    public static final String TEXT_ASK_PHONE_NUMBER = "Напишите Ваш номер телефона для связи";

    //regex

    public static final String REGEX_PHONE_NUMBER = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
}
