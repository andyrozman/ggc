
package ggc.core.plugins;

import ggc.core.util.I18nControl;
import ggc.gui.MainFrame;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


public class PumpsPlugIn implements ActionListener
{
    
    private boolean installed = false;
    I18nControl ic = I18nControl.getInstance();
    private String commands[] = { 
		"MN_PUMPS_READ_DESC", 
		"MN_PUMPS_LIST_DESC", 
		"MN_PUMPS_CONFIG_DESC", 
		"MN_PUMP_PROFILES_DESC", 
		"MN_PUMPS_MANUAL_ENTRY_DESC", 
		"MN_PUMPS_ADDITIONAL_DATA_DESC",
	    			};
    Container parent = null;

    
    
    public PumpsPlugIn(Container parent)
    {
	this.parent = parent;
	checkIfInstalled();
    }

    private void checkIfInstalled()
    {
    }
    
    
    public String getName()
    {
	return ic.getMessage("PUMPS_PLUGIN");
    }
    
    
    public void readPumpsData()
    {
	this.featureNotImplemented(commands[0]);
    }
    
    public void pumpsList()
    {
	this.featureNotImplemented(commands[1]);
    }
    
    
    public void pumpsConfiguration()
    {
	this.featureNotImplemented(commands[2]);
    }

    
    
    public void pumpsProfiles()
    {
	this.featureNotImplemented(commands[3]);
	
    }
    
    public void pumpManualEntry()
    {
	this.featureNotImplemented(commands[4]);
	
    }

    public void pumpAddAdditionalData()
    {
	this.featureNotImplemented(commands[5]);
	
    }
    
    
    
    

    /*
            else if ((command.equals("pumps_read")) ||
        	    (command.equals("pumps_list")) ||
        	    (command.equals("pumps_profile")) ||
        	    (command.equals("pumps_manual_entry")) ||
        	    (command.equals("pumps_additional_data")) ||
        	    (command.equals("pumps_config"))
        	    )
            {
        	featureNotImplemented(command, "0.5 " + m_ic.getMessage("OR") + " 0.6");
            }
            
            
	// pumps
	this.menu_pumps = this.createMenu("MN_PUMPS", null);
	this.createAction(menu_pumps, "MN_PUMP_PROFILES", "MN_PUMP_PROFILES_DESC", "pumps_profile", null);
	this.menu_pumps.addSeparator();
	this.createAction(menu_pumps, "MN_PUMPS_MANUAL_ENTRY", "MN_PUMPS_MANUAL_ENTRY_DESC", "pumps_manual_entry", null);
	this.createAction(menu_pumps, "MN_PUMPS_READ", "MN_PUMPS_READ_DESC", "pumps_read", null);
	this.createAction(menu_pumps, "MN_PUMPS_ADDITIONAL_DATA", "MN_PUMPS_ADDITIONAL_DATA_DESC", "pumps_additional_data", null);
	this.menu_pumps.addSeparator();
	this.createAction(menu_pumps, "MN_PUMPS_LIST", "MN_PUMPS_LIST_DESC", "pumps_list", null);
	this.menu_pumps.addSeparator();
	this.createAction(menu_pumps, "MN_PUMPS_CONFIG", "MN_PUMPS_CONFIG_DESC", "pumps_config", null);
            
            
    */

	
	
    
    
    /* 
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {
	String command = e.getActionCommand();

	
        if (command.equals("pumps_read"))
        {
            this.readPumpsData();
        }
        else if (command.equals("pumps_list"))
        {
            this.pumpsList();
        }
        else if (command.equals("pumps_profile"))
        {
            this.pumpsProfiles();
        }
        else if (command.equals("pumps_manual_entry")) 
        {
            this.pumpManualEntry();
            
        }
        else if (command.equals("pumps_additional_data")) 
        {
            this.pumpAddAdditionalData();
        }
        else if (command.equals("pumps_config"))
        {
            this.pumpsConfiguration();
        }
        else
	{
	    System.out.println("Wrong command for this plug-in [Pumps]: " + command);
	}
	
    }
    
    public void featureNotImplemented(String command_desc)
    {
    	String text = String.format(ic.getMessage("PLUGIN_NOT_INSTALLED_OR_AVAILABLE"), this.getName());
    	    
    	text += "\n\n'" + ic.getMessage(command_desc) +"' ";
    	text += String.format(ic.getMessage("IMPLEMENTED_VERSION"), "0.5");
    	text += "!\n\n";
    
    	JOptionPane.showMessageDialog(this.parent, text, ic.getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);

    }
 
    
    
    
}

