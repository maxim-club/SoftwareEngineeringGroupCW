import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Test connection endpoint
export const testConnection = async () => {
  try {
    const response = await apiClient.get('/test');
    console.log('Backend connection successful:', response.data);
    return response.data;
  } catch (error) {
    console.error('Backend connection failed:', error);
    throw error;
  }
};

export default apiClient;