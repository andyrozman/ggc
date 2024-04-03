package ggc.cgms.data.defs;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 * Created by andy on 13/11/17.
 */
public enum CGMSViewerFilter implements CodeEnumWithTranslation
{
    All(0, "FILTER_ALL"),
    SensorReadings(1, "CGMS_VIEWER_FILTER_SENSOR_READINGS"), //
    SensorCalibrations(2, "CGMS_VIEWER_FILTER_CALIBRATIONS"), //
    Alarms(3, "CGMS_VIEWER_FILTER_ALARMS"), //
    Events(4, "CGMS_VIEWER_FILTER_EVENTS"), //
    Errors(5, "CGMS_VIEWER_FILTER_ERRORS"), //
    SensorTrends(6, "CGMS_VIEWER_FILTER_TRENDS"), //
    TransmiterEvent(7, "CGMS_VIEWER_FILTER_TRANS_EVENTS"), //
    AdditionalData(8, "CGMS_VIEWER_FILTER_ADDITIONAL_DATA"), //

    ;

    static Hashtable<String, CGMSViewerFilter> translationMapping = new Hashtable<String, CGMSViewerFilter>();
    static Hashtable<Integer, CGMSViewerFilter> codeMapping = new Hashtable<Integer, CGMSViewerFilter>();

    static
    {
        for (CGMSViewerFilter pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (CGMSViewerFilter pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
        }

        translated = true;
    }

    int code;
    String i18nKey;
    String translation;
    static boolean translated = false;


    CGMSViewerFilter(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public String getTranslation()
    {
        return translation;
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public int getCode()
    {
        return code;
    }


    public String getI18nKey()
    {
        return i18nKey;
    }


    public String getName()
    {
        return this.name();
    }



    public CGMSViewerFilter getByDescription(String description)
    {
        if (translationMapping.containsKey(description))
        {
            return translationMapping.get(description);
        }
        else
        {
            return CGMSViewerFilter.All;
        }
    }


    public static CGMSViewerFilter getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return CGMSViewerFilter.All;
        }
    }

    public static Object[] getAllValues()
    {
        return values();
    }

    public String toString()
    {
        return this.translation;
    }

} 