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

    // SA - Sentiment Analysis
    public List<User> getUserForSA() {
        Query query = new Query();

        // By default these two below will be considered as AND criterias
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"));
//        query.addCriteria(Criteria.where("email").exists(true));
//        query.addCriteria(Criteria.where("email").ne(null).ne(""));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));


        // And this can be used for OR or AND condition in this way as well
        /*
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.orOperator(
                Criteria.where("email").exists(true),
                Criteria.where("sentimentAnalysis").is(true)
        ));
        */

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}