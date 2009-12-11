package ggc.cgm.util;

import ggc.cgm.manager.CGMManager;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.atech.db.hibernate.HibernateDb;
import com.atech.i18n.mgr.LanguageManager;

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
 *  Filename:     DataAccessCGM  
 *  Description:  Singelton class containing all data used through plugin
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DataAccessCGM extends DataAccessPlugInBase
{



    private static DataAccessCGM s_da = null; // This is handle to unique 

    private CGMManager m_device_manager = null;

        
        

    // ********************************************************
    // ******      Constructors and Access methods        *****    
    // ********************************************************

    /**
     *
     *  This is DataAccess constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */
    private DataAccessCGM(LanguageManager lm)
    {
        super(lm, new GGCCGMICRunner());
    } 

    
    /** 
     * Init Special - All methods that we support should be called here
     */
    public void initSpecial()
    {
        this.loadFonts();

        checkPrerequisites();
    }
    
    
    /**
     *
     *  This method returns reference to DataAccessCGM object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to DataAccessCGM instance
     */
    public static DataAccessCGM getInstance()
    {
        //if (s_da == null)
        //    s_da = new DataAccessCGM();
        return s_da;
    }

    
    /**
     * Create Instance
     * 
     * @param lm
     * @return
     */
    public static DataAccessCGM createInstance(LanguageManager lm)
    {
        if (s_da == null)
            s_da = new DataAccessCGM(lm);
        return s_da;
    }
     

    //  Method:       deleteInstance
    /**
     *  This method sets handle to DataAccess to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
    }

 
    
    


    
    
    
    
    // ********************************************************
    // ******         Abstract Methods                    *****    
    // ********************************************************
    

    /** 
     * Get Application Name
     */
    public String getApplicationName()
    {
    	return "GGC_CGMSTool";
    }
    
    
    /** 
     * Check Prerequisites for Plugin
     */
    public void checkPrerequisites()
    {
    }
    
    
    
    
    // ********************************************************
    // ******                   Manager                   *****    
    // ********************************************************

    /**
     * Get Device Manager
     * 
     * @return
     */
    public CGMManager getCGMManager()
    {
        return this.m_device_manager;
    }



    // ********************************************************
    // ******          Parent handling (for UIs)          *****    
    // ********************************************************

    /**
     *  Utils
     */

/*
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
*/




    // ********************************************************
    // ******          Dates and Times Handling           *****    
    // ********************************************************

    public String getCurrentDateString()
    {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.DAY_OF_MONTH) + "."
                + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR);
    }


    // ********************************************************
    // ******                   Database                  *****    
    // ********************************************************


    /** 
     * Get HibernateDb instance (for working with database in plugin)
     */
    @Override
    public HibernateDb getHibernateDb()
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    // ********************************************************
    // ******                Configuration                *****    
    // ********************************************************
    
    
    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * Create Device Configuration for plugin
     */
    @Override
    public void createDeviceConfiguration()
    {
        // TODO Auto-generated method stub
    }

    
    // ********************************************************
    // ******            About Methods                    *****    
    // ********************************************************
    
    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInAboutContext()
    {
        // TODO Auto-generated method stub
    }

    
    // ********************************************************
    // ******                  Version                    *****    
    // ********************************************************
    
    /**
     * Create Plugin Version
     */
    @Override
    public void createPlugInVersion()
    {
        // TODO Auto-generated method stub
        
    }

    
    // ********************************************************
    // ******         Web Lister Methods                  *****    
    // ********************************************************
    
    /**
     * Create WebLister (for List) Context for plugin
     */
    @Override
    public void createWebListerContext()
    {
        // TODO Auto-generated method stub
        
    }

    
    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
        // TODO Auto-generated method stub
        
    }


    /**
     * Load Manager instance
     */
    @Override
    public void loadManager()
    {
        // TODO Auto-generated method stub
        
    }

    
    /**
     * Load Device Data Handler
     */
    @Override
    public void loadDeviceDataHandler()
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * Get Images for Devices
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#getDeviceImagesRoot()
     * @return String with images path 
     */
    @Override
    public String getDeviceImagesRoot()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Load PlugIns
     */
    @Override
    public void loadPlugIns()
    {
        // TODO Auto-generated method stub
    }


    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
    @Override
    public void createCustomDb()
    {
        // TODO Auto-generated method stub
        
    }


    /**
     * Create Old Data Reader
     */
    public void createOldDataReader()
    {
    }
    
    
}
