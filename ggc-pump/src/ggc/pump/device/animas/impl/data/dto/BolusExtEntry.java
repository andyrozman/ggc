package ggc.pump.device.animas.impl.data.dto;

import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.pump.device.animas.impl.converter.AnimasBaseDataV2Converter;
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
 *  Filename:     BolusExtEntry
 *  Description:  Bolus Extended Entry
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class BolusExtEntry
{
    public short syncRecordId;
    public short i2C;
    public int carbs;
    public int bg;
    public int isf;
    public int bgTarget;
    public int bgDelta;
    public boolean iobEnabled;
    public float iobValue;
    public boolean bgCorrection;
    public float iobDuration;
    public BolusEntry bolusEntry;

    static DataAccessPump dataAccessPump = DataAccessPump.getInstance();


    public void createPreparedData(AnimasDevicePacket packet, AnimasBaseDataV2Converter conv)
    {
        if (this.bg >0)
        {
            conv.writeDataInternal(packet, "AdditionalData_BG", bolusEntry.dateTime, //
                    AnimasUtils.getDecimalValueString(this.bg, 4, 2));
        }

        if (this.carbs >0)
        {
            conv.writeDataInternal(packet, "AdditionalData_CH", bolusEntry.dateTime, //
                    "" + this.carbs);
        }


        float bolus = this.bolusEntry.getValue();
        float bgCorrIns = calculateBGCorrectionInsulin();

        String bolusWizard = "BG=" + this.bg +
                ";CH=" + this.carbs +
                ";CH_UNIT=g" +
                ";CH_INS_RATIO=" + this.i2C +
                ";BG_INS_RATIO=" + this.isf +
                ";BG_TARGET_LOW=" + (this.bgTarget - this.bgDelta) +
                ";BG_TARGET_HIGH=" + (this.bgTarget + this.bgDelta) +
                ";BOLUS_TOTAL=" + AnimasUtils.getDecimalValueString(bolus, 4, 2) +
                ";BOLUS_CORRECTION=" + AnimasUtils.getDecimalValueString(bgCorrIns, 4, 2) +
                ";BOLUS_FOOD=" + AnimasUtils.getDecimalValueString(bolus-bgCorrIns, 4, 2) +
                ";UNABSORBED_INSULIN=" + AnimasUtils.getDecimalValueString(this.iobValue, 4, 3);

        conv.writeDataInternal(packet, "Event_BolusWizard", bolusEntry.dateTime, bolusWizard);

    }

    private float calculateBGCorrectionInsulin()
    {
        if (!bgCorrection)
        {
            return 0.0f;
        }
        else
        {

            float bgLimit = this.bg - this.bgTarget;

            if (bgLimit < 0)
            {
                return 0.0f;
            }
            else
            {
                float corr = bgLimit / (this.isf * 1.0f);
                return dataAccessPump.getFloatValueFromString(dataAccessPump.getDecimalHandler().getDecimalNumberAsString(corr, 2));
            }
        }

    }


    public String toStringFull()
    {
        return String.format(
                "BolusExtEntry [syncRecordId=%s, i2C=%s, carbs=%s, bg=%s, isf=%s" +
                        ", bgTarget=%s, bgDelta=%s, iobEnabled=%s, iobValue=%s, bgCorrection=%s, IOBDuaration=%s," +
                        "BolusEntry=%s]",
                syncRecordId, i2C, carbs, bg, isf, bgTarget, bgDelta, iobEnabled, iobValue, bgCorrection, iobDuration, bolusEntry.toString());
    }


    public String toString()
    {
        return String.format(
                "BolusExtEntry [syncRecordId=%s, i2C=%s, carbs=%s, bg=%s, isf=%s" +
                ", bgTarget=%s, bgDelta=%s, iobEnabled=%s, iobValue=%s, bgCorrection=%s, IOBDuaration=%s" +
                "]",
                syncRecordId, i2C, carbs, bg, isf, bgTarget, bgDelta, iobEnabled, iobValue, bgCorrection, iobDuration);
    }


}
