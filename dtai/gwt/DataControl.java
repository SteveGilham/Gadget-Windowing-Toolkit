/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: DataControl.java,v 1.6 1997/10/04 02:40:57 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/DataControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Color;
import java.awt.Dimension;
import java11.awt.event.MouseEvent;
import java11.awt.event.MouseListener;
import java11.awt.event.FocusListener;
import java11.awt.event.FocusEvent;


/**
 * The DataControl abstract base
 *
 * Base class for data edit control for edit attribute data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public abstract class DataControl extends BorderGadget implements MouseListener, FocusListener {

	protected Object data;
	protected LabelGadget label = new LabelGadget("", LabelGadget.HORIZ_ALIGN_LEFT);
	protected TextFieldGadget edit = new TextFieldGadget();
    protected String lastValue;
    protected boolean selected;

    protected double max = -1.0;
    protected double min = -1.0;

	/**
	 * sets the data of the data control
	 * @param data  the data of the data control
	 */
	public abstract void setData(Object data);

	/**
	 * gets the data of the data control
	 * @return  the data of the data control
	 */
	public abstract Object getData();


    protected Object oldData;

	/**
	 * saves the data of the data control
	 * @param data  the data of the data control
	 */
	public void saveData(Object data) {
	    if ( data != null && ! data.equals(oldData) ) {
        	setData(data);
        	oldData = data;
            if (getParent() instanceof DataSheet) {
        	    ((DataSheet) getParent()).updateScreen(this);
        	}
        }
	}

	/**
	 * default constructor which constructs a data control
	 */
	public DataControl() {
		setLayout(null);
		setNoInsets();

        setBorderType(BorderGadget.LINE);
        setBorderThickness(1);
        setBorderColor(Color.gray);

        edit.setBorderType(BorderGadget.LINE);
        edit.setBorderThickness(1);
        edit.setBorderColor(Color.gray);
        edit.setBackground(label.getBackground());
        label.addMouseListener(this);
        edit.addMouseListener(this);
        edit.addFocusListener(this);
 		add(label);
		add(edit);

	}

    public DataControl(Object data) {
        this();
        oldData = data;
    }

	/**
	 * responds to the user selecting the data control
	 * @param in  whether or not the data control is selected
	 */
	public void setSelected(boolean in) {
	    if (in != selected) {
	        selected = in;
            label.setSelected(in);
            if (in) {
    	        edit.requestFocus();
    	        edit.selectAll();
    	    }
    	    if (getParent() instanceof DataSheet) {
    	        ((DataSheet) getParent()).setActiveControl(this);
    	    }
    	    lastValue = edit.getText();
        }
	}

	/**
	 * arranges the gadgets
	 */
	public void doLayout() {
		if (getParent() != null) {
			int maxSize = getParent().getSize().width/3;
			Dimension editSize = edit.getPreferredSize();
			label.setBounds(1, 1, maxSize-2, editSize.height-2, false);
			edit.setBounds(maxSize, 0, maxSize*2, editSize.height, false);
			setSize(getParent().getSize().width , editSize.height, false);
		}
	}

    /**
     * gets the preffered size of the data control
     * @return  the preferred size
     */
    public Dimension getPreferredSize() {
     	if (getParent() != null) {
     	    Dimension editSize = edit.getPreferredSize();
     	    Dimension labelSize = label.getPreferredSize();
     	    return new Dimension (editSize.width+labelSize.width+140, editSize.height);
     	}
     	return new Dimension(0, 0);
    }

    /**
     * gets the minimum size of the data control
     * @return  the minimum size
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * resets the data of the data control
     */
    public void reset() {
        if (lastValue != null) {
            edit.setText(lastValue);
        }
    }

    /**
     * gets the label text of the data control
     * @return  the label text of the data control
     */
    public String getLabelText() {
        return label.getLabel();
    }

    public void setMaxMin(double max, double min) {
        this.max = max;
        this.min = min;
    }

    /**
     * mouseClicked
     * @param e  the mouse event
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * mousePressed
     * @param e  the mouse event
     */
    public void mousePressed(MouseEvent e) {
        setSelected(true);
    }

    /**
     * mouseReleased
     * @param e  the mouse event
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * mouseEntered
     * @param e  the mouse event
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * mouseExcited
     * @param e  the mouse event
     */
    public void mouseExited(MouseEvent e) {
    }

    public void focusGained(FocusEvent e) {
        setSelected(true);
    }

    public void focusLost(FocusEvent e) {
    }

    public boolean isDialog() {
        return false;
    }

}

