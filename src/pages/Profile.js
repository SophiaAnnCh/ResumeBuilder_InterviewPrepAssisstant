import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Profile = () => {
  const navigate = useNavigate();
  const [userData, setUserData] = useState({
    username: '',
    password: '',
    role: '' // Added role to userData state
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);

  useEffect(() => {
    const username = localStorage.getItem('username');
    if (!username) {
      navigate('/');
      return;
    }

    fetchUserData(username);
  }, [navigate]);

  const fetchUserData = async (username) => {
    try {
      const response = await axios.get(`http://localhost:8080/profile/view?username=${username}`);
      setUserData({
        username: response.data.username,
        password: '',
        role: response.data.role // Store the role from the fetched data
      });
    } catch (err) {
      setError('Failed to load profile data');
    }
  };

  const handleChange = (e) => {
    setUserData({
      ...userData,
      [e.target.name]: e.target.value
    });
  };

  const handleUpdate = async () => {
    try {
      await axios.put(
        `http://localhost:8080/profile/update?username=${localStorage.getItem('username')}`,
        userData
      );
      setSuccess('Profile updated successfully');
      setIsEditing(false);
      localStorage.setItem('username', userData.username);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update profile');
    }
  };

  const handleDelete = async () => {
    try {
      await axios.delete(
        `http://localhost:8080/profile/delete?username=${localStorage.getItem('username')}`
      );
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete account');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    navigate('/');
  };

  return (
    <Container component="main" maxWidth="sm">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Paper elevation={3} sx={{ p: 4, width: '100%' }}>
          <Typography component="h1" variant="h5" align="center" gutterBottom>
            Profile
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          {success && (
            <Alert severity="success" sx={{ mb: 2 }}>
              {success}
            </Alert>
          )}

          <Box component="form" sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              label="Username"
              name="username"
              value={userData.username}
              onChange={handleChange}
              disabled={!isEditing}
            />
            {isEditing && (
              <TextField
                margin="normal"
                required
                fullWidth
                label="New Password"
                name="password"
                type="password"
                value={userData.password}
                onChange={handleChange}
              />
            )}

            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between' }}>
              {isEditing ? (
                <>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={handleUpdate}
                  >
                    Save Changes
                  </Button>
                  <Button
                    variant="outlined"
                    onClick={() => setIsEditing(false)}
                  >
                    Cancel
                  </Button>
                </>
              ) : (
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => setIsEditing(true)}
                >
                  Edit Profile
                </Button>
              )}
            </Box>

            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between' }}>
              <Button
                variant="contained"
                color="error"
                onClick={() => setShowDeleteDialog(true)}
              >
                Delete Account
              </Button>
              <Button
                variant="outlined"
                color="primary"
                onClick={handleLogout}
              >
                Logout
              </Button>
            </Box>
          </Box>
        </Paper>
      </Box>

      <Dialog
        open={showDeleteDialog}
        onClose={() => setShowDeleteDialog(false)}
      >
        <DialogTitle>Confirm Account Deletion</DialogTitle>
        <DialogContent>
          Are you sure you want to delete your account? This action cannot be undone.
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShowDeleteDialog(false)}>Cancel</Button>
          <Button onClick={handleDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default Profile;