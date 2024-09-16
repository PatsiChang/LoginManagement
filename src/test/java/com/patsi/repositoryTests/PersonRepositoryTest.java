package com.patsi.repositoryTests;

import com.patsi.bean.Person;
import com.patsi.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;

public class PersonRepositoryTest extends CommonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void setUpPersonRepositoryTest() {
        entityManager.persist(
            Person.builder()
            .userId("unitTestUser")
            .name("UnitTester")
            .email("testPerson@gmail.com")
            .password("unitTestPassword").build());
    }

    @Test
    public void testSavePerson() {
        personRepository.save(
            Person.builder()
                .userId("testSavePersonUserId")
                .name("testSavePersonName")
                .email("testSavePersonEmail@gmail.com")
                .password("testSavePersonPassword")
                .build());
        assertEquals("testSavePersonEmail@gmail.com",
            personRepository.findByUserId("testSavePersonUserId").get().getEmail());
        assertEquals(Optional.empty(), personRepository.findByUserId("invalidUserId"));
    }

    @Test
    public void testFindAllPerson() {
        assertEquals(1, personRepository.findAll().size());
        assertEquals("unitTestUser", personRepository.findAll().get(0).getUserId());

    }

    @Test
    public void testFindPersonById() {
        UUID uid = personRepository.findByUserId("unitTestUser").get().getUid();
        assertEquals("unitTestUser", personRepository.findById(uid).get().getUserId());

    }

    @Test
    public void testFindPersonByUserId() {
        String validUserId = "unitTestUser";
        String invalidUserId = "invalidUnitTestUser";

        assertEquals("testPerson@gmail.com", personRepository.findByUserId(validUserId).get().getEmail());
        assertEquals(Optional.empty(), personRepository.findByUserId(invalidUserId));
    }

    @Test
    public void testFindPersonByEmail() {
        String validEmail = "testPerson@gmail.com";
        String invalidEmail = "invalidTestPerson@gmail.com";

        assertEquals("unitTestUser", personRepository.findByEmail(validEmail).get().getUserId());
        assertEquals(Optional.empty(), personRepository.findByEmail(invalidEmail));

    }

    @Test
    public void testDeletePerson() {
        UUID uid = personRepository.findByUserId("unitTestUser").get().getUid();
        personRepository.deleteById(uid);
        assertEquals(Optional.empty(), personRepository.findByUserId("unitTestUser"));
    }
}
