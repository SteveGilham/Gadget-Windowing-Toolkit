/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: DoubleControl.java,v 1.5 1997/10/04 02:40:57 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/DoubleControl.java,v $
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
 * The DoubleControl class
 *
 * GUI Class for editing floating point data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class DoubleControl extends DataControl implements KeyListener, FocusListener {

   MaskedTextFieldGadget maskText = new MaskedTextFieldGadget(" 99999999999");

	public DoubleControl (String label) {
		this(label, new Double(0.0));
	}

	/**
     * constructor which creates a double control with the specified label and data
     * @param label  the label of the double control
     * @param data  the floating point data of the double control
     */
	public DoubleControl (String label, Object data) {
	    super(data);
		this.label.setLabel(label);
        maskText.setSpecialChar('.');
		setData(data);
	    //edit.setEditable(false);
	    maskText.setFont(edit.getFont());
	    add(maskText);
	    //maskText.setNextFocusGadget(maskText);
	    maskText.addFocusListener(this);
	    maskText.setVisible(false);
	    maskText.addKeyListener(this);
        maskText.setBackground(this.label.getBackground());
        maskText.setNewLimitChar(' ', " -1234567890");
	}

	/**
	 * sets the data(floating point) of the double control
	 * @param data  the floating point data of the double control
	 */
	public void setData(Object data) {
	    if ( data != null ) {
            edit.setText(doubleToString((Double)data));
            maskText.setText(doubleToString((Double)data));
        }
        else {
            edit.setText("0.0");
            maskText.setText("0.0");
        }
	}

	/**
     * gets the data(floating point) of the double control
     * @return  the floating point data of the double control
     */
	public Object getData() {
		return new Double(edit.getText().trim());
	}

	/**
	 * brings up a masked text edit field
	 * @param in  whether or not the double control is selected
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
            Double temp = new Double(maskText.getText().trim());
            double d = temp.doubleValue();
            if ((max == -1.0 && min == -1.0) || ( d >= min && d <= max )) {
                edit.setText(doubleToString(temp));
            }
            else {
                dtai.util.ShowUser.warning("Input " + d + " outside acceptable range ["
                    + min + ", " + max + "].  Resetting to " + oldData.toString() + "...");
            }
        }
        catch(Exception e) {
            dtai.util.ShowUser.error(e, "Invalid format for real value.");
        }
    }

    /**
     * resets the data of the double control
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

    String doubleToString(Double d) {
        boolean negtive = false;
        String retVal = "";
        double newVal = d.doubleValue();
        if (newVal < 0.0) {
            negtive = true;
            newVal = newVal * -1.0;
        }
        if (newVal == 0.0) return "0.00";
        newVal = newVal * 1000000.0;
        int mag = 0;
        while (newVal > 1.0) {
            newVal = (newVal/10.0);
            mag++;
        }
        newVal = Math.abs(d.doubleValue()) * 1000000.0;
        for (int i = mag - 1; i >= 0; i-- ) {
            double val = Math.pow(10.0, i);
            int character = (int) (newVal/val);
            retVal += character;
            newVal -= (val*character);
        }
        int size = 7 - retVal.length();
        for (int j = size; j > 0; j--) {
            retVal = "0" + retVal;
        }
        if (negtive) {
            return "-" + retVal.substring(0, retVal.length()-6) + "." + retVal.substring(retVal.length()-6, retVal.length());
        }
        else {
            return retVal.substring(0, retVal.length()-6) + "." + retVal.substring(retVal.length()-6, retVal.length());
        }
     }

}
