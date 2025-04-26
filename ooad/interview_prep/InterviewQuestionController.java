package com.project.ooad.interview_prep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/interview")
public class InterviewQuestionController {

    @Autowired
    private InterviewQuestionService service;

    @GetMapping("/question")
    public ResponseEntity<InterviewQuestion> getRandomQuestion(@RequestParam(required = false) String tag) {
        Optional<InterviewQuestion> question = service.getRandomQuestion();
        return question.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Map<String, Object>> evaluateAnswer(@RequestBody Map<String, Object> request) {
        Long questionId = Long.valueOf(request.get("questionId").toString());
        String userAnswer = (String) request.get("answer");
        
        Optional<InterviewQuestion> question = service.getQuestionById(questionId);
        if (question.isPresent()) {
            boolean isCorrect = service.evaluateAnswer(userAnswer, question.get().getExpectedAnswer());
            Map<String, Object> response = new HashMap<>();
            response.put("correct", isCorrect);
            if (isCorrect) {
                response.put("feedback", "Great job! Your answer is correct.");
            } else {
                response.put("feedback", "Your answer needs improvement. Consider the following: " + 
                             question.get().getExpectedAnswer());
            }
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("correct", false);
        errorResponse.put("feedback", "Could not evaluate your answer. Question not found.");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}