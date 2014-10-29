package ggc.pump.gui.manual;

import ggc.core.data.Converter_mgdL_mmolL;
import ggc.core.plugins.GGCPluginType;
import ggc.core.plugins.NutriPlugIn;
import ggc.core.util.DataAccess;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.util.DataAccessPump;
import ggc.shared.fooddesc.FoodDescriptionDialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.dialogs.TransferDialog;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.utils.ATSwingUtils;

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
 *  Filename:     PumpDataAdditionalWizardTwo
 *  Description:  Wizard for adding Additional data (step 2)
 *
 *  Author: Andy {andy@atech-software.com}
 */

// fix this

public class PumpDataAdditionalWizardTwo extends JDialog implements ActionListener, HelpCapable, /*
                                                                                                  * FocusListener
                                                                                                  * ,
                                                                                                  */KeyListener,
        ChangeListener
{

    private static final long serialVersionUID = 6600123145384610341L;

    private DataAccessPump m_da = DataAccessPump.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    JLabel label_title = new JLabel();
    Font f_normal = m_da.getFont(DataAccessPump.FONT_NORMAL);
    Font f_bold = m_da.getFont(DataAccessPump.FONT_NORMAL);

    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;
    private Container m_parent = null;

    private int m_type = 0;

    JLabel label_1, label_2;
    JTextField text_1;
    JDecimalTextField num_1, num_2;
    JSpinner spin_1, spin_2;
    JCheckBox cb_1;
    JButton button_1;
    int width;

    // PumpAdditionalDataType m_pump_add; // = new PumpAdditionalDataType();

    boolean was_action = false;
    PumpValuesEntryExt pump_objects_ext[];

    PumpValuesEntryExt data_object;
    PumpValuesEntryExt data_object2;
    private String internal_data[] = null;
    float food_ch;

    // new data
    /**
     * Constructor
     *
     * @param wiz_one
     * @param type
     */
    public PumpDataAdditionalWizardTwo(PumpDataAdditionalWizardOne wiz_one, String type) // ,
                                                                                         // PumpAdditionalDataType
                                                                                         // pump_add)
    {
        super(wiz_one, "", true);

        // this.m_pump_add = pump_add;

        // this.old_data = data;
        m_parent = wiz_one;
        this.m_type = m_da.getAdditionalTypes().getTypeFromDescription(type);

        ATSwingUtils.initLibrary();

        init();
    }

    /**
     * Constructor
     *
     * @param dialog
     * @param pc
     */
    public PumpDataAdditionalWizardTwo(JDialog dialog, PumpValuesEntryExt pc)
    {
        super(dialog, "", true);

        // FIXME
        // this.m_pump_add = m_da.getAdditionalTypes();

        // this.old_data = data;
        m_parent = dialog;
        this.m_type = pc.getType();

        ATSwingUtils.initLibrary();

        init();

        data_object = pc;

        loadObject();
    }

    /**
     * Constructor
     *
     * @param dialog
     * @param pc
     * @param pc2
     */
    public PumpDataAdditionalWizardTwo(JDialog dialog, PumpValuesEntryExt pc, PumpValuesEntryExt pc2)
    {
        super(dialog, "", true);

        // FIXME
        // this.m_pump_add = m_da.getAdditionalTypes();

        // this.old_data = data;
        m_parent = dialog;
        this.m_type = pc.getType();

        ATSwingUtils.initLibrary();

        this.data_object = pc;
        this.data_object2 = pc2;

        init();

        loadObject();
    }

    String food_data = null;

    // for edit
    private void loadObject()
    {
        if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_ACTIVITY
                || this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT
                || this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_URINE)
        {
            this.text_1.setText(this.data_object.getValue());
            // po.setValue(this.text_1.getText());
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_BG)
        {
            // this.num_1.setValue(new Float(this.data_object.getValue()));
            // this.focusProcess(num_1);
            // po.setValue(this.num_1.getValue().toString());
            this.spin_1.setValue(new Float(this.data_object.getValue()));
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_CH)
        {

            this.spin_1.setValue(new Float(this.data_object.getValue()));
            // this.num_1.setValue(new Float(this.data_object.getValue()));
            // po.setValue(this.num_1.getValue().toString());
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC)
        {
            food_data = this.data_object.getValue();

            if (this.data_object2 != null)
            {
                food_ch = m_da.getFloatValueFromString(this.data_object2.getValue());
                if (food_ch > 0)
                {
                    this.cb_1.setSelected(true);
                }
            }
            else
            {
                food_ch = 0.0f;
            }

            this.num_1.setText(DataAccess.getFloatAsString(food_ch, 2));

        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB)
        {
            food_data = this.data_object.getValue();

            if (this.data_object2 != null)
            {
                food_ch = m_da.getFloatValueFromString(this.data_object2.getValue());

                if (food_ch > 0)
                {
                    this.cb_1.setSelected(true);
                }
            }
            else
            {
                food_ch = 0.0f;
                this.num_1.setText("" + food_ch);
            }

            this.num_1.setText(DataAccess.getFloatAsString(food_ch, 2));

        }
        else
        {
            System.out.println("Load for this type is not implemented !!!");
        }

    }

    int startx = 0;

    private void init()
    {
        width = 380;
        int height = 310;

        this.setSize(width, height);

        m_da.addComponent(this);
        this.m_da.centerJDialog(this, this.m_parent);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        startx = 60;

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title = ATSwingUtils.getTitleLabel(m_ic.getMessage("ADD_PARAMETER2"), 0, 35, width, 35, panel,
            ATSwingUtils.FONT_BIG_BOLD);

        setTitle(m_ic.getMessage("ADD_PARAMETER2"));

        String button_command[] = { "cancel", m_ic.getMessage("CANCEL"), "ok", m_ic.getMessage("OK"),
        // "help", m_ic.getMessage("HELP")
        };

        // button
        String button_icon[] = { "cancel.png", "ok.png" };
        // null, "ok.png", "cancel.png" };

        int button_coord[] = { startx, 230, 120, 1, startx + 130, 230, 130, 1,
        // 170, 190, 140, 1,
        // 30, 620, 110, 1,
        // 145, 620, 110, 1,
        // 250, 390, 80, 0
        };

        JButton button;
        // int j=0;
        for (int i = 0, j = 0, k = 0; i < button_coord.length; i += 4, j += 2, k++)
        {
            button = new JButton("   " + button_command[j + 1]);
            button.setActionCommand(button_command[j]);
            // button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
            button.addActionListener(this);

            if (button_icon[k] != null)
            {
                button.setIcon(m_da.getImageIcon_22x22(button_icon[k], this));
            }

            if (button_coord[i + 3] == 0)
            {
                button.setEnabled(false);
            }

            if (k <= 1)
            {
                ATSwingUtils.addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], 23, panel);
            }
            else
            {
                ATSwingUtils.addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], 25, panel);
            }
        }

        help_button = m_da.createHelpButtonByBounds(startx + 140, 195, 120, 25, this);
        panel.add(help_button);

        m_da.enableHelp(this);

        initType();

    }

    private void initType()
    {
        switch (this.m_type)
        {
            case PumpAdditionalDataType.PUMP_ADD_DATA_ACTIVITY:
            case PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT:
            case PumpAdditionalDataType.PUMP_ADD_DATA_URINE:
                {
                    areaText();
                }
                break;

            case PumpAdditionalDataType.PUMP_ADD_DATA_BG:
                {
                    areaBG();
                }
                break;

            case PumpAdditionalDataType.PUMP_ADD_DATA_CH:
                {
                    areaCH();
                }
                break;

            case PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB:
            case PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC:
                {
                    areaFood();
                }
                break;

            default:
                {
                    areaUnsupported();
                }
                break;
        }
    }

    // activity, comment, urine
    private void areaText()
    {
        this.label_1 = ATSwingUtils.getLabel(null, startx, 100, 110, 25, this.main_panel);
        this.text_1 = ATSwingUtils.getTextField("", startx, 130, 260, 25, this.main_panel);

        if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_ACTIVITY)
        {
            this.label_1.setText(m_ic.getMessage("ACTIVITY"));
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT)
        {
            this.label_1.setText(m_ic.getMessage("COMMENT"));
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_URINE)
        {
            this.label_1.setText(m_ic.getMessage("URINE"));
        }

    }

    /**
     * Area: BG
     */
    public void areaBG()
    {

        ATSwingUtils.getLabel(m_ic.getMessage("BLOOD_GLUCOSE") + ":", startx + 20, 108, 100, 25, main_panel);
        ATSwingUtils.getLabel("mg/dL", startx + 130, 108, 100, 25, main_panel);
        ATSwingUtils.getLabel("mmol/L", startx + 130, 138, 100, 25, main_panel);

        /*
         * this.num_1 = ATSwingUtils.getNumericTextField(2, 0, new Integer(0),
         * startx + 180, 108, 55, 25, this.main_panel);
         * this.num_2 = ATSwingUtils.getNumericTextField(2, 1, new Float(0.0f),
         * startx + 180, 138, 55, 25, this.main_panel);
         * this.num_1.addFocusListener(this);
         * this.num_1.addKeyListener(this);
         * this.num_2.addFocusListener(this);
         * this.num_2.addKeyListener(this);
         */

        this.spin_1 = ATSwingUtils.getJSpinner(0.0f, 0.0f, 999.0f, 1.0f, startx + 180, 108, 55, 25, this.main_panel);
        this.spin_1.addChangeListener(this);

        this.spin_2 = ATSwingUtils.getJSpinner(0.0f, 0.0f, 55.4f, 0.1f, startx + 180, 138, 55, 25, this.main_panel);
        this.spin_2.addChangeListener(this);

    }

    /**
     * Area: CH
     */
    public void areaCH()
    {

        ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") + ":", startx + 20, 108, 100, 25, main_panel);

        // this.num_1 = ATSwingUtils.getNumericTextField(2, 0, new Integer(0),
        // startx + 180, 108, 55, 25, this.main_panel);

        this.spin_1 = ATSwingUtils.getJSpinner(0.0f, 0.0f, 250.0f, 1, startx + 180, 108, 55, 25, this.main_panel);

    }

    /**
     * Area: Food
     */
    public void areaFood()
    {
        // TODO

        this.cb_1 = new JCheckBox();
        this.cb_1.setBounds(startx + 10, 140, 200, 25);
        this.cb_1.addActionListener(this);
        this.cb_1.setActionCommand("check");
        // this.cb_1.setEnabled(false);
        this.main_panel.add(this.cb_1);

        JLabel l = ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") + ":", 0, 60, width, 35, main_panel,
            ATSwingUtils.FONT_NORMAL_BOLD_P2);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        // label_title =
        // ATSwingUtils.getTitleLabel(m_ic.getMessage("ADD_PARAMETER2"), 0, 35,
        // width, 35, panel, ATSwingUtils.FONT_BIG_BOLD);

        this.num_1 = ATSwingUtils.getNumericTextField(2, 0, new Integer(0), startx + 180, 108, 55, 25, this.main_panel);
        this.num_1.setEditable(false);

        button_1 = ATSwingUtils.getButton(m_ic.getMessage("SET"), startx, 175, 120, 25, this.main_panel,
            ATSwingUtils.FONT_NORMAL, null, "food_desc", this, m_da);

        ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") + ":", startx + 10, 110, 100, 25, main_panel);

        if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB)
        {
            this.cb_1.setText("  " + m_ic.getMessage("IS_FOOD_SET"));
            // ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") + ":", startx +
            // 20, 108, 100, 25, main_panel);
            // this.num_1 = ATSwingUtils.getNumericTextField(2, 0, new
            // Integer(0), startx + 180, 108, 55, 25, this.main_panel);
            l.setText("(" + m_ic.getMessage("FOOD_FROM_DB") + ")");
        }
        else
        {
            this.cb_1.setText("  " + m_ic.getMessage("IS_FOOD_AND_CH_SET"));
            l.setText("(" + m_ic.getMessage("FOOD_BY_DESC") + ")");
            // button_1.setBounds(startx, 175, 120, 25);
        }
    }

    /**
     * Area: Unsupported
     */
    private void areaUnsupported()
    {
        this.label_1 = new JLabel("Unsuported type : " + m_da.getAdditionalTypes().getDescriptions()[this.m_type]);
        this.label_1.setBounds(startx, 100, 100, 25);
        this.main_panel.add(this.label_1);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            // System.out.println("wizard_2 [cancel]");
            this.was_action = false;
            m_da.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("ok"))
        {
            m_da.removeComponent(this);
            cmdOk();
        }
        else if (action.equals("check"))
        {
            System.out.println("check 1");
            this.cb_1.setSelected(!this.cb_1.isSelected());
        }
        else if (action.equals("food_desc"))
        {

            if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC)
            {

                FoodDescriptionDialog td = new FoodDescriptionDialog(this.food_data, food_ch, this);

                if (td.wasAction())
                {
                    this.internal_data = td.getResultValuesString();
                    float f = m_da.getFloatValue(this.internal_data[1]);
                    this.cb_1.setSelected(f > 0);

                    food_data = this.internal_data[0];
                    food_ch = f;
                    this.num_1.setText(DataAccess.getFloatAsString(food_ch, 2));
                }

            }
            else
            {
                PlugInClient pc = DataAccess.getInstance().getPlugIn(GGCPluginType.NUTRITION_TOOL_PLUGIN);

                if (pc.isActiveWarning(true, this))
                {
                    Object[] data = pc.executeCommandDialogReturn(this, NutriPlugIn.COMMAND_DB_FOOD_SELECTOR,
                        this.food_data);

                    if (data != null)
                    {
                        food_data = (String) data[0];
                        food_ch = new Float((String) data[1]);

                        // float f = m_da.getFloatValue(this.internal_data[1]);
                        this.cb_1.setSelected(food_ch > 0);

                        this.internal_data = new String[2];
                        this.internal_data[0] = food_data;
                        this.internal_data[1] = (String) data[1];

                        this.num_1.setText(DataAccess.getFloatAsString(food_ch, 2));
                    }
                }
            }

        }
        else
        {
            System.out.println("PumpDataAdditionalWizardTwo::unknown command: " + action);
        }

    }

    /**
     * Was Action
     *
     * @return
     */
    public boolean wasAction()
    {
        return this.was_action;
    }

    /**
     * Get Objects
     *
     * @return
     */
    public PumpValuesEntryExt[] getObjects()
    {
        return pump_objects_ext;

    }

    private void cmdOk()
    {
        // System.out.println("wizard_2 [ok]");

        this.was_action = true;

        if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB
                || this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC)
        {
            this.pump_objects_ext = new PumpValuesEntryExt[2];

            if (this.internal_data == null)
            {
                PumpValuesEntryExt po = new PumpValuesEntryExt("Pump Manual");
                po.setType(this.m_type);
                po.setValue("");
                this.pump_objects_ext[0] = po;

                po = new PumpValuesEntryExt("Pump Manual");
                po.setType(PumpAdditionalDataType.PUMP_ADD_DATA_CH);
                po.setValue("0");
                this.pump_objects_ext[1] = po;

            }
            else
            {
                PumpValuesEntryExt po = new PumpValuesEntryExt("Pump Manual");
                po.setType(this.m_type);
                po.setValue(this.internal_data[0]);
                this.pump_objects_ext[0] = po;

                po = new PumpValuesEntryExt("Pump Manual");
                po.setType(PumpAdditionalDataType.PUMP_ADD_DATA_CH);
                po.setValue(this.internal_data[1]);
                this.pump_objects_ext[1] = po;
            }

            // PumpValuesEntryExt po = new PumpValuesEntryExt(this.m_pump_add);
            // po.setType(this.m_type);

        }
        else
        {
            this.pump_objects_ext = new PumpValuesEntryExt[1];

            PumpValuesEntryExt po = new PumpValuesEntryExt("Pump Manual");
            po.setType(this.m_type);

            if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_ACTIVITY
                    || this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT
                    || this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_URINE)
            {
                po.setValue(this.text_1.getText());
            }
            else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_BG)
            {
                // po.setValue(this.num_1.getValue().toString());
                po.setValue("" + m_da.getFloatValue(this.spin_1.getValue()));
            }
            else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_CH)
            {
                // System.out.println("val: " + this.num_1.getValue());
                // po.setValue(this.num_1.getValue().toString());
                po.setValue("" + m_da.getFloatValue(this.spin_1.getValue()));
            }
            else
            {
                System.out.println("Command for this type is not implemented !!!");
            }

            this.pump_objects_ext[0] = po;
        }

        this.dispose();

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
        return "PumpTool_Data_Additional_2";
    }

    /**
     * focusGained
     */
    public void focusGained(FocusEvent arg0)
    {
    }

    boolean in_action = false;

    /**
     * focusLost
     */
    public void focusLost(FocusEvent ev)
    {
        focusProcess(ev.getSource());
    }

    private void focusProcess(Object src)
    {

        if (in_action)
            return;

        in_action = true;

        if (src.equals(this.num_1))
        {
            // System.out.println("text1: " + this.ftf_bg1.getText());
            if (this.num_1.getText().trim().length() == 0)
            {
                in_action = false;
                return;
            }

            // System.out.println("focus lost: bg2");
            int val = m_da.getJFormatedTextValueInt(this.num_1);

            // System.out.println("Bg Conv: ")

            float v_2 = m_da.getBGConverter().getValueByType(DataAccessPlugInBase.BG_MGDL,
                DataAccessPlugInBase.BG_MMOL, val);
            // float v_2 = m_da.getBGValueDifferent(DataAccessPump.BG_MGDL,
            // val);
            this.num_2.setValue(new Float(v_2));
        }
        else if (src.equals(this.num_2))
        {
            // System.out.println("text2: " + this.ftf_bg2.getText());

            if (this.num_2.getText().trim().length() == 0)
            {
                in_action = false;
                return;
            }

            // System.out.println("focus lost: bg2");
            float val = m_da.getJFormatedTextValueFloat(this.num_2);
            int v_2 = (int) m_da.getBGConverter().getValueByType(DataAccessPlugInBase.BG_MMOL,
                DataAccessPlugInBase.BG_MGDL, val);
            // int v_2 = (int) m_da.getBGValueDifferent(DataAccessPump.BG_MMOL,
            // val);
            this.num_1.setValue(new Integer(v_2));
        }
        else
        {
            System.out.println("focus lost: unknown");
        }

        in_action = false;

    }

    /**
     * keyTyped
     */
    public void keyTyped(KeyEvent e)
    {
    }

    /**
     * keyPressed
     */
    public void keyPressed(KeyEvent e)
    {
    }

    /**
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e)
    {

        if (e.getSource().equals(this.num_1) || e.getSource().equals(this.num_2))
        {
            focusProcess(e.getSource());
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            cmdOk();
        }
    }

    @SuppressWarnings("unused")
    private TransferDialog createInstance()
    {

        if (m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB)
            return null;

        try
        {
            String cl_name = null;

            if (m_type == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC)
            {
                cl_name = "ggc.gui.dialogs.DailyRowMealsDialog";
            }
            else
            {
                cl_name = "";
            }

            Class<?> c = Class.forName(cl_name);

            Constructor<?>[] constructors = c.getConstructors();

            int found = -1;

            for (int i = 0; i < constructors.length; i++)
            {
                if (constructors[i].toString()
                        .equals("public ggc.gui.dialogs.DailyRowMealsDialog(javax.swing.JDialog)"))
                {
                    System.out.println("Found: " + i + "] " + constructors[i]);
                    found = i;

                    break;
                }
                System.out.println("[num=" + i + "] " + constructors[i]);
            }

            if (found > -1)
            {
                TransferDialog td = (TransferDialog) constructors[found].newInstance(this);
                return td;
            }
            else
                return null;

        }
        catch (Exception ex)
        {
            System.out.println("Ex:" + ex);
            ex.printStackTrace();
            return null;
        }

    }

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

        if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_CH)
            return;

        if (in_action)
            return;

        in_action = true;

        // m_da.getFloatValue(this.spinner_arr[5].getValue()),

        // System.out.println("change event - in action");

        if (src.equals(this.spin_1))
        {
            // System.out.println("spinner 4: " +
            // this.spinner_arr[4].getValue());

            // .ftf_bg1.getText().trim().length()
            if (this.m_da.getFloatValue(this.spin_1.getValue()) == 0.0f)
            {
                // this.ftf_bg2.setValue(new Float(0.0f));
                this.spin_2.setValue(0.0f);
                in_action = false;
                return;
            }

            // System.out.println("focus lost: bg1 (spinner[4]). mg/dL: " +
            // this.spinner_arr[4].getValue());
            float val = this.m_da.getFloatValue(this.spin_1.getValue());
            // m_da.getJFormatedTextValueInt(ftf_bg1);
            // float v_2 = m_da.getBGValueDifferent(DataAccess.BG_MGDL, val);
            float v_2 = m_da.getBGConverter().getValueByType(Converter_mgdL_mmolL.UNIT_mg_dL,
                Converter_mgdL_mmolL.UNIT_mmol_L, val);
            // this.ftf_bg2.setValue(new Float(v_2));

            // System.out.println("focus lost: bg1 (spinner[4]). mmol/L: " +
            // v_2);

            this.spin_2.setValue(v_2);
        }
        else if (src.equals(this.spin_2)) // if (src.equals(this.ftf_bg2))
        {
            // System.out.println("text2: " + this.ftf_bg2.getText());

            if (this.m_da.getFloatValue(this.spin_2.getValue()) == 0.0f) // (this.ftf_bg2.getText().trim().length()
                                                                         // ==
                                                                         // 0)
            {
                this.spin_1.setValue(0);
                // this.ftf_bg1.setValue(new Integer(0));
                in_action = false;
                return;
            }

            // System.out.println("focus lost: bg2 (spinner[5])");

            // System.out.println("focus lost: bg2");
            float val = this.m_da.getFloatValue(this.spin_2.getValue());
            // m_da.getJFormatedTextValueFloat(ftf_bg2);
            // int v_2 = (int) m_da.getBGValueDifferent(DataAccess.BG_MMOL,
            // val);
            int v_2 = (int) m_da.getBGConverter().getValueByType(Converter_mgdL_mmolL.UNIT_mmol_L,
                Converter_mgdL_mmolL.UNIT_mg_dL, val);
            // this.ftf_bg1.setValue(new Integer(v_2));
            this.spin_1.setValue(v_2);
        }
        else
        {
            System.out.println("focus lost: unknown");
        }

        in_action = false;

    }

}
