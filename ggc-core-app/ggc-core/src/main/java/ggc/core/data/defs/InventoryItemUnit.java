package ggc.core.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

public enum InventoryItemUnit implements CodeEnumWithTranslation
{

    None(0, "NONE"), //
    Unit(1, "UNIT_UNIT"), //
    ML(2, "UNIT_ML"), //
    Pieces(3, "UNIT_PIECES"), //
    ;

    static String[] descriptions;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, InventoryItemUnit> codeMapping = new HashMap<Integer, InventoryItemUnit>();
    static Map<String, CodeEnumWithTranslation> keyMapping = new HashMap<String, CodeEnumWithTranslation>();

    static
    {
        for (InventoryItemUnit pbt : values())
        {
            keyMapping.put(pbt.i18nKey, pbt);
            codeMapping.put(pbt.code, pbt);
        }
    }

    int code;
    String i18nKey;
    String translation;


    InventoryItemUnit(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        for (InventoryItemUnit pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
        }

        String[] stockUnitDescriptions = { ic.getMessage("SELECT"), //
                                           ic.getMessage("UNIT_UNIT"), //
                                           ic.getMessage("UNIT_ML"), //
                                           ic.getMessage("UNIT_PIECES"), //
        };

        descriptions = stockUnitDescriptions;
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


    public static InventoryItemUnit getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return InventoryItemUnit.None;
        }
    }


    /**
     * Get Type from Description
     *
     * @param str
     *            type as string
     * @return type as int
     */
    public static InventoryItemUnit getByDescription(String str)
    {
        return getByCode(ATDataAccessAbstract.getTypeFromDescription(str, translationMapping));
    }


    public static InventoryItemUnit getByI18nKey(String str)
    {
        return getByCode(ATDataAccessAbstract.getTypeFromDescription(str, keyMapping));
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
