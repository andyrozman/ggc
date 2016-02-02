package ggc.core.data.graph.v2;

import com.atech.graphics.graphs.v2.defs.GraphValueType;

/**
 * Created by andy on 06.01.16.
 */
public enum GGCGraphValueType implements GraphValueType
{

    BloodGlucose_mmol_L("mmol/L", "#0.0"), //
    BloodGlucose_mg_dL("mg/dL", "#0"), //
    ;

    String unit;
    String formatForValue;


    GGCGraphValueType(String unit, String formatForValue)
    {
        this.unit = unit;
        this.formatForValue = formatForValue;
    }


    public String getUnit()
    {
        return unit;
    }


    public String getFormatForValue()
    {
        return formatForValue;
    }
}
