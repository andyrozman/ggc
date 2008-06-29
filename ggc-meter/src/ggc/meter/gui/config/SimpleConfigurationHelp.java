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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class SimpleConfigurationHelp extends JDialog implements ActionListener 
{

    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccessMeter m_da; // = DataAccessMeter.getInstance();

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




    public SimpleConfigurationHelp(JDialog parent)
    {
        super(parent, "", true);
        
        m_da = DataAccessMeter.getInstance();
        
        m_da.addComponent(this);

        setSize(500,580);
        setTitle(m_ic.getMessage("SIMPLE_PREFERENCES_HELP"));
        m_da.centerJDialog(this, parent); //, da.getMainParent());

        //help_button = m_da.createHelpButtonBySize(120, 25, this);

        init();
        this.setResizable(false);
        this.setVisible(true);
    }


    private void init()
    {

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
        prefPane.setBounds(0,0,500,520);
        
        
        
        JLabel label = new JLabel(m_ic.getMessage("SIMPLE_PREFERENCES_HELP"));
        label.setBounds(20,20, 500, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(m_da.getFont(DataAccessMeter.FONT_BIG_BOLD));
        
        prefPane.add(label);
        
        
        label = new JLabel(m_ic.getMessage("SIMPLE_PREFERENCES_HELP_CONTENT"));
        label.setBounds(20,60, 460, 410);
        //label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        prefPane.add(label);
        
        

        //set the buttons up...

        JButton cancelButton = new JButton("  " +m_ic.getMessage("CLOSE"));
        cancelButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        cancelButton.setActionCommand("close");
        cancelButton.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        cancelButton.addActionListener(this);
        cancelButton.setBounds(190, 495, 120, 25);
        this.prefPane.add(cancelButton);

        getContentPane().add(prefPane, BorderLayout.CENTER);
    }


    
    public void loadConfiguration()
    {
        
    }
    



    //private int PANEL_MAIN = 0;


    
    
    
    

    // ---
    // ---  End
    // ---


    public String toString()
    {
        return "ggc.meter.gui.config.SimpleConfigurationHelp";
    }



    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        if (action.equals("close")) 
        {
            m_da.removeComponent(this);
            this.dispose();
        }
        else
            System.out.println("PropertiesFrame: Unknown command: " + action);

    }

    

    
    
}
