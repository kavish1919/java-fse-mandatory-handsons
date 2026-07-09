package com.cognizant.factory.document;

/**
 * WordDocument – Concrete Product for Microsoft Word files (.docx).
 *
 * <p>Implements the {@link Document} interface and provides Word-specific
 * behaviour for open, save, and close operations.
 */
public class WordDocument implements Document {

    private static final String TYPE = "Word";

    /**
     * Opens the Word document.
     */
    @Override
    public void open() {
        System.out.println("[WordDocument] Opening Word document (.docx)...");
        System.out.println("[WordDocument] Parsing XML structure and loading styles.");
    }

    /**
     * Saves the Word document.
     */
    @Override
    public void save() {
        System.out.println("[WordDocument] Saving Word document (.docx)...");
        System.out.println("[WordDocument] Serialising content to Office Open XML format.");
    }

    /**
     * Closes the Word document and releases COM/file handles.
     */
    @Override
    public void close() {
        System.out.println("[WordDocument] Closing Word document and releasing file handles.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return TYPE;
    }
}
