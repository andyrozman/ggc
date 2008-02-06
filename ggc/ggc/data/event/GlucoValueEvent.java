


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure Java application to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MonthlyValues
 *  Purpose:  This is data class for storing monthly values. 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */



/*
 * Created on 15.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.event;


import java.util.EventObject;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class GlucoValueEvent extends EventObject
{

    public static final int INSERT = 0;

    public static final int DELETE = 2;

    public static final int UPDATE = 3;

    private int type = -1;

    private int firstRow = -1;

    private int lastRow = -1;

    private int column = -1;


    /**
     * Constructor for GlucoValueEvent.
     * @param source
     */
    public GlucoValueEvent(Object source, int firstRow, int lastRow, int column, int type)
    {
        super(source);

        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.column = column;
        this.type = type;
    }

    /**
     * Returns the column.
     * @return int
     */
    public int getColumn()
    {
        return column;
    }

    /**
     * Returns the firstRow.
     * @return int
     */
    public int getFirstRow()
    {
        return firstRow;
    }

    /**
     * Returns the lastRow.
     * @return int
     */
    public int getLastRow()
    {
        return lastRow;
    }

    /**
     * Returns the type.
     * @return int
     */
    public int getType()
    {
        return type;
    }

}
