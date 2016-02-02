package ggc.plugin.gui;

import javax.swing.*;

import com.atech.print.gui.PrintDialogRange;

import ggc.core.util.DataAccess;
import ggc.plugin.report.PluginReportDefinition;
import ggc.plugin.util.DataAccessPlugInBase;

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

public class PluginPrintDialog extends PrintDialogRange
{

    private static final long serialVersionUID = 1814657939095170990L;

    PluginReportDefinition reportsDefinition;


    /**
     * Constructor
     *
     * @param frame
     * @param type
     */
    public PluginPrintDialog(JFrame frame, DataAccessPlugInBase dataAccessPlugInBase,
            PluginReportDefinition reportsDefinition, int type)
    {
        super(frame, type, dataAccessPlugInBase, dataAccessPlugInBase.getI18nControlInstance(), true, false);
        this.reportsDefinition = reportsDefinition;

        System.out.println("Reports def 1: " + reportsDefinition);

        this.initDialog();
    }


    /**
     * getHelpId - get id for Help
     */
    @Override
    public String getHelpId()
    {
        return reportsDefinition.getHelpId();
    }


    /**
     * Get Pdf Viewer (path to software)
     *
     * @return
     */
    @Override
    public String getExternalPdfViewer()
    {
        return DataAccess.getInstance().getConfigurationManagerWrapper().getExternalPdfVieverPath().replace('\\', '/');
    }


    /**
     * Get Report Types
     *
     * @return
     */
    @Override
    public String[] getReportTypes()
    {
        System.out.println("Reports def 2: " + reportsDefinition);
        return reportsDefinition.getReportsNames();
    }


    /**
     * Start Printing Action
     *
     * @throws Exception
     */
    @Override
    public void startPrintingAction() throws Exception
    {
        this.reportsDefinition.startReportingAction(this);
    }


    public JComboBox getComboBoxListOfReports()
    {
        return this.cbTemplate;
    }


    @Override
    public String getExternalPdfViewerParameters()
    {
        return DataAccess.getInstance().getConfigurationManagerWrapper().getExternalPdfVieverParameters();
    }


    @Override
    public boolean isExternalPdfViewerActivated()
    {
        return DataAccess.getInstance().getConfigurationManagerWrapper().getUseExternalPdfViewer();
    }


    @Override
    public boolean disableLookAndFeelSettingForInternalPdfViewer()
    {
        return true;
    }

}
