 /****************************************************************
 **  Copyright 1996 by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: StringDialog.java,v 1.4 1997/09/24 22:13:35 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/StringDialog.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import dtai.gui.StdDialog;
import dtai.gwt.TextAreaGadget;
import java.util.Vector;
import java.awt.Frame;
import java.util.StringTokenizer;
import java11.awt.event.ActionEvent;
import java11.awt.event.ActionListener;
import java11.awt.event.ItemEvent;
import java11.awt.event.ItemListener;
import java11.util.EventObject;

/**
 * the StringDialog class
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class StringDialog extends StdDialog implements ActionListener {

	private ButtonGadget done;
	private ButtonGadget cancel;

	private PanelGadget body;
	private StringArrayControl stringControl;
	private String strings[];
    private PanelGadget listPanel = new PanelGadget();
    private PanelGadget editboxPanel = new PanelGadget();

    private TextAreaGadget textArea = new TextAreaGadget(7, 30);
    private LabelGadget listLabel = new LabelGadget("");
    private boolean isVisible = false;
    private static String default_strings[] = {""};

    /**
     * constructor which creates a string dialog box with the
     * specified string array control
     * @param control  the string array control to be placed in the string dialog box
     */
    public StringDialog(StringArrayControl control) {
		super(control.getShell().getFrame(), "String Array Edit", false);
        setInitialPosition(GadgetDialog.UPPER, GadgetDialog.CENTER);

		this.stringControl = control;
		body = getBody();
		body.setLayout(new GadgetBorderLayout());
		buildMenu();
	}

	private void buildMenu () {

		addButtonGadget( done = new ButtonGadget( "OK" ) );
		addButtonGadget( cancel = new ButtonGadget( "Cancel" ) );

		done.addActionListener(this);
		cancel.addActionListener(this);

        listLabel.setLabel(stringControl.getLabelText());
        listPanel.setLayout( new GadgetBorderLayout() );
        listPanel.add("North", listLabel);
        listPanel.add("South", textArea);
        body.add("North", listPanel);

	}

	/**
	 * sets the strings of the string dialog box
	 * @param strings[]  the strings of the string dialog box
	 */
	public void setStrings(String strings[]) {
	    this.strings = strings;
	    textArea.setText("");
	    for (int i = 0; i < strings.length; i++) {
	        textArea.append(strings[i]+"\n");
	    }
	}

	/**
     * actionPerformed
     * @param ae  the action event
     */
	public void actionPerformed (ActionEvent ae) {
		if (ae.getSource() == done) {
		    saveStrings();
			hide();
		}
		else if (ae.getSource() == cancel) {
			hide();
		}
	}

	public void saveStrings() {
	    String text = textArea.getText();
	    Vector v = new Vector();
	    strings = default_strings;
	    StringTokenizer toks = new StringTokenizer( text, "\n", true );
        boolean blankLine = false;
        while ( toks.hasMoreTokens() ) {
            String line = toks.nextToken();
            if (line.equals("\n")) {
                if (blankLine) {
                    v.addElement("");
                }
                blankLine = true;
            } else {
                blankLine = false;
                v.addElement(line);
            }
        }
        if (v.size() > 0) {
	        strings = new String[v.size()];
	        for (int i = 0; i < v.size(); i++) {
	            strings[i] = (String) v.elementAt(i);
	        }
        }
        stringControl.saveData(strings);
   	}

    /**
     * show
     */
    public void show() {
        super.show();
        isVisible = true;
        textArea.selectAll();
    }

     /**
     * hide
     */
    public void hide() {
        super.hide();
        isVisible = false;
    }

    public boolean dialogVisible() {
        return isVisible;
    }

    public boolean isDialog() {
        return true;
    }


}
