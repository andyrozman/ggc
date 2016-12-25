package main.java.ggc.pump.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.plugin.data.enums.DeviceConfigurationGroup;
import main.java.ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 27.02.15.
 */
public enum PumpConfigurationGroup implements DeviceConfigurationGroup
{
    General(1, "GROUP_GENERAL"), //
    Device(2, "GROUP_DEVICE"), //

    Insulin(3, "GROUP_INSULIN"), //

    Basal(4, "GROUP_BASAL"), //
    Bolus(5, "GROUP_BOLUS"), //
    Sound(6, "GROUP_SOUND"), //

    Other(20, "GROUP_OTHER"), ; //

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, PumpConfigurationGroup> codeMapping = new HashMap<Integer, PumpConfigurationGroup>();

    int code;
    String i18nKey;
    String translation;

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpConfigurationGroup pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

    }


    PumpConfigurationGroup(int code, String i18nKey)
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

}
