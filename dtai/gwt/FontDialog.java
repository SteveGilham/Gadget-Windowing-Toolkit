 /****************************************************************
 **  Copyright 1996 by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: FontDialog.java,v 1.3 1997/12/24 20:51:08 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/FontDialog.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import dtai.gui.StdDialog;
import java.awt.Font;
import java.awt.Frame;
import java11.awt.event.ActionEvent;
import java11.awt.event.ActionListener;
import java11.awt.event.ItemEvent;
import java11.awt.event.ItemListener;
import java11.util.EventObject;

/**
 * FontDialog
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class FontDialog extends StdDialog implements ActionListener, ItemListener {

	private ButtonGadget cancel;
	private ButtonGadget ok;

	private LabelGadget fontLabel = new LabelGadget("Font Face");
	private LabelGadget styleLabel = new LabelGadget("Font Style");
	private LabelGadget sizeLabel = new LabelGadget("Font Size");
	private LabelGadget sampleLabel = new LabelGadget("Sample");

	private ComboBoxGadget fontSize = new ComboBoxGadget(5);
	private FontStyleChoice styleCombo = new FontStyleChoice();
	private FontFaceChoice fontCombo = new FontFaceChoice();
	private LabelGadget testLabel = new LabelGadget("AaBbYyZz");
	private PanelGadget body;
	private FontControl fontControl;

    /**
     * constructor which creats a font dialog box
     * @param control  the font control being placed in the font dialog box
     */
    public FontDialog(FontControl control) {
		super(control.getShell().getFrame(), "Font Selection", false);
		this.fontControl = control;
		body = getBody();
		body.setLayout(new GadgetGridLayout(5, 2));
		buildMenu();
	}

	private void buildMenu () {

		body.add(fontLabel);
		body.add(fontCombo);

		body.add(styleLabel);
		body.add(styleCombo);

		body.add(sizeLabel);
		fontSize.add("8");
		fontSize.add("10");
		fontSize.add("12");
		fontSize.add("14");
		fontSize.add("16");
        body.add(fontSize);

        body.add(new LabelGadget(""));
        body.add(new LabelGadget(""));

        body.add(sampleLabel);
        body.add(testLabel);

		addButtonGadget( ok = new ButtonGadget( "OK" ) );
		addButtonGadget( cancel = new ButtonGadget( "Cancel" ) );

		ok.addActionListener(this);
		cancel.addActionListener(this);
		fontSize.addItemListener(this);
		styleCombo.addItemListener(this);
        fontCombo.addItemListener(this);
	}

	/**
	 * sets the labels of the font
	 * @param font  the font whose label is being set
	 */
	public void setFontLabels(Font font) {
	    fontSize.setText(Integer.toString(font.getSize()));
	    fontCombo.setText(font.getName());
		styleCombo.setStyleValue(font.getStyle());
	}

	private void updateSample() {
	    Font newFont = new Font( fontCombo.getName(), styleCombo.getStyleValue(),
                                  Integer.parseInt(fontSize.getText()) );
        testLabel.setFont(newFont);
	}


	/**
	 * actionPerformed
	 * @param ae  the action event
	 */
	public void actionPerformed (ActionEvent ae) {
		if (ae.getSource() == ok) {
		    saveFontLabels();
			hide();
		}
		else if (ae.getSource() == cancel) {
			hide();
		}
	}

	private void saveFontLabels() {
		Font newFont = new Font( fontCombo.getName(), styleCombo.getStyleValue(),
                                  Integer.parseInt(fontSize.getText()) );
	    if (true) {
	        fontControl.saveData(newFont);
	    }
	    setFontLabels(newFont);
	}

	/**
     * invoked when an item's state has changed
     * @param e  the item event
     */
	public void itemStateChanged(ItemEvent e) {
        updateSample();
    }
}
