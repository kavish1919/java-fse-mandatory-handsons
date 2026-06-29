package com.cognizant.factory.document;

/**
 * Concrete document representing a Portable Document Format file (.pdf).
 *
 * <p>Simulates PDF-specific operations such as rendering pages, embedding fonts,
 * and applying encryption before persistence.
 */
public class PdfDocument implements Document {

    private static final String TYPE = "PDF Document (.pdf)";
    private boolean isOpen = false;

    @Override
    public void open() {
        isOpen = true;
        System.out.println("  [PDF]  Opening document — initialising PDF renderer and page layout.");
    }

    @Override
    public void save(String content) {
        if (!isOpen) {
            throw new IllegalStateException("Cannot save a closed PDF document.");
        }
        System.out.printf("  [PDF]  Rendering and saving content as .pdf: \"%s\"%n", content);
    }

    @Override
    public void close() {
        isOpen = false;
        System.out.println("  [PDF]  Closing document — flushing page buffer and releasing renderer.");
    }

    @Override
    public String getDocumentType() {
        return TYPE;
    }
}
