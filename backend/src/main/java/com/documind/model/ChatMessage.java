package com.documind.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/** One question + answer pair. */
@Document(collection = "chat_messages")
public record ChatMessage(
        @Id String id,
        @Indexed String documentId,
        String question,
        String answer,
        Instant askedAt
) {
}
