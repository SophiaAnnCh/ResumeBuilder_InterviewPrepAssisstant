package com.project.ooad.skill_extract_job_match.repository;

import com.project.ooad.skill_extract_job_match.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByTitle(String title);
    boolean existsByTitle(String title);
}