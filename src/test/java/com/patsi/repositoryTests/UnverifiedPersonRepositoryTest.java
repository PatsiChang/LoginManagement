package com.patsi.repositoryTests;

import com.patsi.bean.UnverifiedPerson;
import com.patsi.repository.UnverifiedPersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;

public class UnverifiedPersonRepositoryTest extends CommonRepositoryTest{

    @Autowired
    private UnverifiedPersonRepository unverifiedPersonRepository;

    @BeforeEach
    void setupUnverifiedPersonRepositoryTest() {
        entityManager.persist(
            UnverifiedPerson.builder()
                .token("000000")
                .userId("unverifiedPersonUserIdForTest")
                .name("unverifiedPersonNameForTest")
                .email("unverifiedPersonUserEmail@gmail.com")
                .password("unverifiedPersonPwdForTest")
                .build()
        );
    }

    @Test
    public void testSaveUnverifiedPerson() {
        unverifiedPersonRepository.save(
            UnverifiedPerson.builder()
                .token("111111")
                .userId("unverifiedPersonUserIdForTestSave")
                .name("unverifiedPersonNameForTestSave")
                .email("unverifiedPersonUserEmailTestSave@gmail.com")
                .password("unverifiedPersonPwdForTestSave")
                .build()
        );
        assertTrue(unverifiedPersonRepository.findByToken("111111").isPresent());
    }

    @Test
    public void testFindAllUnverifiedPerson() {
        assertEquals(1, unverifiedPersonRepository.findAll().size());
        assertEquals("000000", unverifiedPersonRepository.findAll().get(0).getToken());
    }

    @Test
    public void testFindUnverifiedPersonByToken() {
        String validToken = "000000";
        String invalidToken = "111111";

        assertEquals("unverifiedPersonUserIdForTest",
            unverifiedPersonRepository.findByToken(validToken).get().getUserId());
        assertEquals(Optional.empty(),
            unverifiedPersonRepository.findByToken(invalidToken));
    }

    @Test
    public void testFindUnverifiedPersonByUserId() {
        String validUserId = "unverifiedPersonUserIdForTest";
        String invalidUserId = "unverifiedPersonInvalidUserIdForTest";

        assertEquals("unverifiedPersonUserIdForTest",
            unverifiedPersonRepository.findByUserId(validUserId).get().getUserId());
        assertEquals(Optional.empty(),
            unverifiedPersonRepository.findByToken(invalidUserId));
    }

    @Test
    public void testFindUnverifiedPersonByEmail() {
        String validEmail = "unverifiedPersonUserEmail@gmail.com";
        String invalidEmail = "unverifiedPersonUserInvalidEmail@gmail.com";

        assertEquals("unverifiedPersonUserIdForTest",
            unverifiedPersonRepository.findByEmail(validEmail).get().getUserId());
        assertEquals(Optional.empty(),
            unverifiedPersonRepository.findByEmail(invalidEmail));
    }

    @Test
    public void testDeleteUnverifiedPersonByToken() {
        unverifiedPersonRepository.deleteByToken("000000");
        assertFalse(unverifiedPersonRepository.findByToken("000000").isPresent());

    }


}
