package com.myproject.journalApp.repository;

import com.myproject.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<JournalEntry, ObjectId> {

}


//controller ---> service ---> repository