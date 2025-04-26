package com.project.ooad.resume_builder.controller;

import com.project.ooad.resume_builder.model.Resume;
import com.project.ooad.resume_builder.repository.ResumeRepository;
import com.project.ooad.resume_builder.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class ResumeBuilderController {

    // Injecting the repository for database access
    @Autowired
    private ResumeRepository resumeRepository;

    // Injecting PdfService for PDF generation
    @Autowired
    private PdfService pdfService;

    // Endpoint to create a resume
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createResume(@RequestBody Resume resume) {
        Resume savedResume = resumeRepository.save(resume);
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedResume.getId());
        response.put("message", "Resume created successfully");
        return ResponseEntity.ok(response);
    }

    // Endpoint to get a resume by ID
    @GetMapping("/{id}")
    public Resume getResume(@PathVariable Long id) {
        return resumeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
    }

    // Endpoint to preview a resume (could be further developed)
    @GetMapping("/{id}/preview")
    public String previewResume(@PathVariable Long id) {
        return "Previewing resume with ID: " + id;
    }

    // Endpoint to export resume as PDF
    @GetMapping("/{id}/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        try {
            Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
            
            byte[] pdfBytes = pdfService.generatePdf(resume);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=resume.pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "PDF generation failed");
        }
    }
}
