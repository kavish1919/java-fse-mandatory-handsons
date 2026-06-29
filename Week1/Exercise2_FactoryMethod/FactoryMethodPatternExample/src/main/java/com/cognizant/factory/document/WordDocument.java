package com.cognizant.factory.document;

/**
 * Concrete document representing a Microsoft Word file (.docx).
 *
 * <p>Simulates rich-text operations typical of a word processor, such as
 * paragraph formatting, spell-checking, and .docx serialisation.
 */
public class WordDocument implements Document {

    private static final String TYPE = "Microsoft Word Document (.docx)";
    private boolean isOpen = false;

    @Override
    public void open() {
        isOpen = true;
        System.out.println("  [Word] Opening document — loading DOCX parser and font engine.");
    }

    @Override
    public void save(String content) {
        if (!isOpen) {
            throw new IllegalStateException("Cannot save a closed Word document.");
        }
        System.out.printf("  [Word] Saving content to .docx format: \"%s\"%n", content);
    }

    @Override
    public void close() {
        isOpen = false;
        System.out.println("  [Word] Closing document — releasing font cache and parser.");
    }

    @Override
    public String getDocumentType() {
        return TYPE;
    }
}
