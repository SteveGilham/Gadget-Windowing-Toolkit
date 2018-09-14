/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: URLListControl.java,v 1.1 1997/09/23 21:00:51 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/URLListControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Dimension;
import java.awt.Rectangle;
import java11.awt.event.ActionEvent;
import java11.awt.event.ActionListener;

/**
 * The StringArrayControl class
 *
 * GUI Class for editing string array data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class URLListControl extends DataControl implements ActionListener {

    URLListDialog urlListDialog;
    DotButtonGadget dotButton = new DotButtonGadget();
    URLList url;
    boolean isVisible = false;

	/**
	 * constructor which creates a string array control with the specified
	 * label and data(string array)
	 * @param label  the label
	 * @param data  the data of the string array control
	 */
	public URLListControl (String label, Object data) {
	    dotButton.addActionListener(this);
        add(dotButton);
        dotButton.setVisible(false);
		this.label.setLabel(label);
		setData(data);
		//edit.setEditable(false);

    }

	/**
	 * sets the data(string array) of the string array control
	 * @param data  the data(string array) of the string array control
	 */
	public void setData(Object data) {
	    if (data instanceof URLList) {
	        url = (URLList) data;
	        if (url.getDescriptions() != null && url.getDescriptions().length > 0) {
                edit.setText(" ( " + url.getDescriptions()[0] + " ) ");
	        } else {
                edit.setText("(None)");
	        }
	    }
	}

    /**
	 * gets the data(string array) of the string array control
	 * @param data  the data(string array) of the string array control
	 */
	public Object getData() {
		return url;
	}

	/**
	 * brings up the string array choice box
	 * @param in  whether or not the string array control is selected
	 */
	public void setSelected(boolean in) {
	    super.setSelected(in);
	    if (in) {
    	    dotButton.setVisible(true);
    		dotButton.requestFocus();
	    } else {
    	    dotButton.setVisible(false);
	        hideDialog();
	    }
    }

   	/**
   	 * arranges the gadgets
   	 */
   	public void doLayout() {
        super.doLayout();
        Rectangle rect = edit.getBounds();
        Dimension d = dotButton.getPreferredSize();
        dotButton.setBounds(rect.x+rect.width-d.width, rect.y, d.width, rect.height, false);
    }

	public void showDialog() {
	    if (urlListDialog == null) {
	        urlListDialog = new URLListDialog(this);
	    }
	    urlListDialog.setURLList(url);
	    urlListDialog.show();
	}

	public void hideDialog() {
	    if (urlListDialog != null) {
    	    if (urlListDialog.dialogVisible()) urlListDialog.hide();
	        urlListDialog.saveURLs();
	    }
	}

	/**
	 * actionPerformed
	 * @param e  the action event
	 */
	public void actionPerformed(ActionEvent e) {
        showDialog();
	}

	public boolean isDialog() {
        return true;
    }
}
