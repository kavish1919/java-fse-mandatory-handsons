package com.cognizant.factory.document;

/**
 * Abstraction for all document types in the document management system.
 *
 * <p>Each document supports a lifecycle of: open → edit → save → close.
 * Concrete implementations carry type-specific rendering and persistence logic.
 *
 * <p>Design: Interface over abstract class allows a concrete document to
 * implement multiple contracts without breaking single inheritance.
 */
public interface Document {

    /**
     * Opens the document, initialising any required resources.
     */
    void open();

    /**
     * Saves the current state of the document to its backing store.
     *
     * @param content the textual content to persist
     */
    void save(String content);

    /**
     * Closes the document and releases any held resources.
     */
    void close();

    /**
     * Returns a human-readable description of the document type and format.
     *
     * @return document type descriptor
     */
    String getDocumentType();
}
