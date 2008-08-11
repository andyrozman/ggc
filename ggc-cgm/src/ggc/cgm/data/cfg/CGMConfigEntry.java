package ggc.cgm.data.cfg;

import ggc.cgm.util.I18nControl;

import java.util.Hashtable;


public class CGMConfigEntry
{
    
    I18nControl ic = I18nControl.getInstance();
    
    public int id;
    public String name;
    public String meter_company;
    public String meter_device;
    public String communication_port;
    public boolean ds_fix = false;
    public String ds_area = "";
    public int ds_winter_change = 0;
    public int ds_summer_change = 0;
    boolean valid = true;

    
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
        this.meter_company = getParameter("COMPANY", selected, cfg, true);
        this.meter_device = getParameter("DEVICE", selected, cfg, true);
        this.communication_port = getParameter("CONNECTION_PARAMETER", selected, cfg, true);

        this.ds_area = getParameter("TIMEZONE", selected, cfg, false);
        
        if (this.ds_area==null)
            this.ds_area = "Europe/Prague";
        
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
    
    public String getParameter(String tag, int selected, Hashtable<String,String> cfg, boolean validate)
    {
        if (cfg.containsKey("METER_" + selected + "_" + tag))
        {
            return cfg.get("METER_" + selected + "_" + tag);
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
