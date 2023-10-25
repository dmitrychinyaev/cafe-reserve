package ru.dmitrychinyaev.cafereserve.entity;

import org.springframework.stereotype.Component;

@Component
public class TelegramBotCommon {

    //bot commands
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_HELP = "/help";

    //text

    public static final String TEXT_GREETING = "Приветствую, %s!";
}
