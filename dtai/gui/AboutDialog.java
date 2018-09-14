/****************************************************************
 **
 **  $Id: AboutDialog.java,v 1.6 1997/08/06 23:27:28 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gui/AboutDialog.java,v $
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

import java.awt.Frame;

/**
 * AboutDialog - pops up an "About ..." box for an application
 *
 * @version	1.0b1
 * @author	DTAI, Incorporated
 *
 * $Id: AboutDialog.java,v 1.6 1997/08/06 23:27:28 cvs Exp $
 *
 * $Source: /cvs/classes/dtai/gui/AboutDialog.java,v $
 */

public class AboutDialog extends SimpleDialog {

	/**
	 * @param frame 	the Frame for this dialog
	 * @param what		the "About.." description for this dialog
	 */
    public AboutDialog ( Frame frame, String what ) {
        super ( frame, "About "+what, "OK" );
        setCenterText();
    }
}
