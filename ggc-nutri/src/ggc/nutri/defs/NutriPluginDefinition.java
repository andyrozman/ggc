package ggc.nutri.defs;

import java.util.List;

import com.atech.app.data.about.CreditsGroup;
import com.atech.i18n.I18nControlRunner;
import com.atech.i18n.mgr.LanguageManager;

import ggc.core.plugins.GGCPluginType;
import ggc.nutri.report.def.NutriReportDefinition;
import ggc.nutri.util.DataAccessNutri;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.report.PluginReportDefinition;

/**
 * Created by andy on 18.10.15.
 */
public class NutriPluginDefinition extends DevicePluginDefinitionAbstract
{

    String PLUGIN_VERSION = "1.3.6";
    String PLUGIN_NAME = "GGC Nutrition Plugin";


    public NutriPluginDefinition(DataAccessNutri dataAccessPump)
    {
        super(dataAccessPump);
    }


    public NutriPluginDefinition(LanguageManager languageManager, I18nControlRunner i18nControlRunner)
    {
        super(languageManager, i18nControlRunner);
    }


    public int getCopyrightFrom()
    {
        return 2009;
    }


    public List<CreditsGroup> getCredits()
    {
        return null;
    }


    public String getAboutImagePath()
    {
        return null;
    }


    public int[] getAboutImageSize()
    {
        return new int[0];
    }


    public String getPluginVersion()
    {
        return this.PLUGIN_VERSION;
    }


    public String getPluginName()
    {
        return this.PLUGIN_NAME;
    }


    public GGCPluginType getPluginType()
    {
        return GGCPluginType.NutritionToolPlugin;
    }


    @Override
    public List<BaseListEntry> getWebListerItems()
    {
        return null;
    }


    @Override
    public String getWebListerDescription()
    {
        return null;
    }

    NutriReportDefinition reportsNutriDefinition;


    @Override
    public PluginReportDefinition getReportsDefinition()
    {
        if (reportsNutriDefinition == null)
        {
            this.reportsNutriDefinition = new NutriReportDefinition((DataAccessNutri) this.dataAccess);
        }
        return reportsNutriDefinition;
    }

}
