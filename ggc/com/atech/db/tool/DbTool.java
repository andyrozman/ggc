package com.atech.db.tool;
 
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.GGCDb;
import ggc.nutrition.GGCTreeRoot;
import ggc.util.DataAccess;
import ggc.util.I18nControl;


public class DbTool extends JFrame implements TreeSelectionListener 
{
    private JPanel mainPane;
    private JTree tree;

    private DbToolAccess m_da = null;

    private static boolean DEBUG = false;

//    private static boolean playWithLineStyle = false;
//    private static String lineStyle = "Horizontal";
    
//    private static boolean useSystemLookAndFeel = false;


    private I18nControl ic = null;
    public JPanel  panels[] = null;
    private int selectedPanel = 0;


    static
    {
	try
	{
	    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	}
	catch(Exception ex)
	{
	}
    }



    public DbTool() 
    {

	super();

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

	m_da = DbToolAccess.createInstance(this);
	m_da.loadApplicationData();
        ic = I18nControl.getInstance();
	m_da.m_databases_treeroot.loadData();

	createGUI();

	loadAvailableDatabaseDefinitions();
        createPanels();
        makePanelVisible(0);
 
        this.setVisible(true);
        
    }


    public DbTool(DbToolApplicationInterface intr) 
    {

	super();

	m_da = DbToolAccess.createInstance(this);
	//m_da.loadApplicationData();
	ic = I18nControl.getInstance();
	m_da.m_databases_treeroot.loadData(intr);

	createGUI();

	loadAvailableDatabaseDefinitions();
	createPanels();
	makePanelVisible(1);

	this.setVisible(true);

    }


    public void createGUI()
    {

	this.setResizable(false);

        Toolkit theKit = this.getToolkit();
	Dimension wndSize = theKit.getScreenSize();

	int x, y; 
	x = wndSize.width/2 - 400;
	y = wndSize.height/2 - 300;

	this.setBounds(x, y, 800, 600);
	this.setTitle(ic.getMessage("DB_TOOL"));

	JPanel panel = new JPanel();
	panel.setLayout(new GridLayout(1,0));

	//Create a tree that allows one selection at a time.

	tree = new JTree();
	tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.setModel(new DbToolTreeModel(m_da.m_databases_treeroot));
	tree.setCellRenderer(new DbToolTreeCellRenderer());
/*
	tree.setCellRenderer(new TreeCellRenderer()
	    {
		/**
		 * Sets the value of the current tree cell to <code>value</code>.
		 * If <code>selected</code> is true, the cell will be drawn as if
		 * selected. If <code>expanded</code> is true the node is currently
		 * expanded and if <code>leaf</code> is true the node represets a
		 * leaf and if <code>hasFocus</code> is true the node currently has
		 * focus. <code>tree</code> is the <code>JTree</code> the receiver is being
		 * configured for.  Returns the <code>Component</code> that the renderer
		 * uses to draw the value.
		 *
		 * @return	the <code>Component</code> that the renderer uses to draw the value
		 */
/*		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
		    JLabel label = null;

		    if (value instanceof DatabaseSettings)
		    {
			DatabaseSettings ds = (DatabaseSettings)value;

			if (ds.isDefault)
			{
			    label = new JLabel(ds.toString());
			    label.setFont(DataAccess.getInstance().getFont(DbToolAccess.FONT_NORMAL_BOLD));
			}
			else
			{
			    label = new JLabel(ds.toString());
			    label.setFont(DataAccess.getInstance().getFont(DbToolAccess.FONT_NORMAL));
			}
			return label;
		    }
		    else
		    {
			label = new JLabel(value.toString());
			label.setFont(DataAccess.getInstance().getFont(DbToolAccess.FONT_NORMAL));
			return label;
		    }
		}
	    });
	    */
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
	splitPane.setDividerLocation(300); //XXX: ignored in some releases
					   //of Swing. bug 4101306
	splitPane.setPreferredSize(new Dimension(400, 300));
	panel.add(splitPane);
	this.add(panel);

    }


    public void loadAvailableDatabaseDefinitions()
    {
	DatabaseDefinitions dd = new DatabaseDefinitions();

	Hashtable table = dd.getTableOfAvailableDatabases();
	ArrayList list = dd.getListOfAvailableDatabases();

	//System.out.println(m_da);

	m_da.createAvailableDatabases(list.size());

	for (int i=0; i<list.size(); i++)
	{
	    String name = (String)list.get(i);

//	    System.out.println("name: " + name);

	    String id = (String)table.get(name);
	    int idn = Integer.parseInt(id);

	    DatabaseDefObject ds = new DatabaseDefObject(dd.getDatabaseName(idn),
						       dd.getJdbcDriver(idn),
						       dd.getJdbcURL(idn),
						       dd.getDatabasePort(idn),
						       dd.getHibernateDialect(idn),
						       dd.getHibernateDialectWithout(idn) );
	    m_da.addAvailableDatabase(i, ds);
	    m_da.m_tableOfDatabases.put(ds.name, ds);
	}

    }

    


    public void createPanels()
    {

	panels = new JPanel[3];

	panels[0] = new PanelRoot(this);
	panels[1] = new PanelDatabaseRoot(this);
        panels[2] = new PanelDatabaseSet(this);
//        panels[1] = new PanelNutritionFoodGroup(this);
//        panels[2] = new PanelNutritionFood(this);
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

    public static final int PANEL_ROOT = 0;
    public static final int PANEL_DATABASE_ROOT = 1;
    public static final int PANEL_DATABASE = 2;


    /**
     *  Windows Process Event
     *  Used for redefining windows command (Close)
     */
    protected void processWindowEvent(WindowEvent e) 
    {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            System.exit(0);
        }
    } 


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
	System.out.println("DbTool::valueChanged:: " + tree.getLastSelectedPathComponent());

	if (tree.getLastSelectedPathComponent() instanceof DbToolTreeRoot)
	{
	    DbToolTreeRoot r = (DbToolTreeRoot)tree.getLastSelectedPathComponent();
	    if (r.type==1)
	    {
		PanelDatabaseRoot pdr = (PanelDatabaseRoot)panels[1];
		pdr.setData(r.m_app); // (DbToolTreeRoot)tree.getLastSelectedPathComponent());
		makePanelVisible(DbTool.PANEL_DATABASE_ROOT);
	    }
	    else
                makePanelVisible(DbTool.PANEL_ROOT);
	}
	else if (tree.getLastSelectedPathComponent() instanceof DbToolApplicationInterface)
	{
	    PanelDatabaseRoot pdr = (PanelDatabaseRoot)panels[1];
	    pdr.setData((DbToolApplicationInterface)tree.getLastSelectedPathComponent());
	    makePanelVisible(DbTool.PANEL_DATABASE_ROOT);
	}
	else if (tree.getLastSelectedPathComponent() instanceof DatabaseSettings)
	{
	    PanelDatabaseSet pd = (PanelDatabaseSet)panels[2];
	    pd.setData((DatabaseSettings)tree.getLastSelectedPathComponent());
	    makePanelVisible(DbTool.PANEL_DATABASE);
	}

    }

    public static void main(String args[])
    {
	DbTool tool = new DbTool();
    }


}
