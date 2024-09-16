package com.patsi.serviceTests;

import com.patsi.repository.PersonRepository;
import com.patsi.repository.UnverifiedPersonRepository;
import com.patsi.service.FlagToggleServices;
import com.patsi.service.LoginValueServices;
import com.patsi.service.PersonInfoService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.TestPropertySource;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = {"saltForPasswordEncryption = 8ab15c1c1c271e297c3f6d34e695b3f8",
    "algorithmForPasswordEncryption = SHA3-256"})
public class PersonInfoServiceTest extends CommonTest{
    @InjectMocks
    private PersonInfoService personInfoService;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UnverifiedPersonRepository unverifiedPersonRepository;
    @Mock
    private FlagToggleServices flagToggleServices;
    @Mock
    private LoginValueServices loginValueServices;

    void verifyAccountSetUp() {
        when(personRepository.findByEmail("personA@gmail.com"))
            .thenReturn(Optional.empty());
        when(unverifiedPersonRepository.findByToken("111111"))
            .thenReturn(Optional.ofNullable(unverifiedPersonA));
        when(loginValueServices.getSaltProperties())
            .thenReturn("8ab15c1c1c271e297c3f6d34e695b3f8");
        when(loginValueServices.getAlgorithmProperties())
            .thenReturn("SHA3-256");
    }

    @Test
    void getAllPersonTest() {
        when(personRepository.findAll())
            .thenReturn(personList);
        assertEquals(personList, personInfoService.getAllPerson());
    }
    @Test
    void registerVerifiedPersonTestWithVerifiedAccount() {
        when(personRepository.findByEmail("personA@gmail.com"))
            .thenReturn(Optional.ofNullable(personA));
        assertEquals("Account already Activated",
            personInfoService.registerVerifiedPerson("000000", "personA@gmail.com"));
    }
    @Test
    void registerVerifiedPersonTestWithUnverifiedAccountAndValidToken() {
        verifyAccountSetUp();
        assertEquals("Successfully Created Account",
            personInfoService.registerVerifiedPerson("111111", "unverifiedPersonA@gmail.com"));
    }
    @Test
    void registerVerifiedPersonTestWithUnverifiedAccountAndInvalidToken() {
        verifyAccountSetUp();
        assertEquals("Invalid Token!",
            personInfoService.registerVerifiedPerson("000000", "unverifiedPersonA@gmail.com"));
    }
    @Test
    void registerVerifiedPersonTestWithUnverifiedAccountAndInvalidEmail() {
        verifyAccountSetUp();
        assertEquals("Passcode mismatch Email!",
            personInfoService.registerVerifiedPerson("111111", "unverifiedPersonB@gmail.com"));
    }
    @Test
    void registerUnverifiedPersonTest() throws MessagingException {
        when(flagToggleServices.getEnableEmailFlag()).thenReturn(false);
        assertEquals("",
            personInfoService.registerUnverifiedPerson(unverifiedPersonA));
    }
    @Test
    void deletePeronTestWithUnverifiedAccountAndInvalidEmail() throws MessagingException {
        personInfoService.deletePeron(personA);
        verify(personRepository, times(1)).deleteById(personA.getUid());
    }

}
