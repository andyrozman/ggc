package ggc.core.util;

import java.util.HashMap;

import com.atech.i18n.I18nControlAbstract;
import ggc.core.plugins.GGCPluginType;

public class GGCI18nControlContext extends HashMap<String, I18nControlAbstract>
{

    private static GGCI18nControlContext sGGCI18nControlContext;

    private boolean defaultLanguageRecognitionInitialized = false;
    private String defaultLanguage;
    private String selectedLanguage;

    private String[] languages;

    private HashMap<GGCPluginType, String> loadedPluginTypes;


    private GGCI18nControlContext()
    {
        loadedPluginTypes = new HashMap<GGCPluginType, String>();
    }


    public static GGCI18nControlContext getInstance()
    {
        if (sGGCI18nControlContext == null)
        {
            sGGCI18nControlContext = new GGCI18nControlContext();
        }

        return sGGCI18nControlContext;
    }


    public boolean isGGCPluginTypeLoaded(GGCPluginType pluginType)
    {
        return false;
    }


    // public String getTranslation(String key, GGCPluginType pluginType)
    // {
    // String translation = null;
    //
    // translation = getTranslationFromContext(key, pluginType);
    //
    // if (isNotNullAndSame(key, translation))
    // {
    // return translation;
    // }
    //
    //
    // if (pluginType.isPlugin())
    // {
    // translation = getTranslationFromContext(key, GGCPluginType.PluginBase);
    //
    // if (isNotNullAndSame(key, translation))
    // {
    // return translation;
    // }
    //
    // translation = getTranslationFromContext(key, GGCPluginType.Core);
    //
    // if (isNotNullAndSame(key, translation))
    // {
    // return translation;
    // }
    // }
    //
    // return key;
    // }

    private boolean isNotNullAndSame(String key, String translation)
    {
        if (translation == null)
        {
            return false;
        }

        return (!translation.equals(key));
    }


    // private String getTranslationFromContext(String key, GGCPluginType
    // pluginType)
    // {
    // for(String lang : languages)
    // {
    // String fullKey = pluginType.getKey() + "_" + lang;
    //
    // if (this.containsKey(fullKey))
    // {
    // String tr = this.get(fullKey).getMessage(key);
    // if (isNotNullAndSame(key, tr))
    // {
    // return tr;
    // }
    // }
    // }
    //
    // return null;
    // }

    public String getMessageFromCatalog(String key, GGCPluginType pluginType)
    {
        String translation = null;

        translation = getMessageFromContext(key, pluginType);

        if (translation != null)
        {
            return translation;
        }

        if (pluginType.isPlugin())
        {
            translation = getMessageFromContext(key, GGCPluginType.PluginBase);

            if (translation != null)
            {
                return translation;
            }

            translation = getMessageFromContext(key, GGCPluginType.Core);

            if (translation != null)
            {
                return translation;
            }
        }

        return key;
    }


    private String getMessageFromContext(String key, GGCPluginType pluginType)
    {

        for (String lang : languages)
        {
            String fullKey = pluginType.getKey() + "_" + lang;
            // System.out.println("fullKey: " + fullKey + ", key: " + key);

            if (this.containsKey(fullKey))
            {
                String tr = this.get(fullKey).getMessageFromCatalogOrNull(key);
                if (tr != null)
                {
                    return tr;
                }
            }
        }

        return null;
    }


    public boolean isDefaultLanguageRecognitionInitialized()
    {
        return defaultLanguageRecognitionInitialized;
    }


    public void setDefaultLanguageRecognitionInitialized(boolean defaultLanguageRecognitionInitialized)
    {
        this.defaultLanguageRecognitionInitialized = defaultLanguageRecognitionInitialized;
    }


    public String getDefaultLanguage()
    {
        return defaultLanguage;
    }


    public void setDefaultLanguage(String defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
    }


    public String getSelectedLanguage()
    {
        return selectedLanguage;
    }


    public void setSelectedLanguage(String selectedLanguage)
    {
        this.selectedLanguage = selectedLanguage;
    }


    public boolean isSelectedLanguageDefaultLanguage()
    {
        return this.selectedLanguage.equals(this.defaultLanguage);
    }


    public void addLanguageInstance(GGCPluginType pluginType, String language, I18nControlAbstract ic)
    {
        if (!loadedPluginTypes.containsKey(pluginType))
        {
            this.loadedPluginTypes.put(pluginType, "");
        }

        this.put(pluginType.getKey() + "_" + language, ic);
    }


    public void prepareContext()
    {
        boolean two = false;

        if (!this.isSelectedLanguageDefaultLanguage())
        {
            two = true;
            this.languages = new String[2];
        }
        else
        {
            this.languages = new String[1];
        }

        languages[0] = this.selectedLanguage;

        if (two)
        {
            languages[1] = this.defaultLanguage;
        }

    }

}
