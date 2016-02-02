package ggc.plugin.graph;

import javax.swing.*;

import ggc.plugin.DevicePlugInServer;

/**
 * Created by andy on 19.12.15.
 */
public interface PluginGraphDefinition
{

    /**
     * Get PlugIn Print Menus
     *
     * Menus for Printing Reports.
     *
     * @return
     */
    JMenu[] getPlugInGraphMenus(DevicePlugInServer pluginServer);


    /**
     * Start printing action from Menu
     *
     * @param actionCommand
     */
    void startPlugInGraphMenuAction(String actionCommand);

}
