/****************************************************************
 **
 **  $Id: TableGadget.java,v 1.40 1998/02/13 19:46:38 ccp Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/TableGadget.java,v $
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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Rectangle;
import java.util.Vector;
import java11.awt.Adjustable;
import java11.awt.event.AdjustmentEvent;
import java11.awt.event.AWTEvent;
import java11.awt.event.MouseEvent;

/**
 * TableGadget
 * @version 1.1
 * @author DTAI, Incorporated
 */

public class TableGadget extends ScrollListGadget {

    private TablePanel panel;

    private Vector headers = new Vector(); // of ButtonGadget's for column headers
    private boolean showingCursor = false;
    private int mouseDownX;
    private ColumnHeader resizeHeader;
    private int origWidth;
    private ColumnHeader lastHeaderClicked;
    private boolean needLastColumn = true;
    private boolean columnWidthsSet = false;
    private boolean cellMode = false;

    /**
     * Creates a new scrolling list initialized with no visible Lines or multiple selections.
     */

    public TableGadget() {
        this( 0, false );
    }

    /**
     * Creates a new table initialized with the specified number of visible lines and a boolean stating whether multiple selections are allowed or not.
     *
     * @param rows - the number of items to show.
     */

    public TableGadget(int rows) {
        this( rows, false );
    }

    /**
     * Creates a new table initialized with the specified number of visible lines and a boolean stating whether multiple selections are allowed or not.
     *
     * @param rows - the number of items to show.
     * @param multipleMode - if true then multiple selections are allowed.
     */

    public TableGadget(int rows, boolean multipleMode) {
        super(rows, multipleMode);
        viewport.setBackground(null);
        add( panel = new TablePanel( listgrid ) );
        panel.add( listgrid );
    }

    /**
     * sets the background for the row area
     * @param color - the background color
     */
    public void setRowBackground(Color color) {
        listgrid.setBackground(color);
    }

    /**
     * update
     * @param g - TBD
     */
    public void update(GadgetGraphics g) {
        Color lgb = listgrid.normalBackground;
        if ( lgb == null && normalBackground == null) {
            if (getBackground(g) != Gadget.transparent ) {
                listgrid.normalBackground = Color.white;
            }
            else {
                listgrid.normalBackground = null;
            }
        }
        super.update(g);
        listgrid.normalBackground = lgb;
    }

    /**
     * addColumn
     * @param name - TBD
     */
    public void addColumn( String name ) {
        addColumnAt(name,headers.size()-1);
    }

    /**
     * addColumn
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
        if ( needLastColumn ) {
            needLastColumn = false;
            addColumnAt(null,0);
            columnIndex++;
        }
        ColumnHeader header = new ColumnHeader( this,name );
        headers.insertElementAt( header, columnIndex );
        panel.add( header, columnIndex );
        Gadget gadgets[] = listgrid.getGadgets();
        for ( int i = 0; i < gadgets.length; i++ ) {
            RowPanel row = (RowPanel)(((ListGridItem)gadgets[i]).getGadget());
            row.addCellAt( header, columnIndex );
        }
    }

    /**
     * removeAllColumns
     */
    public void removeAllColumns() {
        listgrid.removeAll();
      	headers.removeAllElements();
        needLastColumn = true;
        panel.removeAll();
        panel.add( listgrid );
    }

    /**
     * removeAllRows
     */
    public void removeAllRows() {
        listgrid.removeAll();
    }

    /**
     * Sets the column width
     * @param columnIndex - TBD
     * @param width - TBD
     */
    public void setColumnWidth( int columnIndex, int width ) {
        ColumnHeader header = (ColumnHeader)(headers.elementAt(columnIndex));
        header.givenWidth = width;
        header.setSize( width, header.height );
        columnWidthsSet = true;
    }

    /**
     * setColumnChars
     * @param columnIndex - TBD
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
     * Resets the column width.
     * @param columnIndex - TBD
     */
    public void resetColumnWidth( int columnIndex ) {
        int width = ((ColumnHeader)headers.elementAt(columnIndex)).getPreferredSize().width;
        int numRows = listgrid.getGadgetCount();
        Gadget gadgets[] = listgrid.getGadgets();
        for ( int i = 0; i < numRows; i++ ) {
            RowPanel row = (RowPanel)(((ListGridItem)gadgets[i]).getGadget());
            LabelGadget cell = ((LabelGadget)row.getGadget(columnIndex));
            width = Math.max(width,cell.getPreferredSize().width);
        }
        setColumnWidth(columnIndex,width+5);
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
     * resetColumnWidths
     */
    public void resetColumnWidths() {
		synchronized(getTreeLock()) {
            int count = headers.size();
            for ( int i = 0; i < count; i++ ) {
                resetColumnWidth(i);
            }
        }
    }

    /**
     * checkColumnWidths
     */
    private void checkColumnWidths() {
        int count = headers.size();
        for ( int i = 0; i < count; i++ ) {
            ColumnHeader header = (ColumnHeader)headers.elementAt(i);
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
    }

    /**
     * setCell
     * @param rowIndex - TBD
     * @param columnIndex - TBD
     * @param value - TBD
     */
    public void setCell( int rowIndex, int columnIndex, String value ) {
        setCell(rowIndex,columnIndex,value,value.toUpperCase());
    }

    private void ensureRowCount( int min ) {
        int count = getItemCount();
        while ( count < min ) {
            RowPanel row = new RowPanel( headers );
            listgrid.add( row );
            count++;
        }
    }

    /**
     * setCell
     * @param rowIndex - TBD
     * @param columnIndex - TBD
     * @param value - TBD
     * @param sortValue - TBD
     */
    public void setCell( int rowIndex, int columnIndex, String value, Object sortValue ) {
		synchronized(getTreeLock()) {
            ensureRowCount(rowIndex+1);
            Gadget gadgets[] = listgrid.getGadgets();
            RowPanel row = (RowPanel)(((ListGridItem)gadgets[rowIndex]).getGadget());
            LabelGadget label = (LabelGadget)row.getGadget(columnIndex);
            label.setLabel( value );
            label.setArg( sortValue );
        }
    }

    /**
     * setRowArg
     * @param rowIndex - TBD
     * @param arg - TBD
     */
    public void setRowArg( int rowIndex, Object arg ) {
        ensureRowCount(rowIndex+1);
        Gadget gadgets[] = listgrid.getGadgets();
        RowPanel row = (RowPanel)(((ListGridItem)gadgets[rowIndex]).getGadget());
        row.setArg( arg );
    }

    /**
     * setRowArg
     * @param rowIndex - TBD
     * @param arg - TBD
     */
    public void setRowColor(int rowIndex, Color color) {
        ensureRowCount(rowIndex+1);
        Gadget gadgets[] = listgrid.getGadgets();
        RowPanel row = (RowPanel)(((ListGridItem)gadgets[rowIndex]).getGadget());
        row.setForeground(color);
        row.setSelectedBackground(color);
    }

    /**
     * getRow
     * @param rowIndex - TBD
     * @return Gadget
     */
    public Gadget getRow( int rowIndex ) {
        ensureRowCount(rowIndex+1);
        Gadget gadgets[] = listgrid.getGadgets();
        return ((ListGridItem)gadgets[rowIndex]).getGadget();
    }

    /**
     * getRowArg
     * @param rowIndex - TBD
     * @return Object
     */
    public Object getRowArg( int rowIndex ) {
        return getRow(rowIndex).getArg();
    }

    /**
     * getRowIndexFromArg
     * @param arg - TBD
     * @return int
     */
    public int getRowIndexFromArg( Object arg ) {
		synchronized(getTreeLock()) {
            int count = getItemCount();
    	    for ( int i = 0; i < count; i++ ) {
    	        Object rowarg = getRow(i).getArg();
    	        if ( rowarg == arg ) {
    	            return i;
    	        }
            }
            return -1;
        }
    }

    /**
     * Gets the cell associated with the specified index.
     *
     * @param columnIndex - the position of the item
     * @see getItemCount
     * @return String
     */
    public String getCell(int rowIndex, int columnIndex) {
        RowPanel row = (RowPanel)(listgrid.getItemGadget(rowIndex));
        LabelGadget cell = ((LabelGadget)row.getGadget(columnIndex));
        return cell.getLabel();
    }

    /**
     * getItem
     * @param rowIndex - TBD
     * @return String
     */
    public String getItem(int rowIndex) {
        StringBuffer buf = new StringBuffer();
        RowPanel row = (RowPanel)(listgrid.getItemGadget(rowIndex));
        int numColumns = headers.size();
        for ( int i = 0; i < numColumns; i++ ) {
            if ( i > 0 ) {
                buf.append( '\t' );
            }
            LabelGadget cell = ((LabelGadget)row.getGadget(i));
            buf.append(cell.getLabel());
        }
        return buf.toString();
    }

    /**
     * Returns the preferred dimensions needed for the list with the specified amount of rows.
     *
     * @return Dimension - the preferred dimensions.
     */
    public Dimension getPreferredSize() {
	    if ( valid && ( pref_width >= 0 ) && ( pref_height >= 0 ) ) {
	        return new Dimension( pref_width, pref_height );
	    }
	    Dimension pref = getPreferredSize(listgrid.getPrefRows());
	    pref_width = pref.width;
	    pref_height = pref.height;
	    return pref;
    }

    /**
     * gets the preferred size
     * @param rows - TBD
     * @return Dimension
     */
    public Dimension getPreferredSize(int rows) {
        Dimension pref = panel.getPreferredSize( listgrid.getPrefRows() );
        Dimension outerpref = getOuterPreferredSize();
        pref.width += outerpref.width;
        pref.height += outerpref.height;
        return pref;
    }

    /**
     * adjustmentValueChanged
     * @param e - TBD
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if ( e.getAdjustable() == getHAdjustable() ) {
            panel.setLocation( -hScrollbar.getValue(), panel.y );
        }
        else {
            listgrid.setTopIndex( vScrollbar.getValue() );
        }
    }

    /**
     * headerChanged
     * @param header - TBD
     */
    public void headerChanged(ColumnHeader header) {
        int columnIndex = headers.indexOf(header);
        int numRows = listgrid.getGadgetCount();
        for ( int i = 0; i < numRows; i++ ) {
            RowPanel row = (RowPanel)(listgrid.getItemGadget(i));
            LabelGadget cell = ((LabelGadget)row.getGadget(columnIndex));
            cell.setSize( header.width, cell.height );
        }
        if ( headers.size() > 0 ) {
            ColumnHeader lastHeader = (ColumnHeader)headers.lastElement();
            if ( lastHeader != header ) {
                int change = header.width - header.lastWidth;
                lastHeader.setSize( Math.max(2,lastHeader.width - change),
                                    lastHeader.height );
            }
        }
    }

    /**
     * compare
     * @param left - TBD
     * @param right - TBD
     * @return int
     */
    public int compare( Object left, Object right ) {
        if ( left instanceof String ) {
            if ( right instanceof String ) {
                return ((String)left).compareTo((String)right);
            }
            else {
                return 1;
            }
        }
        else if ( left instanceof Number ) {
            if ( right instanceof Number ) {
                double leftval = ((Number)left).doubleValue();
                double rightval = ((Number)right).doubleValue();
                if ( leftval == rightval ) {
                    return 0;
                }
                else if ( leftval < rightval ) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            else if ( right instanceof String ) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else {
            if ( ( right instanceof String ) ||
                 ( right instanceof Number ) ) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    /**
     * headerClicked
     * @param header - TBD
     */
    public void headerClicked(ColumnHeader header) {
        int order = 1;
        if ( lastHeaderClicked == header ) {
            order = -1;
            lastHeaderClicked = null;
        }
        else {
            lastHeaderClicked = header;
        }
        Gadget rows[] = listgrid.getGadgets();
        int columnIdx = headers.indexOf(header);
        for ( int j = 1; j < rows.length; j++ ) {
            Gadget temp = rows[j];
            int i = j-1;
            while ( ( i >= 0 ) &&
                    ( ( compare( ((RowPanel)((ListGridItem)rows[i]).getGadget()).
                                    getGadget(columnIdx).getArg(),
                                 ((RowPanel)((ListGridItem)temp).getGadget()).
                                    getGadget(columnIdx).getArg() )
                        * order ) > 0 ) ) {
                rows[i+1] = rows[i];
                i--;
            }
            rows[i+1] = temp;
        }
        listgrid.removeAll();
        for ( int i = 0; i < rows.length; i++ ) {
            listgrid.add( rows[i] );
        }
    }

    /**
     * headerMouseMoved
     * @param header - TBD
     * @param mouse - TBD
     */
    public void headerMouseMoved(ColumnHeader header, MouseEvent mouse) {
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
     * @param mouse	- TBD
     */
    public void headerMouseExited(ColumnHeader header, MouseEvent mouse) {
        if ( showingCursor ) {
            Gadget gadget = header.parent.getGadgetAt( mouse.getX()+header.x,
                                                       mouse.getY()+header.y );
            if ( ! ( gadget instanceof ColumnHeader ) ||
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
     */
    public void headerMousePressed(ColumnHeader header, MouseEvent mouse) {
        if ( mouse.getClickCount() == 2 ) {
            if ( resizeHeader != null ) {
                resetColumnWidth( headers.indexOf(resizeHeader) );
                resizeHeader = null;
                headerMouseExited(header,mouse);
                mouse.consume();
            }
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
                resizeHeader = (ColumnHeader)(headers.elementAt(pos-1));
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
    public void headerMouseDragged(ColumnHeader header, MouseEvent mouse) {
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
    public void headerMouseReleased(ColumnHeader header, MouseEvent mouse) {
        if ( showingCursor ) {
            headerMouseMoved(header,mouse);
            mouse.consume();
        }
    }
}

/**
 * TablePanel
 * @version 1.1
 * @author DTAI, Incorporated
 */
class TablePanel extends PanelGadget {

    private ListGridGadget listgrid;

    /**
     * TablePanel
     * @param listgrid - TBD
     */
    public TablePanel( ListGridGadget listgrid ) {
        super( null );
        this.listgrid = listgrid;
    }

    /**
     * getPreferredSize
     * @return Dimension
     */
    public Dimension getPreferredSize() {
	    if ( valid && ( pref_width >= 0 ) && ( pref_height >= 0 ) ) {
	        return new Dimension( pref_width, pref_height );
	    }
	    return getPreferredSize(listgrid.getPrefRows());
    }

    /**
     * getPReferredSize
     * @param rows - TBD
     * @return Dimension
     */
    public Dimension getPreferredSize(int rows) {
        Gadget gadgets[] = getGadgets();
        int maxHeight = 0;
        int prefWidth = 0;
        for ( int i = 0; i < gadgets.length; i++ ) {
            Gadget gadget = gadgets[i];
            if ( gadget != listgrid ) {
                int headerHeight = gadget.pref_height;
                int headerWidth = gadget.pref_width;
                if ( headerHeight < 0 || headerWidth < 0) {
                    Dimension headerSize = gadget.getPreferredSize();
                    headerHeight = headerSize.height;
                    headerWidth = headerSize.width;
                }
                maxHeight = Math.max( maxHeight, headerHeight );
                int defaultColWidth = gadget.getSize().width;
                if (defaultColWidth == 0) {
                    defaultColWidth = headerWidth;
                }
                prefWidth += defaultColWidth;
            }
        }
        Dimension pref = listgrid.getPreferredSize( rows );
        pref.height += maxHeight;
        pref.width = Math.max(pref.width,prefWidth);
        return pref;
    }

    /**
     * requiresVertScrollbar
     * @return boolean
     */
    protected boolean requiresVertScrollbar() {
        return listgrid.requiresVertScrollbar();
    }

    /**
     * doLayout
     */
    public void doLayout() {
        Gadget gadgets[] = getGadgets();
        int maxHeight = 0;
        for ( int i = 0; i < gadgets.length; i++ ) {
            Gadget gadget = gadgets[i];
            if ( gadget != listgrid ) {
                int headerHeight = gadget.pref_height;
                if ( headerHeight < 0 ) {
                    headerHeight = gadget.getPreferredSize().height;
                }
                maxHeight = Math.max( maxHeight, headerHeight );
            }
        }
        int headerX = 0;
        for ( int i = 0; i < gadgets.length; i++ ) {
            Gadget gadget = gadgets[i];
            if ( gadget != listgrid ) {
                ColumnHeader header = (ColumnHeader)gadget;
                if ( i < ( gadgets.length - 2 ) ) {
                    header.setBounds(headerX,0,header.width,maxHeight,false);
                    headerX += header.width;
                }
                else {
                    header.setBounds(headerX,0,
                         Math.max(header.givenWidth,(width-headerX)),
                         maxHeight,false);
                }
            }
        }
        if ( height > 0 ) {
            listgrid.setBounds(0, maxHeight, width, height-maxHeight,false);
        }
    }
}

/**
 * RowPanel
 * @version 1.1
 * @author DTAI, Incorporated
 */
class RowPanel extends PanelGadget {

    private Vector headers;

    public RowPanel( Vector headers ) {
        super( null );
		setSaveGraphicsForChildren(true);
		setPaintAnyOrder(true);
		setClippingRequired(false);
        this.headers = headers;
        for ( int i = 0; i < headers.size(); i++ ) {
            ColumnHeader header = (ColumnHeader)headers.elementAt(i);
            addCell( header );
        }
    }

    /**
     * addCell
     * @param header - TBD
     */
    public final void addCell( ColumnHeader header ) {
        addCellAt(header,gadgetCount);
    }

    /**
    * addCellAt
    * @param header - TBD
    * @param pos - TBD
    */
    public void addCellAt( ColumnHeader header, int pos ) {
		synchronized(getTreeLock()) {
            LabelGadget label = new LabelGadget();
            label.setBackground(null);
            label.setHorizAlign( LabelGadget.HORIZ_ALIGN_LEFT );
            label.setVertAlign( LabelGadget.VERT_ALIGN_TOP );
            label.setSize( header.width, 0 );
            add( label, pos );
        }
    }

    /**
     * getPreferredSize
     * @return Dimension
     */
    public Dimension getPreferredSize() {
	    if ( valid && ( pref_width >= 0 ) && ( pref_height >= 0 ) ) {
	        return new Dimension( pref_width, pref_height );
	    }
        Gadget gadgets[] = getGadgets();
        Dimension pref = new Dimension();
        for ( int i = 0; i < gadgets.length; i++ ) {
            Gadget gadget = gadgets[i];
            int cellHeight = gadget.pref_height;
            if ( cellHeight < 0 ) {
                cellHeight = gadget.getPreferredSize().height;
            }
            pref.height = Math.max( pref.height, cellHeight );
            ColumnHeader header = (ColumnHeader)headers.elementAt(i);
            if ( i < ( gadgets.length - 1 ) ) {
                pref.width += header.width;
            }
            else {
                pref.width += Math.min(header.givenWidth,header.width);
            }
        }
        return pref;
    }

    /**
     * doLoyout
     */
    public void doLayout() {
		synchronized(getTreeLock()) {
            Gadget gadgets[] = getGadgets();
            int maxHeight = 0;
            for ( int i = 0; i < gadgets.length; i++ ) {
                Gadget gadget = gadgets[i];
                int cellHeight = gadget.pref_height;
                if ( cellHeight < 0 ) {
                    cellHeight = gadget.getPreferredSize().height;
                }
                maxHeight = Math.max( maxHeight, cellHeight );
            }
            int cellX = 0;
            for ( int i = 0; i < gadgets.length; i++ ) {
                Gadget gadget = gadgets[i];
                int cellWidth = ((ColumnHeader)headers.elementAt(i)).width;
                gadget.setBounds(cellX,0,cellWidth,maxHeight,false);
                cellX += cellWidth;
            }
        }
    }

    /**
     * toString
     * @return String
     */
    public String toString() {
        return ((LabelGadget)gadgets[0]).getLabel();
    }
}

/**
 * Columnheader
 * @version 1.1
 * @author DTAI, Incorporated
 */
class ColumnHeader extends ButtonGadget {

    private TableGadget table;
    int givenWidth = 0;
    int lastWidth = 0;

    /**
     * ColumnHeader
     * @param table - TBD
     * @param name - TBD
     */
    public ColumnHeader( TableGadget table, String name ) {
        super(name);
        if ( name == null ) {
            setEnabled( false );
        }
        this.table = table;
        setFocusAllowed( false );
    }

    /**
     * invalidate
     * @param invalidateParent - TBD
     */
    public void invalidate( boolean invalidateParent ) {
        super.invalidate( invalidateParent );
        if ( width != lastWidth ) {
            table.headerChanged( this );
            lastWidth = width;
        }
    }

    /**
     * click
     * @param e - TBD
     */
    public void click( AWTEvent e ) {
        table.headerClicked(this);
    }

    /**
     * processMouseEvent
     * @param mouse - TBD
     */
    public void processMouseEvent(MouseEvent e) {
	    switch(e.getID()) {
	        case MouseEvent.MOUSE_PRESSED: {
                table.headerMousePressed(this,e);
	            break;
	        }
	        case MouseEvent.MOUSE_RELEASED: {
                table.headerMouseReleased(this,e);
	            break;
	        }
	        case MouseEvent.MOUSE_EXITED: {
                table.headerMouseExited(this,e);
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
                table.headerMouseMoved(this,e);
                break;
            }
	        case MouseEvent.MOUSE_DRAGGED: {
                table.headerMouseDragged(this,e);
                break;
            }
        }
	    if (!e.isConsumed()) {
            super.processMouseMotionEvent(e);
        }
    }
}
