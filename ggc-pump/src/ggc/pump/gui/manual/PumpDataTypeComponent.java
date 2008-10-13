package ggc.pump.gui.manual;

import ggc.pump.util.I18nControl;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PumpDataTypeComponent extends JPanel
{
    
    private static final long serialVersionUID = -4449947661003378689L;

    public static final int TYPE_NONE = 0;
    public static final int TYPE_BASAL = 1;
    public static final int TYPE_BOLUS = 2;
    public static final int TYPE_EVENT = 3;
    public static final int TYPE_ALARM = 4;
    public static final int TYPE_ERROR = 5;
    public static final int TYPE_REPORT = 6;
    public static final int TYPE_ADD_DATA = 7;
    
    TemporaryBasalRateComponent tbr_cmp = null;
    JLabel label_1, label_2, label_3, label_4;
    JTextField text_1, text_2;
    JComboBox combo_1, combo_2;
    int type = 0;
    int height = 0;
    int width = 400;
    
    I18nControl ic = I18nControl.getInstance();

    private Object[] type_items = {
                                   ic.getMessage("SELECT_ITEM"),
                                   ic.getMessage("BASAL_DOSE"),
                                   ic.getMessage("BOLUS_DOSE"),
                                   ic.getMessage("EVENT"),
                                   ic.getMessage("ALARM"),
                                   ic.getMessage("ERROR"),
                                   ic.getMessage("REPORT"),
                                   ic.getMessage("ADDITIONAL_DATA")
    };
    
    
    
    public PumpDataTypeComponent(int startx)
    {
        super();
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
                    //this.setComboAndText();
                    this.setUnsupported();
                } break;
        
            case PumpDataTypeComponent.TYPE_BASAL:
            case PumpDataTypeComponent.TYPE_BOLUS:
            case PumpDataTypeComponent.TYPE_REPORT:
                {
                    this.setUnsupported();
                } break;
    
            case PumpDataTypeComponent.TYPE_NONE:
            case PumpDataTypeComponent.TYPE_ADD_DATA:
                default:
                {
                    this.setEmpty();
                }  break;
                    
        
        }
        
        
   
        
        //this.setHeight(0);
        //JPanel p = new JPanel();
        
        
    }

    
    public Object[] getItems()
    {
        return this.type_items;
    }
    
    
    private void hideAll()
    {
        tbr_cmp.setVisible(false);
        label_1.setVisible(false); 
        label_1.setHorizontalAlignment(JLabel.LEFT);
        label_2.setVisible(false);
        label_3.setVisible(false);
        label_4.setVisible(false);
        text_1.setVisible(false);
        text_2.setVisible(false);
        combo_1.setVisible(false);
        combo_2.setVisible(false);
        
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
    
    private void setComboAndText()
    {
        
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
    
    private class TemporaryBasalRateComponent extends JPanel
    {

        private static final long serialVersionUID = -1192269467658397557L;

        
        


    }
    
    
    
    
}
