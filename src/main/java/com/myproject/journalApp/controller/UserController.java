package com.myproject.journalApp.controller;

import com.myproject.journalApp.api.response.QuoteResponse;
import com.myproject.journalApp.api.response.WeatherResponse;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.repository.UserRepository;
import com.myproject.journalApp.service.QuoteService;
import com.myproject.journalApp.service.UserService;
import com.myproject.journalApp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final WeatherService weatherService;
    private final QuoteService quoteService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Bhubaneswar");
        String greeting = "Hi " + authentication.getName();
        if (weatherResponse != null) {
            greeting += ", Weather feels like " + weatherResponse.getCurrent().getFeelslike() + " degree Celsius. \n";
        }
        QuoteResponse quote = quoteService.getQuote();
        if (quote != null) {
            greeting += "Quote of the day: \"" + quote.getQuote() + "\" - " + quote.getAuthor();
        }
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }
}

//Hello