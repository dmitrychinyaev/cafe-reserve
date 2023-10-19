package ru.dmitrychinyaev.cafereserve.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class TelegramBotConfiguration {
    @Value("${bot.name}")
    String botname;
    @Value("${bot.token}")
    String token;
}
