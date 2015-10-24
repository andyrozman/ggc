package ggc.plugin.data;

import java.util.Hashtable;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import ggc.plugin.output.OutputWriter;

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
    private static final Logger LOG = LoggerFactory.getLogger(DeviceValuesWriter.class);
    boolean debug = false;
    /**
     * Is Silent Mode
     */
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
     * @param is_silent
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


    public void setDebug(boolean dbg)
    {
        this.debug = dbg;
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
        return this.writeObject(_type, _datetime, _value, StringUtils.isNotBlank(_value));
    }


    public boolean writeObject(String _type, ATechDate _datetime)
    {
        return this.writeObject(_type, _datetime, null, false);
    }


    /**
     * Write Object
     * @param _type
     * @param _datetime
     * @param _value
     * @return
     */
    public boolean writeObject(String _type, ATechDate _datetime, String _value, boolean isNumericValue)
    {
        if (this.containsKey(_type))
        {
            DeviceTempValues dtv = this.get(_type);

            if (StringUtils.isNotBlank(_value))
            {
                boolean numeric = false;

                if (dtv.getIsNumericValue() != null)
                {
                    numeric = dtv.getIsNumericValue().booleanValue();
                }
                else
                {
                    numeric = isNumericValue;
                }

                if (numeric)
                {
                    _value = _value.replace(',', '.');
                }
            }

            // if (!this.is_silent_mode)
            {
                output_writer.writeData(dtv.getData(_datetime, _value));
            }
            return true;
        }
        else
        {
            LOG.warn("Unknown key:" + _type);
            return false;
        }
    }


    /**
     * Write Object
     * @param _type
     * @param _datetime
     * @param _value
     * @return
     */
    public boolean writeObject(String _type, ATechDate _datetime, Number _value)
    {
        if (this.containsKey(_type))
        {
            DeviceTempValues dtv = this.get(_type);

            // if (!this.is_silent_mode)
            {
                output_writer.writeData(dtv.getData(_datetime, "" + _value));
            }
            return true;
        }
        else
        {
            LOG.warn("Unknown key:" + _type);
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
            if (_value != null)
            {
                _value = _value.replace(',', '.');
            }
            DeviceTempValues dtv = this.get(_type);

            // if (!this.is_silent_mode)
            {
                output_writer.writeData(dtv.getData(_datetime, code_type, _value));
            }
            return true;
        }
        else
        {
            LOG.warn("Unknown key:" + _type);
            return false;
        }
    }


    public void addConfiguration(String key, DeviceTempValues deviceTempValues)
    {
        if (debug)
        {
            System.out.println("Config [key=" + key + ", DeviceTempValues=" + deviceTempValues.toString() + "]");
        }

        put(key, deviceTempValues);
    }

}
