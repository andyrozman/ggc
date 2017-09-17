package ggc.plugin.device;

import java.util.List;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectableInterfaceV2;

import ggc.core.util.GGCI18nControl;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceType;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     DeviceAbstract  
 *  Description:  
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class DeviceAbstract implements DeviceInterface, SelectableInterfaceV2
{

    protected DataAccessPlugInBase dataAccess;
    protected AbstractDeviceCompany deviceCompany;
    protected String deviceSourceName;
    protected String connectionParameters = null;
    protected String connectionParametersRaw = null;
    protected DeviceSpecialConfigPanelInterface specialConfig = null;

    protected boolean canReadData = false;
    protected boolean canReadPartitialData = false;
    protected boolean canReadDeviceInfo = false;
    protected boolean canReadDeviceConfiguration = false;
    protected GGCI18nControl i18nControlAbstract = null; // DataAccessMeter.getInstance().getI18nControlInstance();
    protected OutputWriter outputWriter;
    protected List<GGCPlugInFileReaderContext> fileContexts;
    protected String specialConfigKey = null;


    /**
     * Constructor
     * 
     * @param da
     */
    public DeviceAbstract(DataAccessPlugInBase da)
    {
        // System.out.println("DeviceAbstract: " + da);
        this.dataAccess = da;
        this.i18nControlAbstract = da.getI18nControlInstance();
        loadFileContexts();
        this.initSpecialConfig();
    }


    /**
     * Constructor
     * @param adc 
     * 
     * @param da
     */
    public DeviceAbstract(AbstractDeviceCompany adc, DataAccessPlugInBase da)
    {
        this(da);
        /*
         * this.dataAccess = da;
         * this.i18nControlAbstract = da.getI18nControlInstance();
         * loadFileContexts();
         * this.initSpecialConfig();
         */

        // System.out.println("DA: " + this.dataAccess);

        this.setDeviceCompany(adc);
        /*
         * this.dataAccess = da;
         * this.i18nControlAbstract = da.getI18nControlInstance();
         * loadFileContexts();
         */
        // this.initSpecialConfig();
    }


    /**
     * Constructor
     * 
     * @param da
     * @param output_writer_ 
     */
    public DeviceAbstract(DataAccessPlugInBase da, OutputWriter output_writer_)
    {
        this.dataAccess = da;
        this.i18nControlAbstract = da.getI18nControlInstance();
        this.outputWriter = output_writer_;
        loadFileContexts();
        this.initSpecialConfig();
    }


    /**
     * Constructor
     * 
     * @param parameters
     * @param writer
     * @param da
     */
    public DeviceAbstract(String parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        this.dataAccess = da;
        this.i18nControlAbstract = da.getI18nControlInstance();
        this.outputWriter = writer;
        loadFileContexts();
        this.initSpecialConfig();
        this.setConnectionParameters(parameters);
    }


    /**
     * Pre Init Init
     * 
     * @param parameters
     * @param writer
     * @param da
     */
    public void preInitInit(String parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        this.dataAccess = da;
        this.i18nControlAbstract = da.getI18nControlInstance();
        this.outputWriter = writer;
        loadFileContexts();
        this.initSpecialConfig();
        this.setConnectionParameters(parameters);
    }


    // Device Interface

    /** 
     * clearDeviceData - Clear data from device 
     */
    public void clearDeviceData()
    {
    }


    /**
     * This is method for reading partial data from device. This can be used if your device can be read partialy 
     * (from some date to another)
     * 
     * @throws PlugInBaseException 
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    /** 
     * This is method for reading configuration, in case that dump doesn't give this information.
     * 
     * @throws PlugInBaseException
     */
    public void readConfiguration() throws PlugInBaseException
    {
    }


    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do).
     *  
     * @throws PlugInBaseException
     */
    public void readInfo() throws PlugInBaseException
    {
    }


    /** 
     * Set Device Allowed Actions
     */
    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data,
            boolean can_read_device_info, boolean can_read_device_configuration)
    {
        this.canReadData = can_read_data;
        this.canReadPartitialData = can_read_partitial_data;
        this.canReadDeviceInfo = can_read_device_info;
        this.canReadDeviceConfiguration = can_read_device_configuration;
    }


    /**
     * canReadData - Can Meter Class read data from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadData()
    {
        return this.canReadData;
    }


    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * 
     * @return true if action is allowed
     */
    public boolean canReadPartitialData()
    {
        return this.canReadPartitialData;
    }


    /**
     * canReadDeviceInfo - tells if we can read info about device
     * 
     * @return true if action is allowed
     */
    public boolean canReadDeviceInfo()
    {
        return this.canReadDeviceInfo;
    }


    /**
     * canReadConfiguration - tells if we can read configuration from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadConfiguration()
    {
        return this.canReadDeviceConfiguration;
    }


    /**
     * setDeviceCompany - set Company for device
     * 
     * @param company
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        this.deviceCompany = company;
    }


    /**
     * getDeviceCompany - get Company for device
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.deviceCompany;
    }


    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * 
     * @return 
     */
    public DeviceIdentification getDeviceInfo()
    {
        return this.outputWriter.getDeviceIdentification();
    }


    /**
     * Get Device Source Name
     * 
     * @return
     */
    public String getDeviceSourceName()
    {
        return deviceSourceName;
    }


    /**
     * Set DataAccess Instance
     * @param da
     */
    public void setDataAccessInstance(DataAccessPlugInBase da)
    {
        this.dataAccess = da;
        this.i18nControlAbstract = da.getI18nControlInstance();
    }


    // SelectableInterface

    /** 
     * getShortDescription
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /**
     * getColumnCount - return number of displayable columns
     * 
     * @return number of displayable columns
     */
    public int getColumnCount()
    {
        return dataAccess.getPluginDeviceUtil().getColumnCount();
    }


    /**
     * getColumnName - return name of specified column
     * 
     * @param num number of column
     * @return string displaying name of column (usually this is I18N version of string
     */
    public String getColumnName(int num)
    {
        return dataAccess.getPluginDeviceUtil().getColumnName(num);
    }


    /**
     * getColumnValue - return value of specified column
     * 
     * @param num number of column
     * @return string value of column
     */
    public String getColumnValue(int num)
    {
        return dataAccess.getPluginDeviceUtil().getColumnValue(num, this);
    }


    /**
     * getColumnValue - return value of specified column
     *
     * @param num number of column
     * @return string value of column
     */
    public String getToolTipValue(int num)
    {
        return dataAccess.getPluginDeviceUtil().getTooltipValue(num, this);
    }


    /**
     * getColumnValueObject - return value of specified column
     * 
     * @param num number of column
     * @return string value of column
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }


    /**
     * getColumnWidth - return width of specified column
     * 
     * @param num number of column
     * @param width total width of table
     * @return width in int of column
     */
    public int getColumnWidth(int num, int width)
    {
        return dataAccess.getPluginDeviceUtil().getColumnWidth(num, width);
    }


    /**
     * isFound(String) - if this object is filtered or not.
     * 
     * @param text String we search for
     * @return true if object is correct, fakse if not.
     */
    public boolean isFound(String text)
    {
        return true;
    }


    /**
     * isFound(int) - if this object is filtered or not.
     * 
     * @param value we searching for
     * @return true if object is correct, fakse if not.
     */
    public boolean isFound(int value)
    {
        return true;
    }


    /**
     * isFound(int,int,int) - if this object is filtered or not.
     * 
     * @param from date received from DateComponent
     * @param till date received from DateComponent
     * @param state 0 = none selected, 1=from used, 2=till used, 3=both used
     * @return true if object is correct, false if not.
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    /**
     * setSearchContext - set context for searching
     */
    public void setSearchContext()
    {
    }


    /**
     * setColumnSorter - sets class that will help with column sorting
     * 
     * @param cs ColumnSorter instance
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }


    public int compareTo(SelectableInterface o)
    {
        return this.dataAccess.getPluginDeviceUtil().compareTo(this, o);
    }


    public int compareTo(SelectableInterfaceV2 o)
    {
        return this.dataAccess.getPluginDeviceUtil().compareTo(this, o);
    }


    /** 
     * test
     */
    public void test()
    {
    }


    /** 
     * getItemId
     */
    public long getItemId()
    {
        return this.getDeviceId();
    }


    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    public boolean isReadableDevice()
    {
        return true;
    }


    /**
     * Get File Download Types as FileReaderContext. 
     * 
     * @return
     */
    public List<GGCPlugInFileReaderContext> getFileDownloadTypes(DownloadSupportType downloadSupportType)
    {
        if (downloadSupportType == DownloadSupportType.DownloadConfigFile)
        {
            return null;
        }
        else
        {
            return this.fileContexts;
        }
    }


    /**
     * hasIndeterminateProgressStatus - if status can't be determined then JProgressBar will go from 
     *     left to right side, without displaying progress.
     * @return
     */
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }


    /**
     * Load File Contexts
     */
    public void loadFileContexts()
    {
    }


    /**
     * Device has special progress status (we use this functions mostly if we have progress,
     * that is not tied just to entry readings)
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }


    /**
     * Set Device Type
     * 
     * @param group
     * @param device
     * @param type type of device (1 = Meter, 2=Pump, 3=CGMS, 4=Other)
     */
    public void setDeviceType(String group, String device, DeviceType type)
    {
        DeviceIdentification di = new DeviceIdentification(dataAccess.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        if (this.outputWriter != null)
        {
            this.outputWriter.setDeviceIdentification(di);
        }

        this.deviceSourceName = group + " " + device;
    }


    /**
     * Get Connection Parameters
     * 
     * @return
     */
    public String getMainConnectionParameter()
    {
        return this.connectionParameters;
    }


    /**
     * Get Connection Parameters
     * 
     * @return
     */
    public String getConnectionParameters()
    {
        return this.connectionParameters;
    }


    /**
     * Set Connection Parameters
     * 
     * @param param 
     */
    public void setConnectionParameters(String param)
    {
        this.connectionParametersRaw = param;

        if (this.hasNoConnectionParameters())
        {
            this.connectionParameters = "";
        }
        else
        {
            if (this.hasSpecialConfig())
            {
                this.specialConfig.loadConnectionParameters(param);
                this.connectionParameters = this.specialConfig.getDefaultParameter();
            }
            else
            {
                this.connectionParameters = this.connectionParametersRaw;
            }
        }

    }


    /**
     * Are Connection Parameters Valid - validate
     * 
     * @return
     */
    public boolean areConnectionParametersValid()
    {
        return this.areConnectionParametersValid(this.connectionParameters);
    }


    /**
     * Are Connection Parameters Valid (String) - validate
     * 
     * @param param 
     * @return
     */
    public boolean areConnectionParametersValid(String param)
    {
        if (this.hasNoConnectionParameters())
            return true;
        else
        {
            if (this.hasSpecialConfig())
            {
                this.specialConfig.loadConnectionParameters(param);
                return this.getSpecialConfigPanel().areConnectionParametersValid();
            }
            else
                return this.connectionParameters != null && this.connectionParameters.length() > 0;
        }
    }


    /**
     * Has No Connection Parameters - In rare cases we have no parameters for a device (for example if 
     * we support just import from non-permanent location)
     * 
     * @return
     */
    public boolean hasNoConnectionParameters()
    {
        return false;
    }


    /**
     * Has Special Config
     * 
     * @return
     */
    public boolean hasSpecialConfig()
    {
        return this.specialConfigKey != null;
    }


    /**
     * Get Special Config Panel
     * 
     * @return
     */
    public DeviceSpecialConfigPanelInterface getSpecialConfigPanel()
    {
        return null;
    }


    /**
     * Initialize Special Config (setting specialConfigKey)
     */
    public void initSpecialConfig()
    {
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
        return false;
    }


    /**
     * Pre Init Device - Does preinit
     */
    public void preInitDevice()
    {
    }


    /**
     * Set Output Writer
     * 
     * @param ow
     */
    public void setOutputWriter(OutputWriter ow)
    {
        this.outputWriter = ow;
    }


    public boolean hasDefaultParameter()
    {
        return true;
    }


    public String getDeviceSpecialComment()
    {
        return "";
    }


    /**
     * Get Output Writer instance
     * @return
     */
    public OutputWriter getOutputWriter()
    {
        return this.outputWriter;
    }

}
