package ca.hitkumar.journalApp.scheduler;

import ca.hitkumar.journalApp.cache.AppCache;
import ca.hitkumar.journalApp.entity.JournalEntry;
import ca.hitkumar.journalApp.entity.UserEntry;
import ca.hitkumar.journalApp.enums.Sentiment;
import ca.hitkumar.journalApp.repository.UserRepositoryImpl;
import ca.hitkumar.journalApp.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AppCache appCache;

    /**
     * Run every Sunday at 9 AM
     */
    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail() {
        List<UserEntry> users = userRepository.getUserForSA();
        Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);

        for (UserEntry user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();

            // Filter journal entries from the last 7 days and extract sentiments
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().toInstant().isAfter(sevenDaysAgo))
                    .map(JournalEntry::getSentiment)
                    .collect(Collectors.toList());

            // Count sentiments
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            // Find most frequent sentiment
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            // Send email if a sentiment was found
            if (mostFrequentSentiment != null) {
                emailService.sendEmail(
                        user.getEmail(),
                        "Sentiment for last 7 days",
                        mostFrequentSentiment.toString()
                );
            }
        }
    }

    /**
     * Run every 10 minutes to clear cache
     */
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }
}
