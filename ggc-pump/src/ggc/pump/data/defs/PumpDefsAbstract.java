package ggc.pump.data.defs;

import ggc.plugin.data.DeviceDefsAbstract;
import ggc.pump.util.DataAccessPump;

public abstract class PumpDefsAbstract extends DeviceDefsAbstract 
{

    //protected DataAccessPump da = DataAccessPump.getInstance();
    //protected I18nControlAbstract ic = da.getI18nControlInstance();

    
    
    /**
     * Constructor
     */
    public PumpDefsAbstract()
    {
        this.da = DataAccessPump.getInstance();
        this.ic = da.getI18nControlInstance();
    }
    
    
    
    
}