/****************************************************************
 **  Copyright 1996 by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: FontFaceChoice.java,v 1.2 1998/01/21 20:08:18 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/FontFaceChoice.java,v $
 **
 ****************************************************************/

package dtai.gwt;

/**
 * The FontFaceChoice class gives the user
 * font choices.
 *
 * @author		DTAI, Inc.
 */
public class FontFaceChoice extends ChoiceGadget {

	/**
	 * Constructor sets the number of visible choices.
	 */
    public FontFaceChoice() {
        super(5);
        setSorted(true);
        fillList();
    }

    private void fillList() {
	    add("Helvetica");
	    add("TimesRoman");
	    add("Courier");
	    add("Dialog");
	    add("DialogInput");
    }

	/**
	 * Returns the name of the current font.
	 * @return		current font as a String
	 */
    public String getName() {
        return getText();
    }

	/**
	 * Selects the given font.
	 * @param name		the String name of the font to be selected
	 */
    public void setName(String name) {
        select(name);
    }
}
