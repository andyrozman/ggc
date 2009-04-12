package ggc.pump.gui;

import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.print.PrintPumpDataBase;
import ggc.pump.print.PrintPumpDataExt;
import ggc.pump.util.DataAccessPump;

import javax.swing.JFrame;

import com.atech.print.PrintAbstractIText;
import com.atech.print.PrintDialogRange;

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
        super(frame, type, DataAccessPump.getInstance(), false);
    }
    

    /**
     * getHelpId - get id for Help
     */
    @Override
    public String getHelpId()
    {
        // TODO Auto-generated method stub
        return null;
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

    
    /**
     * Get Report Types
     * 
     * @return
     */
    @Override
    public String[] getReportTypes()
    {
        return new String[] { m_ic.getMessage("PUMP_DATA_BASE"), m_ic.getMessage("PUMP_DATA_EXT") };
    }

    /**
     * Start Printing Action
     * 
     * @throws Exception 
     */
    @Override
    public void startPrintingAction() throws Exception
    {
     
        DeviceValuesRange dvr = DataAccessPump.getInstance().getDb().getRangePumpValues(this.dc_from.getDateObject(), this.dc_to.getDateObject());
        //System.out.println(this.dc_from.getDate() + " " + this.dc_to.getDate());
        
        PrintAbstractIText pa = null;
        
        if (this.cb_template.getSelectedIndex() == 0)
        {
            pa = new PrintPumpDataBase(dvr);
        }
        else if (this.cb_template.getSelectedIndex() == 1)
        {
            pa = new PrintPumpDataExt(dvr);
        }
        
        
        
        displayPDF(pa.getNameWithPath());
        
        
    }
    
}
