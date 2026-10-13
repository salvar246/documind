package com.documind.controller;

import com.documind.model.DocumentMeta;
import com.documind.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentMeta upload(@RequestParam("file") MultipartFile file) {
        return documentService.upload(file);
    }

    @GetMapping
    public List<DocumentMeta> list() {
        return documentService.listAll();
    }
}
