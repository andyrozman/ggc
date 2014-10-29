package ggc.pump.gui.manual;

import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.util.DataAccess;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.gui.profile.ProfileSelectorPump;
import ggc.pump.util.DataAccessPump;
import ggc.shared.bolushelper.BolusHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.components.TimeComponent;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: Pump Tool (support for Pump devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename:     PumpDataTypeComponent
 * Description:  Selection for different base selections and settings
 *
 * Author: Andy {andy@atech-software.com}
 */

public class PumpDataTypeComponent extends JPanel implements ActionListener
{

    private static final long serialVersionUID = -4449947661003378689L;

    TemporaryBasalRateComponent tbr_cmp = null;
    SquareBolusComponent bolus_sq = null;
    JLabel label_1, label_2, label_3, label_4;
    JTextField text_1, text_2;
    JComboBox combo_1, combo_2;
    JDecimalTextField num_tf_1_d2, num_tf_2_d2;
    JRadioButton rb_1, rb_2, rb_3;
    ButtonGroup bg;
    ProfileComponent profile_comp;
    JButton button_1;
    TimeComponent tc_1;

    PumpBaseType type = PumpBaseType.None;
    int height = 0;
    int width = 400;

    // PumpReport m_p_report = new PumpReport();
    // PumpEvents m_p_event = new PumpEvents();
    // PumpAlarms m_p_alarm = new PumpAlarms();
    // PumpErrors m_p_error = new PumpErrors();
    // PumpBolusType m_p_bolus = new PumpBolusType();
    // PumpBasalSubType m_p_basal = new PumpBasalSubType();

    PumpDataRowDialog m_parent = null;

    private DataAccessPump m_da = DataAccessPump.getInstance();
    private I18nControlAbstract ic = m_da.getI18nControlInstance();

    private Object[] type_items = { ic.getMessage("SELECT_ITEM"), ic.getMessage("BASAL_DOSE"),
                                   ic.getMessage("BOLUS_DOSE"), ic.getMessage("EVENT"), ic.getMessage("ALARM"),
                                   ic.getMessage("ERROR"), ic.getMessage("REPORT"),
                                   ic.getMessage("PEN_INJECTION_BASAL"), ic.getMessage("PEN_INJECTION_BOLUS"),
                                   ic.getMessage("ADDITIONAL_DATA") };

    /**
     * Constructor
     *
     * @param parent
     * @param startx
     */
    public PumpDataTypeComponent(PumpDataRowDialog parent, int startx)
    {
        super();
        this.m_parent = parent;
        this.setLayout(null);
        init();
        this.setBounds(30, startx, width, height);
    }

    /**
     * Init
     */
    public void init()
    {
        tbr_cmp = new TemporaryBasalRateComponent();
        this.add(tbr_cmp);
        label_1 = new JLabel();
        label_1.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD));
        this.add(label_1);
        label_2 = new JLabel();
        label_2.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD));
        this.add(label_2);
        label_3 = new JLabel();
        label_3.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD));
        this.add(label_3);
        label_4 = new JLabel();
        label_4.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD));
        this.add(label_4);
        text_1 = new JTextField();
        this.add(text_1);
        text_2 = new JTextField();
        this.add(text_2);
        combo_1 = new JComboBox();
        this.add(combo_1);
        combo_2 = new JComboBox();
        this.add(combo_2);

        num_tf_1_d2 = new JDecimalTextField(new Float(0.0f), 2);
        this.add(num_tf_1_d2);
        num_tf_2_d2 = new JDecimalTextField(new Float(0.0f), 2);
        this.add(num_tf_2_d2);

        combo_1.addActionListener(this);
        combo_2.addActionListener(this);

        // JRadioButton rb_1, rb_2;

        this.rb_1 = new JRadioButton();
        this.add(rb_1);
        this.rb_2 = new JRadioButton();
        this.add(rb_2);
        this.rb_3 = new JRadioButton();
        this.add(rb_3);

        this.bg = new ButtonGroup();

        profile_comp = new ProfileComponent();
        this.add(profile_comp);

        this.tc_1 = new TimeComponent();
        this.add(tc_1);

        this.button_1 = ATSwingUtils.getButton("", 0, 0, 0, 0, this, ATSwingUtils.FONT_NORMAL, "magic-wand.png",
            "bolus_helper", this, m_da);

        this.bolus_sq = new SquareBolusComponent();
        this.add(this.bolus_sq);

    }

    /**
     * Set Type
     *
     * @param type
     */
    public void setType(PumpBaseType type)
    {
        if (this.type == type)
            return;

        this.type = type;

        switch (this.type)
        {

            case Event:
            case Alarm:
            case Error:
                {
                    this.setComboAndText();
                }
                break;

            case Basal:
                {
                    this.setBasal();
                }
                break;

            case Bolus:
                {
                    this.setBolus();
                }
                break;

            case Report:
                {
                    this.setReport();
                }
                break;

            case PenInjectionBasal:
            case PenInjectionBolus:
                {
                    this.setNumericTextAndText();
                }
                break;

            case None:
            case AdditionalData:
            default:
                {
                    this.setEmpty();
                }
                break;
        }

    }

    /**
     * Get Items
     *
     * @return
     */
    public Object[] getItems()
    {
        return this.type_items;
    }

    private void hideAll()
    {
        tbr_cmp.setVisible(false);
        label_1.setVisible(false);
        // label_1.setHorizontalAlignment(JLabel.LEFT);
        label_2.setVisible(false);
        label_3.setVisible(false);
        label_4.setVisible(false);
        text_1.setVisible(false);
        text_2.setVisible(false);
        combo_1.setVisible(false);
        combo_2.setVisible(false);
        num_tf_1_d2.setVisible(false);
        num_tf_2_d2.setVisible(false);
        combo_1.setActionCommand("");
        combo_2.setActionCommand("");
        rb_1.setVisible(false);
        rb_2.setVisible(false);
        rb_3.setVisible(false);
        profile_comp.setVisible(false);
        this.button_1.setVisible(false);
        this.tc_1.setVisible(false);
        this.bolus_sq.setVisible(false);

        this.repaint();
    }

    private void setEmpty()
    {
        this.hideAll();
        setHeight(0);
    }

    /*
     * private void setUnsupported()
     * {
     * this.hideAll();
     * label_1.setBounds(0, 20, 370, 25);
     * label_1.setText("Component doesn't support this type: " +
     * this.type_items[type]);
     * label_1.setVisible(true);
     * this.setHeight(40);
     * }
     */

    // type: event, alarm, error
    private void setComboAndText()
    {
        this.hideAll();

        this.label_1.setBounds(0, 22, 150, 25);
        this.label_1.setVisible(true);

        this.combo_1.setBounds(150, 20, 180, 25);
        this.combo_1.setVisible(true);

        this.label_2.setBounds(0, 57, 150, 25);
        this.label_2.setVisible(true);

        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);

        this.label_2.setText(ic.getMessage("COMMENT") + ":");

        this.setHeight(85);

        if (this.type == PumpBaseType.Event)
        {
            this.label_1.setText(ic.getMessage("EVENT_TYPE") + ":");
            addAllItems(this.combo_1, this.m_da.getPumpEventTypes().getDescriptions());
        }
        else if (this.type == PumpBaseType.Alarm)
        {
            this.label_1.setText(ic.getMessage("ALARM_TYPE") + ":");
            addAllItems(this.combo_1, PumpAlarms.getDescriptions());
        }
        else if (this.type == PumpBaseType.Error)
        {
            this.label_1.setText(ic.getMessage("ERROR_TYPE") + ":");
            addAllItems(this.combo_1, this.m_da.getPumpErrorTypes().getDescriptions());
        }
        else
        {
            this.combo_1.removeAllItems();
        }

    }

    // type: pen bolus, pen basal
    private void setNumericTextAndText()
    {
        this.hideAll();

        this.label_1.setBounds(0, 22, 150, 25);
        this.label_1.setVisible(true);

        this.num_tf_1_d2.setBounds(150, 20, 180, 25);
        this.num_tf_1_d2.setVisible(true);
        this.num_tf_1_d2.setValue(new Float(0.0f));

        this.label_2.setBounds(0, 57, 150, 25);
        this.label_2.setVisible(true);
        this.label_2.setText(ic.getMessage("COMMENT") + ":");

        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);

        this.setHeight(85);

        if (this.type == PumpBaseType.PenInjectionBasal)
        {
            this.label_1.setText(ic.getMessage("BASAL_INSULIN") + ":");
        }
        else if (this.type == PumpBaseType.PenInjectionBolus)
        {
            this.label_1.setText(ic.getMessage("BOLUS_INSULIN") + ":");
        }

    }

    // type: report
    private void setReport()
    {
        this.hideAll();

        this.label_1.setBounds(0, 22, 150, 25);
        this.label_1.setVisible(true);
        this.label_1.setText(ic.getMessage("REPORT_TYPE") + ":");

        this.combo_1.setBounds(150, 20, 180, 25);
        this.combo_1.setVisible(true);
        addAllItems(this.combo_1, m_da.getPumpReportTypes().getDescriptions());

        this.label_2.setText(ic.getMessage("REPORT_TEXT") + ":");
        this.label_2.setBounds(0, 57, 150, 25);
        this.label_2.setVisible(true);

        this.text_2.setBounds(150, 55, 180, 25);
        this.text_2.setVisible(true);

        this.label_3.setBounds(0, 92, 150, 25);
        this.label_3.setVisible(true);
        this.label_3.setText(ic.getMessage("COMMENT") + ":");

        this.text_1.setBounds(150, 90, 180, 25);
        this.text_1.setVisible(true);

        this.setHeight(115);

    }

    int sub_type = 0;

    // types: basal
    private void setBasal()
    {
        this.hideAll();
        this.sub_type = 0;

        this.label_1.setBounds(0, 17, 150, 25);
        this.label_1.setVisible(true);
        this.label_1.setText(ic.getMessage("BASAL_TYPE") + ":");

        this.combo_1.setBounds(110, 15, 220, 25);
        this.combo_1.setVisible(true);
        this.combo_1.setActionCommand("basal");
        addAllItems(this.combo_1, this.m_da.getBasalSubTypes().getDescriptions());

        this.label_2.setBounds(0, 57, 150, 25);
        this.label_2.setVisible(true);
        this.label_2.setText(ic.getMessage("COMMENT") + ":");

        this.text_1.setBounds(110, 55, 220, 25);
        this.text_1.setVisible(true);

        this.setHeight(85);

    }

    /**
     * Set Basal Sub Type
     *
     * @param stype
     */
    public void setBasalSubType(int stype)
    {
        // 20 55
        if (this.sub_type == stype)
            return;
        else
        {
            this.sub_type = stype;
        }

        this.combo_1.setSelectedIndex(stype);

        this.num_tf_1_d2.setVisible(false);
        this.num_tf_2_d2.setVisible(false);
        this.label_3.setVisible(false);
        this.label_4.setVisible(false);
        this.tbr_cmp.setVisible(false);

        this.rb_1.setVisible(false);
        this.rb_2.setVisible(false);
        this.rb_3.setVisible(false);

        this.bg.remove(rb_1);
        this.bg.remove(rb_2);
        this.bg.remove(rb_3);

        profile_comp.setVisible(false);

        // comment
        this.label_2.setVisible(true);
        this.text_1.setVisible(true);

        switch (this.sub_type)
        {
            case PumpBasalSubType.PUMP_BASAL_VALUE:
                {
                    this.label_2.setBounds(0, 92, 150, 25);
                    this.text_1.setBounds(150, 90, 180, 25);

                    this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                    this.num_tf_1_d2.setVisible(true);
                    this.label_3.setBounds(0, 55, 150, 25);
                    this.label_3.setText(ic.getMessage("AMOUNT") + ":");
                    this.label_3.setVisible(true);

                    this.setHeight(115);

                }
                break;

            case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE:
                {
                    this.label_2.setBounds(0, 92, 150, 25);
                    this.text_1.setBounds(150, 90, 180, 25);

                    this.tbr_cmp.setBounds(0, 55, 180, 25);
                    this.tbr_cmp.setVisible(true);

                    // this.num_tf_1_d2.setVisible(true);
                    // this.label_3.setBounds(0, 55, 150, 25);
                    // this.label_3.setText(ic.getMessage("AMOUNT") + ":");
                    // this.label_3.setVisible(true);

                    this.setHeight(115);

                }
                break;

            case PumpBasalSubType.PUMP_BASAL_PROFILE:
                {
                    this.label_2.setBounds(0, 90, 150, 25);
                    this.text_1.setBounds(150, 90, 180, 25);

                    this.profile_comp.setBounds(0, 55, 180, 25);
                    this.profile_comp.setVisible(true);

                    this.setHeight(115);

                }
                break;

            case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_PROFILE:
                {
                    this.label_2.setBounds(0, 127, 150, 25);
                    this.text_1.setBounds(150, 125, 180, 25);

                    this.profile_comp.setBounds(0, 55, 180, 25);
                    this.profile_comp.setVisible(true);

                    this.tbr_cmp.setBounds(0, 90, 180, 25);
                    this.tbr_cmp.setVisible(true);

                    this.setHeight(150);

                }
                break;

            case PumpBasalSubType.PUMP_BASAL_PUMP_STATUS:
                {
                    this.label_2.setBounds(0, 142, 150, 25);
                    this.text_1.setBounds(150, 140, 180, 25);

                    this.rb_1.setText("  " + ic.getMessage("ON"));
                    this.rb_1.setBounds(150, 55, 200, 25);
                    this.rb_1.setVisible(true);
                    this.rb_1.setSelected(true);
                    this.rb_2.setText("  " + ic.getMessage("OFF"));
                    this.rb_2.setBounds(150, 80, 200, 25);
                    this.rb_2.setVisible(true);
                    this.rb_3.setText("  " + ic.getMessage("SUSPENDED"));
                    this.rb_3.setBounds(150, 105, 200, 25);
                    this.rb_3.setVisible(true);

                    this.bg.add(rb_1);
                    this.bg.add(rb_2);
                    this.bg.add(rb_3);

                    // this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                    // this.num_tf_1_d2.setVisible(true);

                    this.label_3.setBounds(0, 57, 150, 25);
                    this.label_3.setText(ic.getMessage("PUMP_STATUS") + ":");
                    this.label_3.setVisible(true);

                    this.setHeight(165);

                }
                break;

            default:
                {
                    this.label_2.setVisible(false);
                    this.text_1.setVisible(false);

                    this.setHeight(55);

                }
                break;
        }

        this.m_parent.realignComponents();

    }

    // types: bolus
    private void setBolus()
    {
        this.hideAll();
        this.sub_type = 0;

        this.label_1.setBounds(0, 20, 150, 25);
        this.label_1.setVisible(true);
        this.label_1.setText(ic.getMessage("BOLUS_TYPE") + ":");

        // System.out.println("DHHHD" +
        // DataAccess.this.m_da.getBolusSubTypes().getStaticDescriptionArray());

        this.combo_1.setBounds(150, 20, 180, 25);
        this.combo_1.setVisible(true);
        this.combo_1.setActionCommand("bolus");
        addAllItems(this.combo_1, this.m_da.getBolusSubTypes().getStaticDescriptionArray());
        // this.combo_1.setSelectedIndex(1);

        this.button_1.setBounds(120, 20, 25, 25);
        this.button_1.setVisible(true);

        this.label_2.setBounds(0, 55, 150, 25);
        this.label_2.setVisible(true);
        this.label_2.setText(ic.getMessage("COMMENT") + ":");

        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);

        this.sub_type = -1;
        this.setBolusSubType(PumpBolusType.PUMP_BOLUS_STANDARD);
        // this.setHeight(85);

    }

    /**
     * Set Bolus Sub Type
     * @param stype
     */
    public void setBolusSubType(int stype)
    {
        // 20 55
        if (this.sub_type == stype)
            return;
        else
        {
            this.sub_type = stype;
        }

        // fix subtype
        // getIndexFromStaticDescriptionArrayWithID(stype)

        this.combo_1.setSelectedIndex(this.m_da.getBolusSubTypes().getIndexFromStaticDescriptionArrayWithID(stype));

        this.num_tf_1_d2.setVisible(false);
        this.num_tf_2_d2.setVisible(false);
        this.label_3.setVisible(false);
        this.label_4.setVisible(false);
        this.tc_1.setVisible(false);
        this.bolus_sq.setVisible(false);

        switch (this.sub_type)
        {
            case PumpBolusType.PUMP_BOLUS_STANDARD:
            case PumpBolusType.PUMP_BOLUS_AUDIO_SCROLL:
            case PumpBolusType.PUMP_BOLUS_DUAL_NORMAL:
                {
                    this.label_2.setBounds(0, 90, 150, 25);
                    this.text_1.setBounds(150, 90, 180, 25);

                    // this.text_1.setVisible(true);
                    // this.label_2.setVisible(true);

                    this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                    this.num_tf_1_d2.setVisible(true);
                    this.label_3.setBounds(0, 55, 150, 25);
                    this.label_3.setText(ic.getMessage("AMOUNT") + ":");
                    this.label_3.setVisible(true);

                    this.setHeight(115);
                }
                break;

            case PumpBolusType.PUMP_BOLUS_SQUARE:
            case PumpBolusType.PUMP_BOLUS_DUAL_SQUARE:
                {
                    // TODO
                    this.label_2.setBounds(0, 90, 150, 25);
                    this.text_1.setBounds(110, 90, 220, 25);

                    // this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                    // this.num_tf_1_d2.setVisible(true);
                    // this.label_3.setBounds(0, 55, 150, 25);
                    // this.label_3.setText(ic.getMessage("AMOUNT") + ":");
                    // this.label_3.setVisible(true);

                    this.bolus_sq.setBounds(0, 55, 200, 25);
                    this.bolus_sq.setType(SquareBolusComponent.SQUARE_SINGLE);
                    this.bolus_sq.setVisible(true);

                    this.setHeight(115);
                }
                break;

            case PumpBolusType.PUMP_BOLUS_MULTIWAVE:
                {
                    this.label_2.setBounds(0, 125, 150, 25);
                    this.text_1.setBounds(150, 125, 180, 25);

                    this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                    this.num_tf_1_d2.setVisible(true);
                    this.label_3.setBounds(0, 55, 150, 25);
                    this.label_3.setText(ic.getMessage("IMMEDIATE_AMOUNT") + ":");
                    this.label_3.setVisible(true);

                    // 90
                    /*
                     * this.label_4.setText(ic.getMessage("AMOUNT_MW_2") + ":");
                     * label_4.setBounds(0, 90, 150, 25);
                     * this.label_4.setVisible(true);
                     * this.num_tf_2_d2.setBounds(150, 90, 180, 25);
                     * this.num_tf_2_d2.setVisible(true);
                     */
                    // this.tc_1.setVisible(false);

                    this.bolus_sq.setBounds(0, 90, 200, 25);
                    this.bolus_sq.setType(SquareBolusComponent.SQUARE_DUAL);
                    this.bolus_sq.setVisible(true);

                    this.setHeight(150);

                }
                break;

            case PumpBolusType.PUMP_BOLUS_NONE:
                {
                    /*
                     * this.num_tf_1_d2.setVisible(false);
                     * this.num_tf_2_d2.setVisible(false);
                     * this.label_3.setVisible(false);
                     * this.label_4.setVisible(false);
                     */

                    this.label_2.setBounds(0, 55, 150, 25);
                    this.text_1.setBounds(150, 55, 180, 25);
                    this.setHeight(85);
                }
                break;

        }

        this.m_parent.realignComponents();

    }

    /**
     * Are Required Elements Set. For checking if elements are set.
     *
     * @return
     */
    public boolean areRequiredElementsSet()
    {
        // System.out.println("!!!!  Are Elements Set - Not Implemented   !!!!");

        switch (this.type)
        {

            case Basal:
                {
                    switch (this.sub_type)
                    {
                        case PumpBasalSubType.PUMP_BASAL_VALUE:
                            {
                                return m_da.getFloatValue(this.num_tf_1_d2.getCurrentValue()) >= 0;
                            }

                        case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE:
                            {
                                return this.tbr_cmp.isValueSet();
                            }

                        case PumpBasalSubType.PUMP_BASAL_PROFILE:
                            {
                                return this.profile_comp.isValueSet();
                            }

                        case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_PROFILE:
                            {
                                return this.tbr_cmp.isValueSet() && this.profile_comp.isValueSet();
                            }

                        case PumpBasalSubType.PUMP_BASAL_PUMP_STATUS:
                            {
                                return this.rb_1.isSelected() || this.rb_2.isSelected() || this.rb_3.isSelected();
                            }

                        default:
                            return false;
                    }
                }

            case Bolus:
                {
                    switch (this.sub_type)
                    {
                        case PumpBolusType.PUMP_BOLUS_STANDARD:
                        case PumpBolusType.PUMP_BOLUS_AUDIO_SCROLL:
                            {
                                return this.num_tf_1_d2.getText().length() > 0;
                                // return
                                // (m_da.getFloatValue(this.num_tf_1_d2.getCurrentValue())
                                // > 0);
                            }
                        case PumpBolusType.PUMP_BOLUS_SQUARE:
                            {
                                return this.bolus_sq.isValueSet();
                            }

                        case PumpBolusType.PUMP_BOLUS_MULTIWAVE:
                            {
                                return m_da.getFloatValue(this.num_tf_1_d2.getCurrentValue()) > 0
                                        && this.bolus_sq.isValueSet();
                            }

                        default:
                            {
                                return false;

                            }

                    }
                }

            case Event:
            case Alarm:
            case Error:
                {
                    return this.combo_1.getSelectedIndex() > 0;
                }

            case Report:
                {
                    return this.combo_1.getSelectedIndex() > 0 && this.text_2.getText().trim().length() != 0;
                }

            case PenInjectionBasal:
            case PenInjectionBolus:
                {
                    return m_da.getFloatValue(this.num_tf_1_d2.getCurrentValue()) > 0;
                }

            case AdditionalData:
            default:
                return true;

        }

    }

    /**
     * Load Data
     *
     * @param data
     */
    public void loadData(PumpValuesEntry data)
    {
        // System.out.println("Load data not implemented yet !");
        setType(data.getBaseType());
        this.text_1.setText(data.getComment());

        // this.combo_1.setSelectedIndex(data.getBaseType());
        switch (this.type)
        {
            case Basal:
                {
                    this.setBasalSubType(data.getSubType());

                    switch (this.sub_type)
                    {
                        case PumpBasalSubType.PUMP_BASAL_VALUE:
                            {
                                this.num_tf_1_d2.setValue(m_da.getFloatValueFromString(data.getValue()));
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE:
                            {
                                this.tbr_cmp.setValue(data.getValue());
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_PROFILE:
                            {
                                this.profile_comp.setValue(data.getValue());
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_PROFILE:
                            {
                                String s[] = this.getParsedValues(data.getValue());
                                this.profile_comp.setValue(s[0]);
                                this.tbr_cmp.setValue(s[1]);
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_PUMP_STATUS:
                            {
                                int i = m_da.getIntValueFromString(data.getValue(), 0);

                                if (i == 1)
                                {
                                    this.rb_1.setSelected(true);
                                }
                                else if (i == 2)
                                {
                                    this.rb_2.setSelected(true);
                                }
                                else if (i == 3)
                                {
                                    this.rb_3.setSelected(true);
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                break;

            case Bolus:
                {
                    this.setBolusSubType(data.getSubType());

                    switch (this.sub_type)
                    {
                        case PumpBolusType.PUMP_BOLUS_STANDARD:
                        case PumpBolusType.PUMP_BOLUS_AUDIO_SCROLL:
                            {
                                this.num_tf_1_d2.setValue(m_da.getFloatValueFromString(data.getValue()));
                            }
                            break;

                        case PumpBolusType.PUMP_BOLUS_SQUARE:
                            {
                                this.bolus_sq.setValue(data.getValue());
                            }
                            break;

                        case PumpBolusType.PUMP_BOLUS_MULTIWAVE:
                            {

                                String s[] = this.getParsedValues(data.getValue());
                                // String s[] = data.getValue().split(regex)
                                this.num_tf_1_d2.setValue(m_da.getFloatValueFromString(s[0]));
                                // this.num_tf_2_d2.setValue(m_da.getFloatValueFromString(s[1]));
                                this.bolus_sq.setValue(data.getValue());

                            }
                            break;

                        default:
                            {

                            }
                            break;

                    }
                    // TODO
                }
                break;

            case Event:
                {
                    this.combo_1.setSelectedItem(data.getSubTypeString());
                }
                break;
            case Alarm:
            case Error:
                {
                    this.combo_1.setSelectedIndex(data.getSubType());
                }
                break;

            case Report:
                {
                    this.combo_1.setSelectedIndex(data.getSubType());
                    this.text_2.setText(data.getValue());
                }
                break;

            case PenInjectionBasal:
            case PenInjectionBolus:
                {
                    this.num_tf_1_d2.setValue(m_da.getFloatValueFromString(data.getValue()));
                }
                break;

            case AdditionalData:
                break;

            default:
                System.out.println("Load not implemented for this type: " + data.getBaseType());
                break;

        }

    }

    /**
     * Save Data (we put in PumpValuesEntry, which is then set to right values)
     *
     * @param pve
     */
    public void saveData(PumpValuesEntry pve)
    {
        pve.setComment(this.text_1.getText());

        switch (this.type)
        {
            case Basal:
                {
                    pve.setSubType(sub_type);

                    switch (this.sub_type)
                    {
                        case PumpBasalSubType.PUMP_BASAL_VALUE:
                            {
                                pve.setValue("" + this.num_tf_1_d2.getCurrentValue());
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE:
                            {
                                pve.setValue(this.tbr_cmp.getValue());
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_PROFILE:
                            {
                                pve.setValue(this.profile_comp.getValue());
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_PROFILE:
                            {
                                pve.setValue("PROFILE_ID=" + this.profile_comp.getValue() + ";TBR="
                                        + this.tbr_cmp.getValue());
                            }
                            break;

                        case PumpBasalSubType.PUMP_BASAL_PUMP_STATUS:
                            {
                                if (this.rb_1.isSelected())
                                {
                                    pve.setValue("1");
                                }
                                else if (this.rb_2.isSelected())
                                {
                                    pve.setValue("2");
                                }
                                else if (this.rb_3.isSelected())
                                {
                                    pve.setValue("3");
                                }
                                else
                                {
                                    pve.setValue("0");
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                break;

            case Bolus:
                {
                    pve.setSubType(sub_type);

                    switch (this.sub_type)
                    {
                        case PumpBolusType.PUMP_BOLUS_STANDARD:
                        case PumpBolusType.PUMP_BOLUS_AUDIO_SCROLL:
                            {
                                pve.setValue("" + this.num_tf_1_d2.getCurrentValue());

                            }
                            break;

                        // case PumpBolusType.PUMP_BOLUS_DUAL_SQUARE:
                        case PumpBolusType.PUMP_BOLUS_SQUARE:
                            {
                                pve.setValue(this.bolus_sq.getValue());
                                // pve.setValue("AMOUNT_SQUARE=" + amount +
                                // ";DURATION=" + e);

                            }
                            break;

                        case PumpBolusType.PUMP_BOLUS_MULTIWAVE:
                            {
                                /*
                                 * pve.setValue(String.format(
                                 * "AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s",
                                 * str[0],
                                 * str[2],
                                 * str[3]));
                                 */

                                // pve.setValue("AMOUNT_1=" +
                                // this.num_tf_1_d2.getCurrentValue() +
                                // ";AMOUNT_2=" +
                                // this.num_tf_2_d2.getCurrentValue());

                                pve.setValue("IMMEDIATE_AMOUNT=" + this.num_tf_1_d2.getCurrentValue() + ";"
                                        + this.bolus_sq.getValue());

                            }
                            break;

                        default:
                            {

                            }
                            break;

                    }
                    // TODO
                }
                break;

            case Event:
                {
                    pve.setSubType(m_da.getPumpEventTypes().getTypeFromDescription(
                        (String) this.combo_1.getSelectedItem()));
                }
                break;

            case Alarm:
            case Error:
                {
                    pve.setSubType(this.combo_1.getSelectedIndex());
                }
                break;

            case Report:
                {
                    pve.setSubType(this.combo_1.getSelectedIndex());
                    pve.setValue(this.text_2.getText());
                }
                break;

            case PenInjectionBasal:
            case PenInjectionBolus:
                {
                    pve.setSubType(0);
                    pve.setValue("" + this.num_tf_1_d2.getCurrentValue());
                }
                break;

            case AdditionalData:
            default:
                break;

        }

        // System.out.println("Save data not implemented yet !");

    }

    private String[] getParsedValues(String val)
    {
        ArrayList<String> lst = new ArrayList<String>();

        StringTokenizer strtok = new StringTokenizer(val, ";");

        while (strtok.hasMoreTokens())
        {
            String tk = strtok.nextToken();
            lst.add(tk.substring(tk.indexOf("=") + 1));
        }

        String ia[] = new String[lst.size()];
        return lst.toArray(ia);
    }

    private void addAllItems(JComboBox cb, String[] array)
    {
        cb.removeAllItems();

        for (String element : array)
        {
            cb.addItem(element);
        }
    }

    private void addAllItems(JComboBox cb, Hashtable<Integer, String> ht)
    {
        cb.removeAllItems();

        for (Enumeration<Integer> en = ht.keys(); en.hasMoreElements();)
        {
            Integer i = en.nextElement();
            cb.addItem(ht.get(i));
        }
    }

    private void addAllItems(JComboBox cb, Map<Integer, ? extends CodeEnumWithTranslation> map)
    {
        cb.removeAllItems();

        for (Entry<Integer, ? extends CodeEnumWithTranslation> en : map.entrySet())
        {
            cb.addItem(en.getValue().getTranslation());
        }
    }

    /**
     * Set Height
     *
     * @param height
     */
    public void setHeight(int height)
    {
        this.height = height;
        this.setSize(width, height);
    }

    /**
     * Get Height
     */
    @Override
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Get Base Type
     *
     * @return
     */
    public PumpBaseType getBaseType()
    {
        return this.type;
    }

    /**
     * Action Performed
     */
    public void actionPerformed(ActionEvent ev)
    {
        String cmd = ev.getActionCommand();

        if (cmd.equals("bolus"))
        {
            // System.out.println("Bolus event: " +
            // this.combo_1.getSelectedIndex());

            String s = (String) this.combo_1.getSelectedItem();
            setBolusSubType(this.m_da.getBolusSubTypes().getTypeFromDescription(s));

            // setBolusSubType(this.combo_1.getSelectedIndex());
        }
        else if (cmd.equals("basal"))
        {
            // System.out.println("Basal event: " +
            // this.combo_1.getSelectedIndex());
            setBasalSubType(this.combo_1.getSelectedIndex());
        }
        else if (cmd.equals("bolus_helper"))
        {
            System.out.println("Bolus Helper"); // Basal event: " +
            // this.combo_1.getSelectedIndex());
            // setBasalSubType(this.combo_1.getSelectedIndex());
            // m_parent.dtc

            float _bg = 0.0f;
            float ch = 0.0f;

            if (m_parent.ht_data.containsKey(this.m_da.getAdditionalTypes().getTypeDescription(
                PumpAdditionalDataType.PUMP_ADD_DATA_BG)))
            {
                PumpValuesEntryExt pvex = m_parent.ht_data.get(this.m_da.getAdditionalTypes().getTypeDescription(
                    PumpAdditionalDataType.PUMP_ADD_DATA_BG));
                _bg = m_da.getFloatValueFromString(pvex.getValue());

                if (m_da.getBGMeasurmentType() != DataAccess.BG_MGDL)
                {
                    _bg = m_da.getBGValueDifferent(DataAccess.BG_MGDL, _bg);
                }
            }

            if (m_parent.ht_data.containsKey(this.m_da.getAdditionalTypes().getTypeDescription(
                PumpAdditionalDataType.PUMP_ADD_DATA_CH)))
            {
                PumpValuesEntryExt pvex = m_parent.ht_data.get(this.m_da.getAdditionalTypes().getTypeDescription(
                    PumpAdditionalDataType.PUMP_ADD_DATA_CH));
                ch = m_da.getFloatValueFromString(pvex.getValue());
            }

            BolusHelper bh = new BolusHelper(m_parent, _bg, // m_da.getJFormatedTextValueFloat(ftf_bg2),
                    ch, // m_da.getJFormatedTextValueFloat(this.ftf_ch),
                    m_parent.dtc.getDateTime(), 2, DataAccess.INSULIN_PUMP);

            if (bh.hasResult())
            {
                this.num_tf_1_d2.setValue(bh.getResult());
                // this.ftf_ins1.setValue(bh.getResult());
            }

        }
    }

    private class ProfileComponent extends JPanel implements ActionListener
    {

        private static final long serialVersionUID = 1195430308386555236L;
        JLabel label_1_1, label_2_1;
        JButton b_button_1;
        // PumpValuesEntryProfile profile = null;
        String profile;

        public ProfileComponent()
        {
            super();
            this.setLayout(null);
            this.init();
        }

        private void init()
        {
            label_1_1 = new JLabel(ic.getMessage("PROFILE") + ":");
            label_1_1.setBounds(0, 0, 140, 25);
            label_1_1.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD));
            this.add(label_1_1);

            label_2_1 = new JLabel(ic.getMessage("NOT_SELECTED"));
            label_2_1.setBounds(150, 0, 140, 25);
            this.add(label_2_1);

            b_button_1 = new JButton("...");
            b_button_1.setBounds(300, 0, 25, 25);
            b_button_1.addActionListener(this);
            this.add(b_button_1);

        }

        @Override
        public void setBounds(int x, int y, int width, int height)
        {
            super.setBounds(x, y, 350, 30);
        }

        public void actionPerformed(ActionEvent arg0)
        {
            ProfileSelectorPump psp = new ProfileSelectorPump(m_da, m_parent);
            if (psp.wasAction())
            {
                label_2_1.setText(psp.getSelectedObject().toString());
                this.profile = psp.getSelectedObject().toString();
            }
            /*
             * System.out.println("Profile Selector N/A !!!");
             * JOptionPane.showMessageDialog(this,
             * "Profile functionality will be added at " +
             * "later time (version 0.5), so profile " +
             * "selecttion is currently not possible.",
             * ic.getMessage("PFORILE"),
             * JOptionPane.WARNING_MESSAGE);
             */
        }

        public boolean isSelected()
        {
            return profile != null;
            // return
            // !(label_2_1.getText().equals(ic.getMessage("NOT_SELECTED")));
        }

        public boolean isValueSet()
        {
            return isSelected();
        }

        public String getValue()
        {
            /*
             * if (this.profile==null)
             * return "0";
             * else
             * return "" + this.profile.getId();
             */
            return this.profile;
        }

        public void setValue(String val)
        {

            this.profile = val;
            label_2_1.setText(this.profile);
            /*
             * long id = m_da.getLongValueFromString(val, 0L);
             * if (id == 0)
             * {
             * this.profile = null;
             * }
             * else
             * {
             * this.profile = new PumpValuesEntryProfile();
             * this.profile.setId(id);
             * m_da.getDb().get(this.profile);
             * }
             */
        }
    }

    /**
     *  Square Bolus Component - Component used for setting Square Bolus
     */
    private class SquareBolusComponent extends JPanel
    {

        private static final long serialVersionUID = -1888760063675164725L;

        // String[] vals = { "-", "+" };

        JSpinner spinner = null;
        // JComboBox cb_sign = null;
        JLabel label_1_1, label_2_1;
        TimeComponent cmp_time = null;
        int m_type = 0;

        public static final int SQUARE_SINGLE = 1;
        public static final int SQUARE_DUAL = 2;
        DataAccessPump da = DataAccessPump.getInstance();

        public SquareBolusComponent()
        {
            super();
            ATSwingUtils.initLibrary();
            this.setLayout(null);
            this.init();
        }

        private void init()
        {
            label_1_1 = ATSwingUtils.getLabel(ic.getMessage("SQUARE_AMOUNT") + ":", 0, 0, 120, 25, this,
                ATSwingUtils.FONT_NORMAL_BOLD);
            this.add(label_1_1);

            spinner = new JSpinner();
            spinner.setModel(new SpinnerNumberModel(0.0f, 0.0f, da.getMaxBolusValue(), da.getBolusStep()));
            spinner.setBounds(110, 0, 50, 25);
            spinner.setValue(0.1f);
            this.add(spinner);

            label_2_1 = ATSwingUtils.getLabel(ic.getMessage("DURATION_SHORT") + ":", 175, 0, 120, 25, this,
                ATSwingUtils.FONT_NORMAL_BOLD);
            this.add(label_2_1);

            cmp_time = new TimeComponent();
            cmp_time.setBounds(245, 0, 50, 25);
            this.add(cmp_time);

        }

        // Type: 1 = AMOUNT_SQUARE=%s;DURATION=%s
        // 2 = AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s
        public void setType(int type_in)
        {
            this.m_type = type_in;
        }

        @Override
        public void setBounds(int x, int y, int width, int height)
        {
            super.setBounds(x, y, 350, 30);
        }

        public String getValue()
        {
            return String.format("AMOUNT_SQUARE=%s;DURATION=%s",
                "" + m_da.getFormatedBolusValue(m_da.getFloatValue(this.spinner.getValue())),
                this.cmp_time.getTimeString());

            // System.out.println("getValue not implemented");
            // return null;

        }

        public void setValue(String val)
        {
            String s[] = getParsedValues(val);

            if (m_type == SquareBolusComponent.SQUARE_SINGLE)
            {
                this.spinner.setValue(m_da.getFloatValueFromString(s[0], 0.0f));
                this.setTime(s[1]);
            }
            else
            {
                this.spinner.setValue(m_da.getFloatValueFromString(s[1], 0.0f));

                if (s.length > 2)
                {
                    this.setTime(s[2]);
                }
                else
                {
                    this.cmp_time.setTime(0);
                }
            }

            // String s[] = data.getValue().split(regex)
            // this.num_tf_1_d2.setValue(m_da.getFloatValueFromString(s[0]));
            // this.num_tf_2_d2.setValue(m_da.getFloatValueFromString(s[1]));

            // System.out.println("setValue not implemented");
        }

        private void setTime(String val)
        {
            String s1, s2;

            s1 = val.substring(0, val.indexOf(":"));
            s2 = val.substring(val.indexOf(":") + 1);

            int tm = m_da.getIntValueFromString(s1, 0) * 100 + m_da.getIntValueFromString(s2, 0);

            this.cmp_time.setTime(tm);
        }

        public boolean isValueSet()
        {
            return true;
        }

        @Override
        public void setVisible(boolean vis)
        {
            // System.out.println("SquareBolusCompo: setVisible" + vis);
            super.setVisible(vis);

            spinner.setVisible(vis);
            label_1_1.setVisible(vis);
            label_2_1.setVisible(vis);
            cmp_time.setVisible(vis);
        }

    }

    private class TemporaryBasalRateComponent extends JPanel implements ItemListener
    {
        // I18nControlAbstract ic =
        // DataAccessPump.getInstance().getI18nControlInstance();
        ConfigurationManager cm = m_da.getConfigurationManager();

        private static final long serialVersionUID = -1192269467658397557L;
        String[] vals = { "-", "+" };
        String[] units = { ic.getMessage("TBR_TYPE_UNIT"), ic.getMessage("TBR_TYPE_PROC") };

        JSpinner spinner = null;
        JComboBox cb_sign = null;
        JComboBox cb_unit = null;
        JLabel label_1_1, label_2_1;

        TimeComponent cmp_time = null;

        SpinnerNumberModel model_unit;
        SpinnerNumberModel model_proc;

        public TemporaryBasalRateComponent()
        {
            super();
            this.setLayout(null);
            this.init();
        }

        private void init()
        {
            label_1_1 = new JLabel(ic.getMessage("TEMPORARY_BASAL_RATE_SHORT") + ":");
            label_1_1.setBounds(0, 0, 140, 25);
            label_1_1.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD));
            this.add(label_1_1);

            cb_sign = new JComboBox(vals);
            cb_sign.setBounds(110, 0, 53, 25);
            cb_sign.setSelectedIndex(1);
            this.add(cb_sign);

            model_unit = new SpinnerNumberModel(0, cm.getFloatValue("PUMP_UNIT_MIN"),
                    cm.getFloatValue("PUMP_UNIT_MAX"), cm.getFloatValue("PUMP_UNIT_STEP"));

            model_proc = new SpinnerNumberModel(100, cm.getFloatValue("PUMP_PROC_MIN"),
                    cm.getFloatValue("PUMP_PROC_MAX"), cm.getFloatValue("PUMP_PROC_STEP"));

            spinner = new JSpinner();
            // spinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
            spinner.setBounds(168, 0, 48, 25);
            spinner.setValue(100);
            this.add(spinner);

            cb_unit = new JComboBox(units);
            cb_unit.setBounds(220, 0, 110, 25);
            cb_unit.addItemListener(this);
            this.add(cb_unit);

            int tbr_type = cm.getIntValue("PUMP_TBR_TYPE");

            if (tbr_type != 0)
            {
                cb_unit.setEnabled(false);
                cb_unit.setSelectedIndex(tbr_type - 1);
            }

            setSpinnerModel();

            label_2_1 = ATSwingUtils.getLabel(ic.getMessage("DURATION_SHORT") + ":", 175, 0, 120, 25, this,
                ATSwingUtils.FONT_NORMAL_BOLD);
            this.add(label_2_1);

            cmp_time = new TimeComponent();
            cmp_time.setBounds(245, 0, 50, 25);
            this.add(cmp_time);

            /*
             * label_2_1 = new JLabel("%");
             * label_2_1.setBounds(320, 0, 40, 25);
             * this.add(label_2_1);
             */

        }

        private void setSpinnerModel()
        {
            if (this.cb_unit.getSelectedIndex() == 0)
            {
                spinner.setModel(model_unit);
            }
            else
            {
                spinner.setModel(model_proc);
            }
        }

        @Override
        public void setBounds(int x, int y, int width, int height)
        {
            super.setBounds(x, y, 350, 30);
        }

        public String getValue()
        {
            String sign = "", unit = "%", val = "";

            if (this.cb_sign.getSelectedIndex() == 0)
            {
                sign = "-";
            }

            if (this.cb_unit.getSelectedIndex() == 0)
            {
                unit = "U";
                // FIXME
                // val = Rounding.specialRounding(m_da.getDoinput_val,
                // cm.getStringValue("PEN_BASAL_PRECISSION"));
            }
            else
            {
                val = "" + m_da.getIntValue(spinner.getValue());
            }

            return String.format("TBR_VALUE=%s%s;TBR_UNIT=%s;DURATION=%s", sign, val, unit,
                this.cmp_time.getTimeString());

        }

        public void setValue(String val)
        {
            String[] str = getParsedValues(val);
            String s = str[0];

            if (s.startsWith("-"))
            {
                s = s.substring(1);
                this.cb_sign.setSelectedIndex(0);
            }
            else
            {
                this.cb_sign.setSelectedIndex(1);
            }

            if (str[1].equals("%"))
            {
                this.cb_unit.setSelectedIndex(1);
            }
            else
            {
                this.cb_unit.setSelectedIndex(0);
            }

        }

        public boolean isValueSet()
        {
            return true;
        }

        public void itemStateChanged(ItemEvent ie)
        {
            setSpinnerModel();
        }

    }

}
