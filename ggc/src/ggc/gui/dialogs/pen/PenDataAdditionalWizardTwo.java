package ggc.gui.dialogs.pen;

import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// fix this

public class PenDataAdditionalWizardTwo extends JDialog implements ActionListener, HelpCapable, FocusListener, KeyListener
{

    private static final long serialVersionUID = 6600123145384610341L;

    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();
    JLabel label_title = new JLabel();
    Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);
    Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL);

    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null; 
    private Container m_parent = null;

    private int m_type = 0;

    JLabel label_1, label_2;
    JTextField text_1;
    JDecimalTextField num_1, num_2;
    JCheckBox cb_1;

    //PumpAdditionalDataType m_pump_add; // = new PumpAdditionalDataType();

    boolean was_action = false;
    //PumpValuesEntryExt pump_objects_ext[];
    
    //PumpValuesEntryExt data_object;

    // new data
    public PenDataAdditionalWizardTwo(PenDataAdditionalWizardOne wiz_one) //, String type, PumpAdditionalDataType pump_add)
    {
        super(wiz_one, "", true);

//        this.m_pump_add = pump_add;

        // this.old_data = data;
        m_parent = wiz_one;
//        this.m_type = m_pump_add.getTypeFromDescription(type);

        ATSwingUtils.initLibrary();

        init();
    }
/*
    public PenDataAdditionalWizardTwo(JDialog dialog, PumpValuesEntryExt pc)
    {
        super(dialog, "", true);

        //this.m_pump_add = m_da.getAdditionalType();

        // this.old_data = data;
        m_parent = dialog;
        this.m_type = pc.getType();

        ATSwingUtils.initLibrary();

        init();
        
//        data_object = pc;
        
        loadObject();
    }
  */  
    
    private void loadObject()
    {
        /*
        if ((this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_ACTIVITY) || 
            (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT) || 
            (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_URINE))
        {
            this.text_1.setText(this.data_object.getValue());
            //po.setValue(this.text_1.getText());
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_BG)
        {
            this.num_1.setValue(new Float(this.data_object.getValue()));
            this.focusProcess(num_1);
            //po.setValue(this.num_1.getValue().toString());
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_CH)
        {
            this.num_1.setValue(new Float(this.data_object.getValue()));
//            po.setValue(this.num_1.getValue().toString());
        }
        else
            System.out.println("Load for this type is not implemented !!!");
    */    
    }
    
    
    private void init()
    {
        int width = 320;
        int height = 300;

        this.setSize(width, height);

        this.m_da.centerJDialog(this, this.m_parent);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title = ATSwingUtils.getTitleLabel(m_ic.getMessage("ADD_PARAMETER2"), 0, 35, width, 35, panel, ATSwingUtils.FONT_BIG_BOLD);

        setTitle(m_ic.getMessage("ADD_PARAMETER2"));

        // JLabel label = new JLabel(m_ic.getMessage("SELECT_ADDITONAL_DATA"));
        // label.setBounds(30, 100, 250, 25);
        // label.setFont(f_bold);
        // panel.add(label);

        /*        
                JComboBox cb_type = new JComboBox(createItems());
                cb_type.setBounds(30, 135, 240, 25);
                panel.add(cb_type);
          */

        String button_command[] = { "cancel", m_ic.getMessage("CANCEL"), "ok", m_ic.getMessage("OK"),
        // "help", m_ic.getMessage("HELP")
        };

        // button
        String button_icon[] = { "cancel.png", "ok.png" };
        // null, "ok.png", "cancel.png" };

        int button_coord[] = { 30, 230, 120, 1, 160, 230, 130, 1,
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
                ATSwingUtils.addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], 23, panel);
            else
                ATSwingUtils.addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], 25, panel);
        }

        help_button = m_da.createHelpButtonByBounds(170, 195, 120, 25, this);
        panel.add(help_button);

        // m_da.enableHelp(this);

        initType();

    }

    private void initType()
    {
        /*
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
                //areaUnsupported();
            }
            break;

        case PumpAdditionalDataType.PUMP_ADD_DATA_FOOD:
            {
                areaFood();
            }
            break;

        default:
            {
                areaUnsupported();
            }
            break;
        }*/
    }

    // activity, comment, urine
    private void areaText()
    {
        /*
        this.label_1 = ATSwingUtils.getLabel(null, 30, 100, 100, 25, this.main_panel);
        this.text_1 = ATSwingUtils.getTextField("", 30, 130, 250, 25, this.main_panel);

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
*/
    }

    public void areaBG()
    {
        
        ATSwingUtils.getLabel(m_ic.getMessage("BLOOD_GLUCOSE") + ":", 50, 108, 100, 25, main_panel);
        ATSwingUtils.getLabel("mg/dL", 160, 108, 100, 25, main_panel);
        ATSwingUtils.getLabel("mmol/L", 160, 138, 100, 25, main_panel);
        
        this.num_1 = ATSwingUtils.getNumericTextField(2, 0, new Integer(0), 210, 108, 55, 25, this.main_panel);
        this.num_2 = ATSwingUtils.getNumericTextField(2, 1, new Float(0.0f), 210, 138, 55, 25, this.main_panel);

        this.num_1.addFocusListener(this);
        this.num_1.addKeyListener(this);
        this.num_2.addFocusListener(this);
        this.num_2.addKeyListener(this);
    }
    
    
    public void areaCH()
    {
        
        ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") + ":", 50, 108, 100, 25, main_panel);
        
        this.num_1 = ATSwingUtils.getNumericTextField(2, 0, new Integer(0), 210, 108, 55, 25, this.main_panel);
        
    }
    

    public void areaFood()
    {
        
        ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") + ":", 50, 108, 100, 25, main_panel);
        
        this.num_1 = ATSwingUtils.getNumericTextField(2, 0, new Integer(0), 210, 108, 55, 25, this.main_panel);
    
        this.cb_1 =  new JCheckBox();
        this.cb_1.setBounds(50, 150, 100, 25);
        this.cb_1.addActionListener(this);
        this.cb_1.setActionCommand("check");
        this.main_panel.add(this.cb_1);
        
        
    }
    
    
    

    private void areaUnsupported()
    {
        /*
        this.label_1 = new JLabel("Unsuported type : " + this.m_pump_add.getDescriptions()[this.m_type]);
        this.label_1.setBounds(20, 100, 100, 25);
        this.main_panel.add(this.label_1);
*/
    }

    public boolean isMealSet()
    {
        return false;
        /*
        if ((this.m_dailyValuesRow.meals == null) || (this.m_dailyValuesRow.meals.trim().length() == 0))
            return false;
        else
            return true; */
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            System.out.println("wizard_2 [cancel]");
            this.was_action = false;
            this.dispose();
        }
        else if (action.equals("ok"))
        {
            cmdOk();
        }
        else if (action.equals("check"))
        {
            System.out.println("check 1");
        }
        /*        else if (action.equals("edit_food"))
                {
                    
                    DailyValuesMealSelectorDialog dvms = new DailyValuesMealSelectorDialog(
                            m_da, this.m_dailyValuesRow.getMealsIds());

                    if (dvms.wasAction())
                    {
                        this.m_dailyValuesRow.setMealsIds(dvms.getStringForDb());
                        this.ftf_ch.setValue(new Float(dvms.getCHSum()
                                .replace(',', '.')));

                        if (!dvms.getStringForDb().equals(""))
                        {
                            this.cb_food_set.setSelected(true);
                        }
                }
                else if (action.equals("update_ch"))
                {
                    PanelMealSelector pms = new PanelMealSelector(this, null,
                            this.m_dailyValuesRow.getMealsIds());
                    this.ftf_ch.setValue(new Float(pms.getCHSumString().replace(',',
                            '.')));
                }
                else if (action.equals("bolus_helper"))
                {
                    BolusHelper bh = new BolusHelper(this, m_da.getJFormatedTextValueFloat(ftf_bg2), m_da.getJFormatedTextValueFloat(this.ftf_ch), this.dtc.getDateTime());
                    
                    if (bh.hasResult())
                    {
                        this.ftf_ins1.setValue(bh.getResult());
                    }
                } */
        else
            System.out.println("PumpDataAdditionalWizardTwo::unknown command: " + action);

    }

    public boolean wasAction()
    {
        return this.was_action;
    }

    /*
    public PumpValuesEntryExt[] getObjects()
    {
        return pump_objects_ext;

    }*/

    private void cmdOk()
    {
        // TODO:
/*
        System.out.println("wizard_2 [ok]");

        this.was_action = true;

        this.pump_objects_ext = new PumpValuesEntryExt[1];

        PumpValuesEntryExt po = new PumpValuesEntryExt(this.m_pump_add);
        po.setType(this.m_type);

        if ((this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_ACTIVITY) || 
            (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT) || 
            (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_URINE))
        {
            po.setValue(this.text_1.getText());
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_BG)
        {
            po.setValue(this.num_1.getValue().toString());
        }
        else if (this.m_type == PumpAdditionalDataType.PUMP_ADD_DATA_CH)
        {
            System.out.println("val: " + this.num_1.getValue());
            po.setValue(this.num_1.getValue().toString());
        }
        else
            System.out.println("Command for this type is not implemented !!!");

        this.pump_objects_ext[0] = po;

        this.dispose();  END HERE */
        // this.m_p

        /*        
                if (this.m_add_action)
                {
                    // add

                    if (debug)
                        System.out.println("dV: " + dV);

                    // this.m_dailyValuesRow = new DailyValuesRow();

                    this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());

                    // if (isFieldSet(BGField.getText()))

                    float f = m_da.getJFormatedTextValueFloat(ftf_bg1);

                    if (f > 0.0)
                    {
                        //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex(
                        // )+1, f);
                        this.m_dailyValuesRow.setBG(1, f);
                    }

                    this.m_dailyValuesRow.setIns1(m_da
                            .getJFormatedTextValueInt(this.ftf_ins1));
                    this.m_dailyValuesRow.setIns2(m_da
                            .getJFormatedTextValueInt(this.ftf_ins2));
                    // checkDecimalFields(Ins1Field.getText()));
                    //this.m_dailyValuesRow.setIns2(checkDecimalFields(Ins2Field.getText
                    // ()));
                    //this.m_dailyValuesRow.setCH(checkDecimalFields(BUField.getText()))
                    // ;
                    this.m_dailyValuesRow.setCH(m_da
                            .getJFormatedTextValueFloat(this.ftf_ch));
                    this.m_dailyValuesRow.setActivity(ActField.getText());
                    this.m_dailyValuesRow.setUrine(UrineField.getText());
                    this.m_dailyValuesRow.setComment(CommentField.getText());
                    // this.m_dailyValuesRow.setMealIdsList(null);

                    dV.setNewRow(this.m_dailyValuesRow);
                    this.m_actionDone = true;
                    this.dispose();
                }
                else
                {

                    // edit
                    this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());

                    float f = m_da.getJFormatedTextValueFloat(ftf_bg1);

                    if (f > 0.0)
                    {
                        //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex(
                        // )+1, f);
                        this.m_dailyValuesRow.setBG(1, f);
                    }

                    // if (isFieldSet(BGField.getText()))
                    //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex()+1,
                    // checkDecimalFields(BGField.getText()));
                    this.m_dailyValuesRow.setIns1(m_da
                            .getJFormatedTextValueInt(this.ftf_ins1));
                    this.m_dailyValuesRow.setIns2(m_da
                            .getJFormatedTextValueInt(this.ftf_ins2));

                    //this.m_dailyValuesRow.setIns1(checkDecimalFields(Ins1Field.getText
                    // ()));
                    //this.m_dailyValuesRow.setIns2(checkDecimalFields(Ins2Field.getText
                    // ()));
                    this.m_dailyValuesRow.setCH(m_da
                            .getJFormatedTextValueFloat(this.ftf_ch));
                    //this.m_dailyValuesRow.setCH(checkDecimalFields(BUField.getText()))
                    // ;
                    this.m_dailyValuesRow.setActivity(ActField.getText());
                    this.m_dailyValuesRow.setUrine(UrineField.getText());
                    this.m_dailyValuesRow.setComment(CommentField.getText());
                    // this.m_dailyValuesRow.setMealIdsList(null);

                    // mod.fireTableChanged(null);
                    // clearFields();
                    this.m_actionDone = true;
                    this.dispose();
                }
        */
    }

    public boolean isFieldSet(String text)
    {
        if ((text == null) || (text.trim().length() == 0))
            return false;
        else
            return true;
    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /*
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /*
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }

    /*
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "pages.GGC_Pump_Add_Parameter_Wizard1";
    }

    
    
    /*
     * focusGained
     */
    public void focusGained(FocusEvent arg0)
    {
    }

    boolean in_action = false;

    /*
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
            float v_2 = m_da.getBGValueDifferent(DataAccess.BG_MGDL, val);
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
            int v_2 = (int) m_da.getBGValueDifferent(DataAccess.BG_MMOL, val);
            this.num_1.setValue(new Integer(v_2));
        }
        else
            System.out.println("focus lost: unknown");

        in_action = false;

    }

    
    
    public void keyTyped(KeyEvent e)
    {
    }

    public void keyPressed(KeyEvent e)
    {
    }

    /**
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e)
    {

        if ((e.getSource().equals(this.num_1)) || (e.getSource().equals(this.num_2)))
        {
            focusProcess(e.getSource());
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            cmdOk();
        }

    }
    
    
    
    
    
}
