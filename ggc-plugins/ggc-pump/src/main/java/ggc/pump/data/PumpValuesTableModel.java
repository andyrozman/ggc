package main.java.ggc.pump.data;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesTableModel;
import main.java.ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     PumpValuesTableModel  
 *  Description:  Model for table of Pump values
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpValuesTableModel extends DeviceValuesTableModel
{

    private static final long serialVersionUID = -3199123443953228082L;


    /**
     * Constructor
     * 
     * @param ddh 

     */
    public PumpValuesTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessPump.getInstance(), ddh, source);

    }


    @Override
    protected void initColumns()
    {
        addColumn(0, "DATETIME", 100, false);
        addColumn(1, "ENTRY_TYPE", 30, false);
        addColumn(2, "BASE_TYPE", 50, false);
        addColumn(3, "SUB_TYPE", 50, false);
        addColumn(4, "VALUE", 105, false);
        addColumn(5, "STATUS", 40, false);
        addColumn(6, "", 25, true);
    }

}
