package com.project.ooad.resume_builder.controller;

import com.project.ooad.resume_builder.model.Resume;
import com.project.ooad.resume_builder.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ResumeViewController {

    @Autowired
    private ResumeRepository resumeRepository;

    @GetMapping("/resume/view/{id}")
    public String viewResume(@PathVariable Long id, Model model) {
        Resume resume = resumeRepository.findById(id).orElseThrow();
        model.addAttribute("resume", resume);
        return "basic"; // basic.html in /templates/
    }
}
