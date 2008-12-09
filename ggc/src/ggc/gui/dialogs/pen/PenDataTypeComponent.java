package ggc.gui.dialogs.pen;

import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.gui.manual.PumpDataTypeComponent;
import ggc.pump.util.I18nControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


public class PenDataTypeComponent extends JPanel implements ActionListener
{
    
    private static final long serialVersionUID = -4449947661003378689L;

    public static final int TYPE_NONE = 0;
    public static final int TYPE_BASAL = 1;
    public static final int TYPE_BOLUS = 2;
    public static final int TYPE_EVENT = 3;
    public static final int TYPE_ALARM = 4;
    public static final int TYPE_ERROR = 5;
    public static final int TYPE_REPORT = 6;
    public static final int TYPE_PEN_INJECTION_BASAL = 7;
    public static final int TYPE_PEN_INJECTION_BOLUS = 8;
    public static final int TYPE_ADDITIONAL_DATA = 9;
    
    TemporaryBasalRateComponent tbr_cmp = null;
    JLabel label_1, label_2, label_3, label_4;
    JTextField text_1, text_2;
    JComboBox combo_1, combo_2;
    JDecimalTextField num_tf_1_d2, num_tf_2_d2;
    JRadioButton rb_1, rb_2, rb_3;
    ButtonGroup bg;
    ProfileComponent profile_comp;
    
    int type = 0;
    int height = 0;
    int width = 400;
    
    PumpReport m_p_report = new PumpReport();
    PumpEvents m_p_event = new PumpEvents();
    PumpAlarms m_p_alarm = new PumpAlarms();
    PumpErrors m_p_error = new PumpErrors();
    PumpBolusType m_p_bolus = new PumpBolusType();
    PumpBasalSubType m_p_basal = new PumpBasalSubType();
    
    DailyRowDialogPen m_parent = null;
    
    I18nControl ic = I18nControl.getInstance();

    private Object[] type_items = {
                                   ic.getMessage("SELECT_ITEM"),
                                   ic.getMessage("BASAL_DOSE"),
                                   ic.getMessage("BOLUS_DOSE"),
                                   ic.getMessage("EVENT"),
                                   ic.getMessage("ALARM"),
                                   ic.getMessage("ERROR"),
                                   ic.getMessage("REPORT"),
                                   ic.getMessage("PEN_INJECTION_BASAL"),
                                   ic.getMessage("PEN_INJECTION_BOLUS"),
                                   ic.getMessage("ADDITIONAL_DATA")
    };
    
    
    
    
    
    public PenDataTypeComponent(DailyRowDialogPen parent, int startx)
    {
        super();
        this.m_parent = parent;
        this.setLayout(null);
        init();
        this.setBounds(30, startx, width, height);
    }
    
    public void init()
    {
        tbr_cmp = new TemporaryBasalRateComponent();
        this.add(tbr_cmp);
        label_1 = new JLabel(); 
        this.add(label_1);
        label_2 = new JLabel();
        this.add(label_2);
        label_3 = new JLabel();
        this.add(label_3);
        label_4 = new JLabel();
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
        
        //JRadioButton rb_1, rb_2;
        
        this.rb_1 = new JRadioButton();
        this.add(rb_1);
        this.rb_2 = new JRadioButton();
        this.add(rb_2);
        this.rb_3 = new JRadioButton();
        this.add(rb_3);
        
        this.bg = new ButtonGroup();

        
        profile_comp = new ProfileComponent();
        this.add(profile_comp);
        
        
    }
    
    
    
    public void setType(int type)
    {
        if (this.type==type)
            return;
        
        this.type=type;
        
        switch(this.type)
        {

            case PumpDataTypeComponent.TYPE_EVENT:
            case PumpDataTypeComponent.TYPE_ALARM:
            case PumpDataTypeComponent.TYPE_ERROR:
                {
                    this.setComboAndText();
                } break;
        
            case PumpDataTypeComponent.TYPE_BASAL:
                {
                    this.setBasal();
                } break;
                
            case PumpDataTypeComponent.TYPE_BOLUS:
                {
                    this.setBolus();
                } break;
                
            case PumpDataTypeComponent.TYPE_REPORT:
                {
                    this.setReport();
                } break;
                
            case PumpDataTypeComponent.TYPE_PEN_INJECTION_BASAL:
            case PumpDataTypeComponent.TYPE_PEN_INJECTION_BOLUS:
                {
                    this.setNumericTextAndText();
                } break;
    
            case PumpDataTypeComponent.TYPE_NONE:
            case PumpDataTypeComponent.TYPE_ADDITIONAL_DATA:
            default:
                {
                    this.setEmpty();
                }  break;
        }
        
    }

    
    public Object[] getItems()
    {
        return this.type_items;
    }
    
    
    private void hideAll()
    {
        tbr_cmp.setVisible(false);
        label_1.setVisible(false); 
//        label_1.setHorizontalAlignment(JLabel.LEFT);
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
    }
    
    private void setEmpty()
    {
        this.hideAll();
        setHeight(0);
    }
    
    /*
    private void setUnsupported()
    {
        this.hideAll();
        label_1.setBounds(0, 20, 370, 25);
        label_1.setText("Component doesn't support this type: " + this.type_items[type]);
        label_1.setVisible(true);
        this.setHeight(40);
    }*/

    // type: event, alarm, error
    private void setComboAndText()
    {
        this.hideAll();
        
        this.label_1.setBounds(0, 20, 150, 25);
        this.label_1.setVisible(true);
        
        this.combo_1.setBounds(150, 20, 180, 25);
        this.combo_1.setVisible(true);

        this.label_2.setBounds(0, 55, 150, 25);
        this.label_2.setVisible(true);
        
        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);

        this.label_2.setText(ic.getMessage("COMMENT") + ":");
        
        this.setHeight(85);

        if (this.type == PumpDataTypeComponent.TYPE_EVENT)
        {
            this.label_1.setText(ic.getMessage("EVENT_TYPE")+ ":");
            addAllItems(this.combo_1, this.m_p_event.getDescriptions());
        }
        else if (this.type == PumpDataTypeComponent.TYPE_ALARM)
        {
            this.label_1.setText(ic.getMessage("ALARM_TYPE")+ ":");
            addAllItems(this.combo_1, this.m_p_alarm.getDescriptions());
        }
        else if (this.type == PumpDataTypeComponent.TYPE_ERROR)
        {
            this.label_1.setText(ic.getMessage("ERROR_TYPE")+ ":");
            addAllItems(this.combo_1, this.m_p_error.getDescriptions());
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

        this.label_1.setBounds(0, 20, 150, 25);
        this.label_1.setVisible(true);

        this.num_tf_1_d2.setBounds(150, 20, 180, 25);
        this.num_tf_1_d2.setVisible(true);
        this.num_tf_1_d2.setValue(new Float(0.0f));
        
        this.label_2.setBounds(0, 55, 150, 25);
        this.label_2.setVisible(true);
        this.label_2.setText(ic.getMessage("COMMENT") + ":");
        
        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);

        
        this.setHeight(85);
        
        if (this.type == PumpDataTypeComponent.TYPE_PEN_INJECTION_BASAL)
        {
            this.label_1.setText(ic.getMessage("BASAL_INSULIN") + ":");
        }
        else if (this.type == PumpDataTypeComponent.TYPE_PEN_INJECTION_BOLUS)
        {
            this.label_1.setText(ic.getMessage("BOLUS_INSULIN") + ":");
        }
        
    }
    
    
    // type: report
    private void setReport()
    {
        this.hideAll();

        this.label_1.setBounds(0, 20, 150, 25);
        this.label_1.setVisible(true);
        this.label_1.setText(ic.getMessage("REPORT_TYPE") + ":");
        
        this.combo_1.setBounds(150, 20, 180, 25);
        this.combo_1.setVisible(true);
        addAllItems(this.combo_1, m_p_report.getDescriptions());

        this.label_2.setText(ic.getMessage("REPORT_TEXT") + ":");
        this.label_2.setBounds(0, 55, 150, 25);
        this.label_2.setVisible(true);
        
        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);

        this.label_3.setBounds(0, 90, 150, 25);
        this.label_3.setVisible(true);
        this.label_3.setText(ic.getMessage("COMMENT") + ":");

        this.text_2.setBounds(150, 90, 180, 25);
        this.text_2.setVisible(true);
        
        
        this.setHeight(115);
        
        
        
    }
    

    int sub_type = 0;

    // types: basal
    private void setBasal()
    {
        this.hideAll();
        this.sub_type = 0;
        
        this.label_1.setBounds(0, 20, 150, 25);
        this.label_1.setVisible(true);
        this.label_1.setText(ic.getMessage("BASAL_TYPE") + ":");
        
        this.combo_1.setBounds(150, 20, 180, 25);
        this.combo_1.setVisible(true);
        this.combo_1.setActionCommand("basal");
        addAllItems(this.combo_1, this.m_p_basal.getDescriptions());
        
        this.label_2.setBounds(0, 55, 150, 25);
        this.label_2.setVisible(true);
        this.label_2.setText(ic.getMessage("COMMENT") + ":");
        
        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);
        
        this.setHeight(85);
        
    }

    
    public void setBasalSubType(int stype)
    {
        // 20 55 
        if (this.sub_type==stype)
            return;
        else
            this.sub_type = stype;

        
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
        
        
        switch(this.sub_type)
        {
            case PumpBasalSubType.PUMP_BASAL_VALUE:
            {
                this.label_2.setBounds(0, 90, 150, 25);
                this.text_1.setBounds(150, 90, 180, 25);
                
                this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                this.num_tf_1_d2.setVisible(true);
                this.label_3.setBounds(0, 55, 150, 25);
                this.label_3.setText(ic.getMessage("AMOUNT") + ":");
                this.label_3.setVisible(true);
                
                this.setHeight(115);
                
            } break;


            case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE:
            {
                this.label_2.setBounds(0, 90, 150, 25);
                this.text_1.setBounds(150, 90, 180, 25);
                
                this.tbr_cmp.setBounds(0, 55, 180, 25);
                this.tbr_cmp.setVisible(true);

                //this.num_tf_1_d2.setVisible(true);
                //this.label_3.setBounds(0, 55, 150, 25);
                //this.label_3.setText(ic.getMessage("AMOUNT") + ":");
                //this.label_3.setVisible(true);
                
                this.setHeight(115);
                
            } break;

            
            case PumpBasalSubType.PUMP_BASAL_PROFILE:
            {
                this.label_2.setBounds(0, 90, 150, 25);
                this.text_1.setBounds(150, 90, 180, 25);
                
                this.profile_comp.setBounds(0, 55, 180, 25);
                this.profile_comp.setVisible(true);

                this.setHeight(115);

            } break;
            
            case PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_PROFILE:
            {
                this.label_2.setBounds(0, 125, 150, 25);
                this.text_1.setBounds(150, 125, 180, 25);

                
                this.profile_comp.setBounds(0, 55, 180, 25);
                this.profile_comp.setVisible(true);
                
                this.tbr_cmp.setBounds(0, 90, 180, 25);
                this.tbr_cmp.setVisible(true);

                this.setHeight(150);
                
            } break;
            
            case PumpBasalSubType.PUMP_BASAL_PUMP_STATUS:
            {
                this.label_2.setBounds(0, 140, 150, 25);
                this.text_1.setBounds(150, 140, 180, 25);
                
                this.rb_1.setText(ic.getMessage("ON"));
                this.rb_1.setBounds(150, 55, 200, 25);
                this.rb_1.setVisible(true);
                this.rb_1.setSelected(true);
                this.rb_2.setText(ic.getMessage("OFF"));
                this.rb_2.setBounds(150, 80, 200, 25);
                this.rb_2.setVisible(true);
                this.rb_3.setText(ic.getMessage("SUSPENDED"));
                this.rb_3.setBounds(150, 105, 200, 25);
                this.rb_3.setVisible(true);
                
                this.bg.add(rb_1);
                this.bg.add(rb_2);
                this.bg.add(rb_3);
                
                //this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                //this.num_tf_1_d2.setVisible(true);
                
                
                
                this.label_3.setBounds(0, 55, 150, 25);
                this.label_3.setText(ic.getMessage("PUMP_STATUS") + ":");
                this.label_3.setVisible(true);
                
                this.setHeight(165);
                
            } break;
            
            
            default:
            {
                this.label_2.setVisible(false);
                this.text_1.setVisible(false);
                
                this.setHeight(55);
                
            } break;
        }

        //this.m_parent.realignComponents();
        
    }
    
    
    
    // types: bolus
    private void setBolus()
    {
        this.hideAll();
        this.sub_type = 0;
        
        this.label_1.setBounds(0, 20, 150, 25);
        this.label_1.setVisible(true);
        this.label_1.setText(ic.getMessage("BOLUS_TYPE") + ":");
        
        this.combo_1.setBounds(150, 20, 180, 25);
        this.combo_1.setVisible(true);
        this.combo_1.setActionCommand("bolus");
        addAllItems(this.combo_1, this.m_p_bolus.getDescriptions());
        
        this.label_2.setBounds(0, 55, 150, 25);
        this.label_2.setVisible(true);
        this.label_2.setText(ic.getMessage("COMMENT") + ":");
        
        this.text_1.setBounds(150, 55, 180, 25);
        this.text_1.setVisible(true);
        
        this.setHeight(85);

    }

    
    
    public void setBolusSubType(int stype)
    {
        // 20 55 
        if (this.sub_type==stype)
            return;
        else
            this.sub_type = stype;

        
        this.num_tf_1_d2.setVisible(false);
        this.num_tf_2_d2.setVisible(false);
        this.label_3.setVisible(false);
        this.label_4.setVisible(false);
        
        
        switch(this.sub_type)
        {
            case PumpBolusType.PUMP_BOLUS_STANDARD:
            case PumpBolusType.PUMP_BOLUS_SCROLL:
            case PumpBolusType.PUMP_BOLUS_EXTENDED:
            {
                this.label_2.setBounds(0, 90, 150, 25);
                this.text_1.setBounds(150, 90, 180, 25);
                
                this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                this.num_tf_1_d2.setVisible(true);
                this.label_3.setBounds(0, 55, 150, 25);
                this.label_3.setText(ic.getMessage("AMOUNT") + ":");
                this.label_3.setVisible(true);
                
                this.setHeight(115);
            } break;
            
            case PumpBolusType.PUMP_BOLUS_MULTIWAVE:
            {
                this.label_2.setBounds(0, 125, 150, 25);
                this.text_1.setBounds(150, 125, 180, 25);
                
                this.num_tf_1_d2.setBounds(150, 55, 180, 25);
                this.num_tf_1_d2.setVisible(true);
                this.label_3.setBounds(0, 55, 150, 25);
                this.label_3.setText(ic.getMessage("AMOUNT_MW_1") + ":");
                this.label_3.setVisible(true);
                
                //90
                this.label_4.setText(ic.getMessage("AMOUNT_MW_2") + ":");
                label_4.setBounds(0, 90, 150, 25);
                this.label_4.setVisible(true);
                this.num_tf_2_d2.setBounds(150, 90, 180, 25);
                this.num_tf_2_d2.setVisible(true);
                
                
                
                this.setHeight(150);
                
            } break;
                 
        
        
            case PumpBolusType.PUMP_BOLUS_NONE:
            {
/*                this.num_tf_1_d2.setVisible(false);
                this.num_tf_2_d2.setVisible(false);
                this.label_3.setVisible(false);
                this.label_4.setVisible(false); */
                
                this.label_2.setBounds(0, 55, 150, 25);
                this.text_1.setBounds(150, 55, 180, 25);
                this.setHeight(85);
            } break;
            
        }
        
        //this.m_parent.realignComponents();
        
    }
    
    
    
    
    
    
    private void addAllItems(JComboBox cb, String[] array)
    {
        cb.removeAllItems();
        
        for(int i=0; i<array.length; i++)
        {
            cb.addItem(array[i]);
        }
    }
    
    
    public void setHeight(int height)
    {
        this.height = height;
        this.setSize(width, height);
    }
    
    public int getHeight()
    {
        return this.height;
    }

    
    public void actionPerformed(ActionEvent ev)
    {
        String cmd = ev.getActionCommand();
        
        if (cmd.equals("bolus"))
        {
//            System.out.println("Bolus event: " + this.combo_1.getSelectedIndex());
            setBolusSubType(this.combo_1.getSelectedIndex());
        }
        else if (cmd.equals("basal"))
        {
//            System.out.println("Basal event: " + this.combo_1.getSelectedIndex());
            setBasalSubType(this.combo_1.getSelectedIndex());
        }
    }
    
    
    
    private class ProfileComponent extends JPanel implements ActionListener
    {
        
        private static final long serialVersionUID = 1195430308386555236L;
        JLabel label_1_1, label_2_1;
        JButton button_1;

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
            this.add(label_1_1);
            
            label_2_1 = new JLabel(ic.getMessage("NOT_SELECTED"));
            label_2_1.setBounds(150, 0, 140, 25);
            this.add(label_2_1);
            
            button_1 = new JButton("...");
            button_1.setBounds(300, 0, 25, 25 );
            button_1.addActionListener(this);
            this.add(button_1);
            
        }
        
        
        public void setBounds(int x, int y, int width, int height)
        {
            super.setBounds(x, y, 350, 30);
        }

        public void actionPerformed(ActionEvent arg0)
        {
            // TODO Profile selector
            System.out.println("Profile selector called");
        }
        
        
        
    }
    
    
    private class TemporaryBasalRateComponent extends JPanel
    {

        
        private static final long serialVersionUID = -1192269467658397557L;
        String[] vals = { "-", "+" };
        
        JSpinner spinner = null;
        JComboBox cb_sign = null;
        JLabel label_1_1, label_2_1;
        
        public TemporaryBasalRateComponent()
        {
            super();
            this.setLayout(null);
            this.init();
        }

        private void init()
        {
            label_1_1 = new JLabel(ic.getMessage("TEMPORARY_BASAL_RATE") + ":");
            label_1_1.setBounds(0, 0, 140, 25);
            this.add(label_1_1);
            
            
            cb_sign = new JComboBox(vals);
            cb_sign.setBounds(220, 0, 40, 25);
            this.add(cb_sign);
            
            spinner = new JSpinner();
            spinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
            spinner.setBounds(270, 0, 40, 25);
            this.add(spinner);
            
            label_2_1 = new JLabel("%");
            label_2_1.setBounds(320, 0, 40, 25);
            this.add(label_2_1);
            
        }
        
        
        public void setBounds(int x, int y, int width, int height)
        {
            super.setBounds(x, y, 350, 30);
        }
        


    }

    
    
    
    
}
