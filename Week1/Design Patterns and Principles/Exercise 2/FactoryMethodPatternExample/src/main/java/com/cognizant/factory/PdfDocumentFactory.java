package com.cognizant.factory;

import com.cognizant.factory.document.Document;
import com.cognizant.factory.document.PdfDocument;

/**
 * PdfDocumentFactory – Concrete Creator for PDF documents.
 *
 * <p>Overrides {@link DocumentFactory#createDocument()} to return a
 * {@link PdfDocument} instance.
 */
public class PdfDocumentFactory extends DocumentFactory {

    /**
     * Creates and returns a new {@link PdfDocument}.
     *
     * @return a new {@code PdfDocument}
     */
    @Override
    public Document createDocument() {
        System.out.println("[PdfDocumentFactory] Creating a new PDF document.");
        return new PdfDocument();
    }
}
