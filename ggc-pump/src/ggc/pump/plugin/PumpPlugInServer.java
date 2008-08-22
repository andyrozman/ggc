package ggc.pump.plugin;

import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.list.BaseListDialog;
import ggc.pump.gui.config.SimpleConfigurationDialog;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.awt.Container;

import javax.swing.JFrame;

import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;

public class PumpPlugInServer extends PlugInServer
{

    String plugin_version = "0.1.3";
    
    public static final int COMMAND_READ_PUMP_DATA = 0;
    public static final int COMMAND_PUMPS_LIST = 1;
    public static final int COMMAND_CONFIGURATION = 2;
    public static final int COMMAND_PROFILES = 3;
    public static final int COMMAND_MANUAL_ENTRY = 4;
    public static final int COMMAND_ADDITIONAL_DATA = 5;
    public static final int COMMAND_ABOUT = 6;
    
    private String commands[] = { 
                                  "MN_PUMPS_READ_DESC", 
                                  "MN_PUMPS_LIST_DESC", 
                                  "MN_PUMPS_CONFIG_DESC",
                                                               
                                  "MN_PUMP_PROFILES_DESC", 
                                  "MN_PUMPS_MANUAL_ENTRY_DESC",
                                  "MN_PUMPS_ADDITIONAL_DATA_DESC", 
                                  
                                  "MN_PUMPS_ABOUT" };
    
    
    
    public PumpPlugInServer()
    {
        super();
    }
    
    
    public PumpPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessPump.getInstance().addComponent(cont);
    }
    

    
    
    
    /* 
     * executeCommand
     */
    @Override
    public void executeCommand(int command, Object obj_data)
    {
        switch(command)
        {

            case PumpPlugInServer.COMMAND_CONFIGURATION:
            {
                new SimpleConfigurationDialog(this.m_da);
                return;
            }
            
            case PumpPlugInServer.COMMAND_PUMPS_LIST:
            {
                new BaseListDialog((JFrame)this.parent, DataAccessPump.getInstance());
                return;
            }

            case PumpPlugInServer.COMMAND_ABOUT:
            {
                new AboutBaseDialog((JFrame)this.parent, DataAccessPump.getInstance());
                return;
            }

            
            
            default:
            {
                this.featureNotImplemented(commands[command]);
                return;
            }
        }
        
    }

    /* 
     * getName
     */
    @Override
    public String getName()
    {
        return ic.getMessage("PUMP_PLUGIN");
    }

    /* 
     * getVersion
     */
    @Override
    public String getVersion()
    {
        return this.plugin_version;
    }

    /* 
     * getWhenWillBeImplemented
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return "0.4";
    }

    /* 
     * initPlugIn
     */
    @Override
    public void initPlugIn()
    {
        ic = m_da.getI18nControlInstance();
        //ic = I18nControl.getInstance();
        //ic.setLanguage(this.selected_lang);
        //ic = I18nControl.getInstance();
        //ic.setLanguage(this.selected_lang);
        I18nControl.getInstance().setLanguage(this.selected_lang);
        DataAccessPump.getInstance().addComponent(this.parent);
    }
    
    
    
    
}


