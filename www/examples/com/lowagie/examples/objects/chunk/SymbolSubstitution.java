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

package com.lowagie.examples.objects.chunk;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Shows how special characters are substituted with Phrase.getInstance.
 * 
 * @author blowagie
 */

public class SymbolSubstitution {

	/**
	 * How to substiture special characters with Phrase.getInstance.
	 * 
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {

		System.out.println("Symbol Substitution");

		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer that listens to the document
			PdfWriter.getInstance(document,
					new FileOutputStream("SymbolSubstitution.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
            document.add(Phrase.getInstance("What is the " + (char) 945 + "-coefficient of the "
                    + (char) 946 + "-factor in the " + (char) 947 + "-equation?\n"));
                    for (int i = 913; i < 970; i++) {
                        document.add(Phrase.getInstance(" " + String.valueOf(i) + ": " + (char) i));
                    }
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}