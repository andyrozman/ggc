package ggc.plugin.data;

import ggc.plugin.util.DataAccessPlugInBase;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

public abstract class DeviceDefsAbstract
{

    protected DataAccessPlugInBase da = null;
    protected I18nControlAbstract ic = null; 

    
    protected Hashtable<Integer,String> data_desc = new Hashtable<Integer,String>(); 
    protected Hashtable<String,String> data_mapping = new Hashtable<String,String>(); 
    protected Hashtable<Integer,String> data_value_template = new Hashtable<Integer,String>(); 
    
    
    
    
    
    /**
     * Constructor
     */
    public DeviceDefsAbstract()
    {
    }
    
    
    /**
     * Set Data Description
     * 
     * @param type type as int
     * @param desc desc as string
     */
    protected void setDataDesc(int type, String desc)
    {
        this.data_mapping.put(ic.getMessage(desc), "" + type);
        this.data_desc.put(type, ic.getMessage(desc));
    }

    
    /**
     * Set Data Description
     * 
     * @param type type as int
     * @param desc desc as string
     */
    protected void setDataDesc(int type, String desc, String value_template)
    {
        this.data_mapping.put(ic.getMessage(desc), "" + type);
        this.data_desc.put(type, ic.getMessage(desc));
        this.data_value_template.put(type, value_template);
    }
    
    

    /**
     * Get Type from Description
     * 
     * @param str type as string
     * @return type as int
     */
    public int getTypeFromDescription(String str)
    {
        String s = "0";
        
        if (this.data_mapping.containsKey(str))
            s = this.data_mapping.get(str);
        
        return Integer.parseInt(s);
    }
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public Hashtable<Integer,String> getDescriptions()
    {
        return this.data_desc;
    }


    /**
     * Get Description For Type
     * 
     * @param type 
     * @return String description of event
     */
    public String getDescriptionForType(int type)
    {
        return this.data_desc.get(type);
    }

    
    /**
     * Get Description For Type
     * 
     * @param type 
     * @return String description of event
     */
    public String getValueTemplateForType(int type)
    {
        return this.data_value_template.get(type);
    }
    
    
    
    /**
     * Get Description by ID
     * 
     * @param id
     * @return
     */
    public String getDescriptionByID(int id)
    {
        return this.getDescriptionForType(id);
    }
    
    
    
}