package com.myproject.journalApp.repository;

import com.myproject.journalApp.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {

}


//controller ---> service ---> repository