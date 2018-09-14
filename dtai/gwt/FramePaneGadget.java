/****************************************************************
 **
 **  $Id: FramePaneGadget.java,v 1.21 1997/11/20 19:37:51 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/FramePaneGadget.java,v $
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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java11.awt.event.MouseEvent;
import java11.awt.event.MouseListener;
import java11.awt.event.MouseMotionListener;

/**
 * FramePaneGadget
 * @version 1.1
 * @author  DTAI, Incorporated
 */
public class FramePaneGadget extends PanelGadget
    implements MouseListener, MouseMotionListener {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int orientation;
    private boolean showingCursor = false;

    private int linepos;
    private Gadget dragGadget;

	/**
	 * FramePaneGadget
	 */
	public FramePaneGadget() {
		this( HORIZONTAL );
	}

    /**
     * FramePaneGadget
     * @param orientation	Frame orientation, either vertical or horizontal
     */
    public FramePaneGadget( int orientation ) {
        super.setLayout( null );
        setOrientation(orientation);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

	/**
     * returns true if children can overlap each other.
     * @return false
	 */
	public boolean childrenCanOverlap() {
	    return false;
	}

    /**
     * Gets the orientation of the button.
     * @return orientation
     */
	public final int getOrientation() {
		return orientation;
	}


	/**
	 * Sets the button with the specified orientation
	 * @param orientation - the orientation to set the button with
	 * either VERTICAL (1) or HORIZONTAL (0)
	 */
    public void setOrientation( int orientation ) {
	    synchronized(getTreeLock()) {
            if ( ( orientation != VERTICAL ) &&
                 ( orientation != HORIZONTAL ) ) {
                throw new IllegalArgumentException();
            }
    	    if ( orientation != this.orientation ) {
        	    this.orientation = orientation;
        	    invalidate();
        	}
        }
	}

    /**
     * Adds the specified gadget to this frame pane container. If the frame
     * pane has an existing child gadget, that gadget is removed and the new one is
     * added.
     * Overrides:
     *      add in class Container
     *
     * @param gadget - the gadget to be added
     * @param pos - position of child gadget (must be <= 0)
     * @return Gadget
     */
    public Gadget add( Gadget gadget, int pos ) {
        synchronized(getTreeLock()) {
            BorderGadget frame = new BorderGadget();
            frame.setBorderType( BorderGadget.THREED_IN );
            frame.setBorderThickness( 3 );
            frame.setLayout( new GadgetBorderLayout() );
            frame.add("Center",gadget);
            return super.add( frame, pos );
        }
    }

    /**
     * removes a gadget
     * @param gadget	Gadget to be removed
     * @param i			index of Gadget to be removed
     */
    protected void remove( Gadget gadget, int i ) {
        synchronized(getTreeLock()) {
            super.remove( getGadget(i), i );
    	}
    }

    /**
     * gets the index of the gadget
     * @param gadget - TBD
     * @return int
     */
    public int getGadgetIndex( Gadget gadget ) {
        synchronized(getTreeLock()) {
            if ( gadget.parent == this ) {
                return super.getGadgetIndex(gadget);
            }
            else {
        	    for (int i = 0 ; i < gadgetCount ; i++) {
            		if (gadgets[i] == gadget.parent) {
            		    return i;
            		}
            	}
            	return -1;
            }
        }
    }

    /**
     * gets the minimum dimensions
     * @return Dimension
     */
    public Dimension getMinimumSize() {
        synchronized(getTreeLock()) {
            Dimension min = new Dimension();
            for ( int i = 0; i < gadgetCount; i++ ) {
                Gadget gadget = gadgets[i];
                Dimension childMin = gadget.getMinimumSize();
                int childWidth = childMin.width;
                int childHeight = childMin.height;
                if ( orientation == HORIZONTAL ) {
                    min.width += childWidth;
                    min.height = Math.max(min.height, childHeight);
                }
                else {
                    min.width = Math.max(min.width, childWidth);
                    min.height += childHeight;
                }
            }
            return min;
        }
    }

    /**
     * gets the preferred dimensions
     * @return Dimension
     */
    public Dimension getPreferredSize() {
	    if ( valid && ( pref_width >= 0 ) && ( pref_height >= 0 ) ) {
	        return new Dimension( pref_width, pref_height );
	    }

        synchronized(getTreeLock()) {
            Dimension pref = new Dimension();
            for ( int i = 0; i < gadgetCount; i++ ) {
                Gadget gadget = gadgets[i];
                int childWidth = gadget.pref_width;
                int childHeight = gadget.pref_height;
                if ( ( childWidth < 0 ) ||
                     ( childHeight < 0 ) ) {
                    Dimension childPref = gadget.getPreferredSize();
                    childWidth = childPref.width;
                    childHeight = childPref.height;
                }
                if ( orientation == HORIZONTAL ) {
                    pref.width += childWidth;
                    pref.height = Math.max(pref.height, childHeight);
                }
                else {
                    pref.width = Math.max(pref.width, childWidth);
                    pref.height += childHeight;
                }
            }
            return pref;
        }
    }

    /**
     * Lays out this container by resizing its child to its preferred size.
	 * If the new preferred size of the child causes the current scroll
	 * position to be invalid, the scroll position is set to the closest
	 * valid position.
     * Overrides:
     *      doLayout in class Container
     * @see validate
     */
    public void doLayout() {
        synchronized(getTreeLock()) {
            for ( int i = 0; i < gadgetCount; i++ ) {
                ContainerGadget gadget = (ContainerGadget)gadgets[i];
                if ( gadget.gadgetCount > 0 ) {
                    gadget.setVisible( gadget.gadgets[0].visible );
                }
            }
            Gadget[] gadgets = getVisibleGadgets();
            int gadgetCount = gadgets.length;
            if ( gadgetCount < 1 ) {
                return;
            }
            int nextx = 0;
            int nexty = 0;
            int xs[] = new int[gadgetCount];
            int ys[] = new int[gadgetCount];
            int widths[] = new int[gadgetCount];
            int heights[] = new int[gadgetCount];
            for ( int i = 0; i < gadgetCount; i++ ) {
                Gadget gadget = gadgets[i];
                xs[i] = nextx;
                ys[i] = nexty;
                if ( orientation == HORIZONTAL ) {
                    heights[i] = height;
                    if ( gadget.width == 0 ) {
                        Dimension pref = gadget.getPreferredSize();
                        widths[i] = pref.width;
                    }
                    else {
                        widths[i] = gadget.width;
                    }
                    nextx += widths[i];
                }
                else {
                    widths[i] = width;
                    if ( gadget.height == 0 ) {
                        Dimension pref = gadget.getPreferredSize();
                        heights[i] = pref.height;
                    }
                    else {
                        heights[i] = gadget.height;
                    }
                    nexty += heights[i];
                }
            }
            if ( orientation == HORIZONTAL ) {
                for ( int i = gadgetCount-1;
                      ( i >= 0 ) && ( nextx > width );
                      i-- ) {
                    Gadget gadget = gadgets[i];
                    Dimension min = gadget.getMinimumSize();
                    if ( min.width < widths[i] ) {
                        nextx -= widths[i] - min.width;
                        widths[i] = min.width;
                    }
                }
                nextx = 0;
                for ( int i = 0; i < gadgetCount; i++ ) {
                    xs[i] = nextx;
                    nextx += widths[i];
                }
                widths[gadgetCount-1] = width - xs[gadgetCount-1];
            }
            else {
                for ( int i = gadgetCount-1;
                      ( i >= 0 ) && ( nexty > height );
                      i-- ) {
                    Gadget gadget = gadgets[i];
                    Dimension min = gadget.getMinimumSize();
                    if ( min.height < heights[i] ) {
                        nexty -= heights[i] - min.height;
                        heights[i] = min.height;
                    }
                }
                nexty = 0;
                for ( int i = 0; i < gadgetCount; i++ ) {
                    ys[i] = nexty;
                    nexty += heights[i];
                }
                heights[gadgetCount-1] = height - ys[gadgetCount-1];
            }
            for ( int i = 0; i < gadgetCount; i++ ) {
                Gadget gadget = gadgets[i];
                gadget.setBounds(xs[i],ys[i],widths[i],heights[i],false);
            }
        }
    }

    private void drawLine() {
        GadgetGraphics g = shell.getTempGraphics();
        Point offset = getOffset();
        g.clipRect( offset.x, offset.y, width, height );
        Color bg = getFinalBackground();
        if ( bg.equals(Color.black) ) {
            g.setColor(Color.white);
        }
        else {
            g.setColor(Color.black);
        }
        g.setXORMode(bg);
        if ( orientation == HORIZONTAL ) {
            g.drawLine(linepos+offset.x,offset.y,linepos+offset.x,height+offset.y);
        }
        else {
            g.drawLine(offset.x,linepos+offset.y,width+offset.x,linepos+offset.y);
        }
        g.dispose();
    }

    private Gadget findDragGadget(int mousex,int mousey) {
        for ( int i = 0; i < gadgetCount; i++ ) {
            Gadget gadget = gadgets[i];
            if ( gadget.visible ) {
                if ( orientation == HORIZONTAL ) {
                    int borderx = gadget.x + gadget.width;
                    if ( ( borderx < width ) &&
                         ( mousex > borderx - 4 ) &&
                         ( mousex < borderx + 4 ) ) {
                        return gadget;
                    }
                }
                else {
                    int bordery = gadget.y + gadget.height;
                    if ( ( bordery < height ) &&
                         ( mousey > bordery - 4 ) &&
                         ( mousey < bordery + 4 ) ) {
                        return gadget;
                    }
                }
            }
        }
        return null;
    }

    /**
     * mouseDragged
     * @param e	the firing MouseEvent
     */
    public void mouseDragged(MouseEvent e) {
        synchronized (getTreeLock()) {
            if ( dragGadget != null ) {
                drawLine();
                if ( orientation == HORIZONTAL ) {
                    linepos = e.getX();
                }
                else {
                    linepos = e.getY();
                }
                drawLine();
            }
        }
    }

    private void hideCursor() {
        showingCursor = false;
        setCursor(null);
    }

    private void showCursor() {
        showingCursor = true;
        if ( orientation == HORIZONTAL ) {
            setCursor(GadgetCursor.getPredefinedCursor(GadgetCursor.W_RESIZE_CURSOR));
        }
        else {
            setCursor(GadgetCursor.getPredefinedCursor(GadgetCursor.N_RESIZE_CURSOR));
        }
    }

    /**
     * mouseMoved
     * @param e	the firing MouseEvent
     */
    public void mouseMoved(MouseEvent e) {
        Gadget dragGadget = findDragGadget(e.getX(),e.getY());
        if ( dragGadget == null ) {
            if ( showingCursor ) {
                hideCursor();
            }
        }
        else {
            if ( ! showingCursor ) {
                showCursor();
            }
        }
    }

    /**
     * mouseClicked
     * @param e	the firing MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * mousePressed
     * @param e	the firing MouseEvent
     */
    public void mousePressed(MouseEvent e) {
        synchronized (getTreeLock()) {
            dragGadget = findDragGadget(e.getX(),e.getY());
            if ( dragGadget != null ) {
                if ( orientation == HORIZONTAL ) {
                    linepos = e.getX();
                }
                else {
                    linepos = e.getY();
                }
                drawLine();
            }
        }
    }

    /**
     * mouseReleased
     * @param e	the firing MouseEvent
     */
    public void mouseReleased(MouseEvent e) {
        if ( dragGadget != null ) {
            drawLine();
            Dimension min = dragGadget.getMinimumSize();
            if ( orientation == HORIZONTAL ) {
                if ( linepos < dragGadget.x + min.width ) {
                    linepos = dragGadget.x + min.width;
                }
                dragGadget.setSize(linepos-dragGadget.x,dragGadget.height);
            }
            else {
                if ( linepos < dragGadget.y + min.height ) {
                    linepos = dragGadget.y + min.height;
                }
                dragGadget.setSize(dragGadget.width,linepos-dragGadget.y);
            }
            dragGadget = null;
        }
    }

    /**
     * mouseEntered - empty mehtod
     * @param e	the firing MouseEvent
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
	 *
     * mouseExited
     * @param e	the firing MouseEvent
     */
    public void mouseExited(MouseEvent e) {
        if ( showingCursor ) {
            hideCursor();
        }
    }
}
