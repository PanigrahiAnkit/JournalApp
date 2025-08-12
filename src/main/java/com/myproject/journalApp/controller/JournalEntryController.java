package com.myproject.journalApp.controller;

import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.service.JournalEntryService;
import com.myproject.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) { // localhost:8080/journal GET
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) { //
        // localhost:8080/journal
        // GET
        try {
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myID}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myID) {
        Optional<JournalEntry> journalEntry = journalEntryService.findByID(myID); // Introduced a variable using Ctrl+Alt+V
        if(journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myID}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myID) { // ?: Refers to wildcard entry
        journalEntryService.deleteById(myID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?>  updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {
//        JournalEntry old = journalEntryService.findByID(id).orElse(null);
//
//        if(old != null) {
//            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() :
//                    old.getTitle());
//            old.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() :
//                    old.getContent());
//
//            journalEntryService.saveEntry(old, user);
//            return new ResponseEntity<>(old, HttpStatus.OK);
//        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}