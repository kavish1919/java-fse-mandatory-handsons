package com.cognizant.factory;

import com.cognizant.factory.document.Document;
import com.cognizant.factory.document.ExcelDocument;
import com.cognizant.factory.document.PdfDocument;
import com.cognizant.factory.document.WordDocument;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DocumentFactoryTest – JUnit 5 test suite for the Factory Method Pattern.
 *
 * <p>Test cases:
 * <ol>
 *   <li>WordDocumentFactory creates a non-null Document.</li>
 *   <li>WordDocumentFactory creates a WordDocument instance.</li>
 *   <li>WordDocument.getType() returns "Word".</li>
 *   <li>PdfDocumentFactory creates a non-null Document.</li>
 *   <li>PdfDocumentFactory creates a PdfDocument instance.</li>
 *   <li>PdfDocument.getType() returns "PDF".</li>
 *   <li>ExcelDocumentFactory creates a non-null Document.</li>
 *   <li>ExcelDocumentFactory creates an ExcelDocument instance.</li>
 *   <li>ExcelDocument.getType() returns "Excel".</li>
 *   <li>Each factory call returns a fresh (distinct) instance.</li>
 *   <li>open(), save(), close() execute without exceptions (Word).</li>
 *   <li>open(), save(), close() execute without exceptions (PDF).</li>
 *   <li>open(), save(), close() execute without exceptions (Excel).</li>
 *   <li>openNewDocument() template method completes without exceptions.</li>
 * </ol>
 */
class DocumentFactoryTest {

    // -----------------------------------------------------------------------
    // WordDocumentFactory tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-01: WordDocumentFactory.createDocument() returns non-null")
    void testWordFactoryReturnsNonNull() {
        DocumentFactory factory = new WordDocumentFactory();
        assertNotNull(factory.createDocument(),
                "WordDocumentFactory.createDocument() must not return null.");
    }

    @Test
    @DisplayName("TC-02: WordDocumentFactory produces a WordDocument instance")
    void testWordFactoryProducesCorrectType() {
        Document doc = new WordDocumentFactory().createDocument();
        assertInstanceOf(WordDocument.class, doc,
                "Expected WordDocument but got: " + doc.getClass().getSimpleName());
    }

    @Test
    @DisplayName("TC-03: WordDocument.getType() returns 'Word'")
    void testWordDocumentGetType() {
        Document doc = new WordDocumentFactory().createDocument();
        assertEquals("Word", doc.getType(),
                "WordDocument.getType() should return \"Word\".");
    }

    // -----------------------------------------------------------------------
    // PdfDocumentFactory tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-04: PdfDocumentFactory.createDocument() returns non-null")
    void testPdfFactoryReturnsNonNull() {
        DocumentFactory factory = new PdfDocumentFactory();
        assertNotNull(factory.createDocument(),
                "PdfDocumentFactory.createDocument() must not return null.");
    }

    @Test
    @DisplayName("TC-05: PdfDocumentFactory produces a PdfDocument instance")
    void testPdfFactoryProducesCorrectType() {
        Document doc = new PdfDocumentFactory().createDocument();
        assertInstanceOf(PdfDocument.class, doc,
                "Expected PdfDocument but got: " + doc.getClass().getSimpleName());
    }

    @Test
    @DisplayName("TC-06: PdfDocument.getType() returns 'PDF'")
    void testPdfDocumentGetType() {
        Document doc = new PdfDocumentFactory().createDocument();
        assertEquals("PDF", doc.getType(),
                "PdfDocument.getType() should return \"PDF\".");
    }

    // -----------------------------------------------------------------------
    // ExcelDocumentFactory tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-07: ExcelDocumentFactory.createDocument() returns non-null")
    void testExcelFactoryReturnsNonNull() {
        DocumentFactory factory = new ExcelDocumentFactory();
        assertNotNull(factory.createDocument(),
                "ExcelDocumentFactory.createDocument() must not return null.");
    }

    @Test
    @DisplayName("TC-08: ExcelDocumentFactory produces an ExcelDocument instance")
    void testExcelFactoryProducesCorrectType() {
        Document doc = new ExcelDocumentFactory().createDocument();
        assertInstanceOf(ExcelDocument.class, doc,
                "Expected ExcelDocument but got: " + doc.getClass().getSimpleName());
    }

    @Test
    @DisplayName("TC-09: ExcelDocument.getType() returns 'Excel'")
    void testExcelDocumentGetType() {
        Document doc = new ExcelDocumentFactory().createDocument();
        assertEquals("Excel", doc.getType(),
                "ExcelDocument.getType() should return \"Excel\".");
    }

    // -----------------------------------------------------------------------
    // Factory independence test – each call should return a distinct object
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-10: Each createDocument() call returns a distinct (new) instance")
    void testEachCallReturnsNewInstance() {
        DocumentFactory factory = new WordDocumentFactory();
        Document first  = factory.createDocument();
        Document second = factory.createDocument();
        assertNotSame(first, second,
                "Factory method should create a new Document on every call.");
    }

    // -----------------------------------------------------------------------
    // Lifecycle / API smoke tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-11: WordDocument open/save/close do not throw exceptions")
    void testWordDocumentLifecycle() {
        Document doc = new WordDocumentFactory().createDocument();
        assertDoesNotThrow(doc::open,  "WordDocument.open() must not throw.");
        assertDoesNotThrow(doc::save,  "WordDocument.save() must not throw.");
        assertDoesNotThrow(doc::close, "WordDocument.close() must not throw.");
    }

    @Test
    @DisplayName("TC-12: PdfDocument open/save/close do not throw exceptions")
    void testPdfDocumentLifecycle() {
        Document doc = new PdfDocumentFactory().createDocument();
        assertDoesNotThrow(doc::open,  "PdfDocument.open() must not throw.");
        assertDoesNotThrow(doc::save,  "PdfDocument.save() must not throw.");
        assertDoesNotThrow(doc::close, "PdfDocument.close() must not throw.");
    }

    @Test
    @DisplayName("TC-13: ExcelDocument open/save/close do not throw exceptions")
    void testExcelDocumentLifecycle() {
        Document doc = new ExcelDocumentFactory().createDocument();
        assertDoesNotThrow(doc::open,  "ExcelDocument.open() must not throw.");
        assertDoesNotThrow(doc::save,  "ExcelDocument.save() must not throw.");
        assertDoesNotThrow(doc::close, "ExcelDocument.close() must not throw.");
    }

    @Test
    @DisplayName("TC-14: openNewDocument() template method completes without exceptions")
    void testTemplateMethodOpenNewDocument() {
        assertDoesNotThrow(() -> new WordDocumentFactory().openNewDocument(),
                "WordDocumentFactory.openNewDocument() must not throw.");
        assertDoesNotThrow(() -> new PdfDocumentFactory().openNewDocument(),
                "PdfDocumentFactory.openNewDocument() must not throw.");
        assertDoesNotThrow(() -> new ExcelDocumentFactory().openNewDocument(),
                "ExcelDocumentFactory.openNewDocument() must not throw.");
    }
}
