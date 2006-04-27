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
 *  Purpose:  The MainFrame of the app. Contains MenuBars, ToolBars, StatusBars, ...
 *
 *  Author:   schultd
 */

package ggc.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.text.View;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

import ggc.gui.dialogs.CourseGraphDialog;
import ggc.gui.dialogs.DailyStatsDialog;
import ggc.gui.dialogs.FrequencyGraphDialog;
import ggc.gui.dialogs.HbA1cDialog;
import ggc.gui.dialogs.PrintingDialog;
import ggc.gui.dialogs.PropertiesDialog;
import ggc.gui.dialogs.SpreadGraphDialog;
import ggc.gui.panels.info.InfoPanel;
import ggc.nutrition.NutritionTreeDialog;
import ggc.print.PrintMonthlyReport;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;
import ggc.util.VersionChecker;


public class MainFrame extends JFrame 
{

    // Version information
    public  static String s_version = "0.2.1.11";
    private String full_version = "v" + s_version;
    private String version_date = "15th April 2006";

    private I18nControl m_ic = null;
    public static SkinLookAndFeel s_skinlf;

    private static final String skinLFdir = "../lib/skinLFThemes/";

    //fields
    private JMenuBar menuBar = new JMenuBar();

    private JToolBar toolBar = new JToolBar();

    private JLabel lblTest = new JLabel();

    //private GGCAction connectAction, disconnectAction, newAction, openAction, closeAction, 
    private GGCAction quitAction, prefAction, readMeterAction;

    private GGCAction viewDailyAction, viewCourseGraphAction,
            viewSpreadGraphAction, viewFrequencyGraphAction;

    private GGCAction viewHbA1cAction;

    private GGCAction foodNutrAction, foodMealsAction, reportPDFSimpleAction,
            reportPDFExtendedAction;

    private GGCAction aboutAction, checkVersionAction;

    //private DailyStatsFrame dailyStatsWindow;
    public StatusBar statusPanel;

    public InfoPanel informationPanel;

    //public static DataBaseHandler s_dbH;

    private DataAccess m_da = null;

    //public static boolean s_developer_version = false;

    //GGCProperties props = GGCProperties.getInstance();

    /**
     *   Static definitions (Look and Feel)
     */
    static 
    {

        //DataAccess.getInstance();

        //MainFrame.setLookAndFeel("coronaHthemepack.zip");
        //MainFrame.setLookAndFeel("midnightthemepack.zip");
        //MainFrame.setLookAndFeel("solunaRthemepack.zip");

        //MainFrame.setLookAndFeel("blueTurquesathemepack.zip");  OK
        //MainFrame.setLookAndFeel("cougarthemepack.zip");
        //MainFrame.setLookAndFeel("opusOSBluethemepack.zip"); ?
        //MainFrame.setLookAndFeel("underlingthemepack.zip"); ?
        //MainFrame.setLookAndFeel("royalInspiratthemepack.zip"); ?
        //	MainFrame.setLookAndFeel("hmmXPBluethemepack.zip");
        MainFrame.setLookAndFeel("blueMetalthemepack.zip"); // Win (not so bad) ???
        //	MainFrame.setLookAndFeel("architectBluethemepack.zip");
        //	MainFrame.setLookAndFeel("roueBluethemepack.zip");
        //	MainFrame.setLookAndFeel("quickSilverRthemepack.zip");  
        //	MainFrame.setLookAndFeel("chaNinja-Bluethemepack.zip");  // Mhm
        //	MainFrame.setLookAndFeel("crystal2themepack.zip");  // mhm
        //	MainFrame.setLookAndFeel("toxicthemepack.zip");
        //	MainFrame.setLookAndFeel("amarachthemepack.zip");
        //	MainFrame.setLookAndFeel("b0sumiErgothempack.zip");
        //	MainFrame.setLookAndFeel("gfxOasisthemepack.zip");
        //	MainFrame.setLookAndFeel("iBarthemepack.zip");
        //      MainFrame.setLookAndFeel("midnightthemepack.zip");	 // not os bad
        //	MainFrame.setLookAndFeel("solunaRthemepack.zip");		 // nn
        //	MainFrame.setLookAndFeel("tigerGraphitethemepack.zip");
        //	MainFrame.setLookAndFeel("gorillathemepack.zip");	  // nn
        //	MainFrame.setLookAndFeel("fatalEthemepack.zip");
        //	MainFrame.setLookAndFeel("b0sumithemepack.zip");
        //	MainFrame.setLookAndFeel("architectOlivethemepack.zip");
        //	MainFrame.setLookAndFeel("mmMagra-Xthemepack.zip");
        //	MainFrame.setLookAndFeel("silverLunaXPthemepack.zip");
        //	MainFrame.setLookAndFeel("opusOSDeepthemepack.zip");
        //	MainFrame.setLookAndFeel("coronaHthemepack.zip");
        //	MainFrame.setLookAndFeel("cougarthemepack.zip");
        //	MainFrame.setLookAndFeel("cougarthemepack.zip");
        //	MainFrame.setLookAndFeel("cougarthemepack.zip");
        //	MainFrame.setLookAndFeel("cougarthemepack.zip");
        //	MainFrame.setLookAndFeel("cougarthemepack.zip");

    }

    public static void setLookAndFeel(String name) 
    {
        try 
        {
            SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(skinLFdir+ name));

            s_skinlf = new com.l2fprod.gui.plaf.skin.SkinLookAndFeel();
            UIManager.setLookAndFeel(s_skinlf);

            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        } 
        catch (Exception ex) 
        {
            System.err.println("Error loading L&F: " + ex);
        }
    }

    //constructor
    public MainFrame(String title, boolean developer_version) 
    {
        // this is the first chance to call this method after an instance of GGCProperties has been created
        //m_ic.setLanguage();

        m_da = DataAccess.createInstance(this);
        m_ic = I18nControl.getInstance();

        statusPanel = new StatusBar();

        setTitle(title + " (" + full_version + ")");
        setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

      //  MainFrame.developer_version = developer_version;

        JMenu fileMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_FILE"));
        JMenu viewMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_VIEW"));
        JMenu readMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_READ"));
        JMenu foodMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_FOOD"));
        JMenu reportMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_REPORT"));
        JMenu optionMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_OPTION"));
        JMenu helpMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_HELP"));
        //JMenu testMenu = new JMenu("Test");

        fileMenu.setMnemonic(m_ic.getMnemonic("MN_FILE"));
        viewMenu.setMnemonic(m_ic.getMnemonic("MN_VIEW"));
        optionMenu.setMnemonic(m_ic.getMnemonic("MN_OPTION"));
        helpMenu.setMnemonic(m_ic.getMnemonic("MN_HELP"));
        foodMenu.setMnemonic(m_ic.getMnemonic("MN_FOOD"));
        reportMenu.setMnemonic(m_ic.getMnemonic("MN_REPORT"));

        quitAction = new GGCAction("MN_QUIT", "MN_QUIT_DESC", "file_quit");

        viewDailyAction = new GGCAction("MN_DAILY", "MN_DAILY_DESC", "view_daily");
        viewDailyAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/daily.gif")));
        viewCourseGraphAction = new GGCAction("MN_COURSE", "MN_COURSE_DESC", "view_course");
        viewCourseGraphAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/course.gif")));
        viewSpreadGraphAction = new GGCAction("MN_SPREAD", "MN_SPREAD_DESC", "view_spread");
        viewSpreadGraphAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/spread.gif")));
        viewFrequencyGraphAction = new GGCAction("MN_FREQUENCY", "MN_FREQUENCY_DESC", "view_freq");
        viewFrequencyGraphAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/frequency.gif")));
        viewHbA1cAction = new GGCAction("MN_HBA1C", "MN_HBA1C_DESC", "view_hba1c");

        readMeterAction = new GGCAction("MN_FROM_METER", "MN_FROM_METER_DESC", "read_meter");
        readMeterAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/readmeter.gif")));

        foodNutrAction = new GGCAction("MN_NUTRDB", "MN_NUTRDB_DESC", "food_nutrition");
        foodMealsAction = new GGCAction("MN_MEALS", "MN_MEALS_DESC", "food_meals");

        reportPDFSimpleAction = new GGCAction("MN_PDF_SIMPLE", "MN_PDF_SIMPLE_DESC", "report_pdf_simple");
        reportPDFExtendedAction = new GGCAction("MN_PDF_EXT", "MN_PDF_EXT_DESC", "report_pdf_extended");

        prefAction = new GGCAction("MN_PREFERENCES", "MN_PREFERENCES_DESC", "option_pref");

        aboutAction = new GGCAction("MN_ABOUT", "MN_ABOUT_DESC", "hlp_about");
        checkVersionAction = new GGCAction("MN_CHECK_FOR_UPDATE", "MN_CHECK_FOR_UPDATE_DESC", "hlp_check");

        //GGCAction test = new GGCAction("Print", "Print Test", "print_test");

        // File menu
        addMenuItem(fileMenu, quitAction);

        // View menu
        addMenuItem(viewMenu, viewDailyAction);
        addMenuItem(viewMenu, viewCourseGraphAction);
        addMenuItem(viewMenu, viewSpreadGraphAction);
        addMenuItem(viewMenu, viewFrequencyGraphAction);
        viewMenu.addSeparator();
        addMenuItem(viewMenu, viewHbA1cAction);

        // Read menu
        addMenuItem(readMenu, readMeterAction);

        // Food menu
        addMenuItem(foodMenu, foodNutrAction);
        addMenuItem(foodMenu, foodMealsAction);

        // report menu
        addMenuItem(reportMenu, reportPDFSimpleAction);
        addMenuItem(reportMenu, reportPDFExtendedAction);

        // Option menu
        addMenuItem(optionMenu, prefAction);

        // Help menu
        addMenuItem(helpMenu, aboutAction);
        addMenuItem(helpMenu, checkVersionAction);

        //addMenuItem(testMenu, test);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(readMenu);
        menuBar.add(foodMenu);
        menuBar.add(reportMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);

        //if (MainFrame.developer_version)
        //    menuBar.add(testMenu);

        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        addToolBarButton(viewDailyAction);
        addToolBarButton(viewCourseGraphAction);
        addToolBarButton(viewSpreadGraphAction);
        addToolBarButton(viewFrequencyGraphAction);
        addToolBarSpacer();
        addToolBarSpacer();

        addToolBarButton(viewHbA1cAction);
        addToolBarSpacer();
        addToolBarSpacer();

        addToolBarButton(readMeterAction);

        getContentPane().add(toolBar, BorderLayout.NORTH);

        //statusPanel = StatusBar.getInstance();
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        m_da.startDb(statusPanel);

        statusPanel.setStatusMessage(m_ic.getMessage("INIT"));

        //s_dbH = DataBaseHandler.getInstance();
        //s_dbH.setStatus();

        //Information Portal Setup
        informationPanel = new InfoPanel();
        getContentPane().add(informationPanel, BorderLayout.CENTER);

        setDbActions(false);
        this.setVisible(true);

    }

    public MainFrame getMyParent() 
    {
        return this;
    }

    public void setDbActions(boolean opened) 
    {
        viewDailyAction.setEnabled(opened);
        viewSpreadGraphAction.setEnabled(opened);
        viewCourseGraphAction.setEnabled(opened);
        viewFrequencyGraphAction.setEnabled(opened);
        viewHbA1cAction.setEnabled(opened);
        readMeterAction.setEnabled(opened);
        //s_dbH.setStatus();
    }

    private void close() 
    {
        //write to prefs to file on close.
        //props.write();
        //dbH.disconnectDb();
        m_da.getDb().closeDb();
        m_da.deleteInstance();
        dispose();
        System.exit(0);
    }

    private JMenuItem addMenuItem(JMenu menu, Action action) 
    {
        JMenuItem item = menu.add(action);

        KeyStroke keystroke = (KeyStroke) action.getValue(action.ACCELERATOR_KEY);
        if (keystroke != null)
            item.setAccelerator(keystroke);

        return item;
    }

    private void addToolBarSpacer() 
    {
        toolBar.addSeparator();
    }

    private JButton addToolBarButton(Action action) 
    {
        final JButton button = toolBar.add(action);

        button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(24, 24));

        return button;
    }


    class GGCAction extends AbstractAction 
    {

        GGCAction(String name, String command) 
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            char ch = m_ic.getMnemonic(name);

            if (ch != '0')
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch,
                        Event.CTRL_MASK));

            if (command != null)
                putValue(ACTION_COMMAND_KEY, command);

            command = name;
        }

        GGCAction(String name, String tooltip, String command) 
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            char ch = m_ic.getMnemonic(name);

            if (ch != '0')
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch,
                        Event.CTRL_MASK));

            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, m_ic.getMessage(tooltip));

            if (command != null)
                putValue(ACTION_COMMAND_KEY, command);
        }

        public void actionPerformed(ActionEvent e) 
        {

            String command = e.getActionCommand();

            if (command.equals("file_quit")) 
            {
                close();
            } 
            else if (command.equals("view_daily")) 
            {
                new DailyStatsDialog(getMyParent());
            } 
            else if (command.equals("view_course")) 
            {
                new CourseGraphDialog(MainFrame.this); //.showMe();
            } 
            else if (command.equals("view_spread")) 
            {
                new SpreadGraphDialog(MainFrame.this);
            } 
            else if (command.equals("view_freq")) 
            {
                new FrequencyGraphDialog(MainFrame.this); //.showMe();
            } 
            else if (command.equals("view_hba1c")) 
            {
                new HbA1cDialog(MainFrame.this);
            } 
            else if (command.equals("option_pref")) 
            {
                new PropertiesDialog(MainFrame.this);
            } 
            else if (command.equals("read_meter")) 
            {
                new ReadMeterDialog(MainFrame.this);
            } 
            else if (command.equals("hlp_about")) 
            {
                JOptionPane.showMessageDialog(null,
                                "GNU Gluco Control " + s_version, "About GGC",
                                JOptionPane.INFORMATION_MESSAGE);
            } 
            else if (command.equals("hlp_check")) 
            {
                new VersionChecker().checkForUpdate();
            } 
            else if (command.equals("print_test")) 
            {
                new PrintMonthlyReport();

                try 
                {
                    String pathToAcrobat = "d:/Program Files/Adobe/Acrobat 6.0/Reader/AcroRd32.exe";
                    Runtime.getRuntime().exec(
                            pathToAcrobat + " " + "HelloWorld2.pdf");
                } 
                catch (Exception ex) 
                {
                    System.out.println("Error running AcrobatReader");
                }

            } 
            else if (command.equals("food_nutrition")) 
            {
                new NutritionTreeDialog(m_da);
                //System.out.println("Command N/A: Food Nutrition");
            } 
            else if (command.equals("food_meals")) 
            {
                System.out.println("Command N/A: Food Meals");
            } 
            else if (command.equals("report_pdf_simple")) 
            {
                new PrintingDialog(MainFrame.this, 1);
                System.out.println("Command N/A: Report PDF Simple");
            } 
            else if (command.equals("report_pdf_extended")) 
            {
                new PrintingDialog(MainFrame.this, 2);
                System.out.println("Command N/A: Report PDF Extended");
            } 
            else
                System.out.println("Unknown Command: " + command);

        }
    }

    private class CloseListener extends WindowAdapter 
    {
        public void windowClosing(WindowEvent e) 
	{
            close();
        }
    }
}