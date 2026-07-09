package com.cognizant.factory.document;

/**
 * Document – Abstract Product interface for the Factory Method Pattern.
 *
 * <p>Every concrete document type (Word, PDF, Excel) must implement this
 * contract, guaranteeing that the rest of the system can work with any
 * document without knowing its concrete class.
 *
 * <p>Responsibilities exposed to clients:
 * <ul>
 *   <li>{@link #open()}   – Load / initialise the document.</li>
 *   <li>{@link #save()}   – Persist the document content.</li>
 *   <li>{@link #close()}  – Release resources held by the document.</li>
 *   <li>{@link #getType()} – Return a human-readable document-type label.</li>
 * </ul>
 */
public interface Document {

    /**
     * Opens (loads) the document, making it ready for editing.
     */
    void open();

    /**
     * Saves the current state of the document to its backing store.
     */
    void save();

    /**
     * Closes the document and releases any associated resources.
     */
    void close();

    /**
     * Returns the document type label (e.g., {@code "Word"}, {@code "PDF"}, {@code "Excel"}).
     *
     * @return non-null, non-empty type string
     */
    String getType();
}
