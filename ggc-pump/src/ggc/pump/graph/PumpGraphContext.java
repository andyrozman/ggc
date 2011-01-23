package ggc.pump.graph;

import ggc.plugin.graph.data.PlugInGraphConstants;
import ggc.plugin.graph.util.PlugInGraphContext;

import java.util.Hashtable;

public class PumpGraphContext extends PlugInGraphContext
{

    Hashtable<Integer, Integer> pump_extended_values_map = null;
    
    
    
    public PumpGraphContext()
    {
        super();
    }
    
    
    public void loadMappings()
    {
        
        pump_extended_values_map = new Hashtable<Integer,Integer>(); 
        
        pump_extended_values_map.put(1, -1); // activity
        pump_extended_values_map.put(2, -1); // comment
        pump_extended_values_map.put(3, PlugInGraphConstants.PUMP_BG); // bg
        pump_extended_values_map.put(4, -1); // urine
        pump_extended_values_map.put(5, PlugInGraphConstants.PUMP_CH); // ch
        pump_extended_values_map.put(6, -1); // food_db
        pump_extended_values_map.put(7, -1); // food_desc
        
        
    }
    
    
    public static final int OBJECT_PUMP_VALUES = 1;
    
    public static final int OBJECT_PUMP_EXT_VALUES = 2;

    public static final int OBJECT_PUMP_PROFILES = 3;
    
    
    
    
    public int getObjectMapping(int object_base_type, int type)
    {
        if (object_base_type==OBJECT_PUMP_EXT_VALUES)
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
