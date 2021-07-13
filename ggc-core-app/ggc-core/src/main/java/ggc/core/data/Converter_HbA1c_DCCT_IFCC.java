package ggc.core.data;

import com.atech.misc.converter.ATechConverter;

import ggc.core.data.defs.HbA1cType;

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

public class Converter_HbA1c_DCCT_IFCC extends ATechConverter
{

    /**
     * Unit: mg/dL
     */
    public static final int UNIT_DCCT = 1;

    /**
     * Unit: mmol/L
     */
    public static final int UNIT_IFFC = 2;

    public static final int UNIT_eAG = 3;


    // http://www.ngsp.org/ifccngsp.asp

    // private static final float MGDL_TO_MMOL_FACTOR = 0.0555f;

    // private static final float MMOL_TO_MGDL_FACTOR = 18.016f;

    /**
     *
     */
    public Converter_HbA1c_DCCT_IFCC()
    {
        super(UNIT_DCCT, // unit #1
                UNIT_IFFC, // unit #2
                ATechConverter.BASETYPE_FLOAT, // unit #1 type
                ATechConverter.BASETYPE_INT, // unit #2 type
                "%", // unit #1 name
                "mmol/mol", // unit #2 name
                null, // factor 1 -> 2
                null, // factor 2 -> 1
                1, // decimal precission (nr. of decimals) unit #1
                0); // decimal precission (nr. of decimals) unit #2

    }


     public float getValueByType(HbA1cType inputType, HbA1cType outputType, Number value)
     {
         if (inputType == outputType)
             return value.floatValue();

         float valueOut = 0.0f;


         switch(outputType)
         {
             case NGSP_Percent:
             {
                 switch(inputType)
                 {
                     case IFCC_mmolmol:
                         valueOut = (0.09148f*value.floatValue()) + 2.152f;
                         break;
                     case eAG_mgdL:
                         break;
                     case eAG_mmolL:
                         break;
                     case JDS:
                         valueOut = (0.09274f*value.floatValue()) +1.724f;
                         break;
                     case MonoS:
                         valueOut = (0.09274f*value.floatValue()) +1.724f;
                         break;
                 }



             } break;

             case IFCC_mmolmol:
             {
                 switch(inputType)
                 {

                     case NGSP_Percent:
                         valueOut = (10.93f*value.floatValue()) - 23.50f;
                         break;

                     case eAG_mgdL:
                         break;
                     case eAG_mmolL:
                         break;
                     case JDS:
                         break;
                     case MonoS:
                         break;
                 }
             } break;

             case eAG_mgdL:
             {

                 switch(inputType)
                 {

                     case NGSP_Percent:
                         break;
                     case IFCC_mmolmol:
                         break;

                     case eAG_mmolL:
                         break;
                     case JDS:
                         break;
                     case MonoS:
                         break;
                 }




             } break;
             case eAG_mmolL:
             {

                 switch(inputType)
                 {

                     case NGSP_Percent:
                         break;
                     case IFCC_mmolmol:
                         break;

                     case eAG_mgdL:
                         break;
                     case JDS:
                         break;
                     case MonoS:
                         break;
                 }




             } break;
             case JDS:
             {
                 switch(inputType)
                 {

                     case NGSP_Percent:
                         break;
                     case IFCC_mmolmol:
                         valueOut = (10.78f*value.floatValue()) - 18.59f;
                         break;
                     case eAG_mgdL:
                     case eAG_mmolL:
                     {

                     }
                         break;

                     case MonoS:
                         break;
                 }
             } break;
             case MonoS:
             {
                 switch(inputType)
                 {

                     case NGSP_Percent:
                         break;
                     case IFCC_mmolmol:
                         break;
                     case eAG_mgdL:
                         break;
                     case eAG_mmolL:
                         break;
                     case JDS:
                         break;

                 }
             } break;

         }




//         if (outputType==HbA1cType.DCCT_Percent)
//         {
//             // NGSP = [0.9148 * IFCC] + 2.152) mgdL
//             // NGSP = [0.09148 * IFCC] + 2.152). mmol/L
//             if (inputType==HbA1cType.IFCC_mmolmol)
//             {
//                 valueOut = (0.09148f * value.floatValue()) + 2.152f;
//             }
//             else // HbA1cType.eAG_mgdL
//             {
//                 valueOut = (0.9148f * value.floatValue()) + 2.152f;
//             }
//         }
//         else if (outputType==HbA1cType.IFCC_mmolmol)
//         {
//             if (inputType==HbA1cType.DCCT_Percent)
//             {
//
//             }
//             else // HbA1cType.eAG_mgdL
//             {
//
//             }
//
//         }
//         else if (outputType==HbA1cType.eAG_mgdL)
//         {
//             if (inputType==HbA1cType.DCCT_Percent)
//             {
//
//             }
//             else // HbA1cType.IFCC_mmolmol
//             {
//
//             }
//
//
//
//         }

         return getDecimaledFloat(valueOut, outputType.getValuePrecision().getDecimalPlaces());
     //return this.getValueByType(getType(inputType), getType(outputType), value.floatValue());
     }

    public float getValueByType(int input_type, int output_type, float value)
    {

        if (input_type == output_type)
            return value;

        float factor_to_source = 0.0f;
        float factor_to_target = 0.0f;
        @SuppressWarnings("unused")
        int source_type = 0;
        int source_precission = 0;
        int target_precission = 0;
        int target_type = 0;

        // NGSP = [0.9148 * IFCC] + 2.152) mgdL
        // NGSP = [0.09148 * IFCC] + 2.152). mmol/L
        if (output_type == this.unit_1) // %
        {
            // IFCC-HbA1c (mmol/mol) = [DCCT-HbA1c (%) - 2.15] x 10.929

            float res = (value / 10.929f) + 2.15f;

            return Float.parseFloat(getFormatedFloat(res, 1));

            // target_type = this.unit1_type;
            // target_precission = unit1_type_precission;
            // source_type = this.unit_2;
            // source_precission = unit2_type_precission;
            // factor_to_target = unit2_to_unit1_factor;
            // factor_to_source = unit1_to_unit2_factor;
        }
        else
        {
            float res = (value - 2.15f) * 10.929f;
            // IFCC-HbA1c (mmol/mol) = [DCCT-HbA1c (%) - 2.15] x 10.929

            return Float.parseFloat(getFormatedFloat(res, 0));
        }

        // String input_v = getFormatedFloat(value, source_precission);
        //
        // debug("input_v: " + input_v);
        //
        // float val = value * factor_to_target;
        // float val_source = Float.parseFloat(input_v.replace(",", "."));
        //
        // if (target_type == BASETYPE_INT)
        // {
        // int vali = (int) val;
        //
        // float step = getStep(target_type, target_precission);
        // float last_step = 0.0f; // vali-step;
        //
        // for (float i = vali - step; i < vali + 20; i += step)
        // {
        // if (last_step == 0.0f)
        // {
        // last_step = i;
        // }
        //
        // float v2 = i * factor_to_source;
        // // v2 = Math.round(v2);
        // String s = getFormatedFloat(v2, source_precission);
        //
        // debug("Compare[s]: i=" + i + " val: " + input_v + " ?= " + s);
        // debug("Compare[f]: i=" + i + " val: " + vali + " ?= " + v2);
        //
        // if (s.equals(input_v))
        // return i;
        //
        // if (v2 > val_source)
        // {
        // debug("Calculated value has overriden. New value: " + v2 + " source value: " +
        // val_source);
        // return last_step;
        // }
        //
        // last_step = i;
        // }
        //
        // return 0.0f;
        // }
        // else
        // // UNIT_FLOAT
        // {
        // float vali = val;
        //
        // float step = getStep(target_type, target_precission);
        // float last_step = 0.0f; // getDecimaledFloat(vali,
        // // target_precission)-step;
        // // float val_source = Float.parseFloat(input_v);
        //
        // for (float i = vali - step; i < vali + 20; i += step)
        // {
        // if (last_step == 0.0f)
        // {
        // last_step = i;
        // }
        //
        // i = getDecimaledFloat(i, target_precission);
        // float v2 = i * factor_to_source;
        //
        // String s = getFormatedFloat(v2, source_precission);
        //
        // debug("Compare[s]: i=" + i + " val: " + input_v + " ?= " + s);
        // debug("Compare[f]: i=" + i + " val: " + vali + " ?= " + v2);
        //
        // if (s.equals(input_v))
        // return i;
        //
        // if (v2 > val_source)
        // {
        // debug("Calculated value has overriden. New value: " + v2 + " source value: " +
        // val_source);
        // return last_step;
        // }
        //
        // last_step = i;
        //
        // }
        // return 0.0f;
        // }

        // IFCC-HbA1c (mmol/mol) = [DCCT-HbA1c (%) - 2.15] x 10.929

    }


    // public int getType(GlucoseUnitType type)
    // {
    // return type == GlucoseUnitType.mg_dL ? UNIT_mg_dL : UNIT_mmol_L;
    // }

    /**
     * @param args
     */
    public static void main(String args[])
    {
        System.out.println("Convert 66 mmol/mol to 8.2 %");
        Converter_HbA1c_DCCT_IFCC cnv = new Converter_HbA1c_DCCT_IFCC();

        // System.out.println("Value: " + cnv.getBGValueByType(2, 1, 9.3f));
        // System.out.println("Value: " + cnv.getBGValueByType(UNIT_mmol_L,
        // UNIT_mg_dL, 11.6f));
        System.out.println("Value: " + cnv.getValueByType(HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 66));
    }

}
