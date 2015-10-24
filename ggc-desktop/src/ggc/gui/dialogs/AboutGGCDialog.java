package ggc.gui.dialogs;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.atech.app.data.about.CreditsEntry;
import com.atech.app.data.about.CreditsGroup;
import com.atech.app.data.about.LibraryInfoEntry;
import com.atech.app.data.about.LicenceInfo;
import com.atech.app.gui.about.AboutCustomPanel;
import com.atech.app.gui.about.AboutDialog;
import com.atech.utils.ATSwingUtils;
import ggc.core.util.DataAccess;

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

public class AboutGGCDialog extends AboutDialog
{

    private static final long serialVersionUID = -5655078691807335660L;


    public AboutGGCDialog(JFrame parent)
    {
        super(parent, true, DataAccess.getInstance().getI18nControlInstance());

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
        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();

        lst_libs.add(new LibraryInfoEntry("Hibernate", //
                "3.1", //
                "www.hibernate.org", //
                "LGPL", //
                "Library for object-oriented access to DBs"));

        lst_libs.add(new LibraryInfoEntry("H2 Database", //
                "1.0.69", //
                "www.h2database.com", //
                "MPL 1.1 & EPL 1.0", //
                "Internal Java DB", //
                "Copyright (c) 2004-2008 by the H2 Group. All rights reserved."));

        // lst_libs.add(li);

        lst_libs.add(new LibraryInfoEntry("Atech-Tools", //
                "0.7.x", //
                "www.atech-software.com", //
                "LGPL", //
                "Helper Library for Swing/Hibernate/...", //
                "Copyright (c) 2006-2015 Atech Software Ltd. All rights reserved."));

        lst_libs.add(new LibraryInfoEntry("SkinLF", //
                "6.7", //
                "www.l2fprod.com", //
                "LGPL", //
                "Skins Library", //
                "Copyright (c) 2000-2006 L2FProd.com.  All rights reserved."));

        lst_libs.add(new LibraryInfoEntry("iText", //
                "2.0.7", //
                "www.lowagie.com/iText/", //
                "MPL", //
                "Library for PDF creation (printing)"));

        lst_libs.add(new LibraryInfoEntry("Java Help", //
                "2.0_05", //
                "https://javahelp.java.net/", //
                "GPL (v2)", //
                "Java Help Framework"));

        lst_libs.add(new LibraryInfoEntry("Apache Commons Lang", //
                "2.6", //
                "commons.apache.org/lang/", //
                "Apache", //
                "Helper methods for java.lang library"));

        lst_libs.add(new LibraryInfoEntry("Apache Commons Collections", //
                "2.6", //
                "https://commons.apache.org/proper/commons-collections/", //
                "Apache", //
                "Helper methods for Collections"));

        lst_libs.add(new LibraryInfoEntry("Simple Logging Facade for Java (slf4j)", //
                "1.7.12", //
                "http://www.slf4j.org/", //
                "MIT", //
                "Logging facade (works together with log4j)"));

        lst_libs.add(new LibraryInfoEntry("Log 4 Java (log4j)", //
                "1.2.16", //
                "http://logging.apache.org/log4j/2.x/log4j-1.2-api/index.html", //
                "Apache", //
                "Logger and all around wrapper for logging utilities"));

        lst_libs.add(new LibraryInfoEntry("ICE Pdf Viewer", //
                "5.0.7", //
                "http://www.icesoft.org/java/projects/ICEpdf/overview.jsf", //
                "", //
                "Internal PDF Viewer"));

        this.setLibraries(lst_libs);

        // custom page
        createCustomTab();

        // title
        this.setTitle(m_ic.getMessage("ABOUT_GGC"));

        // finalize
        this.createAbout();
        this.setSize(500, 400);

        this.showAbout();
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
                + m_ic.getMessage("GGC_TITLE") + "</b><br>&nbsp;&nbsp;(c) 2002-2015  "
                + m_ic.getMessage("GGC_DEVELOPMENT_TEAM") + "<br>" + m_ic.getMessage("SEE_CREDITS")
                + "<br><A HREF=\"http://ggc.sourceforge.net/\">http://ggc.sourceforge.net/</A><br>"
                + m_ic.getMessage("LICENCE") + " GPL v2.0<br></font></body></html>");

        acp.add(jEditorPaneAbout); // , BoxLayout.PAGE_AXIS);

        this.addCustomPanel(AboutDialog.PLACEMENT_BEFORE_STATIC_TABS, acp);

    }

}
