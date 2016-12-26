package ggc.pump.data.dto;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;

import ggc.plugin.data.enums.ValueType;
import ggc.pump.data.defs.RatioType;
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
 *  Filename:     SettingTimeValueEntry
 *  Description:  Setting Time Value Entry
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class RatioDTO
{

    RatioType type;
    ATechDate time;
    int index;

    short valueAmountShort;
    float valueAmountFloat;
    Short valueDeltaShort;
    Float valueDeltaFloat;

    boolean valueTypeShort = true;
    ValueType valueType;

    private String unitString = null;


    public RatioDTO(RatioType type, int index, ATechDate time, short value)
    {
        this.setBaseData(type, index, time);
        setValuesShort(value, null);
    }


    public RatioDTO(RatioType type, int index, ATechDate time, short value, String unit)
    {
        this.setBaseData(type, index, time);
        setValuesShort(value, null);
        this.unitString = unit;
    }


    public RatioDTO(RatioType type, int index, ATechDate time, float value)
    {
        this.setBaseData(type, index, time);
        setValuesFloat(value, null);
    }


    public RatioDTO(RatioType type, int index, ATechDate time, float value, String unit)
    {
        this.setBaseData(type, index, time);
        setValuesFloat(value, null);
        this.unitString = unit;

    }


    public RatioDTO(RatioType type, int index, ATechDate time, short value, short delta)
    {
        this.setBaseData(type, index, time);
        setValuesShort(value, delta);
    }


    public RatioDTO(RatioType type, int index, ATechDate time, short value, short delta, String unit)
    {
        this.setBaseData(type, index, time);
        setValuesShort(value, delta);
        this.unitString = unit;

    }


    public RatioDTO(RatioType type, int index, ATechDate time, float value, float delta)
    {
        this.setBaseData(type, index, time);
        setValuesFloat(value, delta);
    }


    public RatioDTO(RatioType type, int index, ATechDate time, float value, float delta, String unit)
    {
        this.setBaseData(type, index, time);
        setValuesFloat(value, delta);
        this.unitString = unit;

    }


    private void setBaseData(RatioType type, int index, ATechDate time)
    {
        this.type = type;
        this.index = index;
        this.time = time;
        this.valueType = type.getValueType();
    }


    private void setValuesShort(short value, Short delta)
    {
        this.valueTypeShort = true;
        this.valueAmountShort = value;
        this.valueDeltaShort = delta;
        if (this.valueDeltaShort == null)
            this.valueType = ValueType.Simple;
    }


    private void setValuesFloat(float value, Float delta)
    {
        this.valueTypeShort = false;
        this.valueAmountFloat = value;
        this.valueDeltaFloat = delta;

        if (this.valueDeltaFloat == null)
            this.valueType = ValueType.Simple;
    }

    static String templateNoDeltaMain;
    static String templateDeltaMain;


    private void setTranslations()
    {
        if (templateNoDeltaMain == null)
        {
            I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

            templateNoDeltaMain = ic.getMessage("CFG_BASE_FROM") + "=%s, " + ic.getMessage("CFG_BASE_AMOUNT")
                    + "=<ValueFormat>";
            templateDeltaMain = ic.getMessage("CFG_BASE_FROM") + "=%s, " + ic.getMessage("CFG_BASE_AMOUNT")
                    + "=<ValueFormat>, " + ic.getMessage("CFG_BASE_DELTA") + "=<ValueFormat>";
        }
    }


    public String getSettingValue()
    {
        setTranslations();

        String template = this.valueType == ValueType.Delta ? templateDeltaMain : templateNoDeltaMain;

        String value;

        if (this.valueTypeShort)
        {
            if (valueType == ValueType.Range)
                template = template.replace("<ValueFormat>", "%d - %d");
            else
                template = template.replace("<ValueFormat>", "%d");

            if (valueDeltaShort == null)
            {
                value = String.format(template, time.getTimeString(), valueAmountShort);
            }
            else
            {
                value = String.format(template, time.getTimeString(), valueAmountShort, valueDeltaShort);
            }
        }
        else
        {
            if (valueType == ValueType.Range)
                template = template.replace("<ValueFormat>", "%6.4f - %6.4f");
            else
                template = template.replace("<ValueFormat>", "%6.4f");

            if (valueDeltaFloat == null)
            {
                value = String.format(template, time.getTimeString(), valueAmountFloat);
            }
            else
            {
                value = String.format(template, time.getTimeString(), valueAmountFloat, valueDeltaFloat);
            }
        }

        if (this.unitString != null)
            value += " " + this.unitString;

        return value;
    }


    public int getIndex()
    {
        return this.index;
    }


    public RatioType getType()
    {
        return type;
    }


    public String getTimeAsString()
    {
        return time.getTimeString();
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
                sb.append(String.format(",delta=%6.4f", valueDeltaFloat));
            }
        }

        return sb.toString();
    }


    public String getUnitString()
    {
        return unitString;
    }


    public void setUnitString(String unitString)
    {
        this.unitString = unitString;
    }
}
