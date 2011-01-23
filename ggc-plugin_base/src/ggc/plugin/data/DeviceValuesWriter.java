package ggc.plugin.data;

import ggc.plugin.output.OutputWriter;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;


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
 *  Filename:     DeviceValuesWriter
 *  Description:  Collection of DeviceTempValues
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceValuesWriter extends Hashtable<String, DeviceTempValues>  
{
    
    private static final long serialVersionUID = -1649768167774901903L;
    OutputWriter output_writer = null;
    private static Log log = LogFactory.getLog(DeviceValuesWriter.class);
    public boolean is_silent_mode = false;

    
    /**
     * Constructor
     */
    public DeviceValuesWriter()
    {
        this(false);
    }
    
    /**
     * Constructor
     */
    public DeviceValuesWriter(boolean is_silent)
    {
        this.is_silent_mode = is_silent;
    }
    
    
    /**
     * Constructor
     *
     * @param ow
     */
    public DeviceValuesWriter(OutputWriter ow)
    {
        this.output_writer = ow;
    }
    
    
    /**
     * Set output writer
     * 
     * @param ow
     */
    public void setOutputWriter(OutputWriter ow)
    {
        this.output_writer = ow;
    }
    
	
    /**
     * Write Object 
     * @param _type 
     * @param _datetime 
     * @param _value 
     * @return 
     */
    public boolean writeObject(String _type, ATechDate _datetime, String _value)
    {
        if (this.containsKey(_type))
        {
            _value = _value.replace(',', '.');
            DeviceTempValues dtv = this.get(_type);
            
            if (!this.is_silent_mode)
                output_writer.writeData(dtv.getData(_datetime, _value));
            return true;
        }
        else
        {
            log.warn("Unknown key:" + _type);
            return false;
        }
    }


    
    /**
     * Write Object 
     * 
     * @param _type 
     * @param _datetime 
     * @param code_type 
     * @param _value 
     * @return 
     */
    public boolean writeObject(String _type, ATechDate _datetime, int code_type, String _value)
    {
        if (this.containsKey(_type))
        {
            if (_value!=null)
                _value = _value.replace(',', '.');
            DeviceTempValues dtv = this.get(_type);

            if (!this.is_silent_mode)
                output_writer.writeData(dtv.getData(_datetime, code_type, _value));
            return true;
        }
        else
        {
            log.warn("Unknown key:" + _type);
            return false;
        }
    }
    
    
    
}	
