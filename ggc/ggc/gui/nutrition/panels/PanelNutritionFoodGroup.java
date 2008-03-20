package ggc.gui.nutrition.panels;

import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.MealGroup;
import ggc.gui.nutrition.GGCTreeRoot;
import ggc.gui.nutrition.NutritionTreeDialog;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman

    
public class PanelNutritionFoodGroup extends GGCTreePanel //JPanel
{

//    I18nControl ic = I18nControl.getInstance();
    DataAccess m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_name, label_name_i18n, label_parent, label_title, label_name_i18n_key;
    JButton button;

    NutritionTreeDialog m_dialog = null;
    
    int group_type = 2;
    

    public PanelNutritionFoodGroup(NutritionTreeDialog dia, int type)
    {

        super(true, I18nControl.getInstance());
        
        this.group_type = type;

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


        label_title = new JLabel(ic.getMessage("FOOD_GROUP"));
        label_title.setBounds(0, 35, 520, 40);
        label_title.setFont(font_big); 
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

	//this.setDescription(ch.getDescription());
	//this.setDescription_i18n(ch.getDescription_i18n());

        label = new JLabel(ic.getMessage("GROUP_NAME") + ":" );
        label.setBounds(60, 110, 100, 25);
        label.setFont(fnt_14_bold); 
        this.add(label, null);

	label_name = new JLabel();
	label_name.setBounds(60, 140, 300, 25);
	label_name.setFont(fnt_14); 
	this.add(label_name, null);
        
        
	
        label = new JLabel(ic.getMessage("TRANSLATION_KEYWORD") + ":");
        label.setBounds(60, 190, 320, 25);
        label.setFont(fnt_14_bold); 
        this.add(label, null);



	label_name_i18n_key = new JLabel();
	label_name_i18n_key.setBounds(60, 220, 320, 25);
	label_name_i18n_key.setFont(fnt_14);
	
	this.add(label_name_i18n_key, null);
	
	
	
        label = new JLabel(ic.getMessage("TRANSLATED_NAME") + ":");
        label.setBounds(60, 270, 300, 25);
        label.setFont(fnt_14_bold); 
        this.add(label, null);



	label_name_i18n = new JLabel();
	label_name_i18n.setBounds(60, 300, 300, 25);
	label_name_i18n.setFont(fnt_14); 
	this.add(label_name_i18n, null);


	
        label = new JLabel(ic.getMessage("PARENT_GROUP"));
        label.setBounds(60, 350, 300, 25);
        label.setFont(fnt_14_bold); 
        this.add(label, null);


        label_parent = new JLabel("ddd");
        label_parent.setBounds(60, 380, 300, 25);
        label_parent.setFont(fnt_14); 
        this.add(label_parent, null);
        
        
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


    public void setParent(Object obj)
    {
	
    }
    
    
    
    public void setData(Object obj)
    {
	//if (obj instanceof GGCTreeRoot)
	//    return;
	
	if ((group_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (group_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    FoodGroup group = (FoodGroup)obj;

	    label_title.setText(ic.getMessage("FOOD_GROUP"));
	    label_name.setText(group.getName());
	    label_name_i18n_key.setText(group.getName_i18n());
	    label_name_i18n.setText(ic.getMessage(group.getName_i18n()));
	    
	    if (group.getParentId()>0)
	    {
		FoodGroup fg = m_da.tree_roots.get("2").m_groups_ht.get("" + group.getParentId());
		this.label_parent.setText(fg.getName());
	    }
	    else
	    {
		this.label_parent.setText(ic.getMessage("ROOT"));
	    }
	}
	else
	{
	    MealGroup group = (MealGroup)obj;

	    label_title.setText(ic.getMessage("MEAL_GROUP"));
	    label_name.setText(group.getName());
	    label_name_i18n_key.setText(group.getName_i18n());
	    label_name_i18n.setText(ic.getMessage(group.getName_i18n()));
	    
	    if (group.getParent_id()>0)
	    {
		MealGroup fg = m_da.tree_roots.get("3").m_meal_groups_ht.get("" + group.getParent_id());
		this.label_parent.setText(fg.getName());
	    }
	    else
	    {
		this.label_parent.setText(ic.getMessage("ROOT"));
	    }
	    
	    
	}

    }


    /**
     * Get Warning string. This method returns warning string for either add or edit.
     * If value returned is null, then no warning message box will be displayed.
     * 
     * @param action_type type of action (ACTION_ADD, ACTION_EDIT)
     * @return String value as warning string
     */
    public String getWarningString(int action_type)
    {
	return null;
    }



    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    public boolean hasDataChanged()
    {
	return false;
    }



    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    public boolean saveData()
    {
	return false;
    }


}
    
    

