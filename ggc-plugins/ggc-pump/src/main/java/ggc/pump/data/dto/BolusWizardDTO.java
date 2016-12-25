package main.java.ggc.pump.data.dto;

import com.atech.utils.data.ATechDate;

/**
 * Created by andy on 18.05.15.
 */
public class BolusWizardDTO
{

    // bloodGlucose and bgTarets are in mg/dL
    public Integer bloodGlucose = 0; // mg/dL
    public Integer carbs = 0;
    public String chUnit = "g";

    public Float carbRatio = 0.0f;
    public Float insulinSensitivity = 0.0f;
    public Integer bgTargetLow = 0;
    public Integer bgTargetHigh = 0;
    public Float bolusTotal = 0.0f;
    public Float correctionEstimate = 0.0f;
    public Float foodEstimate = 0.0f;
    public Float unabsorbedInsulin = 0.0f;
    public ATechDate atechDate;


    public String getValue()
    {
        return String.format(
            "BG=%d;CH=%d;CH_UNIT=%s;CH_INS_RATIO=%5.3f;BG_INS_RATIO=%5.3f;"
                    + "BG_TARGET_LOW=%d;BG_TARGET_HIGH=%d;BOLUS_TOTAL=%5.3f;"
                    + "BOLUS_CORRECTION=%5.3f;BOLUS_FOOD=%5.3f;UNABSORBED_INSULIN=%5.3f", //
            bloodGlucose, carbs, chUnit, carbRatio, insulinSensitivity, bgTargetLow, //
            bgTargetHigh, bolusTotal, correctionEstimate, foodEstimate, unabsorbedInsulin);
    }


    public String toString()
    {
        return "BolusWizardDTO [dateTime=" + atechDate.getDateTimeString() + ", " + getValue() + "]";
    }
}
