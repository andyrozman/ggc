package ggc.core.util;

import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.info.LanguageInfo;



public class GGCLanguageInfo extends LanguageInfo
{

    public GGCLanguageInfo(I18nControlAbstract ic)
    {
        super(ic);
    }

    @Override
    public String getHelpSetName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getJarBaseName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLangaugeConfigFile()
    {
        return "/GGC_Languages.properties";
    }

    
    
    
    
}