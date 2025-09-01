package com.myproject.journalApp.repository;

import com.myproject.journalApp.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {
        Query query = new Query();

        query.addCriteria(Criteria.where("email").exists(true));

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}