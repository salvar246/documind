import { useEffect, useRef, useState } from 'react';

export default function ChatBox({ messages, loading, disabled, onSend }) {
  const [question, setQuestion] = useState('');
  const bottomRef = useRef(null);

  // Scroll to the newest message whenever the chat changes.
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, loading]);

  function handleSubmit(e) {
    e.preventDefault();
    const trimmed = question.trim();
    if (!trimmed || loading || disabled) return;
    onSend(trimmed);
    setQuestion('');
  }

  return (
    <div className="chat">
      <div className="messages">
        {messages.length === 0 && !loading && (
          <p className="muted">
            {disabled ? 'Select a document to start chatting.' : 'Ask a question about this document.'}
          </p>
        )}

        {messages.map((m) => (
          <div key={m.id}>
            <div className="bubble user">{m.question}</div>
            <div className="bubble ai">{m.answer}</div>
          </div>
        ))}

        {loading && <div className="bubble ai loading">Thinking…</div>}
        <div ref={bottomRef} />
      </div>

      <form className="chat-form" onSubmit={handleSubmit}>
        <input
          type="text"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          placeholder="Ask something about the document…"
          disabled={disabled || loading}
        />
        <button type="submit" disabled={disabled || loading || !question.trim()}>
          Send
        </button>
      </form>
    </div>
  );
}
