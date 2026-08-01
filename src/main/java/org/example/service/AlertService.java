package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${alert.email.to}")
    private String emailTo;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${telegram.bot.token}")
    private String telegramToken;

    @Value("${telegram.chat.id}")
    private String telegramChatId;

    public void sendEmail(String subject, String body) {
        try {
            String url = "https://api.brevo.com/v3/smtp/email";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);

            Map<String, Object> payload = Map.of(
                    "sender", Map.of("email", senderEmail, "name", "IPO Tracker"),
                    "to", List.of(Map.of("email", emailTo)),
                    "subject", subject,
                    "textContent", body
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            restTemplate.postForObject(url, request, String.class);
            log.info("Email alert sent via Brevo: {}", subject);
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