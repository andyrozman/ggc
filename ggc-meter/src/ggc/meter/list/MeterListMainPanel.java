package ggc.meter.list;

//package ggc.gui.nutrition.panels;

//import ggc.gui.nutrition.NutritionTreeDialog;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


public class MeterListMainPanel extends MeterListAbstractPanel //JPanel
{
	static final long serialVersionUID = 0L;


    I18nControl ic = I18nControl.getInstance();
    DataAccessMeter m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button;

    MeterListDialog m_dialog = null;

    String[] nutrition_db = {
	    "",
	    "USDA_NUTRITION_DATABASE",
	    "USER_NUTRITION_DATABASE",
	    "MEAL_DATABASE"
    };
    

    public MeterListMainPanel(MeterListDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = DataAccessMeter.getInstance();

        font_big = m_da.getFont(DataAccessMeter.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccessMeter.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccessMeter.FONT_NORMAL);

        createPanel();

    }



    public void createPanel()
    {

	
	
        this.setSize(460, 520);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.PLAIN, 14);

        //String nut_db = nutrition_db[this.m_dialog.getType()];
        
        

        label = new JLabel(ic.getMessage("METERS_LIST"));
        label.setBounds(0, 35, 520, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(ic.getMessage("METERS_LIST_DESC"));
        label.setBounds(40, 120, 400, 250);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(fnt_18); 
        this.add(label, null);

        return;
    }


    public void setData(Object obj)
    {
    }


}
    
    
