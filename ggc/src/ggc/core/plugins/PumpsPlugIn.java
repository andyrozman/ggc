package ggc.core.plugins;

import ggc.core.util.DataAccess;

import java.awt.Container;
import java.awt.event.ActionEvent;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.plugin.PlugInServer;

public class PumpsPlugIn extends PlugInClient
{

    
    public static final int COMMAND_READ_PUMP_DATA = 0;
    public static final int COMMAND_PUMPS_LIST = 1;
    public static final int COMMAND_CONFIGURATION = 2;
    public static final int COMMAND_PROFILES = 3;
    public static final int COMMAND_MANUAL_ENTRY = 4;
    public static final int COMMAND_ADDITIONAL_DATA = 5;
    public static final int COMMAND_ABOUT = 6;
    
    
    /*
    private String commands[] = { "MN_PUMPS_READ_DESC", 
                                  "MN_PUMPS_LIST_DESC", 
                                  "MN_PUMPS_CONFIG_DESC",
                                  "MN_PUMP_PROFILES_DESC", 
                                  "MN_PUMPS_MANUAL_ENTRY_DESC",
                                  "MN_PUMPS_ADDITIONAL_DATA_DESC", };
*/
    
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
        try
        {
            Class<?> c = Class.forName("ggc.pump.plugin.PumpPlugInServer");

            this.m_server = (PlugInServer) c.newInstance();
            installed = true;
            
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!! Db: " + DataAccess.getInstance().getDb());
            
            
            this.m_server.init(this.parent, 
                DataAccess.getInstance().getI18nControlInstance().getSelectedLangauge(), 
                DataAccess.getInstance(), 
                this, 
                DataAccess.getInstance().getDb() );
        }
        catch (Exception ex)
        {
            System.out.println("Ex:" + ex);

        }
        
        
    }

    public String getName()
    {
        return ic.getMessage("PUMPS_PLUGIN");
    }

    public void readPumpsData()
    {
        this.featureNotImplemented(commands[0]);
    }

    
    public void initPlugin()
    {
        this.commands = new String[7];
        this.commands[0] = "MN_PUMPS_READ_DESC";
        this.commands[1] = "MN_PUMPS_LIST_DESC";
        this.commands[2] = "MN_PUMPS_CONFIG_DESC";
        this.commands[3] = "MN_PUMP_PROFILES_DESC";
        this.commands[4] = "MN_PUMPS_MANUAL_ENTRY_DESC";
        this.commands[5] = "MN_PUMPS_ADDITIONAL_DATA_DESC";
        this.commands[6] = "MN_PUMPS_ABOUT_DESC";

        this.commands_implemented = new boolean[7];
        this.commands_implemented[0] = false;
        this.commands_implemented[1] = false;
        this.commands_implemented[2] = true;
        this.commands_implemented[3] = false;
        this.commands_implemented[4] = true;
        this.commands_implemented[5] = true;
        this.commands_implemented[6] = true;
        
        this.commands_will_be_done = new String[7];
        this.commands_will_be_done[0] = "0.5";
        this.commands_will_be_done[1] = "0.4";
        this.commands_will_be_done[2] = null;
        this.commands_will_be_done[3] = "0.5";
        this.commands_will_be_done[4] = null;
        this.commands_will_be_done[5] = null;
        this.commands_will_be_done[6] = null;
        
    }

    /*
     * else if ((command.equals("pumps_read")) || (command.equals("pumps_list"))
     * || (command.equals("pumps_profile")) ||
     * (command.equals("pumps_manual_entry")) ||
     * (command.equals("pumps_additional_data")) ||
     * (command.equals("pumps_config")) ) { featureNotImplemented(command,
     * "0.5 " + m_ic.getMessage("OR") + " 0.6"); }
     * 
     * 
     * // pumps this.menu_pumps = this.createMenu("MN_PUMPS", null);
     * this.createAction(menu_pumps, "MN_PUMP_PROFILES",
     * "MN_PUMP_PROFILES_DESC", "pumps_profile", null);
     * this.menu_pumps.addSeparator(); this.createAction(menu_pumps,
     * "MN_PUMPS_MANUAL_ENTRY", "MN_PUMPS_MANUAL_ENTRY_DESC",
     * "pumps_manual_entry", null); this.createAction(menu_pumps,
     * "MN_PUMPS_READ", "MN_PUMPS_READ_DESC", "pumps_read", null);
     * this.createAction(menu_pumps, "MN_PUMPS_ADDITIONAL_DATA",
     * "MN_PUMPS_ADDITIONAL_DATA_DESC", "pumps_additional_data", null);
     * this.menu_pumps.addSeparator(); this.createAction(menu_pumps,
     * "MN_PUMPS_LIST", "MN_PUMPS_LIST_DESC", "pumps_list", null);
     * this.menu_pumps.addSeparator(); this.createAction(menu_pumps,
     * "MN_PUMPS_CONFIG", "MN_PUMPS_CONFIG_DESC", "pumps_config", null);
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
            this.executeCommand(PumpsPlugIn.COMMAND_PUMPS_LIST);
        }
        else if (command.equals("pumps_profile"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_PROFILES);
        }
        else if (command.equals("pumps_manual_entry"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_MANUAL_ENTRY);
        }
        else if (command.equals("pumps_additional_data"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_ADDITIONAL_DATA);
        }
        else if (command.equals("pumps_config"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_CONFIGURATION);
        }
        else if (command.equals("pumps_about"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_ABOUT);
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
        if (this.m_server != null)
            return String.format(ic.getMessage("STATUS_INSTALLED"), this.m_server.getVersion());
        else
            return ic.getMessage("STATUS_NOT_INSTALLED");
    }

    public void setReturnData(Object return_data, StatusReporterInterface stat_rep_int)
    {
    }

}
