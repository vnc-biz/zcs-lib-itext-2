/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * --> Copyright 2001-2004 by Bruno Lowagie <--
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://www.lowagie.com/iText/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@lists.sourceforge.net
 */

package com.lowagie.examples.general.faq;


import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Demonstrates how the PDF coordinate system works.
 * @author blowagie
 */
public class XandYcoordinates {
    /**
     * Creates a PDF document with shapes, lines and text at specific X and Y coordinates.
     * @param args no arguments needed here
     */
    public static void main(String[] args) {
        
        System.out.println("X and Y coordinate system");        
        // step 1: creation of a document-object
        Document document = new Document();
        
        try {
            
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("XandY.pdf"));
            
            // step 3: we open the document
            document.open();
            
            // step 4:
            PdfContentByte cb = writer.getDirectContent();
            
            // we create a PdfTemplate
            PdfTemplate template = cb.createTemplate(25, 25);
            
            // we add some crosses to visualize the coordinates
            template.moveTo(13, 0);
            template.lineTo(13, 25);
            template.moveTo(0, 13);
            template.lineTo(50, 13);
            template.stroke();
            
            // we add the template on different positions
            cb.addTemplate(template, 216 - 13, 720 - 13);
            cb.addTemplate(template, 360 - 13, 360 - 13);
            cb.addTemplate(template, 360 - 13, 504 - 13);
            cb.addTemplate(template, 72 - 13, 144 - 13);
            cb.addTemplate(template, 144 - 13, 288 - 13);

            cb.moveTo(216, 720);
            cb.lineTo(360, 360);
            cb.lineTo(360, 504);
            cb.lineTo(72, 144);
            cb.lineTo(144, 288);
            cb.stroke();
            
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.beginText();
            cb.setFontAndSize(bf, 12);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "(3\", 10\")", 216 + 25, 720 + 5, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "(5\", 5\")", 360 + 25, 360 + 5, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "(5\", 7\")", 360 + 25, 504 + 5, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "(1\", 2\")", 72 + 25, 144 + 5, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "(2\", 4\")", 144 + 25, 288 + 5, 0);
            cb.endText(); 
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