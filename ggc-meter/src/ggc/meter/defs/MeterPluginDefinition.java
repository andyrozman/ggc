package ggc.meter.defs;

import java.util.ArrayList;
import java.util.List;

import com.atech.app.data.about.CreditsEntry;
import com.atech.app.data.about.CreditsGroup;
import com.atech.app.data.about.FeaturesEntry;
import com.atech.app.data.about.LibraryInfoEntry;
import com.atech.i18n.mgr.LanguageManager;

import ggc.core.plugins.GGCPluginType;
import ggc.meter.device.ascensia.AscensiaUsbMeterHandler;
import ggc.meter.util.GGCMeterICRunner;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.graph.PluginGraphDefinition;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.report.PluginReportDefinition;

/**
 * Created by andy on 18.10.15.
 */
public class MeterPluginDefinition extends DevicePluginDefinitionAbstract
{

    String PLUGIN_VERSION = "2.4.1";
    String PLUGIN_NAME = "GGC Meter Plugin";


    public MeterPluginDefinition(LanguageManager languageManager)
    {
        super(languageManager, new GGCMeterICRunner());
    }


    public int getCopyrightFrom()
    {
        return 2006;
    }


    public String getAboutImagePath()
    {
        return "/icons/about_meter.jpg";
    }


    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 200;
        sz[1] = 125;

        return sz;
    }


    @Override
    public List<LibraryInfoEntry> getPluginLibraries()
    {
        List<LibraryInfoEntry> libraryInfoList = new ArrayList<LibraryInfoEntry>();

        libraryInfoList.add(
            new LibraryInfoEntry("XML Pull Parser", "3.1.1.4c", "http://www.extreme.indiana.edu/xgws/xsoap/xpp/",
                    "Indiana University Extreme! Lab Software License", "Xml parser for processing xml document",
                    "Copyright (c) 2002 Extreme! Lab, Indiana University. All rights reserved."));

        return libraryInfoList;
    }


    public List<CreditsGroup> getCredits()
    {
        ArrayList<CreditsGroup> creditsGroups = new ArrayList<CreditsGroup>();
        CreditsGroup cg = new CreditsGroup(i18nControl.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com",
                "Full framework and support for Ascensia, Roche, LifeScan devices"));
        cg.addCreditsEntry(
            new CreditsEntry("Alexander Balaban", "abalaban1@yahoo.ca", "Support (partitial) for OT UltraSmart"));
        cg.addCreditsEntry(new CreditsEntry("Ophir Setter", "ophir.setter@gmail.com", "Support for Freestyle Meters"));
        creditsGroups.add(cg);
        cg = new CreditsGroup(i18nControl.getMessage("HELPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Rafael Ziherl (RAF)", "", "Supplied hardware for Roche development"));
        creditsGroups.add(cg);

        return creditsGroups;
    }


    public List<FeaturesEntry> getImplementedFeatures()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Base Meter Tool Framework"));
        outList.add(new FeaturesEntry("Various output types"));
        outList.add(new FeaturesEntry("Communication Framework"));
        outList.add(new FeaturesEntry("Graphical Interface (GGC integration)"));
        outList.add(new FeaturesEntry("About dialog"));
        outList.add(new FeaturesEntry("List of meters"));
        outList.add(new FeaturesEntry("Configuration"));

        return outList;
    }


    public List<FeaturesEntry> getNotImplementedFeatures()
    {
        return null;
    }


    public List<FeaturesEntry> getSupportedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Ascensia/Bayer: Most devices"));
        outList.add(new FeaturesEntry("Accu-Chek/Roche: All supported by SmartPix 3.x"));
        outList.add(new FeaturesEntry("LifeScan: Ultra, Profile, Easy, UltraSmart"));
        outList.add(new FeaturesEntry("Abbott: Optium Xceeed, PrecisionXtra, Frestyle"));

        return outList;
    }


    public List<FeaturesEntry> getPlannedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("LifeScan: Ultra2 (in 2016)"));
        outList.add(new FeaturesEntry("Wellion: Calla, Luna (in 2016 ?)"));

        return outList;
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
        return GGCPluginType.MeterToolPlugin;
    }


    @Override
    public List<BaseListEntry> getWebListerItems()
    {
        List<BaseListEntry> weblisterItems = new ArrayList<BaseListEntry>();

        weblisterItems.add(new BaseListEntry("Abbott Diabetes Care", "/meters/abbott.html", 4));
        weblisterItems.add(new BaseListEntry("Arkray USA (formerly Hypoguard)", "/meters/arkray.html", 5));
        weblisterItems.add(new BaseListEntry("Bayer Diagnostics", "/meters/bayer.html", 1));
        weblisterItems.add(new BaseListEntry("Diabetic Supply of Suncoast", "/meters/dsos.html", 5));
        weblisterItems.add(new BaseListEntry("Diagnostic Devices", "/meters/prodigy.html", 5));
        weblisterItems.add(new BaseListEntry("HealthPia America", "/meters/healthpia.html", 5));
        weblisterItems.add(new BaseListEntry("Home Diagnostics", "/meters/home_diagnostics.html", 5));
        weblisterItems.add(new BaseListEntry("Lifescan", "/meters/lifescan.html", 4));
        weblisterItems.add(new BaseListEntry("Nova Biomedical", "/meters/nova_biomedical.html", 5));
        weblisterItems.add(new BaseListEntry("Roche Diagnostics", "/meters/roche.html", 2));
        weblisterItems.add(new BaseListEntry("Sanvita", "/meters/sanvita.html", 5));
        weblisterItems.add(new BaseListEntry("U.S. Diagnostics", "/meters/us_diagnostics.html", 5));
        weblisterItems.add(new BaseListEntry("WaveSense", "/meters/wavesense.html", 5));

        return weblisterItems;
    }


    @Override
    public String getWebListerDescription()
    {
        return this.i18nControl.getMessage("DEVICE_LIST_WEB_DESC");
    }


    @Override
    public PluginReportDefinition getReportsDefinition()
    {
        // no reports for this plugin
        return null;
    }


    @Override
    public PluginGraphDefinition getGraphsDefinition()
    {
        // no graphs for this plugin
        return null;
    }


    @Override
    public String getPluginActionsPrefix()
    {
        return "meters_";
    }


    @Override
    public void registerDeviceHandlers()
    {
        // Ascensia
        DeviceHandlerManager.getInstance().addDeviceHandler(new AscensiaUsbMeterHandler());
    }

}
