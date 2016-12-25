package ggc.cgms.data;

import com.atech.data.NotImplementedException;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.CodeEnum;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.cgms.data.defs.CGMSBaseDataType;
import ggc.cgms.data.defs.CGMSObject;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceTempValues;
import ggc.plugin.output.OutputWriterData;

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
 *  Filename:     CGMSTempValues
 *  Description:  Class for Writer 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSTempValues extends DeviceTempValues
{

    public CGMSTempValues(int _object_type, int _base_type, Boolean isNumericValue)
    {
        this(_object_type, _base_type, 0, null, isNumericValue);
    }


    public CGMSTempValues(int _object_type, CodeEnumWithTranslation _base_type, Boolean isNumericValue)
    {
        this(_object_type, _base_type.getCode(), 0, null, isNumericValue);
    }

    // public CGMSTempValues(int _object_type, CodeEnumWithTranslation
    // _base_type,
    // Boolean isNumericValue)
    // {
    // this(_object_type, _base_type.getCode(), _sub_type.getCode(), null,
    // isNumericValue);
    // }

    // public CGMSTempValues(int _object_type, int _base_type, Boolean
    // isNumericValue)
    // {
    // this(_object_type, _base_type, 0, null, isNumericValue);
    // }


    // public CGMSTempValues(int _object_type, CodeEnumWithTranslation
    // _base_type, Boolean isNumericValue)
    // {
    // this(_object_type, _base_type.getCode(), 0, null, isNumericValue);
    // }

    public CGMSTempValues(CGMSObject objectType, CGMSBaseDataType baseType)
    {
        this(objectType.getCode(), baseType.getCode(), 0, null, true);
    }


    /**
     * IMPORTANT: Sub type is written as value and not as real subType.
     *
     * @param objectType
     * @param baseType
     * @param subType
     */
    public CGMSTempValues(CGMSObject objectType, CGMSBaseDataType baseType, CodeEnum subType)
    {
        this(objectType.getCode(), baseType.getCode(), 0, "" + subType.getCode(), true);
    }


    // not used here
    public CGMSTempValues(CGMSObject objectType, CGMSBaseDataType baseType, Boolean isNumericValue)
    {
        this(objectType.getCode(), baseType.getCode(), 0, null, isNumericValue);
    }


    // not used here
    public CGMSTempValues(int objectType, int baseType, int subType, String _value_template, Boolean isNumericValue)
    {
        super(objectType, baseType, subType, _value_template, isNumericValue);
    }


    /**
     * @param objectType
     * @param baseType
     *
     * @param _value_template
     */
    public CGMSTempValues(CGMSObject objectType, CGMSBaseDataType baseType, String _value_template,
            Boolean isNumericValue)
    {
        super(objectType.getCode(), baseType.getCode(), 0, _value_template, isNumericValue);
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
    // public CGMSTempValues(CGMSObject objectType, CGMSBaseDataType baseType)
    // {
    // super(objectType.getCode(), baseType.getCode());
    // }


    /*
     * public void writeObject(OutputWriter ow, String _value)
     * {
     * }
     */

    /** 
     * getData
     */
    @Override
    public OutputWriterData getData(ATechDate dt, Integer subType, String _value)
    {
        String val = _value;
        Integer stype = subType;

        if (stype == null)
        {
            stype = this.sub_type;
        }

        // if (val == null)
        // {
        // val = "";
        // }

        if (this.object_type == CGMSObject.Base.getCode())
        {
            new NotImplementedException(this.getClass(), "getData() with objectType CGMSObject.Base.");
            // CGMSValuesEntry cve = new CGMSValuesEntry();
            //
            // cve.setType(this.base_type);
            // //cve.set.setSubType(stype);
            // cve.setDateTimeObject(dt);
            //
            // cve.va
            //
            // if (this.value_template==null)
            // {
            // cve.set.setValue(val);
            // }
            // else
            // {
            // pve.setValue(this.value_template + "=" + val);
            // }
            //
            // return pve;
        }
        else if (this.object_type == CGMSObject.SubEntry.getCode())
        {
            CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
            sub.setDateTimeObject(dt);
            sub.setType(this.base_type);
            sub.setSource(DataAccessCGMS.getInstance().getSourceDevice());

            if (val == null)
            {
                if (subType != null)
                    sub.value = subType;
                else
                    sub.value = 0;
            }
            else
            {
                sub.value = Integer.parseInt(val);
            }

            // if (this.value_template==null)
            // {
            //
            // }
            // else
            // {
            // sub.setValue(this.value_template + "=" + val);
            // }

            return sub;

        }

        return null;
    }


    private void test()
    {
        // String val = _value;
        // int stype = _sub_type;
        //
        // if (stype == -1)
        // {
        // stype = this.sub_type;
        // }
        //
        // if (val == null)
        // {
        // val = "";
        // }
        //
        // if (this.object_type == PumpTempValues.OBJECT_BASE)
        // {
        // PumpValuesEntry pve = new PumpValuesEntry();
        // pve.setBaseType(this.base_type);
        // pve.setSubType(stype);
        // pve.setDateTimeObject(dt);
        //
        // if (this.value_template == null)
        // {
        // pve.setValue(val);
        // }
        // else
        // {
        // pve.setValue(this.value_template + "=" + val);
        // }
        //
        // return pve;
        // }
        // else if (this.object_type == PumpTempValues.OBJECT_BASE_SET_SUBTYPE)
        // {
        // PumpValuesEntry pve = new PumpValuesEntry();
        // pve.setBaseType(this.base_type);
        // pve.setSubType(Integer.parseInt(val));
        // pve.setDateTimeObject(dt);
        //
        // return pve;
        // }
        // else if (this.object_type == PumpTempValues.OBJECT_EXT)
        // {
        // PumpValuesEntryExt pvex = new PumpValuesEntryExt();
        // pvex.setType(this.base_type);
        // pvex.setDateTimeObject(dt);
        //
        // if (this.value_template == null)
        // {
        // pvex.setValue(val);
        // }
        // else
        // {
        // pvex.setValue(this.value_template + "=" + val);
        // }
        //
        // return pvex;
        // }
        //
        // return null;

    }

}
