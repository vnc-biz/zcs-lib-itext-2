/*
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Paulo Soares.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;

import java.util.ArrayList;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.ElementListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.BadElementException;

/** This is a table that can be put at an absolute position but can also
 * be added to the document as the class <CODE>Table</CODE>.
 * In the last case when crossing pages the table always break at full rows; if a
 * row is bigger than the page it is dropped silently to avoid infinite loops.
 * <P>
 * A PdfPTableEvent can be associated to the table to do custom drawing
 * when the table is rendered.
 * @author Paulo Soares (psoares@consiste.pt)
 */

public class PdfPTable implements Element{
    
    /** The index of the original <CODE>PdfcontentByte</CODE>.
     */    
    public static final int BASECANVAS = 0;
    /** The index of the duplicate <CODE>PdfContentByte</CODE> where the backgroung will be drawn.
     */    
    public static final int BACKGROUNDCANVAS = 1;
    /** The index of the duplicate <CODE>PdfContentByte</CODE> where the border lines will be drawn.
     */    
    public static final int LINECANVAS = 2;
    /** The index of the duplicate <CODE>PdfContentByte</CODE> where the text will be drawn.
     */    
    public static final int TEXTCANVAS = 3;
    
    protected ArrayList rows = new ArrayList();
    protected float totalHeight = 0;
    protected PdfPCell currentRow[];
    protected int currentRowIdx = 0;
    protected PdfPCell defaultCell = new PdfPCell((Phrase)null);
    protected float totalWidth = 0;
    protected float relativeWidths[];
    protected float absoluteWidths[];
    protected PdfPTableEvent tableEvent;
    
/** Holds value of property headerRows. */
    protected int headerRows;
    
/** Holds value of property widthPercentage. */
    protected float widthPercentage = 80;
    
/** Holds value of property horizontalAlignment. */
    private int horizontalAlignment = Element.ALIGN_CENTER;
    
    /** Constructs a <CODE>PdfPTable</CODE> with the relative column widths.
     * @param relativeWidths the relative column widths
     */    
    public PdfPTable(float relativeWidths[]) {
        if (relativeWidths == null)
            throw new NullPointerException("The widths array in PdfPTable constructor can not be null.");
        if (relativeWidths.length == 0)
            throw new IllegalArgumentException("The widths array in PdfPTable constructor can not have zero length.");
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
    }
    
    /** Constructs a <CODE>PdfPTable</CODE> with <CODE>numColumns</CODE> columns.
     * @param numColumns the number of columns
     */    
    public PdfPTable(int numColumns) {
        if (numColumns <= 0)
            throw new IllegalArgumentException("The number of columns in PdfPTable constructor must be greater than zero.");
        relativeWidths = new float[numColumns];
        for (int k = 0; k < numColumns; ++k)
            relativeWidths[k] = 1;
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
    }
    
    /** Constructs a copy of a <CODE>PdfPTable</CODE>.
     * @param table the <CODE>PdfPTable</CODE> to be copied
     */    
    public PdfPTable(PdfPTable table) {
        relativeWidths = new float[table.relativeWidths.length];
        absoluteWidths = new float[table.relativeWidths.length];
        System.arraycopy(table.relativeWidths, 0, relativeWidths, 0, relativeWidths.length);
        System.arraycopy(table.absoluteWidths, 0, absoluteWidths, 0, relativeWidths.length);
        totalWidth = table.totalWidth;
        totalHeight = table.totalHeight;
        currentRowIdx = table.currentRowIdx;
        tableEvent = table.tableEvent;
        defaultCell = new PdfPCell(table.defaultCell);
        currentRow = new PdfPCell[table.currentRow.length];
        for (int k = 0; k < currentRow.length; ++k) {
            if (table.currentRow[k] == null)
                break;
            currentRow[k] = new PdfPCell(table.currentRow[k]);
        }
        for (int k = 0; k < table.rows.size(); ++k) {
            rows.add(new PdfPRow((PdfPRow)(table.rows.get(k))));
        }
    }
    
    /** Sets the relative widths of the table.
     * @param relativeWidths the relative widths of the table.
     * @throws DocumentException if the number of widths is different than tne number
     * of columns
     */    
    public void setWidths(float relativeWidths[]) throws DocumentException {
        if (relativeWidths.length != this.relativeWidths.length)
            throw new DocumentException("Wrong number of columns.");
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        totalHeight = 0;
        calculateWidths();
        calculateHeights();
    }

    /** Sets the relative widths of the table.
     * @param relativeWidths the relative widths of the table.
     * @throws DocumentException if the number of widths is different than tne number
     * of columns
     */    
    public void setWidths(int relativeWidths[]) throws DocumentException {
        float tb[] = new float[relativeWidths.length];
        for (int k = 0; k < relativeWidths.length; ++k)
            tb[k] = relativeWidths[k];
        setWidths(tb);
    }

    private void calculateWidths() {
        if (totalWidth <= 0)
            return;
        float total = 0;
        for (int k = 0; k < absoluteWidths.length; ++k) {
            total += relativeWidths[k];
        }
        for (int k = 0; k < absoluteWidths.length; ++k) {
            absoluteWidths[k] = totalWidth * relativeWidths[k] / total;
        }
    }
    
    /** Sets the full width of the table.
     * @param totalWidth the full width of the table.
     */    
    public void setTotalWidth(float totalWidth) {
        if (this.totalWidth == totalWidth)
            return;
        this.totalWidth = totalWidth;
        totalHeight = 0;
        calculateWidths();
        calculateHeights();
    }

    /** Gets the full width of the table.
     * @return the full width of the table
     */    
    public float getTotalWidth() {
        return totalWidth;
    }

    void calculateHeights() {
        if (totalWidth <= 0)
            return;
        totalHeight = 0;
        for (int k = 0; k < rows.size(); ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            row.setWidths(absoluteWidths);
            totalHeight += row.getMaxHeights();
        }
    }
    
    /** Gets the default <CODE>PdfPCell</CODE> that will be used as
     * reference for all the <CODE>addCell</CODE> methods except
     * <CODE>addCell(PdfPCell)</CODE>.
     * @return default <CODE>PdfPCell</CODE>
     */    
    public PdfPCell getDefaultCell() {
        return defaultCell;
    }
    
    /** Adds a cell element.
     * @param cell the cell element
     */    
    public void addCell(PdfPCell cell) {
        PdfPCell ncell = new PdfPCell(cell);
        currentRow[currentRowIdx++] = ncell;
        if (currentRowIdx >= currentRow.length) {
            PdfPRow row = new PdfPRow(currentRow);
            if (totalWidth > 0) {
                row.setWidths(absoluteWidths);
                totalHeight += row.getMaxHeights();
            }
            rows.add(row);
            currentRow = new PdfPCell[absoluteWidths.length];
            currentRowIdx = 0;
        }
    }
    
    /** Adds a cell element.
     * @param text the text for the cell
     */    
    public void addCell(String text) {
        addCell(new Phrase(text));
    }
    
    /** Adds a cell element.
     * @param table the table to be added to the cell
     */    
    public void addCell(PdfPTable table) {
        defaultCell.setTable(table);
        addCell(defaultCell);
        defaultCell.setTable(null);
    }
    
    /** Adds a cell element.
     * @param phrase the <CODE>Phrase</CODE> to be added to the cell
     */    
    public void addCell(Phrase phrase) {
        defaultCell.setPhrase(phrase);
        addCell(defaultCell);
        defaultCell.setPhrase(null);
    }
    
    /** Writes the selected rows to the document.<br>
     * <CODE>canvases</CODE> is obtained from <CODE>beginWrittingRows()</CODE>.
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written - 1. If it is -1 all the
     * rows to the end are written
     * @param xPos the x write coodinate
     * @param yPos the y write coodinate
     * @param canvases an array of 4 <CODE>PdfContentByte</CODE> obtained from
     * <CODE>beginWrittingRows()</CODE>
     * @return the y coordinate position of the bottom of the last row
     * @see #beginWritingRows(com.lowagie.text.pdf.PdfContentByte)
     */    
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        if (totalWidth <= 0)
            throw new RuntimeException("The width must be greater than zero.");
        int size = rows.size();
        if (rowEnd < 0)
            rowEnd = size;
        if (rowStart >= size || rowStart >= rowEnd)
            return yPos;
        rowEnd = Math.min(rowEnd, size);
        float yPosStart = yPos;
        for (int k = rowStart; k < rowEnd; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            row.writeCells(xPos, yPos, canvases);
            yPos -= row.getMaxHeights();
        }
        if (tableEvent != null) {
            float heights[] = new float[rowEnd - rowStart + 1];
            heights[0] = yPosStart;
            for (int k = rowStart; k < rowEnd; ++k) {
                PdfPRow row = (PdfPRow)rows.get(k);
                heights[k - rowStart + 1] = heights[k - rowStart] - row.getMaxHeights();
            }
            float widths[] = new float[absoluteWidths.length + 1];
            widths[0] = xPos;
            for (int k = 0; k < absoluteWidths.length; ++k)
                widths[k + 1] = widths[k] + absoluteWidths[k];
            tableEvent.tableLayout(this, widths, heights, 0, rowStart, canvases);
        }
        return yPos;
    }
    
    /** Writes the selected rows to the document.
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written - 1. If it is -1 all the
     * rows to the end are written
     * @param xPos the x write coodinate
     * @param yPos the y write coodinate
     * @param canvas the <CODE>PdfContentByte</CODE> where the rows will
     * be written to
     * @return the y coordinate position of the bottom of the last row
     */    
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        PdfContentByte[] canvases = beginWritingRows(canvas);
        float y = writeSelectedRows(rowStart, rowEnd, xPos, yPos, canvases);
        endWritingRows(canvases);
        return y;
    }
    
    /** Gets and initializes the 4 layers where the table is written to. The text or graphics are added to
     * one of the 4 <CODE>PdfContentByte</CODE> returned with the following order:<p>
     * <ul>
     * <li><CODE>PdfPtable.BASECANVAS</CODE> - the original <CODE>PdfContentByte</CODE>. Anything placed here
     * will be under the table.
     * <li><CODE>PdfPtable.BACKGROUNDCANVAS</CODE> - the layer where the background goes to.
     * <li><CODE>PdfPtable.LINECANVAS</CODE> - the layer where the lines go to.
     * <li><CODE>PdfPtable.TEXTCANVAS</CODE> - the layer where the text go to. Anything placed here
     * will be over the table.
     * </ul><p>
     * The layers are placed in sequence on top of each other.
     * @param canvas the <CODE>PdfContentByte</CODE> where the rows will
     * be written to
     * @return an array of 4 <CODE>PdfContentByte</CODE>
     * @see #writeSelectedRows(int, int, float, float, PdfContentByte[])
     */    
    public static PdfContentByte[] beginWritingRows(PdfContentByte canvas) {
        return new PdfContentByte[]{
            canvas,
            canvas.getDuplicate(),
            canvas.getDuplicate(),
            canvas.getDuplicate(),
        };
    }
    
    /** Finishes writing the table.
     * @param canvases the array returned by <CODE>beginWritingRows()</CODE>
     */    
    public static void endWritingRows(PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[BASECANVAS];
        canvas.saveState();
        canvas.add(canvases[BACKGROUNDCANVAS]);
        canvas.restoreState();
        canvas.saveState();
        canvas.setLineCap(2);
        canvas.resetRGBColorStroke();
        canvas.add(canvases[LINECANVAS]);
        canvas.restoreState();
        canvas.add(canvases[TEXTCANVAS]);
    }
    
    /** Gets the number of rows in this table.
     * @return the number of rows in this table
     */    
    public int size() {
        return rows.size();
    }
    
    /** Gets the total height of the table.
     * @return the total height of the table
     */    
    public float getTotalHeight() {
        return totalHeight;
    }
    
    /** Gets the height of a particular row.
     * @param idx the row index (starts at 0)
     * @return the height of a particular row
     */    
    public float getRowHeight(int idx) {
        if (totalWidth <= 0 || idx < 0 || idx >= rows.size())
            return 0;
        PdfPRow row = (PdfPRow)rows.get(idx);
        return row.getMaxHeights();
    }
    
    /** Gets the height of the rows that constitute the header as defined by
     * <CODE>setHeaderRows()</CODE>.
     * @return the height of the rows that constitute the header
     */    
    public float getHeaderHeight() {
        float total = 0;
        int size = Math.min(rows.size(), headerRows);
        for (int k = 0; k < size; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            total += row.getMaxHeights();
        }
        return total;
    }
    
    /** Deletes a row from the table.
     * @param rowNumber the row to be deleted
     * @return <CODE>true</CODE> if the row was deleted
     */    
    public boolean deleteRow(int rowNumber) {
        if (rowNumber < 0 || rowNumber >= rows.size()) {
            return false;
        }
        if (totalWidth > 0) {
            PdfPRow row = (PdfPRow)rows.get(rowNumber);
            totalHeight -= row.getMaxHeights();
        }
        rows.remove(rowNumber);
        return true;
    }
    
    /** Deletes the last row in the table.
     * @return <CODE>true</CODE> if the last row was deleted
     */    
    public boolean deleteLastRow() {
        return deleteRow(rows.size() - 1);
    }
    
    /** Gets the number of the rows that constitute the header.
     * @return the number of the rows that constitute the header
     */
    public int getHeaderRows() {
        return headerRows;
    }
    
    /** Sets the number of the top rows that constitute the header.
     * This header has only meaning if the table is added to <CODE>Document</CODE>
     * and the table crosses pages.
     * @param headerRows the number of the top rows that constitute the header
     */
    public void setHeaderRows(int headerRows) {
        if (headerRows < 0)
            headerRows = 0;
        this.headerRows = headerRows;
    }
    
    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public ArrayList getChunks() {
        return new ArrayList();
    }
    
    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return Element.PTABLE;
    }
    
    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener	an <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
    /** Gets the width percentage that the table will occupy in the page.
     * @return the width percentage that the table will occupy in the page
     */
    public float getWidthPercentage() {
        return widthPercentage;
    }
    
    /** Sets the width percentage that the table will occupy in the page.
     * @param widthPercentage the width percentage that the table will occupy in the page
     */
    public void setWidthPercentage(float widthPercentage) {
        this.widthPercentage = widthPercentage;
    }
    
    /** Gets the horizontal alignment of the table relative to the page.
     * @return the horizontal alignment of the table relative to the page
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }
    
    /** Sets the horizontal alignment of the table relative to the page.
     * It only has meaning if the width precentage is less than
     * 100%.
     * @param horizontalAlignment the horizontal alignment of the table relative to the page
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }
    
    //add by Jin-Hsia Yang
    PdfPRow getRow(int idx) {
        return (PdfPRow)rows.get(idx);
    }
    //end add

    /** Sets the table event for this table.
     * @param event the table event for this table
     */    
    public void setTableEvent(PdfPTableEvent event) {
        tableEvent = event;
    }
    
    /** Gets the table event for this page.
     * @return the table event for this page
     */    
    public PdfPTableEvent getTableEvent() {
        return tableEvent;
    }
    
    public float[] getAbsoluteWidths() {
        return absoluteWidths;
    }
}
