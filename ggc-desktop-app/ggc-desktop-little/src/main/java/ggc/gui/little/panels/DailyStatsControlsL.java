package ggc.gui.little.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import ggc.core.data.DailyValues;
import ggc.core.util.DataAccess;
import ggc.gui.little.GGCLittle;
import ggc.gui.main.panels.AbstractInfoPanel;
import ggc.gui.main.panels.InfoPanelType;
import info.clearthought.layout.TableLayout;

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
 *  Filename:     DailyStatsControlsL  
 *  Description:  Panel for Daily Stats Control
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DailyStatsControlsL extends AbstractInfoPanel implements ActionListener
{

    private static final long serialVersionUID = -3682024667811580299L;
    GGCLittle m_little = null;
    JButton[] buttons = new JButton[4];
    // MainLittlePanel m_mlp;
    // GregorianCalendar m_gc = null;
    Map<String, JButton> buttonMap = new HashMap<String, JButton>();


    // private I18nControlAbstract i18nControl =
    // DataAm_da.getI18nControlInstance();

    /**
     * Constructor
     * 
     * @param mlp
     */
    public DailyStatsControlsL(MainLittlePanel mlp)
    {
        super("");
        m_little = mlp.getApplication();
        // m_gc = new GregorianCalendar();
        init();
    }


    private void init()
    {

        double sizes[][] = { { 0.50, 0.50 }, { 0.25, 0.25, 0.25, 0.25 }, };

        setLayout(new TableLayout(sizes));

        // add(new JLabel(i18nControl.getMessage(i18nKey) + ":"), "1, " +
        // currentLine);

        addButton("ADD_ROW", "add_entry", "0,0");
        addButton("EDIT_ROW", "edit_entry", "1,0");
        addButton("DELETE_ROW", "delete_entry", "0,1");
        addButton("SHOW_DAILY_GRAPH", "show_graph", "1,1");
        addButton("READ_METER", "read_meter", "0,2");
        addButton("READ_PUMP", "read_pump", "1,2");
        addButton("READ_CGMS", "read_cgms", "0,3");
        addButton("MN_HELP", "hlp_help", "1,3");

        if (true)
            return;

        this.setLayout(new GridLayout(4, 2));
        setBorder(BorderFactory.createTitledBorder(
            DataAccess.getInstance().getI18nControlInstance().getMessage("DAILY_CONTROL") + ":"));

        // Dimension dim = new Dimension(120, 20);

        JButton button = new JButton(i18nControl.getMessage("SHOW_DAILY_GRAPH"));
        // button.setPreferredSize(dim);
        button.setActionCommand("show_daily_graph");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[0] = button;
        this.add(button);

        button = new JButton(i18nControl.getMessage("ADD_ROW"));
        // button.setPreferredSize(dim);
        button.setActionCommand("add_row");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[1] = button;
        this.add(button);

        button = new JButton(i18nControl.getMessage("EDIT_ROW"));
        // button.setPreferredSize(dim);
        button.setActionCommand("edit_row");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[2] = button;
        this.add(button);

        button = new JButton(i18nControl.getMessage("DELETE_ROW"));
        // button.setPreferredSize(dim);
        button.setActionCommand("delete_row");
        button.addActionListener(this);
        button.setEnabled(false);
        buttons[3] = button;
        this.add(button);

        setVisible(true);
    }


    private void addButton(String key, String actionCommand, String position)
    {
        JButton button = new JButton(i18nControl.getMessage(key));
        // button.setPreferredSize(dim);
        button.setActionCommand(actionCommand);
        button.addActionListener(this.m_little);
        button.setEnabled(false);
        // buttons[3] = button;

        buttonMap.put(actionCommand, button);

        add(button, position);
    }


    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!! refreshInfo: DailyStats");

        boolean start = dataAccess.getDb().isDbStarted();

        for (JButton button : this.buttons)
        {
            button.setEnabled(start);
        }
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.DailyValuesController;
    }


    // /**
    // * Get Table
    // *
    // * @return
    // */
    // public JTable getTable()
    // {
    // return this.m_mlp.dailyStats.getTable();
    // }

    /**
     * Get Day Data
     * 
     * @return
     */
    public DailyValues getDayData()
    {
        // FIXME
        return null;
        // return this.m_mlp.dailyStats.getTableModel().getDailyValues();
    }

    // /**
    // * Reload Table
    // */
    // public void reloadTable()
    // {
    // // dataAccess.getDayStats(new GregorianCalendar());
    // dataAccess.loadDailySettingsLittle(m_gc, true);
    // this.m_mlp.dailyStats.getTableModel().setDataValues(dataAccess.getDayStats(m_gc));
    // }


    // /**
    // * Get Frame
    // * @return
    // */
    // public JFrame getFrame()
    // {
    // return this.m_mlp.m_little;
    // }

    /**
     * Action Performed
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {

        // String command = e.getActionCommand();
        //
        // if (command.equals("add_row"))
        // {
        // DailyValues dv = getDayData();
        //
        // DailyRowDialog aRF = new DailyRowDialog(dv,
        // dataAccess.getCurrentDateString(), getFrame());
        //
        // if (aRF.actionSuccessful())
        // {
        // dataAccess.getDb().saveDayStats(dv);
        // reloadTable();
        // }
        // }
        // else if (command.equals("edit_row"))
        // {
        // int srow = getTable().getSelectedRow();
        //
        // if (srow == -1)
        // {
        // JOptionPane.showMessageDialog(this,
        // i18nControl.getMessage("SELECT_ROW_FIRST"),
        // i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
        // return;
        // }
        //
        // DailyValues dv = getDayData();
        //
        // DailyRowDialog aRF = new DailyRowDialog(dv.getRow(srow), getFrame());
        //
        // if (aRF.actionSuccessful())
        // {
        // dataAccess.getDb().saveDayStats(dv);
        // reloadTable();
        // }
        //
        // }
        // else if (command.equals("delete_row"))
        // {
        // int srow = getTable().getSelectedRow();
        //
        // if (srow == -1)
        // {
        // JOptionPane.showMessageDialog(this,
        // i18nControl.getMessage("SELECT_ROW_FIRST"),
        // i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
        // return;
        // }
        //
        // try
        // {
        // DailyValues dv = getDayData();
        //
        // dv.deleteRow(srow);
        //
        // dataAccess.getDb().saveDayStats(dv);
        // reloadTable();
        // }
        // catch (Exception ex)
        // {
        // System.out.println("DailyStatsDialog:Action:Delete Row: " + ex);
        // }
        // }
        // else if (command.equals("show_daily_graph"))
        // {
        // // DailyGraphDialog dgd = new DailyGraphDialog(m_mlp.m_little);
        // // dgd.setDailyValues(getDayData());
        // }
        // else
        // {
        // System.out.println("DailyStatsDialog:Unknown Action: " + command);
        // }
    }

    /*
     * String command = e.getActionCommand();
     * if (command.equals("add_row"))
     * {
     * //SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
     * //AddRowFrame aRF = AddRowFrame.getInstance(model, dayData,
     * sf.format(System.currentTimeMillis()));
     * //aRF.show();
     * }
     * else if (command.equals("delete_row"))
     * {
     * //dayData.deleteRow(m_little.m_infoPanel.dailyStats.table.getSelectedRow(
     * )
     * );
     * //model.fireTableChanged(null);
     * }
     * else if (command.equals("show_daily_graph"))
     * {
     * }
     * }
     */


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!! doRefresh: DailyStats");

        for (JButton button : buttonMap.values())
        {
            button.setEnabled(true);
        }

    }

}
