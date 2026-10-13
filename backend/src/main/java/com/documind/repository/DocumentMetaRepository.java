package com.documind.repository;

import com.documind.model.DocumentMeta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentMetaRepository extends MongoRepository<DocumentMeta, String> {
    List<DocumentMeta> findAllByOrderByUploadedAtDesc();
}
