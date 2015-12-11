package ggc.cgms.defs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atech.app.data.about.CreditsEntry;
import com.atech.app.data.about.CreditsGroup;
import com.atech.app.data.about.FeaturesEntry;
import com.atech.app.data.about.LibraryInfoEntry;
import com.atech.i18n.mgr.LanguageManager;

import ggc.cgms.device.animas.AnimasCGMSHandler;
import ggc.cgms.device.dexcom.DexcomHandler;
import ggc.cgms.report.def.CGMSReportDefinition;
import ggc.cgms.util.DataAccessCGMS;
import ggc.cgms.util.GGC_CGMS_ICRunner;
import ggc.core.plugins.GGCPluginType;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.report.PluginReportDefinition;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMSPluginDefinition
 *  Description: CGMS PlugIn Definition
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSPluginDefinition extends DevicePluginDefinitionAbstract
{

    String PLUGIN_VERSION = "1.4.0";
    String PLUGIN_NAME = "GGC CGMS Plugin";

    CGMSReportDefinition reportsCGMSDefinition;


    public CGMSPluginDefinition(LanguageManager languageManager)
    {
        super(languageManager, new GGC_CGMS_ICRunner());
    }


    public int getCopyrightFrom()
    {
        return 2009;
    }


    public String getAboutImagePath()
    {
        return "/icons/cgms_about.jpg";
    }


    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 400;
        sz[1] = 203;

        return sz;
    }


    @Override
    public List<LibraryInfoEntry> getPluginLibraries()
    {
        return null;
    }


    public List<CreditsGroup> getCredits()
    {
        CreditsGroup cg = new CreditsGroup(this.i18nControl.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com",
                "Framework, About, Outputs, Communication, Devices,..."));

        return Arrays.asList(cg);
    }


    public List<FeaturesEntry> getImplementedFeatures()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Base CGMS Tools Framework"));
        outList.add(new FeaturesEntry("Various output types"));
        outList.add(new FeaturesEntry("Communication Framework"));
        outList.add(new FeaturesEntry("Reading data"));
        outList.add(new FeaturesEntry("Configuration"));
        outList.add(new FeaturesEntry("List of CGMSes"));
        outList.add(new FeaturesEntry("About dialog"));
        outList.add(new FeaturesEntry("Data Viewer with Daily Graph"));

        return outList;
    }


    public List<FeaturesEntry> getNotImplementedFeatures()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Reports"));
        outList.add(new FeaturesEntry("Graphs (we have only daily graph for now)"));

        return outList;
    }


    public List<FeaturesEntry> getSupportedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Dexcom (file imports from DM3 App)"));
        outList.add(new FeaturesEntry("Dexcom G4"));
        outList.add(new FeaturesEntry("Animas Vibe (Dexcom Integration)"));

        return outList;
    }


    public List<FeaturesEntry> getPlannedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Minimed (2016)"));
        outList.add(new FeaturesEntry("Freestyle Navigator (?? no contact from company)"));

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
        return GGCPluginType.CGMSToolPlugin;
    }


    @Override
    public List<BaseListEntry> getWebListerItems()
    {
        List<BaseListEntry> weblister_items = new ArrayList<BaseListEntry>();

        weblister_items
                .add(new BaseListEntry("Abbott Diabetes Care", "/cgms/abbott.html", BaseListEntry.STATUS_NOTPLANNED));
        weblister_items.add(new BaseListEntry("Animas", "/cgms/animas.html", BaseListEntry.STATUS_DONE));
        weblister_items.add(new BaseListEntry("Dexcom", "/cgms/dexcom.html", BaseListEntry.STATUS_PART_IMPLEMENTED));
        weblister_items.add(new BaseListEntry("Minimed", "/cgms/minimed.html", BaseListEntry.STATUS_PLANNED));

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
        if (reportsCGMSDefinition == null)
        {
            this.reportsCGMSDefinition = new CGMSReportDefinition((DataAccessCGMS) this.dataAccess);
        }

        return reportsCGMSDefinition;
    }


    @Override
    public void registerDeviceHandlers()
    {
        // Animas CGMS - Dexcom (Vibe)
        DeviceHandlerManager.getInstance().addDeviceHandler(new AnimasCGMSHandler());

        // Dexcom: G4
        DeviceHandlerManager.getInstance().addDeviceHandler(new DexcomHandler());
    }

}
