package com.project.ooad.skill_extract_job_match.service;

import com.project.ooad.skill_extract_job_match.model.Candidate;
import com.project.ooad.skill_extract_job_match.model.Job;
import com.project.ooad.skill_extract_job_match.model.Skill;
import com.project.ooad.skill_extract_job_match.repository.CandidateRepository;
import com.project.ooad.skill_extract_job_match.repository.JobRepository;
import com.project.ooad.skill_extract_job_match.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobMatchingService {

    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public JobMatchingService(JobRepository jobRepository, 
                            CandidateRepository candidateRepository,
                            SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
    }

    @PostConstruct
    @Transactional
    public void initializeData() {
        // Only initialize if the database is empty
        if (jobRepository.count() == 0) {
            // Create skills
            Map<String, Skill> skillMap = new HashMap<>();
            for (String skillName : Arrays.asList(
                    // Programming Languages
                    "Java", "Python", "JavaScript", "C++", "C#", "Ruby", "Go", "PHP", "Swift",
                    // Web Technologies
                    "HTML", "CSS", "React", "Angular", "Vue.js", "Node.js", "Spring Boot", "Django",
                    // Databases
                    "SQL", "MongoDB", "PostgreSQL", "Redis", "Cassandra",
                    // Cloud & DevOps
                    "AWS", "Azure", "GCP", "Docker", "Kubernetes", "CI/CD", "Jenkins",
                    // Data Science & AI
                    "Machine Learning", "Deep Learning", "TensorFlow", "PyTorch", "Pandas", "NumPy",
                    // Other Skills
                    "REST API", "GraphQL", "Microservices", "Git", "Agile", "Scrum", "Linux"
            )) {
                Skill skill = new Skill(skillName, 1);
                skillMap.put(skillName, skillRepository.save(skill));
            }
            
            // Create jobs with required skills
            createJob("Senior Software Engineer", 
                Arrays.asList("Java", "Spring Boot", "SQL", "Microservices", "Docker", "REST API"), skillMap);
            createJob("Full Stack Developer", 
                Arrays.asList("JavaScript", "React", "Node.js", "MongoDB", "REST API", "Git"), skillMap);
            createJob("Data Scientist", 
                Arrays.asList("Python", "Machine Learning", "Pandas", "SQL", "TensorFlow", "NumPy"), skillMap);
            createJob("DevOps Engineer", 
                Arrays.asList("Docker", "Kubernetes", "CI/CD", "AWS", "Jenkins", "Linux"), skillMap);
            createJob("Frontend Developer", 
                Arrays.asList("HTML", "CSS", "JavaScript", "React", "Vue.js", "Git"), skillMap);
            createJob("Backend Developer", 
                Arrays.asList("Java", "Spring Boot", "PostgreSQL", "REST API", "Microservices"), skillMap);
            createJob("Cloud Architect", 
                Arrays.asList("AWS", "Azure", "GCP", "Kubernetes", "Microservices", "Docker"), skillMap);
            createJob("AI Engineer", 
                Arrays.asList("Python", "Deep Learning", "TensorFlow", "PyTorch", "Machine Learning"), skillMap);
            createJob("Database Administrator", 
                Arrays.asList("SQL", "PostgreSQL", "MongoDB", "Redis", "Cassandra"), skillMap);
            
            // Create sample candidates with diverse skill sets
            createCandidate("Ananya", 
                Arrays.asList("Java", "Spring Boot", "SQL", "React", "AWS", "Docker", "REST API"), skillMap);
            createCandidate("Bob", 
                Arrays.asList("Python", "Machine Learning", "Pandas", "NumPy", "SQL", "TensorFlow"), skillMap);
            createCandidate("Charlie", 
                Arrays.asList("Docker", "Kubernetes", "CI/CD", "AWS", "Jenkins", "Linux"), skillMap);
            createCandidate("Diana", 
                Arrays.asList("JavaScript", "React", "HTML", "CSS", "Node.js", "MongoDB"), skillMap);
            createCandidate("Eva", 
                Arrays.asList("Java", "Python", "AWS", "Docker", "Microservices", "PostgreSQL"), skillMap);
        }
    }
    
    private void createJob(String title, List<String> skillNames, Map<String, Skill> skillMap) {
        Job job = new Job(title);
        for (String skillName : skillNames) {
            job.addRequiredSkill(skillMap.get(skillName));
        }
        jobRepository.save(job);
    }
    
    private void createCandidate(String name, List<String> skillNames, Map<String, Skill> skillMap) {
        Candidate candidate = new Candidate(name);
        for (String skillName : skillNames) {
            if (skillMap.containsKey(skillName)) {
                candidate.addSkill(skillMap.get(skillName));
            }
        }
        candidateRepository.save(candidate);
    }

    public List<String> findMatchingJobs(String jobTitle) {
        Optional<Job> jobOpt = jobRepository.findByTitle(jobTitle);
        if (jobOpt.isEmpty()) {
            return List.of();
        }
        
        Job job = jobOpt.get();
        Set<Skill> requiredSkills = job.getRequiredSkills();
        
        List<Candidate> allCandidates = candidateRepository.findAll();
        
        return allCandidates.stream()
                .filter(candidate -> candidate.getSkills().containsAll(requiredSkills))
                .map(Candidate::getName)
                .collect(Collectors.toList());
    }

    public List<String> matchResumeSkillsWithJobs(List<String> skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return List.of();
        }
        
        // Get the skill objects for the provided skill names
        List<Skill> skills = skillNames.stream()
                .map(name -> skillRepository.findByName(name).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        Set<Skill> skillSet = new HashSet<>(skills);
        
        // Find all jobs that require only skills the candidate has
        return jobRepository.findAll().stream()
                .filter(job -> skillSet.containsAll(job.getRequiredSkills()))
                .map(Job::getTitle)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> rankJobsBySkillMatch(List<String> skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return List.of();
        }
        
        // Get the skill objects for the provided skill names
        Set<Skill> skills = skillNames.stream()
                .map(name -> skillRepository.findByName(name).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        return jobRepository.findAll().stream()
                .map(job -> {
                    String jobTitle = job.getTitle();
                    Set<Skill> requiredSkills = job.getRequiredSkills();
                    
                    // Count matched skills
                    long matchedCount = requiredSkills.stream()
                            .filter(skills::contains)
                            .count();
                    
                    // Calculate percentage match
                    double percentMatch = requiredSkills.isEmpty() ? 0 : 
                        ((double) matchedCount / requiredSkills.size()) * 100;
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("job", jobTitle);
                    result.put("matchedSkills", matchedCount);
                    result.put("totalSkills", requiredSkills.size());
                    result.put("percentMatch", Math.round(percentMatch));
                    
                    return result;
                })
                .sorted((a, b) -> Long.compare((Long) b.get("matchedSkills"), (Long) a.get("matchedSkills")))
                .collect(Collectors.toList());
    }

    public String findBestJob(String userName) {
        Optional<Candidate> candidateOpt = candidateRepository.findByName(userName);
        if (candidateOpt.isEmpty()) {
            return "No resume skills found for " + userName;
        }
        
        Set<Skill> userSkills = candidateOpt.get().getSkills();
        
        return jobRepository.findAll().stream()
                .map(job -> {
                    String jobTitle = job.getTitle();
                    Set<Skill> requiredSkills = job.getRequiredSkills();
                    int matchScore = calculateMatchScore(userSkills, requiredSkills);
                    double matchPercentage = requiredSkills.isEmpty() ? 0 : 
                        ((double) matchScore / requiredSkills.size()) * 100;
                    
                    return Map.entry(jobTitle, new MatchResult(matchScore, matchPercentage));
                })
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    MatchResult result = entry.getValue();
                    return entry.getKey() + " (" + result.getMatchScore() + " skills matched, " + 
                        Math.round(result.getMatchPercentage()) + "% match)";
                })
                .orElse("No suitable job found.");
    }

    private int calculateMatchScore(Set<Skill> userSkills, Set<Skill> requiredSkills) {
        int count = 0;
        for (Skill skill : requiredSkills) {
            if (userSkills.contains(skill)) {
                count++;
            }
        }
        return count;
    }
    
    @Transactional
    public void updateCandidateSkills(String candidateName, List<String> skillNames) {
        // Find or create candidate
        Candidate candidate = candidateRepository.findByName(candidateName)
                .orElse(new Candidate(candidateName));
        
        // Find or create skills
        Set<Skill> skills = new HashSet<>();
        for (String skillName : skillNames) {
            Skill skill = skillRepository.findByName(skillName)
                    .orElseGet(() -> skillRepository.save(new Skill(skillName, 1)));
            skills.add(skill);
        }
        
        // Update candidate's skills
        candidate.setSkills(skills);
        candidateRepository.save(candidate);
    }
    
    // Helper class for job matching results
    private static class MatchResult implements Comparable<MatchResult> {
        private final int matchScore;
        private final double matchPercentage;
        
        public MatchResult(int matchScore, double matchPercentage) {
            this.matchScore = matchScore;
            this.matchPercentage = matchPercentage;
        }
        
        public int getMatchScore() {
            return matchScore;
        }
        
        public double getMatchPercentage() {
            return matchPercentage;
        }
        
        @Override
        public int compareTo(MatchResult other) {
            // First compare by percentage
            int percentageCompare = Double.compare(this.matchPercentage, other.matchPercentage);
            if (percentageCompare != 0) {
                return percentageCompare;
            }
            // If percentage is the same, compare by absolute number of skills
            return Integer.compare(this.matchScore, other.matchScore);
        }
    }
}
