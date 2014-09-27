package ggc.core.data.cfg;

import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.Settings;
import ggc.core.util.DataAccess;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     ConfigurationManager
 *  Description:  Configuration Manager is class for managing configuration settings 
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ConfigurationManager
{

    private static Log s_logger = LogFactory.getLog(ConfigurationManager.class);
    private boolean changed = false;

    private String cfg_string[] = { "NAME", "Unknown user", "INS1_NAME", "Insulin 1", "INS1_ABBR", "Ins1", "INS2_NAME",
                                   "Insulin 2", "INS2_ABBR", "Ins2", "METER_PORT", "", "LAF_NAME",
                                   "blueMetalthemepack.zip", "PRINT_PDF_VIEWER_PATH", "", "PRINT_EMPTY_VALUE", "",
                                   "SELECTED_COLOR_SCHEME", "Default Scheme", "TIMEZONE", "", "SW_MODE_DESC",
                                   "PEN_INJECTION_MODE", "PEN_BASAL_PRECISSION", "1", "PEN_BOLUS_PRECISSION", "1",
                                   "PUMP_BASAL_PRECISSION", "0.1", "PUMP_BOLUS_PRECISSION", "0.1", "INS_CARB_RULE",
                                   "RULE_500", "SENSITIVITY_RULE", "RULE_1800", "PUMP_INSULIN", "",
                                   "PRINT_PDF_VIEWER_PARAMETERS", "", "INS3_NAME", "Insulin 3", "INS3_ABBR", "Ins3",
                                   "RATIO_MODE", "Base" // Base, Extended
    };

    private String cfg_int[] = {
                                "METER_TYPE",
                                "0",
                                "BG_UNIT",
                                "2", // 1=mg/dl, 2= mmol/l
                                "RENDER_RENDERING", "0", "RENDER_DITHERING", "0", "RENDER_INTERPOLATION", "0",
                                "RENDER_ANTIALIASING", "0", "RENDER_TEXT_ANTIALIASING", "0", "RENDER_COLOR_RENDERING",
                                "0", "RENDER_FRACTIONAL_METRICS", "0", "PRINT_LUNCH_START_TIME", "1100",
                                "PRINT_DINNER_START_TIME", "1800", "PRINT_NIGHT_START_TIME", "2100", "SW_MODE", "0",
                                "PUMP_TBR_TYPE", "0", "INS1_TYPE", "2", // 0 -
                                                                        // Not
                                                                        // enabled,
                                                                        // 1=Basal,
                                                                        // 2=Bolus
                                "INS2_TYPE", "1", "INS3_TYPE", "0" };

    private String cfg_float[] = { "BG1_LOW", "60.0f", "BG1_HIGH", "200.0f", "BG1_TARGET_LOW", "80.0f",
                                  "BG1_TARGET_HIGH", "120.0f", "BG2_LOW", "3.0f", "BG2_HIGH", "20.0f",
                                  "BG2_TARGET_LOW", "4.4f", "BG2_TARGET_HIGH", "14.0f", "RATIO_CH_INSULIN", "0.0f",
                                  "RATIO_BG_INSULIN", "0.0f", "PEN_MAX_BASAL", "80.0f", "PEN_MAX_BOLUS", "40.0f",
                                  "PUMP_MAX_BASAL", "80.0f", "PUMP_MAX_BOLUS", "20.0f", "PUMP_UNIT_MIN", "0.0f",
                                  "PUMP_UNIT_MAX", "40.0f", "PUMP_UNIT_STEP", "0.1f", "PUMP_PROC_MIN", "0.0f",
                                  "PUMP_PROC_MAX", "200.0f", "PUMP_PROC_STEP", "5.0f", "LAST_TDD", "0.0f" };

    private String cfg_boolean[] = { "METER_DAYLIGHTSAVING_TIME_FIX", "false", "PRINT_USE_EXTERNAL_PDF_VIEWER", "false" };

    Hashtable<String, Settings> cfg_values = new Hashtable<String, Settings>();
    DataAccess m_da = null;

    public ConfigurationManager(DataAccess da)
    {
        this.m_da = da;
    }

    public void checkConfiguration(Hashtable<String, Settings> values, GGCDb db)
    {
        this.cfg_values = values;

        for (int j = 1; j < 5; j++)
        {
            String[] arr = null;

            if (j == 1)
            {
                arr = cfg_string;
            }
            else if (j == 2)
            {
                arr = cfg_int;
            }
            else if (j == 3)
            {
                arr = cfg_float;
            }
            else if (j == 4)
            {
                arr = cfg_boolean;
            }

            for (int i = 0; i < arr.length; i += 2)
            {
                if (!values.containsKey(arr[i]))
                {
                    addNewValue(arr[i], arr[i + 1], j, db);
                }
            }
        }
    }

    public void addNewValue(String name, String def_value, int type, GGCDb db)
    {
        addNewValue(name, def_value, type, db, true);
    }

    /**
     * Add New configuration value
     * 
     * @param name name of configuration parameter
     * @param defaultValue value (presumably def. value if added from this class)
     * @param parameterType type of parameter (1=string, 2=int, 3=float, 4=boolean)
     * @param db db instance
     * @param addToConfigurationValues 
     */
    public void addNewValue(String name, String defaultValue, int parameterType, GGCDb db,
            boolean addToConfigurationValues)
    {
        Settings s = new Settings();

        s.setKey(name);
        s.setDescription(m_da.getI18nControlInstance().getMessage("CFG_" + name));
        s.setType(parameterType);
        s.setValue(defaultValue);
        s.setPerson_id((int) m_da.getCurrentUserId());

        db.add(s);

        if (addToConfigurationValues)
        {
            this.cfg_values.put(name, s);
            this.changed = true;
        }
    }

    public boolean getBooleanValue(String key)
    {
        if (checkIfValueExists(key))
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
        if (checkIfValueExists(key))
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

            if (prev_val != value)
            {
                s.setValue("" + value);
                s.setElementEdited();
                this.changed = true;
            }
        }
    }

    public int getIntValue(String key)
    {
        if (checkIfValueExists(key))
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

    public void setIntValue(String key, int value)
    {
        if (checkIfValueExists(key))
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

            if (prev_val != value)
            {
                s.setValue("" + value);
                s.setElementEdited();
                this.changed = true;
            }
        }
    }

    public float getFloatValue(String key)
    {
        if (checkIfValueExists(key))
        {
            Settings s = this.cfg_values.get(key);

            return m_da.getFloatValueFromString(s.getValue(), 0.0f);
        }

        return 0.0f;
    }

    public void setFloatValue(String key, float value)
    {
        if (checkIfValueExists(key))
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

            if (prev_val != value)
            {
                s.setValue("" + value + "f");
                s.setElementEdited();
                this.changed = true;
            }
        }
    }

    public String getStringValue(String key)
    {
        if (checkIfValueExists(key))
        {
            Settings s = this.cfg_values.get(key);
            return s.getValue();
        }
        else
            return "";
    }

    public void setStringValue(String key, String value)
    {
        if (checkIfValueExists(key))
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

    private boolean checkIfValueExists(String key)
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
        if (!changed)
            return;

        GGCDb db = this.m_da.getDb();

        for (Enumeration<String> en = this.cfg_values.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();

            Settings s = this.cfg_values.get(key);

            if (s.isElementAdded())
            {
                db.add(s);
            }
            else if (s.isElementEdited())
            {
                db.edit(s);
            }
        }

    }

    public Hashtable<String, String> loadExtendedRatioData()
    {
        return null;
    }

    public void saveExtendedRatioData(Hashtable<String, String> dta)
    {
        // return null;
    }

}
