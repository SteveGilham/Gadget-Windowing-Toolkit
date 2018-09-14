/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: BooleanControl.java,v 1.9 1997/12/09 03:22:22 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/BooleanControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Rectangle;
import java11.awt.event.ItemEvent;
import java11.awt.event.ItemListener;

/**
 * The BooleanControl class
 *
 * GUI Class for editing boolean data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class BooleanControl extends DataControl implements ItemListener {

    ChoiceGadget choice = new ChoiceGadget(2);

	/**
	 * Constructor which creates a boolean control with the specified label
	 * @param   the label of the boolean control
	 */
	public BooleanControl (String label) {
		this(label, new Boolean(false));
	}

	/**
	 * Constructor which creates a boolean control with the specified label and the data
	 * set to true or false
	 * @param   the label of the boolean control
	 * @param data  the data(true/false) of the boolean control (as a String)
	 */
	public BooleanControl (String label, Object data) {
	    super(data);
        choice.add("false");
        choice.add("true");
        choice.setVisible(false);
        choice.addItemListener(this);
        add(choice);
		this.label.setLabel(label);
		setData(data);
		//edit.setEditable(false);
	}

	/**
	 * sets the data of the boolean control either true or false
	 * @param data  the data(true/false) of the boolean control
	 */
	public void setData(Object data) {
	    if (data != null) {
	        if ( ((Boolean)data).booleanValue() ) {
	            if (choice.getSelectedIndex() != 1) {
                    choice.select(1);
                }
            }
	        else {
	            if (choice.getSelectedIndex() != 0) {
                    choice.select(0);
                }
	        }
	    }
        else {
            edit.setText("false");
        }
	}

	/**
	 * gets the data(true/false) of the boolean control
	 * @return  the data of the boolean control(true/false)
	 */
	public Object getData() {
		return new Boolean(edit.getText());
	}

	/**
	 * brings up the boolean choice box
	 * @param in  whether or not the boolean control is selected
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
     * Invoked when an item's state has been changed.
     * @param e  the item event
     */
    public void itemStateChanged(ItemEvent e) {
        edit.setText(choice.getText());
        saveData(new Boolean(edit.getText()));

    }

    /**
     * resets the data of the control
     */
    public void reset() {
        super.reset();
        choice.setVisible(false);
    }

	/**
	 * arranges gadgets
	 */
	public void doLayout() {
        super.doLayout();
        Rectangle rect = edit.getBounds();
        choice.setBounds(rect.x, rect.y, rect.width-1, rect.height);
    }

    public ChoiceGadget getChoice() {
        return choice;
    }
}
