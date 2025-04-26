import React, { useState } from 'react';
import { 
  Container, Typography, Box, TextField, Button, Grid, Paper, 
  Stepper, Step, StepLabel, Divider, Alert, Card, CardContent 
} from '@mui/material';
import axios from 'axios';

const ResumeBuilder = () => {
  const [activeStep, setActiveStep] = useState(0);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    address: '',
    objective: '',
    education: '',
    experience: '',
    skills: '',
  });
  const [resumeId, setResumeId] = useState(null);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const steps = ['Personal Information', 'Education & Experience', 'Skills & Objective', 'Preview'];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleNext = () => {
    setActiveStep(prevStep => prevStep + 1);
  };

  const handleBack = () => {
    setActiveStep(prevStep => prevStep - 1);
  };

  const handleSubmit = async () => {
    try {
      setError(null);
      const response = await axios.post('/api/resume/create', formData);
      setResumeId(response.data.id);
      setSuccess(true);
    } catch (err) {
      setError('Failed to create resume. Please try again.');
      console.error('Error creating resume:', err);
    }
  };

  const renderStepContent = (step) => {
    switch (step) {
      case 0:
        return (
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Full Name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Phone"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Address"
                name="address"
                value={formData.address}
                onChange={handleChange}
                multiline
                rows={2}
              />
            </Grid>
          </Grid>
        );
      case 1:
        return (
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Education"
                name="education"
                value={formData.education}
                onChange={handleChange}
                multiline
                rows={4}
                helperText="Enter your educational qualifications (degrees, institutions, years)"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Work Experience"
                name="experience"
                value={formData.experience}
                onChange={handleChange}
                multiline
                rows={6}
                helperText="Enter your work experience (positions, companies, responsibilities, years)"
              />
            </Grid>
          </Grid>
        );
      case 2:
        return (
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Career Objective"
                name="objective"
                value={formData.objective}
                onChange={handleChange}
                multiline
                rows={3}
                helperText="Write a short career objective or professional summary"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Skills"
                name="skills"
                value={formData.skills}
                onChange={handleChange}
                multiline
                rows={4}
                helperText="List your skills, separated by commas (e.g., Java, Spring Boot, SQL)"
              />
            </Grid>
          </Grid>
        );
      case 3:
        return (
          <Card variant="outlined">
            <CardContent>
              <Typography variant="h5" gutterBottom>
                {formData.name}
              </Typography>
              
              <Typography color="textSecondary" gutterBottom>
                {formData.email} • {formData.phone}
              </Typography>
              
              {formData.address && (
                <Typography color="textSecondary" paragraph>
                  {formData.address}
                </Typography>
              )}
              
              <Divider sx={{ my: 2 }} />
              
              {formData.objective && (
                <>
                  <Typography variant="h6" gutterBottom>
                    Career Objective
                  </Typography>
                  <Typography paragraph>
                    {formData.objective}
                  </Typography>
                </>
              )}
              
              {formData.education && (
                <>
                  <Typography variant="h6" gutterBottom>
                    Education
                  </Typography>
                  <Typography paragraph>
                    {formData.education}
                  </Typography>
                </>
              )}
              
              {formData.experience && (
                <>
                  <Typography variant="h6" gutterBottom>
                    Experience
                  </Typography>
                  <Typography paragraph>
                    {formData.experience}
                  </Typography>
                </>
              )}
              
              {formData.skills && (
                <>
                  <Typography variant="h6" gutterBottom>
                    Skills
                  </Typography>
                  <Typography paragraph>
                    {formData.skills}
                  </Typography>
                </>
              )}
            </CardContent>
          </Card>
        );
      default:
        return null;
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 6 }}>
      <Paper sx={{ p: 4 }}>
        <Typography variant="h4" component="h1" align="center" gutterBottom>
          Resume Builder
        </Typography>
        
        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>
        
        {success ? (
          <Box textAlign="center">
            <Alert severity="success" sx={{ mb: 3 }}>
              Resume created successfully! Your Resume ID is: {resumeId}
            </Alert>
            <Button 
              variant="outlined" 
              onClick={() => window.open(`/api/resume/${resumeId}/download-pdf`, '_blank')}
            >
              Download PDF
            </Button>
          </Box>
        ) : (
          <>
            {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}
            
            <Box sx={{ mb: 4 }}>
              {renderStepContent(activeStep)}
            </Box>
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
              <Button
                disabled={activeStep === 0}
                onClick={handleBack}
              >
                Back
              </Button>
              
              <Button
                variant="contained"
                color="primary"
                onClick={activeStep === steps.length - 1 ? handleSubmit : handleNext}
              >
                {activeStep === steps.length - 1 ? 'Create Resume' : 'Next'}
              </Button>
            </Box>
          </>
        )}
      </Paper>
    </Container>
  );
};

export default ResumeBuilder;