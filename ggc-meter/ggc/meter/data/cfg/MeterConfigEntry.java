package ggc.meter.data.cfg;

import ggc.meter.util.I18nControl;


public class MeterConfigEntry
{
    
    I18nControl ic = I18nControl.getInstance();
    
    public int id;
    public String name;
    public String meter_company;
    public String meted_device;
    public String communication_port;
    public boolean ds_fix = false;
    public String ds_area = "";
    public int ds_winter_change = 0;
    public int ds_summer_change = 0;

    
    public String getDayLightFix()
    {
        if (!ds_fix)
            return ic.getMessage("DS_NO");
        else
            return String.format(ic.getMessage("DS_FIX_SHORT"), ds_winter_change, ds_summer_change);
        
    }

    public String getDayLightFixLong()
    {
        if (!ds_fix)
            return ic.getMessage("DS_NO");
        else
            return String.format(ic.getMessage("DS_FIX_LONG"), ds_winter_change, ds_summer_change, ds_area);
        
    }
    
    
}
