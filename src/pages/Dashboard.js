import React from 'react';
import { Container, Typography, Grid, Paper, Box, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import DescriptionIcon from '@mui/icons-material/Description';
import WorkIcon from '@mui/icons-material/Work';
import QuizIcon from '@mui/icons-material/Quiz';

const Dashboard = () => {
  const navigate = useNavigate();

  const features = [
    {
      title: 'Resume Builder',
      description: 'Create professional resumes tailored to your career goals with our easy-to-use builder.',
      icon: <DescriptionIcon sx={{ fontSize: 64, color: 'primary.main' }} />,
      path: '/resume-builder'
    },
    {
      title: 'Job Matcher',
      description: 'Upload your resume and discover job opportunities that match your skills and experience.',
      icon: <WorkIcon sx={{ fontSize: 64, color: 'primary.main' }} />,
      path: '/job-matcher'
    },
    {
      title: 'Interview Prep',
      description: 'Prepare for your interviews with common questions and expert guidance.',
      icon: <QuizIcon sx={{ fontSize: 64, color: 'primary.main' }} />,
      path: '/interview-prep'
    }
  ];

  return (
    <Container maxWidth="lg" sx={{ py: 8 }}>
      <Box textAlign="center" mb={8}>
        <Typography variant="h3" component="h1" gutterBottom>
          Your Career Journey Starts Here
        </Typography>
        <Typography variant="h6" color="textSecondary" paragraph>
          A comprehensive platform to build your resume, find matching jobs, and prepare for interviews.
        </Typography>
      </Box>

      <Grid container spacing={4}>
        {features.map((feature, index) => (
          <Grid item xs={12} md={4} key={index}>
            <Paper 
              sx={{ 
                p: 4, 
                height: '100%', 
                display: 'flex', 
                flexDirection: 'column', 
                alignItems: 'center',
                transition: 'transform 0.3s, box-shadow 0.3s',
                '&:hover': {
                  transform: 'translateY(-8px)',
                  boxShadow: '0 12px 20px rgba(0,0,0,0.1)',
                }
              }}
            >
              <Box mb={2}>{feature.icon}</Box>
              <Typography variant="h5" component="h2" gutterBottom align="center">
                {feature.title}
              </Typography>
              <Typography align="center" paragraph>
                {feature.description}
              </Typography>
              <Box mt="auto" pt={2}>
                <Button 
                  variant="contained" 
                  color="primary" 
                  onClick={() => navigate(feature.path)}
                >
                  Get Started
                </Button>
              </Box>
            </Paper>
          </Grid>
        ))}
      </Grid>

      {/* Add Profile Button */}
      <Box textAlign="center" mt={4}>
        <Button 
          variant="outlined" 
          color="secondary" 
          onClick={() => navigate('/profile')}
        >
          Profile
        </Button>
      </Box>
    </Container>
  );
};

export default Dashboard;