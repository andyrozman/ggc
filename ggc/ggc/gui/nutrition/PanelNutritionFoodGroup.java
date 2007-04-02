package ggc.gui.nutrition;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ggc.db.datalayer.FoodGroup;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman

    
public class PanelNutritionFoodGroup extends GGCTreePanel //JPanel
{

    I18nControl ic = I18nControl.getInstance();
    DataAccess m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_name, label_name_i18n;
    JButton button;

    NutritionTreeDialog m_dialog = null;


    public PanelNutritionFoodGroup(NutritionTreeDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = DataAccess.getInstance();

        font_big = m_da.getFont(DataAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);

        createPanel();

    }



    public void createPanel()
    {

        this.setSize(420, 460);
        this.setLayout(null);

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
	Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);


        label = new JLabel(ic.getMessage("FOOD_GROUP"));
        label.setBounds(0, 35, 420, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);

	//this.setDescription(ch.getDescription());
	//this.setDescription_i18n(ch.getDescription_i18n());

        label = new JLabel(ic.getMessage("GROUP_NAME") + ":" );
        label.setBounds(40, 150, 100, 30);
        label.setFont(fnt_14_bold); 
        this.add(label, null);

        label = new JLabel(ic.getMessage("TRANSLATED_NAME") + ":");
        label.setBounds(40, 230, 300, 60);
        label.setFont(fnt_14_bold); 
        this.add(label, null);


	label_name = new JLabel();
	label_name.setBounds(80, 180, 300, 30);
	label_name.setFont(fnt_14); 
	this.add(label_name, null);

	label_name_i18n = new JLabel();
	label_name_i18n.setBounds(80, 260, 300, 60);
	label_name_i18n.setFont(fnt_14); 
	this.add(label_name_i18n, null);




/*
        label = new JLabel(ic.getMessage("EDIT_VIEW"));
        label.setBounds(40, 280, 300, 30);
        label.setFont(fnt_18); 
        this.add(label, null);

        label = new JLabel(ic.getMessage("EDIT_VIEW_DESC"));
        label.setBounds(40, 310, 300, 60);
        label.setFont(font_normal); 
        this.add(label, null);
*/
        return;
    }

    public void setSelectedGroup(FoodGroup group)
    {
	//label_name
	//label_name_i18n
    }


    public void setData(Object obj)
    {
	FoodGroup group = (FoodGroup)obj;

	label_name.setText(group.getDescription());
	label_name_i18n.setText(group.getDescription_i18n());

    }




}
    
    

