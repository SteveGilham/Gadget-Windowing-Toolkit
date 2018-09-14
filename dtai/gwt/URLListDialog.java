 /****************************************************************
 **  Copyright 1996 by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: URLListDialog.java,v 1.1 1997/09/23 21:00:51 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/URLListDialog.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import dtai.gui.StdDialog;
import dtai.gwt.TextAreaGadget;
import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;
import java.awt.Frame;
import java.util.StringTokenizer;
import java11.awt.event.ActionEvent;
import java11.awt.event.ActionListener;
import java11.awt.event.ItemEvent;
import java11.awt.event.ItemListener;
import java11.util.EventObject;

/**
 * the URLListDialog class
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class URLListDialog extends StdDialog implements ActionListener, ItemListener {

	private ButtonGadget done;
	private ButtonGadget cancel;
	private ButtonGadget add;
	private ButtonGadget delete;

	private PanelGadget body;
	private URLListControl urlListControl;
	private URLList url;

	private TableGadget urlTable = new TableGadget(7);
	private PanelGadget newURLPanel = new PanelGadget();
	private TextFieldGadget description = new TextFieldGadget(15);
	private TextFieldGadget urlText = new TextFieldGadget();
	private ComboBoxGadget urlPrefix = new ComboBoxGadget(3);
	private PanelGadget urlEditPanel = new PanelGadget();
	private boolean isVisible = false;

    /**
     * constructor which creates a URL List dialog box with the
     * specified URL List control
     * @param control  the URL List control to be placed in the edit dialog box
     */
    public URLListDialog(URLListControl control) {
		super(control.getShell().getFrame(), "URL List Edit", false);
        setInitialPosition(GadgetDialog.UPPER, GadgetDialog.CENTER);
		this.urlListControl = control;
		body = getBody();
		body.setLayout(new GadgetBorderLayout());
		urlEditPanel.setLayout(new GadgetBorderLayout());
		newURLPanel.setLayout(new GadgetGridLayout(2,2));
		buildMenu();
	}

	private void buildMenu () {
		addButtonGadget( done = new ButtonGadget( "OK" ) );
		addButtonGadget( add = new ButtonGadget( "Add" ) );
		addButtonGadget( delete = new ButtonGadget( "Delete" ) );
		addButtonGadget( cancel = new ButtonGadget( "Cancel" ) );
		done.addActionListener(this);
		add.addActionListener(this);
		delete.addActionListener(this);
		cancel.addActionListener(this);

		urlPrefix.add("http://");
		urlPrefix.add("mailto://");
		urlPrefix.add("ftp://");
		urlPrefix.add("gopher://");
		urlPrefix.select(0);
		urlEditPanel.add("Center", new LabelGadget("URL:"));
		urlEditPanel.add("East", urlPrefix);

		delete.setEnabled(false);
        urlTable.addColumn("Description", 200);
        urlTable.addColumn("URL", 150);
        urlTable.addItemListener(this);
        newURLPanel.add(new LabelGadget("Description:", DisplayGadget.HORIZ_ALIGN_LEFT ));
        newURLPanel.add(description);
        newURLPanel.add(urlEditPanel);
        newURLPanel.add(urlText);
        body.add("North", urlTable);
        SeparatorGadget separator =  new SeparatorGadget();
        separator.setVMargin(15);
        body.add("Center", separator);
        body.add("South", newURLPanel);
	}

	/**
	 * sets the URL List of the URL List dialog box
	 * @param URLList the urls of the URL List edit dialog box
	 */
	public void setURLList(URLList url) {
	    this.url = (URLList) url.clone();
	}

    private boolean addURL() {
	    if (description.getText().equals("") &&
	        urlText.getText().equals("")) {
	            return true;
	        }
	    try {
	        if (description.getText().equals("")) {
                dtai.util.ShowUser.info("The Description field can not empty!");
                return false;
            }
            this.url.addURL(description.getText(), urlPrefix.getText() + urlText.getText());
            description.setText("");
            urlText.setText("");
            refreshList();
	    }
	    catch (MalformedURLException e) {
            dtai.util.ShowUser.error(e, "Invalid format for URL value. ");
	        return false;
	    }
        return true;
    }

	/**
     * actionPerformed
     * @param ae  the action event
     */
	public void actionPerformed (ActionEvent ae) {
		if (ae.getSource() == done) {
		    if (addURL()) {
		        saveURLs();
			    hide();
		    }
		}
		else if (ae.getSource() == cancel) {
		    hide();
		} else if (ae.getSource() == add) {
		    addURL();
		} else if (ae.getSource() == delete) {
		    int row = urlTable.getSelectedIndex();
		    if (row >= 0) {
		        this.url.removeURL(urlTable.getCell(row, 0));
                refreshList();
		    }
		}
	}

	public void saveURLs() {
	    this.urlListControl.setData(this.url);
   	}

    /**
     * show
     */
    public void show() {
        refreshList();
        isVisible = true;
        super.show();
    }

    /**
     * hide
     */
    public void hide() {
        isVisible = false;
        super.hide();
    }

    public boolean dialogVisible() {
        return isVisible;
    }

    private void refreshList() {
        urlTable.removeAllRows();
        delete.setEnabled(false);
        if (url != null) {
            String desc[] = url.getDescriptions();
            URL urls[] = url.getURLs();
            if (urls != null) {
                for (int i = 0; i < urls.length; i++) {
                    urlTable.setCell(i, 0, desc[i]);
                    urlTable.setCell(i, 1, urls[i].toString());
                }
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            delete.setEnabled(true);
        } else {
            delete.setEnabled(false);
        }
    }




}
