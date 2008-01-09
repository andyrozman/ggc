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
 
import java.awt.Dimension;
import java.awt.GridLayout;
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.Iterator;
//import java.util.logging.Level;

import javax.swing.JDialog;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
//import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
//import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import ggc.db.GGCDb;
import ggc.gui.nutrition.GGCTreeRoot;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;



public class NutritionTreeDialog extends JDialog implements TreeSelectionListener 
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



    public NutritionTreeDialog(DataAccess da) 
    {

        super(da.getParent(), "", true);
	//super((JDialog)null, "", true);

        m_da = da;
        ic = m_da.m_i18n;

        //this.setResizable(false);
        this.setBounds(80, 50, 740, 560);
        this.setTitle(ic.getMessage("USDA_NUTRITION_DATABASE"));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));


        //Create a tree that allows one selection at a time.
        tree = new JTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setModel(new NutritionTreeModel(m_da.m_nutrition_treeroot));
        tree.addTreeSelectionListener(this);

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



    public void createPanels()
    {

	panels = new GGCTreePanel[3];
        //panels = new JPanel[9];

        panels[0] = new PanelNutritionMain(this);
        panels[1] = new PanelNutritionFoodGroup(this);
        panels[2] = new PanelNutritionFood(this);
/*        panels[3] = new ViewDiocesePanel(this);
        panels[4] = new ViewParishPanel(this);
        panels[5] = new ViewDiocesePersonalPanel(this);
        panels[6] = new ViewParishPersonalPanel(this);
        panels[7] = new DiocesePersonalPanel(this);
        panels[8] = new ParishPersonalPanel(this); */

        for(int i = 0; i < panels.length ; i++)
        {
            mainPane.add(panels[i]);
        }

        makePanelVisible(0);

    }

    public static final int PANEL_MAIN = 0;
    public static final int PANEL_FOODGROUP = 1;
    public static final int PANEL_FOOD = 2;



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



    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) 
    {

	if (tree.getLastSelectedPathComponent() instanceof GGCTreeRoot)
	{
	    makePanelVisible(NutritionTreeDialog.PANEL_MAIN);
	    System.out.println("NutritionTreeDialog::valueChanged:: NOT IMPLEMENTED");
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

    public static void main(String args[])
    {
	GGCDb db = new GGCDb();
	db.initDb();

	DataAccess da = DataAccess.getInstance();

	/*JFrame fr = new JFrame();
	//fr.setBounds(0,0,640,480);
	//fr.setVisible(true);
	//da.setParent(fr); */
	da.m_nutrition_treeroot = new GGCTreeRoot(1);

	/*NutritionTreeDialog ntd =*/ 
	new NutritionTreeDialog(da);

    }


}
