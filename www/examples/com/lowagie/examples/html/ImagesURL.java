/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * --> Copyright 2001-2005 by Bruno Lowagie <--
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
package com.lowagie.examples.html;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.HtmlWriter;

/**
 * Images example that uses the complete URL of the Image.
 */
public class ImagesURL {
    
    /**
     * Images example with a complete path to the images.
     * @param args	no arguments needed
     */
    public static void main(String[] args) {
        
        System.out.println("Images with complete URL");
        
        // step 1: creation of a document-object
        Document document = new Document();
        
        try {
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            HtmlWriter.getInstance(document, new FileOutputStream("images.html"));
            
            // step 3: we open the document
            document.open();
            
            // step 4:
            document.add(new Paragraph("A picture of my dog: otsoe.jpg"));
            Image jpg = Image.getInstance(new URL("http://itext.sourceforge.net/tutorial/html/otsoe.jpg"));
            document.add(jpg);
            document.add(new Paragraph("getacro.gif"));
            Image gif= Image.getInstance(new URL("http://itext.sourceforge.net/tutorial/html/getacro.gif"));
            document.add(gif);
            document.add(new Paragraph("pngnow.png"));
            Image png = Image.getInstance(new URL("http://itext.sourceforge.net/tutorial/html/pngnow.png"));
            document.add(png);
            document.add(new Paragraph("iText.bmp"));
            Image bmp = Image.getInstance(new URL("http://itext.sourceforge.net/tutorial/html/iText.bmp"));
            document.add(bmp);
            document.add(new Paragraph("iText.wmf"));
            Image wmf = Image.getInstance(new URL("http://itext.sourceforge.net/tutorial/html/iText.wmf"));
            document.add(wmf);
            document.add(new Paragraph("iText.tif"));
            Image tiff = Image.getInstance(new URL("http://itext.sourceforge.net/tutorial/html/iText.tif"));
            document.add(tiff);
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