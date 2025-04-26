package com.project.ooad.skill_extract_job_match.controller;

import com.project.ooad.skill_extract_job_match.service.JobMatchingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job-match")
public class JobMatchingController {
    
    private final JobMatchingService jobMatchingService;

    public JobMatchingController(JobMatchingService jobMatchingService) {
        this.jobMatchingService = jobMatchingService;
    }

    @GetMapping("/{jobTitle}")
    public List<String> getMatchingJobs(@PathVariable String jobTitle) {
        return jobMatchingService.findMatchingJobs(jobTitle);
    }

    @PostMapping("/resume/skills/match")
    public List<String> matchResumeSkills(@RequestBody List<String> skills) {
        return jobMatchingService.matchResumeSkillsWithJobs(skills);
    }
    
    @PostMapping("/rank-jobs")
    public List<Map<String, Object>> rankJobs(@RequestBody List<String> resumeSkills) {
        return jobMatchingService.rankJobsBySkillMatch(resumeSkills);
    }
    
    @GetMapping("/best-job/{name}")
    public String getBestJobForUser(@PathVariable String name) {
        return jobMatchingService.findBestJob(name);
    }
}
