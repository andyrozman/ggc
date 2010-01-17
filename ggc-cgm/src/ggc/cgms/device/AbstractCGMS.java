package ggc.cgms.device;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.file.FileReaderContext;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractCGMS implements CGMSInterface, SelectableInterface
{

    AbstractDeviceCompany cgms_company;

    protected int m_status = 0;
    protected I18nControlAbstract ic = null; //DataAccessPump.getInstance().getI18nControlInstance();

    //protected String m_info = "";
    //protected int m_time_difference = 0;
    //protected ArrayList<CGMSValuesEntry> data = null;
    
    protected OutputWriter m_output_writer = null;
    
    //protected String[] profile_names = null;
    protected String device_name;
    protected OutputWriter output_writer;
    protected String parameter;

    /**
     * Constructor
     */
    public AbstractCGMS()
    {
        super();
        ic = DataAccessCGMS.getInstance().getI18nControlInstance();
    }

    
    /**
     * Constructor
     * 
     * @param param 
     * @param ow
     */
    public AbstractCGMS(String param, OutputWriter ow)
    {
        super();
        ic = DataAccessCGMS.getInstance().getI18nControlInstance();
        this.m_output_writer = ow;
        this.parameter = param;
    }
    
    
    /**
     * Constructor
     * 
     * @param ow
     */
    public AbstractCGMS(OutputWriter ow)
    {
        super();
        this.m_output_writer = ow;
    }


    /**
     * Constructor
     * 
     * @param cmp
     */
    public AbstractCGMS(AbstractDeviceCompany cmp)
    {
        super();
        this.setDeviceCompany(cmp);
        this.setCGMSType(cmp.getName(), getName());
    }
    
    
    
    /**
     * Set Pump Type
     * 
     * @param group
     * @param device
     */
    public void setCGMSType(String group, String device)
    {
        this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification();
        di.company = group;
        di.device_selected = device;
        
        if (this.output_writer!=null)
            this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
        //this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
        
        this.device_source_name = group + " " + device;
        
    }
    
    
    
    boolean can_read_data = false; 
    boolean can_read_partitial_data = false;
    boolean can_clear_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;
    
    
    
    
    
    /** 
     * Set Device Allowed Actions
     */
    public void setDeviceAllowedActions(boolean can_read_data, 
                                        boolean can_read_partitial_data,
                                        boolean can_read_device_info,
                                        boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data; 
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;
    }
    
    
    
    
    
    /*
    public GenericMeter(int meter_type, String portName)
    {

	super(meter_type,
	      9600, 
	      SerialPort.DATABITS_8, 
	      SerialPort.STOPBITS_1, 
	      SerialPort.PARITY_NONE);

	data = new ArrayList<DailyValuesRow>();

	try
	{
	    this.setPort(portName);

	    if (!this.open())
	    {
		this.m_status = 1;
	    }
	}
	catch(Exception ex)
	{
	    System.out.println("AscensiaMeter -> Error adding listener: " + ex);
	    ex.printStackTrace();
	}
    }
*/




    /**
     * Get Name
     */
    public String getName()
    {
        return "Generic device";
    }





    //************************************************
    //***                    Test                  ***
    //************************************************

    /** 
     * test
     */
    public void test()
    {
    }



    //************************************************
    //***        Available Functionality           ***
    //************************************************


    
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
     * canClearData - Can Meter class clear data from meter.
     * 
     * @return true if action is allowed
     */
    public boolean canClearData()
    {
        return this.can_clear_data;
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
        return 5;
    }


    

    float device_columns_width[] = { 0.25f, 0.25f, 0.3f, 0.1f, 0.1f };
    String device_columns[] = null;
    
    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        if (device_columns==null)
        {
            this.device_columns = new String[5];
            device_columns[0] = ic.getMessage("DEVICE_COMPANY");
            device_columns[1] = ic.getMessage("DEVICE_DEVICE");
            device_columns[2] = ic.getMessage("DEVICE_CONNECTION");
            device_columns[3] = ic.getMessage("DEVICE_DOWNLOAD");
            device_columns[4] = ic.getMessage("DEVICE_SETTINGS");
        }
        
        return device_columns[num-1];
    }


    /** 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        //System.out.println("Num: " + num);
        switch(num)
        {
            case 1:
                return this.getDeviceCompany().getName();

            case 2:
                return this.getName();
                
            case 3:
                return this.getDeviceCompany().getConnectionSamples();

            case 4:
                if (this.getDownloadSupportType()==DownloadSupportType.DOWNLOAD_YES)
                    return DataAccessCGMS.getInstance().getYesNoOption(true);
                else
                    return DataAccessCGMS.getInstance().getYesNoOption(false);
                
            case 5:
                //return "Bo/Ba/Tbr";
                return DataAccessCGMS.getInstance().getYesNoOption(false);
                
                
            default:                 
                return "N/A: " + num;
        }
    }
    
    


    /** 
     * Get Column Value Object
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }


    
    
    /** 
     * Get Column Width
     */
    public int getColumnWidth(int num, int width)
    {
        return (int)(this.device_columns_width[num-1] * width);
    }


    /** 
     * Get Item Id
     */
    public long getItemId()
    {
        return 0;
    }


    /** 
     * Get Short Description
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /** 
     * Is Found
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    /** 
     * Is Found
     */
    public boolean isFound(int value)
    {
        return true;
    }


    /** 
     * Is Found
     */
    public boolean isFound(String text)
    {
        return true;
    }


    /** 
     * Set Column Sorter
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }


    /** 
     * Set Search Context
     */
    public void setSearchContext()
    {
    }


    /**
     * setDeviceCompany - set Company for device
     * 
     * @param company
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        this.cgms_company = company;
    }
    
    
    /**
     * getDeviceCompany - get Company for device
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.cgms_company;
    }

    

    
    /**
     * Get Download Support Type (if device supports downloading data from it)
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }
    
    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * 
     * @return
     */
    public String getTemporaryBasalTypeDefinition()
    {
        return "TYPE=Unit;STEP=0.1";
    }

    
    /**
     * Get Bolus Precission
     * 
     * @return
     */
    public float getBolusPrecission()
    {
        return 0.1f;
    }
    

    /**
     * Get Basal Precission
     * 
     * @return
     */
    public float getBasalPrecission()
    {
        return 0.1f;
    }

    
    /**
     * Are Pump Settings Set
     * 
     * @return
     */
    public boolean arePumpSettingsSet()
    {
        return false;
    }
    
    
    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    public int howManyMonthsOfDataStored()
    {
        return 6;
    }
    
    String device_source_name;
    
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
     * Get Download SupportType Configuration
     */
    public int getDownloadSupportTypeConfiguration()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
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
    public FileReaderContext[] getFileDownloadTypes()
    {
        return null;
    }
    
}