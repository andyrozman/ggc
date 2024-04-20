package ggc.nutri.util;

import javax.swing.*;

import com.atech.app.data.about.ModuleInfoEntry;
import ggc.core.db.GGCDb;
import ggc.nutri.db.GGCDbCache;
import ggc.nutri.db.GGCDbNutri;
import ggc.nutri.defs.NutriPluginDefinition;
import ggc.plugin.util.DataAccessPlugInBase;

// TODO: Auto-generated Javadoc
/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     DataAccessNutri
 *  Description:  Static data class used by Nutrition Plugin.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// FIXME needs little refactoring and deleting of stuff
public class DataAccessNutri extends DataAccessPlugInBase
{

    private static DataAccessNutri s_da = null; // This is handle to unique

    /**
     * The m_main.
     */
    JFrame m_main = null;

    // ********************************************************
    // ****** Constructors and Access methods *****
    // ********************************************************


    // Constructor: DataAccessMeter
    /**
     *
     *  This is DataAccessMeter constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */
    private DataAccessNutri(NutriPluginDefinition nutriPluginDefinition)
    {
        super(nutriPluginDefinition);
        initSpecial();
    }


    /** 
     * Init Special - All methods that we support should be called here
     */
    @Override
    public void initSpecial()
    {
        // System.out.println("init special");
        // checkPrerequisites();
        // createWebListerContext();
        // createPlugInAboutContext();
        // createConfigurationContext();
        // createPlugInVersion();
        // loadDeviceDataHandler();
        // loadManager();
        // loadReadingStatuses();
        // createPlugInDataRetrievalContext();
        // loadWebLister();
    }


    // Method: getInstance
    // Author: Andy
    /**
     *
     *  This method returns reference to OmniI18nControl object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to OmniI18nControl object
     * 
     */
    public static DataAccessNutri getInstance()
    {
        // if (s_da == null)
        // s_da = new DataAccessNutri(null);
        return s_da;
    }


    /**
     * Create Instance 
     * 
     * @param nutriPluginDefinition
     * @return
     */
    public static DataAccessNutri createInstance(NutriPluginDefinition nutriPluginDefinition)
    {
        if (s_da == null)
        {
            s_da = new DataAccessNutri(nutriPluginDefinition);
        }
        return s_da;
    }


    /**
     *  This method sets handle to DataAccessMeter to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        super.m_i18n = null;
    }

    // ********************************************************
    // ****** About Methods *****
    // ********************************************************

    // /**
    // * Create About Context for plugin
    // */
    // @Override
    // public void createPlugInAboutContext()
    // {
    // }

    // ********************************************************
    // ****** Web Lister Methods *****
    // ********************************************************

    // ********************************************************
    // ****** Abstract Methods *****
    // ********************************************************


    // ********************************************************
    // ****** Configuration *****
    // ********************************************************

    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
    }


    /**
     * Create Device Configuration for plugin
     */
    @Override
    public void createDeviceConfiguration()
    {
        // this.device_config = new DeviceConfiguration(this);
    }


    /**
     * Create Data Retrieval Context for Plug-in
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#createPlugInDataRetrievalContext()
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
    }


    /**
     * Load Device Manager
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadManager()
     */
    @Override
    public void loadManager()
    {
    }


    /**
     * Load Device Data Handler
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadDeviceDataHandler()
     */
    @Override
    public void loadDeviceDataHandler()
    {
    }


    /**
     * Get Images for Devices
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#getDeviceImagesRoot()
     */
    @Override
    public String getDeviceImagesRoot()
    {
        return "/icons/meters/";
    }


    /**
     * Gets the db.
     * 
     * @return the db
     */
    public GGCDb getDb()
    {
        // return DataAccessNutri.getInstance().getDb();
        return null;
    }

    /**
     * The db_nutri.
     */
    GGCDbNutri db_nutri = null;


    /**
     * Gets the nutri db.
     * 
     * @return the nutri db
     */
    public GGCDbNutri getNutriDb()
    {
        return db_nutri;
    }


    /**
     * Sets the nutri db
     * 
     * @param db the new nutri db
     */
    public void setNutriDb(GGCDbNutri db)
    {
        db_nutri = db;
    }


    /**
     * Gets the db cache
     * 
     * @return the db cache
     */
    public GGCDbCache getDbCache()
    {
        return db_nutri.getDbCache();
    }


    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
    @Override
    public void createCustomDb()
    {
    }


    @Override
    public void prepareGraphContext()
    {

    }

    @Override
    public boolean includeBasePluginLibraries() {
        return false;
    }


    @Override
    public ModuleInfoEntry getPluginModule() {
        return ModuleInfoEntry.builder()
                .name(i18n.getMessage("NUTRI_PLUGIN_NAME"))
                .version(new ggc.nutri.defs.Version().getVersion())
                .description(i18n.getMessage("NUTRI_PLUGIN_DESCRIPTION"))
                .build();
    }

}
