package ggc.cgms.device.dexcom.receivers.data.output;

import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadData;
import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadDataConfig;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class ConsoleOutputParser implements DataOutputParserInterface
{

    public void parse(DataOutputParserType parserType, ReceiverDownloadData data)
    {

        System.out.println("Serial Number: " + data.getSerialNumber());

        switch (parserType)
        {
            case Configuration:
                {
                    for (ReceiverDownloadDataConfig cfg : data.getConfigs())
                    {
                        System.out.println(cfg);
                    }
                }
                break;
            case G4_EGVData:
            case G4_InsertionTime:
            case G4_MeterData:
            case G4_UserEventData:
                {
                    for (Object dtEl : data.getDataByType(parserType))
                    {
                        System.out.println(dtEl);
                    }
                }
                break;
            default:
                break;

        }

    }

}
