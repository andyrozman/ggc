package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.XmlProtocol;
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

public abstract class AbstractXmlPump extends XmlProtocol implements PumpInterface // ,
                                                                                   // SelectableInterface
{

    String connection_port = "";

    /**
     * Constructor
     * 
     * @param ow 
     */
    public AbstractXmlPump(OutputWriter ow)
    {
        super(DataAccessPump.getInstance(), ow);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public AbstractXmlPump(AbstractDeviceCompany cmp)
    {
        super(DataAccessPump.getInstance());
        this.setDeviceCompany(cmp);
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
        // this.device_name = device;

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
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return this.connection_port;
    }

    /**
     * setConnectionPort - connection port data
     * @param con_port 
     * 
     */
    public void setConnectionPort(String con_port)
    {
        this.connection_port = con_port;
    }

    /**
     * Dispose
     */
    public void dispose()
    {
    }

    // ************************************************
    // *** Device Implemented methods ***
    // ************************************************

    /** 
     * Is Device Communicating
     */
    public boolean isDeviceCommunicating()
    {
        return true;
    }

    /** 
     * Get Download Support Type
     */
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NoDownloadSupport;
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

}
