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

package gui;


import db.DataBaseHandler;
import util.GGCProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainFrame extends JFrame
{
    //fields
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private JLabel lblTest = new JLabel();
    private GGCAction newAction, openAction, closeAction, quitAction;
    private GGCAction prefAction;
    private GGCAction readMeterAction;
    private GGCAction viewDailyAction;
    private GGCAction aboutAction;
    private DailyStatsFrame dailyStatsWindow;
    DataBaseHandler dbH;

    //constructor
    public MainFrame(String title)
    {
        setTitle(title);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener())

        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu readMenu = new JMenu("Read");
        JMenu optionMenu = new JMenu("Option");
        JMenu helpMenu = new JMenu("Help");
        fileMenu.setMnemonic('F');
        viewMenu.setMnemonic('V');
        optionMenu.setMnemonic('O');
        helpMenu.setMnemonic('H');

        newAction = new GGCAction("New", KeyStroke.getKeyStroke('N', Event.CTRL_MASK), "Create a new DataBase");
        openAction = new GGCAction("Open", KeyStroke.getKeyStroke('O', Event.CTRL_MASK), "Open existing DataBase");
        closeAction = new GGCAction("Close", KeyStroke.getKeyStroke('C', Event.CTRL_MASK), "Close current DataBase");
        quitAction = new GGCAction("Quit", KeyStroke.getKeyStroke('Q', Event.CTRL_MASK), "Quit ggc");

        viewDailyAction = new GGCAction("Daily", KeyStroke.getKeyStroke('D', Event.CTRL_MASK), "View Daily Stats");

        readMeterAction = new GGCAction("from Meter", KeyStroke.getKeyStroke('R', Event.CTRL_MASK), "Read Data From Meter");

        prefAction = new GGCAction("Preferences", KeyStroke.getKeyStroke('P', Event.CTRL_MASK), "Preferences");

        aboutAction = new GGCAction("About", KeyStroke.getKeyStroke('A', Event.CTRL_MASK), "About GGC");

        addMenuItem(fileMenu, newAction);
        addMenuItem(fileMenu, openAction);
        addMenuItem(fileMenu, closeAction);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, quitAction);

        addMenuItem(viewMenu, viewDailyAction);

        addMenuItem(readMenu, readMeterAction);

        addMenuItem(optionMenu, prefAction);

        addMenuItem(helpMenu, aboutAction);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(readMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);

        addToolBarButton(newAction);
        addToolBarButton(openAction);
        addToolBarButton(closeAction);
        toolBar.addSeparator();
        addToolBarButton(viewDailyAction);
        toolBar.addSeparator();
        addToolBarButton(readMeterAction);
        toolBar.addSeparator();
        addToolBarButton(prefAction);

        getContentPane().add(toolBar, BorderLayout.NORTH);

        dbH = DataBaseHandler.getInstance();
        if (dbH.isConnectedToDB()) {
            openAction.setEnabled(false);
        } else {
            closeAction.setEnabled(false);
            viewDailyAction.setEnabled(false);
        }
    }

    private void close()
    {
        //write to prefs to file on close.
        GGCProperties p = GGCProperties.getInstance();
        p.write();

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

    private JButton addToolBarButton(Action action)
    {
        JButton button = toolBar.add(action);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //button.setRolloverEnabled(true);
        return button;
    }

    class GGCAction extends AbstractAction
    {
        GGCAction(String name)
        {
            super(name);
        }

        GGCAction(String name, KeyStroke keystroke)
        {
            this(name);
            if (keystroke != null)
                putValue(ACCELERATOR_KEY, keystroke);
        }

        GGCAction(String name, String tooltip)
        {
            this(name);
            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, tooltip);
        }

        GGCAction(String name, KeyStroke keystroke, String tooltip)
        {
            this(name, keystroke);
            if (tooltip != null)
                putValue(SHORT_DESCRIPTION, tooltip);
        }

        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Quit")) {
                close();
            } else if (e.getActionCommand().equals("New")) {
                String tmpName = JOptionPane.showInputDialog("Enter DB Name to create:");
                if (tmpName != null && !tmpName.equals("")) {
                    dbH.createNewDataBase(tmpName);
                    openAction.setEnabled(false);
                    closeAction.setEnabled(true);
                    viewDailyAction.setEnabled(true);
                } else
                    JOptionPane.showMessageDialog(null, "Invalid Name for Database", "GGC Error - Invalid Name", JOptionPane.ERROR_MESSAGE);
            } else if (e.getActionCommand().equals("Open")) {
                dbH.setDBName(JOptionPane.showInputDialog("Enter DB Name to open:"));
                dbH.connect();
                if (dbH.isConnectedToDB()) {
                    openAction.setEnabled(false);
                    closeAction.setEnabled(true);
                    viewDailyAction.setEnabled(true);
                }
            } else if (e.getActionCommand().equals("Close")) {
                dbH.closeConnection();
                if (!dbH.isConnectedToDB()) {
                    openAction.setEnabled(true);
                    closeAction.setEnabled(false);
                    viewDailyAction.setEnabled(false);
                }
            } else if (e.getActionCommand().equals("Daily")) {
                DailyStatsFrame.showMe();
            } else if (e.getActionCommand().equals("Preferences")) {
                PropertiesFrame.showMe();
            } else if (e.getActionCommand().equals("from Meter")) {
                ReadMeterFrame.showMe();
            } else if (e.getActionCommand().equals("About")) {
                JOptionPane.showMessageDialog(null, "GNU Gluco Control v0.0.1", "About GGC", JOptionPane.INFORMATION_MESSAGE);
            }
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