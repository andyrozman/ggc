package ggc.plugin.device.impl.minimed.data.dto;

import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;

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
 *  Filename:     MinimedConnectionParametersDTO
 *  Description:  Minimed Connection Parameters DTO.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedConnectionParametersDTO
{

    public String portName;
    public MinimedCommInterfaceType interfaceType;
    public String serialNumber;
    public int[] serialNumberBCD;
    private String nextLinkConnector;


    public MinimedConnectionParametersDTO(String communicationParameters)
    {
        // PUMP_5_CONNECTION_PARAMETER=/dev/ttyUSB1#;#MINIMED_INTERFACE!=CareLinkUSB#;#MINIMED_SERIAL!=6374638

        // communicationParameters is packed: interfaceType;port;serial_number
        String[] ports = communicationParameters.split("#;#");

        this.interfaceType = MinimedCommInterfaceType.getByName(getParameterValue(ports[1]));

        System.out.println("Interface: " + ports[1] + ", type=" + this.interfaceType);

        this.portName = ports[0];
        this.serialNumber = getParameterValue(ports[2]);
        this.serialNumberBCD = MedtronicUtil.getBitUtils().makePackedBCD(this.serialNumber);

        MedtronicUtil.setConnectionParameters(this);
    }


    public String getParameterValue(String value)
    {
        String[] val = value.split("!=");
        return val[1];
    }


    public void setNextLinkConnector(String nextLinkConnector)
    {
        this.nextLinkConnector = nextLinkConnector;
    }


    public String getNextLinkConnector()
    {
        return nextLinkConnector;
    }
}
