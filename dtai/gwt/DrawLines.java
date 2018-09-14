/****************************************************************
 **
 **  $Id: DrawLines.java,v 1.13 1997/09/11 01:22:25 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/DrawLines.java,v $
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

import java.awt.Rectangle;

/**
 * DrawLine - draws a line with varying thickness and dashes if selected.
 *
 * @version	1.1
 * @author	DTAI, Incorporated
 *
 * $Id: DrawLines.java,v 1.13 1997/09/11 01:22:25 cvs Exp $
 *
 * $Source: /cvs/classes/dtai/gwt/DrawLines.java,v $
 */
public class DrawLines {

	/**
	 * array of x coordinates of the points
	 */
    public int[] xpoints;

	/**
	 * array of y coordinates of the points
	 */
    public int[] ypoints;

	/**
	 * number of points in the line
	 */
	public int npoints;

    int thickness = 1;
    int dashOnLen = 1;
    int dashOffLen = 0;

    public static final int NO_HANDLES = 0;
    public static final int INSIDE_HANDLES = 1;
    public static final int OUTSIDE_HANDLES = 2;
    public static final int ALL_HANDLES = INSIDE_HANDLES | OUTSIDE_HANDLES;
    int handles = NO_HANDLES;

    protected static final int CLICK_TOLERANCE = 5;
    protected static final double RADIANS_TO_DEGREES = (double)180.0 / Math.PI;
    private Rectangle handle_rect = new Rectangle();

	/**
	 * Drawlines
	 */
    public DrawLines() {
    }

	/**
	 * drawslines with a specified number of points in line
	 *
	 * @param npoints	number of points in line
	 */
    public DrawLines( int npoints ) {
        this( new int[npoints], new int[npoints], npoints );
    }

	/**
	 * DrawLines
	 *
	 * @param xpoints	array of x coordinates of the points
	 * @param ypoints	array of y coordinates of the points
	 * @param npoints	number of points in line
	 */
    public DrawLines( int[] xpoints, int[] ypoints, int npoints ) {
        setPoints( xpoints, ypoints, npoints );
    }

	/**
	 * sets the x points, y points, and number of points on line
	 *
	 * @param xpoints	array of x coordinates of the points
	 * @param ypoints	array of y coordinates of the points
	 * @param npoints	number of points in line
	 */
    public void setPoints( int[] xpoints, int[] ypoints, int npoints ) {
        this.xpoints = xpoints;
        this.ypoints = ypoints;
		this.npoints = npoints;
    }


	/**
	 * Sets the thickness of the line.
	 *
	 * @param thickness	thickness of the line
	 */
    public void setThickness( int thickness ) {
        this.thickness = thickness;
    }

	/**
	 * Gets the thickness of the line
	 *
	 * @return thickness
	 */
    public int getThickness() {
        return thickness;
    }

	/**
	 * setHandles
	 *
	 * @param handles	new value for handles
	 */
    public void setHandles( int handles ) {
        this.handles = handles;
    }

	/**
	 * getHandles
	 *
	 * @return handles
	 */
    public int getHandles() {
        return handles;
    }

    /**
     * This is ignored if dashOffLen == 0.  Otherwise, it's the
     * approximate number of pixels in length to draw a dash in the line.
	 *
	 * @param len approximate number of pixels in a dash of the line.
	 */
    public void setDashOnLen( int len ) {
        dashOnLen = len;
    }

    /**
     * returns the setting
	 *
	 * @return dashOnLen
	 */
    public int getDashOnLen() {
        return dashOnLen;
    }

    /**
     * The line is solid if dashOffLen == 0.  Otherwise, this is the
     * approximate number of pixels in length to skip between dashes.
	 *
	 * @param setDashOffLen approximate number of pixels to skip between dashes.
	 */
    public void setDashOffLen( int len ) {
        dashOffLen = len;
    }

    /**
     * returns the setting
	 *
	 * @return dashOffLen
	 */
    public int getDashOffLen() {
        return dashOffLen;
    }


	/**
	 * handleContains
	 *
	 * @param point_x	x coordinate of point in question
	 * @param point_y	y coordinate of point in question
	 * @param vertex_x	x coordinate of rectangle vertex
	 * @param vertex_y 	y coordinate of rectangle vertex
	 * @return specified point is/is not in the handle rectangle
	 */
    public boolean handleContains( int point_x, int point_y, int vertex_x, int vertex_y ) {
        int half = Math.max( thickness, 2 );
        handle_rect.reshape( vertex_x - half, vertex_y - half,
                             half * 2, half * 2 );
        return handle_rect.inside( point_x, point_y );
    }

	/**
	 * drawHandle
	 *
	 * @param g	GadgetGraphics window
	 * @param x	x coordinate
	 * @param y	y coordinate
	 */
    public void drawHandle( GadgetGraphics g, int x, int y ) {
        int half = Math.max( thickness, 2 );
        g.fillRect( x - half, y - half, half * 2, half * 2 );
    }

    /**
	 * draw
	 *
	 * @param g	GadgetGraphics window
	 */
    public void draw( GadgetGraphics g ) {
        if ( ( xpoints != null ) &&
			 ( ypoints != null ) &&
			 ( npoints > 1 ) ) {
            g.drawPolyline( xpoints, ypoints, npoints, thickness, dashOnLen, dashOffLen );
            if ( ( handles & OUTSIDE_HANDLES ) != 0 ) {
                drawHandle( g, xpoints[0], ypoints[0] );
                drawHandle( g, xpoints[npoints-1], ypoints[npoints-1] );
            }
            if ( ( handles & INSIDE_HANDLES ) != 0 ) {
                for ( int idx = 1; idx < npoints-1; idx++ ) {
                    drawHandle( g, xpoints[idx], ypoints[idx] );
                }
            }
        }
    }

	/**
	 * segmentContains
	 *
	 * @param x			x coordinate of point in question
	 * @param y			y coordinate of point in question
	 * @param from_x	starting x coordinate of segment
	 * @param from_y	starting y coordinate of segment
	 * @param to_x		ending x coordinate of segment
	 * @param to_y		ending y coordinate of segment
	 * @return given point is/is not on given segment
	 */
    public boolean segmentContains( int x, int y,
                                    int from_x, int from_y,
                                    int to_x, int to_y ) {

        int tolerance = Math.max( CLICK_TOLERANCE, thickness );

        //normalize point and from
        x -= from_x;
        y -= from_y;
        to_x -= from_x;
        to_y -= from_y;

        if ( to_x < 0 ) {
            to_x *= -1;
            x *= -1;
        }

        if ( to_y < 0 ) {
            to_y *= -1;
            y *= -1;
        }

        if ( ( x < ( 0 - tolerance ) ) ||
             ( y < ( 0 - tolerance ) ) ||
             ( x > ( to_x + tolerance ) ) ||
             ( y > ( to_y + tolerance ) ) ) {
            return false;
        }

        if ( to_x == 0 ) {
            if ( ( x < ( - tolerance ) ) ||
                 ( x > tolerance ) ) {
                return false;
            }
            return true;
        }

        float slope = (float)to_y/(float)to_x;

        float targ_y = slope * x;

        if ( ( y >= ( targ_y - tolerance ) ) &&
             ( y <= ( targ_y + tolerance ) ) ) {
            return true;
        }

        float inv_slope = (float)to_x/(float)to_y;

        float targ_x = inv_slope * y;

        if ( ( x >= ( targ_x - tolerance ) ) &&
             ( x <= ( targ_x + tolerance ) ) ) {
            return true;
        }
        return false;
    }

	/**
	 * contains
	 *
	 * @param x	x coordinate
	 * @param y	y coordinate
	 * @return point is/is not on line
	 */
    public boolean contains( int x, int y ) {
        if ( ( xpoints != null ) &&
			 ( npoints > 1 ) &&
			 ( ypoints != null ) ) {
            for ( int idx = 1; idx < npoints; idx++ ) {
                if ( segmentContains( x, y, xpoints[idx-1], ypoints[idx-1],
                             xpoints[idx], ypoints[idx] ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getBoundingBox
     * @return Rectangle
     */
    public Rectangle getBoundingBox() {

    	int minX = Integer.MAX_VALUE;
    	int minY = Integer.MAX_VALUE;
    	int maxX = Integer.MIN_VALUE;
    	int maxY = Integer.MIN_VALUE;

    	for (int i = 0; i < npoints; i++) {
    	    minX = Math.min( minX, xpoints[i] );
    	    maxX = Math.max( maxX, xpoints[i] );
    	    minY = Math.min( minY, ypoints[i] );
    	    maxY = Math.max( maxY, ypoints[i] );
    	}
    	return new Rectangle( minX, minY, maxX - minX, maxY - minY);
    }


	/**
	 * finds the index of the segment closest to the given point
	 *
	 * @param x	x coordinate
	 * @param y	y coordinate
	 * @return index of segment closest to given point
	 */
    public int findClosestSegment( int x, int y ) {
        if ( ( xpoints != null ) &&
			 ( npoints > 1 ) &&
			 ( ypoints != null ) ) {
            for ( int idx = 1; idx < npoints; idx++ ) {
                if ( segmentContains( x, y, xpoints[idx-1], ypoints[idx-1],
						  xpoints[idx], ypoints[idx] ) ) {
                    return idx - 1;
                }
            }
        }
        return -1;
    }

	/**
	 * finds the index of the vertex closest to the given point
	 *
	 * @param x	x coordinate
	 * @param y	y coordinate
	 * @return index of vertex closest to given point
	 */
    public int findClosestVertex( int x, int y ) {
        if ( ( xpoints != null ) &&
			 ( npoints > 1 ) &&
			 ( ypoints != null ) ) {
            for ( int idx = 0; idx < npoints; idx++ ) {
                if ( handleContains( x, y, xpoints[idx], ypoints[idx] ) ) {
                    return idx;
                }
            }
        }
        return -1;
    }
}
