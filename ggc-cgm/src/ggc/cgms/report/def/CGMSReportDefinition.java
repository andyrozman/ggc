package ggc.cgms.report.def;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.gui.PluginPrintDialog;
import ggc.plugin.report.PluginReportDefinition;

/**
 * Created by andy on 17.10.15.
 *
 * NOT DEPRECATED, JUST NOT IMPLEMENTED
 *
 */
@Deprecated
public class CGMSReportDefinition implements PluginReportDefinition
{

    DataAccessCGMS dataAccessCGMS; // = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl; // =
                                     // dataAccessCGMS.getI18nControlInstance();

    DevicePlugInServer pluginServer;

    public enum CGMSReportType
    {
        Simple(1), //
        Extended(2), //
        Profiles(3), //
        BasalCheck(4), //
        DailyTimesheet_Base(5), //
        DailyTimesheet_BaseCGMS(6),

        ;

        int type;


        CGMSReportType(int type)
        {
            this.type = type;
        }
    }


    public CGMSReportDefinition(DataAccessCGMS dataAccessCGMS)
    {
        this.dataAccessCGMS = dataAccessCGMS;
        this.i18nControl = dataAccessCGMS.getI18nControlInstance();
    }


    public String[] getReportsNames()
    {
        return new String[] { //
                              this.i18nControl.getMessage("PUMP_DATA_BASE"), //
                              this.i18nControl.getMessage("PUMP_DATA_EXT"), //
                              this.i18nControl.getMessage("PUMP_DATA_PROFILES"), //
                              this.i18nControl.getMessage("PUMP_DATA_BASAL_CHECK"), //
                              this.i18nControl.getMessage("PUMP_DATA_DAILY_TIMESHEET_1"), //
                              this.i18nControl.getMessage("PUMP_DATA_DAILY_TIMESHEET_2"), //
                // TIMESHEET_3 = 1 + Food
                // TIMESHEET_4 = 1 + CGMS + Food
        };
    }


    public void startPrintingAction(PluginPrintDialog pluginPrintDialog) throws Exception
    {
        // PrintAbstractIText pa = null;
        //
        // JComboBox cbTemplate = pluginPrintDialog.getComboBoxListOfReports();
        //
        // int selectedIndex = cbTemplate.getSelectedIndex();
        //
        // if (selectedIndex == 0 || selectedIndex == 1)
        // {
        // // TODO refactor to use PrintParameter
        // DeviceValuesRange dvr = DataAccessPump.getInstance().getDb()
        // .getRangePumpValues(pluginPrintDialog.getFromDateObject(),
        // pluginPrintDialog.getToDateObject());
        // // System.out.println(this.dc_from.getDate() + " " +
        // // this.dc_to.getDate());
        //
        // if (selectedIndex == 0)
        // {
        // pa = new PrintPumpDataBase(dvr);
        // }
        // else // if (selectedIndex == 1)
        // {
        // pa = new PrintPumpDataExt(dvr);
        // }
        // }
        // else
        // {
        // PrintParameters parameters = new PrintParameters();
        // parameters.setRangeFrom(pluginPrintDialog.getFromDateObject());
        // parameters.setRangeTo(pluginPrintDialog.getToDateObject());
        //
        // if (selectedIndex == 2)
        // {
        // pa = new PrintPumpDataProfiles(parameters);
        // }
        // else if (selectedIndex == 3)
        // {
        // pa = new PrintPumpBasalCheckSheet(parameters);
        // }
        // else if (selectedIndex == 4)
        // {
        // pa = new PrintPumpDataDailyTimeSheet(parameters,
        // PrintPumpDataDailyTimeSheet.PrintPumpDataDailyTimeSheetType.BaseSheet);
        // }
        // else if (selectedIndex == 5)
        // {
        // pa = new PrintPumpDataDailyTimeSheet(parameters,
        // PrintPumpDataDailyTimeSheet.PrintPumpDataDailyTimeSheetType.BaseWithCGMS);
        // }
        // }
        //
        // pluginPrintDialog.displayPDF(pa.getRelativeNameWithPath());

    }


    public String getHelpId()
    {
        return "PumpTool_Print";
    }


    /**
     * Get PlugIn Print Menus
     *
     * Since printing is also PlugIn specific we need to add Printing jobs to application.
     *
     * @return
     */
    public JMenu[] getPlugInPrintMenus(DevicePlugInServer pluginServer)
    {
        // this.pluginServer = pluginServer;
        //
        // JMenu menu_reports_pump = ATSwingUtils.createMenu("MN_PUMP",
        // "MN_PUMP_PRINT_DESC", this.i18nControl);
        //
        // addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_SIMPLE",
        // "report_print_pump_simple");
        // addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_EXT",
        // "report_print_pump_ext");
        // addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_PROFILE",
        // "report_print_profile");
        // addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_BASAL_CHECK",
        // "report_print_basal_check");
        // addPrintMenuItem(menu_reports_pump,
        // "MN_PUMP_PRINT_DAILY_TIMESHEET_1", "report_print_daily_timesheet_1");
        // addPrintMenuItem(menu_reports_pump,
        // "MN_PUMP_PRINT_DAILY_TIMESHEET_2", "report_print_daily_timesheet_2");
        //
        // JMenu[] mns = new JMenu[1];
        // mns[0] = menu_reports_pump;
        //
        // return mns;

        return null;
    }


    private void addPrintMenuItem(JMenu menuReports, String name, String actionCommand)
    {
        ATSwingUtils.createMenuItem(menuReports, name, name + "_DESC", actionCommand, pluginServer, "print.png",
            i18nControl, dataAccessCGMS, pluginServer.getParent());
    }


    public void startPlugInPrintMenusAction(String actionCommand)
    {
        // PumpReportType pumpReportType = null;
        //
        // if (actionCommand.equals("report_print_pump_simple"))
        // {
        // pumpReportType = PumpReportType.Simple;
        // }
        // else if (actionCommand.equals("report_print_pump_ext"))
        // {
        // pumpReportType = PumpReportType.Extended;
        // }
        // else if (actionCommand.equals("report_print_profile"))
        // {
        // pumpReportType = PumpReportType.Profiles;
        // }
        // else if (actionCommand.equals("report_print_basal_check"))
        // {
        // pumpReportType = PumpReportType.BasalCheck;
        // }
        // else if (actionCommand.equals("report_print_daily_timesheet_1"))
        // {
        // pumpReportType = PumpReportType.DailyTimesheet_Base;
        // }
        // else if (actionCommand.equals("report_print_daily_timesheet_2"))
        // {
        // pumpReportType = PumpReportType.DailyTimesheet_BaseCGMS;
        // }
        // else
        // {
        // System.out.println("PumpPlugInServer::Unknown Report Command: " +
        // actionCommand);
        // }
        //
        // if (pumpReportType != null)
        // {
        // new PluginPrintDialog((JFrame) pluginServer.getParent(),
        // dataAccessCGMS, this, pumpReportType.type);
        // }

    }

}
