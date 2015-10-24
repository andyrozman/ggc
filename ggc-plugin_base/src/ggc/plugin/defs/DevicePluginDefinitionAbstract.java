package ggc.plugin.defs;

import java.util.ArrayList;
import java.util.List;

import com.atech.app.data.about.FeaturesEntry;
import com.atech.app.data.about.FeaturesGroup;
import com.atech.app.defs.AppPluginDefinitionAbstract;
import com.atech.i18n.I18nControlRunner;
import com.atech.i18n.mgr.LanguageManager;

import ggc.core.plugins.GGCPluginType;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.report.PluginReportDefinition;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * Created by andy on 18.10.15.
 */
public abstract class DevicePluginDefinitionAbstract extends AppPluginDefinitionAbstract
{

    public DevicePluginDefinitionAbstract(DataAccessPlugInBase dataAccess)
    {
        super(dataAccess);
    }


    public DevicePluginDefinitionAbstract(LanguageManager languageManager, I18nControlRunner i18nControlRunner)
    {
        super(languageManager, i18nControlRunner);
    }


    public List<FeaturesGroup> getFeatures()
    {
        List<FeaturesGroup> lst_features = new ArrayList<FeaturesGroup>();

        addFeatureGroup("IMPLEMENTED_FEATURES", getImplementedFeatures(), lst_features);
        addFeatureGroup("SUPPORTED_DEVICES", getSupportedDevices(), lst_features);
        addFeatureGroup("NOT_IMPLEMENTED_FEATURES", getNotImplementedFeatures(), lst_features);
        addFeatureGroup("PLANNED_DEVICES", getPlannedDevices(), lst_features);

        return lst_features;
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


    public abstract GGCPluginType getPluginType();


    public abstract List<BaseListEntry> getWebListerItems();


    public abstract String getWebListerDescription();


    public abstract PluginReportDefinition getReportsDefinition();


    /**
     * Register Device Handlers - Used for registration of DeviceHandlers for Devices V2
     */
    public void registerDeviceHandlers()
    {
    }
}
