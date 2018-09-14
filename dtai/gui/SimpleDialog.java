/****************************************************************
 **
 **  $Id: SimpleDialog.java,v 1.17 1998/03/01 03:03:50 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gui/SimpleDialog.java,v $
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
import dtai.gwt.DisplayGadget;
import dtai.gwt.Gadget;
import dtai.gwt.GadgetDialog;
import dtai.gwt.LabelGadget;
import java.awt.Frame;
import java.util.Vector;
import java11.awt.event.ActionEvent;
import java11.awt.event.ActionListener;
import java11.util.EventObject;

/**
 * SimpleDialog - superclass for error, warning, info, etc. dialogs
 *
 * @version	1.0b1
 * @author	DTAI, Incorporated
 *
 * $Id: SimpleDialog.java,v 1.17 1998/03/01 03:03:50 kadel Exp $
 *
 * $Source: /cvs/classes/dtai/gui/SimpleDialog.java,v $
 */

public class SimpleDialog extends StdDialog implements ActionListener {

    private Vector  labels = new Vector();
    private LabelGadget messageLabel;

    /**
     * SimpleDialog constructor
     * @param frame  the dialog box frame
     * @param dialog_header  the dialog box header
     */
    protected SimpleDialog( Frame  frame, String dialog_header ) {
        this( frame, dialog_header, true );
    }

    /**
     * SimpleDialog constructor
     * @param frame  the dialog box frame
     * @param dialog_header  the dialog box header
     * @param modal  is/is not modal
     */
    protected SimpleDialog( Frame  frame, String dialog_header, boolean modal ) {
        this(frame, dialog_header, modal, false);
    }

    /**
     * SimpleDialog constructor
     * @param frame  the dialog box frame
     * @param dialog_header  the dialog box header
     * @param modal  is/is not modal
     * @param trueModal  show() does not return to caller until hide() is called
     */
    protected SimpleDialog( Frame  frame, String dialog_header, boolean modal, boolean trueModal ) {
        super( frame, dialog_header, modal, trueModal );
        messageLabel = new LabelGadget();
        messageLabel.setWrapLength( 40 );
        setLeftJustified();
        getBody().add( "Center", messageLabel );
    }

    /**
     * SimpleDialog constructor
     * @param frame  the dialog box frame
     * @param dialog_header  the dialog box header
     * @param defaultButton  the default button
     */
    protected SimpleDialog( Frame  frame, String dialog_header,
							String defaultButton ) {
	    this(frame,dialog_header,defaultButton,true);
	}

    /**
     * SimpleDialog constructor
     * @param frame  the dialog box frame
     * @param dialog_header  the dialog box header
     * @param defaultButton  the default button
     * @param modal  is/is not modal
     */
    protected SimpleDialog( Frame  frame, String dialog_header,
							String defaultButton, boolean modal ) {
        this( frame, dialog_header, defaultButton, modal, false );
	}

    /**
     * SimpleDialog constructor
     * @param frame  the dialog box frame
     * @param dialog_header  the dialog box header
     * @param defaultButton  the default button
     * @param modal  is/is not modal
     * @param trueModal  show() does not return to caller until hide() is called
     */
    protected SimpleDialog( Frame  frame, String dialog_header,
							String defaultButton, boolean modal, boolean trueModal ) {
        this( frame, dialog_header, modal, trueModal );
        ButtonGadget button;
        addButtonGadget ( button = new ButtonGadget ( defaultButton ) );
        button.addActionListener( this );
    }

	/**
	 * justifies the message label left
	 */
    public void setLeftJustified() {
        messageLabel.setHorizAlign( LabelGadget.HORIZ_ALIGN_LEFT );
    }

	/**
	 * justifies the message label right
	 */
    public void setRightJustified() {
        messageLabel.setHorizAlign( LabelGadget.HORIZ_ALIGN_RIGHT );
    }

	/**
	 * justifies the message label center
	 */
    public void setCenterText() {
        messageLabel.setHorizAlign( LabelGadget.HORIZ_ALIGN_CENTER );
    }

	/**
	 * sets the maximum line length of the message label
	 * @param len		the integer maximim line length
	 */
    public void setMaxLineLength( int len ) {
        messageLabel.setWrapLength( len );
    }

	/**
	 * sets the message for the label
	 * @param message		the String message for the label
	 */
    public void setMessage( String message ) {
        messageLabel.setLabel( message );
    }

	/**
	 * Checks if the user clicked on "OK"
	 * @param action	the ActionEvent.
	 */
    public void actionPerformed( ActionEvent action ) {

        if ( ((ButtonGadget)action.getSource()).getLabel().equals( "OK" ) ) {
            hide();
        }
    }
}

