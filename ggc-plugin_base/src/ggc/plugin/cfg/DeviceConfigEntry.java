package ggc.plugin.cfg;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;


public class DeviceConfigEntry
{
    
    I18nControlAbstract ic = null;
    
    public int id;
    public String name;
    public String device_company;
    public String device_device;
    public String communication_port;
    public boolean ds_fix = false;
    public String ds_area = "";
    public String ds_area_long = "";
    public int ds_winter_change = 0;
    public int ds_summer_change = 0;
    boolean valid = true;
    DeviceConfigurationDefinition dcd;
    
    public DeviceConfigEntry(DeviceConfigurationDefinition dcd, I18nControlAbstract ic)
    {
        this.ic = ic;
        this.dcd = dcd;
    }

    public DeviceConfigEntry(I18nControlAbstract ic)
    {
        this.ic = ic;
    }
    
    
    
    public DeviceConfigEntry clone()
    {
        DeviceConfigEntry dce = new DeviceConfigEntry(ic);
        
        dce.id = id;
        dce.name = name;
        dce.device_company = device_company;
        dce.device_device = device_device;
        dce.communication_port = communication_port;
        dce.ds_fix = ds_fix;
        dce.ds_area = ds_area;
        dce.ds_area_long = ds_area_long;
        dce.ds_winter_change = ds_winter_change;
        dce.ds_summer_change = ds_summer_change;
        dce.valid = valid;
        
        return dce;
    }
    
    public void setDeviceConfigurationDefinition(DeviceConfigurationDefinition dcd)
    {
        this.dcd = dcd;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }

    public void setId(String id)
    {
        this.id = Integer.parseInt(id);
    }
    
    
    
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

    
    public boolean isValid()
    {
        return this.valid;
    }
    
    
    public void readConfiguration(Hashtable<String,String> cfg, int selected)
    {
        this.name = getParameter("NAME", selected, cfg, true);
        this.device_company = getParameter("COMPANY", selected, cfg, true);
        this.device_device = getParameter("DEVICE", selected, cfg, true);
        this.communication_port = getParameter("CONNECTION_PARAMETER", selected, cfg, true);

        if (!dcd.doesDeviceSupportTimeFix())
            return;
        
        this.ds_area = getParameter("TIMEZONE", selected, cfg, false);
        
        if (this.ds_area==null)
        {
            this.ds_area = "Europe/Prague";
            this.ds_area_long = "(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague";
        }
        
        this.ds_area_long = getParameter("TIMEZONE_LONG", selected, cfg, false);
        
        if (this.ds_area_long==null)
        {
            this.ds_area_long = "(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague";
        }
        
        
        
        
        this.ds_fix = getParameterBoolean("DAYLIGHTSAVING_FIX", selected, cfg);
        
        if (ds_fix)
        {
            this.ds_summer_change = getParameterInt("SUMMER_FIX", selected, cfg);
            this.ds_winter_change = getParameterInt("WINTER_FIX", selected, cfg);
            
        }
        else
        {
            this.ds_summer_change = 0;
            this.ds_winter_change = 0;
        }
        

        
        
        /*
        METER_1_NAME=My meter xx
        METER_1_COMPANY=Ascensia/Bayer
        METER_1_DEVICE=Contour
        METER_1_CONNECTION_PARAMETER=COM9
        METER_1_TIMEZONE=Europe/Prague
        METER_1_DAYLIGHTSAVING_FIX=YES
        METER_1_WINTER_FIX=0
        METER_1_SUMMER_FIX=+1         */
        
    }
    
    
    public String getFileEntry()
    {
        String prefix = dcd.getDevicePrefix() + "_" + this.id + "_";
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(prefix + "NAME=" + this.name + "\n");
        sb.append(prefix + "COMPANY=" + this.device_company + "\n");
        sb.append(prefix + "DEVICE=" + this.device_device + "\n");

        sb.append(prefix + "CONNECTION_PARAMETER=" + this.communication_port + "\n");
        
        if (this.dcd.doesDeviceSupportTimeFix())
        {
            sb.append(prefix + "TIMEZONE=" + this.ds_area + "\n");
            sb.append(prefix + "TIMEZONE_LONG=" + this.ds_area_long + "\n");
            sb.append(prefix + "DAYLIGHTSAVING_FIX=" + getBoolean(this.ds_fix) + "\n");
            sb.append(prefix + "WINTER_FIX=" + getTimeChange(this.ds_winter_change) + "\n");
            sb.append(prefix + "SUMMER_FIX=" + getTimeChange(this.ds_summer_change) + "\n");
        }
        
        return sb.toString();
    }
    
    
    public String getTimeChange(int change)
    {
        if (change==0)
            return "0";
        else if (change==-1)
            return "-1";
        else //if (change==+1)
            return "+1";
        
    }
    
    
    
    private String getBoolean(boolean val)
    {
        if (val)
            return "YES";
        else
            return "NO";
    }
    
    
    
    public String getParameter(String tag, int selected, Hashtable<String,String> cfg, boolean validate)
    {
        if (cfg.containsKey(this.dcd.getDevicePrefix() + "_" + selected + "_" + tag))
        {
            return cfg.get(this.dcd.getDevicePrefix() + "_" + selected + "_" + tag);
        }
        else
        {
            //System.out.println("not found: "  + tag);
            if (validate)
                valid = false;
        }
        
        return null;
    }
    

    public boolean getParameterBoolean(String tag, int selected, Hashtable<String,String> cfg)
    {
        String par = getParameter(tag, selected, cfg, false);
        
        if (par==null)
        {
            return false;
        }
        
        if ((par.equals("1")) || (par.toUpperCase().equals("YES")) || (par.toUpperCase().equals("TRUE")))
        {
            return true;
        }
        else
            return false;
        
    }
    

    public int getParameterInt(String tag, int selected, Hashtable<String,String> cfg)
    {
        String par = getParameter(tag, selected, cfg, false);
        
        if (par==null)
        {
            return 0;
        }
        
        try
        {
            String p = par.trim();
            if (p.startsWith("+"))
                p = p.substring(1);
            
            int s = Integer.parseInt(p);
            return s;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public String toString()
    {
/*        public int id;
        public String name;
        public String meter_company;
        public String meter_device;
        public String communication_port;
        public boolean ds_fix = false;
        public String ds_area = "";
        public int ds_winter_change = 0;
        public int ds_summer_change = 0;
        boolean valid = true;
*/
        
        return "ds_fix=" + this.ds_fix + ",area=" + this.ds_area + ",winter=" + this.ds_winter_change + ",summer=" + this.ds_summer_change;
    }
    
    
    
}
