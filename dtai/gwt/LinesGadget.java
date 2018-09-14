/****************************************************************
 **
 **  $Id: LinesGadget.java,v 1.22 1997/08/06 23:27:06 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/LinesGadget.java,v $
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

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * LinesGadget
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class LinesGadget extends Gadget {

    public DrawLines points;

    /**
     * LinesGadget
     */
    public LinesGadget() {
        points = new DrawLines();
        init();
    }

    /**
     * LinesGadget
     * @param npoints	number of points in line
     */
    public LinesGadget( int npoints ) {
        points = new DrawLines( npoints );
        init();
    }

    /**
     * LinesGadget
     * @param xpoints[] array of x coordinates of points in line
     * @param ypoints[] array of y coordinates of points in line
     * @parm npoints	number of points in line
     */
    public LinesGadget( int xpoints[], int ypoints[], int npoints ) {
        points = new DrawLines( xpoints, ypoints, npoints );
        init();
    }

    private void init() {
        setThickness(1);
        setIgnoringMouse( true );
    }

	/**
	 * gets the minimum size
	 * @return Dimension
	 */
	public Dimension getMinimumSize() {

	    Rectangle box = points.getBoundingBox();

	    int halfThickness = ( getThickness() + 1 ) / 2;

	    return new Dimension( box.x + box.width + halfThickness + 1,
	                          box.y + box.height + halfThickness + 1 );
	}

    /**
     * sets the thickness of the line
     * @param thickness	thickness of line
     */
    public void setThickness( int thickness ) {
        points.setThickness( thickness );
    }

    /**
     * gets the thickness of the line
     * @return thickness of line
     */
    public int getThickness() {
        return points.getThickness();
    }

	    /**
     * This is ignored if dashOffLen == 0.  Otherwise, it's the
     * approximate number of pixels in length to draw a dash in the line.
	 *
	 * @param len approximate number of pixels in a dash of the line.
	 */
    public void setDashOnLen( int len ) {
        points.setDashOnLen(len);
    }

    /**
     * returns the setting
	 *
	 * @return dashOnLen
	 */
    public int getDashOnLen() {
        return points.getDashOnLen();
    }

    /**
     * The line is solid if dashOffLen == 0.  Otherwise, this is the
     * approximate number of pixels in length to skip between dashes.
	 *
	 * @param setDashOffLen approximate number of pixels to skip between dashes.
	 */
    public void setDashOffLen( int len ) {
        points.setDashOffLen(len);
    }

    /**
     * returns the setting
	 *
	 * @return dashOffLen
	 */
    public int getDashOffLen() {
        return points.getDashOffLen();
    }

    /**
	 * update
	 * @param g	GadgetGraphics object for drawing
	 */
	public void update( GadgetGraphics g ) {
	    g.setColor(getForeground(g));
        points.draw( g );
	}

    /**
     * Checks whether this component "contains" the specified (x, y)
	 * location, where x and y are defined to be relative to the coordinate system of
	 * this component.
     * @param x - the x coordinate
     * @param y - the y coordinate
     * @param exclude	Gadget to exclude from search
     * @see getGadgetAt
     * @return this component does/does not contain the point
     */
	public boolean contains( int x, int y, Gadget exclude ) {
		synchronized(getTreeLock()) {
    		if ( ( ! isShowing() ) ||
    		     ( this == exclude ) ||
    		     ( isIgnoringMouse() ) ||
    		     ( ! ( (x >= 0) &&
    		           (x <= width) &&
    		           (y >= 0) &&
    		           (y <= height) ) ) ) {
                return false;
            }
            return points.contains( x, y );
        }
	}
}
