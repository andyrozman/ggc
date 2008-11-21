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
 *  Filename: DataAccess
 *  Purpose:  Used for utility works and static data handling (this is singelton
 *      class which holds all our definitions, so that we don't need to create them
 *      again for each class.      
 *
 *  Author:   andyrozman
 */

package ggc.cgm.util;

import ggc.cgm.manager.CGMManager;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.atech.db.hibernate.HibernateDb;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DataAccessCGM extends DataAccessPlugInBase
{

    // LF
    //    Hashtable<String,String> availableLF_full = null;
    //    Object[]  availableLF = null;
    //    Object[]  availableLang = null;
    //    private LanguageInfo m_lang_info = null;

    //    String selectedLF = null;
    //    String subSelectedLF = null;

    // Config file
    //    Hashtable<String,String> config_db_values = null;
    //    public int selected_db = -1;
    //    public int selected_lang = 1;
    //    public String selected_LF_Class = null; // class
    //  public String selected_LF_Name = null; // name
    //    public String skinLFSelected = null;
    //    String allDbs[] = null;


    private static DataAccessCGM s_da = null; // This is handle to unique 

    private CGMManager m_meterManager = null;


//    public Hashtable<String,String> metersUrl;
//    public ArrayList<String> metersNames;
        
        

    // ********************************************************
    // ******      Constructors and Access methods        *****    
    // ********************************************************

    //   Constructor:  DataAccess
    /**
     *
     *  This is DataAccess constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */
    private DataAccessCGM()
    {
    	super(I18nControl.getInstance());
    } 

    
    public void initSpecial()
    {
        this.loadFonts();

        checkPrerequisites();
    }
    
    
    //  Method:       getInstance
    //  Author:       Andy
    /**
     *
     *  This method returns reference to OmniI18nControl object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to OmniI18nControl object
     * 
     */
    public static DataAccessCGM getInstance()
    {
        if (s_da == null)
            s_da = new DataAccessCGM();
        return s_da;
    }

    public static DataAccessCGM createInstance(JFrame main)
    {
        if (s_da == null)
        {
            //GGCDb db = new GGCDb();
            s_da = new DataAccessCGM();
//x            s_da.setParent(main);
        }

        return s_da;
    }

 
    
    
    

  


    /*
     static public DataAccess getInstance()
     {
     return m_da;
     }
     */

    //  Method:       deleteInstance
    /**
     *  This method sets handle to DataAccess to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
    }

 
    
    

    /*
    public void startDb(StatusBarL bar2)
    {
        GGCDbLoader loader = new GGCDbLoader(this, bar2);
        loader.start();
    } */
/*
    public GGCDb getDb()
    {
        return m_db;
    }
*/

    
    
    
    
    // ********************************************************
    // ******         Abstract Methods                    *****    
    // ********************************************************
    


    public String getApplicationName()
    {
    	return "GGC_CGMSTool";
    }
    
    
    
    public void checkPrerequisites()
    {
    }
    
    
    public String getImagesRoot()
    {
    	return "/icons/";
    }
    
    
    public void loadBackupRestoreCollection()
    {
    }
    
    
    
    


    // ********************************************************
    // ******                    Db                       *****    
    // ********************************************************



    // ********************************************************
    // ******                   Pumps                    *****    
    // ********************************************************

    public CGMManager getCGMManager()
    {
        return this.m_meterManager;
    }


    // ********************************************************
    // ******                   Pumps                    *****    
    // ********************************************************


    // ********************************************************
    // ******                  Settings                   *****    
    // ********************************************************

    public Color getColor(int color)
    {
        return new Color(color);
    }
/*
    public ConfigurationManager getConfigurationManager()
    {
	return this.m_cfgMgr;
    }
*/



    // ********************************************************
    // ******          Parent handling (for UIs)          *****    
    // ********************************************************

/*
    public I18nControlAbstract getI18nInstance()
    {
        return I18nControl.getInstance();
    }
*/
    /**
     *  Utils
     */


    public Image getImage(String filename, Component cmp)
    {
        Image img;

        InputStream is = this.getClass().getResourceAsStream(filename);

        if (is==null)
            System.out.println("Error reading image: "+filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            int c;
            while ((c = is.read()) >=0)
            baos.write(c);


            //JDialog.getT
            //JFrame.getToolkit();
            
            if (cmp==null)
            	cmp = new JLabel();
            
            img = cmp.getToolkit().createImage(baos.toByteArray());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return img;
    }



    // ********************************************************
    // ******               Look and Feel                 *****    
    // ********************************************************


    public static String[] getLFData()
    {
        String out[] = new String[2];

        try
        {
            Properties props = new Properties();

            FileInputStream in = new FileInputStream("../data/GGC_Config.properties");
            props.load(in);

            out[0] = (String)props.get("LF_CLASS");
            out[1] = (String)props.get("SKINLF_SELECTED");

            return out;

        }
        catch(Exception ex)
        {
            System.out.println("DataAccess::getLFData::Exception> " + ex);
            return null;
        }
    }




    // ********************************************************
    // ******          Dates and Times Handling           *****    
    // ********************************************************

    public String getCurrentDateString()
    {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.DAY_OF_MONTH) + "."
                + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR);
    }


    





    @Override
    public HibernateDb getHibernateDb()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void createConfigurationContext()
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void createDeviceConfiguration()
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void createPlugInAboutContext()
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void createPlugInVersion()
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void createWebListerContext()
    {
        // TODO Auto-generated method stub
        
    }




}
