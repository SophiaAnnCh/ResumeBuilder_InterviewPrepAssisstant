package com.project.ooad.skill_extract_job_match.controller;

import com.project.ooad.skill_extract_job_match.model.Skill;
import com.project.ooad.skill_extract_job_match.service.FileProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeUploadController {

    private final FileProcessor fileProcessor;

    public ResumeUploadController(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file,
                                          @RequestParam("name") String candidateName) {
        try {
            // Save uploaded file temporarily
            File tempFile = Files.createTempFile("resume_", ".pdf").toFile();
            file.transferTo(tempFile);

            // Extract and analyze skills
            List<Skill> skills = fileProcessor.processResume(tempFile);

            // Update skills in the database
            fileProcessor.updateResumeSkills(candidateName, skills);

            // Delete temp file
            tempFile.delete();

            return ResponseEntity.ok().body(skills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process resume: " + e.getMessage());
        }
    }
}

