package com.project.ooad.ui;

import com.project.ooad.interview_prep.InterviewQuestion;
import com.project.ooad.interview_prep.InterviewQuestionService;
import com.project.ooad.resume_builder.model.Resume;
import com.project.ooad.resume_builder.service.ResumeService;
import com.project.ooad.skill_extract_job_match.service.JobMatchingService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Optional;

@Component
public class MainController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextArea resumePreview;
    @FXML private TextField candidateNameField;
    @FXML private ListView<String> matchingJobsList;
    @FXML private Label questionLabel;
    @FXML private TextArea answerField;
    @FXML private Label feedbackLabel;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private JobMatchingService jobMatchingService;

    @Autowired
    private InterviewQuestionService interviewQuestionService;

    private InterviewQuestion currentQuestion;

    @FXML
    protected void handleCreateResume() {
        Resume resume = new Resume();
        resume.setName(nameField.getText());
        resume.setEmail(emailField.getText());
        
        String result = resumeService.createResume(resume);
        resumePreview.setText("Resume created successfully!\n\nName: " + resume.getName() + 
                            "\nEmail: " + resume.getEmail());
    }

    @FXML
    protected void handleUploadResume() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Handle resume upload
            candidateNameField.setText(selectedFile.getName().replace(".pdf", ""));
        }
    }

    @FXML
    protected void handleFindJobs() {
        String candidateName = candidateNameField.getText();
        String bestJob = jobMatchingService.findBestJob(candidateName);
        matchingJobsList.getItems().clear();
        matchingJobsList.getItems().add(bestJob);
    }

    @FXML
    protected void handleNextQuestion() {
        Optional<InterviewQuestion> question = interviewQuestionService.getRandomQuestion();
        question.ifPresent(q -> {
            currentQuestion = q;
            questionLabel.setText(q.getQuestion());
            answerField.clear();
            feedbackLabel.setText("");
        });
    }

    @FXML
    protected void handleSubmitAnswer() {
        if (currentQuestion != null && !answerField.getText().isEmpty()) {
            boolean isCorrect = interviewQuestionService.evaluateAnswer(
                answerField.getText(), 
                currentQuestion.getExpectedAnswer()
            );
            
            feedbackLabel.setText(isCorrect ? 
                "Correct answer! Well done!" : 
                "Not quite right. Expected answer: " + currentQuestion.getExpectedAnswer());
        }
    }
}