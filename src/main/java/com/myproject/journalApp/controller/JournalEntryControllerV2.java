package com.myproject.journalApp.controller;

import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {


    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll() { // localhost:8080/journal GET
        return null;
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry) { // localhost:8080/journal GET
        journalEntryService.saveEntry(myEntry);
        return true;
    }

    @GetMapping("id/{myID}")
    public JournalEntry getJournalEntryById(@PathVariable Long myID) {
        return null;
    }

    @DeleteMapping("id/{myID}")
    public JournalEntry deleteJournalEntryById(@PathVariable Long myID) {
        return null;
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateJournalById(@PathVariable Long id, @RequestBody JournalEntry myEntry) {
        return null;
    }
}