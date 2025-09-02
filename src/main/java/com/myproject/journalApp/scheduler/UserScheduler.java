package com.myproject.journalApp.scheduler;

import com.myproject.journalApp.cache.AppCache;
import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.enums.Sentiment;
import com.myproject.journalApp.repository.UserRepositoryImpl;
import com.myproject.journalApp.service.EmailService;
import com.myproject.journalApp.service.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final EmailService emailService;
    private final UserRepositoryImpl userRepository;
    private final AppCache appCache;

//    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMail() {
        List<User> users = userRepository.getUserForSA();
        for(User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> filteredEntries =
                    journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7,
                            ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            String entry = String.join(" ", filteredEntries);
            String sentiment = sentimentAnalysisService.getSentiment(entry);
            emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", sentiment);
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // every 5 minute
    public void clearAppCache() {
        appCache.init();
    }
}