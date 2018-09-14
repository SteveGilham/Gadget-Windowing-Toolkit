/****************************************************************
 **
 **  $Id: MultiColumnListGadget.java,v 1.7 1997/08/06 23:27:07 cvs Exp $
 **
 **  $Source: /cvs/classes/dtai/gwt/MultiColumnListGadget.java,v $
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


/**
 * A scrolling list of text items.
 * @version 1.1
 * @author DTAI, Incorporated
 */
public class MultiColumnListGadget extends GridGadget {

    /**
     * Creates a new scrolling list initialized with no visible Lines or multiple selections.
     */
    public MultiColumnListGadget() {
        this( 0, false );
    }

    /**
     * Creates a new scrolling list initialized with the specified number of 
	 * visible lines.  Multiple selections are not allowed.
     *
     * @param rows - the number of items to show.
     */
    public MultiColumnListGadget(int rows) {
        this( rows, false );
    }

    /**
     * Creates a new scrolling list initialized with the specified number of 
	 * visible lines and a boolean stating whether multiple selections are allowed or not.
     *
     * @param rows - the number of items to show.
     * @param multipleMode - if true then multiple selections are allowed.
     */
    public MultiColumnListGadget(int rows, boolean multipleMode) {
        super(rows,multipleMode);
    }

    /**
     * setCell
     * @param rowindex - the position of the row
     * @param columnindex - the position of the column
     * @param value	- the label for the CellGadget at the specified index
     */
    public void setCell( int rowIndex, int columnIndex, String value ) {
        ensureRowCount(rowIndex+1);
        ((LabelGadget)getCellGadget(rowIndex,columnIndex)).setLabel( value );
    }

    /**
     * Gets the label with the specified index.
     *
     * @param rowindex - the position of the row
     * @param columnindex - the position of the column
     * @return label of the cellGadget at the specified index
     * @see getItemCount
     */
    public String getCell(int rowIndex, int columnIndex) {
        return ((LabelGadget)getCellGadget(rowIndex,columnIndex)).getLabel();
    }
}
