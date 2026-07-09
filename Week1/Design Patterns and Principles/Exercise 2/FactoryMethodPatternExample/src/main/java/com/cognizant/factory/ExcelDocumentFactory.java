package com.cognizant.factory;

import com.cognizant.factory.document.Document;
import com.cognizant.factory.document.ExcelDocument;

/**
 * ExcelDocumentFactory – Concrete Creator for Excel spreadsheets.
 *
 * <p>Overrides {@link DocumentFactory#createDocument()} to return an
 * {@link ExcelDocument} instance.
 */
public class ExcelDocumentFactory extends DocumentFactory {

    /**
     * Creates and returns a new {@link ExcelDocument}.
     *
     * @return a new {@code ExcelDocument}
     */
    @Override
    public Document createDocument() {
        System.out.println("[ExcelDocumentFactory] Creating a new Excel spreadsheet.");
        return new ExcelDocument();
    }
}
