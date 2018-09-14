/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: ColorControl.java,v 1.5 1997/12/09 03:22:23 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/ColorControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Color;
import java.awt.Rectangle;
import java11.awt.event.ItemEvent;
import java11.awt.event.ItemListener;

/**
 * The StringControl class
 *
 * GUI Class for editing string data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class ColorControl extends DataControl implements ItemListener {

    ColorComboGadget choice = new ColorComboGadget();
    BorderGadget testColor = new BorderGadget();

	/**
	 * Constructor which creates a color control with the specified label
	 * @param label  the label of the color control
	 */
	public ColorControl (String label) {
		this(label, Color.black);
	}

	/**
	 * Constructor which creates a color control with the specified label
	 * and data(color)
	 * @param label  the label of the color control
	 * @param data  the data(color) of the color control
	 */
	public ColorControl (String label, Object data) {
	    super(data);
	    testColor.setBorderColor(Color.white);
	    testColor.setBorderThickness(1);
	    testColor.setBorderType(BorderGadget.LINE);
	    testColor.setBorderColor(Color.gray);
	    testColor.setMargins(1);
        choice.setNextFocusGadget(choice);
        choice.addItemListener(this);
        choice.setVisible(false);
        add(choice);
        add(testColor);
		this.label.setLabel(label);
		setData(data);
		//edit.setEditable(false);
		testColor.setBackground(choice.getColor());
	}

	/**
     * sets the color of the color control
     * @param data  the data(color) of the color control
     */
	public void setData(Object data) {
	    if (data instanceof Color) {
	        if (choice.getColor() != (Color) data) {
	            choice.setColor((Color) data);
	        }
            edit.setText(choice.getText());
       		testColor.setBackground(choice.getColor());
	    }
	}

	/**
	 * gets the color of the color control
	 * @return  the data(color) of the color control
	 */
	public Object getData() {
		return choice.getColor();
	}

	/**
	 * * brings up the color choice box
	 * @param in  whether or not the color control is selected
	 */
	public void setSelected(boolean in) {
	    super.setSelected(in);
	    if (in) {
	        choice.setBounds(edit.getBounds());
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
        testColor.setBackground(choice.getColor());
        saveData(choice.getColor());
    }

    /**
     * resets the data of the color control
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
        testColor.setBounds(rect.x, rect.y, 16, rect.height, false);
        choice.setBounds(rect.x+15, rect.y, rect.width-13, rect.height, false);
        edit.setBounds(rect.x+15, rect.y, rect.width-13, rect.height, false);
    }


}
