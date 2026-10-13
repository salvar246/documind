import axios from 'axios';

// All requests go to /api/... (Vite proxies them to http://localhost:8080)
const client = axios.create({
  baseURL: '/api',
  timeout: 90000, // AI answers can take a while
});

export async function uploadDocument(file) {
  const formData = new FormData();
  formData.append('file', file);
  const res = await client.post('/documents/upload', formData);
  return res.data;
}

export async function fetchDocuments() {
  const res = await client.get('/documents');
  return res.data;
}

export async function askQuestion(documentId, question) {
  const res = await client.post('/chat/ask', { documentId, question });
  return res.data;
}

export async function fetchHistory(documentId) {
  const res = await client.get('/chat/history', { params: { documentId } });
  return res.data;
}

// Pulls a readable message out of an Axios error.
export function getErrorMessage(err) {
  if (err.response?.data?.message) return err.response.data.message;
  if (err.code === 'ECONNABORTED') return 'The request timed out. Please try again.';
  if (err.request) return 'Cannot reach the server. Is the backend running?';
  return 'Something went wrong.';
}
