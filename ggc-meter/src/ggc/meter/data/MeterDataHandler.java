package ggc.meter.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.meter.device.MeterInterface;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.Hashtable;


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
 *  Filename:     MeterDataHandler
 *  Description:  Data Handler for Meter Tool
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterDataHandler extends DeviceDataHandler
{

    MeterValuesExtTableModel m_model2;
    
    /**
     * Constructor
     * 
     * @param da
     */
    public MeterDataHandler(DataAccessPlugInBase da)
    {
        super(da);
    }
    
    
    
    
    /**
     * Execute export Db
     * 
     * @see ggc.plugin.data.DeviceDataHandler#executeExportDb()
     */
    @SuppressWarnings("deprecation")
    public void executeExportDb()
    {
        
        // TODO Fix
        //System.out.println("Checked entries: " + this.getDeviceValuesTableModel().getCheckedEntries());
        this.m_server.setReturnData(this.getDeviceValuesTableModel().getCheckedEntries(), this);
        
        
        
        
    }
    
    
    /**
     * Execute Export Other (not supported for now)
     * 
     * @see ggc.plugin.data.DeviceDataHandler#executeExportOther()
     */
    public void executeExportOther()
    {
    }
    
    
    /**
     * Create Device Values Table Model
     */
    public void createDeviceValuesTableModel()
    {
        this.m_model = new MeterValuesTableModel(this, m_da.getSourceDevice());
        this.m_model2 = new MeterValuesExtTableModel(this, m_da.getSourceDevice());
    }


    
    /**
     * Get Device Values Table Model (for this tool we override main method)
     * 
     * @return DeviceValuesTableModel instance (or derivate thereof)
     */
    public DeviceValuesTableModel getDeviceValuesTableModel()
    {
        if (m_model==null)
            createDeviceValuesTableModel();
        
        MeterInterface mi = (MeterInterface)m_da.getSelectedDeviceInstance();
        
        System.out.println("getDeviceValuesTableModel(): " + mi.getInterfaceTypeForMeter() );
        
        if (mi.getInterfaceTypeForMeter()==MeterInterface.METER_INTERFACE_SIMPLE)
            return m_model;
        else
            return m_model2;
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
        if ((data==null) || (data.size()==0))
        {
            //System.out.println("NO Old data: " + old_data);
            old_data = new Hashtable<String,DayValueH>();
        }
        else
        {
            old_data = (Hashtable<String,DayValueH>)data;
            //System.out.println("Old data: " + old_data);
        }
    }




    /** 
     * Set Reading Finished
     */
    public void setReadingFinished()
    {
        // TODO Auto-generated method stub
        
    }

    
    
    
    
}	