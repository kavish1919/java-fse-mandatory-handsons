package com.cognizant.factory;

import com.cognizant.factory.document.Document;
import com.cognizant.factory.document.PdfDocument;

/** Concrete factory that produces {@link PdfDocument} instances. */
public class PdfDocumentFactory extends DocumentFactory {

    @Override
    public Document createDocument() {
        return new PdfDocument();
    }
}
