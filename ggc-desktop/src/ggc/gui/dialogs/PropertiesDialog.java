package ggc.gui.dialogs;

import ggc.core.data.cfg.ConfigCellRenderer;
import ggc.core.util.DataAccess;
import ggc.gui.panels.prefs.AbstractPrefOptionsPanel;
import ggc.gui.panels.prefs.PrefFontsAndColorPane;
import ggc.gui.panels.prefs.PrefGeneralPane;
import ggc.gui.panels.prefs.PrefLanguagePane;
import ggc.gui.panels.prefs.PrefMedicalDataPane;
import ggc.gui.panels.prefs.PrefModePane;
import ggc.gui.panels.prefs.PrefPrintingPane;
import ggc.gui.panels.prefs.PrefRenderingQualityPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     PropertiesDialog
 *  Description:  Dialog for setting properties for application.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PropertiesDialog extends JDialog implements ListSelectionListener, ActionListener, HelpCapable
{

    private static final long serialVersionUID = -5813992933782713913L;
    private DataAccess m_da; // = DataAccess.getInstance();
    private I18nControlAbstract m_ic = DataAccess.getInstance().getI18nControlInstance();

    @SuppressWarnings("rawtypes")
    private JList list = null;
    private JPanel prefPane;
    JButton help_button;

    private ArrayList<JPanel> panels = null;
    private Hashtable<String, String> panel_id = null;

    int current_index = 0;
    boolean ok_action = false;

    private PrefFontsAndColorPane pfacpane=null; //used for updating preferences correctly when changes are made to mg/dl or mmol/l.
    private PrefRenderingQualityPane prqpane=null; //used for updating preferences correctly when changes are made to mg/dl or mmol/l.

    /**
     * Config types
     */
    public String config_types[] = { 
        m_ic.getMessage("MODE"),                            
    	m_ic.getMessage("GENERAL"),
    	m_ic.getMessage("MEDICAL_DATA"),
    	m_ic.getMessage("COLORS_AND_FONTS"),
    	m_ic.getMessage("RENDERING_QUALITY"),
    //	m_ic.getMessage("METER_CONFIGURATION"),
    	m_ic.getMessage("PRINTING"),
        m_ic.getMessage("LANGUAGE")
    };



    /**
     * Constructor
     * 
     * @param da
     */
    public PropertiesDialog(DataAccess da)
    {
        super(da.getMainParent(), "", true);
        
        m_da = da;

/*        Rectangle rec = parent.getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-320, y-240, 640, 480);*/
        setSize(640,480);
        setTitle(m_ic.getMessage("PREFERENCES"));
        m_da.centerJDialog(this, da.getMainParent());

        help_button = m_da.createHelpButtonBySize(120, 25, this);
        createPanels();

        init();
        selectPanel(0);
        this.setResizable(false);
        this.setVisible(true);
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void init()
    {
        Dimension dim = new Dimension(120, 25);

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
	list = new JList(config_types); 
	list.addListSelectionListener(this);
	ConfigCellRenderer renderer = new ConfigCellRenderer();
	renderer.setPreferredSize(new Dimension(100, 75));
	list.setCellRenderer(renderer);
	list.setSelectedIndex(0);



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
        JButton okButton = new JButton("   " + m_ic.getMessage("OK"));
        okButton.setPreferredSize(dim);
        okButton.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        okButton.setActionCommand("ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("   " +m_ic.getMessage("CANCEL"));
        cancelButton.setPreferredSize(dim);
        cancelButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        JButton applyButton = new JButton("   " +m_ic.getMessage("APPLY"));
        applyButton.setPreferredSize(dim);
        applyButton.setIcon(m_da.getImageIcon_22x22("flash.png", this));
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);

        //...and align them in a row at the buttom.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        buttonPanel.add(help_button);

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
    private int PANEL_MODE = 0;
    private int PANEL_GENERAL = 1;
    private int PANEL_MEDICAL_DATA = 2;
    private int PANEL_COLORS = 3;
    private int PANEL_RENDERING = 4;
    private int PANEL_PRINTING = 5;
    private int PANEL_LANGUAGE = 6;
    //    private int PANEL_METER = 4;

    /**
     * Create Panels 
     */
    public void createPanels()
    {
	// Each node must have a panel, and panel numbers must be as they are added 
	// to. You must add panels in same order as you add, nodes.

        panels = new ArrayList<JPanel>();
        panel_id = new Hashtable<String, String>();

        //addPanel(m_ic.getMessage("PREFERENCES"), this.PANEL_MAIN, new PrefMainPane());
        addPanel(m_ic.getMessage("MODE"), PANEL_MODE, new PrefModePane(this));
        addPanel(m_ic.getMessage("GENERAL"), this.PANEL_GENERAL, new PrefGeneralPane(this));
        addPanel(m_ic.getMessage("MEDICAL_DATA"), this.PANEL_MEDICAL_DATA, new PrefMedicalDataPane(this));
        pfacpane = new PrefFontsAndColorPane(this); //to be able to use updateGraphView with an instance later.
        addPanel(m_ic.getMessage("COLORS_AND_FONTS"), PANEL_COLORS, pfacpane); //
        prqpane = new PrefRenderingQualityPane(this);//to be able to use updateGraphView with an instance later.
        addPanel(m_ic.getMessage("RENDERING_QUALITY"), PANEL_RENDERING, prqpane);
        addPanel(m_ic.getMessage("PRINTING"), PANEL_PRINTING, new PrefPrintingPane(this));
        addPanel(m_ic.getMessage("LANGUAGE"), PANEL_LANGUAGE, new PrefLanguagePane(this));
//        addPanel(m_ic.getMessage("METER_CONFIGURATION"), PANEL_METER, new PrefMeterConfPane(this));
    }


    // ---
    // ---  End
    // ---


    private void addPanel(String name, int id, JPanel panel)
    {
        panels.add(panel);
        //panel_id.put(name, ""+id);
    }


    /**
     * Select Panel (string)
     * 
     * @param s
     */
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
    	selected_panel = panels.get(Integer.parseInt(id));
        prefPane.invalidate();
        prefPane.validate();
        prefPane.repaint();
        
        m_da.enableHelp(this);
    }

    JPanel selected_panel = null;
    
    
    /**
     * Select Panel (int)
     * 
     * @param index
     */
    public void selectPanel(int index)
    {
    	/*
    	if (!panel_id.containsKey(s))
    	{
    	    System.out.println("No such panel: " + s);
    	    return;
    	}
    
    	String id = panel_id.get(s); */
    
        pfacpane.updateGraphView(); //Used to remove inconsistencies when updating charts with mmol/l or mg/dl in preferences.
        prqpane.updateGraphView(); //Used to remove inconsistencies when updating charts with mmol/l or mg/dl in preferences.
        prefPane.remove(1);
    	prefPane.add(panels.get(index), BorderLayout.CENTER);
    	selected_panel = panels.get(index);
    	prefPane.invalidate();
    	prefPane.validate();
    	prefPane.repaint();

    	m_da.enableHelp(this);    	
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

    
    /**
     * Was Action Successful
     * 
     * @return true if action was successful (dialog closed with OK)
     */
    public boolean actionSuccessful()
    {
        return ok_action;
    }


    private void save()
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

    private void reset()
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


    // ****************************************************************
    // ******              HelpCapable Implementation             *****
    // ****************************************************************
    
    /** 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
	return this.getRootPane();
    }

    /** 
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
	return this.help_button;
    }

    /** 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
	return ((HelpCapable)this.selected_panel).getHelpId();
    }
    
    
}