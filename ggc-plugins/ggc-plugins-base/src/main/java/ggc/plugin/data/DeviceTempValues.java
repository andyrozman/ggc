package ggc.plugin.data;

import ggc.plugin.output.OutputWriterData;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.CodeEnumWithTranslation;

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
 *  Filename:     DeviceTempValues
 *  Description:  DeviceTempValues for Device Writer.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class DeviceTempValues
{
    protected int object_type = 1;
    protected int base_type = 1;
    protected int sub_type = 0;
    protected String value_template;
    private Boolean isNumericValue;

    /**
     * @param _object_type
     * @param _base_type
     * @param _sub_type
     */
    public DeviceTempValues(int _object_type, int _base_type, int _sub_type)
    {
        this(_object_type, _base_type, _sub_type, null, null);
    }

    public DeviceTempValues(int _object_type, int _base_type, int _sub_type, Boolean isNumericValue)
    {
        this(_object_type, _base_type, _sub_type, null, isNumericValue);
    }

    public DeviceTempValues(int _object_type, CodeEnumWithTranslation _base_type, int _sub_type)
    {
        this(_object_type, _base_type.getCode(), _sub_type, null, null);
    }

    public DeviceTempValues(int _object_type, CodeEnumWithTranslation _base_type, CodeEnumWithTranslation _sub_type)
    {
        this(_object_type, _base_type.getCode(), _sub_type.getCode(), null, null);
    }

    public DeviceTempValues(int _object_type, CodeEnumWithTranslation _base_type, int _sub_type, Boolean isNumericValue)
    {
        this(_object_type, _base_type.getCode(), _sub_type, null, isNumericValue);
    }

    public DeviceTempValues(int _object_type, CodeEnumWithTranslation _base_type, CodeEnumWithTranslation _sub_type,
            Boolean isNumericValue)
    {
        this(_object_type, _base_type.getCode(), _sub_type.getCode(), null, isNumericValue);
    }

    /**
     * @param _object_type
     * @param _base_type
     *
     */
    public DeviceTempValues(int _object_type, int _base_type)
    {
        this(_object_type, _base_type, 0, null, null);
    }

    public DeviceTempValues(int _object_type, CodeEnumWithTranslation _base_type)
    {
        this(_object_type, _base_type.getCode(), 0, null, null);
    }

    public DeviceTempValues(int _object_type, int _base_type, Boolean isNumericValue)
    {
        this(_object_type, _base_type, 0, null, isNumericValue);
    }

    public DeviceTempValues(int _object_type, CodeEnumWithTranslation _base_type, Boolean isNumericValue)
    {
        this(_object_type, _base_type.getCode(), 0, null, isNumericValue);
    }

    /**
     * @param _object_type
     * @param _base_type
     * @param _sub_type
     * @param _value_template
     */
    public DeviceTempValues(int _object_type, int _base_type, int _sub_type, String _value_template,
            Boolean isNumericValue)
    {
        this.object_type = _object_type;
        this.base_type = _base_type;
        this.sub_type = _sub_type;
        this.value_template = _value_template;
        this.isNumericValue = isNumericValue;
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
        return getData(dt, null, _value);
    }

    /**
     * Get Data
     *
     * @param dt
     * @param _sub_type
     * @param _value
     * @return
     */
    public abstract OutputWriterData getData(ATechDate dt, Integer _sub_type, String _value);

    public Boolean getIsNumericValue()
    {
        return isNumericValue;
    }

    public void setIsNumericValue(Boolean isNumericValue)
    {
        this.isNumericValue = isNumericValue;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName() + " [");
        sb.append("base_type=" + this.base_type);

        if (sub_type>0)
            sb.append(", sub_type=" + this.sub_type);

        sb.append(", object_type=" + this.object_type);

        if (value_template!=null)
            sb.append(", value_template=" + value_template);

        if (isNumericValue==null)
            sb.append(", is_numeric_value=" + isNumericValue);

        return sb.toString();
    }
}
