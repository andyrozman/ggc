package main.java.ggc.pump.graph;

import java.util.Hashtable;

import ggc.plugin.graph.data.PlugInGraphConstants;
import ggc.plugin.graph.util.PlugInGraphContext;

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
 *  Filename:     PumpGraphContext  
 *  Description:  Pump Graph Context
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpGraphContext extends PlugInGraphContext
{

    Hashtable<Integer, Integer> pump_extended_values_map = null;

    /**
     * Constructor
     */
    public PumpGraphContext()
    {
        super();
    }

    @Override
    public void loadMappings()
    {

        pump_extended_values_map = new Hashtable<Integer, Integer>();

        pump_extended_values_map.put(1, -1); // activity
        pump_extended_values_map.put(2, -1); // comment
        pump_extended_values_map.put(3, PlugInGraphConstants.PUMP_BG); // bg
        pump_extended_values_map.put(4, -1); // urine
        pump_extended_values_map.put(5, PlugInGraphConstants.PUMP_CH); // ch
        pump_extended_values_map.put(6, -1); // food_db
        pump_extended_values_map.put(7, -1); // food_desc

    }

    /**
     * Object Pump: Values
     */
    public static final int OBJECT_PUMP_VALUES = 1;

    /**
     * Object Pump: Extended Values
     */
    public static final int OBJECT_PUMP_EXT_VALUES = 2;

    /**
     * Object Pump: Profiles
     */
    public static final int OBJECT_PUMP_PROFILES = 3;

    @Override
    public int getObjectMapping(int object_base_type, int type)
    {
        if (object_base_type == OBJECT_PUMP_EXT_VALUES)
        {
            if (this.pump_extended_values_map.containsKey(type))
                return this.pump_extended_values_map.get(type);
            else
                return -1;
        }
        else
            return -1;

    }

}
