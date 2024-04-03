package ggc.meter.device.accuchek.impl;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.accuchek.AccuChekSmartPixMeter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * Created by andy on 20.10.15.
 */
public class AccuChekMeterReader extends AccuChekSmartPixMeter
{

    MeterDeviceDefinition deviceType;


    public AccuChekMeterReader(MeterDeviceDefinition deviceType, DataAccessPlugInBase da)
    {
        this(deviceType, null, null, da);
    }


    // this one is only for file download
    public AccuChekMeterReader(MeterDeviceDefinition deviceType, String connectionParameter, DataAccessPlugInBase da)
    {
        this(deviceType, connectionParameter, null, da);
    }


    public AccuChekMeterReader(MeterDeviceDefinition deviceType, String connectionParameter, OutputWriter writer,
            DataAccessPlugInBase da)
    {
        super(connectionParameter, writer, da);
        this.deviceType = deviceType;
    }


    public String getName()
    {
        return this.deviceType.getDeviceName();
    }


    public String getIconName()
    {
        return this.deviceType.getIconName();
    }


    public int getDeviceId()
    {
        return this.deviceType.getDeviceId();
    }


    public String getInstructions()
    {
        return this.deviceType.getInstructionsI18nKey();
    }


    public String getComment()
    {
        return null;
    }


    public String getDeviceClassName()
    {
        return this.getClass().getName();
    }


    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory
     *
     * @return number
     */
    @Override
    public int getMaxMemoryRecords()
    {
        return this.deviceType.getMaxRecords();
    }


    /**
     * getNrOfElementsFor1s - How many elements are read in 1s (which is our refresh time)
     * @return number of elements
     */
    @Override
    public int getNrOfElementsFor1s()
    {
        switch (deviceType)
        {
            case AccuChekNano:
                return 20;

            case AccuChekAvivaCombo:
            case AccuChekAviva:
                return 12;

            case AccuChekActive:
            case AccuChekAdvantage:
            case AccuChekComfort:
            case AccuChekCompact:
            case AccuChekCompactPlus:
            case AccuChekGo:
            case AccuChekIntegra:
            case AccuChekPerforma:
            case AccuChekSensor:
                return 10;

            default:
                return 0;

        }
    }

    // public int getInterfaceTypeForMeter()
    // {
    // switch (deviceType)
    // {
    // case AccuChekAvivaCombo:
    // return MeterInterface.METER_INTERFACE_EXTENDED;
    //
    // default:
    // return MeterInterface.METER_INTERFACE_SIMPLE;
    //
    // }
    // }

}
