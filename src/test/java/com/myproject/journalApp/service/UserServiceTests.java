package com.myproject.journalApp.service;

import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


/* @SpringBootTest
- Boots the full Spring application context for the test.
- That means all beans get created and Spring tries to connect to the configured MongoDB.
- This is an integration-style test (heavier and slower than a focused repository “slice” test).
*/

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Disabled
    @Test
    public void testFindByUserName() {
        assertNotNull(userRepository.findByUserName("Banu"));
    }

    @Disabled
    @Test
    public void testUserHasJournalEntries() {
        User user = userRepository.findByUserName("Ankit");
        assertFalse(user.getJournalEntries().isEmpty());
    }


    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 6, 8",
            "1, 9, 11"
    })
    public void test(int a, int b, int expected) {
        assertEquals(expected, a+b);
    }



}