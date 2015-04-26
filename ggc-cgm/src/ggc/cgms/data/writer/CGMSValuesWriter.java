package ggc.cgms.data.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesWriter extends CGMSDataWriter // Hashtable<String,
// DeviceTempValues>
{

    static CGMSValuesWriter staticInstance;
    // static CGMSDataWriter cgmsDataWriter;
    boolean debug = false;

    private static Log LOG = LogFactory.getLog(CGMSValuesWriter.class);


    // private OutputWriter outputWriter;

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
        // cgmsDataWriter.setOutputWriter(writer);

        return staticInstance;
    }


    // public void setOutputWriter(OutputWriter writer)
    // {
    // super.se
    // this.outputWriter = writer;
    // }

    private void loadDeviceDefinitions()
    {

        // cgmsDataWriter = new CGMSDataWriter();

        // Sensor data
        this.put("SensorReading", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.SensorReading));

        // // Transmiter Events
        // addConfiguration("TransmiterEvent_SensorNotActive",
        // CGMSBaseDataType.TransmiterEvent,
        // CGMSTransmiterEvents.SensorNotActive);
        //
        // addConfiguration("TransmiterEvent_AbsoluteAberration",
        // CGMSBaseDataType.TransmiterEvent,
        // CGMSTransmiterEvents.AbsoluteAberration);
        //
        // addConfiguration("TransmiterEvent_MinimallyEGVAberration",
        // CGMSBaseDataType.TransmiterEvent,
        // CGMSTransmiterEvents.MinimallyEGVAberration);
        //
        // addConfiguration("TransmiterEvent_NoAntenna",
        // CGMSBaseDataType.TransmiterEvent, CGMSTransmiterEvents.NoAntenna);
        //
        // addConfiguration("TransmiterEvent_SensorOutOfCal",
        // CGMSBaseDataType.TransmiterEvent,
        // CGMSTransmiterEvents.SensorOutOfCal);
        //
        // addConfiguration("TransmiterEvent_CountsAberration",
        // CGMSBaseDataType.TransmiterEvent,
        // CGMSTransmiterEvents.CountsAberration);
        //
        // addConfiguration("TransmiterEvent_PowerAberration",
        // CGMSBaseDataType.TransmiterEvent,
        // CGMSTransmiterEvents.PowerAberration);
        //
        // addConfiguration("TransmiterEvent_RFBadStatus",
        // CGMSBaseDataType.TransmiterEvent,
        // CGMSTransmiterEvents.RFBadStatus);

        // Alarms
        // addConfiguration("Alarm_BatteryLow", CGMSBaseDataType.Alarm,
        // CGMSAlarms.BatteryLow);
        // addConfiguration("Alarm_AlarmClock", CGMSBaseDataType.Alarm,
        // CGMSAlarms.AlarmClock);
        // addConfiguration("Alarm_ReviewDateTime",
        // CGMSBaseDataType.Alarm, CGMSAlarms.ReviewDateTime);
        // addConfiguration("Alarm_CalibrationRequired",
        // CGMSBaseDataType.Alarm, CGMSAlarms.CalibrationRequired);
        //
        // addConfiguration("Alarm_WeakSignal", CGMSBaseDataType.Alarm,
        // CGMSAlarms.WeakSignal);
        // addConfiguration("Alarm_SensorAlarm", CGMSBaseDataType.Alarm,
        // CGMSAlarms.SensorAlarm);
        // addConfiguration("Alarm_CalibrationError",
        // CGMSBaseDataType.Alarm, CGMSAlarms.CalibrationError);
        // addConfiguration("Alarm_SensorEndOfLifeAproaching",
        // CGMSBaseDataType.Alarm,
        // CGMSAlarms.SensorEndOfLifeAproaching);
        // addConfiguration("Alarm_SensorEndOfLifeAproachingDescription",
        // CGMSBaseDataType.Alarm,
        // CGMSAlarms.SensorEndOfLifeAproachingDescription);
        // addConfiguration("Alarm_SensorError", CGMSBaseDataType.Alarm,
        // CGMSAlarms.SensorError);
        // addConfiguration("Alarm_SensorChangeRequired",
        // CGMSBaseDataType.Alarm, CGMSAlarms.SensorChangeRequired);
        // addConfiguration("Alarm_SensorLost", CGMSBaseDataType.Alarm,
        // CGMSAlarms.SensorLost);
        // addConfiguration("Alarm_HighGlucosePredicted",
        // CGMSBaseDataType.Alarm, CGMSAlarms.HighGlucosePredicted);
        // addConfiguration("Alarm_LowGlucosePredicted",
        // CGMSBaseDataType.Alarm, CGMSAlarms.LowGlucosePredicted);
        // addConfiguration("Alarm_HighGlucose", CGMSBaseDataType.Alarm,
        // CGMSAlarms.HighGlucose);
        // addConfiguration("Alarm_LowGlucose", CGMSBaseDataType.Alarm,
        // CGMSAlarms.LowGlucose);
        //
        // addConfiguration("Alarm_SessionExpired",
        // CGMSBaseDataType.Alarm, CGMSAlarms.SessionExpired);
        // addConfiguration("Alarm_CGMSFailure", CGMSBaseDataType.Alarm,
        // CGMSAlarms.CGMSFailure);
        // addConfiguration("Alarm_SessionStopped",
        // CGMSBaseDataType.Alarm, CGMSAlarms.SessionStopped);
        // addConfiguration("Alarm_GlucoseRiseRateTooHigh",
        // CGMSBaseDataType.Alarm,
        // CGMSAlarms.GlucoseRiseRateTooHigh);
        // addConfiguration("Alarm_GlucoseFallRateTooHigh",
        // CGMSBaseDataType.Alarm,
        // CGMSAlarms.GlucoseFallRateTooHigh);
        // addConfiguration("Alarm_UnknownAlarm", CGMSBaseDataType.Alarm,
        // CGMSAlarms.UnknownAlarm);

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

        // addConfiguration("Event_ControllerPowerDown",
        // CGMSBaseDataType.Event, CGMSEvents.ControllerPowerDown);
        // addConfiguration("Event_ControllerPowerUp",
        // CGMSBaseDataType.Event, CGMSEvents.ControllerPowerUp);
        // addConfiguration("Event_DateTimeSet", CGMSBaseDataType.Event,
        // CGMSEvents.DateTimeSet);
        // addConfiguration("Event_DateTimeChanged",
        // CGMSBaseDataType.Event, CGMSEvents.DateTimeChanged);
        //
        // addConfiguration("Event_SensorConnectionLost",
        // CGMSBaseDataType.Event, CGMSEvents.SensorConnectionLost);
        // addConfiguration("Event_SensorStart", CGMSBaseDataType.Event,
        // CGMSEvents.SensorStart);
        // addConfiguration("Event_SensorStop", CGMSBaseDataType.Event,
        // CGMSEvents.SensorStop);
        // addConfiguration("Event_SensorCalibrationWithMeter",
        // CGMSBaseDataType.Event,
        // CGMSEvents.SensorCalibrationWithMeter);
        // addConfiguration("Event_SensorWaitingForCalibration",
        // CGMSBaseDataType.Event,
        // CGMSEvents.SensorWaitingForCalibration);
        // addConfiguration("Event_SensorSetCalibrationFactor",
        // CGMSBaseDataType.Event,
        // CGMSEvents.SensorSetCalibrationFactor);
        // addConfiguration("Event_SensorPreInit", CGMSBaseDataType.Event,
        // CGMSEvents.SensorPreInit);
        // addConfiguration("Event_SensorInit", CGMSBaseDataType.Event,
        // CGMSEvents.SensorInit);
        // addConfiguration("Event_SensorBurst", CGMSBaseDataType.Event,
        // CGMSEvents.SensorBurst);
        // addConfiguration("Event_SensorWeakSignal",
        // CGMSBaseDataType.Event, CGMSEvents.SensorWeakSignal);
        //
        // addConfiguration("Event_SensorDataLowBg",
        // CGMSBaseDataType.Event, CGMSEvents.SensorDataLowBg);
        // addConfiguration("Event_SensorCalibrationWithMeterNow",
        // CGMSBaseDataType.Event,
        // CGMSEvents.SensorCalibrationWithMeterNow);

    }


    private void addConfiguration(String key, CGMSBaseDataType baseDataType, CodeEnum subDataType)
    {
        this.put(key, new CGMSTempValues(CGMSObject.SubEntry, baseDataType, subDataType));
    }


    // public boolean writeObject(String _type, ATechDate _datetime, String
    // _value)
    // {
    // return this.writeObject(_type, _datetime, _value,
    // StringUtils.isNotBlank(_value));
    // }
    //
    //
    // public boolean writeObject(String _type, ATechDate _datetime)
    // {
    // return this.writeObject(_type, _datetime, null, false);
    // }
    //
    //
    // /**
    // * Write Object
    // * @param _type
    // * @param _datetime
    // * @param _value
    // * @return
    // */
    // public boolean writeObject(String _type, ATechDate _datetime, String
    // _value, boolean isNumericValue)
    // {
    // return this.cgmsDataWriter.writeObject(_type, _datetime, _value,
    // isNumericValue);
    // }
    //
    //
    // /**
    // * Write Object
    // * @param _type
    // * @param _datetime
    // * @param _value
    // * @return
    // */
    // public boolean writeObject(String _type, ATechDate _datetime, Number
    // _value)
    // {
    // return cgmsDataWriter.writeObject(_type, _datetime, _value);
    // }
    //
    //
    // /**
    // * Write Object
    // *
    // * @param _type
    // * @param _datetime
    // * @param code_type
    // * @param _value
    // * @return
    // */
    // public boolean writeObject(String _type, ATechDate _datetime, int
    // code_type, String _value)
    // {
    // return this.cgmsDataWriter.writeObject(_type, _datetime, _value);
    // }

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
