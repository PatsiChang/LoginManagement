package com.patsi.serviceTests;

import com.patsi.bean.LogInSession;
import com.patsi.bean.Person;
import com.patsi.bean.UserLogin;
import com.patsi.repository.PersonRepository;
import com.patsi.repository.SessionRepository;
import com.patsi.service.LogInSessionService;
import com.patsi.service.LoginService;
import com.patsi.service.LoginValueServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestPropertySource(properties = {"saltForPasswordEncryption = 8ab15c1c1c271e297c3f6d34e695b3f8",
    "algorithmForPasswordEncryption = SHA3-256"}
)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private PersonRepository personRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private LogInSessionService logInSessionService;
    @Mock
    private LoginValueServices loginValueServices;

    //Login
    final String validUID = "TestSalt2";
    final UUID validPersonId = UUID.randomUUID();
    final Person validPerson = new Person(validPersonId, "TestSalt2", "TestSalt2",
        "patsi@gmail.com", "da39dc482848b95ee4370158fbd42ac4e8056e118795abe781531fe36a9b0527");
    final LogInSession expectedLoginSession = new LogInSession();
    final String expectedToken = "test-token-123";
    final Date currentDate = new Date();
    //Logout
    final String validToken = "validToken";
    final String invalidToken = "invalidToken";


    @BeforeEach
    void setUp() {
        when(logInSessionService.findPerson(validUID))
            .thenReturn(new Person());
        when(personRepository.findByUserId("TestSalt2"))
            .thenReturn(Optional.of(validPerson));
        when(sessionRepository.findByCustomerId(validPersonId))
            .thenReturn(Optional.of(expectedLoginSession));
        when(logInSessionService.createUserToken())
            .thenReturn(expectedToken);
        when(loginValueServices.getSaltProperties())
            .thenReturn("8ab15c1c1c271e297c3f6d34e695b3f8");
        when(loginValueServices.getAlgorithmProperties())
            .thenReturn("SHA3-256");
    }


    //Login
    @Test
    void testCheckLogInIfValidUserIdAndPassword() {
        UserLogin validUser = new UserLogin(validUID, "TestSalt2");
        assertEquals(expectedToken, loginService.checkLogIn(validUser));
        verify(logInSessionService).endSession(validPersonId);
    }

    @Test
    void testCheckLogInIfInvalidUserIdAndPasswordWithNoExistingSession() {
        when(sessionRepository.findByCustomerId(validPersonId))
            .thenReturn(Optional.empty());
        UserLogin user2 = new UserLogin(validUID, "TestSalt2");
        assertEquals(expectedToken, loginService.checkLogIn(user2));
        verify(logInSessionService).findPerson(any());
        verify(logInSessionService, never()).endSession(validPersonId);
    }

    @Test
    void testCheckLogInIfInvalidUserIdAndPassword() {
        UserLogin user2 = new UserLogin("TestSalt2", "WrongPassword");
        assertEquals(null, loginService.checkLogIn(user2));
        verify(logInSessionService).findPerson(any());
        verifyNoMoreInteractions(logInSessionService);
    }

    //Logout
    @Test
    void testLogoutWithValidToken() {
        assertEquals(true, loginService.logOut(validToken));
        verify(logInSessionService).endSessionByToken(validToken);
    }

    @Test
    void testLogoutWithInvalidToken() {
        assertEquals(true, loginService.logOut(invalidToken));
        verify(logInSessionService).endSessionByToken(invalidToken);
    }
}
