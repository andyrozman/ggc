package ggc.plugin.defs;

import java.util.ArrayList;
import java.util.List;

import com.atech.app.data.about.FeaturesEntry;
import com.atech.app.data.about.FeaturesGroup;
import com.atech.app.defs.AppPluginDefinitionAbstract;
import com.atech.i18n.I18nControlRunner;
import com.atech.i18n.mgr.LanguageManager;
import com.atech.utils.java.VersionResolver;

import ggc.core.plugins.GGCPluginType;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.graph.PluginGraphDefinition;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.report.PluginReportDefinition;

/**
 * Created by andy on 18.10.15.
 */
public abstract class DevicePluginDefinitionAbstract extends AppPluginDefinitionAbstract
{

    // private static final Logger LOG =
    // LoggerFactory.getLogger(DevicePluginDefinitionAbstract.class);

    protected String pluginVersion;
    protected GGCPluginType pluginType;
    protected String pluginActionPrefix;
    protected String pluginVersionClass;
    protected String pluginName;
    protected boolean useBaseLibraries = true;


    public DevicePluginDefinitionAbstract(LanguageManager languageManager, I18nControlRunner i18nControlRunner)
    {
        super(languageManager, i18nControlRunner);
        initVersion();
    }


    public DevicePluginDefinitionAbstract(LanguageManager languageManager, //
            I18nControlRunner i18nControlRunner, //
            String pluginName, //
            GGCPluginType pluginType, //
            String pluginActionPrefix, //
            String pluginVersionClass)
    {
        super(languageManager, i18nControlRunner);
        this.pluginName = pluginName;
        this.pluginType = pluginType;
        this.pluginActionPrefix = pluginActionPrefix;
        this.pluginVersionClass = pluginVersionClass;

        initVersion();
    }


    private void initVersion()
    {
        if (this.getPluginVersionClass() != null)
        {
            this.pluginVersion = VersionResolver.getVersion(this.getPluginVersionClass(),
                this.getClass().getSimpleName());
        }
    }


    public List<FeaturesGroup> getFeatures()
    {
        List<FeaturesGroup> listFeatures = new ArrayList<FeaturesGroup>();

        addFeatureGroup("IMPLEMENTED_FEATURES", getImplementedFeatures(), listFeatures);
        addFeatureGroup("SUPPORTED_DEVICES", getSupportedDevices(), listFeatures);
        addFeatureGroup("NOT_IMPLEMENTED_FEATURES", getNotImplementedFeatures(), listFeatures);
        addFeatureGroup("PLANNED_DEVICES", getPlannedDevices(), listFeatures);

        return listFeatures;
    }


    /**
     * Get Name of Plugin (for internal use)
     *
     * @return
     */
    public String getPluginName()
    {
        return this.pluginName;
    }


    /**
     * getPluginType - get Plugin Type
     */
    public GGCPluginType getPluginType()
    {
        return this.pluginType;
    }


    /**
     * This is prefix for this plugins actions. So for example PumpPlugin has
     * prefix 'pumps_', so we know that actionCommand that starts with 'pumps_' is
     * intended for this plugin.
     *
     * @return Prefix with underscore
     */
    public String getPluginActionsPrefix()
    {
        return this.pluginActionPrefix;
    }


    public String getPluginVersion()
    {
        return this.pluginVersion;
    }


    public String getPluginVersionClass()
    {
        return this.pluginVersionClass;
    }


    public List<FeaturesEntry> getImplementedFeatures()
    {
        return null;
    }


    public List<FeaturesEntry> getNotImplementedFeatures()
    {
        return null;
    }


    public List<FeaturesEntry> getSupportedDevices()
    {
        return null;
    }


    public List<FeaturesEntry> getPlannedDevices()
    {
        return null;
    }


    public abstract List<BaseListEntry> getWebListerItems();


    public String getWebListerDescription()
    {
        return this.i18nControl.getMessage("DEVICE_LIST_WEB_DESC");
    }


    public abstract PluginReportDefinition getReportsDefinition();


    public abstract PluginGraphDefinition getGraphsDefinition();


    /**
     * Register Device Handlers - Used for registration of DeviceHandlers for Devices V2.
     * This will dinamically load all handlers. If you had to change loading of
     * handlers, override this method.
     */
    public void registerDeviceHandlers()
    {
        // register dynamic handlers
        DeviceHandlerManager.getInstance().registerDeviceHandlersDynamically(getPluginType());
    }


    public boolean isUseBaseLibraries()
    {
        return useBaseLibraries;
    }
}
