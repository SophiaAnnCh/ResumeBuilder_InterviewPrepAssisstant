package com.project.ooad.resume_builder.repository;

import com.project.ooad.resume_builder.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    // Custom queries if needed
}
