/****************************************************************
 **
 **  $Id: MaskedTextFieldGadget.java,v 1.20 1997/11/20 19:37:52 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/MaskedTextFieldGadget.java,v $
 **
 ****************************************************************
 **
 **  Gadget Windowing Toolkit (GWT) Java Class Library
 **  Copyright (C) 1997  DTAI, Incorporated (http://www.dtai.com)
 **
 **  This library is free software; you can redistribute it and/or
 **  modify it under the terms of the GNU Library General Public
 **  License as published by the Free Software Foundation; either
 **  version 2 of the License, or (at your option) any later version.
 **
 **  This library is distributed in the hope that it will be useful,
 **  but WITHOUT ANY WARRANTY; without even the implied warranty of
 **  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 **  Library General Public License for more details.
 **
 **  You should have received a copy of the GNU Library General Public
 **  License along with this library (file "COPYING.LIB"); if not,
 **  write to the Free Software Foundation, Inc.,
 **  59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Font;

/**
 * a = for any alphabetical character
 * A = for any alphabetical character, convert lower case to uppercase
 * 9 = for numbers only
 * x = for any alphanumeric character
 * X = for any alphanumeric character, convert lower case to uppercase
 * c = any keyboard character
 * C = any keyboard character, convert lower case to uppercase
 * You can define special characters that can be typed in anywhere
 * You can specify limit characters and corresponding legal values (no case conversion)
 * all other characters, such as (-, + $) are treated as masking characters
 * These codes are then used to create an input mask.  For example an US Social
 * Security Number mask would be entered as 999-99-9999.  The nines stand for
 * numbers while the hyphens are used as masking characters that are automatically
 * displayed.
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class MaskedTextFieldGadget extends TextFieldGadget {

    private MaskedTextLine line;
    private Font newFont;

	/**
	 * Constructs a Text with no text or mask.
	 */
	public MaskedTextFieldGadget() {
		this( "", "" );
	}

	/**
	 * Constructs a Text with the specified mask and no text.
	 * @param mask - the mask of the button
	 */
	public MaskedTextFieldGadget( String mask ) {
	    this( mask, ""  );
	}

	/**
	 * Constructs a Text with the specified text and mask.
	 *
	 * @param mask - the mask of the button
	 * @param text - the text of the button
	 */
	public MaskedTextFieldGadget( String mask, String text ) {
	    this( mask, text, new MaskedTextLine() );
        setAllowedToShrink( false );
	}

	/**
	 * MaskedTextFieldGadget
	 * @param mask - the mask of the button
	 * @param text - the text of the button
	 * @param line - the line of masked text
	 */
	protected MaskedTextFieldGadget( String mask, String text, MaskedTextLine line ) {
	    super( text, mask.length(), line );
	    this.line = line;
	    line.setMask( mask );
	    setText(text);
	}

	/**
	 * getFont
	 * @param g		the GadgetGraphics object
	 * @return Font
	 */
	public Font getFont(GadgetGraphics g) {
	    Font currentFont = super.getFont(g);
	    if ( font != null ) {
	        return currentFont;
	    }
        if ( ! currentFont.equals( newFont ) ) {
            newFont = new Font( "Courier", currentFont.getStyle(), currentFont.getSize() );
        }
        return newFont;
	}

	/**
	 * Gets the text without mask characters.
	 * @see setText
	 * @return  the unmaksed text
	 */
	public String getInputText() {
		return line.getInputText();
	}

	/**
	 * Gets the mask of the button.
	 * @see setText
	 * @return the mask
	 */
	public String getMask() {
		return line.getMask();
	}

	/**
	 * Sets the mask.
	 * @param mask - the mask
	 * @see getMask
	 */
	public void setMask( String mask ) {
	    synchronized(getTreeLock()) {
    	    line.setMask(mask);
    	}
    }

    /**
     * set a character that can be anywhere
     * @param c		new special character
     */
    public void setSpecialChar( char c ) {
        line.setSpecialChar(c);
    }

    /**
     * specify a new limit character and its legal values
     * @param c			new limit character
     * @param limits	legal values for limit character
     */
    public void setNewLimitChar( char c, String limits ) {
        line.setNewLimitChar(c,limits);
    }
}
