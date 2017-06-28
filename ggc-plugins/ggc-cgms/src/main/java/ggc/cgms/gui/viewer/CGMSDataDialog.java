package ggc.cgms.gui.viewer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

import com.atech.graphics.calendar.CalendarEvent;
import com.atech.graphics.calendar.CalendarListener;
import com.atech.graphics.calendar.CalendarPane;
import com.atech.graphics.components.MultiLineTooltipModel;
import com.atech.graphics.graphs.GraphViewer;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.cgms.data.CGMSDailyStatistics;
import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.db.GGC_CGMSDb;
import ggc.cgms.data.graph.CGMSGraphViewDaily;
import ggc.cgms.util.CGMSUtil;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     Dexcom 7
 *  Description:  Dexcom 7 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDataDialog extends JDialog implements ActionListener, HelpCapable
{

    // private static Log log = LogFactory.getLog(PumpDataDialog.class);

    private static final long serialVersionUID = -3923519587722702685L;
    private DataAccessCGMS m_da = DataAccessCGMS.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    CGMSDataTableModel model = null;
    JScrollPane resultsPane;

    JTable table;

    // public boolean save_needed = false;

    CalendarPane calPane;
    CGMSDailyStatistics stats = null;

    // JLabel sumIns1, sumIns2, sumIns;
    // JLabel avgIns1, avgIns2, avgIns;
    // JLabel doseIns1, doseIns2, doseIns;
    // JLabel sumBE, avgBE, meals;
    // JLabel avgBG, highestBG, lowestBG;
    // JLabel readings, stdDev;

    JLabel avg_BG_1, high_BG_1, readings_1, std_dev_1, low_BG_1;
    JLabel avg_BG_2, high_BG_2, readings_2, std_dev_2, low_BG_2;

    JLabel lblDate;
    JButton saveButton;
    JButton help_button;
    DeviceValuesDay dayData;

    GGC_CGMSDb m_db = null;

    GregorianCalendar current_date;

    Component parent = null;
    private ArrayList<CGMSValuesSubEntry> dayDataList;


    /**
     * Constructor
     *
     * @param da
     * @param _parent
     */
    public CGMSDataDialog(DataAccessCGMS da, JDialog _parent)
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
    public CGMSDataDialog(DataAccessCGMS da, JFrame _parent)
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
        setTitle(m_ic.getMessage("CGMS_DAILY_OVERVIEW") + "  [" + gc.get(Calendar.DAY_OF_MONTH) + "."
                + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR) + "]");
    }


    /**
     * Get Table Model
     *
     * @return
     */
    public CGMSDataTableModel getTableModel()
    {
        return model;
    }


    private void refreshData()
    {
        dayData = m_da.getDb().getDailyCGMSValues(this.current_date);
        dayData.sort();
        model.setDailyValues(dayData);

        dayDataList = CGMSUtil.getDataList(dayData.getList());
        stats.processFullCollection(dayDataList);
        updateLabels();

        this.model.fireTableChanged(null);
    }


    /**
     * Get This Parent
     * @return
     */
    public CGMSDataDialog getThisParent()
    {
        return this;
    }


    protected void close()
    {
        this.dispose();
        m_da.getPlugInServerInstance().getParent().requestFocus();
    }


    private void init()
    {

        setTitle(new GregorianCalendar());
        this.m_db = m_da.getDb();
        stats = new CGMSDailyStatistics();
        m_da.addComponent(this);

        setSize(550, 470);
        ATSwingUtils.centerJDialog(this, parent);

        // setBounds(150, 150, 550, 500);

        // Panel for Insulin Stats

        Box dayStats = Box.createVerticalBox();

        JPanel pan_sub = new JPanel(new GridLayout(3, 2));
        pan_sub.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_CGMS") + ":"));

        pan_sub.add(new JLabel(m_ic.getMessage("AVG_BG") + ":"));
        pan_sub.add(avg_BG_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("HIGHEST") + ":"));
        pan_sub.add(high_BG_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("READINGS") + ":"));
        pan_sub.add(readings_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("STD_DEV") + ":"));
        pan_sub.add(std_dev_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("LOWEST") + ":"));
        pan_sub.add(low_BG_1 = new JLabel());

        dayStats.add(pan_sub);

        pan_sub = new JPanel(new GridLayout(3, 2));
        pan_sub.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_CALIB") + ":"));

        pan_sub.add(new JLabel(m_ic.getMessage("AVG_BG") + ":"));
        pan_sub.add(avg_BG_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("HIGHEST") + ":"));
        pan_sub.add(high_BG_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("READINGS") + ":"));
        pan_sub.add(readings_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("STD_DEV") + ":"));
        pan_sub.add(std_dev_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("LOWEST") + ":"));
        pan_sub.add(low_BG_2 = new JLabel());

        dayStats.add(pan_sub);

        /*
         * JPanel BGPanel = new JPanel(new GridLayout(0, 6));
         * BGPanel.setBorder(BorderFactory.createTitledBorder(i18nControl.
         * getMessage(
         * "BLOOD_GLUCOSE_CALIB") + ":"));
         * BGPanel.add(new JLabel(i18nControl.getMessage("AVG_BG") + ":"));
         * BGPanel.add(avgBG = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("HIGHEST") + ":"));
         * BGPanel.add(highestBG = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("READINGS") + ":"));
         * BGPanel.add(readings = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("STD_DEV") + ":"));
         * BGPanel.add(stdDev = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("LOWEST") + ":"));
         * BGPanel.add(lowestBG = new JLabel());
         * InsPanel.add(new JLabel(getIns1Abbr() + ":"));
         * InsPanel.add(sumIns1 = new JLabel());
         * InsPanel.add(new JLabel(i18nControl.getMessage("AVG") + " " +
         * getIns1Abbr()
         * + ":"));
         * InsPanel.add(avgIns1 = new JLabel());
         * InsPanel.add(new JLabel(i18nControl.getMessage("DOSE") + " " +
         * getIns1Abbr()
         * + ":"));
         * InsPanel.add(doseIns1 = new JLabel());
         * InsPanel.add(new JLabel(getIns2Abbr() + ":"));
         * InsPanel.add(sumIns2 = new JLabel());
         * InsPanel.add(new JLabel(i18nControl.getMessage("AVG") + " " +
         * getIns2Abbr()
         * + ":"));
         * InsPanel.add(avgIns2 = new JLabel());
         * InsPanel.add(new JLabel(i18nControl.getMessage("DOSE") + " " +
         * getIns2Abbr()
         * + ":"));
         * InsPanel.add(doseIns2 = new JLabel());
         * InsPanel.add(new JLabel(i18nControl.getMessage("TOTAL") + ":"));
         * InsPanel.add(sumIns = new JLabel());
         * InsPanel.add(new JLabel("")); //i18nControl.getMessage("AVG_INS") +
         * ":"));
         * InsPanel.add(avgIns = new JLabel());
         * InsPanel.add(new JLabel(i18nControl.getMessage("DOSE_INS") + ":"));
         * InsPanel.add(doseIns = new JLabel());
         * // Panel for BU Stats
         * JPanel BUPanel = new JPanel(new GridLayout(1, 6));
         * BUPanel.setBorder(BorderFactory.createTitledBorder(i18nControl.
         * getMessage(
         * "BREAD_UNITS") + ":"));
         * BUPanel.add(new JLabel(i18nControl.getMessage("SUM") + ":"));
         * BUPanel.add(sumBE = new JLabel());
         * BUPanel.add(new JLabel(i18nControl.getMessage("AVG") + ":"));
         * BUPanel.add(avgBE = new JLabel());
         * BUPanel.add(new JLabel(i18nControl.getMessage("MEALS") + ":"));
         * BUPanel.add(meals = new JLabel());
         * // Panel for BG Stats
         * JPanel BGPanel = new JPanel(new GridLayout(0, 6));
         * BGPanel.setBorder(BorderFactory.createTitledBorder(i18nControl.
         * getMessage(
         * "BLOOD_GLUCOSE") + ":"));
         * BGPanel.add(new JLabel(i18nControl.getMessage("AVG_BG") + ":"));
         * BGPanel.add(avgBG = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("HIGHEST") + ":"));
         * BGPanel.add(highestBG = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("READINGS") + ":"));
         * BGPanel.add(readings = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("STD_DEV") + ":"));
         * BGPanel.add(stdDev = new JLabel());
         * BGPanel.add(new JLabel(i18nControl.getMessage("LOWEST") + ":"));
         * BGPanel.add(lowestBG = new JLabel());
         */

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

        dayData = m_da.getDb().getDailyCGMSValues(this.current_date);
        dayData.sort();
        dayDataList = CGMSUtil.getDataList(dayData.getList());
        stats.processFullCollection(dayDataList);

        updateLabels();

        model = new CGMSDataTableModel(dayData);

        table = new JTable(model)
        {

            /**
             *
             */
            private static final long serialVersionUID = -2174342213914259805L;


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

        DataAccess.getSkinManager().reSkinifyComponent(table);

        resultsPane = new JScrollPane(table);
        // resultsPane.getViewport().addMouseListener(ma);
        // resultsPane.getViewport().setBackground(table.getBackground());

        @SuppressWarnings("unused")
        DeviceValuesDay pvd = new DeviceValuesDay(DataAccessCGMS.getInstance());

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

        /*
         * JButton addButton = new JButton("  " +
         * i18nControl.getMessage("ADD"));
         * addButton.setPreferredSize(dim);
         * addButton.setIcon(dataAccess.getImageIcon_22x22("table_add.png",
         * this));
         * addButton.setActionCommand("add_row");
         * addButton.addActionListener(this);
         * EntryBox.add(addButton);
         * JButton editButton = new JButton("  " +
         * i18nControl.getMessage("EDIT"));
         * editButton.setPreferredSize(dim);
         * editButton.setIcon(dataAccess.getImageIcon_22x22("table_edit.png",
         * this));
         * editButton.setActionCommand("edit_row");
         * editButton.addActionListener(this);
         * EntryBox.add(editButton);
         * JButton delButton = new JButton("  " +
         * i18nControl.getMessage("DELETE"));
         * delButton.setPreferredSize(dim);
         * delButton.setIcon(dataAccess.getImageIcon_22x22("table_delete.png",
         * this));
         * delButton.setActionCommand("delete_row");
         * delButton.addActionListener(this);
         * EntryBox.add(delButton);
         */
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

        // updateLabels();
        // TODO re-enable
        m_da.enableHelp(this);

        setVisible(true);
    }


    private void updateLabels()
    {
        if (dayData == null)
            return;

        // JLabel avg_BG_1, high_BG_1, readings_1, std_dev_1, low_BG_1;
        // JLabel avg_BG_2, high_BG_2, readings_2, std_dev_2, low_BG_2;

        this.readings_1.setText(this.stats.getItemStatisticValueAsStringInt(CGMSValuesSubEntry.STAT_COUNT_BG1));
        this.readings_2.setText(this.stats.getItemStatisticValueAsStringInt(CGMSValuesSubEntry.STAT_COUNT_BG2));

        int dec_pls = 1;

        if (this.m_da.getGlucoseUnitType() == GlucoseUnitType.mg_dL)
        {
            dec_pls = 0;
        }

        // JLabel avg_BG_1, high_BG_1, readings_1, std_dev_1, low_BG_1;
        // JLabel avg_BG_2, high_BG_2, readings_2, std_dev_2, low_BG_2;

        avg_BG_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_AVG_BG1, dec_pls));
        std_dev_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_STD_DEV_BG1, dec_pls));
        high_BG_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MAX_BG1, dec_pls));
        low_BG_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MIN_BG1, dec_pls));

        avg_BG_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_AVG_BG2, dec_pls));
        std_dev_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_STD_DEV_BG2, dec_pls));
        high_BG_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MAX_BG2, dec_pls));
        low_BG_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MIN_BG2, dec_pls));

        /*
         * sumIns1.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_SUM_BOLUS, 1));
         * //df.format(dayData.getSumIns1()));
         * sumIns2.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_SUM_BASAL, 1));
         * //df.format(dayData.getSumIns2()));
         * sumIns.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_SUM_TOGETHER, 1));
         * //df.format(dayData.getSumIns()));
         * avgIns1.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_AVG_BOLUS, 1));
         * avgIns2.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_AVG_BASAL, 1));
         * //df.format(dayData.getAvgIns2()));
         * //avgIns.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_SUM_TOGETHER, 1));
         * //df.format(dayData.getAvgIns()));
         * doseIns1.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_DOSES_BOLUS, 0));
         * doseIns2.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_DOSES_BASAL, 0)); //dayData.getIns2Count() + "");
         * doseIns.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.INS_DOSES_TOGETHER, 0)); //dayData.getInsCount() +
         * "");
         * sumBE.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.CH_SUM, 0));
         * avgBE.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.CH_AVG, 0));
         * meals.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.MEALS, 0));
         * readings.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_COUNT, 0));
         * //if ()
         * int dec_pls = 1;
         * if (this.dataAccess.getGlucoseUnitType()==DataAccessPump.BG_MGDL)
         * {
         * dec_pls = 0;
         * avgBG.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_AVG, dec_pls));
         * stdDev.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_STD_DEV, dec_pls));
         * highestBG.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_MAX, dec_pls));
         * lowestBG.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_MIN, dec_pls));
         * }
         * else
         * {
         * dec_pls = 1;
         * avgBG.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_AVG, dec_pls));
         * stdDev.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_STD_DEV, dec_pls));
         * highestBG.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_MAX, dec_pls));
         * lowestBG.setText(this.stats.getItemStatisticValueAsStringFloat(
         * PumpValuesEntry.BG_MIN, dec_pls));
         * }
         * /*
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


    /*
     * public void processWindowEvent(WindowEvent e) { if (e.getID() ==
     * WindowEvent.WINDOW_CLOSING) { this.dispose(); }
     * super.processWindowEvent(e); }
     */
    /*
     * private String getIns1Abbr()
     * {
     * return i18nControl.getMessage("BOLUS"); //"Bolus Insulin";
     * }
     * private String getIns2Abbr()
     * {
     * return i18nControl.getMessage("BASAL");
     * //return "Basal Insulin";
     * }
     */
    /**
     * Action Performed
     */
    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();

        /*
         * if (command.equals("add_row"))
         * {
         * SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
         * PumpDataRowDialog pdrd = new PumpDataRowDialog(dayData,
         * sf.format(calPane.getSelectedDate()), this);
         * if (pdrd.wasAction())
         * {
         * refreshData();
         * }
         * }
         * else if (command.equals("edit_row"))
         * {
         * this.editEntry(true);
         * }
         * else if (command.equals("delete_row"))
         * {
         * if (table.getSelectedRow() == -1)
         * {
         * JOptionPane.showMessageDialog(this,
         * i18nControl.getMessage("SELECT_ROW_FIRST"),
         * i18nControl.getMessage("ERROR"),
         * JOptionPane.ERROR_MESSAGE);
         * return;
         * }
         * //DeviceValuesEntryInterface dei =
         * this.dayData.getRowAt(table.getSelectedRow());
         * int option_selected = JOptionPane.showOptionDialog(this,
         * i18nControl.getMessage("ARE_YOU_SURE_DELETE"), i18nControl
         * .getMessage("QUESTION"), JOptionPane.YES_NO_OPTION,
         * JOptionPane.QUESTION_MESSAGE, null,
         * dataAccess.options_yes_no, JOptionPane.YES_OPTION);
         * if (option_selected == JOptionPane.YES_OPTION)
         * {
         * int idx = table.getSelectedRow();
         * PumpValuesEntry pve = (PumpValuesEntry)this.dayData.getRowAt(idx);
         * boolean refresh = false;
         * if (pve.getBaseType()==PumpBaseType.PUMP_DATA_ADDITIONAL_DATA)
         * {
         * refresh = deleteAdditionalDataCheck(pve, idx); //, false);
         * if (refresh)
         * {
         * this.dayData.removeEntry(idx);
         * refreshData();
         * }
         * }
         * else
         * {
         * if (pve.getAdditionalDataCount()>0)
         * {
         * refresh = deleteAdditionalDataCheck(pve, idx); //, false);
         * if (refresh)
         * {
         * m_db.delete(pve);
         * this.dayData.removeEntry(idx);
         * refreshData();
         * }
         * }
         * else
         * {
         * m_db.delete(pve);
         * this.dayData.removeEntry(idx);
         * refreshData();
         * }
         * }
         * }
         * }
         * else
         */
        if (command.equals("close"))
        {
            m_da.removeComponent(this);
            this.close();
        }
        else if (command.equals("show_daily_graph"))
        {
            new GraphViewer(new CGMSGraphViewDaily(this.current_date, this.dayDataList), m_da, this, true);
        }
        else
        {
            System.out.println("CGMSDataDialog:Unknown Action: " + command);
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
        return "CGMSTool_Data_Overview";
    }

}
