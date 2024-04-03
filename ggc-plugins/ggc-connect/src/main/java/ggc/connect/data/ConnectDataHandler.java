package ggc.connect.data;

import ggc.connect.util.DataAccessConnect;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     PumpDataHandler
 *  Description:  Data Handler for Pump Tool
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class ConnectDataHandler extends DeviceDataHandler
{

    /**
     * Constructor
     *
     * @param da
     */
    public ConnectDataHandler(DataAccessPlugInBase da)
    {
        super(da);
    }


    /**
     * Create Device Values Table Model
     */
    @Override
    public void createDeviceValuesTableModel()
    {
        this.m_model = new ConnectValuesTableModel(this, DataAccessConnect.getInstance().getSourceDevice());
    }


    /** 
     * Set Reading Finished
     */
    public void setReadingFinished()
    {
    }

}
