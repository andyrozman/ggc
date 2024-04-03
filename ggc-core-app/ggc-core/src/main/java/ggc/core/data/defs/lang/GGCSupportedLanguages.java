package ggc.core.data.defs.lang;

import java.util.ArrayList;
import java.util.List;

import com.atech.data.enums.LanguageISO639;
import com.atech.i18n.info.SupportedLanguage;

/**
 * Created by andy on 30/06/17.
 */
public enum GGCSupportedLanguages implements SupportedLanguage
{
    English(LanguageISO639.English, true), //
    Slovene(LanguageISO639.Slovene), //
    French(LanguageISO639.French), //
    German(LanguageISO639.German) //
    ;

    private static List<SupportedLanguage> supportedLanguagesList;

    LanguageISO639 languageISO639;
    boolean defaultLanguage;


    GGCSupportedLanguages(LanguageISO639 languageISO639)
    {
        this(languageISO639, false);
    }


    GGCSupportedLanguages(LanguageISO639 languageISO639, boolean defaultLanguage)
    {
        this.languageISO639 = languageISO639;
        this.defaultLanguage = defaultLanguage;
    }


    public boolean isDefaultLanguage()
    {
        return this.defaultLanguage;
    }


    public LanguageISO639 getLanguageDefinition()
    {
        return this.languageISO639;
    }


    /**
     * Only the ones that are supported.
     *
     * @return
     */
    public List<SupportedLanguage> getLanguagesToTranslate()
    {
        if (supportedLanguagesList == null)
        {

            List<SupportedLanguage> outList = new ArrayList<SupportedLanguage>();

            for (SupportedLanguage lang : values())
            {
                if (!lang.isDefaultLanguage())
                {
                    outList.add(lang);
                }
            }

            supportedLanguagesList = outList;
        }

        return supportedLanguagesList;
    }


    public SupportedLanguage getMasterLanguage()
    {
        return English;
    }

}
