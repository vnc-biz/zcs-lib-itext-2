package com.lowagie.text.pdf;

import com.lowagie.text.*;

import java.util.ArrayList;

/**
 * Formats content into one or more columns bounded by a
 * rectangle.  The columns may be simple rectangles or
 * more complicated shapes. Add all of the columns before
 * adding content.  A MultiColumnText object may be added to
 * a document using <CODE>Document.add</CODE>.
 * @author Steve Appling
 */
public class MultiColumnText implements Element {

    /** special constant for automatic calculation of height */
    public static final float AUTOMATIC = -1f;

    /**
     * total desiredHeight of columns.  If <CODE>AUTOMATIC</CODE>, this means fill pages until done.
     * This may be larger than one page
     */
    private float desiredHeight;

    /**
     * total height of element written out so far
     */
    private float totalHeight;

    /**
     * true if all the text could not be written out due to height restriction
     */
    private boolean overflow;

    /**
     * Top of the columns - y position on starting page.
     * If <CODE>AUTOMATIC</CODE>, it means current y position when added to document
     */
    private float top;

    /**
     * used to store the y position of the bottom of the page
     */
    private float pageBottom;

    /**
     * ColumnText object used to do all the real work.  This same object is used for all columns
     */
    private ColumnText columnText;

    /**
     * Array of <CODE>ColumnDef</CODE> objects used to define the columns
     */
    private ArrayList columnDefs;

    /**
     * true if all columns are simple (rectangular)
     */
    private boolean simple = true;

    /**
     * Default constructor.  Sets height to <CODE>AUTOMATIC</CODE>.
     * Columns will repeat on each page as necessary to accomodate content length.
     */
    public MultiColumnText() {
        this(AUTOMATIC);
    }

    /**
     * Construct a MultiColumnText container of the specified height.
     * If height is <CODE>AUTOMATIC</CODE>, fill complete pages until done.
     * If a specific height is used, it may span one or more pages.
     *
     * @param height
     */
    public MultiColumnText(float height) {
        columnDefs = new ArrayList();
        desiredHeight = height;
        top = AUTOMATIC;
        // canvas will be set later
        columnText = new ColumnText(null);
        totalHeight = 0f;
        overflow = false;
    }

    /**
     * Indicates that all of the text did not fit in the
     * specified height
     *
     * @return true if the text didn't fit
     */
    public boolean isOverflow() {
        return overflow;
    }

    /**
     * Copy the parameters from the specified ColumnText to use
     * when rendering.  Parameters like <CODE>setArabicOptions</CODE>
     * must be set in this way.
     *
     * @param sourceColumn
     */
    public void useColumnParams(ColumnText sourceColumn) {
        // note that canvas will be overwritten later
        columnText.setSimpleVars(sourceColumn);
    }

    /**
     * Add a new column.  The parameters are limits for each column
     * wall in the format of a sequence of points (x1,y1,x2,y2,...).
     *
     * @param left  limits for left column
     * @param right limits for right column
     */
    public void addColumn(float[] left, float[] right) {
        ColumnDef nextDef = new ColumnDef(left, right);
        simple = nextDef.isSimple();
        columnDefs.add(nextDef);
    }

    /**
     * Add a simple rectangular column with specified left
     * and right x position boundaries.
     *
     * @param left  left boundary
     * @param right right boundary
     */
    public void addSimpleColumn(float left, float right) {
        ColumnDef newCol = new ColumnDef(left, right);
        columnDefs.add(newCol);
    }

    /**
     * Add the specified number of evenly spaced rectangular columns.
     * Columns will be seperated by the specified gutterWidth.
     *
     * @param left        left boundary of first column
     * @param right       right boundary of last column
     * @param gutterWidth width of gutter spacing between columns
     * @param numColumns  number of columns to add
     */
    public void addRegularColumns(float left, float right, float gutterWidth, int numColumns) {
        float currX = left;
        float width = right - left;
        float colWidth = (width - (gutterWidth * (numColumns - 1))) / numColumns;
        for (int i = 0; i < numColumns; i++) {
            addSimpleColumn(currX, currX + colWidth);
            currX += colWidth + gutterWidth;
        }
    }

    /**
     * Add an element to be rendered in a column.
     * Note that you can only add a <CODE>Phrase</CODE>
     * or a <CODE>Chunk</CODE> if the columns are
     * not all simple.  This is an underlying restriction in
     * {@link com.lowagie.text.pdf.ColumnText}
     *
     * @param element element to add
     * @throws DocumentException if element can't be added
     */
    public void addElement(Element element) throws DocumentException {
        if (simple) {
            columnText.addElement(element);
        } else if (element instanceof Phrase) {
            columnText.addText((Phrase) element);
        } else if (element instanceof Chunk) {
            columnText.addText((Chunk) element);
        } else {
            throw new DocumentException("Can't add " + element.getClass() + " to MultiColumnText with complex columns");
        }
    }


    /**
     * Write out the columns.  After writing, use
     * {@link #isOverflow()} to see if all text was written.
     *
     * @param canvas    PdfContentByte to write with
     * @param document  document to write to (only used to get page limit info)
     * @param documentY starting y position to begin writing at
     * @return
     * @throws DocumentException
     */
    public float write(PdfContentByte canvas, PdfDocument document, float documentY) throws DocumentException {
        columnText.canvas = canvas;
        if (top == AUTOMATIC) {
            top = documentY;  // shouldn't I be able to get this from the document?
        }
        if (columnDefs.size() == 0) {
            throw new DocumentException("MultiColumnText has no columns");
        }
        columnText.setYLine(top);
        pageBottom = document.bottom();
        float currentHeight = 0;
        int currentColumn = 0;
        boolean done = false;
        try {
            while (!done) {
                ColumnDef currentDef = (ColumnDef) columnDefs.get(currentColumn);
                columnText.setYLine(top);

                float[] left = currentDef.resolvePositions(Rectangle.LEFT);
                float[] right = currentDef.resolvePositions(Rectangle.RIGHT);
                currentHeight = Math.max(currentHeight, getHeight(left, right));

                if (currentDef.isSimple()) {
                    columnText.setSimpleColumn(left[2], left[3], right[0], right[1]);
                } else {
                    columnText.setColumns(left, right);
                }

                int result = columnText.go();
                if ((result & ColumnText.NO_MORE_TEXT) != 0) {
                    done = true;
                } else if (currentColumn + 1 < columnDefs.size()) {
                    currentColumn++;
                } else {  // check if we are done because of height
                    totalHeight += currentHeight;

                    if ((desiredHeight != AUTOMATIC) && (totalHeight >= desiredHeight)) {
                        done = true;
                        overflow = true;
                    } else {  // need to start new page and reset the columns
                        document.newPage();
                        currentColumn = 0;
                        top = document.top();
                        currentHeight = 0;
                    }
                }
            }
        } catch (DocumentException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return currentHeight;
    }

    /**
     * Figure out the height of a column from the border extents
     *
     * @param left  left border
     * @param right right border
     * @return height
     */
    private float getHeight(float[] left, float[] right) {
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < left.length; i += 2) {
            min = Math.min(min, left[i + 1]);
            max = Math.max(max, left[i + 1]);
        }
        for (int i = 0; i < right.length; i += 2) {
            min = Math.min(min, right[i + 1]);
            max = Math.max(max, right[i + 1]);
        }
        return max - min;
    }


    /**
     * Processes the element by adding it to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener	an <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        } catch (DocumentException de) {
            return false;
        }
    }

    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */

    public int type() {
        return Element.MULTI_COLUMN_TEXT;
    }

    /**
     * Returns null - not used
     *
     * @return	null
     */

    public ArrayList getChunks() {
        return null;
    }

    /**
     * Calculates the appropriate y position for the bottom
     * of the columns on this page.
     *
     * @return
     */
    private float getColumnBottom() {
        if (desiredHeight == AUTOMATIC) {
            return pageBottom;
        } else {
            return Math.max(top - (desiredHeight - totalHeight), pageBottom);
        }
    }

    /**
     * Inner class used to define a column
     */
    private class ColumnDef {
        private float[] left;
        private float[] right;

        ColumnDef(float[] newLeft, float[] newRight) {
            left = newLeft;
            right = newRight;
        }

        ColumnDef(float leftPosition, float rightPosition) {
            left = new float[4];
            left[0] = leftPosition; // x1
            left[1] = top;          // y1
            left[2] = leftPosition; // x2
            if (desiredHeight == AUTOMATIC || top == AUTOMATIC) {
                left[3] = AUTOMATIC;
            } else {
                left[3] = top - desiredHeight;
            }

            right = new float[4];
            right[0] = rightPosition; // x1
            right[1] = top;           // y1
            right[2] = rightPosition; // x2
            if (desiredHeight == AUTOMATIC || top == AUTOMATIC) {
                right[3] = AUTOMATIC;
            } else {
                right[3] = top - desiredHeight;
            }
        }

        /**
         * Resolves the positions for the specified side of the column
         * into real numbers once the top of the column is known.
         *
         * @param side either <CODE>Rectangle.LEFT</CODE>
         *             or <CODE>Rectangle.RIGHT</CODE>
         * @return the array of floats for the side
         */
        float[] resolvePositions(int side) {
            if (side == Rectangle.LEFT) {
                return resolvePositions(left);
            } else {
                return resolvePositions(right);
            }
        }

        private float[] resolvePositions(float[] positions) {
            if (!isSimple()) {
                return positions;
            }
            if (top == AUTOMATIC) {
                // this is bad - must be programmer error
                throw new RuntimeException("resolvePositions called with top=AUTOMATIC (-1).  " +
                        "Top position must be set befure lines can be resolved");
            }
            positions[1] = top;
            positions[3] = getColumnBottom();
            return positions;
        }

        /**
         * Checks if column definition is a simple rectangle
         * @return true if it is a simple column 
         */
        private boolean isSimple() {
            return (left.length == 4 && right.length == 4) && (left[0] == left[2] && right[0] == right[2]);
        }

    }
}