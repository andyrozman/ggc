package ggc.pump.gui;

import javax.swing.*;

import com.atech.print.engine.PrintAbstractIText;
import com.atech.print.engine.PrintParameters;
import com.atech.print.gui.PrintDialogRange;

import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.print.*;
import ggc.pump.util.DataAccessPump;

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
 *  Filename:     PumpPrintDialog
 *  Description:  Dialog for printing Pump reports
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public class PumpPrintDialog extends PrintDialogRange
{

    private static final long serialVersionUID = 8767665433469855349L;

    public static enum PumpReportType
    {
        Simple(1), //
        Extended(2), //
        Profiles(3), //
        BasalCheck(4), //
        DailyTimesheet_1(5);

        int type;


        PumpReportType(int type)
        {
            this.type = type;
        }
    }

    /**
     * Pump Report: Simple
     */
    public static final int PUMP_REPORT_SIMPLE = 1;

    /**
     * Pump Report: Extended
     */
    public static final int PUMP_REPORT_EXTENDED = 2;


    /**
     * Constructor
     *
     * @param frame
     * @param type
     */
    public PumpPrintDialog(JFrame frame, int type)
    {
        super(frame, type, DataAccessPump.getInstance(), true);
    }


    public PumpPrintDialog(JFrame frame, PumpReportType type)
    {
        super(frame, type.type, DataAccessPump.getInstance(), true);
    }


    /**
     * getHelpId - get id for Help
     */
    @Override
    public String getHelpId()
    {
        return "PumpTool_Print";
    }


    /**
     * Get Pdf Viewer (path to software)
     *
     * @return
     */
    @Override
    public String getExternalPdfViewer()
    {
        return DataAccess.getInstance().getSettings().getExternalPdfVieverPath().replace('\\', '/');
    }


    /**
     * Get Report Types
     *
     * @return
     */
    @Override
    public String[] getReportTypes()
    {
        // FIXME

        return new String[] { this.i18nControl.getMessage("PUMP_DATA_BASE"),
                             this.i18nControl.getMessage("PUMP_DATA_EXT"), //
                             this.i18nControl.getMessage("PUMP_DATA_PROFILES"), //
                             this.i18nControl.getMessage("PUMP_DATA_BASAL_CHECK"), //
                             this.i18nControl.getMessage("PUMP_DATA_DAILY_TIMESHEET_1"), //
        // "Daily Table Report II (Events,Color)", //
        };
    }


    /**
     * Start Printing Action
     *
     * @throws Exception
     */
    @Override
    public void startPrintingAction() throws Exception
    {

        PrintAbstractIText pa = null;

        if (this.cbTemplate.getSelectedIndex() == 0 || this.cbTemplate.getSelectedIndex() == 1)
        {
            DeviceValuesRange dvr = DataAccessPump.getInstance().getDb()
                    .getRangePumpValues(this.getFromDateObject(), this.getToDateObject());
            // System.out.println(this.dc_from.getDate() + " " +
            // this.dc_to.getDate());

            if (this.cbTemplate.getSelectedIndex() == 0)
            {
                pa = new PrintPumpDataBase(dvr);
            }
            else if (this.cbTemplate.getSelectedIndex() == 1)
            {
                pa = new PrintPumpDataExt(dvr);
            }
        }
        else
        {
            // FIXME add to PrintParameters class
            PrintParameters parameters = new PrintParameters();
            parameters.put("RANGE_FROM", this.getFromDateObject());
            parameters.put("RANGE_TO", this.getToDateObject());

            if (this.cbTemplate.getSelectedIndex() == 2)
            {
                pa = new PrintPumpDataProfiles(parameters);
            }
            else if (this.cbTemplate.getSelectedIndex() == 3)
            {
                pa = new PrintPumpBasalCheckSheet(parameters);
            }
            else if (this.cbTemplate.getSelectedIndex() == 4)
            {
                pa = new PrintPumpDataDailyTimeSheet(parameters);
            }
        }

        displayPDF(pa.getRelativeNameWithPath());

    }


    @Override
    public String getExternalPdfViewerParameters()
    {
        return DataAccess.getInstance().getSettings().getExternalPdfVieverParameters();
    }


    @Override
    public boolean isExternalPdfViewerActivated()
    {
        return DataAccess.getInstance().getSettings().getUseExternalPdfViewer();
    }


    @Override
    public boolean disableLookAndFeelSettingForInternalPdfViewer()
    {
        return true;
    }

}
