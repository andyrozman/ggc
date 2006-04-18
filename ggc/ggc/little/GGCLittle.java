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
 *  Author:   andyrozman
 */


// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


package ggc.little;


import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

import ggc.db.DataBaseHandler;
import ggc.gui.*;
import ggc.gui.panels.info.InfoPanel;
import ggc.print.PrintMonthlyReport;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;
import ggc.util.VersionChecker;

import ggc.gui.dialogs.DailyStatsDialog;
import ggc.gui.dialogs.CourseGraphDialog;
import ggc.gui.dialogs.*;


public class GGCLittle extends JFrame
{
    
    private I18nControl m_ic = I18nControl.getInstance();        
    public static SkinLookAndFeel s_skinlf;
    public LInfoPanel m_infoPanel = null;

    //fields
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private JLabel lblTest = new JLabel();
    private GGCAction connectAction, disconnectAction, newAction, openAction, closeAction, quitAction;
    private GGCAction prefAction;
    private GGCAction readMeterAction;
    private GGCAction viewDailyAction, viewCourseGraphAction, viewSpreadGraphAction, viewFrequencyGraphAction;
    private GGCAction viewHbA1cAction;
    private GGCAction aboutAction, checkVersionAction;
//    private DailyStatsDialog dailyStatsWindow;
    private StatusBar statusPanel;
    private InfoPanel informationPanel;
    public static DataBaseHandler s_dbH;

    //GGCProperties props = GGCProperties.getInstance();

    /**
     *   Static definitions (Look and Feel)
     */
    static
    {
	GGCLittle.setLookAndFeel("blueMetalthemepack.zip");  // Win (not so bad) ???
    }



    public static void setLookAndFeel(String name)
    {

	try
	{
	
	    SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack("../lib/skinLFThemes/"+name));      
    
	    s_skinlf = new com.l2fprod.gui.plaf.skin.SkinLookAndFeel();
	    UIManager.setLookAndFeel(s_skinlf);

            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);

	}
	catch(Exception ex)
	{
            System.err.println("Error loading L&F: " + ex);
	}
    }



    //constructor
    public GGCLittle(String title, boolean developer_version)
    {
        setTitle(title);
        //setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

	File f = new File("../data/ggc_db.lck");

	if (f.exists())
	{
	    f.delete();
	}


        //this.developer_version = developer_version;

        JMenu fileMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_FILE"));
        JMenu viewMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_VIEW"));
        JMenu readMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_READ"));
        JMenu optionMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_OPTION"));
        JMenu helpMenu = new JMenu(m_ic.getMessageWithoutMnemonic("MN_HELP"));
        JMenu testMenu = new JMenu("Test");
        fileMenu.setMnemonic(m_ic.getMnemonic("MN_FILE"));
        viewMenu.setMnemonic(m_ic.getMnemonic("MN_VIEW"));
        optionMenu.setMnemonic(m_ic.getMnemonic("MN_OPTION"));
        helpMenu.setMnemonic(m_ic.getMnemonic("MN_HELP"));


        connectAction = new GGCAction("MN_CONNECT", "MN_CONNECT_DESC", "file_connect");
        connectAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/connect.gif")));
        
	disconnectAction = new GGCAction("MN_DISCONNECT", "MN_DISCONNECT_DESC", "file_disconnect");
        disconnectAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/disconnect.gif")));
/*
        newAction = new GGCAction("MN_NEW", "MN_NEW_DESC", "file_new");
        newAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/new.gif")));
        openAction = new GGCAction("MN_OPEN", "MN_OPEN_DESC", "file_open");
        openAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/open.gif")));
        closeAction = new GGCAction("MN_CLOSE", "MN_CLOSE_DESC", "file_close");
        closeAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/close.gif")));
*/	
	
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

        prefAction = new GGCAction("MN_PREFERENCES", "MN_PREFERENCES_DESC", "option_pref");

        aboutAction = new GGCAction("MN_ABOUT", "MN_ABOUT_DESC", "hlp_about");
        checkVersionAction = new GGCAction("MN_CHECK_FOR_UPDATE", "MN_CHECK_FOR_UPDATE_DESC", "hlp_check");

        GGCAction test = new GGCAction("Print", "Print Test", "print_test");

        addMenuItem(fileMenu, connectAction);
        addMenuItem(fileMenu, disconnectAction);
        fileMenu.addSeparator();
        //addMenuItem(fileMenu, newAction);
        //addMenuItem(fileMenu, openAction);
        //addMenuItem(fileMenu, closeAction);
        //fileMenu.addSeparator();
        addMenuItem(fileMenu, quitAction);

        addMenuItem(viewMenu, viewDailyAction);
        addMenuItem(viewMenu, viewCourseGraphAction);
        addMenuItem(viewMenu, viewSpreadGraphAction);
        addMenuItem(viewMenu, viewFrequencyGraphAction);
        viewMenu.addSeparator();
        addMenuItem(viewMenu, viewHbA1cAction);

        addMenuItem(readMenu, readMeterAction);

        addMenuItem(optionMenu, prefAction);

        addMenuItem(helpMenu, aboutAction);
        addMenuItem(helpMenu, checkVersionAction);

        addMenuItem(testMenu, test);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(readMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);
        

        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        addToolBarButton(connectAction);
        addToolBarButton(disconnectAction);

        addToolBarSpacer();
        //addToolBarButton(newAction);
        //addToolBarButton(openAction);
        //addToolBarButton(closeAction);
        addToolBarSpacer();
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

        statusPanel = new StatusBar();
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        //statusPanel.setDataSourceText(props.getDataSource() + "[" + m_ic.getMessage("NO_CONNECTION") + "]");
        statusPanel.setStatusMessage(m_ic.getMessage("INIT"));

	s_dbH = DataBaseHandler.getInstance();
	s_dbH.connectDb();

	if (!s_dbH.isConnected())
	{
	    s_dbH.connectDb();
	}

	s_dbH.setStatus();

	//statusPanel.setDataSourceText(props.getDataSource() + "[" + m_ic.getMessage("NO_CONNECTION") + "]");


        //if (props.getAutoConnect())
        //    s_dbH.connectDb();

	setDbActions();

	/*
        if (s_dbH.isConnected()) {
            if (s_dbH.isConnectedToDB())
                setActionEnabledStateDBOpened();
            else
                setActionEnabledStateDBClosed();
        } else
            setActionEnabledStateDisconnected();
	*/
	
        //Information Portal Setup
        //informationPanel = new InfoPanel();

	// Little specific stuff

	m_infoPanel = new LInfoPanel(this);

        getContentPane().add(m_infoPanel, BorderLayout.CENTER);
    }


    public GGCLittle getMyParent()
    {
	return this;
    }


    /*
    private void setActionEnabledStateDisconnected()
    {
        setConActions(false);
        setDBActionsAllFalse();
    } */

/*
    private void setActionEnabledStateConnected()
    {
        setConActions(true);
        
	if (props.getDataSource().equals("HSQL"))
	    setDBActions(true);
	else
	    setDBActions(false);
    }

    private void setActionEnabledStateDBOpened()
    {
        setConActions(true);
        setDBActions(true);
    }

    private void setActionEnabledStateDBClosed()
    {
        setConActions(true);
        setDBActions(false);
    }
*/
/*    private void setConActions(boolean connected)
    {
        connectAction.setEnabled(!connected);
        disconnectAction.setEnabled(connected);
    }
    */

    private void setDbActions()
    {

	setDBActions(s_dbH.isConnected());

/*
	if (s_dbH.isConnected())
	{
	    connectAction.setEnabled(false);
	    disconnectAction.setEnabled(true);
	}
	else
	{
	    connectAction.setEnabled(true);
	    disconnectAction.setEnabled(false);
	}
  */
    }



    private void setDBActions(boolean opened)
    {

	connectAction.setEnabled(!opened);
	disconnectAction.setEnabled(opened);

        //openAction.setEnabled(!opened);
        //closeAction.setEnabled(opened);
        //newAction.setEnabled(!opened);

        viewDailyAction.setEnabled(opened);
        viewSpreadGraphAction.setEnabled(opened);
        viewCourseGraphAction.setEnabled(opened);
        viewFrequencyGraphAction.setEnabled(opened);
        viewHbA1cAction.setEnabled(opened);

        readMeterAction.setEnabled(opened);
    }


    /*
    private void setDBActionsAllFalse()
    {
        openAction.setEnabled(false);
        closeAction.setEnabled(false);
        newAction.setEnabled(false);

        viewDailyAction.setEnabled(false);
        viewSpreadGraphAction.setEnabled(false);
        viewCourseGraphAction.setEnabled(false);
        viewFrequencyGraphAction.setEnabled(false);
        viewHbA1cAction.setEnabled(false);

        readMeterAction.setEnabled(false);
    } */

    private void close()
    {
        //write to prefs to file on close.
        //props.write();
	s_dbH.disconnectDb();
        dispose();
        System.exit(0);
    }

    private JMenuItem addMenuItem(JMenu menu, Action action)
    {
        JMenuItem item = menu.add(action);

        KeyStroke keystroke = (KeyStroke)action.getValue(action.ACCELERATOR_KEY);
        if (keystroke != null)
            item.setAccelerator(keystroke);
        return item;
    }

    private void addToolBarSpacer()
    {
        toolBar.addSeparator();
	
	//JLabel lbl = new JLabel(new ImageIcon(getClass().getResource("/icons/spacer.gif")));
        //lbl.setEnabled(false);
        //toolBar.add(lbl);
    }

    private JButton addToolBarButton(Action action)
    {
        final JButton button = toolBar.add(action);

        button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        button.setFocusPainted(false);

        button.setPreferredSize(new Dimension(24, 24));

	//button.setIcon((ImageIcon)action.getValue(Action.SMALL_ICON));
/*
        button.addMouseListener(new MouseListener()
        {
            public void mouseEntered(MouseEvent e)
            {
                if (button.isEnabled()) 
                {
                    button.setBorder(BorderFactory.createLineBorder(new Color(8, 36, 106), 1));
                    button.setBackground(new Color(180, 190, 213));
                }
		
            }

            public void mouseExited(MouseEvent e)
            {
                button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                button.setBackground(new Color(213, 210, 205));
            }

            public void mouseClicked(MouseEvent e)
            {
            }

            public void mousePressed(MouseEvent e)
            {
            }

            public void mouseReleased(MouseEvent e)
            {
            }

        });	*/


        //button.setRolloverIcon(new ImageIcon("ggc/icons/connect.png"));

        //button.setRolloverEnabled(true);
        return button;
    }

    class GGCAction extends AbstractAction
    {
        //private String command = null;

        GGCAction(String name, String command)
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            char ch = m_ic.getMnemonic(name);

            if (ch!='0') 
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));

            if (command!=null)
                putValue(ACTION_COMMAND_KEY, command);

            command = name;
        }
/*
        GGCAction(String name, KeyStroke keystroke)
        {
            this();
            setName(m_ic.getMessageWithoutMnemonic(name));
            if (keystroke != null)
                putValue(ACCELERATOR_KEY, keystroke);
        }
   */
        GGCAction(String name, String tooltip, String command)
        {
            super();
            setName(m_ic.getMessageWithoutMnemonic(name));

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic(name));

            char ch = m_ic.getMnemonic(name);

            if (ch!='0') 
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ch, Event.CTRL_MASK));

            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, m_ic.getMessage(tooltip));

            if (command!=null)
                putValue(ACTION_COMMAND_KEY, command);
        }

/*
        GGCAction(String name, KeyStroke keystroke, String tooltip)
        {
            this(name, keystroke);
            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, tooltip);
        }
*/

        public void actionPerformed(ActionEvent e)
        {

            String command = e.getActionCommand();

	    //System.out.println("Command: " + command);

            if (command.equals("file_quit")) 
	    {
                close();
            } 
	    else if (command.equals("file_connect")) 
	    {

                //s_dbH = DataBaseHandler.getInstance();

		

                s_dbH.connectDb();


		//System.out.println("Connect" + s_dbH + "  " + s_dbH.isConnected());

		setDbActions();

		s_dbH.setStatus();
                
		//System.out.println(s_dbH.isConnected()

/*

		if (s_dbH.isConnected()) 
		{

                    if (s_dbH.isConnectedToDB())
                        setActionEnabledStateDBOpened();
                    else
                        setActionEnabledStateDBClosed();
                } 
		else
                    setActionEnabledStateDisconnected();
*/                
		informationPanel.refreshPanels();

            } 
            else if (command.equals("file_disconnect")) 
	    {

		s_dbH.disconnectDb();
		setDbActions();

		s_dbH.setStatus();

		/*
                if (s_dbH.isConnected())
                    setActionEnabledStateConnected();
                else
                    setActionEnabledStateDisconnected();
                DataBaseHandler.killHandler();
                s_dbH = null; */
                informationPanel.refreshPanels();

            } 
/*            else if (command.equals("file_new")) {

                if (s_dbH == null)
                    return;

		String tmpName; 

		//System.out.println(props.getDataSource());

		if (props.getDataSource().equals("HSQL"))
		{
		    tmpName = "HSQLDB";
		}
		else
		    tmpName = JOptionPane.showInputDialog(m_ic.getMessage("ENTER_DB_TO_CREATE")+":");
                
		if (tmpName != null && !tmpName.equals("")) 
		{
                    s_dbH.createNewDataBase(tmpName);
                    if (s_dbH.isConnectedToDB())
                        setActionEnabledStateDBOpened();
                    else
                        setActionEnabledStateDBClosed();
                } 
		else
                    JOptionPane.showMessageDialog(null, m_ic.getMessage("INVALID_NAME_FOR_DB"), "GGC " + m_ic.getMessage("ERROR")+ " - " + m_ic.getMessage("INVALID_NAME"), JOptionPane.ERROR_MESSAGE);

                informationPanel.refreshPanels();

            } 
            else if (command.equals("file_open")) {

                //s_dbH.setDBName(JOptionPane.showInputDialog("Enter DB Name to open:"));
                if (s_dbH == null)
                    return;
                s_dbH.openDataBase(true);
                if (s_dbH.isConnectedToDB())
                    setActionEnabledStateDBOpened();
                else
                    setActionEnabledStateDBClosed();
                informationPanel.refreshPanels();

            } 
            else if (command.equals("file_close")) {

                if (s_dbH == null)
                    return;
                s_dbH.disconnectDb();

		/*
                if (s_dbH.isConnectedToDB())
                    setActionEnabledStateDBOpened();
                else
                    setActionEnabledStateDBClosed();
		    */
/*                informationPanel.refreshPanels();

            } */
            else if (command.equals("view_daily")) 
            {
                //DailyStatsFrame.showMe();

		DailyStatsDialog df = new DailyStatsDialog(getMyParent());

            } 
            else if (command.equals("view_course")) 
            {
                new CourseGraphDialog(getMyParent()); //.showMe();
            } 
            else if (command.equals("view_spread")) 
            {
		new SpreadGraphDialog(getMyParent());
            } 
            else if (command.equals("view_freq")) 
            {
                new FrequencyGraphDialog(getMyParent());
            } 
            else if (command.equals("view_hba1c")) 
            {
                HbA1cFrame.showMe();
            } 
            else if (command.equals("option_pref")) 
            {
                //PropertiesFrame.showMe(MainFrame.this);
            } 
            else if (command.equals("read_meter")) 
            {
                //ReadMeterDialog.showMe(MainFrame.this);
            } 
            else if (command.equals("hlp_about")) 
            {
                // FIX This
                JOptionPane.showMessageDialog(null, "GNU Gluco Control v0.0.1", "About GGC", JOptionPane.INFORMATION_MESSAGE);
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
                    String pathToAcrobat = "d:/Program Files/Adobe/Acrobat 6.0/Reader/AcroRd32.exe"	;

                    Runtime.getRuntime().exec(pathToAcrobat+ " " + "HelloWorld2.pdf"); 
                }
                catch(Exception ex)
                {
                    System.out.println("Error running AcrobatReader");
                }

            }
            else
                System.out.println("Unknown Command: " + command);

        }
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
	    s_dbH.disconnectDb();
            close();
        }
    }


    public static void main(String args[])
    {
	GGCLittle gl = new GGCLittle("GGCL - GNU Gluco Control Little", false);
	Toolkit theKit = gl.getToolkit();
	Dimension wndSize = theKit.getScreenSize();

	//mainWindow.setBounds(wndSize.width / 4, wndSize.height / 4, (int)(wndSize.width * 0.66), (int)(wndSize.height * 0.66));

	int x, y; 

	x = wndSize.width/2 - 400;
	y = wndSize.height/2 - 300;

	gl.setBounds(x, y, 600, 440);
	gl.setVisible(true);


    }


}