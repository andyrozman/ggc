package ggc.core.nutrition.panels;

import ggc.core.nutrition.GGCTreeRoot;
import ggc.core.nutrition.NutritionTreeDialog;
import ggc.core.nutrition.dialogs.NutritionGroupDialog;
import ggc.core.util.DataAccess;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.MealGroup;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.atech.graphics.components.EditableAbstractPanel;

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman

    
public class PanelNutritionFoodGroupEdit extends GGCTreePanel implements ActionListener //JPanel
{


//    I18nControl ic = I18nControl.getInstance();
 
    DataAccess m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_name, label_name_i18n, label_parent, label_title;
    JButton button, button_select;
    
    JTextField tf_parent, tf_name, tf_name_i18n, tf_name_i18n_key;

    NutritionTreeDialog m_dialog = null;
    int group_type = 2;


    FoodGroup parent_food_group = null;
    MealGroup parent_meal_group = null;
    
    FoodGroup food_group = null;
    MealGroup meal_group = null;
    
    boolean was_saved = true;
    //private boolean add = false;
    
    
    
    
    //JTextField tf_name_i18n=null;
    

    public PanelNutritionFoodGroupEdit(NutritionTreeDialog dia, int type)
    {

        super(true, dia.ic);
        
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


        label_title = new JLabel(ic.getMessage("FOOD_GROUP_EDIT_ADD"));
        label_title.setBounds(0, 35, 520, 40);
        label_title.setFont(font_big); 
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

	//this.setDescription(ch.getDescription());
	//this.setDescription_i18n(ch.getDescription_i18n());

        label = new JLabel(ic.getMessage("GROUP_NAME") + ":" );
        label.setBounds(80, 100, 100, 30);
        label.setFont(fnt_14_bold); 
        this.add(label, null);

	tf_name = new JTextField();
	tf_name.setBounds(80, 140, 320, 25);
	tf_name.setFont(fnt_14); 
	tf_name.addKeyListener(new KeyListener(){

	    /* 
	     * keyReleased()
	     * Creates I18n keyword and gets translation
	     * 
	     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	     */
	    public void keyReleased(KeyEvent arg0)
	    {
		createKeyWord();
	    }

	    public void keyPressed(KeyEvent e) { }
	    public void keyTyped(KeyEvent e) { }
	    
	});
	this.add(tf_name, null);
        
	//new KeyListener();
        
	
        label = new JLabel(ic.getMessage("TRANSLATION_KEYWORD") + ":");
        label.setBounds(80, 180, 320, 60);
        label.setFont(fnt_14_bold); 
        this.add(label, null);



	tf_name_i18n_key = new JTextField();
	tf_name_i18n_key.setBounds(80, 230, 320, 25);
	tf_name_i18n_key.setFont(fnt_14);
	tf_name_i18n_key.setEditable(false);
	
	this.add(tf_name_i18n_key, null);
	
	
        label = new JLabel(ic.getMessage("TRANSLATED_NAME") + ":");
        label.setBounds(80, 265, 320, 60);
        label.setFont(fnt_14_bold); 
        this.add(label, null);



	tf_name_i18n = new JTextField();
	tf_name_i18n.setBounds(80, 315, 320, 25);
	tf_name_i18n.setFont(fnt_14);
	tf_name_i18n.setEditable(false);
	
	this.add(tf_name_i18n, null);





        label = new JLabel(ic.getMessage("PARENT_GROUP"));
        label.setBounds(80, 365, 300, 30);
        label.setFont(fnt_14_bold); 
        this.add(label, null);
        
        
        tf_parent = new JTextField();
        tf_parent.setBounds(80, 400, 320, 25);
        tf_parent.setEditable(false);
        this.add(tf_parent, null);
        
  
        button_select = new JButton(ic.getMessage("SELECT_GROUP"));
        button_select.setActionCommand("select_group");
        button_select.addActionListener(this);
        button_select.setBounds(260, 430, 140,25);
        this.add(button_select, null);
        
/*
        label_parent = new JLabel(ic.getMessage("Desc"));
        label_parent.setBounds(40, 370, 300, 60);
        label_parent.setFont(font_normal); 
        this.add(label_parent, null);
*/
        
        createKeyWord();
        
        return;
    }

    public void createKeyWord()
    {
	String key = m_da.makeI18nKeyword(tf_name.getText());
	tf_name_i18n_key.setText(key);
	tf_name_i18n.setText(ic.getMessage(key));
    }
    
    /*
    public void setSelectedGroup(FoodGroup group)
    {
	//label_name
	//label_name_i18n
    }*/

    
    
    public boolean checkRecursiveGroup(FoodGroup fg, long target)
    {
	if (fg.getId() == target)
	    return true;
	else
	{
	    for(int i=0; i<fg.getGroupChildrenCount(); i++)
	    {
		boolean check = checkRecursiveGroup((FoodGroup)fg.getGroupChild(i), target);
		if (check==true)
		    return true;
	    }
	    
	    return false;
	}
    }
    

    public boolean checkRecursiveGroup(MealGroup fg, long target)
    {
	if (fg.getId() == target)
	    return true;
	else
	{
	    for(int i=0; i<fg.getGroupChildrenCount(); i++)
	    {
		boolean check = checkRecursiveGroup((MealGroup)fg.getGroupChild(i), target);
		if (check==true)
		    return true;
	    }
	    
	    return false;
	}
    }
    
    
    
    FoodGroup parent_group;

    public void setParent(Object obj)
    {
	this.was_saved = false;
	this.button_select.setEnabled(false);
	
	if (group_type == GGCTreeRoot.TREE_USER_NUTRITION)
	{
	    setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
	    tf_name.setText(ic.getMessage("NEW_GROUP"));
	    this.label_title.setText(ic.getMessage("ADD_FOOD_GROUP"));
	    this.parent_food_group = (FoodGroup)obj;
	    this.tf_parent.setText(this.parent_food_group.getName());
	    createKeyWord();
	}
	else
	{
	    setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
	    tf_name.setText(ic.getMessage("NEW_GROUP"));
	    this.label_title.setText(ic.getMessage("ADD_MEAL_GROUP"));
	    this.parent_meal_group = (MealGroup)obj;
	    this.tf_parent.setText(this.parent_meal_group.getName());
	    createKeyWord();
	}
    }
    

    public void setParentRoot(Object obj)
    {
	this.was_saved = false;
	this.button_select.setEnabled(false);
	
	if (group_type == GGCTreeRoot.TREE_USER_NUTRITION)
	{
	    setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
	    tf_name.setText(ic.getMessage("NEW_GROUP"));
	    this.label_title.setText(ic.getMessage("ADD_FOOD_GROUP"));
	    this.parent_food_group = new FoodGroup(2);
	    this.parent_food_group.setId(0L);
	    this.tf_parent.setText(ic.getMessage("ROOT"));
	    createKeyWord();
	}
	else
	{
	    setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
	    tf_name.setText(ic.getMessage("NEW_GROUP"));
	    this.label_title.setText(ic.getMessage("ADD_MEAL_GROUP"));
	    this.parent_meal_group = new MealGroup();
	    this.parent_meal_group.setId(0L);
	    this.tf_parent.setText(ic.getMessage("ROOT"));
	    createKeyWord();
	}
    }
    
    
    
    public void setData(Object obj)
    {
	this.was_saved = false;
	this.button_select.setEnabled(true);

	
	if ((group_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (group_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);
	    this.label_title.setText(ic.getMessage("EDIT_FOOD_GROUP"));
	    this.food_group = (FoodGroup)obj;

	    tf_name.setText(this.food_group.getName());
	    tf_name_i18n.setText(this.food_group.getName_i18n());
	 
	    if (this.meal_group.getParent_id()>0)
	    {
		this.parent_food_group = m_da.tree_roots.get("2").m_groups_ht.get("" + this.food_group.getParentId());
		this.tf_parent.setText(this.parent_food_group.getName());
	    }
	    else
	    {
		this.parent_food_group = null;
		this.tf_parent.setText(ic.getMessage("ROOT"));
	    }
	    
	    createKeyWord();

	}
	else
	{
	    setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);
	    this.label_title.setText(ic.getMessage("EDIT_MEAL_GROUP"));
	    this.meal_group = (MealGroup)obj;

	    tf_name.setText(this.meal_group.getName());
	    tf_name_i18n.setText(this.meal_group.getName_i18n());
	    
	    if (this.meal_group.getParent_id()>0)
	    {
		this.parent_meal_group = m_da.tree_roots.get("3").m_meal_groups_ht.get("" + this.meal_group.getParent_id());
		this.tf_parent.setText(this.parent_meal_group.getName());
	    }
	    else
	    {
		this.parent_meal_group = null;
		this.tf_parent.setText(ic.getMessage("ROOT"));
	    }
	    
	    createKeyWord();
	    
	}
	
	
/*	
	this.label_title.setText(ic.getMessage("EDIT_FOOD_GROUP"));
	FoodGroup group = (FoodGroup)obj;

	label_name.setText(group.getName());
	label_name_i18n.setText(group.getName_i18n());
*/
    }


    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
	
	String command = e.getActionCommand();
	
	if (command.equals("select_group"))
	{
	    NutritionGroupDialog ngd = new NutritionGroupDialog(m_da, this.m_dialog.m_tree_type);
	    
	    if (ngd.wasAction())
	    {
		if (this.m_dialog.m_tree_type == GGCTreeRoot.TREE_USER_NUTRITION)
		{
		    FoodGroup fg = (FoodGroup)ngd.getSelectedObject();
		    
		    boolean rec = this.checkRecursiveGroup(this.food_group, fg.getId());
		    
		    if (rec)
		    {
			JOptionPane.showMessageDialog(this, ic.getMessage("CANT_SELECT_CHILD_OR_GROUP"), ic.getMessage("WARNING"),JOptionPane.WARNING_MESSAGE);
			return;
		    }
		    else
		    {
			this.parent_food_group = fg;
			this.tf_parent.setText(this.parent_food_group.getName());
		    }
		}
		else
		{
		    MealGroup fg = (MealGroup)ngd.getSelectedObject();

		    boolean rec = this.checkRecursiveGroup(this.meal_group, fg.getId());
		    
		    if (rec)
		    {
			JOptionPane.showMessageDialog(this, ic.getMessage("CANT_SELECT_CHILD_OR_GROUP"), ic.getMessage("WARNING"),JOptionPane.WARNING_MESSAGE);
			return;
		    }
		    else
		    {
			this.parent_meal_group = fg;
			this.tf_parent.setText(this.parent_meal_group.getName());
		    }
		}
		
		
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
	if (this.isAddAction())
	{
	    // add
	    return ic.getMessage("WISH_TO_SAVE_NEW_GROUP");
	}
	else
	{
	    // edit
	    return ic.getMessage("WISH_TO_SAVE_EDITED_GROUP");
	}
	
    }



    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    public boolean hasDataChanged()
    {
	
	if (this.was_saved)
	    return false;
	
	if (this.isAddAction())
	{
	    return true;
	}
	else
	{
	    if (this.m_dialog.m_tree_type== GGCTreeRoot.TREE_USER_NUTRITION)
	    {
		if ((this.getParentId() != this.food_group.getParentId()) ||
		    (!this.food_group.getName().equals(this.tf_name.getText())) ||
		    (!this.food_group.getName_i18n().equals(this.tf_name_i18n_key.getText())))
		    return true;
	    }
	    else
	    {
		if ((this.getParentId() != this.meal_group.getParent_id()) ||
		    (!this.meal_group.getName().equals(this.tf_name.getText())) ||
		    (!this.meal_group.getName_i18n().equals(this.tf_name_i18n_key.getText())))
		    return true;
	    }
	    
	    return false;
	}
    }



    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    public boolean saveData()
    {
	if (this.isAddAction())
	{
	    
	    if (this.m_dialog.m_tree_type== GGCTreeRoot.TREE_USER_NUTRITION)
	    {
		FoodGroup fg = new FoodGroup(2);

		fg.setParentId(getParentId());
		fg.setName(tf_name.getText().trim());
		fg.setName_i18n(this.tf_name_i18n_key.getText());
		
		this.m_da.getDb().add(fg);
		this.was_saved = true;
	
		this.addFoodGroup2Tree(fg);
		
	    }
	    else
	    {
		MealGroup fg = new MealGroup();
		
		fg.setParent_id(getParentId());
		fg.setName(tf_name.getText().trim());
		fg.setName_i18n(this.tf_name_i18n_key.getText());
		
		this.m_da.getDb().add(fg);
		this.was_saved = true;

		this.addMealGroup2Tree(fg);
		
	    }
	    
	    return true;
	}
	else
	{
	    if (this.m_dialog.m_tree_type== GGCTreeRoot.TREE_USER_NUTRITION)
	    {
		long prev_parent_id =this.food_group.getParentId();
		
		this.food_group.setParentId(getParentId());
		this.food_group.setName(tf_name.getText().trim());
		this.food_group.setName_i18n(this.tf_name_i18n_key.getText());

		this.m_da.getDb().edit(this.food_group);
		
		if (prev_parent_id != this.food_group.getParentId())
		{
		    this.removeFoodGroupFromTree(this.food_group, prev_parent_id);
		    this.addFoodGroup2Tree(this.food_group);
		}
		
	    }
	    else
	    {
		
		long prev_parent_id =this.meal_group.getParent_id();
		
		this.meal_group.setParent_id(getParentId());
		this.meal_group.setName(tf_name.getText().trim());
		this.meal_group.setName_i18n(this.tf_name_i18n_key.getText());

		this.m_da.getDb().edit(this.meal_group);
		
		if (prev_parent_id != this.meal_group.getParent_id())
		{
		    this.removeMealGroupFromTree(this.meal_group, prev_parent_id);
		    this.addMealGroup2Tree(this.meal_group);
		}
	    }
	    
	    return false;
	}
    }


    private void removeFoodGroupFromTree(FoodGroup fg, long prev_parent_id)
    {
	if (prev_parent_id==0)
	{
	    m_da.tree_roots.get("2").m_groups_tree.remove(fg);
	}
	else
	{
	    m_da.tree_roots.get("2").m_groups_ht.get("" + prev_parent_id).removeChild(fg);
	}
    }
    
    
    private void removeMealGroupFromTree(MealGroup fg, long prev_parent_id)
    {
	if (prev_parent_id==0)
	{
	    m_da.tree_roots.get("3").m_meal_groups_tree.remove(fg);
	}
	else
	{
	    m_da.tree_roots.get("3").m_meal_groups_ht.get("" + prev_parent_id).removeChild(fg);
	}
	
    }
    
    
    
    private void addFoodGroup2Tree(FoodGroup fg)
    {
	if (fg.getParentId()==0)
	{
	    m_da.tree_roots.get("2").m_groups_tree.add(fg);
	}
	else
	{
	    m_da.tree_roots.get("2").m_groups_ht.get("" + fg.getParentId()).addChild(fg);
	}
    }

    
    private void addMealGroup2Tree(MealGroup fg)
    {
	if (fg.getParent_id()==0)
	{
	    m_da.tree_roots.get("3").m_meal_groups_tree.add(fg);
	}
	else
	{
	    m_da.tree_roots.get("3").m_meal_groups_ht.get("" + fg.getParent_id()).addChild(fg);
	}
    }
    
    
    private long getParentId()
    {
	if (this.m_dialog.m_tree_type== GGCTreeRoot.TREE_USER_NUTRITION)
	{
	    if (this.parent_food_group==null)
		return 0L;
	    else
		return this.parent_food_group.getId();
	}
	else
	{
	    if (this.parent_meal_group==null)
		return 0L;
	    else
		return this.parent_meal_group.getId();
	}
	
	
    }

    
    

}
    
    

