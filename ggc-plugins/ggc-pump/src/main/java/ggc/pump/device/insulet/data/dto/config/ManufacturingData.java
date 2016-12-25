package main.java.ggc.pump.device.insulet.data.dto.config;

import main.java.ggc.pump.data.defs.PumpConfigurationGroup;
import main.java.ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */

public class ManufacturingData extends ConfigRecord
{

    String dataMnf;


    public ManufacturingData()
    {
        super(true);
    }


    @Override
    public void customProcess(int[] data)
    {
        dataMnf = getString(data, 2, this.length - 4);
    }


    @Override
    public String toString()
    {
        return "ManufacturingData {" + "dataMnf='" + dataMnf + '\'' + ", length=" + this.length + "}";
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.ManufacturingData;
    }


    @Override
    public void writeConfigData()
    {
        writeSetting("PCFG_OMNI_MANUFACTURING_DATA", dataMnf, dataMnf, PumpConfigurationGroup.Device);
    }
}
