/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * --> Copyright 2001 by Bruno Lowagie <--
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://www.lowagie.com/iText/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 */

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfContentByte;

public class Chap1001 {
    
    public static void main(String[] args) {
        
        System.out.println("Chapter 10 example 1: Simple Graphic");
        
        // step 1: creation of a document-object
        Document document = new Document();
        
        try {
            
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Chap1001.pdf"));
            
            // step 3: we open the document
            document.open();
            
            // step 4: we grab the ContentByte and do some stuff with it
            PdfContentByte cb = writer.getDirectContent();
            
            // an example of a rectangle with a diagonal in very thick lines
            cb.setLineWidth(10f);
            // draw a rectangle
            cb.rectangle(100, 700, 100, 100);
            // add the diagonal
            cb.moveTo(100, 700);
            cb.lineTo(200, 800);
            // stroke the lines
            cb.stroke();
            
            // an example of some circles
            cb.setLineDash(3, 3, 0);
            cb.setRGBColorStrokeF(0f, 255f, 0f);
            cb.circle(150f, 500f, 100f);
            cb.stroke();
            
            cb.setLineWidth(5f);
            cb.resetRGBColorStroke();
            cb.circle(150f, 500f, 50f);
            cb.stroke();
            
            // example with colorfill
            cb.setRGBColorFillF(0f, 255f, 0f);
            cb.moveTo(100f, 200f);
            cb.lineTo(200f, 250f);
            cb.lineTo(400f, 150f);
            // because we change the fill color BEFORE we stroke the triangle
            // the color of the triangle will be red instead of green
            cb.setRGBColorFillF(255f, 0f, 0f);
            cb.closePathFillStroke();
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
