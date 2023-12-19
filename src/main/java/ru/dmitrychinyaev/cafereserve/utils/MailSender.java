package ru.dmitrychinyaev.cafereserve.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.dmitrychinyaev.cafereserve.entity.BotCommons;

@Service
public class MailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.recipient}")
    private String recipient;


    public void send(String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(recipient);
        mailMessage.setSubject(BotCommons.TEXT_SUBJECT_EMAIL);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
