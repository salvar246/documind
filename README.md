# DocuMind AI - AI-Powered Document Assistant

Upload a text-based PDF, ask questions, and get answers grounded only in that document.

**Stack:** React + Vite + Axios | Java 21 + Spring Boot 3.4 | Spring AI 1.0 + OpenAI | MongoDB | Apache PDFBox 3 | Maven

## Folder structure
```
documind-ai/
  backend/   (Spring Boot: controller / service / repository / model / dto / exception)
  frontend/  (React + Vite)
```

## Prerequisites
- JDK 21, Maven 3.9+, Node.js 18+ (20+ recommended)
- MongoDB running on localhost:27017 (Docker: `docker run -d --name documind-mongo -p 27017:27017 mongo:7`)
- An OpenAI API key

## 1. Run the backend
```bash
cd backend
# macOS / Linux
export OPENAI_API_KEY=sk-your-key-here
# Windows PowerShell:  $env:OPENAI_API_KEY="sk-your-key-here"
mvn spring-boot:run
```
Optional env vars: `OPENAI_MODEL` (default gpt-4o-mini), `MONGODB_URI` (default mongodb://localhost:27017/documind).

## 2. Run the frontend
```bash
cd frontend
npm install
npm run dev
```
Open http://localhost:5173

## 3. Test the API with curl
```bash
curl -F "file=@sample.pdf" http://localhost:8080/api/documents/upload
curl http://localhost:8080/api/documents
curl -X POST http://localhost:8080/api/chat/ask -H "Content-Type: application/json" \
  -d '{"documentId":"<ID_FROM_UPLOAD>","question":"What is this document about?"}'
curl "http://localhost:8080/api/chat/history?documentId=<ID_FROM_UPLOAD>"
```

## Things to try
- Ask something in the PDF -> grounded answer.
- Ask something unrelated ("Who won the 2018 World Cup?") -> "I could not find this information in the document."
- Upload a .txt renamed to .pdf, an empty file, or a scanned-image PDF -> clear error message.

## Known limitations (MVP)
- PDF text is kept **in memory** (`ConcurrentHashMap`). After a backend restart, the document list and chat history remain (MongoDB) but you must re-upload the PDF to ask new questions.
- Only the first 60,000 characters (about 15k tokens) of a PDF are sent to the model (`documind.max-context-chars`).
- Text-based PDFs only (no OCR), no authentication, no embeddings / vector search.

## Next steps (to mention in an interview)
Store text chunks in MongoDB, add embeddings + vector search (RAG), streaming answers, user authentication, OCR for scanned PDFs.
