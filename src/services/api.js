import axios from 'axios';

// Base URL for your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with proper headers
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
  timeout: 10000,
});

// Request interceptor to ensure JSON content type
apiClient.interceptors.request.use(
  (config) => {
    // Ensure we're sending JSON
    if (config.data && typeof config.data === 'object') {
      config.data = JSON.stringify(config.data);
    }
    config.headers['Content-Type'] = 'application/json';
    
    console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('❌ API Request Error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => {
    console.log(`✅ API Response: ${response.status} ${response.config.url}`);
    return response;
  },
  (error) => {
    console.error('❌ API Response Error:', error.response?.status, error.message);
    return Promise.reject(error);
  }
);

// Student API functions
export const studentAPI = {
  // Get all students
  getAllStudents: async () => {
    try {
      const response = await apiClient.get('/students');
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch students: ${error.message}`);
    }
  },

  // Add new student
  addStudent: async (studentData) => {
    try {
      const response = await apiClient.post('/students', studentData, {
        headers: {
          'Content-Type': 'application/json',
        }
      });
      return response.data;
    } catch (error) {
      throw new Error(`Failed to add student: ${error.message}`);
    }
  },

  // Update student
  updateStudent: async (studentId, studentData) => {
    try {
      const response = await apiClient.put(`/students/${studentId}`, studentData, {
        headers: {
          'Content-Type': 'application/json',
        }
      });
      return response.data;
    } catch (error) {
      throw new Error(`Failed to update student: ${error.message}`);
    }
  },

  // Delete student
  deleteStudent: async (studentId) => {
    try {
      const response = await apiClient.delete(`/students/${studentId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to delete student: ${error.message}`);
    }
  }
};

// Fee API functions
export const feeAPI = {
  // Get all fee records
  getAllFeeRecords: async () => {
    try {
      const response = await apiClient.get('/fees');
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch fee records: ${error.message}`);
    }
  },

  // Get due fees
  getDueFees: async () => {
    try {
      const response = await apiClient.get('/fees/due');
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch due fees: ${error.message}`);
    }
  },

  // Add fee record
  addFeeRecord: async (feeData) => {
    try {
      const response = await apiClient.post('/fees', feeData, {
        headers: {
          'Content-Type': 'application/json',
        }
      });
      return response.data;
    } catch (error) {
      throw new Error(`Failed to add fee record: ${error.message}`);
    }
  }
};

// Test API connection (only GET request)
export const testConnection = async () => {
  try {
    console.log('🔍 Testing API connection...');
    const response = await apiClient.get('/students');
    console.log('✅ API Response:', response);
    
    return { 
      success: true, 
      message: `Connected! Found ${response.data.length} students` 
    };
  } catch (error) {
    console.error('❌ API Error Details:', error);
    
    if (error.code === 'ECONNREFUSED') {
      return { success: false, message: 'Backend not running on port 8080' };
    }
    if (error.response?.status === 404) {
      return { success: false, message: 'API endpoint not found' };
    }
    if (error.response?.status === 415) {
      return { success: false, message: 'Unsupported Media Type - Fixed!' };
    }
    
    return { success: false, message: `Error: ${error.message}` };
  }
};

export default apiClient;
