package ggc.pump.device.accuchek;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.defs.device.PumpDeviceDefinition;

/**
 * Created by andy on 20.10.15.
 */
public class AccuChekPumpReader extends AccuChekSmartPixPump
{

    PumpDeviceDefinition deviceType;


    public AccuChekPumpReader(PumpDeviceDefinition deviceType, DataAccessPlugInBase da)
    {
        super(null, null, da);
        this.deviceType = deviceType;
    }


    // this one is only for file download
    public AccuChekPumpReader(PumpDeviceDefinition deviceType, String conn_parameter, DataAccessPlugInBase da)
    {
        super(conn_parameter, da);
        this.deviceType = deviceType;
    }


    public AccuChekPumpReader(PumpDeviceDefinition deviceType, String conn_parameter, OutputWriter writer,
            DataAccessPlugInBase da)
    {
        super(conn_parameter, writer, da);
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

            case AccuChekCombo:
            case AccuChekSpirit:
                return 85;

            case AccuChekDTron:
            case DisetronicDTron:
                return 10;

            default:
                return 0;

        }
    }


    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     *
     * @return
     */
    public String getTemporaryBasalTypeDefinition()
    {
        return this.deviceType.getTempBasalType().getDefinition();
    }


    /**
     * Get Bolus Step (precission)
     *
     * @return
     */
    public float getBolusStep()
    {
        return this.deviceType.getBolusStep();
    }


    /**
     * Get Basal Step (precission)
     *
     * @return
     */
    public float getBasalStep()
    {
        return this.deviceType.getBasalStep();
    }


    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     *
     * @return
     */
    @Override
    public boolean arePumpSettingsSet()
    {
        return false;
    }

}
