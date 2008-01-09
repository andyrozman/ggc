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
 *  Author:   andyrozman
 */


// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

import ggc.util.DataAccess;
import ggc.util.I18nControl;


public class GGCLittle extends JFrame implements WindowListener
{

    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccess m_da = null;
    public static SkinLookAndFeel s_skinlf;

    // GUI
    public MainLittlePanel informationPanel = null;
    public StatusBarL statusPanel;

    //fields
    private JToolBar toolBar = new JToolBar();
    private GGCAction readMeterAction, quitAction, aboutAction;


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
        m_da = DataAccess.createInstance(this);
        m_ic = I18nControl.getInstance();

        statusPanel = new StatusBarL();

        setTitle(title + " (" /*+ full_version*/ + ")");

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        /*
    	File f = new File("../data/ggc_db.lck");
    
    	if (f.exists())
    	{
    	    f.delete();
    	}
*/


        quitAction = new GGCAction("MN_QUIT", "MN_QUIT_DESC", "file_quit");

        readMeterAction = new GGCAction("MN_FROM_METER", "MN_FROM_METER_DESC", "read_meter");
        readMeterAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/readmeter.gif")));

        aboutAction = new GGCAction("MN_ABOUT", "MN_ABOUT_DESC", "hlp_about");
        aboutAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/about.gif")));

        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        
        addToolBarSpacer();
        addToolBarButton(readMeterAction);

        JLabel l = new JLabel("");
        l.setPreferredSize(new Dimension(510,20));

        toolBar.add(l);
        addToolBarButton(aboutAction);
        

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        m_da.startDb(statusPanel);
        setDbActions(false);
        statusPanel.setStatusMessage(m_ic.getMessage("INIT"));

        informationPanel = new MainLittlePanel(this);
        getContentPane().add(informationPanel, BorderLayout.CENTER);
    }


    public GGCLittle getMyParent()
    {
        return this;
    }


    public void setDbActions(boolean opened)
    {
        readMeterAction.setEnabled(opened);
    }



    private void close()
    {
        //write to prefs to file on close.
        //props.write();
        m_da.getDb().closeDb();
        //s_dbH.disconnectDb();
        dispose();
        System.exit(0);
    }

    private JMenuItem addMenuItem(JMenu menu, Action action)
    {
        JMenuItem item = menu.add(action);

        KeyStroke keystroke = (KeyStroke)action.getValue(Action.ACCELERATOR_KEY);
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
            else if (command.equals("view_daily")) 
            {
                //DailyStatsFrame.showMe();

                //DailyStatsDialog df = new DailyStatsDialog(getMyParent());

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
            else
                System.out.println("GGCLittle:Unknown Command: " + command);

        }
    }

    // Window Listener

    public void windowDeactivated(WindowEvent e) {}
    public void windowActivated(java.awt.event.WindowEvent e) {} 
    public void windowDeiconified(java.awt.event.WindowEvent e) {}
    public void windowIconified(java.awt.event.WindowEvent e) {}
    public void windowClosed(java.awt.event.WindowEvent e) {}
    public void windowOpened(java.awt.event.WindowEvent e) {}

    //windowOpened
    public void windowClosing(WindowEvent e)
    {
        close();
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