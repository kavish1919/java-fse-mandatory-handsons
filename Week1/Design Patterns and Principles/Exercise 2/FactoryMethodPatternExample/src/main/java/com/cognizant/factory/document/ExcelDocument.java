package com.cognizant.factory.document;

/**
 * ExcelDocument – Concrete Product for Microsoft Excel spreadsheets (.xlsx).
 *
 * <p>Implements the {@link Document} interface and provides Excel-specific
 * behaviour for open, save, and close operations.
 */
public class ExcelDocument implements Document {

    private static final String TYPE = "Excel";

    /**
     * Opens the Excel spreadsheet.
     */
    @Override
    public void open() {
        System.out.println("[ExcelDocument] Opening Excel spreadsheet (.xlsx)...");
        System.out.println("[ExcelDocument] Loading workbook, sheets, and formula engine.");
    }

    /**
     * Saves the Excel spreadsheet.
     */
    @Override
    public void save() {
        System.out.println("[ExcelDocument] Saving Excel spreadsheet (.xlsx)...");
        System.out.println("[ExcelDocument] Recalculating formulas and writing OOXML package.");
    }

    /**
     * Closes the Excel spreadsheet and releases the formula engine.
     */
    @Override
    public void close() {
        System.out.println("[ExcelDocument] Closing Excel spreadsheet and releasing formula engine.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return TYPE;
    }
}
