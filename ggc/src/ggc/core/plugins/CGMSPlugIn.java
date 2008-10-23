package ggc.core.plugins;

import java.awt.Container;
import java.awt.event.ActionEvent;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;

public class CGMSPlugIn extends PlugInClient
{

    public static final int COMMAND_READ_CGMS_DATA = 0;
    public static final int COMMAND_CGMS_LIST = 1;
    public static final int COMMAND_CGMS_CONFIGURATION = 2;
    public static final int COMMAND_CGMS_ABOUT = 3;
    
    
    public CGMSPlugIn(Container parent, I18nControlAbstract ic)
    {
        super(parent, ic);
    }

    public CGMSPlugIn()
    {
        super();
    }

    public void checkIfInstalled()
    {
    }

    public String getName()
    {
        return ic.getMessage("CGMS_PLUGIN");
    }

    
    public void initPlugin()
    {
        this.commands = new String[4];
        this.commands[0] = "MN_CGMS_READ_DESC";
        this.commands[1] = "MN_CGMS_LIST_DESC";
        this.commands[2] = "MN_CGMS_CONFIG_DESC";
        this.commands[3] = "MN_CGMS_ABOUT_DESC";

        this.commands_implemented = new boolean[4];
        this.commands_implemented[0] = false;
        this.commands_implemented[1] = false;
        this.commands_implemented[2] = false;
        this.commands_implemented[3] = false;

        this.commands_will_be_done = new String[4];
        this.commands_will_be_done[0] = "0.5";
        this.commands_will_be_done[1] = "0.5";
        this.commands_will_be_done[2] = "0.5";
        this.commands_will_be_done[3] = "0.5";

    }

    public void readCGMSData()
    {
        this.featureNotImplemented(commands[0]);
    }

    public void CGMSList()
    {
        this.featureNotImplemented(commands[1]);
    }

    public void CGMSConfiguration()
    {
        this.featureNotImplemented(commands[2]);
    }

    /* 
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("cgms_read"))
        {
            this.readCGMSData();
        }
        else if (command.equals("cgms_list"))
        {
            this.executeCommand(CGMSPlugIn.COMMAND_CGMS_LIST);
        }
        else if (command.equals("cgms_config"))
        {
            this.executeCommand(CGMSPlugIn.COMMAND_CGMS_CONFIGURATION);
        }
        else if (command.equals("cgms_about"))
        {
            this.executeCommand(CGMSPlugIn.COMMAND_CGMS_ABOUT);
        }
        else
        {
            System.out.println("Wrong command for this plug-in [CGMS]: " + command);
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

    public void setReturnData(Object return_data, StatusReporterInterface stat_rep_int)
    {
    }

}
