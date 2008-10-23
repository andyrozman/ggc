package ggc.pump.data.defs;

import java.util.Hashtable;

import ggc.pump.util.DataAccessPump;

import com.atech.i18n.I18nControlAbstract;



/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterInterface
 *  Purpose:  This is interface class, used for meters. It should be primary implemented by protocol class, and 
 *       protocol class should be used as super class for meter definitions. Each meter family "should" 
 *       have it's own super class and one class for each meter.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 */


public class PumpBasalSubType
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    public String[] basal_desc = { ic.getMessage("SELECT_BASAL_TYPE"),
                       ic.getMessage("BASAL_VALUE"),             
                       ic.getMessage("BASAL_PROFILE"),             
                       ic.getMessage("BASAL_TEMPORARY_BASAL_RATE"),             
                       ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_PROFILE"),             
                       ic.getMessage("BASAL_PUMP_STATUS"),             
    };
    
    
    Hashtable<String,String> basal_mapping = new Hashtable<String,String>(); 
    
    public static final int PUMP_BASAL_VALUE = 1;
    public static final int PUMP_BASAL_PROFILE = 2;
    public static final int PUMP_BASAL_TEMPORARY_BASAL_RATE = 3;
    public static final int PUMP_BASAL_TEMPORARY_BASAL_RATE_PROFILE = 4;
    public static final int PUMP_BASAL_PUMP_STATUS = 5;

    
    public PumpBasalSubType()
    {
        this.basal_mapping.put(ic.getMessage("BASAL_VALUE"), "1");             
        this.basal_mapping.put(ic.getMessage("BASAL_PROFILE"), "2");             
        this.basal_mapping.put(ic.getMessage("BASAL_TEMPORARY_BASAL_RATE"), "3");             
        this.basal_mapping.put(ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_PROFILE"), "4");             
        this.basal_mapping.put(ic.getMessage("BASAL_ON_OFF"), "5");             
    }
    
    
    public int getTypeFromDescription(String str)
    {
        String s = "0";
        
        if (this.basal_mapping.containsKey(str))
            s = this.basal_mapping.get(str);
        
        return Integer.parseInt(s);
    }
    
    public String[] getDescriptions()
    {
        return this.basal_desc;
    }
    
    
}
