package ggc.nutri.defs;

import java.util.List;

import com.atech.app.data.about.CreditsGroup;
import com.atech.i18n.mgr.LanguageManager;

import ggc.core.plugins.GGCPluginType;
import ggc.nutri.report.def.NutriReportDefinition;
import ggc.nutri.util.GGCNutriICRunner;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.graph.PluginGraphDefinition;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.report.PluginReportDefinition;

/**
 * Created by andy on 18.10.15.
 */
public class NutriPluginDefinition extends DevicePluginDefinitionAbstract
{

    private static String PLUGIN_NAME = "GGC Nutrition Plugin";


    public NutriPluginDefinition(LanguageManager languageManager)
    {
        super(languageManager, //
                new GGCNutriICRunner(), //
                PLUGIN_NAME, //
                GGCPluginType.NutritionToolPlugin, //
                "nutri_", //
                "ggc.nutri.defs.Version");
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

    // FIXME-Andy
    NutriReportDefinition reportsNutriDefinition;


    @Override
    public PluginReportDefinition getReportsDefinition()
    {
        // FIXME-Andy
        return null;

        // if (reportsNutriDefinition == null)
        // {
        // this.reportsNutriDefinition = new
        // NutriReportDefinition((DataAccessNutri) this.dataAccess);
        // }
        // return reportsNutriDefinition;
    }


    @Override
    public PluginGraphDefinition getGraphsDefinition()
    {
        // no graphs
        return null;
    }

}
