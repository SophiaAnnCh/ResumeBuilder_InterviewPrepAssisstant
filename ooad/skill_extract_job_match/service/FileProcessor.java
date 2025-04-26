package com.project.ooad.skill_extract_job_match.service;

import com.project.ooad.skill_extract_job_match.model.Skill;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileProcessor {

    private final SkillExtractor extractor;
    private final SkillAnalyzer analyzer;
    private final JobMatchingService jobMatchingService;

    public FileProcessor(SkillExtractor extractor, SkillAnalyzer analyzer, JobMatchingService jobMatchingService) {
        this.extractor = extractor;
        this.analyzer = analyzer;
        this.jobMatchingService = jobMatchingService;
    }

    public List<Skill> processResume(File file) {
        try {
            String text = extractor.extractTextFromPDF(file);
            return analyzer.analyzeSkills(text);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process resume: " + e.getMessage(), e);
        }
    }

    public void updateResumeSkills(String candidateName, List<Skill> skills) {
        try {
            // Extract skill names
            List<String> skillNames = skills.stream()
                                        .map(Skill::getName)
                                        .collect(Collectors.toList());

            // Update candidate skills in database
            jobMatchingService.updateCandidateSkills(candidateName, skillNames);
            
        } catch (Exception e) {
            throw new RuntimeException("Error updating resume skills: " + e.getMessage(), e);
        }
    }
}
