package ggc.data.cfg;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: ConfigurationManager
 *  Purpose:  Configuration Manager is class for managing configuration settings 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import ggc.db.GGCDb;
import ggc.db.datalayer.Settings;
import ggc.core.util.DataAccess;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 


public class ConfigurationManager
{

    private static Log s_logger = LogFactory.getLog(ConfigurationManager.class); 
    private boolean changed = false;



    private String cfg_string[] = 
    {
        "NAME", "Unknown user",
        "INS1_NAME", "Insulin 1",
        "INS1_ABBR", "Ins1",
        "INS2_NAME", "Insulin 2",
        "INS2_ABBR", "Ins2",
        "METER_PORT", "",
        "LAF_NAME", "blueMetalthemepack.zip",
        "PRINT_PDF_VIEWER_PATH", "",
        "PRINT_EMPTY_VALUE", "",
        "SELECTED_COLOR_SCHEME", "Default Scheme",
        "TIMEZONE", ""
    };

    private String cfg_int[] = 
    {
        "METER_TYPE", "0",
        "BG_UNIT", "2",  // 1=mg/dl, 2= mmol/l
        "RENDER_RENDERING", "0",
        "RENDER_DITHERING", "0",
        "RENDER_INTERPOLATION", "0",
        "RENDER_ANTIALIASING", "0",
        "RENDER_TEXT_ANTIALIASING", "0",
        "RENDER_COLOR_RENDERING", "0",
        "RENDER_FRACTIONAL_METRICS", "0",
        "PRINT_LUNCH_START_TIME", "1100",
        "PRINT_DINNER_START_TIME", "1800",
        "PRINT_NIGHT_START_TIME", "2100"
    };


    private String cfg_float[] = 
    {
        "BG1_LOW", "60.0f",
        "BG1_HIGH", "200.0f",
        "BG1_TARGET_LOW", "80.0f",
        "BG1_TARGET_HIGH", "120.0f",
        "BG2_LOW", "3.0f",
        "BG2_HIGH", "20.0f",
        "BG2_TARGET_LOW", "4.4f",
        "BG2_TARGET_HIGH", "14.0f",

    };

    private String cfg_boolean[] = 
    {
        "METER_DAYLIGHTSAVING_TIME_FIX", "false"
    };



    Hashtable<String,Settings> cfg_values = new Hashtable<String,Settings>();
    DataAccess m_da = null;


    public ConfigurationManager(DataAccess da)
    {
        this.m_da = da;
    }


    public void checkConfiguration(Hashtable<String,Settings> values)
    {

        this.cfg_values = values;

        //XXX: The following is pretty ugly code; better to store arrays in another array and use foreach loop?
        for (int j=1; j<5; j++)
        {
            String[] arr = null;

            if (j==1)
            {
                arr = cfg_string;
            }
            else if (j==2)
            {
                arr = cfg_int;
            }
            else if (j==3)
            {
                arr = cfg_float;
            }
            else if (j==4)
            {
                arr = cfg_boolean;
            }


            for (int i=0; i<arr.length; i+=2)
            {
                if (!values.containsKey(arr[i]))
                {
                    addNewValue(arr[i], arr[i+1], j);
                }
            }


        }


    }

    private void addNewValue(String name, String def_value, int type)
    {
        //System.out.println("addNewValue:: name=" + name);

        Settings s = new Settings();

        s.setKey(name);
        s.setDescription(m_da.getI18nControlInstance().getMessage("CFG_"+name));
        s.setType(type);
        s.setValue(def_value);
        s.setPerson_id(m_da.getCurrentPersonId());
        s.setElementAdded();

        this.cfg_values.put(name, s);   

        this.changed = true;
    }


    public int getIntValue(String key)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);

            try
            {
                return Integer.parseInt(s.getValue());
            }
            catch (Exception ex)
            {
                s_logger.warn("Invalid value for key=" + key + " found. It should be integer.");
            }

        }

        return -1;

    }


    public boolean getBooleanValue(String key)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);

            try
            {
                return Boolean.parseBoolean(s.getValue());
            }
            catch (Exception ex)
            {
                s_logger.warn("Invalid value for key=" + key + " found. It should be boolean.");
            }

        }

        return false;

    }


    public void setBooleanValue(String key, boolean value)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);

            boolean prev_val = false;
            try
            {
                prev_val = Boolean.parseBoolean(s.getValue());
            }
            catch (Exception ex)
            {
                s_logger.warn("Invalid value for key=" + key + " found. It should be boolean.");
            }

            //System.out.println("setIntValue: " + value);

            if (prev_val!=value)
            {
                s.setValue("" + value);
                s.setElementEdited();
                this.changed = true;
            }

        }

    }




    public void setIntValue(String key, int value)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);

            int prev_val = 0;
            try
            {
                prev_val = Integer.parseInt(s.getValue());
            }
            catch (Exception ex)
            {
                s_logger.warn("Invalid value for key=" + key + " found. It should be integer.");
            }

            //System.out.println("setIntValue: " + value);

            if (prev_val!=value)
            {
                s.setValue("" + value);
                s.setElementEdited();
                this.changed = true;
                //System.out.println("setIntValue: Success");
            }

        }

    }


    public float getFloatValue(String key)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);

            try
            {
                return Float.parseFloat(s.getValue());
            }
            catch (Exception ex)
            {
                s_logger.warn("Invalid value for key=" + key + " found. It should be float.");
            }
        }

        return 0.0f;

    }


    public void setFloatValue(String key, float value)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);

            float prev_val = 0;
            try
            {
                prev_val = Float.parseFloat(s.getValue());
            }
            catch (Exception ex)
            {
                s_logger.warn("Invalid value for key=" + key + " found. It should be float.");
            }

            if (prev_val!=value)
            {
                s.setValue("" + value + "f");
                s.setElementEdited();
                this.changed = true;
            }
        }
    }



    public String getStringValue(String key)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);
            return s.getValue();
        }
        else
            return "";

    }

    public void setStringValue(String key, String value)
    {
        if (checkIfExists(key))
        {
            Settings s = this.cfg_values.get(key);

            if (!s.getValue().equals(value))
            {
                s.setValue("" + value);
                s.setElementEdited();
                this.changed = true;
            }
        }
    }



    private boolean checkIfExists(String key)
    {
        if (this.cfg_values.containsKey(key))
            return true;
        else
        {
            s_logger.warn("Configuration key " + key + " doesn't exist.");
            return false;
        }
    }


    public void saveConfig()
    {

        //System.out.println("Save Config - Start [changed=" + changed +"]");

        if (!changed)
            return;

        GGCDb db = this.m_da.getDb();

        for (Enumeration<String> en = this.cfg_values.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();

            Settings s = this.cfg_values.get(key);

            if (s.isElementAdded())
                db.add(s);
            else if (s.isElementEdited())
                db.edit(s);
        }

    }



}


