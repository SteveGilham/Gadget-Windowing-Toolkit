/****************************************************************
 **  Copyright 1996 by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: FontStyleChoice.java,v 1.2 1998/01/21 20:08:18 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/FontStyleChoice.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The FontStyleChoice class provides bold and italic
 * font styles, maintained in a hashtable.
 *
 * @author		DTAI, Inc.
 */
public class FontStyleChoice extends ChoiceGadget {

    private Hashtable styles = new Hashtable();

	/**
	 * Constructor sets the visible style choices.
	 */
    public FontStyleChoice() {
        super(4);
        setSorted(true);
        initFontStyles();
        fillList();
    }

    private void initFontStyles() {
	    styles.put("Plain", new Integer(Font.PLAIN));
	    styles.put("Bold", new Integer(Font.BOLD));
	    styles.put("Italic", new Integer(Font.ITALIC));
	    styles.put("Bold Italic", new Integer(Font.BOLD | Font.ITALIC));
    }

    private void fillList() {
	    add("Plain");
	    add("Bold");
	    add("Italic");
	    add("Bold Italic");
    }

	/**
	 * Returns the current font style.
	 * @return		the current font style
	 */
    public int getStyleValue() {
        Integer styleInteger = (Integer)styles.get(getText());
        if ( styleInteger == null ) {
            return Font.PLAIN;
        }
        return styleInteger.intValue();
    }

	/**
	 * Selects the given font style from the hashtable of
	 * valid choices.
	 * @param newFontStyle		the chosen font style
	 */
    public void setStyleValue( int newFontStyle ) {
        for ( Enumeration e = styles.keys(); e.hasMoreElements(); ) {
            String name = (String)e.nextElement();
            int style = ((Integer)styles.get(name)).intValue();
            if ( style == newFontStyle )  {
                select(name);
                return;
            }
        }
    }
}
