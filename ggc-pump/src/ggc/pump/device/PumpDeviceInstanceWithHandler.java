package ggc.pump.device;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DeviceDefinition;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.device.v2.DeviceHandler;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.pump.data.defs.*;

import java.util.Hashtable;

/**
 * Created by andy on 10.02.15.
 */
public class PumpDeviceInstanceWithHandler extends DeviceInstanceWithHandler implements PumpInterface
{
    PumpDeviceDefinition pumpDeviceDefinition;

    public PumpDeviceInstanceWithHandler(DeviceDefinition deviceDefinition)
    {
        super(deviceDefinition);
        pumpDeviceDefinition = (PumpDeviceDefinition)deviceDefinition;
    }


    public int getMaxMemoryRecords()
    {
        return 0;
    }

    public void loadPumpSpecificValues()
    {

    }

    public Hashtable<String, PumpAlarms> getAlarmMappings()
    {
        return null;
    }

    public Hashtable<String, PumpEvents> getEventMappings()
    {
        return null;
    }

    public Hashtable<String, PumpErrors> getErrorMappings()
    {
        return null;
    }

    public Hashtable<String, PumpBolusType> getBolusMappings()
    {
        return null;
    }

    public Hashtable<String, PumpReport> getReportMappings()
    {
        return null;
    }

    public DeviceIdentification getDeviceInfo()
    {
        return null;
    }

    public String getTemporaryBasalTypeDefinition()
    {
        return null;
    }

    public float getBolusStep()
    {
        return 0;
    }

    public float getBasalStep()
    {
        return 0;
    }

    public boolean arePumpSettingsSet()
    {
        return false;
    }

    public int howManyMonthsOfDataStored()
    {
        return 0;
    }
}
