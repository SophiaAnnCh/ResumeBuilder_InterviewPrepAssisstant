import axios from 'axios';

// Create API service to handle all backend calls
const api = {
  // Resume Builder endpoints
  resume: {
    create: (resumeData) => axios.post('/api/resume/create', resumeData),
    download: (id) => axios.get(`/api/resume/${id}/download-pdf`),
  },
  
  // Job Matcher endpoints
  jobs: {
    uploadResume: (file, name) => {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('name', name);
      return axios.post('/resume/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
    },
    findMatchingJobs: (skillNames) => axios.post('/job-match/rank-jobs', skillNames),
    findBestJob: (candidateName) => axios.get(`/job-match/best-job/${candidateName}`),
    getAllCandidates: () => axios.get('/api/candidates'),
  },
  
  // Interview Prep endpoints
  interview: {
    getRandomQuestion: (tag) => {
      const endpoint = tag ? `/api/interview/question?tag=${tag}` : '/api/interview/question';
      return axios.get(endpoint);
    },
    evaluateAnswer: (questionId, answer) => 
      axios.post('/api/interview/evaluate', { questionId, answer }),
  }
};

export default api;