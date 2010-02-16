package ggc.nutri.dialogs;
 
import ggc.nutri.data.GGCTreeRoot;
import ggc.nutri.data.NutritionGroupModel;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     NutritionGroupDialog
 *  Description:  Nutrition Group Dialog
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class NutritionGroupDialog extends JDialog implements TreeSelectionListener, ActionListener, HelpCapable
{

    private static final long serialVersionUID = 3610528674361903899L;
    private DataAccessNutri m_da = null;
    private I18nControlAbstract ic = null;

    private JTree tree;
    JTextField tf_selected;
    JButton help_button = null;
    Object action_object = null;

    /**
     * Tree Type
     */
    public int m_tree_type = 1;
    
    /**
     * Group: Foods
     */
    public static int GROUP_FOODS = 2;
    
    /**
     * Group: Meals
     */
    public static int GROUP_MEALS = 3;
    
    
    
    /**
     * Constructor
     * 
     * @param parent 
     * @param type
     */
    public NutritionGroupDialog(JDialog parent, int type) 
    {

        super(parent, "", true);
        //super((JDialog)null, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();
        this.m_tree_type = type;

        //this.setResizable(false);
        this.setBounds(160, 100, 300, 350);
        setTitle();

        init();
 
        this.setVisible(true);
        
    }


    /**
     * Constructor
     * 
     * @param parent 
     * @param type
     */
    public NutritionGroupDialog(JFrame parent, int type) 
    {

        super(parent, "", true);
        //super((JDialog)null, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();
        this.m_tree_type = type;

        //this.setResizable(false);
        this.setBounds(160, 100, 300, 350);
        setTitle();

        init();
 
        this.setVisible(true);
        
    }
    
    
    private void init()
    {
        ATSwingUtils.initLibrary();
        
        this.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 300, 150);
        
        JLabel label = new JLabel(ic.getMessage("SELECTED_GROUP"));
        label.setBounds(30, 70, 120, 25);
        panel.add(label, null);
        
        
        tf_selected = new JTextField();
        tf_selected.setBounds(30, 100, 230, 25);
        tf_selected.setEditable(false);
        panel.add(tf_selected);


        ATSwingUtils.getButton("   " + ic.getMessage("OK"), 150, 10, 110, 25, panel, 
            ATSwingUtils.FONT_NORMAL, "ok.png", "select", this, 
            m_da);
        
        ATSwingUtils.getButton("   " + ic.getMessage("CANCEL"), 150, 40, 110, 25, panel, 
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, 
            m_da);
        

        help_button = m_da.createHelpButtonByBounds(150, 70, 110, 25, panel, ATSwingUtils.FONT_NORMAL);
        panel.add(help_button);
        
        
       /* 
        JButton button = new JButton(ic.getMessage("OK"));
        button.setActionCommand("select");
        button.setBounds(180, 30, 80, 25);
        button.addActionListener(this);
        panel.add(button);
        
        button = new JButton(ic.getMessage("CANCEL"));
        button.setActionCommand("close");
        button.setBounds(180, 60, 80, 25);
        button.addActionListener(this);
        panel.add(button);
        */		
        //Create a tree that allows one selection at a time.
        tree = new JTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setTreeModel(tree);
        //tree.setModel(new NutritionTreeModel(m_da.m_nutrition_treeroot));
        tree.addTreeSelectionListener(this);

        JScrollPane treeView = new JScrollPane(tree);
        treeView.setBounds(30, 150, 230, 140);

        m_da.enableHelp(this);
        
        this.add(panel, null);
        this.add(treeView, null);
	
    }
    
    
    
    
    /**
     * Get Type
     * @return
     */
    public int getType()
    {
        return this.m_tree_type;
    }
    

    private void setTitle()
    {
    	if (this.getType()==2)
    	    this.setTitle(ic.getMessage("FOOD_GROUPS"));
    	else if (this.getType()==3)
    	    this.setTitle(ic.getMessage("MEAL_GROUPS"));
    }

    private void setTreeModel(JTree tree)
    {
        tree.setModel(new NutritionGroupModel(m_da.getDbCache().tree_roots.get("" + this.m_tree_type), this.m_tree_type));
    }
    
    

    Object selected_last_path = null;

    /** 
     * Required by TreeSelectionListener interface. 
     */
    public void valueChanged(TreeSelectionEvent e) 
    {
    	this.selected_last_path = tree.getLastSelectedPathComponent();
    	this.tf_selected.setText(this.selected_last_path.toString());
    }

    
    /**
     * Was Action
     * 
     * @return
     */
    public boolean wasAction()
    {
        return (this.action_object!=null);
    }
    
    /**
     * Get Selected Object
     * 
     * @return
     */
    public Object getSelectedObject()
    {
        return this.action_object;
    }
    

    /**
     * Action Performed
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
    	String cmd = e.getActionCommand();
    	
    	if (cmd.equals("select"))
    	{
    	    if ((this.selected_last_path == null) || (this.selected_last_path instanceof GGCTreeRoot))
    		this.action_object = null;
    	    else
    		this.action_object = this.selected_last_path;
    	    
    	    this.dispose();
    	}
    	else if (cmd.equals("close"))
    	{
    	    this.dispose();
    	}
    }


    public Component getComponent()
    {
        return this;
    }


    public JButton getHelpButton()
    {
        return this.help_button;
    }


    public String getHelpId()
    {
        if (this.getType()==NutritionGroupDialog.GROUP_FOODS)
            return "GGC_Food_User_Group_View";
        else if (this.getType()==NutritionGroupDialog.GROUP_MEALS)
            return "GGC_Food_Meal_Group_View"; 
        else
            return null;
    }
    
}