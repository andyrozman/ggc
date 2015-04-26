package ggc.core.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 28.02.15.
 */
public enum StockUsageUnit implements CodeEnumWithTranslation
{
    None(0, "NONE"), Day(1, "TIME_DAY"), Week(2, "TIME_WEEK"), Month(3, "TIME_MONTH"),

    ;

    static String[] descriptions;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, StockUsageUnit> codeMapping = new HashMap<Integer, StockUsageUnit>();

    static
    {
        I18nControlAbstract ic = DataAccess.getInstance().getI18nControlInstance();

        for (StockUsageUnit pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

        String[] basal_desc_lcl = { ic.getMessage("SELECT"), ic.getMessage("TIME_DAY"), //
                                   ic.getMessage("TIME_WEEK"), //
                                   ic.getMessage("TIME_MONTH"), //

        };

        descriptions = basal_desc_lcl;
    }

    int code;
    String i18nKey;
    String translation;


    private StockUsageUnit(int code, String i18nKey)
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


    /**
     * Get Type from Description
     *
     * @param str
     *            type as string
     * @return type as int
     */
    public static int getTypeFromDescription(String str)
    {
        return ATDataAccessAbstract.getTypeFromDescription(str, translationMapping);
    }


    public static StockUsageUnit getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return StockUsageUnit.Day;
        }
    }


    public static StockUsageUnit getByDescription(String description)
    {
        return getByCode(getTypeFromDescription(description));
    }


    /**
     * Get Descriptions (array)
     *
     * @return array of strings with description
     */
    public static String[] getDescriptions()
    {
        return descriptions;
    }

}
