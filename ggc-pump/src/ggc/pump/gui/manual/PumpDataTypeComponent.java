package ggc.pump.gui.manual;

import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.util.I18nControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.atech.graphics.components.JDecimalTextField;

public class PumpDataTypeComponent extends JPanel implements ActionListener
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
    int type = 0;
    int height = 0;
    int width = 400;
    
    PumpReport m_p_report = new PumpReport();
    PumpEvents m_p_event = new PumpEvents();
    PumpAlarms m_p_alarm = new PumpAlarms();
    PumpErrors m_p_error = new PumpErrors();
    PumpBolusType m_p_bolus = new PumpBolusType();
    
    PumpDataRowDialog m_parent = null;
    
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
    
    
    
    
    
    public PumpDataTypeComponent(PumpDataRowDialog parent, int startx)
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
    }
    
    private void setEmpty()
    {
        this.hideAll();
        setHeight(0);
    }
    
    private void setUnsupported()
    {
        this.hideAll();
        label_1.setBounds(0, 20, 370, 25);
        label_1.setText("Component doesn't support this type: " + this.type_items[type]);
        label_1.setVisible(true);
        this.setHeight(40);
    }

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

        
        
        // TODO
//        this.setUnsupported();

        
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
        
        // TODO
        this.setUnsupported();
    }

    
    // types: bolus
    private void setBolus()
    {
        this.hideAll();
        
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
        
        this.m_parent.realignComponents();
        
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
            System.out.println("Bolus event: " + this.combo_1.getSelectedIndex());
            setBolusSubType(this.combo_1.getSelectedIndex());
        }
        
        
        // TODO Auto-generated method stub
        
    }
    
    
    
    
    
    
    private class TemporaryBasalRateComponent extends JPanel
    {

        private static final long serialVersionUID = -1192269467658397557L;

        
        


    }

    
    
    
    
}
