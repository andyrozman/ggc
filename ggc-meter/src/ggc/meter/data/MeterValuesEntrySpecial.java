package ggc.meter.data;

import java.util.Hashtable;

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
     * Special Entry: CH
     */
    public static final int SPECIAL_ENTRY_CH = 3;
    
    
    
    public static final int SPECIAL_ENTRY_MAX = 3;
    
    
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
    
    
//    String special_entry_tags[] = { "", "URINE", "URINE", "CH" };
//    String special_entry_units[] = { "", " mmol/L", " mg/dL", " g" };  // this are not required, if your special 
//    boolean special_entry_transfer_unit[] = { false, true, true, false };                                                             // entry has no unit, leave this empty (have at least one space as unit), so that code will work
//    int special_entry_pump_map[] = { -1, 4, 4, 5 };
    
    Hashtable<String, SpecialEntryDefinition> special_entries = null; //new Hashtable<String, String>(); 
    static Hashtable<String, String> allowed_types = null;
    
    
    public MeterValuesEntrySpecial(int id, String value)
    {
        this.special_entry_id = id;
        this.special_entry_value = value;
    }
    
    
    public static Hashtable<String,String> getAllowedPumpMappedTypes()
    {
        if (allowed_types==null)
        {
            allowed_types = new Hashtable<String, String>();
            allowed_types.put("4", ""); // URINE
            allowed_types.put("5", ""); // CH
            allowed_types.put("3", ""); // BG
        }
        
        return allowed_types;
    }
    
    
    /**
     * Do we Transfer Units For Special Entry
     * 
     * @return
     */
    public boolean doWeTransferUnitsForSpecialEntry()
    {
        return this.special_entries.get("" + this.special_entry_id).transfer_unit;
    }

    
    
    /**
     * Get Pump Mapped Type
     * 
     * @return
     */
    public int getPumpMappedType()
    {
        return this.special_entries.get("" + this.special_entry_id).pump_map;
    }
    
    
    public void setDateTime(long dt)
    {
        this.datetime_tag = dt;
    }
    
    public String getPumpCompareId()
    {
        return (this.datetime_tag * 100) + "_" + getPumpMappedType();
    }
    
    
    /**
     * Get Special Entry DbEntry
     * @return
     */
    public String getSpecialEntryDbEntry()
    {
        StringBuffer sb = new StringBuffer();

        SpecialEntryDefinition sed = this.special_entries.get("" + this.special_entry_id);
        
        sb.append(sed.tag); //this.special_entry_tags[this.special_entry_id]);
        sb.append("=");
        sb.append(this.special_entry_value);
        
        if (sed.transfer_unit)
        {
            sb.append(" " + sed.unit);
        }
        
        return sb.toString();
    }
    
    
    
    
    public void initSpecialEntries()
    {
        if (this.special_entries==null)
        {
            this.special_entries = new Hashtable<String, SpecialEntryDefinition>();
            this.special_entries.put("1", new SpecialEntryDefinition(1, "URINE", "mmol/L", true, 4));
            this.special_entries.put("2", new SpecialEntryDefinition(2, "URINE", "mg/dL", true, 4));
            this.special_entries.put("3", new SpecialEntryDefinition(3, "CH", "g", false, 5));
            this.special_entries.put("-2", new SpecialEntryDefinition(-2, "BG", "", false, 3));
            
            /*
//    String special_entry_tags[] = { "", "URINE", "URINE", "CH" };

            String special_entry_units[] = { "", " mmol/L", " mg/dL", " g" };  // this are not required, if your special 
            boolean special_entry_transfer_unit[] = { false, true, true, false };                                                             // entry has no unit, leave this empty (have at least one space as unit), so that code will work
            int special_entry_pump_map[] = { -1, 4, 4, 5 };
            */
// SpecialEntryDefinition(int id_, String tag_, String unit_, boolean transfer_unit_, int pump_map_ )
            
        }
        
        Hashtable<String, String> special_entry_tags = new Hashtable<String, String>(); 
        
    }
    
    public String getPackedValue()
    {
        String v = this.special_entry_value;
        
        if (this.special_entries.get("" + this.special_entry_id).transfer_unit)
        {
            v += " " + this.special_entries.get("" + this.special_entry_id).unit;
        }
        return v;
    }
    
    
  
    private class SpecialEntryDefinition
    {
        public int id;
        public String tag;
        public String unit;
        boolean transfer_unit;
        public int pump_map;

        
        public SpecialEntryDefinition(int id_, String tag_, String unit_, boolean transfer_unit_ )
        {
            this(id_, tag_, unit_, transfer_unit_, -1);
        }
        
        
        public SpecialEntryDefinition(int id_, String tag_, String unit_, boolean transfer_unit_, int pump_map_ )
        {
            this.id = id_;
            this.tag = tag_;
            this.unit = unit_;
            this.transfer_unit = transfer_unit_;
            this.pump_map = pump_map_;
        }
        
        
        
    }
    
    
    
    
}
