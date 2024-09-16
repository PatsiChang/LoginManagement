package com.patsi.repositoryTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
public class CommonRepositoryTest {
    @Autowired
    protected TestEntityManager entityManager;

    //Variables
    protected UUID randomUidForLoginTesting = UUID.randomUUID();

}
