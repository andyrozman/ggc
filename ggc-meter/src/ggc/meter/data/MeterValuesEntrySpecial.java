package ggc.meter.data;

import ggc.core.data.ExtendedDailyValue;
import ggc.meter.util.DataAccessMeter;

import java.util.Hashtable;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:      MeterValuesEntrySpecial  
 *  Description:   Special Meter Values Entries
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterValuesEntrySpecial
{

    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
    public static final int SPECIAL_ENTRY_URINE_MMOLL = 1;

    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
    public static final int SPECIAL_ENTRY_URINE_MGDL = 2;

    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
    public static final int SPECIAL_ENTRY_URINE_DESCRIPTIVE = 3;

    /**
     * Special Entry: CH
     */
    public static final int SPECIAL_ENTRY_CH = 4;

    /**
     * Special Entry: Max
     */
    public static final int SPECIAL_ENTRY_MAX = 4;

    /**
     * Special Entry: BG
     */
    public static final int SPECIAL_ENTRY_BG = -2;

    /**
     * Special Entry: URINE Combined
     */
    public static final int SPECIAL_ENTRY_URINE_COMBINED = 100;

    /**
     * Special Entry Id
     */
    public int special_entry_id = -1;

    /**
     * Special Entry Value
     */
    public String special_entry_value = null;

    /**
     * Date time for event
     */
    public long datetime_tag = 0L;

    /**
     * Source
     */
    public String source = null;

    // String special_entry_tags[] = { "", "URINE", "URINE", "CH" };
    // String special_entry_units[] = { "", " mmol/L", " mg/dL", " g" }; // this
    // are not required, if your special
    // boolean special_entry_transfer_unit[] = { false, true, true, false }; //
    // entry has no unit, leave this empty (have at least one space as unit), so
    // that code will work
    // int special_entry_pump_map[] = { -1, 4, 4, 5 };

    static Hashtable<String, SpecialEntryDefinition> special_entries = null; // new
                                                                             // Hashtable<String,
                                                                             // String>();
    static Hashtable<String, String> allowed_types = null;

    /**
     * Constructor 
     * 
     * @param id
     * @param value
     */
    public MeterValuesEntrySpecial(int id, String value)
    {
        initSpecialEntries();
        if (id == SPECIAL_ENTRY_URINE_COMBINED)
        {
            processUrine(value);
        }
        else
        {
            this.special_entry_id = id;
            this.special_entry_value = value;
        }
    }

    /**
     * Get Allowed Pump Mapped Types
     * 
     * @return
     */
    public static Hashtable<String, String> getAllowedPumpMappedTypes()
    {
        if (allowed_types == null)
        {
            allowed_types = new Hashtable<String, String>();
            allowed_types.put("4", ""); // URINE
            allowed_types.put("5", ""); // CH
            allowed_types.put("3", ""); // BG
        }

        return allowed_types;
    }

    private void processUrine(String value)
    {
        if (value.toLowerCase().contains("mmol/"))
        {
            this.special_entry_id = MeterValuesEntrySpecial.SPECIAL_ENTRY_URINE_MMOLL;

            int idx = value.toLowerCase().indexOf("mmol/");

            this.special_entry_value = value.substring(0, idx);
            this.special_entry_value = this.special_entry_value.trim();
        }
        else if (value.toLowerCase().contains("mg/"))
        {
            this.special_entry_id = MeterValuesEntrySpecial.SPECIAL_ENTRY_URINE_MMOLL;

            int idx = value.toLowerCase().indexOf("mg/");

            this.special_entry_value = value.substring(0, idx);
            this.special_entry_value = this.special_entry_value.trim();
        }
        else
        {
            this.special_entry_id = MeterValuesEntrySpecial.SPECIAL_ENTRY_URINE_DESCRIPTIVE;
            this.special_entry_value = value;
        }
    }

    /**
     * Which Special is DVE
     * 
     * @param dv_type
     * @return
     */
    public int whichSpecialIsDVE(int dv_type)
    {
        switch (dv_type)
        {
            case ExtendedDailyValue.EXTENDED_URINE:
                return SPECIAL_ENTRY_URINE_COMBINED;

            default:
                return -1;
        }
    }

    /**
     * Do we Transfer Units For Special Entry
     * 
     * @return
     */
    public boolean doWeTransferUnitsForSpecialEntry()
    {
        return special_entries.get("" + this.special_entry_id).transfer_unit;
    }

    /**
     * Get Pump Mapped Type
     * 
     * @return
     */
    public int getPumpMappedType()
    {
        return special_entries.get("" + this.special_entry_id).pump_map;
    }

    /**
     * Set Date Time 
     * 
     * @param dt
     */
    public void setDateTime(long dt)
    {
        this.datetime_tag = dt;
    }

    /**
     * Get Pump Compare Id
     * 
     * @return
     */
    public String getPumpCompareId()
    {
        return this.datetime_tag * 100 + "_" + getPumpMappedType();
    }

    /**
     * Get Special Entry DbEntry
     * @return
     */
    public String getSpecialEntryDbEntry()
    {
        StringBuffer sb = new StringBuffer();

        SpecialEntryDefinition sed = special_entries.get("" + this.special_entry_id);

        sb.append(sed.tag); // this.special_entry_tags[this.special_entry_id]);
        sb.append("=");
        sb.append(getValueValue());

        if (sed.transfer_unit)
        {
            sb.append(" " + sed.unit);
        }

        return sb.toString();
    }

    private void initSpecialEntries()
    {
        if (special_entries == null)
        {
            special_entries = new Hashtable<String, SpecialEntryDefinition>();
            special_entries.put("1", new SpecialEntryDefinition(1, "URINE", "mmol/L", true, 4,
                    ExtendedDailyValue.EXTENDED_URINE, SpecialEntryDefinition.TYPE_STRING));
            special_entries.put("2", new SpecialEntryDefinition(2, "URINE", "mg/dL", true, 4,
                    ExtendedDailyValue.EXTENDED_URINE, SpecialEntryDefinition.TYPE_STRING));
            special_entries.put("3", new SpecialEntryDefinition(3, "URINE", "", false, 4,
                    ExtendedDailyValue.EXTENDED_URINE, SpecialEntryDefinition.TYPE_STRING));
            special_entries.put("4", new SpecialEntryDefinition(4, "CH", "g", false, 5, -1,
                    SpecialEntryDefinition.TYPE_DECIMAL_1));
            special_entries.put("-2", new SpecialEntryDefinition(-2, "BG", "", false, 3, -1,
                    SpecialEntryDefinition.TYPE_INTEGER));
        }

        // Hashtable<String, String> special_entry_tags = new Hashtable<String,
        // String>();

    }

    /**
     * Get Packed Value
     * 
     * @return
     */
    public String getPackedValue()
    {
        String v = getValueValue();

        if (special_entries.get("" + this.special_entry_id).transfer_unit)
        {
            v += " " + special_entries.get("" + this.special_entry_id).unit;
        }
        return v;
    }

    /**
     * get Value Value
     * @return
     */
    public String getValueValue()
    {
        String v = null;
        DataAccessMeter da = DataAccessMeter.getInstance();

        if (special_entries.get("" + this.special_entry_id).type == SpecialEntryDefinition.TYPE_INTEGER)
        {
            v = "" + da.getIntValueFromString(this.special_entry_value.trim(), 0);
        }
        else if (special_entries.get("" + this.special_entry_id).type == SpecialEntryDefinition.TYPE_DECIMAL_1)
        {
            v = ""
                    + da.getDecimalHandler().getDecimalAsString(
                        da.getFloatValueFromString(this.special_entry_value.trim(), 0.0f) * 1.0f, 1);
        }
        else if (special_entries.get("" + this.special_entry_id).type == SpecialEntryDefinition.TYPE_DECIMAL_2)
        {
            v = ""
                    + da.getDecimalHandler().getDecimalAsString(
                        da.getFloatValueFromString(this.special_entry_value.trim(), 0.0f) * 1.0f, 2);
        }
        else
        {
            v = this.special_entry_value.trim();
        }

        return v;
    }

    /**
     * Get Type Description 
     * 
     * @return
     */
    public String getTypeDescription()
    {
        return DataAccessMeter.getInstance().getI18nControlInstance()
                .getMessage(special_entries.get("" + this.special_entry_id).tag);
    }

    /**
     * Get Extended Freetype description
     * 
     * @return
     */
    public String getExtendedFreetypeDescription()
    {
        return this.getTypeDescription() + ": " + this.getPackedValue();
    }

    private class SpecialEntryDefinition
    {
        public int id;
        public String tag;
        public String unit;
        boolean transfer_unit;
        public int pump_map;
        public int dailyvalue_ext_map;
        public int type;

        public static final int TYPE_DECIMAL_1 = 1;
        public static final int TYPE_DECIMAL_2 = 2;
        public static final int TYPE_STRING = 3;
        public static final int TYPE_INTEGER = 4;

        /*
         * public SpecialEntryDefinition(int id_, String tag_, String unit_,
         * boolean transfer_unit_ )
         * {
         * this(id_, tag_, unit_, transfer_unit_, -1, -1, -1);
         * }
         */

        public SpecialEntryDefinition(int id_, String tag_, String unit_, boolean transfer_unit_, int pump_map_,
                int dv_ext_map, int type_)
        {
            this.id = id_;
            this.tag = tag_;
            this.unit = unit_;
            this.transfer_unit = transfer_unit_;
            this.pump_map = pump_map_;
            this.dailyvalue_ext_map = dv_ext_map;
            this.type = type_;
        }

    }

}
