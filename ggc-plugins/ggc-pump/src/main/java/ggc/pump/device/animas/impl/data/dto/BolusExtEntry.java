package ggc.pump.device.animas.impl.data.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.device.impl.animas.data.AnimasDeviceReplyPacket;
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

    private static final Logger LOG = LoggerFactory.getLogger(BolusExtEntry.class);

    public short syncRecordId;
    public short i2C;
    public int carbs;
    public int bgOriginal;
    public int bg;
    public int isf;
    public int bgTarget;
    public int bgDelta;
    public boolean iobEnabled;
    public float iobValue;
    public boolean bgCorrection;
    public float iobDuration;
    public BolusEntry bolusEntry;

    boolean hasBg = false;

    static DataAccessPump dataAccessPump = DataAccessPump.getInstance();


    public void createPreparedData(AnimasDeviceReplyPacket packet, AnimasBaseDataV2Converter conv)
    {

        if (this.carbs > 0)
        {
            conv.writeDataInternal("AdditionalData_CH", bolusEntry.dateTime, //
                "" + this.carbs);
        }

        float bolus = this.bolusEntry.getValue();
        // float bgEntry = 0.0f;

        hasBg = determineBg();

        if (hasBg)
        {
            bg = bgOriginal;
        }

        float bgCorrIns = calculateBGCorrectionInsulin();

        String bolusWizard = "BG=" + this.bg + ";CH=" + this.carbs + ";CH_UNIT=g" + ";CH_INS_RATIO=" + this.i2C
                + ";BG_INS_RATIO=" + this.isf + ";BG_TARGET_LOW=" + (this.bgTarget - this.bgDelta) + ";BG_TARGET_HIGH="
                + (this.bgTarget + this.bgDelta) + ";BOLUS_TOTAL=" + AnimasUtils.getDecimalValueString(bolus, 4, 2)
                + ";BOLUS_CORRECTION=" + AnimasUtils.getDecimalValueString(bgCorrIns, 4, 2) + ";BOLUS_FOOD="
                + AnimasUtils.getDecimalValueString(bolus - bgCorrIns, 4, 2) + ";UNABSORBED_INSULIN="
                + AnimasUtils.getDecimalValueString(this.iobValue, 4, 3);

        if (this.bg > 0)
        {
            conv.writeDataInternal("AdditionalData_BG", bolusEntry.dateTime, //
                AnimasUtils.getDecimalValueString(this.bg, 4, 2));
        }

        conv.writeDataInternal("Event_BolusWizard", bolusEntry.dateTime, bolusWizard);

        // LOG.debug(toStringFull());
    }


    private boolean determineBg()
    {
        if (this.bgOriginal > 0)
        {
            if (bolusEntry.bolusTypeAnimas == 4)
            {
                return true;
            }
            else if ((bolusEntry.bolusTypeAnimas == 5) || (bolusEntry.bolusTypeAnimas == 6))
            {
                return this.bgCorrection;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }


    private float calculateBGCorrectionInsulin()
    {
        if (!bgCorrection)
        {
            return 0.0f;
        }
        else
        {
            if (!hasBg)
            {
                return 0.0f;
            }

            float bgLimit = this.bg - this.bgTarget;

            if (bgLimit < 0)
            {
                return 0.0f;
            }
            else
            {
                checkDataAccessSet();

                float corr = bgLimit / (this.isf * 1.0f);
                return dataAccessPump
                        .getFloatValueFromString(dataAccessPump.getDecimalHandler().getDecimalNumberAsString(corr, 2));
            }
        }

    }


    private void checkDataAccessSet()
    {
        if (dataAccessPump == null)
        {
            dataAccessPump = DataAccessPump.getInstance();
        }
    }


    public String toStringFull()
    {
        return String.format(
            "BolusExtEntry [syncRecordId=%s, i2C=%s, carbs=%s, bg=%s (%s), isf=%s"
                    + ", bgTarget=%s, bgDelta=%s, iobEnabled=%s, iobValue=%s, bgCorrection=%s, IOBDuaration=%s,"
                    + "BolusEntry=%s]",
            syncRecordId, i2C, carbs, bg, bgOriginal, isf, bgTarget, bgDelta, iobEnabled, iobValue, bgCorrection,
            iobDuration, bolusEntry.toString());
    }


    public String toString()
    {
        return String.format(
            "BolusExtEntry [syncRecordId=%s, i2C=%s, carbs=%s, bg=%s (%s), isf=%s"
                    + ", bgTarget=%s, bgDelta=%s, iobEnabled=%s, iobValue=%s, bgCorrection=%s, IOBDuaration=%s" + "]",
            syncRecordId, i2C, carbs, bg, bgOriginal, isf, bgTarget, bgDelta, iobEnabled, iobValue, bgCorrection,
            iobDuration);
    }

}
