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
 *  Filename: DailyStatsPanelL
 *  Purpose:  Enter and view all data for one day. (fix)
 *
 *  Author:   andyrozman {andy@atech-software.com}
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman

package ggc.gui.little;

import ggc.data.DailyStatsTableModel;
import ggc.data.DailyValues;
import ggc.gui.panels.info.AbstractInfoPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.GregorianCalendar;

import javax.swing.JScrollPane;
import javax.swing.JTable;


public class DailyStatsPanelL extends AbstractInfoPanel //extends JPanel implements ActionListener
{
    
    public DailyStatsTableModel model = null;
    JScrollPane resultsPane;
    public JTable table;

    DailyValues dayData;


    public DailyStatsPanelL()
    {
        super(""); 

        setTitle(m_ic.getMessage("DAILY_VALUES") + ": (" + m_da.getCurrentDateString() + ")");
        init();
    }


    public DailyStatsTableModel getTableModel()
    {
        return model;
    }

    @Override
    public void refreshInfo()
    {
        /*
        System.out.println("Refresh Data Table");
        DailyValues dv = m_da.getDayStats(new GregorianCalendar());

        if (dv!=null)
        {
            dayData = dv;
            model.setDailyValues(dayData);
            this.model.fireTableChanged(null);
        }
        */
    }

    private void init()
    {
        this.setLayout(new GridLayout(1,1));

        dayData = m_da.getDayStats(new GregorianCalendar());

        if (dayData==null)
            dayData = new DailyValues();

        model = new DailyStatsTableModel(dayData);
        /*model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                //DailyGraphFrame.redraw();
                //updateLabels();
                //saveButton.setEnabled(true);
            }
        }); */
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        resultsPane = new JScrollPane(table);
        add(resultsPane, BorderLayout.CENTER);

        setVisible(true);
    }


}