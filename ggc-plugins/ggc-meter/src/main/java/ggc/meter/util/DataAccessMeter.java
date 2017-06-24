package ggc.meter.util;

import ggc.core.data.ExtendedDailyValueHandler;
import ggc.meter.data.MeterDataHandler;
import ggc.meter.data.MeterDataReader;
import ggc.meter.data.MeterValuesEntryDataType;
import ggc.meter.data.cfg.MeterConfigurationDefinition;
import ggc.meter.data.db.GGCMeterDb;
import ggc.meter.defs.MeterPluginDefinition;
import ggc.meter.device.MeterDeviceInstanceWithHandler;
import ggc.meter.device.MeterDisplayInterfaceType;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterManager;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.data.enums.DeviceEntryStatus;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     DataAccessMeter  
 *  Description:  Static data class used by Meter Plugin.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DataAccessMeter extends DataAccessPlugInBase
{

    // private static final Logger LOG =
    // LoggerFactory.getLogger(DataAccessMeter.class);

    private static DataAccessMeter s_da = null;

    private MeterManager m_meterManager = null;

    /**
     * Extended Handler: Daily Value
     */
    public static final String EXTENDED_HANDLER_DAILY_VALUE = "dvh";

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
    private DataAccessMeter(MeterPluginDefinition meterPluginDefinition)
    {
        super(meterPluginDefinition);

        initSpecial();
    }


    /** 
     * Init Special - All methods that we support should be called here
     */
    @Override
    public void initSpecial()
    {
        // System.out.println("init special");

        // createWebListerContext();
        // createPlugInAboutContext();
        createConfigurationContext();
        // createPlugInVersion();
        loadDeviceDataHandler();
        // loadManager();
        // loadReadingStatuses();
        createPlugInDataRetrievalContext();
        loadWebLister();
        createOldDataReader();
        this.loadConverters();
        this.loadExtendedHandlers();

        prepareTranslationForEnums();
    }


    public void prepareTranslationForEnums()
    {
        DeviceEntryStatus.translateKeywords(this.getI18nControlInstance());
        MeterValuesEntryDataType.translateKeywords(this.getI18nControlInstance());
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
    public static DataAccessMeter getInstance()
    {
        // if (s_da == null)
        // s_da = new DataAccessMeter(null);
        return s_da;
    }


    /**
     * Create Instance
     * 
     * @param meterPluginDefinition MeterPluginDefinition instance
     *
     * @return DataAccessMeter static instance
     */
    public static DataAccessMeter createInstance(MeterPluginDefinition meterPluginDefinition)
    {
        if (s_da == null)
        {
            s_da = new DataAccessMeter(meterPluginDefinition);
        }
        return s_da;
    }


    /**
     *  This method sets handle to DataAccessMeter to null and deletes the instance. 
     */
    public void deleteInstance()
    {
        super.m_i18n = null;
    }

    // ********************************************************
    // ****** Version *****
    // ********************************************************


    // ********************************************************
    // ****** Manager *****
    // ********************************************************

    /**
     * Get Device Manager
     * 
     * @return
     */
    public MeterManager getMeterManager()
    {
        return this.m_meterManager;
    }

    // ********************************************************
    // ****** Db *****
    // ********************************************************

    /**
     * The m_db.
     */
    GGCMeterDb m_db;


    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
    @Override
    public void createCustomDb()
    {
        this.m_db = new GGCMeterDb(this.hibernateDb);
        this.pluginDb = this.m_db;
    }


    /**
     * Get Db
     * 
     * @return
     */
    public GGCMeterDb getDb()
    {
        return this.m_db;
    }


    // ********************************************************
    // ****** Configuration *****
    // ********************************************************

    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
        this.device_config_def = new MeterConfigurationDefinition();
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
     * Create Data Retrieval Context for Plug-in
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#createPlugInDataRetrievalContext()
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
        loadBasePluginTranslations();
    }


    /**
     * Load Device Manager
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadManager()
     */
    @Override
    public void loadManager()
    {
        this.m_manager = MeterManager.getInstance(this);
    }


    /**
     * Load Device Data Handler
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadDeviceDataHandler()
     */
    @Override
    public void loadDeviceDataHandler()
    {
        this.m_ddh = new MeterDataHandler(this);
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
     * Create Old Data Reader
     */
    @Override
    public void createOldDataReader()
    {
        this.m_old_data_reader = new MeterDataReader(this);
    }


    /**
     * Is Data Download Screen Wide
     * 
     * @return
     */
    @Override
    public boolean isDataDownloadScreenWide()
    {

        MeterDisplayInterfaceType displayInterfaceType = MeterDisplayInterfaceType.Simple;

        if (getSelectedDeviceInstance() instanceof MeterInterface)
        {
            MeterInterface mi = (MeterInterface) getSelectedDeviceInstance();

            if (mi.getInterfaceTypeForMeter() != MeterInterface.METER_INTERFACE_SIMPLE)
            {
                displayInterfaceType = MeterDisplayInterfaceType.Extended;
            }
        }
        else
        {
            MeterDeviceInstanceWithHandler deviceInstance = (MeterDeviceInstanceWithHandler) getSelectedDeviceInstance();

            displayInterfaceType = deviceInstance.getInterfaceTypeForMeter();
        }

        return (displayInterfaceType == MeterDisplayInterfaceType.Extended);
    }


    @Override
    public void prepareGraphContext()
    {

    }


    // ExtendedDailyValue edv_handler = null;

    /**
     * Get Extended Daily Value Handler
     * @return
     */
    public ExtendedDailyValueHandler getExtendedDailyValueHandler()
    {
        return (ExtendedDailyValueHandler) this.getExtendedHandler(EXTENDED_HANDLER_DAILY_VALUE);
    }


    /**
     * Load Extended Handlers. Database tables can contain extended field, which is of type text and can
     *    contain a lot of other data, stored in this field, this is hanlder for that field. Each table, 
     *    would use different handler.
     */
    @Override
    public void loadExtendedHandlers()
    {
        // System.out.println("Load Extended Handler: " + new
        // ExtendedDailyValue(this));

        this.addExtendedHandler(EXTENDED_HANDLER_DAILY_VALUE, new ExtendedDailyValueHandler());
        // this.extended_handlers = new Hashtable<>();
        // this.extended_handlers.put(EXTENDED_HANDLER_DAILY_VALUE, new
        // ExtendedDailyValue(this));

        System.out.println("!!!!!!!!!!!!!! LoadExtendedHandler: " + extended_handlers);

    }

}
