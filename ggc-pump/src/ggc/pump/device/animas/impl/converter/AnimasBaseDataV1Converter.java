package ggc.pump.device.animas.impl.converter;

import com.atech.utils.data.ATechDate;
import ggc.plugin.data.enums.ClockModeType;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.pump.device.animas.impl.data.AnimasPumpDeviceData;
import ggc.pump.device.animas.impl.data.dto.BasalProfileEntry;
import ggc.pump.device.animas.impl.data.dto.BolusEntry;
import ggc.pump.device.animas.impl.data.dto.PumpSettings;
import ggc.pump.device.animas.impl.handler.v1.AnimasV1CommandType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
 *  Filename:     AnimasBaseDataV1Converter
 *  Description:  Converter for basic Animas Data for Implementation V1
 *        NOT FULLY IMPLEMENTED, IT PROBABLY WON'T BE EVER
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasBaseDataV1Converter
{

    public static final Log LOG = LogFactory.getLog(AnimasBaseDataV1Converter.class);
    AnimasPumpDeviceData data;

    HashMap<String, BigDecimal> bigDecimals = new HashMap<String, BigDecimal>();
    AnimasDeviceReader deviceReader;
    private boolean inDataProcessingPacket = false;

    public AnimasBaseDataV1Converter(AnimasDeviceReader deviceReader, AnimasDeviceData data)
    {
        this.deviceReader = deviceReader;

        this.data = (AnimasPumpDeviceData)data;
    }

    public AnimasDeviceData getData()
    {
        return this.data;
    }


    public void processReturnedRawData(AnimasDevicePacket animasDevicePacket)
    {

    }


    public void processRawData(AnimasV1CommandType cmdType, String[] rawData)
    {
        switch (cmdType)
        {
            case PumpInfo:
                decodeSettings(rawData);
                break;

            case DailyTotalsLog:
                decodeDailyTotals(rawData);
                break;

            case AlarmLog:
                this.decodeAlarmLog(rawData);
                break;

            case BolusLog:
                this.decodeBolusLog(rawData);
                break;

            default:
                LOG.warn("Processing for this dataType is not possible !");
        }
    }



    private void decodeSettings(String[] rawData)
    {
        //HashMap<String,String> settingsMap = new HashMap<String, String>();

        //AnimasUtils.wasDataTypeProcessed(AnimasDataType.AdvancedSettings);

        PumpSettings pumpSettings = this.data.pumpSettings;

        int sn1 = 0;
        int sn2 = 0;
        int data2 = 0;
        //int basalNrOffset = 0;
        int hour = 0;
        int minute = 0;
        //double amount = 0.0D;
        int[] mem0 = new int['á'];
        int[] mem1 = new int['á'];
        int[] mem3 = new int['á'];

        for (int i = 0; i < 224; i++)
        {
            mem0[(i + 1)] = Integer.parseInt("" + rawData[i].charAt(3) + rawData[i].charAt(4), 16);
            mem1[(i + 1)] = Integer.parseInt("" + rawData[i].charAt(5) + rawData[i].charAt(6), 16);
            mem3[(i + 1)] = Integer.parseInt("" + rawData[i].charAt(7) + rawData[i].charAt(8), 16);
            //incProgressBar(this.dldProgBar);
        }


        sn1 = mem3[(Integer.parseInt("20", 16) - 31)];
        sn2 = mem3[(Integer.parseInt("21", 16) - 31)];

        if (sn1 >= 128)
        {
            sn1 -= 128;
        }

        // FIXME settings

//        String serialNumber = String.valueOf((sn1 * 256) + sn2);
//        settingsMap.put("PUMP_SERIAL_NO", serialNumber);
//        //setLabelText(this.serialNumberLabel, serialNumber);
//
//        settingsMap.put("ADVMAXTDD", String.valueOf(mem0[118] * 5));
//        settingsMap.put("ADVMAXBASAL", String.valueOf(mem0[119] * 0.05D));
//        settingsMap.put("ADVMAXBOLUS", String.valueOf(mem0[120] * 0.1D));
//

        pumpSettings.autoOffEnabled = getBooleanValue(mem0[97]);
        pumpSettings.autoOffTimeoutHr = (short)mem0[98];

        pumpSettings.audioBolusEnabled = getBooleanValue(mem0[110]);

//        settingsMap.put("ADVAUDIOBOLSTEP", String.valueOf(0.5D + (0.5D * mem0[109])));

        pumpSettings.advancedBolusEnabled = getBooleanValue(mem0[111]);



//        if (mem0[99] == 1)
//        {
//            settingsMap.put("ADVCARTWARNLVL", "1");
//        }
//        else
//        {
//            settingsMap.put("ADVCARTWARNLVL", "0");
//        }
//        if (mem0[121] == 1)
//        {
//            settingsMap.put("ADVOCCLLIMITS", "1");
//        }
//        else
//        {
//            settingsMap.put("ADVOCCLLIMITS", "0");
//        }


        pumpSettings.clockMode = ClockModeType.getByCode(mem0[103]);

        this.data.setSoundVolume(AnimasSoundType.TempBasal, mem0[108] == 1 ? SoundValueType.Enabled : SoundValueType.Disabled);

        this.data.setSoundVolume(AnimasSoundType.Normal, mem0[114] == 1 ? SoundValueType.Enabled : SoundValueType.Disabled);



        for (int i = 0; i < 4; i++)
        {
            data.setBasalName((i + 1), "Basal_Program_Name" + " " + String.valueOf(i + 1));
        }



        int basalNrOffset = 0;
        float amount = 0.0f;

        for(int j=0; j<4; j++)
        {
            basalNrOffset = j * 25;
            data2 = mem1[(basalNrOffset + 2)];

            for (int i = 1; i <= data2; i++)
            {
                ATechDate time = AnimasUtils.calculateTimeFromTimeSet(mem1[(basalNrOffset + (i * 2) + 1)]);

                amount = mem1[(basalNrOffset + ((i + 1) * 2))] * 0.05f;

                data.addBasalProfileEntry((j+1), new BasalProfileEntry(time, amount));
            }
        }
    }


    protected Boolean getBooleanValue(int s)
    {
        return (s == 1) ? Boolean.TRUE : Boolean.FALSE;
    }

    private void decodeDailyTotals(String[] rawData)
    {
        int basal1 = 0;
        int basal2 = 0;
        int all1 = 0;
        int all2 = 0;
        double basal= 0.0D;
        double tdd = 0.0D;
        for (int i = 0; i < 256; i++)
        {

            if ((rawData[i].length() <= 8) || (!checkIfTimeValid(rawData[i], 1, 3, 5)))
            {
                continue;
            }

            ATechDate dateTime = getATDateTime(rawData[i], false);

            basal1 = Integer.parseInt("" + rawData[i].charAt(9) + rawData[i].charAt(10), 16);
            basal2 = Integer.parseInt("" + rawData[i].charAt(11) + rawData[i].charAt(12), 16);
            all1 = Integer.parseInt("" + rawData[i].charAt(13) + rawData[i].charAt(14), 16);
            all2 = Integer.parseInt("" + rawData[i].charAt(15) + rawData[i].charAt(16), 16);

            basal = basal2 + (0.005D * basal1);
            tdd = all2 + (0.005D * all1);

            data.writeData("TDD_All_Insulin", dateTime, String.format("%4.3f", tdd));
            data.writeData("TDD_Basal_Insulin", dateTime, String.format("%4.3f", basal));
        }

    }


    private void decodeAlarmLog(String[] rawData)
    {
        for (int i = 0; i < 256; i++)
        {
            if (rawData[i].length() > 8)
            {
                data.writeData("Alarm_Other", getATDateTime(rawData[i], true), //
                        "" + getInt(rawData[i].charAt(13), rawData[i].charAt(14)));
            }
        }
    }


    private void decodeBolusLog(String[] rawData)
    {
        int bolusRawValue = 0;
        int bolusType = 0;

        for (int i = 0; i < 256; i++)
        {
            if ((rawData[i].length() <= 8) || (!checkIfTimeValid(rawData[i], 0, 0, 0)))
            {
                continue;
            }

            bolusRawValue = getInt(rawData[i].charAt(13), rawData[i].charAt(14));
            bolusType = getInt(rawData[i].charAt(15), rawData[i].charAt(16));

            float bolusVal = bolusRawValue / 100.0f;

            data.writeData(BolusEntry.decodeBolusAnimasV1(bolusType), //
                    getATDateTime(rawData[i], true), //
                    AnimasUtils.getDecimalValueString(bolusVal, 5, 3));
        }
    }


    private int getInt(char element1, char element2)
    {
        return Integer.parseInt("" + element1 + element1, 16);
    }


    private boolean checkIfTimeValid(String data, int yearCheck, int monthCheck, int dayCheck)
    {
        int year = getInt(data.charAt(3), data.charAt(4));
        int month = getInt(data.charAt(5), data.charAt(6));
        int day =  getInt(data.charAt(7), data.charAt(8));

        return ((year != 1) && (month != 3) && (day != 5));
    }


    private ATechDate getATDateTime(String data, boolean withTime)
    {
        if (withTime)
        {
            return new ATechDate( //
                    getInt(data.charAt(7), data.charAt(8)), //
                    getInt(data.charAt(5), data.charAt(6)), //
                    getInt(data.charAt(3), data.charAt(4)) + 2000, //
                    getInt(data.charAt(9), data.charAt(10)), //
                    getInt(data.charAt(11), data.charAt(12)), //
                    ATechDate.FORMAT_DATE_AND_TIME_S);
        }
        else
        {
            return new ATechDate( //
                    getInt(data.charAt(7), data.charAt(8)), //
                    getInt(data.charAt(5), data.charAt(6)), //
                    getInt(data.charAt(3), data.charAt(4)) + 2000, //
                    0, //
                    0, //
                    ATechDate.FORMAT_DATE_AND_TIME_S);
        }

    }


    private ATechDate getATDateTime(int year, int month, int day, int hour, int minute)
    {
        return new ATechDate(day, month, year, hour, minute, ATechDate.FORMAT_DATE_AND_TIME_S);

    }

}
