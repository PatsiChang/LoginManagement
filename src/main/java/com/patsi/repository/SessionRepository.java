package com.patsi.repository;

import com.patsi.bean.LogInSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<LogInSession, UUID> {
    LogInSession save(LogInSession logInSession);

    List<LogInSession> findAll();

    Optional<LogInSession> findBySessionToken(String token);

    Optional<LogInSession> findByCustomerId(UUID customerId);

    void deleteByCustomerId(UUID customerId);
    void deleteBySessionToken(String token);
}
