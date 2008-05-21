
package ggc.core.plugins;

import ggc.core.util.I18nControl;
import ggc.gui.MainFrame;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


public class MetersPlugIn implements ActionListener
{
    
    private boolean installed = false;
    I18nControl ic = I18nControl.getInstance();
    private String commands[] = { "MN_METERS_READ_DESC",
	    			"MN_METERS_LIST_DESC",	
	    			"MN_METERS_CONFIG_DESC"
	    			};
    Container parent = null;
    
    
    public MetersPlugIn(Container parent)
    {
	this.parent = parent;
	checkIfInstalled();
    }

    private void checkIfInstalled()
    {
    }
    
    
    public String getName()
    {
	return ic.getMessage("METERS_PLUGIN");
    }
    
    
    public void readMeterData()
    {
	this.featureNotImplemented(commands[0]);
    }
    
    public void metersList()
    {
	this.featureNotImplemented(commands[1]);
    }
    
    
    public void meterConfiguration()
    {
	this.featureNotImplemented(commands[2]);
    }
    

    /*
	// meters
	this.menu_meters = this.createMenu("MN_METERS", null);
	this.createAction(menu_meters, "MN_METERS_READ", "MN_METERS_READ_DESC", "meters_read", null);
	this.menu_meters.addSeparator();
	this.createAction(menu_meters, "MN_METERS_LIST", "MN_METERS_LIST_DESC", "meters_list", null);
	this.menu_meters.addSeparator();
	this.createAction(menu_meters, "MN_METERS_CONFIG", "MN_METERS_CONFIG_DESC", "meters_config", null);
    */

	
	
    
    
    /* 
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {
	String command = e.getActionCommand();
	
	if (command.equals("meters_read"))
	{
	    this.readMeterData();
	}
	else if (command.equals("meters_list")) 
	{
	    this.metersList();
	}
	else if (command.equals("meters_config"))
	{
	    this.meterConfiguration();
	}
	else 
	{
	    System.out.println("Wrong command for this plug-in [Meters]: " + command);
	}
	
    }
    
    public void featureNotImplemented(String command_desc)
    {
    	String text = String.format(ic.getMessage("PLUGIN_NOT_INSTALLED_OR_AVAILABLE"), this.getName());
    	    
    	text += "\n\n'" + ic.getMessage(command_desc) +"' ";
    	text += String.format(ic.getMessage("IMPLEMENTED_VERSION"), "0.4");
    	text += "!\n\n";
    
    	JOptionPane.showMessageDialog(this.parent, text, ic.getMessage("INFORMATION"), JOptionPane.INFORMATION_MESSAGE);

    }
 
    
    
    
    
    
}
