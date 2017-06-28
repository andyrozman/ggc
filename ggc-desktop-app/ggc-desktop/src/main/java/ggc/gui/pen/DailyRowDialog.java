package ggc.gui.pen;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.components.DateTimeComponent;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.Converter_mgdL_mmolL;
import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.ExtendedDailyValueType;
import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.plugins.GGCPluginType;
import ggc.core.plugins.NutriPlugIn;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.bolushelper.BolusHelper;
import ggc.gui.dialogs.fooddesc.FoodDescriptionDialog;

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
 *  Filename:     DailyRowDialog
 *  Description:  Dialog for adding entry for day and time
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this

// This one will be deprecated, with DailyRowDialogPen
// need to refactor this
public class DailyRowDialog extends JDialog implements ActionListener, KeyListener, HelpCapable, ChangeListener // FocusListener,
{

    private static final long serialVersionUID = 6763016271693781911L;

    private DataAccess dataAccess = DataAccess.getInstance();
    private I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();
    private ConfigurationManagerWrapper configurationManagerWrapper = dataAccess.getConfigurationManagerWrapper();

    private boolean m_actionDone = false;

    JTextField ActField, CommentField, UrineField;
    JLabel label_title = new JLabel();
    // JLabel label_food;
    JCheckBox cb_food_set;
    DateTimeComponent dtc;

    String sDate = null;
    DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;
    // NumberFormat bg_displayFormat, bg_editFormat;

    // JComponent components[] = new JComponent[9];

    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;

    private boolean m_add_action = true;
    private Container m_parent = null;

    // ftf_ins1, ftf_ins2,
    private JSpinner[] spinner_arr = null;
    Hashtable<Integer, Integer> table_ins_pos;
    Hashtable<Integer, Integer> table_pos_ins;
    int insulin_count = 0;


    /**
     * Constructor
     * 
     * @param ndV
     * @param nDate
     * @param dialog
     */
    public DailyRowDialog(DailyValues ndV, String nDate, JDialog dialog)
    {
        super(dialog, "", true);

        m_parent = dialog;
        initParameters(ndV, nDate);
    }


    /**
     * Constructor
     * 
     * @param ndV
     * @param nDate
     * @param frame
     */
    public DailyRowDialog(DailyValues ndV, String nDate, JFrame frame)
    {
        super(frame, "", true);
        m_parent = frame;
        initParameters(ndV, nDate);
    }


    /**
     * Constructor
     * 
     * @param ndr
     * @param dialog
     */
    public DailyRowDialog(DailyValuesRow ndr, JDialog dialog)
    {
        super(dialog, "", true);
        m_parent = dialog;
        initParameters(ndr);
    }


    /**
     * Constructor
     * 
     * @param ndr
     * @param frame
     */
    public DailyRowDialog(DailyValuesRow ndr, JFrame frame)
    {
        super(frame, "", true);
        m_parent = frame;
        initParameters(ndr);
    }


    private void initParameters(DailyValues ndV, String nDate)
    {
        setTitle(i18nControl.getMessage("ADD_ROW"));
        label_title.setText(i18nControl.getMessage("ADD_ROW"));

        sDate = nDate;
        dV = ndV;
        this.m_add_action = true;
        init();
        setDate();

        if (this.m_dailyValuesRow == null)
        {
            this.m_dailyValuesRow = new DailyValuesRow();
        }
        this.updateMealsSet();
        this.setVisible(true);
    }


    private void initParameters(DailyValuesRow ndr)
    {
        setTitle(i18nControl.getMessage("EDIT_ROW"));
        label_title.setText(i18nControl.getMessage("EDIT_ROW"));

        sDate = ndr.getDateAsString();
        this.m_dailyValuesRow = ndr;

        this.m_add_action = false;
        init();
        load();

        this.updateMealsSet();
        this.setVisible(true);
    }


    private void setDate()
    {
        // System.out.println("Date: " + sDate);

        StringTokenizer strtok = new StringTokenizer(sDate, ".");

        String day = strtok.nextToken();
        String month = strtok.nextToken();
        String year = strtok.nextToken();

        GregorianCalendar gc = new GregorianCalendar();

        String dt = null;
        String sg = "" + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.DAY_OF_MONTH), 2) + "."
                + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.MONTH) + 1, 2) + "." + gc.get(Calendar.YEAR);

        if (sg.equals(sDate))
        {
            dt = year + dataAccess.getLeadingZero(month, 2) + dataAccess.getLeadingZero(day, 2)
                    + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.HOUR_OF_DAY), 2)
                    + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.MINUTE), 2);
        }
        else
        {
            dt = year + dataAccess.getLeadingZero(month, 2) + dataAccess.getLeadingZero(day, 2) + "0000";
        }

        this.dtc.setDateTime(Long.parseLong(dt));

    }


    private void load()
    {
        this.dtc.setDateTime(this.m_dailyValuesRow.getDateTime());

        if (m_dailyValuesRow.getBG() > 0)
        {
            this.spinner_arr[4].setValue((int) m_dailyValuesRow.getBGRaw());

            // this.ftf_bg1.setValue(new Integer((int)
            // m_dailyValuesRow.getBGRaw()));
            // this.ftf_bg2.setValue(new
            // Float(dataAccess.getBGValueDifferent(DataAccess.BG_MGDL,
            // m_dailyValuesRow.getBGRaw())));
        }

        float val = 0.0f;

        if (this.table_ins_pos.containsKey(1))
        {
            val = this.m_dailyValuesRow.getIns1() + 0.1f * dataAccess.getFloatValueFromString(
                this.m_dailyValuesRow.getExtendedValue(ExtendedDailyValueType.DecimalPartInsulin1), 0.0f);
            this.spinner_arr[this.table_ins_pos.get(1)].setValue(val);
            // this.spinner_arr[0].setValue(val); //new Integer((int)
            // this.m_dailyValuesRow.getIns1()));
        }

        if (this.table_ins_pos.containsKey(2))
        {
            val = this.m_dailyValuesRow.getIns2() + 0.1f * dataAccess.getFloatValueFromString(
                this.m_dailyValuesRow.getExtendedValue(ExtendedDailyValueType.DecimalPartInsulin2), 0.0f);
            this.spinner_arr[this.table_ins_pos.get(2)].setValue(val); // new
                                                                       // Integer((int)
                                                                       // this.m_dailyValuesRow.getIns2()));
        }

        if (this.table_ins_pos.containsKey(3))
        {
            val = this.m_dailyValuesRow.getIns3();
            this.spinner_arr[this.table_ins_pos.get(3)].setValue(val);
        }

        // this.ftf_ch.setValue(new Float(this.m_dailyValuesRow.getCH()));
        this.spinner_arr[3].setValue(this.m_dailyValuesRow.getCH());

        ActField.setText(this.m_dailyValuesRow.getActivity());
        UrineField.setText(this.m_dailyValuesRow.getUrine());

        this.cb_food_set.setEnabled(false);
        this.cb_food_set.setSelected(this.m_dailyValuesRow.areMealsSet());
        this.cb_food_set.setEnabled(true);

        CommentField.setText(this.m_dailyValuesRow.getComment());
    }


    private void init()
    {
        dataAccess.addComponent(this);

        int x = 0;
        int y = 0;
        int width = 400;
        int height = 500;

        Rectangle bnd = m_parent.getBounds();

        x = bnd.width / 2 + bnd.x - width / 2;
        y = bnd.height / 2 + bnd.y - height / 2;

        this.setBounds(x, y, width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;
        spinner_arr = new JSpinner[6];
        // 0 = Ins1
        // 1 = Ins2
        // 2 = Ins3
        // 3 = CH
        // 4 = BG.1
        // 5 = BG.2

        this.getContentPane().add(panel);

        // this was added much later, so GUI won't be rewritten if not necessary
        ATSwingUtils.initLibrary();

        label_title.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(0, 15, 400, 35);
        panel.add(label_title);

        addLabel(i18nControl.getMessage("DATE") + ":", 78, panel);
        addLabel(i18nControl.getMessage("TIME") + ":", 108, panel);

        this.dtc = new DateTimeComponent(this.dataAccess, DateTimeComponent.ALIGN_VERTICAL, 5);
        dtc.setBounds(140, 75, 100, 35);
        panel.add(dtc);

        addLabel(i18nControl.getMessage("BLOOD_GLUCOSE") + ":", 138, panel);
        // addLabel(configurationManagerWrapper.getIns1Name() + " (" +
        // configurationManagerWrapper.getIns1Abbr() + ") :",
        // 198, panel);

        addLabel("mg/dL", 140, 138, panel);
        addLabel("mmol/L", 255, 138, panel);

        /*
         * this.ftf_bg1 = getTextField(2, 0, new Integer(0), 190, 138, 45, 25,
         * panel);
         * this.ftf_bg2 = getTextField(2, 1, new Float(0.0f), 315, 138, 45, 25,
         * panel);
         * this.ftf_bg1.addFocusListener(this);
         * this.ftf_bg2.addFocusListener(this);
         */

        this.spinner_arr[4] = ATSwingUtils.getJSpinner(0.0f, 0.0f, 999.0f, 1.0f, 190, 138, 45, 25, panel);
        this.spinner_arr[4].addChangeListener(this);

        this.spinner_arr[5] = ATSwingUtils.getJSpinner(0.0f, 0.0f, 55.4f, 0.1f, 315, 138, 45, 25, panel);
        this.spinner_arr[5].addChangeListener(this);

        // int next_y = 168;
        int next_y = 173;
        insulin_count = 0;

        // table = new Hashtable<Integer,Integer>();

        table_ins_pos = new Hashtable<Integer, Integer>();
        table_pos_ins = new Hashtable<Integer, Integer>();

        // GGCProperties configurationManagerWrapper = dataAccess.getSettings();

        int ins_type = 0;

        if (configurationManagerWrapper.getIns1Type() > 0)
        {
            ins_type = configurationManagerWrapper.getIns1Type();

            addLabel(configurationManagerWrapper.getIns1Name() + ":", next_y, panel);
            // addLabel(i18nControl.getMessage("INSULIN_1") + ":", next_y,
            // panel);

            table_ins_pos.put(1, insulin_count);
            table_pos_ins.put(insulin_count, 1);

            this.spinner_arr[insulin_count] = ATSwingUtils.getJSpinner(0.0f, 0.0f,
                dataAccess.getMaxValues(DataAccess.INSULIN_PEN_INJECTION, ins_type),
                dataAccess.getInsulinPrecision(DataAccess.INSULIN_PEN_INJECTION, ins_type), 140, next_y, 55, 25, panel);

            next_y += 30;
            insulin_count++;
        }

        if (configurationManagerWrapper.getIns2Type() > 0)
        {
            ins_type = configurationManagerWrapper.getIns2Type();

            addLabel(configurationManagerWrapper.getIns2Name() + ":", next_y, panel);
            // addLabel(i18nControl.getMessage("INSULIN_2") + ":", next_y,
            // panel);
            // table.put(insulin_count, 2);
            table_ins_pos.put(2, insulin_count);
            table_pos_ins.put(insulin_count, 2);

            this.spinner_arr[insulin_count] = ATSwingUtils.getJSpinner(0.0f, 0.0f,
                dataAccess.getMaxValues(DataAccess.INSULIN_PEN_INJECTION, ins_type),
                dataAccess.getInsulinPrecision(DataAccess.INSULIN_PEN_INJECTION, ins_type), 140, next_y, 55, 25, panel);

            next_y += 30;
            insulin_count++;
        }

        if (configurationManagerWrapper.getIns3Type() > 0)
        {
            ins_type = configurationManagerWrapper.getIns3Type();

            addLabel(configurationManagerWrapper.getIns3Name() + ":", next_y, panel);
            // addLabel(i18nControl.getMessage("INSULIN_3") + ":", next_y,
            // panel);
            // table.put(insulin_count, 3);
            table_ins_pos.put(3, insulin_count);
            table_pos_ins.put(insulin_count, 3);

            this.spinner_arr[insulin_count] = ATSwingUtils.getJSpinner(0.0f, 0.0f,
                dataAccess.getMaxValues(DataAccess.INSULIN_PEN_INJECTION, ins_type),
                dataAccess.getInsulinPrecision(DataAccess.INSULIN_PEN_INJECTION, ins_type), 140, next_y, 55, 25, panel);

            next_y += 30;
            insulin_count++;
        }

        // addLabel(i18nControl.getMessage("INSULIN_1") + ":", 168, panel);
        // addLabel(configurationManagerWrapper.getIns2Name() + " (" +
        // configurationManagerWrapper.getIns2Abbr() + "):",
        // 228, panel);
        // addLabel(i18nControl.getMessage("INSULIN_2") + ":", 198, panel);

        /*
         * this.spinner_arr[0] = ATSwingUtils.getJSpinner(0.0f, 0.0f,
         * dataAccess.getMaxValues(DataAccess.INSULIN_PEN_INJECTION,
         * DataAccess.INSULIN_DOSE_BOLUS),
         * dataAccess.getInsulinPrecision(DataAccess.INSULIN_PEN_INJECTION,
         * DataAccess.INSULIN_DOSE_BOLUS),
         * 140, 168, 55, 25, panel);
         * //getTextField(2, 0, new Integer(0), 140, 198, 55, 25, panel);
         * // FIXME
         * //this.ftf_ins2 = getTextField(2, 0, new Integer(0), 140, 228, 55,
         * 25, panel);
         * this.spinner_arr[1] = ATSwingUtils.getJSpinner(0.0f, 0.0f,
         * dataAccess.getMaxValues(DataAccess.INSULIN_PEN_INJECTION,
         * DataAccess.INSULIN_DOSE_BASAL),
         * dataAccess.getInsulinPrecision(DataAccess.INSULIN_PEN_INJECTION,
         * DataAccess.INSULIN_DOSE_BASAL),
         * 140, 198, 55, 25, panel);
         * //getTextField(2, 0, new Integer(0), 140, 228, 55, 25, panel);
         */

        // int next_y = 168;

        // int insulin_count = 2;

        // next_y += insulin_count * 30;

        addLabel(i18nControl.getMessage("CH_LONG") + ":", next_y, panel);
        addLabel(i18nControl.getMessage("FOOD") + ":", next_y + 30, panel);

        // this.ftf_ch = getTextField(2, 2, new Float(0.0f), 140, next_y, 55,
        // 25, panel);

        this.spinner_arr[3] = ATSwingUtils.getJSpinner(0.0f, 0.0f, 250.0f, 1, 140, next_y, 55, 25, panel);

        addComponent(cb_food_set = new JCheckBox(" " + i18nControl.getMessage("FOOD_SET")), 90, next_y + 32, 130,
            panel);
        cb_food_set.setMultiClickThreshhold(500);
        cb_food_set.addChangeListener(new ChangeListener()
        {

            /**
             * Invoked when the target of the listener has changed its state.
             * 
             * @param e ChangeEvent object
             */
            public void stateChanged(ChangeEvent e)
            {
                if (in_process)
                    return;

                in_process = true;
                cb_food_set.setSelected(isMealSet());
                in_process = false;
            }
        });

        // bottom entries

        int next_bottom_y = 0;

        if (insulin_count == 3)
        {
            next_bottom_y = 327;
        }
        else
        {
            next_bottom_y = 297;
        }

        addLabel(i18nControl.getMessage("URINE") + ":", next_bottom_y, panel);
        addLabel(i18nControl.getMessage("ACTIVITY") + ":", next_bottom_y + 30, panel);
        addLabel(i18nControl.getMessage("COMMENT") + ":", next_bottom_y + 60, panel);

        addComponent(UrineField = new JTextField(), 120, next_bottom_y, 240, panel);
        addComponent(ActField = new JTextField(), 120, next_bottom_y + 30, 240, panel);
        addComponent(CommentField = new JTextField(), 120, next_bottom_y + 60, 240, panel);

        String button_command[] = { "bolus_helper", i18nControl.getMessage("BOLUS_HELPER"), //
                                    "update_ch", i18nControl.getMessage("UPDATE_FROM_FOOD"), //
                                    "edit_food", i18nControl.getMessage("EDIT_FOOD"), //
                                    "ok", i18nControl.getMessage("OK"), //
                                    "cancel", i18nControl.getMessage("CANCEL"), //
                                    "food_desc", i18nControl.getMessage("FOOD_BY_DESCRIPTION"), //
        };

        String button_icon[] = { null, null, null, "ok.png", "cancel.png", null };

        int button_coord[] = { 220, 173, 140, 1, 220, 203, 140, 1, 220, 233, 140, 1, 30, next_bottom_y + 102, 110, 1,
                               145, next_bottom_y + 102, 110, 1, 220, 263, 140, 1

                // 250, 390, 80, 0
        };

        JButton button;
        // int j=0;
        for (int i = 0, j = 0, k = 0; i < button_coord.length; i += 4, j += 2, k++)
        {
            button = new JButton("  " + button_command[j + 1]);
            button.setActionCommand(button_command[j]);
            // button.setFont(dataAccess.getFont(DataAccess.FONT_NORMAL));
            button.addActionListener(this);

            if (button_icon[k] != null)
            {
                button.setIcon(ATSwingUtils.getImageIcon_22x22(button_icon[k], this, dataAccess));
            }

            if (button_coord[i + 3] == 0)
            {
                button.setEnabled(false);
            }

            // if (k <= 1)
            // addComponent(button, button_coord[i], button_coord[i + 1],
            // button_coord[i + 2], panel);
            // else
            addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], 25, false, panel);

        }

        help_button = ATSwingUtils.createHelpButtonByBounds(260, next_bottom_y + 102, 110, 25, this,
            ATSwingUtils.FONT_NORMAL, dataAccess);
        panel.add(help_button);
        dataAccess.enableHelp(this);

        this.setBounds(x, y, width, next_bottom_y + 175);

    }


    private boolean isMealSet()
    {
        return this.m_dailyValuesRow.areMealsSet();
    }


    private void addLabel(String text, int posY, JPanel parent)
    {
        addLabel(text, 30, posY, parent);
    }


    private void addLabel(String text, int posX, int posY, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(posX, posY, 100, 25);
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD));
        parent.add(label);
    }


    private void addComponent(JComponent comp, int posX, int posY, int width, JPanel parent)
    {
        addComponent(comp, posX, posY, width, 23, true, parent);
    }


    private void addComponent(JComponent comp, int posX, int posY, int width, int height, boolean change_font,
            JPanel parent)
    {
        comp.setBounds(posX, posY, width, height);
        comp.addKeyListener(this);
        parent.add(comp);
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
            commandOk();
        }
        else if (action.equals("edit_food"))
        {
            commandEditFood();
        }
        else if (action.equals("update_ch"))
        {
            if (this.isMealSet())
            {
                if (dataAccess.isValueSet(this.m_dailyValuesRow.getMealsIds()))
                {

                    PlugInClient pc = DataAccess.getInstance().getPlugIn(GGCPluginType.NutritionToolPlugin);

                    if ((pc != null) && (pc.isActiveWarning(true, this)))
                    {
                        Object[] data = pc.executeCommandDialogReturn(this, NutriPlugIn.COMMAND_RECALCULATE_CH,
                            this.m_dailyValuesRow.getMealsIds());

                        if (data != null)
                        {
                            updateMealsSet();
                            setCh((String) data[0]);
                        }
                    }

                }
                else if (dataAccess.isValueSet(this.m_dailyValuesRow.getFoodDescriptionCH()))
                {
                    updateMealsSet();
                    setCh(this.m_dailyValuesRow.getFoodDescriptionCH());
                }
            }
        }
        else if (action.equals("bolus_helper"))
        {
            BolusHelper bh = new BolusHelper(this, dataAccess.getFloatValue(this.spinner_arr[5].getValue()),
                    // dataAccess.getJFormatedTextValueFloat(ftf_bg2),
                    // dataAccess.getJFormatedTextValueFloat(this.ftf_ch),
                    dataAccess.getFloatValue(this.spinner_arr[3].getValue()), this.dtc.getDateTime(), 1,
                    DataAccess.INSULIN_PEN_INJECTION);

            if (bh.hasResult())
            {
                this.spinner_arr[0].setValue(bh.getResult());
            }
        }
        else if (action.equals("food_desc"))
        {
            new FoodDescriptionDialog(this.m_dailyValuesRow, this);
            updateMealsSet();
            setCh(this.m_dailyValuesRow.getFoodDescriptionCH());
        }
        else
        {
            System.out.println("DailyRowDialog::unknown command: " + action);
        }

    }


    private void commandEditFood()
    {
        PlugInClient pc = DataAccess.getInstance().getPlugIn(GGCPluginType.NutritionToolPlugin);

        if ((pc != null) && (pc.isActiveWarning(true, this)))
        {
            Object[] data = pc.executeCommandDialogReturn(this, NutriPlugIn.COMMAND_DB_FOOD_SELECTOR,
                this.m_dailyValuesRow.getMealsIds());

            if (data != null)
            {
                this.m_dailyValuesRow.setMealsIds((String) data[0]);
                // this.ftf_ch.setValue(new Float((String)data[1]));
                this.spinner_arr[3].setValue(this.m_dailyValuesRow.getCH());

                updateMealsSet();
            }
        }
    }


    private void setCh(String ch_str)
    {
        if (!dataAccess.isValueSet(ch_str))
        {
            this.spinner_arr[3].setValue(0.0f);
            // this.ftf_ch.setValue(0.0f);
            return;
        }

        // String s = ch_str;
        // ch_str = ch_str.replace(ATDataAccessAbstract.false_decimal,
        // ATDataAccessAbstract.real_decimal);
        ch_str = ch_str.replace(",", ".");
        // this.ftf_ch.setValue(dataAccess.getFloatValue(ch_str));
        this.spinner_arr[3].setValue(dataAccess.getFloatValue(ch_str));
    }


    private void updateMealsSet()
    {
        int meals_set = this.m_dailyValuesRow.areMealsSetType();

        if (meals_set == 0)
        {
            this.cb_food_set.setSelected(false);
            this.cb_food_set.setText("  " + String.format(i18nControl.getMessage("FOOD_SET"), "?"));
        }
        else
        {
            this.cb_food_set.setSelected(true);
            this.cb_food_set.setText("  " + String.format(i18nControl.getMessage("FOOD_SET"), meals_set));
        }

    }


    private void commandOk()
    {

        this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());

        float f = dataAccess.getFloatValue(this.spinner_arr[4].getValue());

        if (f > 0.0)
        {
            this.m_dailyValuesRow.setBG(GlucoseUnitType.mg_dL, f);
        }

        setInsulinValues();

        this.m_dailyValuesRow.setCH(dataAccess.getFloatValue(this.spinner_arr[3].getValue()));

        this.m_dailyValuesRow.setActivity(ActField.getText());
        this.m_dailyValuesRow.setUrine(UrineField.getText());
        this.m_dailyValuesRow.setComment(CommentField.getText());
        this.m_dailyValuesRow.createExtendedString();

        if (this.m_add_action)
        {
            dV.addRow(this.m_dailyValuesRow);
        }

        this.m_actionDone = true;

        dataAccess.removeComponent(this);
        this.dispose();

        // to-do
        /*
         * if (this.m_add_action)
         * {
         * // add
         * if (debug)
         * System.out.println("dV: " + dV);
         * // this.m_dailyValuesRow = new DailyValuesRow();
         * this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());
         * float f = dataAccess.getJFormatedTextValueFloat(ftf_bg1);
         * if (f > 0.0)
         * {
         * this.m_dailyValuesRow.setBG(1, f);
         * }
         * setInsulinValues();
         * this.m_dailyValuesRow.setCH(dataAccess.getJFormatedTextValueFloat(
         * this
         * .ftf_ch
         * ));
         * this.m_dailyValuesRow.setActivity(ActField.getText());
         * this.m_dailyValuesRow.setUrine(UrineField.getText());
         * this.m_dailyValuesRow.setComment(CommentField.getText());
         * dV.addRow(this.m_dailyValuesRow);
         * this.m_actionDone = true;
         * }
         * else
         * {
         * // edit
         * this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());
         * float f = dataAccess.getJFormatedTextValueFloat(ftf_bg1);
         * if (f > 0.0)
         * {
         * this.m_dailyValuesRow.setBG(1, f);
         * }
         * setInsulinValues();
         * this.m_dailyValuesRow.setCH(dataAccess.getJFormatedTextValueFloat(
         * this
         * .ftf_ch
         * ));
         * this.m_dailyValuesRow.setActivity(ActField.getText());
         * this.m_dailyValuesRow.setUrine(UrineField.getText());
         * this.m_dailyValuesRow.setComment(CommentField.getText());
         * this.m_actionDone = true;
         * }
         * dataAccess.removeComponent(this);
         * this.dispose();
         */
    }


    private void setInsulinValues()
    {

        for (int i = 0; i < this.insulin_count; i++)
        {
            JSpinner sp = this.spinner_arr[i];

            String vd = dataAccess.getFloatAsString(dataAccess.getFloatValue(sp.getValue()), 1);
            vd = vd.replace(',', '.');

            int v_i = Integer.parseInt(vd.substring(0, vd.indexOf(".")));
            int v_d = Integer.parseInt(vd.substring(vd.indexOf(".") + 1));

            int ins_num = this.table_pos_ins.get(i);

            if (ins_num == 1)
            {
                this.m_dailyValuesRow.setIns1(v_i);
                if (v_d > 0)
                {
                    this.m_dailyValuesRow.setExtendedValue(ExtendedDailyValueType.DecimalPartInsulin1, "" + v_d, true);
                }
            }
            else if (ins_num == 2)
            {
                this.m_dailyValuesRow.setIns2(v_i);
                if (v_d > 0)
                {
                    this.m_dailyValuesRow.setExtendedValue(ExtendedDailyValueType.DecimalPartInsulin2, "" + v_d, true);
                }
            }
            else if (ins_num == 3)
            {
                this.m_dailyValuesRow.setIns3(vd);
            }
        }
    }


    public boolean actionSuccessful()
    {
        return m_actionDone;
    }


    public void keyTyped(KeyEvent e)
    {
    }


    public void keyPressed(KeyEvent e)
    {
    }


    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            commandOk();
        }
    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    public Component getComponent()
    {
        return this.getRootPane();
    }


    public JButton getHelpButton()
    {
        return this.help_button;
    }


    public String getHelpId()
    {
        return "GGC_BG_Daily_Add";
    }


    public void focusGained(FocusEvent event)
    {
    }

    boolean in_action = false;


    /**
     * focusLost
     */
    /*
     * public void focusLost(FocusEvent ev)
     * {
     * focusProcess(ev.getSource());
     * }
     * private void focusProcess(Object src)
     * {
     * if (in_action)
     * return;
     * in_action = true;
     * if (src.equals(this.ftf_bg1))
     * {
     * if (this.ftf_bg1.getText().trim().length() == 0)
     * {
     * in_action = false;
     * this.ftf_bg2.setValue(new Float(0.0f));
     * return;
     * }
     * // System.out.println("focus lost: bg2");
     * int val = dataAccess.getJFormatedTextValueInt(ftf_bg1);
     * // float v_2 = dataAccess.getBGValueDifferent(DataAccess.BG_MGDL, val);
     * float v_2 =
     * dataAccess.getBGConverter().getValueByType(Converter_mgdL_mmolL.
     * UNIT_mg_dL
     * ,
     * Converter_mgdL_mmolL.UNIT_mmol_L, val);
     * this.ftf_bg2.setValue(new Float(v_2));
     * }
     * else if (src.equals(this.ftf_bg2))
     * {
     * // System.out.println("text2: " + this.ftf_bg2.getText());
     * if (this.ftf_bg2.getText().trim().length() == 0)
     * {
     * in_action = false;
     * this.ftf_bg1.setValue(new Integer(0));
     * return;
     * }
     * // System.out.println("focus lost: bg2");
     * float val = dataAccess.getJFormatedTextValueFloat(ftf_bg2);
     * // int v_2 = (int) dataAccess.getBGValueDifferent(DataAccess.BG_MMOL,
     * val);
     * int v_2 =
     * (int)dataAccess.getBGConverter().getValueByType(Converter_mgdL_mmolL.
     * UNIT_mmol_L
     * , Converter_mgdL_mmolL.UNIT_mg_dL, val);
     * this.ftf_bg1.setValue(new Integer(v_2));
     * }
     * else
     * System.out.println("focus lost: unknown");
     * in_action = false;
     * }
     */
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() instanceof JSpinner)
        {
            changeProcess(e.getSource());
        }
    }


    private void changeProcess(Object src)
    {
        System.out.println("change event - before");

        if (in_action)
            return;

        in_action = true;

        // dataAccess.getFloatValue(this.spinner_arr[5].getCode()),

        // System.out.println("change event - in action");

        if (src.equals(this.spinner_arr[4]))
        {
            // System.out.println("spinner 4: " +
            // this.spinner_arr[4].getCode());

            // .ftf_bg1.getText().trim().length()
            if (this.dataAccess.getFloatValue(this.spinner_arr[4].getValue()) == 0.0f)
            {
                // this.ftf_bg2.setValue(new Float(0.0f));
                this.spinner_arr[5].setValue(0.0f);
                in_action = false;
                return;
            }

            // System.out.println("focus lost: bg1 (spinner[4]). mg/dL: " +
            // this.spinner_arr[4].getCode());
            float val = this.dataAccess.getFloatValue(this.spinner_arr[4].getValue());
            // dataAccess.getJFormatedTextValueInt(ftf_bg1);
            // float v_2 = dataAccess.getBGValueDifferent(DataAccess.BG_MGDL,
            // val);
            float v_2 = dataAccess.getBGConverter().getValueByType(Converter_mgdL_mmolL.UNIT_mg_dL,
                Converter_mgdL_mmolL.UNIT_mmol_L, val);
            // this.ftf_bg2.setValue(new Float(v_2));

            // System.out.println("focus lost: bg1 (spinner[4]). mmol/L: " +
            // v_2);

            this.spinner_arr[5].setValue(v_2);
        }
        else if (src.equals(this.spinner_arr[5])) // if
                                                  // (src.equals(this.ftf_bg2))
        {
            // System.out.println("text2: " + this.ftf_bg2.getText());

            if (this.dataAccess.getFloatValue(this.spinner_arr[5].getValue()) == 0.0f) // (this.ftf_bg2.getText().trim().length()
            // ==
            // 0)
            {
                this.spinner_arr[4].setValue(0);
                // this.ftf_bg1.setValue(new Integer(0));
                in_action = false;
                return;
            }

            // System.out.println("focus lost: bg2 (spinner[5])");

            // System.out.println("focus lost: bg2");
            float val = this.dataAccess.getFloatValue(this.spinner_arr[5].getValue());
            // dataAccess.getJFormatedTextValueFloat(ftf_bg2);
            // int v_2 = (int)
            // dataAccess.getBGValueDifferent(DataAccess.BG_MMOL,
            // val);
            int v_2 = (int) dataAccess.getBGConverter().getValueByType(Converter_mgdL_mmolL.UNIT_mmol_L,
                Converter_mgdL_mmolL.UNIT_mg_dL, val);
            // this.ftf_bg1.setValue(new Integer(v_2));
            this.spinner_arr[4].setValue(v_2);
        }
        else
        {
            System.out.println("focus lost: unknown");
        }

        in_action = false;

    }

}
