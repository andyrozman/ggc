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
 *  Filename: DailyStatsControlsL
 *  Purpose:  Enter and view all data for one day. (fix)
 *
 *  Author:   andyrozman {andy@atech-software.com}
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman

package ggc.gui.little;


import ggc.data.DailyValues;
import ggc.gui.dialogs.DailyGraphDialog;
import ggc.gui.dialogs.DailyRowDialog;
import ggc.gui.panels.info.AbstractInfoPanel;
import ggc.core.util.I18nControl;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class DailyStatsControlsL extends AbstractInfoPanel implements ActionListener
{
    
    GGCLittle m_little = null;
    JButton[] buttons = new JButton[4];
    MainLittlePanel m_mlp;
    GregorianCalendar m_gc = null;

    //GGCDb m_db = m_da.getDb();

    public DailyStatsControlsL(MainLittlePanel mlp)
    {
        super("");
        m_mlp = mlp;
        m_gc = new GregorianCalendar();
        init();
    }




    private void init()
    {

        this.setLayout(new GridLayout(2,2));
        setBorder(BorderFactory.createTitledBorder(I18nControl.getInstance().getMessage("DAILY_CONTROL")+":"));

        Dimension dim = new Dimension(120, 20);

        JButton button = new JButton(m_ic.getMessage("SHOW_DAILY_GRAPH"));
        button.setPreferredSize(dim);
        button.setActionCommand("show_daily_graph");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[0] = button;
        this.add(button);

        button = new JButton(m_ic.getMessage("ADD_ROW"));
        button.setPreferredSize(dim);
        button.setActionCommand("add_row");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[1] = button;
        this.add(button);

        button = new JButton(m_ic.getMessage("EDIT_ROW"));
        button.setPreferredSize(dim);
        button.setActionCommand("edit_row");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[2] = button;
        this.add(button);

        button = new JButton(m_ic.getMessage("DELETE_ROW"));
        button.setPreferredSize(dim);
        button.setActionCommand("delete_row");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[3] = button;
        this.add(button);

        setVisible(true);
    }


    @Override
    public void refreshInfo()
    {
        boolean start = m_da.getDb().isDbStarted();

        for (int i=0; i<this.buttons.length; i++)
        {
            buttons[i].setEnabled(start);
        }
    }

    public JTable getTable()
    {
        return this.m_mlp.dailyStats.table;
    }


    public DailyValues getDayData()
    {
        return this.m_mlp.dailyStats.model.getDailyValues();
    }

    public void reloadTable()
    {
        //m_da.getDayStats(new GregorianCalendar());
        m_da.loadDailySettingsLittle(m_gc, true);
        this.m_mlp.dailyStats.model.setDailyValues(m_da.getDayStats(m_gc));
    }

    public JFrame getFrame()
    {
        return this.m_mlp.m_little;
    }


    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();

        if (command.equals("add_row"))
        {
            DailyValues dv = getDayData();

            DailyRowDialog aRF = new DailyRowDialog(dv, m_da.getCurrentDateString(), getFrame());

            if (aRF.actionSuccesful()) 
            {
                m_da.getDb().saveDayStats(dv);
                reloadTable();
            }
        }
        else if (command.equals("edit_row"))
        {
            int srow = getTable().getSelectedRow();

            if (srow==-1) 
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            DailyValues dv = getDayData();

            DailyRowDialog aRF = new DailyRowDialog(dv.getRowAt(srow), getFrame());

            if (aRF.actionSuccesful()) 
            {
                m_da.getDb().saveDayStats(dv);
                reloadTable();
            }

        }
        else if (command.equals("delete_row"))
        {
            int srow = getTable().getSelectedRow();

            if (srow==-1) 
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            try 
            {
                DailyValues dv = getDayData();

                dv.deleteRow(srow);

                m_da.getDb().saveDayStats(dv);
                reloadTable();
            }
            catch (Exception ex) 
            {
                System.out.println("DailyStatsDialog:Action:Delete Row: " + ex);
            }
        } 
        else if (command.equals("show_daily_graph"))
        {
            DailyGraphDialog dgd = new DailyGraphDialog(m_mlp.m_little);
            dgd.setDailyValues(getDayData());
        }
        else
            System.out.println("DailyStatsDialog:Unknown Action: " + command);
    }

/*
	String command = e.getActionCommand();

	if (command.equals("add_row"))
	{
	    //SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
	    //AddRowFrame aRF = AddRowFrame.getInstance(model, dayData, sf.format(System.currentTimeMillis()));
	    //aRF.show();
	}
	else if (command.equals("delete_row"))
	{
	    //dayData.deleteRow(m_little.m_infoPanel.dailyStats.table.getSelectedRow());
	    //model.fireTableChanged(null);
	}
	else if (command.equals("show_daily_graph"))
	{

	}

    }

*/
    

   
    
}