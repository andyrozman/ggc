package main.java.ggc.pump.defs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.app.data.about.CreditsEntry;
import com.atech.app.data.about.CreditsGroup;
import com.atech.app.data.about.FeaturesEntry;
import com.atech.app.data.about.LibraryInfoEntry;
import com.atech.i18n.mgr.LanguageManager;

import ggc.core.plugins.GGCPluginType;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.graph.PluginGraphDefinition;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.report.PluginReportDefinition;
import main.java.ggc.pump.defs.report.PumpReportDefinition;
import main.java.ggc.pump.device.accuchek.AccuChekPumpHandler;
import main.java.ggc.pump.device.animas.AnimasIR1200Handler;
import main.java.ggc.pump.device.dana.DanaPumpHandler;
import main.java.ggc.pump.device.insulet.InsuletHandler;
import main.java.ggc.pump.device.minimed.MinimedPumpDeviceHandler;
import main.java.ggc.pump.graph.PumpGraphDefintion;
import main.java.ggc.pump.util.DataAccessPump;
import main.java.ggc.pump.util.GGCPumpICRunner;

/**
 * Created by andy on 18.10.15.
 */
public class PumpPluginDefinition extends DevicePluginDefinitionAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(PumpPluginDefinition.class);

    String PLUGIN_VERSION = "2.0.1";
    String PLUGIN_NAME = "GGC Pump Plugin";

    PumpReportDefinition reportsPumpDefinition;
    PumpGraphDefintion graphsPumpDefinition;


    public PumpPluginDefinition(LanguageManager languageManager)
    {
        super(languageManager, new GGCPumpICRunner());
    }


    public int getCopyrightFrom()
    {
        return 2008;
    }


    public String getAboutImagePath()
    {
        return "/icons/pumps_about.jpg";
    }


    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 400;
        sz[1] = 200;

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
        List<CreditsGroup> lst_credits = new ArrayList<CreditsGroup>();

        CreditsGroup cg = new CreditsGroup(i18nControl.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com",
                "Framework, About, Outputs, All devices")); //
        cg.addCreditsEntry(new CreditsEntry("Tidepool.org", "www.tidepool.org", "Supplied code for Insulet device"));

        cg.addCreditsEntry(new CreditsEntry("decoding-carelink Project & Benjamin West",
                "https://github.com/bewest/decoding-carelink",
                "Ben has given me a lot of support, and also his project has a lot of testing data for history data decoding."));
        cg.addCreditsEntry(new CreditsEntry("Nightscout Project", "www.nightscout.info",
                "Nightscout's code for data download helped me with my implementation of pump reader."));

        lst_credits.add(cg);

        return lst_credits;
    }


    public List<FeaturesEntry> getImplementedFeatures()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Base Pump Tools Framework"));
        outList.add(new FeaturesEntry("Various output types"));
        outList.add(new FeaturesEntry("Communication Framework"));
        outList.add(new FeaturesEntry("Reading data"));
        outList.add(new FeaturesEntry("Configuration"));
        outList.add(new FeaturesEntry("List of pumps"));
        outList.add(new FeaturesEntry("Manual data entry (also additional data entry)"));
        outList.add(new FeaturesEntry("Profiles (partitial)"));
        outList.add(new FeaturesEntry("About dialog"));

        return outList;
    }


    public List<FeaturesEntry> getNotImplementedFeatures()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Profiles (graphical edit)"));
        outList.add(new FeaturesEntry("Graphs (we have only daily graph for now)"));

        return outList;
    }


    public List<FeaturesEntry> getSupportedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Dana (only works on Windows and Linux)"));
        outList.add(new FeaturesEntry("Accu-chek/Roche [D-Tron - Combo]"));
        outList.add(new FeaturesEntry("Animas/One Touch [IR1200 - Vibe]"));
        outList.add(new FeaturesEntry("Omnipod"));

        return outList;
    }


    public List<FeaturesEntry> getPlannedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Minimed (end of 2015, start of 2016)"));
        outList.add(new FeaturesEntry("Tandem (?? no contact from company yet)"));

        return outList;
    }


    public String getPluginVersion()
    {
        return this.PLUGIN_VERSION;
    }


    /**
     * Get Name of Plugin (for internal use)
     *
     * @return
     */
    public String getPluginName()
    {
        return this.PLUGIN_NAME;
    }


    public GGCPluginType getPluginType()
    {
        return GGCPluginType.PumpToolPlugin;
    }


    @Override
    public List<BaseListEntry> getWebListerItems()
    {
        List<BaseListEntry> weblister_items = new ArrayList<BaseListEntry>();

        weblister_items.add(new BaseListEntry("Animas", "/pumps/animas.html", BaseListEntry.STATUS_DONE));
        weblister_items.add(new BaseListEntry("Asante", "/pumps/asante.html", BaseListEntry.STATUS_NOTPLANNED));
        weblister_items.add(new BaseListEntry("Deltec", "/pumps/deltec.html", BaseListEntry.STATUS_NOTPLANNED));
        weblister_items.add(new BaseListEntry("Insulet", "/pumps/insulet.html", BaseListEntry.STATUS_DONE));
        weblister_items.add(new BaseListEntry("Minimed", "/pumps/minimed.html", BaseListEntry.STATUS_PLANNED));
        weblister_items.add(new BaseListEntry("Roche", "/pumps/roche.html", BaseListEntry.STATUS_DONE));
        weblister_items.add(new BaseListEntry("Sooil", "/pumps/sooil.html", BaseListEntry.STATUS_DONE));
        weblister_items.add(new BaseListEntry("Tandem", "/pumps/tandem.html", BaseListEntry.STATUS_PLANNED));

        return weblister_items;
    }


    @Override
    public String getWebListerDescription()
    {
        return this.i18nControl.getMessage("DEVICE_LIST_WEB_DESC");
    }


    @Override
    public PluginReportDefinition getReportsDefinition()
    {
        if (reportsPumpDefinition == null)
        {
            this.reportsPumpDefinition = new PumpReportDefinition((DataAccessPump) this.dataAccess);
        }

        return reportsPumpDefinition;
    }


    @Override
    public PluginGraphDefinition getGraphsDefinition()
    {
        if (graphsPumpDefinition == null)
        {
            this.graphsPumpDefinition = new PumpGraphDefintion((DataAccessPump) this.dataAccess);
        }

        return graphsPumpDefinition;
    }


    @Override
    public String getPluginActionsPrefix()
    {
        return "pumps_";
    }


    @Override
    public void registerDeviceHandlers()
    {
        DeviceHandlerManager deviceHandlerManager = DeviceHandlerManager.getInstance();

        // Animas
        deviceHandlerManager.addDeviceHandler(new AnimasIR1200Handler());

        // Insulet
        deviceHandlerManager.addDeviceHandler(new InsuletHandler());

        // Accu-Chek/Roche
        deviceHandlerManager.addDeviceHandler(new AccuChekPumpHandler());

        // Dana
        deviceHandlerManager.addDeviceHandler(new DanaPumpHandler());

        // Minimed
        deviceHandlerManager.addDeviceHandler(new MinimedPumpDeviceHandler((DataAccessPump) this.dataAccess));

    }
}
