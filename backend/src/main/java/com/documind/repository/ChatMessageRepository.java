package com.documind.repository;

import com.documind.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByDocumentIdOrderByAskedAtAsc(String documentId);

    List<ChatMessage> findAllByOrderByAskedAtAsc();
}
