
package ggc.data.cfg;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import ggc.db.GGCDb;
import ggc.db.datalayer.Settings;
import ggc.util.DataAccess;

// NEW - Please don't edit, since this file is in creation mode
//  Andy


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
	    "SELECTED_COLOR_SCHEME", "Default Scheme"
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


    Hashtable<String,Settings> cfg_values = new Hashtable<String,Settings>();
    DataAccess m_da = null;


    public ConfigurationManager(DataAccess da)
    {
	this.m_da = da;
    }


    public void checkConfiguration(Hashtable<String,Settings> values)
    {

	this.cfg_values = values;

	for(int j=1; j<4; j++)
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


	    for(int i=0; i<arr.length; i+=2)
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
	Settings s = new Settings();

	s.setKey(name);
	s.setDescription(m_da.getI18nInstance().getMessage("CFG_"+name));
	s.setType(type);
	s.setValue(def_value);
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
	    catch(Exception ex)
	    {
		s_logger.warn("Invalid value for key=" + key + " found. It should be integer.");
	    }

	}

	return -1;
	
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
	    catch(Exception ex)
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
	    catch(Exception ex)
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
	    catch(Exception ex)
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
	    s_logger.warn("Configuration key " + key + " doesn't exists.");
	    return false;
	}
    }


    public void saveConfig()
    {

	//System.out.println("Save Config - Start [changed=" + changed +"]");

	if (!changed)
	    return;

	GGCDb db = this.m_da.getDb();

	for(Enumeration<String> en = this.cfg_values.keys(); en.hasMoreElements(); )
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

