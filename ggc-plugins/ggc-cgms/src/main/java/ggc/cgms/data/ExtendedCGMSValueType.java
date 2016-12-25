package ggc.cgms.data;

import java.util.HashMap;
import java.util.Map;

import com.atech.db.ext.ExtendedEnumType;

public enum ExtendedCGMSValueType implements ExtendedEnumType<ExtendedCGMSValueType>
{

    None("NONE"), //
    SubType("SUB_TYPE"), //
    Source("SOURCE"), //
    ;

    int code;
    String i18nKey;
    String translation;

    static Map<String, ExtendedCGMSValueType> mapByKey;
    static Map<String, ExtendedCGMSValueType> mapByName;

    static
    {
        mapByKey = new HashMap<String, ExtendedCGMSValueType>();
        mapByName = new HashMap<String, ExtendedCGMSValueType>();

        for (ExtendedCGMSValueType vt : values())
        {
            mapByKey.put(vt.getI18nKey(), vt);
            mapByName.put(vt.name(), vt);
        }
    }


    ExtendedCGMSValueType(String i18nKey)
    {
        this.i18nKey = i18nKey;
    }


    public ExtendedCGMSValueType getEnumTypeByKey(String key)
    {
        return mapByKey.get(key);
    }


    public ExtendedCGMSValueType getEnumTypeByName(String key)
    {
        return mapByName.get(key);
    }


    public String getI18nKey()
    {
        return this.i18nKey;
    }


    public String getTranslation()
    {
        return this.translation;
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public String getName()
    {
        return name();
    }


    public boolean useI18nKey()
    {
        return true;
    }


    public ExtendedCGMSValueType[] getAllValues()
    {
        return values();
    }

}
