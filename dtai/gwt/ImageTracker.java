/****************************************************************
 **
 **  $Id: ImageTracker.java,v 1.9 1997/08/06 23:27:06 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/ImageTracker.java,v $
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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

/**
 * ImageTracker - pops up an "About ..." box for an application
 *
 * @version	1.1
 * @author	DTAI, Incorporated
 *
 * $Id: ImageTracker.java,v 1.9 1997/08/06 23:27:06 cvs Exp $
 *
 * $Source: /cvs/classes/dtai/gwt/ImageTracker.java,v $
 */

public class ImageTracker implements ImageObserver {

    private TrackImage doit;
	private int x = -1;
	private int y = -1;

    /**
     * ImageTracker
     */
    public ImageTracker() {
    }

    /**
     * ImageTracker
     * @param image - TBD
     */
    public ImageTracker( Image image ) throws Exception {
		waitFor( image );
    }

	/**
	 * ImageTracker
	 * @param image - TBD
	 * @param x - TBD
	 * @param y - TBD
	 */
	public ImageTracker( Image image , int x, int y) throws Exception {
        this.x = x;
		this.y = y;
		waitFor( image );
    }


    /**
     * waitFor
     * @param image - TBD
     */
    public synchronized void waitFor( Image image ) throws Exception {

        if ( doit == null ) {
            doit = new TrackImage();
        }

        if ( ! Toolkit.getDefaultToolkit().prepareImage( image, x, y, doit ) ) {
            while ( ( doit.flags & ( FRAMEBITS | ALLBITS | ABORT | ERROR ) ) == 0 ) {
                try {
                    wait( 10 );
                }
                catch ( InterruptedException ie ) {
                }
            }
        }
        if ( ( doit.flags & ERROR ) != 0 ) {
            throw new Exception( "Error loading image" );
        }
    }

	/**
	 * imageUpdate
	 * @param img - TBD
	 * @param flags - TBD
	 * @param x - TBD
	 * @param y - TBD
	 * @param w - TBD
	 * @param h - TBD
	 * @return boolean
	 */
	public boolean imageUpdate( Image img, int flags,
	                            int x, int y, int w, int h ) {
	// DONT TRY TO USE THIS CLASS AS AN IMAGE OBSERVER!!!
		return false;
	}
}

/**
 * TrackImage
 * @version 1.1
 * @author DTAI, Incorporated
 */
class TrackImage implements ImageObserver {

    int flags;

/**
 * Repaints the component when the image has changed.
 * @param img - TBD
 * @param flags - TBD
 * @param x - TBD
 * @param y - TBD
 * @param w - TBD
 * @param h - TBD
 * @return true if image has changed; false otherwise.
 */

	public boolean imageUpdate( Image img, int flags,
	                            int x, int y, int w, int h ) {

		this.flags = flags;
		if ( ( flags & ( FRAMEBITS | ALLBITS | ABORT | ERROR ) ) != 0) {
		    return false;
		}
		return true;

	}
}
