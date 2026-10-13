package com.documind.service;

import com.documind.exception.BadRequestException;
import com.documind.exception.NotFoundException;
import com.documind.model.DocumentMeta;
import com.documind.repository.DocumentMetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentService {

    private final PdfTextService pdfTextService;
    private final DocumentMetaRepository documentRepository;

    /**
     * In-memory store: documentId -> extracted PDF text.
     * LIMITATION: this is lost when the server restarts and is not shared between servers.
     * A production version would store text/chunks in MongoDB or a vector database.
     */
    private final Map<String, String> textStore = new ConcurrentHashMap<>();

    public DocumentService(PdfTextService pdfTextService, DocumentMetaRepository documentRepository) {
        this.pdfTextService = pdfTextService;
        this.documentRepository = documentRepository;
    }

    public DocumentMeta upload(MultipartFile file) {
        validate(file);

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Could not read the uploaded file.", e);
        }

        // Real PDFs always start with "%PDF" - this stops people renaming other files to .pdf
        if (bytes.length < 4 || bytes[0] != '%' || bytes[1] != 'P' || bytes[2] != 'D' || bytes[3] != 'F') {
            throw new BadRequestException("The file is not a valid PDF.");
        }

        PdfTextService.ExtractedPdf extracted = pdfTextService.extract(bytes);

        DocumentMeta meta = new DocumentMeta(
                null,
                file.getOriginalFilename(),
                file.getSize(),
                extracted.pageCount(),
                extracted.text().length(),
                Instant.now());
        DocumentMeta saved = documentRepository.save(meta);

        textStore.put(saved.id(), extracted.text());
        return saved;
    }

    public List<DocumentMeta> listAll() {
        return documentRepository.findAllByOrderByUploadedAtDesc();
    }

    public DocumentMeta getMeta(String id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document not found."));
    }

    /** Returns the in-memory text, or throws a friendly error if the server was restarted. */
    public String getText(String id) {
        String text = textStore.get(id);
        if (text == null) {
            throw new NotFoundException(
                    "The text for this document is no longer in memory (server restarted?). Please upload it again.");
        }
        return text;
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please choose a PDF file to upload.");
        }
        String name = file.getOriginalFilename();
        if (name == null || !name.toLowerCase().endsWith(".pdf")) {
            throw new BadRequestException("Only .pdf files are allowed.");
        }
    }
}
