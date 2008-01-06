/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */


package ggc.data.meter;

import javax.swing.ImageIcon;

import ggc.data.DailyValuesRow;
import ggc.data.meter.device.AscensiaContourMeter;
import ggc.data.meter.device.AscensiaDEXMeter;
import ggc.data.meter.device.GenericMeter;
import ggc.data.meter.device.MeterInterface;
import ggc.db.GGCDb;
import ggc.db.hibernate.MeterH;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;

public class MeterConfiguration
{

    protected I18nControl m_ic = I18nControl.getInstance();
    MeterH meter = null;
    DataAccess m_da = DataAccess.getInstance();
    int m_error = 0;

    MeterInterface m_meter_device;

    /**
     * Constructor for SerialMeterImport.
     */
    public MeterConfiguration()
    {
        int meter_id = m_da.getSettings().getMeterType();

        if (meter_id<=0)
        {
            System.out.println("MeterImportManager: No meter defined. Exiting.");
            this.m_error = -1;
        }

        initMeter();

        //this.m_meter_device = new AscensiaDEXMeter("COM9");
        //this.m_meter_device.setComPort("

        //this.m_meter_device.getInfo();
        //this.m_meter_device.test();
        //this.m_meter_device.test2();
        //this.m_meter_device.

    }


    public void initMeter()
    {
        //this.m_meter_device = new AscensiaDEXMeter("COM9");
        this.m_meter_device = new GenericMeter();
    }


    public int getErrorCode()
    {
        return this.m_error;
    }


    public MeterInterface getMeterDevice()
    {
        return this.m_meter_device;
    }



    /**
     * Gets the image
     * @return Returns a ImageIcon
     */
    public ImageIcon getIcon()
    {
        return this.m_meter_device.getIcon();
    }


    /**
     * Gets the name
     * @return Returns a String
     */
    public String getName()
    {
        return this.m_meter_device.getName(); 
    }


    public String getInfo()
    {
        return this.m_meter_device.getInfo(); 
    }


    public boolean isStatusOK()
    {
        return this.m_meter_device.isStatusOK();
    }


    public int getTimeDifference()
    {
        return this.m_meter_device.getTimeDifference();
    }


/*
    public String[] getAvailableMeters()
    {
        return this.meter_names;
    }
*/
/*
    public String getMeterClassName(int index)
    {
        //return this.m_meter_device.
        //return this.meter_classes[index];
    }

    public String getMeterDeviceClassName(int index)
    {
        return this.meter_device_classes[index];
    }
    

    public String getMeterClassName(String name)
    {
    int index = 0;

    for (int i=0; i<this.meter_names.length; i++)
    {
        if (this.meter_names.equals(name))
        {
        index = i;
        break;
        }
    }

    return this.meter_classes[index];
    }
*/

    public static final int METER_INTERFACE_1 = 1;  // old meter interface (stephen)
    public static final int METER_INTERFACE_2 = 2;  // new meter interface (andy)

    public int getSelectedMeterIndex(int type, int index)
    {
	if (type==1)
	{
	    if (index<4)
	    {
		return index;
	    }
	    else
		return 0;
	}
	else if (type==2)
	{
	    if (index>3)
	    {
		return index;
	    }
	    else
		return 0;
	}
	else
	    return 0;

    }

    
    //************************************************
    //***        Available Functionality           ***
    //************************************************


    /**
     * canReadData - Can Meter Class read data from device
     */
    public boolean canReadData()
    {
        return true;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     */
    public boolean canReadPartitialData()
    {
        return false;
    }

    /**
     * canClearData - Can Meter class clear data from meter.
     */
    public boolean canClearData()
    {
        return false;
    }




/*
    public Object[] getAvailableMetersCombo()
    {
        Object oo[] = new Object[this.meter_names.length+1];
        oo[0] = this.m_ic.getMessage("NONE");

        for (int i=0; i<this.meter_names.length; i++) 
        {
            oo[i+1] = this.meter_names[i];
        }

        return oo;
    }
*/

}
