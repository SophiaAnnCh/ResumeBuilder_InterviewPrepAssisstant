import React, { useState } from 'react';
import { 
  Container, Typography, Box, Button, Grid, Paper, 
  TextField, Card, CardContent, Divider, List, 
  ListItem, ListItemText, LinearProgress, Alert
} from '@mui/material';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import axios from 'axios';

const JobMatcher = () => {
  const [candidateName, setCandidateName] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);
  const [fileName, setFileName] = useState('');
  const [extractedSkills, setExtractedSkills] = useState([]);
  const [matchingJobs, setMatchingJobs] = useState([]);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadError, setUploadError] = useState(null);
  const [jobsLoading, setJobsLoading] = useState(false);

  const handleFileSelect = (event) => {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      setSelectedFile(file);
      setFileName(file.name);
      setUploadError(null);
      
      // Auto-fill candidate name from file name (remove .pdf extension)
      const nameFromFile = file.name.replace('.pdf', '').replace(/_/g, ' ');
      setCandidateName(nameFromFile);
    } else {
      setUploadError('Please select a PDF file');
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setUploadError('Please select a file to upload');
      return;
    }

    if (!candidateName) {
      setUploadError('Please enter a candidate name');
      return;
    }

    setIsUploading(true);
    setUploadError(null);

    const formData = new FormData();
    formData.append('file', selectedFile);
    formData.append('name', candidateName);

    try {
      const response = await axios.post('/resume/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      setExtractedSkills(response.data);
      setIsUploading(false);
    } catch (error) {
      console.error('Error uploading resume:', error);
      setUploadError('Failed to upload resume. Please try again.');
      setIsUploading(false);
    }
  };

  const handleFindJobs = async () => {
    if (extractedSkills.length === 0) {
      setUploadError('Please upload a resume first');
      return;
    }

    setJobsLoading(true);

    try {
      // Get skill names from skill objects
      const skillNames = extractedSkills.map(skill => skill.name);
      
      // Get ranked jobs based on skills
      const response = await axios.post('/job-match/rank-jobs', skillNames);
      setMatchingJobs(response.data);
      setJobsLoading(false);
    } catch (error) {
      console.error('Error finding matching jobs:', error);
      setUploadError('Failed to find matching jobs. Please try again.');
      setJobsLoading(false);
    }
  };

  const handleBestJob = async () => {
    if (!candidateName) {
      setUploadError('Please enter a candidate name');
      return;
    }

    setJobsLoading(true);

    try {
      const response = await axios.get(`/job-match/best-job/${candidateName}`);
      const bestJobText = response.data;
      
      // Parse the best job text response
      setMatchingJobs([{ 
        job: bestJobText,
        percentMatch: 100  // Default since the backend already found the best match
      }]);
      
      setJobsLoading(false);
    } catch (error) {
      console.error('Error finding best job:', error);
      setUploadError('Failed to find best job. Please try again.');
      setJobsLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 6 }}>
      <Typography variant="h4" component="h1" align="center" gutterBottom>
        Job Matcher
      </Typography>
      
      <Grid container spacing={4}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3, height: '100%' }}>
            <Typography variant="h6" gutterBottom>
              Upload Resume
            </Typography>
            
            <Box sx={{ mb: 3 }}>
              <TextField
                fullWidth
                label="Candidate Name"
                value={candidateName}
                onChange={(e) => setCandidateName(e.target.value)}
                margin="normal"
              />
            </Box>
            
            <Box 
              sx={{ 
                border: '2px dashed #ccc', 
                borderRadius: 2, 
                p: 3, 
                textAlign: 'center',
                mb: 3,
                cursor: 'pointer',
                '&:hover': {
                  borderColor: 'primary.main',
                  backgroundColor: 'rgba(0, 0, 0, 0.02)'
                }
              }}
              onClick={() => document.getElementById('resume-upload').click()}
            >
              <input
                type="file"
                id="resume-upload"
                accept="application/pdf"
                style={{ display: 'none' }}
                onChange={handleFileSelect}
              />
              <CloudUploadIcon fontSize="large" color="primary" />
              <Typography variant="body1" sx={{ mt: 1 }}>
                {fileName || 'Click to select a PDF resume'}
              </Typography>
            </Box>
            
            {uploadError && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {uploadError}
              </Alert>
            )}
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
              <Button
                variant="contained"
                onClick={handleUpload}
                disabled={!selectedFile || isUploading}
              >
                {isUploading ? 'Uploading...' : 'Extract Skills'}
              </Button>
              
              <Button
                variant="outlined"
                onClick={handleBestJob}
                disabled={!candidateName || jobsLoading}
              >
                Find Best Match
              </Button>
            </Box>
            
            {isUploading && <LinearProgress sx={{ mt: 2 }} />}
            
            {extractedSkills.length > 0 && (
              <Box sx={{ mt: 3 }}>
                <Typography variant="subtitle1" gutterBottom>
                  Extracted Skills:
                </Typography>
                <List dense>
                  {extractedSkills.map((skill, index) => (
                    <ListItem key={index} disablePadding>
                      <ListItemText
                        primary={skill.name}
                        secondary={`Frequency: ${skill.frequency}`}
                      />
                    </ListItem>
                  ))}
                </List>
                
                <Box sx={{ mt: 2 }}>
                  <Button
                    variant="contained"
                    color="secondary"
                    onClick={handleFindJobs}
                    disabled={jobsLoading}
                    fullWidth
                  >
                    Find Matching Jobs
                  </Button>
                </Box>
              </Box>
            )}
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3, height: '100%' }}>
            <Typography variant="h6" gutterBottom>
              Matching Jobs
            </Typography>
            
            {jobsLoading ? (
              <Box textAlign="center" py={4}>
                <Typography variant="body1" gutterBottom>
                  Finding the best matches for you...
                </Typography>
                <LinearProgress />
              </Box>
            ) : matchingJobs.length > 0 ? (
              <List>
                {matchingJobs.map((jobMatch, index) => (
                  <React.Fragment key={index}>
                    {index > 0 && <Divider component="li" />}
                    <ListItem alignItems="flex-start">
                      <Card sx={{ width: '100%' }}>
                        <CardContent>
                          <Typography variant="h6" component="div">
                            {typeof jobMatch.job === 'string' 
                              ? jobMatch.job 
                              : jobMatch.job}
                          </Typography>
                          
                          {jobMatch.percentMatch && (
                            <Box sx={{ mt: 2 }}>
                              <Typography variant="body2" color="text.secondary">
                                Match: {jobMatch.percentMatch}%
                              </Typography>
                              <LinearProgress 
                                variant="determinate" 
                                value={jobMatch.percentMatch} 
                                color={jobMatch.percentMatch > 70 ? "success" : "primary"}
                                sx={{ mt: 1, height: 10, borderRadius: 5 }}
                              />
                            </Box>
                          )}
                          
                          {jobMatch.matchedSkills && jobMatch.totalSkills && (
                            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                              {jobMatch.matchedSkills} of {jobMatch.totalSkills} required skills matched
                            </Typography>
                          )}
                        </CardContent>
                      </Card>
                    </ListItem>
                  </React.Fragment>
                ))}
              </List>
            ) : (
              <Box sx={{ textAlign: 'center', py: 6 }}>
                <Typography variant="body1" color="text.secondary">
                  Upload your resume and find matching jobs based on your skills
                </Typography>
              </Box>
            )}
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default JobMatcher;