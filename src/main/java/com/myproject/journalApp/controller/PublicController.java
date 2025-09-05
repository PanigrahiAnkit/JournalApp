package com.myproject.journalApp.controller;

import com.myproject.journalApp.dto.LoginRequestDTO;
import com.myproject.journalApp.dto.UserDTO;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.service.UserDetailsServiceImpl;
import com.myproject.journalApp.service.UserService;
import com.myproject.journalApp.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/public")
@Tag(
        name = "Public APIs",
        description = "Endpoints for public operations such as health check, user signup, and login."
)
public class PublicController {


    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @GetMapping("/health-check")
    @Operation(
            summary = "Health check",
            description = "Returns OK if the service is running."
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public String healthCheck() {
        return "OK";
    }


    @PostMapping("/signup")
    @Operation(
            summary = "User signup",
            description = "Registers a new user with the provided details."
    )
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public void signup(@RequestBody UserDTO user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setSentimentAnalysis(user.getSentimentAnalysis());
        userService.saveNewUser(newUser);
    }


    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token if credentials are valid."
    )
    @ApiResponse(responseCode = "200", description = "Login successful, JWT returned")
    @ApiResponse(responseCode = "400", description = "Incorrect username or password")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUserName(),
                            loginRequestDTO.getPassword()
                    )
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occured while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

}