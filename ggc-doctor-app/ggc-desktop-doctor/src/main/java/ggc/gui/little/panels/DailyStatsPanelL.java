package ggc.gui.little.panels;

import ggc.core.data.DailyStatsTableModel;
import ggc.core.data.DailyValues;
import ggc.gui.panels.info.AbstractInfoPanel;
import ggc.gui.panels.info.InfoPanelsIds;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.GregorianCalendar;

import javax.swing.JScrollPane;
import javax.swing.JTable;

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


public class DailyStatsPanelL extends AbstractInfoPanel //extends JPanel implements ActionListener
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
        super(""); 

        setTitle(m_ic.getMessage("DAILY_VALUES") + ": (" + m_da.getCurrentDateString() + ")");
        init();
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

        if (dv!=null)
        {
            dayData = dv;
            model.setDailyValues(dayData);
            this.model.fireTableChanged(null);
        }
    }
    
    
    
    private void init()
    {
        this.setLayout(new GridLayout(1,1));

        dayData = m_da.getDayStats(new GregorianCalendar());

        //if (dayData==null)
        //    dayData = DataAccess.getInstance().getDayStats(new GregorianCalendar());
//        refreshDayData();
  
        System.out.println("DailyStatPanelL: " + dayData);
        
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

    
    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public String getTabName()
    {
        return "DailyStatsPanel";
    }

    
    /**
     * Do Refresh - This method can do Refresh
     */
    public void doRefresh()
    {
    }
    

    /**
     * Get Panel Id
     * 
     * @return id of panel
     */
    @Override
    public int getPanelId()
    {
        return InfoPanelsIds.INFO_PANEL_NONE;
    }
    
    
}