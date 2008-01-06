/*
 * Created by IntelliJ IDEA.
 * User: schultd
 * Date: 11.07.2002
 * Time: 16:18:55
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */

package ggc.gui.dialogs;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


import ggc.data.cfg.ConfigCellRenderer;
import ggc.gui.panels.prefs.*;

import ggc.util.I18nControl;
import ggc.util.DataAccess;


public class PropertiesDialog extends JDialog implements ListSelectionListener, ActionListener
{

    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccess m_da = DataAccess.getInstance();

    private DefaultMutableTreeNode prefNode;
    //private JTree prefTree;
    private JList list = null;
    private JPanel prefPane;
    //private DefaultTreeModel prefTreeModel;
    //private JScrollPane prefTreePane;
    //private AbstractPrefOptionsPanel prefOptionsPane;

    public ArrayList<JPanel> panels = null;
    public Hashtable<String, String> panel_id = null;

    int current_index = 0;

    boolean ok_action = false;


    public String config_types[] = { 
	m_ic.getMessage("GENERAL"),
	m_ic.getMessage("MEDICAL_DATA"),
	m_ic.getMessage("COLORS_AND_FONTS"),
	m_ic.getMessage("RENDERING_QUALITY"),
	m_ic.getMessage("METER_CONFIGURATION"),
	m_ic.getMessage("PRINTING")
    };



    public PropertiesDialog(JFrame parent)
    {
        super(parent, "", true);

        Rectangle rec = parent.getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-320, y-240, 640, 480);
        setTitle(m_ic.getMessage("PREFERENCES"));

        createPanels();
        init();
        this.setResizable(false);
        this.setVisible(true);
    }


    private void init()
    {
        Dimension dim = new Dimension(80, 20);

        //createNodes();

	/*
 private JTree prefTree;
 private DefaultTreeModel prefTreeModel;
     private JScrollPane prefTreePane;

	*/

	// Configuration icons
/*	public ImageIcon config_icons[] = {
	    new ImageIcon("images/cfg_db.gif"), 
	    new ImageIcon("images/cfg_look.gif"), 
	    new ImageIcon("images/cfg_myparish.gif"), 
	    new ImageIcon("images/cfg_masses.gif"), 
	    new ImageIcon("images/cfg_users.gif"), 
	    new ImageIcon("images/cfg_lang.gif"), 
	    new ImageIcon("images/cfg_web.gif"), 
	    null
	};

    public String config_types[] = { 
	m_ic.getMessage("GENERAL"),
	m_ic.getMessage("MEDICAL_DATA"),
	m_ic.getMessage("COLORS_AND_FONTS"),
	m_ic.getMessage("RENDERING_QUALITY"),
	m_ic.getMessage("METER_CONFIGURATION"),
	m_ic.getMessage("PRINTING")
    };

*/
	list = new JList(config_types); //m_da.config_types);
	list.addListSelectionListener(this);
	ConfigCellRenderer renderer = new ConfigCellRenderer();
	renderer.setPreferredSize(new Dimension(100, 75));
	list.setCellRenderer(renderer);




	/*
        DefaultTreeModel prefTreeModel = new DefaultTreeModel(prefNode);

        prefTree = new JTree(prefTreeModel);
        prefTree.putClientProperty("JTree.lineStyle", "Angled");
        prefTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent e)
            {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)prefTree.getLastSelectedPathComponent();

		if (selectedNode == null)
		    return;

		selectPanel(selectedNode.toString());
            }
        });

        JScrollPane prefTreePane = new JScrollPane(prefTree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	*/

	JScrollPane prefTreePane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	prefPane = new JPanel(new BorderLayout());
	

        //set the buttons up...
        JButton okButton = new JButton(m_ic.getMessage("OK"));
        okButton.setPreferredSize(dim);
        okButton.setActionCommand("ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton(m_ic.getMessage("CANCEL"));
        cancelButton.setPreferredSize(dim);
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        JButton applyButton = new JButton(m_ic.getMessage("APPLY"));
        applyButton.setPreferredSize(dim);
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);

        //...and align them in a row at the buttom.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        prefPane.add(buttonPanel, BorderLayout.SOUTH);
        prefPane.add(panels.get(0), BorderLayout.CENTER);

        getContentPane().add(prefTreePane, BorderLayout.WEST);
	//getContentPane().add(list, BorderLayout.WEST);
        getContentPane().add(prefPane, BorderLayout.CENTER);
    }



    // ---
    // ---   NODES and PANELS
    // ---   (if you add node and panel for configuration options, please change 
    // ---    following methods and add values)
    // ---

/*
    public void createNodes()
    {
        prefNode = new DefaultMutableTreeNode(m_ic.getMessage("PREFERENCES"));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("GENERAL")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("MEDICAL_DATA")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("COLORS_AND_FONTS")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("RENDERING_QUALITY")));
        //DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(m_ic.getMessage("DATA_STORING"));
        //dataNode.add(new DefaultMutableTreeNode(m_ic.getMessage("MYSQL_SETUP")));
        //dataNode.add(new DefaultMutableTreeNode(m_ic.getMessage("TEXTFILE_SETUP")));
        //prefNode.add(dataNode);
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("METER_CONFIGURATION")));
        //prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("NUTRITION")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("PRINTING")));
	    /*DefaultMutableTreeNode meterNode = new DefaultMutableTreeNode("Meters");
	    meterNode.add(new DefaultMutableTreeNode("Glucocard"));
        prefNode.add(meterNode);*/
  //  }



    //private int PANEL_MAIN = 0;
    private int PANEL_GENERAL = 0;
    private int PANEL_MEDICAL_DATA = 1;
    private int PANEL_COLORS = 2;
    private int PANEL_RENDERING = 3;
    private int PANEL_METER = 4;
    private int PANEL_PRINTING = 5;


    public void createPanels()
    {
	// Each node must have a panel, and panel numbers must be as they are added 
	// to. You must add panels in same order as you add, nodes.

        panels = new ArrayList<JPanel>();
        panel_id = new Hashtable<String, String>();

        //addPanel(m_ic.getMessage("PREFERENCES"), this.PANEL_MAIN, new PrefMainPane());
        addPanel(m_ic.getMessage("GENERAL"), this.PANEL_GENERAL, new PrefGeneralPane());
        addPanel(m_ic.getMessage("MEDICAL_DATA"), this.PANEL_MEDICAL_DATA, new PrefMedicalDataPane());
        addPanel(m_ic.getMessage("COLORS_AND_FONTS"), PANEL_COLORS, new PrefFontsAndColorPane(this));
        addPanel(m_ic.getMessage("RENDERING_QUALITY"), PANEL_RENDERING, new PrefRenderingQualityPane());
        addPanel(m_ic.getMessage("METER_CONFIGURATION"), PANEL_METER, new PrefMeterConfPane(this));
        addPanel(m_ic.getMessage("PRINTING"), PANEL_PRINTING, new PrefPrintingPane());
    }


    // ---
    // ---  End
    // ---


    private void addPanel(String name, int id, JPanel panel)
    {
        panels.add(panel);
        //panel_id.put(name, ""+id);
    }


    public void selectPanel(String s)
    {

        if (!panel_id.containsKey(s))
        {
            System.out.println("No such panel: " + s);
            return;
        }

        String id = panel_id.get(s);

        prefPane.remove(1);
        prefPane.add(panels.get(Integer.parseInt(id)), BorderLayout.CENTER);
        prefPane.invalidate();
        prefPane.validate();
        prefPane.repaint();

    }

    public void selectPanel(int index)
    {
	/*
	if (!panel_id.containsKey(s))
	{
	    System.out.println("No such panel: " + s);
	    return;
	}

	String id = panel_id.get(s); */

	prefPane.remove(1);
	prefPane.add(panels.get(index), BorderLayout.CENTER);
	prefPane.invalidate();
	prefPane.validate();
	prefPane.repaint();

    }



    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        if (action.equals("ok")) 
        {
            save();
	    ok_action = true;
            this.dispose();
        }
        else if (action.equals("cancel")) 
        {
            reset();
            this.dispose();
        }
        else if (action.equals("apply")) 
        {
            save();
        }
        else
            System.out.println("PropertiesFrame: Unknown command: " + action);

    }


    public boolean wasOKAction()
    {
	return ok_action;
    }


    public void save()
    {
        for (int i=0; i<panels.size(); i++)
        {
            AbstractPrefOptionsPanel pn = (AbstractPrefOptionsPanel)panels.get(i);
            pn.saveProps();
        }

        m_da.getSettings().save();

	/*
	if (m_da.getDbConfig().hasChanged())
	{
	    m_da.getDbConfig().saveConfig();
	}
	*/
    }

    public void reset()
    {
        DataAccess.getInstance().getSettings().reload();
    }



    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e)
    {
	if (current_index != list.getSelectedIndex())
	{
	    current_index = list.getSelectedIndex();
	    selectPanel(current_index);
	    //System.out.println(list.getSelectedValue());
	}

    }


}
