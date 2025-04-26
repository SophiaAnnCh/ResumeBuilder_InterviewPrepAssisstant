package com.project.ooad.resume_builder;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.project.ooad.resume_builder")
@EnableJpaRepositories("com.project.ooad.resume_builder")
public class ResumeFormattingServiceApplication {
    // Configuration for resume builder module
}
