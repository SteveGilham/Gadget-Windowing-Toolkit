/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: StringControl.java,v 1.1 1997/08/25 20:33:52 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/StringControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

/**
 * The StringControl class
 *
 * GUI Class for editing string data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class StringControl extends DataControl {

	/**
	 * constructor which creates a string control with the specified label
	 * @param label  the label of the string control
	 */
	public StringControl (String label) {
		this(label, "");
	}

	/**
	 * constructor which creates a string control with the specified label
	 * and data(string)
	 * @param label  the label of the string control
	 * @param data  the data(string) of the string control
	 */
	public StringControl (String label, Object data) {
		this.label.setLabel(label);
		setData(data);
	}

	/**
	 * sets the data(string) of the string control
	 * @param data  the data(string) of the string control
	 */
	public void setData(Object data) {
	    if ( data != null ) {
            edit.setText(data.toString());
        }
        else {
            edit.setText("");
        }
	}

	/**
	 * gets the data(string) of the string control
	 * @return  the data(string) of the string control
	 */
	public Object getData() {
		return edit.getText();
	}
}
