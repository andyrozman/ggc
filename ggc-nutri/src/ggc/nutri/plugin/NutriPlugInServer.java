package ggc.nutri.plugin;

import ggc.core.util.DataAccess;
import ggc.nutri.data.GGCTreeRoot;
import ggc.nutri.db.GGCDbNutri;
import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.db.datalayer.Meal;
import ggc.nutri.db.datalayer.MealGroup;
import ggc.nutri.dialogs.NutritionTreeDialog;
import ggc.nutri.gui.print.PrintFoodDialog;
import ggc.nutri.util.DataAccessNutri;
import ggc.nutri.util.I18nControl;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class NutriPlugInServer extends PlugInServer implements ActionListener
{

    /**
     *  Command: Load Database  
     */
    public static final int COMMAND_LOAD_DATABASE = 4;
    
    
    
    private String commands[] = { 
                                  "MN_NUTRI_READ_DESC", 
                                  "MN_NUTRI_LIST_DESC", 
                                  "MN_NUTRI_CONFIG_DESC",
                                                               
                                  "MN_LOAD_DATABASE_DESC", 
                                  "MN_NUTRI_ABOUT" };
    
    
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
            
            //this.
        }
        
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
        
        GGCDbNutri db = new GGCDbNutri(((DataAccess)m_da).getDb());
        da.setNutriDb(db);
        
        
        m_da.loadSpecialParameters();
        //System.out.println("PumpServer: " + m_da.getSpecialParameters().get("BG"));
        
        //da.setBGMeasurmentType(m_da.getIntValueFromString(m_da.getSpecialParameters().get("BG")));
    }
    
    
    public void loadDb()
    {
        //GGCDbNutri db = new GGCDbNutri(((DataAccess)m_da).getDb());
        //db.loadNutritionDatabase();
        
        DataAccessNutri.getInstance().getNutriDb().loadNutritionDatabase();
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
        BackupRestoreCollection brc_nut = new BackupRestoreCollection("NUTRITION_OBJECTS", this.ic);
        brc_nut.addNodeChild(new FoodGroup(this.ic));
        brc_nut.addNodeChild(new FoodDescription(this.ic));
        brc_nut.addNodeChild(new MealGroup(this.ic));
        brc_nut.addNodeChild(new Meal(this.ic));
        
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

    
    
}