package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${alert.email.to}")
    private String emailTo;

    @Value("${telegram.bot.token}")
    private String telegramToken;

    @Value("${telegram.chat.id}")
    private String telegramChatId;

    public AlertService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email alert sent: {}", subject);
        } catch (Exception e) {
            log.error("Failed to send email alert: {}", e.getMessage());
        }
    }

    public void sendTelegram(String text) {
        try {
            String url = "https://api.telegram.org/bot" + telegramToken + "/sendMessage";
            Map<String, String> payload = Map.of("chat_id", telegramChatId, "text", text);
            restTemplate.postForObject(url, payload, String.class);
            log.info("Telegram alert sent");
        } catch (Exception e) {
            log.error("Failed to send Telegram alert: {}", e.getMessage());
        }
    }
}