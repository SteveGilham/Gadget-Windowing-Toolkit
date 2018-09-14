/****************************************************************
 **
 **  $Id: GadgetFrame.java,v 1.29 1998/03/10 20:30:41 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/GadgetFrame.java,v $
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Window;
import java.util.Vector;
import java11.awt.event.AWTEvent;
import java11.awt.event.WindowEvent;
import java11.awt.event.WindowListener;

/**
 * A Frame is a top-level window with a title.
 * The default layout for a frame is BorderLayout.
 *
 * Frames are capable of generating the following types of window events:
 * WindowOpened, WindowClosing, WindowClosed, WindowIconified,
 * WindowDeiconified, WindowActivated, WindowDeactivated.
 * @version 	1.1
 * @author 	DTAI, Incorporated
 */
public class GadgetFrame extends Frame implements GadgetManager {

    private OverlayPanelGadget overlayPanel;
    private PanelGadget mainPanel;
    private GadgetShell shell;
    private boolean constructed = false;

    WindowListener windowListener;

    /**
     * Constructs a new Frame that is initially invisible.
     * @see Gadget#setSize
     * @see Gadget#setVisible
     */
    public GadgetFrame() {
        init();
    }

    /**
     * Constructs a new, initially invisible Frame with the specified
     * title.
     * @param title the title for the frame
     * @see Gadget#setSize
     * @see Gadget#setVisible
     */
    public GadgetFrame( String title ) {
        super( title );
        init();
    }

    private void init() {
        // set up the Frame

        setBackground( Color.lightGray );

        // set up the GadgetShell

		shell = new GadgetShell();
        add( "Center", shell );

        // set up the CardLayout managed panel for the main gadgets and overlays

        PanelGadget cards = new PanelGadget();
        cards.setLayout( new GadgetCardLayout() );
        shell.add( cards );

        // add the transparent panel for overlays first so it's on top

        cards.add( "overlayPanel", overlayPanel = new OverlayPanelGadget() );
        shell.setOverlayPanel( overlayPanel );

        // add the main panel

        cards.add( "mainPanel", mainPanel = new PanelGadget() );
        mainPanel.setLayout(new GadgetBorderLayout());

        // start error checking for invalid use by programmers

        constructed = true;
    }

    /**
     * Overridden to make it an error to add components to it.
     * @param comp - the component to be added
     * @return Component
     */
    public Component add(Component comp) {
        if ( ! constructed ) {
            return super.add( comp );
        }
        throw new IllegalArgumentException( "Programmer error. Invalid to add a Component to a GadgetApplet" );
    }

    /**
     * Overridden to make it an error to add components to it.
     * @param comp the component to be added
     * @param pos the position at which to insert the component. -1
     * means insert at the end.
     * @see #remove
     * @return Component
     */
    public Component add(Component comp, int pos) {
        synchronized(shell.getGadgetTreeLock()) {
            if ( ! constructed ) {
                return super.add( comp, pos );
            }
            throw new IllegalArgumentException( "Programmer error. Invalid to add a Component to a GadgetApplet" );
        }
    }

    /**
     * Overridden to make it an error to add components to it.
     * @param name the component name
     * @param comp the component to be added
     * @see #remove
     * @see LayoutManager
     * @return Component
     */
    public Component add(String name, Component comp) {
        synchronized(shell.getGadgetTreeLock()) {
            if ( ! constructed ) {
                return super.add( name, comp );
            }
            throw new IllegalArgumentException( "Programmer error. Invalid to add a Component to a GadgetApplet" );
        }
    }

    /**
     * Overridden to make it an error to add a Component layout manager to it.
     * @param mgr the specified layout manager
     * @see dtai.gwt.ContainerGadget#layout
     * @see dtai.gwt.ContainerGadget#getLayout
     */
    public void setLayout(LayoutManager mgr) {
        if ( ! constructed ) {
            super.setLayout( mgr );
        }
        else {
            throw new IllegalArgumentException( "Programmer error. Invalid to add a Component layout manager to a GadgetApplet" );
        }
    }

    /**
     * Returns the panel in which overlays (menus, tips) are to be drawn.
     * @see #getGadget
     * @return OverlayPanelGadget
     */
    public OverlayPanelGadget getOverlayPanel() {
        return overlayPanel;
    }

    /**
     * Returns the panel in which all other gadgets (non-overlays) are to be drawn.
     * @see #getGadget
     * @return PanelGadget
     */
    public PanelGadget getMainPanel() {
        return mainPanel;
    }

   /**
     * Returns the number of gadgets in this panel.
     * @see #getGadget
     * @return int
     */
    public int getGadgetCount() {
	    return mainPanel.getGadgetCount();
    }

    /**
     * Gets the nth gadget in this container.
     * @param n the number of the gadget to get
     * @exception ArrayIndexOutOfBoundsException If the nth value does not
     * exist.
     * @return Gadget
     */
    public Gadget getGadget(int n) {
        synchronized(shell.getGadgetTreeLock()) {
    	    return mainPanel.getGadget(n);
    	}
    }

    /**
     * Gets all the gadgets in this container.
     * @return Gadget
     */
    public Gadget[] getGadgets() {
        synchronized(shell.getGadgetTreeLock()) {
    	    return mainPanel.getGadgets();
        }
    }

    /**
     * Adds the specified gadget to this container.
     * @param comp the gadget to be added
     * @return Gadget
     */
    public Gadget add(Gadget gadget) {
	    return mainPanel.add( gadget );
    }

    /**
     * Adds the specified gadget to this container at the given position.
     * @param comp the gadget to be added
     * @param pos the position at which to insert the gadget. -1
     * means insert at the end.
     * @see #remove
     * @return Gadget
     */
    public Gadget add(Gadget gadget, int pos) {
        synchronized(shell.getGadgetTreeLock()) {
    	    return mainPanel.add( gadget, pos );
    	}
    }

    /**
     * Adds the specified gadget to this container. The gadget
     * is also added to the layout manager of this container using the
     * name specified
.
     * @param name the gadget name
     * @param gadget the gadget to be added
     * @see #remove
     * @see GadgetLayoutManager
     * @return Gadget
     */
    public Gadget add(String name, Gadget gadget) {
        synchronized(shell.getGadgetTreeLock()) {
    	    return mainPanel.add( name, gadget );
    	}
    }

    /**
     * Removes the specified gadget from this container.
     * @param gadget the gadget to be removed
     * @see #add
     */
    public void remove(Gadget gadget) {
        synchronized(shell.getGadgetTreeLock()) {
    	    mainPanel.remove( gadget );
    	}
    }

    /**
     * Sets the gadget help for this container.
     * @param gadgetHelp the gadget help
     */
    public void setGadgetHelp(GadgetHelp gadgetHelp) {
	    mainPanel.setGadgetHelp( gadgetHelp );
    }

    /**
     * Sets the layout manager for this container.
     * @param mgr the specified layout manager
     * @see dtai.gwt.ContainerGadget#layout
     * @see dtai.gwt.ContainerGadget#getLayout
     */
    public void setLayout(GadgetLayoutManager mgr) {
	    mainPanel.setLayout( mgr );
    }

    /**
     * Locates the gadget that contains the x,y position.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return null if the gadget is not within the x and y
     * coordinates; returns the gadget otherwise.
     * @see Gadget#inside
     */
    public Gadget getGadgetAt(int x, int y) {
        synchronized(shell.getGadgetTreeLock()) {
    	    return mainPanel.getGadgetAt( x, y );
    	}
    }

/**
 * Locates the gadget that contains the specified point.
 * Overrides:
 *        getGadgetAt in class Gadget
 * @param p - the point
 * @return null if the gadget does not contain the point; returns the gadget otherwise.
 * @see contains
 */

    public Gadget getGadgetAt(Point p) {
        return getGadgetAt( p.x, p.y );
    }

	/**
	 * hide
	 */
	public void hide() {
	    super.hide();
	    processEvent( new WindowEvent( this, new Event( this, -1, this ), WindowEvent.WINDOW_CLOSED ) );
	}

	/**
	 * show
	 */
	public void show() {
	    super.show();
	    processEvent( new WindowEvent( this, new Event( this, -1, this ), WindowEvent.WINDOW_OPENED ) );
	}

	/**
	 * Adds the specified listener to be notified when component
	 * events occur on this component.
	 *
	 * @param l 	the listener to receive the events
	 */
	public synchronized void addWindowListener(WindowListener l) {
        windowListener = GWTEventMulticaster.add(windowListener, l);
	}

	/**
	 * Removes the specified listener so it no longer receives
	 * window events on this window.
	 *
	 * @param l 		the listener to remove
	 */
	public synchronized void removeWindowListener(WindowListener l) {
        windowListener = GWTEventMulticaster.remove(windowListener, l);
	}

	/**
	 * handleEvent
	 * @param evt 		the Event to handle
	 * @return			boolean result of event handling
	 */
	public boolean handleEvent( Event evt ) {

        if ( evt.id == Event.WINDOW_DESTROY ) {
            processEvent( new WindowEvent( this, evt, WindowEvent.WINDOW_CLOSING ) );
            return true;
        }
        if ( evt.id == Event.WINDOW_ICONIFY ) {
            processEvent( new WindowEvent( this, evt, WindowEvent.WINDOW_ICONIFIED ) );
            return true;
        }
        if ( evt.id == Event.WINDOW_DEICONIFY ) {
            processEvent( new WindowEvent( this, evt, WindowEvent.WINDOW_DEICONIFIED ) );
            return true;
        }
        return false;
	}

	/**
	 * processEvent
	 *
	 * @param e		a WindowEvent- we handle WINDOW_CLOSING
	 *										 WINDOW_CLOSED
	 *										 WINDOW_OPENED
	 *										 WINDOW_ICONIFIED
	 *										 WINDOW_DEICONIFIED
	 * @return boolean result
	 */
	protected void processEvent(AWTEvent e) {

		if (e instanceof WindowEvent) {
		    processWindowEvent((WindowEvent)e);
		}
	}

	protected void processWindowEvent(WindowEvent e) {
	    if (windowListener != null) {
    		switch( e.getID() ) {

    			case WindowEvent.WINDOW_CLOSING: {
    				windowListener.windowClosing(e);
    				break;
    			}
    			case WindowEvent.WINDOW_CLOSED: {
    				windowListener.windowClosed(e);
    				break;
    			}
    			case WindowEvent.WINDOW_OPENED: {
    				windowListener.windowOpened(e);
    				break;
    			}
    			case WindowEvent.WINDOW_ICONIFIED: {
    				windowListener.windowIconified(e);
    				break;
    			}
    			case WindowEvent.WINDOW_DEICONIFIED: {
    				windowListener.windowDeiconified(e);
    				break;
    			}
    			case WindowEvent.WINDOW_ACTIVATED: {
    				windowListener.windowActivated(e);
    				break;
    			}
    			case WindowEvent.WINDOW_DEACTIVATED: {
    				windowListener.windowDeactivated(e);
    				break;
    			}
    		}
		}
	}

    /**
     * pack
     */
    public void pack() {
        super.pack();
        Dimension pref = preferredSize();
        resize( pref ); // because there are bugs
    }
}
