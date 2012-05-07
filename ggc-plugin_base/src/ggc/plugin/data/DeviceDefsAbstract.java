package ggc.plugin.data;

import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     DeviceValues
 *  Description:  Collection of DeviceValuesDay.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class DeviceDefsAbstract
{

    protected DataAccessPlugInBase da = null;
    protected I18nControlAbstract ic = null; 

    
    protected Hashtable<Integer,String> data_desc = new Hashtable<Integer,String>(); 
    protected Hashtable<String,String> data_mapping = new Hashtable<String,String>(); 
    protected Hashtable<Integer,String> data_value_template = new Hashtable<Integer,String>(); 
    protected ArrayList<Integer> data_int = new ArrayList<Integer>();
    protected String[] desc_array = null;
    
    
    
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
        this.data_int.add(type);
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
        this.data_int.add(type);
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

    
    public void finalizeAdding()
    {
        String[] dd = new String[this.data_int.size()];
        
        for(int i=0; i<this.data_int.size(); i++)
        {
            dd[i] = getDescriptionForType(this.data_int.get(i));
        }
        
        this.desc_array = dd;
    }
    
    
    public String[] getStaticDescriptionArray()
    {
        return this.desc_array;
    }
    
    
    public int getIndexFromStaticDescriptionArrayWithID(int id)
    {
        String dsc = getDescriptionForType(id);
        
        return getIndexFromStaticDescriptionArray(dsc);
    }
    
    
    
    public int getIndexFromStaticDescriptionArray(String desc)
    {
        for(int i=0; i < this.desc_array.length; i++)
        {
            if (this.desc_array[i].equals(desc))
                return i;
        }
        
        return 0;
    }
    
    
    
}