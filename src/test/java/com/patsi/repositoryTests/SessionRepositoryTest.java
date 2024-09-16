package com.patsi.repositoryTests;

import com.patsi.bean.LogInSession;
import com.patsi.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SessionRepositoryTest extends CommonRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    public void setUpSessionRepositoryTest() {
        entityManager.persist(
            LogInSession.builder()
                .customerId(randomUidForLoginTesting)
                .sessionToken("tokenForSessionRepositoryTest")
                .expiryTime(System.currentTimeMillis() + 50000L).build());
    }

    @Test
    public void testSaveSession() throws InterruptedException {
        sessionRepository.save(
            LogInSession.builder()
                .customerId(randomUidForLoginTesting)
                .sessionToken("sessionTokenForTestSaveSession")
                .expiryTime(System.currentTimeMillis() + 3000L)
                .build()
        );
        assertEquals(randomUidForLoginTesting,
            sessionRepository.findBySessionToken("sessionTokenForTestSaveSession").get().getCustomerId());
        Thread.sleep(3000);
        assertEquals(true,
            sessionRepository.findBySessionToken("sessionTokenForTestSaveSession").get().getExpiryTime()
                < System.currentTimeMillis());
    }

    @Test
    public void testFindAllSession() {
        assertEquals(1, sessionRepository.findAll().size());
        assertEquals(randomUidForLoginTesting, sessionRepository.findAll().get(0).getCustomerId());
    }

    @Test
    public void testFindSessionBySessionToken() {
        String validSessionToken = "tokenForSessionRepositoryTest";
        String invalidSessionToken = "invalidTokenForSessionRepositoryTest";

        assertEquals(randomUidForLoginTesting,
            sessionRepository.findBySessionToken(validSessionToken).get().getCustomerId());
        assertEquals(Optional.empty(), sessionRepository.findBySessionToken(invalidSessionToken));
    }

    @Test
    public void testFindSessionByCustomerId() {
        UUID validCustomerId = randomUidForLoginTesting;
        UUID invalidCustomerId = UUID.randomUUID();

        assertEquals(randomUidForLoginTesting,
            sessionRepository.findByCustomerId(validCustomerId).get().getCustomerId());
        assertEquals(Optional.empty(), sessionRepository.findByCustomerId(invalidCustomerId));
    }

    @Test
    public void testDeleteSessionByCustomerId() {
        sessionRepository.deleteByCustomerId(randomUidForLoginTesting);
        assertEquals(Optional.empty(), sessionRepository.findByCustomerId(randomUidForLoginTesting));
        assertEquals(Optional.empty(), sessionRepository.findBySessionToken("tokenForSessionRepositoryTest"));
    }

    @Test
    public void testDeleteSessionBySessionToken() {
        sessionRepository.deleteBySessionToken("tokenForSessionRepositoryTest");
        assertEquals(Optional.empty(), sessionRepository.findByCustomerId(randomUidForLoginTesting));
        assertEquals(Optional.empty(), sessionRepository.findBySessionToken("tokenForSessionRepositoryTest"));
    }



}
