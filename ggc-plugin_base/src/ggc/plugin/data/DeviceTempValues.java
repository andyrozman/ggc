package ggc.plugin.data;

import ggc.plugin.output.OutputWriterData;

import com.atech.utils.ATechDate;


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



public abstract class DeviceTempValues 
{
    protected int object_type = 1;
    protected int base_type = 1;
    protected int sub_type = 0;
    protected String value_template;

    
    /**
     * @param _object_type
     * @param _base_type
     * @param _sub_type
     */
    public DeviceTempValues(int _object_type, int _base_type, int _sub_type)
    {
        this(_object_type, _base_type, _sub_type, null);
    }

    
    /**
     * @param _object_type
     * @param _base_type
     * @param _sub_type
     */
    public DeviceTempValues(int _object_type, int _base_type)
    {
        this(_object_type, _base_type, 0, null);
    }
    
    
    /**
     * @param _object_type
     * @param _base_type
     * @param _sub_type
     * @param _value_template 
     */
    public DeviceTempValues(int _object_type, int _base_type, int _sub_type, String _value_template)
    {
        this.object_type = _object_type; 
        this.base_type = _base_type;
        this.sub_type = _sub_type;
        this.value_template = _value_template;
    }
    
    
    /**
     * Get Data
     * 
     * @param dt
     * @param _sub_type
     * @return
     */
    public OutputWriterData getData(ATechDate dt, int _sub_type)
    {
        return getData(dt, _sub_type, null);
    }

    
    /**
     * Get Data
     * 
     * @param dt
     * @param _value
     * @return
     */
    public OutputWriterData getData(ATechDate dt, String _value)
    {
        return getData(dt, -1, _value);
    }
    
    
    
    /**
     * Get Data
     * 
     * @param dt
     * @param _sub_type
     * @param _value
     * @return
     */
    public abstract OutputWriterData getData(ATechDate dt, int _sub_type, String _value);
    
    
}	
