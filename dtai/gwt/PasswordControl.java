/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: PasswordControl.java,v 1.1 1997/08/25 20:33:52 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/PasswordControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

/**
 * The PasswordControl class
 *
 * GUI Class for editing string(password) data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class PasswordControl extends StringControl {

	/**
	 * constructor which creates a password control with the specified label
	 * @param label  the label of the password control
	 */
	public PasswordControl (String label) {
		this(label, "");
	}

	/**
	 * constructor which creates a password control with the specified label
	 * and data(password string)
	 * @param label  the label of the password control
	 * @param data  the data(password string) of the password control
	 */
	public PasswordControl (String label, Object data) {
	    super(label, data);
		edit.setEchoChar('*');
	}
}
