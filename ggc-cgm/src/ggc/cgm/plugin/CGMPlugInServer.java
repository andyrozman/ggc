package ggc.cgm.plugin;

import ggc.cgm.util.DataAccessCGM;
import ggc.cgm.util.I18nControl;

import java.awt.Container;

import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;

public class CGMPlugInServer extends PlugInServer
{
    /**
     * Version of Meter Tool
     */
    private String cgm_tool_version = "0.0.1";
    
    public static final int COMMAND_READ_METER_DATA = 0;
    public static final int COMMAND_METERS_LIST = 1;
    public static final int COMMAND_CONFIGURATION = 2;
    
    private String commands[] = { 
            "MN_METERS_READ_DESC",
            "MN_METERS_LIST_DESC",  
            "MN_METERS_CONFIG_DESC"
            };
    
    
    
    public CGMPlugInServer()
    {
        super();
    }
    
    
    public CGMPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessCGM.getInstance().addComponent(cont);
    }
    

    
    
    
    /* 
     * executeCommand
     */
    @Override
    public void executeCommand(int command, Object obj_data)
    {
        switch(command)
        {
            case CGMPlugInServer.COMMAND_READ_METER_DATA:
            {
                this.featureNotImplemented(commands[CGMPlugInServer.COMMAND_READ_METER_DATA]);
//                DbDataReaderAbstract reader = (DbDataReaderAbstract)obj_data; 
                //new MeterInstructionsDialog(reader, this);
                return;
            }

            case CGMPlugInServer.COMMAND_METERS_LIST:
            {
                this.featureNotImplemented(commands[CGMPlugInServer.COMMAND_METERS_LIST]);
                return;
            }

            default:
            case CGMPlugInServer.COMMAND_CONFIGURATION:
            {
                this.featureNotImplemented(commands[CGMPlugInServer.COMMAND_CONFIGURATION]);
                //m_da.listComponents();
                //new SimpleConfigurationDialog(this.m_da);
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
        return ic.getMessage("METERS_PLUGIN");
    }

    /* 
     * getVersion
     */
    @Override
    public String getVersion()
    {
        return this.cgm_tool_version;
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
        I18nControl.getInstance().setLanguage(this.selected_lang);
        DataAccessCGM.getInstance().addComponent(this.parent);
    }
    
    
    
    
}


