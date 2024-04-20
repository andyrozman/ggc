package ggc.gui.dialogs;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import com.atech.app.data.about.*;
import com.atech.app.gui.about.AboutCustomPanel;
import com.atech.app.gui.about.AboutDialog;
import com.atech.plugin.PlugInClient;
import com.atech.utils.ATSwingUtils;

import ggc.core.plugins.GGCPluginType;
import ggc.core.util.DataAccess;
import lombok.extern.slf4j.Slf4j;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     AboutGGCDialog
 *  Description:  About GGC Dialog.
 * 
 *  Author: Andy {andy@atech-software.com}  
 */

@Slf4j
public class AboutGGCDialog extends AboutDialog
{

    private static final long serialVersionUID = -5655078691807335660L;

    private int currentYear;
    private String titleI18nKey = "GGC_TITLE";

    DataAccess dataAccess = DataAccess.getInstance();


    public AboutGGCDialog(JFrame parent)
    {
        this(parent, "GGC_TITLE");
    }


    public AboutGGCDialog(JFrame parent, String title)
    {
        super(parent, true, DataAccess.getInstance().getI18nControlInstance());

        currentYear = (new GregorianCalendar()).get(Calendar.YEAR);

        // licence
        this.setLicenceType(LicenceInfo.LICENCE_GPL_v2_0);

        // credits
        ArrayList<CreditsGroup> lst_credits = new ArrayList<CreditsGroup>();

        CreditsGroup cg = new CreditsGroup(m_ic.getMessage("CURRENT_DEVELOPERS"));
        cg.addCreditsEntry(
            new CreditsEntry("Aleksander Rozman (Andy)", "andyrozman@users.sourceforge.net", "Current main developer"));
        cg.addCreditsEntry(
            new CreditsEntry("Reinhold Rumberger", "rumbi@users.sourceforge.net", "Tester (linux) and developer"));
        lst_credits.add(cg);

        cg = new CreditsGroup(m_ic.getMessage("PREVIOUS_DEVELOPERS"));
        cg.addCreditsEntry(new CreditsEntry("Dieter Schultschik", "schultd@users.sourceforge.net",
                "Creator and Designer of this application"));
        cg.addCreditsEntry(
            new CreditsEntry("Stephan Schrader", "sschrade@users.sourceforge.net", "First meters supported..."));
        lst_credits.add(cg);

        this.setCredits(lst_credits);

        // set display system properties
        this.setDisplayProperties(true);

        // libraries
        List<LibraryInfoEntry> lst_libs = new ArrayList<>();

        lst_libs.add(new LibraryInfoEntry("Hibernate", //
                "3.1", //
                "www.hibernate.org", //
                "LGPL", //
                "Library for object-oriented access to DBs"));

        lst_libs.add(new LibraryInfoEntry("H2 Database", //
                "2.2.224", //
                "www.h2database.com", //
                "MPL 1.1 & EPL 1.0", //
                "Internal Java DB", //
                "Copyright (c) 2004-2024 by the H2 Group. All rights reserved."));

        // lst_libs.add(li);

        lst_libs.add(new LibraryInfoEntry("Atech Tools", //
                "0.8.8", //
                "https://github.com/andyrozman/atech-tools", //
                "LGPL (v2.1)", //
                "Java Helper Library for Swing/Hibernate/SQL...", //
                "Copyright (c) 2006-" + currentYear + " Atech Software Ltd. All rights reserved."));

        lst_libs.add(new LibraryInfoEntry("SkinLF", //
                "6.7", //
                "www.l2fprod.com", //
                "LGPL", //
                "Skins Library", //
                "Copyright (c) 2000-2006 L2FProd.com.  All rights reserved."));

        lst_libs.add(new LibraryInfoEntry("iTextPdf", //
                "5.5.13.3", //
                "http://itextpdf.com/", //
                "Affero GPL v3", //
                "Library for PDF creation (printing)"));

        lst_libs.add(new LibraryInfoEntry("Java Help", //
                "2.0_05", //
                "https://javahelp.java.net/", //
                "GPL (v2)", //
                "Java Help Framework"));

        lst_libs.add(new LibraryInfoEntry("Apache Commons Lang3", //
                "2.6", //
                "commons.apache.org/lang/", //
                "Apache", //
                "Helper methods for java.lang library"));

        lst_libs.add(new LibraryInfoEntry("Apache Commons Collections", //
                "3.2.2", //
                "https://commons.apache.org/proper/commons-collections/", //
                "Apache", //
                "Helper methods for Collections"));

        lst_libs.add(new LibraryInfoEntry("Apache Commons IO", //
                "2.16.0", //
                "https://commons.apache.org/proper/commons-io/", //
                "Apache", //
                "Helper methods for IO operations"));

        lst_libs.add(new LibraryInfoEntry("Simple Logging Facade for Java (slf4j)", //
                "1.7.36", //
                "http://www.slf4j.org/", //
                "MIT", //
                "Logging facade (works together with log4j)"));

        lst_libs.add(new LibraryInfoEntry("Log 4 Java (log4j_reloaded)", //
                "1.2.25", //
                "http://logging.apache.org/log4j/2.x/log4j-1.2-api/index.html", //
                "Apache", //
                "Logger and all around wrapper for logging utilities"));

        lst_libs.add(new LibraryInfoEntry("ICE Pdf Viewer", //
                "5.0.7", //
                "http://www.icesoft.org/java/projects/ICEpdf/overview.jsf", //
                "", //
                "Internal PDF Viewer"));

        lst_libs.add(new LibraryInfoEntry("JFreeChart", //
                "1.0.13", //
                "http://https://www.jfree.org/jfreechart/", //
                "LGPL (v3)", //
                "Library for Graphs"));

        lst_libs.add(new LibraryInfoEntry("Pygmy Http Server", //
                "0.4.3", //
                "https://github.com/andyrozman/pygmy-httpd/", //
                "Artistic", //
                "Small Web Server"));

        lst_libs.add(new LibraryInfoEntry("Gson", //
                "2.9.1", //
                "https://github.com/google/gson", //
                "Apache 2.0", //
                "Serialization/Deserialization library for Json"));

        lst_libs.add(new LibraryInfoEntry("Table Layout", //
                "4.3.0", //
                "https://www.clearthought.info/sun/products/jfc/tsc/articles/tablelayout/", //
                "", //
                "Table Layout for Java"));

        List<LibraryInfoEntry> allLibraries = new ArrayList<>();

        addUniqueLibraries(allLibraries, lst_libs);

        GGCPluginType[] keys = {
                GGCPluginType.MeterToolPlugin,
                GGCPluginType.NutritionToolPlugin,
                GGCPluginType.PumpToolPlugin,
                GGCPluginType.CGMSToolPlugin,
                GGCPluginType.ConnectToolPlugin
        };

        List<ModuleInfoEntry> modules = new ArrayList<>();
        modules.add(dataAccess.getCoreModule());
        modules.add(getDesktopModule());

        boolean isBasePluginDone = false;

        for (GGCPluginType pluginType : keys) {
            PlugInClient pic = dataAccess.getPlugIn(pluginType);

            List<LibraryInfoEntry> typedList = pic.getDataFromPlugin(Collections.singletonMap("pluginLibraries", ""))
                    .stream()
                    .map(a -> (LibraryInfoEntry) a)
                    .collect(Collectors.toList());

            addUniqueLibraries(allLibraries, typedList);

            if (!isBasePluginDone) {
                ModuleInfoEntry moduleInfo = (ModuleInfoEntry)pic.getDataFromPlugin(Collections.singletonMap("pluginBaseModule", "")).get(0);
                isBasePluginDone = true;
                modules.add(moduleInfo);
            }

            ModuleInfoEntry moduleInfo = (ModuleInfoEntry)pic.getDataFromPlugin(Collections.singletonMap("pluginModule", "")).get(0);
            modules.add(moduleInfo);
        }

        modules.add(getHelpModule());

        this.setModules(modules);
        this.setLibraries(allLibraries);

        // Main About Tab
        createCustomTab();

        // title
        this.setTitle(m_ic.getMessage("ABOUT_GGC"));

        // finalize
        this.createAbout();
        this.setSize(500, 400);

        this.showAbout();
    }

    Set<String> libraryIndex = new HashSet<>();

    private void addUniqueLibraries(List<LibraryInfoEntry> targetList, List<LibraryInfoEntry> sourceList) {
        for (LibraryInfoEntry libraryInfoEntry : sourceList) {
            String name = libraryInfoEntry.getName();
            if (!libraryIndex.contains(name)) {
                libraryIndex.add(name);
                targetList.add(libraryInfoEntry);
            }
        }
    }


    private void createCustomTab()
    {
        AboutCustomPanel acp = new AboutCustomPanel(m_ic);
        acp.setTabName(m_ic.getMessage("ABOUT"));
        acp.setLayout(new BoxLayout(acp, BoxLayout.PAGE_AXIS));

        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());

        JLabel l = new JLabel(new ImageIcon(ATSwingUtils.getImage("/icons/about_logo.gif", this).getScaledInstance(500,
            125, java.awt.Image.SCALE_SMOOTH)));
        p1.add(l, BorderLayout.CENTER);

        JLabel l2 = new JLabel();
        l2.setPreferredSize(new Dimension(100, 10));

        p1.add(l2, BorderLayout.SOUTH);
        acp.add(p1);

        JEditorPane jEditorPaneAbout = new javax.swing.JEditorPane();
        jEditorPaneAbout.setBackground(new java.awt.Color(204, 204, 204));
        jEditorPaneAbout.setEditable(false);
        jEditorPaneAbout.setMinimumSize(new java.awt.Dimension(104, 90));
        jEditorPaneAbout.setOpaque(false);
        jEditorPaneAbout.setPreferredSize(new java.awt.Dimension(104, 90));

        jEditorPaneAbout.setContentType("text/html");
        jEditorPaneAbout.setText("<HTML><body><font face=\"SansSerif\" size=\"3\"><center><b>"
                + m_ic.getMessage(titleI18nKey) + "</b><br>&nbsp;&nbsp;(c) 2002-" + currentYear + " "
                + m_ic.getMessage("GGC_DEVELOPMENT_TEAM") + "<br>" + m_ic.getMessage("SEE_CREDITS")
                + "<br><A HREF=\"http://ggc.sourceforge.net/\">http://ggc.sourceforge.net/</A><br>"
                + m_ic.getMessage("LICENCE") + " GPL v2.0<br></font></body></html>");

        acp.add(jEditorPaneAbout); // , BoxLayout.PAGE_AXIS);

        this.addCustomPanel(AboutDialog.PLACEMENT_BEFORE_STATIC_TABS, acp);

    }


    private void createModulesTab() {

        AboutCustomPanel acp = new AboutCustomPanel(m_ic);
        acp.setTabName(m_ic.getMessage("MODULES"));
        acp.setLayout(new BoxLayout(acp, BoxLayout.PAGE_AXIS));

        this.addCustomPanel(AboutDialog.PLACEMENT_BEFORE_STATIC_TABS, acp);

    }

    public ModuleInfoEntry getDesktopModule() {
        return ModuleInfoEntry.builder()
                .name(m_ic.getMessage("DESKTOP_MODULE_NAME"))
                .version(DataAccess.CORE_VERSION)
                .description(m_ic.getMessage("DESKTOP_MODULE_DESCRIPTION"))
                .build();
    }


    public ModuleInfoEntry getHelpModule() {
        return ModuleInfoEntry.builder()
                .name(m_ic.getMessage("HELP_MODULE_NAME"))
                .version(DataAccess.HELP_VERSION)
                .description(m_ic.getMessage("HELP_MODULE_DESCRIPTION"))
                .build();
    }

}
