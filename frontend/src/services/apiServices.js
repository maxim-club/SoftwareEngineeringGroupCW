// frontend/src/services/apiService.js

/**
 * API Service - Connects Frontend to Backend
 * 
 * This service makes HTTP requests to the Spring Boot backend
 * Backend runs on: http://localhost:8080
 * Frontend runs on: http://localhost:3000
 * 
 * Available Endpoints:
 * - GET /api/spaces - Get all spaces
 * - GET /api/spaces/{id} - Get space by ID
 * - GET /api/spaces/search?q={keyword} - Search spaces
 * - GET /api/spaces/filter/noise?level={QUIET} - Filter by noise
 * - GET /api/spaces/filter/occupancy?level={EMPTY} - Filter by occupancy
 * - GET /api/spaces/filter/features?computers=true - Filter by features
 */

import axios from 'axios';

// Backend API base URL
const API_BASE_URL = 'http://localhost:8080';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 second timeout
});

// Error handler
const handleError = (error, context) => {
  if (error.response) {
    // Server responded with error status
    console.error(`❌ API Error (${context}):`, error.response.status, error.response.data);
    throw new Error(`Server error: ${error.response.status}`);
  } else if (error.request) {
    // Request made but no response
    console.error(`❌ Network Error (${context}):`, 'No response from server');
    throw new Error('Cannot connect to server. Is the backend running?');
  } else {
    // Something else went wrong
    console.error(`❌ Error (${context}):`, error.message);
    throw error;
  }
};

// ============================================
// MAIN API FUNCTIONS
// ============================================

/**
 * Get all study spaces
 * @returns {Promise<Array>} Array of StudySpaceProfile objects
 */
export async function getAllSpaces() {
  try {
    console.log('📡 API: Fetching all spaces...');
    const response = await apiClient.get('/api/spaces');
    console.log('✅ API: Got', response.data.length, 'spaces');
    return response.data;
  } catch (error) {
    handleError(error, 'getAllSpaces');
  }
}

/**
 * Get space by ID
 * @param {string} id - Space ID
 * @returns {Promise<Object>} StudySpaceProfile object
 */
export async function getSpaceById(id) {
  try {
    console.log('📡 API: Fetching space with ID:', id);
    const response = await apiClient.get(`/api/spaces/${id}`);
    console.log('✅ API: Got space:', response.data.roomLocation);
    return response.data;
  } catch (error) {
    handleError(error, 'getSpaceById');
  }
}

/**
 * Search spaces by keyword (searches roomLocation and notes)
 * @param {string} query - Search keyword
 * @returns {Promise<Array>} Array of matching spaces
 */
export async function searchSpaces(query) {
  try {
    console.log('📡 API: Searching for:', query);
    const response = await apiClient.get('/api/spaces/search', {
      params: { q: query }
    });
    console.log('✅ API: Found', response.data.length, 'results');
    return response.data;
  } catch (error) {
    handleError(error, 'searchSpaces');
  }
}

/**
 * Filter spaces by noise level
 * @param {string} level - "QUIET" | "MODERATE" | "LOUD"
 * @returns {Promise<Array>} Array of matching spaces
 */
export async function filterByNoiseLevel(level) {
  try {
    console.log('📡 API: Filtering by noise level:', level);
    const response = await apiClient.get('/api/spaces/filter/noise', {
      params: { level: level }
    });
    console.log('✅ API: Found', response.data.length, 'spaces');
    return response.data;
  } catch (error) {
    handleError(error, 'filterByNoiseLevel');
  }
}

/**
 * Filter spaces by occupancy level
 * @param {string} level - "EMPTY" | "SPARSE" | "BUSY" | "FULL"
 * @returns {Promise<Array>} Array of matching spaces
 */
export async function filterByOccupancy(level) {
  try {
    console.log('📡 API: Filtering by occupancy:', level);
    const response = await apiClient.get('/api/spaces/filter/occupancy', {
      params: { level: level }
    });
    console.log('✅ API: Found', response.data.length, 'spaces');
    return response.data;
  } catch (error) {
    handleError(error, 'filterByOccupancy');
  }
}

/**
 * Filter spaces by features/amenities
 * @param {Object} features - { computers: boolean, groups: boolean }
 * @returns {Promise<Array>} Array of matching spaces
 */
export async function filterByFeatures(features = {}) {
  try {
    console.log('📡 API: Filtering by features:', features);
    const response = await apiClient.get('/api/spaces/filter/features', {
      params: features
    });
    console.log('✅ API: Found', response.data.length, 'spaces');
    return response.data;
  } catch (error) {
    handleError(error, 'filterByFeatures');
  }
}

/**
 * Get spaces suitable for groups
 * @returns {Promise<Array>} Array of group-friendly spaces
 */
export async function getGroupSpaces() {
  return filterByFeatures({ groups: true });
}

/**
 * Get spaces with computers
 * @returns {Promise<Array>} Array of spaces with computers
 */
export async function getComputerSpaces() {
  return filterByFeatures({ computers: true });
}

// Export default object with all functions
export default {
  getAllSpaces,
  getSpaceById,
  searchSpaces,
  filterByNoiseLevel,
  filterByOccupancy,
  filterByFeatures,
  getGroupSpaces,
  getComputerSpaces,
};