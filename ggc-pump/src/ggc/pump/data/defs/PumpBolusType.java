package ggc.pump.data.defs;

import java.util.Hashtable;

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
 *  Filename:     PumpBolusType  
 *  Description:  Pump Bolus Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpBolusType extends PumpDefsAbstract
{

    //DataAccessPump da = DataAccessPump.getInstance();
    //I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Bolus Descriptions (for manual entry module)
     */
    public String[] bolus_desc = { ic.getMessage("SELECT_BOLUS_TYPE"),
                       ic.getMessage("BOLUS_STANDARD"),             
                       ic.getMessage("BOLUS_AUDIO"),             
                       ic.getMessage("BOLUS_SQUARE"),             
                       ic.getMessage("BOLUS_MULTIWAVE"),             
    };
    
    
    Hashtable<String,String> bolus_mapping = new Hashtable<String,String>(); 
    
    
    // "AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s"
    
    /**
     * Pump Bolus: None
     */
    public static final int PUMP_BOLUS_NONE = 0;

    /**
     * Pump Bolus: Standard
     */
    public static final int PUMP_BOLUS_STANDARD = 1;

    /**
     * Pump Bolus: Scrool
     */
    public static final int PUMP_BOLUS_AUDIO_SCROLL = 2;

    /**
     * Pump Bolus: Square (Insulin value through some duration)
     */
    public static final int PUMP_BOLUS_SQUARE = 3;

    /**
     * Pump Bolus: Multiwave
     */
    public static final int PUMP_BOLUS_MULTIWAVE = 4;
    

    
    
    /**
     * Pump Bolus: Dual/Normal
     */
    public static final int PUMP_BOLUS_DUAL_NORMAL = 6;

    
    /**
     * Pump Bolus: Dual/Square (Insulin value through some duration)
     */
    public static final int PUMP_BOLUS_DUAL_SQUARE = 7;
    
    
    /**
     * Constructor
     */
    public PumpBolusType()
    {
        super();
        
        this.setDataDesc(PumpBolusType.PUMP_BOLUS_STANDARD, ic.getMessage("BOLUS_STANDARD"));
        this.setDataDesc(PumpBolusType.PUMP_BOLUS_AUDIO_SCROLL, ic.getMessage("BOLUS_AUDIO"));
        this.setDataDesc(PumpBolusType.PUMP_BOLUS_SQUARE, ic.getMessage("BOLUS_SQUARE"), "AMOUNT_SQUARE=%s;DURATION=%s");
        this.setDataDesc(PumpBolusType.PUMP_BOLUS_MULTIWAVE, ic.getMessage("BOLUS_MULTIWAVE"), "AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s");
        this.setDataDesc(PumpBolusType.PUMP_BOLUS_DUAL_NORMAL, ic.getMessage("BOLUS_DUAL_NORMAL"));
        this.setDataDesc(PumpBolusType.PUMP_BOLUS_DUAL_SQUARE, ic.getMessage("BOLUS_DUAL_SQUARE"), "AMOUNT_SQUARE=%s;DURATION=%s");
        this.setDataDesc(PumpBolusType.PUMP_BOLUS_NONE, ic.getMessage("BOLUS_NONE"));
        
        this.finalizeAdding();
    }
    
    
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptionsArray()
    {
        return this.bolus_desc;
    }
    

}
