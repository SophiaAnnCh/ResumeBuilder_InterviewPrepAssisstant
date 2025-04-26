package com.project.ooad.resume_builder.service;


import org.springframework.stereotype.Service;

@Service
public class DocumentGenerator {
    public byte[] generateDocument(Object resume, String format) {
        return new byte[0];
    }

    public String generatePreview(Object resume) {
        return "<html><body>Preview</body></html>";
    }
}
