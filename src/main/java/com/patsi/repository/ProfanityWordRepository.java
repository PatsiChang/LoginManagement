package com.patsi.repository;

import com.patsi.bean.ProfanityWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfanityWordRepository extends JpaRepository<ProfanityWords, String> {
    List<ProfanityWords> findAll();
}