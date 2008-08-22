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

package ggc.pump.list;
 
import ggc.pump.util.DataAccessPump;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import com.atech.i18n.I18nControlAbstract;



public class PumpListDialog extends JDialog implements TreeSelectionListener, ActionListener
{
	static final long serialVersionUID = 0L;


    private JPanel mainPane;
    private JTree tree;

    private DataAccessPump m_da = null;


//    private static boolean playWithLineStyle = false;
//    private static String lineStyle = "Horizontal";
    
//    private static boolean useSystemLookAndFeel = false;


    private I18nControlAbstract ic = null;
    public PumpListAbstractPanel  panels[] = null;
// x   private int selectedPanel = 0;

//    addMouseListener(this);
//    add(pop);
    
    PumpListRoot m_root = new PumpListRoot();
    
    
    public PumpListDialog(DataAccessPump da) 
    {

        super(new JFrame(), "", true);
	//super((JDialog)null, "", true);

        m_da = da;
        ic = m_da.getI18nControlInstance();

        //this.setResizable(false);
        this.setBounds(80, 50, 800, 600);
        setTitle();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));
        
        //this.pop.s
        //add(pop);


        //Create a tree that allows one selection at a time.
        tree = new JTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setTreeModel(tree);
        //tree.setModel(new NutritionTreeModel(m_da.m_nutrition_treeroot));
        tree.addTreeSelectionListener(this);
        //tree.addMouseListener(this);


        JScrollPane treeView = new JScrollPane(tree);

        mainPane = new JPanel();
        mainPane.setLayout(null);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT/*.VERTICAL_SPLIT*/);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(mainPane);

        Dimension minimumSize = new Dimension(100, 50);
        mainPane.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(200); //XXX: ignored in some releases
                                           //of Swing. bug 4101306
        splitPane.setPreferredSize(new Dimension(500, 300));

        panel.add(splitPane);

        createPanels();
        this.add(panel);
        makePanelVisible(0);
 
        this.setVisible(true);
        
    }

    

    public void setTitle()
    {
	    this.setTitle(ic.getMessage("METERS_LIST"));
    }

    public void setTreeModel(JTree tree)
    {
	    tree.setModel(new PumpListModel(this.m_root));
    }
    
    
    public void createPanels()
    {
    	panels = new PumpListAbstractPanel[2];

        panels[0] = new PumpListMainPanel(this);
        panels[1] = new PumpListCompanyPanel(this);

        for(int i = 0; i < panels.length ; i++)
        {
            mainPane.add(panels[i]);
        }

        makePanelVisible(0);

    }

    public static final int PANEL_MAIN = 0;
    public static final int PANEL_METER_COMPANY = 1;



    /** 
     * Makes selected panel visible
     */
    public void makePanelVisible(int num)
    {
//x        selectedPanel = num;

        for(int i = 0; i < panels.length; i++)
            if(i == num)
                panels[i].setVisible(true);
            else
                panels[i].setVisible(false);
    }


    Object selected_last_path = null;

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) 
    {

    	this.selected_last_path = tree.getLastSelectedPathComponent();

    	//this.displayPanel(1);
    	
    	if (this.selected_last_path instanceof PumpListRoot)
    	{
    		makePanelVisible(PumpListDialog.PANEL_MAIN);
    	}
    	else if (this.selected_last_path instanceof String)
    	{
    	    makePanelVisible(PumpListDialog.PANEL_METER_COMPANY);
    	    this.panels[PumpListDialog.PANEL_METER_COMPANY].setData(this.selected_last_path);
    	}
	
    	
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

    public static int PANEL_VIEW = 0;
    public static int PANEL_EDIT = 1;
    public static int PANEL_ADD  = 2;
    public static int PANEL_ADD_ITEM  = 3;
    
    

    private void displayPanel(int special_action)
    {
	
    	/*
	//System.out.println()
	
	if (this.selected_last_path instanceof GGCTreeRoot)
	{
	    makePanelVisible(NutritionTreeDialog.PANEL_MAIN);
	}
	else if (this.selected_last_path instanceof FoodGroup)
	{
	    if (special_action == NutritionTreeDialog.PANEL_VIEW)
	    {
                makePanelVisible(NutritionTreeDialog.PANEL_FOODGROUP);
                this.panels[NutritionTreeDialog.PANEL_FOODGROUP].setData(this.selected_last_path);
	    }
	    else if (special_action == NutritionTreeDialog.PANEL_EDIT)
	    {
                makePanelVisible(NutritionTreeDialog.PANEL_FOODGROUP_EDIT);
                this.panels[NutritionTreeDialog.PANEL_FOODGROUP_EDIT].setData(this.selected_last_path);
		
		//System.out.println("FoodGroup Edit failed");
	    }
	    else if (special_action == NutritionTreeDialog.PANEL_ADD)
	    {
		//System.out.println("FoodGroup Add Group failed");
                makePanelVisible(NutritionTreeDialog.PANEL_FOODGROUP_EDIT);
                this.panels[NutritionTreeDialog.PANEL_FOODGROUP_EDIT].setParent(this.selected_last_path);
	    }
	    else
	    {
		//System.out.println("FoodGroup Add Item failed");
                makePanelVisible(NutritionTreeDialog.PANEL_FOOD_EDIT);
                this.panels[NutritionTreeDialog.PANEL_FOOD_EDIT].setParent(this.selected_last_path);
	    }
	}
	else if (this.selected_last_path instanceof FoodDescription)
	{
	    if (special_action == NutritionTreeDialog.PANEL_VIEW)
	    {
		makePanelVisible(NutritionTreeDialog.PANEL_FOOD);
		this.panels[NutritionTreeDialog.PANEL_FOOD].setData(this.selected_last_path);
	    }
	    else if (special_action == NutritionTreeDialog.PANEL_EDIT)
	    {
		System.out.println("FoodDescription Edit failed");
	    }
	    else
	    {
		System.out.println("FoodDescription Add failed");
	    }
	    
	}
	*/
    }
    
    
    
    
    
    
    
    boolean made = false;
    int menu_prev_type = 0;
    

    
    public int getTreeItemType()
    {
/*    	
	if (this.mouse_selected_object instanceof GGCTreeRoot)
	{
	    return 1;
	}
	else
	{
	    if (this.m_tree_type==2)
	    {
		if (this.mouse_selected_object instanceof FoodGroup)
		    return 2;
		else
		    return 3;
	    }
	    else if (this.m_tree_type==3)
	    {
		if (this.mouse_selected_object instanceof MealGroup)
		    return 2;
		else
		    return 3;
		
	    }
	    else
            {
                System.out.println("Error on mouse click: Wrong type:" + this.mouse_selected_object);
                return -1;
            }
	    
	}
	//else
	*/
    	
    	return 0;
    	
    }
    
    
    
    public static void main(String args[])
    {
        DataAccessPump da = DataAccessPump.getInstance();
	
	

	/*NutritionTreeDialog ntd =*/ 
	new PumpListDialog(da);

    }


    public void actionPerformed(ActionEvent ae)
    {
    	/*
	// TODO Auto-generated method stub
	//System.out.println("Action performed, NOT handled");
	
	String command = ae.getActionCommand();
	
	if (command.equals("close"))
	{
	}
	else if (command.equals("view"))
	{
	    this.selected_last_path = this.mouse_selected_object;
	    this.displayPanel(NutritionTreeDialog.PANEL_VIEW);
	}
	else if (command.equals("add_item"))
	{
	    this.selected_last_path = this.mouse_selected_object;
	    this.displayPanel(NutritionTreeDialog.PANEL_ADD_ITEM);
	}
	else if (command.equals("add_group"))
	{
	    this.selected_last_path = this.mouse_selected_object;
	    this.displayPanel(NutritionTreeDialog.PANEL_ADD);
	}
	else if ((command.equals("edit_item")) || 
	         (command.equals("edit_group")))
	{
	    this.selected_last_path = this.mouse_selected_object;
	    this.displayPanel(NutritionTreeDialog.PANEL_EDIT);
	}
	
	*/
	
    }
    
    
    
}
