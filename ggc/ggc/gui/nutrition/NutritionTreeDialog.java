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

package ggc.gui.nutrition;
 
import ggc.db.GGCDb;
import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.Meal;
import ggc.db.datalayer.MealGroup;
import ggc.gui.nutrition.panels.GGCTreePanel;
import ggc.gui.nutrition.panels.PanelNutritionFood;
import ggc.gui.nutrition.panels.PanelNutritionFoodEdit;
import ggc.gui.nutrition.panels.PanelNutritionFoodGroup;
import ggc.gui.nutrition.panels.PanelNutritionFoodGroupEdit;
import ggc.gui.nutrition.panels.PanelNutritionMain;
import ggc.gui.nutrition.panels.PanelNutritionMeal;
import ggc.gui.nutrition.panels.PanelNutritionMealEdit;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;



public class NutritionTreeDialog extends JDialog implements TreeSelectionListener, MouseListener, ActionListener
{

    private JPanel mainPane;
    private JTree tree;

    private DataAccess m_da = null;


//    private static boolean playWithLineStyle = false;
//    private static String lineStyle = "Horizontal";
    
//    private static boolean useSystemLookAndFeel = false;


    private I18nControl ic = null;
    public GGCTreePanel  panels[] = null;
    private int selectedPanel = 0;

    public int m_tree_type = 1;
    
    private Hashtable<String,MenuItem> menus; 
    boolean selector = false;
    Object selected_object = null;

    
    
    PopupMenu pop = new PopupMenu(); // the popup menu you want to use with the tree

    
    public NutritionTreeDialog(DataAccess da, int type) 
    {
	this(da, type, false);
    }

    
    public NutritionTreeDialog(DataAccess da, int type, boolean selector)
    {
        super(da.getParent(), "", true);

        this.selector = selector;
        this.m_da = da;
        this.ic = m_da.m_i18n;
        this.m_tree_type = type;

        init();
    }
    

    
    public void init()
    {
	
        //this.setResizable(false);
	if (selector)
	    this.setBounds(120, 90, 740, 560);
	else
	    this.setBounds(80, 50, 740, 560);

	
        setTitle();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));

        //Create a tree that allows one selection at a time.
        tree = new JTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setTreeModel(tree);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);
        tree.add(pop);


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
        initMenu();
 
        this.setVisible(true);
	
	
    }
    
    
    
    public int getType()
    {
	return this.m_tree_type;
    }
    

    public void setTitle()
    {
	if (this.getType()==1)
	    this.setTitle(ic.getMessage("USDA_NUTRITION_DATABASE"));
	else if (this.getType()==2)
	    this.setTitle(ic.getMessage("USER_NUTRITION_DATABASE"));
	else if (this.getType()==3)
	    this.setTitle(ic.getMessage("MEALS_DATABASE"));
    }

    public void setTreeModel(JTree tree)
    {
	//if ((this.m_tree_type==1) || (this.m_tree_type==2))
	    tree.setModel(new NutritionTreeModel(m_da.tree_roots.get("" + this.m_tree_type)));
	//else
	//    tree.setModel(new NutritionTreeModel(m_da.tree_roots.get("" + this.m_tree_type)));
	//else
	    
    }
    
    
    public void createPanels()
    {

	panels = new GGCTreePanel[3];
        //panels = new JPanel[9];

	//System.out.println("getType" + this.getType());
        
        if (this.getType()==1)
        {
            panels = new GGCTreePanel[3];
            panels[0] = new PanelNutritionMain(this);
            panels[1] = new PanelNutritionFoodGroup(this, this.getType());
            panels[2] = new PanelNutritionFood(this);
        }
        else if (this.getType()==2)
        {
            panels = new GGCTreePanel[5];
            panels[0] = new PanelNutritionMain(this);
            panels[1] = new PanelNutritionFoodGroup(this, this.getType());
            panels[2] = new PanelNutritionFood(this);
            panels[3] = new PanelNutritionFoodGroupEdit(this, this.getType());
            panels[4] = new PanelNutritionFoodEdit(this);
        }
        else if (this.getType()==3)
        {
            
            panels = new GGCTreePanel[5];
            panels[0] = new PanelNutritionMain(this);
            panels[1] = new PanelNutritionFoodGroup(this, this.getType());
            panels[2] = new PanelNutritionMeal(this);
            panels[3] = new PanelNutritionFoodGroupEdit(this, this.getType());
            panels[4] = new PanelNutritionMealEdit(this);
        }
            

        for(int i = 0; i < panels.length ; i++)
        {
            mainPane.add(panels[i]);
        }

        makePanelVisible(0);

    }

    public static final int PANEL_MAIN = 0;
    public static final int PANEL_GROUP = 1;
    public static final int PANEL_ITEM = 2;
    public static final int PANEL_GROUP_EDIT = 3;
    public static final int PANEL_ITEM_EDIT = 4;



    /** 
     * Makes selected panel visible
     */
    public void makePanelVisible(int num)
    {
        selectedPanel = num;

        for(int i = 0; i < panels.length; i++)
            if(i == num)
                panels[i].setVisible(true);
            else
                panels[i].setVisible(false);
    }

    
    private GGCTreePanel getSelectedPanel()
    {
	return panels[this.selectedPanel];
    }
    

    Object selected_last_path = null;
    //GGCTreePanel selected_last_panel = null;

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) 
    {
	
	//System.out.println("panel: " + this.getSelectedPanel());
	
	if ((this.getSelectedPanel().isPanelEditable()) &&
	    (this.getSelectedPanel().hasDataChanged()))
	{
	    this.getSelectedPanel().actionWarningAndExecute();
	    
	    setTreeModel(this.tree);
/*	    tree.cancelEditing();
	    tree.validate();
	    tree.repaint();
	    tree.revalidate();
	    tree.treeDidChange();*/
	}
	
	this.selected_last_path = tree.getLastSelectedPathComponent();
	this.displayPanel(NutritionTreeDialog.PANEL_VIEW);
	
    }

    public static int PANEL_VIEW = 0;
    public static int PANEL_EDIT = 1;
    public static int PANEL_ADD  = 2;
    public static int PANEL_ADD_ITEM  = 3;
    
    

    private void displayPanel(int special_action)
    {
	
	//System.out.println()
	
	if (this.selected_last_path instanceof GGCTreeRoot)
	{
	    
	    if (special_action == NutritionTreeDialog.PANEL_ADD)
	    {
		System.out.println("We are in root add)");
		
                makePanelVisible(NutritionTreeDialog.PANEL_GROUP_EDIT);
                this.panels[NutritionTreeDialog.PANEL_GROUP_EDIT].setParentRoot(this.selected_last_path);
		
	    }
	    else
	    {
		    this.selected_object = null;
		    makePanelVisible(NutritionTreeDialog.PANEL_MAIN);
		
	    }
	    
	    
	}
	else if ((this.selected_last_path instanceof FoodGroup) ||
		 (this.selected_last_path instanceof MealGroup))
	{
	    this.selected_object = null;
	    if (special_action == NutritionTreeDialog.PANEL_VIEW)
	    {
                makePanelVisible(NutritionTreeDialog.PANEL_GROUP);
                this.panels[NutritionTreeDialog.PANEL_GROUP].setData(this.selected_last_path);
	    }
	    else if (special_action == NutritionTreeDialog.PANEL_EDIT)
	    {
                makePanelVisible(NutritionTreeDialog.PANEL_GROUP_EDIT);
                this.panels[NutritionTreeDialog.PANEL_GROUP_EDIT].setData(this.selected_last_path);
		
		//System.out.println("FoodGroup Edit failed");
	    }
	    else if (special_action == NutritionTreeDialog.PANEL_ADD)
	    {
		//System.out.println("FoodGroup Add Group failed");
                makePanelVisible(NutritionTreeDialog.PANEL_GROUP_EDIT);
                this.panels[NutritionTreeDialog.PANEL_GROUP_EDIT].setParent(this.selected_last_path);
	    }
	    else
	    {
		//System.out.println("FoodGroup Add Item failed");
                makePanelVisible(NutritionTreeDialog.PANEL_ITEM_EDIT);
                this.panels[NutritionTreeDialog.PANEL_ITEM_EDIT].setParent(this.selected_last_path);
	    }
	}
	else if ((this.selected_last_path instanceof FoodDescription) ||
	         (this.selected_last_path instanceof Meal))
	{
	    selected_object = this.selected_last_path;
	    if (special_action == NutritionTreeDialog.PANEL_VIEW)
	    {
		makePanelVisible(NutritionTreeDialog.PANEL_ITEM);
		this.panels[NutritionTreeDialog.PANEL_ITEM].setData(this.selected_last_path);
	    }
	    else if (special_action == NutritionTreeDialog.PANEL_EDIT)
	    {
                makePanelVisible(NutritionTreeDialog.PANEL_ITEM_EDIT);
                this.panels[NutritionTreeDialog.PANEL_ITEM_EDIT].setData(this.selected_last_path);
	    }
/*	    else
	    {
		System.out.println("FoodDescription Add failed");
                makePanelVisible(NutritionTreeDialog.PANEL_ITEM_EDIT);
                this.panels[NutritionTreeDialog.PANEL_ITEM_EDIT].setParent(this.selected_last_path);
	    } */
	    
	}
	
    }
    
    
    
    public void mouseExited(MouseEvent me) {}

    public void mouseEntered(MouseEvent me) {}

    private Object mouse_selected_object = null;
    
    public void mouseReleased(MouseEvent me)
    {
	
	//System.out.println("Mouse released");
	
	if (me.isPopupTrigger()) // right click, show popup menu
	{
	    
	    
	    mouse_selected_object = tree.getClosestPathForLocation(me.getX(), me.getY()).getLastPathComponent();
	    tree.setSelectionPath(tree.getClosestPathForLocation(me.getX(), me.getY()));
	    
	    //System.out.println("MouseSelected: " + mouse_selected_object);
	    //System.out.println(me.getSource());
//	    mouse_selected_object = me.getSource();
	    createMenu();
		//System.out.println("Mouse: popUp trigger");
		//pop.add(new MenuItem("Test"));
	    pop.show(me.getComponent(), me.getX(), me.getY());
	}
	else
	{
		// System.out.println("Mouse: popUp NO trigger");
	    // 	put the image stuff here
	}
    }

    public void mousePressed(MouseEvent me)
    {}

    public void mouseClicked(MouseEvent me)
    {}     
    
    
    
    public void initMenu()
    {
	this.menus = new Hashtable<String,MenuItem>();
	
	String mens[];
	
	if (this.m_tree_type == 1)
	{
	    mens = new String[] {
		"view", "VIEW",
		"add_group", "ADD_GROUP",
		"close", "CLOSE"
	    };
	    
	    
	    
	}
	else if (this.m_tree_type==2)
	{
	    mens = new String[] {
			"view", "VIEW", 
			"close", "CLOSE",
			"edit_group", "EDIT_GROUP",
			"add_group", "ADD_GROUP",
			"add_item", "ADD_FOOD_DESCRIPTION",
			"edit_item", "EDIT_FOOD_DESCRIPTION"
		    };
	} 
	else 
	{
	    mens = new String[] {
			"view", "VIEW", 
			"close", "CLOSE",
			"edit_group", "EDIT_GROUP",
			"add_group", "ADD_GROUP",
			"add_item", "ADD_MEAL",
			"edit_item", "EDIT_MEAL"
		    };
	}

	for(int i=0; i<mens.length; i+=2)
	{
	    MenuItem men = new MenuItem();
	    men.setActionCommand(mens[i]);
	    men.setLabel(ic.getMessage("PM_NUT_" + mens[i+1]));
	    men.addActionListener(this);
	    
	    this.menus.put(mens[i], men);
	}
	
	
    }
    
    boolean made = false;
    int menu_prev_type = 0;
    
    public void createMenu()
    {
	if (this.m_tree_type==1)
	{
	    if (made)
	    	return;
	    
	    this.pop.add(menus.get("view"));
	    this.pop.addSeparator();
	    //this.pop.add(menus.get("add_group"));
	    //this.pop.addSeparator();
	    this.pop.add(menus.get("close"));
	    made = true;
	}
	else
	{
	    int curr_type = getTreeItemType();
	    
	    if (this.menu_prev_type == curr_type)
		return;
	    else
	    {
		if (curr_type == 1)
		{
		    pop.removeAll();
		    this.pop.add(menus.get("view"));
		    this.pop.addSeparator();
		    this.pop.add(menus.get("add_group"));
		    this.pop.addSeparator();
		    this.pop.add(menus.get("close"));
		    //made = true;
		    
		}
		else if (curr_type == 2)
		{
		    pop.removeAll();
		    
		    pop.add(menus.get("view"));
		    pop.addSeparator();

		    pop.add(menus.get("edit_group"));
		    pop.add(menus.get("add_group"));
		    pop.add(menus.get("add_item"));
		    pop.addSeparator();
		    
		    pop.add(menus.get("close"));
		    
		}
		else if (curr_type==3)
		{
		    pop.removeAll();
		    
		    pop.add(menus.get("view"));
		    pop.addSeparator();
		    pop.add(menus.get("edit_item"));
		    pop.addSeparator();
		    pop.add(menus.get("close"));
		}
		else
		{
		    pop.removeAll();
		    
		    pop.add(menus.get("view"));
		    pop.addSeparator();
		    pop.add(menus.get("close"));
		}
	    }
		
		
	    
	}
    }

    
    public int getTreeItemType()
    {
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
	
    }
    
    
    public boolean wasAction()
    {
	return (this.selected_object != null);
    }
    
    public Object getSelectedObject()
    {
	return this.selected_object;
    }
    
    public int getSelectedObjectType()
    {
	return this.m_tree_type;
    }
    
    
    public static void main(String args[])
    {
	DataAccess da = DataAccess.getInstance();
	
	GGCDb db = new GGCDb(da);
	db.initDb();
	
	da.setDb(db);
	
	//System.out.println("Load Nutrition #1");
	//db.loadNutritionDb1();

	/*
	System.out.println("Load Nutrition #2");
	db.loadNutritionDb2();
	*/
	//System.out.println("Load Meals");
	//db.loadMealsDb();
	
	
	//db.initDb();

	//DataAccess da = DataAccess.getInstance();

	
	//GGCDbLoader loader = new GGCDbLoader(da);
        //loader.start();
	
	
	//makeFakeData();
	
	
	/*JFrame fr = new JFrame();
	//fr.setBounds(0,0,640,480);
	//fr.setVisible(true);
	//da.setParent(fr); */
	//da.m_nutrition_treeroot = new GGCTreeRoot(1);

	/*NutritionTreeDialog ntd =*/ 
	new NutritionTreeDialog(da, GGCTreeRoot.TREE_MEALS);

    }


    public void actionPerformed(ActionEvent ae)
    {
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
	
	
	
    }
    
    
    
}
