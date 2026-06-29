package com.cognizant.factory;

/**
 * Entry point demonstrating the Factory Method Pattern.
 *
 * <p>Clients depend only on {@link DocumentFactory} — they never
 * instantiate concrete document classes directly, satisfying the
 * Dependency Inversion Principle.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Factory Method Pattern Demo — Document Management System ===");

        // Array of factories — adding a new document type only requires a
        // new factory subclass; existing client code is unchanged (OCP).
        DocumentFactory[] factories = {
            new WordDocumentFactory(),
            new PdfDocumentFactory(),
            new ExcelDocumentFactory()
        };

        String[] contents = {
            "Q3 Financial Report — Draft v1.0",
            "Employee Onboarding Handbook — Confidential",
            "Sales Dashboard — FY 2025 Actuals"
        };

        for (int i = 0; i < factories.length; i++) {
            factories[i].processDocument(contents[i]);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}
