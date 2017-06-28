package ggc.gui.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.*;
import java.util.List;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.graphics.components.DialogSizePersistInterface;
import com.atech.graphics.dialogs.guilist.GUIListDialog;
import com.atech.graphics.graphs.GraphViewer;
import com.atech.help.HelpContext;
import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.refresh.EventObserverInterface;
import com.atech.plugin.PlugInClient;
import com.atech.upgrade.client.gui.UpgradeDialog;
import com.atech.utils.ATSwingUtils;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

import ggc.GGC;
import ggc.core.data.graph.v1.db.GraphV1DbRetrieverCore;
import ggc.core.data.graph.v1.gui.HbA1cDialog;
import ggc.core.data.graph.v1.view.GraphViewAbstract;
import ggc.core.data.graph.v1.view.GraphViewCourse;
import ggc.core.data.graph.v1.view.GraphViewFrequency;
import ggc.core.data.graph.v1.view.GraphViewSpread;
import ggc.core.db.GGCDbLoader;
import ggc.core.db.tool.transfer.BackupDialog;
import ggc.core.db.tool.transfer.RestoreGGCSelectorDialog;
import ggc.core.plugins.GGCPluginType;
import ggc.core.util.DataAccess;
import ggc.core.util.RefreshInfo;
import ggc.core.util.upgrade.GGCUpgradeApplicationContext;
import ggc.gui.cfg.PropertiesDialog;
import ggc.gui.dialogs.AboutGGCDialog;
import ggc.gui.dialogs.PrintingDialog;
import ggc.gui.dialogs.doctor.DoctorListDef;
import ggc.gui.dialogs.doctor.appointment.AppointmentListDef;
import ggc.gui.dialogs.stock.def.StockListDef;
import ggc.gui.main.panels.MainWindowInfoPanel;
import ggc.gui.pen.DailyStatsDialog;
import ggc.shared.ratio.RatioBaseDialog;
import ggc.shared.ratio.RatioExtendedDialog;

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
 *  Filename:     GGC  
 *  Description:  Main GUI file, here toolbar, menus are created.
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */

public class MainFrame extends JFrame implements EventObserverInterface, ActionListener, DialogSizePersistInterface
{

    private static final long serialVersionUID = -8971779470148201332L;
    private static final String skinLFdir = "../data/skinlf_themes/";
    /**
     * Skin Look and Feel
     */
    public static SkinLookAndFeel s_skinlf;

    /**
     * Developer version 
     */
    public static boolean developer_version = false;
    private static Logger LOG = LoggerFactory.getLogger(MainFrame.class);

    private GraphV1DbRetrieverCore graphV1DbRetrieverCore;

    /**
     * Status panels
     */
    public StatusBar statusPanel;
    /**
     * Information panels
     */
    public MainWindowInfoPanel informationPanel;
    boolean title_set = false;
    /**
     * Menu Bar
     */
    private JMenuBar menuBar = new JMenuBar();
    /**
     * Tool Bars
     */
    private Hashtable<GGCToolbarType, JToolBar> toolbars = null;
    private DataAccess dataAccess = null;
    private I18nControlAbstract i18Control = null;
    private Map<String, JMenu> menus = null;
    private Map<String, JMenuItem> actions = null;
    private Map<GGCToolbarType, Map<String, JButton>> toolbarItems = new HashMap<GGCToolbarType, Map<String, JButton>>();

    private GGCToolbarType current_toolbar = GGCToolbarType.None;
    private String nextVersion = "0.8";


    /**
     * Static definitions (Look and Feel)
     */
    static
    {
        MainFrame.setLookAndFeel();
    }


    /**
     * Constructor
     * 
     * @param title
     * @param developer_version
     */
    public MainFrame(String title, boolean developer_version)
    {
        dataAccess = DataAccess.createInstance(this);

        i18Control = dataAccess.getI18nControlInstance();

        dataAccess.addComponent(this);

        dataAccess.setDeveloperMode(developer_version);

        statusPanel = new StatusBar(this);

        this.actions = new Hashtable<String, JMenuItem>();
        graphV1DbRetrieverCore = new GraphV1DbRetrieverCore();
        MainFrame.developer_version = developer_version;

        String title_full = "  GGC - GNU Gluco Control (" + GGC.full_version + ")";

        if (developer_version)
        {
            title_full += " - Developer edition";
        }

        setTitle(title_full);

        this.setIconImage(ATSwingUtils.getImageIcon_22x22("diabetesbluecircle.png", this, dataAccess).getImage());

        setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });

        helpInit();

        // menus, toolbars
        createMenus();
        createToolBars();
        this.setSoftwareMode();
        dataAccess.addObserver(DataAccess.OBSERVABLE_STATUS, this);
        dataAccess.addObserver(DataAccess.OBSERVABLE_DB, this);

        // getContentPane().add(this.toolbars.get("TOOLBAR_PEN"),
        // BorderLayout.NORTH);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        dataAccess.startDb();

        statusPanel.setStatusMessage(i18Control.getMessage("INIT"));

        // information panel
        informationPanel = new MainWindowInfoPanel();
        getContentPane().add(informationPanel, BorderLayout.CENTER);

        setMenusByDbLoad(StatusBar.DB_STOPPED);

        dataAccess.startToObserve();
        this.setVisible(true);

    }


    /**
     * Set Look & Feel
     */
    public static void setLookAndFeel()
    {

        try
        {

            String data[] = DataAccess.getLFData();

            if (data == null)
                return;
            else
            {
                if (data[0].equals("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"))
                {
                    SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(skinLFdir + data[1]));

                    s_skinlf = new com.l2fprod.gui.plaf.skin.SkinLookAndFeel();
                    UIManager.setLookAndFeel(s_skinlf);

                    setExceptionsForLookAndFeel(data[1], s_skinlf);
                }
                else
                {
                    UIManager.setLookAndFeel(data[0]);
                }

                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
            }
        }
        catch (Exception ex)
        {
            System.err.println("Error loading L&F: " + ex);
        }

    }

    private static Map<String, Object> skinLfOverrides;


    public static void setExceptionsForLookAndFeel(String skinName, SkinLookAndFeel skinLookAndFeel)
    {
        System.out.println("Skin Name: " + skinName);

        if (skinName.equals("modernthemepack_orig.zip"))
        {
            skinLfOverrides = new HashMap<String, Object>();

            skinLfOverrides.put("JTableHeader.backgroundColor", new Color(102, 178, 255));
            skinLfOverrides.put("JTableHeader.borderColor", Color.gray);

            DataAccess.setSkinLfOverrides(skinLfOverrides);
        }

        // for (Object keyo : skinLookAndFeel.getDefaults().keySet())
        // {
        // String key = (String) keyo;
        //
        // // if (key.contains("Table"))
        // {
        // System.out.println("Key2: " + key);
        // }
        // }

    }


    private void setSoftwareMode()
    {
        setSoftwareMode(false);
    }


    /**
     * Get Software Mode
     * 
     * @return
     */
    public String getSoftwareMode()
    {
        return " [" + i18Control.getMessage(dataAccess.getSoftwareModeDescription()) + "]";
    }


    // ------------------------------------------------------
    // -- Menus
    // ------------------------------------------------------

    private void setSoftwareMode(boolean force)
    {
        // System.out.println("SW: " + dataAccess.getSoftwareMode());

        // if (dataAccess.getSoftwareMode() == -1)
        // return;

        String title_full = "  GGC - GNU Gluco Control (" + GGC.full_version + ")";

        if (developer_version)
        {
            title_full += " - Developer edition";
        }

        title_full += getSoftwareMode();
        setTitle(title_full);

        boolean changed = false;

        if (dataAccess.isPumpMode())
        {
            if (this.current_toolbar != GGCToolbarType.Pump)
            {
                changed = true;
                this.current_toolbar = GGCToolbarType.Pump;
            }
        }
        else
        {
            if (this.current_toolbar != GGCToolbarType.PenInjection)
            {
                changed = true;
                this.current_toolbar = GGCToolbarType.PenInjection;
            }
        }

        if ((changed) || (force))
        {
            getContentPane().remove(this.toolbars.get(GGCToolbarType.PenInjection));
            getContentPane().remove(this.toolbars.get(GGCToolbarType.Pump));
            getContentPane().add(this.toolbars.get(this.current_toolbar), BorderLayout.NORTH);
        }

    }


    private void createMenus()
    {
        JMenu menux, menuxsub;
        this.menus = new Hashtable<String, JMenu>();

        // file menu
        menux = this.createMenu("MN_FILE", null);
        this.createMenuItem(menux, "MN_LOGIN", "MN_LOGIN_DESC", "file_login", "logon.png");
        this.createMenuItem(menux, "MN_LOGOUT", "MN_LOGOUT_DESC", "file_logout", "logout.png");
        menux.addSeparator();
        this.createMenuItem(menux, "MN_QUIT", "MN_QUIT_DESC", "file_quit", null);

        this.menus.put("MENU_FILE", menux);

        // pen/injection menu
        menux = this.createMenu("MN_PEN_INJECTION", null);
        this.createMenuItem(menux, "MN_DAILY", "MN_DAILY_DESC", "pen_view_daily", "calendar.png");
        menux.addSeparator();

        // menuxsub = this.createMenu(menux, "MN_DATA_GRAPH", null);
        // this.createMenuItem(menuxsub, "MN_COURSE", "MN_COURSE_DESC",
        // "view_course", "line-chart.png");
        // this.createMenuItem(menuxsub, "MN_SPREAD", "MN_SPREAD_DESC",
        // "view_spread", "dot-chart.png");
        // this.createMenuItem(menuxsub, "MN_FREQUENCY", "MN_FREQUENCY_DESC",
        // "view_freq", "column-chart.png");
        //
        // menux.addSeparator();
        this.createMenuItem(menux, "MN_HBA1C", "MN_HBA1C_DESC", "pen_view_hba1c", "pie-chart.png");

        // addRatioMenu(menux, true);

        this.menus.put("MENU_PEN", menux);

        // reports menu
        menux = this.createMenu("MN_REPORTS_GRAPHS", null);

        menux.add(createTextMenuItem("MN_REPORTS"));

        menuxsub = this.createMenu(menux, "MN_PEN_INJECTION", "MN_REPORTS_DESC");
        this.createMenuItem(menuxsub, "MN_PDF_SIMPLE", "MN_PDF_SIMPLE_DESC", "report_pdf_simple", "print.png");
        this.createMenuItem(menuxsub, "MN_PDF_EXT", "MN_PDF_EXT_DESC", "report_pdf_extended", "print.png");

        this.menus.put("MENU_PRINT", menux);

        // misc menu
        menux = this.createMenu("MN_MISC", null);

        // misc - ratios
        menux.add(createTextMenuItem("MN_DATA_RATIO"));
        addRatioMenu(menux, false);

        menux.addSeparator();

        // misc - doctors
        menux.add(createTextMenuItem("MN_DOCTOR"));
        this.createMenuItem(menux, "MN_DOCS", "MN_DOCS_DESC", "doc_docs", null);
        this.createMenuItem(menux, "MN_APPOINT", "MN_APPOINT_DESC", "doc_appoint", null);

        menux.addSeparator();

        menux.add(createTextMenuItem("MN_STOCKS"));
        this.createMenuItem(menux, "MN_STOCKS", "MN_STOCKS_DESC", "doc_stocks", null);

        this.menus.put("MENU_MISC", menux);

        // tools menu
        menux = this.createMenu("MN_TOOLS", null);
        this.createMenuItem(menux, "MN_PREFERENCES", "MN_PREFERENCES_DESC", "tools_pref", "preferences.png");
        menux.addSeparator();

        menuxsub = this.createMenu(menux, "MN_DB_MAINT", "MN_DB_MAINT_DESC");
        this.createMenuItem(menuxsub, "MN_DB_BACKUP", "MN_DB_BACKUP_DESC", "tools_db_backup", "export1.png");
        this.createMenuItem(menuxsub, "MN_DB_RESTORE", "MN_DB_RESTORE_DESC", "tools_db_restore", "import1.png");

        menux.addSeparator();
        this.createMenuItem(menux, "MN_MISC_SYNCHRONIZE", "MN_MISC_SYNCHRONIZE_DESC", "misc_synchronize", null);

        this.menus.put("MENU_TOOLS", menux);

        // help menu
        menux = this.createMenu("MN_HELP", null);
        menux.add(dataAccess.getHelpContext().getHelpItem());
        menux.addSeparator();
        this.createMenuItem(menux, "MN_CHECK_FOR_UPDATE", "MN_CHECK_FOR_UPDATE_DESC", "hlp_check_update", null);
        menux.addSeparator();
        this.createMenuItem(menux, "MN_ABOUT", "MN_ABOUT_DESC", "hlp_about", null);

        if (MainFrame.developer_version)
        {
            menux.addSeparator();
            this.createMenuItem(menux, "MN_TEST", "MN_TEST_DESC", "test", null);
        }

        this.menus.put("MENU_HELP", menux);

    }


    private void refreshMenus()
    {
        refreshMenus(true);
    }


    private void refreshMenus(boolean refresh_init)
    {
        // System.out.println("Refresh Menus: " + refresh_init);
        this.menuBar.removeAll();

        // file
        this.menuBar.add(this.menus.get("MENU_FILE"));

        // pen-injection
        this.menuBar.add(this.menus.get("MENU_PEN"));

        // plugins
        GGCPluginType[] keys = { GGCPluginType.NutritionToolPlugin, //
                                 GGCPluginType.MeterToolPlugin, //
                                 GGCPluginType.PumpToolPlugin, //
                                 GGCPluginType.CGMSToolPlugin, //
                                 GGCPluginType.ConnectToolPlugin };

        for (GGCPluginType key : keys)
        {
            if (dataAccess.isPluginAvailable(key))
            {
                PlugInClient pic = dataAccess.getPlugIn(key);

                if (pic.getPlugInMainMenu() != null)
                {
                    JMenu menuTmp = pic.getPlugInMainMenu();

                    if (key == GGCPluginType.PumpToolPlugin)
                    {
                        // addRatioMenu(menuTmp, true);
                    }

                    this.menuBar.add(menuTmp);
                }
            }
        }

        if (refresh_init)
        {
            prepareReportsGraphsMenu();
        }

        this.menuBar.add(this.menus.get("MENU_PRINT"));

        // misc menu
        this.menuBar.add(this.menus.get("MENU_MISC"));

        // tools
        this.menuBar.add(this.menus.get("MENU_TOOLS"));

        // help
        this.menuBar.add(this.menus.get("MENU_HELP"));

        this.setJMenuBar(this.menuBar);
    }


    private JMenuItem createTextMenuItem(String text)
    {
        JMenuItem mi = new JMenuItem(i18Control.getMessageWithoutMnemonic(text));
        mi.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD));

        return mi;
    }


    private void prepareReportsGraphsMenu()
    {
        JMenu parentMenu = this.menus.get("MENU_PRINT");

        GGCPluginType[] keys = { GGCPluginType.NutritionToolPlugin, //
                                 GGCPluginType.MeterToolPlugin, //
                                 GGCPluginType.PumpToolPlugin, //
                                 GGCPluginType.CGMSToolPlugin, };

        // this.menus.get("MENU_PRINT").add(mi);

        // reports menu
        // for (Enumeration<String> en = dataAccess.getPlugins().keys();
        // en.hasMoreElements();)
        for (GGCPluginType pluginType : keys)
        {
            // String key = en.nextElement();

            PlugInClient pic = dataAccess.getPlugIn(pluginType);

            if (pic.getPlugInReportMenus() != null)
            {
                JMenu[] menus = pic.getPlugInReportMenus();

                for (JMenu menu2 : menus)
                {
                    // this.menus.get("MENU_PRINT").add(menu2);
                    parentMenu.add(menu2);
                }
            }
        }

        parentMenu.addSeparator();

        parentMenu.add(createTextMenuItem("MN_GRAPHS"));

        JMenu menuxsub = this.createMenu(parentMenu, "MN_PEN_INJECTION", null);
        this.createMenuItem(menuxsub, "MN_COURSE", "MN_COURSE_DESC", "pen_graph_course", "line-chart.png");
        this.createMenuItem(menuxsub, "MN_SPREAD", "MN_SPREAD_DESC", "pen_graph_spread", "dot-chart.png");
        this.createMenuItem(menuxsub, "MN_FREQUENCY", "MN_FREQUENCY_DESC", "pen_graph_freq", "column-chart.png");

        // mi = new JMenuItem("Graphs");
        // mi.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD));
        // this.menus.get("MENU_PRINT").add(mi);

        // reports menu
        // for (Enumeration<String> en = dataAccess.getPlugins().keys();
        // en.hasMoreElements();)
        for (GGCPluginType pluginType : keys)
        {
            // String key = en.nextElement();

            PlugInClient pic = dataAccess.getPlugIn(pluginType);

            if (pic.getPlugInGraphMenus() != null)
            {
                JMenu[] menus = pic.getPlugInGraphMenus();

                for (JMenu menu2 : menus)
                {
                    // this.menus.get("MENU_PRINT").add(menu2);
                    parentMenu.add(menu2);
                }
            }
        }

    }


    private void addRatioMenu(JMenu menuParent, boolean withSeparator)
    {
        if (withSeparator)
        {
            menuParent.addSeparator();
        }

        JMenu menuxsub = this.createMenu(menuParent, "MN_DATA_RATIO", null);
        this.createMenuItem(menuxsub, "MN_RATIO_BASE", "MN_RATIO_BASE_DESC", "ratio_base", null);
        this.createMenuItem(menuxsub, "MN_RATIO_EXTENDED", "MN_RATIO_EXTENDED_DESC", "ratio_extended", null);
    }


    private JMenu createMenu(String name, String tool_tip)
    {
        JMenu item = new JMenu(i18Control.getMessageWithoutMnemonic(name));
        item.setMnemonic(i18Control.getMnemonic(name));

        if (tool_tip != null)
        {
            item.setToolTipText(tool_tip);
        }

        this.menuBar.add(item);

        return item;
    }


    // ------------------------------------------------------
    // -- Toolbars
    // ------------------------------------------------------

    private JMenu createMenu(JMenu parent, String name, String tool_tip)
    {
        JMenu item = new JMenu(i18Control.getMessageWithoutMnemonic(name));
        item.setMnemonic(i18Control.getMnemonic(name));

        if (tool_tip != null)
        {
            item.setToolTipText(i18Control.getMessage(tool_tip));
        }

        parent.add(item);

        return item;
    }


    private void createToolBars()
    {
        this.toolbars = new Hashtable<GGCToolbarType, JToolBar>();
        createToolBar_PenInjection();
        createToolBar_Pump();
    }


    private void createToolBar_PenInjection()
    {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        this.toolbars.put(GGCToolbarType.PenInjection, toolbar);

        this.toolbarItems.put(GGCToolbarType.PenInjection, new HashMap<String, JButton>());

        // this.toolbar_pen_items = new Hashtable<String, JMenuItem>();

        Dimension d = new Dimension(25, 25);

        toolbar.addSeparator(d);
        this.createToolbarButton("MN_LOGIN", "MN_LOGIN_DESC", "file_login", "logon.png", GGCToolbarType.PenInjection);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_DAILY", "MN_DAILY_DESC", "pen_view_daily", "calendar.png",
            GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_COURSE", "MN_COURSE_DESC", "pen_graph_course", "line-chart.png",
            GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_SPREAD", "MN_SPREAD_DESC", "pen_graph_pread", "dot-chart.png",
            GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_FREQUENCY", "MN_FREQUENCY_DESC", "pen_graph_freq", "column-chart.png",
            GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_HBA1C", "MN_HBA1C_DESC", "pen_view_hba1c", "pie-chart.png",
            GGCToolbarType.PenInjection);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_MEALS", "MN_MEALS_DESC", "food_meals", "food.png", GGCToolbarType.PenInjection);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_PDF_SIMPLE", "MN_PDF_SIMPLE_DESC", "report_pdf_simple", "print.png",
            GGCToolbarType.PenInjection);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_PREFERENCES", "MN_PREFERENCES_DESC", "tools_pref", "preferences.png",
            GGCToolbarType.PenInjection);
        toolbar.addSeparator(d);
        toolbar.addSeparator(d);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_HELP", "MN_HELP_DESC", "hlp_help", "help.png", GGCToolbarType.PenInjection);

        // this.toolbars.put("TOOLBAR_PEN", toolbar);

    }


    private void createToolBar_Pump()
    {
        JToolBar toolbar = new JToolBar();

        // toolbar_pump = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        this.toolbars.put(GGCToolbarType.Pump, toolbar);

        this.toolbarItems.put(GGCToolbarType.Pump, new HashMap<String, JButton>());
        // this.toolbar_pump_items = new Hashtable<String, JMenuItem>();

        Dimension d = new Dimension(25, 25);

        toolbar.addSeparator(d);
        this.createToolbarButton("MN_LOGIN", "MN_LOGIN_DESC", "file_login", "logon.png", GGCToolbarType.Pump);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_PUMPS_MANUAL_ENTRY", "MN_PUMPS_MANUAL_ENTRY_DESC", "pumps_manual_entry",
            "calendar.png", GGCToolbarType.Pump);
        this.createToolbarButton("MN_HBA1C" + " Old", "MN_HBA1C_DESC", "view_hba1c_old", "pie-chart.png",
            GGCToolbarType.Pump);

        this.createToolbarButton("MN_HBA1C", "MN_HBA1C_DESC", "pen_view_hba1c", "pie-chart.png", GGCToolbarType.Pump);

        toolbar.addSeparator(d);

        this.createToolbarButton("MN_MEALS", "MN_MEALS_DESC", "food_meals", "food.png", GGCToolbarType.Pump);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_PUMP_PRINT_EXT", "MN_PUMP_PRINT_EXT_DESC", "report_print_pump_ext", "print.png",
            GGCToolbarType.Pump);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_PREFERENCES", "MN_PREFERENCES_DESC", "tools_pref", "preferences.png",
            GGCToolbarType.Pump);
        toolbar.addSeparator(d);
        toolbar.addSeparator(d);
        toolbar.addSeparator(d);

        this.createToolbarButton("MN_HELP", "MN_HELP_DESC", "hlp_help", "help.png", GGCToolbarType.Pump);

        // this.toolbars.put("TOOLBAR_PUMP", toolbar);

    }


    /**
     * Set Toolbar by Db Load
     *
     * @param status status type
     */
    public void setToolbarByDbLoad(int status)
    {
        setToolBarItemEnabled("file_login", false);

        if (status == StatusBar.DB_STOPPED)
        {
            // this.createToolbarButton("MN_LOGIN", "MN_LOGIN_DESC",
            // "file_login", "logon.png");
            setToolBarItemEnabled("pen_view_daily", false);
            setToolBarItemEnabled("pen_graph_course", false);
            setToolBarItemEnabled("pen_graph_spread", false);
            setToolBarItemEnabled("pen_graph_freq", false);
            setToolBarItemEnabled("pen_view_hba1c", false);

            // setToolBarItemEnabled("food_meals", false);

            setToolBarItemEnabled("report_pdf_simple", false);

            setToolBarItemEnabled("tools_pref", false);
        }
        else if (status == StatusBar.DB_INIT_DONE)
        {
            setToolBarItemEnabled("pen_view_daily", true);
            setToolBarItemEnabled("pen_graph_course", true);
            setToolBarItemEnabled("pen_graph_spread", true);
            setToolBarItemEnabled("pen_graph_freq", true);
            setToolBarItemEnabled("pen_view_hba1c", true);

            setToolBarItemEnabled("report_pdf_simple", true);
        }
        else if (status == StatusBar.DB_BASE_DONE)
        {
            setToolBarItemEnabled("tools_pref", true);
            loadInitialSize();
        }
        // else // if (status == StatusBar.DB_LOADED)
        // {
        //
        // }

    }


    private void loadInitialSize()
    {
        dataAccess.loadWindowSize(this);
    }


    private void saveInitialSize()
    {
        dataAccess.saveWindowSize(this);

        // dataAccess.getConfigurationManagerWrapper().setMainWindowSize(this.getSize());
        dataAccess.getConfigurationManagerWrapper().saveConfig();
    }


    private void setToolBarItemEnabled(String item_name, boolean enabled)
    {
        for (GGCToolbarType ttype : GGCToolbarType.getActiveValues())
        {
            if (this.toolbarItems.get(ttype).containsKey(item_name))
            {
                this.toolbarItems.get(ttype).get(item_name).setEnabled(enabled);
            }
        }
    }


    private void createToolbarButton(String name, String toolTip, String actionCommand, String iconSmall,
            GGCToolbarType toolbarType)
    {

        JButton button = new JButton();
        button.setName(i18Control.getMessageWithoutMnemonic(name));

        if (toolTip != null)
        {
            button.setToolTipText(i18Control.getMessage(toolTip));
        }

        if (actionCommand != null)
        {
            button.setActionCommand(actionCommand);
        }

        if (iconSmall != null)
        {
            button.setIcon(ATSwingUtils.getImageIcon(iconSmall, 28, 28, this, dataAccess));
        }

        button.addActionListener(this);

        this.toolbarItems.get(toolbarType).put(actionCommand, button);
        this.toolbars.get(toolbarType).add(button);

        // button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        // button.setFocusPainted(false);
        // button.setPreferredSize(new Dimension(28, 28));

        button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(32, 32));

        this.toolbars.get(toolbarType).add(button);

    }


    private void createMenuItem(JMenu menu, String name, String toolTip, String actionCommand, String iconSmall)
    {
        JMenuItem item = new JMenuItem(i18Control.getMessageWithoutMnemonic(name));
        // item.setName(i18nControl.getMessageWithoutMnemonic(name));

        if (i18Control.hasMnemonic(name))
        {
            char ch = i18Control.getMnemonic(name);
            item.setAccelerator(KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));
        }

        if (toolTip != null)
        {
            item.setToolTipText(i18Control.getMessage(toolTip));
        }

        if (actionCommand != null)
        {
            item.setActionCommand(actionCommand);
            item.addActionListener(this);
        }

        if (iconSmall != null)
        {
            item.setIcon(ATSwingUtils.getImageIcon(iconSmall, 15, 15, this, dataAccess));
        }

        if (menu != null)
        {
            menu.add(item);
        }

        if (!this.actions.containsKey(actionCommand))
        {
            this.actions.put(actionCommand, item);
        }
    }


    // ------------------------------------------------------
    // -- Help
    // ------------------------------------------------------

    /**
     * Set menus by Db Loading status
     * 
     * @param status
     */
    public void setMenusByDbLoad(int status)
    {

        // System.out.println("setMenusByDbLoad: " + status);

        if (status == StatusBar.DB_STOPPED)
        {
            // bgs menu
            this.menus.get("MENU_PEN").setEnabled(false);
            this.actions.get("pen_view_daily").setEnabled(false);
            // this.actions.get("view_course").setEnabled(false);
            // this.actions.get("view_spread").setEnabled(false);
            // this.actions.get("view_freq").setEnabled(false);
            this.actions.get("pen_view_hba1c").setEnabled(false);

            // reports menu
            this.menus.get("MENU_PRINT").setEnabled(false);
            this.actions.get("report_pdf_simple").setEnabled(false);
            this.actions.get("report_pdf_extended").setEnabled(false);

            // tools menu
            this.menus.get("MENU_TOOLS").setEnabled(false);
            this.actions.get("tools_pref").setEnabled(false);

            // help menu
            this.actions.get("hlp_check_update").setEnabled(false);

        }
        else if (status == StatusBar.DB_INIT_DONE)
        {
            // bgs menu
            this.menus.get("MENU_PEN").setEnabled(true);
            this.actions.get("pen_view_daily").setEnabled(true);
            // this.actions.get("view_course").setEnabled(true);
            // this.actions.get("view_spread").setEnabled(true);
            // this.actions.get("view_freq").setEnabled(true);
            this.actions.get("pen_view_hba1c").setEnabled(true);

            // reports menu
            this.menus.get("MENU_PRINT").setEnabled(true);
            this.actions.get("report_pdf_simple").setEnabled(true);
            this.actions.get("report_pdf_extended").setEnabled(true);

            // tools menu
            this.menus.get("MENU_TOOLS").setEnabled(true);
            this.actions.get("tools_pref").setEnabled(true);

            // help menu
            this.actions.get("hlp_check_update").setEnabled(true);

        }
        else if (status == StatusBar.DB_BASE_DONE)
        {

        }
        else if (status == StatusBar.DB_LOADED)
        {
            /*
             * // food menu
             * this.menu_food.setEnabled(true);
             * this.actions.get("food_nutrition_1").setEnabled(true);
             * this.actions.get("food_nutrition_2").setEnabled(true);
             * this.actions.get("food_meals").setEnabled(true);
             */
            setSoftwareMode(true); // changed
        }

        setToolbarByDbLoad(status);
    }


    private void helpInit()
    {
        LOG.debug("JavaHelp - START");

        HelpContext hc = dataAccess.getHelpContext();

        LOG.debug("JavaHelp - HelpContext: " + hc);

        JMenuItem helpItem = new JMenuItem(i18Control.getMessage("HELP") + "...");
        helpItem.setIcon(new ImageIcon(getClass().getResource("/icons/help.gif")));
        hc.setHelpItem(helpItem);

        String mainHelpSetName = "/" + dataAccess.getLanguageManager().getHelpSet();

        LOG.debug("JavaHelp - MainHelpSetName: " + mainHelpSetName);

        hc.setMainHelpSetName(mainHelpSetName);

        // try to find the helpset and create a HelpBroker object
        if (hc.getMainHelpBroker() == null)
        {
            HelpSet main_help_set = null;

            try
            {
                URL hsURL = getClass().getResource(mainHelpSetName);
                main_help_set = new HelpSet(null, hsURL);
            }
            catch (HelpSetException ex)
            {
                LOG.error("HelpSet " + mainHelpSetName + " could not be opened.", ex);
            }

            HelpBroker main_help_broker = null;

            if (main_help_set != null)
            {
                LOG.debug("JavaHelp - Main Help Set present, creating broker.");
                main_help_broker = main_help_set.createHelpBroker();
            }

            CSH.DisplayHelpFromSource csh = null;

            if (main_help_broker != null)
            {
                // CSH.DisplayHelpFromSource is a convenience class to display
                // the helpset
                csh = new CSH.DisplayHelpFromSource(main_help_broker);

                if (csh != null)
                {
                    // listen to ActionEvents from the helpItem
                    hc.getHelpItem().addActionListener(csh);
                }
            }

            hc.setDisplayHelpFromSourceInstance(csh);
            hc.setMainHelpBroker(main_help_broker);
            hc.setMainHelpSet(main_help_set);

            CSH.trackCSEvents();
        }

        LOG.debug("JavaHelp - END");
    }


    private void close()
    {

        saveInitialSize();
        // dataAccess.getSettings().save();

        if (dataAccess != null)
        {
            if (dataAccess.getDb() != null)
            {
                dataAccess.getDb().closeDb();
            }

            DataAccess.deleteInstance();
        }

        dispose();
        System.exit(0);
    }


    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();

        if (command.startsWith("meters_"))
        {
            dataAccess.getPlugIn(GGCPluginType.MeterToolPlugin).actionPerformed(e);
        }
        else if (command.startsWith("pumps_") || command.startsWith("report_print_pump"))
        {
            dataAccess.getPlugIn(GGCPluginType.PumpToolPlugin).actionPerformed(e);
        }
        else if (command.startsWith("cgms_"))
        {
            dataAccess.getPlugIn(GGCPluginType.CGMSToolPlugin).actionPerformed(e);
        }
        else if (command.startsWith("food_"))
        {
            dataAccess.getPlugIn(GGCPluginType.NutritionToolPlugin).actionPerformed(e);
        }
        else if (command.equals("file_quit"))
        {
            close();
        }
        else if (command.equals("pen_view_daily"))
        {
            new DailyStatsDialog(dataAccess);
        }
        else if (command.equals("pen_graph_course"))
        {
            startGraphViewer(new GraphViewCourse(null, graphV1DbRetrieverCore));
        }
        else if (command.equals("pen_graph_spread"))
        {
            startGraphViewer(new GraphViewSpread(null, graphV1DbRetrieverCore));
        }
        else if (command.equals("pen_graph_freq"))
        {
            startGraphViewer(new GraphViewFrequency(null, graphV1DbRetrieverCore));
        }
        else if (command.equals("pen_view_hba1c"))
        {
            new HbA1cDialog(dataAccess, graphV1DbRetrieverCore);
        }
        else if (command.equals("tools_pref"))
        {
            PropertiesDialog pd = new PropertiesDialog(dataAccess);

            if (pd.actionSuccessful())
            {
                informationPanel.invalidatePanelsConstants();
                informationPanel.refreshPanels();
            }
        }
        else if (command.equals("hlp_about"))
        {
            new AboutGGCDialog(this);
        }
        else if (command.equals("hlp_check_update"))
        {
            UpgradeDialog ud = new UpgradeDialog(MainFrame.this, new GGCUpgradeApplicationContext(dataAccess),
                    dataAccess);
            ud.enableHelp("GGC_Tools_Update");
            ud.showDialog();
        }
        else if (command.equals("hlp_help"))
        {
            dataAccess.getHelpContext().getDisplayHelpFromSourceInstance().actionPerformed(e);
        }
        else if (command.equals("ratio_base"))
        {
            new RatioBaseDialog(MainFrame.this);
        }
        else if (command.equals("ratio_extended"))
        {
            new RatioExtendedDialog(MainFrame.this);
        }
        else if (command.equals("tools_db_backup"))
        {
            new BackupDialog(MainFrame.this, dataAccess);
        }
        else if (command.equals("tools_db_restore"))
        {
            RestoreGGCSelectorDialog rsd = new RestoreGGCSelectorDialog(MainFrame.this, dataAccess);
            rsd.showDialog();

            // update main panels
            DataAccess.getInstance().loadDailySettings(new GregorianCalendar(), true);
            dataAccess.setChangeOnEventSource(DataAccess.OBSERVABLE_PANELS, RefreshInfo.PANEL_GROUP_ALL_DATA);
        }
        else if (command.equals("report_pdf_simple"))
        {
            new PrintingDialog(MainFrame.this, 1, PrintingDialog.PRINT_DIALOG_YEAR_MONTH_OPTION);
        }
        else if (command.equals("report_pdf_extended"))
        {
            new PrintingDialog(MainFrame.this, 2, PrintingDialog.PRINT_DIALOG_YEAR_MONTH_OPTION);
        }
        else if (command.equals("doc_docs"))
        {
            // FIXME
            if (MainFrame.developer_version)
            {
                new GUIListDialog(MainFrame.this, new DoctorListDef(), dataAccess);
            }
            else
            {
                featureNotImplemented(command, nextVersion);
            }
        }
        else if (command.equals("doc_appoint"))
        {
            if (MainFrame.developer_version)
            {
                new GUIListDialog(MainFrame.this, new AppointmentListDef(), dataAccess);

                // new AppointmentsDialog(MainFrame.this);
            }
            else
            {
                featureNotImplemented(command, nextVersion);
            }

        }
        else if (command.equals("file_login") || command.equals("file_logout"))
        {
            featureNotImplemented(command, nextVersion);
        }
        else if (command.equals("misc_synchronize")) // ||
        // (command.equals("doc_stocks")))
        {
            featureNotImplemented(command, "0.7");
        }
        else if (command.equals("doc_stocks"))
        {
            if (MainFrame.developer_version)
            {
                new GUIListDialog(MainFrame.this, new StockListDef(), dataAccess);
            }
            else
            {
                featureNotImplemented(command, nextVersion);
            }

        }
        else if (command.equals("test"))
        {

            // new DailyRowMealsDialog(null, new JDialog());

            // spread graph
            // new GraphViewer(new GraphViewSpread(), dataAccess);

            /*
             * // graph course
             * new GraphViewer(new GraphViewCourse(), dataAccess);
             */

            // ratio calculator
            // @SuppressWarnings("unused")
            // RatioCalculatorDialog rcd = new
            // RatioCalculatorDialog(MainFrame.this);

            // BasalRateEstimator bre = new BasalRateEstimator();

            /*
             * // daily view
             * GregorianCalendar gc = new GregorianCalendar();
             * gc.set(GregorianCalendar.DAY_OF_MONTH, 18);
             * gc.set(GregorianCalendar.MONTH, 10);
             * gc.set(GregorianCalendar.YEAR, 2008);
             * new GraphViewer(new GraphViewDaily(gc), dataAccess);
             */

            // new HbA1cDialog(dataAccess);
            // ImportDacioDb idb = new
            // ImportDacioDb("../data/temp/zivila.csv", true); //args[
            // idb.convertFoods();
            /*
             * DayValuesData dvd =
             * dataAccess.getDb().getDayValuesData(20081001,
             * 20091007); // .getMonthlyValues(yr,
             * // mnth);
             * PrintFoodMenuExt2 psm = new PrintFoodMenuExt2(dvd);
             * PrintingDialog.displayPDFExternal(psm.getName());
             */
            // BolusHelper bh = new BolusHelper(MainFrame.this);
            // featureNotImplemented(command, "0.6");
        }
        else
        {
            System.out.println("Unknown Command: " + command);
        }

    }


    private void startGraphViewer(GraphViewAbstract graphViewAbstract)
    {
        Dimension size = dataAccess.getConfigurationManagerWrapper().getGraphViewerSize();

        graphViewAbstract.setInitialSize(size);

        GraphViewer graphViewer = new GraphViewer(graphViewAbstract, dataAccess, MainFrame.this, true);

        size = graphViewer.getSize();

        dataAccess.getConfigurationManagerWrapper().setGraphViewerSize(size);
    }


    /**
     * Invalidate panels
     */
    public void invalidatePanels()
    {
        this.informationPanel.invalidatePanelsConstants();
    }

    /*
     * private JButton addToolBarButtonWithName(String cmd) { return
     * addToolBarButton(this.actions.get(cmd)); }
     */


    // class GGCAction extends AbstractAction
    // {
    //
    // /**
    // *
    // */
    // private static final long serialVersionUID = -1022459758999093522L;
    //
    //
    // // GGCAction(String name, String command)
    // // {
    // // super();
    // // setName(i18nControl.getMessageWithoutMnemonic(name));
    // //
    // // putValue(Action.NAME, i18nControl.getMessageWithoutMnemonic(name));
    // //
    // // if (i18nControl.hasMnemonic(name))
    // // {
    // // char ch = i18nControl.getMnemonic(name);
    // // putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch,
    // // Event.CTRL_MASK));
    // // }
    // // else
    // // {
    // // putValue(ACCELERATOR_KEY, null);
    // // }
    // //
    // // if (command != null)
    // // {
    // // putValue(ACTION_COMMAND_KEY, command);
    // // }
    // //
    // // command = name;
    // // }
    //
    // GGCAction(String name, String tooltip, String command)
    // {
    // super();
    // setName(i18nControl.getMessageWithoutMnemonic(name));
    //
    // putValue(Action.NAME, i18nControl.getMessageWithoutMnemonic(name));
    //
    // // char ch = i18nControl.getMnemonic(name);
    //
    // // System.out.println("Char ch: '" + ch + "'");
    //
    // // if ((ch != '0') || (ch != ' '))
    // if (i18nControl.hasMnemonic(name))
    // {
    // char ch = i18nControl.getMnemonic(name);
    //
    // putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));
    // // System.out.println("Found");
    // }
    // else
    // {
    // putValue(ACCELERATOR_KEY, null);
    // // System.out.println("NOT Found");
    // }
    //
    // if (tooltip != null)
    // {
    // putValue(SHORT_DESCRIPTION, i18nControl.getMessage(tooltip));
    // }
    //
    // if (command != null)
    // {
    // putValue(ACTION_COMMAND_KEY, command);
    // }
    // }
    //
    //
    // public String getName()
    // {
    // return (String) getValue(Action.NAME);
    // }
    //
    // String next_version = "0.7";
    //
    //
    // }

    /**
     * Refresh panels
     */
    public void refreshPanels()
    {
        this.informationPanel.refreshPanels();
    }


    private void featureNotImplemented(String cmd, String version)
    {
        String text = i18Control.getMessage("FEATURE");

        text += " '" + this.actions.get(cmd).getName() + "' ";
        text += String.format(i18Control.getMessage("IMPLEMENTED_VERSION"), version);
        text += "!";

        JOptionPane.showMessageDialog(MainFrame.this, text, i18Control.getMessage("INFORMATION"),
            JOptionPane.INFORMATION_MESSAGE);

    }


    private void featureNotImplementedDescription(String desc, String version)
    {
        String text = i18Control.getMessage("FEATURE");

        text += " '" + desc + "' ";
        text += String.format(i18Control.getMessage("IMPLEMENTED_VERSION"), version);
        text += "!";

        JOptionPane.showMessageDialog(MainFrame.this, text, i18Control.getMessage("INFORMATION"),
            JOptionPane.INFORMATION_MESSAGE);

    }


    /**
     * To String
     * 
     * @see java.awt.Component#toString()
     */
    @Override
    public String toString()
    {
        return "GGC::MainFrame";
    }


    /**
     * Update
     */
    public void update(Observable obj, Object arg)
    {
        // System.out.println("update");
        if (arg instanceof Integer)
        {
            Integer i = (Integer) arg;

            if (!title_set)
            {
                if (i >= RefreshInfo.PANEL_GROUP_GENERAL_INFO)
                {
                    title_set = true;
                    this.setSoftwareMode();
                }
            }

            // Integer i = (Integer)arg;
            setMenusByDbLoad(i.intValue());

            if (dataAccess.getDbLoadingStatus() >= GGCDbLoader.DB_DATA_BASE)
            {
                ;
            }
            this.setSoftwareMode();
            // this.setTitle();

            if (i == RefreshInfo.DB_LOADED)
            {
                refreshMenus();
            }
        }
        else if (arg instanceof Exception)
        {
            Exception ex = (Exception) arg;
            LOG.error("Error connecting to Database: " + ex.getMessage(), ex);

            dataAccess.createErrorDialog("Main", "Opening Db", ex, getDbSettingsAndCause(false), //
                getDbSettingsAndCause(true), //
                i18Control.getMessage("DB_PROBLEM_NOT_CONNECTED_SOLUTION"), //
                i18Control.getMessage("DB_PROBLEM_NOT_CONNECTED_SOLUTION_TOOLTIP"));

            System.exit(1);
        }
    }


    private String getDbSettingsAndCause(boolean forToolTip)
    {
        HibernateConfiguration hc = dataAccess.getDb().getHibernateConfiguration();

        StringBuilder sb = new StringBuilder();

        if (!forToolTip)
        {
            return String.format(i18Control.getMessage("DB_PROBLEM_NOT_CONNECTED"), hc.db_num, hc.db_conn_name,
                hc.db_driver_class);
        }
        else
        {
            // sb.append(
            // "There was problem connecting/getting metadata from you database.
            // Likely cause of this might be misconfiguration of your database
            // connection and/or missing database driver (JDBC).");
            // sb.append("<br><b>Currently selected configuration is: </b>");
            // sb.append(hc.db_num);
            // sb.append(" - " + hc.db_conn_name);
            // sb.append("<br><b>Driver Class:</b> " + hc.db_driver_class);
            // sb.append("<br>Connection: " + hc.db_conn_url);
            // sb.append("<br>Username: " + hc.db_conn_username);
            // sb.append("<br>Password: " + hc.db_conn_password);
            // sb.append("<br>Dialect: " + hc.db_hib_dialect);

            return String.format(i18Control.getMessage("DB_PROBLEM_NOT_CONNECTED_TOOLTIP"), hc.db_num, hc.db_conn_name,
                hc.db_driver_class, hc.db_conn_url, hc.db_conn_username, hc.db_conn_password, hc.db_hib_dialect);
        }
        //
        // return sb.toString();
    }


    /**
     * This refresh is used when configuration of plugin changes
     */
    // FIXME
    public void refreshMenusPlugins()
    {
        this.refreshMenus(false);
    }


    private JMenu getPlugInMenu(GGCPluginType name)
    {
        if (dataAccess.isPluginAvailable(name))
        {
            PlugInClient pic = dataAccess.getPlugIn(name);
            return pic.getPlugInMainMenu();
        }
        else
            return null;
    }


    public String getSettingKey()
    {
        return "MAIN_WINDOW_SIZE";
    }


    public Dimension getMinimalSize()
    {
        return new Dimension(800, 600);
    }


    public Dimension getDefaultSize()
    {
        return new Dimension(800, 600);
    }


    public Window getContainer()
    {
        return this;
    }

    enum GGCToolbarType
    {
        PenInjection, //
        Pump, //
        None;

        private static List<GGCToolbarType> activeValues;


        static
        {
            activeValues = new ArrayList<GGCToolbarType>();
            activeValues.add(GGCToolbarType.PenInjection);
            activeValues.add(GGCToolbarType.Pump);
        }


        public static List<GGCToolbarType> getActiveValues()
        {
            return activeValues;
        }
    }

}