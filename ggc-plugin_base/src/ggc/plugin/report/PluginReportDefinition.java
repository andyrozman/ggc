package ggc.plugin.report;

import javax.swing.*;

import ggc.plugin.DevicePlugInServer;
import ggc.plugin.gui.PluginPrintDialog;

/**
 * Created by andy on 17.10.15.
 */
public interface PluginReportDefinition
{

    /**
     * Get Names of reports for Plugin
     * @return
     */
    String[] getReportsNames();


    /**
     * Start printing action. We need to read getComboBoxListOfReports() to get number of selected report
     * and then start printing action.
     *
     * @param pluginPrintDialog
     */
    void startPrintingAction(PluginPrintDialog pluginPrintDialog) throws Exception;


    /**
     * Get HelpId for Help
     * 
     * @return
     */
    String getHelpId();


    /**
     * Get PlugIn Print Menus
     *
     * Menus for Printing Reports.
     *
     * @return
     */
    JMenu[] getPlugInPrintMenus(DevicePlugInServer pluginServer);


    /**
     * Start printing action from Menu
     * 
     * @param actionCommand
     */
    void startPlugInPrintMenusAction(String actionCommand);

}
