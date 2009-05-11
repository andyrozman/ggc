package ggc.pump.device.minimed;

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
 *  Filename:     MinimedCareLink
 *  Description:  Minimed CareLink "device". This is for importing data from CareLink
 *                export file
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MinimedSPMData 
{
    public static final int SOURCE_NONE = 0;
    public static final int SOURCE_PUMP = 1;
    public static final int SOURCE_CGMS = 2;
    
    public static final int VALUE_DOUBLE = 1;
    public static final int VALUE_INT = 2;
    public static final int VALUE_STRING = 3;
    public static final int VALUE_PROFILE = 4;
    
    public Hashtable<Long,Float> profiles = null;
    
    
    public MinimedSPMData(int _source, int _value_type)
    {
        this.source = _source;
        this.value_type = _value_type;
    }
    
    
    public int source = 0;
    public long datetime = 0L;
    
    public int base_type = 0;
    public int sub_type = 0;
    
    public int value_type = 0;
    public double value_dbl = 0.0d;
    public int value_int = 0;
    public String value_str = "";
    
    public String getValue()
    {
        if (value_type == VALUE_DOUBLE)
        {
            return "" + value_dbl;
        }
        else if (value_type == VALUE_INT)
        {
            return "" + value_int;
        }
        else
            return null;
    }

    
    /**
     * Add Profile 
     * 
     * @param time
     * @param value
     */
    public void addProfile(short time, float value)
    {
        long dt = 0L;
        
        int hour = (int)(time/60);
        int minute = time - (hour*60);
        
        dt = (hour * 100) + minute;
        
        if (this.profiles==null)
        {
            this.profiles = new Hashtable<Long,Float>();
        }
        
        this.profiles.put(dt, value);
        
    }
    
    
}