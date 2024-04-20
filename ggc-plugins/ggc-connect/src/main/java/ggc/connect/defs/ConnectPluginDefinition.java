package ggc.connect.defs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atech.app.data.about.CreditsEntry;
import com.atech.app.data.about.CreditsGroup;
import com.atech.app.data.about.FeaturesEntry;
import com.atech.app.data.about.LibraryInfoEntry;
import com.atech.i18n.mgr.LanguageManager;

import ggc.connect.util.GGCConnectICRunner;
import ggc.core.plugins.GGCPluginType;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.graph.PluginGraphDefinition;
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

public class ConnectPluginDefinition extends DevicePluginDefinitionAbstract
{

    // String PLUGIN_VERSION = "0.1.0";
    static String PLUGIN_NAME = "GGC Connect Plugin";


    public ConnectPluginDefinition(LanguageManager languageManager)
    {
        super(languageManager, //
                new GGCConnectICRunner(), //
                PLUGIN_NAME, //
                GGCPluginType.ConnectToolPlugin, //
                "connect_", //
                "ggc.connect.defs.Version");
        this.useBaseLibraries = false;
    }


    public int getCopyrightFrom()
    {
        return 2017;
    }


    public String getAboutImagePath()
    {
        return "/icons/connect_about.jpg";
    }


    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 290;
        sz[1] = 190;

        return sz;
    }


    @Override
    public List<LibraryInfoEntry> getPluginLibraries()
    {
        List<LibraryInfoEntry> outList = new ArrayList<LibraryInfoEntry>();

        outList.add(new LibraryInfoEntry("Apache POI", "3.16", //
                "https://poi.apache.org/", "Apache Licence", //
                "Java API for Microsoft Documents", //
                "Copyright © 2002-2017 The Apache Software Foundation. All rights reserved."));

        return outList;
    }


    public List<CreditsGroup> getCredits()
    {
        CreditsGroup cg = new CreditsGroup(this.i18nControl.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com", "Framework, About."));

        return Arrays.asList(cg);
    }


    public List<FeaturesEntry> getImplementedFeatures()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Base Connect Framework"));
        outList.add(new FeaturesEntry("About dialog"));

        return outList;
    }


    public List<FeaturesEntry> getNotImplementedFeatures()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Configuration"));

        return outList;
    }


    public List<FeaturesEntry> getSupportedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("Diasend (Work in Progress)"));

        return outList;
    }


    public List<FeaturesEntry> getPlannedDevices()
    {
        List<FeaturesEntry> outList = new ArrayList<FeaturesEntry>();

        outList.add(new FeaturesEntry("DiaSend (?)"));
        outList.add(new FeaturesEntry("SiDiary (?)"));
        outList.add(new FeaturesEntry("MS Health (?)"));
        outList.add(new FeaturesEntry("Libra (?)"));
        outList.add(new FeaturesEntry("Open Platform for Clinical Nutrition (2018-19)"));
        outList.add(new FeaturesEntry("Tidepool (2025)"));
        outList.add(new FeaturesEntry("NightScout (2024)"));

        return outList;
    }


    // /**
    // * Get Name of Plugin (for internal use)
    // *
    // * @return
    // */
    // public String getPluginName()
    // {
    // return this.PLUGIN_NAME;
    // }
    //
    //
    // public GGCPluginType getPluginType()
    // {
    // return GGCPluginType.ConnectToolPlugin;
    // }

    @Override
    public List<BaseListEntry> getWebListerItems()
    {
        return null;
    }


    // @Override
    // public String getWebListerDescription()
    // {
    // return this.i18nControl.getMessage("DEVICE_LIST_WEB_DESC");
    // }

    @Override
    public PluginReportDefinition getReportsDefinition()
    {
        // if (reportsCGMSDefinition == null)
        // {
        // this.reportsCGMSDefinition = new
        // CGMSReportDefinition((DataAccessCGMS) this.dataAccess);
        // }
        //
        // return reportsCGMSDefinition;

        return null;
    }


    @Override
    public PluginGraphDefinition getGraphsDefinition()
    {
        // if (graphsCGMSDefintion == null)
        // {
        // this.graphsCGMSDefintion = new CGMSGraphDefintion((DataAccessCGMS)
        // this.dataAccess);
        // }
        //
        // return graphsCGMSDefintion;
        return null;
    }

    // @Override
    // public String getPluginActionsPrefix()
    // {
    // return "connect_";
    // }

    // @Override
    // public void registerDeviceHandlers()
    // {
    // }

}
