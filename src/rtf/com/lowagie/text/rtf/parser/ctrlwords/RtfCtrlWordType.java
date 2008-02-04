/* $Id$
 * $Name$
 *
 * Copyright 2007 by Howard Shank (hgshank@yahoo.com)
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.text.rtf.parser.ctrlwords;
/**
 * <code>RtfCtrlWordType</code> indicates the type of control word.
 * 
 * RTF control words are divided up into:
 * 	Destination, Flag, Value, Toggle, Symbol.
 *
 * Destination: The current destination for values and text to be sent.
 * Flag: 0/1 value types. Represents true/false, on/off value types.
 * Toggle: Flips a Flag value on/off.
 * Value: an Integer value data type. (Exception: Some control words this is a long data value type)
 * Symbol: Special RTF characters such as \{, \} and others.
 * 
 * @author Howard Shank (hgshank@yahoo.com)
 * @since 2.0.8
 */
public final class RtfCtrlWordType {
	/**
	 * Control word is unidentified.
	 */
	public static final int UNIDENTIFIED = -1;
	/**
	 * Control word is a destination.
	 */
	public static final int DESTINATION = 0;
	/**
	 * Control word is a newer destination.
	 */
	public static final int DESTINATION_EX = 1;
	/**
	 * Control word is a flag.
	 */
	public static final int FLAG = 2;
	/**
	 * Control word is a value.
	 */
	public static final int VALUE = 3;
	/**
	 * Control word is a flag toggle.
	 */
	public static final int TOGGLE = 4;
	/**
	 * Control word is a special symbol.
	 */
	public static final int SYMBOL = 5;
}
