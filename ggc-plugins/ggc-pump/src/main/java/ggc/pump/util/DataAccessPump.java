package ggc.pump.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.atech.app.data.about.ModuleInfoEntry;
import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.data.enums.DeviceEntryStatus;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpDataHandler;
import ggc.pump.data.PumpDataReader;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.cfg.PumpConfigurationDefinition;
import ggc.pump.data.defs.*;
import ggc.pump.data.util.PumpBasalManager;
import ggc.pump.data.util.PumpBolusManager;
import ggc.pump.db.GGCPumpDb;
import ggc.pump.defs.PumpPluginDefinition;
import ggc.pump.defs.report.PumpReportDefinition;
import ggc.pump.device.insulet.util.InsuletUtil;
import ggc.pump.graph.PumpGraphContext;
import ggc.pump.manager.PumpManager;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     DataAccessPump
 *  Description:  Contains instances of utilities, private (plugin specific) data, help classes, ...
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class DataAccessPump extends DataAccessPlugInBase
{

    private static DataAccessPump s_da = null; // This is handle to unique

    private PumpManager m_pumpManager = null;

    private PumpBasalManager pumpBasalManager;
    private PumpBolusManager pumpBolusManager;
    private PumpReportDefinition reportsPumpDefinition;

    /**
     * The selected_person_id.
     */
    // long selected_person_id = 0;

    // ********************************************************
    // ****** Constructors and Access methods *****
    // ********************************************************


    // Constructor: DataAccess
    /**
     *
     * This is DataAccess constructor; Since classes use Singleton Pattern,
     * constructor is protected and can be accessed only with getInstance()
     * method.<br>
     * <br>
     *
     * @param pumpPluginDefinition
     */
    private DataAccessPump(PumpPluginDefinition pumpPluginDefinition)
    {
        super(pumpPluginDefinition);
    }


    /**
     * Init Special
     */
    @Override
    public void initSpecial()
    {
        // appPluginDefinition.setDataAccess(this);

        // this.createWebListerContext();
        // this.createPlugInAboutContext();
        this.createConfigurationContext();

        loadDeviceDataHandler();
        // loadManager();
        // loadReadingStatuses();
        this.createPlugInDataRetrievalContext();
        this.createDeviceConfiguration();
        this.createOldDataReader();
        loadWebLister();
        this.loadConverters();
        this.loadSorters();
        this.loadGraphContext();

        this.prepareTranslationForEnums();

        // managers : basal manager
        this.prepareManagers();

        loadExternalLibrariesInDevelopment();

        InsuletUtil.setDataAccess(this);

    }


    private void loadExternalLibrariesInDevelopment()
    {
        // File fileMinimed = new File("../MinimedIntegration.txt");
        //
        // System.out.println("" + fileMinimed.exists());
        //
        // if (fileMinimed.exists())
        // {
        // this.loadExternalLibrary("ggc.pump.test.MinimedIntegration");
        // }
    }


    public void prepareManagers()
    {
        this.pumpBasalManager = new PumpBasalManager(this);
        this.pumpBolusManager = new PumpBolusManager(this);
    }


    public PumpBasalManager getBasalManager()
    {
        return this.pumpBasalManager;
    }


    public PumpBolusManager getBolusManager()
    {
        return this.pumpBolusManager;
    }


    private void prepareTranslationForEnums()
    {
        SoundValueType.translateKeywords(this.getI18nControlInstance());
        GlucoseUnitType.translateKeywords(this.getI18nControlInstance());
        ClockModeType.translateKeywords(this.getI18nControlInstance());
        AnimasSoundType.translateKeywords(this.getI18nControlInstance(), this.getPluginType());
        DeviceEntryStatus.translateKeywords(this.getI18nControlInstance());
        PumpConfigurationGroup.translateKeywords(this.getI18nControlInstance());
        PumpBaseType.translateKeywords(this.getI18nControlInstance());
        PumpReport.translateKeywords(this.getI18nControlInstance());
        PumpAlarms.translateKeywords(this.getI18nControlInstance());
        RatioType.translateKeywords(this.getI18nControlInstance());
        PumpAdditionalDataType.translateKeywords(this.getI18nControlInstance());
        PumpBasalType.translateKeywords(this.getI18nControlInstance());
        PumpErrors.translateKeywords(this.getI18nControlInstance());
        PumpEventType.translateKeywords(this.getI18nControlInstance());
        PumpBolusType.translateKeywords(this.getI18nControlInstance());
    }


    /**
     * getInstance
     * 
     * This method returns reference to DataAccessPump object created, or if no
     * object was created yet, it creates one.<br>
     * <br>
     *
     * @return Reference to DataAccessPump object
     *
     */
    public static DataAccessPump getInstance()
    {
        // if (s_da == null)
        // s_da = new DataAccessPump();
        return s_da;
    }


    /**
     * Create Instance
     *
     * @param pumpPluginDefinition
     * @return
     */
    public static DataAccessPump createInstance(PumpPluginDefinition pumpPluginDefinition)
    {
        if (s_da == null)
        {
            s_da = new DataAccessPump(pumpPluginDefinition);
        }
        return s_da;
    }


    // Method: deleteInstance
    /**
     * This method sets handle to DataAccess to null and deletes the instance. <br>
     * <br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
        i18n_plugin = null;
    }

    // ********************************************************
    // ****** Init Methods *****
    // ********************************************************


    // ********************************************************
    // ****** Dates and Times Handling *****
    // ********************************************************

    /**
     * Get Start Year
     */
    @Override
    public int getStartYear()
    {
        return 1980;
    }

    // ********************************************************
    // ****** Db *****
    // ********************************************************

    /**
     * The m_db.
     */
    GGCPumpDb m_db;


    /**
     * Create Custom Db
     *
     * This is for plug-in specific database implementation
     */
    @Override
    public void createCustomDb()
    {
        this.m_db = new GGCPumpDb(this.hibernateDb);
        this.pluginDb = this.m_db;
    }


    /**
     * Get Db
     *
     * @return
     */
    public GGCPumpDb getDb()
    {
        return this.m_db;
    }


    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
        this.device_config_def = new PumpConfigurationDefinition();
    }


    /**
     * Create Device Configuration for plugin
     */
    @Override
    public void createDeviceConfiguration()
    {
        this.device_config = new DeviceConfiguration(this);
    }


    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
        loadBasePluginTranslations();

        m_entry_type = new PumpValuesEntry(true);
        this.data_download_screen_wide = true;
    }


    /**
     * Load Manager instance
     */
    @Override
    public void loadManager()
    {
        this.m_pumpManager = PumpManager.getInstance(this);
        this.m_manager = this.m_pumpManager;
    }


    /**
     * Load Device Data Handler
     */
    @Override
    public void loadDeviceDataHandler()
    {
        this.deviceDataHandler = new PumpDataHandler(this);
    }


    /**
     * Get Images for Devices (must end with backslash)
     *
     * @return String with images path
     */
    @Override
    public String getDeviceImagesRoot()
    {
        return "/icons/pumps/";
    }


    /**
     * Get Selected Persons Id
     *
     * @return
     */
    /*
     * public long getSelectedPersonId() { return this.selected_person_id; } /**
     * Set Selected Persons Id
     * @param id
     */
    /*
     * public void setSelectedPersonId(long id) { this.selected_person_id = id;
     * }
     */

    /**
     * Load Special Parameters
     *
     * @see com.atech.utils.ATDataAccessAbstract#loadSpecialParameters()
     */
    @Override
    public void loadSpecialParameters()
    {
        this.special_parameters = new Hashtable<String, String>();
        this.special_parameters.put("BG", "" + this.getGlucoseUnitType());
    }


    // configuration settings

    /**
     * Gets the max basal value.
     *
     * @return the max basal value
     */
    public float getMaxBasalValue()
    {
        // TODO read from pump
        return 8.0f;
    }


    /**
     * Gets the max bolus value.
     *
     * @return the max bolus value
     */
    public float getMaxBolusValue()
    {
        // TODO read from pump
        return 25.0f;
    }


    /**
     * Gets the basal step.
     *
     * @return the basal step
     */
    public float getBasalStep()
    {
        // TODO read from pump
        return 0.05f;
    }


    /**
     * Gets the bolus step.
     *
     * @return the bolus step
     */
    public float getBolusStep()
    {
        // TODO read from pump
        return 0.1f;
    }


    /**
     * Get Bolus Precision
     *
     * @return
     */
    public int getBolusPrecision()
    {
        return 1;
    }


    /**
     * Get Basal Precision
     *
     * @return
     */
    public int getBasalPrecision()
    {
        return 2;
    }


    /**
     * Create Old Data Reader
     */
    @Override
    public void createOldDataReader()
    {
        this.m_old_data_reader = new PumpDataReader(this);
    }


    /**
     * Get Formated Bolus Value
     *
     * @param val
     * @return
     */
    public String getFormatedBolusValue(float val)
    {
        return this.getDecimalHandler().getDecimalNumberAsString(val, this.getBolusPrecision());
    }


    /**
     * Get Formated Bolus Value
     *
     * @param val
     * @return
     */
    public String getFormatedBasalValue(float val)
    {
        return this.getDecimalHandler().getDecimalNumberAsString(val, this.getBasalPrecision());
    }


    /**
     * Get Decimal Handler
     *
     * @return
     */
    /*
     * public DecimalHandler getDecimalHandler() { return this.dec_handler; }
     */

    /**
     * Get Parsed Values
     *
     * @param val
     * @return
     */
    public String[] getParsedValues(String val)
    {
        ArrayList<String> lst = new ArrayList<String>();

        StringTokenizer strtok = new StringTokenizer(val, ";");

        while (strtok.hasMoreTokens())
        {
            String tk = strtok.nextToken();
            lst.add(tk.substring(tk.indexOf("=") + 1));
        }

        String ia[] = new String[lst.size()];
        return lst.toArray(ia);
    }


    @Override
    public void loadSorters()
    {
        sorters.put("Profile", "DESC");
    }


    /**
     * Get Max Decimals that will be used by DecimalHandler
     *
     * @return
     */
    @Override
    public int getMaxDecimalsUsedByDecimalHandler()
    {
        return 3;
    }


    @Override
    public void prepareGraphContext()
    {

    }


    /**
     * Load Graph Context
     */
    public void loadGraphContext()
    {
        this.graph_context = new PumpGraphContext();
    }


    public PumpBasalManager getPumpBasalManager()
    {
        return pumpBasalManager;
    }


    @Override
    public ModuleInfoEntry getPluginModule() {
        return ModuleInfoEntry.builder()
                .name(i18n.getMessage("PUMP_PLUGIN_NAME"))
                .version(new ggc.pump.defs.Version().getVersion())
                .description(i18n.getMessage("PUMP_PLUGIN_DESCRIPTION"))
                .build();
    }


}
