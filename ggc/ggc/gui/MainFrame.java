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

import ggc.gui.dialogs.AboutGGCDialog;
import ggc.gui.dialogs.CourseGraphDialog;
import ggc.gui.dialogs.DailyStatsDialog;
import ggc.gui.dialogs.FrequencyGraphDialog;
import ggc.gui.dialogs.HbA1cDialog;
import ggc.gui.dialogs.PrintingDialog;
import ggc.gui.dialogs.PropertiesDialog;
import ggc.gui.dialogs.SpreadGraphDialog;
import ggc.gui.nutrition.NutritionTreeDialog;
import ggc.gui.panels.info.InfoPanel;
import ggc.util.DataAccess;
import ggc.util.I18nControl;
import ggc.util.VersionChecker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;


// 
//   Short History 
// 
//   0.2.2  Configuration with pictures and in database 	
// 
// 
// 
// 
//   0.2.3   Configuration
//   0.2.4   Menus
//   0.2.5   Nutrition-1
//   0.2.6   Config COM
// 
//   0.2.7   New entry dialog
//   0.2.8   About Dialog
//   0.2.9   New entry finalization
// 
//   0.2.9.1   new entry done
//   0.2.9.2   new windows added (stock, schedule)
//   0.2.9.3   remove meters support
//    Menus (all), About, Db (all)
// 


public class MainFrame extends JFrame 
{

    // Version information
    public  static String s_version = "0.2.9.3";
    private String full_version = "v" + s_version;

    private String version_date = "8th January 2008";

    private I18nControl m_ic = null;
    public static SkinLookAndFeel s_skinlf;

    private static final String skinLFdir = "../data/skinlf_themes/";
    
    public static boolean developer_version = false;

    //fields
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();

    //private JLabel lblTest = new JLabel();


    private JMenu menu_file, menu_bgs, menu_food, menu_doctor, menu_reports, menu_tools, menu_help;


    private Hashtable<String,GGCAction> actions = null;

    /*
    private GGCAction ac_file_quit,    // file
	ac_bgs_daily,
	ac_rep_simple, ac_rep_ext,     // reports
	ac_food_nutr, ac_food_meals,   // food
	ac_doc_docs, ac_doc_appoint;   // doctors

    //private GGCAction connectAction, disconnectAction, newAction, openAction, closeAction, 
    private GGCAction quitAction, prefAction, readMeterAction;

    private GGCAction viewDailyAction, viewCourseGraphAction,
            viewSpreadGraphAction, viewFrequencyGraphAction;

    private GGCAction viewHbA1cAction;

    private GGCAction foodNutrAction, foodMealsAction, reportPDFSimpleAction,
            reportPDFExtendedAction;

    private GGCAction aboutAction, checkVersionAction;
*/
    //private DailyStatsFrame dailyStatsWindow;
    public StatusBar statusPanel;

    public InfoPanel informationPanel;

    private DataAccess m_da = null;


    /**
     *   Static definitions (Look and Feel)
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

            if (data==null)
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







    //constructor
    public MainFrame(String title, boolean developer_version) 
    {
        // this is the first chance to call this method after an instance of GGCProperties has been created
        //m_ic.setLanguage();

        m_da = DataAccess.createInstance(this);
        m_ic = I18nControl.getInstance();

        statusPanel = new StatusBar();

        this.actions = new Hashtable<String,GGCAction>();

        setTitle(title + " (" + full_version + ")");
        setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        MainFrame.developer_version = developer_version;


//menu_file, menu_bgs, menu_food, menu_doctor, menu_reports, menu_tools, menu_help;


	// file menu
	this.menu_file = this.createMenu("MN_FILE", null);
	this.createAction(this.menu_file, "MN_QUIT", "MN_QUIT_DESC", "file_quit", null);

	// bgs menu
	this.menu_bgs = this.createMenu("MN_BGS", null);
	this.createAction(this.menu_bgs, "MN_DAILY", "MN_DAILY_DESC", "view_daily", "daily.gif"); 
	this.createAction(this.menu_bgs, "MN_COURSE", "MN_COURSE_DESC", "view_course", "course.gif");
	this.createAction(this.menu_bgs, "MN_SPREAD", "MN_SPREAD_DESC", "view_spread", "spread.gif");
	this.createAction(this.menu_bgs, "MN_FREQUENCY", "MN_FREQUENCY_DESC", "view_freq", "frequency.gif");
	this.menu_bgs.addSeparator();
	this.createAction(this.menu_bgs, "MN_HBA1C", "MN_HBA1C_DESC", "view_hba1c", null);
//	this.menu_bgs.addSeparator();
//	this.createAction(this.menu_bgs, "MN_FROM_METER", "MN_FROM_METER_DESC", "read_meter", "readmeter.gif");


	// food menu
	this.menu_food = this.createMenu("MN_FOOD", null);
	this.createAction(this.menu_food, "MN_NUTRDB_USDB", "MN_NUTRDB_USDB_DESC", "food_nutrition_1", null);
	this.menu_food.addSeparator();
	this.createAction(this.menu_food, "MN_NUTRDB_USER", "MN_NUTRDB_USER_DESC", "food_nutrition_2", null);
	this.menu_food.addSeparator();
	this.createAction(this.menu_food, "MN_MEALS", "MN_MEALS_DESC", "food_meals", null);

	
	// doctors menu
	this.menu_doctor = this.createMenu("MN_DOCTOR", null);
	this.createAction(this.menu_doctor, "MN_DOCS", "MN_DOCS_DESC", "doc_docs", null);
	this.menu_doctor.addSeparator();
	this.createAction(this.menu_doctor, "MN_APPOINT", "MN_APPOINT_DESC", "doc_appoint", null);
	this.menu_doctor.addSeparator();
	this.createAction(this.menu_doctor, "MN_STOCKS", "MN_STOCKS_DESC", "doc_stocks", null);

	// reports menu
	this.menu_reports = this.createMenu("MN_REPORTS", null);
	this.createAction(this.menu_reports, "MN_PDF_SIMPLE", "MN_PDF_SIMPLE_DESC", "report_pdf_simple", null);
	this.createAction(this.menu_reports, "MN_PDF_EXT", "MN_PDF_EXT_DESC", "report_pdf_extended", null);

	// tools menu
	this.menu_tools = this.createMenu("MN_TOOLS", null);
	this.createAction(this.menu_tools, "MN_PREFERENCES", "MN_PREFERENCES_DESC", "tools_pref", null);
	this.menu_tools.addSeparator();
	this.createAction(this.menu_tools, "MN_DB_MAINT", "MN_DB_MAINT_DESC", "tools_db_maint", null);
	this.menu_tools.addSeparator();
	this.createAction(this.menu_tools, "MN_METER_LIST", "MN_METER_LIST_DESC", "tools_mlist", null);

	//addMenuItem(menu_tools, prefAction);

	// help menu
	this.menu_help = this.createMenu("MN_HELP", null);
	this.menu_help.addSeparator();
	this.createAction(this.menu_help,"MN_CHECK_FOR_UPDATE", "MN_CHECK_FOR_UPDATE_DESC", "hlp_check", null);
	this.menu_help.addSeparator();
	this.createAction(this.menu_help,"MN_ABOUT", "MN_ABOUT_DESC", "hlp_about", null);


	//this.createAction(null, "&Test", "Testing option", "test", null);


        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        addToolBarButtonWithName("view_daily");
        addToolBarButtonWithName("view_course"); 
        addToolBarButtonWithName("view_spread");
        addToolBarButtonWithName("view_freq");
        addToolBarSpacer();
        addToolBarSpacer();

        addToolBarButtonWithName("view_hba1c");
        addToolBarSpacer();
        addToolBarSpacer();
/* 
        // meters removed for 0.3
        addToolBarButtonWithName("read_meter");
        addToolBarSpacer();
        addToolBarSpacer();
        */

        addToolBarButtonWithName("tools_pref");
        addToolBarSpacer();
        addToolBarSpacer();

        addToolBarButtonWithName("test");

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        m_da.startDb(statusPanel);

        statusPanel.setStatusMessage(m_ic.getMessage("INIT"));

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
    
    	if (tool_tip!=null)
    	{
    	    item.setToolTipText(tool_tip);
    	}
    
    	//System.out.println("Item: " + item);
    
    	this.menuBar.add(item);
    
    	return item;
    }


    private void createAction(JMenu menu, String name, String tip, String action_command, String icon_small)
    {
    	GGCAction action = new GGCAction(name, tip, action_command);
    
    	if (icon_small!=null)
    	{
    	    action.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/" + icon_small)));
    	}
    
    	if (menu!=null)
    	    menu.add(action);
    
    	this.actions.put(action_command, action);

        //return action;
    }


    public void setDbActions(boolean opened) 
    {

    	this.menu_bgs.setEnabled(opened);
    	this.menu_food.setEnabled(opened);
    	this.menu_doctor.setEnabled(opened);
    	this.menu_reports.setEnabled(opened);
    
    	this.actions.get("view_daily").setEnabled(opened);
    	this.actions.get("view_course").setEnabled(opened);
    	this.actions.get("view_spread").setEnabled(opened);
    	this.actions.get("view_freq").setEnabled(opened);
    	this.actions.get("view_hba1c").setEnabled(opened);
//x    	this.actions.get("read_meter").setEnabled(opened);
    	this.actions.get("tools_pref").setEnabled(opened);


	/*   FIXXXXXXXXXXXX
        viewDailyAction.setEnabled(opened);
        viewSpreadGraphAction.setEnabled(opened);
        viewCourseGraphAction.setEnabled(opened);
        viewFrequencyGraphAction.setEnabled(opened);
        viewHbA1cAction.setEnabled(opened);
        readMeterAction.setEnabled(opened);
	prefAction.setEnabled(opened);
        //s_dbH.setStatus();
	*/

        System.out.println("FIIIIIIIIIIIIIIIIIIIIIIXXXX this");
    }

    private void close() 
    {
        //write to prefs to file on close.
        //props.write();
        //dbH.disconnectDb();

	if (m_da!=null)
	{
	    if (m_da.getDb()!=null)
		m_da.getDb().closeDb();
    
	    m_da.deleteInstance();
	}

        dispose();
        System.exit(0);
    }

    private JMenuItem addMenuItem(JMenu menu, Action action) 
    {
        JMenuItem item = menu.add(action);

        //System.out.println(action.getValue(Action.ACCELERATOR_KEY));

        KeyStroke keystroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
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


    private JButton addToolBarButtonWithName(String cmd) 
    {
	return addToolBarButton(this.actions.get(cmd));
    }



    class GGCAction extends AbstractAction 
    {

        GGCAction(String name, String command) 
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            if (m_ic.hasMnemonic(name))
            {
                char ch = m_ic.getMnemonic(name);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));
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

            //char ch = m_ic.getMnemonic(name);

            //System.out.println("Char ch: '" + ch + "'");

            //if ((ch != '0') || (ch != ' '))
            if (m_ic.hasMnemonic(name))
            {
                char ch = m_ic.getMnemonic(name);

                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch,
                        Event.CTRL_MASK));
//                System.out.println("Found");
            }
            else
            {
                putValue(ACCELERATOR_KEY, null);
//                System.out.println("NOT Found");
            }

            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, m_ic.getMessage(tooltip));

            if (command != null)
                putValue(ACTION_COMMAND_KEY, command);
        }


    	public String getName()
    	{
    	    return (String)getValue(Action.NAME);
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
            else if (command.equals("tools_pref")) 
            {
                PropertiesDialog pd = new PropertiesDialog(MainFrame.this);

        		if (pd.wasOKAction())
        		{
        		    informationPanel.invalidatePanelsConstants();
        		    informationPanel.refreshPanels();
        		}
            } 
/*            else if (command.equals("read_meter")) 
            {
                new MeterReadDialog(MainFrame.this);
            } */
/*            else if (command.equals("hlp_about")) 
            {
                JOptionPane.showMessageDialog(null,
                                "GNU Gluco Control " + s_version, "About GGC",
                                JOptionPane.INFORMATION_MESSAGE);
            } */
            else if (command.equals("hlp_check")) 
            {
                new VersionChecker().checkForUpdate();
            } 
            else if (command.equals("food_nutrition_1")) 
            {
                new NutritionTreeDialog(m_da);
                //System.out.println("Command N/A: Food Nutrition");
            } 
/*            else if (command.equals("food_nutrition_2")) 
	    {

	    }*/
/*            else if (command.equals("food_meals")) 
            {
                System.out.println("Command N/A: Food Meals");
            }  */
            else if (command.equals("report_pdf_simple")) 
            {
                new PrintingDialog(MainFrame.this, 1);
            } 
	    else if (command.equals("hlp_about"))
	    {
		new AboutGGCDialog(getMyParent());
	    }
	    else if ((command.equals("read_meter")) ||
                 (command.equals("food_nutrition_2")) || 
                 (command.equals("tools_mlist")) ||
                 (command.equals("tools_db_maint"))) 
	    {
		featureNotImplemented(command, "0.4");
	    }
	    else if ((command.equals("report_pdf_extended")) ||
                 (command.equals("doc_docs")) ||
                 (command.equals("doc_appoint")) ||
                 (command.equals("doc_stocks")) ||
                 (command.equals("food_meals")))
	    {
		featureNotImplemented(command, "0.5");
	    }
	    else if (command.equals("test"))
	    {
            //ggc.gui.ReadMeterDialog rm = new ggc.gui.ReadMeterDialog(MainFrame.this);
	    }
        else
                System.out.println("Unknown Command: " + command);

        }
    }


    public void featureNotImplemented(String cmd, String version)
    {

    	String text = m_ic.getMessage("FEATURE");
    
    	text += " '" + this.actions.get(cmd).getName() +"' ";
    	text += m_ic.getMessage("IMPLEMENTED_VERSION");
    	text += " " + version+" !";
    
    	JOptionPane.showMessageDialog(MainFrame.this, text, m_ic.getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);

    }


    private class CloseListener extends WindowAdapter 
    {
        @Override
        public void windowClosing(WindowEvent e) 
        {
            close();
        }
    }
}