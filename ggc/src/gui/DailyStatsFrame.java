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

package gui;


import datamodels.calendar.CalendarEvent;
import datamodels.calendar.CalendarListener;
import datamodels.DailyStatsTableModel;
import datamodels.DailyValues;
import gui.calendar.calendarPane;

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

import util.GGCProperties;


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

        Box boxInsSumTxt = Box.createVerticalBox();
        boxInsSumTxt.add(new JLabel(props.getIns1Name() + ":"));
        boxInsSumTxt.add(new JLabel(props.getIns2Name() + ":"));
        boxInsSumTxt.add(new JLabel("Total Ins:"));

        Box boxInsSumVal = Box.createVerticalBox();
        sumIns1 = new JLabel();
        sumIns1.setHorizontalAlignment(JLabel.RIGHT);
        sumIns2 = new JLabel();
        sumIns2.setHorizontalAlignment(JLabel.RIGHT);
        sumIns = new JLabel();
        sumIns.setHorizontalAlignment(JLabel.RIGHT);
        boxInsSumVal.add(sumIns1);
        boxInsSumVal.add(sumIns2);
        boxInsSumVal.add(sumIns);

        Box boxInsSum = Box.createHorizontalBox();
        boxInsSum.add(boxInsSumTxt);
        boxInsSum.add(Box.createHorizontalStrut(10));
        boxInsSum.add(boxInsSumVal);
        boxInsSum.add(Box.createGlue());

        Box boxInsAvgTxt = Box.createVerticalBox();
        boxInsAvgTxt.add(new JLabel("Avg " + props.getIns1Abbr() + ":"));
        boxInsAvgTxt.add(new JLabel("Avg " + props.getIns2Abbr() + ":"));
        boxInsAvgTxt.add(new JLabel("Avg Ins:"));

        Box boxInsAvgVal = Box.createVerticalBox();
        avgIns1 = new JLabel();
        avgIns1.setHorizontalAlignment(JLabel.RIGHT);
        avgIns2 = new JLabel();
        avgIns2.setHorizontalAlignment(JLabel.RIGHT);
        avgIns = new JLabel();
        avgIns.setHorizontalAlignment(JLabel.RIGHT);
        boxInsAvgVal.add(avgIns1);
        boxInsAvgVal.add(avgIns2);
        boxInsAvgVal.add(avgIns);

        Box boxInsAvg = Box.createHorizontalBox();
        boxInsAvg.add(boxInsAvgTxt);
        boxInsAvg.add(Box.createHorizontalStrut(10));
        boxInsAvg.add(boxInsAvgVal);
        boxInsAvg.add(Box.createGlue());

        Box boxInsDoseTxt = Box.createVerticalBox();
        boxInsDoseTxt.add(new JLabel("Dose " + props.getIns1Abbr() + ":"));
        boxInsDoseTxt.add(new JLabel("Dose " + props.getIns2Abbr() + ":"));
        boxInsDoseTxt.add(new JLabel("Dose Ins:"));

        Box boxInsDoseVal = Box.createVerticalBox();
        doseIns1 = new JLabel();
        doseIns1.setHorizontalAlignment(JLabel.RIGHT);
        doseIns2 = new JLabel();
        doseIns2.setHorizontalAlignment(JLabel.RIGHT);
        doseIns = new JLabel();
        doseIns.setHorizontalAlignment(JLabel.RIGHT);
        boxInsDoseVal.add(doseIns1);
        boxInsDoseVal.add(doseIns2);
        boxInsDoseVal.add(doseIns);

        Box boxInsDose = Box.createHorizontalBox();
        boxInsDose.add(boxInsDoseTxt);
        boxInsDose.add(Box.createHorizontalStrut(10));
        boxInsDose.add(boxInsDoseVal);
        boxInsDose.add(Box.createGlue());

        Box boxIns = Box.createHorizontalBox();
        boxIns.add(boxInsSum);
        boxIns.add(Box.createHorizontalStrut(5));
        boxIns.add(boxInsAvg);
        boxIns.add(Box.createHorizontalStrut(5));
        boxIns.add(boxInsDose);

        JPanel panelIns = new JPanel(new BorderLayout());
        panelIns.setBorder(BorderFactory.createTitledBorder("Insulin:"));
        panelIns.add(boxIns, BorderLayout.CENTER);

        Box boxBE = Box.createHorizontalBox();
        boxBE.add(new JLabel("Sum BE:"));
        boxBE.add(Box.createHorizontalStrut(10));
        sumBE = new JLabel();
        sumBE.setHorizontalAlignment(JLabel.RIGHT);
        boxBE.add(sumBE);
        boxBE.add(Box.createGlue());
        boxBE.add(Box.createHorizontalStrut(5));
        boxBE.add(new JLabel("AvgBE:"));
        avgBE = new JLabel();
        avgBE.setHorizontalAlignment(JLabel.RIGHT);
        boxBE.add(Box.createHorizontalStrut(10));
        boxBE.add(avgBE);
        boxBE.add(Box.createGlue());
        boxBE.add(Box.createHorizontalStrut(5));
        boxBE.add(new JLabel("Meals:"));
        boxBE.add(Box.createHorizontalStrut(10));
        meals = new JLabel();
        meals.setHorizontalAlignment(JLabel.RIGHT);
        boxBE.add(meals);
        boxBE.add(Box.createGlue());

        JPanel panelBE = new JPanel(new BorderLayout());
        panelBE.setBorder(BorderFactory.createTitledBorder("BE:"));
        panelBE.add(boxBE, BorderLayout.CENTER);

        Box boxBGStatTxt = Box.createVerticalBox();
        boxBGStatTxt.add(new JLabel("Avg BG:"));
        boxBGStatTxt.add(new JLabel("StdDev:"));

        Box boxBGStatVal = Box.createVerticalBox();
        avgBG = new JLabel();
        avgBG.setHorizontalAlignment(JLabel.RIGHT);
        stdDev = new JLabel();
        stdDev.setHorizontalAlignment(JLabel.RIGHT);
        boxBGStatVal.add(avgBG);
        boxBGStatVal.add(stdDev);

        Box boxBGStat = Box.createHorizontalBox();
        boxBGStat.add(boxBGStatTxt);
        boxBGStat.add(Box.createHorizontalStrut(10));
        boxBGStat.add(boxBGStatVal);
        boxBGStat.add(Box.createGlue());

        Box boxBGLHTxt = Box.createVerticalBox();
        boxBGLHTxt.add(new JLabel("Highest:"));
        boxBGLHTxt.add(new JLabel("Lowest:"));

        Box boxBGLHVal = Box.createVerticalBox();
        highestBG = new JLabel();
        highestBG.setHorizontalAlignment(JLabel.LEFT);
        lowestBG = new JLabel();
        lowestBG.setHorizontalAlignment(JLabel.RIGHT);
        boxBGLHVal.add(highestBG);
        boxBGLHVal.add(lowestBG);

        Box boxBGLH = Box.createHorizontalBox();
        boxBGLH.add(boxBGLHTxt);
        boxBGLH.add(Box.createHorizontalStrut(10));
        boxBGLH.add(boxBGLHVal);
        boxBGLH.add(Box.createGlue());

        Box boxBGReadingsTxt = Box.createVerticalBox();
        boxBGReadingsTxt.add(new JLabel("Readings:"));

        Box boxBGReadingsVal = Box.createVerticalBox();
        readings = new JLabel();
        readings.setHorizontalAlignment(JLabel.RIGHT);
        boxBGReadingsVal.add(readings);

        Box boxBGReadings = Box.createHorizontalBox();
        boxBGReadings.add(boxBGReadingsTxt);
        boxBGReadings.add(Box.createHorizontalStrut(10));
        boxBGReadings.add(boxBGReadingsVal);
        boxBGReadings.add(Box.createGlue());

        Box boxBG = Box.createHorizontalBox();
        boxBG.add(boxBGStat);
        boxBG.add(Box.createHorizontalStrut(5));
        boxBG.add(boxBGLH);
        boxBG.add(Box.createHorizontalStrut(5));
        boxBG.add(boxBGReadings);
        boxBG.setBackground(Color.green);

        JPanel panelBG = new JPanel(new BorderLayout());
        panelBG.setBorder(BorderFactory.createTitledBorder("Blood Glucose:"));
        panelBG.add(boxBG, BorderLayout.CENTER);

        Box dayStats = Box.createVerticalBox();
        dayStats.add(panelIns);
        dayStats.add(panelBE);
        dayStats.add(panelBG);

        JPanel dayHeader = new JPanel();
        dayHeader.setLayout(new BorderLayout());

        JPanel dayCalendar = new JPanel();
        dayCalendar.setBorder(BorderFactory.createTitledBorder("Date:"));

        final calendarPane calPane = new calendarPane();
        calPane.addCalendarListener(new CalendarListener()
        {
            public void dateHasChanged(CalendarEvent e)
            {
                dayData.setDateAndUpdate(e.getNewDate());
                model.fireTableChanged(null);
                saveButton.setEnabled(false);
                updateLabels();
                DailyGraphFrame.redraw();
            }
        });
        dayCalendar.add(calPane);


        dayHeader.add(dayCalendar, BorderLayout.WEST);
        dayHeader.add(dayStats, BorderLayout.CENTER);

        dayData = DailyValues.getInstance();
        dayData.setDateAndUpdate(System.currentTimeMillis());
        model = new DailyStatsTableModel(dayData);
        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                DailyGraphFrame.redraw();
                saveButton.setEnabled(true);
            }
        });
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultsPane = new JScrollPane(table);

        JPanel EntryBox = new JPanel(new FlowLayout(FlowLayout.RIGHT,1,2));
        Dimension dim = new Dimension(120,20);

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