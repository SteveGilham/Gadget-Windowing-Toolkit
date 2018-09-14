/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: IntegerControl.java,v 1.8 1997/10/16 17:30:16 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/IntegerControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import dtai.util.ShowUser;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java11.awt.event.FocusEvent;
import java11.awt.event.FocusListener;
import java11.awt.event.KeyEvent;
import java11.awt.event.KeyListener;

/**
 * The IntegerControl class
 *
 * GUI Class for editing integer data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class IntegerControl extends DataControl implements KeyListener, FocusListener {

    MaskedTextFieldGadget maskText = new MaskedTextFieldGadget("C99999999999999");

	public IntegerControl (String label) {
		this(label, new Integer(0));
	}

	/**
	 * constructor which creates an integer control with the specified label
	 * and data
	 * @param label  the label of the integer control
	 * @param data  the data(integer) of the integer control
	 */
	public IntegerControl (String label, Object data) {
	    super(data);
		this.label.setLabel(label);
		setData(data);
	    maskText.setFont(edit.getFont());
		add(maskText);
	    maskText.addFocusListener(this);
	    maskText.setVisible(false);
	    maskText.addKeyListener(this);
        maskText.setBackground(this.label.getBackground());
        maskText.setNewLimitChar(' ', " -1234567890");

	}

	/**
     * sets the data(integer) of the integer control
     * @param data  the data(integer) of the integer control
     */
	public void setData(Object data) {
	    if ( data != null ) {
            edit.setText(data.toString());
            maskText.setText(data.toString());
        }
        else {
            edit.setText("0");
            maskText.setText("0");
        }
	}

	/**
     * gets the data(integer) of the integer control
     * @return  the data(integer) of the integer control
     */
	public Object getData() {
	    String value = edit.getText().trim();
	    if (value.length()==0) {
	        return new Integer(0);
	    }
		return new Integer(value);
	}

	/**
	 * brings up a masked text field edit
	 * @param in  whether or not the integer control is selected
	 */
	public void setSelected(boolean in) {
	    super.setSelected(in);
	    if (in) {
	        maskText.setBounds(edit.getBounds());
    	    maskText.setVisible(true);
    	    maskText.setText(edit.getText());
    	    maskText.requestFocus();
	    } else {
    	    maskText.setVisible(false);
	    }
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
            saveData();
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
        saveData();
    }

    private void saveData() {
        try {
            Integer temp;
            if (maskText.getText().trim().equals(""))
               temp = new Integer(0);
            else
               temp = new Integer(maskText.getText().trim());
            int i = temp.intValue();
            if ((max == -1.0 && min == -1.0) || ( i >= min && i <= max )) {
                edit.setText(temp.toString());
            }
            else {
                dtai.util.ShowUser.warning("Input " + i + " outside acceptable range ["
                    + min + ", " + max + "].  Resetting to " + oldData.toString() + "...");
            }
        }
        catch(Exception e) {
            dtai.util.ShowUser.error(e, "Invalid format for Integer value.");
        }
    }

    /**
     * resets the data of the integer control
     */
    public void reset() {
        super.reset();
        maskText.setText(edit.getText());
        maskText.setVisible(false);
    }

    /**
     * arranges the gadgets
     */
    public void doLayout() {
        super.doLayout();
        Rectangle rect = edit.getBounds();
        maskText.setBounds(rect.x, rect.y, rect.width-1, rect.height);
    }

}
