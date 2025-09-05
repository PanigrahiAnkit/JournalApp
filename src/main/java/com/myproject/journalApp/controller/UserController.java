package com.myproject.journalApp.controller;

import com.myproject.journalApp.api.response.QuoteResponse;
import com.myproject.journalApp.api.response.WeatherResponse;
import com.myproject.journalApp.dto.UserUpdateDTO;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.repository.UserRepository;
import com.myproject.journalApp.service.QuoteService;
import com.myproject.journalApp.service.UserService;
import com.myproject.journalApp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "User APIs",
        description = "Endpoints for user profile management, including update, delete, and personalized greeting."
)

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final WeatherService weatherService;
    private final QuoteService quoteService;

    @PutMapping
    @Operation(
            summary = "Update user profile",
            description = "Updates the authenticated user's profile information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        userInDb.setUserName(userUpdateDTO.getUserName());
        userInDb.setPassword(userUpdateDTO.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Operation(
            summary = "Delete user account",
            description = "Deletes the authenticated user's account."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(
            summary = "Get personalized greeting",
            description = "Returns a personalized greeting with weather and quote of the day for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Greeting returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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