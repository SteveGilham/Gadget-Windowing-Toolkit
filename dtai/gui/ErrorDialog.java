/****************************************************************
 **
 **  $Id: ErrorDialog.java,v 1.12 1997/08/06 23:27:28 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gui/ErrorDialog.java,v $
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

import dtai.gwt.ButtonGadget;
import dtai.util.ShowUser;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Frame;
import java.net.MalformedURLException;
import java.net.URL;
import java11.awt.event.ActionEvent;
import java11.awt.event.ActionListener;
import java11.util.EventObject;


/**
 * ErrorDialog - pops up an error message
 *
 * @version	1.0b1
 * @author	DTAI, Incorporated
 *
 * $Id: ErrorDialog.java,v 1.12 1997/08/06 23:27:28 cvs Exp $
 *
 * $Source: /cvs/classes/dtai/gui/ErrorDialog.java,v $
 */

public class ErrorDialog extends SimpleDialog implements ActionListener {

    private String helpTopic;

	/**
	 * @param frame			the Frame for this dialog
	 * @param helpTopic		the String error message
	 */
    public ErrorDialog ( Frame frame , String helpTopic) {
        super ( frame, "Error!", "OK");
        this.helpTopic = helpTopic;
        if ((helpTopic != null) && (!helpTopic.equals(""))) {
	        ButtonGadget help;
            addButtonGadget ( help = new ButtonGadget ( "Help" ) );
            help.addActionListener( this );
        }
    }

    /**
	 * @param action		the ActionEvent firing the error dialog
	 */

    public void actionPerformed( ActionEvent action ) {
        super.actionPerformed( action );

        if ( ((ButtonGadget)action.getSource()).getLabel().equals( "Help" ) ) {
            if (dtai.util.ShowUser.getApplet() != null) {
                try {
                    URL doc = new URL(dtai.util.ShowUser.getApplet().getDocumentBase(), helpTopic );
                    dtai.util.ShowUser.getApplet().getAppletContext().showDocument( doc, "HelpWIndow");
                }
                catch ( MalformedURLException mue) {
                    dtai.util.ShowUser.info("Unable to open help file " + helpTopic );
                }
            }
        }
    }

}
