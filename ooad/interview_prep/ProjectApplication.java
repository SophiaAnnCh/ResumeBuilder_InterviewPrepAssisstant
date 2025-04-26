package com.project.ooad.interview_prep;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.project.ooad.interview_prep")
@EnableJpaRepositories("com.project.ooad.interview_prep")
public class ProjectApplication {
    // Configuration for interview prep module
}

