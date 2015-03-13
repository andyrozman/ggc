package ggc.cgms.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.enums.DeviceConfigurationGroup;


/**
 * Created by andy on 27.02.15.
 */
public enum CGMSConfigurationGroup implements DeviceConfigurationGroup
{
    General(1, "GROUP_GENERAL"), //
    Device(2, "GROUP_DEVICE"), //

    Transmiter(3, "GROUP_TRANSMITER"), // ????
    Warnings(4, "GROUP_WARNINGS"), //

    Sound(6, "GROUP_SOUND"), //




    Other(20, "GROUP_OTHER"), //

;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, CGMSConfigurationGroup> codeMapping = new HashMap<Integer, CGMSConfigurationGroup>();


    int code;
    String i18nKey;
    String translation;


    static
    {
        I18nControlAbstract ic = DataAccessCGMS.getInstance().getI18nControlInstance();

        for (CGMSConfigurationGroup pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

    }




    CGMSConfigurationGroup(int code, String i18nKey)
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





}
