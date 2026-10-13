import { useEffect, useRef, useState } from 'react';
import { askQuestion, fetchDocuments, fetchHistory, getErrorMessage, uploadDocument } from './api';
import DocumentList from './components/DocumentList';
import ChatBox from './components/ChatBox';

export default function App() {
  const [documents, setDocuments] = useState([]);
  const [selectedId, setSelectedId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [uploading, setUploading] = useState(false);
  const [asking, setAsking] = useState(false);
  const [error, setError] = useState('');
  const fileInputRef = useRef(null);

  // Load the document list once when the page opens.
  useEffect(() => {
    fetchDocuments()
      .then(setDocuments)
      .catch((err) => setError(getErrorMessage(err)));
  }, []);

  // Load chat history whenever a different document is selected.
  useEffect(() => {
    if (!selectedId) {
      setMessages([]);
      return;
    }
    fetchHistory(selectedId)
      .then(setMessages)
      .catch((err) => setError(getErrorMessage(err)));
  }, [selectedId]);

  async function handleFileChange(e) {
    const file = e.target.files[0];
    e.target.value = ''; // lets the user pick the same file again later
    if (!file) return;

    setError('');
    setUploading(true);
    try {
      const doc = await uploadDocument(file);
      setDocuments((prev) => [doc, ...prev]);
      setSelectedId(doc.id);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setUploading(false);
    }
  }

  async function handleSend(question) {
    setError('');
    setAsking(true);
    try {
      const reply = await askQuestion(selectedId, question);
      setMessages((prev) => [...prev, reply]);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setAsking(false);
    }
  }

  return (
    <div className="app">
      <header className="header">
        <h1>DocuMind AI</h1>
        <p>Upload a PDF and ask questions. Answers come only from your document.</p>
      </header>

      {error && (
        <div className="error" role="alert">
          {error}
          <button className="link" onClick={() => setError('')}>Dismiss</button>
        </div>
      )}

      <main className="layout">
        <aside className="panel">
          <h2>Documents</h2>
          <input
            ref={fileInputRef}
            type="file"
            accept="application/pdf,.pdf"
            onChange={handleFileChange}
            hidden
          />
          <button onClick={() => fileInputRef.current.click()} disabled={uploading}>
            {uploading ? 'Uploading…' : 'Upload PDF'}
          </button>
          <DocumentList documents={documents} selectedId={selectedId} onSelect={setSelectedId} />
        </aside>

        <section className="panel chat-panel">
          <h2>Chat</h2>
          <ChatBox
            messages={messages}
            loading={asking}
            disabled={!selectedId}
            onSend={handleSend}
          />
        </section>
      </main>
    </div>
  );
}
