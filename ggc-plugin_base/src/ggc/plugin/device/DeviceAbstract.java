package ggc.plugin.device;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;

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


public abstract class DeviceAbstract implements DeviceInterface, SelectableInterface
{

    protected DataAccessPlugInBase m_da;
    protected AbstractDeviceCompany device_company;
    protected String device_source_name;
    protected String connection_parameters = null;
    protected String connection_parameters_raw = null;
    protected DeviceSpecialConfigPanelInterface special_config = null;
    
    protected boolean can_read_data = false;
    protected boolean can_read_partitial_data = false;
    protected boolean can_read_device_info = false;
    protected boolean can_read_device_configuration = false;
    protected I18nControlAbstract ic = null; //DataAccessMeter.getInstance().getI18nControlInstance();
    protected OutputWriter output_writer;
    protected GGCPlugInFileReaderContext[] file_contexts;
    
    /**
     * Device Type: Meter
     */
    public static final int DEVICE_TYPE_METER = 1;

    /**
     * Device Type: Pump
     */
    public static final int DEVICE_TYPE_PUMP = 2;
    
    /**
     * Device Type: CGMS
     */
    public static final int DEVICE_TYPE_CGMS = 3;
    
    /**
     * Device Type: Other
     */
    public static final int DEVICE_TYPE_OTHER = 4;
    
    
    
    /**
     * Constructor
     * 
     * @param da
     */
    public DeviceAbstract(DataAccessPlugInBase da)
    {
        //System.out.println("DeviceAbstract: " + da);
        this.m_da = da;
        this.ic = da.getI18nControlInstance();
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
/*        this.m_da = da;
        this.ic = da.getI18nControlInstance();
        loadFileContexts();
        this.initSpecialConfig();*/
        
        //System.out.println("DA: " + this.m_da);
        
        this.setDeviceCompany(adc);
/*        this.m_da = da;
        this.ic = da.getI18nControlInstance();
        loadFileContexts();*/
        //this.initSpecialConfig();
    }
    
    
    
    /**
     * Constructor
     * 
     * @param da
     * @param output_writer_ 
     */
    public DeviceAbstract(DataAccessPlugInBase da, OutputWriter output_writer_)
    {
        this.m_da = da;
        this.ic = da.getI18nControlInstance();
        this.output_writer = output_writer_;
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
        this.m_da = da;
        this.ic = da.getI18nControlInstance();
        this.output_writer = writer;
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
        this.m_da = da;
        this.ic = da.getI18nControlInstance();
        this.output_writer = writer;
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
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.device_company;
    }

    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * 
     * @return 
     */
    public DeviceIdentification getDeviceInfo()
    {
        return this.output_writer.getDeviceIdentification();
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
    
    
    /**
     * Set DataAccess Instance
     * @param da
     */
    public void setDataAccessInstance(DataAccessPlugInBase da)
    {
        this.m_da = da;
        this.ic = da.getI18nControlInstance();
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
        return m_da.getPluginDeviceUtil().getColumnCount();
    }


    /**
     * getColumnName - return name of specified column
     * 
     * @param num number of column
     * @return string displaying name of column (usually this is I18N version of string
     */
    public String getColumnName(int num)
    {
        return m_da.getPluginDeviceUtil().getColumnName(num);
    }    


    /**
     * getColumnValue - return value of specified column
     * 
     * @param num number of column
     * @return string value of column
     */
    public String getColumnValue(int num)
    {
        return m_da.getPluginDeviceUtil().getColumnValue(num, this);
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
        return m_da.getPluginDeviceUtil().getColumnWidth(num, width);
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




    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param   o the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *      is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
    public int compareTo(SelectableInterface o)
    {
        return 0;
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
     * Does this device support file download. Some devices have their native software, which offers export 
     * into some files (usually CSV files or even XML). We sometimes add support to download from such
     * files, and in some cases this is only download supported. 
     *  
     * @return
     */
    public boolean isFileDownloadSupported()
    {
        return false;
    }
    
    
    /**
     * Get File Download Types as FileReaderContext. 
     * 
     * @return
     */
    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        return this.file_contexts;
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
    public void setDeviceType(String group, String device, int type)
    {
        DeviceIdentification di = new DeviceIdentification(m_da.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;
        
        if (this.output_writer!=null)
            this.output_writer.setDeviceIdentification(di);
        
        this.device_source_name = group + " " + device;
    }
    

    /**
     * Get Connection Parameters
     * 
     * @return
     */
    public String getMainConnectionParameter()
    {
        return this.connection_parameters;
    }
    
    
    
    /**
     * Get Connection Parameters
     * 
     * @return
     */
    public String getConnectionParameters()
    {
        return this.connection_parameters;
    }
    
    /**
     * Set Connection Parameters
     * 
     * @param param 
     */
    public void setConnectionParameters(String param)
    {
        this.connection_parameters_raw = param;
        
        if (this.hasNoConnectionParameters())
            this.connection_parameters = "";
        else
        {
            if (this.hasSpecialConfig())
            {
                this.special_config.loadConnectionParameters(param);
                this.connection_parameters = this.special_config.getDefaultParameter();
            }
            else
                this.connection_parameters = this.connection_parameters_raw;
        }
        
    }
    
    
    /**
     * Are Connection Parameters Valid - validate
     * 
     * @return
     */
    public boolean areConnectionParametersValid()
    {
        return this.areConnectionParametersValid(this.connection_parameters);
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
                this.special_config.loadConnectionParameters(param);
                return this.getSpecialConfigPanel().areConnectionParametersValid();
            }
            else
                return ((this.connection_parameters!=null) && (this.connection_parameters.length()>0));
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
        return (this.special_config!=null);
    }
    
    
    /**
     * Get Special Config Panel
     * 
     * @return
     */
    public DeviceSpecialConfigPanelInterface getSpecialConfigPanel()
    {
        return this.special_config;
    }

    
    /**
     * Initialize Special Config
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
     * 
     * @see hasPreInit
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
        this.output_writer = ow;
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
        return this.output_writer;
    }
    
    
}
