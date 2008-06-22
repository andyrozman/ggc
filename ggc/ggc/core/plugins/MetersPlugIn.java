
package ggc.core.plugins;

import ggc.core.util.DataAccess;

import java.awt.Container;
import java.awt.event.ActionEvent;

import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.plugin.PlugInServer;


public class MetersPlugIn extends PlugInClient 
{
    
    private PlugInServer m_srv = null;

    
    public static final int COMMAND_READ_METER_DATA = 0;
    public static final int COMMAND_METERS_LIST = 1;
    public static final int COMMAND_CONFIGURATION = 2;
    
    
    
    public MetersPlugIn(Container parent, I18nControlAbstract ic)
    {
	super(parent, ic);
    }
    
    

    public MetersPlugIn()
    {
	super();
    }
    
    
    public void initPlugin()
    {
	this.commands = new String[3];
	this.commands[0] = "MN_METERS_READ_DESC";
	this.commands[1] = "MN_METERS_LIST_DESC";
	this.commands[2] = "MN_METERS_CONFIG_DESC";
	
	this.commands_implemented = new boolean[3];
	this.commands_implemented[0] = false;
	this.commands_implemented[1] = false;
	this.commands_implemented[2] = true;
    }
    
    
    
    public void checkIfInstalled()
    {
	try
	{
            Class c = Class.forName("ggc.meter.plugin.MeterPlugInServer");

            //PlugInServer obj = (PlugInServer)c.newInstance();
            this.m_server = (PlugInServer)c.newInstance();
	    installed = true;
	    this.m_server.init(this.parent, DataAccess.getInstance().getI18nControlInstance().getSelectedLangauge(), DataAccess.getInstance());
	    
	    //System.out.println("We have an instance !!! " + this.m_srv);
	}
	catch(Exception ex)
	{
	    System.out.println("Ex:" + ex);
	    
	}
	
	    //System.out.println("We have an instance !!! " + this.m_srv);
	
	
    }
    
    
    
    
    public String getName()
    {
	return ic.getMessage("METERS_PLUGIN");
    }
    

    
    public void readMeterData()
    {
	this.featureNotImplemented(commands[MetersPlugIn.COMMAND_READ_METER_DATA]);
    }
    
    public void metersList()
    {
	this.featureNotImplemented(commands[MetersPlugIn.COMMAND_METERS_LIST]);
    }
    
    
    public void meterConfiguration()
    {
	this.featureNotImplemented(commands[MetersPlugIn.COMMAND_CONFIGURATION]);
    }
    

    /* 
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {
	String command = e.getActionCommand();
	
	if (command.equals("meters_read"))
	{
	    this.executeCommand(MetersPlugIn.COMMAND_READ_METER_DATA);
	    //this.readMeterData();
	}
	else if (command.equals("meters_list")) 
	{
	    this.executeCommand(MetersPlugIn.COMMAND_METERS_LIST);
	    //this.metersList();
	}
	else if (command.equals("meters_config"))
	{
	    this.executeCommand(MetersPlugIn.COMMAND_CONFIGURATION);
	    //this.meterConfiguration();
	}
	else 
	{
	    System.out.println("Wrong command for this plug-in [Meters]: " + command);
	}
	
    }
    

    public String getWhenWillBeImplemented()
    {
	return "0.3";
    }
    
    
    
    public String getShortStatus()
    {
	if (this.m_server!=null)
	{
	    return String.format(ic.getMessage("STATUS_INSTALLED"), this.m_server.getVersion());
	}
	else
	    return String.format(ic.getMessage("STATUS_NOT_AVAILABLE"), "0.3");
    }
    
    
    
}
