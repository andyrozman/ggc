package ggc.core.plugins;

import ggc.core.util.DataAccess;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.plugin.PlugInServer;

/**
 * Application: GGC - GNU Gluco Control
 * See AUTHORS for copyright information.
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * Filename: PumpsPlugIn
 * Description: Class Pump Plugin Client
 * Author: andyrozman {andy@atech-software.com}
 */

public class NutriPlugIn extends PlugInClient
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
     * Command: Load Database
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
     * private String commands[] = {
     * "MN_NUTRI_DB_USDA",
     * "MN_NUTRI_DB_USER",
     * "MN_NUTRI_DB_MEAL",
     * "MN_LOAD_DATABASE_DESC",
     * "MN_NUTRI_ABOUT",
     * "MN_OPEN_DB_SELECTOR",
     * "MN_RECALCULATE_CH};
     */

    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;

    DataAccess m_da = DataAccess.getInstance();

    /**
     * Constructor
     * 
     * @param parent
     * @param ic
     */
    public NutriPlugIn(Component parent, I18nControlAbstract ic)
    {
        super((JFrame) parent, ic);
    }

    /**
     * Constructor
     */
    public NutriPlugIn()
    {
        super();
    }

    /**
     * Check If Installed
     */
    @Override
    public void checkIfInstalled()
    {
        try
        {
            Class<?> c = Class.forName("ggc.nutri.plugin.NutriPlugInServer");

            this.m_server = (PlugInServer) c.newInstance();
            installed = true;

            this.m_server.init(this.parent, DataAccess.getInstance().getI18nControlInstance().getSelectedLanguage(),
                DataAccess.getInstance(), this, DataAccess.getInstance().getDb());

            // System.out.println("Class done");
        }
        catch (Exception ex)
        {
            this.installed = false;
            // System.out.println("Ex:" + ex);
        }
    }

    /**
     * Get Name Base (untranslated)
     * 
     * @return name of plugin
     */
    @Override
    public String getNameBase()
    {
        return "NUTRITION_PLUGIN";
    }

    /**
     * Init Plugin
     */
    @Override
    public void initPlugin()
    {
        this.commands = new String[7];
        this.commands[0] = "MN_NUTRI_DB_USDA";
        this.commands[1] = "MN_NUTRI_DB_USER";
        this.commands[2] = "MN_NUTRI_DB_MEAL";
        this.commands[3] = "MN_LOAD_DATABASE_DESC";
        this.commands[4] = "MN_NUTRI_ABOUT";
        this.commands[5] = "MN_OPEN_DB_SELECTOR";
        this.commands[6] = "MN_RECALCULATE_CH";

        this.commands_implemented = new boolean[7];
        this.commands_implemented[0] = true;
        this.commands_implemented[1] = true;
        this.commands_implemented[2] = true;
        this.commands_implemented[3] = true;
        this.commands_implemented[4] = true;
        this.commands_implemented[5] = true;
        this.commands_implemented[6] = true;

        this.commands_will_be_done = new String[7];
        this.commands_will_be_done[0] = null;
        this.commands_will_be_done[1] = null;
        this.commands_will_be_done[2] = null;
        this.commands_will_be_done[3] = null;
        this.commands_will_be_done[4] = null;
        this.commands_will_be_done[5] = null;
        this.commands_will_be_done[6] = null;

    }

    /**
     * actionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.m_server.actionPerformed(e);
        // String command = e.getActionCommand();
        // System.out.println("Wrong command for this plug-in [Nutri]: " +
        // command);
    }

    /**
     * Get When Will Be Implemented
     * 
     * @return
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return "0.5";
    }

    /**
     * Get Short Status
     * 
     * @return
     */
    @Override
    public String getShortStatus()
    {
        if (this.m_server != null)
            return String.format(ic.getMessage("STATUS_INSTALLED"), this.m_server.getVersion());
        else
            return ic.getMessage("STATUS_NOT_INSTALLED");
    }

    @SuppressWarnings("unused")
    private void refreshPanels(int mask)
    {
        // MainFrame mf = (MainFrame)parent;
        // mf.informationPanel.refreshGroup(mask);
    }

    /**
     * Set Return Data (for getting data from plugin - async)
     * 
     * @param return_data
     * @param stat_rep_int
     */
    @Override
    public void setReturnData(Object return_data, StatusReporterInterface stat_rep_int)
    {
    }

}
