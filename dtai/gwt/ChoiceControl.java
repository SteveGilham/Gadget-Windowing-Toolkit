/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: ChoiceControl.java,v 1.6 1997/12/09 03:22:23 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/ChoiceControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Rectangle;
import java11.awt.event.ItemEvent;
import java11.awt.event.ItemListener;
import java11.awt.event.FocusListener;
import java11.awt.event.FocusEvent;

/**
 * The StringControl class
 *
 * GUI Class for editing string data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class ChoiceControl extends DataControl implements ItemListener, FocusListener {

    ComboBoxGadget choice;

	/**
	 * Constructor which creats a choice control with the specified
	 * label and combo box gadget
	 * @param label  the label of the choice control
	 * @param choice  the combo box gadget of the choice control
	 */
	protected ChoiceControl (String label, ComboBoxGadget choice) {
		this(label, new String(""), choice);
	}

	/**
	 * Constructor which creats a choice control with the specified
	 * label, combo box gadget, and data
	 * @param label  the label
	 * @param choice  the combo box gadget of the choice control
	 * @param data  the data of the choice control
	 */
	protected ChoiceControl (String label, Object data, ComboBoxGadget choice) {
		this.choice = choice;
        choice.addFocusListener(this);
        choice.setVisible(false);
        choice.addItemListener(this);
        add(choice);
		this.label.setLabel(label);
		setData(data);
	}
   /**
     * focusGained
     * @param e - the focus event
     */
    public void focusGained(FocusEvent e) {
        setSelected(true);
    }

    /**
     * focusLost
     * @param e - the focus event
     */
    public void focusLost(FocusEvent e) {
    }

	/**
	 * sets the data of the choice control
	 * @param data  the data of the choice control
	 */
	public void setData(Object data) {
        edit.setText(data.toString());
        choice.select(data.toString());
	}

	/**
     * gets the data of the choice control
     * @return  the data of the choice control
     */
	public Object getData() {
		return choice.getText();
	}

	/**
	 * brings up the choice control box
	 * @param in  whether or not the choice control is selected
	 */
	public void setSelected(boolean in) {
	    super.setSelected(in);
	    if (in) {
    	    choice.setVisible(true);
    	    choice.requestFocus();
    	    choice.showList();
	    } else {
    	    choice.setVisible(false);
            edit.setText(choice.getText());
	    }
    }

    /**
     * invoked when an item's state has changed
     * @param e  the item event
     */
    public void itemStateChanged(ItemEvent e) {
        edit.setText(choice.getText());
        saveData(edit.getText());
    }

    /**
     * resets the data of the choice control
     */
    public void reset() {
        super.reset();
        choice.setVisible(false);
    }

	/**
	 * arranges the gadgets
     */
	public void doLayout() {
        super.doLayout();
        Rectangle rect = edit.getBounds();
        choice.setBounds(rect.x, rect.y, rect.width-1, rect.height);
    }

}
