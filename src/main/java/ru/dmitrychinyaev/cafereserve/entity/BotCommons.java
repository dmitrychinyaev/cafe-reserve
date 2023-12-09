package ru.dmitrychinyaev.cafereserve.entity;

import org.springframework.stereotype.Component;

@Component
public class BotCommons {

    //bot commands
    public static final String COMMAND_START = "/start";

    //text
    public static final String TEXT_ASK_DATE = "На какое число Вы хотите забронировать столик?";
    public static final String TEXT_ASK_PEOPLE = "На какое количество человек?";
    public static final String TEXT_ASK_PHONE_NUMBER = "Напишите Ваш номер телефона для связи";
    public static final String TEXT_ASK_TIME = "На какое время?";
    public static final String TEXT_BAD_DATE = "К сожалению, в эту дату кафе не работает. Попробуйте выбрать другую";
    public static final String TEXT_GREETING = "Здравствуйте, %s!";
    public static final String TEXT_TRY_AGAIN = "Неправильный запрос. Попробуйте еще раз";
    public static final String TEXT_SUCCESS_BOOKING = "Вы успешно забронировали!";
    public static final String TEXT_SOMETHING_WRONG = "Что-то пошло не так. Попробуйте начать заново. Нажмите команду /start";

    //regex
    public static final String REGEX_PHONE_NUMBER = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    public static final String REGEX_ASK_DATE = "\\d{2}";
    public static final String REGEX_ASK_TIME = "\\d{2}:00";
    public static final String REGEX_ASK_PEOPLE = "\\d";
    public static final String REGEX_DAY_MONTH = "dd.MM";
    public static final String REGEX_DAY = "dd";
}
