/****************************************************************
 **  Copyright 199X by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************
 **
 **  $Id: DataSheet.java,v 1.5 1997/10/06 22:15:53 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/DataSheet.java,v $
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Dimension;
import java11.awt.event.KeyEvent;
import java11.awt.event.KeyListener;
import java11.awt.event.FocusListener;
import java11.awt.event.FocusEvent;
import java11.awt.event.ItemListener;
import java11.awt.event.ItemEvent;
import java.awt.Event;

/**
 * The DataSheet Class
 *
 * Container class for Data Controls
 *
 * @version 1.0  19 Feb 1997
 * @author       DTAI, Inc.
 */

public class DataSheet extends PanelGadget implements java11.awt.ItemSelectable, KeyListener {

    private DataControl activeControl = null;
    private int selectedIndex[] = new int[1];
    private String selectedName[] = {""};

    ItemListener itemListener;

	/**
	 * default constructor which constructs a data sheet
	 */
	public DataSheet() {
		setLayout(null);
		this.addKeyListener(this);
	}

	/**
	 * gets the active control of the data sheet
	 * @return  the active control
	 */
	public DataControl getActiveControl() {
	    return activeControl;
	}

    public void clearSelection() {
        if (activeControl != null) {
            activeControl.setSelected(false);
        }
    }

	/**
	 * sets the active control of the data sheet
	 * @param control  the active control
	 */
	public void setActiveControl(DataControl control) {
	    if (control != activeControl) {
	        if (activeControl != null) {
	            activeControl.setSelected(false);
	            activeControl.saveData(activeControl.getData());
	        }
    	    this.activeControl = control;
	    }
	}

	/**
	 * arranges the gadgets
	 */
	public void doLayout() {
		int totalHeight = 0;
		Gadget g[] = getGadgets();
		for (int i = 0; i < getGadgetCount(); i++) {
		    if (g[i].isVisible()) {
    		    g[i].doLayout();
    		    g[i].setLocation(0, totalHeight);
    		    totalHeight += g[i].getSize().height-1;
    		}
		}
	}

    /**
     * gets the preferred size of the data sheet
     * @return  the preferred size
     */
    public Dimension getPreferredSize() {
		int totalHeight = 0;
		int totalWidth = 0;
        doLayout();
		Gadget g[] = getGadgets();
		for (int i = 0; i < getGadgetCount(); i++) {
		    if (g[i].isVisible()) {
    		    Dimension d = g[i].getPreferredSize();
    		    totalHeight += d.height-1;
    		    if (totalWidth < d.width) {
    		        totalWidth = d.width;
    		    }
    		}
		}
     	return new Dimension(totalWidth, totalHeight+1);
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
        if (e.getKeyCode() == KeyEvent.ESCAPE) {
            if (activeControl != null) {
                activeControl.reset();
            }
        } else if (//(e.getKeyCode() == KeyEvent.UP) ||
                   ((e.getKeyCode() == KeyEvent.TAB) &&
                    (e.isShiftDown()))) {
            if ( activeControl != null) {
                int index = getGadgetIndex(activeControl);
                if (index == 0) {
                    index = getGadgetCount()-1;
                } else {
                    index--;
                }
                DataControl newControl = (DataControl) getGadget(index);
                newControl.setSelected(true);
                e.consume();
            }
        } else if (//(e.getKeyCode() == KeyEvent.DOWN) ||
                   (e.getKeyCode() == KeyEvent.ENTER) ||
                   (e.getKeyCode() == KeyEvent.TAB)) {
            if ( activeControl != null) {
                if (e.getKeyCode() == KeyEvent.ENTER && activeControl.isDialog()) {
                    return;
                }
                int numVis = getVisibleGadgetCount();
                if (numVis != 0) {
                    int index = getGadgetIndex(activeControl);
                    if (!(index == numVis-1)) {
                        index++;
                    }
                    else {
                        index = 0;
                    }
                    while (!getGadget(index).isVisible()) {
                        index++;
                    }
                    DataControl newControl = (DataControl) getGadget(index);
                    newControl.setSelected(true);
                    e.consume();
                }
            }
        }
    }

    private int getVisibleGadgetCount() {
        Gadget g[] = getGadgets();
        int visible = 0;
        for (int i = 0; i < g.length; i++) {
            if (g[i].isVisible()) visible++;
        }
        return visible;
    }

    /**
     * keyReleased
     * @param e  the key event
     */
    public void keyReleased(KeyEvent e) {
    }


	/**
	 * Adds the specified listener to be notified when component
	 * events occur on this component.
	 *
	 * @param l 	the listener to receive the events
	 */
	public synchronized void addItemListener(ItemListener l) {
        itemListener = GWTEventMulticaster.add(itemListener, l);
	}

	/**
	 * Removes the specified listener so it no longer receives
	 * item events on this item.
	 *
	 * @param l 		the listener to remove
	 */
	public synchronized void removeItemListener(ItemListener l) {
        itemListener = GWTEventMulticaster.remove(itemListener, l);
	}

    /**
     * gets the selected indeces
     * @return int[]
     */
    public int[] getSelectedIndexes() {
        if (activeControl != null) {
            selectedIndex[0] = getGadgetIndex(activeControl);
        }
        return selectedIndex;
    }


    /**
     * gets the selected items
     * @return  the selected items
     */
    public String[] getSelectedItems() {
        if (activeControl != null) {
            selectedName[0] = activeControl.getLabelText();
        }
        return selectedName;
    }

	/**
	 * updates the screen
	 * @param control  the control to be updated
	 */
	public void updateScreen(DataControl control) {
        ItemEvent itemEvent = new ItemEvent (this, new Event( control, -1, null ),
                                             ItemEvent.ITEM_STATE_CHANGED, control,
                                             ItemEvent.SELECTED);
        processItemEvent(itemEvent);
    }

    /**
     * processItemEvent
     * @param e  the item event
     */
    protected void processItemEvent(ItemEvent e) {
		if (itemListener != null) {
    		if(e.getID() == ItemEvent.ITEM_STATE_CHANGED) {
    			itemListener.itemStateChanged(e);
    		}
    	}
	}



}
