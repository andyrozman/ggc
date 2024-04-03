package ggc.nutri.gui.print;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.print.gui.PrintDialogRange;

import ggc.core.data.DayValuesData;
import ggc.core.util.DataAccess;
import ggc.nutri.print.*;
import ggc.nutri.util.DataAccessNutri;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     PrintingDialog  
 *  Description:  Dialog for Printing (Nutrition)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this

public class PrintFoodDialog extends PrintDialogRange
{

    private static final long serialVersionUID = -7482208504803865975L;

    private I18nControlAbstract ic = DataAccessNutri.getInstance().getParentI18nControlInstance();

    private String[] report_types_2 = { ic.getMessage("FOOD_MENU_BASE"), ic.getMessage("FOOD_MENU_EXT_I"),
                                        ic.getMessage("FOOD_MENU_EXT_II"),
            // i18nControl.getMessage("FOOD_MENU_EXT_III")
    };

    DataAccessNutri dataAccessNutri;


    /**
     * Constructor
     * 
     * @param frame
     * @param type
     */
    public PrintFoodDialog(JFrame frame, int type)
    {
        // , PrintDialogType dialogType
        super(frame, type, DataAccessNutri.getInstance(), DataAccessNutri.getInstance().getParentI18nControlInstance(),
                true);
    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpId()
    {
        return "GGC_Food_Print";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getReportTypes()
    {
        if (this.report_types_2 == null)
        {
            report_types_2 = new String[3];
            report_types_2[0] = this.i18nControl.getMessage("FOOD_MENU_BASE");
            report_types_2[1] = this.i18nControl.getMessage("FOOD_MENU_EXT_I");
            report_types_2[2] = this.i18nControl.getMessage("FOOD_MENU_EXT_II");
        }

        return report_types_2;
    }


    public DataAccessNutri getDataAccessLocal()
    {
        if (dataAccessNutri == null)
        {
            dataAccessNutri = (DataAccessNutri) this.dataAccess;
        }

        return this.dataAccessNutri;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startPrintingAction() throws Exception
    {
        DataAccessNutri da = getDataAccessLocal();

        DayValuesData dvd = da.getDb().getDayValuesData(this.getFromDate(), this.getToDate()); // .getMonthlyValues(yr,
                                                                                               // mnth);

        PrintFoodMenuAbstract pa = null;

        if (this.cbTemplate.getSelectedIndex() == 0)
        {
            pa = new PrintFoodMenuBase(dvd);
        }
        else if (this.cbTemplate.getSelectedIndex() == 1)
        {
            pa = new PrintFoodMenuExt1(dvd);
        }
        else if (this.cbTemplate.getSelectedIndex() == 2)
        {
            pa = new PrintFoodMenuExt2(dvd);
        }
        else if (this.cbTemplate.getSelectedIndex() == 3)
        {
            pa = new PrintFoodMenuExt3(dvd);
        }

        displayPDF(pa.getRelativeNameWithPath());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean weHaveSecondaryType()
    {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getExternalPdfViewer()
    {
        return DataAccess.getInstance().getConfigurationManagerWrapper().getExternalPdfVieverPath().replace('\\', '/');
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getExternalPdfViewerParameters()
    {
        return DataAccess.getInstance().getConfigurationManagerWrapper().getExternalPdfVieverParameters();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExternalPdfViewerActivated()
    {
        return DataAccess.getInstance().getConfigurationManagerWrapper().getUseExternalPdfViewer();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean disableLookAndFeelSettingForInternalPdfViewer()
    {
        return true;
    }

}
