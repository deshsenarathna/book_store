import axios from 'axios';

const API_URL = 'http://localhost:8080/api/books';

// Add request interceptor for debugging
axios.interceptors.request.use(
  (config) => {
    console.log('Making API request to:', config.url);
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Add response interceptor for debugging
axios.interceptors.response.use(
  (response) => {
    console.log('API response received:', response.status, response.data);
    return response;
  },
  (error) => {
    console.error('API error:', error.message);
    if (error.response) {
      console.error('Error status:', error.response.status);
      console.error('Error data:', error.response.data);
    } else if (error.request) {
      console.error('No response received:', error.request);
    }
    return Promise.reject(error);
  }
);

// GET all books
export const getBooks = () => axios.get(API_URL);

// ADD book
export const addBook = (book) => axios.post(API_URL, book);

// UPDATE book
export const updateBook = (id, book) => axios.put(`${API_URL}/${id}`, book);

// DELETE book
export const deleteBook = (id) => axios.delete(`${API_URL}/${id}`);

// GET single book (optional)
export const getBookById = (id) => axios.get(`${API_URL}/${id}`);
