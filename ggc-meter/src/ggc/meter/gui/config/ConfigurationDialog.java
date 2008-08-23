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
 *  Filename: PropertiesDialog
 *
 *  Purpose:  Dialog for setting properties for application.
 *
 *  Author:   andyrozman {andy@atech-software.com}
 *
 */
package ggc.meter.gui.config;


import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.atech.help.HelpCapable;


public class ConfigurationDialog extends JDialog implements ListSelectionListener, ActionListener, HelpCapable
{

    private static final long serialVersionUID = -293909330225800995L;
    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccessMeter m_da; // = DataAccessMeter.getInstance();

//    private DefaultMutableTreeNode prefNode;
    //private JTree prefTree;
    private JList list = null;
    private JPanel prefPane;
    JButton help_button;

    public ArrayList<JPanel> panels = null;
    public Hashtable<String, String> panel_id = null;

    int current_index = 0;

    boolean ok_action = false;


    public String config_types[] = { 
            m_ic.getMessage("METERS_COMPANY_LIST"),
            m_ic.getMessage("METERS_LIST"),
            m_ic.getMessage("METERS_LOCATION"),
            m_ic.getMessage("METERS_CONFIG"),
            m_ic.getMessage("OTHER"),
    };



    public ConfigurationDialog(DataAccessMeter da)
    {
        super(da.getMainParent(), "", true);
        
        m_da = da;
        
        m_da.addComponent(this);

/*        Rectangle rec = parent.getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-320, y-240, 640, 480);*/
        setSize(700,500);
        setTitle(m_ic.getMessage("PREFERENCES"));
        m_da.centerJDialog(this, da.getMainParent());

        help_button = m_da.createHelpButtonBySize(120, 25, this);
        createPanels();

        init();
        selectPanel(0);
        this.setResizable(false);
        this.setVisible(true);
    }


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
        JButton okButton = new JButton("  " + m_ic.getMessage("OK"));
        okButton.setPreferredSize(dim);
        okButton.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        okButton.setActionCommand("ok");
        okButton.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("  " +m_ic.getMessage("CANCEL"));
        cancelButton.setPreferredSize(dim);
        cancelButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        cancelButton.setActionCommand("cancel");
        cancelButton.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        cancelButton.addActionListener(this);

        JButton applyButton = new JButton("  " +m_ic.getMessage("APPLY"));
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





    //private int PANEL_MAIN = 0;
    private int PANEL_METERS_LIST = 0;
    private int PANEL_METERS_CONFIG = 1;
    private int PANEL_METERS_LOCATION = 2;
    private int PANEL_OTHERS = 3;


    
    
    
    public void createPanels()
    {
	// Each node must have a panel, and panel numbers must be as they are added 
	// to. You must add panels in same order as you add, nodes.

        panels = new ArrayList<JPanel>();
        panel_id = new Hashtable<String, String>();

        addPanel(m_ic.getMessage("METERS_COMPANY_LIST"), this.PANEL_METERS_LIST, new MeterCompanyPanel(this));
        addPanel(m_ic.getMessage("METERS_LIST"), this.PANEL_METERS_LIST, new MeterStatusPanel(this));
        addPanel(m_ic.getMessage("METERS_CONFIG"), this.PANEL_METERS_CONFIG, new MeterConfigPanel(this));
        addPanel(m_ic.getMessage("METERS_LOCATION"), this.PANEL_METERS_LOCATION, new MeterLocationPanel(this));
        addPanel(m_ic.getMessage("OTHER"), this.PANEL_OTHERS, new MeterOtherPanel(this));
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
    	selected_panel = panels.get(Integer.parseInt(id));
        prefPane.invalidate();
        prefPane.validate();
        prefPane.repaint();
        
        m_da.enableHelp(this);
    }

    JPanel selected_panel = null;
    
    
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
    	selected_panel = panels.get(index);
    	prefPane.invalidate();
    	prefPane.validate();
    	prefPane.repaint();

//Andy    	m_da.enableHelp(this);    	
    }



    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        if (action.equals("ok")) 
        {
            System.out.println("Ok Action not implemented");
            
//            save();
//            ok_action = true;
//            this.dispose();
        }
        else if (action.equals("cancel")) 
        {
            System.out.println("Cancel Action not implemented");
            //reset();
            this.dispose();
        }
        else if (action.equals("apply")) 
        {
            System.out.println("Apply Action not implemented");
            //save();
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
/* xa       for (int i=0; i<panels.size(); i++)
        {
            AbstractPreferencesPanel pn = (AbstractPreferencesPanel)panels.get(i);
            pn.saveProps();
        }

        m_da.getSettings().save();
*/
	/*
	if (m_da.getDbConfig().hasChanged())
	{
	    m_da.getDbConfig().saveConfig();
	}
	*/
    }

    public void reset()
    {
//xa        DataAccessMeter.getInstance().getSettings().reload();
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
    
    /* 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
	return this.getRootPane();
    }

    /* 
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
	return this.help_button;
    }

    /* 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
	return ((HelpCapable)this.selected_panel).getHelpId();
    }
    
    
    public static void main(String[] args)
    {
        JFrame fr = new JFrame();
        fr.setBounds(0,0,800,600);
        
        DataAccessMeter da = DataAccessMeter.createInstance(fr);
        da.setMainParent(fr);
        
        new ConfigurationDialog(da);
        
        
    }
    
    
}
