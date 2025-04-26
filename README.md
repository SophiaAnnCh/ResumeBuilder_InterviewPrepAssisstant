ResumeBuilder_InterviewPrepAssistant is a backend-first project designed to help users generate professional resumes and prepare for interviews.
It leverages Spring Boot for the core backend and Stanford CoreNLP models for Natural Language Processing (NLP) tasks, offering an intelligent way to analyze, build, and enhance resume content — and even simulate interview questions!

This repo contains:

The core backend logic.

Necessary frontend files for UI (separate from the Spring Boot app).

Note: This is not a full-fledged Spring Boot app yet — users must create a Spring Boot project and integrate the core logic manually.

Setup Instructions
1. Create a Spring Boot Project
You will need to:

Set up a new Spring Boot application using Spring Initializr or your IDE.

Add the necessary dependencies:

Spring Web

Spring Boot DevTools (optional)


Any other dependencies for your project (e.g., JSON parsing).

2. Integrate the Core Logic
Place the provided core logic files into your project (usually into a package like com.example.resumebuilder or similar).

3. Create a lib Directory
In the same directory where your core logic is located, create a folder named lib.


4. Install Stanford CoreNLP Models
Download the Stanford CoreNLP models.

Extract the contents and place the model .jar files into your new lib directory.

You can alternatively automate model downloads using Maven/Gradle dependencies, but manual placement is suggested for simplicity in this setup.
