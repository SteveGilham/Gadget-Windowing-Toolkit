/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: StringArrayControl.java,v 1.4 1997/09/24 22:13:35 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/StringArrayControl.java,v $
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

public class StringArrayControl extends DataControl implements ActionListener {

    StringDialog stringDialog;
    String strings[] = {""};
    DotButtonGadget dotButton = new DotButtonGadget();

	/**
	 * constructor which creates a string array control with the specified
	 * label and data(string array)
	 * @param label  the label
	 * @param data  the data of the string array control
	 */
	public StringArrayControl (String label, Object data) {
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
	    if (data instanceof String[]) {
	        strings = (String[]) data;
	        if (strings.length > 0) {
                if (strings[0].length() > 15) {
                    edit.setText(" ( " + strings[0].substring(0, 15) + " ) ");
                }
                else {
                    edit.setText(" ( " + strings[0] + " ) ");
	            }
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
		return strings;
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
	    if (stringDialog == null) {
	        stringDialog = new StringDialog(this);
	    }
	    stringDialog.setStrings(strings);
	    stringDialog.show();
	}

	public void hideDialog() {
	    if (stringDialog != null) {
    	    if (stringDialog.dialogVisible()) stringDialog.hide();
	        stringDialog.saveStrings();
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
