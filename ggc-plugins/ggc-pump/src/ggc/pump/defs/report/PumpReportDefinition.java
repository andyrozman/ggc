package ggc.pump.defs.report;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.print.engine.PrintAbstractIText;
import com.atech.print.engine.PrintParameters;
import com.atech.utils.ATSwingUtils;

import ggc.plugin.DevicePlugInServer;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.gui.PluginPrintDialog;
import ggc.plugin.report.PluginReportDefinition;
import ggc.pump.report.*;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 17.10.15.
 */
public class PumpReportDefinition implements PluginReportDefinition
{

    DataAccessPump dataAccessPump; // = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl; // =
                                     // dataAccessPump.getI18nControlInstance();

    DevicePlugInServer pluginServer;

    public enum PumpReportType
    {
        Simple(1), //
        Extended(2), //
        Profiles(3), //
        BasalCheck(4), //
        DailyTimesheet_Base(5), //
        DailyTimesheet_BaseCGMS(6),

        ;

        int type;


        PumpReportType(int type)
        {
            this.type = type;
        }
    }


    public PumpReportDefinition(DataAccessPump dataAccessPump)
    {
        this.dataAccessPump = dataAccessPump;
        this.i18nControl = dataAccessPump.getI18nControlInstance();
    }


    public String[] getReportsNames()
    {
        System.out.println("Reports def: getReportsNames");
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


    public void startReportingAction(PluginPrintDialog pluginPrintDialog) throws Exception
    {
        PrintAbstractIText pa = null;

        JComboBox cbTemplate = pluginPrintDialog.getComboBoxListOfReports();

        int selectedIndex = cbTemplate.getSelectedIndex();

        if (selectedIndex == 0 || selectedIndex == 1)
        {
            // TODO refactor to use PrintParameter
            DeviceValuesRange dvr = DataAccessPump.getInstance().getDb()
                    .getRangePumpValues(pluginPrintDialog.getFromDateObject(), pluginPrintDialog.getToDateObject());
            // System.out.println(this.dc_from.getDate() + " " +
            // this.dc_to.getDate());

            if (selectedIndex == 0)
            {
                pa = new PrintPumpDataBase(dvr);
            }
            else
            // if (selectedIndex == 1)
            {
                pa = new PrintPumpDataExt(dvr);
            }
        }
        else
        {
            PrintParameters parameters = new PrintParameters();
            parameters.setRangeFrom(pluginPrintDialog.getFromDateObject());
            parameters.setRangeTo(pluginPrintDialog.getToDateObject());

            if (selectedIndex == 2)
            {
                pa = new PrintPumpDataProfiles(parameters);
            }
            else if (selectedIndex == 3)
            {
                pa = new PrintPumpBasalCheckSheet(parameters);
            }
            else if (selectedIndex == 4)
            {
                pa = new PrintPumpDataDailyTimeSheet(parameters,
                        PrintPumpDataDailyTimeSheet.PrintPumpDataDailyTimeSheetType.BaseSheet);
            }
            else if (selectedIndex == 5)
            {
                pa = new PrintPumpDataDailyTimeSheet(parameters,
                        PrintPumpDataDailyTimeSheet.PrintPumpDataDailyTimeSheetType.BaseWithCGMS);
            }
        }

        pluginPrintDialog.displayPDF(pa.getRelativeNameWithPath());

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
    public JMenu[] getPlugInReportMenus(DevicePlugInServer pluginServer)
    {

        this.pluginServer = pluginServer;

        // I18nControlAbstract icp =
        // DataAccessPump.getInstance().getI18nControlInstance();

        JMenu menu_reports_pump = ATSwingUtils.createMenu("MN_PUMP", "MN_PUMP_PRINT_DESC", this.i18nControl);

        addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_SIMPLE", "pumps_report_simple");
        addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_EXT", "pumps_report_ext");
        addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_PROFILE", "pumps_report_profile");
        addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_BASAL_CHECK", "pumps_report_basal_check");
        addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_DAILY_TIMESHEET_1", "pumps_report_daily_timesheet_1");
        addPrintMenuItem(menu_reports_pump, "MN_PUMP_PRINT_DAILY_TIMESHEET_2", "pumps_report_daily_timesheet_2");

        JMenu[] mns = new JMenu[1];
        mns[0] = menu_reports_pump;

        return mns;
    }


    private void addPrintMenuItem(JMenu menuReports, String name, String actionCommand)
    {
        ATSwingUtils.createMenuItem(menuReports, name, name + "_DESC", actionCommand, pluginServer, "print.png",
            i18nControl, dataAccessPump, pluginServer.getParent());
    }


    public void startPlugInReportMenuAction(String actionCommand)
    {
        PumpReportType pumpReportType = null;

        if (actionCommand.equals("pumps_report_simple"))
        {
            pumpReportType = PumpReportType.Simple;
        }
        else if (actionCommand.equals("pumps_report_ext"))
        {
            pumpReportType = PumpReportType.Extended;
        }
        else if (actionCommand.equals("pumps_report_profile"))
        {
            pumpReportType = PumpReportType.Profiles;
        }
        else if (actionCommand.equals("pumps_report_basal_check"))
        {
            pumpReportType = PumpReportType.BasalCheck;
        }
        else if (actionCommand.equals("pumps_report_daily_timesheet_1"))
        {
            pumpReportType = PumpReportType.DailyTimesheet_Base;
        }
        else if (actionCommand.equals("pumps_report_daily_timesheet_2"))
        {
            pumpReportType = PumpReportType.DailyTimesheet_BaseCGMS;
        }
        else
        {
            System.out.println("PumpPlugInServer::Unknown Report Command: " + actionCommand);
        }

        if (pumpReportType != null)
        {
            new PluginPrintDialog((JFrame) pluginServer.getParent(), dataAccessPump, this, pumpReportType.type);
        }

    }

}
