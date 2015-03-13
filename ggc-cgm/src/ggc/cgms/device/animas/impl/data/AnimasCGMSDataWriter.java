package ggc.cgms.device.animas.impl.data;

import ggc.cgms.data.CGMSDataWriter;
import ggc.cgms.data.CGMSTempValues;
import ggc.cgms.data.defs.*;
import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.device.impl.animas.handler.AnimasDataWriter;

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
 *  Filename:     AnimasCGMSDataWriter
 *  Description:  Animas CGMS Data Writer
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AnimasCGMSDataWriter implements AnimasDataWriter
{
    //DeviceValuesWriter deviceValuesWriter;
    CGMSDataWriter cgmsDataWriter;
    OutputWriter outputWriter;

    public AnimasCGMSDataWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
        this.createDeviceValuesWriter(outputWriter);
    }

    public DeviceValuesWriter getDeviceValuesWriter()
    {
        return null;
    }

    public CGMSDataWriter getCGMSDataWriter()
    {
        return this.cgmsDataWriter;
    }

    public OutputWriter getOutputWriter()
    {
        return this.outputWriter;
    }

    public void createDeviceValuesWriter(OutputWriter ow)
    {
        this.cgmsDataWriter = new CGMSDataWriter(this.outputWriter); // isSilent

        // Sensor data
        this.cgmsDataWriter.put("SensorReading", new CGMSTempValues(CGMSObject.SubEntry,
                        CGMSBaseDataType.SensorReading));

        // Sensor Events
        this.cgmsDataWriter.put("TransmiterEvent_1", new CGMSTempValues(CGMSObject.SubEntry,
                CGMSBaseDataType.TransmiterEvent, CGMSTransmiterEvents.SensorNotActive));

        this.cgmsDataWriter.put("TransmiterEvent_2", new CGMSTempValues(CGMSObject.SubEntry,
                CGMSBaseDataType.TransmiterEvent, CGMSTransmiterEvents.SensorNotActive));

        this.cgmsDataWriter.put("TransmiterEvent_3", new CGMSTempValues(CGMSObject.SubEntry,
                CGMSBaseDataType.TransmiterEvent, CGMSTransmiterEvents.SensorNotActive));

        this.cgmsDataWriter.put("TransmiterEvent_1", new CGMSTempValues(CGMSObject.SubEntry,
                CGMSBaseDataType.TransmiterEvent, CGMSTransmiterEvents.SensorNotActive));

        this.cgmsDataWriter.put("TransmiterEvent_1", new CGMSTempValues(CGMSObject.SubEntry,
                CGMSBaseDataType.TransmiterEvent, CGMSTransmiterEvents.SensorNotActive));


        // EventTransmiter_
        //CGMSErrors.

//        SpecialGlucoseValues
//
//        SensorNotActive(1), //
//                MinimallyEGVAberration(2), //
//                NoAntenna(3), //
//                SensorOutOfCal(5), //
//                CountsAberration(6), //
//                AbsoluteAberration(9), //
//                PowerAberration(10), //
//                RFBadStatus(12);

        // Events
        //this.deviceValuesWriter.put("SensorReading", new CGMSTempValues(CGMSObject.SubEntry, //
        //        CGMSBaseDataType.SensorReading));

//        this.deviceValuesWriter.put("Event_Basal_Run", new CGMSTempValues(PumeadingpTempValues.OBJECT_BASE, //
//                PumpBaseType.Event, PumpEvents.BasalRun, false));
//
//        // Alarms
//        this.deviceValuesWriter.put("Alarm_Call_Service", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.CallService, false));
//
//        this.deviceValuesWriter.put("Alarm_Replace_Battery", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.BatteryReplace, false));
//
//        this.deviceValuesWriter.put("Alarm_Empty_Cartridge", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.EmptyCartridge, false));
//
//        this.deviceValuesWriter.put("Alarm_Auto_Off", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.AutoOff, false));
//
//        this.deviceValuesWriter.put("Alarm_Low_Battery", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.BatteryLow, false));
//
//        this.deviceValuesWriter.put("Alarm_Low_Cartridge", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.CartridgeLow, false));
//
//        this.deviceValuesWriter.put("Alarm_Other", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.UnknownAlarm, false));
//
//        this.deviceValuesWriter.put("Alarm_Occlusion_Detected", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Alarm, PumpAlarms.NoDelivery, false));
//
//        // TDD
//        this.deviceValuesWriter.put("TDD_All_Insulin", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Report, PumpReport.InsulinTotalDay, true));
//
//        this.deviceValuesWriter.put("TDD_Basal_Insulin", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Report, PumpReport.BasalTotalDay, true));
//
//        // Prime
//        this.deviceValuesWriter.put("Event_PrimeInfusionSet", new CGMSTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Event, PumpEvents.PrimeInfusionSet, true));
//
//        this.deviceValuesWriter.put("Event_FillCannula", new CGMSTempValues(CGPumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Event, PumpEvents.FillCannula, true));
//
//        // Bolus
//        this.deviceValuesWriter.put("Bolus_Normal", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Bolus, PumpBolusType.Normal, true));
//
//        this.deviceValuesWriter.put("Bolus_Audio", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Bolus, PumpBolusType.Audio, true));
//
//        this.deviceValuesWriter.put("Bolus_Extended", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Bolus, PumpBolusType.Extended, false));
//
//        // Basal
//        this.deviceValuesWriter.put("Basal_Value_Change", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Basal, PumpBasalSubType.ValueChange, true));
//
//        // Bolus Extended
//        this.deviceValuesWriter.put("Additional_BG", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.AdditionalData, PumpAdditionalDataType.BloodGlucose, true));
//
//        this.deviceValuesWriter.put("Additional_CH", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.AdditionalData, PumpAdditionalDataType.Carbohydrates, true));
//
//        this.deviceValuesWriter.put("BolusWizard", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
//                PumpBaseType.Event, PumpEvents.BolusWizard, false));

    }

}
