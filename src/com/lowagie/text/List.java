/*
 * $Id$
 * $Name$
 * 
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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

package com.lowagie.text;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A <CODE>List</CODE> contains several <CODE>ListItem</CODE>s.
 * <P>
 * <B>Example 1:</B>
 * <BLOCKQUOTE><PRE>
 * <STRONG>List list = new List(true, 20);</STRONG>
 * <STRONG>list.add(new ListItem("First line"));</STRONG>
 * <STRONG>list.add(new ListItem("The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?"));</STRONG>
 * <STRONG>list.add(new ListItem("Third line"));</STRONG>
 * </PRE></BLOCKQUOTE> 
 *
 * The result of this code looks like this:
 *	<OL>
 *		<LI>
 *			First line
 *		</LI>
 *		<LI>
 *			The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?
 *		</LI>
 *		<LI>
 *			Third line
 *		</LI>
 *	</OL>
 *	
 * <B>Example 2:</B>
 * <BLOCKQUOTE><PRE>
 * <STRONG>List overview = new List(false, 10);</STRONG>
 * <STRONG>overview.add(new ListItem("This is an item"));</STRONG>
 * <STRONG>overview.add("This is another item");</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * The result of this code looks like this:
 *	<UL>
 *		<LI>
 *			This is an item
 *		</LI>
 *		<LI>
 *			This is another item
 *		</LI>
 *	</UL>
 *
 * @see		Element
 * @see		ListItem
 *
 * @author  bruno@lowagie.com
 */

public class List implements Element {

// membervariables

	/** This is the <CODE>ArrayList</CODE> containing the different <CODE>ListItem</CODE>s. */
	private ArrayList list = new ArrayList();

	/** This variable indicates if the list has to be numbered. */
	private boolean numbered;

	/** This variable indicates the first number of a numbered list. */
	private int first = 1;

	/** This is the listsymbol of a list that is not numbered. */
	private Chunk symbol = new Chunk("-");

	/** The indentation of this list on the left side. */
	private int indentationLeft;

	/** The indentation of this list on the right side. */
	private int indentationRight;

	/** The indentation of the listitems. */
	private int symbolIndent;

// constructors

	/**
	 * Constructs a <CODE>List</CODE>.
	 * <P>
	 * Remark: the parameter <VAR>symbolIndent</VAR> is important for instance when
	 * generating PDF-documents; it indicates the indentation of the listsymbol.
	 * It is not important for HTML-documents.
	 *
	 * @param	numbered		a boolean
	 * @param	symbolIndent	the indentation that has to be used for the listsymbol
	 */

	public List(boolean numbered, int symbolIndent) {
		this.numbered = numbered;
		this.symbolIndent = symbolIndent;
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to an
	 * <CODE>ElementListener</CODE>. 
     *
	 * @param	listener	an <CODE>ElementListener</CODE>
	 * @return	<CODE>true</CODE> if the element was processed successfully
     */

    public boolean process(ElementListener listener) {
		try {
			for (Iterator i = list.iterator(); i.hasNext(); ) {
				listener.add((Element) i.next());
			}
			return true;
		}
		catch(DocumentException de) {
			return false;
		}
	}

    /**
     * Gets the type of the text element. 
     *
     * @return	a type
     */

    public int type() {
		return Element.LIST;
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
     */

    public ArrayList getChunks() {
		 ArrayList tmp = new ArrayList();
		 for (Iterator i = list.iterator(); i.hasNext(); ) {
			 tmp.addAll(((Element) i.next()).getChunks());
		 }
		 return tmp;
	}

// methods to set the membervariables

	/**
	 * Adds a <CODE>ListItem</CODE> to the <CODE>List</CODE>.
	 * 
	 * @param	item	the item to add.
	 */

	public void add(ListItem item) {
		if (numbered) {
			Chunk chunk = new Chunk(String.valueOf(first + list.size()));
			chunk.append(".");
			item.setListSymbol(chunk);
		}
		else {
			item.setListSymbol(symbol);
		}
		item.setIndentationLeft(symbolIndent);
		item.setIndentationRight(0);
		list.add(item);
	}

	/**
	 * Adds a (nested) <CODE>List</CODE> to the <CODE>List</CODE>.
	 * 
	 * @param	nested		the list to add.
	 */

	public void add(List nested) {
		nested.setIndentationLeft(nested.indentationLeft() + symbolIndent);
		first--;
		list.add(nested);
	}

	/**
	 * Adds a <CODE>ListItem</CODE> to the <CODE>List</CODE>.
	 * <P>
	 * This is a shortcut for <CODE>add(ListItem item)</CODE>.
	 *
	 * @param	item	a <CODE>String</CODE>
	 */

	public void add(String item) {
		this.add(new ListItem(item));
	}

	/**
	 * Sets the indentation of this paragraph on the left side.
	 *
	 * @param	indentation		the new indentation
	 */

	public final void setIndentationLeft(int indentation) {
		this.indentationLeft = indentation;
	}

	/**
	 * Sets the indentation of this paragraph on the right side.
	 *
	 * @param	indentation		the new indentation
	 */

	public final void setIndentationRight(int indentation) {
		this.indentationRight = indentation;
	}

	/**
	 * Sets the number that has to come first in the list.
	 *
	 * @param	first		a number
	 */

	public final void setFirst(int first) {
		this.first = first;
	}

	/**
	 * Sets the listsymbol.
	 *
	 * @param	symbol		a <CODE>Chunk</CODE>
	 */

	public final void setListSymbol(Chunk symbol) {
		this.symbol = symbol;
	}

	/**
	 * Sets the listsymbol.
	 * <P>
	 * This is a shortcut for <CODE>setListSymbol(Chunk symbol)</CODE>.
	 *
	 * @param	symbol		a <CODE>String</CODE>
	 */

	public final void setListSymbol(String symbol) {
		this.symbol = new Chunk(symbol);
	}

// methods to retrieve information

	/**
	 * Gets all the items in the list.
	 *
	 * @return	an <CODE>ArrayList</CODE> containing <CODE>ListItem</CODE>s.
	 */

	public ArrayList getItems() {
		return list;
	}

	/**
	 * Gets the size of the list.
	 *
	 * @return	a <CODE>size</CODE>
	 */

	public int size() {
		return list.size();
	}

	/**
	 * Gets the leading of the first listitem.
	 *
	 * @return	a <CODE>leading</CODE>
	 */

	public int leading() {
		if (list.size() < 1) {
			return -1;
		}
		ListItem item = (ListItem) list.get(0);
		return item.leading();
	}

	/**
	 * Checks if the list is numbered.
	 * 
	 * @return	<CODE>true</CODE> if the list is numbered, <CODE>false</CODE> otherwise.
	 */

	public final boolean isNumbered() {
		return numbered;
	}

	/**
	 * Gets the indentation of this paragraph on the left side.
	 *
	 * @return	the indentation
	 */

	public final int indentationLeft() {
		return indentationLeft;
	}

	/**
	 * Gets the indentation of this paragraph on the right side.
	 *
	 * @return	the indentation
	 */

	public final int indentationRight() {
		return indentationRight;
	}

	/**
	 * Returns a representation of this <CODE>Paragraph</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<LIST TYPE=\"");
		if (!numbered) {
			buf.append("NOT");
		}
		buf.append("NUMBERED\">\n");
		if (numbered) {
			buf.append("\t<FIRST>");
			buf.append(first);
			buf.append("</FIRST>\n");
		}
		else {
			buf.append("\t<SYMBOL>");
			buf.append(symbol.toString());
			buf.append("</SYMBOL>\n");
		}
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			buf.append(i.next().toString());
		}
		buf.append("\n</LIST>\n");								
		return buf.toString();
	}
}