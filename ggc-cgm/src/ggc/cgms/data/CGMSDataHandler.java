package ggc.cgms.data;

import java.util.Hashtable;

import ggc.core.db.hibernate.DayValueH;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMSDataHandler
 *  Description:  Data Handler for CGMSs Tool
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDataHandler extends DeviceDataHandler
{

    /**
     * Constructor
     * 
     * @param da
     */
    public CGMSDataHandler(DataAccessPlugInBase da)
    {
        super(da);
    }


    /**
     * Create Device Values Table Model
     */
    @Override
    public void createDeviceValuesTableModel()
    {
        this.m_model = new CGMSValuesTableModel(this, m_da.getSourceDevice()); // dataAccess.getSourceDevice());

    }


    /**
     * Set Device Data
     * 
     * @param data data as Hashtable<String,?> data
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setDeviceData(Hashtable<String, ?> data)
    {
        if (data == null || data.size() == 0)
        {
            // System.out.println("NO Old data: " + old_data);
            old_data = new Hashtable<String, DayValueH>();
        }
        else
        {
            old_data = data;
            // System.out.println("Old data: " + old_data);
        }
    }


    /** 
     * Set Reading Finished
     */
    public void setReadingFinished()
    {
    }

}
