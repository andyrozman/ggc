/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MainFrame.java
 *  Purpose:  The MainFrame of the app. Contains MenuBars, ToolBars, 
 *            StatusBars, ...
 *
 *  Author:   schultd, andyrozman
 */

package ggc.gui;

import ggc.GGC;
import ggc.core.db.tool.transfer.BackupDialog;
import ggc.core.db.tool.transfer.RestoreGGCSelectorDialog;
import ggc.core.nutrition.GGCTreeRoot;
import ggc.core.nutrition.NutritionTreeDialog;
import ggc.core.plugins.CGMSPlugIn;
import ggc.core.plugins.MetersPlugIn;
import ggc.core.plugins.PumpsPlugIn;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;
import ggc.gui.dialogs.AboutGGCDialog;
import ggc.gui.dialogs.AppointmentsDialog;
import ggc.gui.dialogs.BolusHelper;
import ggc.gui.dialogs.CourseGraphDialog;
import ggc.gui.dialogs.DailyStatsDialog;
import ggc.gui.dialogs.DoctorsDialog;
import ggc.gui.dialogs.FrequencyGraphDialog;
import ggc.gui.dialogs.HbA1cDialog;
import ggc.gui.dialogs.PrintingDialog;
import ggc.gui.dialogs.PropertiesDialog;
import ggc.gui.dialogs.RatioDialog;
import ggc.gui.dialogs.SpreadGraphDialog;
import ggc.gui.panels.info.InfoPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.atech.help.HelpContext;
import com.atech.update.client.UpdateDialog;
import com.atech.update.config.UpdateConfiguration;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;


public class MainFrame extends JFrame
{

    private static final long serialVersionUID = -8971779470148201332L;

    private I18nControl m_ic = null;
    public static SkinLookAndFeel s_skinlf;

    private static final String skinLFdir = "../data/skinlf_themes/";

    public static boolean developer_version = false;

    // fields
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();

    // private JLabel lblTest = new JLabel();

    private JMenu menu_file, menu_bgs, menu_food, menu_doctor, menu_printing,
            menu_tools, menu_help, menu_meters, menu_pumps, menu_data_graphs,
            menu_cgms /* , menu_misc */;

    private Hashtable<String, GGCAction> actions = null;
    private Hashtable<String, GGCAction> toolbar_items = null;

    /*
     * private GGCAction ac_file_quit, // file ac_bgs_daily, ac_rep_simple,
     * ac_rep_ext, // reports ac_food_nutr, ac_food_meals, // food ac_doc_docs,
     * ac_doc_appoint; // doctors
     * 
     * //private GGCAction connectAction, disconnectAction, newAction,
     * openAction, closeAction, private GGCAction quitAction, prefAction,
     * readMeterAction;
     * 
     * private GGCAction viewDailyAction, viewCourseGraphAction,
     * viewSpreadGraphAction, viewFrequencyGraphAction;
     * 
     * private GGCAction viewHbA1cAction;
     * 
     * private GGCAction foodNutrAction, foodMealsAction, reportPDFSimpleAction,
     * reportPDFExtendedAction;
     * 
     * private GGCAction aboutAction, checkVersionAction;
     */
    // private DailyStatsFrame dailyStatsWindow;
    public StatusBar statusPanel;

    public InfoPanel informationPanel;

    private DataAccess m_da = null;


    /**
     * Static definitions (Look and Feel)
     */
    static
    {
        MainFrame.setLookAndFeel();
    }

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

    // Specialty Definitions - Types of Doctors

    // constructor
    public MainFrame(String title, boolean developer_version)
    {
        // this is the first chance to call this method after an instance of
        // GGCProperties has been created
        // m_ic.setLanguage();

        //System.out.println("MainFrame before creation");
        m_da = DataAccess.createInstance(this);

        //System.out.println("m_da: " + m_da);
        
        m_ic = I18nControl.getInstance();

        m_da.addComponent(this);

        statusPanel = new StatusBar();

        this.actions = new Hashtable<String, GGCAction>();
        MainFrame.developer_version = developer_version;

        String title_full = title + " (" + GGC.full_version + ")";

        if (developer_version)
            title_full += " - Developer edition";

        setTitle(title_full);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        helpInit();

        initPlugIns();

        // menu_file, menu_bgs, menu_food, menu_doctor, menu_reports,
        // menu_tools, menu_help;

        createMenus();

        createToolBar();
        /*
         * addToolBarButtonWithName("view_daily");
         * addToolBarButtonWithName("view_course");
         * addToolBarButtonWithName("view_spread");
         * addToolBarButtonWithName("view_freq"); addToolBarSpacer();
         * addToolBarSpacer();
         * 
         * addToolBarButtonWithName("view_hba1c"); addToolBarSpacer();
         * addToolBarSpacer(); //addToolBarButtonWithName("view_freq");
         * 
         * 
         * 
         * addToolBarButtonWithName("tools_pref"); addToolBarSpacer();
         * addToolBarSpacer(); addToolBarButtonWithName("hlp_help");
         */
        // this.menu_help.add(GGCHelp.helpItem);

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        m_da.startDb(statusPanel);

        statusPanel.setStatusMessage(m_ic.getMessage("INIT"));

        // Information Portal Setup
        informationPanel = new InfoPanel();
        getContentPane().add(informationPanel, BorderLayout.CENTER);

        // setDbActions(false);
        setMenusByDbLoad(StatusBar.DB_STOPPED);
        this.setVisible(true);

    }

    private void initPlugIns()
    {
        m_da.addPlugIn(DataAccess.PLUGIN_METERS, new MetersPlugIn(this, m_ic));
        //m_da.getPlugIn(DataAccess.PLUGIN_METERS).checkIfInstalled();

        m_da.addPlugIn(DataAccess.PLUGIN_PUMPS, new PumpsPlugIn(this, m_ic));
        //m_da.getPlugIn(DataAccess.PLUGIN_PUMPS).checkIfInstalled();

        m_da.addPlugIn(DataAccess.PLUGIN_CGMS, new CGMSPlugIn(this, m_ic));
        //m_da.getPlugIn(DataAccess.PLUGIN_CGMS).checkIfInstalled();
    }

    public void createMenus()
    {
        // file menu
        this.menu_file = this.createMenu("MN_FILE", null);
        this.createAction(this.menu_file, "MN_LOGIN", "MN_LOGIN_DESC",
                "file_login", "logon.png");
        this.createAction(this.menu_file, "MN_LOGOUT", "MN_LOGOUT_DESC",
                "file_logout", "logout.png");
        this.menu_file.addSeparator();
        this.createAction(this.menu_file, "MN_QUIT", "MN_QUIT_DESC",
                "file_quit", null);

        // bgs menu
        this.menu_bgs = this.createMenu("MN_DATA", null);
        this.createAction(this.menu_bgs, "MN_DAILY", "MN_DAILY_DESC",
                "view_daily", "calendar.png"); // "daily.gif");
        this.menu_bgs.addSeparator();

        this.menu_data_graphs = this.createMenu(this.menu_bgs, "MN_DATA_GRAPH",
                null);

        this.createAction(this.menu_data_graphs, "MN_COURSE", "MN_COURSE_DESC",
                "view_course", "line-chart.png"); // "course.gif");
        this.createAction(this.menu_data_graphs, "MN_SPREAD", "MN_SPREAD_DESC",
                "view_spread", "dot-chart.png"); // "spread.gif");
        this.createAction(this.menu_data_graphs, "MN_FREQUENCY",
                "MN_FREQUENCY_DESC", "view_freq", "column-chart.png"); // "frequency.gif"
                                                                       // );
        this.menu_bgs.addSeparator();
        this.createAction(this.menu_bgs, "MN_HBA1C", "MN_HBA1C_DESC",
                "view_hba1c", "pie-chart.png"); // null);
        this.menu_bgs.addSeparator();
        this.createAction(this.menu_bgs, "MN_RATIO_CALC", "MN_RATIO_CALC_DESC",
                "view_ratio", null); // null);

        // food menu
        this.menu_food = this.createMenu("MN_FOOD", null);
        this.createAction(this.menu_food, "MN_NUTRDB_USDB",
                "MN_NUTRDB_USDB_DESC", "food_nutrition_1", null);
        this.menu_food.addSeparator();
        this.createAction(this.menu_food, "MN_NUTRDB_USER",
                "MN_NUTRDB_USER_DESC", "food_nutrition_2", null);
        this.menu_food.addSeparator();
        this.createAction(this.menu_food, "MN_MEALS", "MN_MEALS_DESC",
                "food_meals", null);

        // doctors menu
        this.menu_doctor = this.createMenu("MN_DOCTOR", null);
        this.createAction(this.menu_doctor, "MN_DOCS", "MN_DOCS_DESC",
                "doc_docs", null);
        this.menu_doctor.addSeparator();
        this.createAction(this.menu_doctor, "MN_APPOINT", "MN_APPOINT_DESC",
                "doc_appoint", null);
        this.menu_doctor.addSeparator();
        this.createAction(this.menu_doctor, "MN_STOCKS", "MN_STOCKS_DESC",
                "doc_stocks", null);

        // reports menu
        this.menu_printing = this.createMenu("MN_PRINTING", null);
        JMenu menu_reports = this.createMenu(this.menu_printing, "MN_REPORTS",
                "MN_REPORTS_DESC");
        this.createAction(menu_reports, "MN_PDF_SIMPLE", "MN_PDF_SIMPLE_DESC",
                "report_pdf_simple", "print.png");
        this.createAction(menu_reports, "MN_PDF_EXT", "MN_PDF_EXT_DESC",
                "report_pdf_extended", "print.png");

        // meters
        this.menu_meters = this.createMenu("MN_METERS", null);
        this.createAction(menu_meters, "MN_METERS_READ", "MN_METERS_READ_DESC", "meters_read", null);
        this.menu_meters.addSeparator();
        this.createAction(menu_meters, "MN_METERS_LIST", "MN_METERS_LIST_DESC", "meters_list", null);
        this.menu_meters.addSeparator();
        this.createAction(menu_meters, "MN_METERS_CONFIG", "MN_METERS_CONFIG_DESC", "meters_config", null);

        // pumps
        this.menu_pumps = this.createMenu("MN_PUMPS", null);
        this.createAction(menu_pumps, "MN_PUMP_PROFILES",
                "MN_PUMP_PROFILES_DESC", "pumps_profile", null);
        this.menu_pumps.addSeparator();
        this.createAction(menu_pumps, "MN_PUMPS_MANUAL_ENTRY",
                "MN_PUMPS_MANUAL_ENTRY_DESC", "pumps_manual_entry", null);
        this.createAction(menu_pumps, "MN_PUMPS_READ", "MN_PUMPS_READ_DESC",
                "pumps_read", null);
        this.createAction(menu_pumps, "MN_PUMPS_ADDITIONAL_DATA",
                "MN_PUMPS_ADDITIONAL_DATA_DESC", "pumps_additional_data", null);
        this.menu_pumps.addSeparator();
        this.createAction(menu_pumps, "MN_PUMPS_LIST", "MN_PUMPS_LIST_DESC",
                "pumps_list", null);
        this.menu_pumps.addSeparator();
        this.createAction(menu_pumps, "MN_PUMPS_CONFIG",
                "MN_PUMPS_CONFIG_DESC", "pumps_config", null);

        // meters
        this.menu_cgms = this.createMenu("MN_CGMS", null);
        this.createAction(menu_cgms, "MN_CGMS_READ", "MN_CGMS_READ_DESC",
                "cgms_read", null);
        this.menu_cgms.addSeparator();
        this.createAction(menu_cgms, "MN_CGMS_LIST", "MN_CGMS_LIST_DESC",
                "cgms_list", null);
        this.menu_cgms.addSeparator();
        this.createAction(menu_cgms, "MN_CGMS_CONFIG", "MN_CGMS_CONFIG_DESC",
                "cgms_config", null);

        // tools menu
        this.menu_tools = this.createMenu("MN_TOOLS", null);
        this.createAction(this.menu_tools, "MN_PREFERENCES",
                "MN_PREFERENCES_DESC", "tools_pref", "preferences.png");
        this.menu_tools.addSeparator();

        JMenu menu_db_maint = this.createMenu(this.menu_tools, "MN_DB_MAINT",
                "MN_DB_MAINT_DESC");
        this.createAction(menu_db_maint, "MN_DB_BACKUP", "MN_DB_BACKUP_DESC",
                "tools_db_backup", "export1.png");
        this.createAction(menu_db_maint, "MN_DB_RESTORE", "MN_DB_RESTORE_DESC",
                "tools_db_restore", "import1.png");

        // this.createAction(this.menu_tools, "MN_DB_MAINT", "MN_DB_MAINT_DESC",
        // "tools_db_maint", null);
        this.menu_tools.addSeparator();
        this.createAction(this.menu_tools, "MN_MISC_SYNCHRONIZE",
                "MN_MISC_SYNCHRONIZE_DESC", "misc_synchronize", null);

        // help menu
        this.menu_help = this.createMenu("MN_HELP", null);
        this.menu_help.add(m_da.getHelpContext().getHelpItem());
        this.menu_help.addSeparator();
        this.createAction(this.menu_help, "MN_CHECK_FOR_UPDATE",
                "MN_CHECK_FOR_UPDATE_DESC", "hlp_check_update", null);
        this.menu_help.addSeparator();
        this.createAction(this.menu_help, "MN_ABOUT", "MN_ABOUT_DESC", "hlp_about", null);
        
        if (MainFrame.developer_version)
        {
            this.menu_help.addSeparator();
            this.createAction(this.menu_help, "MN_TEST", "MN_TEST_DESC", "test", null);
        }

    }

    private void createToolBar()
    {

        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        this.toolbar_items = new Hashtable<String, GGCAction>();

        Dimension d = new Dimension(25, 25);

        toolBar.addSeparator(d);
        this.createToolbarAction("MN_LOGIN", "MN_LOGIN_DESC", "file_login",
                "logon.png");
        toolBar.addSeparator(d);

        this.createToolbarAction("MN_DAILY", "MN_DAILY_DESC", "view_daily",
                "calendar.png");
        this.createToolbarAction("MN_COURSE", "MN_COURSE_DESC", "view_course",
                "line-chart.png");
        this.createToolbarAction("MN_SPREAD", "MN_SPREAD_DESC", "view_spread",
                "dot-chart.png");
        this.createToolbarAction("MN_FREQUENCY", "MN_FREQUENCY_DESC",
                "view_freq", "column-chart.png");
        this.createToolbarAction("MN_HBA1C", "MN_HBA1C_DESC", "view_hba1c",
                "pie-chart.png");
        toolBar.addSeparator(d);

        this.createToolbarAction("MN_MEALS", "MN_MEALS_DESC", "food_meals",
                "food.png");
        toolBar.addSeparator(d);

        this.createToolbarAction("MN_PDF_SIMPLE", "MN_PDF_SIMPLE_DESC",
                "report_pdf_simple", "print.png");
        toolBar.addSeparator(d);

        this.createToolbarAction("MN_PREFERENCES", "MN_PREFERENCES_DESC",
                "tools_pref", "preferences.png");
        toolBar.addSeparator(d);
        toolBar.addSeparator(d);
        toolBar.addSeparator(d);

        this.createToolbarAction("MN_HELP", "MN_HELP_DESC", "hlp_help",
                "help.png");

    }

    public void helpInit()
    {
        HelpContext hc = new HelpContext("../data/help/GGC.hs");

        m_da.setHelpContext(hc);

        JMenuItem helpItem = new JMenuItem(m_ic.getMessage("HELP") + "...");
        helpItem.setIcon(new ImageIcon(getClass()
                .getResource("/icons/help.gif")));

        hc.setHelpItem(helpItem);

        String mainHelpSetName = "../data/help/GGC.hs";
        mainHelpSetName = mainHelpSetName.replace("/", File.separator);

        hc.setMainHelpSetName(mainHelpSetName);

        // try to find the helpset and create a HelpBroker object
        if (hc.getMainHelpBroker() == null)
        {

            // System.out.println("Help init broker");

            HelpSet main_help_set = null;
            // HelpContext.mainHelpSet = null;

            // X ClassLoader cl = MainFrame.class.getClassLoader();
            // String help_url = "jar:file:pis_lang-0.1.jar!/help/PIS.hs";

            String help_url = "jar:file:ggc_help-0.1.jar!/help/en/GGC.hs";

            try
            {
                URL hsURL = new URL(help_url);

                if (hsURL == null)
                    System.out
                            .println("HelpSet " + help_url /*
                                                            * PISMain.mainHelpSetName
                                                            */+ " not found.");
                else
                    main_help_set = new HelpSet(null, hsURL);
            }
            catch (HelpSetException ee)
            {
                System.out.println("HelpSet " + help_url
                        + " could not be opened.");
                System.out.println(ee.getMessage());
            }
            catch (MalformedURLException ee)
            {
                System.out.println("Problem with HelpSet path: " + help_url
                        + "\n" + ee);
            }

            HelpBroker main_help_broker = null;

            if (main_help_set != null)
            {
                // System.out.println(
                // "Help: Main Help Set present, creating broker");
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

    }

    public MainFrame getMyParent()
    {
        return this;
    }

    public void invalidatePanels()
    {
        this.informationPanel.invalidatePanelsConstants();
    }

    public void refreshPanels()
    {
        this.informationPanel.refreshPanels();
    }

    private JMenu createMenu(String name, String tool_tip)
    {
        JMenu item = new JMenu(m_ic.getMessageWithoutMnemonic(name));
        item.setMnemonic(m_ic.getMnemonic(name));

        if (tool_tip != null)
        {
            item.setToolTipText(tool_tip);
        }

        // System.out.println("Item: " + item);

        this.menuBar.add(item);

        return item;
    }

    private JMenu createMenu(JMenu parent, String name, String tool_tip)
    {
        JMenu item = new JMenu(m_ic.getMessageWithoutMnemonic(name));
        item.setMnemonic(m_ic.getMnemonic(name));

        if (tool_tip != null)
        {
            item.setToolTipText(tool_tip);
        }

        // System.out.println("Item: " + item);

        parent.add(item);
        // this.menuBar.add(item);

        return item;
    }

    private void createToolbarAction(String name, String tip,
            String action_command, String icon_small)
    {
        GGCAction action = new GGCAction(name, tip, action_command);

        if (icon_small != null)
        {
            action.putValue(Action.SMALL_ICON, m_da.getImageIcon(icon_small,
                    24, 24, this));
        }

        // this.toolBar.add(action);
        this.toolbar_items.put(action_command, action);

        addToolBarButton(action);

    }

    private void createAction(JMenu menu, String name, String tip,
            String action_command, String icon_small)
    {
        GGCAction action = new GGCAction(name, tip, action_command);

        if (icon_small != null)
        {
            action.putValue(Action.SMALL_ICON, m_da.getImageIcon(icon_small,
                    15, 15, this));
            // new ImageIcon(getClass().getResource("/icons/" + icon_small)));
            // action.putValue(Action.LARGE_ICON_KEY, new
            // ImageIcon(getClass().getResource("/icons/" + icon_small)));
        }

        if (menu != null)
            menu.add(action);

        this.actions.put(action_command, action);

        // return action;
    }

    public void setMenusByDbLoad(int status)
    {
        if (status == StatusBar.DB_STOPPED)
        {
            // bgs menu
            this.menu_bgs.setEnabled(false);
            this.actions.get("view_daily").setEnabled(false);
            this.actions.get("view_course").setEnabled(false);
            this.actions.get("view_spread").setEnabled(false);
            this.actions.get("view_freq").setEnabled(false);
            this.actions.get("view_hba1c").setEnabled(false);

            // reports menu
            this.menu_printing.setEnabled(false);
            this.actions.get("report_pdf_simple").setEnabled(false);
            this.actions.get("report_pdf_extended").setEnabled(false);

            // food menu
            this.menu_food.setEnabled(false);
            this.actions.get("food_nutrition_1").setEnabled(false);
            this.actions.get("food_nutrition_2").setEnabled(false);
            this.actions.get("food_meals").setEnabled(false);

            // tools menu
            this.menu_tools.setEnabled(false);
            this.actions.get("tools_pref").setEnabled(false);

            // help menu
            this.actions.get("hlp_check_update").setEnabled(false);

        }
        else if (status == StatusBar.DB_INIT_DONE)
        {

            // bgs menu
            this.menu_bgs.setEnabled(true);
            this.actions.get("view_daily").setEnabled(true);
            this.actions.get("view_course").setEnabled(true);
            this.actions.get("view_spread").setEnabled(true);
            this.actions.get("view_freq").setEnabled(true);
            this.actions.get("view_hba1c").setEnabled(true);

            // reports menu
            this.menu_printing.setEnabled(true);
            this.actions.get("report_pdf_simple").setEnabled(true);
            this.actions.get("report_pdf_extended").setEnabled(true);

            // tools menu
            this.menu_tools.setEnabled(true);
            this.actions.get("tools_pref").setEnabled(true);

            // help menu
            this.actions.get("hlp_check_update").setEnabled(true);

        }
        else if (status == StatusBar.DB_BASE_DONE)
        {

        }
        else if (status == StatusBar.DB_LOADED)
        {
            // food menu
            this.menu_food.setEnabled(true);
            this.actions.get("food_nutrition_1").setEnabled(true);
            this.actions.get("food_nutrition_2").setEnabled(true);
            this.actions.get("food_meals").setEnabled(true);
        }

        /*
         * 
         * // file menu this.createAction(this.menu_file, "MN_LOGIN",
         * "MN_LOGIN_DESC", "file_login", null);
         * this.createAction(this.menu_file, "MN_LOGOUT", "MN_LOGOUT_DESC",
         * "file_logout", null);
         * 
         * 
         * // tools menu this.createAction(this.menu_tools, "MN_DB_MAINT",
         * "MN_DB_MAINT_DESC", "tools_db_maint", null);
         * this.createAction(this.menu_tools, "MN_MISC_SYNCHRONIZE",
         * "MN_MISC_SYNCHRONIZE_DESC", "misc_synchronize", null);
         * 
         * // meters this.createAction(menu_meters, "MN_METERS_READ",
         * "MN_METERS_READ_DESC", "meters_read", null);
         * this.createAction(menu_meters, "MN_METERS_LIST",
         * "MN_METERS_LIST_DESC", "meters_list", null);
         * this.createAction(menu_meters, "MN_METERS_CONFIG",
         * "MN_METERS_CONFIG_DESC", "meters_config", null);
         * 
         * // pumps this.createAction(menu_pumps, "MN_PUMPS_READ",
         * "MN_PUMPS_READ_DESC", "pumps_read", null);
         * this.createAction(menu_pumps, "MN_PUMPS_LIST", "MN_PUMPS_LIST_DESC",
         * "pumps_list", null); this.createAction(menu_pumps, "MN_PUMPS_CONFIG",
         * "MN_PUMPS_CONFIG_DESC", "pumps_config", null);
         * 
         * // doctors menu this.createAction(this.menu_doctor, "MN_DOCS",
         * "MN_DOCS_DESC", "doc_docs", null);
         * this.createAction(this.menu_doctor, "MN_APPOINT", "MN_APPOINT_DESC",
         * "doc_appoint", null); this.createAction(this.menu_doctor,
         * "MN_STOCKS", "MN_STOCKS_DESC", "doc_stocks", null);
         */

        setToolbarByDbLoad(status);
    }

    public void setToolbarByDbLoad(int status)
    {

        // this.toolbar_items.get("hlp_help").setEnabled(true)
        this.toolbar_items.get("file_login").setEnabled(false);

        if (status == StatusBar.DB_STOPPED)
        {
            // this.createToolbarAction("MN_LOGIN", "MN_LOGIN_DESC",
            // "file_login", "logon.png");
            this.toolbar_items.get("view_daily").setEnabled(false);
            this.toolbar_items.get("view_course").setEnabled(false);
            this.toolbar_items.get("view_spread").setEnabled(false);
            this.toolbar_items.get("view_freq").setEnabled(false);
            this.toolbar_items.get("view_hba1c").setEnabled(false);

            this.toolbar_items.get("food_meals").setEnabled(false);

            this.toolbar_items.get("report_pdf_simple").setEnabled(false);

            this.toolbar_items.get("tools_pref").setEnabled(false);
        }
        else if (status == StatusBar.DB_INIT_DONE)
        {
            this.toolbar_items.get("view_daily").setEnabled(true);
            this.toolbar_items.get("view_course").setEnabled(true);
            this.toolbar_items.get("view_spread").setEnabled(true);
            this.toolbar_items.get("view_freq").setEnabled(true);
            this.toolbar_items.get("view_hba1c").setEnabled(true);

            this.toolbar_items.get("report_pdf_simple").setEnabled(true);
        }
        else if (status == StatusBar.DB_BASE_DONE)
        {
            this.toolbar_items.get("tools_pref").setEnabled(true);
        }
        else if (status == StatusBar.DB_LOADED)
        {
            this.toolbar_items.get("food_meals").setEnabled(true);
        }

    }

    /*
     * public void setDbActions(boolean opened) {
     * 
     * this.menu_bgs.setEnabled(opened); this.menu_food.setEnabled(opened);
     * this.menu_doctor.setEnabled(opened);
     * this.menu_printing.setEnabled(opened);
     * 
     * this.menu_meters.setEnabled(true); this.menu_pumps.setEnabled(true);
     * 
     * this.actions.get("view_daily").setEnabled(opened);
     * this.actions.get("view_course").setEnabled(opened);
     * this.actions.get("view_spread").setEnabled(opened);
     * this.actions.get("view_freq").setEnabled(opened);
     * this.actions.get("view_hba1c").setEnabled(opened); //x
     * this.actions.get("read_meter").setEnabled(opened);
     * this.actions.get("tools_pref").setEnabled(opened);
     * 
     * 
     * / FIXXXXXXXXXXXX viewDailyAction.setEnabled(opened);
     * viewSpreadGraphAction.setEnabled(opened);
     * viewCourseGraphAction.setEnabled(opened);
     * viewFrequencyGraphAction.setEnabled(opened);
     * viewHbA1cAction.setEnabled(opened); readMeterAction.setEnabled(opened);
     * prefAction.setEnabled(opened); //s_dbH.setStatus();
     */

    // System.out.println("FIIIIIIIIIIIIIIIIIIIIIIXXXX this");
    // }
    private void close()
    {
        // write to prefs to file on close.
        // props.write();
        // dbH.disconnectDb();

        if (m_da != null)
        {
            if (m_da.getDb() != null)
                m_da.getDb().closeDb();

            DataAccess.deleteInstance();
        }

        dispose();
        System.exit(0);
    }

    /*
     * private JMenuItem addMenuItem(JMenu menu, Action action) { JMenuItem item
     * = menu.add(action);
     * 
     * //System.out.println(action.getValue(Action.ACCELERATOR_KEY));
     * 
     * KeyStroke keystroke = (KeyStroke)
     * action.getValue(Action.ACCELERATOR_KEY); if (keystroke != null)
     * item.setAccelerator(keystroke);
     * 
     * return item; }
     */

    /*
     * private void addToolBarSpacer() { toolBar.addSeparator(); }
     */

    private JButton addToolBarButton(Action action)
    {
        final JButton button = toolBar.add(action);

        button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(28, 28));

        return button;
    }

    /*
     * private JButton addToolBarButtonWithName(String cmd) { return
     * addToolBarButton(this.actions.get(cmd)); }
     */

    class GGCAction extends AbstractAction
    {

        /**
         * 
         */
        private static final long serialVersionUID = -1022459758999093522L;

        GGCAction(String name, String command)
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            if (m_ic.hasMnemonic(name))
            {
                char ch = m_ic.getMnemonic(name);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch,
                        Event.CTRL_MASK));
            }
            else
            {
                putValue(ACCELERATOR_KEY, null);
            }

            if (command != null)
                putValue(ACTION_COMMAND_KEY, command);

            command = name;
        }

        GGCAction(String name, String tooltip, String command)
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            // char ch = m_ic.getMnemonic(name);

            // System.out.println("Char ch: '" + ch + "'");

            // if ((ch != '0') || (ch != ' '))
            if (m_ic.hasMnemonic(name))
            {
                char ch = m_ic.getMnemonic(name);

                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch,
                        Event.CTRL_MASK));
                // System.out.println("Found");
            }
            else
            {
                putValue(ACCELERATOR_KEY, null);
                // System.out.println("NOT Found");
            }

            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, m_ic.getMessage(tooltip));

            if (command != null)
                putValue(ACTION_COMMAND_KEY, command);
        }

        public String getName()
        {
            return (String) getValue(Action.NAME);
        }

        public void actionPerformed(ActionEvent e)
        {

            String command = e.getActionCommand();

            if (command.startsWith("meters_"))
            {
                m_da.getPlugIn(DataAccess.PLUGIN_METERS).actionPerformed(e);
            }
            else if (command.startsWith("pumps_"))
            {
                m_da.getPlugIn(DataAccess.PLUGIN_PUMPS).actionPerformed(e);
            }
            else if (command.startsWith("cgms_"))
            {
                m_da.getPlugIn(DataAccess.PLUGIN_CGMS).actionPerformed(e);
            }
            else if (command.equals("file_quit"))
            {
                close();
            }
            else if (command.equals("view_daily"))
            {
                new DailyStatsDialog(m_da);
            }
            else if (command.equals("view_course"))
            {
                new CourseGraphDialog(m_da);
            }
            else if (command.equals("view_spread"))
            {
                new SpreadGraphDialog(m_da);
            }
            else if (command.equals("view_freq"))
            {
                new FrequencyGraphDialog(m_da);
            }
            else if (command.equals("view_hba1c"))
            {
                new HbA1cDialog(m_da);
            }
            else if (command.equals("tools_pref"))
            {
                PropertiesDialog pd = new PropertiesDialog(m_da);

                if (pd.wasOKAction())
                {
                    informationPanel.invalidatePanelsConstants();
                    informationPanel.refreshPanels();
                }
            }
            else if (command.equals("hlp_about"))
            {
                new AboutGGCDialog(getMyParent());
            }
            else if (command.equals("hlp_check_update"))
            {
                // TODO: Should be in DataAccess
                UpdateConfiguration uconf = new UpdateConfiguration();

                UpdateDialog ud = new UpdateDialog((JFrame) MainFrame.this,
                        uconf, m_da);
                ud.enableHelp("pages.GGC_Tools_Update");
                ud.showDialog();
            }
            else if (command.equals("hlp_help"))
            {
                m_da.getHelpContext().getDisplayHelpFromSourceInstance()
                        .actionPerformed(e);
            }
            /*
             * else if (command.equals("hlp_check")) { new
             * VersionChecker().checkForUpdate(); }
             */
            else if (command.equals("food_nutrition_1"))
            {
                new NutritionTreeDialog(m_da, GGCTreeRoot.TREE_USDA_NUTRITION);
            }
            else if (command.equals("food_nutrition_2"))
            {
                new NutritionTreeDialog(m_da, GGCTreeRoot.TREE_USER_NUTRITION);
            }
            else if (command.equals("food_meals"))
            {
                new NutritionTreeDialog(m_da, GGCTreeRoot.TREE_MEALS);
            }
            else if (command.equals("tools_db_backup"))
            {
                new BackupDialog(MainFrame.this, m_da);
            }
            else if (command.equals("tools_db_restore"))
            {
                RestoreGGCSelectorDialog rsd = new RestoreGGCSelectorDialog(MainFrame.this, m_da);
                rsd.showDialog();
            }
            else if (command.equals("report_pdf_simple"))
            {
                new PrintingDialog(MainFrame.this, 1);
            }
            else if (command.equals("report_pdf_extended"))
            {
                new PrintingDialog(MainFrame.this, 2);
            }
            else if (command.equals("view_ratio"))
            {
                new RatioDialog(getMyParent());
            }
            else if (command.equals("doc_docs"))
            {
                if (MainFrame.developer_version)
                {
                    new DoctorsDialog(MainFrame.this);
                }
                else
                    featureNotImplemented(command, "0.5");

            }
            else if (command.equals("doc_appoint"))
            {
                if (MainFrame.developer_version)
                {
                    new AppointmentsDialog(MainFrame.this);
                }
                else
                    featureNotImplemented(command, "0.5");

            }
            else // if ((command.equals("report_pdf_extended")) ||
            if ((command.equals("doc_stocks"))
                    || (command.equals("file_loginx"))
                    || (command.equals("file_logout")))
            {
                featureNotImplemented(command, "0.5");
            }
            else if ((command.equals("misc_synchronize")))
            {
                featureNotImplemented(command, "0.6");
            }
            else if ((command.equals("test")))
            {
                BolusHelper bh = new BolusHelper(MainFrame.this);
                //featureNotImplemented(command, "0.6");
            }
            /*
             * else if ((command.equals("pumps_read")) ||
             * (command.equals("pumps_list")) ||
             * (command.equals("pumps_profile")) ||
             * (command.equals("pumps_manual_entry")) ||
             * (command.equals("pumps_additional_data")) ||
             * (command.equals("pumps_config")) ) {
             * featureNotImplemented(command, "0.5 " + m_ic.getMessage("OR") +
             * " 0.6"); }
             */
            else if (command.equals("file_login"))
            {
                // ggc.gui.ReadMeterDialog rm = new
                // ggc.gui.ReadMeterDialog(MainFrame.this);

                System.out.println("In login");
/*
                try
                {
                    throw new Exception("Test Exception");
                }
                catch (Exception ex)
                {
                    System.out.println("we falled into exception");
                    m_da.createErrorDialog("MainFrame", "", ex,
                            "Exception in mainframe.");
                }
*/
            }
            else
                System.out.println("Unknown Command: " + command);

        }
    }

    public void featureNotImplemented(String cmd, String version)
    {
        String text = m_ic.getMessage("FEATURE");

        text += " '" + this.actions.get(cmd).getName() + "' ";
        text += String.format(m_ic.getMessage("IMPLEMENTED_VERSION"), version);
        text += "!";

        JOptionPane.showMessageDialog(MainFrame.this, text, m_ic
                .getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);

    }

    private class CloseListener extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            close();
        }
    }

    public String toString()
    {
        return "MainFrame";
    }

}