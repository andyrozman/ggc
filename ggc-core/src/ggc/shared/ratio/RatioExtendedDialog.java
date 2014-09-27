package ggc.shared.ratio;

import ggc.core.data.ExtendedRatioCollection;
import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.util.DataAccess;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

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
 *  Filename:     RatioExtendedDialog  
 *  Description:  Dialog for extended ratios
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// NOT IMPLEMENTED YET

public class RatioExtendedDialog extends JDialog implements ActionListener, HelpCapable, FocusListener, KeyListener
{

    private static final long serialVersionUID = 2914296915890126837L;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    // JComboBox cb_time_range, cb_icarb_rule, cb_sens_rule;
    // ArrayList<RatioEntry> list_ratios = new ArrayList<RatioEntry>();
    RatioEntryDisplay red = null;
    JTable table_list_ratios;

    boolean in_action = false;

    ConfigurationManager cfg_mgr = DataAccess.getInstance().getConfigurationManager();

    // private GGCProperties props = m_da.getSettings();

    private boolean m_actionDone = false;

    // private long last_change = 0;

    // static AddRowFrame singleton = null;

    // JTextField DateField, TimeField, /*BGField, Ins1Field, Ins2Field,
    // BUField,*/
    // ActField, CommentField, UrineField;

    // JComboBox cob_bg_type; //= new JComboBox();

    // JFormattedTextField ftf_ins1, ftf_ins2, ftf_bg1, ftf_ch, ftf_bg2;
    // JTextFieldFormatted

    JLabel label_title = new JLabel();
    // JLabel label_food;
    // JCheckBox cb_food_set;

    // DateTimeComponent dtc;

    float tdd;

    // JButton AddButton;

    // String sDate = null;

    // DailyValues dV = null;
    // DailyValuesRow m_dailyValuesRow = null;

    // NumberFormat bg_displayFormat, bg_editFormat;

    // JComponent components[] = new JComponent[9];

    // Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);
    // Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL);
    boolean in_process;
    // boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;

    JDecimalTextField dtf_ch_ins, dtf_ins_bg, dtf_bg_ch;

    // private boolean m_add_action = true;
    private Container m_parent = null;
    static ExtendedRatioCollection erc = null;

    /**
     * Constructor
     * 
     * @param dialog
     */
    public RatioExtendedDialog(JFrame dialog)
    {
        super(dialog, "", true);

        m_parent = dialog;

        this.tdd = this.m_da.getConfigurationManager().getFloatValue("LAST_TDD");

        init();

        setTitle(m_ic.getMessage("RATIO_EXTENDED"));
        label_title.setText(m_ic.getMessage("RATIO_EXTENDED"));

        load();

        this.setVisible(true);

    }

    public static ExtendedRatioCollection getExtendedRatioCollection()
    {
        erc = new ExtendedRatioCollection();
        erc.load();

        System.out.println("ExtendedRatioCollection: " + erc.size());

        return erc;
    }

    /**
     * Load
     */
    private void load()
    {

        RatioExtendedDialog.erc = new ExtendedRatioCollection();
        RatioExtendedDialog.erc.load();

        System.out.println("ExtendedRatioCollection: " + erc.size());

        ((AbstractTableModel) this.table_list_ratios.getModel()).fireTableDataChanged();

        // load data

        // load express

        // RATIO_MODE=Base, Extended
        // EXTENDED_RATIO_COUNT=2

        // EXTENDED_RATIO_1=From;To;Ch/Ins;Bg/Ins;Ch/BG;Procent
        /*
         * Hashtable<String,String> dta = new Hashtable<String,String>();
         * dta.put("EXTENDED_RATIO_COUNT", "2");
         * dta.put("EXTENDED_RATIO_1", "0000;1159;6.67f;1.33f;5f;100");
         * dta.put("EXTENDED_RATIO_2", "1200;2259;6.67f;1.33f;5f;100");
         * if (dta.size()==0)
         * return;
         * int count = 0;
         * if (dta.containsKey("EXTENDED_RATIO_COUNT"))
         * {
         * count = m_da.getIntValueFromString(dta.get("EXTENDED_RATIO_COUNT"),
         * 0);
         * }
         * if (count==0)
         * return;
         * for(int i=1; i<=count; i++)
         * {
         * list_ratios.add(new RatioEntry(dta.get("EXTENDED_RATIO_" + i)));
         * }
         * /*
         * this.dtc.setDateTime(this.m_dailyValuesRow.getDateTime());
         * if (m_dailyValuesRow.getBG()>0)
         * {
         * this.ftf_bg1.setValue(new Integer((int)m_dailyValuesRow.getBGRaw()));
         * this.ftf_bg2.setValue(new
         * Float(m_da.getBGValueDifferent(DataAccess.BG_MGDL,
         * m_dailyValuesRow.getBGRaw())));
         * }
         * this.ftf_ins1.setValue(new
         * Integer((int)this.m_dailyValuesRow.getIns1()));
         * this.ftf_ins2.setValue(new
         * Integer((int)this.m_dailyValuesRow.getIns2()));
         * this.ftf_ch.setValue(new Float(this.m_dailyValuesRow.getCH()));
         * ActField.setText(this.m_dailyValuesRow.getActivity());
         * UrineField.setText(this.m_dailyValuesRow.getUrine());
         * this.cb_food_set.setEnabled(false);
         * this.cb_food_set.setSelected(this.m_dailyValuesRow.areMealsSet());
         * this.cb_food_set.setEnabled(true);
         * CommentField.setText(this.m_dailyValuesRow.getComment());
         */

    }

    private boolean save()
    {
        if (RatioExtendedDialog.erc.save())
            return true;
        else
            return false;

    }

    private void init()
    {

        ATSwingUtils.initLibrary();

        red = new RatioEntryDisplay(m_ic);

        this.setBounds(0, 0, 500, 500);

        m_da.centerJDialog(this, m_parent);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 500, 500);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title = ATSwingUtils.getTitleLabel("", 0, 15, 500, 35, panel, ATSwingUtils.FONT_BIG_BOLD);

        /*
         * label_title.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
         * label_title.setHorizontalAlignment(JLabel.CENTER);
         * label_title.setBounds(0, 15, 400, 35);
         * panel.add(label_title);
         */

        // base panel

        JPanel p1 = ATSwingUtils.getPanel(30, 70, 300, 150, null,
            new TitledBorder("Base Ratio's (for reference only)"), panel);

        ATSwingUtils
                .getLabel(m_ic.getMessage("INSULIN_CARB_RATIO"), 20, 30, 150, 25, p1, ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_ch_ins = ATSwingUtils.getNumericTextField(3, 2, new Float(0.0f), 170, 30, 60, 25, p1);
        dtf_ch_ins.addFocusListener(this);
        dtf_ch_ins.addKeyListener(this);

        ATSwingUtils.getLabel(m_ic.getMessage("SENSITIVITY_FACTOR_LONG"), 20, 60, 150, 45, p1,
            ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_ins_bg = ATSwingUtils.getNumericTextField(3, 2, new Float(0.0f), 170, 70, 60, 25, p1);
        dtf_ins_bg.addFocusListener(this);
        dtf_ins_bg.addKeyListener(this);

        ATSwingUtils.getLabel(m_ic.getMessage("BG_OH_RATIO"), 20, 110, 150, 25, p1, ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_bg_ch = ATSwingUtils.getNumericTextField(3, 2, new Float(0.0f), 170, 110, 60, 25, p1);
        dtf_bg_ch.addFocusListener(this);
        dtf_bg_ch.addKeyListener(this);

        ATSwingUtils.getButton("", 250, 30, 30, 30, p1, ATSwingUtils.FONT_NORMAL, "calculator.png", "calculator", this,
            m_da);

        // extended panel

        JPanel p2 = ATSwingUtils.getPanel(30, 230, 450, 230, null, new TitledBorder("Extended Ratio's"), panel);

        ATSwingUtils
                .getButton("", 20, 20, 30, 30, p2, ATSwingUtils.FONT_NORMAL, "table_add.png", "add_row", this, m_da);

        ATSwingUtils.getButton("", 55, 20, 30, 30, p2, ATSwingUtils.FONT_NORMAL, "table_edit.png", "edit_row", this,
            m_da);

        ATSwingUtils.getButton("", 90, 20, 30, 30, p2, ATSwingUtils.FONT_NORMAL, "table_delete.png", "delete_row",
            this, m_da);

        ATSwingUtils.getButton("", 400, 20, 30, 30, p2, ATSwingUtils.FONT_NORMAL, "table_sql_check.png", "check_data",
            this, m_da);

        this.table_list_ratios = new JTable();

        this.table_list_ratios.setModel(new AbstractTableModel()
        {

            private static final long serialVersionUID = -3326447034673814643L;
            RatioEntry re = new RatioEntry();

            public int getColumnCount()
            {
                return re.getColumns();
            }

            public int getRowCount()
            {
                return erc.size();
            }

            public Object getValueAt(int rowIndex, int columnIndex)
            {
                return erc.get(rowIndex).getColumnValue(columnIndex);
            }

            /**
             * Get Column Name
             * 
             * @see javax.swing.table.AbstractTableModel#getColumnName(int)
             */
            @Override
            public String getColumnName(int index)
            {
                return m_ic.getMessage(re.getColumnName(index));
            }

        });

        // this.createModel(this.list_ratios, this.table_list_ratios, this.red);

        this.table_list_ratios.setRowSelectionAllowed(true);
        this.table_list_ratios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table_list_ratios.setDoubleBuffered(true);

        JScrollPane scroll_1 = new JScrollPane(this.table_list_ratios);
        scroll_1.setBounds(10, 55, 430, 160); // 30, 305, 460, 160
        p2.add(scroll_1, null);

        // panel

        ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 360, 80, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "ok.png", "ok", this, m_da);

        ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 360, 115, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "cancel", this, m_da);

        help_button = m_da.createHelpButtonByBounds(360, 150, 110, 25, this);
        panel.add(help_button);
        m_da.enableHelp(this);

    }

    /*
     * private void createModel(ArrayList<?> lst, JTable table, ATTableData
     * object)
     * {
     * ATTableModel model = new ATTableModel(lst, object);
     * table.setModel(model);
     * // int twidth2 = this.getWidth()-50;
     * table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
     * TableColumnModel cm2 = table.getColumnModel();
     * for (int i = 0; i < object.getColumnsCount(); i++)
     * {
     * cm2.getColumn(i).setHeaderValue(object.getColumnHeader(i));
     * cm2.getColumn(i).setPreferredWidth(object.getColumnWidth(i, 430));
     * }
     * }
     */

    private static final int RATIO_CH_INSULIN = 1;
    private static final int RATIO_BG_INSULIN = 2;
    private static final int RATIO_BG_CH = 3;

    private void calculateRatio(Object obj)
    {
        if (obj.equals(this.dtf_ch_ins))
        {
            calculateRatio(RATIO_CH_INSULIN);
        }
        else if (obj.equals(this.dtf_ins_bg))
        {
            calculateRatio(RATIO_BG_INSULIN);
        }
        else if (obj.equals(this.dtf_bg_ch))
        {
            calculateRatio(RATIO_BG_CH);
        }
    }

    private void calculateRatio(int type)
    {
        // System.out.println("calculate Ratio: " + type);

        float v1 = this.m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue());
        float v2 = this.m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue());
        float v3 = this.m_da.getFloatValue(this.dtf_bg_ch.getCurrentValue());

        if (type == RATIO_CH_INSULIN)
        {
            if (checkSet(v1, v2))
            {
                float v4 = v1 / v2;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=1,2;value=" + v4);
                this.dtf_bg_ch.setValue(new Float(v4));
            }
            else if (checkSet(v1, v3))
            {
                float v4 = v1 / v3;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=1,3;value=" + v4);
                this.dtf_ins_bg.setValue(new Float(v4));
            }
            // else
            // System.out.println("calculate Ratio [type=" + type +
            // ",check NO");

        }
        else if (type == RATIO_BG_INSULIN)
        {
            if (checkSet(v2, v1))
            {
                float v4 = v1 / v2;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=2,1;value=" + v4);
                this.dtf_bg_ch.setValue(new Float(v4));
            }
            else if (checkSet(v2, v3))
            {
                float v4 = v2 * v3;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=2,3;value=" + v4);
                this.dtf_ch_ins.setValue(new Float(v4));
            }
            // else
            // System.out.println("calculate Ratio [type=" + type +
            // ",check NO");
        }
        else
        {
            if (checkSet(v1, v2))
            {
                float v4 = v1 / v2;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=3,1;value=" + v4);
                this.dtf_bg_ch.setValue(v4);
            }
            else if (checkSet(v3, v2))
            {
                float v4 = v2 * v3;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=3,2;value=" + v4);
                this.dtf_ch_ins.setValue(new Float(v4));
            }
            // else
            // System.out.println("calculate Ratio [type=" + type +
            // ",check NO");
        }

        // JDecimalTextField dtf_ch_ins, dtf_ins_bg, dtf_bg_ch;

    }

    private boolean checkSet(float v1, float v2)
    {
        // System.out.println("checkSet [v1=" + v1 + ",v2=" + v2 + "]");
        if (v1 != 0.0f && v2 != 0.0f)
            return true;
        else
            return false;
    }

    /** 
     * Focus Lost
     */
    public void focusLost(FocusEvent fe)
    {
        if (in_action)
            return;

        in_action = true;
        calculateRatio(fe.getSource());
        in_action = false;
    }

    public void focusGained(FocusEvent e)
    {
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e)
    {
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e)
    {

        // System.out.println("key released [" + in_action + "]");

        if (in_action)
            return;

        in_action = true;

        // System.out.println("key released [" + in_action + "]");
        calculateRatio(e.getSource());

        in_action = false;

        // System.out.println("key released [" + in_action + "]");

        /*
         * if (e.getKeyCode() == KeyEvent.VK_ENTER)
         * {
         * cmdOk();
         * }
         */

    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            this.dispose();
        }
        else if (action.equals("ok"))
        {
            cmdOk();
        }
        else if (action.equals("calculator"))
        {
            RatioCalculatorDialog rcd = new RatioCalculatorDialog(this);

            if (rcd.actionSuccesful())
            {
                float[] res = rcd.getResults();

                this.dtf_ch_ins.setValue(res[0]);
                this.dtf_ins_bg.setValue(res[1]);
                this.dtf_bg_ch.setValue(res[2]);
                this.tdd = res[3];
            }
        }
        else if (action.equals("add_row"))
        {
            if (!checkTDD())
                return;

            RatioEntryDialog red = new RatioEntryDialog(this, tdd, 100);

            if (red.actionSuccesful())
            {
                // this.list_ratios.add(red.getResultObject());
                RatioExtendedDialog.erc.add(red.getResultObject());
                ((AbstractTableModel) this.table_list_ratios.getModel()).fireTableDataChanged();
            }
        }
        else if (action.equals("edit_row"))
        {
            if (!checkTDD())
                return;

            if (this.table_list_ratios.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            RatioEntry re = RatioExtendedDialog.erc.get(this.table_list_ratios.getSelectedRow());

            RatioEntryDialog red = new RatioEntryDialog(this, tdd, re);

            if (red.actionSuccesful())
            {
                ((AbstractTableModel) this.table_list_ratios.getModel()).fireTableDataChanged();
            }

        }
        else if (action.equals("delete_row"))
        {
            if (this.table_list_ratios.getSelectedRow() == -1)
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
                RatioEntry re = RatioExtendedDialog.erc.get(this.table_list_ratios.getSelectedRow());
                RatioExtendedDialog.erc.remove(re);
                ((AbstractTableModel) this.table_list_ratios.getModel()).fireTableDataChanged();
            }
            catch (Exception ex)
            {
                System.out.println("RatioExtendedDialog:Action:Delete Row: " + ex);
                // log.error("Action::Delete Row::Exception: " + ex, ex);
            }

        }
        else if (action.equals("check_data"))
        {
            if (!checkData())
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("CHECK_RATIOEXTENDED_FAILED"),
                    m_ic.getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("CHECK_RATIOEXTENDED_DATA_OK"),
                    m_ic.getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
        {
            System.out.println("RatioExtendedDialog::unknown command: " + action);
        }

    }

    private boolean checkTDD()
    {

        if (tdd == 0.0f)
        {
            JOptionPane.showMessageDialog(this, m_ic.getMessage("TDD_MUST_BE_SET_BEFORE_ACTION"),
                m_ic.getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean checkData()
    {
        // System.out.println("RatioExtendedDialog::CheckData not implemented. "
        // );

        int[] timex = new int[24 * 60];

        for (int i = 0; i < 24 * 60; i++)
        {
            timex[i] = 0;
        }

        for (int i = 0; i < RatioExtendedDialog.erc.size(); i++)
        {
            RatioEntry re = RatioExtendedDialog.erc.get(i);

            // System.out.println("#" + i + ": " + re.getMinuteFrom() + " " +
            // re.getMinuteTo());

            for (int j = re.getMinuteFrom(); j <= re.getMinuteTo(); j++)
            {
                timex[j] += 1;
            }
        }

        boolean error = false;

        for (int i = 0; i < 24 * 60; i++)
        {
            if (timex[i] != 1)
            {
                int h = i / 60;
                // System.out.println("Timex: " + timex[i] +
                // ", Error on minute: " + (i/60) + ":" + (i-(h*60)) );
                error = true;
                // return false;
            }
        }

        return !error;
    }

    private void cmdOk()
    {

        if (!checkData())
        {
            JOptionPane.showMessageDialog(this, m_ic.getMessage("CHECK_RATIOEXTENDED_FAILED"),
                m_ic.getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        if (save())
        {
            this.dispose();
        }

        // to-do
        /*
         * if (this.m_add_action)
         * {
         * // add
         * if (debug)
         * System.out.println("dV: " + dV);
         * //this.m_dailyValuesRow = new DailyValuesRow();
         * this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());
         * // if (isFieldSet(BGField.getText()))
         * float f = m_da.getJFormatedTextValueFloat(ftf_bg1);
         * if (f>0.0)
         * {
         * //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex()+1,
         * f);
         * this.m_dailyValuesRow.setBG(1, f);
         * }
         * this.m_dailyValuesRow.setIns1(m_da.getJFormatedTextValueInt(this.ftf_ins1
         * ));
         * this.m_dailyValuesRow.setIns2(m_da.getJFormatedTextValueInt(this.ftf_ins2
         * ));
         * // checkDecimalFields(Ins1Field.getText()));
         * //
         * this.m_dailyValuesRow.setIns2(checkDecimalFields(Ins2Field.getText(
         * )));
         * //this.m_dailyValuesRow.setCH(checkDecimalFields(BUField.getText()));
         * this.m_dailyValuesRow.setCH(m_da.getJFormatedTextValueFloat(this.ftf_ch
         * ));
         * this.m_dailyValuesRow.setActivity(ActField.getText());
         * this.m_dailyValuesRow.setUrine(UrineField.getText());
         * this.m_dailyValuesRow.setComment(CommentField.getText());
         * //this.m_dailyValuesRow.setMealIdsList(null);
         * dV.addRow(this.m_dailyValuesRow);
         * this.m_actionDone = true;
         * this.dispose();
         * }
         * else
         * {
         * // edit
         * this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());
         * float f = m_da.getJFormatedTextValueFloat(ftf_bg1);
         * if (f>0.0)
         * {
         * //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex()+1,
         * f);
         * this.m_dailyValuesRow.setBG(1, f);
         * }
         * // if (isFieldSet(BGField.getText()))
         * // this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex()+1,
         * checkDecimalFields(BGField.getText()));
         * this.m_dailyValuesRow.setIns1(m_da.getJFormatedTextValueInt(this.ftf_ins1
         * ));
         * this.m_dailyValuesRow.setIns2(m_da.getJFormatedTextValueInt(this.ftf_ins2
         * ));
         * //
         * this.m_dailyValuesRow.setIns1(checkDecimalFields(Ins1Field.getText(
         * )));
         * //
         * this.m_dailyValuesRow.setIns2(checkDecimalFields(Ins2Field.getText(
         * )));
         * this.m_dailyValuesRow.setCH(m_da.getJFormatedTextValueFloat(this.ftf_ch
         * ));
         * //
         * this.m_dailyValuesRow.setCH(checkDecimalFields(BUField.getText()));
         * this.m_dailyValuesRow.setActivity(ActField.getText());
         * this.m_dailyValuesRow.setUrine(UrineField.getText());
         * this.m_dailyValuesRow.setComment(CommentField.getText());
         * //this.m_dailyValuesRow.setMealIdsList(null);
         * //mod.fireTableChanged(null);
         * //clearFields();
         * this.m_actionDone = true;
         * this.dispose();
         * }
         */
    }

    /**
     * Was Action Successful
     * 
     * @return
     */
    public boolean actionSuccessful()
    {
        return m_actionDone;
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
        return "GGC_Ratio_Extended";
    }

}
