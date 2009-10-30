package ggc.nutri.plugin;

import ggc.core.util.DataAccess;
import ggc.nutri.data.GGCTreeRoot;
import ggc.nutri.db.GGCDbNutri;
import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.db.datalayer.Meal;
import ggc.nutri.db.datalayer.MealGroup;
import ggc.nutri.dialogs.DailyValuesMealSelectorDialog;
import ggc.nutri.dialogs.NutritionTreeDialog;
import ggc.nutri.gui.print.PrintFoodDialog;
import ggc.nutri.panels.PanelMealSelector;
import ggc.nutri.util.DataAccessNutri;
import ggc.nutri.util.I18nControl;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.BackupRestorePlugin;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;

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
 *  Filename:     NutriPlugInServer  
 *  Description:  PlugIn Server for Nutrition Plugin
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class NutriPlugInServer extends PlugInServer implements ActionListener
{

    /**
     * Command: Db USDA Tree
     */
    public static final int COMMAND_DB_USDA = 0;
    
    
    /**
     * Command: Db User Tree
     */
    public static final int COMMAND_DB_USER = 1;
    
    /**
     * Command: Db Meal Tree
     */
    public static final int COMMAND_DB_MEAL = 2;
    
    /**
     *  Command: Load Database  
     */
    public static final int COMMAND_LOAD_DATABASE = 3;
    
    /**
     * Command: About
     */
    public static final int COMMAND_ABOUT = 4;
    
    /**
     * Command: Food Selector
     */
    public static final int COMMAND_DB_FOOD_SELECTOR = 5;
    
    /**
     * Command: Recalculate CH
     */
    public static final int COMMAND_RECALCULATE_CH = 6;
    
    
    
/*    
    private String commands[] = { 
                                  "MN_NUTRI_READ_DESC", 
                                  "MN_NUTRI_LIST_DESC", 
                                  "MN_NUTRI_CONFIG_DESC",
                                                               
                                  "MN_LOAD_DATABASE_DESC", 
                                  "MN_NUTRI_ABOUT" };
  */  
    
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
        
            case NutriPlugInServer.COMMAND_LOAD_DATABASE:
            {
                this.loadDb();
            } break;
        
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
        
        DataAccessNutri da = DataAccessNutri.getInstance();
        da.addComponent(this.parent);
        da.setHelpContext(this.m_da.getHelpContext());
        da.setPlugInServerInstance(this);
//        da.createDb(m_da.getHibernateDb());
//        da.initAllObjects();
        da.loadSpecialParameters();
        
        GGCDbNutri _db = new GGCDbNutri(((DataAccess)m_da).getDb());
        da.setNutriDb(_db);
        
        this.backup_restore_enabled = true;
        m_da.loadSpecialParameters();
        //System.out.println("PumpServer: " + m_da.getSpecialParameters().get("BG"));
        
        //da.setBGMeasurmentType(m_da.getIntValueFromString(m_da.getSpecialParameters().get("BG")));
    }
    
    
    /**
     * Load Db
     */
    public void loadDb()
    {
        DataAccessNutri.getInstance().getNutriDb().loadNutritionDbBase();
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


    /**
     * Get Backup Objects (if available)
     * 
     * @return
     */
    @Override
    public BackupRestoreCollection getBackupObjects()
    {
        I18nControlAbstract ic_loc = ggc.nutri.util.I18nControl.getInstance(); 
        BackupRestoreCollection brc_nut = new BackupRestoreCollection("NUTRITION_OBJECTS", ic_loc);
        brc_nut.addNodeChild(new FoodGroup(ic_loc));
        brc_nut.addNodeChild(new FoodDescription(ic_loc));
        brc_nut.addNodeChild(new MealGroup(ic_loc));
        brc_nut.addNodeChild(new Meal(ic_loc));
        
        return brc_nut;
    }


    /**
     * Get PlugIn Main Menu 
     * 
     * This is new way to handle everything, previously we used to pass ActionListener items through
     * plugin framework, but in new way, we will use this one. We just give main application menu,
     * which contains all items accessible through menus.
     *  
     * @return
     */
    @Override
    public JMenu getPlugInMainMenu()
    {
        // food menu
        JMenu menu_food = ATSwingUtils.createMenu("MN_FOOD", null, ic);
        ATSwingUtils.createMenuItem(menu_food, 
                                    "MN_NUTRDB_USDB", 
                                    "MN_NUTRDB_USDB_DESC", 
                                    "food_nutrition_1", 
                                    this, null, 
                                    ic, DataAccessNutri.getInstance(), parent);
        //.createAction(this.menu_food, "MN_NUTRDB_USDB", "MN_NUTRDB_USDB_DESC", "food_nutrition_1", null);
        menu_food.addSeparator();

        ATSwingUtils.createMenuItem(menu_food, 
            "MN_NUTRDB_USER", 
            "MN_NUTRDB_USER_DESC", 
            "food_nutrition_2", 
            this, null, 
            ic, DataAccessNutri.getInstance(), parent);
        
//        this.createAction(this.menu_food, "MN_NUTRDB_USER", "MN_NUTRDB_USER_DESC", "food_nutrition_2", null);
        menu_food.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_food, 
            "MN_MEALS", 
            "MN_MEALS_DESC", 
            "food_meals", 
            this, null, 
            ic, DataAccessNutri.getInstance(), parent);
                
//        this.createAction(this.menu_food, "MN_MEALS", "MN_MEALS_DESC", "food_meals", null);
        
        return menu_food;
    }


    /**
     * Get PlugIn Print Menus 
     * 
     * Since printing is also PlugIn specific we need to add Printing jobs to application.
     *  
     * @return
     */
    @Override
    public JMenu[] getPlugInPrintMenus()
    {
        JMenu menu_reports_foodmenu = ATSwingUtils.createMenu("MN_FOODMENU", "MN_FOODMENU_DESC", ic);
        
        ATSwingUtils.createMenuItem(menu_reports_foodmenu, 
            "MN_FOODMENU_SIMPLE", 
            "MN_FOODMENU_SIMPLE_DESC", 
            "report_foodmenu_simple",
            this, "print.png", 
            ic, DataAccessNutri.getInstance(), parent);
        
        
        ATSwingUtils.createMenuItem(menu_reports_foodmenu,
            "MN_FOODMENU_EXT1", 
            "MN_FOODMENU_EXT1_DESC", 
            "report_foodmenu_ext1", 
            this, "print.png", 
            ic, DataAccessNutri.getInstance(), parent);
        
        
        ATSwingUtils.createMenuItem(menu_reports_foodmenu,
            "MN_FOODMENU_EXT2", 
            "MN_FOODMENU_EXT2_DESC", 
            "report_foodmenu_ext2", 
            this, "print.png", 
            ic, DataAccessNutri.getInstance(), parent);
        
        JMenu[] mns = new JMenu[1];
        mns[0] = menu_reports_foodmenu;
        
        return mns;
    }


    /** 
     * Action Performed
     */
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        
        if (command.equals("food_nutrition_1"))
        {
            new NutritionTreeDialog((JFrame)parent, DataAccessNutri.getInstance(), GGCTreeRoot.TREE_USDA_NUTRITION);
        }
        else if (command.equals("food_nutrition_2"))
        {
            new NutritionTreeDialog((JFrame)parent, DataAccessNutri.getInstance(), GGCTreeRoot.TREE_USER_NUTRITION);
        }
        else if (command.equals("food_meals"))
        {
            new NutritionTreeDialog((JFrame)parent, DataAccessNutri.getInstance(), GGCTreeRoot.TREE_MEALS);
        }
        else if (command.equals("report_foodmenu_simple"))
        {
            new PrintFoodDialog((JFrame)parent, 1, PrintFoodDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
        }
        else if (command.equals("report_foodmenu_ext1"))
        {
            new PrintFoodDialog((JFrame)parent, 2, PrintFoodDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
        }
        else if (command.equals("report_foodmenu_ext2"))
        {
            new PrintFoodDialog((JFrame)parent, 3, PrintFoodDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
        }
        /*else if (command.equals("report_foodmenu_ext3"))
        {
            // disabled for now, until it's implement to fully function
            new PrintingDialog(MainFrame.this, 4, PrintingDialog.PRINT_DIALOG_RANGE_DAY_OPTION);
        } */
        
        
    }

    
    
    

    /**
     * Execute Command Dialog Return - This one executes command that starts dialog, with
     *   dialog as parent, and supply of Object as input data. Input data can be anything
     *   even ArrayList of data. As returning parameters we get array of Object, or null
     *   if action was unsuccessful
     * 
     * @param dialog parent dialog
     * @param command command id (specific to plugin)
     * @param data as Object (can be ArrayList)
     * 
     * @return Array of Objects or null
     */
    public Object[] executeCommandDialogReturn(JDialog dialog, int command, Object data)
    {
        if ((command!=NutriPlugInServer.COMMAND_DB_FOOD_SELECTOR) &&
                (command!=NutriPlugInServer.COMMAND_RECALCULATE_CH))
        {
            System.out.println("ExecuteCommandDialogReturn[" + getName() + "] is not valid for this command: " + command);
            return null;
        }
        else
        {
            if (command==NutriPlugInServer.COMMAND_DB_FOOD_SELECTOR)
            {
                DailyValuesMealSelectorDialog dvms = new DailyValuesMealSelectorDialog(this.m_da, dialog, (String)data);

                if (dvms.wasAction())
                {
                    Object[] ret = new Object[2];
                    ret[0] = dvms.getStringForDb();
                    ret[1] = dvms.getCHSum().replace(',', '.');
                    
                    return ret;
                }
                else
                    return null;
            }
            else if (command==NutriPlugInServer.COMMAND_RECALCULATE_CH)
            {
                PanelMealSelector pms = new PanelMealSelector(dialog, null, (String)data);
                
                Object[] ret = new Object[2];
                ret[0] = pms.getCHSumString();
                
                return ret;
            }
            else
                return null;
        }
    }
    
    
    
    /**
     * Get Backup Restore Handler
     * 
     * @return
     */
    public BackupRestorePlugin getBackupRestoreHandler()
    {
        return new BackupRestoreNutriHandler();
    }
    
    
}