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

package ggc.gui;


import ggc.datamodels.DailyStatsTableModel;
import ggc.datamodels.DailyValues;
import ggc.datamodels.calendar.CalendarEvent;
import ggc.datamodels.calendar.CalendarListener;
import ggc.db.DataBaseHandler;
import ggc.gui.calendar.calendarPane;
import ggc.util.GGCProperties;

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


public class DailyStatsFrame extends JFrame
{
    DailyStatsTableModel model = null;
    JScrollPane resultsPane;
    JTable table;

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

    private DailyStatsFrame()
    {
        super("DailyStatsFrame");
        init();
    }

    public static DailyStatsFrame getInstance()
    {
        if (singleton == null)
            singleton = new DailyStatsFrame();
        return singleton;
    }

    public static void showMe()
    {
        if (singleton == null)
            singleton = new DailyStatsFrame();
        singleton.show();
        DailyGraphFrame.showMe();
    }

    public DailyStatsTableModel getTableModel()
    {
        return model;
    }

    protected void close()
    {
        DailyGraphFrame.closeMe();
        dispose();
        singleton = null;
    }

    private void init()
    {
        setBounds(150, 150, 550, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        //Panel for Insulin Stats
        JPanel InsPanel = new JPanel(new GridLayout(3, 6));
        InsPanel.setBorder(BorderFactory.createTitledBorder("Insulin:"));

        InsPanel.add(new JLabel(props.getIns1Abbr() + ":"));
        InsPanel.add(sumIns1 = new JLabel());
        InsPanel.add(new JLabel("Avg " + props.getIns1Abbr() + ":"));
        InsPanel.add(avgIns1 = new JLabel());
        InsPanel.add(new JLabel("Dose " + props.getIns1Abbr() + ":"));
        InsPanel.add(doseIns1 = new JLabel());

        InsPanel.add(new JLabel(props.getIns2Abbr() + ":"));
        InsPanel.add(sumIns2 = new JLabel());
        InsPanel.add(new JLabel("Avg " + props.getIns2Abbr() + ":"));
        InsPanel.add(avgIns2 = new JLabel());
        InsPanel.add(new JLabel("Dose " + props.getIns2Abbr() + ":"));
        InsPanel.add(doseIns2 = new JLabel());

        InsPanel.add(new JLabel("Total:"));
        InsPanel.add(sumIns = new JLabel());
        InsPanel.add(new JLabel("Avg Ins:"));
        InsPanel.add(avgIns = new JLabel());
        InsPanel.add(new JLabel("Dose Ins:"));
        InsPanel.add(doseIns = new JLabel());

        //Panel for BU Stats
        JPanel BUPanel = new JPanel(new GridLayout(1, 6));
        BUPanel.setBorder(BorderFactory.createTitledBorder("Bread Units:"));

        BUPanel.add(new JLabel("Sum:"));
        BUPanel.add(sumBE = new JLabel());
        BUPanel.add(new JLabel("Avg:"));
        BUPanel.add(avgBE = new JLabel());
        BUPanel.add(new JLabel("Meals:"));
        BUPanel.add(meals = new JLabel());

        //Panel for BG Stats
        JPanel BGPanel = new JPanel(new GridLayout(0, 6));
        BGPanel.setBorder(BorderFactory.createTitledBorder("Blood Glucose:"));

        BGPanel.add(new JLabel("Avg BG:"));
        BGPanel.add(avgBG = new JLabel());
        BGPanel.add(new JLabel("Highest:"));
        BGPanel.add(highestBG = new JLabel());
        BGPanel.add(new JLabel("Readings:"));
        BGPanel.add(readings = new JLabel());
        BGPanel.add(new JLabel("StdDev:"));
        BGPanel.add(stdDev = new JLabel());
        BGPanel.add(new JLabel("Lowest:"));
        BGPanel.add(lowestBG = new JLabel());

        Box dayStats = Box.createVerticalBox();
        dayStats.add(InsPanel);
        dayStats.add(BUPanel);
        dayStats.add(BGPanel);

        JPanel dayHeader = new JPanel();
        dayHeader.setLayout(new BorderLayout());

        JPanel dayCalendar = new JPanel();
        dayCalendar.setBorder(BorderFactory.createTitledBorder("Date:"));

        final calendarPane calPane = new calendarPane();
        calPane.addCalendarListener(new CalendarListener()
        {
            public void dateHasChanged(CalendarEvent e)
            {
                dayData = dbH.getDayStats(new Date(e.getNewDate()));
                model.setDailyValues(dayData);
                saveButton.setEnabled(false);
                updateLabels();
                DailyGraphFrame.setDailyValues(dayData);
            }
        });
        dayCalendar.add(calPane);


        dayHeader.add(dayCalendar, BorderLayout.WEST);
        dayHeader.add(dayStats, BorderLayout.CENTER);

        dayData = dbH.getDayStats(new Date(System.currentTimeMillis()));
        DailyGraphFrame.setDailyValues(dayData);

        model = new DailyStatsTableModel(dayData);
        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                DailyGraphFrame.redraw();
                updateLabels();
                saveButton.setEnabled(true);
            }
        });
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        resultsPane = new JScrollPane(table);

        JPanel EntryBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 2));
        Dimension dim = new Dimension(120, 20);

        JButton addButton = new JButton("Add Row");
        addButton.setPreferredSize(dim);
        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
                AddRowFrame aRF = AddRowFrame.getInstance(model, dayData, sf.format(calPane.getSelectedDate()));
                aRF.show();
            }
        });
        EntryBox.add(addButton);

        JButton delButton = new JButton("Delete Row");
        delButton.setPreferredSize(dim);
        delButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dayData.deleteRow(table.getSelectedRow());
                model.fireTableChanged(null);
            }
        });
        EntryBox.add(delButton);

        saveButton = new JButton("Save");
        saveButton.setPreferredSize(dim);
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dayData.saveDay();
            }
        });
        saveButton.setEnabled(false);
        EntryBox.add(saveButton);

        getContentPane().add(resultsPane, BorderLayout.CENTER);
        getContentPane().add(dayHeader, BorderLayout.NORTH);
        getContentPane().add(EntryBox, BorderLayout.SOUTH);

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

        setVisible(true);

        updateLabels();
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

    public void processWindowEvent(WindowEvent e)
    {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
        }
        super.processWindowEvent(e);
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            close();
        }
    }
}