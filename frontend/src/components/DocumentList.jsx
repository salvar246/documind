export default function DocumentList({ documents, selectedId, onSelect }) {
  if (documents.length === 0) {
    return <p className="muted">No documents yet. Upload a PDF to begin.</p>;
  }

  return (
    <ul className="doc-list">
      {documents.map((doc) => (
        <li
          key={doc.id}
          className={`doc-item ${doc.id === selectedId ? 'selected' : ''}`}
          onClick={() => onSelect(doc.id)}
        >
          <strong>{doc.fileName}</strong>
          <span className="muted">
            {doc.pageCount} page{doc.pageCount === 1 ? '' : 's'} · {(doc.sizeBytes / 1024).toFixed(0)} KB
          </span>
        </li>
      ))}
    </ul>
  );
}
