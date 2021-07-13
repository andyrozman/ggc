package ggc.connect.enums;

import com.atech.i18n.I18nControlAbstract;

public enum ConnectOperationType
{
    None("SELECT_DOT"), //
    ImportData("IMPORT_DATA"), //
    ViewConfiguration("VIEW_CONFIGURATION"), //
    ImportAndView(ImportData, ViewConfiguration), //
    ;

    Object[] children;
    String key;
    String translatedKey;

    private static boolean translated = false;


    ConnectOperationType(String key)
    {
        this.key = key;
        this.translatedKey = key;
    }


    ConnectOperationType(ConnectOperationType... items)
    {
        children = items;
    }


    public Object[] getObjects()
    {
        if (children != null)
        {
            return children;
        }
        else
        {
            Object[] obj = new Object[1];
            obj[0] = this;

            return obj;
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (ConnectOperationType pbt : values())
        {
            pbt.translatedKey = ic.getMessage(pbt.key);
        }

        translated = true;
    }


    public Object[] getObjectsWithNone()
    {
        if (children != null)
        {
            Object[] endObject = new Object[children.length + 1];
            endObject[0] = ConnectOperationType.None;

            for (int i = 0; i < children.length; i++)
            {
                endObject[i + 1] = children[i];
            }

            return endObject;
        }
        else
        {
            Object[] obj = new Object[2];
            obj[0] = ConnectOperationType.None;
            obj[1] = this;

            return obj;
        }
    }


    public String toString()
    {
        return translatedKey;
    }

}
