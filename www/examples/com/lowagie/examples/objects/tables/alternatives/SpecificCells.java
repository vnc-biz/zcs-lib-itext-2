/*
 * $Id$
 * $Name$
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://itext.sourceforge.net/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@lists.sourceforge.net
 */
package com.lowagie.examples.objects.tables.alternatives;

import java.awt.Point;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * In this example we are going to add Cell in specific columns/rows.
 */
public class SpecificCells {
    /**
     * In this example we are going to add Cell in specific columns/rows.
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        System.out.println("adding cells at a specific position");
        // step 1: creation of a document-object
        Document document = new Document();
        try {
            // step 2: creation of the writer
            PdfWriter.getInstance(document, new FileOutputStream("specificcells.pdf"));
            // step 3: we open the document
            document.open();
            // step 4: we create a table and add it to the document
            Table aTable;
            
            aTable = new Table(4,4);    // 4 rows, 4 columns
            aTable.setAlignment(Element.ALIGN_RIGHT);
            aTable.addCell("2.2", new Point(2,2));
            aTable.addCell("3.3", new Point(3,3));
            aTable.addCell("2.1", new Point(2,1));
            aTable.addCell("1.3", new Point(1,3));
            document.add(aTable);
			document.add(new Paragraph("converted to PdfPTable:"));
			aTable.setConvert2pdfptable(true);
			document.add(aTable);
            document.newPage();     
            aTable = new Table(4,4);    // 4 rows, 4 columns
            aTable.setAlignment(Element.ALIGN_CENTER);
            aTable.addCell("2.2", new Point(2,2));
            aTable.addCell("3.3", new Point(3,3));
            aTable.addCell("2.1", new Point(2,1));
            aTable.addCell("1.3", new Point(1,3));
            aTable.addCell("5.2", new Point(5,2));
            aTable.addCell("6.1", new Point(6,1));
            aTable.addCell("5.0", new Point(5,0));  
            document.add(aTable);
			document.add(new Paragraph("converted to PdfPTable:"));
			aTable.setConvert2pdfptable(true);
			document.add(aTable);
            document.newPage();
            aTable = new Table(2,2);    // 2 rows, 2 columns
            aTable.setAlignment(Element.ALIGN_LEFT);
            aTable.setAutoFillEmptyCells(true);
            aTable.addCell("0.0");
            aTable.addCell("0.1");
            aTable.addCell("1.0");
            aTable.addCell("1.1");
            aTable.addColumns(2);
            float[] f = {1f, 1f, 1f, 1f};
            aTable.setWidths(f);
            aTable.addCell("2.2", new Point(2,2));
            aTable.addCell("3.3", new Point(3,3));
            aTable.addCell("2.1", new Point(2,1));
            aTable.addCell("1.3", new Point(1,3));
            aTable.addCell("5.2", new Point(5,2));
            aTable.addCell("6.1", new Point(6,1));
            aTable.addCell("5.0", new Point(5,0)); 
            document.add(aTable); 
			document.add(new Paragraph("converted to PdfPTable:"));
			aTable.setConvert2pdfptable(true);
			document.add(aTable);
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        // step 5: we close the document
        document.close();
    }
}
