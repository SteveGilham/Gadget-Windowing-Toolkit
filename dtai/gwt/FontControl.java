/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: FontControl.java,v 1.5 1998/01/21 20:08:18 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/FontControl.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java11.awt.event.ActionEvent;
import java11.awt.event.ActionListener;

/**
 * The FontControl class
 *
 * GUI Class for editing font data
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class FontControl extends DataControl implements ActionListener {

    DotButtonGadget fontButton = new DotButtonGadget();
    Font font;
    FontDialog fontDialog;

	/**
	 * constructor which creates a font control with the specified label
	 * @param label  the label of the font control
	 */
	public FontControl (String label) {
		this(label, new Font("TimesRoman", Font.PLAIN, 10));
	}

	/**
	 * constructor which creates a font control with the specified label
	 * and data
	 * @param label  the label of the font control
	 * @param data  the data(font) of the font control
	 */
	public FontControl (String label, Object data) {
        fontButton.addActionListener(this);
        add(fontButton);
        fontButton.setVisible(false);
		this.label.setLabel(label);
		setData(data);
		//edit.setEditable(false);
    }

	/**
	 * sets the data(font) of the font control
	 * @param data  the data(font) of the font control
	 */
	public void setData(Object data) {
	    if (data instanceof Font) {
	        font = (Font) data;
	        String text = font.getName();
	        int style = font.getStyle();
	        if ((style & Font.BOLD) != 0) {
	            if ((style & Font.ITALIC) != 0) {
	                text += ", Bold, Italic";
	            } else {
	                text += ", Bold";
	            }
	        } else if ((style & Font.ITALIC) != 0) {
                text += ", Italic";
	        }
	        text += ", "+Integer.toString(font.getSize())+" pt";
            edit.setText(text);
	    }
	}

	/**
	 * gets the data(font) of the font control
	 * @return  the data(font) of the font control
	 */
	public Object getData() {
		return font;
	}

	/**
	 * brings up the font choice box
	 * @param in  whether or not the font control is selected
	 */
	public void setSelected(boolean in) {
	    super.setSelected(in);
	    if (in) {
    	    fontButton.setVisible(true);
    		fontButton.requestFocus();
	    } else {
    	    fontButton.setVisible(false);
	    }
    }

    /**
     * resets the data of the font control
     */
    public void reset() {
        super.reset();
        fontButton.setVisible(false);
    }

   	/**
     * arranges the gadgets
     */
   	public void doLayout() {
        super.doLayout();
        Dimension d = fontButton.getPreferredSize();
        Rectangle rect = edit.getBounds();
        fontButton.setBounds(rect.x+rect.width-d.width, rect.y, d.width, rect.height, false);
    }



	/**
	 * actionPerformed
	 * @param e  the action event
	 */
	public void actionPerformed(ActionEvent e) {
	    if (fontDialog == null) {
	        fontDialog = new FontDialog(this);
	    }
	    fontDialog.setFontLabels(font);
	    fontDialog.show();
	}

}
