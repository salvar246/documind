package com.documind.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/** Metadata about an uploaded PDF. The PDF text itself is NOT stored here (it stays in memory). */
@Document(collection = "documents")
public record DocumentMeta(
        @Id String id,
        String fileName,
        long sizeBytes,
        int pageCount,
        int charCount,
        Instant uploadedAt
) {
}
