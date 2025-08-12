package com.myproject.journalApp.service;

import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.repository.JournalEntryRepository;
import com.myproject.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByID(ObjectId Id) {
        return userRepository.findById(Id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

}