package ggc.cgms.device;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AbstractCGMS extends DeviceAbstract // CGMSInterface,
// SelectableInterface
{

    // AbstractDeviceCompany cgms_company;

    // protected int m_status = 0;
    // protected I18nControlAbstract i18nControlAbstract = null;
    // //DataAccessPump.getInstance().getI18nControlInstance();

    // protected String m_info = "";
    // protected int m_time_difference = 0;
    // protected ArrayList<CGMSValuesEntry> data = null;

    // protected OutputWriter m_output_writer = null;

    // protected String[] profile_names = null;
    protected String device_name;
    // protected OutputWriter outputWriter;
    protected String parameter;


    // protected DataAccessCGMS dataAccess;

    /**
     * Constructor
     */
    public AbstractCGMS()
    {
        super(DataAccessCGMS.getInstance());
    }


    /**
     * Constructor
     * 
     * @param param 
     * @param ow
     */
    public AbstractCGMS(String param, OutputWriter ow)
    {
        super(DataAccessCGMS.getInstance(), ow);
        this.parameter = param;
    }


    /**
     * Constructor
     * 
     * @param ow
     */
    public AbstractCGMS(OutputWriter ow)
    {
        super(DataAccessCGMS.getInstance(), ow);
    }


    /**
     * Constructor (should be used)
     * 
     * @param cmp
     */
    public AbstractCGMS(AbstractDeviceCompany cmp)
    {
        // super(DataAccessCGMS.getInstance());
        super(cmp, DataAccessCGMS.getInstance());
        // this.setDeviceCompany(cmp);
        this.setCGMSType(cmp.getName(), getName());
    }


    /**
     * Constructor
     * 
     * @param params
     * @param writer
     * @param da
     */
    public AbstractCGMS(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
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

        DeviceIdentification di = new DeviceIdentification(dataAccess.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        if (this.outputWriter != null)
        {
            this.outputWriter.setDeviceIdentification(di);
            // this.outputWriter.
            // this.device_instance =
            // MeterManager.getInstance().getMeterDevice(group, device);
        }

        this.deviceSourceName = group + " " + device;

    }


    /**
     * Get Name
     */
    public String getName()
    {
        return "Generic device";
    }


    /**
     * Get Download Support Type (if device supports downloading data from it)
     * 
     * @return
     */
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NotSupportedByGGC;
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


    /**
     * hasIndeterminateProgressStatus - if status can't be determined then JProgressBar will go from 
     *     left to right side, without displaying progress.
     * @return
     */
    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }


    @Override
    public boolean hasDefaultParameter()
    {
        return false;
    }

    /**
     * getInterfaceTypeForMeter - most meter devices, store just BG data, this use simple interface, but
     *    there are some device which can store different kind of data (Ketones - Optium Xceed; Food, Insulin
     *    ... - OT Smart, etc), this devices require more extended data display.
     * @return
     */
    /*
     * public int getInterfaceTypeForMeter()
     * {
     * return 0;
     * }
     */

}
