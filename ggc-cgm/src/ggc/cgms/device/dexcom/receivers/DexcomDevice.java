package ggc.cgms.device.dexcom.receivers;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for Pump devices)
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
 *  Filename:     FRC_MinimedCarelink
 *  Description:  Minimed Carelink File Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum DexcomDevice
{
    Dexcom_G4(ReceiverApiType.G4_Api, "Dexcom G4"), //

    Dexcom_7_Receiver(ReceiverApiType.R2_Api, "Dexcom 7"), //
    Dexcom_7_Plus(ReceiverApiType.R2_Api, "Dexcom 7 Plus"), //

    ;

    private ReceiverApiType api;
    private String description;


    private DexcomDevice(ReceiverApiType api, String desc)
    {
        this.api = api;
        this.description = desc;
    }


    public ReceiverApiType getApi()
    {
        return api;
    }


    public void setApi(ReceiverApiType api)
    {
        this.api = api;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }

}
