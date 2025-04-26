import React from 'react';
import { AppBar, Toolbar, Typography, Button, Container, Box } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import WorkIcon from '@mui/icons-material/Work';

const Header = () => {
  const navigate = useNavigate();
  const location = useLocation();
  
  const isActive = (path) => {
    return location.pathname === path;
  };
  
  return (
    <AppBar position="static" elevation={0}>
      <Container maxWidth="lg">
        <Toolbar disableGutters>
          <Box display="flex" alignItems="center" sx={{ mr: 4 }}>
            <WorkIcon sx={{ mr: 1 }} />
            <Typography
              variant="h6"
              noWrap
              component="div"
              sx={{ fontWeight: 'bold', cursor: 'pointer' }}
              onClick={() => navigate('/')}
            >
              Career Compass
            </Typography>
          </Box>
          
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            <Button
              color="inherit"
              onClick={() => navigate('/resume-builder')}
              sx={{ 
                mx: 1, 
                borderBottom: isActive('/resume-builder') ? '2px solid white' : 'none',
                borderRadius: 0,
                paddingBottom: '4px'
              }}
            >
              Resume Builder
            </Button>
            
            <Button
              color="inherit"
              onClick={() => navigate('/job-matcher')}
              sx={{ 
                mx: 1, 
                borderBottom: isActive('/job-matcher') ? '2px solid white' : 'none',
                borderRadius: 0,
                paddingBottom: '4px'
              }}
            >
              Job Matcher
            </Button>
            
            <Button
              color="inherit"
              onClick={() => navigate('/interview-prep')}
              sx={{ 
                mx: 1, 
                borderBottom: isActive('/interview-prep') ? '2px solid white' : 'none',
                borderRadius: 0,
                paddingBottom: '4px'
              }}
            >
              Interview Prep
            </Button>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Header;