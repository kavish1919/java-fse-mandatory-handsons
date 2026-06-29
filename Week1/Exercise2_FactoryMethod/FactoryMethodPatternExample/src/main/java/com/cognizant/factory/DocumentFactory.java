package com.cognizant.factory;

import com.cognizant.factory.document.Document;

/**
 * Abstract Factory defining the Factory Method contract.
 *
 * <p>Subclasses override {@link #createDocument()} to return the correct
 * {@link Document} implementation without exposing instantiation logic to clients.
 *
 * <p>The template method {@link #processDocument(String)} provides a standard
 * open → save → close lifecycle, ensuring consistent document handling
 * regardless of the concrete type created.
 *
 * <p>Design rationale: abstract class (not interface) because a shared
 * {@code processDocument} template belongs here.
 */
public abstract class DocumentFactory {

    /**
     * Factory Method — subclasses provide the concrete {@link Document}.
     *
     * @return a new {@link Document} instance of the appropriate type
     */
    public abstract Document createDocument();

    /**
     * Template method that orchestrates the document lifecycle.
     *
     * <p>Clients call this method; they never call {@link #createDocument()}
     * directly, maintaining the Open/Closed Principle.
     *
     * @param content text content to save into the new document
     */
    public final void processDocument(String content) {
        Document document = createDocument();
        System.out.println("\n→ Creating " + document.getDocumentType());
        document.open();
        document.save(content);
        document.close();
    }
}
