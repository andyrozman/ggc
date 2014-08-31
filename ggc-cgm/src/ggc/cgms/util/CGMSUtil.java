package ggc.cgms.util;

import com.atech.db.ext.ExtendedHandler;

import ggc.plugin.util.I18nControlPlugin;

public class CGMSUtil
{

    private static DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();
    
    private static I18nControlPlugin i18nControl = dataAccess.getI18nControlInstance();
    
    
    public static I18nControlPlugin getI18Control()
    {
        return i18nControl;
    }
    
    public static String getTranslatedString(String key)
    {
        return i18nControl.getMessage(key);
    }
    
    public static int getCurrentUserId()
    {
        return (int)dataAccess.getCurrentUserId();
    }
    
    public static ExtendedHandler getExtendedHandler(String key)
    {
        return dataAccess.getExtendedHandler(key);
    }
    
    
}
