package ggc.cgms.gui.viewer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

import javax.swing.*;

import com.atech.graphics.calendar.CalendarEvent;
import com.atech.graphics.calendar.CalendarListener;
import com.atech.graphics.calendar.CalendarPane;
import com.atech.graphics.components.jtable.JTableWithToolTip;
import com.atech.graphics.graphs.GraphViewer;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.cgms.data.CGMSDailyStatistics;
import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.db.GGC_CGMSDb;
import ggc.cgms.data.defs.CGMSViewerFilter;
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
    JComboBox filterCombo;

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


    private void refreshTable()
    {
        refreshData();

        model.setDayDataList(dayDataList, (CGMSViewerFilter) filterCombo.getSelectedItem());

        this.model.fireTableChanged(null);
    }


    public void refreshData()
    {
        dayData = m_da.getDb().getDailyCGMSValues(this.current_date, true);
        dayDataList = CGMSUtil.getDataList(dayData.getList());
        stats.processFullCollection(dayDataList);

        updateLabels();

        Collections.sort(dayDataList, new Comparator<CGMSValuesSubEntry>()
        {

            public int compare(final CGMSValuesSubEntry keyValue1, final CGMSValuesSubEntry keyValue2)
            {
                return keyValue1.time - keyValue2.time;
            }
        });
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

        setSize(650, 500);
        ATSwingUtils.centerJDialog(this, parent);

        // setBounds(150, 150, 550, 500);

        // Panel for Insulin Stats

        Box dayStats = Box.createVerticalBox();

        JPanel pan_sub = new JPanel(new GridLayout(3, 2));
        pan_sub.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_CGMS") + ":"));

        pan_sub.add(new JLabel(m_ic.getMessage("AVG_BG") + ":"));
        pan_sub.add(avg_BG_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("READINGS") + ":"));
        pan_sub.add(readings_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("HIGHEST") + ":"));
        pan_sub.add(high_BG_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("LOWEST") + ":"));
        pan_sub.add(low_BG_1 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("STD_DEV") + ":"));
        pan_sub.add(std_dev_1 = new JLabel());

        dayStats.add(pan_sub);

        pan_sub = new JPanel(new GridLayout(3, 2));
        pan_sub.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_CALIB") + ":"));

        pan_sub.add(new JLabel(m_ic.getMessage("AVG_BG") + ":"));
        pan_sub.add(avg_BG_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("READINGS") + ":"));
        pan_sub.add(readings_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("HIGHEST") + ":"));
        pan_sub.add(high_BG_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("LOWEST") + ":"));
        pan_sub.add(low_BG_2 = new JLabel());
        pan_sub.add(new JLabel(m_ic.getMessage("STD_DEV") + ":"));
        pan_sub.add(std_dev_2 = new JLabel());

        dayStats.add(pan_sub);

        pan_sub = new JPanel(new BorderLayout());
        pan_sub.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("FILTER") + ":"));

        // pan_sub.add(new JLabel(i18nControl.getMessage("DATA_FILTER") + ":"));
        pan_sub.add(filterCombo = new JComboBox(CGMSViewerFilter.getAllValues()));

        filterCombo.addItemListener(new ItemListener()
        {

            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    model.filterData((CGMSViewerFilter) e.getItem());
                }
            }
        });

        dayStats.add(pan_sub);

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
                refreshTable();
            }
        });

        calPane.setBounds(10, 10, 300, 200);
        dayCalendar.add(calPane);

        dayHeader.add(dayCalendar, BorderLayout.WEST);
        dayHeader.add(dayStats, BorderLayout.CENTER);

        refreshData();

        model = new CGMSDataTableModel(dayDataList);

        table = new JTableWithToolTip(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        DataAccess.getSkinManager().reSkinifyComponent(table);

        resultsPane = new JScrollPane(table);

        float[] widths = { 0.2f, 0.2f, 0.4f, 0.2f };

        for (int i = 0; i < widths.length; i++)
        {
            int ww = (int) (widths[i] * 460.0f);
            // System.out.println("w: " + i+ " = " + ww);
            table.getColumnModel().getColumn(i).setWidth(ww);
        }

        System.out.println(resultsPane.getWidth());

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

        // TODO re-enable
        m_da.enableHelp(this);

        setVisible(true);
    }


    private void updateLabels()
    {
        if (dayData == null)
            return;

        this.readings_1.setText(this.stats.getItemStatisticValueAsStringInt(CGMSValuesSubEntry.STAT_COUNT_BG1));
        this.readings_2.setText(this.stats.getItemStatisticValueAsStringInt(CGMSValuesSubEntry.STAT_COUNT_BG2));

        int dec_pls = 1;

        if (this.m_da.getGlucoseUnitType() == GlucoseUnitType.mg_dL)
        {
            dec_pls = 0;
        }

        avg_BG_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_AVG_BG1, dec_pls));
        std_dev_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_STD_DEV_BG1, dec_pls));
        high_BG_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MAX_BG1, dec_pls));
        low_BG_1.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MIN_BG1, dec_pls));

        avg_BG_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_AVG_BG2, dec_pls));
        std_dev_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_STD_DEV_BG2, dec_pls));
        high_BG_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MAX_BG2, dec_pls));
        low_BG_2.setText(this.stats.getItemStatisticValueAsStringFloat(CGMSValuesSubEntry.STAT_MIN_BG2, dec_pls));

    }


    /**
     * Action Performed
     */
    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();

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
