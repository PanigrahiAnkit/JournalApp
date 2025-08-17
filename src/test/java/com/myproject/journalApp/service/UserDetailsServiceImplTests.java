package com.myproject.journalApp.service;

import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests using Mockito without a Spring application context.
 *
 * Rationale:
 * - @SpringBootTest was removed to avoid starting the full Spring context for a simple unit test.
 *   Booting the context is slow and may attempt to connect to external resources (e.g., MongoDB).
 * - With no Spring context, @Autowired fields would be null. To avoid NullPointerException, this test
 *   uses Mockito to construct and inject dependencies.
 *
 * How this test is wired:
 * - @ExtendWith(MockitoExtension.class) enables Mockito for JUnit 5.
 * - @Mock creates test doubles for the service's collaborators (e.g., repositories).
 * - @InjectMocks creates the system under test and injects the @Mock fields into it via constructor,
 *   setter, or field injection as available.
 *
 * Benefits:
 * - Fast, deterministic, and isolated unit tests (no I/O, no Spring bootstrap).
 * - Clear control over collaborator behavior via when(...).thenReturn(...) stubbing.
 *
 * Notes:
 * - Replace any @MockitoBean/@SpyBean/@Autowired from the Spring-based test with @Mock and @InjectMocks.
 * - Keep mocking focused: only mock direct collaborators of the class under test.
 * - If you need to verify integration with Spring configuration or the database, write a separate
 *   integration or slice test (e.g., @DataMongoTest or @SpringBootTest) apart from this unit test.
 */

/**
* MockitoAnnotations.initMocks(...) was deprecated. Use MockitoAnnotations.openMocks(...) or, preferably with JUnit5,
 * use the MockitoExtension which initializes mocks automatically.
*/

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTests {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    /*
    * This can also be used, if not, using @ExtendUsing(MockitoExtension.class)
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
     */


    @Test
    void loadUserByUsernameTest() {
        when(userRepository.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(User.builder()
                        .userName("Ankit")
                        .password("aasassa")
                        .roles(new ArrayList<>())
                        .build());

        UserDetails user = userDetailsService.loadUserByUsername("Ankit");
        assertThat(user).isNotNull();

    }
}