package com.cognizant.factory;

import com.cognizant.factory.document.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the Factory Method implementation.
 *
 * <p>Verifies:
 * <ul>
 *   <li>Each factory returns the correct concrete type (positive)</li>
 *   <li>The document lifecycle completes without exceptions (positive)</li>
 *   <li>Saving a closed document throws (negative)</li>
 * </ul>
 */
@DisplayName("DocumentFactory Tests")
class DocumentFactoryTest {

    @Test
    @DisplayName("WordDocumentFactory must produce a WordDocument")
    void testWordFactoryProducesWordDocument() {
        Document doc = new WordDocumentFactory().createDocument();
        assertInstanceOf(WordDocument.class, doc);
        assertEquals("Microsoft Word Document (.docx)", doc.getDocumentType());
    }

    @Test
    @DisplayName("PdfDocumentFactory must produce a PdfDocument")
    void testPdfFactoryProducesPdfDocument() {
        Document doc = new PdfDocumentFactory().createDocument();
        assertInstanceOf(PdfDocument.class, doc);
        assertEquals("PDF Document (.pdf)", doc.getDocumentType());
    }

    @Test
    @DisplayName("ExcelDocumentFactory must produce an ExcelDocument")
    void testExcelFactoryProducesExcelDocument() {
        Document doc = new ExcelDocumentFactory().createDocument();
        assertInstanceOf(ExcelDocument.class, doc);
        assertEquals("Microsoft Excel Spreadsheet (.xlsx)", doc.getDocumentType());
    }

    @Test
    @DisplayName("processDocument() must complete the full lifecycle without throwing")
    void testProcessDocumentLifecycle() {
        assertDoesNotThrow(() ->
            new WordDocumentFactory().processDocument("Lifecycle test content")
        );
    }

    @Test
    @DisplayName("Saving a closed document must throw IllegalStateException")
    void testSaveOnClosedDocumentThrows() {
        Document doc = new PdfDocumentFactory().createDocument();
        // doc is never opened — save must reject this call
        assertThrows(IllegalStateException.class, () -> doc.save("content"));
    }

    @Test
    @DisplayName("Each factory call must produce a distinct object instance")
    void testEachCallProducesNewInstance() {
        DocumentFactory factory = new ExcelDocumentFactory();
        Document d1 = factory.createDocument();
        Document d2 = factory.createDocument();
        assertNotSame(d1, d2, "Factory must return a new instance on every call.");
    }
}
