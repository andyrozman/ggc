package ggc.core.data.defs;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.core.util.DataAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 27.02.15.
 */
public enum StockTypeBase implements CodeEnumWithTranslation
{
    None(0, "NONE"),
    Insulin(1, "STOCKTYPE_INSULIN"),
    BG(2, "STOCKTYPE_BG"),
    Pen(3, "STOCKTYPE_PEN"),
    Pump(4, "STOCKTYPE_PUMP"),
    CGMS(5, "STOCKTYPE_CGMS")
    ;


    static String[] descriptions;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, StockTypeBase> codeMapping = new HashMap<Integer, StockTypeBase>();

    static
    {
        I18nControlAbstract ic = DataAccess.getInstance().getI18nControlInstance();

        for (StockTypeBase pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

        String[] basal_desc_lcl = { ic.getMessage("SELECT_STOCK_TYPE"),
                ic.getMessage("STOCKTYPE_INSULIN"), //
                ic.getMessage("STOCKTYPE_BG"), //
                ic.getMessage("STOCKTYPE_PEN"), //
                ic.getMessage("STOCKTYPE_PUMP"), //
                ic.getMessage("STOCKTYPE_CGMS"), //
        };

        descriptions = basal_desc_lcl;
    }



    int code;
    String i18nKey;
    String translation;

    private StockTypeBase(int code, String i18nKey)
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

    public static StockTypeBase getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return StockTypeBase.None;
        }
    }

    /**
     * Get Type from Description
     *
     * @param str
     *            type as string
     * @return type as int
     */
    public static StockTypeBase getByDescription(String str)
    {
        return getByCode(ATDataAccessAbstract.getTypeFromDescription(str, translationMapping));
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
