package com.project.ooad.resume_builder.service;

import org.springframework.stereotype.Service;

@Service
public class TemplateService {
    public String getDefaultTemplate() {
        return "Default template";
    }
}
