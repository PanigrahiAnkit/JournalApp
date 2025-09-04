package com.myproject.journalApp.scheduler;

import com.myproject.journalApp.cache.AppCache;
import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.enums.Sentiment;
import com.myproject.journalApp.model.SentimentData;
import com.myproject.journalApp.repository.UserRepositoryImpl;
import com.myproject.journalApp.service.EmailService;
import com.myproject.journalApp.service.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final EmailService emailService;
    private final UserRepositoryImpl userRepository;
    private final AppCache appCache;
    private final KafkaTemplate<String, SentimentData> kafkaTemplate;

//    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMail() {
        List<User> users = userRepository.getUserForSA();
        for(User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments =
                    journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7,
                            ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for(Sentiment sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;

            for(Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if(mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for" +
                        " last 7 days " + mostFrequentSentiment).build();
                try{
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData); //
                    // Asynchronously sending email
                } catch (Exception e) {
                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                    // Synchronously sending email
                }
            }
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // every 5 minute
    public void clearAppCache() {
        appCache.init();
    }
}