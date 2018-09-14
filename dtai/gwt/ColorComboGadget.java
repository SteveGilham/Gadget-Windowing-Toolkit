/****************************************************************
 **
 **  $Id: ColorComboGadget.java,v 1.2 1997/08/06 23:27:01 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/ColorComboGadget.java,v $
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

import dtai.gwt.ChoiceGadget;
import dtai.gwt.ComboBoxGadget;
import dtai.gwt.Gadget;
import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The ColorCombo class provides color choices
 * for the user, maintained in a hashtable.
 *
 * @author		DTAI, Inc.
 */
public class ColorComboGadget extends ChoiceGadget {

    private Hashtable colors = new Hashtable();

	/**
	 * Constructor sets the number of visible choices and
	 * initializes colors list.
	 */
    public ColorComboGadget() {
        super(5);
        setSorted(true);
        initColors();
        fillList();
        colors.put( "Black", Color.black );
        colors.put( "White", Color.white );
    }

    private void initColors() {
        colors.put( "Blue", Color.blue );
        colors.put( "Brown", new Color(128, 0, 0));
        colors.put( "Cyan", Color.cyan );
        colors.put( "Dark Gray", Color.darkGray );
        colors.put( "Gray", Color.gray );
        colors.put( "Green", Color.green );
        colors.put( "Light Gray", Color.lightGray );
        colors.put( "Magenta", Color.magenta );
        colors.put( "Orange", Color.orange );
        colors.put( "Pink", Color.pink );
        colors.put( "Red", Color.red );
        colors.put( "Yellow", Color.yellow );
    }

    private void fillList() {
        add("Black");
        add("White");
        for ( Enumeration e = colors.keys(); e.hasMoreElements(); ) {
            String name = (String)e.nextElement();
            add(name);
        }
    }

	/**
	 * Gets the color from the colors hashtable, else returns
	 * the default gadget color.
	 * @return the color
	 */
    public Color getColor() {
        String name = getText().trim();
        if ( name.length() > 0 ) {
            if ( Character.isLowerCase(name.charAt(0)) ) {
                name = Character.toUpperCase(name.charAt(0))+name.substring(1);
            }
            int space = name.indexOf(' ');
            if ( space > 0 ) {
                if ( Character.isLowerCase(name.charAt(space+1)) ) {
                    name = name.substring(0,space+1)+
                           Character.toUpperCase(name.charAt(space+1))+
                           name.substring(space+2);
                }
            }
        }
        Color color = (Color)colors.get(name);
        if ( color != null ) {
            return color;
        }
        name = name.trim();
        int offset=0;
        if ( name.length() > 0 ) {
            if ( name.charAt( 0 ) == '#' ) {
                offset=1;
            }
            try {
                int r = Integer.valueOf( name.substring( offset, offset+2 ), 16 ).intValue();
                int g = Integer.valueOf( name.substring( offset+2, offset+4 ), 16 ).intValue();
                int b = Integer.valueOf( name.substring( offset+4, offset+6 ), 16 ).intValue();
                return new Color( r, g, b );
            }
            catch ( Exception e ) {
            }
        }
        return Gadget.defaultColor;
    }

	/**
	 * Selects the given color from the hashtable, if it can be found
	 * there.  Otherwise, it is decomposed into its red, green, and blue
	 * components and stored via it's ingredients.
	 * @param newColor	the new color
	 */
    public void setColor( Color newColor ) {
        for ( Enumeration e = colors.keys(); e.hasMoreElements(); ) {
            String name = (String)e.nextElement();
            Color color = (Color)colors.get(name);
            if ( ( color == newColor ) ||
                 ( ( color != null ) &&
                   newColor.equals(color) ) ) {
                select(name);
                return;
            }
        }
        setText( "#"+
                 Integer.toString( newColor.getRed(), 16 )+
                 Integer.toString( newColor.getGreen(), 16 )+
                 Integer.toString( newColor.getBlue(), 16 ) );
    }

    public Hashtable getColorsHashtable() {

        return colors;

    }

}
