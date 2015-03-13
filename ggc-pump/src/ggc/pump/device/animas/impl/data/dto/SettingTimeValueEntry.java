package ggc.pump.device.animas.impl.data.dto;


import com.atech.i18n.I18nControlAbstract;
import ggc.pump.device.animas.impl.data.enums.AnimasBolusSettingSubType;
import ggc.pump.util.DataAccessPump;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

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
 *  Filename:     SettingTimeValueEntry
 *  Description:  Setting Time Value Entry
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class SettingTimeValueEntry
{
    Log LOG = LogFactory.getLog(SettingTimeValueEntry.class);

    AnimasBolusSettingSubType type;
    ATechDate time;
    int index;

    short valueAmountShort;
    float valueAmountFloat;
    Short valueDeltaShort;
    Float valueDeltaFloat;

    boolean valueTypeShort = true;
    boolean deltaSet = false;

    public SettingTimeValueEntry(AnimasBolusSettingSubType type, int index, ATechDate time, short value)
    {
        this.setBaseData(type, index, time);
        setValuesShort(value, null);
    }

    public SettingTimeValueEntry(AnimasBolusSettingSubType type, int index, ATechDate time, float value)
    {
        this.setBaseData(type, index, time);
        setValuesFloat(value, null);
    }

    public SettingTimeValueEntry(AnimasBolusSettingSubType type, int index, ATechDate time, short value, short delta)
    {
        this.setBaseData(type, index, time);
        setValuesShort(value, delta);
    }

    public SettingTimeValueEntry(AnimasBolusSettingSubType type, int index, ATechDate time, float value, float delta)
    {
        this.setBaseData(type, index, time);
        setValuesFloat(value, delta);
    }

    private void setBaseData(AnimasBolusSettingSubType type, int index, ATechDate time)
    {
        this.type = type;
        this.index = index;
        this.time = time;
    }

    private void setValuesShort(short value, Short delta)
    {
        this.valueTypeShort = true;
        this.valueAmountShort = value;
        this.valueDeltaShort = delta;
        this.deltaSet = (this.valueDeltaShort != null);
    }

    private void setValuesFloat(float value, Float delta)
    {
        this.valueTypeShort = false;
        this.valueAmountFloat = value;
        this.valueDeltaFloat = delta;
        this.deltaSet = (this.valueDeltaFloat != null);
    }

    static String templateNoDeltaMain; // = "From=%s,Amount=<ValueFormat>";
    static String templateDeltaMain; // = "From=%s,Amount=<ValueFormat>,Delta=<ValueFormat>";

    private void setTranslations()
    {
        if (templateNoDeltaMain==null)
        {
            I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

            templateNoDeltaMain = ic.getMessage("CFG_BASE_FROM") + "=%s, " + ic.getMessage("CFG_BASE_AMOUNT") + "=<ValueFormat>";
            templateDeltaMain = ic.getMessage("CFG_BASE_FROM") + "=%s, " + ic.getMessage("CFG_BASE_AMOUNT")
                    + "=<ValueFormat>, " + ic.getMessage("CFG_BASE_DELTA") + "=<ValueFormat>";


        }
    }



    // FIXME use i18nControl
    public String getSettingValue()
    {
        setTranslations();

        //String templateNoDelta = "From=%s,Amount=<ValueFormat>";
        //String templateDelta = "From=%s,Amount=<ValueFormat>,Delta=<ValueFormat>";

        String template = this.deltaSet ? templateDeltaMain : templateNoDeltaMain;

        if (this.valueTypeShort)
        {
            template = template.replace("<ValueFormat>", "%d");
            //LOG.debug("Template: " + template);

            if (valueDeltaShort == null)
            {
                return String.format(template, time.getTimeString(), valueAmountShort);
            }
            else
            {
                return String.format(template, time.getTimeString(), valueAmountShort, valueDeltaShort);
            }
        }
        else
        {
            template = template.replace("<ValueFormat>", "%6.4f");
            //LOG.debug("Template: " + template);

            if (valueDeltaShort == null)
            {
                return String.format(template, time.getTimeString(), valueAmountFloat);
            }
            else
            {
                return String.format(template, time.getTimeString(), valueAmountFloat, valueDeltaFloat);
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SettingTimeValueEntry [");
        sb.append("type=" + type.name());
        sb.append(",time=" + time.getTimeString());
        sb.append(",index=" + index);

        if (this.valueTypeShort)
        {
            sb.append(",amount=" + valueAmountShort);
            if (valueDeltaShort != null)
            {
                sb.append(",delta=" + valueDeltaShort);
            }
        }
        else
        {
            sb.append(String.format(",amount=%6.4f", valueAmountFloat));
            if (valueDeltaShort != null)
            {
                sb.append(String.format(",deltea=%6.4f", valueDeltaFloat));
            }
        }

        return sb.toString();
    }

}
