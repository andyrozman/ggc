
package ggc.plugin.device.impl.accuchek;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.plugin.protocol.XmlProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
 *  Filename:     AccuChekSmartPix  
 *  Description:  This is processing file for data from AccuChekSmartPix device
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AccuChekSmartPix extends XmlProtocol 
{
    
    private static Log log = LogFactory.getLog(AccuChekSmartPix.class);
    
    protected int max_records = 0;
    protected AccuChekSmartPixReaderAbstract reader = null;
    protected boolean communication_established = false;

    /**
     * Constructor
     * 
     * @param cmp 
     * @param da 
     */
    public AccuChekSmartPix(AbstractDeviceCompany cmp, DataAccessPlugInBase da)
    {
        super(cmp, da);
        //this.setDeviceCompany(cmp);
        //this.setDeviceType(group, device, type)
    }
    

    /**
     * Constructor
     * 
     * @param params 
     * 
     * @param drive_letter
     * @param writer
     * @param da 
     * @param max_records_ 
     */
    public AccuChekSmartPix(String params, OutputWriter writer, DataAccessPlugInBase da, int max_records_)
    {
        this(params, writer, da);
        this.setConnectionParameters(params);
        this.max_records = max_records_;
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
//        this.setConnectionParameters(params);
        this.m_da = da;
        //this.max_records = max_records;
        this.ic = m_da.getI18nControlInstance();
        
        
        this.setConnectionParameters(params);
        
        

        // FIXME
        
        //this.special_config.loadConnectionParameters(params);
        
        //this.special_config.loadConnectionParameters("/media/SMART_PIX/#;#SMARTPIX_VERSION!=3.x");
        
        //this.setConnectionParameters("/media/SMART_PIX/#;#SMARTPIX_VERSION!=3.x");
        
        //this.setConnectionParameters(this.special_config.getDefaultParameter());
        //initReader();
        
        
        
    }
    
    
    /**
     * Set Connection Parameters
     * 
     * @param param 
     */
    public void setConnectionParameters(String param)
    {
        super.setConnectionParameters(param);
        this.initReader();
    }
    
    
    
    
    private void initReader()
    {
        //this.root_drive = this.special_config.getDefaultParameter();
        //this.root_drive = "/media/SMART_PIX/";
        
        System.out.println("Init reader !!!");
        
        if (this.special_config.getParameter("SMARTPIX_VERSION").equals(AccuChekSmartPixSpecialConfig.SMARTPIX_V2))
            this.reader = new AccuChekSmartPixReaderV2(m_da, this.output_writer, this);
        else
            this.reader = new AccuChekSmartPixReaderV3(m_da, this.output_writer, this);

        try
        {
            openDevice();
        }
        catch(Exception ex)
        {
            log.error("Error checking if device present. Ex.: " + ex, ex);
        }
    }
    
    

    
    /**
     * Get Root Drive
     * 
     * @return
     */
    public String getRootDrive()
    {
        return this.getMainConnectionParameter();
    }
    

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
        reader.readDevice();
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


    protected void readXmlFileFromDevice()
    {
        
        this.writeStatus("PIX_FINISHED_REPORT_READY");
        this.output_writer.setSpecialProgress(95);

        
        File f1 = new File(m_da.pathResolver(this.getMainConnectionParameter() + "\\REPORT\\XML"));
        
        File[] fls = f1.listFiles(new FileFilter()
        {

            public boolean accept(File file)
            {
                return ((file.getName().toUpperCase().contains(".XML")) &&
                        (file.getName().startsWith(getFirstLetterForReport())));
            }}
        );
        
        //processXml(fls[0]);
        this.processXml(fls[0]);

        this.output_writer.setSpecialProgress(100);
        this.output_writer.setSubStatus(null);
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
        return openDevice();
    }
    

    
    private boolean openDevice()
    {
        //System.out.println("open():: m_da: " + m_da);
        System.out.println("open():: getMainConnectionParameter: " + this.getMainConnectionParameter());
        File f = new File(m_da.pathResolver(this.getMainConnectionParameter()));
        
        //System.out.println("open():: " + m_da.pathResolver(this.getMainConnectionParameter()));
        
        
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
        return "DEVICE_PIX_SPECIAL_COMMENT";
    }
    
    
    /**
     * Initialize Special Config
     */
    public void initSpecialConfig()
    {
        if (DataAccessPlugInBase.special_configs.containsKey("AccuChekSmartPix"))
        {
            this.special_config = (AccuChekSmartPixSpecialConfig)DataAccessPlugInBase.special_configs.get("AccuChekSmartPix");
        }
        else
        {
            //System.out.println("!!!!!!!! inti Special config");
            this.special_config = new AccuChekSmartPixSpecialConfig(m_da, this);
            DataAccessPlugInBase.special_configs.put("AccuChekSmartPix", (DeviceSpecialConfigPanelAbstract)this.special_config);
        }
    }
    
    
    /**
     * Has Pre Init - Some devices when started are in unusal state, this methods
     *    help us to put them in state we need. Example is AccuChek SmartPix, which is
     *    in autodetect state when we attach it, now if we put a meter/pump before it,
     *    it starts to automatically read, but GGC needs some time to do preliminary 
     *    stuff, so we need to have SmartPix in NO AutoScan mode). 
     * @return
     */
    public boolean hasPreInit()
    {
        if (this.reader==null)
        {
            System.out.println("hasPreInit() problem: reader==null");
            return false;
        }
        else
        {
            return this.reader.hasPreInit();
        }
        
    }
    
    
    /**
     * Pre Init Device - Does preinit
     * 
     * @see hasPreInit
     */
    public void preInitDevice()
    {
        if (this.reader==null)
            this.initReader();
        
        this.reader.preInitDevice();
    }
    
    
    /**
     * Load File Contexts - Load file contexts that device supports
     */
    public void loadFileContexts()
    {
        this.file_contexts = new GGCPlugInFileReaderContext[1];
        this.file_contexts[0] = new FRC_AccuChekSmartPixXml(m_da, this.output_writer, this);
    }

    
    public String getConnectionPort()
    {
        return this.getMainConnectionParameter();
    }

    
    
}
