package ggc.nutri.plugin;

import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.db.datalayer.Meal;
import ggc.nutri.db.datalayer.MealGroup;
import ggc.nutri.util.DataAccessNutri;
import ggc.nutri.util.I18nControl;

import java.awt.Container;

import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class NutriPlugInServer extends PlugInServer
{

    //String plugin_version = "0.1.7.1";
    
    /**
     *  Command: Read Pump Data  
     */
    public static final int COMMAND_READ_PUMP_DATA = 0;
    
    /**
     *  Command: Pumps List  
     */
    public static final int COMMAND_PUMPS_LIST = 1;
    
    /**
     *  Command: Configuration  
     */
    public static final int COMMAND_CONFIGURATION = 2;
    
    /**
     *  Command: Profiles  
     */
    public static final int COMMAND_PROFILES = 3;
    
    /**
     *  Command: Manual Entry 
     */
    public static final int COMMAND_MANUAL_ENTRY = 4;
    
    /**
     *  Command: Additional Data  
     */
    public static final int COMMAND_ADDITIONAL_DATA = 5;
    
    /**
     *  Command: About  
     */
    public static final int COMMAND_ABOUT = 6;
    
    
    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;
    
    
    private String commands[] = { 
                                  "MN_PUMPS_READ_DESC", 
                                  "MN_PUMPS_LIST_DESC", 
                                  "MN_PUMPS_CONFIG_DESC",
                                                               
                                  "MN_PUMP_PROFILES_DESC", 
                                  "MN_PUMPS_MANUAL_ENTRY_DESC",
                                  "MN_PUMPS_ADDITIONAL_DATA_DESC", 
                                  
                                  "MN_PUMPS_ABOUT" };
    
    
    //I18nControl ic = I18nControl.getInstance();
    
    
    /**
     * Constructor
     */
    public NutriPlugInServer()
    {
        super();
    }
    
    
    /**
     * Constructor
     * 
     * @param cont
     * @param selected_lang
     * @param da
     */
    public NutriPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessNutri.getInstance().addComponent(cont);
        //DataAccessPump.getInstance().setPlugInServerInstance(this);
        //DataAccessPump.getInstance().m
    }
    

    
    
    
    /**
     * Execute Command on Server Side
     * 
     * @param command
     */
    @Override
    public void executeCommand(int command, Object obj_data)
    {
        switch(command)
        {
/*
            case PumpPlugInServer.COMMAND_CONFIGURATION:
            {
                new DeviceConfigurationDialog((JFrame)this.parent, DataAccessPump.getInstance());
                //new SimpleConfigurationDialog(this.m_da);
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

            case PumpPlugInServer.COMMAND_PROFILES:
            {
                System.out.println("parent: " + this.parent);
                new ProfileSelector(DataAccessPump.getInstance(), this.parent);
                return;
            }
            
            
            case PumpPlugInServer.COMMAND_MANUAL_ENTRY:
            case PumpPlugInServer.COMMAND_ADDITIONAL_DATA:
            {
                new PumpDataDialog(DataAccessPump.getInstance(), this.parent);
                return;
            } 
            
            default:
            {
                this.featureNotImplemented(commands[command]);
                return;
            }
            */
        }
        
    }

    /**
     * Get Name of plugin
     * 
     * @return
     */
    @Override
    public String getName()
    {
        return ic.getMessage("NUTRITION_PLUGIN");
    }

    /**
     * Get Version of plugin
     * 
     * @return
     */
    @Override
    public String getVersion()
    {
        return DataAccessNutri.PLUGIN_VERSION;
    }

    /**
     * Get Information When will it be implemented
     * 
     * @return
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return "0.4";
    }

    /**
     * Init PlugIn which needs to be implemented 
     */
    @Override
    public void initPlugIn()
    {
        ic = m_da.getI18nControlInstance();
        I18nControl.getInstance().setLanguage(this.selected_lang);
        
        //System.out.println("initPlugIn");
        
        DataAccessNutri da = DataAccessNutri.getInstance();
        da.addComponent(this.parent);
        da.setHelpContext(this.m_da.getHelpContext());
        da.setPlugInServerInstance(this);
//        da.createDb(m_da.getHibernateDb());
//        da.initAllObjects();
        da.loadSpecialParameters();
        
        m_da.loadSpecialParameters();
        //System.out.println("PumpServer: " + m_da.getSpecialParameters().get("BG"));
        
        //da.setBGMeasurmentType(m_da.getIntValueFromString(m_da.getSpecialParameters().get("BG")));
    }
    
    
    /**
     * Get Return Object
     * 
     * @param ret_obj_id
     * @return
     */
    @Override
    public Object getReturnObject(int ret_obj_id)
    {
        return null;
    }


    @Override
    public BackupRestoreCollection getBackupObjects()
    {
        BackupRestoreCollection brc_nut = new BackupRestoreCollection("NUTRITION_OBJECTS", this.ic);
        brc_nut.addNodeChild(new FoodGroup(this.ic));
        brc_nut.addNodeChild(new FoodDescription(this.ic));
        brc_nut.addNodeChild(new MealGroup(this.ic));
        brc_nut.addNodeChild(new Meal(this.ic));
        
        return brc_nut;
    }


    @Override
    public JMenu getPlugInMainMenu()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public JMenu getPlugInPrintMenu()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}