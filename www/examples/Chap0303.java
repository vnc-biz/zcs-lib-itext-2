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
 * itext@lowagie.com
 */

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.html.HtmlWriter;

public class Chap0303 {
    
    public static void main(String[] args) {
        
        System.out.println("Chapter 3 example 3: Annotations");
        
        // step 1: creation of a document-object
        Document document = new Document();
        
        try {
            
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            PdfWriter.getInstance(document, new FileOutputStream("Chap0303.pdf"));
            
            // step 3: we open the document
            document.open();
            
            // step 4:
            
            List list = new List(true, 20);
            list.add(new ListItem("First line"));
            list.add(new ListItem("The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?"));
            list.add(new ListItem("Third line"));
            document.add(list);
            
            document.add(new Paragraph("some books I really like:"));
            document.add(new Annotation("books", "This is really a very short list, I like a lot of books."));
            ListItem listItem;
            list = new List(true, 15);
            listItem = new ListItem("When Harlie was one", FontFactory.getFont(FontFactory.TIMES_NEW_ROMAN, 12));
            listItem.add(new Chunk(" by David Gerrold", FontFactory.getFont(FontFactory.TIMES_NEW_ROMAN, 11, Font.ITALIC)));
            list.add(listItem);
            listItem = new ListItem("The World according to Garp", FontFactory.getFont(FontFactory.TIMES_NEW_ROMAN, 12));
            listItem.add(new Chunk(" by John Irving", FontFactory.getFont(FontFactory.TIMES_NEW_ROMAN, 11, Font.ITALIC)));
            list.add(listItem);
            listItem = new ListItem("Decamerone", FontFactory.getFont(FontFactory.TIMES_NEW_ROMAN, 12));
            listItem.add(new Chunk(" by Giovanni Boccaccio", FontFactory.getFont(FontFactory.TIMES_NEW_ROMAN, 11, Font.ITALIC)));
            list.add(listItem);
            document.add(list);
            
            document.add(new Phrase("Some authors I really like:"));
            document.add(new Annotation("authors", "Maybe it's because I wanted to be an writer myself that I wrote iText."));
            list = new List(false, 20);
            list.setListSymbol(new Chunk("*", FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD)));
            listItem = new ListItem("Isaac Asimov");
            list.add(listItem);
            List sublist;
            sublist = new List(true, 10);
            sublist.setListSymbol(new Chunk("", FontFactory.getFont(FontFactory.HELVETICA, 8)));
            sublist.add("The Foundation Trilogy");
            sublist.add("The Complete Robot");
            sublist.add("Caves of Steel");
            sublist.add("The Naked Sun");
            list.add(sublist);
            listItem = new ListItem("John Irving");
            list.add(listItem);
            sublist = new List(true, 10);
            sublist.setListSymbol(new Chunk("", FontFactory.getFont(FontFactory.HELVETICA, 8)));
            sublist.add("The World according to Garp");
            sublist.add("Hotel New Hampshire");
            sublist.add("A prayer for Owen Meany");
            sublist.add("Widow for a year");
            list.add(sublist);
            listItem = new ListItem("Kurt Vonnegut");
            list.add(listItem);
            sublist = new List(true, 10);
            sublist.setListSymbol(new Chunk("", FontFactory.getFont(FontFactory.HELVETICA, 8)));
            sublist.add("Slaughterhouse 5");
            sublist.add("Welcome to the Monkey House");
            sublist.add("The great pianola");
            sublist.add("Galapagos");
            list.add(sublist);
            document.add(list);
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
