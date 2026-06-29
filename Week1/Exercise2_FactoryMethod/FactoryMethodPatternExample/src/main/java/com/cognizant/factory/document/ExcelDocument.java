package com.cognizant.factory.document;

/**
 * Concrete document representing a Microsoft Excel spreadsheet (.xlsx).
 *
 * <p>Simulates spreadsheet-specific operations such as formula evaluation,
 * cell formatting, and workbook serialisation.
 */
public class ExcelDocument implements Document {

    private static final String TYPE = "Microsoft Excel Spreadsheet (.xlsx)";
    private boolean isOpen = false;

    @Override
    public void open() {
        isOpen = true;
        System.out.println("  [Excel] Opening document — loading workbook and formula engine.");
    }

    @Override
    public void save(String content) {
        if (!isOpen) {
            throw new IllegalStateException("Cannot save a closed Excel document.");
        }
        System.out.printf("  [Excel] Evaluating formulas and saving content to .xlsx: \"%s\"%n", content);
    }

    @Override
    public void close() {
        isOpen = false;
        System.out.println("  [Excel] Closing document — flushing formula cache and workbook.");
    }

    @Override
    public String getDocumentType() {
        return TYPE;
    }
}
