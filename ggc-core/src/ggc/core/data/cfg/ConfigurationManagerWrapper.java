package ggc.core.data.cfg;

import com.atech.graphics.graphs.GraphConfigProperties;
import ggc.core.data.defs.GlucoseUnitType;

/**
 * Created by andy on 10.10.15.
 */
public class ConfigurationManagerWrapper implements GraphConfigProperties
{

    ConfigurationManager configurationManager;


    public ConfigurationManagerWrapper(ConfigurationManager configurationManager)
    {
        this.configurationManager = configurationManager;
    }


    public void saveConfig()
    {
        this.configurationManager.saveConfig();
    }


    private void setStringValue(String key, String stringValue)
    {
        this.configurationManager.setStringValue(key, stringValue);
    }


    private String getStringValue(String key)
    {
        return this.configurationManager.getStringValue(key);
    }


    private void setFloatValue(String key, float floatValue)
    {
        this.configurationManager.setFloatValue(key, floatValue);
    }


    private float getFloatValue(String key)
    {
        return this.configurationManager.getFloatValue(key);
    }


    private void setIntValue(String key, int intValue)
    {
        this.configurationManager.setIntValue(key, intValue);
    }


    private int getIntValue(String key)
    {
        return this.configurationManager.getIntValue(key);
    }


    private boolean getBooleanValue(String key)
    {
        return this.configurationManager.getBooleanValue(key);
    }


    private void setBooleanValue(String key, boolean booleanValue)
    {
        this.configurationManager.setBooleanValue(key, booleanValue);
    }


    /**
     * Get User's Name
     *
     * @return
     */
    public String getUserName()
    {
        return getStringValue("NAME");
    }


    /**
     * Set User's Name
     *
     * @param value
     */
    public void setUserName(String value)
    {
        setStringValue("NAME", value);
    }


    // ---
    // --- Medical Data (Insulins and BG)
    // ---

    /**
     * Get BG: Target High
     *
     * @return
     */
    public float getBGTargetHigh()
    {
        if (this.isGlucoseUnitMgDL())
        {
            return this.getBG1TargetHigh();
        }
        else
        {
            return this.getBG2TargetHigh();
        }
    }


    /**
     * Get BG: Target Low
     *
     * @return
     */
    public float getBGTargetLow()
    {
        if (this.isGlucoseUnitMgDL())
        {
            return this.getBG1TargetLow();
        }
        else
        {
            return this.getBG2TargetLow();
        }
    }


    /**
     * Get BG: High
     *
     * @return
     */
    public float getBG_High()
    {
        if (this.isGlucoseUnitMgDL())
        {
            return this.getBG1High();
        }
        else
        {
            return this.getBG2High();
        }
    }


    /**
     * Get BG: Low
     *
     * @return
     */
    public float getBGLow()
    {
        if (this.isGlucoseUnitMgDL())
        {
            return this.getBG1Low();
        }
        else
        {
            return this.getBG2Low();
        }
    }


    public boolean isGlucoseUnitMgDL()
    {
        return (getGlucoseUnit() == GlucoseUnitType.mg_dL);
    }


    /**
     * Get BG Unit
     *
     * @return
     */
    public GlucoseUnitType getGlucoseUnit()
    {
        return GlucoseUnitType.getByCode(getIntValue("BG_UNIT"));
    }


    /**
     * Set BG Unit
     *
     * @param unitType
     */
    public void setGlucoseUnit(GlucoseUnitType unitType)
    {
        setIntValue("BG_UNIT", unitType.getCode());
    }


    /**
     * Get BG 1 (mg/dL): High
     *
     * @return
     */
    public float getBG1High()
    {
        return getFloatValue("BG1_HIGH");
    }


    /**
     * Set BG 1 (mg/dL): High
     *
     * @param value
     */
    public void setBG1High(float value)
    {
        setFloatValue("BG1_HIGH", value);
    }


    /**
     * Get BG 1 (mg/dL): Low
     *
     * @return
     */
    public float getBG1Low()
    {
        return getFloatValue("BG1_LOW");
    }


    /**
     * Set BG 1 (mg/dL): Low
     *
     * @param value
     */
    public void setBG1Low(float value)
    {
        setFloatValue("BG1_LOW", value);
    }


    /**
     * Get BG 1 (mg/dL): Target High
     *
     * @return
     */
    public float getBG1TargetHigh()
    {
        return getFloatValue("BG1_TARGET_HIGH");
    }


    /**
     * Set BG 1 (mg/dL): Target High
     *
     * @param value
     */
    public void setBG1TargetHigh(float value)
    {
        setFloatValue("BG1_TARGET_HIGH", value);
    }


    /**
     * Get BG 1 (mg/dL): Target Low
     *
     * @return
     */
    public float getBG1TargetLow()
    {
        return getFloatValue("BG1_TARGET_LOW");
    }


    /**
     * Set BG 1 (mg/dL): Target Low
     *
     * @param value
     */
    public void setBG1TargetLow(float value)
    {
        setFloatValue("BG1_TARGET_LOW", value);
    }


    /**
     * Get BG 2 (mmol/L): High
     *
     * @return
     */
    public float getBG2High()
    {
        return getFloatValue("BG2_HIGH");
    }


    /**
     * Set BG 2 (mmol/L): High
     *
     * @param value
     */
    public void setBG2High(float value)
    {
        setFloatValue("BG2_HIGH", value);
    }


    /**
     * Get BG 2 (mmol/L): Low
     *
     * @return
     */
    public float getBG2Low()
    {
        return getFloatValue("BG2_LOW");
    }


    /**
     * Set BG 2 (mmol/L): Low
     *
     * @param value
     */
    public void setBG2Low(float value)
    {
        setFloatValue("BG2_LOW", value);
    }


    /**
     * Get BG 2 (mmol/L): Taget High
     *
     * @return
     */
    public float getBG2TargetHigh()
    {
        return getFloatValue("BG2_TARGET_HIGH");
    }


    /**
     * Set BG 2 (mmol/L): Target High
     *
     * @param value
     */
    public void setBG2TargetHigh(float value)
    {
        setFloatValue("BG2_TARGET_HIGH", value);
    }


    /**
     * Get BG 2 (mmol/L): Target Low
     *
     * @return
     */
    public float getBG2TargetLow()
    {
        return getFloatValue("BG2_TARGET_LOW");
    }


    /**
     * Set BG 2 (mmol/L): Target Low
     *
     * @param value
     */
    public void setBG2TargetLow(float value)
    {
        setFloatValue("BG2_TARGET_LOW", value);
    }


    /**
     * Get Insulin 1 Abbreviation
     *
     * @return
     */
    public String getIns1Abbr()
    {
        return getStringValue("INS1_ABBR");
    }


    /**
     * Set Insulin 1 Abbreviation
     *
     * @param ins1Abbr
     */
    public void setIns1Abbr(String ins1Abbr)
    {
        setStringValue("INS1_ABBR", ins1Abbr);
    }


    /**
     * Get Insulin 1 Name
     *
     * @return
     */
    public String getIns1Name()
    {
        return getStringValue("INS1_NAME");
    }


    /**
     * Set Insulin 1 Name
     *
     * @param ins1Name
     */
    public void setIns1Name(String ins1Name)
    {
        setStringValue("INS1_NAME", ins1Name);
    }


    /**
     * Get Insulin 1 Type
     *
     * @return
     */
    public int getIns1Type()
    {
        return getIntValue("INS1_TYPE");
    }


    /**
     * Set Insulin 1 Type
     *
     * @param ins
     */
    public void setIns1Type(int ins)
    {
        setIntValue("INS1_TYPE", ins);
    }


    /**
     * Get Insulin 2 Abbreviation
     *
     * @return
     */
    public String getIns2Abbr()
    {
        return getStringValue("INS2_ABBR");
    }


    /**
     * Set Insulin 2 Abbreviation
     *
     * @param ins2Abbr
     */
    public void setIns2Abbr(String ins2Abbr)
    {
        setStringValue("INS2_ABBR", ins2Abbr);
    }


    /**
     * Get Insulin 2 Name
     *
     * @return
     */
    public String getIns2Name()
    {
        return getStringValue("INS2_NAME");
    }


    /**
     * Set Insulin 2 Name
     *
     * @param ins2Name
     */
    public void setIns2Name(String ins2Name)
    {
        setStringValue("INS2_NAME", ins2Name);
    }


    /**
     * Get Insulin 2 Type
     *
     * @return
     */
    public int getIns2Type()
    {
        return getIntValue("INS2_TYPE");
    }


    /**
     * Set Insulin 2 Type
     *
     * @param ins
     */
    public void setIns2Type(int ins)
    {
        setIntValue("INS2_TYPE", ins);
    }


    /**
     * Get Insulin 3 Abbreviation
     *
     * @return
     */
    public String getIns3Abbr()
    {
        return getStringValue("INS3_ABBR");
    }


    /**
     * Set Insulin 3 Abbreviation
     *
     * @param ins3Abbr
     */
    public void setIns3Abbr(String ins3Abbr)
    {
        setStringValue("INS3_ABBR", ins3Abbr);
    }


    /**
     * Get Insulin 3 Name
     *
     * @return
     */
    public String getIns3Name()
    {
        return getStringValue("INS3_NAME");
    }


    /**
     * Set Insulin 3 Name
     *
     * @param ins3Name
     */
    public void setIns3Name(String ins3Name)
    {
        setStringValue("INS3_NAME", ins3Name);
    }


    /**
     * Get Insulin 3 Type
     *
     * @return
     */
    public int getIns3Type()
    {
        return getIntValue("INS3_TYPE");
    }


    /**
     * Set Insulin 3 Type
     *
     * @param ins
     */
    public void setIns3Type(int ins)
    {
        setIntValue("INS3_TYPE", ins);
    }


    /**
     * Get Pump Insulin Name
     *
     * @return
     */
    public String getPumpInsulin()
    {
        return getStringValue("PUMP_INSULIN");
    }


    /**
     * Set Pump Insulin Name
     *
     * @param pumpInsulin
     */
    public void setPumpInsulin(String pumpInsulin)
    {
        setStringValue("PUMP_INSULIN", pumpInsulin);
    }


    // ---
    // --- Graphs
    // ---

    /**
     * Get AntiAliasing
     */
    public int getAntiAliasing()
    {
        return getIntValue("RENDER_ANTIALIASING");
    }


    /**
     * Set AntiAliasing
     *
     * @param value
     */
    public void setAntiAliasing(int value)
    {
        setIntValue("RENDER_ANTIALIASING", value);
    }


    /**
     * Get Color Rendering
     */
    public int getColorRendering()
    {
        return getIntValue("RENDER_COLOR_RENDERING");
    }


    /**
     * Set Color Rendering
     *
     * @param value
     */
    public void setColorRendering(int value)
    {
        setIntValue("RENDER_COLOR_RENDERING", value);
    }


    /**
     * Get Dithering
     */
    public int getDithering()
    {
        return getIntValue("RENDER_DITHERING");
    }


    /**
     * Set Dithering
     *
     * @param value
     */
    public void setDithering(int value)
    {
        setIntValue("RENDER_DITHERING", value);
    }


    /**
     * Get Fractional Meetrics
     */
    public int getFractionalMetrics()
    {
        return getIntValue("RENDER_FRACTIONAL_METRICS");
    }


    /**
     * Set Fractional Meetrics
     *
     * @param value
     */
    public void setFractionalMetrics(int value)
    {
        setIntValue("RENDER_FRACTIONAL_METRICS", value);
    }


    /**
     * Get Interpolation
     */
    public int getInterpolation()
    {
        return getIntValue("RENDER_INTERPOLATION");
    }


    /**
     * Set Interpolation
     *
     * @param value
     */
    public void setInterpolation(int value)
    {
        setIntValue("RENDER_INTERPOLATION", value);
    }


    /**
     * Get Rendering
     */
    public int getRendering()
    {
        return getIntValue("RENDER_RENDERING");
    }


    /**
     * Set Rendering
     *
     * @param value
     */
    public void setRendering(int value)
    {
        setIntValue("RENDER_RENDERING", value);
    }


    /**
     * Get Text Antialiasing
     */
    public int getTextAntiAliasing()
    {
        return getIntValue("RENDER_TEXT_ANTIALIASING");
    }


    /**
     * Set Text Antialiasing
     *
     * @param value
     */
    public void setTextAntiAliasing(int value)
    {
        setIntValue("RENDER_TEXT_ANTIALIASING", value);
    }


    /**
     * Get Selected Color Scheme
     *
     * @return
     */
    public String getSelectedColorScheme()
    {
        return getStringValue("SELECTED_COLOR_SCHEME");
    }


    /**
     * Set Selected Color Scheme
     *
     * @param selectedColorScheme
     */
    public void setSelectedColorScheme(String selectedColorScheme)
    {
        setStringValue("SELECTED_COLOR_SCHEME", selectedColorScheme);
    }


    // ---
    // --- Printing
    // ---

    /**
     * Get PDF Viewer Path
     *
     * @return
     */
    public String getExternalPdfVieverPath()
    {
        return getStringValue("PRINT_PDF_VIEWER_PATH");
    }


    /**
     * Set PDF Viewer Path
     *
     * @param pdfViewerPath
     */
    public void setExternalPdfVieverPath(String pdfViewerPath)
    {
        setStringValue("PRINT_PDF_VIEWER_PATH", pdfViewerPath);
    }


    /**
     * Get PDF Viewer Path
     *
     * @return
     */
    public String getExternalPdfVieverParameters()
    {
        return getStringValue("PRINT_PDF_VIEWER_PARAMETERS");
    }


    /**
     * Set PDF Viewer Path
     *
     * @param pdfViewerParameters
     */
    public void setExternalPdfVieverParameters(String pdfViewerParameters)
    {
        setStringValue("PRINT_PDF_VIEWER_PARAMETERS", pdfViewerParameters);
    }


    /**
     * Get PDF Viewer Path
     *
     * @return
     */
    public boolean getUseExternalPdfViewer()
    {
        return getBooleanValue("PRINT_USE_EXTERNAL_PDF_VIEWER");
    }


    /**
     * Set PDF Viewer Path
     *
     * @param value
     */
    public void setUseExternalPdfViewer(boolean value)
    {
        setBooleanValue("PRINT_USE_EXTERNAL_PDF_VIEWER", value);
    }


    /**
     * Get Print Empty Value
     *
     * @return
     */
    public String getPrintEmptyValue()
    {
        return getStringValue("PRINT_EMPTY_VALUE");
    }


    /**
     * Set Print Empty Value
     *
     * @param emptyValue
     */
    public void setPrintEmptyValue(String emptyValue)
    {
        setStringValue("PRINT_EMPTY_VALUE", emptyValue);
    }


    /**
     * Get Print Start Time: Lunch
     *
     * @return
     */
    public int getPrintLunchStartTime()
    {
        return getIntValue("PRINT_LUNCH_START_TIME");
    }


    /**
     * Set Print Start Time: Lunch
     *
     * @param value
     */
    public void setPrintLunchStartTime(int value)
    {
        setIntValue("PRINT_LUNCH_START_TIME", value);
    }


    /**
     * Get Print Start Time: Dinner
     *
     * @return
     */
    public int getPrintDinnerStartTime()
    {
        return getIntValue("PRINT_DINNER_START_TIME");
    }


    /**
     * Set Print Start Time: Dinner
     *
     * @param value
     */
    public void setPrintDinnerStartTime(int value)
    {
        setIntValue("PRINT_DINNER_START_TIME", value);
    }


    /**
     * Get Print Start Time: Night
     *
     * @return
     */
    public int getPrintNightStartTime()
    {
        return getIntValue("PRINT_NIGHT_START_TIME");
    }


    /**
     * Set Print Start Time: Night
     *
     * @param value
     */
    public void setPrintNightStartTime(int value)
    {
        setIntValue("PRINT_NIGHT_START_TIME", value);
    }


    // ---
    // --- Ratios
    // ---

    /**
     * Get Ratio CH/Insulin
     *
     * @return
     */
    public float getRatioCHInsulin()
    {
        return getFloatValue("RATIO_CH_INSULIN");
    }


    /**
     * Set Ratio CH/Insulin
     *
     * @param val
     */
    public void setRatioCHInsulin(float val)
    {
        setFloatValue("RATIO_CH_INSULIN", val);
    }


    /**
     * Get Ratio BG/Insulin
     *
     * @return
     */
    public float getRatioBGInsulin()
    {
        return getFloatValue("RATIO_BG_INSULIN");
    }


    /**
     * Set Ratio BG/Insulin
     *
     * @param val
     */
    public void setRatioBGInsulin(float val)
    {
        setFloatValue("RATIO_BG_INSULIN", val);
    }


    /**
     * Get Ratio Mode
     *
     * @return
     */
    public String getRatioMode()
    {
        return getStringValue("RATIO_MODE");
    }


    /**
     * Set Ratio Mode
     *
     * @param val
     */
    public void setRatioMode(String val)
    {
        setStringValue("RATIO_MODE", val);
    }


    public void setLastTotalDailyDose(float lastTotalDailyDose)
    {
        setFloatValue("LAST_TDD", lastTotalDailyDose);
    }


    public float getLastTotalDailyDose()
    {
        return getFloatValue("LAST_TDD");
    }


    public void setInsulinCarbRule(String insulinCarbRule)
    {
        setStringValue("INS_CARB_RULE", insulinCarbRule);
    }


    public String getInsulinCarbRule()
    {
        return getStringValue("INS_CARB_RULE");
    }


    public void setSensitivityRule(String sensitivityRule)
    {
        setStringValue("SENSITIVITY_RULE", sensitivityRule);
    }


    public String getSensitivityRule()
    {
        return getStringValue("SENSITIVITY_RULE");
    }


    public String getPenBasalPrecision()
    {
        return getStringValue("PEN_BASAL_PRECISSION");
    }


    public void setPenBasalPrecision(String penBasalPrecision)
    {
        setStringValue("PEN_BASAL_PRECISSION", penBasalPrecision);
    }


    public String getPenBolusPrecision()
    {
        return getStringValue("PEN_BOLUS_PRECISSION");
    }


    public void setPenBolusPrecision(String penBolusPrecision)
    {
        setStringValue("PEN_BOLUS_PRECISSION", penBolusPrecision);
    }


    public void setSoftwareModeDescription(String softwareModeDescription)
    {
        setStringValue("SW_MODE_DESC", softwareModeDescription);
    }


    public String getSoftwareModeDescription()
    {
        return getStringValue("SW_MODE_DESC");
    }


    public void setSoftwareMode(int softwareMode)
    {
        setIntValue("SW_MODE", softwareMode);
    }


    public int getSoftwareMode()
    {
        return getIntValue("SW_MODE");
    }


    public float getPenMaxBasal()
    {
        return getFloatValue("PEN_MAX_BASAL");
    }


    public void setPenMaxBasal(float penMaxBasal)
    {
        setFloatValue("PEN_MAX_BASAL", penMaxBasal);
    }


    public float getPenMaxBolus()
    {
        return getFloatValue("PEN_MAX_BOLUS");
    }


    public void setPenMaxBolus(float penMaxBolus)
    {
        setFloatValue("PEN_MAX_BOLUS", penMaxBolus);
    }


    public float getPumpMaxBasal()
    {
        return getFloatValue("PUMP_MAX_BASAL");
    }


    public void setPumpMaxBasal(float pumpMaxBasal)
    {
        setFloatValue("PUMP_MAX_BASAL", pumpMaxBasal);
    }


    public String getPumpBolusPrecision()
    {
        return getStringValue("PUMP_BOLUS_PRECISSION");
    }


    public void setPumpBolusPrecision(String pumpBolusPrecision)
    {
        setStringValue("PUMP_BOLUS_PRECISSION", pumpBolusPrecision);
    }


    public String getPumpBasalPrecision()
    {
        return getStringValue("PUMP_BASAL_PRECISSION");
    }


    public void setPumpBasalPrecision(String pumpBasalPrecision)
    {
        setStringValue("PUMP_BASAL_PRECISSION", pumpBasalPrecision);
    }


    public float getPumpMaxBolus()
    {
        return getFloatValue("PUMP_MAX_BOLUS");
    }


    public void setPumpMaxBolus(float pumpMaxBolus)
    {
        setFloatValue("PUMP_MAX_BOLUS", pumpMaxBolus);
    }


    public int getPumpTBRType()
    {
        return getIntValue("PUMP_TBR_TYPE");
    }


    public void setPumpTBRType(int pumpTBRType)
    {
        setIntValue("PUMP_TBR_TYPE", pumpTBRType);
    }


    public float getTBRUnitMin()
    {
        return getFloatValue("PUMP_UNIT_MIN");
    }


    public void setTBRUnitMin(float TBRUnitMin)
    {
        setFloatValue("PUMP_UNIT_MIN", TBRUnitMin);
    }


    public float getTBRUnitMax()
    {
        return getFloatValue("PUMP_UNIT_MAX");
    }


    public void setTBRUnitMax(float TBRUnitMax)
    {
        setFloatValue("PUMP_UNIT_MAX", TBRUnitMax);
    }


    public float getTBRUnitStep()
    {
        return getFloatValue("PUMP_UNIT_STEP");
    }


    public void setTBRUnitStep(float TBRUnitStep)
    {
        setFloatValue("PUMP_UNIT_STEP", TBRUnitStep);
    }


    public float getTBRProcentMin()
    {
        return getFloatValue("PUMP_PROC_MIN");
    }


    public void setTBRProcentMin(float TBRProcentMin)
    {
        setFloatValue("PUMP_PROC_MIN", TBRProcentMin);
    }


    public float getTBRProcentMax()
    {
        return getFloatValue("PUMP_PROC_MAX");
    }


    public void setTBRProcentMax(float TBRProcentMax)
    {
        setFloatValue("PUMP_PROC_MAX", TBRProcentMax);
    }


    public float getTBRProcentStep()
    {
        return getFloatValue("PUMP_PROC_STEP");
    }


    public void setTBRProcentStep(float TBRProcentStep)
    {
        setFloatValue("PUMP_PROC_STEP", TBRProcentStep);
    }


    public boolean getUseCGMSDataInPumpDailyGraph()
    {
        return getBooleanValue("USE_CGMS_DATA_IN_DAILY_PUMP_DISPLAY");
    }


    public void setUseCGMSDataInPumpDailyGraph(boolean useCGMSDataInPumpDailyGraph)
    {
        setBooleanValue("USE_CGMS_DATA_IN_DAILY_PUMP_DISPLAY", useCGMSDataInPumpDailyGraph);
    }


    public boolean getUseCGMSDataInPenDailyGraph()
    {
        return getBooleanValue("USE_CGMS_DATA_IN_DAILY_PEN_DISPLAY");
    }


    public void setUseCGMSDataInPenDailyGraph(boolean useCGMSDataInPenDailyGraph)
    {
        setBooleanValue("USE_CGMS_DATA_IN_DAILY_PEN_DISPLAY", useCGMSDataInPenDailyGraph);
    }

}
