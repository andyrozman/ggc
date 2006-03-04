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
 *  Purpose:  Enter and view all data for one day.
 *
 *  Author:   schultd
 */

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


public class DailyStatsPanel extends JPanel implements ActionListener
{
    
    private I18nControl m_ic = I18nControl.getInstance();    

    DailyStatsTableModel model = null;
    JScrollPane resultsPane;
    
    JTable table;

    public boolean save_needed = false;


    calendarPane calPane;

    JLabel sumIns1, sumIns2, sumIns;
    JLabel avgIns1, avgIns2, avgIns;
    JLabel doseIns1, doseIns2, doseIns;
    JLabel sumBE, avgBE, meals;
    JLabel avgBG, highestBG, lowestBG;
    JLabel readings, stdDev;

    JLabel lblDate;
    JButton saveButton;
    private DailyGraphFrame dailyGraphWindow;
    DailyValues dayData;
    private DataBaseHandler dbH = DataBaseHandler.getInstance();
    private static DailyStatsFrame singleton = null;

    private GGCProperties props = GGCProperties.getInstance();
    GGCLittle m_little = null;

    public DailyStatsPanel(GGCLittle little)
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

    public DailyStatsTableModel getTableModel()
    {
        return model;
    }

    protected void close()
    {
        //DailyGraphFrame.closeMe();
        //this.dispose();

	dayData.saveDay();

        //singleton = null;
    }

    private void init()
    {
//        setBounds(150, 150, 550, 500);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //addWindowListener(new CloseListener());
	this.setLayout(new GridLayout(1,1));

/*
        //Panel for Insulin Stats
        JPanel InsPanel = new JPanel(new GridLayout(3, 6));
        InsPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("INSULIN")+":"));

        InsPanel.add(new JLabel(props.getIns1Abbr() + ":"));
        InsPanel.add(sumIns1 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVG")+" " + props.getIns1Abbr() + ":"));
        InsPanel.add(avgIns1 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSE")+" " + props.getIns1Abbr() + ":"));
        InsPanel.add(doseIns1 = new JLabel());

        InsPanel.add(new JLabel(props.getIns2Abbr() + ":"));
        InsPanel.add(sumIns2 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVG")+" " + props.getIns2Abbr() + ":"));
        InsPanel.add(avgIns2 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSE")+" " + props.getIns2Abbr() + ":"));
        InsPanel.add(doseIns2 = new JLabel());

        InsPanel.add(new JLabel(m_ic.getMessage("TOTAL")+":"));
        InsPanel.add(sumIns = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVG_INS")+":"));
        InsPanel.add(avgIns = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSE_INS")+":"));
        InsPanel.add(doseIns = new JLabel());

        //Panel for BU Stats
        JPanel BUPanel = new JPanel(new GridLayout(1, 6));
        BUPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BREAD_UNITS")+":"));

        BUPanel.add(new JLabel(m_ic.getMessage("SUM")+":"));
        BUPanel.add(sumBE = new JLabel());
        BUPanel.add(new JLabel(m_ic.getMessage("AVG")+":"));
        BUPanel.add(avgBE = new JLabel());
        BUPanel.add(new JLabel(m_ic.getMessage("MEALS")+":"));
        BUPanel.add(meals = new JLabel());

        //Panel for BG Stats
        JPanel BGPanel = new JPanel(new GridLayout(0, 6));
        BGPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BLOOD_GLUCOSE")+":"));

        BGPanel.add(new JLabel(m_ic.getMessage("AVG_BG")+":"));
        BGPanel.add(avgBG = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("HIGHEST")+":"));
        BGPanel.add(highestBG = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("READINGS")+":"));
        BGPanel.add(readings = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("STD_DEV")+":"));
        BGPanel.add(stdDev = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("LOWEST")+":"));
        BGPanel.add(lowestBG = new JLabel());

        Box dayStats = Box.createVerticalBox();
    //    dayStats.add(InsPanel);
    //    dayStats.add(BUPanel);
    //    dayStats.add(BGPanel);

        JPanel dayHeader = new JPanel();
        dayHeader.setLayout(new BorderLayout());

        JPanel dayCalendar = new JPanel();
        dayCalendar.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("DATE")+":"));
    */
/*
        calPane = new calendarPane();
        calPane.addCalendarListener(new CalendarListener()
        {
            public void dateHasChanged(CalendarEvent e)
            {
                dayData = dbH.getDayStats(new Date(e.getNewDate()));
                model.setDailyValues(dayData);
                //saveButton.setEnabled(false);
                updateLabels();
                DailyGraphFrame.setDailyValues(dayData);
            }
        });
        dayCalendar.add(calPane);


        dayHeader.add(dayCalendar, BorderLayout.WEST);
        dayHeader.add(dayStats, BorderLayout.CENTER);
*/
        dayData = dbH.getDayStats(new Date(System.currentTimeMillis()));
        //DailyGraphFrame.setDailyValues(dayData);

        model = new DailyStatsTableModel(dayData);
        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                //DailyGraphFrame.redraw();
                updateLabels();
                //saveButton.setEnabled(true);
            }
        });
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        resultsPane = new JScrollPane(table);

/*
	Dimension dim = new Dimension(120, 20);
        
	JPanel gg = new JPanel();
	gg.setLayout(new BorderLayout());
	//gg.setPreferredSize(dim);
*/

/*
	JPanel EntryBox1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 2));
	
	

        JButton tButton = new JButton(m_ic.getMessage("TTT"));
        tButton.setPreferredSize(dim);
	//tButton.setMaximumSize(dim);
	tButton.setActionCommand("show_daily_graph");
//	tButton.addActionListener(this);

	EntryBox1.add(tButton);

	gg.add(EntryBox1, BorderLayout.WEST);
	
	
	JPanel EntryBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 2));
        //Dimension dim = new Dimension(120, 20);

        JButton addButton = new JButton(m_ic.getMessage("ADD_ROW"));
        addButton.setPreferredSize(dim);
	addButton.setActionCommand("add_row");
    //    addButton.addActionListener(this);
        
/*	addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
                AddRowFrame aRF = AddRowFrame.getInstance(model, dayData, sf.format(calPane.getSelectedDate()));
                aRF.show();
            }
        }); */
/*        EntryBox.add(addButton);

        JButton delButton = new JButton(m_ic.getMessage("DELETE_ROW"));
        delButton.setPreferredSize(dim);
	delButton.setActionCommand("delete_row");
  //      delButton.addActionListener(this);
/*        delButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dayData.deleteRow(table.getSelectedRow());
                model.fireTableChanged(null);
            }
        }); */
/*        EntryBox.add(delButton);

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
/*        EntryBox.add(saveButton);


	gg.add(EntryBox, BorderLayout.EAST);
	*/

        add(resultsPane, BorderLayout.CENTER);
//        add(dayHeader, BorderLayout.NORTH);
        //getContentPane().add(EntryBox, BorderLayout.SOUTH);
//	add(gg, BorderLayout.SOUTH);

        //enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        //setVisible(true);

//        updateLabels();

	//System.out.println("Little: " + m_little);
	//System.out.println("Info panel: " + m_little.m_infoPanel);
	//System.out.println("Control: " + m_little.m_infoPanel.control);

	//m_little.m_infoPanel.control.addActionCommands(this);



	setVisible(true);
    }


    public void updateLabels()
    {
        if (dayData == null)
            return;

        DecimalFormat df = new DecimalFormat("#0.0");
        sumIns1.setText(df.format(dayData.getSumIns1()));
        sumIns2.setText(df.format(dayData.getSumIns2()));
        sumIns.setText(df.format(dayData.getSumIns()));

        avgIns1.setText(df.format(dayData.getAvgIns1()));
        avgIns2.setText(df.format(dayData.getAvgIns2()));
        avgIns.setText(df.format(dayData.getAvgIns()));

        doseIns1.setText(dayData.getIns1Count() + "");
        doseIns2.setText(dayData.getIns2Count() + "");
        doseIns.setText(dayData.getInsCount() + "");

        sumBE.setText(df.format(dayData.getSumBE()));
        avgBE.setText(df.format(dayData.getAvgBE()));
        meals.setText(dayData.getBECount() + "");

        avgBG.setText(df.format(dayData.getAvgBG()));
        stdDev.setText(df.format(dayData.getStdDev()));
        highestBG.setText(df.format(dayData.getHighestBG()));
        lowestBG.setText(df.format(dayData.getLowestBG()));
        readings.setText(dayData.getBGCount() + "");
    }

/*
    public void processWindowEvent(WindowEvent e)
    {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
        }
        super.processWindowEvent(e);
    }
      */

    public void actionPerformed(ActionEvent e)
    {

	String command = e.getActionCommand();

	if (command.equals("add_row"))
	{
	    SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        // XXX: my version of Java doesn't seem to accept sf.format(long), Does this work with 1.5?
	    AddRowFrame aRF = AddRowFrame.getInstance(model, dayData, sf.format(System.currentTimeMillis()));
	    aRF.show();
	}
	else if (command.equals("delete_row"))
	{
	    dayData.deleteRow(table.getSelectedRow());
	    model.fireTableChanged(null);
	}
	else if (command.equals("close"))
	{
	    close();
	}
	else if (command.equals("show_daily_graph"))
	{

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