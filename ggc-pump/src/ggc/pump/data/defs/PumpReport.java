package ggc.pump.data.defs;

import ggc.pump.util.DataAccessPump;

import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     PumpReport  
 *  Description:  Pump Report Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpReport
{
    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Report Descriptions
     */
    public String[] report_desc = { 
                       ic.getMessage("SELECT_SUBTYPE"),
                       ic.getMessage("REPORT_MISC"),             
                       ic.getMessage("REPORT_BOLUS_TOTAL_DAY"),             
                       ic.getMessage("REPORT_BASAL_TOTAL_DAY"),             
                       ic.getMessage("REPORT_INSULIN_TOTAL_DAY"),             
    };
    
    
    /**
     * Pump Report: Misc
     */
    public static final int PUMP_REPORT_MISC = 1;

    /**
     * Pump Report: Bolus Total Day
     */
    public static final int PUMP_REPORT_BOLUS_TOTAL_DAY = 2;
    
    /**
     * Pump Report: Basal Total Day
     */
    public static final int PUMP_REPORT_BASAL_TOTAL_DAY = 3;
    
    /**
     * Pump Report: Basal Total Day
     */
    public static final int PUMP_REPORT_INSULIN_TOTAL_DAY = 4;
    
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.report_desc;
    }
    

}
