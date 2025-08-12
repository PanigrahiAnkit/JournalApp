package com.myproject.journalApp.service;

import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findByID(ObjectId Id) {
        return journalEntryRepository.findById(Id);
    }

    public void deleteById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }

}