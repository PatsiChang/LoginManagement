package com.patsi.repository;

import com.patsi.bean.UnverifiedPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UnverifiedPersonRepository extends JpaRepository<UnverifiedPerson, String> {
    UnverifiedPerson save(UnverifiedPerson unverifiedPerson);

    List<UnverifiedPerson> findAll();

    Optional<UnverifiedPerson> findByToken(String token);

    Optional<UnverifiedPerson> findByUserId(String userId);

    Optional<UnverifiedPerson> findByEmail(String email);

    void deleteByToken(String token);

}
