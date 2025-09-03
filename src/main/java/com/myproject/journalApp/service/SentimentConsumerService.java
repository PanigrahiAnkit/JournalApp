package com.myproject.journalApp.service;

import com.myproject.journalApp.model.SentimentData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentimentConsumerService {

    private final EmailService emailService;

    @KafkaListener(topics = "weekly-sentiments", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData) { // Deserialization
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData) {
        emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
    }
}