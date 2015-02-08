package ggc.pump.device.animas.impl.handler;

import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.device.impl.animas.handler.AnimasDataWriter;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.PumpTempValues;
import ggc.pump.data.defs.*;


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
 *  Filename:     AnimasPumpDataWriter
 *  Description:  Animas Pump Data Writer
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasPumpDataWriter implements AnimasDataWriter
{
    DeviceValuesWriter deviceValuesWriter;

    public AnimasPumpDataWriter(OutputWriter ow)
    {
        this.createDeviceValuesWriter(ow);
    }

    public DeviceValuesWriter getDeviceValuesWriter()
    {
        return deviceValuesWriter;
    }

    public void createDeviceValuesWriter(OutputWriter ow)
    {
        this.deviceValuesWriter = new DeviceValuesWriter(false); // isSilent

        if (ow!=null)
        {
            this.deviceValuesWriter.setOutputWriter(ow);
        }
        else
        {
            this.deviceValuesWriter.setOutputWriter(new ConsoleOutputWriter()); // this.outputWriter);
        }


        // Events
        this.deviceValuesWriter.put("Event_Basal_Stop", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BasalStop, false));

        this.deviceValuesWriter.put("Event_Basal_Run", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BasalRun, false));

        // Alarms
        this.deviceValuesWriter.put("Alarm_Call_Service", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.CallService, false));

        this.deviceValuesWriter.put("Alarm_Replace_Battery", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.BatteryReplace, false));

        this.deviceValuesWriter.put("Alarm_Empty_Cartridge", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.EmptyCartridge, false));

        this.deviceValuesWriter.put("Alarm_Auto_Off", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.AutoOff, false));

        this.deviceValuesWriter.put("Alarm_Low_Battery", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.BatteryLow, false));

        this.deviceValuesWriter.put("Alarm_Low_Cartridge", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.CartridgeLow, false));

        this.deviceValuesWriter.put("Alarm_Other", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.UnknownAlarm, false));

        this.deviceValuesWriter.put("Alarm_Occlusion_Detected", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.NoDelivery, false));

        // TDD
        this.deviceValuesWriter.put("TDD_All_Insulin", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Report, PumpReport.InsulinTotalDay, true));

        this.deviceValuesWriter.put("TDD_Basal_Insulin", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Report, PumpReport.BasalTotalDay, true));

        // Prime
        this.deviceValuesWriter.put("Event_PrimeInfusionSet", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.PrimeInfusionSet, true));

        this.deviceValuesWriter.put("Event_FillCannula", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.FillCannula, true));

        // Bolus
        this.deviceValuesWriter.put("Bolus_Normal", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Bolus, PumpBolusType.Normal, true));

        this.deviceValuesWriter.put("Bolus_Audio", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Bolus, PumpBolusType.Audio, true));

        this.deviceValuesWriter.put("Bolus_Extended", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Bolus, PumpBolusType.Extended, false));

        // Bolus Extended
        this.deviceValuesWriter.put("Additional_BG", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.AdditionalData, PumpAdditionalDataType.BloodGlucose, true));

        this.deviceValuesWriter.put("Additional_CH", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.AdditionalData, PumpAdditionalDataType.Carbohydrates, true));

        this.deviceValuesWriter.put("BolusWizard", new PumpTempValues(PumpTempValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BolusWizard, false));

    }

}
