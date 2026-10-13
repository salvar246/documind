package com.documind.dto;

import jakarta.validation.constraints.NotBlank;

public record AskRequest(
        @NotBlank(message = "documentId is required") String documentId,
        @NotBlank(message = "Question must not be empty") String question
) {
}
