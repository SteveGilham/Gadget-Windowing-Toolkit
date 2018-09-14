/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: URLList.java,v 1.1 1997/09/23 21:00:51 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/URLList.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import dtai.util.ShowUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * The URLList class
 *
 * GUI Class for managing list of Descriptions and URLs
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class URLList {

    private Hashtable url = new Hashtable();

    public URLList() {
    }

    public URLList(Hashtable url) {
        this.url = url;
    }

    public void addURL(String description, String newURL) throws MalformedURLException {
        URL testURL = new URL(newURL);
        url.put(description, newURL);
    }

    public Object clone() {
        return new URLList((Hashtable)url.clone());
    }

    public void removeURL(String description) {
        this.url.remove(description);
    }

    public URL[] getURLs() {
        URL urlList[] = null;
        if (url != null) {
            urlList = new URL[url.size()];
            try {
                int i = 0;
                for (Enumeration e = url.elements() ; e.hasMoreElements() ;) {
                    urlList[i++] = new URL((String) e.nextElement());
                }
            }
            catch (MalformedURLException e) {
                dtai.util.ShowUser.error(e, "Invalid format for URL value. ");
            }
        }
        return urlList;
    }

    public String[] getDescriptions() {
        String descriptionsList[] = null;
        if (url != null) {
           descriptionsList = new String[url.size()];
            int i = 0;
            for (Enumeration e = url.keys() ; e.hasMoreElements() ;) {
                descriptionsList[i++] = (String) e.nextElement();
            }
        }
        return descriptionsList;
    }

}
