/****************************************************************
 **
 **  $Id: MaskedTextLine.java,v 1.10 1997/11/20 19:37:52 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/MaskedTextLine.java,v $
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

import java.util.Vector;

/**
 * MaskedTextLine
 * @version 1.1
 * @author DTAI, Incorporated
 */
class MaskedTextLine extends TextLine {

    private static final char nc = '\0'; // null character
    private String mask;
    private Vector specialChars;
    private Vector newLimitChars;

    /**
     * MaskedTextLine
     * @param mask
     */
    public MaskedTextLine(String mask) {
        setMask(mask);
    }

    /**
     * MaskedTextLine
     */
    public MaskedTextLine() {
    }

	/**
	 * getText
	 * @return ""
	 * @return String
	 */
	public String getText() {
	    if ( text.length() == 0 ) {
	        return "";
	    }
	    else {
    	    char chars[] = new char[text.length()];
    	    text.getChars(0,text.length(),chars,0);
    	    for ( int i = 0; i < chars.length; i++ ) {
    	        if ( chars[i] == nc ) {
    	            chars[i] = ' ';
    	        }
    	    }
    		return new String( chars );
    	}
	}

    /**
     * returns user inputted text without the masking characters
     * @return String
     */
    public String getInputText() {
        String mask = getMask();
        String text = getText();

        char[] maskChar = mask.toCharArray();
        char[] textChar = text.toCharArray();
        StringBuffer inputText = new StringBuffer();

        for ( int i=0; i<maskChar.length; i++ ) {
            if ( isLimitChar( maskChar[i] ) ) {
                inputText.append( textChar[i] );
            }
        }
        return inputText.toString().trim();
    }

    /**
     * set a special character that can be typed in anywhere
     * @param c
     */
    public void setSpecialChar( char c ) {
	    synchronized(getTreeLock()) {
            if ( specialChars == null ) {
                specialChars = new Vector();
            }
            specialChars.addElement( new Character( c ) );
        }
    }

    /**
     * specify a new limit character and its legal values
     * @param c
     * @param limits
     */
    public void setNewLimitChar( char c, String limits ) {
	    synchronized(getTreeLock()) {
            if ( newLimitChars == null ) {
                newLimitChars = new Vector();
            }
            newLimitChars.addElement( new LimitChar( c, limits ) );
        }
    }

    /**
     * isLimitChar
     * @param c
     * @return boolean result
     */
    public boolean isLimitChar( char c ) {
        if ( ( c == 'a' ) ||
             ( c == 'A' ) ||
             ( c == '9' ) ||
             ( c == 'x' ) ||
             ( c == 'X' ) ||
             ( c == 'c' ) ||
             ( c == 'C' ) ||
             isNewLimitChar( c ) ) {
            return true;
        }
        else {
            return false;
        }
	}

    /**
     * test if c is a new limit character
     * @param c
     * @return boolean result
     */
    public boolean isNewLimitChar( char c ) {
        if ( ! ( newLimitChars == null ) ) {
            for ( int i=0; i<newLimitChars.size(); i++ ) {
                if ( c == ( (LimitChar) newLimitChars.elementAt( i ) ).getChar() )
                    return true;
            }
        }
        return false;
    }

    /**
     * isGoodChar
     * @param limitChar
     * @param testChar
     * @return boolean result
     */
    public boolean isGoodChar( char limitChar, char testChar ) {
        if ( limitChar == 'a' ) {
            if ( ( ( testChar >= 'a' ) &&
                   ( testChar <= 'z' ) ) ||
                 ( ( testChar >= 'A' ) &&
                   ( testChar <= 'Z' ) ) ) {
                return true;
            }
        }
        else if ( limitChar == 'A' ) {
            if ( ( ( testChar >= 'a' ) &&
                   ( testChar <= 'z' ) ) ||
                 ( ( testChar >= 'A' ) &&
                   ( testChar <= 'Z' ) ) ) {
                return true;
            }
        }
        else if ( limitChar == '9' ) {
            if ( ( testChar >= '0' ) &&
                 ( testChar <= '9' ) ) {
                return true;
            }
        }
        else if ( limitChar == 'x' ) {
            if ( ( ( testChar >= 'a' ) &&
                   ( testChar <= 'z' ) ) ||
                 ( ( testChar >= 'A' ) &&
                   ( testChar <= 'Z' ) ) ||
                 ( ( testChar >= '0' ) &&
                   ( testChar <= '9' ) ) ) {
                return true;
            }
        }
        else if ( limitChar == 'X' ) {
            if ( ( ( testChar >= 'a' ) &&
                   ( testChar <= 'z' ) ) ||
                 ( ( testChar >= 'A' ) &&
                   ( testChar <= 'Z' ) ) ||
                 ( ( testChar >= '0' ) &&
                   ( testChar <= '9' ) ) ) {
                return true;
            }
        }
        else if ( limitChar == 'c' ) {
            return true;
        }
        else if ( limitChar == 'C' ) {
            return true;
        }
        else if ( isNewGoodChar( limitChar, testChar ) ) {
            return true;
        }
        return isSpecialChar( testChar );
    }

    /**
     * test if testChar is within limits of new limit char
     * @param limitChar
     * @param testChar
     * @return boolean result
     */
    protected boolean isNewGoodChar( char limitChar, char testChar ) {
        if ( ! ( newLimitChars == null ) ) {
            for ( int i=0; i<newLimitChars.size(); i++ ) {
                LimitChar newLimitChar = ( (LimitChar) newLimitChars.elementAt( i ) );

                if ( limitChar == newLimitChar.getChar() ) {
                    for ( int j=0; j<newLimitChar.getLimits().length(); j++ ) {
                        if ( testChar == newLimitChar.getLimits().charAt( j ) ) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * check if it's a specialChar
     * @param testChar
     * @return boolean result
     */
    public boolean isSpecialChar( char testChar ) {
        if (specialChars != null) {
            for (int i=0; i<specialChars.size(); i++) {
                if (testChar == ( ( Character )specialChars.elementAt( i ) ).charValue() ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * convertText
     * @param orig
     * @return String
     */
    private String convertText( String orig ) {

        if ( mask == null ) {
            return orig;
        }

        char chars[] = mask.toCharArray();

        int i;
        for ( i = 0;
              ( i < chars.length ) && ( i < orig.length() );
              i++ ) {

            char tc = orig.charAt( i );

            if ( isLimitChar( chars[i] ) ) {
                if ( isGoodChar( chars[i], tc ) ) {
                    if ( ( chars[i] == 'A' ) ||
                         ( chars[i] == 'C' ) ||
                         ( chars[i] == 'X' ) ) {
                        chars[i] = Character.toUpperCase( tc );
                    }
                    else {
                        chars[i] = tc;
                    }
                }
                else {
                    chars[i] = nc;
                }
            }
        }
        for ( ;
              i < chars.length;
              i++ ) {
            if ( isLimitChar( chars[i] ) ) {
                chars[i] = nc;
            }
        }
        return new String( chars );
    }

    /**
     * getMask
     * @return String
     */
    public String getMask() {
        return mask;
    }

	/**
	 * setMask
	 * @param mask
	 */
	public void setMask( String mask ) {
	    synchronized(getTreeLock()) {
    	    if ( mask == null ) {
    	        mask = "";
    	    }
    	    if ( ( this.mask == null ) ||
    	         ( ! this.mask.equals( mask ) ) ) {
                this.mask = mask;

    			setColumns( mask.length() );

                setText( getText() );
        	}
        }
    }

	/**
	 * setText
	 * @param text
	 */
	public void setText( String text ) {
	    synchronized(getTreeLock()) {
    	    super.setText( convertText( text ) );
    	}
	}


/**
 * deleteSelection
 */
	protected void deleteSelection() {
	    synchronized(getTreeLock()) {
    	    if ( selectionStart < selectionEnd ) {
    	        String str = text.toString();
    	        text = new StringBuffer();
    	        if ( selectionStart > 0 ) {
    	            text.append( str.substring( 0, selectionStart ) );
    	        }
    	        for ( int i = selectionStart; i < selectionEnd; i++ ) {
    	            if ( isLimitChar( mask.charAt( i ) ) ) {
    	                text.append( nc );
    	            }
    	            else {
    	                text.append( mask.charAt( i ) );
    	            }
    	        }
    	        if ( selectionEnd < str.length() ) {
    	            text.append( str.substring( selectionEnd, str.length() ) );
    	        }

    	        selectionEnd = selectionStart;
    	        caretPosition = selectionStart;
    	    }
    	}
	}

	/**
	 * resetCaretPosition
	 */
	private void resetCaretPosition() {
		synchronized(getTreeLock()) {
    	    while ( ( caretPosition < mask.length() ) &&
    	            ( ! isLimitChar( mask.charAt( caretPosition ) ) ) &&
    	            ( ( caretPosition == 0 ) ||
    	              ( ! isLimitChar( mask.charAt( caretPosition - 1 ) ) ) ) ) {
    	        caretPosition++;
    	    }
    	    while ( ( caretPosition > 0 ) &&
    	            ( text.charAt( caretPosition-1 ) == nc ) &&
    	            ( ( caretPosition >= mask.length() ) ||
    	              ( isLimitChar( mask.charAt( caretPosition ) ) ||
    	                ( text.charAt( caretPosition ) == nc ) ) ) ) {
    	        caretPosition--;
    	    }
    	    selectionStart = selectionEnd = caretPosition;
    	}
	}

	/**
	 * resetCaretPositionRight
	 * @param shiftDown
	 */
	protected void resetCaretPositionRight( boolean shiftDown ) {
		synchronized(getTreeLock()) {
		    boolean fromstart = caretPosition == selectionStart;
    	    while ( ( caretPosition < mask.length() ) &&
    	            ( ! isLimitChar( mask.charAt( caretPosition ) ) ) ) {
    	        caretPosition++;
    	    }
    	    while ( ( caretPosition > 0 ) &&
    	            ( text.charAt( caretPosition-1 ) == nc ) ) {
    	        caretPosition--;
    	    }
    	    if ( fromstart ) {
        	    if ( ! shiftDown ) {
        	        selectionEnd = caretPosition;
        	    }
        	    selectionStart = caretPosition;
    	    }
    	    else {
        	    if ( ! shiftDown ) {
        	        selectionStart = caretPosition;
        	    }
        	    selectionEnd = caretPosition;
        	}
    	}
	}

	/**
	 * resetCaretPositionLeft
	 * @param shiftDown
	 */
	private void resetCaretPositionLeft( boolean shiftDown ) {
		synchronized(getTreeLock()) {
		    boolean fromend = caretPosition == selectionEnd;
    	    while ( ( caretPosition > 0 ) &&
    	            ( ! isLimitChar( mask.charAt( caretPosition ) ) ) &&
    	            ( ! isLimitChar( mask.charAt( caretPosition-1 ) ) ) ) {
    	        caretPosition--;
    	    }
    	    while ( ( caretPosition > 0 ) &&
    	            ( text.charAt( caretPosition-1 ) == nc ) ) {
    	        caretPosition--;
    	    }
    	    if ( fromend ) {
        	    if ( ! shiftDown ) {
        	        selectionStart = caretPosition;
        	    }
        	    selectionEnd = caretPosition;
    	    }
    	    else {
        	    if ( ! shiftDown ) {
        	        selectionEnd = caretPosition;
        	    }
        	    selectionStart = caretPosition;
        	}
        }
	}

	/**
	 * moveLeft
	 * @param ctrlDown
	 * @param shiftDown
	 */
	protected void moveLeft( boolean ctrlDown, boolean shiftDown ) {
		synchronized(getTreeLock()) {
    	    super.moveLeft( ctrlDown, shiftDown );
    	    resetCaretPositionLeft(shiftDown);
    	}
	}

	/**
	 * moveRight
	 * @param ctrlDown
	 * @param shiftDown
	 */
	protected void moveRight( boolean ctrlDown, boolean shiftDown ) {
		synchronized(getTreeLock()) {
    	    super.moveRight( ctrlDown, shiftDown );
            if ( selectionStart == selectionEnd ) {
        	    resetCaretPosition();
        	}
    	    while ( ( caretPosition < mask.length() ) &&
    	            ( text.charAt( caretPosition ) == nc ) ) {
    	        caretPosition++;
    	    }
    	    resetCaretPositionRight(shiftDown);
    	}
	}

	/**
	 * typeChar
	 * @param c
	 */
	protected void typeChar( char c ) {
		synchronized(getTreeLock()) {
    	    deleteSelection();
    	    resetCaretPositionRight(false);
    	    if ( caretPosition < mask.length() ) {
    	        char mc = mask.charAt( caretPosition );
        	    if ( isGoodChar( mc, c ) ) {
                    if ( ( mc == 'A' ) ||
                         ( mc == 'C' ) ||
                         ( mc == 'X' ) ) {
                        text.setCharAt( caretPosition, Character.toUpperCase( c ) );
                    }
                    else {
                        text.setCharAt( caretPosition, c );
                    }
            	    select( caretPosition+1 );
                }
            }
    	    repaint();
    	}
	}

    /**
     * paint
     * @param g
     */
    public void paint( GadgetGraphics g ) {
        if ( selectionStart == selectionEnd ) {
            resetCaretPosition();
        }
        else if ( ! mouseDown ) {
            boolean selectedNothing = true;
            for ( int i = selectionStart; i < selectionEnd; i++ ) {
	            if ( ( text.charAt( i ) != nc ) &&
	                 ( isLimitChar( mask.charAt( i ) ) ) ) {
	                selectedNothing = false;
	                break;
	            }
            }
            if ( selectedNothing ) {
                caretPosition = selectionStart;
                resetCaretPosition();
            }
        }
        super.paint( g );
    }

    /**
     * backspaceKey
     */
    protected void backspaceKey() {
		synchronized(getTreeLock()) {
            super.backspaceKey();
    	    resetCaretPositionLeft(false);
    	}
    }

    /**
     * deleteKey
     */
    protected void deleteKey() {
		synchronized(getTreeLock()) {
            super.deleteKey();
    	    resetCaretPositionRight(false);
    	    resetCaretPosition();
    	    int i = caretPosition;
    	    while ( ( text.charAt( i ) == nc ) &&
    	            ( i < ( mask.length() - 1 ) ) &&
    	            ( mask.charAt( i ) == mask.charAt( i+1 ) ) ) {
    	        text.setCharAt( i, text.charAt( i+1 ) );
    	        text.setCharAt( i+1, nc );
    	        i++;
    	    }
    	}
    }
}


/**
 * class to hold limit character and its legal values
 * @version 1.1
 * @author DTAI, Incorporated
 */
class LimitChar {
    private char c;
    private String limits;

    /**
     * LimitChar
     * @param c
     * @param limits
     */
    LimitChar (char c, String limits) {
        this.c = c;
        this.limits = limits;
    }

    /**
     * getChar
     * @return char
     */
    protected char getChar() {
        return c;
    }

    /**
     * getLimits
     * @return String
     */
    protected String getLimits() {
        return limits;
    }


}
