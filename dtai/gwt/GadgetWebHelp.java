/****************************************************************
 **
 **  $Id: GadgetWebHelp.java,v 1.13 1997/08/06 23:27:05 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/GadgetWebHelp.java,v $
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

import dtai.util.ShowUser;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * GadgetWebHelp
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class GadgetWebHelp implements GadgetHelp {

    private AppletContext appletContext;
    URL baseURL;
    String target;

    /**
     * GadgetWebHelp
     * @param appletContext - TBD
     * @param baseURL - TBD
     */
    public GadgetWebHelp( AppletContext appletContext, URL baseURL ) {
        this( appletContext, baseURL, "_self" );
    }

    /**
     * GadgetWebHelp
     * @param appletContext - TBD
     * @param baseURL - TBD
     * @param target - TBD
     */
    public GadgetWebHelp( AppletContext appletContext, URL baseURL,
                          String target ) {
        this.appletContext = appletContext;
        this.baseURL = baseURL;
        this.target = target;
    }

    /**
     * showHelp
     * @param gadget - TBD
     */
    public void showHelp( Gadget gadget ) {
        String relativeURL = gadget.getHelpId();
        showHelp(relativeURL);
    }

    /**
     * Shows any help information.
     * @param relativeURL	the arbitrary helpId
     */
    public void showHelp( String relativeURL ) {
        try {
            URL doc = baseURL;

            if ( relativeURL != null ) {
                doc = new URL( baseURL, relativeURL );
            }
            appletContext.showDocument( doc, target);
        }
        catch ( MalformedURLException mue) {
            dtai.util.ShowUser.error( mue, "There is an error with the help web page and/or target\n"+
                            baseURL+" with "+relativeURL );
        }
    }
}
