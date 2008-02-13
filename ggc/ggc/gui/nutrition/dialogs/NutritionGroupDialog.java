/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: NutritionTreeDialog
 *  Purpose:  Main class for displaying nutrition information.
 *
 *  Author:   andyrozman
 */

package ggc.gui.nutrition.dialogs;
 
import ggc.gui.nutrition.data.NutritionGroupModel;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;



public class NutritionGroupDialog extends JDialog implements TreeSelectionListener, ActionListener
{

    private JPanel mainPane;
    private JTree tree;
    
    JTextField tf_selected;

    private DataAccess m_da = null;


//    private static boolean playWithLineStyle = false;
//    private static String lineStyle = "Horizontal";
    
//    private static boolean useSystemLookAndFeel = false;

    private I18nControl ic = null;
    //public GGCTreePanel  panels[] = null;
// x   private int selectedPanel = 0;

    public int m_tree_type = 1;
    
    public static int GROUP_FOODS = 2;
    public static int GROUP_MEALS = 3;
    
    //boolean action_done = false;
    Object action_object = null;
    
    
    public NutritionGroupDialog(DataAccess da, int type) 
    {

        super(da.getParent(), "", true);
	//super((JDialog)null, "", true);

        System.out.println("type:" + type);
        
        m_da = da;
        ic = m_da.m_i18n;
        this.m_tree_type = type;

        //this.setResizable(false);
        this.setBounds(160, 100, 300, 350);
        setTitle();

        //this.setLayout(new BorderLayout());

        /*
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));
        */
        //this.pop.s
        //add(pop);


        init();
 
        this.setVisible(true);
        
    }

    
    public void init()
    {
	
	this.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 300, 150);
        
        JLabel label = new JLabel(ic.getMessage("SELECTED_GROUP"));
        label.setBounds(30, 30, 120, 25);
        panel.add(label, null);
        
        
        tf_selected = new JTextField();
        tf_selected.setBounds(30, 60, 200, 25);
        panel.add(tf_selected);
        
        JButton button = new JButton(ic.getMessage("SELECT"));
        button.setActionCommand("select");
        button.setBounds(230, 30, 70, 25);
        button.addActionListener(this);
        panel.add(button);
        
        button = new JButton(ic.getMessage("CLOSE"));
        button.setActionCommand("close");
        button.setBounds(230, 60, 70, 25);
        button.addActionListener(this);
        panel.add(button);
        		
        
        
        
        

        //Create a tree that allows one selection at a time.
        tree = new JTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setTreeModel(tree);
        //tree.setModel(new NutritionTreeModel(m_da.m_nutrition_treeroot));
        tree.addTreeSelectionListener(this);


        JScrollPane treeView = new JScrollPane(tree);
        treeView.setBounds(30, 150, 200, 100);

        /*
        mainPane = new JPanel();
        mainPane.setLayout(null);
        */

        this.add(panel, null);
        this.add(treeView, null);
	
	
    }
    
    
    
    
    public int getType()
    {
	return this.m_tree_type;
    }
    

    public void setTitle()
    {
	if (this.getType()==2)
	    this.setTitle(ic.getMessage("FOOD_GROUPS"));
	else if (this.getType()==3)
	    this.setTitle(ic.getMessage("MEAL_GROUPS"));
    }

    public void setTreeModel(JTree tree)
    {
	tree.setModel(new NutritionGroupModel(m_da.tree_roots.get("" + this.m_tree_type), this.m_tree_type));
    }
    
    



    Object selected_last_path = null;

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) 
    {

	this.selected_last_path = tree.getLastSelectedPathComponent();

	//this.displayPanel(NutritionTreeDialog.PANEL_VIEW);
	
	/*
	if (tree.getLastSelectedPathComponent() instanceof GGCTreeRoot)
	{
	    makePanelVisible(NutritionTreeDialog.PANEL_MAIN);
	    //System.out.println("NutritionTreeDialog::valueChanged:: NOT IMPLEMENTED");
	    //Diocese dio = (Diocese)tree.getLastSelectedPathComponent();
	    //((ViewDiocesePanel)panels[DioceseCfgDialog.PANEL_VIEW_DIOCESE]).setData((DioceseH)dio); //node2.getObject());
	}
	else if (tree.getLastSelectedPathComponent() instanceof FoodGroup)
	{
	    makePanelVisible(NutritionTreeDialog.PANEL_FOODGROUP);
	    this.panels[NutritionTreeDialog.PANEL_FOODGROUP].setData(tree.getLastSelectedPathComponent());
	}
	else if (tree.getLastSelectedPathComponent() instanceof FoodDescription)
	{
	    makePanelVisible(NutritionTreeDialog.PANEL_FOOD);
	    this.panels[NutritionTreeDialog.PANEL_FOOD].setData(tree.getLastSelectedPathComponent());
	}
*/

/*
        if (panels[selectedPanel] instanceof EditablePanel)
        {
            EditablePanel p = (EditablePanel)panels[selectedPanel];
            if (p.hasDataChanged())
            {
                if (!p.saveData())
                    return;
            }
        }


        if (tree.getLastSelectedPathComponent() instanceof Diocese)
        {
            makePanelVisible(DioceseCfgDialog.PANEL_VIEW_DIOCESE);
            Diocese dio = (Diocese)tree.getLastSelectedPathComponent();
            ((ViewDiocesePanel)panels[DioceseCfgDialog.PANEL_VIEW_DIOCESE]).setData((DioceseH)dio); //node2.getObject());
        }
        else if (tree.getLastSelectedPathComponent() instanceof Parish)
        {
            makePanelVisible(DioceseCfgDialog.PANEL_VIEW_PARISH);
            Parish pi = (Parish)tree.getLastSelectedPathComponent();
            ((ViewParishPanel)panels[DioceseCfgDialog.PANEL_VIEW_PARISH]).setData((ParishH)pi);
        }
        else if (tree.getLastSelectedPathComponent() instanceof ParishPerson)
        {
            ParishPerson pp = (ParishPerson)tree.getLastSelectedPathComponent();
            makePanelVisible(DioceseCfgDialog.PANEL_VIEW_PARISH_PERSONAL);
            ((ViewParishPersonalPanel)panels[DioceseCfgDialog.PANEL_VIEW_PARISH_PERSONAL]).setData((ParishH)pp.getParish());
        }
        else if (tree.getLastSelectedPathComponent() instanceof DiocesePerson)
        {
            DiocesePerson pp = (DiocesePerson)tree.getLastSelectedPathComponent();
            makePanelVisible(DioceseCfgDialog.PANEL_VIEW_DIOCESE_PERSONAL);
            ((ViewDiocesePanel)panels[DioceseCfgDialog.PANEL_VIEW_DIOCESE_PERSONAL]).setData((DioceseH)pp.getDiocese());
        } 
        else
        {
            System.out.println("DioceseCfgDialog::valueChanged::UnknownAction");
        }
*/
    }

    
    public boolean wasAction()
    {
	return (this.action_object!=null);
    }
    

    public void actionPerformed(ActionEvent e)
    {
	String cmd = e.getActionCommand();
	
	if (cmd.equals("select"))
	{
	    this.action_object = this.selected_last_path;
	    this.dispose();
	}
	else if (cmd.equals("close"))
	{
	    this.dispose();
	}
	    
	
	
	
    }

    


    
    
    
    

    
    
    
}
