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


import ggc.meter.util.DataAccess;
import ggc.meter.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class SimpleConfigurationHelp extends JDialog implements ActionListener //, HelpCapable
{

    /**
     * 
     */
    private static final long serialVersionUID = 6811126019155116487L;
    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccess m_da; // = DataAccess.getInstance();

    JTextArea editArea;
    
//    private DefaultMutableTreeNode prefNode;
    //private JTree prefTree;
    //private JList list = null;
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



    public SimpleConfigurationHelp(DataAccess da)
    {
        super(da.getMainParent(), "", true);
        
        m_da = da;
        
        m_da.addComponent(this);

        setSize(500,400);
        setTitle(m_ic.getMessage("SIMPLE_PREFERENCES"));
        m_da.centerJDialog(this, da.getMainParent());

        help_button = m_da.createHelpButtonBySize(120, 25, this);

        init();
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





//	prefPane = new JPanel(new BorderLayout());
	
        prefPane = new JPanel(null);
        
        JLabel label = new JLabel("SIMPLE_PREFERENCES");
        label.setBounds(20,20, 480, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        
        prefPane.add(label);
        
        
        JTextArea editArea = new JTextArea(15, 60);
        editArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(editArea);        
        scrollingText.setBounds(20,40,420,250);
        
        prefPane.add(scrollingText);

        //set the buttons up...
        JButton okButton = new JButton("  " + m_ic.getMessage("OK"));
        okButton.setPreferredSize(dim);
        okButton.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        okButton.setActionCommand("ok");
        okButton.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("  " +m_ic.getMessage("CANCEL"));
        cancelButton.setPreferredSize(dim);
        cancelButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        cancelButton.setActionCommand("cancel");
        cancelButton.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
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
        //prefPane.add(this.prefPane, BorderLayout.CENTER);

        //getContentPane().add(prefTreePane, BorderLayout.WEST);
	//getContentPane().add(list, BorderLayout.WEST);
        getContentPane().add(prefPane, BorderLayout.CENTER);
    }


    
    public void loadConfiguration()
    {
        
    }
    



    //private int PANEL_MAIN = 0;


    
    
    
    

    // ---
    // ---  End
    // ---


    



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
        for (int i=0; i<panels.size(); i++)
        {
            AbstractPreferencesPanel pn = (AbstractPreferencesPanel)panels.get(i);
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
        //return ((HelpCapable)this.selected_panel).getHelpId();
        return "";
    }
    
    
    public static void main(String[] args)
    {
        JFrame fr = new JFrame();
        fr.setBounds(0,0,800,600);
        
        DataAccess da = DataAccess.createInstance(fr);
        da.setMainParent(fr);
        
        new SimpleConfigurationDialog(da);
        
        
    }
    
    
}
