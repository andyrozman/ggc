package ggc.cgms.data.writer;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.cgms.data.CGMSValuesExtendedEntry;
import ggc.cgms.data.defs.CGMSExtendedDataType;
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
 *  Filename:     PumpTempValues
 *  Description:  Class for Writer
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSWriterValues extends DeviceTempValues
{

//    public CGMSWriterValues(int _object_type, int _base_type, int _sub_type, Boolean isNumericValue)
//    {
//        this(_object_type, _base_type, _sub_type, null, isNumericValue);
//    }
//
//
//    public CGMSWriterValues(int _object_type, CodeEnumWithTranslation _base_type, int _sub_type, Boolean isNumericValue)
//    {
//        this(_object_type, _base_type.getCode(), _sub_type, null, isNumericValue);
//    }


    public CGMSWriterValues(int objectType, CodeEnumWithTranslation _base_type, CodeEnumWithTranslation _sub_type, Boolean isNumericValue)
    {
        this(objectType, _base_type.getCode(), _sub_type.getCode(), null, isNumericValue);
    }


//    public CGMSWriterValues(int _object_type, int _base_type, Boolean isNumericValue)
//    {
//        this(_object_type, _base_type, 0, null, isNumericValue);
//    }


    public CGMSWriterValues(int objectType, CodeEnumWithTranslation _base_type, Boolean isNumericValue)
    {
        this(objectType, _base_type.getCode(), 0, null, isNumericValue);
    }


    public CGMSWriterValues(int objectType, int _base_type, int _sub_type, String _value_template, Boolean isNumericValue)
    {
        super(objectType, _base_type, _sub_type, _value_template, isNumericValue);
    }


    /**
     * CGMS Object: Extended
     */
    public static final int OBJECT_EXT = 2;




    /*
     * public void writeObject(OutputWriter ow, String _value)
     * {
     * }
     */

    /**
     * getData
     */
    @Override
    public OutputWriterData getData(ATechDate dt, Integer _sub_type, String _value)
    {
        String val = _value;
        Integer stype = _sub_type;

        if (stype == null)
        {
            stype = this.sub_type;
        }

        if (val == null)
        {
            val = "";
        }


        if (this.object_type == CGMSWriterValues.OBJECT_EXT)
        {
            CGMSValuesExtendedEntry pvex = new CGMSValuesExtendedEntry();
            pvex.setType(CGMSExtendedDataType.getByCode(this.base_type));
            pvex.setDateTimeObject(dt);
            pvex.subType = stype;

            if (this.value_template == null)
            {
                pvex.setValue(val);
            }
            else
            {
                pvex.setValue(this.value_template + "=" + val);
            }

            pvex.prepareEntry_v2();

            System.out.println("pvex: " + pvex.toString());

            return pvex;
        }

        return null;
    }

    @Override
    public OutputWriterData getData(ATechDate dt, Integer _sub_type, String _value, String source) {
        return null;
    }

}
