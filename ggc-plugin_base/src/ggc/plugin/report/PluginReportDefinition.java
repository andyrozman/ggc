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
     * Start reporting action. We need to read getComboBoxListOfReports() to get number of selected report
     * and then start report action.
     *
     * @param pluginPrintDialog
     */
    void startReportingAction(PluginPrintDialog pluginPrintDialog) throws Exception;


    /**
     * Get HelpId for Help
     * 
     * @return
     */
    String getHelpId();


    /**
     * Get PlugIn Report Menus
     *
     * Menus for Printing Reports.
     *
     * @return
     */
    JMenu[] getPlugInReportMenus(DevicePlugInServer pluginServer);


    /**
     * Start report action from Menu
     * 
     * @param actionCommand
     */
    void startPlugInReportMenuAction(String actionCommand);

}
