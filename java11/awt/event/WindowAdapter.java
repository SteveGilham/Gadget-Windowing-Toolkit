/****************************************************************
 **
 **  $Id: WindowAdapter.java,v 1.1 1998/01/25 21:29:23 kadel Exp $
 **
 **  $Source: /cvs/classes/java11/awt/event/WindowAdapter.java,v $
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

package java11.awt.event;

import java11.util.EventListener;

public class WindowAdapter implements WindowListener {

    /**
     * Invoked when a window has been opened.
     */

    public void windowOpened(WindowEvent e) {
    }

    /**
     * Invoked when a window is in the process of being closed. The close operation can be overridden at this point.
     */

    public void windowClosing(WindowEvent e) {
    }

    /**
     * Invoked when a window has been closed.
     */

    public void windowClosed(WindowEvent e) {
    }

    /**
     * Invoked when a window is iconified.
     */

    public void windowIconified(WindowEvent e) {
    }

    /**
     * Invoked when a window is de-iconified.
     */

    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * Invoked when a window is activated.
     */

    public void windowActivated(WindowEvent e) {
    }

    /**
     * Invoked when a window is de-activated.
     */

    public void windowDeactivated(WindowEvent e) {
    }
}
