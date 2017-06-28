package ggc.gui.little;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.observe.EventObserverInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.java.VersionResolver;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.defs.DatabaseStatusType;
import ggc.core.data.defs.GGCObservableType;
import ggc.core.data.defs.RefreshInfoType;
import ggc.core.db.GGCDb;
import ggc.core.gui.GGCGuiHelper;
import ggc.core.plugins.GGCPluginType;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.AboutGGCDialog;
import ggc.gui.little.panels.DailyStatsListPanelL;
import ggc.gui.little.panels.MainLittlePanel;
import ggc.gui.main.StatusBar;
import ggc.gui.pen.DailyRowDialog;

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
 *  Filename:     GGCLittle  
 *  Description:  This is small version of GGC application, intended for quick adding to 
 *      database, for example when you make measurement or take insulin and you don't 
 *      need the whole application.
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCLittle extends JFrame implements ActionListener, EventObserverInterface
{

    private static final long serialVersionUID = -7744922647111948669L;
    private static Logger LOG = LoggerFactory.getLogger(GGCLittle.class);

    private GGCGuiHelper guiHelper; // = new GGCGuiHelper(LOG);

    private I18nControlAbstract i18nControl = null; // I18nControl.getInstance();
    private DataAccess dataAccess = null;
    private GGCDb m_db = null;

    // private static SkinLookAndFeel s_skinlf;
    // private static final String skinLFdir = "../data/skinlf_themes/";
    private Map<String, JMenuItem> actions = null;

    // GUI
    private MainLittlePanel informationPanel = null;
    private StatusBar statusPanel;
    private DailyStatsListPanelL dailyStats = null;

    // fields
    private JToolBar toolBar = new JToolBar();
    // private GGCAction readMeterAction, quitAction, aboutAction;

    // public DailyValues dayData;

    GregorianCalendar gc_current = null;

    boolean is_visible = true;

    String ggc_little_version = "v0.2.3";

    /**
     * Static definitions (Look and Feel)
     */
    static
    {
        GGCGuiHelper.setLog(LOG);
        GGCGuiHelper.setLookAndFeel();
        DataAccess.getSkinManager().setSkinLfOverrides(GGCGuiHelper.getSkinLfOverrides());
    }


    /**
     * Constructor 
     * 
     * @param developer_version
     */
    public GGCLittle(boolean developer_version)
    {
        dataAccess = DataAccess.createInstance(this);
        i18nControl = dataAccess.getI18nControlInstance();
        m_db = dataAccess.getDb();

        guiHelper = new GGCGuiHelper();
        guiHelper.setDataAccess(dataAccess);

        this.actions = new Hashtable<String, JMenuItem>();

        statusPanel = new StatusBar();

        String title = i18nControl.getMessage("GGC_LITTLE_TITLE");

        ggc_little_version = "v"
                + VersionResolver.getVersion("ggc.gui.little.data.Version", this.getClass().getSimpleName());

        setTitle(title + " (" + ggc_little_version + ")");

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });

        createToolbar();
        createSystemTrayInstance();

        /*
         * JMenuBar jmb = new JMenuBar(); this.setJMenuBar(jmb);
         * quitAction = new GGCAction("MN_QUIT", "MN_QUIT_DESC", "file_quit");
         * jmb.add(new JMenuItem(quitAction));
         */

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        guiHelper.helpInit();
        guiHelper.setApplicationIcon(this);

        dataAccess.getObserverManager().addObserver(GGCObservableType.Status, this);
        dataAccess.getObserverManager().addObserver(GGCObservableType.Database, this);

        // dataAccess.addObserver(observable_id, inst)

        dataAccess.getObserverManager().startToObserve();

        dataAccess.startDb(); // statusPanel);
        setDbActions(false);
        statusPanel.setStatusMessage(i18nControl.getMessage("INIT"));

        // dayData = DataAccess.getInstance().getDayStats(new
        // GregorianCalendar());

        gc_current = new GregorianCalendar();
        informationPanel = new MainLittlePanel(this);

        dailyStats = informationPanel.dailyStats;

        getContentPane().add(informationPanel, BorderLayout.CENTER);
        this.setBounds(100, 100, 800, 600);

        this.getRootPane().addComponentListener(new ComponentAdapter()
        {

            public void componentResized(ComponentEvent e)
            {
                // This is only called when the user releases the mouse button.
                resizeEvent();
            }
        });

        // this.setResizable(false);
        this.setVisible(true);
    }


    private void resizeEvent()
    {
        double x = this.getBounds().getWidth() - 360;

        emptyLabelBeforeHelp.setSize((int) x, 30);
        emptyLabelBeforeHelp.setPreferredSize(new Dimension((int) x, 30));

        // emptyLabelBeforeHelp.getWidth()
        System.out.println("componentResized [" + this.getBounds().getWidth());
    }

    JLabel emptyLabelBeforeHelp;


    private void createToolbar()
    {

        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        JLabel label_empty = new JLabel("");
        label_empty.setPreferredSize(new Dimension(40, 20));

        this.toolBar.add(guiHelper.getEmptyLabel(10, 20));

        createToolbarButton("MN_QUIT", "MN_QUIT_LT_DESC", "quit", "exit.png");

        this.toolBar.add(guiHelper.getEmptyLabel(30, 20));

        createToolbarButton("TB_ADD_ROW", "TB_ADD_ROW_DESC", "add_entry", "table_add.png");
        createToolbarButton("TB_EDIT_ROW", "TB_EDIT_ROW_DESC", "edit_entry", "table_edit.png");
        createToolbarButton("TB_DELETE_ROW", "TB_DELETE_ROW_DESC", "delete_entry", "table_delete.png");

        this.toolBar.add(guiHelper.getEmptyLabel(30, 20));

        createToolbarButton("TB_SHOW_GRAPH", "TB_SHOW_GRAPH_DESC", "show_graph", "line-chart.png");

        // this.toolBar.add(getEmptyLabel(30,20));

        // createToolbarAction("MN_FROM_METER", "TB_READ_METER_LT_DESC",
        // "read_meter", "readmeter.gif");

        this.toolBar.add(emptyLabelBeforeHelp = guiHelper.getEmptyLabel(430, 20));

        createToolbarButton("MN_HELP", "TB_HELP_DESC", "hlp_help", "help.png");
        createToolbarButton("MN_ABOUT", "TB_ABOUT_DESC", "hlp_about", "about.png");

        // this.actions.get("read_meter").setEnabled(false);

        /*
         * readMeterAction = new GGCAction("MN_FROM_METER",
         * "MN_FROM_METER_DESC", "read_meter");
         * readMeterAction.putValue(Action.SMALL_ICON, new
         * ImageIcon(getClass().getResource("/icons/readmeter.gif")));
         * aboutAction = new GGCAction("MN_ABOUT", "MN_ABOUT_DESC",
         * "hlp_about"); aboutAction.putValue(Action.SMALL_ICON, new
         * ImageIcon(getClass().getResource("/icons/about.gif")));
         * toolBar.setFloatable(false); toolBar.setLayout(new
         * FlowLayout(FlowLayout.LEFT, 1, 1));
         * addToolBarSpacer(); addToolBarButton(readMeterAction);
         * JLabel l = new JLabel(""); l.setPreferredSize(new Dimension(510,20));
         * toolBar.add(l); addToolBarButton(aboutAction);
         */

    }


    private void createSystemTrayInstance()
    {

        /*
         * JPopupMenu menu = new JPopupMenu();
         * menu.add(createItem("SHOW_APP", "SHOW_APP_DESC", "show_app"));
         * menu.add(createItem("HIDE_APP", "HIDE_APP_DESC", "hide_app"));
         * menu.addSeparator(); menu.add(createItem("EXIT_APP", "EXIT_APP_DESC",
         * "exit_app")); menu.add(new JLabel()); menu.add(new JLabel());
         * SystemTray tray = SystemTray.getSystemTray();
         * TrayIcon ti = new TrayIcon(dataAccess.getImageIcon("medical_bag.png",
         * 15,
         * 15, this) , i18nControl.getMessage("GGC_LITTLE_TITLE"), menu);
         * ti.setIconAutoSize(true); ti.addActionListener(new ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * JOptionPane.showMessageDialog(null,
         * i18nControl.getMessage("GGC_LITTLE_TITLE"),
         * i18nControl.getMessage("ABOUT"),
         * JOptionPane.INFORMATION_MESSAGE); } }); tray.addTrayIcon(ti);
         */

    }

    /*
     * private JMenuItem createItem(String name, String desc, String action) {
     * JMenuItem item = new JMenuItem(i18nControl.getMessage(name));
     * item.setActionCommand(action);
     * //item.setName(i18nControl.getMessage(name));
     * item.setToolTipText(i18nControl.getMessage(desc));
     * item.addActionListener(this);
     * return item; }
     */

    // /**
    // * Get Status Panel
    // *
    // * @return
    // */
    // public StatusBarL getStatusPanel()
    // {
    // return this.statusPanel;
    // }


    /**
     * Get Information Panel
     * 
     * @return
     */
    public MainLittlePanel getInformationPanel()
    {
        return this.informationPanel;
    }


    private void createToolbarButton(String name, String toolTip, String actionCommand, String iconSmall
    /* , GGCToolbarType toolbarType */)
    {

        JButton button = new JButton();
        button.setName(i18nControl.getMessageWithoutMnemonic(name));

        if (toolTip != null)
        {
            button.setToolTipText(i18nControl.getMessage(toolTip));
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

        // this.toolbarItems.get(toolbarType).put(actionCommand, button);
        // this.toolbars.get(toolbarType).add(button);

        toolBar.add(button);

        button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(32, 32));

        // this.toolbars.get(toolbarType).add(button);

        // this.actions.put(action_command, action);

    }


    /**
     * Get My Parent
     * 
     * @return
     */
    public GGCLittle getMyParent()
    {
        return this;
    }


    /**
     * Set Db Actions
     * 
     * @param opened
     */
    public void setDbActions(boolean opened)
    {
        // readMeterAction.setEnabled(opened);
    }


    private void close()
    {
        // write to prefs to file on close.
        // props.write();
        dataAccess.getDb().closeDb();
        // s_dbH.disconnectDb();
        dispose();
        System.exit(0);
    }


    private boolean checkDateSame()
    {
        if (m_db == null)
        {
            this.m_db = dataAccess.getDb();
        }

        if (!dataAccess.isSameDay(this.gc_current, new GregorianCalendar()))
        {
            // not same date
            JOptionPane.showMessageDialog(getMyParent(), i18nControl.getMessage("CURRENT_DATE_HAS_CHANGED"),
                i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);

            this.dailyStats.refreshInfo();

            return false;
        }
        else
            return true;

    }


    /**
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {

        // FIXME add/edit/delete for both types

        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("show_app"))
        {
            if (is_visible)
            {
                this.toFront();
            }
            else
            {
                this.setVisible(true);
            }
        }
        else if (actionCommand.equals("hide_app"))
        {
            is_visible = false;
            this.setVisible(false);

        }
        else if (actionCommand.equals("exit_app"))
        {
            close();
        }
        else if (actionCommand.equals("quit"))
        {
            close();
        }
        else if (actionCommand.equals("add_entry"))
        {
            if (checkIfPumpAndDisplayError("Adding entry"))
            {
                return;
            }

            checkDateSame();

            SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");

            DailyRowDialog aRF = new DailyRowDialog(dailyStats.getDayData(), sf.format(gc_current.getTime()),
                    getMyParent());

            if (aRF.actionSuccessful())
            {
                System.out.println("m_db:" + m_db);
                System.out.println("dailyStats.dayData:" + dailyStats.getDayData());

                m_db.saveDayStats(dailyStats.getDayData());
                dailyStats.getTableModel().fireTableChanged(null);
                // this.model.fireTableChanged(null);
            }

            // DailyValues dv = getDayData();
            //
            // DailyRowDialog aRF = new DailyRowDialog(dv,
            // dataAccess.getCurrentDateString(), getFrame());
            //
            // if (aRF.actionSuccessful())
            // {
            // dataAccess.getDb().saveDayStats(dv);
            // reloadTable();
            // }

        }
        else if (actionCommand.equals("edit_entry"))
        {
            if (checkIfPumpAndDisplayError("Edit entry"))
            {
                return;
            }

            checkDateSame();

            if (dailyStats.getTable().getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(getMyParent(), i18nControl.getMessage("SELECT_ROW_FIRST"),
                    i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            DailyValuesRow dvr = dailyStats.getDayData().getRow(dailyStats.getTable().getSelectedRow());

            DailyRowDialog aRF = new DailyRowDialog(dvr, getMyParent());

            if (aRF.actionSuccessful())
            {
                m_db.saveDayStats(dailyStats.getDayData());
                dailyStats.getTableModel().fireTableChanged(null);
            }

        }
        else if (actionCommand.equals("delete_entry"))
        {
            if (checkIfPumpAndDisplayError("Delete entry"))
            {
                return;
            }

            checkDateSame();

            if (dailyStats.getTable().getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(getMyParent(), i18nControl.getMessage("SELECT_ROW_FIRST"),
                    i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            int option_selected = JOptionPane.showOptionDialog(getMyParent(),
                i18nControl.getMessage("ARE_YOU_SURE_DELETE_ROW"), i18nControl.getMessage("QUESTION"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, dataAccess.options_yes_no,
                JOptionPane.YES_OPTION);

            if (option_selected == JOptionPane.NO_OPTION)
                return;

            try
            {
                dailyStats.getDayData().deleteRow(dailyStats.getTable().getSelectedRow());
                dailyStats.getTableModel().fireTableChanged(null);
                m_db.saveDayStats(dailyStats.getDayData());
            }
            catch (Exception ex)
            {
                System.out.println("DailyStatsDialog:Action:Delete Row: " + ex);
                // log.error("Action::Delete Row::Exception: " + ex, ex);
            }
        }
        else if (actionCommand.equals("show_graph"))
        {
            showFunctionalityNotAvailableYet("Show daily graph");
            // DailyGraphDialog dgd = new DailyGraphDialog(getMyParent(),
            // dailyStats.getDayData());
            // dgd.setDailyValues(dailyStats.getDayData());
        }
        else if (actionCommand.equals("read_meter"))
        {
            executePluginAction(GGCPluginType.MeterToolPlugin, e, "meters_read");
        }
        else if (actionCommand.equals("read_pump"))
        {
            if (dataAccess.isPluginAvailable(GGCPluginType.MeterToolPlugin))
            {
                dataAccess.getPlugIn(GGCPluginType.MeterToolPlugin)
                        .actionPerformed(new ActionEvent(this, e.getID() + 1, "meters_read"));
            }

            showFunctionalityNotAvailableYet("Read Pump");
            // ReadMeterDialog.showMe(MainFrame.this);
        }
        else if (actionCommand.equals("read_cgms"))
        {
            showFunctionalityNotAvailableYet("Read CGMS");
            // ReadMeterDialog.showMe(MainFrame.this);
        }
        else if (actionCommand.equals("hlp_about"))
        {
            // FIX This
            new AboutGGCDialog(getMyParent());
            // JOptionPane.showMessageDialog(null,
            // "GNU Gluco Control v0.0.1", "About GGC",
            // JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            System.out.println("GGCLittle:Unknown Command: " + actionCommand);
        }

    }


    private void executePluginAction(GGCPluginType pluginType, ActionEvent e, String actionCommand)
    {
        if (dataAccess.isPluginAvailable(pluginType))
        {
            dataAccess.getPlugIn(pluginType).actionPerformed(new ActionEvent(this, e.getID(), actionCommand));
        }

    }


    private void showFunctionalityNotAvailableYet(String functionality)
    {
        dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error,
            functionality + " is not working in this application yet. Please use full version of GGC instead.");
    }


    private boolean checkIfPumpAndDisplayError(String errorDescriptionTarget)
    {
        if (dataAccess.isPumpMode())
        {
            dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error,
                errorDescriptionTarget + " for Pump is not working in this application yet. Please use GGC instead.");
            return true;
        }

        return false;
    }


    /**
     * Main Method for running
     * @param args
     */
    public static void main(String args[])
    {
        new GGCLittle(args.length > 0);
        /*
         * GGCLittle gl = new GGCLittle("GGCL - GNU Gluco Control Little",
         * false); Toolkit theKit = gl.getToolkit(); Dimension wndSize =
         * theKit.getScreenSize();
         * //mainWindow.setBounds(wndSize.width / 4, wndSize.height / 4,
         * (int)(wndSize.width 0.66), (int)(wndSize.height 0.66));
         * int x, y;
         * x = wndSize.width/2 - 400; y = wndSize.height/2 - 300;
         * gl.setBounds(x, y, 600, 440); gl.setVisible(true);
         */

    }


    public void update(Observable obj, Object arg)
    {
        // System.out.println("!!!! update app: " +
        // this.getClass().getSimpleName() + " - " + arg);

        if (arg instanceof RefreshInfoType)
        {
            RefreshInfoType refreshInfoType = (RefreshInfoType) arg;

            // if (!title_set)
            // {
            // if (refreshInfoType.getCode() >=
            // RefreshInfoType.GeneralInfo.getCode())
            // {
            // title_set = true;
            // this.setSoftwareMode();
            // }
            // }

            // Integer i = (Integer)arg;

            // this.setSoftwareMode();
            // this.setTitle();

        }
        else if (arg instanceof DatabaseStatusType)
        {
            // DatabaseStatusType databaseStatusType = (DatabaseStatusType) arg;
            //
            // setMenusByDbLoad(databaseStatusType.getCode());
            //
            // if (databaseStatusType == DatabaseStatusType.Loaded)
            // {
            // refreshMenus();
            // }
        }
        else if (arg instanceof Exception)
        {
            Exception ex = (Exception) arg;
            LOG.error("Error connecting to Database: " + ex.getMessage(), ex);

            dataAccess.createErrorDialog("Main", "Opening Db", ex, guiHelper.getDbSettingsAndCause(false), //
                guiHelper.getDbSettingsAndCause(true), //
                i18nControl.getMessage("DB_PROBLEM_NOT_CONNECTED_SOLUTION"), //
                i18nControl.getMessage("DB_PROBLEM_NOT_CONNECTED_SOLUTION_TOOLTIP"));

            System.exit(1);
        }
        else
        {
            LOG.error("Unallowed update type ({}) - {}.", arg.getClass().getSimpleName(), arg);
        }

    }
}
