package ggc.gui.little;

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
 *  Filename: GGCLittle
 *  Purpose:  This is small version of GGC application, intended for quick adding to 
 *      database, for example when you make measurement or take insulin and you don't 
 *      need the whole application.
 *
 *  Author:   andyrozman {andy@atech-software.com}
 */

import ggc.core.data.DailyValuesRow;
import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.AboutGGCDialog;
import ggc.gui.dialogs.DailyRowDialog;
import ggc.gui.dialogs.graphs.DailyGraphDialog;
import ggc.gui.little.panels.DailyStatsPanelL;
import ggc.gui.little.panels.MainLittlePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.atech.help.HelpContext;
import com.atech.i18n.I18nControlAbstract;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

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

public class GGCLittle extends JFrame implements WindowListener, ActionListener
{

    private static final long serialVersionUID = -7744922647111948669L;
    private I18nControlAbstract m_ic = null; //I18nControl.getInstance();
    private DataAccess m_da = null;
    private GGCDb m_db = null;

    private static SkinLookAndFeel s_skinlf;
    private static final String skinLFdir = "../data/skinlf_themes/";
    private Hashtable<String, GGCAction> actions = null;

    // GUI
    private MainLittlePanel informationPanel = null;
    private StatusBarL statusPanel;
    private DailyStatsPanelL dailyStats = null;

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
        GGCLittle.setLookAndFeel(); // Win (not so bad) ???
    }

    /**
     * Set Look And Feel
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
     * @param developer_version
     */
    public GGCLittle(boolean developer_version)
    {
        m_da = DataAccess.createInstance(this);
        m_ic = m_da.getI18nControlInstance();
        m_db = m_da.getDb();

        this.actions = new Hashtable<String, GGCAction>();

        statusPanel = new StatusBarL(this);

        String title = m_ic.getMessage("GGC_LITTLE_TITLE");

        setTitle(title + " (" + ggc_little_version + ")");

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        createToolbar();
        createSystemTrayInstance();

        /*
         * JMenuBar jmb = new JMenuBar(); this.setJMenuBar(jmb);
         * 
         * quitAction = new GGCAction("MN_QUIT", "MN_QUIT_DESC", "file_quit");
         * jmb.add(new JMenuItem(quitAction));
         */

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        helpInit();

        //m_da.addObserver(observable_id, inst)
        
        m_da.startDb(); //statusPanel);
        setDbActions(false);
        statusPanel.setStatusMessage(m_ic.getMessage("INIT"));

        // dayData = DataAccess.getInstance().getDayStats(new
        // GregorianCalendar());

        gc_current = new GregorianCalendar();
        informationPanel = new MainLittlePanel(this);

        dailyStats = informationPanel.dailyStats;

        getContentPane().add(informationPanel, BorderLayout.CENTER);
        setSize(640, 480);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void createToolbar()
    {

        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        JLabel label_empty = new JLabel("");
        label_empty.setPreferredSize(new Dimension(40, 20));

        this.toolBar.add(getEmptyLabel(10, 20));

        createToolbarAction("MN_QUIT", "MN_QUIT_LT_DESC", "quit", "exit.png");

        this.toolBar.add(getEmptyLabel(30, 20));

        createToolbarAction("TB_ADD_ROW", "TB_ADD_ROW_DESC", "add_entry", "table_add.png");
        createToolbarAction("TB_EDIT_ROW", "TB_EDIT_ROW_DESC", "edit_entry", "table_edit.png");
        createToolbarAction("TB_DELETE_ROW", "TB_DELETE_ROW_DESC", "delete_entry", "table_delete.png");

        this.toolBar.add(getEmptyLabel(30, 20));

        createToolbarAction("TB_SHOW_GRAPH", "TB_SHOW_GRAPH_DESC", "show_graph", "line-chart.png");

        // this.toolBar.add(getEmptyLabel(30,20));

        // createToolbarAction("MN_FROM_METER", "TB_READ_METER_LT_DESC",
        // "read_meter", "readmeter.gif");

        this.toolBar.add(getEmptyLabel(360, 20));

        createToolbarAction("MN_ABOUT", "TB_ABOUT_DESC", "hlp_about", "about.png");

        // this.actions.get("read_meter").setEnabled(false);

        /*
         * readMeterAction = new GGCAction("MN_FROM_METER",
         * "MN_FROM_METER_DESC", "read_meter");
         * readMeterAction.putValue(Action.SMALL_ICON, new
         * ImageIcon(getClass().getResource("/icons/readmeter.gif")));
         * 
         * aboutAction = new GGCAction("MN_ABOUT", "MN_ABOUT_DESC",
         * "hlp_about"); aboutAction.putValue(Action.SMALL_ICON, new
         * ImageIcon(getClass().getResource("/icons/about.gif")));
         * 
         * toolBar.setFloatable(false); toolBar.setLayout(new
         * FlowLayout(FlowLayout.LEFT, 1, 1));
         * 
         * 
         * addToolBarSpacer(); addToolBarButton(readMeterAction);
         * 
         * JLabel l = new JLabel(""); l.setPreferredSize(new Dimension(510,20));
         * 
         * toolBar.add(l); addToolBarButton(aboutAction);
         */

    }

    private void createSystemTrayInstance()
    {

        /*
         * JPopupMenu menu = new JPopupMenu();
         * 
         * menu.add(createItem("SHOW_APP", "SHOW_APP_DESC", "show_app"));
         * menu.add(createItem("HIDE_APP", "HIDE_APP_DESC", "hide_app"));
         * menu.addSeparator(); menu.add(createItem("EXIT_APP", "EXIT_APP_DESC",
         * "exit_app")); menu.add(new JLabel()); menu.add(new JLabel());
         * 
         * 
         * SystemTray tray = SystemTray.getSystemTray();
         * 
         * TrayIcon ti = new TrayIcon(m_da.getImageIcon("medical_bag.png", 15,
         * 15, this) , m_ic.getMessage("GGC_LITTLE_TITLE"), menu);
         * 
         * ti.setIconAutoSize(true); ti.addActionListener(new ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * JOptionPane.showMessageDialog(null,
         * m_ic.getMessage("GGC_LITTLE_TITLE"), m_ic.getMessage("ABOUT"),
         * JOptionPane.INFORMATION_MESSAGE); } }); tray.addTrayIcon(ti);
         */

    }

    /*
     * private JMenuItem createItem(String name, String desc, String action) {
     * JMenuItem item = new JMenuItem(m_ic.getMessage(name));
     * item.setActionCommand(action); //item.setName(m_ic.getMessage(name));
     * item.setToolTipText(m_ic.getMessage(desc)); item.addActionListener(this);
     * 
     * 
     * return item; }
     */

    
    /**
     * Get Status Panel
     * 
     * @return
     */
    public StatusBarL getStatusPanel()
    {
        return this.statusPanel;
    }
    
    /**
     * Get Information Panel
     * 
     * @return
     */
    public MainLittlePanel getInformationPanel()
    { 
        return this.informationPanel;
    }
    
    
    private JLabel getEmptyLabel(int width, int height)
    {
        JLabel label_empty = new JLabel("");
        label_empty.setPreferredSize(new Dimension(width, height));

        return label_empty;

    }

    private void createToolbarAction(String name, String tip, String action_command, String icon_small)
    {
        GGCAction action = new GGCAction(name, tip, action_command);

        if (icon_small != null)
        {
            action.putValue(Action.SMALL_ICON, m_da.getImageIcon(icon_small, 24, 24, this));
        }

        // this.toolBar.add(action);
        this.actions.put(action_command, action);

        addToolBarButton(action);

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
        m_da.getDb().closeDb();
        // s_dbH.disconnectDb();
        dispose();
        System.exit(0);
    }

    /*
     * private JMenuItem addMenuItem(JMenu menu, Action action) { JMenuItem item
     * = menu.add(action);
     * 
     * KeyStroke keystroke = (KeyStroke)action.getValue(Action.ACCELERATOR_KEY);
     * if (keystroke != null) item.setAccelerator(keystroke); return item; }
     */
    /*
     * private void addToolBarSpacer() { toolBar.addSeparator(); }
     */

    private JButton addToolBarButton(Action action)
    {
        final JButton button = toolBar.add(action);

        button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        button.setFocusPainted(false);

        button.setPreferredSize(new Dimension(28, 28));

        // button.setIcon((ImageIcon)action.getValue(Action.SMALL_ICON));
        /*
         * button.addMouseListener(new MouseListener() { public void
         * mouseEntered(MouseEvent e) { if (button.isEnabled()) {
         * button.setBorder(BorderFactory.createLineBorder(new Color(8, 36,
         * 106), 1)); button.setBackground(new Color(180, 190, 213)); }
         * 
         * }
         * 
         * public void mouseExited(MouseEvent e) {
         * button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
         * button.setBackground(new Color(213, 210, 205)); }
         * 
         * public void mouseClicked(MouseEvent e) { }
         * 
         * public void mousePressed(MouseEvent e) { }
         * 
         * public void mouseReleased(MouseEvent e) { }
         * 
         * });
         */

        // button.setRolloverIcon(new ImageIcon("ggc/icons/connect.png"));
        // button.setRolloverEnabled(true);
        return button;
    }

    class GGCAction extends AbstractAction
    {
        // private String command = null;

        /**
         * 
         */
        private static final long serialVersionUID = 5381628629001707536L;

        GGCAction(String name, String command)
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            char ch = m_ic.getMnemonic(name);

            if (ch != '0')
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));

            if (command != null)
                putValue(ACTION_COMMAND_KEY, command);

            command = name;
        }

        /*
         * GGCAction(String name, KeyStroke keystroke) { this();
         * setName(m_ic.getMessageWithoutMnemonic(name)); if (keystroke != null)
         * putValue(ACCELERATOR_KEY, keystroke); }
         */
        GGCAction(String name, String tooltip, String command)
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            char ch = m_ic.getMnemonic(name);

            if (ch != '0')
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));

            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, m_ic.getMessage(tooltip));

            if (command != null)
                putValue(ACTION_COMMAND_KEY, command);
        }

        /*
         * GGCAction(String name, KeyStroke keystroke, String tooltip) {
         * this(name, keystroke); if (tooltip != null)
         * putValue(SHORT_DESCRIPTION, tooltip); }
         */

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();

            if (command.equals("quit"))
            {
                close();
            }
            else if (command.equals("add_entry"))
            {
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
            }
            else if (command.equals("edit_entry"))
            {
                checkDateSame();

                if (dailyStats.getTable().getSelectedRow() == -1)
                {
                    JOptionPane.showMessageDialog(getMyParent(), m_ic.getMessage("SELECT_ROW_FIRST"), m_ic
                            .getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
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
            else if (command.equals("delete_entry"))
            {
                checkDateSame();

                if (dailyStats.getTable().getSelectedRow() == -1)
                {
                    JOptionPane.showMessageDialog(getMyParent(), m_ic.getMessage("SELECT_ROW_FIRST"), m_ic
                            .getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int option_selected = JOptionPane.showOptionDialog(getMyParent(), m_ic
                        .getMessage("ARE_YOU_SURE_DELETE_ROW"), m_ic.getMessage("QUESTION"), JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, m_da.options_yes_no, JOptionPane.YES_OPTION);

                if (option_selected == JOptionPane.NO_OPTION)
                {
                    return;
                }

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
            else if (command.equals("show_graph"))
            {
                DailyGraphDialog dgd = new DailyGraphDialog(getMyParent(), dailyStats.getDayData());
                dgd.setDailyValues(dailyStats.getDayData());
            }
            else if (command.equals("read_meter"))
            {
                // ReadMeterDialog.showMe(MainFrame.this);
            }
            else if (command.equals("hlp_about"))
            {
                // FIX This
                new AboutGGCDialog(getMyParent());
                // JOptionPane.showMessageDialog(null,
                // "GNU Gluco Control v0.0.1", "About GGC",
                // JOptionPane.INFORMATION_MESSAGE);
            }
            else
                System.out.println("GGCLittle:Unknown Command: " + command);

        }
    }

    /**
     * Help Init
     */
    public void helpInit()
    {
        // TODO fix this
        HelpContext hc = new HelpContext("../data/help/GGC.hs");

        m_da.setHelpContext(hc);

        JMenuItem helpItem = new JMenuItem(m_ic.getMessage("HELP") + "...");
        helpItem.setIcon(new ImageIcon(getClass().getResource("/icons/help.gif")));

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

            // ClassLoader cl = MainFrame.class.getClassLoader();
            // String help_url = "jar:file:pis_lang-0.1.jar!/help/PIS.hs";

            String help_url = "jar:file:ggc_help-0.1.jar!/help/en/GGC.hs";

            try
            {
                URL hsURL = new URL(help_url);

                if (hsURL == null)
                    System.out.println("HelpSet " + help_url /*
                                                              * PISMain.mainHelpSetName
                                                              */+ " not found.");
                else
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
                // System.out.println("Help: Main Help Set present, creating broker");
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

    private boolean checkDateSame()
    {
        if (m_db == null)
            this.m_db = m_da.getDb();

        if (!m_da.isSameDay(this.gc_current, new GregorianCalendar()))
        {
            // not same date
            JOptionPane.showMessageDialog(getMyParent(), m_ic.getMessage("CURRENT_DATE_HAS_CHANGED"), m_ic
                    .getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);

            this.dailyStats.refreshInfo();

            return false;
        }
        else
        {
            return true;
        }

    }

    // Window Listener

    /** 
     */
    public void windowDeactivated(WindowEvent e) { }

    /** 
     */
    public void windowActivated(java.awt.event.WindowEvent e) { }

    /** 
     */
    public void windowDeiconified(java.awt.event.WindowEvent e) { }

    /** 
     */
    public void windowIconified(java.awt.event.WindowEvent e) { }

    /** 
     */ 
    public void windowClosed(java.awt.event.WindowEvent e) { }

    /** 
     */
    public void windowOpened(java.awt.event.WindowEvent e) { }

    /**
     * Window Closing Event
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e)
    {
        close();
    }

    /**
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {

        String action = e.getActionCommand();

        if (action.equals("show_app"))
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
        else if (action.equals("hide_app"))
        {
            is_visible = false;
            this.setVisible(false);

        }
        else if (action.equals("exit_app"))
        {
            close();
        }
        else
            System.out.println("Action: " + action);

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
         * 
         * //mainWindow.setBounds(wndSize.width / 4, wndSize.height / 4,
         * (int)(wndSize.width 0.66), (int)(wndSize.height 0.66));
         * 
         * int x, y;
         * 
         * x = wndSize.width/2 - 400; y = wndSize.height/2 - 300;
         * 
         * gl.setBounds(x, y, 600, 440); gl.setVisible(true);
         */

    }

}