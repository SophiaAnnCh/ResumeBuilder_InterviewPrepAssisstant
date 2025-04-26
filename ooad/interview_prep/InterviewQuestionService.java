package com.project.ooad.interview_prep;


import edu.stanford.nlp.pipeline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

@Service
public class InterviewQuestionService {

    @Autowired
    private InterviewQuestionRepository repository;

    private final StanfordCoreNLP pipeline;

    private static final Logger logger = LoggerFactory.getLogger(InterviewQuestionService.class);

    public InterviewQuestionService() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        this.pipeline = new StanfordCoreNLP(props);
    }

    @PostConstruct
    @Transactional
    public void initializeData() {
        // Only initialize if the database is empty
        if (repository.count() == 0) {
            logger.info("Initializing interview questions database...");
            
            // Java questions
            createQuestion(
                "What is the difference between an interface and an abstract class in Java?",
                "An interface only allows abstract method declarations (Java 8+ also allows default and static methods) and constants, while an abstract class can have method implementations, constructors, instance variables, and more. A class can implement multiple interfaces but extend only one abstract class. Interfaces are used for defining a contract, while abstract classes are for providing a base implementation.",
                "Java"
            );
            
            createQuestion(
                "Explain Java memory management and garbage collection.",
                "Java memory is divided into heap (objects), stack (method calls and local variables), and method area (class metadata). Garbage collection automatically reclaims memory by removing objects with no references. Java uses different GC algorithms like Serial, Parallel, CMS, and G1, each with different performance characteristics. Developers can suggest garbage collection with System.gc() but cannot force it.",
                "Java"
            );
            
            // Python questions
            createQuestion(
                "What are decorators in Python and how do they work?",
                "Decorators are functions that modify the behavior of other functions or methods. They use the @decorator syntax and allow for code reuse and separation of concerns. Decorators take a function as input, define a wrapper function that adds functionality, and return the wrapper. Common uses include logging, timing, access control, and validation.",
                "Python"
            );
            
            // JavaScript questions
            createQuestion(
                "Explain closures in JavaScript.",
                "A closure is a function that has access to variables in its outer (enclosing) lexical scope, even after the outer function has returned. It 'closes over' the variables, preserving them. Closures are useful for data encapsulation, creating private variables, and maintaining state in asynchronous operations.",
                "JavaScript"
            );
            
            // Data Structures questions
            createQuestion(
                "What is the difference between a LinkedList and an ArrayList?",
                "LinkedList stores elements in nodes with references to next/previous nodes, while ArrayList stores elements in a resizable array. LinkedList excels at insertions/deletions (O(1) if position is known), while ArrayList has better random access (O(1)). LinkedList has higher memory overhead due to storing references. ArrayList may need to resize and copy elements, but offers better locality of reference and cache performance.",
                "Data Structures"
            );
            
            // Algorithms questions
            createQuestion(
                "Explain the time and space complexity of QuickSort algorithm.",
                "QuickSort has an average time complexity of O(n log n), but worst-case O(n²) when poorly pivoted. Its space complexity is O(log n) for the recursive call stack. QuickSort works by selecting a pivot element, partitioning the array around the pivot, and recursively sorting the sub-arrays. It's efficient for large datasets and has good cache locality, but isn't stable and can degrade with poor pivot selection.",
                "Algorithms"
            );
            
            // System Design questions
            createQuestion(
                "How would you design a URL shortening service like TinyURL?",
                "A URL shortening service requires: 1) Data storage for mapping short URLs to long ones (database), 2) URL shortening algorithm (hash function or counter-based encoding to base62), 3) Redirect service to send users to original URLs, 4) Analytics tracking (optional). Key challenges include handling high read volume (use caching), ensuring uniqueness of short URLs, and preventing abuse. For scaling, consider database sharding, caching layers, and load balancing.",
                "System Design"
            );
            
            // Behavioral questions
            createQuestion(
                "Describe a challenging project you worked on and how you overcame obstacles.",
                "A strong answer would include: 1) Brief project description with clear challenges, 2) Specific obstacles you personally faced, 3) Actions you took to address them (with emphasis on your unique contribution), 4) Results achieved (quantifiable if possible), and 5) Lessons learned. Focus on demonstrating problem-solving, resilience, communication skills, and technical competence.",
                "Behavioral"
            );
            
            // SQL questions
            createQuestion(
                "Explain the difference between INNER JOIN and LEFT JOIN in SQL.",
                "INNER JOIN returns only the matching rows from both tables based on the join condition. LEFT JOIN returns all rows from the left table and matched rows from the right table (with NULL values for non-matches). INNER JOIN is more restrictive and filters out records, while LEFT JOIN preserves all records from the left table regardless of matches in the right table.",
                "SQL"
            );
            
            // General questions
            createQuestion(
                "What is RESTful API architecture and its key principles?",
                "REST (Representational State Transfer) is an architectural style for designing networked applications. Key principles include: 1) Statelessness - server doesn't store client state between requests, 2) Client-server separation - interfaces are decoupled, 3) Cacheability - responses explicitly define themselves as cacheable or non-cacheable, 4) Uniform interface - resources are identified by URIs, manipulated through representations, self-descriptive messages, and HATEOAS, 5) Layered system - client can't tell what layer it's connected to, 6) Resources are accessed using standard HTTP methods (GET, POST, PUT, DELETE).",
                "General"
            );
            
            logger.info("Interview questions initialized successfully.");
        }
    }

    private void createQuestion(String question, String expectedAnswer, String tag) {
        InterviewQuestion q = new InterviewQuestion();
        q.setQuestion(question);
        q.setExpectedAnswer(expectedAnswer);
        q.setTags(tag); // Assuming we have a setter for tags
        repository.save(q);
    }

    public Optional<InterviewQuestion> getRandomQuestion() {
        List<InterviewQuestion> questions = repository.findAll();
        if (questions.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        return Optional.of(questions.get(random.nextInt(questions.size())));
    }

    public boolean evaluateAnswer(String userAnswer, String expectedAnswer) {
        // Define stop words to ignore
        Set<String> stopWords = new HashSet<>(Arrays.asList("and", "or", "the", "a", "an", "is", "are", "was", "were", "in", "on", "at", "of", "to", "for", "with", "by", "as", "from"));

        // Log the raw user and expected answers
        logger.info("Raw user answer: {}", userAnswer);
        logger.info("Raw expected answer: {}", expectedAnswer);

        // Lemmatize and filter stop words from user answer
        CoreDocument userDoc = new CoreDocument(userAnswer);
        pipeline.annotate(userDoc);
        String userLemmas = userDoc.tokens().stream()
                .map(token -> token.lemma().toLowerCase())
                .filter(lemma -> !stopWords.contains(lemma))
                .reduce("", (a, b) -> a + " " + b);

        // Lemmatize and filter stop words from expected answer
        CoreDocument expectedDoc = new CoreDocument(expectedAnswer);
        pipeline.annotate(expectedDoc);
        String expectedLemmas = expectedDoc.tokens().stream()
                .map(token -> token.lemma().toLowerCase())
                .filter(lemma -> !stopWords.contains(lemma))
                .reduce("", (a, b) -> a + " " + b);

        // Log the lemmatized answers for debugging
        logger.info("Lemmatized user answer: {}", userLemmas);
        logger.info("Lemmatized expected answer: {}", expectedLemmas);

        // Calculate semantic similarity using cosine similarity
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        Map<CharSequence, Integer> userVector = Arrays.asList(userLemmas.split(" ")).stream().collect(HashMap::new, (m, v) -> m.put(v, 1), Map::putAll);
        Map<CharSequence, Integer> expectedVector = Arrays.asList(expectedLemmas.split(" ")).stream().collect(HashMap::new, (m, v) -> m.put(v, 1), Map::putAll);

        // Log the vectors for debugging
        logger.info("User vector: {}", userVector);
        logger.info("Expected vector: {}", expectedVector);

        Double similarity = cosineSimilarity.cosineSimilarity(userVector, expectedVector);

        // Log the similarity score for debugging
        logger.info("Similarity score: {}", similarity);

        // Handle null or invalid similarity scores
        if (similarity == null || similarity < 0.95) {
            logger.info("Answer is incorrect. Similarity below threshold.");
            return false;
        }

        logger.info("Answer is correct. Similarity above threshold.");
        return true;
    }

    public Optional<InterviewQuestion> getQuestionById(Long id) {
        return repository.findById(id);
    }
    
}