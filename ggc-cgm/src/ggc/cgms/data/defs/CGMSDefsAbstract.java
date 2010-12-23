package ggc.cgms.data.defs;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceDefsAbstract;

public abstract class CGMSDefsAbstract extends DeviceDefsAbstract 
{

    //protected DataAccessPump da = DataAccessPump.getInstance();
    //protected I18nControlAbstract ic = da.getI18nControlInstance();

    
    
    /**
     * Constructor
     */
    public CGMSDefsAbstract()
    {
        this.da = DataAccessCGMS.getInstance();
        this.ic = da.getI18nControlInstance();
    }
    
    
    
    
}