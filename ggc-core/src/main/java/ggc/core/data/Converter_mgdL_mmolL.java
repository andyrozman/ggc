package ggc.core.data;

import com.atech.misc.converter.ATechConverter;
import ggc.core.data.defs.GlucoseUnitType;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     Converter_mgdL_mmolL
 *  Description:  mg/dL -> mmol/L converter.
 *
 *  Author:    Andy {andy@atech-software.com}
 */

public class Converter_mgdL_mmolL extends ATechConverter
{

    /**
     * Unit: mg/dL
     */
    public static final int UNIT_mg_dL = 1;

    /**
     * Unit: mmol/L
     */
    public static final int UNIT_mmol_L = 2;


    private static final float MGDL_TO_MMOL_FACTOR = 0.0555f;

    private static final float MMOL_TO_MGDL_FACTOR = 18.016f;

    /**
     * 
     */
    public Converter_mgdL_mmolL()
    {
        super(UNIT_mg_dL, // unit #1
                UNIT_mmol_L, // unit #2
                ATechConverter.BASETYPE_INT, // unit #1 type
                ATechConverter.BASETYPE_FLOAT, // unit #2 type
                "mmol/L", // unit #1 name
                "mg/dL", // unit #2 name
                MGDL_TO_MMOL_FACTOR, // factor 1 -> 2
                MMOL_TO_MGDL_FACTOR, // factor 2 -> 1
                0, // decimal precission (nr. of decimals) unit #1
                1); // decimal precission (nr. of decimals) unit #2

    }


    public float getValueByType(GlucoseUnitType inputType, GlucoseUnitType outputType, Number value)
    {
        return this.getValueByType(getType(inputType), getType(outputType), value.floatValue());
    }


    public int getType(GlucoseUnitType type)
    {
        return type == GlucoseUnitType.mg_dL ? UNIT_mg_dL : UNIT_mmol_L;
    }


    /**
     * @param args
     */
    public static void main(String args[])
    {
        System.out.println("Convert 9.3 mmol/L to 167 mg/dL");
        Converter_mgdL_mmolL cnv = new Converter_mgdL_mmolL();

        // System.out.println("Value: " + cnv.getBGValueByType(2, 1, 9.3f));
        // System.out.println("Value: " + cnv.getBGValueByType(UNIT_mmol_L,
        // UNIT_mg_dL, 11.6f));
        System.out.println("Value: " + cnv.getValueByType(UNIT_mg_dL, UNIT_mmol_L, 209));
    }

}
