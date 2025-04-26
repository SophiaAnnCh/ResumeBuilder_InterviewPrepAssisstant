package com.project.ooad.skill_extract_job_match.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.List;


public class FileReaderUtil {
    public static Map<String, List<String>> loadJsonFile(String fileName) {
        // Implementation to load JSON file and return a Map
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = FileReaderUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            return objectMapper.readValue(inputStream, new TypeReference<Map<String, List<String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }
    }
