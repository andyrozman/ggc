
package ggc.core.plugins;

import java.awt.Container;
import java.awt.event.ActionEvent;

import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;


public class PumpsPlugIn extends PlugInClient
{
    
    private String commands[] = { 
		"MN_PUMPS_READ_DESC", 
		"MN_PUMPS_LIST_DESC", 
		"MN_PUMPS_CONFIG_DESC", 
		"MN_PUMP_PROFILES_DESC", 
		"MN_PUMPS_MANUAL_ENTRY_DESC", 
		"MN_PUMPS_ADDITIONAL_DATA_DESC",
	    			};

    

    public PumpsPlugIn(Container parent, I18nControlAbstract ic)
    {
	super(parent, ic);
    }
    
    

    public PumpsPlugIn()
    {
	super();
    }
    
    
    public void checkIfInstalled()
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
    

    public void initPlugin()
    {
	this.commands = new String[6];
	this.commands[0] = "MN_PUMPS_READ_DESC";
	this.commands[1] = "MN_PUMPS_LIST_DESC";
	this.commands[2] = "MN_PUMPS_CONFIG_DESC";
	this.commands[3] = "MN_PUMP_PROFILES_DESC";
	this.commands[4] = "MN_PUMPS_MANUAL_ENTRY_DESC";
	this.commands[5] = "MN_PUMPS_ADDITIONAL_DATA_DESC";

	this.commands_implemented = new boolean[6];
	this.commands_implemented[0] = false;
	this.commands_implemented[1] = false;
	this.commands_implemented[2] = false;
	this.commands_implemented[3] = false;
	this.commands_implemented[4] = false;
	this.commands_implemented[5] = false;
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
    
 
    public String getWhenWillBeImplemented()
    {
	return "0.4";
    }
    
    
    
    public String getShortStatus()
    {
	return String.format(ic.getMessage("STATUS_NOT_AVAILABLE"), "0.4");
    }

    
}

