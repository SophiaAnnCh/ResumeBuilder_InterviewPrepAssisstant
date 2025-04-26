package com.project.ooad.skill_extract_job_match.repository;

import com.project.ooad.skill_extract_job_match.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByName(String name);
    boolean existsByName(String name);
}