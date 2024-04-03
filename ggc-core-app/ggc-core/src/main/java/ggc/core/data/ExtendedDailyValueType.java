package ggc.core.data;

import java.util.HashMap;
import java.util.Map;

import com.atech.db.ext.ExtendedEnumType;

public enum ExtendedDailyValueType implements ExtendedEnumType<ExtendedDailyValueType>
{

    None("NONE"), //
    Activity("ACTIVITY"), //
    Urine("URINE"), //
    Urine_mgdL("URINE_MGDL"), //
    Urine_mmolL("URINE_MMOLL"), //
    FoodDescription("FOOD_DESCRIPTION"), //
    FoodCarbohydrate("FOOD_DESC_CH"), //

    DecimalPartInsulin1("DECIMAL_INS1"), //
    DecimalPartInsulin2("DECIMAL_INS2"), //
    Insulin3("INSULIN_3"), //
    Source("SOURCE"), //
    GlucometerMarkers("GLUCOMETER_MARKERS"), //

    ;

    int code;
    String i18nKey;
    String translation;

    static Map<String, ExtendedDailyValueType> mapByKey;
    static Map<String, ExtendedDailyValueType> mapByName;


    static
    {
        mapByKey = new HashMap<String, ExtendedDailyValueType>();
        mapByName = new HashMap<String, ExtendedDailyValueType>();

        for (ExtendedDailyValueType vt : values())
        {
            mapByKey.put(vt.getI18nKey(), vt);
            mapByName.put(vt.name(), vt);
        }
    }


    ExtendedDailyValueType(String i18nKey)
    {
        this.i18nKey = i18nKey;
    }


    public ExtendedDailyValueType getEnumTypeByKey(String key)
    {
        return mapByKey.get(key);
    }


    public ExtendedDailyValueType getEnumTypeByName(String key)
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


    public ExtendedDailyValueType[] getAllValues()
    {
        return values();
    }

}
