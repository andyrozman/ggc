package ggc.plugin.list;

import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Dimension;
import java.awt.GridLayout;

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

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     BaseListDialog  
 *  Description:  Base List Dialog
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class BaseListDialog extends JDialog implements TreeSelectionListener // ,
                                                                             // ActionListener
{

    private static final long serialVersionUID = -331463474036415168L;
    private JPanel mainPane;
    private JTree tree;

    protected DataAccessPlugInBase m_da = null;
    private I18nControlAbstract ic = null;
    protected BaseListAbstractPanel panels[] = null;
    BaseListRoot m_root = null;

    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public BaseListDialog(JFrame parent, DataAccessPlugInBase da)
    {

        super(parent, "", true);

        m_da = da;
        ic = m_da.getI18nControlInstance();

        this.setTitle();

        m_da.addComponent(this);

        m_root = new BaseListRoot(m_da);

        // this.setResizable(false);
        this.setBounds(80, 50, 710, 523);
        setTitle();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));

        m_da.centerJDialog(this, parent);

        // this.pop.s
        // add(pop);

        // Create a tree that allows one selection at a time.
        tree = new JTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setTreeModel(tree);
        // tree.setModel(new NutritionTreeModel(dataAccess.m_nutrition_treeroot));
        tree.addTreeSelectionListener(this);
        tree.setCellRenderer(new BaseListRenderer());
        // tree.addMouseListener(this);

        JScrollPane treeView = new JScrollPane(tree);

        mainPane = new JPanel();
        mainPane.setLayout(null);

        // Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT/*
                                                                          * .
                                                                          * VERTICAL_SPLIT
                                                                          */);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(mainPane);

        Dimension minimumSize = new Dimension(100, 50);
        mainPane.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(200); // ignored in some releases of Swing.
                                           // bug 4101306
        splitPane.setPreferredSize(new Dimension(500, 300));

        panel.add(splitPane);

        createPanels();
        this.add(panel);
        makePanelVisible(0);

        this.setResizable(false);
        this.setVisible(true);

    }

    private void setTitle()
    {
        this.setTitle(String.format(ic.getMessage("DEVICE_LIST_WEB"), ic.getMessage("DEVICE_NAME_BIG")));
    }

    /**
     * Set Tree Model
     * 
     * @param tree
     */
    public void setTreeModel(JTree tree)
    {
        tree.setModel(new BaseListModel(this.m_root));
    }

    /**
     * Create Panels
     */
    public void createPanels()
    {
        panels = new BaseListAbstractPanel[2];

        panels[0] = new BaseListMainPanel(this);
        panels[1] = new BaseListBrowserPanel(this);
        panels[1].setSize(500, 495); // 500

        for (BaseListAbstractPanel panel : panels)
        {
            mainPane.add(panel);
        }

        makePanelVisible(0);

    }

    /**
     * Panel: Main
     */
    public static final int PANEL_MAIN = 0;

    /**
     * Panel: Browser
     */
    public static final int PANEL_BROWSER = 1;

    /** 
     * Makes selected panel visible
     * 
     * @param num 
     */
    public void makePanelVisible(int num)
    {
        // x selectedPanel = num;

        for (int i = 0; i < panels.length; i++)
            if (i == num)
            {
                panels[i].setVisible(true);
            }
            else
            {
                panels[i].setVisible(false);
            }
    }

    Object selected_last_path = null;

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e)
    {

        this.selected_last_path = tree.getLastSelectedPathComponent();

        // this.displayPanel(1);

        if (this.selected_last_path instanceof BaseListRoot)
        {
            makePanelVisible(BaseListDialog.PANEL_MAIN);
        }
        else if (this.selected_last_path instanceof BaseListEntry)
        {
            // else if (this.selected_last_path instanceof String)
            makePanelVisible(BaseListDialog.PANEL_BROWSER);
            this.panels[BaseListDialog.PANEL_BROWSER].setData(this.selected_last_path);
        }
    }

    /**
     * Dispose
     * 
     * @see java.awt.Window#dispose()
     */
    @Override
    public void dispose()
    {
        this.m_da.removeComponent(this);
        super.dispose();
    }

    boolean made = false;
    int menu_prev_type = 0;

    /**
     * Get Tree Item Type
     * 
     * @return
     */
    public int getTreeItemType()
    {
        return 0;
    }

}
