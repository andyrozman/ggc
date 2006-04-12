
package com.atech.i18n.tool;


import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;


public class I18nCheck
{

    /**
     *  Resource bundle identificator
     */
    ResourceBundle res;

    // Hashtable of Resource Bundles for different langauges
    Hashtable resourceBundles;

    // Reversed Hashtable of Resource Bundles for different langauges
    //Hashtable reverseRBs;


    //public String[] lang_short = null;

    private ArrayList lang_short = null;

    private String m_prefix = null;
    private String default_lang = null;




    //   Constructor:  OmniI18nControl
    /**
     *
     *  This is OmniI18nControl constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */ 
    public I18nCheck(String prefix, String default_lang)
    {
        resourceBundles = new Hashtable();
        lang_short = new ArrayList();
        this.m_prefix = prefix;
        this.default_lang = default_lang;

        getAvailableFiles();

        if (lang_short.size()<2) 
        {
            System.out.println("  WARNING: You need at least two language files to compare.");
            return;
        }

        loadLanguages();
        checkLanguages();
//        checkLanguages(lang);

    } 
    

    public void getAvailableFiles()
    {
        File fl = new File(".");

        String[] files = fl.list();

        for (int i=0; i< files.length; i++) 
        {
            if ((files[i].startsWith(this.m_prefix + "_")) &&
                (files[i].indexOf(".properties")!=-1))
            {
                String sub = files[i].substring(this.m_prefix.length() + 1, files[i].indexOf(".properties"));

                if (sub.length()<=3) 
                {
                    this.lang_short.add(sub);
                    //System.out.println(sub);
                }
            }
        }

    }
    



    public void loadLanguages()
    {

        System.out.println(" --- Load Resource Bundles ---");
        for (int i=0; i<lang_short.size(); i++)
        {
            String lang = (String)this.lang_short.get(i);
            String name = this.m_prefix + "_" + lang + ".properties";

            //Locale l = new Locale(lang_short[i]);

            ResourceBundle rb = getResourceBundle(lang);

            if (rb==null)
            {
                System.out.println("Error loading: " + name);
                //rb = getResourceBundle(lcls[0]);
            }
            else
                System.out.println("Resource File " + name + " loaded succesfully");

            //System.out.println("RB Full: " + resourceBundles + " loaded RB: " +rb);
            
            if (lang.equals(this.default_lang))
                res = rb;
            else
                resourceBundles.put(lang, rb);

        }
        System.out.println(" --- End of loading of Resource Bundles ---");

    }


    public ResourceBundle getResourceBundle(Locale lcl)
    {

        ResourceBundle rb = null;
        try
        {
            rb = ResourceBundle.getBundle(this.m_prefix, lcl);
        }
        catch (MissingResourceException mre)
        {
            System.out.println("Couldn't find resource file for Locale: " + lcl);
        }

        return rb;

    }


    public ResourceBundle getResourceBundle(String lcl_str)
    {

        Locale lcl = new Locale(lcl_str);

        ResourceBundle rb = null;
        try
        {
            rb = ResourceBundle.getBundle(this.m_prefix, lcl);
        }
        catch (MissingResourceException mre)
        {
            System.out.println("Couldn't find resource file for Locale: " + lcl);
        }

        return rb;

    }


















    public void checkLanguages()
    {

        for(Enumeration en = this.resourceBundles.keys(); en.hasMoreElements(); )
        {
            String key = (String)en.nextElement();
            ResourceBundle rb = (ResourceBundle)this.resourceBundles.get(key);

            System.out.println("==================================================================");
            System.out.println("Language " + this.m_prefix + "_" + key + ".properties");
            System.out.println("==================================================================");
            checkIfLangFileHasAllKeywords(rb);
            checkIfLangHasNoLongerUsedKeywords(rb);
        }

/*
        for (int i=0; i<lang_short.size(); i++)
        {
            String lang = (String)this.lang_short.get(i);


            if (!lang_short.get(i)[i].equals(lang_def))
            {
//                System.out.println("Checking " + this.languages[i] + " language.");
                //this.checkIfLangFileHasAllKeywords(lang_short[i]);
                System.out.println("\n\n");
            }

        }
        */

    }




    public void checkIfLangFileHasAllKeywords(String short_lang)
    {
        ResourceBundle rb = (ResourceBundle)resourceBundles.get(short_lang);
        checkIfLangFileHasAllKeywords(rb);
    }


    public void checkIfLangFileHasAllKeywords(ResourceBundle rb)
    {

        //ResourceBundle rb = (ResourceBundle)resourceBundles.get(short_lang);

        StringBuffer sb = new StringBuffer();

        for (Enumeration en = res.getKeys(); en.hasMoreElements(); )
        {
            String key = (String)en.nextElement();

            try
            {
                if (rb.getString(key)==null)
                    sb.append(key + "\n");
                    //System.out.println(key);
            }
            catch(Exception ex)
            {
                //System.out.println(key);
                sb.append(key + "\n");
            }
        }

        if (sb.length()==0) 
        {
            System.out.println("\nNO MISSING KEYWORDS !");
            System.out.println("---------------------");
        }
        else
        {
            System.out.println("\nMISSING KEYWORDS:");
            System.out.println("-----------------");
            System.out.println(sb);
        }

    }



    public void checkIfLangHasNoLongerUsedKeywords(ResourceBundle rb)
    {

        //ResourceBundle rb = (ResourceBundle)resourceBundles.get(short_lang);

        StringBuffer sb = new StringBuffer();

        for (Enumeration en = rb.getKeys(); en.hasMoreElements(); )
        {
            String key = (String)en.nextElement();

            try
            {
                if (res.getString(key)==null)
                    sb.append(key + "\n");
                    //System.out.println(key);
            }
            catch(Exception ex)
            {
                //System.out.println(key);
                sb.append(key + "\n");
            }
        }

        if (sb.length()==0) 
        {
        }
        else
        {
            System.out.println("KEYWORDS NO LONGER USED:");
            System.out.println("------------------------");
            System.out.println(sb);
        }

    }

    


    public static void main(String args[])
    {
        if (args.length!=2)
        {
            System.out.println(" Usage:  java I18nCheck <lang_file_prefix> <default_language_short>");
            System.exit(1);
        }


        I18nCheck i1 = new I18nCheck(args[0], args[1]);

    }

}


