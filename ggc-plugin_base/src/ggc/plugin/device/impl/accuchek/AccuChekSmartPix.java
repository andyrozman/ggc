
package ggc.plugin.device.impl.accuchek;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.plugin.protocol.XmlProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.File;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     ----  
 *  Description:  ----
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AccuChekSmartPix  extends XmlProtocol 
{
    
    //protected OutputWriter output_writer = null;
    protected String port_param;
    //protected DataAccessPlugInBase m_da;
    protected int max_records = 0;
    //protected I18nControlAbstract m_ic = null;
    protected AccuChekSmartPixReaderAbstract reader = null;
    
    protected AbstractDeviceCompany device_company = null;
    protected boolean communication_established = false;
    protected String device_source_name;
    

    /**
     * Constructor
     * 
     * @param cmp 
     * @param da 
     */
    public AccuChekSmartPix(AbstractDeviceCompany cmp, DataAccessPlugInBase da)
    {
        super(da);
        this.setDeviceCompany(cmp);
    }
    
    
    
    /**
     * Constructor
     * 
     * @param params 
     * 
     * @param drive_letter
     * @param writer
     * @param da 
     * @param max_records 
     */
    public AccuChekSmartPix(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(da, writer);
        //super();
        //this.setConnectionPort(drive_letter);
        this.output_writer = writer;
        this.port_param = params;
        this.m_da = da;
        //this.max_records = max_records;
        this.ic = m_da.getI18nControlInstance();
        
        this.reader = new AccuChekSmartPixReaderV2(m_da, writer, this);
        
        
//XV        this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());
        
        
//        this.xml_processor = new AccuChekSmartPixProcessor(this.output_writer);
        
    //    this.setMeterType("Roche", this.getName());
        
        try
        {
    //        this.open();
        }
        catch(Exception ex)
        {
        }
        
    }
    
    
    
    /**
     * Constructor
     * 
     * @param params 
     * 
     * @param drive_letter
     * @param writer
     * @param da 
     * @param max_records 
     */
    public AccuChekSmartPix(String params, OutputWriter writer, DataAccessPlugInBase da, int max_records)
    {
        super(da, writer);
        //super();
        //this.setConnectionPort(drive_letter);
        this.output_writer = writer;
        this.port_param = params;
        this.m_da = da;
        this.max_records = max_records;
        this.ic = m_da.getI18nControlInstance();
        
        this.reader = new AccuChekSmartPixReaderV2(m_da, writer, this);
        
        
//XV        this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());
        
        
//        this.xml_processor = new AccuChekSmartPixProcessor(this.output_writer);
        
    //    this.setMeterType("Roche", this.getName());
        
        try
        {
    //        this.open();
        }
        catch(Exception ex)
        {
        }
        
    }

    
    /**
     * Constructor
     * 
     * @param cmp 
     */
/*    public AccuChekSmartPix(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
  */  
    
    
    /** 
     * open
     */
/*    public boolean open() throws PlugInBaseException
    {
        File f = new File(this.getConnectionPort());
        
        if (f.exists())
            communication_established = true;
        else
            communication_established = false;

        return communication_established;
    }
  */  
    
    
    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


    /**
     * Get Max Memory Records
     * 
     * @return 
     */
    public abstract int getMaxMemoryRecords();
    
    
    /**
     * Process Xml - This differs for Meter or/and Pump
     * @param file
     */
    public abstract void processXml(File file);    
    




    /** 
     * readDeviceDataFull
     * @throws PlugInBaseException 
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        System.out.println("readDeviceDataFull");
        
        reader.readDevice();
        
        
/*        
        // write preliminary device identification, based on class
        DeviceIdentification di = this.output_writer.getDeviceIdentification();
        
        di.company = "Accu-Chek";
        di.device_selected = "SmartPix Device Reader";
        
        di.device_identified = "Accu-Chek " + this.getName() + " [not identified]";

        this.output_writer.writeDeviceIdentification();
  */      
        
        /*
        // start working
        String drv = this.port_param;
        String cmd = drv + "\\TRG\\";
        
        cmd = m_da.pathResolver(cmd);

        System.out.println("Cmd: " + cmd);
        
        
        this.writeStatus("PIX_ABORT_AUTOSCAN");
        
        //System.out.println("Abort auto scan");
        
        this.output_writer.setSpecialProgress(5);
        
        // abort auto scan
        File f = new File(cmd + "TRG09.PNG");
        System.out.println("TRG09: " +  f.exists());
        
//        f.setLastModified(System.currentTimeMillis());
        writeToFile(f);
        
        
        f = new File(cmd + "TRG03.PNG");
        writeToFile(f);
//        f.setLastModified(System.currentTimeMillis());
        

        //this.writeStatus("PIX_READING");
        this.output_writer.setSpecialProgress(10);
        
        // read device  
        f = new File(cmd + "TRG09.PNG");
        writeToFile(f);
//      f.setLastModified(System.currentTimeMillis());
        
        f = new File(cmd + "TRG00.PNG");
        writeToFile(f);
//      f.setLastModified(System.currentTimeMillis());
        
        boolean found = false;
        sleep(2000);
        
        int count_el = 0;
        
        
        do
        {

            if (this.isDeviceStopped())
            {
                this.setDeviceStopped();
                found = true;
            }
            
            int st = readStatusFromConfig(drv);
            
            System.out.println("Status: " + st);
            
            if (st==1)
            {
                this.writeStatus("PIX_UNRECOVERABLE_ERROR");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }
            else if (st==2)
            {
                
                this.writeStatus("PIX_FINISHED_READING");
                this.output_writer.setSpecialProgress(90);

                //System.out.println("Finished reading");
                return;
            }
            else if (st==4)
            {
                count_el += this.getNrOfElementsFor1s();
                //System.out.println("Reading elements: " + count_el);
                
                
                float procs_x = (count_el*(1.0f))/this.max_records;
                
                //int procs = (int)(procs_x * 100.0f);
                
                //System.out.println("Procents full: " + procs);
                //float procs_calc = 0.007f * procs;
                
                int pro_calc = (int)((0.2f + (0.007f * (procs_x * 100.0f)))*100.0f);
                
                //System.out.println("Procents: " + pro_calc);
                
                //this.writeStatus(String.format("PIX_READING_ELEMENT", pro_calc + " %"));
                this.writeStatus("PIX_READING_ELEMENT"); //, pro_calc + " %"));
                this.output_writer.setSpecialProgress(pro_calc);

            }
            /*else if (st==20)
            {
                this.writeStatus("PIX_DEVICE_NOT_FOUND");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }*/
/*            else if ((st>99) || (st==20))
            {
                if ((st==101) || (st==20))
                {
                    this.writeStatus("PIX_FINISHED_REPORT_READY");
                    //System.out.println("Finished reading. Report ready." );
                    this.output_writer.setSpecialProgress(95);

                    
                    File f1 = new File(m_da.pathResolver(drv + "\\REPORT\\XML"));
                    
                    File[] fls = f1.listFiles(new FileFilter()
                    {

                        public boolean accept(File file)
                        {
                            return ((file.getName().toUpperCase().contains(".XML")) &&
                                    (file.getName().startsWith(getFirstLetterForReport())));
                        }}
                    );
                    
                    
                    //processXml(fls[0]);
                    processXml(fls[0]);

                    this.output_writer.setSpecialProgress(100);
                    this.output_writer.setSubStatus(null);
                    
                    return;
                    
                }
                else
                {
                    this.writeStatus("PIX_FINISHED_REPORT_READY");
                    this.output_writer.setSpecialProgress(95);

                    return;
                }
            }
                
            
            sleep(1000);
            
            
        } while(found!=true);
        
        this.setDeviceStopped();
        //this.output_writer.setSubStatus(null);
  */      
        //System.out.println("We got out !!!!");
    }
    
    /*
    private void writeToFile(File file)
    {
        try
        {
        //BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        //bw.newLine();
            
            BufferedReader bw = new BufferedReader(new FileReader(file));
            bw.readLine();
        }
        catch(Exception ex)
        {
            System.out.println("Problem writing to file: " + ex);
        }
        
    }*/
    
    

    /** 
     * test
     */
    public void test()
    {
    }
    
    
    
    /**
     * Letter with which report starts (I for insulin pumps, G for glucose meters)
     * 
     * @return
     */
    public abstract String getFirstLetterForReport();
    
    
    
    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     * 
     * @throws PlugInBaseException 
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    /** 
     * This is method for reading configuration
     * 
     * @throws PlugInBaseException
     */
    public void readConfiguration() throws PlugInBaseException
    {
    }
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws PlugInBaseException
     */
    public void readInfo() throws PlugInBaseException
    {
    }
    
    
    
    
    
    /**
     * Is Device Stopped
     * 
     * @return
     */
    public boolean isDeviceStopped()
    {
        if (this.output_writer.isReadingStopped())
            return true;
        
        return false;
        
    }
    
    
    /**
     * Set Device Stopped
     */
    public void setDeviceStopped()
    {
        this.output_writer.setSubStatus(null);
        this.output_writer.setSpecialProgress(100);
        this.output_writer.endOutput();
    }
    
    
    /**
     * Write Status
     * 
     * @param text_i18n
     */
    public void writeStatus(String text_i18n)
    {
        writeStatus(text_i18n, true);
    }
    
    
    /**
     * Write Status
     * 
     * @param text_i18n
     * @param process
     */
    public void writeStatus(String text_i18n, boolean process)
    {
        String tx = "";
        
        if (process)
            tx = ic.getMessage(text_i18n);
        else
            tx = text_i18n;
        
        this.output_writer.setSubStatus(tx);
//x        System.out.println(tx);
        // write log
        
    }
    

    /**
     * getNrOfElementsFor1s - How many elements are read in 1s (which is our refresh time)
     * @return number of elements
     */
    public abstract int getNrOfElementsFor1s();

    
    
    

    
    
    
    
    
    
    
    
    
    
    
    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return true;
    }
    
    
    
    
    
    
    
    
    /** 
     * Get Connection Protocol
     * 
     * @return 
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML;
    }
    

    
    
    
    /**
     * setDeviceCompany - set Company for device
     * 
     * @param company
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        this.device_company = company;
    }
    
    
    
    /**
     * getDeviceCompany - get Company for device
     * 
     * @return 
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.device_company;
    }

    
    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * 
     * @return
     */
    public boolean isReadableDevice()
    {
        return true;
    }

    
    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_FROM_DEVICE + DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE;
    }
    


    /**
     * Does this device support file download. Some devices have their native software, which offers export 
     * into some files (usually CSV files or even XML). We sometimes add support to download from such
     * files, and in some cases this is only download supported. 
     *  
     * @return
     */
    public boolean isFileDownloadSupported()
    {
        return true;
    }
    

    /**
     * Get Connection Port
     * 
     * @return
     */
    public String getConnectionPort()
    {
        return this.port_param;
    }
    
    
    
    
    
    /**
     * Get File Download Types as FileReaderContext. 
     * 
     * @return
     */
    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        // FIXME
        return null;
    }


    // SELECTABLE INTERFACE
    
    /** 
     * compareTo
     */
    public int compareTo(SelectableInterface o)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    /** 
     * Get Column Count
     */
    public int getColumnCount()
    {
        return m_da.getPluginDeviceUtil().getColumnCount();
    }
    
    
    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        return m_da.getPluginDeviceUtil().getColumnName(num);
    }    
    
    
    /** 
     * Get Column Width
     */
    public int getColumnWidth(int num, int width)
    {
        return m_da.getPluginDeviceUtil().getColumnWidth(num, width);
    }
    
    
    /** 
     * getColumnValue - get Value of column, for configuration
     */
    public String getColumnValue(int num)
    {
        return m_da.getPluginDeviceUtil().getColumnValue(num, this);
    }
    
    
    /** 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }


    /** 
     * getItemId
     */
    public long getItemId()
    {
        return 0;
    }

    /** 
     * getShortDescription
     */
    public String getShortDescription()
    {
        return this.getName();
    }

    /** 
     * isFound
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }

    /** 
     * isFound
     */
    public boolean isFound(int value)
    {
        return true;
    }

    /** 
     * isFound
     */
    public boolean isFound(String text)
    {
        return true;
    }

    /** 
     * setColumnSorter
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }

    /** 
     * setSearchContext
     */
    public void setSearchContext()
    {
    }
    
    
    
    
    /**
     * Get Device Source Name
     * 
     * @return
     */
    public String getDeviceSourceName()
    {
        return device_source_name;
    }
    
    // ALLOWED ACTION
    
    
    boolean can_read_data = false;
    boolean can_read_partitial_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;

    /** 
     * Set Device Allowed Actions
     */
    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data, boolean can_read_device_info, boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data;
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;
    }

    
    /**
     * canReadData - Can Meter Class read data from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadData()
    {
        return this.can_read_data;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * 
     * @return true if action is allowed
     */
    public boolean canReadPartitialData()
    {
        return this.can_read_partitial_data;
    }

    /**
     * canReadDeviceInfo - tells if we can read info about device
     * 
     * @return true if action is allowed
     */
    public boolean canReadDeviceInfo()
    {
        return this.can_read_device_info;
    }

    /**
     * canReadConfiguration - tells if we can read configuration from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadConfiguration()
    {
        return this.can_read_device_configuration;
    }

    
    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        return this.output_writer.getDeviceIdentification();
    }
    
    
    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return this.communication_established;
    }
    
    
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }
    
    
    /** 
     * Open
     * 
     * @return 
     * @throws PlugInBaseException 
     */
    public boolean open() throws PlugInBaseException
    {
        File f = new File(this.getConnectionPort());
        
        if (f.exists())
            communication_established = true;
        else
            communication_established = false;

        return communication_established;
    }
    

    
    /**
     * Close
     * 
     * @throws PlugInBaseException
     */
    public void close() throws PlugInBaseException
    {
    }




   

    public void dispose()
    {
    }


    public String getDeviceSpecialComment()
    {
        return "";
    }
    
    
    
    
}
