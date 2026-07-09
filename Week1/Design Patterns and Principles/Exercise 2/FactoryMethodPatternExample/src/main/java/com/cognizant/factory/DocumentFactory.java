package com.cognizant.factory;

import com.cognizant.factory.document.Document;

/**
 * DocumentFactory – Abstract Creator for the Factory Method Pattern.
 *
 * <p>Declares the <em>factory method</em> {@link #createDocument()}, which
 * subclasses must override to instantiate the appropriate {@link Document}
 * concrete product.
 *
 * <p>The template method {@link #openNewDocument()} demonstrates how the
 * creator uses its own factory method without being coupled to any specific
 * product class — the essence of the Factory Method pattern.
 *
 * <pre>
 *   DocumentFactory factory = new WordDocumentFactory();
 *   Document doc = factory.createDocument();   // returns a WordDocument
 *   factory.openNewDocument();                 // open–save–close lifecycle
 * </pre>
 */
public abstract class DocumentFactory {

    /**
     * Factory Method – subclasses decide which {@link Document} to create.
     *
     * @return a new, fully initialised {@link Document} instance
     */
    public abstract Document createDocument();

    /**
     * Template Method – demonstrates the standard open → save → close
     * document lifecycle using the product created by {@link #createDocument()}.
     *
     * <p>Client code calls this method and never needs to know the concrete
     * document type.
     */
    public void openNewDocument() {
        Document document = createDocument();
        System.out.println("\n--- Document lifecycle for type: " + document.getType() + " ---");
        document.open();
        document.save();
        document.close();
        System.out.println("--- Lifecycle complete ---\n");
    }
}
