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
 *  Filename: HbA1cInfoPanel.java
 *  Purpose:  Contains your current HbA1c, based on the BG values entered.
 *
 *  Author:   schultd
 */

package gui.infoPanel;


import datamodels.HbA1cValues;
import db.DataBaseHandler;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Date;


public class HbA1cInfoPanel extends AbstractInfoPanel
{
    private JLabel lblHbA1c;
    private JLabel lblVal;
    private JLabel lblAvgBG;
    private JLabel lblReadings;
    private JLabel lblReadingsPerDay;

    public HbA1cInfoPanel()
    {
        super("HbA1c");
        init();
        refreshInfo();
    }

    private void init()
    {
        setLayout(new BorderLayout());

        JPanel lblPanel = new JPanel(new GridLayout(5, 2));
        lblPanel.setBackground(Color.white);
        lblPanel.add(new JLabel("Your current HbA1c:"));
        lblPanel.add(lblHbA1c = new JLabel());
        lblHbA1c.setFont(new Font("Dialog", Font.BOLD, 14));
        lblPanel.add(new JLabel("Valuation:"));
        lblPanel.add(lblVal = new JLabel());
        lblPanel.add(new JLabel("Avg BG:"));
        lblPanel.add(lblAvgBG = new JLabel());
        lblPanel.add(new JLabel("Readings:"));
        lblPanel.add(lblReadings = new JLabel());
        lblPanel.add(new JLabel("Readings / Day:"));
        lblPanel.add(lblReadingsPerDay = new JLabel());

        add(lblPanel, BorderLayout.NORTH);
    }

    public void refreshInfo()
    {
        HbA1cValues hbVal = null;

        if(DataBaseHandler.hasInstance())
            hbVal = DataBaseHandler.getInstance().getHbA1c(new Date(System.currentTimeMillis()));

        DecimalFormat df = new DecimalFormat("#0.00");

        if (hbVal != null) {
            lblHbA1c.setText(df.format(hbVal.getHbA1c_Method1()) + " %");
            lblVal.setText(hbVal.getValuation());
            lblAvgBG.setText(df.format(hbVal.getAvgBG()));
            lblReadings.setText(hbVal.getReadings() + "");
            lblReadingsPerDay.setText(df.format(hbVal.getReadingsPerDay()));
        }
        else
        {
            lblHbA1c.setText("No DataSource");
            lblVal.setText("");
            lblAvgBG.setText("");
            lblReadings.setText("");
            lblReadingsPerDay.setText("");
        }
    }
}
