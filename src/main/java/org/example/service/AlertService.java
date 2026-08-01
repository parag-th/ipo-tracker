package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telegram.bot.token}")
    private String telegramToken;

    @Value("${telegram.chat.id}")
    private String telegramChatId;

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