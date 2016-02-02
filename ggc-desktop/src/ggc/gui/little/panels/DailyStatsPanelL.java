package ggc.gui.little.panels;

import java.awt.*;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import ggc.core.data.DailyStatsTableModel;
import ggc.core.data.DailyValues;
import ggc.gui.main.panels.AbstractInfoPanel;
import ggc.gui.main.panels.InfoPanelType;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     DailyStatsPanelL  
 *  Description:  Panel for Daily Stats
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DailyStatsPanelL extends AbstractInfoPanel
{

    private static final long serialVersionUID = 3519092324025060409L;
    private DailyStatsTableModel model = null;
    JScrollPane resultsPane;
    private JTable table;
    private DailyValues dayData;


    /**
     * Constructor
     */
    public DailyStatsPanelL()
    {
        super(null);

        setTitle(m_ic.getMessage("DAILY_VALUES") + ": (" + m_da.getCurrentDateString() + ")");
        init();
    }


    public void setTitle(String title)
    {
        TitledBorder titledBorder = (TitledBorder) this.getBorder();
        titledBorder.setTitle(title);
    }


    /**
     * Get Table Model
     * 
     * @return
     */
    public DailyStatsTableModel getTableModel()
    {
        return model;
    }


    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
        refreshDayData();
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.DailyValues;
    }


    /**
     * Get Table
     * 
     * @return
     */
    public JTable getTable()
    {
        return this.table;
    }


    /**
     * Get Day Data
     * 
     * @return
     */
    public DailyValues getDayData()
    {
        return this.dayData;
    }


    private void refreshDayData()
    {
        DailyValues dv = m_da.getDayStats(new GregorianCalendar());

        if (dv != null)
        {
            dayData = dv;
            model.setDailyValues(dayData);
            this.model.fireTableChanged(null);
        }
    }


    private void init()
    {
        this.setLayout(new GridLayout(1, 1));

        dayData = m_da.getDayStats(new GregorianCalendar());

        // if (dayData==null)
        // dayData = DataAccess.getInstance().getDayStats(new
        // GregorianCalendar());
        // refreshDayData();

        System.out.println("DailyStatPanelL: " + dayData);

        model = new DailyStatsTableModel(dayData);
        /*
         * model.addTableModelListener(new TableModelListener()
         * {
         * public void tableChanged(TableModelEvent e)
         * {
         * //DailyGraphFrame.redraw();
         * //updateLabels();
         * //saveButton.setEnabled(true);
         * }
         * });
         */
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        resultsPane = new JScrollPane(table);
        add(resultsPane, BorderLayout.CENTER);

        setVisible(true);
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
    }

}
