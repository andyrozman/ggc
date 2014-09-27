package ggc.pump.device;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.util.DataAccessPump;

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

public abstract class AbstractPump extends DeviceAbstract implements PumpInterface // ,
                                                                                   // SelectableInterface
{

    // AbstractDeviceCompany pump_company;

    protected int m_status = 0;
    // protected I18nControlAbstract ic = null;
    // //DataAccessPump.getInstance().getI18nControlInstance();

    // protected String m_info = "";
    // protected int m_time_difference = 0;
    // protected ArrayList<PumpValuesEntry> data = null;

    // protected OutputWriter m_output_writer = null;

    protected String[] profile_names = null;
    protected String device_name;
    // protected OutputWriter output_writer;
    protected String parameter;

    // protected DataAccessPump m_da;

    /**
     * Constructor
     */
    public AbstractPump()
    {
        super(DataAccessPump.getInstance());
    }

    /**
     * Constructor
     * 
     * @param param 
     * @param ow
     */
    public AbstractPump(String param, OutputWriter ow)
    {
        super(DataAccessPump.getInstance(), ow);
        this.parameter = param;
    }

    /**
     * Constructor
     * 
     * @param comm_parameters
     * @param writer
     * @param da 
     */
    public AbstractPump(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }

    /**
     * Constructor
     * 
     * @param ow
     */
    public AbstractPump(OutputWriter ow)
    {
        super(DataAccessPump.getInstance(), ow);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public AbstractPump(AbstractDeviceCompany cmp)
    {
        super(cmp, DataAccessPump.getInstance());
        // super(DataAccessPump.getInstance());
        // this.setDeviceCompany(cmp);
        this.setPumpType(cmp.getName(), getName());
    }

    /**
     * Set Pump Type
     * 
     * @param group
     * @param device
     */
    public void setPumpType(String group, String device)
    {
        this.device_name = device;

        DeviceIdentification di = new DeviceIdentification(m_da.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        if (this.output_writer != null)
        {
            this.output_writer.setDeviceIdentification(di);
            // this.output_writer.
            // this.device_instance =
            // MeterManager.getInstance().getMeterDevice(group, device);
        }

        this.device_source_name = group + " " + device;

    }

    /**
     * Get Name
     */
    public String getName()
    {
        return "Generic device";
    }

    // ************************************************
    // *** Available Functionality ***
    // ************************************************

    /**
     * Get Profile Names
     * 
     * @return
     */
    public String[] getProfileNames()
    {
        return this.profile_names;
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

}
