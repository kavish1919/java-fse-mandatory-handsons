package com.cognizant.factory;

import com.cognizant.factory.document.Document;

/**
 * DocumentFactoryDemo – Entry-point demonstrating the Factory Method Pattern.
 *
 * <p>Client code works exclusively with {@link DocumentFactory} and
 * {@link com.cognizant.factory.document.Document} — it never references any
 * concrete class directly, satisfying the Open/Closed Principle.
 */
public class DocumentFactoryDemo {

    public static void main(String[] args) {

        System.out.println("=== Factory Method Pattern Demo: Document Management System ===\n");

        // --- Using the template method (open → save → close lifecycle) ------
        System.out.println(">> Running full document lifecycle via template method:\n");

        DocumentFactory wordFactory  = new WordDocumentFactory();
        DocumentFactory pdfFactory   = new PdfDocumentFactory();
        DocumentFactory excelFactory = new ExcelDocumentFactory();

        wordFactory.openNewDocument();
        pdfFactory.openNewDocument();
        excelFactory.openNewDocument();

        // --- Using createDocument() directly --------------------------------
        System.out.println(">> Calling createDocument() directly and checking runtime types:\n");

        Document word  = new WordDocumentFactory().createDocument();
        Document pdf   = new PdfDocumentFactory().createDocument();
        Document excel = new ExcelDocumentFactory().createDocument();

        System.out.printf("  word.getType()  → %s  (class: %s)%n",
                word.getType(),  word.getClass().getSimpleName());
        System.out.printf("  pdf.getType()   → %s   (class: %s)%n",
                pdf.getType(),   pdf.getClass().getSimpleName());
        System.out.printf("  excel.getType() → %s (class: %s)%n",
                excel.getType(), excel.getClass().getSimpleName());

        System.out.println("\n=== Demo complete ===");
    }
}
