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

package ggc.gui.little;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ggc.data.DailyStatsTableModel;
import ggc.data.DailyValues;
import ggc.data.calendar.CalendarEvent;
import ggc.data.calendar.CalendarListener;
//import ggc.db.DataBaseHandler;
import ggc.db.GGCDb;
import ggc.gui.calendar.CalendarPane;
import ggc.gui.dialogs.*;
import ggc.gui.dialogs.DailyRowDialog;
//import ggc.little.GGCLittle;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;
import ggc.gui.panels.info.AbstractInfoPanel;


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