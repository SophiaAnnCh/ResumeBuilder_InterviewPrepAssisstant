package com.project.ooad.skill_extract_job_match;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.project.ooad.skill_extract_job_match")
@EnableJpaRepositories("com.project.ooad.skill_extract_job_match")
public class JobMatchingApplication {
    // Configuration for job matching module
}
