package com.project.ooad.skill_extract_job_match.controller;

import com.project.ooad.skill_extract_job_match.model.Candidate;
import com.project.ooad.skill_extract_job_match.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateController(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Candidate candidate : candidates) {
            Map<String, Object> candidateMap = new HashMap<>();
            candidateMap.put("name", candidate.getName());
            candidateMap.put("skills", candidate.getSkills().stream()
                    .map(skill -> skill.getName())
                    .collect(Collectors.toList()));
            result.add(candidateMap);
        }
        
        return ResponseEntity.ok(result);
    }
}