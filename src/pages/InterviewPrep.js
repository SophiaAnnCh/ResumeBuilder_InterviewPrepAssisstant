import React, { useState, useEffect } from 'react';
import { 
  Container, Typography, Box, Button, Paper, TextField, 
  Card, CardContent, Divider, CircularProgress, Alert,
  Chip, Stack, Grid
} from '@mui/material';
import QuizIcon from '@mui/icons-material/Quiz';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ErrorIcon from '@mui/icons-material/Error';
import axios from 'axios';

const InterviewPrep = () => {
  const [currentQuestion, setCurrentQuestion] = useState(null);
  const [userAnswer, setUserAnswer] = useState('');
  const [feedback, setFeedback] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [answeredQuestions, setAnsweredQuestions] = useState([]);
  const [selectedTag, setSelectedTag] = useState(null);

  const tags = [
    "Java", "Python", "JavaScript", "Data Structures", "Algorithms",
    "System Design", "Behavioral", "SQL", "General"
  ];

  useEffect(() => {
    // Load a question when component mounts
    getRandomQuestion();
  }, [selectedTag]);

  const getRandomQuestion = async () => {
    setIsLoading(true);
    setError(null);
    setFeedback(null);
    
    try {
      // Get random question from backend
      const endpoint = selectedTag 
        ? `/api/interview/question?tag=${selectedTag}` 
        : '/api/interview/question';
        
      const response = await axios.get(endpoint);
      setCurrentQuestion(response.data);
      setUserAnswer('');
    } catch (err) {
      console.error('Error fetching question:', err);
      setError('Failed to load question. Please try again.');
      setCurrentQuestion(null);
    } finally {
      setIsLoading(false);
    }
  };

  const evaluateAnswer = async () => {
    if (!userAnswer.trim()) {
      setError('Please provide an answer');
      return;
    }
    
    setIsLoading(true);
    setError(null);
    
    try {
      const response = await axios.post('/api/interview/evaluate', {
        questionId: currentQuestion.id,
        answer: userAnswer
      });
      
      const result = response.data;
      setFeedback(result);
      
      // Add to answered questions list
      setAnsweredQuestions(prev => [
        {
          question: currentQuestion.question,
          answer: userAnswer,
          correct: result.correct,
          feedback: result.feedback
        },
        ...prev
      ]);
    } catch (err) {
      console.error('Error evaluating answer:', err);
      setError('Failed to evaluate your answer. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleTagSelect = (tag) => {
    setSelectedTag(tag === selectedTag ? null : tag);
  };

  return (
    <Container maxWidth="lg" sx={{ py: 6 }}>
      <Typography variant="h4" component="h1" align="center" gutterBottom>
        Interview Preparation
      </Typography>
      
      <Paper sx={{ p: 3, mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          Filter Questions by Topic
        </Typography>
        
        <Stack direction="row" spacing={1} sx={{ flexWrap: 'wrap', gap: 1 }}>
          {tags.map((tag) => (
            <Chip
              key={tag}
              label={tag}
              onClick={() => handleTagSelect(tag)}
              color={selectedTag === tag ? "primary" : "default"}
              variant={selectedTag === tag ? "filled" : "outlined"}
              clickable
            />
          ))}
        </Stack>
      </Paper>
      
      <Grid container spacing={4}>
        <Grid item xs={12} md={7}>
          <Paper sx={{ p: 3, minHeight: '400px' }}>
            <Box 
              sx={{ 
                display: 'flex', 
                justifyContent: 'space-between', 
                alignItems: 'center',
                mb: 3
              }}
            >
              <Typography variant="h5">
                Practice Question
              </Typography>
              
              <Button 
                variant="outlined" 
                startIcon={<QuizIcon />} 
                onClick={getRandomQuestion}
                disabled={isLoading}
              >
                Next Question
              </Button>
            </Box>
            
            {isLoading ? (
              <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
                <CircularProgress />
              </Box>
            ) : error ? (
              <Alert severity="error" sx={{ my: 2 }}>{error}</Alert>
            ) : currentQuestion ? (
              <>
                <Card variant="outlined" sx={{ mb: 3 }}>
                  <CardContent>
                    <Typography variant="body1">
                      {currentQuestion.question}
                    </Typography>
                    
                    {currentQuestion.tags && (
                      <Box sx={{ mt: 2 }}>
                        {currentQuestion.tags.map((tag, index) => (
                          <Chip 
                            key={index} 
                            label={tag} 
                            size="small" 
                            sx={{ mr: 1, mt: 1 }} 
                          />
                        ))}
                      </Box>
                    )}
                  </CardContent>
                </Card>
                
                <TextField
                  fullWidth
                  label="Your Answer"
                  multiline
                  rows={6}
                  value={userAnswer}
                  onChange={(e) => setUserAnswer(e.target.value)}
                  variant="outlined"
                  placeholder="Type your answer here..."
                  sx={{ mb: 3 }}
                />
                
                <Button
                  variant="contained"
                  onClick={evaluateAnswer}
                  disabled={isLoading || !userAnswer.trim()}
                  fullWidth
                >
                  Submit Answer
                </Button>
                
                {feedback && (
                  <Card 
                    sx={{ 
                      mt: 3, 
                      borderLeft: `4px solid ${feedback.correct ? '#4caf50' : '#f44336'}` 
                    }}
                  >
                    <CardContent>
                      <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                        {feedback.correct ? (
                          <CheckCircleIcon color="success" sx={{ mr: 1 }} />
                        ) : (
                          <ErrorIcon color="error" sx={{ mr: 1 }} />
                        )}
                        <Typography variant="h6">
                          {feedback.correct ? 'Correct!' : 'Needs Improvement'}
                        </Typography>
                      </Box>
                      
                      <Typography variant="body2">
                        {feedback.feedback}
                      </Typography>
                      
                      {!feedback.correct && currentQuestion.expectedAnswer && (
                        <Box sx={{ mt: 2 }}>
                          <Typography variant="subtitle2">
                            Sample Answer:
                          </Typography>
                          <Typography variant="body2">
                            {currentQuestion.expectedAnswer}
                          </Typography>
                        </Box>
                      )}
                    </CardContent>
                  </Card>
                )}
              </>
            ) : (
              <Box textAlign="center" py={6}>
                <Typography>
                  Click "Next Question" to start practicing
                </Typography>
              </Box>
            )}
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={5}>
          <Paper sx={{ p: 3, minHeight: '400px' }}>
            <Typography variant="h5" gutterBottom>
              Your Progress
            </Typography>
            
            {answeredQuestions.length === 0 ? (
              <Box 
                sx={{ 
                  display: 'flex', 
                  flexDirection: 'column', 
                  alignItems: 'center', 
                  justifyContent: 'center',
                  height: '300px'
                }}
              >
                <QuizIcon sx={{ fontSize: 64, color: 'text.disabled', mb: 2 }} />
                <Typography color="text.secondary">
                  Answer some questions to track your progress
                </Typography>
              </Box>
            ) : (
              <Box sx={{ mt: 2 }}>
                <Typography variant="subtitle2" gutterBottom>
                  Recently answered questions:
                </Typography>
                
                {answeredQuestions.map((item, index) => (
                  <React.Fragment key={index}>
                    {index > 0 && <Divider sx={{ my: 2 }} />}
                    <Box>
                      <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                        {item.correct ? (
                          <CheckCircleIcon color="success" fontSize="small" sx={{ mr: 1 }} />
                        ) : (
                          <ErrorIcon color="error" fontSize="small" sx={{ mr: 1 }} />
                        )}
                        <Typography variant="subtitle2" noWrap>
                          Question {answeredQuestions.length - index}
                        </Typography>
                      </Box>
                      
                      <Typography variant="body2" sx={{ fontWeight: 'medium' }} noWrap>
                        {item.question}
                      </Typography>
                    </Box>
                  </React.Fragment>
                ))}
              </Box>
            )}
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default InterviewPrep;