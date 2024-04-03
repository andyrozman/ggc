package ggc.core.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.converter.UnitDefinition;
import com.atech.misc.converter.UnitValuePrecision;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 * Created by andy on 22/11/17.
 */
public enum HbA1cType implements CodeEnumWithTranslation, UnitDefinition
{

    mg_dL(1, "GLUCOSE_UNIT_MGDL", UnitValuePrecision.Integer), //
    mmol_L(2, "GLUCOSE_UNIT_MMOLL", UnitValuePrecision.FloatOneDecimalPlace), //

    // FIXME
    NGSP_Percent(1, "", UnitValuePrecision.FloatOneDecimalPlace), //
    IFCC_mmolmol(2, "", UnitValuePrecision.Integer), //
    eAG_mgdL(3, "GLUCOSE_UNIT_MGDL", UnitValuePrecision.Integer), //
    eAG_mmolL(4, "", UnitValuePrecision.FloatOneDecimalPlace), //
    JDS(5, "", UnitValuePrecision.FloatOneDecimalPlace), //
    MonoS(6, "", UnitValuePrecision.FloatOneDecimalPlace), //


//    NGSP (USA)
//    NGSP = (0.09148*IFCC) + 2.152
//    IFCC = (10.93*NGSP) - 23.50
//
//    JDS/JSCC (Japan)
//    JDS = (0.09274*IFCC) +1 .724
//    IFCC = (10.78*JDS) - 18.59
//
//    Mono-S (Sweden)
//    Mono-S = (0.09890*IFCC) + 0.884
//    IFCC = (10.11*Mono-S) - 8.94


    None(0, "NONE", UnitValuePrecision.None); //

    int code;
    String i18nKey;
    String translation;
    static String[] descriptions;
    UnitValuePrecision valuePrecision;

    static Map<Integer, HbA1cType> codeMapping = new HashMap<Integer, HbA1cType>();
    static Map<String, HbA1cType> mapByDescription = new HashMap<String, HbA1cType>();

    static
    {
        for (HbA1cType el : values())
        {
            codeMapping.put(el.getCode(), el);
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        for (HbA1cType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
        }

        // FIXME
        String[] unitDescriptions = { ic.getMessage("GLUCOSE_UNIT_MGDL"), //
                                     ic.getMessage("GLUCOSE_UNIT_MMOLL"), //
        };

        descriptions = unitDescriptions;

        for (HbA1cType el : values())
        {
            mapByDescription.put(el.getTranslation(), el);
        }
    }


    HbA1cType(int code, String i18nKey, UnitValuePrecision valuePrecision)
    {
        this.code = code;
        this.i18nKey = i18nKey;
        this.valuePrecision = valuePrecision;
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


    public String getDescription()
    {
        return translation;
    }


    public UnitValuePrecision getValuePrecision()
    {
        return this.valuePrecision;
    }


    public String getName()
    {
        return this.name();
    }


    public static HbA1cType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return HbA1cType.None;
        }
    }


    public static HbA1cType getByDescription(String description)
    {
        if (mapByDescription.containsKey(description))
        {
            return mapByDescription.get(description);
        }
        else
        {
            return HbA1cType.None;
        }
    }


    public static String[] getDescriptions()
    {
        return descriptions;
    }

}
