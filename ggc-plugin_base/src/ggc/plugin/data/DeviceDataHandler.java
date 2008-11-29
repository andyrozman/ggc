package ggc.plugin.data;

import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.Hashtable;

import com.atech.db.DbDataReaderAbstract;
import com.atech.db.DbDataReadingFinishedInterface;
import com.atech.graphics.components.StatusReporterInterface;


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
 *  Filename:     CGMValues.java
 *  Description:  Collection of CGMValuesDay.
 * 
 *  Author: Andy {andy@atech-software.com}
 */



//IMPORTANT NOTICE: 
//This class is not implemented yet.

public abstract class DeviceDataHandler implements DbDataReadingFinishedInterface, StatusReporterInterface
{

    protected DataAccessPlugInBase m_da;
    protected DevicePlugInServer m_server;
    protected OutputWriter m_output_writer;
    protected DeviceConfigEntry configured_device;
    protected DbDataReaderAbstract m_reader;
    protected Hashtable<String,?> old_data;
    StatusReporterInterface export_dialog;
    DbDataReadingFinishedInterface m_reading_inst = null; 
    protected DeviceValuesTableModel m_model;
    
    public DeviceDataHandler(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }
    
    
    
    public void setDevicePlugInServer(DevicePlugInServer server)
    {
        this.m_server = server;
    }
    
    
    public void setConfiguredDevice(DeviceConfigEntry _configured_device)
    {
        this.configured_device = _configured_device;
    }
    

    public DeviceConfigEntry getConfiguredDevice()
    {
        return this.configured_device; 
    }
    
    
    public void setDbDataReader(DbDataReaderAbstract reader)
    {
        this.m_reader = reader;
    }

    public DbDataReaderAbstract getDbDataReader()
    {
        return this.m_reader;
    }
    
    
    public void executeExport(StatusReporterInterface exp)
    {
        this.export_dialog = exp;
        
        if (!isOutputWriterSet())
            this.executeExportDb();
        else
            this.executeExportOther();
    }


    public boolean isOutputWriterSet()
    {
        return (this.m_output_writer!=null);
    }
    
    
    public boolean hasOldData()
    {
        return (this.old_data!=null);
    }
    
    public Hashtable<String,?> getOldData()
    {
        return this.old_data;
    }

    
    
    public abstract void executeExportDb();
    
    
    public abstract void executeExportOther();
    
//    this.server.setReturnData(this.meter_data, this);

    
    public void setReadingFinishedObject(DbDataReadingFinishedInterface ddrf)
    {
        this.m_reading_inst = ddrf;
    }
    
    
    /** 
     * readingFinished
     */
    @SuppressWarnings("unchecked")
    public void readingFinished()
    {
        System.out.println("DDH Reading finsihed");
        
        if (this.m_reading_inst!=null)
            this.m_reading_inst.readingFinished();
        
        
        
        if (this.m_reader==null)
        {
            this.setDeviceData(null);
        }
        else
        {
            this.setDeviceData((Hashtable<String,?>)this.m_reader.getData());
        }
    }

    
    public abstract void setDeviceData(Hashtable<String,?> data);
    
    public DeviceValuesTableModel getDeviceValuesTableModel()
    {
        if (m_model==null)
            createDeviceValuesTableModel();
        
        return m_model;
    }
    
    
    public abstract void createDeviceValuesTableModel();

    //Hashtable<String,ArrayList<?>> ht = this.model.getCheckedEntries();

    public void setStatus(int status)
    {
        this.export_dialog.setStatus(status);
    }
    
    
}	
