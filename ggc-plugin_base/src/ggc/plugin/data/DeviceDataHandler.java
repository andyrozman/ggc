package ggc.plugin.data;

import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.gui.DeviceDisplayConfigDialog;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.atech.db.DbDataReaderAbstract;
import com.atech.db.DbDataReadingFinishedInterface;
import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.components.StatusReporterInterface;
import com.atech.utils.file.FileReaderContext;


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
 *  Filename:     DeviceDataHandler
 *  Description:  Device Data Handler abstract class.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


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
    protected DeviceInterface device_interface;
    
    public DeviceDisplayConfigDialog dialog_config = null;
    public DeviceDisplayDataDialog dialog_data = null;
    protected int transfer_type = 0;
    
    public FileReaderContext selected_file_context = null;
    public String selected_file = null;
    
    
    /**
     * Transfer Type: Read Data
     */
    public static final int TRANSFER_READ_DATA = 1;
    
    /**
     * Transfer Type: Read Configuration
     */
    public static final int TRANSFER_READ_CONFIGURATION = 2;
    
    /**
     * Transfer Type: Read File
     */
    public static final int TRANSFER_READ_FILE = 3;
    
    
    /**
     * Constructor
     * 
     * @param da
     */
    public DeviceDataHandler(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }
    
    
    
    /**
     * Set Device PlugIn Server
     * 
     * @param server DevicePlugInServer instance
     */
    public void setDevicePlugInServer(DevicePlugInServer server)
    {
        this.m_server = server;
    }
    
    
    /**
     * Set Configured Device
     * 
     * @param _configured_device DeviceConfigEntry instance
     */
    public void setConfiguredDevice(DeviceConfigEntry _configured_device)
    {
        this.configured_device = _configured_device;
    }
    

    /**
     * Get Configured Device
     * 
     * @return DeviceConfigEntry instance
     */
    public DeviceConfigEntry getConfiguredDevice()
    {
        return this.configured_device; 
    }
    
    
    /**
     * Set Db Data Reader
     * 
     * @param reader DbDataReaderAbstract instance
     */
    public void setDbDataReader(DbDataReaderAbstract reader)
    {
        this.m_reader = reader;
        this.m_reader.setReadingFinishedObject(this);
    }

    /**
     * Get Db Data Reader
     * 
     * @return DbDataReaderAbstract instance
     */
    public DbDataReaderAbstract getDbDataReader()
    {
        return this.m_reader;
    }
    
    
    /**
     * Execute Export
     * 
     * @param exp StatusReporterInterface 
     */
    public void executeExport(StatusReporterInterface exp)
    {
        this.export_dialog = exp;
        
        if (!isOutputWriterSet())
            this.executeExportDb();
        else
            this.executeExportOther();
    }


    /**
     * Is Output Writer Set
     *  
     * @return true if set
     */
    public boolean isOutputWriterSet()
    {
        return (this.m_output_writer!=null);
    }
    
    
    /**
     * Has Old Data for checking if old data exists
     * 
     * @return true if old data is set
     */
    public boolean hasOldData()
    {
        return (this.old_data!=null);
    }
    
    /**
     * Get Old data
     * 
     * @return Hashtable with old data
     */
    public Hashtable<String,?> getOldData()
    {
        return this.old_data;
    }

    
    
    /**
     * Execute export to Database
     */
    public void executeExportDb()
    {
        HibernateDb db = m_da.getHibernateDb();
        
        Hashtable<String, ArrayList<DatabaseObjectHibernate>> ll = this.m_model.getCheckedDOHObjects();
        
        float full_count = 0.0f;
        
        full_count += ll.get("ADD").size();
        full_count += ll.get("EDIT").size();
        
        
        System.out.println("Full count: " + full_count);
        
        int count = 0;
        
        
        for(Enumeration<String> en = ll.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();
            
            boolean add = false;
            
            if (key.equals("ADD"))
                add = true;
            
            ArrayList<DatabaseObjectHibernate> list = ll.get(key);
            
            for(int i=0; i<list.size(); i++)
            {
                if (add)
                    db.add(list.get(i));
                else
                    db.edit(list.get(i));
                
                count++;
                
                float f = (count*(1.0f)) / full_count;
                f *= 100.0f;
                
                int proc = (int)f;
                
                if (proc==100)
                    proc = 99;
                
                System.out.println("Procents: " + proc);
        
                this.setStatus(proc);
                
            }
            
        }

        this.setStatus(100);
        this.export_dialog.setReadingFinished();
        //this.export_dialog.setStatus(100);
    }
    
    
    
    
    
    /**
     * Execute export to Other stuff
     */
    public abstract void executeExportOther();
    

    /**
     * Is Old Data Reading Finished
     * @return
     */
    public boolean isOldDataReadingFinished()
    {
        //return this.m_reader.isFinished();
        return true;
    }
    
    
    /**
     * Set Reading Finished Object
     * 
     * @param ddrf DbDataReadingFinishedInterface instance
     */
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
        //System.out.println("DDH Reading finsihed");
        
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

    
    /**
     * Set Device Data
     * 
     * @param data data as Hashtable<String,?> data
     */
    public abstract void setDeviceData(Hashtable<String,?> data);
    
    
    /**
     * Get Device Values Table Model
     * 
     * @return DeviceValuesTableModel instance (or derivate thereof)
     */
    public DeviceValuesTableModel getDeviceValuesTableModel()
    {
        if (m_model==null)
            createDeviceValuesTableModel();
        
        return m_model;
    }
    
    
    /**
     * Create Device Values Table Model
     */
    public abstract void createDeviceValuesTableModel();


    /**
     * Set Status
     * 
     * @see com.atech.graphics.components.StatusReporterInterface#setStatus(int)
     */
    public void setStatus(int status)
    {
        this.export_dialog.setStatus(status);
    }
    
    
    //public abstract 
    
    public void setDeviceInterface(DeviceInterface di)
    {
        this.device_interface = di;
    }
    
    public DeviceInterface getDeviceInterface()
    {
        return this.device_interface;
    }
    
    
    public void setTransferType(int _type)
    {
        this.transfer_type = _type;
    }
    
    public int getTransferType()
    {
        return this.transfer_type;
    }

    
    
    public boolean isDataTransfer()
    {
        return (this.transfer_type != DeviceDataHandler.TRANSFER_READ_CONFIGURATION);
    }
    
    public boolean isConfigTransfer()
    {
        return (this.transfer_type == DeviceDataHandler.TRANSFER_READ_CONFIGURATION);
    }
    
    
    
    
    
}	
