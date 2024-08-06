package com.patsi.repository;

import com.patsi.bean.Person;
;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    Person save(Person person);

    List<Person> findAll();

    Optional<Person> findById(UUID uid);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByUserId(String userId);

    void deleteById(UUID UID);
}
