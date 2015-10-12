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
 *      Changes by andy 
 *
 */

package ggc.gui.pen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.graphics.calendar.CalendarEvent;
import com.atech.graphics.calendar.CalendarListener;
import com.atech.graphics.calendar.CalendarPane;
import com.atech.graphics.graphs.GraphViewer;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.DailyStatsTableModel;
import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.graph.GraphViewDaily;
import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import ggc.core.util.RefreshInfo;

/**
 *  Application: GGC - GNU Gluco Control
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
 *  Filename: DailyStatsFrame
 *  Purpose:  Enter and view data, selecttor for data, displayed one day.
 *
 *  Author:   schultd
 *  Changes:  andyrozman {andy@atech-software.com}
 */

public class DailyStatsDialog extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = 8762133987606084209L;

    private static Log log = LogFactory.getLog(DailyStatsDialog.class);

    // private I18nControl i18nControl = I18nControl.getInstance();
    private DataAccess m_da = null; // DataAccess.getInstance();
    private I18nControlAbstract m_ic = null;

    DailyStatsTableModel model = null;
    JScrollPane resultsPane;
    GregorianCalendar current_gc;

    JTable table;

    // private boolean save_needed = false;

    CalendarPane calPane;

    JLabel sumIns1, sumIns2, sumIns;
    JLabel avgIns1, avgIns2, avgIns;
    JLabel doseIns1, doseIns2, doseIns;
    JLabel sumBE, avgBE, meals;
    JLabel avgBG, highestBG, lowestBG;
    JLabel readings, stdDev;

    JLabel lblDate;
    JButton saveButton;
    JButton help_button;
    DailyValues dayData;

    GGCDb m_db = null;


    public DailyStatsDialog(DataAccess da)
    {
        super(da.getMainParent(), "DailyStatsDialog", false);

        current_gc = new GregorianCalendar();

        da.addComponent(this);

        this.m_da = da;
        this.m_db = m_da.getDb();
        this.m_ic = da.getI18nControlInstance();
        setTitle(this.current_gc);

        setSize(700, 470);
        ATSwingUtils.centerJDialog(this, m_da.getMainParent());

        init();
    }


    private void setTitle(GregorianCalendar gc)
    {
        setTitle(m_ic.getMessage("DAILYSTATSFRAME") + "  [" + gc.get(Calendar.DAY_OF_MONTH) + "."
                + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR) + "]");
    }


    private DailyStatsTableModel getTableModel()
    {
        return model;
    }


    @SuppressWarnings("unused")
    private DailyStatsDialog getThisParent()
    {
        return this;
    }


    protected void close()
    {
        DataAccess.getInstance().loadDailySettings(new GregorianCalendar(), true);
        // MainFrame mf = DataAccess.getInstance().getParent();

        m_da.setChangeOnEventSource(DataAccess.OBSERVABLE_PANELS, RefreshInfo.PANEL_GROUP_ALL_DATA);

        // mf.informationPanel.refreshPanels();
        m_da.removeComponent(this);
        this.dispose();
    }


    private void init()
    {
        // setBounds(150, 150, 550, 500);

        // Panel for Insulin Stats
        JPanel InsPanel = new JPanel(new GridLayout(3, 6));
        InsPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("INSULIN") + ":"));

        InsPanel.add(new JLabel(m_ic.getMessage("BOLUS_INSULIN_SHORT") + ":"));
        InsPanel.add(sumIns1 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVERAGE") + ":"));
        InsPanel.add(avgIns1 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSES") + ":"));
        InsPanel.add(doseIns1 = new JLabel());

        InsPanel.add(new JLabel(m_ic.getMessage("BASAL_INSULIN_SHORT") + ":"));
        InsPanel.add(sumIns2 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVERAGE") + ":"));
        InsPanel.add(avgIns2 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSES") + ":"));
        InsPanel.add(doseIns2 = new JLabel());

        InsPanel.add(new JLabel(m_ic.getMessage("TOTAL") + ":"));
        InsPanel.add(sumIns = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVG_INS") + ":"));
        InsPanel.add(avgIns = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSE_INS") + ":"));
        InsPanel.add(doseIns = new JLabel());

        // Panel for BU Stats
        JPanel BUPanel = new JPanel(new GridLayout(1, 6));
        BUPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("CH_LONG") + ":"));

        BUPanel.add(new JLabel(m_ic.getMessage("SUM") + ":"));
        BUPanel.add(sumBE = new JLabel());
        BUPanel.add(new JLabel(m_ic.getMessage("AVG") + ":"));
        BUPanel.add(avgBE = new JLabel());
        BUPanel.add(new JLabel(m_ic.getMessage("MEALS") + ":"));
        BUPanel.add(meals = new JLabel());

        // Panel for BG Stats
        JPanel BGPanel = new JPanel(new GridLayout(0, 6));
        BGPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BLOOD_GLUCOSE") + ":"));

        BGPanel.add(new JLabel(m_ic.getMessage("AVG_BG") + ":"));
        BGPanel.add(avgBG = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("HIGHEST") + ":"));
        BGPanel.add(highestBG = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("READINGS") + ":"));
        BGPanel.add(readings = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("STD_DEV") + ":"));
        BGPanel.add(stdDev = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("LOWEST") + ":"));
        BGPanel.add(lowestBG = new JLabel());

        Box dayStats = Box.createVerticalBox();
        dayStats.add(InsPanel);
        dayStats.add(BUPanel);
        dayStats.add(BGPanel);

        JPanel dayHeader = new JPanel();
        dayHeader.setLayout(new BorderLayout());

        JPanel dayCalendar = new JPanel();
        dayCalendar.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("DATE") + ":"));

        calPane = new CalendarPane(m_da);
        calPane.addCalendarListener(new CalendarListener()
        {

            public void dateHasChanged(CalendarEvent e)
            {
                // System.out.println("dateHasChanged");

                // GregorianCalendar gc = e.getNewCalendar();

                current_gc = e.getNewCalendar();

                dayData = m_da.getDb().getDayStats(current_gc);

                model.setDailyValues(dayData);
                // setDailyValues(dayData);
                // saveButton.setEnabled(false);
                updateLabels();
                setTitle(current_gc);
                getTableModel().fireTableChanged(null);
                // x dailyGraphWindow.setDailyValues(dayData);

                // DailyGraphFrame.setDailyValues(dayData);
            }
        });
        // calPane.setBounds(10, 10, 300, 200);
        dayCalendar.add(calPane);

        dayHeader.add(dayCalendar, BorderLayout.WEST);
        dayHeader.add(dayStats, BorderLayout.CENTER);

        dayData = DataAccess.getInstance().getDayStats(new GregorianCalendar());

        // dbH.getDayStats(new Grenew Date(System.currentTimeMillis()));
        // dailyGraphWindow.setDailyValues(dayData);
        // DailyGraphFrame.setDailyValues(dayData);

        model = new DailyStatsTableModel(dayData);
        model.addTableModelListener(new TableModelListener()
        {

            public void tableChanged(TableModelEvent e)
            {
                // dailyGraphWindow.repaint();
                // DailyGraphFrame.repaint(); //.redraw();
                updateLabels();
                // saveButton.setEnabled(true);
            }
        });
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // MouseAdapter ma = new MouseAdapter();

        table.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                {
                    editRow();
                }
            }
        });
        // MouseListener ml = new MouseListener();

        resultsPane = new JScrollPane(table);

        Dimension dim = new Dimension(110, 25);

        JPanel gg = new JPanel();
        gg.setLayout(new BorderLayout());
        // gg.setPreferredSize(dim);

        JPanel EntryBox1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));

        JButton tButton = new JButton("  " + m_ic.getMessage("GRAPH"));
        tButton.setPreferredSize(dim);
        tButton.setIcon(ATSwingUtils.getImageIcon_22x22("course.png", this, m_da));
        // tButton.setMaximumSize(dim);
        tButton.setActionCommand("show_daily_graph");
        tButton.addActionListener(this);

        EntryBox1.add(tButton);

        help_button = ATSwingUtils.createHelpButtonBySize(110, 25, this, m_da);

        EntryBox1.add(help_button);

        gg.add(EntryBox1, BorderLayout.WEST);

        JPanel EntryBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 2));
        // Dimension dim = new Dimension(120, 20);

        JButton addButton = new JButton("  " + m_ic.getMessage("ADD"));
        addButton.setPreferredSize(dim);
        addButton.setIcon(ATSwingUtils.getImageIcon_22x22("table_add.png", this, m_da));
        addButton.setActionCommand("add_row");
        addButton.addActionListener(this);
        EntryBox.add(addButton);

        JButton editButton = new JButton("  " + m_ic.getMessage("EDIT"));
        editButton.setPreferredSize(dim);
        editButton.setIcon(ATSwingUtils.getImageIcon_22x22("table_edit.png", this, m_da));
        editButton.setActionCommand("edit_row");
        editButton.addActionListener(this);
        EntryBox.add(editButton);

        JButton delButton = new JButton("  " + m_ic.getMessage("DELETE"));
        delButton.setPreferredSize(dim);
        delButton.setIcon(ATSwingUtils.getImageIcon_22x22("table_delete.png", this, m_da));
        delButton.setActionCommand("delete_row");
        delButton.addActionListener(this);
        EntryBox.add(delButton);

        saveButton = new JButton("  " + m_ic.getMessage("CLOSE"));
        saveButton.setPreferredSize(dim);
        saveButton.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, m_da));
        saveButton.setActionCommand("close");
        saveButton.addActionListener(this);
        EntryBox.add(saveButton);

        gg.add(EntryBox, BorderLayout.EAST);

        getContentPane().add(resultsPane, BorderLayout.CENTER);
        getContentPane().add(dayHeader, BorderLayout.NORTH);
        // getContentPane().add(EntryBox, BorderLayout.SOUTH);
        getContentPane().add(gg, BorderLayout.SOUTH);

        updateLabels();

        m_da.enableHelp(this);

        setVisible(true);
    }


    private void updateLabels()
    {
        if (dayData == null)
            return;

        DecimalFormat df = new DecimalFormat("#0.0");

        sumIns1.setText(df.format(dayData.getSumBolus()));
        sumIns2.setText(df.format(dayData.getSumBasal()));
        sumIns.setText(df.format(dayData.getSumBasalBolus()));

        avgIns1.setText(df.format(dayData.getAvgBolus()));
        avgIns2.setText(df.format(dayData.getAvgBasal()));
        avgIns.setText(df.format(dayData.getAvgBasalBolus()));

        doseIns1.setText(dayData.getBolusCount() + "");
        doseIns2.setText(dayData.getBasalCount() + "");
        doseIns.setText(dayData.getBasalBolusCount() + "");

        /*
         * sumIns1.setText(df.format(dayData.get.getSumIns1()));
         * sumIns2.setText(df.format(dayData.getSumIns2()));
         * sumIns.setText(df.format(dayData.getSumIns()));
         * avgIns1.setText(df.format(dayData.getAvgIns1()));
         * avgIns2.setText(df.format(dayData.getAvgIns2()));
         * avgIns.setText(df.format(dayData.getAvgIns()));
         * doseIns1.setText(dayData.getIns1Count() + "");
         * doseIns2.setText(dayData.getIns2Count() + "");
         * doseIns.setText(dayData.getInsCount() + "");
         */
        sumBE.setText(df.format(dayData.getSumCH()));
        avgBE.setText(df.format(dayData.getAvgCH()));
        meals.setText(dayData.getCHCount() + "");

        avgBG.setText(df.format(dayData.getAvgBG()));
        stdDev.setText(df.format(dayData.getStdDev()));
        highestBG.setText(df.format(dayData.getHighestBG()));
        lowestBG.setText(df.format(dayData.getLowestBG()));
        readings.setText(dayData.getBGCount() + "");
    }


    /*
     * public void processWindowEvent(WindowEvent e) { if (e.getID() ==
     * WindowEvent.WINDOW_CLOSING) { this.dispose(); }
     * super.processWindowEvent(e); }
     */

    /**
     * Action Performed
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();
        // System.out.println("actionPerformed: " + command);

        if (command.equals("add_row"))
        {
            SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");

            // if (!MainFrame.developer_version)
            {

                DailyRowDialog aRF = new DailyRowDialog(dayData, sf.format(calPane.getSelectedDate()), this);

                if (aRF.actionSuccessful())
                {
                    m_db.saveDayStats(dayData);
                    dayData.sort();
                    this.model.fireTableChanged(null);
                }
            }
            /*
             * else
             * {
             * // testing only
             * new DailyRowDialogPen(dayData,
             * sf.format(calPane.getSelectedDate()), this);
             * }
             */
        }
        else if (command.equals("edit_row"))
        {
            editRow();
        }
        else if (command.equals("delete_row"))
        {
            if (table.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int option_selected = JOptionPane.showOptionDialog(this, m_ic.getMessage("ARE_YOU_SURE_DELETE_ROW"),
                m_ic.getMessage("QUESTION"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                m_da.options_yes_no, JOptionPane.YES_OPTION);

            if (option_selected == JOptionPane.NO_OPTION)
                // System.out.println("Option NO was here!");
                return;

            try
            {
                dayData.deleteRow(table.getSelectedRow());
                model.fireTableChanged(null);
                m_db.saveDayStats(dayData);
            }
            catch (Exception ex)
            {
                System.out.println("DailyStatsDialog:Action:Delete Row: " + ex);
                log.error("Action::Delete Row::Exception: " + ex, ex);
            }
        }
        else if (command.equals("close"))
        {
            this.close();
        }
        else if (command.equals("show_daily_graph"))
        {
            // DailyGraphDialog dgd = new DailyGraphDialog(this, this.dayData);
            // dgd.setDailyValues(this.dayData);

            new GraphViewer(new GraphViewDaily(this.current_gc), m_da, this, true);

        }
        else
        {
            System.out.println("DailyStatsDialog:Unknown Action: " + command);
        }

    }


    private void editRow()
    {

        if (table.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // System.out.println("edit");

        DailyValuesRow dvr = dayData.getRow(table.getSelectedRow());

        // System.out.println("DialyRowDialog starting");

        DailyRowDialog aRF = new DailyRowDialog(dvr, this);

        // System.out.println("DialyRowDialog exit");

        if (aRF.actionSuccessful())
        {
            // System.out.println("DialyRowDialog action done");

            m_db.saveDayStats(dayData);
            dayData.sort();
            this.model.fireTableChanged(null);
        }
        // else
        // System.out.println("DialyRowDialog NO Action! ");

    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }


    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }


    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_BG_Daily_View";
    }

}
