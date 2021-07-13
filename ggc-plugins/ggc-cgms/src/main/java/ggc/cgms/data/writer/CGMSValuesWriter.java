package ggc.cgms.data.writer;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.CodeEnum;

import ggc.cgms.data.CGMSDataWriter;
import ggc.cgms.data.CGMSTempValues;
import ggc.cgms.data.defs.*;
import ggc.plugin.data.DeviceTempValues;
import ggc.plugin.output.OutputWriter;

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
 *  Filename: CGMSValuesWriter
 *  Description: CGMS Values Writer - one for main table, which contains several entries, per line (data for whole day),
 *       this writer is much different than all standard writers, since it groups more entries (all day one) into
 *       one database line.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesWriter extends CGMSDataWriter
{

    private static final long serialVersionUID = -3259908872077416922L;

    static CGMSValuesWriter staticInstance;
    boolean debug = false;



    private CGMSValuesWriter()
    {
        super();
        loadDeviceDefinitions();
    }


    public static CGMSValuesWriter getInstance(OutputWriter writer)
    {
        if (staticInstance == null)
        {
            staticInstance = new CGMSValuesWriter();
        }

        staticInstance.setOutputWriter(writer);

        return staticInstance;
    }



    private void loadDeviceDefinitions()
    {

        this.put("SensorReading", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.SensorReading));

        this.put("SensorCalibration", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.SensorCalibration));

        this.put("ManualReading", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.ManualReading));

        this.put("SensorReadingTrend", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.SensorReadingTrend));


        // Trasmiter Events
        for (CGMSTransmiterEvents transEvents : CGMSTransmiterEvents.getAllValues())
        {
            addConfiguration("TransmiterEvent_" + transEvents.name(), CGMSBaseDataType.TransmiterEvent, transEvents);
        }

        // Alarms
        for (CGMSAlarms alarm : CGMSAlarms.getAllValues())
        {
            addConfiguration("Alarm_" + alarm.name(), CGMSBaseDataType.Alarm, alarm);
        }

        // Events
        for (CGMSEvents event : CGMSEvents.getAllValues())
        {
            addConfiguration("Event_" + event.name(), CGMSBaseDataType.Event, event);
        }
    }


    private void addConfiguration(String key, CGMSBaseDataType baseDataType, CodeEnum subDataType)
    {
        this.put(key, new CGMSTempValues(CGMSObject.SubEntry, baseDataType, subDataType));
    }


    public boolean writeObject(String _type, ATechDate _datetime)
    {
        return this.writeObject(_type, _datetime, null, false);
    }


    public void addConfiguration(String key, DeviceTempValues deviceTempValues)
    {
        if (debug)
        {
            System.out.println("Config [key=" + key + ", DeviceTempValues=" + deviceTempValues.toString() + "]");
        }

        this.put(key, deviceTempValues);
    }

}
