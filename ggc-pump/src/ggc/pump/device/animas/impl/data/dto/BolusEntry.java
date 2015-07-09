package ggc.pump.device.animas.impl.data.dto;

import java.math.BigDecimal;

import com.atech.utils.data.ATechDate;

import ggc.core.util.DataAccess;
import ggc.plugin.device.impl.animas.data.AnimasDeviceReplyPacket;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.pump.device.animas.impl.converter.AnimasBaseDataV2Converter;
import ggc.pump.device.animas.impl.data.AnimasPumpDeviceData;

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
 *  Filename:     BolusEntry
 *  Description:  Bolus Entry
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class BolusEntry
{

    public ATechDate dateTime;
    public short bolusType;
    public String bolusTypeDescription;
    public short bolusTypeAnimas;
    public int bolusSubType;

    public int bolusRecordType;
    public String bolusRecordTypeDescription;
    public BigDecimal amount;
    public BigDecimal requestedAmount;
    public BigDecimal duration;

    public short syncCounter;

    public String bolusGGC;

    public boolean bolusRecordTypeAllowedType = false;


    public void prepareEntry()
    {

        switch (bolusType)
        {

        // types> Noraml, ezCarb, ezBg, Combo Bolus
            case 0: // Normal
                switch (bolusSubType)
                {
                    case 1:
                        bolusTypeDescription = "Normal_Bolus";
                        bolusGGC = "Bolus_Normal";
                        bolusTypeAnimas = 1;
                        break;

                    case 2:
                        bolusTypeDescription = "Audio_Bolus";
                        bolusGGC = "Bolus_Audio";
                        bolusTypeAnimas = 2;
                        break;

                    case 3:
                        bolusTypeDescription = "Extended_Bolus";
                        bolusGGC = "Bolus_Extended";
                        bolusTypeAnimas = 3;
                        break;

                    default:
                        bolusTypeDescription = "Other_Bolus";
                        bolusGGC = "Bolus_Normal";
                        bolusTypeAnimas = 0;
                }
                break;

            case 1:
                bolusTypeDescription = "EzBG_Bolus";
                bolusGGC = "Bolus_Normal";
                bolusTypeAnimas = 4;
                break;

            case 2:
            case 3:
                switch (bolusSubType)
                {
                    case 1:
                        bolusTypeDescription = "CarbSmart_N_Bolus";
                        bolusGGC = "Bolus_Normal";
                        bolusTypeAnimas = 5;
                        break;

                    case 3:
                        bolusTypeDescription = "CarbSmart_E_Bolus";
                        bolusGGC = "Bolus_Extended";
                        bolusTypeAnimas = 6;
                        break;

                    default:
                        bolusTypeDescription = "CarbSmart_Bolus";
                        bolusGGC = "Bolus_Normal";
                        bolusTypeAnimas = 5;
                }
                break;
        }

        bolusRecordTypeAllowedType = false;

        switch (bolusRecordType)
        {
            case 1:
                bolusRecordTypeDescription = "Active Bolus";
                break;

            case 2:
                bolusRecordTypeDescription = "Bolus_Cancelled_Result";
                bolusRecordTypeAllowedType = true;
                break;

            case 0:
            case 3:
                bolusRecordTypeDescription = "Completed Bolus (" + bolusRecordType + ")";
                bolusRecordTypeAllowedType = true;
                break;

            default:
                bolusRecordTypeDescription = "?? Status";
        }

    }


    public float getValue()
    {
        if (amount.floatValue() == requestedAmount.floatValue())
        {
            return amount.floatValue();
        }
        else
        {
            return requestedAmount.floatValue();
        }
    }


    public String getValueAsString()
    {
        float val = getValue();

        return AnimasUtils.getDecimalValueString(val, 5, 3);
    }


    public String getValueForPreparedDataEntry()
    {
        if (this.bolusGGC.equals("Bolus_Extended"))
        {
            return String.format("AMOUNT_SQUARE=%s;DURATION=%s", this.getValueAsString(), getTime());
        }
        else
        {
            return this.getValueAsString();
        }
    }


    private String getTime()
    {
        int minutes = this.duration.intValue();

        int h = minutes / 60;

        minutes -= (h * 60);

        return DataAccess.getLeadingZero(h, 2) + ":" + DataAccess.getLeadingZero(minutes, 2);
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("BolusEntry [dateTime=" + this.dateTime.getDateTimeString());

        sb.append(String.format(", bolusType=%s, bolusTypeDescription=%s, bolusTypeAnimas=%s, bolusSubType=%s",
            bolusType, bolusTypeDescription, bolusTypeAnimas, bolusSubType));

        sb.append(String.format(", bolusRecordType=%s, bolusRecordTypeDescription=%s", bolusRecordType,
            bolusRecordTypeDescription));

        if (requestedAmount.floatValue() == amount.floatValue())
        {
            sb.append(String.format(", Amount=%6.4f U", amount.floatValue()));
        }
        else
        {
            sb.append(String.format(", Requested_Amount=%6.4f U, Amount=%6.4f U", requestedAmount.floatValue(),
                amount.floatValue()));
        }

        if (duration.intValue() > 0)
        {
            // sb.append(String.format(", Duration=%6.4f min",
            // duration.intValue()));
            sb.append(String.format(", Duration=%s min", duration.intValue()));
        }

        if (syncCounter > 0)
        {
            sb.append(", SyncCounter=" + syncCounter);
        }

        sb.append("]");

        return sb.toString();

    }


    public void writeData(AnimasPumpDeviceData data)
    {
        data.writeData(this.bolusGGC, this.dateTime, getValueForPreparedDataEntry());
    }


    public void createPreparedData(AnimasDeviceReplyPacket packet, AnimasBaseDataV2Converter conv)
    {
        float value = getValue();

        if ((value > 0) && (isCorrectEntryValue()))
        {
            conv.writeDataInternal(this.bolusGGC, this.dateTime, getValueForPreparedDataEntry());
        }
    }


    public boolean isCorrectEntryValue()
    {
        return bolusRecordType != 0;
    }


    public static String decodeBolusAnimasV1(int bolusType)
    {

        switch (bolusType)
        {
            case 2:
                return "Bolus_Audio";

            case 3:
                return "Bolus_Extended";

            case 1:
            default:
                return "Bolus_Normal";

        }
    }
}
