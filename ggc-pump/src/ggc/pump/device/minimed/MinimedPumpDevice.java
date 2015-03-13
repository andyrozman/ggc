package ggc.pump.device.minimed;

import ggc.plugin.device.impl.minimed.MinimedDevice;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.*;
import ggc.pump.device.PumpInterface;
import ggc.pump.util.DataAccessPump;

import java.util.Hashtable;

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

    /**
     * Get Alarm Mappings - Map pump specific alarms to Pump Tool specific
     *     alarm codes
     * @return
     */
    public Hashtable<String, PumpAlarms> getAlarmMappings()
    {
        return null;
    }

    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpBolusType> getBolusMappings()
    {
        return null;
    }

    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpErrors> getErrorMappings()
    {
        return null;
    }

    /**
     * Get Event Mappings - Map pump specific events to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpEvents> getEventMappings()
    {
        return null;
    }

    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpReport> getReportMappings()
    {
        return null;
    }

    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
    }

    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }
}
