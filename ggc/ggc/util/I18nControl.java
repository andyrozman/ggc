/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: I18nControl
 *  Purpose:  Used for internationalization
 *
 *  Author:   andyrozman
 */


package ggc.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import java.io.FileInputStream;
import java.util.Properties;

public class I18nControl
{


    
    /**
     *  Resource bundle identificator
     */
    ResourceBundle res;


    static private I18nControl m_i18n = null;   // This is handle to unique 
                                                    // singelton instance

    private static final Locale defaultLocale = Locale.ENGLISH;

    private String selected_language = "en";

    //   Constructor:  I18nControl
    /**
     *
     *  This is I18nControl constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */ 
    private I18nControl()
    {
	getSelectedLanguage();
        setLanguage();
    } 
    

    private void getSelectedLanguage()
    {
	//this.selected_language = DataAccess.getInstance().getSettings().getLanguage();

	try
        {
            Properties props = new Properties();

            FileInputStream in = new FileInputStream("../data/GGC_Config.properties");
            props.load(in);

            this.selected_language = (String)props.get("SELECTED_LANG");

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
    }

    
    //  Method:       getInstance
    //  Author:       Andy
    /**
     *
     *  This method returns reference to I18nControl object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to I18nControl object
     * 
     */ 
    static public I18nControl getInstance()
    {
        if (m_i18n == null)
            m_i18n = new I18nControl();
        return m_i18n;
    }

    //  Method:       deleteInstance
    /**
     *
     *  This method sets handle to I18NControl to null and deletes the instance. <br><br>
     *
     */ 
    public static void deleteInstance()
    {
        m_i18n = null;
    }


    /**
     * This method sets the language according to the preferences.<br>
     * <br>
     * <h3>WARNING:</h3><br>
     * Using this method before GGCProperties.getInstance() has been called at least
     * once will create an endless loop.
     */
    public void setLanguage() 
    {
        //GGCProperties props = GGCProperties.getInstance();
        setLanguage(this.selected_language); //props.getLanguage());
    }

    //  Method:       setLanguage (String language)
    /**
     *
     *  This is helper method for setting language.<br><br>
     *
     *  @param language language which we want to use
     */ 
    public void setLanguage(String language)
    {
        
        Locale l = new Locale(language);
        setLanguage(l);

    }



    //  Method:       setLanguage (String language, String country)
    /**
     *
     *  This is helper method for setting language.<br><br>
     *
     *  @param language language which we want to use
     *  @param country country that uses this language
     */ 
    public void setLanguage(String language, String country)
    {
        
        Locale l = new Locale(language, country);
        setLanguage(l);
    
    }



    //  Method:       setLanguage (Locale)
    /**
     *
     *  This method sets language for control instance. If none is found, english is defaulted.
     *  if none is found, application will exit.<br><br>
     *
     *  @param locale locale that will choose which language will be set
     */ 
    public void setLanguage(Locale locale)
    {

        try
        {

            //ResourceBundle.get
            /*
            try
            {
                File d = new File(".");

                System.out.println(d.getCanonicalPath());
            }
            catch(Exception ex){
                
            }
            */
            res = ResourceBundle.getBundle("GGC", locale);
        }
        catch (MissingResourceException mre)
        {
            System.err.println("Couldn't find resource file(1): GGC_"+locale+".properties (for Locale "+locale+")");
            try
            {
                res = ResourceBundle.getBundle("GGC", defaultLocale);
            }
            catch(Exception ex)
            {
                System.err.println("Exception on reading default resource file (GGC_EN.properties). Exiting application.");
                System.exit(2);
            }
        }

    }



    //  Method: htmlize
    /**
     *  
     * Converts text from bundle into HTML. This must be used if we have control, which has
     * formated text in HTML or is multilined (some of basic java swing components don't 
     * support \n). 
     * 
     * @param input input text we wish to HTMLize
     * @return HTMLized text
     * 
     */
    private String htmlize(String input)
    {
        
        StringBuffer buffer = new StringBuffer("<HTML>");

        input = input.replaceAll("\n", "<BR>");
        input = input.replaceAll("&", "&amp;");

        buffer.append(input);
        buffer.append("</HTML>");

        return buffer.toString();
        
    }

    //  Method: getMessageHTML(String)
    /**
     * 
     *  Helper method to get HTMLized message from Bundle
     * 
     * @param msg non-htmlized (or partitialy HTMLized tekst)
     * @return fully HTMLized message 
     * 
     */
    public String getMessageHTML(String msg)
    {

        String mm = this.getMessage(msg);

        return htmlize(mm);

    }

    //  Method:       getString
    /**
     * 
     *  This helper method calls getMessage(String) and returns message that is
     *  associated with inserted code. It is implemented mainly, because some 
     *  programmers are used that resource msg is returned with this command.
     * 
     *  @param msg id of message we want
     *  @return value for code, or same code back
     */
    public String getString(String msg)
    {
        return this.getMessage(msg);
    }



    //  Method:       returnSameValue (String)
    /**
     * 
     *  Returns same value as it was sent to catalog in case that catalog entry was not
     *  found. This message has inserted spaces so that is easier readable.
     * 
     *  @param msg id of message we want
     *  @return same code back (formated)
     */
    private String returnSameValue(String msg)
    {
        // If we return same msg back, without beeing resolved, we put spaces before %, so
        // that it is much easier readable.
        if (msg.indexOf("%")==-1)
            return msg;

        StringBuffer out=new StringBuffer();
        int idx;
        while ((idx=msg.indexOf("%"))!=-1)
        {
            out.append(msg.substring(0, idx));
            out.append("|%");
            
            msg = msg.substring(idx+1);
                
        }

        out.append(msg);

        return out.toString();

    }



    // Method: resolveMnemonic(String)
    /**
     *  This method extracts mnemonics from message string. Each such string can
     *  contain several & characters, even double &. Last & in String is mnemonic
     *  while other are discarded. Double && is resolved to single & in text. We 
     *  return array of Object. First entry contains mnemonic if this is null, we 
     *  didn't find any mnemonic. Second entry is text without mnemonic and changed 
     *  && substrings. If whole object is returned as null, then String didn't 
     *  contain any & signs.
     * 
     *  @param msg message from message catalog
     *  @return array of Object, containg max. two elements, null can also be returned
     *
     */
    private Object[] resolveMnemonics(String msg)
    {

        if (msg.indexOf("&")==-1)
            return null;


        Object back[] = new Object[2];
        int msg_length = msg.length();
        int code[] = new int[msg_length];
        boolean foundDouble=false;
        boolean foundMnemonic=false;


        for (int i=0;i<msg_length;i++)
        {
            if (msg.charAt(i)=='&')
            {
                // we found mnemonic sign   
                code[i]=1;  // 1 if & sign
                if (i!=0)
                {
                    // check for double &
                    if (code[i-1]==1)  // double & are marked 2
                    {
                        code[i-1]=2;
                        code[i]=2;
                        foundDouble=true;
                    }
                }
            }
            else
                code[i]=0;
        }


        // now we find real menmonic
        for (int i=msg_length-1; i>-1; i--)
        {
            if (code[i]==1)
            {
                code[i]=3;
                if (i==msg_length-1)  // if & is last char we ignore it
                {
                    code[i]=1;
                }
                else
                {
                    foundMnemonic=true;
                    break;
                }
            }
        }


        StringBuffer returnStr = new StringBuffer();

        int lastChange=0;

        for (int i=0; i<msg_length;i++)
        {
            
            if (code[i]==1)  // all & (tagged 1) are removed
            {
                returnStr.append(msg.substring(lastChange, i));
                lastChange=i+1;
                
            } 
            else if (code[i]==2) // all && are replaced with one &
            {
                returnStr.append(msg.substring(lastChange, i));
                returnStr.append("&");
                lastChange=i+2; // was 2
                i=i+1;
            }
            else if (code[i]==3) // this is mnemonic
            {
                back[0]=new Character(msg.charAt(i+1)); 
                returnStr.append(msg.substring(lastChange, i));
                lastChange=i+1;
            }
        }

        returnStr.append(msg.substring(lastChange));

        back[1] = returnStr.toString();

        if (!foundMnemonic)
            back[0]=null;

        return back;
    
    }



    // Method: getMnemonic
    /**
     *  Returns mnemonic of String that is stored in bundle as msg_id. If mnemonic is
     *  not found 0 is returned. Calls private method resolveMnemonics.
     * 
     *  @see resolveMnemonic
     *  @param msg_id id of message in bundle
     *  @return int representation of char that is mnemonic, 0 if none found
     */
    public char getMnemonic(String msg_id)
    {
        try
        {
            Object[] back = resolveMnemonics(getMessageFromCatalog(msg_id));

            if ((back==null) || (back[0]==null))
               return '0';
        
            return ((Character)back[0]).charValue();
        }
        catch (Exception e)
        {
            return '0';
        }

    }



    // Method: getMessageWithoutMnemonic
    /**
     *  Returns String that is stored in bundle as msg_id. It also removes  
     *  mnemonic signs and removed double &. Calls private method resolveMnemonics.
     * 
     *  @see resolveMnemonic
     *  @param msg_id id of message in bundle
     *  @return String message from catalog, woithout mnemonic and double &
     */
    public String getMessageWithoutMnemonic(String msg_id)
    {
        try
        {

            String ret = getMessageFromCatalog(msg_id);

            Object[] back = resolveMnemonics(ret);

            if (back==null)
                return ret;
            else
                return (String)back[1];
        }
        catch(Exception ex)
        {
            return returnSameValue(msg_id);
        }
    }



    // Method: getMessageFromCatalog
    /**
     *  Looks into bundle and returns correct message. This method is syncronized, so only one
     *  message at the time can be returned.
     * 
     *  @param msg id of message in bundle
     *  @return String message from catalog.
     */
    private synchronized String getMessageFromCatalog(String msg)
    {

        try
        {
            
            if (msg==null)
                return "null";
            
            String ret = res.getString(msg);

            if (ret==null)
            {
                System.out.println("Couldn't find message: ");
                return returnSameValue(msg);
            }
            else
                return ret;

        }
        catch(Exception ex)
        {
            return returnSameValue(msg);
        }

    }


    //  Method:       getMessage (String)
    /**
     * 
     *  Helper method to get message from Bundle.
     * 
     *  @param msg id of message we want
     *  @return value for code, or same code back
     */    
    public String getMessage(String msg)
    {

        return getMessageFromCatalog(msg);

    }


    public static void main(String args[])
    {

        I18nControl oc = I18nControl.getInstance();

//        System.out.println(oc.getMessage(12, 1));


    }

}
