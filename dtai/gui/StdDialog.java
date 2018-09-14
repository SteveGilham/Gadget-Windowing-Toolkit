/****************************************************************
 **
 **  $Id: StdDialog.java,v 1.26 1998/03/01 03:03:50 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gui/StdDialog.java,v $
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

package dtai.gui;

import dtai.gwt.BorderGadget;
import dtai.gwt.ButtonGadget;
import dtai.gwt.ContainerGadget;
import dtai.gwt.Gadget;
import dtai.gwt.GadgetBorderLayout;
import dtai.gwt.GadgetDialog;
import dtai.gwt.GadgetFlowLayout;
import dtai.gwt.GadgetGridLayout;
import dtai.gwt.GadgetLayoutManager;
import dtai.gwt.PanelGadget;
import dtai.gwt.SeparatorGadget;
import java.awt.Frame;
import java11.awt.event.WindowEvent;

/**
 * StdDialog - superclass for most DTAI dialogs
 *
 * @version	1.0b1
 * @author	DTAI, Incorporated
 *
 * $Id: StdDialog.java,v 1.26 1998/03/01 03:03:50 kadel Exp $
 *
 * $Source: /cvs/classes/dtai/gui/StdDialog.java,v $
 */

public class StdDialog extends GadgetDialog {

    private BorderGadget buttons;
    private BorderGadget body;
    private boolean separatorAdded = false;
    private PanelGadget buttonArea;

	/**
	 *
	 * StdDialog
	 * @param frame		the Frame for this dialog
	 * @param name		the String name of this dialog
	 * @param modal		is/is not modal
	 */
    public StdDialog ( Frame frame, String name, boolean modal ) {
        this(frame, name, modal, false);
    }

	/**
	 *
	 * StdDialog
	 * @param frame		the Frame for this dialog
	 * @param name		the String name of this dialog
	 * @param modal		is/is not modal
     * @param trueModal show() does not return to caller until hide() is called
	 */
    public StdDialog ( Frame frame, String name, boolean modal, boolean trueModal ) {

        super( frame, name, modal, trueModal );
        setLayout( new GadgetBorderLayout() );
        buttonArea = new PanelGadget();
        add( "South", buttonArea );

        buttonArea.setLayout ( new GadgetBorderLayout( 0, 5 ) );
        PanelGadget buttonArea2 = new PanelGadget();
        buttonArea2.setLayout ( new GadgetFlowLayout() );

        buttons = new BorderGadget();
        buttons.setLayout ( new GadgetGridLayout( 1, 0, 3, 3, 75, 0 ) );
        buttonArea2.add( buttons );
        buttonArea.add( "South", buttonArea2 );

        body = new BorderGadget();
        body.setLayout( new GadgetBorderLayout() );
		body.setMargins(10);
        add( "Center", body );
    }

	/**
	 * Adds the specified button.
	 * @param button		the button to be added
	 */
    public void addButtonGadget( ButtonGadget button ) {
        addSeparator();
        buttons.add( button );
    }
    
    public void show() {
        super.show();
        flushGraphics();
    }

	/**
	 * Adds the specified button.
	 */
    public void insertButtonGadget( ButtonGadget button, int position ) {
        addSeparator();
        buttons.add( button, position );
    }

	/**
	 * Adds the Separator gagdet if needed.
	 * @param button		the button to be added
	 */
    private void addSeparator() {
        /* THIS IS NOT LOOKING RIGHT... IS THIS LIKE WIN 95/NT? --Rich
        if (!separatorAdded) {
		    SeparatorGadget	sg = new SeparatorGadget();
		    sg.setVMargin( 2 );
		    sg.setHMargin( 10 );
            buttonArea.add( "Center", sg );
            separatorAdded = true;
        }
        */
    }

	/**
	 * Returns the body.
	 * @return body
	 * Returns the body of the border gadget
	 * @return  the body of the border gadget
	 */
    public BorderGadget getBody() {
        return body;
    }

	/**
	 * Returns the ButtonArea.
	 * @return buttonArea
	 */
    public PanelGadget getButtonArea() {
        return buttonArea;
    }

	/**
	 * If the parent's event handling returns false, hide the
	 * dialog.
	 * @param window		a WindowEvent
	 */
    public void processWindowEvent( WindowEvent window ) {
        if ( window.getID() == WindowEvent.WINDOW_CLOSING ) {
            hide();
        }
        super.processWindowEvent( window );
    }
}
