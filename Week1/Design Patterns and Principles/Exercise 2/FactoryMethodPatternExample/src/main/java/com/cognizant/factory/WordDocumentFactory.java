package com.cognizant.factory;

import com.cognizant.factory.document.Document;
import com.cognizant.factory.document.WordDocument;

/**
 * WordDocumentFactory – Concrete Creator for Word documents.
 *
 * <p>Overrides {@link DocumentFactory#createDocument()} to return a
 * {@link WordDocument} instance.  The rest of the system only sees the
 * {@link Document} interface returned by the factory method.
 */
public class WordDocumentFactory extends DocumentFactory {

    /**
     * Creates and returns a new {@link WordDocument}.
     *
     * @return a new {@code WordDocument}
     */
    @Override
    public Document createDocument() {
        System.out.println("[WordDocumentFactory] Creating a new Word document.");
        return new WordDocument();
    }
}
