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
 *  Filename: StatisticInfoPanel.java
 *  Purpose:  Contains basic statistics for the last week or month.
 *            Something like avgBG last week, Meter readings, ...
 *
 *  Author:   schultd
 */

package ggc.gui.infoPanel;


import ggc.datamodels.StatisticValues;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class StatisticInfoPanel extends AbstractInfoPanel
{
    Date endDate = new Date();
    Date startDate = new Date(endDate.getTime() - (518400000L)); //now - 6 days in millisec

    JLabel lblAvgBG, lblBGReadings;
    JLabel lblSumBU, lblBUDay, lblCountBU, lblAvgBU, lblBUCountDay;
    JLabel lblSumIns1, lblIns1Day, lblCountIns1, lblAvgIns1, lblIns1CountDay;
    JLabel lblSumIns2, lblIns2Day, lblCountIns2, lblAvgIns2, lblIns2CountDay;

    public StatisticInfoPanel()
    {
        super("");

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        ((TitledBorder)this.getBorder()).setTitle("Statistics for last weeks (" + sdf.format(startDate) + " - " + sdf.format(endDate) + ")");

        init();
        refreshInfo();
    }

    private void init()
    {
        JPanel PanelBG = new JPanel(new GridLayout(2,2));
        PanelBG.setOpaque(false);
        PanelBG.setBorder(BorderFactory.createTitledBorder("BG Statistics:"));
        PanelBG.add(new JLabel("Avg BG:"));
        PanelBG.add(lblAvgBG = new JLabel());
        PanelBG.add(new JLabel("Readings:"));
        PanelBG.add(lblBGReadings = new JLabel());

        JPanel PanelBU = new JPanel(new GridLayout(5, 2));
        PanelBU.setOpaque(false);
        PanelBU.setBorder(BorderFactory.createTitledBorder("BU Statistics:"));
        PanelBU.add(new JLabel("Sum BU:"));
        PanelBU.add(lblSumBU = new JLabel());
        PanelBU.add(new JLabel("BU per Day:"));
        PanelBU.add(lblBUDay = new JLabel());
        PanelBU.add(new JLabel("Meals:"));
        PanelBU.add(lblCountBU = new JLabel());
        PanelBU.add(new JLabel("Avg Meal Size:"));
        PanelBU.add(lblAvgBU = new JLabel());
        PanelBU.add(new JLabel("Meals per Day:"));
        PanelBU.add(lblBUCountDay = new JLabel());

        JPanel PanelIns1 = new JPanel(new GridLayout(5, 2));
        PanelIns1.setOpaque(false);
        PanelIns1.setBorder(BorderFactory.createTitledBorder(props.getIns1Name() + " Statistics:"));
        PanelIns1.add(new JLabel("Sum" + props.getIns1Abbr() + ":"));
        PanelIns1.add(lblSumIns1 = new JLabel());
        PanelIns1.add(new JLabel(props.getIns1Abbr() + " per Day:"));
        PanelIns1.add(lblIns1Day = new JLabel());
        PanelIns1.add(new JLabel("Dose:"));
        PanelIns1.add(lblCountIns1 = new JLabel());
        PanelIns1.add(new JLabel("Avg Dose Size:"));
        PanelIns1.add(lblAvgIns1 = new JLabel());
        PanelIns1.add(new JLabel("Doses per Day:"));
        PanelIns1.add(lblIns1CountDay = new JLabel());

        JPanel PanelIns2 = new JPanel(new GridLayout(5, 2));
        PanelIns2.setOpaque(false);
        PanelIns2.setBorder(BorderFactory.createTitledBorder(props.getIns2Name() + " Statistics:"));
        PanelIns2.add(new JLabel("Sum " + props.getIns2Abbr() + ":"));
        PanelIns2.add(lblSumIns2 = new JLabel());
        PanelIns2.add(new JLabel(props.getIns2Abbr() + " per Day:"));
        PanelIns2.add(lblIns2Day = new JLabel());
        PanelIns2.add(new JLabel("Dose:"));
        PanelIns2.add(lblCountIns2 = new JLabel());
        PanelIns2.add(new JLabel("Avg Dose Size:"));
        PanelIns2.add(lblAvgIns2 = new JLabel());
        PanelIns2.add(new JLabel("Doses per Day:"));
        PanelIns2.add(lblIns2CountDay = new JLabel());

        setLayout(new GridLayout(2,2));

        add(PanelBG);
        add(PanelBU);
        add(PanelIns1);
        add(PanelIns2);
    }

    public void refreshInfo()
    {
        StatisticValues sV = new StatisticValues(startDate, endDate);

        if (sV != null) {
            DecimalFormat df = new DecimalFormat("#0.00");

            lblAvgBG.setText(df.format(sV.getAvgBG()));
            lblBGReadings.setText(df.format(sV.getCountBG()));

            lblSumBU.setText(df.format(sV.getSumBU()));
            lblBUDay.setText(df.format(sV.getAvgBUPerDay()));
            lblCountBU.setText(df.format(sV.getCountBU()));
            lblAvgBU.setText(df.format(sV.getAvgBU()));
            lblBUCountDay.setText(df.format(sV.getBUCountPerDay()));

            lblSumIns1.setText(df.format(sV.getSumIns1()));
            lblIns1Day.setText(df.format(sV.getAvgIns1PerDay()));
            lblCountIns1.setText(df.format(sV.getCountIns1()));
            lblAvgIns1.setText(df.format(sV.getAvgIns1()));
            lblIns1CountDay.setText(df.format(sV.getIns1CountPerDay()));

            lblSumIns2.setText(df.format(sV.getSumIns2()));
            lblIns2Day.setText(df.format(sV.getAvgIns2PerDay()));
            lblCountIns2.setText(df.format(sV.getCountIns2()));
            lblAvgIns2.setText(df.format(sV.getAvgIns2()));
            lblIns2CountDay.setText(df.format(sV.getIns2CountPerDay()));
        }
    }
}
