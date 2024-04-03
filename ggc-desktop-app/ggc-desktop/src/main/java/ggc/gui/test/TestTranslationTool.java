package ggc.gui.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import javax.swing.*;

import org.apache.commons.lang.StringEscapeUtils;

import com.atech.i18n.tool.simple.data.TranslationToolConfigurationDto;
import com.atech.i18n.tool.simple.data.TranslationToolSettingsDto;
import com.atech.i18n.tool.simple.internal.TranslationToolInternalWizard1_ConfigurationCheck;
import com.atech.i18n.tool.simple.internal.TranslationToolInternalWizard2_LanguageSelector;
import com.atech.i18n.tool.simple.internal.TranslationToolInternalWizard3_NewLanguage;

import ggc.core.util.DataAccess;

/**
 * Created by andy on 29.06.17.
 */
public class TestTranslationTool
{

    public static void testLocale()
    {
        Locale.setDefault(Locale.ENGLISH);

        Locale[] availableLocales = Locale.getAvailableLocales();

        Map<String, Set<Locale>> filterLocales = new HashMap<String, Set<Locale>>();

        for (Locale locale : availableLocales)
        {
            // if (StringUtils.isBlank(locale.getDisplayCountry()) &&
            // StringUtils.isBlank(locale.getVariant()))

            // if (locale.getVariant() != null ||
            // !locale.getVariant().equals("") )
            // {
            // continue;
            // }

            if (filterLocales.containsKey(locale.getLanguage()))
            {
                filterLocales.get(locale.getLanguage()).add(locale);
            }
            else
            {
                Set<Locale> newLang = new HashSet<Locale>();
                newLang.add(locale);

                filterLocales.put(locale.getLanguage(), newLang);
            }

            // System.out.println("L: " + locale.getLanguage() + "\t V:" +
            // locale.getVariant() + "\t " + locale.getDisplayName() + "\t C:" +
            // locale.getDisplayCountry());
        }

        for (String localeStr : filterLocales.keySet())
        {

            Set<Locale> locales = filterLocales.get(localeStr);

            for (Locale lcl : locales)
            {
                System.out.println("" + lcl.getDisplayLanguage());
                break;
            }

            for (Locale locale : locales)
            {
                System.out.println("       " + locale.getLanguage() + "\t  V:" + locale.getVariant() + "\t "
                        + locale.getDisplayName() + "\t C:" + locale.getDisplayCountry());
            }

        }

        System.out.println("Found locale count " + filterLocales.size());

    }


    public static void readLanguages()
    {
        File f = new File(".");

        System.out.println("Current: " + f.getAbsolutePath());

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File("./list_of_iso_codes-934j.csv")));

            String line = "";

            while ((line = reader.readLine()) != null)
            {
                // System.out.println("L: " + line);

                String[] entries = line.split(",");

                String languageName = entries[2];
                String languageFamily = entries[1];
                String languageNameNative = entries[3];
                String languageCode1 = entries[4];
                String languageCode2 = entries[5];
                // String languageName = entries[2];

                System.out.println(String.format(" Name: %s,\t Description: %s,\t Family: %s, Code-1: %s", //
                    languageName, //
                    StringEscapeUtils.escapeCsv(languageName) + " (" + StringEscapeUtils.escapeJava(languageNameNative)
                            + ")", //
                    languageFamily, //
                    languageCode1

                ));

                // StringEscapeUtils.escapeC//v()

                // System.out.println(String.format("\t%s(\"%s\", \"%s\",
                // \"%s\", \"%s\"), //", //
                // languageName, //
                // languageName, //
                // languageFamily, //
                // languageCode1, //
                // languageCode2
                //
                // ));

                // StringEscapeUtils.escapeJava(languageNameNative);

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public static void testLangugaeSelector()
    {
        JFrame frame = new JFrame();

        DataAccess dataAccess = DataAccess.createInstance(frame);

        TranslationToolConfigurationDto translationToolConfiguration = dataAccess.getTranslationToolConfiguration();
        translationToolConfiguration.setSettings(new TranslationToolSettingsDto());

        TranslationToolInternalWizard2_LanguageSelector newLang = new TranslationToolInternalWizard2_LanguageSelector(
                frame, dataAccess, translationToolConfiguration);

        newLang.setVisible(true);

    }


    public static void testNewLanguega()
    {
        JFrame frame = new JFrame();

        DataAccess dataAccess = DataAccess.createInstance(frame);

        TranslationToolInternalWizard3_NewLanguage newLang = new TranslationToolInternalWizard3_NewLanguage(frame,
                dataAccess, null);

    }


    public static void testConfigurationCheck()
    {
        JFrame frame = new JFrame();

        DataAccess dataAccess = DataAccess.createInstance(frame);

        TranslationToolInternalWizard1_ConfigurationCheck tt = new TranslationToolInternalWizard1_ConfigurationCheck(
                frame, dataAccess);
        tt.setVisible(true);
    }


    public static void main(String[] args)
    {
        testLangugaeSelector();
    }

}
