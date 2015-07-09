package ggc.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.graphics.dialogs.guilist.GUIListDialog;
import com.atech.graphics.graphs.GraphViewer;
import com.atech.help.HelpContext;
import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.refresh.EventObserverInterface;
import com.atech.plugin.PlugInClient;
import com.atech.update.client.UpdateDialog;
import com.atech.utils.ATSwingUtils;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

import ggc.GGC;
import ggc.core.data.graph.GraphViewCourse;
import ggc.core.data.graph.GraphViewSpread;
import ggc.core.db.GGCDbLoader;
import ggc.core.db.tool.transfer.BackupDialog;
import ggc.core.db.tool.transfer.RestoreGGCSelectorDialog;
import ggc.core.plugins.GGCPluginType;
import ggc.core.util.DataAccess;
import ggc.core.util.RefreshInfo;
import ggc.gui.dialogs.*;
import ggc.gui.dialogs.stock.def.StockListDef;
import ggc.gui.panels.info.InfoPanel;
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

public class MainFrame extends JFrame implements EventObserverInterface, ActionListener
{

    private static final long serialVersionUID = -8971779470148201332L;
    private static Log log = LogFactory.getLog(MainFrame.class);

    /**
     * Skin Look and Feel
     */
    public static SkinLookAndFeel s_skinlf;

    /**
     * Developer version 
     */
    public static boolean developer_version = false;

    /**
     * Menu Bar
     */
    private JMenuBar menuBar = new JMenuBar();

    /**
     * Tool Bars
     */
    private Hashtable<GGCToolbarType, JToolBar> toolbars = null;

    private DataAccess m_da = null;
    private static final String skinLFdir = "../data/skinlf_themes/";
    private I18nControlAbstract m_ic = null;

    private Map<String, JMenu> menus = null;
    private Map<String, JMenuItem> actions = null;

    private Map<GGCToolbarType, Map<String, JButton>> toolbarItems = new HashMap<GGCToolbarType, Map<String, JButton>>();

    // private Hashtable<String, JMenuItem> toolbar_pen_items = null;
    // private Hashtable<String, JMenuItem> toolbar_pump_items = null;
    private GGCToolbarType current_toolbar = GGCToolbarType.None;
    private String next_version = "0.7";

    /**
     * Status panels
     */
    public StatusBar statusPanel;

    /**
     * Information panels
     */
    public InfoPanel informationPanel;

    /**
     * Static definitions (Look and Feel)
     */
    static
    {
        MainFrame.setLookAndFeel();
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


    /**
     * Constructor
     * 
     * @param title
     * @param developer_version
     */
    public MainFrame(String title, boolean developer_version)
    {
        m_da = DataAccess.createInstance(this);

        m_ic = m_da.getI18nControlInstance();

        m_da.addComponent(this);

        m_da.setDeveloperMode(developer_version);

        statusPanel = new StatusBar(this);

        this.actions = new Hashtable<String, JMenuItem>();
        MainFrame.developer_version = developer_version;

        String title_full = "  GGC - GNU Gluco Control (" + GGC.full_version + ")";

        if (developer_version)
        {
            title_full += " - Developer edition";
        }

        setTitle(title_full);

        this.setIconImage(ATSwingUtils.getImageIcon_22x22("diabetesbluecircle.png", this, m_da).getImage());

        setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        helpInit();

        // menus, toolbars
        createMenus();
        createToolBars();
        this.setSoftwareMode();
        m_da.addObserver(DataAccess.OBSERVABLE_STATUS, this);

        // getContentPane().add(this.toolbars.get("TOOLBAR_PEN"),
        // BorderLayout.NORTH);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        m_da.startDb();

        statusPanel.setStatusMessage(m_ic.getMessage("INIT"));

        // information panel
        informationPanel = new InfoPanel();
        getContentPane().add(informationPanel, BorderLayout.CENTER);

        setMenusByDbLoad(StatusBar.DB_STOPPED);

        m_da.startToObserve();
        this.setVisible(true);

    }


    private void setSoftwareMode()
    {
        setSoftwareMode(false);
    }


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

        if (m_da.isPumpMode())
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


    /**
     * Get Software Mode
     * 
     * @return
     */
    public String getSoftwareMode()
    {
        return " [" + m_ic.getMessage(m_da.getSoftwareModeDescription()) + "]";
    }


    // ------------------------------------------------------
    // -- Menus
    // ------------------------------------------------------

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

        // bgs menu
        menux = this.createMenu("MN_DATA", null);
        this.createMenuItem(menux, "MN_DAILY", "MN_DAILY_DESC", "view_daily", "calendar.png");
        menux.addSeparator();

        menuxsub = this.createMenu(menux, "MN_DATA_GRAPH", null);
        this.createMenuItem(menuxsub, "MN_COURSE", "MN_COURSE_DESC", "view_course", "line-chart.png");
        this.createMenuItem(menuxsub, "MN_SPREAD", "MN_SPREAD_DESC", "view_spread", "dot-chart.png");
        this.createMenuItem(menuxsub, "MN_FREQUENCY", "MN_FREQUENCY_DESC", "view_freq", "column-chart.png");

        menux.addSeparator();
        this.createMenuItem(menux, "MN_HBA1C", "MN_HBA1C_DESC", "view_hba1c", "pie-chart.png");

        addRatioMenu(menux);

        this.menus.put("MENU_PEN", menux);

        // doctors menu
        menux = this.createMenu("MN_DOCTOR", null);
        this.createMenuItem(menux, "MN_DOCS", "MN_DOCS_DESC", "doc_docs", null);
        menux.addSeparator();
        this.createMenuItem(menux, "MN_APPOINT", "MN_APPOINT_DESC", "doc_appoint", null);
        menux.addSeparator();
        this.createMenuItem(menux, "MN_STOCKS", "MN_STOCKS_DESC", "doc_stocks", null);

        this.menus.put("MENU_DOCTOR", menux);

        // reports menu
        menux = this.createMenu("MN_PRINTING", null);

        menuxsub = this.createMenu(menux, "MN_REPORTS", "MN_REPORTS_DESC");
        this.createMenuItem(menuxsub, "MN_PDF_SIMPLE", "MN_PDF_SIMPLE_DESC", "report_pdf_simple", "print.png");
        this.createMenuItem(menuxsub, "MN_PDF_EXT", "MN_PDF_EXT_DESC", "report_pdf_extended", "print.png");

        this.menus.put("MENU_PRINT", menux);

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
        menux.add(m_da.getHelpContext().getHelpItem());
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
        // System.out.println("Refresh Menus: ");
        this.menuBar.removeAll();

        this.menuBar.add(this.menus.get("MENU_FILE"));
        this.menuBar.add(this.menus.get("MENU_PEN"));

        JMenu menu = getPlugInMenu(GGCPluginType.NutritionToolPlugin);

        if (menu != null)
        {
            this.menuBar.add(menu);
        }

        // doctors menu
        this.menuBar.add(this.menus.get("MENU_DOCTOR"));

        if (refresh_init)
        {
            // reports menu
            for (Enumeration<String> en = m_da.getPlugins().keys(); en.hasMoreElements();)
            {
                String key = en.nextElement();

                PlugInClient pic = m_da.getPlugIn(key);

                if (pic.getPlugInPrintMenus() != null)
                {
                    JMenu[] menus = pic.getPlugInPrintMenus();

                    for (JMenu menu2 : menus)
                    {
                        this.menus.get("MENU_PRINT").add(menu2);
                    }
                }
            }
        }

        this.menuBar.add(this.menus.get("MENU_PRINT"));

        GGCPluginType[] keys = { GGCPluginType.MeterToolPlugin, GGCPluginType.PumpToolPlugin,
                                GGCPluginType.CGMSToolPlugin, };

        for (GGCPluginType key : keys)
        {
            if (m_da.isPluginAvailable(key))
            {
                PlugInClient pic = m_da.getPlugIn(key);

                if (pic.getPlugInMainMenu() != null)
                {
                    JMenu menuTmp = pic.getPlugInMainMenu();

                    if (key == GGCPluginType.PumpToolPlugin)
                    {
                        addRatioMenu(menuTmp);
                    }

                    this.menuBar.add(menuTmp);
                }
            }
        }

        this.menuBar.add(this.menus.get("MENU_TOOLS"));
        this.menuBar.add(this.menus.get("MENU_HELP"));

        this.setJMenuBar(this.menuBar);
    }


    private void addRatioMenu(JMenu menuParent)
    {
        menuParent.addSeparator();

        JMenu menuxsub = this.createMenu(menuParent, "MN_DATA_RATIO", null);
        this.createMenuItem(menuxsub, "MN_RATIO_BASE", "MN_RATIO_BASE_DESC", "ratio_base", null);
        this.createMenuItem(menuxsub, "MN_RATIO_EXTENDED", "MN_RATIO_EXTENDED_DESC", "ratio_extended", null);
    }


    private JMenu createMenu(String name, String tool_tip)
    {
        JMenu item = new JMenu(m_ic.getMessageWithoutMnemonic(name));
        item.setMnemonic(m_ic.getMnemonic(name));

        if (tool_tip != null)
        {
            item.setToolTipText(tool_tip);
        }

        this.menuBar.add(item);

        return item;
    }


    private JMenu createMenu(JMenu parent, String name, String tool_tip)
    {
        JMenu item = new JMenu(m_ic.getMessageWithoutMnemonic(name));
        item.setMnemonic(m_ic.getMnemonic(name));

        if (tool_tip != null)
        {
            item.setToolTipText(m_ic.getMessage(tool_tip));
        }

        parent.add(item);

        return item;
    }


    // ------------------------------------------------------
    // -- Toolbars
    // ------------------------------------------------------

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

        this.createToolbarButton("MN_DAILY", "MN_DAILY_DESC", "view_daily", "calendar.png", GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_COURSE", "MN_COURSE_DESC", "view_course", "line-chart.png",
            GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_SPREAD", "MN_SPREAD_DESC", "view_spread", "dot-chart.png",
            GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_FREQUENCY", "MN_FREQUENCY_DESC", "view_freq", "column-chart.png",
            GGCToolbarType.PenInjection);
        this.createToolbarButton("MN_HBA1C", "MN_HBA1C_DESC", "view_hba1c", "pie-chart.png",
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
        this.createToolbarButton("MN_HBA1C", "MN_HBA1C_DESC", "view_hba1c", "pie-chart.png", GGCToolbarType.Pump);
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
     * @param status
     */
    public void setToolbarByDbLoad(int status)
    {
        setToolBarItemEnabled("file_login", false);

        if (status == StatusBar.DB_STOPPED)
        {
            // this.createToolbarButton("MN_LOGIN", "MN_LOGIN_DESC",
            // "file_login", "logon.png");
            setToolBarItemEnabled("view_daily", false);
            setToolBarItemEnabled("view_course", false);
            setToolBarItemEnabled("view_spread", false);
            setToolBarItemEnabled("view_freq", false);
            setToolBarItemEnabled("view_hba1c", false);

            // setToolBarItemEnabled("food_meals", false);

            setToolBarItemEnabled("report_pdf_simple", false);

            setToolBarItemEnabled("tools_pref", false);
        }
        else if (status == StatusBar.DB_INIT_DONE)
        {
            setToolBarItemEnabled("view_daily", true);
            setToolBarItemEnabled("view_course", true);
            setToolBarItemEnabled("view_spread", true);
            setToolBarItemEnabled("view_freq", true);
            setToolBarItemEnabled("view_hba1c", true);

            setToolBarItemEnabled("report_pdf_simple", true);
        }
        else if (status == StatusBar.DB_BASE_DONE)
        {
            setToolBarItemEnabled("tools_pref", true);
        }
        else if (status == StatusBar.DB_LOADED)
        {
            // this.toolbar_items.get("food_meals").setEnabled(true);
        }

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
        button.setName(m_ic.getMessageWithoutMnemonic(name));

        if (toolTip != null)
        {
            button.setToolTipText(m_ic.getMessage(toolTip));
        }

        if (actionCommand != null)
        {
            button.setActionCommand(actionCommand);
        }

        if (iconSmall != null)
        {
            button.setIcon(ATSwingUtils.getImageIcon(iconSmall, 28, 28, this, m_da));
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
        JMenuItem item = new JMenuItem(m_ic.getMessageWithoutMnemonic(name));
        // item.setName(m_ic.getMessageWithoutMnemonic(name));

        if (m_ic.hasMnemonic(name))
        {
            char ch = m_ic.getMnemonic(name);
            item.setAccelerator(KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));
        }

        if (toolTip != null)
        {
            item.setToolTipText(m_ic.getMessage(toolTip));
        }

        if (actionCommand != null)
        {
            item.setActionCommand(actionCommand);
            item.addActionListener(this);
        }

        if (iconSmall != null)
        {
            item.setIcon(ATSwingUtils.getImageIcon(iconSmall, 15, 15, this, m_da));
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


    // private void createAction_Old(JMenu menu, String name, String tip, String
    // action_command, String icon_small)
    // {
    // GGCAction action = new GGCAction(name, tip, action_command);
    //
    // if (icon_small != null)
    // {
    // action.putValue(Action.SMALL_ICON, ATSwingUtils.getImageIcon(icon_small,
    // 15, 15, this, dataAccess));
    // // new ImageIcon(getClass().getResource("/icons/" + icon_small)));
    // // action.putValue(Action.LARGE_ICON_KEY, new
    // // ImageIcon(getClass().getResource("/icons/" + icon_small)));
    // }
    //
    // if (menu != null)
    // {
    // menu.add(action);
    // }
    //
    // // this.actions.put(action_command, item);
    //
    // // return action;
    // }

    /**
     * Set menus by Db Loading status
     * 
     * @param status
     */
    public void setMenusByDbLoad(int status)
    {
        if (status == StatusBar.DB_STOPPED)
        {
            // bgs menu
            this.menus.get("MENU_PEN").setEnabled(false);
            this.actions.get("view_daily").setEnabled(false);
            this.actions.get("view_course").setEnabled(false);
            this.actions.get("view_spread").setEnabled(false);
            this.actions.get("view_freq").setEnabled(false);
            this.actions.get("view_hba1c").setEnabled(false);

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
            this.actions.get("view_daily").setEnabled(true);
            this.actions.get("view_course").setEnabled(true);
            this.actions.get("view_spread").setEnabled(true);
            this.actions.get("view_freq").setEnabled(true);
            this.actions.get("view_hba1c").setEnabled(true);

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
            setSoftwareMode();
        }

        setToolbarByDbLoad(status);
    }


    // ------------------------------------------------------
    // -- Help
    // ------------------------------------------------------

    private void helpInit()
    {
        log.debug("JavaHelp - START");

        // System.out.println("Help. Selected language: " +
        // dataAccess.getLanguageManager().getSelectedLanguage());

        HelpContext hc = m_da.getHelpContext();

        log.debug("JavaHelp - HelpContext: " + hc);

        JMenuItem helpItem = new JMenuItem(m_ic.getMessage("HELP") + "...");
        helpItem.setIcon(new ImageIcon(getClass().getResource("/icons/help.gif")));
        hc.setHelpItem(helpItem);

        String mainHelpSetName = "/" + m_da.getLanguageManager().getHelpSet();

        log.debug("JavaHelp - MainHelpSetName: " + mainHelpSetName);

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
                log.error("HelpSet " + mainHelpSetName + " could not be opened.", ex);
            }

            HelpBroker main_help_broker = null;

            if (main_help_set != null)
            {
                log.debug("JavaHelp - Main Help Set present, creating broker.");
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

        log.debug("JavaHelp - END");
    }


    private void helpInit_Old()
    {
        // HelpContext hc = new HelpContext("../data/help/GGC.hs");
        // dataAccess.setHelpContext(hc);
        boolean help_debug = true;

        if (help_debug)
        {
            System.out.println("Help Init");
        }

        System.out.println("Help. Selected language: " + m_da.getLanguageManager().getSelectedLanguage());

        // String selected_language =
        // dataAccess.getLanguageManager().getSelectedLanguage();
        // String defaultHelp =
        // dataAccess.getLanguageManager().getDefaultHelp();

        HelpContext hc = m_da.getHelpContext();

        JMenuItem helpItem = new JMenuItem(m_ic.getMessage("HELP") + "...");
        helpItem.setIcon(new ImageIcon(getClass().getResource("/icons/help.gif")));
        hc.setHelpItem(helpItem);

        // String mainHelpSetName = "../data/help/en/GGC.hs"; // added en

        String mainHelpSetName = "../data/" + m_da.getLanguageManager().getHelpSet();

        mainHelpSetName = mainHelpSetName.replace("/", File.separator);

        hc.setMainHelpSetName(mainHelpSetName);

        // try to find the helpset and create a HelpBroker object
        if (hc.getMainHelpBroker() == null)
        {

            if (help_debug)
            {
                System.out.println("Help init broker");
            }

            HelpSet main_help_set = null;
            // HelpContext.mainHelpSet = null;

            // X ClassLoader cl = MainFrame.class.getClassLoader();
            // String help_url = "jar:file:pis_lang-0.1.jar!/help/PIS.hs";

            // String help_url = "jar:file:ggc_help-0.1.jar!/help/en/GGC.hs";

            String help_url = "jar:file:ggc_help-0.1.jar!/" + m_da.getLanguageManager().getHelpSet();

            System.out.println("URL: " + help_url);

            try
            {
                URL hsURL = new URL(help_url);

                // if (hsURL == null)
                // System.out.println("HelpSet " + help_url + " not found.");
                // else
                main_help_set = new HelpSet(null, hsURL);
            }
            catch (HelpSetException ee)
            {
                System.out.println("HelpSet " + help_url + " could not be opened.");
                System.out.println(ee.getMessage());
            }
            catch (MalformedURLException ee)
            {
                System.out.println("Problem with HelpSet path: " + help_url + "\n" + ee);
            }

            HelpBroker main_help_broker = null;

            if (main_help_set != null)
            {
                log.debug("Help: Main Help Set present, creating broker");
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


    private void close()
    {
        m_da.getSettings().save();

        if (m_da != null)
        {
            if (m_da.getDb() != null)
            {
                m_da.getDb().closeDb();
            }

            DataAccess.deleteInstance();
        }

        dispose();
        System.exit(0);
    }


    /*
     * private JMenuItem addMenuItem(JMenu menu, Action action) { JMenuItem item
     * = menu.add(action);
     * //System.out.println(action.getCode(Action.ACCELERATOR_KEY));
     * KeyStroke keystroke = (KeyStroke)
     * action.getCode(Action.ACCELERATOR_KEY); if (keystroke != null)
     * item.setAccelerator(keystroke);
     * return item; }
     */

    /*
     * private void addToolBarSpacer() { toolBar.addSeparator(); }
     */

    // private JButton addToolBarButton(Action action, GGCToolbarType
    // toolbarType)
    // {
    // final JButton button;
    //
    // if (toolbarType == GGCToolbarType.PenInjection)
    // {
    // button = this.toolbars.get("TOOLBAR_PEN").add(action);
    // }
    // else
    // {
    // button = this.toolbars.get("TOOLBAR_PUMP").add(action);
    // }
    //
    // button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    // button.setFocusPainted(false);
    // button.setPreferredSize(new Dimension(28, 28));
    //
    // return button;
    // }

    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();

        if (command.startsWith("meters_"))
        {
            m_da.getPlugIn(GGCPluginType.MeterToolPlugin).actionPerformed(e);
        }
        else if (command.startsWith("pumps_") || command.startsWith("report_print_pump"))
        {
            m_da.getPlugIn(GGCPluginType.PumpToolPlugin).actionPerformed(e);
        }
        else if (command.startsWith("cgms_"))
        {
            m_da.getPlugIn(GGCPluginType.CGMSToolPlugin).actionPerformed(e);
        }
        else if (command.startsWith("food_"))
        {
            m_da.getPlugIn(GGCPluginType.NutritionToolPlugin).actionPerformed(e);
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
            new GraphViewer(new GraphViewCourse(), m_da, MainFrame.this, true);
        }
        else if (command.equals("view_spread"))
        {
            new GraphViewer(new GraphViewSpread(), m_da, MainFrame.this, true);
        }
        else if (command.equals("view_freq"))
        {
            // new FrequencyGraphDialog(dataAccess);
            featureNotImplementedDescription(m_ic.getMessage("FREQGRAPHFRAME"), next_version);
        }
        else if (command.equals("view_hba1c"))
        {
            new HbA1cDialog(m_da);
        }
        else if (command.equals("tools_pref"))
        {
            PropertiesDialog pd = new PropertiesDialog(m_da);

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
            UpdateDialog ud = new UpdateDialog(MainFrame.this, m_da);
            ud.enableHelp("GGC_Tools_Update");
            ud.showDialog();
        }
        else if (command.equals("hlp_help"))
        {
            m_da.getHelpContext().getDisplayHelpFromSourceInstance().actionPerformed(e);
        }
        else if (command.equals("ratio_base"))
        {
            new RatioBaseDialog(MainFrame.this);
        }
        else if (command.equals("ratio_extended"))
        {
            new RatioExtendedDialog(MainFrame.this);
        }

        /*
         * else if (command.equals("hlp_check")) { new
         * VersionChecker().checkForUpdate(); }
         */

        else if (command.equals("tools_db_backup"))
        {
            new BackupDialog(MainFrame.this, m_da);
        }
        else if (command.equals("tools_db_restore"))
        {
            RestoreGGCSelectorDialog rsd = new RestoreGGCSelectorDialog(MainFrame.this, m_da);
            rsd.showDialog();

            // update main panels
            DataAccess.getInstance().loadDailySettings(new GregorianCalendar(), true);
            m_da.setChangeOnEventSource(DataAccess.OBSERVABLE_PANELS, RefreshInfo.PANEL_GROUP_ALL_DATA);
        }
        else if (command.equals("report_pdf_simple"))
        {
            new PrintingDialog(MainFrame.this, 1, PrintingDialog.PRINT_DIALOG_YEAR_MONTH_OPTION);
        }
        else if (command.equals("report_pdf_extended"))
        {
            new PrintingDialog(MainFrame.this, 2, PrintingDialog.PRINT_DIALOG_YEAR_MONTH_OPTION);
        }
        /*
         * else if (command.equals("report_foodmenu_simple"))
         * {
         * new PrintingDialog(MainFrame.this, 1,
         * PrintingDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
         * }
         * else if (command.equals("report_foodmenu_ext1"))
         * {
         * new PrintingDialog(MainFrame.this, 2,
         * PrintingDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
         * }
         * else if (command.equals("report_foodmenu_ext2"))
         * {
         * new PrintingDialog(MainFrame.this, 3,
         * PrintingDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
         * }
         */
        /*
         * else if (command.equals("report_foodmenu_ext3"))
         * {
         * // disabled for now, until it's implement to fully function
         * new PrintingDialog(MainFrame.this, 4,
         * PrintingDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
         * }
         */
        /*
         * else if (command.equals("view_ratio"))
         * {
         * new RatioDialog(getMyParent());
         * }
         */
        else if (command.equals("doc_docs"))
        {
            if (MainFrame.developer_version)
            {
                new DoctorsDialog(MainFrame.this);
            }
            else
            {
                featureNotImplemented(command, "0.7");
            }

        }
        else if (command.equals("doc_appoint"))
        {
            if (MainFrame.developer_version)
            {
                new AppointmentsDialog(MainFrame.this);
            }
            else
            {
                featureNotImplemented(command, "0.7");
            }

        }
        else // if ((command.equals("report_pdf_extended")) ||
        if (command.equals("file_login") || command.equals("report_foodmenu_ext3") || command.equals("file_logout"))
        {
            featureNotImplemented(command, "0.7");
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
                new GUIListDialog(MainFrame.this, new StockListDef(), m_da);
            }
            else
            {
                featureNotImplemented(command, "0.6");
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
        else if (command.equals("file_login"))
        {
            // ggc.gui.ReadMeterDialog rm = new
            // ggc.gui.ReadMeterDialog(MainFrame.this);

            // System.out.println("In login");
            /*
             * try
             * {
             * throw new Exception("Test Exception");
             * }
             * catch (Exception ex)
             * {
             * System.out.println("we falled into exception");
             * dataAccess.createErrorDialog("MainFrame", "", ex,
             * "Exception in mainframe.");
             * }
             */
        }
        else
        {
            System.out.println("Unknown Command: " + command);
        }

    }


    /**
     * Invalidate panels
     */
    public void invalidatePanels()
    {
        this.informationPanel.invalidatePanelsConstants();
    }


    /**
     * Refresh panels
     */
    public void refreshPanels()
    {
        this.informationPanel.refreshPanels();
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
    // // setName(m_ic.getMessageWithoutMnemonic(name));
    // //
    // // putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));
    // //
    // // if (m_ic.hasMnemonic(name))
    // // {
    // // char ch = m_ic.getMnemonic(name);
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
    // setName(m_ic.getMessageWithoutMnemonic(name));
    //
    // putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));
    //
    // // char ch = m_ic.getMnemonic(name);
    //
    // // System.out.println("Char ch: '" + ch + "'");
    //
    // // if ((ch != '0') || (ch != ' '))
    // if (m_ic.hasMnemonic(name))
    // {
    // char ch = m_ic.getMnemonic(name);
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
    // putValue(SHORT_DESCRIPTION, m_ic.getMessage(tooltip));
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

    private void featureNotImplemented(String cmd, String version)
    {
        String text = m_ic.getMessage("FEATURE");

        text += " '" + this.actions.get(cmd).getName() + "' ";
        text += String.format(m_ic.getMessage("IMPLEMENTED_VERSION"), version);
        text += "!";

        JOptionPane.showMessageDialog(MainFrame.this, text, m_ic.getMessage("INFORMATION"),
            JOptionPane.INFORMATION_MESSAGE);

    }


    private void featureNotImplementedDescription(String desc, String version)
    {
        String text = m_ic.getMessage("FEATURE");

        text += " '" + desc + "' ";
        text += String.format(m_ic.getMessage("IMPLEMENTED_VERSION"), version);
        text += "!";

        JOptionPane.showMessageDialog(MainFrame.this, text, m_ic.getMessage("INFORMATION"),
            JOptionPane.INFORMATION_MESSAGE);

    }

    private class CloseListener extends WindowAdapter
    {

        @Override
        public void windowClosing(WindowEvent e)
        {
            close();
        }
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

    boolean title_set = false;


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

            if (m_da.getDbLoadingStatus() >= GGCDbLoader.DB_DATA_BASE)
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
    }


    /**
     * This refresh is used when configuration of plugin changes
     */
    public void refreshMenusPlugins()
    {
        this.refreshMenus(false);
    }


    private JMenu getPlugInMenu(GGCPluginType name)
    {
        if (m_da.isPluginAvailable(name))
        {
            PlugInClient pic = m_da.getPlugIn(name);
            return pic.getPlugInMainMenu();
        }
        else
            return null;
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
