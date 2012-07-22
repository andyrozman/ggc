package ggc.pump.device.minimed;

import ggc.plugin.device.impl.minimed.MinimedDevice;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.device.PumpInterface;
import ggc.pump.util.DataAccessPump;

public abstract class MinimedPumpDevice extends MinimedDevice implements PumpInterface
{

    
    public MinimedPumpDevice(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
    {
        super(da, device_type, full_port, writer);
    }
    
    public MinimedPumpDevice(DataAccessPlugInBase da, AbstractDeviceCompany cmp)
    {
        super(da, cmp);
        // TODO Auto-generated constructor stub
    }


    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public MinimedPumpDevice(AbstractDeviceCompany cmp)
    {
        super(DataAccessPump.getInstance(), cmp);
    }
    
    
    
}
