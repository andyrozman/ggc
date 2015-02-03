package ggc.core.util;

import com.atech.i18n.I18nControlAbstract;
import ggc.core.plugins.GGCPluginType;

/**
 * Created by andy on 21.12.14.
 */
public class GGCI18nControl extends I18nControlAbstract
{
    GGCPluginType pluginType;
    GGCI18nControlContext context;

    public GGCI18nControl(GGCPluginType pluginType)
    {
        this.pluginType = pluginType;
        this.context = GGCI18nControlContext.getInstance();
    }

    @Override
    protected String getLanguageConfigFile()
    {
        return null;
    }

    @Override
    public void init()
    {

    }

//    @Override
//    public String getMessage(String msgKey)
//    {
//        return this.context.getTranslation(msgKey, this.pluginType);
//    }


    @Override
    public synchronized String getMessageFromCatalog(String msgKey)
    {
        String translation = this.context.getMessageFromCatalog(msgKey, this.pluginType);

        if (translation==null)
        {
            return msgKey;
        }

        return translation;
    }
}
