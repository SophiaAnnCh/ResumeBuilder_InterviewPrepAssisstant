import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Header from './components/Header';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Profile from './pages/Profile';
import JobMatcher from './pages/JobMatcher';
import ResumeBuilder from './pages/ResumeBuilder';
import InterviewPrep from './pages/InterviewPrep';

// Creating a custom theme with primary and secondary colors
const theme = createTheme({
  palette: {
    primary: {
      main: '#2E3B55', // Deep blue
      light: '#4F5B75',
      dark: '#0F1B35',
    },
    secondary: {
      main: '#FF8E53', // Orange accent
      light: '#FFB183',
      dark: '#D06023',
    },
    background: {
      default: '#F5F7FA',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontWeight: 500,
    },
    h2: {
      fontWeight: 500,
    },
    h3: {
      fontWeight: 500,
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          textTransform: 'none',
          fontWeight: 'bold',
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 4px 20px rgba(0,0,0,0.08)',
        },
      },
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/job-matcher" element={<JobMatcher />} />
          <Route path="/resume-builder" element={<ResumeBuilder />} />
          <Route path="/interview-prep" element={<InterviewPrep />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;