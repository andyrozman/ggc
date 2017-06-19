package ggc.core.util;

import java.util.HashMap;

import com.atech.i18n.I18nControlAbstract;

public enum GGCSoftwareMode
{
    PEN_INJECTION_MODE(0, "PEN_INJECTION_MODE"), //
    PUMP_MODE(1, "PUMP_MODE");

    private int mode;
    private String i18nKey;
    private String translation;

    private static boolean translated;

    private static HashMap<Integer, GGCSoftwareMode> map = new HashMap<Integer, GGCSoftwareMode>();

    static
    {
        for (GGCSoftwareMode el : values())
        {
            map.put(el.getMode(), el);
        }
    }


    GGCSoftwareMode(int mode, String i18nKey)
    {
        this.mode = mode;
        this.i18nKey = i18nKey;
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (GGCSoftwareMode pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
        }

        translated = true;
    }


    public int getMode()
    {
        return mode;
    }


    public void setMode(int mode)
    {
        this.mode = mode;
    }


    public static GGCSoftwareMode getEnum(int value)
    {
        return map.get(value);
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public String getTranslation()
    {
        return translation;
    }
}
