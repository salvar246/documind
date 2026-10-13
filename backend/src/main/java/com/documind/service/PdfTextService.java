package com.documind.service;

import com.documind.exception.BadRequestException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;

/** Uses Apache PDFBox to pull plain text out of a PDF. */
@Service
public class PdfTextService {

    public record ExtractedPdf(String text, int pageCount) {
    }

    public ExtractedPdf extract(byte[] pdfBytes) {
        try (PDDocument pdf = Loader.loadPDF(pdfBytes)) {
            String text = new PDFTextStripper().getText(pdf).trim();
            if (text.isEmpty()) {
                throw new BadRequestException(
                        "No text found in this PDF. It may be a scanned image - only text-based PDFs are supported.");
            }
            return new ExtractedPdf(text, pdf.getNumberOfPages());
        } catch (IOException e) {
            throw new BadRequestException(
                    "Could not read this PDF. It may be corrupted or password-protected.", e);
        }
    }
}
