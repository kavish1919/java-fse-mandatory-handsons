package com.cognizant.factory.document;

/**
 * PdfDocument – Concrete Product for Adobe PDF files (.pdf).
 *
 * <p>Implements the {@link Document} interface and provides PDF-specific
 * behaviour for open, save, and close operations.
 */
public class PdfDocument implements Document {

    private static final String TYPE = "PDF";

    /**
     * Opens the PDF document.
     */
    @Override
    public void open() {
        System.out.println("[PdfDocument] Opening PDF document (.pdf)...");
        System.out.println("[PdfDocument] Loading PDF object graph and rendering fonts.");
    }

    /**
     * Saves the PDF document.
     */
    @Override
    public void save() {
        System.out.println("[PdfDocument] Saving PDF document (.pdf)...");
        System.out.println("[PdfDocument] Writing cross-reference table and flushing streams.");
    }

    /**
     * Closes the PDF document and releases rendering resources.
     */
    @Override
    public void close() {
        System.out.println("[PdfDocument] Closing PDF document and releasing rendering resources.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return TYPE;
    }
}
