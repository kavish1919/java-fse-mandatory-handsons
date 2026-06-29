package com.cognizant.factory;

import com.cognizant.factory.document.Document;
import com.cognizant.factory.document.WordDocument;

/** Concrete factory that produces {@link WordDocument} instances. */
public class WordDocumentFactory extends DocumentFactory {

    @Override
    public Document createDocument() {
        return new WordDocument();
    }
}
