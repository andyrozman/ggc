package main.java.ggc.pump.device.insulet.data.dto.config;

import main.java.ggc.pump.data.defs.PumpConfigurationGroup;
import main.java.ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */
public class PdmVersion extends ConfigRecord
{

    short pdmMajor;
    short pdmMinor;
    short pdmPatch;


    public PdmVersion()
    {
        super(true);
    }


    @Override
    public void customProcess(int[] data)
    {
        pdmMajor = getShort(data, 2);
        pdmMinor = getShort(data, 4);
        pdmPatch = getShort(data, 6);
    }


    @Override
    public String toString()
    {
        return "PdmVersion [" + "pdmMajor=" + pdmMajor + ", pdmMinor=" + pdmMinor + ", pdmPatch=" + pdmPatch
                + ", length=" + length + ']';
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.PdmVersion;
    }


    @Override
    public void writeConfigData()
    {
        writeSetting("PCFG_OMNI_PDM_VERSION", pdmMajor + "." + pdmMinor + "_" + pdmPatch, pdmMajor,
            PumpConfigurationGroup.Device);
    }

}
