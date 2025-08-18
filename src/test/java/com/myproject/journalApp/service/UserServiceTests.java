package com.myproject.journalApp.service;

import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/* @SpringBootTest
- Boots the full Spring application context for the test.
- That means all beans get created and Spring tries to connect to the configured MongoDB.
- This is an integration-style test (heavier and slower than a focused repository “slice” test).
*/
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {
            "Ankit",
            "Banu",
            "Anku"
    })
    public void testFindByUserName(String name) {
        // Arrange
        User stub =
                User.builder()
                        .userName(name)
                        .password("<password>")
                        .roles(List.of("USER"))
                        .build();
        when(userRepository.findByUserName(name)).thenReturn(stub);

        // Act
        User found = userRepository.findByUserName(name);

        // Assert
        assertNotNull(found);
        assertEquals(name, found.getUserName());
        verify(userRepository, times(1)).findByUserName(name);
    }

    @Disabled
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testSaveNewUser(User user) {
        // Arrange
        when(userRepository.findByUserName(user.getUserName())).thenReturn(null); // no duplicate user
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        boolean saved = userService.saveNewUser(user);

        // Assert
        assertTrue(saved);
        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Disabled
    @Test
    public void testUserHasJournalEntries() {
        // Arrange
        User user =
                User.builder()
                        .userName("Ankit")
                        .password("<password>")
                        .roles(List.of("USER"))
                        .build();
        // Add a mocked journal entry to avoid needing a concrete JournalEntry constructor
        user.getJournalEntries().add(mock(JournalEntry.class));
        when(userRepository.findByUserName("Ankit")).thenReturn(user);

        // Act
        User found = userRepository.findByUserName("Ankit");

        // Assert
        assertNotNull(found);
        assertFalse(found.getJournalEntries().isEmpty());
        verify(userRepository, times(1)).findByUserName("Ankit");
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 6, 8",
            "1, 9, 11"
    })
    public void test(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }
}