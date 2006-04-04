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
 *  Filename: DailyStatsFrame.java
 *  Purpose:  Enter and view all data for one day. (fix)
 *
 *  Author:   andyrozman
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman

package ggc.little;

import ggc.gui.*;

import ggc.datamodels.DailyStatsTableModel;
import ggc.datamodels.DailyValues;
import ggc.datamodels.calendar.CalendarEvent;
import ggc.datamodels.calendar.CalendarListener;
import ggc.db.DataBaseHandler;
import ggc.gui.calendar.calendarPane;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DailyStatsControlsPanel extends JPanel //implements ActionListener
{
    
    private I18nControl m_ic = I18nControl.getInstance();    

    DailyStatsTableModel model = null;
    //JScrollPane resultsPane;
    
    //JTable table;

    public boolean save_needed = false;


    JButton addButton, graphButton, editButton, delButton;


    JLabel lblDate;
    JButton saveButton;
    private DailyGraphFrame dailyGraphWindow;
    DailyValues dayData;
    private DataBaseHandler dbH = DataBaseHandler.getInstance();
    //private static DailyStatsFrame singleton = null;

    private GGCProperties props = GGCProperties.getInstance();
    GGCLittle m_little = null;

    public DailyStatsControlsPanel(GGCLittle little)
    {
        super();
	m_little = little;
        //setTitle(m_ic.getMessage("DAILYSTATSFRAME"));
        init();
    }

    /*
    public static DailyStatsFrame getInstance()
    {
        if (singleton == null)
            singleton = new DailyStatsFrame();
        return singleton;
    }
    */

    /*
    public static void showMe()
    {
        /*if (singleton == null)
            singleton = new DailyStatsFrame();
        singleton.show(); */
        //DailyGraphFrame.showMe();
    //}



    private void init()
    {
//        setBounds(150, 150, 550, 500);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //addWindowListener(new CloseListener());
	this.setLayout(new GridLayout(1,1));
	setBorder(BorderFactory.createTitledBorder(I18nControl.getInstance().getMessage("DAILY_CONTROL")+":"));


//        Box dayStats = Box.createVerticalBox();
    //    dayStats.add(InsPanel);
    //    dayStats.add(BUPanel);
    //    dayStats.add(BGPanel);




	Dimension dim = new Dimension(120, 20);
        
	JPanel gg = new JPanel();
	gg.setLayout(new BorderLayout());
	//gg.setPreferredSize(dim);


	JPanel EntryBox1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 2));
	

        graphButton = new JButton(m_ic.getMessage("SHOW_DAILY_GRAPH"));
        graphButton.setPreferredSize(dim);
	graphButton.setActionCommand("show_daily_graph");
	//EntryBox1.add(graphButton);

	//gg.add(EntryBox1, BorderLayout.WEST);
	
	
	JPanel EntryBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 2));
        //Dimension dim = new Dimension(120, 20);

	EntryBox.add(graphButton);

        addButton = new JButton(m_ic.getMessage("ADD_ROW"));
        addButton.setPreferredSize(dim);
	addButton.setActionCommand("add_row");
        EntryBox.add(addButton);

        delButton = new JButton(m_ic.getMessage("DELETE_ROW"));
        delButton.setPreferredSize(dim);
	delButton.setActionCommand("delete_row");
        EntryBox.add(delButton);

	/*
        saveButton = new JButton(m_ic.getMessage("CLOSE"));
        saveButton.setPreferredSize(dim);
	saveButton.setActionCommand("close");
//	saveButton.addActionListener(this);
        
	/*saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dayData.saveDay();
            }
        }); */
        //saveButton.setEnabled(false);
        //EntryBox.add(saveButton);


	this.add(EntryBox, BorderLayout.EAST);

//        add(resultsPane, BorderLayout.CENTER);

	setVisible(true);
    }


    public void addActionCommands(ActionListener panel)
    {
	addButton.addActionListener(panel);
	graphButton.addActionListener(panel);
	//editButton
	delButton.addActionListener(panel);
    }


    public void actionPerformed(ActionEvent e)
    {

	String command = e.getActionCommand();

	if (command.equals("add_row"))
	{
	    //SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
	    //AddRowFrame aRF = AddRowFrame.getInstance(model, dayData, sf.format(System.currentTimeMillis()));
	    //aRF.show();
	}
	else if (command.equals("delete_row"))
	{
	    //dayData.deleteRow(m_little.m_infoPanel.dailyStats.table.getSelectedRow());
	    //model.fireTableChanged(null);
	}
	else if (command.equals("close"))
	{
  //          close();
	}
	else if (command.equals("show_daily_graph"))
	{

	}

    }


    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
//            close();
        }
    }
    
}