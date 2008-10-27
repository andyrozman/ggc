package ggc.meter.plugin;

import ggc.meter.gui.MeterInstructionsDialog;
import ggc.meter.gui.config.SimpleConfigurationDialog;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.list.BaseListDialog;

import java.awt.Container;

import javax.swing.JFrame;

import com.atech.db.DbDataReaderAbstract;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;

public class MeterPlugInServer extends PlugInServer
{
    /**
     * Version of Meter Tool
     */
    private String meter_tool_version = "0.3.6";
    
    public static final int COMMAND_READ_METER_DATA = 0;
    public static final int COMMAND_METERS_LIST = 1;
    public static final int COMMAND_CONFIGURATION = 2;
    public static final int COMMAND_ABOUT = 3;
    
    /*
    private String commands[] = { 
            "MN_METERS_READ_DESC",
            "MN_METERS_LIST_DESC",  
            "MN_METERS_CONFIG_DESC",
            "MN_METERS_ABOUT_DESC"
            };
    */
    
    
    
    public MeterPlugInServer()
    {
        super();
    }
    
    
    public MeterPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessMeter.getInstance().addComponent(cont);
    }
    

    
    
    
    /* 
     * executeCommand
     */
    @Override
    public void executeCommand(int command, Object obj_data)
    {
        switch(command)
        {
            case MeterPlugInServer.COMMAND_READ_METER_DATA:
            {
                DbDataReaderAbstract reader = (DbDataReaderAbstract)obj_data; 
                new MeterInstructionsDialog(reader, this);
                return;
            }

            case MeterPlugInServer.COMMAND_METERS_LIST:
            {
                new BaseListDialog((JFrame)parent, DataAccessMeter.getInstance());
                return;
            }

            
            case MeterPlugInServer.COMMAND_ABOUT:
            {
                new AboutBaseDialog((JFrame)parent, DataAccessMeter.getInstance());
                return;
            }
            
            
            default:
            case MeterPlugInServer.COMMAND_CONFIGURATION:
            {
                //m_da.listComponents();
                new SimpleConfigurationDialog(this.m_da);
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
        return this.meter_tool_version;
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
        DataAccessMeter.getInstance().addComponent(this.parent);
    }
    
    
    
    
}


