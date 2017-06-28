package ggc.pump.gui.manual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.atech.graphics.calendar.CalendarEvent;
import com.atech.graphics.calendar.CalendarListener;
import com.atech.graphics.calendar.CalendarPane;
import com.atech.graphics.components.MultiLineTooltipModel;
import com.atech.graphics.graphs.GraphViewer;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.pump.data.PumpDailyStatistics;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.dto.BasalStatistics;
import ggc.pump.data.graph.GraphViewDailyPump;
import ggc.pump.data.util.PumpBasalManager;
import ggc.pump.db.GGCPumpDb;
import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     PumpDataDialog
 *  Description:  Pump Data Dialog
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpDataDialog extends JDialog implements ActionListener, HelpCapable
{

    // private static Log log = LogFactory.getLog(PumpDataDialog.class);

    private static final long serialVersionUID = -4403053763073221824L;
    private DataAccessPump m_da = DataAccessPump.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    PumpDataTableModel model = null;
    JScrollPane resultsPane;

    JTable table;

    // public boolean save_needed = false;

    CalendarPane calPane;
    PumpDailyStatistics stats = null;

    JLabel sumIns1, sumIns2, sumIns;
    JLabel avgIns1, avgIns2, avgIns;
    JLabel doseIns1, doseIns2, doseIns;
    JLabel sumBE, avgBE, meals;
    JLabel avgBG, highestBG, lowestBG;
    JLabel readings, stdDev;

    JLabel lblDate;
    JButton saveButton;
    JButton help_button;
    DeviceValuesDay dayData;

    GGCPumpDb m_db = null;

    GregorianCalendar current_date;

    Component parent = null;

    PumpBasalManager basalManager;


    /**
     * Constructor
     *
     * @param da
     * @param _parent
     */
    public PumpDataDialog(DataAccessPump da, JDialog _parent)
    {
        super(_parent, "", true);

        this.m_da = da;
        this.parent = _parent;

        init();
    }


    /**
     * Constructor
     *
     * @param da
     * @param _parent
     */
    public PumpDataDialog(DataAccessPump da, JFrame _parent)
    {
        super(_parent, "", true);

        this.m_da = da;
        this.parent = _parent;

        init();
    }


    /**
     * Set Title
     *
     * @param gc
     */
    public void setTitle(GregorianCalendar gc)
    {
        this.current_date = gc;
        setTitle(m_ic.getMessage("PUMP_DAILY_OVERVIEW") + "  [" + gc.get(Calendar.DAY_OF_MONTH) + "."
                + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR) + "]");
    }


    /**
     * Get Table Model
     *
     * @return
     */
    public PumpDataTableModel getTableModel()
    {
        return model;
    }


    private void refreshData()
    {
        dayData = m_da.getDb().getDailyPumpValues(this.current_date);
        dayData.sort();
        model.setDailyValues(dayData);
        stats.processFullCollection(getDataList(dayData.getList()));
        updateStatistics();

        this.model.fireTableChanged(null);
    }


    private ArrayList<PumpValuesEntry> getDataList(List<DeviceValuesEntry> list_in)
    {
        ArrayList<PumpValuesEntry> lst = new ArrayList<PumpValuesEntry>();

        for (int i = 0; i < list_in.size(); i++)
        {
            lst.add((PumpValuesEntry) list_in.get(i));
        }

        return lst;
    }


    protected void close()
    {
        this.dispose();
        m_da.getPlugInServerInstance().getParent().requestFocus();
    }


    private void init()
    {
        ATSwingUtils.initLibrary();

        this.basalManager = new PumpBasalManager(this.m_da);

        setTitle(new GregorianCalendar());
        this.m_db = m_da.getDb();
        stats = new PumpDailyStatistics();
        m_da.addComponent(this);

        setSize(700, 470);
        ATSwingUtils.centerJDialog(this, parent);

        // Panel for Insulin Stats

        JPanel insulinPanel = new JPanel(new GridLayout(3, 6));
        insulinPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("INSULIN") + ":"));

        insulinPanel.add(new JLabel(getIns1Abbr() + ":"));
        insulinPanel.add(sumIns1 = new JLabel());
        insulinPanel.add(new JLabel(m_ic.getMessage("AVG") + " " + getIns1Abbr() + ":"));
        insulinPanel.add(avgIns1 = new JLabel());
        insulinPanel.add(new JLabel(m_ic.getMessage("DOSE") + " " + getIns1Abbr() + ":"));
        insulinPanel.add(doseIns1 = new JLabel());

        insulinPanel.add(new JLabel(getIns2Abbr() + ":"));
        insulinPanel.add(sumIns2 = new JLabel());
        insulinPanel.add(new JLabel(m_ic.getMessage("AVG") + " " + getIns2Abbr() + ":"));
        insulinPanel.add(avgIns2 = new JLabel());
        insulinPanel.add(new JLabel()); // i18nControl.getMessage("DOSE") + " "
                                        // +
                                        // getIns2Abbr() + ":"
        insulinPanel.add(doseIns2 = new JLabel());
        doseIns2.setVisible(false);

        insulinPanel.add(new JLabel(m_ic.getMessage("TOTAL") + ":"));
        insulinPanel.add(sumIns = new JLabel());
        insulinPanel.add(new JLabel("")); // i18nControl.getMessage("AVG_INS") +
                                          // ":"));
        insulinPanel.add(avgIns = new JLabel());
        insulinPanel.add(new JLabel(m_ic.getMessage("DOSE_INS") + ":"));
        insulinPanel.add(doseIns = new JLabel());

        // Panel for BU Stats
        JPanel BUPanel = new JPanel(new GridLayout(1, 6));
        BUPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BREAD_UNITS") + ":"));

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
        dayStats.add(insulinPanel);
        dayStats.add(BUPanel);
        dayStats.add(BGPanel);

        JPanel dayHeader = new JPanel();
        dayHeader.setLayout(new BorderLayout());

        JPanel dayCalendar = new JPanel();
        dayCalendar.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("DATE") + ":"));

        calPane = new CalendarPane(this.m_da);
        calPane.addCalendarListener(new CalendarListener()
        {

            public void dateHasChanged(CalendarEvent e)
            {
                setTitle(e.getNewCalendar());
                refreshData();
            }
        });

        calPane.setBounds(10, 10, 300, 200);
        dayCalendar.add(calPane);

        dayHeader.add(dayCalendar, BorderLayout.WEST);
        dayHeader.add(dayStats, BorderLayout.CENTER);

        dayData = m_da.getDb().getDailyPumpValues(this.current_date);
        dayData.sort();
        stats.processFullCollection(this.getDataList(dayData.getList()));
        updateStatistics();

        model = new PumpDataTableModel(dayData);

        // FIXME replace with new JTableWithToolstip
        table = new JTable(model)
        {

            private static final long serialVersionUID = 1613807277320213251L;


            // Implement table cell tool tips.
            @Override
            public String getToolTipText(MouseEvent e)
            {
                // Object source = e.getSource();
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);

                if (model instanceof MultiLineTooltipModel)
                {
                    tip = ((MultiLineTooltipModel) model).getToolTipValue(rowIndex, colIndex);
                }
                else
                {
                    tip = (String) getValueAt(rowIndex, realColumnIndex);
                }

                if (tip != null && tip.length() == 0)
                {
                    tip = null;
                }

                return tip;
            }

        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        table.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                {
                    editEntry(false);
                }
            }

        });

        DataAccess.getSkinManager().reSkinifyComponent(table);

        resultsPane = new JScrollPane(table);

        m_da.getColumnsWidthManual();

        for (int i = 0; i < 5; i++)
        {
            // TODO
            // table.getColumnModel().getColumn(i).setWidth(pvd.getColumnWidth(i,
            // 460));
        }

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

        m_da.enableHelp(this);

        setVisible(true);
    }


    private void updateStatistics()
    {
        if (dayData == null)
            return;

        BasalStatistics basalStatistics = basalManager.getBasalStatistics(this.current_date, dayData);

        float tempValue = 0.0f;

        // insulin - sum
        sumIns1.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.INS_SUM_BOLUS, 3));

        tempValue = basalStatistics.getValueForItem(BasalStatistics.SUM_BASAL);
        sumIns2.setText(getFloatAsString(tempValue, 3));

        tempValue += this.stats.getItemStatisticsValue(PumpValuesEntry.INS_SUM_BOLUS);

        sumIns.setText(getFloatAsString(tempValue, 3));

        avgIns1.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.INS_AVG_BOLUS, 3));
        avgIns2.setText(getFloatAsString(basalStatistics.getValueForItem(BasalStatistics.AVG_BASAL_IN_DAY), 3)); // df.format(dayData.getAvgIns2()));

        // avgIns.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.INS_SUM_TOGETHER,
        // 1)); //df.format(dayData.getAvgIns()));

        doseIns1.setText("     " + this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.INS_DOSES_BOLUS, 0));
        // doseIns2.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.INS_DOSES_BASAL,
        // 0));

        doseIns.setText("     " + this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.INS_DOSES_TOGETHER, 0));

        sumBE.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.CH_SUM, 0));
        avgBE.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.CH_AVG, 0));
        meals.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.MEALS, 0));

        readings.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_COUNT, 0));

        // if ()
        int dec_pls = 1;

        if (this.m_da.getGlucoseUnitType() == GlucoseUnitType.mg_dL)
        {
            dec_pls = 0;
            avgBG.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_AVG, dec_pls));
            stdDev.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_STD_DEV, dec_pls));
            highestBG.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_MAX, dec_pls));
            lowestBG.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_MIN, dec_pls));
        }
        else
        {
            dec_pls = 1;
            avgBG.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_AVG, dec_pls));
            stdDev.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_STD_DEV, dec_pls));
            highestBG.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_MAX, dec_pls));
            lowestBG.setText(this.stats.getItemStatisticValueAsStringFloat(PumpValuesEntry.BG_MIN, dec_pls));
        }

        // basalStatistics.

        // df.format(dayData.getSumIns2()));

        /*
         * DecimalFormat df = new DecimalFormat("#0.0");
         * sumIns1.setText(df.format(dayData.getSumIns1()));
         * sumIns2.setText(df.format(dayData.getSumIns2()));
         * sumIns.setText(df.format(dayData.getSumIns()));
         * avgIns1.setText(df.format(dayData.getAvgIns1()));
         * avgIns2.setText(df.format(dayData.getAvgIns2()));
         * avgIns.setText(df.format(dayData.getAvgIns()));
         * doseIns1.setText(dayData.getIns1Count() + "");
         * doseIns2.setText(dayData.getIns2Count() + "");
         * doseIns.setText(dayData.getInsCount() + "");
         * sumBE.setText(df.format(dayData.getSumCH()));
         * avgBE.setText(df.format(dayData.getAvgCH()));
         * meals.setText(dayData.getCHCount() + "");
         * avgBG.setText(df.format(dayData.getAvgBG()));
         * stdDev.setText(df.format(dayData.getStdDev()));
         * highestBG.setText(df.format(dayData.getHighestBG()));
         * lowestBG.setText(df.format(dayData.getLowestBG()));
         * readings.setText(dayData.getBGCount() + "");
         */
    }


    private String getFloatAsString(float value, int decimalPlaces)
    {
        return m_da.getFormatedValue(value, decimalPlaces);
    }


    private String getIns1Abbr()
    {
        return m_ic.getMessage("BOLUS"); // "Bolus Insulin";
    }


    private String getIns2Abbr()
    {
        return m_ic.getMessage("BASAL");
    }


    /**
     * Action Performed
     */
    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();

        if (command.equals("add_row"))
        {
            SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");

            PumpDataRowDialog pdrd = new PumpDataRowDialog(dayData, sf.format(calPane.getSelectedDate()), this);

            if (pdrd.wasAction())
            {
                refreshData();
            }
        }
        else if (command.equals("edit_row"))
        {
            this.editEntry(true);
        }
        else if (command.equals("delete_row"))
        {
            if (table.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // DeviceValuesEntryInterface dei =
            // this.dayData.getRowAt(table.getSelectedRow());

            int option_selected = JOptionPane.showOptionDialog(this, m_ic.getMessage("ARE_YOU_SURE_DELETE"),
                m_ic.getMessage("QUESTION"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                m_da.options_yes_no, JOptionPane.YES_OPTION);

            if (option_selected == JOptionPane.YES_OPTION)
            {

                int idx = table.getSelectedRow();
                PumpValuesEntry pve = (PumpValuesEntry) this.dayData.getRowAt(idx);

                boolean refresh = false;

                if (pve.getBaseType() == PumpBaseType.AdditionalData)
                {
                    refresh = deleteAdditionalDataCheck(pve, idx); // , false);

                    if (refresh)
                    {
                        this.dayData.removeEntry(idx);
                        refreshData();
                    }
                }
                else
                {

                    if (pve.getAdditionalDataCount() > 0)
                    {
                        refresh = deleteAdditionalDataCheck(pve, idx); // ,
                                                                       // false);

                        if (refresh)
                        {
                            m_db.delete(pve);

                            this.dayData.removeEntry(idx);
                            refreshData();
                        }
                    }
                    else
                    {
                        m_db.delete(pve);

                        this.dayData.removeEntry(idx);
                        refreshData();
                    }

                }

            }

        }
        else if (command.equals("close"))
        {
            m_da.removeComponent(this);
            this.close();
        }
        else if (command.equals("show_daily_graph"))
        {
            new GraphViewer(new GraphViewDailyPump(this.current_date), m_da, this, true);
        }
        else
        {
            System.out.println("PumpDataDialog:Unknown Action: " + command);
        }

    }


    private boolean deleteAdditionalDataCheck(PumpValuesEntry pve, int index) // ..,
                                                                              // boolean
                                                                              // delete)
    {
        boolean delete = false;

        if (pve.getAdditionalDataCount() > 1)
        {
            {
                int option_selected_yy = JOptionPane.showOptionDialog(this,
                    m_ic.getMessage("DELETE_MANY_ADDITIONAL_DATA"), m_ic.getMessage("QUESTION"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, m_da.options_yes_no,
                    JOptionPane.YES_OPTION);

                if (option_selected_yy == JOptionPane.YES_OPTION)
                {
                    delete = true;
                }
            }

            if (delete)
            {
                deleteAdditionalData(pve);
            }

            return delete;
        }
        else
        {
            deleteAdditionalData(pve);

            return true;
        }

    }


    private void deleteAdditionalData(PumpValuesEntry pve)
    {
        Map<PumpAdditionalDataType, PumpValuesEntryExt> additionalData = pve.getAdditionalData();

        for (PumpValuesEntryExt entry : additionalData.values())
        {
            m_db.delete(entry);
        }
    }


    private void editEntry(boolean check)
    {
        if (check)
        {
            if (table.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        PumpValuesEntry dvr = (PumpValuesEntry) dayData.getRowAt(table.getSelectedRow());
        PumpDataRowDialog aRF = new PumpDataRowDialog(dvr, this);

        if (aRF.wasAction())
        {
            refreshData();
        }

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
        return "PumpTool_Data_Overview";
    }

}
