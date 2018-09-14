/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: URLControl.java,v 1.2 1997/09/30 00:57:10 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/URLControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import dtai.util.ShowUser;
import java.net.MalformedURLException;
import java.net.URL;
import java11.awt.event.FocusEvent;
import java11.awt.event.FocusListener;
import java11.awt.event.KeyEvent;
import java11.awt.event.KeyListener;


/**
 * The URLControl class
 *
 * GUI Class for editing string(URL) data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class URLControl extends StringControl implements KeyListener, FocusListener {

    private String url;

	/**
	 * constructor which creates a URL control with the specified label
	 * and data(URL)
	 * @param label  the label of the string control
	 * @param data  the data(URLg) of the string control
	 */
	public URLControl (String label, Object data ) {
		super(label, data);
        edit.addKeyListener(this);
        edit.addFocusListener(this);
        setData(data);
    }

	/**
	 * sets the data(URL) of the URL control
	 * @param data  the data(URL) of the URL control
	 */
	public void setData(Object data) {
        if (data instanceof URL) {
            String urlString = data.toString();
            if (urlString.equals("http:/")) {
                urlString = urlString + "/";
            }
            edit.setText(urlString);
            this.url = urlString;
        } else {
            edit.setText((String) data);
            this.url = (String) data;
        }
	}

	/**
	 * gets the data(URL) of the URL control
	 * @return  the data(URL) of the URL control
	 */
	public Object getData() {
        try {
            URL tmpURL = new URL(edit.toString());
            return tmpURL;
        }
        catch (MalformedURLException e) {
            dtai.util.ShowUser.info("Invalid format for URL value. " + edit.toString());
        }
        reset();
        return null;
	}

	/**
	 * keyTyped
	 * @param e  the key event
     */
	public void keyTyped(KeyEvent e) {
    }

    /**
	 * keyPressed
	 * @param e  the key event
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.ENTER) {
            getData();
        }
    }

    /**
	 * keyReleased
	 * @param e  the key event
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * focusGained
     * @param e  the focus event
     */
    public void focusGained(FocusEvent e) {
    }

    /**
     * focusLost
     * @param e  the focus event
     */
    public void focusLost(FocusEvent e) {
        getData();
    }

}
