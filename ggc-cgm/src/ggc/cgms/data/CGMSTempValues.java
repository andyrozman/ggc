package ggc.cgms.data;

import com.atech.utils.data.CodeEnum;
import ggc.cgms.data.defs.CGMSBaseDataType;
import ggc.cgms.data.defs.CGMSObject;
import ggc.plugin.data.DeviceTempValues;
import ggc.plugin.output.OutputWriterData;

import com.atech.utils.data.ATechDate;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     PumpTempValues
 *  Description:  Class for Writer 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSTempValues extends DeviceTempValues
{

    /**
     * @param objectType
     * @param baseType
     * @param subType
     */
    public CGMSTempValues(int objectType, int baseType, int subType)
    {

        super(objectType, baseType, subType, null);
    }


    /**
     * @param objectType
     * @param baseType
     * @param subType
     */
    public CGMSTempValues(CGMSObject objectType, CGMSBaseDataType baseType, CodeEnum subType)
    {
        super(objectType.getCode(), baseType.getCode(), subType.getCode(), null);
    }

    /**
     * @param objectType
     * @param baseType
     * @param subType
     * @param _value_template
     */
    public CGMSTempValues(int objectType, int baseType, int subType, String _value_template, Boolean isNumericValue)
    {
        super(objectType, baseType, subType, _value_template, isNumericValue);
    }


    /**
     * @param objectType
     * @param baseType
     * @param subType
     * @param _value_template
     */
    public CGMSTempValues(CGMSObject objectType,  CGMSBaseDataType baseType, int subType, String _value_template, Boolean isNumericValue)
    {
        super(objectType.getCode(), baseType.getCode(), subType, _value_template, isNumericValue);
    }



    /**
     * @param objectType
     * @param baseType
     */
    public CGMSTempValues(int objectType, int baseType)
    {
        super(objectType, baseType);
    }


    /**
     * @param objectType
     * @param baseType
     */
    public CGMSTempValues(CGMSObject objectType,  CGMSBaseDataType baseType)
    {
        super(objectType.getCode(), baseType.getCode());
    }



    /*
     * public void writeObject(OutputWriter ow, String _value)
     * {
     * }
     */

    /** 
     * getData
     */
    @Override
    public OutputWriterData getData(ATechDate dt, int subType, String _value)
    {
        String val = _value;
        int stype = subType;

        if (stype == -1)
        {
            stype = this.sub_type;
        }

        if (val == null)
        {
            val = "";
        }

        if (this.object_type == CGMSObject.Base.getCode())
        {
            CGMSValuesEntry pve = new CGMSValuesEntry();
            /*
             * pve.se.setBaseType(this.base_type);
             * pve.setSubType(stype);
             * pve.setDateTimeObject(dt);
             * if (this.value_template==null)
             * {
             * pve.setValue(val);
             * }
             * else
             * {
             * pve.setValue(this.value_template + "=" + val);
             * }
             */
            return pve;
        }
        /*
         * else if (this.object_type == CGMSTempValues.OBJECT_BASE_SET_SUBTYPE)
         * {
         * PumpValuesEntry pve = new PumpValuesEntry();
         * pve.setBaseType(this.base_type);
         * pve.setSubType(Integer.parseInt(val));
         * pve.setDateTimeObject(dt);
         * return pve;
         * }
         */
        else if (this.object_type == CGMSObject.SubEntry.getCode())
        {

            // SUB_TYPE= ; VALUE=; ISIG=

            /*
             * CGMSValuesSubEntry pvex = new CGMSValuesSubEntry();
             * pvex.setType(this.base_type);
             * pvex.setDateTimeObject(dt);
             * if (this.value_template==null)
             * {
             * pvex.setValue(val);
             * }
             * else
             * {
             * pvex.setValue(this.value_template + "=" + val);
             * }
             * return pvex;
             */
        }

        return null;
    }

}
