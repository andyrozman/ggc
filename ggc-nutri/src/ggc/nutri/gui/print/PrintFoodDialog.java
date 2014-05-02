package ggc.nutri.gui.print;

import ggc.core.data.DayValuesData;
import ggc.core.print.PrintAbstract;
import ggc.core.util.DataAccess;
import ggc.nutri.print.PrintFoodMenuBase;
import ggc.nutri.print.PrintFoodMenuExt1;
import ggc.nutri.print.PrintFoodMenuExt2;
import ggc.nutri.print.PrintFoodMenuExt3;
import ggc.nutri.util.DataAccessNutri;

import javax.swing.JFrame;

import com.atech.i18n.I18nControlAbstract;
import com.atech.print.PrintDialogRange;

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
    
    private String[] report_types_2 = { ic.getMessage("FOOD_MENU_BASE"),
                                       ic.getMessage("FOOD_MENU_EXT_I"),
                                       ic.getMessage("FOOD_MENU_EXT_II"),
//                                       m_ic.getMessage("FOOD_MENU_EXT_III")
                                       };


    /**
     * Dialog Options: Year and Month Option
     */
    public static final int PRINT_DIALOG_YEAR_MONTH_OPTION = 1;

    /**
     * Dialog Options: Range with day option
     */
    public static final int PRINT_DIALOG_RANGE_DAY_OPTION = 2;

    
    /**
     * Constructor
     * 
     * @param frame
     * @param type
     */
    public PrintFoodDialog(JFrame frame, int type)
    {
        super(frame, type, DataAccessNutri.getInstance(),  DataAccessNutri.getInstance().getParentI18nControlInstance(), true);
    }
    

    /**
     * Display PDF External (static method)
     * 
     * @param name
     */
/*    public static void displayPDFExternal(String name)
    {
        I18nControlAbstract ic = DataAccess.getInstance().getI18nControlInstance();

        File fl = new File(".." + File.separator + "data" + File.separator + "temp" + File.separator);

        String pdf_viewer = DataAccess.getInstance().getSettings().getPdfVieverPath().replace('\\', '/');
        String file_path = fl.getAbsolutePath().replace('\\', '/');

        if (pdf_viewer.equals(""))
        {
            System.out.println(ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
            return;
        }

        File acr = new File(pdf_viewer);

        if (!acr.exists())
        {
            System.out.println(ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
            return;
        }

        try
        {
            Runtime.getRuntime().exec(
                acr.getAbsoluteFile() + " \"" + fl.getAbsolutePath() + File.separator + name + "\"");
            System.out.println(pdf_viewer + " " + file_path + File.separator + name);
        }
        catch (RuntimeException ex)
        {
            // this.setErrorMessages(m_ic.getMessage("PDF_VIEVER_RUN_ERROR"),
            // null);
            System.out.println("RE running AcrobatReader: " + ex);
            // throw ex;
        }
        catch (Exception ex)
        {
            // this.setErrorMessages(m_ic.getMessage("PDF_VIEVER_RUN_ERROR"),
            // null);
            System.out.println("Error running AcrobatReader: " + ex);
            // throw ex;

        }
    }
*/
    

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_Food_Print";
    }


    @Override
    public String[] getReportTypes()
    {
        if (this.report_types_2==null)
        {
            report_types_2 = new String[3]; 
            report_types_2[0] = m_ic.getMessage("FOOD_MENU_BASE");
            report_types_2[1] = m_ic.getMessage("FOOD_MENU_EXT_I");
            report_types_2[2] = m_ic.getMessage("FOOD_MENU_EXT_II");
        }
        
        return report_types_2;
    }

    @Override
    public void startPrintingAction() throws Exception
    {
        DataAccessNutri da = (DataAccessNutri)m_da;
        
        DayValuesData dvd = da.getNutriDb().getDayValuesData(this.getFromDate(), this.getToDate()); //.getMonthlyValues(yr, mnth);
        
        PrintAbstract pa = null;
        
        if (this.cb_template.getSelectedIndex() == 0)
        {
            pa = new PrintFoodMenuBase(dvd);
        }
        else if (this.cb_template.getSelectedIndex() == 1)
        {
            pa = new PrintFoodMenuExt1(dvd);
        }
        else if (this.cb_template.getSelectedIndex() == 2)
        {
            pa = new PrintFoodMenuExt2(dvd);
        }
        else if (this.cb_template.getSelectedIndex() == 3)
        {
            pa = new PrintFoodMenuExt3(dvd);
        }
        
        displayPDF(pa.getRelativeNameWithPath());
        
    }

    
    /**
     * We have Secondary Type choice 
     * 
     * @return
     */
    public boolean weHaveSecondaryType()
    {
        return false;
    }
    
    
    
    /**
     * Get Pdf Viewer (path to software)
     * 
     * @return
     */
    @Override
    public String getPdfViewer()
    {
        return DataAccess.getInstance().getSettings().getPdfVieverPath().replace('\\', '/');
    }
    
    @Override
    public String getPdfViewerParameters()
    {
        // FIXME
        return "";
    }    
    
    
}
