package com.project.ooad.skill_extract_job_match.service;

import com.project.ooad.skill_extract_job_match.model.Skill;
import com.project.ooad.skill_extract_job_match.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SkillAnalyzer {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillAnalyzer(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> analyzeSkills(String text) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        
        // Get all skills from the database
        List<String> knownSkills = skillRepository.findAll().stream()
                .map(Skill::getName)
                .collect(Collectors.toList());
        
        // Count occurrences of each skill in the text
        for (String skill : knownSkills) {
            int count = countOccurrences(text.toLowerCase(), skill.toLowerCase());
            if (count > 0) {
                frequencyMap.put(skill, count);
            }
        }

        List<Skill> result = new ArrayList<>();
        frequencyMap.forEach((skill, freq) -> result.add(new Skill(skill, freq)));
        return result;
    }

    private int countOccurrences(String text, String keyword) {
        return text.split("(?i)" + keyword).length - 1;
    }
}
