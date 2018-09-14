/****************************************************************
 **
 **  $Id: GridGadget.java,v 1.21 1998/02/26 15:10:29 chris Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/GridGadget.java,v $
 **
 ****************************************************************
 **
 **  Gadget Windowing Toolkit (GWT) Java Class Library
 **  Copyright (C) 1997  DTAI, Incorporated (http://www.dtai.com)
 **
 **  This library is free software; you can redistribute it and/or
 **  modify it under the terms of the GNU Library General Public
 **  License as published by the Free Software Foundation; either
 **  version 2 of the License, or (at your option) any later version.
 **
 **  This library is distributed in the hope that it will be useful,
 **  but WITHOUT ANY WARRANTY; without even the implied warranty of
 **  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 **  Library General Public License for more details.
 **
 **  You should have received a copy of the GNU Library General Public
 **  License along with this library (file "COPYING.LIB"); if not,
 **  write to the Free Software Foundation, Inc.,
 **  59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 **
 ****************************************************************/

package dtai.gwt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Insets;
import java.util.Vector;
import java11.awt.Adjustable;
import java11.awt.event.AdjustmentEvent;
import java11.awt.event.AWTEvent;
import java11.awt.event.FocusEvent;
import java11.awt.event.InputEvent;
import java11.awt.event.KeyEvent;
import java11.awt.event.MouseEvent;

/**
 * A scrolling list of text items.
 * @version 1.1
 * @author DTAI, Incorporated
 */

public class GridGadget extends ScrollPaneGadget implements CellSelectable {

    private MultiColumnPanel panel;
    CellListener cellListener;

    private Vector headers = new Vector(); // of ButtonGadget's for column headers
    private boolean showingCursor = false;
    private int mouseDownX;
    private MCColumnHeader resizeHeader;
    private int origWidth;
    private MCColumnHeader lastHeaderClicked;
    private boolean columnWidthsSet = false;
    int prefRows;
    boolean multipleMode;
    boolean showGrid = false;
    private int topIndex = 0;
    private boolean settingTop = false;

    /**
     * Creates a new scrolling list initialized with no visible Lines or multiple selections.
     */

    public GridGadget() {
        this( 0, false );
    }

    /**
     * Creates a new scrolling list initialized with the specified number of visible lines and a boolean stating whether multiple selections are allowed or not.
     *
     * @param rows - the number of items to show.
     */

    public GridGadget(int rows) {
        this( rows, false );
    }

    /**
     * Creates a new scrolling list initialized with the specified number of visible lines and a boolean stating whether multiple selections are allowed or not.
     *
     * @param rows - the number of items to show.
     * @param multipleMode - if true then multiple selections are allowed.
     */

    public GridGadget(int rows, boolean multipleMode) {
        setMultipleMode(multipleMode);
        setStretchingHorizontal( true );
        setStretchingVertical( true );
        setShrinkingVertical( true );
        add( panel = new MultiColumnPanel(this) );
        prefRows = rows;
    }

    /**
     * Returns true if this list allows multiple selections.
     *
     * @see setMultipleMode
     * @return boolean
     */

    public boolean isMultipleMode() {
        return multipleMode;
    }

    /**
     * Sets whether this list should allow multiple selections or not.
     *
     * @param b - the boolean to allow multiple selections
     * @see isMultipleMode
     */

    public void setMultipleMode(boolean b) {
        multipleMode = b;
    }

    /**
     * Returns true if showing visible grid
     *
     * @see setShowGrid
     * @return boolean
     */

    public boolean getShowGrid() {
        return showGrid;
    }

    /**
     * Sets whether this list should show the grid.
     *
     * @param b - TBD
     * @see getShowGrid
     */

    public void setShowGrid(boolean b) {
        showGrid = b;
    }

	/**
	 * Adds the specified listener to be notified when component
	 * events occur on this component.
	 *
	 * @param l 	the listener to receive the events
	 */
	public synchronized void addCellListener(CellListener l) {
        cellListener = GWTEventMulticaster.add(cellListener, l);
	}

	/**
	 * Removes the specified listener so it no longer receives
	 * cell events on this cell.
	 *
	 * @param l 		the listener to remove
	 */
	public synchronized void removeCellListener(CellListener l) {
        cellListener = GWTEventMulticaster.remove(cellListener, l);
	}

	/**
	 * processEvent
	 *
	 * @param e		a CellEvent
	 * @return boolean result
	 */
	protected void processEvent(AWTEvent e) {
		if (e instanceof CellEvent) {
		    processCellEvent((CellEvent)e);
		} else {
		    super.processEvent(e);
		}
	}

	protected void processCellEvent(CellEvent e) {
		if (cellListener != null) {
    		if(e.getID() == CellEvent.CELL_STATE_CHANGED) {
    			cellListener.cellStateChanged(e);
    		}
    	}
	}

	/**
	 * selectionChanged
	 * @param event - TBD
	 * @param selected - TBD
	 */
	public void selectionChanged( AWTEvent event, boolean selected ) {
	    Event evt;
	    if ( event != null ) {
            evt = event.getEvent();
        }
        else {
            evt = new Event( this, -1, null );
        }
        int stateChange;
        if ( selected ) {
            stateChange = CellEvent.SELECTED;
        }
        else {
            stateChange = CellEvent.DESELECTED;
        }
		processEvent( new CellEvent( this, evt,
		                             CellEvent.CELL_STATE_CHANGED,
		                             stateChange ) );
	}

    /**
     * adjustmentValueChanged
     * @param e - TBD
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int hValue = 0;
        if (hScrollbar.isVisible()) {
            hValue = hScrollbar.getValue();
        }
        int vValue = 0;
        if (vScrollbar.isVisible()) {
            vValue = vScrollbar.getValue();
        }
        if ( e.getAdjustable() == getHAdjustable() ) {
            panel.setLocation( -hValue, panel.y );
        }
        else {
            setTopIndex( vValue );
        }
    }

    /**
     * setTopIndex
     * @param topIndex - TBD
     */
    public void setTopIndex(int topIndex) {
        synchronized(getTreeLock()) {
            if (!settingTop) {
                settingTop = true;
                int vValue = 0;
                if (vScrollbar.isVisible()) {
                    vValue = vScrollbar.getValue();
                }
                if ( topIndex != vValue && topIndex <= vScrollbar.getMaximum() ) {
                    vScrollbar.setValue(topIndex);
                }
                if ( topIndex != this.topIndex ) {
                    this.topIndex = topIndex;
                    panel.setTopIndex( topIndex );
                }
                repaint();
                settingTop = false;
            }
        }
    }

    /**
     * addColumn
     * @param name - TBD
     */
    public void addColumn( String name ) {
        int index = 0;
        if ( headers.size() > 0 ) {
            index = headers.size()-1;
        }
        addColumnAt(name,index);
    }

    /**
     * addColumt
     * @param name - TBD
     * @param prefSize - TBD
     */
    public void addColumn( String name, int prefSize  ) {
        addColumn(name);
        setColumnWidth(headers.size()-2, prefSize);
    }

    /**
     * addColumnAt
     * @param name - TBD
     * @param columnIndex - TBD
     */
    public void addColumnAt( String name, int columnIndex ) {
        MCColumnHeader header;
        if ( headers.size() == 0 ) {
            panel.addColumnAt( header = new MCColumnHeader(this,null), 0 );
            headers.insertElementAt( header, 0 );
        }
        header = new MCColumnHeader( this,name );
        panel.addColumnAt( header, columnIndex );
        headers.insertElementAt( header, columnIndex );
/*
        Gadget gadgets[] = column.getGadgets();
        for ( int i = 0; i < gadgets.length; i++ ) {
            RowPanel row = (RowPanel)(((ColumnItem)gadgets[i]).getGadget());
            row.addCellAt( header, columnIndex );
        }
        */
    }

    /**
     * removeAllColumns
     */
    public void removeAllColumns() {
        headers.removeAllElements();
        panel.removeAll();
    }

    /**
     * removeAllRows
     */
    public void removeAllRows() {
        panel.removeAllRows();
    }

    /**
     * getSelectedCount
     * @return int
     */
    public int getSelectedCount() {
        return panel.getSelectedCount();
    }

    /**
     * getSelectedCells
     * @return GadgetCell[]
     */
    public GadgetCell[] getSelectedCells() {
        return panel.getSelectedCells();
    }

    /**
     * getSelectedCell
     * @return GadgetCell
     */
    public GadgetCell getSelectedCell() {
        return panel.getSelectedCell();
    }

    /**
     * sets the width of the column
     * @param columIndex - TBD
     * @param width - TBD
     */
    public void setColumnWidth( int columnIndex, int width ) {
        MCColumnHeader header = (MCColumnHeader)(headers.elementAt(columnIndex));
        header.givenWidth = width;
        header.setSize( width, header.height );
        columnWidthsSet = true;
    }

    /**
     * setColumnChars
     * @param columIndex - TBD
     * @param numChars - TBD
     */
    public void setColumnChars( int columnIndex, int numChars ) {
        FontMetrics metrics = getFontMetrics(getFont());
        int charWidth = metrics.stringWidth("W");
        int twoCharsWidth = metrics.stringWidth("WW");
        charWidth = twoCharsWidth - charWidth;
        setColumnWidth( columnIndex, ( charWidth * numChars ) );
    }

    /**
     * resets the width of the column
     * @param columnIndex - TBD
     */
    public void resetColumnWidth( int columnIndex ) {
        int columnWidth = ((MCColumnHeader)headers.elementAt(columnIndex)).getPreferredSize().width;
        int listWidth = panel.getPrefColumnWidth(columnIndex)+5;
        if ( columnIndex == 0 ) {
            listWidth++; // to make up for focus margin
        }
        setColumnWidth(columnIndex, Math.max(columnWidth,listWidth));
        /*
        int numRows = column.getGadgetCount();
        Gadget gadgets[] = column.getGadgets();
        for ( int i = 0; i < numRows; i++ ) {
            RowPanel row = (RowPanel)(((ColumnItem)gadgets[i]).getGadget());
            LabelGadget cell = ((LabelGadget)row.getGadget(columnIndex));
            width = Math.max(width,cell.getPreferredSize().width);
        }
        setColumnWidth(columnIndex,width+5);
        */
    }

    /**
     * fontChanged
     */
    public void fontChanged() {
        boolean wasValid = isValid();
        super.fontChanged();
        if ( wasValid ) {
            resetColumnWidths();
        }
    }

    /**
     * resetColumWidths
     */
    public void resetColumnWidths() {
		synchronized(getTreeLock()) {
            int count = headers.size()-1;
            for ( int i = 0; i < count; i++ ) {
                resetColumnWidth(i);
            }
        }
    }

    /**
     * checkColumnWidths
     */
    private void checkColumnWidths() {
        int count = headers.size()-1;
        for ( int i = 0; i < count; i++ ) {
            MCColumnHeader header = (MCColumnHeader)headers.elementAt(i);
            if ( header.givenWidth == 0 ) {
                resetColumnWidth(i);
            }
        }
    }

	/**
	 * validate
	 */
	public void validate() {
        if ( ! columnWidthsSet ) {
            resetColumnWidths();
        }
	    super.validate();
	}

	/**
	 * doLayout
	 */
	public void doLayout() {
	    super.doLayout();
	    checkColumnWidths();
        if ( vScrollbar.isVisible() ) {
            int numVisible = panel.getNumVisible();
            int numGadgets = panel.getNumRows();
            int topIndex = panel.getTopIndex();
            if ( numGadgets > numVisible ) {
                vScrollbar.setMaximum( numGadgets - numVisible );
                vScrollbar.setVisibleAmount( numVisible );
                vScrollbar.setBlockIncrement( numVisible - 1 );
                vScrollbar.setUnitIncrement( 1 );
            }
        }
    }

    /**
     * ensureRowCount
     * @param min - TBD
     */
    protected void ensureRowCount( int min ) {
        panel.ensureRowCount(min);
    }

    /**
     * setCellGadget
     * @param rowIndex - TBD
     * @param columIndex - TBD
     * @param gadget - TBD
     */
    public void setCellGadget( int rowIndex, int columnIndex, Gadget gadget ) {
		synchronized(getTreeLock()) {
            ensureRowCount(rowIndex+1);
            panel.replaceCellGadget(rowIndex,columnIndex,gadget);
        }
    }

    /**
     * Gets the item associated with the specified index.
     *
     * @param rowindex - the row position of the item
     * @param columnIndex - the column position of the item
     * @see getItemCount
     * @return Gadget
     */

    public Gadget getCellGadget(int rowIndex, int columnIndex) {
        return panel.getCellGadget(rowIndex,columnIndex);
    }

    /**
     * gets the preferred dimensions
     * @return Dimension
     */
    public Dimension getPreferredSize() {
	    if ( valid && ( pref_width >= 0 ) && ( pref_height >= 0 ) ) {
	        return new Dimension( pref_width, pref_height );
	    }
	    return getPreferredSize(prefRows);
	}

    /**
     * gets the preferred dimensions
     * @param rows - TBD
     * @return Dimension
     */
    public Dimension getPreferredSize(int rows) {
        return panel.getPreferredSize( rows );
    }

    /**
     * selectCell
     * @param rowIndex - TBD
     * @param columnIndex - TBD
     */
    public void selectCell(int rowIndex,int columnIndex) {
        panel.selectCell(rowIndex,columnIndex);
    }

    /**
     * setColumn
     * @param columnIndex - TBD
     */
    public void selectColumn(int columnIndex) {
        panel.selectColumn(columnIndex);
    }

    /**
     * selectRow
     * @param rowIndex - TBD
     */
    public void selectRow(int rowIndex) {
        panel.selectRow(rowIndex);
    }

    /**
     * selectAll
     */
    public void selectAll() {
        panel.selectAll();
    }

    /**
     * deselectAll
     */
    public void deselectAll() {
        panel.deselectAll();
    }

    /**
     * headerClicked
     * @param header - TBD
     */
    public void headerClicked(MCColumnHeader header) {
        int col = headers.indexOf(header);
        deselectAll();
        selectColumn(col);
    }

    /**
     * headerMouseMoved
     * @param header - TBD
     * @param mouse - TBD
     */
    public void headerMouseMoved(MCColumnHeader header, MouseEvent mouse) {
        boolean showCursor = false;
        int mouseX = mouse.getX();
        if ( ( ( mouseX < 5 ) &&
               ( header != headers.firstElement() ) ) ||
             ( ( mouseX > ( header.width - 5 ) ) &&
               ( header != headers.lastElement() ) ) ) {
            showCursor = true;
        }
        if ( showingCursor != showCursor ) {
            if ( showCursor ) {
                setCursor(GadgetCursor.getPredefinedCursor(GadgetCursor.W_RESIZE_CURSOR));
            }
            else {
                setCursor(null);
                resizeHeader = null;
            }
            showingCursor = showCursor;
        }
    }

    /**
     * headerMouseExited
     * @param header - TBD
     * @param mouse - TBD
     */
    public void headerMouseExited(MCColumnHeader header, MouseEvent mouse) {
        if ( showingCursor ) {
            Gadget gadget = header.parent.getGadgetAt( mouse.getX()+header.x,
                                                       mouse.getY()+header.y );
            if ( ! ( gadget instanceof MCColumnHeader ) ||
                 ( gadget == headers.lastElement() ) ) {
                setCursor(null);
                resizeHeader = null;
                showingCursor = false;
            }
        }
    }

    /**
     * headerMousePressed
     * @param header - TBD
     * @param mouse - TBD
     * @return boolean result
     */
     public void headerMousePressed(MCColumnHeader header, MouseEvent mouse) {
        if ( mouse.getClickCount() == 2 ) {
            if ( resizeHeader != null ) {
                resetColumnWidth( headers.indexOf(resizeHeader) );
                resizeHeader = null;
                headerMouseExited(header,mouse);
                mouse.consume();
            }
            return;
        }
        headerMouseMoved(header,mouse);
        if ( showingCursor ) {
            int mouseX = mouse.getX();
            if ( mouseX > ( header.width - mouseX ) ) {
                resizeHeader = header;
            }
            else {
                int pos = headers.indexOf(header);
                if ( pos <= 0 ) {
                    showingCursor = false;
                    return;
                }
                resizeHeader = (MCColumnHeader)(headers.elementAt(pos-1));
            }
            origWidth = resizeHeader.width;
            mouseDownX = mouseX + header.x;
            mouse.consume();
        }
    }

    /**
     * headerMouseDragged
     * @param header - TBD
     * @param mouse - TBD
     * @return boolean result
     */
    public void headerMouseDragged(MCColumnHeader header, MouseEvent mouse) {
        if ( showingCursor &&
             ( resizeHeader != null ) ) {
            int nextX = mouse.getX() + header.x;
            int newWidth = Math.max(10,origWidth + ( nextX - mouseDownX ) );
            if ( newWidth != resizeHeader.width ) {
                resizeHeader.setSize(newWidth,resizeHeader.height);
            }
            mouse.consume();
        }
    }

    /**
     * headerMouseReleased
     * @param header - TBD
     * @param mouse - TBD
     * @return boolean result
     */
    public void headerMouseReleased(MCColumnHeader header, MouseEvent mouse) {
        if ( showingCursor ) {
            headerMouseMoved(header,mouse);
            mouse.consume();
        }
    }
}

/**
 * Column
 * @version 1.1
 * @DTAI, Incorporated
 */
class Column extends ListGridGadget {

    MultiColumnPanel panel;

    /**
     * Column
     * @param panel - TBD
     */
    protected Column( MultiColumnPanel panel ) {
        this.panel = panel;
        setMultipleMode(true);
    }

    /**
     * setTopIndex
     * @param topIndex - TBD
     */
    public void setTopIndex( int topIndex ) {
        if ( topIndex != getTopIndex() ) {
            super.setTopIndex(topIndex);
            panel.setTopIndex(topIndex);
        }
    }
}

/**
 * ColumnPanel
 * @version 1.1
 * @DTAI, Incorporated
 */
class ColumnPanel extends PanelGadget implements Runnable {

    private int dragx;
    private int dragy;

    private GridGadget grid;
    private MultiColumnPanel panel;
    protected GadgetCell focusCell;
    private Thread thread;
    private MouseEvent curDragEvent;
    private int lastRow = -1;
    private int lastCol = -1;

	/**
	 * isFocusTraversable
     * @return boolean
     */
	public boolean isFocusTraversable() {
		return true;
	}

    /**
     * ColumnPanel
     * @param grid - TBD
     * @param panel - TBD
     */
    public ColumnPanel(GridGadget grid,MultiColumnPanel panel) {
        super();
        setLayout(null);
        this.grid = grid;
        this.panel = panel;
        setConsumingTransparentClicks(true);
    }

	/**
     * returns true if children can overlap each other.
     * @return false
	 */
	public boolean childrenCanOverlap() {
	    return false;
	}

    /**
     * run
     */
    public void run() {
        Thread thisThread = Thread.currentThread();

        while ( thread == thisThread ) {
            int hValue = 0;
            int hVis = 0;
            if (grid.hScrollbar.isVisible()) {
                hValue = grid.hScrollbar.getValue();
                hVis = grid.hScrollbar.getVisibleAmount();
            }
            int vValue = 0;
            if (grid.vScrollbar.isVisible()) {
                vValue = grid.vScrollbar.getValue();
            }
            if ( dragy < 0 ) {
                grid.vScrollbar.setValue(vValue-1);
                //moveUp
            }
            else if ( dragy > height ) {
                grid.vScrollbar.setValue(vValue+1);
                //moveDown
            }
            if ( dragx < hValue ) {
                grid.hScrollbar.setValue(hValue-Math.max(1,((hValue-dragx)/2)));
                //moveLeft
            }
            else if ( dragx > hValue+hVis ) {
                grid.hScrollbar.setValue(hValue+Math.max(1,((dragx-(hValue+hVis))/2)));
                //moveRight
            }
            int minx = hValue+1;
            int maxx = hValue+hVis-2;
            int miny = 2;
            int maxy = height-2;
            int tempx = dragx;
            if ( dragx < minx ) {
                tempx = minx;
            }
            else if ( dragx > maxx ) {
                tempx = maxx;
            }
            int tempy = dragy;
            if ( dragy < miny ) {
                tempy = miny;
            }
            else if ( dragy > maxy ) {
                tempy = maxy;
            }
            GadgetCell cell = getCellAt(tempx,tempy);
            if ( cell == null && dragy > height ) {
                Column listgrid = getColumnAt(tempx);
                if ( listgrid != null ) {
                    int row = listgrid.getGadgetCount()-1;
                    cell = new GadgetCell(row,getGadgetIndex(listgrid),listgrid.getItemGadget(row));
                }
            }
            if ( cell != null ) {
                select(cell.getRow(),cell.getColumn(),curDragEvent,true);
            }
            try {
                thread.sleep( 100 );
            }
            catch ( InterruptedException ie ) {
            }
        }
    }

    private synchronized void stopThread() {
        if ( thread != null ) {
            thread = null;
        }
    }

    /**
     * GadgetGraphics
     * @param g - TBD
     */
    public void update(GadgetGraphics g) {
        Color bg = normalBackground;
        if ( bg == null && parent.normalBackground == null &&
             getBackground(g) != Gadget.transparent ) {
            normalBackground = Color.white;
        }
        else {
            normalBackground = null;
        }
        super.update(g);
        normalBackground = bg;
    }

    private void setFocusCell(int row, int col) {
        if ( focusCell != null ) {
            if ( col != focusCell.getColumn() ) {
                getColumn(focusCell.getColumn()).setFocusGadget(null);
            }
        }
        try {
            Column column = getColumn(col);
    	    showColumn(column);
    	    Gadget gadget = getGadget(row,col);
            column.setFocusGadget(gadget);
            focusCell = new GadgetCell(row,col,getItemGadget(row,col));
        }
        catch ( Exception e ) {
            focusCell = null;
            lastRow = -1;
            lastCol = -1;
        }
    }

    private Gadget getGadget(GadgetCell cell) {
        return getGadget(cell.getRow(),cell.getColumn());
    }

    private Gadget getGadget(int row, int col) {
        if ( col >= gadgetCount || col < 0 ) {
            return null;
        }
        Column column = getColumn(col);
        if ( row >= column.gadgetCount || row < 0 ) {
            return null;
        }
        return column.getGadget(row);
    }

    private Gadget getItemGadget(int row, int col) {
        if ( col >= gadgetCount || col < 0 ) {
            return null;
        }
        Column column = getColumn(col);
        if ( row >= column.gadgetCount || row < 0 ) {
            return null;
        }
        return column.getItemGadget(row);
    }

	/**
	 * processFocusEvent
	 * @param e - TBD
	 */
	protected void processFocusEvent(FocusEvent e) {
		synchronized(getTreeLock()) {
		    if ( gadgetCount == 0 ) {
		        repaint();
		    }
		    else {
    		    if (e.getID() == FocusEvent.FOCUS_GAINED) {
            	    if ( focusCell == null ) {
            	        setFocusCell( 0,0 );
            	    }
            	    if ( focusCell != null ) {
                        getGadget(focusCell).repaint();
                    }
                } else {
            	    if ( focusCell != null ) {
                        getGadget(focusCell).repaint();
                    }
                }
            }
    	}
	    super.processFocusEvent( e );
	}

	private void showColumn(Column col) {
        int hValue = 0;
        int hVis = 0;
        if (grid.hScrollbar.isVisible()) {
            hValue = grid.hScrollbar.getValue();
            hVis = grid.hScrollbar.getVisibleAmount();
        }
	    if ( col.x < hValue ) {
	        grid.hScrollbar.setValue(col.x);
	    }
	    else if ( ( ( col.x + col.width ) > hValue+hVis ) &&
	              ( col.parent.width > col.width ) ) {
	        grid.hScrollbar.setValue((col.x+col.width)-hVis);
	    }
	}

    /**
     * getSelectedCount
     * @return int
     */
    public int getSelectedCount() {
        int count = 0;
        for ( int idx = 0; idx < gadgetCount; idx++ ) {
            Column column = (Column)gadgets[idx];
            count += column.getNumSelected();
        }
        return count;
    }

    /**
     * GadgetCell
     * @return GadgetCell[]
     */
    public GadgetCell[] getSelectedCells() {
        synchronized(getTreeLock()) {
            int count = getSelectedCount();
            GadgetCell[] cells = new GadgetCell[count];
            int cur = 0;
            for ( int i = 0; i < gadgetCount; i++ ) {
                Column column = (Column)gadgets[i];
                int rows[] = column.getSelectedIndexes();
                for ( int j = 0; j < rows.length; j++ ) {
                    cells[cur++] = new GadgetCell(rows[j],i,column.getItemGadget(rows[j]));
                }
            }
            return cells;
        }
    }

    /**
     * selectCell
     * @param rowIndex - TBD
     * @param columnIndex - TBD
     */
    public void selectCell(int rowIndex,int columnIndex) {
        select(rowIndex,columnIndex,null);
    }

    /**
     * selectColumn
     * @param columnIndex - TBD
     */
    public void selectColumn(int columnIndex) {
        Column column = getColumn(columnIndex);
        column.selectAll();
        grid.selectionChanged(null,true);
    }

    /**
     * selectRow
     * @param rowIndex - TBD
     */
    public void selectRow(int rowIndex) {
        for ( int idx = 0; idx < gadgetCount; idx++ ) {
            Column column = (Column)gadgets[idx];
            column.select(rowIndex);
        }
        grid.selectionChanged(null,true);
    }

    /**
     * selectAll
     */
    public void selectAll() {
        for ( int idx = 0; idx < gadgetCount; idx++ ) {
            Column column = (Column)gadgets[idx];
            column.selectAll();
        }
        grid.selectionChanged(null,true);
    }

    /**
     * deselectAll
     */
    public void deselectAll() {
        for ( int idx = 0; idx < gadgetCount; idx++ ) {
            Column column = (Column)gadgets[idx];
            column.deselectAll();
        }
        grid.selectionChanged(null,false);
    }

    private void select(int row, int col, InputEvent e ) {
        select(row,col,e,(e != null && e == curDragEvent));
    }

    private void select(int row, int col, InputEvent e, boolean dragging ) {
        if (dragging && row == lastRow && col == lastCol) {
            return;
        }
        if ( ! grid.multipleMode ||
             ( e != null && ! e.isControlDown() ) ) {
            deselectAll();
        }
	    if ( grid.multipleMode && ( dragging || (e != null && e.isShiftDown()) ) &&
	         focusCell != null ) {
	        int startRow;
	        int endRow;
	        int startCol;
	        int endCol;
	        if ( row < focusCell.getRow() ) {
	            startRow = row;
	            endRow = focusCell.getRow();
	        }
	        else {
	            startRow = focusCell.getRow();
	            endRow = row;
	        }
	        if ( col < focusCell.getColumn() ) {
	            startCol = col;
	            endCol = focusCell.getColumn();
	        }
	        else {
	            startCol = focusCell.getColumn();
	            endCol = col;
	        }
    	    if ((e != null && e.isControlDown()) &&
    	        startRow == endRow && startCol == endCol &&
    	        lastRow == -1 && lastCol == -1) {
                Column column = (Column)gadgets[startCol];
    	        if (column.isIndexSelected(startRow)) {
        	        column.deselect(startRow);
        	    } else {
        	        column.select(startRow);
        	    }
    	    } else {
                for ( int i = startCol; i <= endCol; i++ ) {
                    Column column = (Column)gadgets[i];
            	    for ( int j = startRow; j <= endRow; j++ ) {
                	    column.select(j);
                	}
                }
            }
	    }
	    else {
    	    Column column = getColumn(col);
    	    if ((e != null && e.isControlDown()) && column.isIndexSelected(row)) {
    	        column.deselect(row);
        	} else {
        	    column.select(row);
        	}
    	    setFocusCell(row,col);
    	}
        lastRow = row;
        lastCol = col;
    	if ( ! dragging ) {
            grid.selectionChanged(e,true);
        }
	}

	private void moveTo(int row, int col, InputEvent e) {
	    Gadget next = getGadget(row,col);
    	if ( next != null ) {
    	    setFocusCell(row,col);
    	    select(row,col,e);
    	}
	}

	private void moveUp(InputEvent e) {
	    if ( focusCell != null ) {
    	    moveTo(focusCell.getRow()-1,focusCell.getColumn(),e);
    	}
	}

	private void moveDown(InputEvent e) {
	    if ( focusCell != null ) {
    	    moveTo(focusCell.getRow()+1,focusCell.getColumn(),e);
    	}
	}

	private void moveLeft(InputEvent e) {
	    if ( focusCell != null ) {
    	    moveTo(focusCell.getRow(),focusCell.getColumn()-1,e);
    	}
	}

	private void moveRight(InputEvent e) {
	    if ( focusCell != null ) {
    	    moveTo(focusCell.getRow(),focusCell.getColumn()+1,e);
    	}
	}

	private void moveHome(InputEvent e) {
	    if ( focusCell != null ) {
    	    moveTo(0,0,e);
    	}
	}

	private void moveEnd(InputEvent e) {
	    if ( focusCell != null ) {
    	    moveTo(getColumn(focusCell.getColumn()).gadgetCount-1,gadgetCount-1,e);
    	}
	}

	private void moveUpPage(InputEvent e) {
	    int col = 0;
	    if (focusCell != null) {
	        col = focusCell.getColumn();
	    }
	    Column column = getColumn(col);
	    if (column != null) {
	        int numvis = column.getNumPartlyVisible();
    	    int row = Math.max(0,
                column.getTopIndex()-(numvis-1));
    		moveTo(row,col, e );
    	}
	}

	private void moveDownPage(InputEvent e) {
	    int col = 0;
	    if (focusCell != null) {
	        col = focusCell.getColumn();
	    }
	    Column column = getColumn(col);
	    if (column != null) {
	        int numvis = column.getNumVisible();
    	    int row = Math.min(column.getGadgetCount()-1,
                column.getTopIndex()+numvis+(numvis-1));
    		moveTo(row,col, e );
    	}
	}

    /**
     * processKeyEvent
     * @param e - TBD
     */
    protected void processKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
    	    boolean handled = true;
    		if ( ! e.isActionKey() ) {
    			if ( e.isControlDown() ||
    				 e.isMetaDown() ) {

    				switch ( e.getKeyCode() ) {

    					case 3:    // Ctrl-C
    					case 24: { // Ctrl-X
    						//copy();
    						break;
    					}

    					default: {
    					    handled = false;
    					}
    				}
    			}
    			else {
    			    handled = false;
        		}
    		}
    		else {
    			int nextPos = -1;

    			switch( e.getKeyCode() ) {
    				case KeyEvent.UP: {
    					moveUp( e );
    					break;
    				}
    				case KeyEvent.ENTER:
    				case KeyEvent.DOWN: {
    					moveDown( e );
    					break;
    				}
    				case KeyEvent.LEFT: {
    					moveLeft( e );
    					break;
    				}
    				case KeyEvent.RIGHT: {
    					moveRight( e );
    					break;
    				}
    				case KeyEvent.HOME: {
    				    moveHome( e );
    					break;
    				}
    				case KeyEvent.END: {
    				    moveEnd( e );
    					break;
    				}
    				case KeyEvent.PGUP: {
    				    moveUpPage(e);
    					break;
    				}
    				case KeyEvent.PGDN: {
    				    moveDownPage(e);
    					break;
    				}
    				default: {
    					handled = false;
    				}
    			}
    		}
    		if (handled) {
    		    e.consume();
    		}
    	}

        super.processKeyEvent(e);
	}

	private Column getColumnAt(int mousex) {
        Gadget child = getGadgetAt(mousex,0);
        if ( child != null && child instanceof Column ) {
            return (Column)child;
        }
        return null;
    }

	private GadgetCell getCellAt(int mousex,int mousey) {
        Column column = getColumnAt(mousex);
        if ( column != null ) {
            Gadget child = column.getGadgetAt(mousex-column.x,mousey-column.y);
            if ( child != null && child.parent == column ) {
                int row = column.getGadgetIndex(child);
                return new GadgetCell(row,getGadgetIndex(column),column.getItemGadget(row));
            }
        }
        return null;
    }

	/**
	 * processMouseEvent
	 *
	 * @param e	description
	 */
	protected void processMouseEvent(MouseEvent mouse) {
	    switch(mouse.getID()) {
	        case MouseEvent.MOUSE_PRESSED: {
                GadgetCell cell = getCellAt(mouse.getX(),mouse.getY());
                if ( cell != null ) {
                    if ( ! mouse.isShiftDown() ) {
                        setFocusCell(cell.getRow(),cell.getColumn());
                    }
                    lastRow = -1;
                    lastCol = -1;
                    select(cell.getRow(),cell.getColumn(),mouse,true);
                }
        	    mouse.consume();
        	    break;
        	}
	        case MouseEvent.MOUSE_RELEASED: {
                grid.selectionChanged(mouse,true);
        		stopThread();
        	    mouse.consume();
        	    break;
        	}
	    }
	    super.processMouseEvent( mouse );
	}

	/**
	 * processMouseEvent
	 *
	 * @param e - TBD
	 */
	protected void processMouseMotionEvent(MouseEvent mouse) {
	    switch(mouse.getID()) {
	        case MouseEvent.MOUSE_DRAGGED: {
	            dragx = mouse.getX();
                dragy = mouse.getY();
                int tempx = dragx;
                int minx = dragx;
                int maxx = dragx;
                int tempy = dragy;
                int miny = dragy;
                int maxy = dragy;
                if (grid.hScrollbar.isVisible()) {
                    int hValue = 0;
                    int hVis = 0;
                    if (grid.hScrollbar.isVisible()) {
                        hValue = grid.hScrollbar.getValue();
                        hVis = grid.hScrollbar.getVisibleAmount();
                    }
                    minx = hValue+1;
                    maxx = hValue+grid.hScrollbar.getVisibleAmount()-2;
                    tempx = dragx;
                    if ( dragx < minx ) {
                        tempx = minx;
                    }
                    else if ( dragx > maxx ) {
                        tempx = maxx;
                    }
                }
                if (grid.vScrollbar.isVisible()) {
                    miny = 2;
                    maxy = height-2;
                    tempy = dragy;
                    if ( dragy < miny ) {
                        tempy = miny;
                    }
                    else if ( dragy > maxy ) {
                        tempy = maxy;
                    }
                }
                GadgetCell cell = getCellAt(tempx,tempy);
                if ( cell != null ) {
                    select(cell.getRow(),cell.getColumn(),mouse,true);
                }
                if ( dragx < minx ||
                     dragx > maxx ||
                     dragy < miny || dragy > maxy ) {
                    curDragEvent = mouse;
                    thread = new Thread(this, "dtai.gwt.GridGadget");
                    thread.start();
                }
                else if ( thread != null ) {
                    stopThread();
                }
        	    break;
        	}
        }
	    super.processMouseMotionEvent( mouse );
	}

    /**
     * addColumnAt
     * @param index - TBD
     */
    public void addColumnAt(int index) {
        Column column = new Column(panel);
        column.setIgnoreEvents(true);
        if ( grid.showGrid ) {
            column.getItemBorder().setBorderType(BorderGadget.LINE);
            column.getItemBorder().setBorderThickness(1);
        }
        add( column, index );
    }

    /**
     * getColumn
     * @return Column
     */
    public Column getColumn() {
        if ( gadgetCount > 0 ) {
            return getColumn(0);
        }
        else {
            return null;
        }
    }

    /**
     * getColumn
     * @param col - TBD
     * @return Column
     */
    public Column getColumn(int col) {
        if ( gadgetCount > 0 ) {
            return (Column)gadgets[col];
        }
        else {
            return null;
        }
    }

    /**
     * getPrefColumnWidth
     * @param columnIndex - TBD
     * @return int
     */
    public int getPrefColumnWidth(int columnIndex) {
        return ((Column)gadgets[columnIndex]).getPreferredSize().width;
    }

    /**
     * removeAllRows
     */
    public void removeAllRows() {
        synchronized(getTreeLock()) {
            for ( int idx = 0; idx < gadgetCount; idx++ ) {
                Column column = (Column)gadgets[idx];
                column.removeAll();
            }
            lastRow = -1;
            lastCol = -1;
        }
    }

    /**
     * ensureRowCount
     * @param rowCount - TBD
     */
    public void ensureRowCount(int rowCount) {
        synchronized(getTreeLock()) {
            for ( int idx = 0; idx < gadgetCount; idx++ ) {
                Column column = (Column)gadgets[idx];
                while ( column.gadgetCount < rowCount ) {
                    column.add(new LabelGadget("",LabelGadget.HORIZ_ALIGN_LEFT));
                }
            }
        }
    }

    /**
     * replaceCellGadget
     * @param row - TBD
     * @param col - TBD
     * @param g - TBD
     */
    public void replaceCellGadget(int row,int col,Gadget g) {
        Column column = (Column)gadgets[col];
        column.replace(g,row);
    }

    /**
     * getCellGadget
     * @param rowIndex - TBD
     * @param columnIndex - TBD
     * @return Gadget
     */
    public Gadget getCellGadget(int rowIndex,int columnIndex) {
        Column column = (Column)gadgets[columnIndex];
        return column.getItemGadget(rowIndex);
    }

    /**
     * getPrefferedSize
     * @return Dimension
     */
    public Dimension getPreferredSize() {
	    if ( valid && ( pref_width >= 0 ) && ( pref_height >= 0 ) ) {
	        return new Dimension( pref_width, pref_height );
	    }
	    int rows = 0;
	    if ( gadgetCount > 0 ) {
	        rows = getColumn().gadgetCount;
	    }
	    return getPreferredSize(rows);
    }

    /**
     * getPrefferedSize
     * @param rows - TBD
     * @return Dimension
     */
    public Dimension getPreferredSize(int rows) {
        Gadget gadgets[] = getGadgets();
        Dimension pref = new Dimension();
        pref.width++; // make up for focus margin
        Insets insets = getInsets();
        for ( int i = 0; i < gadgetCount; i++ ) {
            Column column = (Column)gadgets[i];
            int listHeight = column.pref_height;
            if ( listHeight < 0 ) {
                listHeight = column.getPreferredSize().height;
            }
            pref.width += column.pref_width;
            pref.height = Math.max( pref.height, listHeight );
        }
        pref.width += insets.left + insets.right;
        pref.height += insets.top + insets.bottom;
        return pref;
    }
}

/**
 * MultiColumnPanel
 * @version 1.1
 * @author DTAI, Incorporated
 */
class MultiColumnPanel extends PanelGadget {

    private int numColumns = 0;
    private ColumnPanel columns;
    private boolean settingTop = false;

    private GridGadget grid;

    /**
     * MultiColumnPanel
     * @param grid - TBD
     */
    public MultiColumnPanel(GridGadget grid) {
        super( null );
        this.grid = grid;
        add(columns = new ColumnPanel(grid,this));
    }

    /**
     * setTopIndex
     * @param idx - TBD
     */
    public void setTopIndex(int idx) {
        synchronized(getTreeLock()) {
            if (!settingTop) {
                settingTop = true;
                for ( int i = 0; i < numColumns; i++ ) {
                    Column column = (Column)columns.gadgets[i];
                    column.setTopIndex( idx );
                }
                grid.setTopIndex(idx);
                settingTop = false;
            }
        }
    }

    /**
     * getNumVisible
     * @return int
     */
    public int getNumVisible() {
        if ( numColumns > 0 ) {
            Column column = (Column)columns.gadgets[0];
            return column.getNumVisible();
        }
        return 0;
    }

    /**
     * getNumPartlyVisible
     * @return int
     */
    public int getNumPartlyVisible() {
        if ( numColumns > 0 ) {
            Column column = (Column)columns.gadgets[0];
            return column.getNumPartlyVisible();
        }
        return 0;
    }

    /**
     * getNumRows
     * @return int
     */
    public int getNumRows() {
        if ( numColumns > 0 ) {
            Column column = (Column)columns.gadgets[0];
            return column.getGadgetCount();
        }
        return 0;
    }

    /**
     * getTopIndex
     * @return int
     */
    public int getTopIndex() {
        if ( numColumns > 0 ) {
            Column column = (Column)columns.gadgets[0];
            return column.getTopIndex();
        }
        return 0;
    }

    /**
     * addColumnAt
     * @param header - TBD
     * @param index - TBD
     */
    public void addColumnAt(MCColumnHeader header, int index) {
        if ( index > numColumns ) {
            throw new IllegalArgumentException("new col index "+index+
                                               " exceeds number of columns "+numColumns);
        }
        add( header, index );
        if ( gadgetCount > 2 ) {
            numColumns++;
            columns.addColumnAt(index);
        }
    }

    /**
     * removeAll
     */
    public void removeAll() {
        super.removeAll();
        columns.removeAll();
        add(columns);
        numColumns = 0;
    }

    /**
     * getColumn
     * @return Column
     */
    public Column getColumn() {
        return columns.getColumn();
    }

    /**
     * getPrefColumnWidth
     * @param columnIndex - TBD
     * @return int
     */
    public int getPrefColumnWidth(int columnIndex) {
        return columns.getPrefColumnWidth(columnIndex);
    }

    /**
     * removeAllRows
     */
    public void removeAllRows() {
        columns.removeAllRows();
    }

    /**
     * ensureRowCount
     * @param rowCount - TBD
     */
    public void ensureRowCount(int rowCount) {
        columns.ensureRowCount(rowCount);
    }

    /**
     * getSelectedCount
     * @return int
     */
    public int getSelectedCount() {
        return columns.getSelectedCount();
    }

    /**
     * getSelectedCells
     * @return GadgetCell[]
     */
    public GadgetCell[] getSelectedCells() {
        return columns.getSelectedCells();
    }

    /**
     * getSelectedCell
     * @return GadgetCell
     */
    public GadgetCell getSelectedCell() {
        return new GadgetCell(columns.focusCell);
    }

    /**
     * replaceCellGadget
     * @param row - TBD
     * @param col - TBD
     * @param g - TBD
     */
    public void replaceCellGadget(int row,int col,Gadget g) {
        columns.replaceCellGadget(row,col,g);
    }

    /**
     * getCellGadget
     * @param rowIndex - TBD
     * @param columnIndex - TBD
     * @return Gadget
     */
    public Gadget getCellGadget(int rowIndex,int columnIndex) {
        return columns.getCellGadget(rowIndex,columnIndex);
    }

    /**
     * selectedCell
     * @param rowIndex - TBD
     * @param columnIndex - TBD
     */
    public void selectCell(int rowIndex,int columnIndex) {
        columns.selectCell(rowIndex,columnIndex);
    }

    /**
     * selectColumn
     * @param columnIndex - TBD
     */
    public void selectColumn(int columnIndex) {
        columns.selectColumn(columnIndex);
    }

    /**
     * selectRow
     * @param rowIndex - TBD
     */
    public void selectRow(int rowIndex) {
        columns.selectRow(rowIndex);
    }

    /**
     * selectAll
     */
    public void selectAll() {
        columns.selectAll();
    }

    /**
     * deselectAll
     */
    public void deselectAll() {
        columns.deselectAll();
    }

    /**
     * getPrefferedSize
     * @param rows - TBD
     * @return Dimension
     */
    public Dimension getPreferredSize(int rows) {
        Gadget gadgets[] = getGadgets();
        Dimension pref = columns.getPreferredSize(rows);
        pref.width = 1; // make up for focus margin
        int maxHeaderHeight = 0;
        for ( int i = 0; i < numColumns; i++ ) {
            MCColumnHeader header = (MCColumnHeader)gadgets[i];
            Column column = (Column)columns.gadgets[i];
            int headerHeight = header.pref_height;
            if ( headerHeight < 0 ) {
                headerHeight = header.getPreferredSize().height;
            }
            if ( header.pref_width < 0 ) {
                header.getPreferredSize();
            }
            pref.width += Math.max(column.width,header.width);
            maxHeaderHeight = Math.max( maxHeaderHeight, headerHeight );
        }
        pref.width = Math.max(pref.width,columns.pref_width);
        pref.height = columns.pref_height + maxHeaderHeight;
        return pref;
    }

    /**
     * getPrefferedSize
     * @return Dimension
     */
    public Dimension getPreferredSize() {
	    if ( valid && ( pref_width >= 0 ) && ( pref_height >= 0 ) ) {
	        return new Dimension( pref_width, pref_height );
	    }
	    if ( gadgetCount < 3 ) {
	        return new Dimension(0,0);
	    }
        return getPreferredSize( grid.prefRows );
    }

    /**
     * requiresVertScrollbar
     * @return boolean
     */
    protected boolean requiresVertScrollbar() {
        if ( numColumns > 0 ) {
            Column column = (Column)columns.gadgets[0];
            return column.requiresVertScrollbar();
        }
        return false;
    }

    /**
     * doLayout
     */
    public void doLayout() {
        Gadget gadgets[] = getGadgets();
        int maxHeaderHeight = 0;
        for ( int i = 0; i < numColumns; i++ ) {
            MCColumnHeader header = (MCColumnHeader)gadgets[i];
            int headerHeight = header.pref_height;
            if ( headerHeight < 0 ) {
                headerHeight = header.getPreferredSize().height;
            }
            maxHeaderHeight = Math.max( maxHeaderHeight, headerHeight );
        }
        int headerX = 0;
        columns.setBounds(0,maxHeaderHeight,width,height-maxHeaderHeight,false);
        Insets insets = columns.getInsets();
        for ( int i = 0; i <= numColumns; i++ ) {
            MCColumnHeader header = (MCColumnHeader)gadgets[i];
            if ( i == numColumns ) {
                header.setBounds(headerX,0,
                     Math.max(header.givenWidth,(width-headerX)),
                     maxHeaderHeight,false);
            }
            else {
                Column column = (Column)columns.gadgets[i];
                header.setBounds(headerX,0,header.width,maxHeaderHeight,false);
                int adjustment = 0;
                if ( i == 0 ) {
                    adjustment = 1;
                }
                column.setBounds(headerX+adjustment,insets.top,
                                   header.width - adjustment,
                                   columns.height-insets.top-insets.bottom,false);
                headerX += header.width;
            }
        }
    }
}

/**
 * MCColumnHeader
 * @version 1.1
 * @author DTAI, Incorporated
 */
class MCColumnHeader extends ButtonGadget {

    private GridGadget grid;
    private Column column;
    protected int givenWidth = 0;

    /**
     * MCColumnHeader
     * @param grid - TBD
     * @param name - TBD
     */
    public MCColumnHeader( GridGadget grid, String name ) {
        super(name);
        if ( name == null ) {
            setEnabled( false );
        }
        this.grid = grid;
        setFocusAllowed( false );
    }

    /**
     * click
     * @param e - TBD
     */
    public void click( AWTEvent e ) {
        grid.headerClicked(this);
    }

    /**
     * processMouseEvent
     * @param mouse - TBD
     */
    public void processMouseEvent(MouseEvent e) {
	    switch(e.getID()) {
	        case MouseEvent.MOUSE_PRESSED: {
                grid.headerMousePressed(this,e);
	            break;
	        }
	        case MouseEvent.MOUSE_RELEASED: {
                grid.headerMouseReleased(this,e);
	            break;
	        }
	        case MouseEvent.MOUSE_EXITED: {
                grid.headerMouseExited(this,e);
	            break;
	        }
	    }
	    if (!e.isConsumed()) {
            super.processMouseEvent(e);
        }
    }

    /**
     * processMouseMotionEvent
     * @param mouse - TBD
     */
    public void processMouseMotionEvent(MouseEvent e) {
	    switch(e.getID()) {
	        case MouseEvent.MOUSE_MOVED: {
                grid.headerMouseMoved(this,e);
                break;
            }
	        case MouseEvent.MOUSE_DRAGGED: {
                grid.headerMouseDragged(this,e);
                break;
            }
        }
	    if (!e.isConsumed()) {
            super.processMouseMotionEvent(e);
        }
    }
}
