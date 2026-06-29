package com.cognizant.factory;

import com.cognizant.factory.document.Document;
import com.cognizant.factory.document.ExcelDocument;

/** Concrete factory that produces {@link ExcelDocument} instances. */
public class ExcelDocumentFactory extends DocumentFactory {

    @Override
    public Document createDocument() {
        return new ExcelDocument();
    }
}
