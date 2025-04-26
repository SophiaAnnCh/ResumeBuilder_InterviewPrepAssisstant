package com.project.ooad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.ooad")
@EntityScan(basePackages = {
    "com.project.ooad.interview_prep",
    "com.project.ooad.resume_builder.model",
    "com.project.ooad.skill_extract_job_match.model",
    "com.project.ooad.auth.model"
})
@EnableJpaRepositories(basePackages = {
    "com.project.ooad.interview_prep",
    "com.project.ooad.resume_builder.repository",
    "com.project.ooad.skill_extract_job_match.repository",
    "com.project.ooad.auth.repository"
})
public class OoadApplication {
    public static void main(String[] args) {
        SpringApplication.run(OoadApplication.class, args);
    }
}
