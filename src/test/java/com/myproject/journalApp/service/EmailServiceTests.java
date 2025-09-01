package com.myproject.journalApp.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
        emailService.sendEmail("panigrahi0702@gmail.com",
                "Testing Java Mail Sender",
                "This is me trying to test whether I can send an email using java or not");
    }
}