package com.documind.controller;

import com.documind.dto.AskRequest;
import com.documind.model.ChatMessage;
import com.documind.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ChatMessage ask(@Valid @RequestBody AskRequest request) {
        return chatService.ask(request);
    }

    /** Optional ?documentId=... filters history to one document. */
    @GetMapping("/history")
    public List<ChatMessage> history(@RequestParam(required = false) String documentId) {
        return chatService.history(documentId);
    }
}
