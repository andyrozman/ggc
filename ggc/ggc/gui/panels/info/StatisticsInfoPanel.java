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
 *  Filename: StatisticsInfoPanel.java
 *  Purpose:  Contains basic statistics for the last week or month.
 *            Something like avgBG last week, Meter readings, ...
 *
 *  Author:   schultd
 */

package ggc.gui.panels.info;


import ggc.data.StatisticValues;
import ggc.core.util.DataAccess;

import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;


public class StatisticsInfoPanel extends AbstractInfoPanel
{
    GregorianCalendar endDate = null;
    GregorianCalendar startDate = null; //new Date(endDate.getTime() - (518400000L)); //now - 6 days in millisec

    JLabel lblAvgBG, lblBGReadings;
    JLabel lblSumBU, lblBUDay, lblCountBU, lblAvgBU, lblBUCountDay;
    JLabel lblSumIns1, lblIns1Day, lblCountIns1, lblAvgIns1, lblIns1CountDay;
    JLabel lblSumIns2, lblIns2Day, lblCountIns2, lblAvgIns2, lblIns2CountDay;

    JLabel lbl_sum_ins1_day_name, lbl_sum_ins1_name, lbl_sum_ins2_day_name, lbl_sum_ins2_name;

    JPanel PanelIns1, PanelIns2;




    public StatisticsInfoPanel()
    {
        super("");

        m_da = DataAccess.getInstance();

        endDate = new GregorianCalendar();
        startDate = new GregorianCalendar();
        startDate.add(java.util.Calendar.DAY_OF_MONTH, -6);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        ((TitledBorder)this.getBorder()).setTitle(m_ic.getMessage("STATISTICS_FOR_LAST_WEEK")+" (" + sdf.format(startDate.getTime()) + " - " + sdf.format(endDate.getTime()) + ")");

        init();
        refreshInfo();
    }

    private void init()
    {
        JPanel PanelBG = new JPanel(new GridLayout(2,2));
        PanelBG.setOpaque(false);
        PanelBG.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BG_STATISTICS")+":"));
        PanelBG.add(new JLabel(m_ic.getMessage("AVG_BG")+":"));
        PanelBG.add(lblAvgBG = new JLabel());
	lblAvgBG.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBG.add(new JLabel(m_ic.getMessage("READINGS")+":"));
        PanelBG.add(lblBGReadings = new JLabel());
	lblBGReadings.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel PanelBU = new JPanel(new GridLayout(5, 2));
        PanelBU.setOpaque(false);
        PanelBU.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BU_STATISTICS")+":"));
        PanelBU.add(new JLabel(m_ic.getMessage("SUM_BU")+":"));
        PanelBU.add(lblSumBU = new JLabel());
	lblSumBU.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("BU_PER_DAY")+":"));
        PanelBU.add(lblBUDay = new JLabel());
	lblBUDay.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("MEALS")+":"));
        PanelBU.add(lblCountBU = new JLabel());
	lblCountBU.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("AVG_MEAL_SIZE")+":"));
        PanelBU.add(lblAvgBU = new JLabel());
	lblAvgBU.setHorizontalAlignment(SwingConstants.CENTER);
        PanelBU.add(new JLabel(m_ic.getMessage("MEALS_PER_DAY")+":"));
        PanelBU.add(lblBUCountDay = new JLabel());
	lblBUCountDay.setHorizontalAlignment(SwingConstants.CENTER);

        PanelIns1 = new JPanel(new GridLayout(5, 2));
        PanelIns1.setOpaque(false);
        PanelIns1.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("INSULIN_1") + " " +m_ic.getMessage("STATISTICS") + ":"));
        PanelIns1.add(lbl_sum_ins1_name = new JLabel(m_ic.getMessage("SUM") + " " + m_ic.getMessage("INS_1") + ":"));
        PanelIns1.add(lblSumIns1 = new JLabel());
	lblSumIns1.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(lbl_sum_ins1_day_name = new JLabel(m_ic.getMessage("INS_1") + " " + m_ic.getMessage("PER_DAY")+":"));
        PanelIns1.add(lblIns1Day = new JLabel());
	lblIns1Day.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(new JLabel(m_ic.getMessage("DOSE")+":"));
        PanelIns1.add(lblCountIns1 = new JLabel());
	lblCountIns1.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(new JLabel(m_ic.getMessage("AVG_DOSE_SIZE")+":"));
        PanelIns1.add(lblAvgIns1 = new JLabel());
	lblAvgIns1.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns1.add(new JLabel(m_ic.getMessage("DOSES_PER_DAY")+":"));
        PanelIns1.add(lblIns1CountDay = new JLabel());
	lblIns1CountDay.setHorizontalAlignment(SwingConstants.CENTER);

        PanelIns2 = new JPanel(new GridLayout(5, 2));
        PanelIns2.setOpaque(false);
        PanelIns2.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("INSULIN_2") + " " + m_ic.getMessage("STATISTICS")+":"));
        PanelIns2.add(lbl_sum_ins2_name = new JLabel(m_ic.getMessage("SUM") + " " + m_ic.getMessage("INS_2") + ":"));
        PanelIns2.add(lblSumIns2 = new JLabel());
	lblSumIns2.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(lbl_sum_ins2_day_name = new JLabel(m_ic.getMessage("INS_2") + " " + m_ic.getMessage("PER_DAY")+":"));
        PanelIns2.add(lblIns2Day = new JLabel());
	lblIns2Day.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(new JLabel(m_ic.getMessage("DOSE")+":"));
        PanelIns2.add(lblCountIns2 = new JLabel());
	lblCountIns2.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(new JLabel(m_ic.getMessage("AVG_DOSE_SIZE")+":"));
        PanelIns2.add(lblAvgIns2 = new JLabel());
	lblAvgIns2.setHorizontalAlignment(SwingConstants.CENTER);
        PanelIns2.add(new JLabel(m_ic.getMessage("DOSES_PER_DAY")+":"));
        PanelIns2.add(lblIns2CountDay = new JLabel());
	lblIns2CountDay.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new GridLayout(2,2));

        add(PanelBG);
        add(PanelBU);
        add(PanelIns1);
        add(PanelIns2);
    }

    @Override
    public void refreshInfo()
    {

        if (!m_da.isDatabaseInitialized()) 
            return;

        StatisticValues sV = new StatisticValues(startDate, endDate);

        //XXX: This can't actually become null since it just got assigned
        //if (sV != null) 
	{
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

	if (first_refresh)
	{
	    PanelIns1.setBorder(BorderFactory.createTitledBorder(m_da.getSettings().getIns1Name() + " " +m_ic.getMessage("STATISTICS") + ":"));
	    PanelIns2.setBorder(BorderFactory.createTitledBorder(m_da.getSettings().getIns2Name() + " " + m_ic.getMessage("STATISTICS")+":"));

	    lbl_sum_ins1_name.setText(m_ic.getMessage("SUM") + " " + m_da.getSettings().getIns1Abbr() + ":");
	    lbl_sum_ins1_day_name.setText(m_da.getSettings().getIns1Abbr() + " " + m_ic.getMessage("PER_DAY")+":");

	    lbl_sum_ins2_name.setText(m_ic.getMessage("SUM") + " " + m_da.getSettings().getIns2Abbr() + ":");
	    lbl_sum_ins2_day_name.setText(m_da.getSettings().getIns2Abbr() + " " + m_ic.getMessage("PER_DAY")+":");

	    first_refresh = false;
	}


    }
}
