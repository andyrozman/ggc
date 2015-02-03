package ggc.cgms.util;

import ggc.core.util.GGCI18nControl;
import ggc.plugin.util.I18nControlPlugin;

import com.atech.db.ext.ExtendedHandler;

public class CGMSUtil
{

    private static DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();

    private static GGCI18nControl i18nControl = dataAccess.getI18nControlInstance();

    public static GGCI18nControl getI18Control()
    {
        return i18nControl;
    }

    public static String getTranslatedString(String key)
    {
        return i18nControl.getMessage(key);
    }

    public static int getCurrentUserId()
    {
        return (int) dataAccess.getCurrentUserId();
    }

    public static ExtendedHandler getExtendedHandler(String key)
    {
        return dataAccess.getExtendedHandler(key);
    }

}
