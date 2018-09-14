/****************************************************************
 **
 **  $Id: GadgetJavaScriptHelp.java,v 1.11 1997/08/06 23:27:05 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/GadgetJavaScriptHelp.java,v $
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
 * GadgetJavaScripHelp
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class GadgetJavaScriptHelp implements GadgetHelp {

    private AppletContext appletContext;
    private String helpDocument;
    private String scriptFunction;

    /**
     * GadgetJavaScriptHelp
     * @param appletContext - TBD
     * @param script - TBD
     * @param helpDocument - TBD
     */
    public GadgetJavaScriptHelp( AppletContext appletContext, String script, String helpDocument) {
        this.appletContext = appletContext;
        this.helpDocument = helpDocument;
        this.scriptFunction = script;
    }

    /**
     * showHelp
     * @param gadget - TBD
     */
    public void showHelp( Gadget gadget ) {
        String topic = gadget.getHelpId();
        showHelp(topic);
    }

    /**
     * Shows any help information.
     * @param topic	the arbitrary helpId
     */
    public void showHelp( String topic ) {
        try {

            if ( topic != null ) {
                ShowUser.info( "javascript:" + scriptFunction + "('" + helpDocument + "', '" + topic + "')"  );

                URL doc = new URL( "javascript:" + scriptFunction + "('" + helpDocument + "', '" + topic + "')" );
                appletContext.showDocument( doc );
            }

        }
        catch ( MalformedURLException mue) {
            ShowUser.error( mue, "There is an error with the help web page and/or target\n"+
                            helpDocument+" with "+topic );
        }
    }
}
