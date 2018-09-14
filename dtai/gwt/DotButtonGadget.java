/****************************************************************
 **  Copyright 1996 by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: DotButtonGadget.java,v 1.1 1997/08/25 20:33:51 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/DotButtonGadget.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Image;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;

/**
 * ArrowButtonGadget
 *
 * @version	1.1
 * @author	DTAI, Incorporated
 */
public class DotButtonGadget extends ButtonGadget {


    private static final int X = 0xFF000000;
    private static final int WIDTH = 8;
    private static final int HEIGHT = 7;
    private static Image dotsImage;

    private static final int dots[] = {
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        X, X, 0, X, X, 0, X, X,
        X, X, 0, X, X, 0, X, X,
        0, 0, 0, 0, 0, 0, 0, 0,
    };


	/**
	 * default constructor which constructs a dot button gadget
	 */
	public DotButtonGadget() {
        setImage(getDots());
    }

    private Image getDots() {
        if (dotsImage == null) {
            dotsImage =  createImage( new MemoryImageSource ( WIDTH, HEIGHT,
                                                              dots, 0, WIDTH ) );
	    }
	    return dotsImage;
	}
}

