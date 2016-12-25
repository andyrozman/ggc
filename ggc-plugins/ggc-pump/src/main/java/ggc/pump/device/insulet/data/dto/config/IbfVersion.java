package main.java.ggc.pump.device.insulet.data.dto.config;

import main.java.ggc.pump.data.defs.PumpConfigurationGroup;
import main.java.ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */
public class IbfVersion extends ConfigRecord
{

    // 6S8z8z
    int ibfMajor;
    int ibfMinor;
    int ibfPatch;
    int engMajor;
    int engMinor;
    int engPatch;

    String vendorId;
    String productId;


    public IbfVersion()
    {
        super(true);
    }


    // @Override
    // public int process(List<Integer> data, int offset)
    // {
    // System.out.println("1: " + data.get(0));
    // System.out.println("2: " + data.get(1));
    //
    // length = getShort(data, offset) + 2;
    //
    // ibfMajor = getShort(data, offset + 2);
    // ibfMinor = getShort(data, offset + 4);
    // ibfPatch = getShort(data, offset + 6);
    // engMajor = getShort(data, offset + 8);
    // engMinor = getShort(data, offset + 10);
    // engPatch = getShort(data, offset + 12);
    //
    // vendorId = getString(data, offset + 14, 8);
    // productId = getString(data, offset + 22, 8);
    //
    // crc = getShort(data, offset + 30);
    //
    // return length;
    // }

    // public int process(int[] data)
    // {
    // // System.out.println("1: " + data.get(0));
    // // System.out.println("2: " + data.get(1));
    //
    // length = getShort(data, 0) + 2;
    //
    // return length;
    // }

    @Override
    public void customProcess(int[] data)
    {
        ibfMajor = getShort(data, 2);
        ibfMinor = getShort(data, 4);
        ibfPatch = getShort(data, 6);
        engMajor = getShort(data, 8);
        engMinor = getShort(data, 10);
        engPatch = getShort(data, 12);

        vendorId = getString(data, 14, 8);
        productId = getString(data, 22, 8);
    }


    @Override
    public String toString()
    {
        return "IbfVersion {" + "ibfMajor=" + ibfMajor + ", ibfMinor=" + ibfMinor + ", ibfPatch=" + ibfPatch
                + ", engMajor=" + engMajor + ", engMinor=" + engMinor + ", engPatch=" + engPatch + ", vendorId='"
                + vendorId + '\'' + ", productId='" + productId + '\'' + ", length=" + length + "' }";
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.IbfVersion;
    }


    @Override
    public void writeConfigData()
    {
        writeSetting("PCFG_OMNI_IBF_VERSION", ibfMajor + "." + ibfMinor + "_" + ibfPatch, ibfMajor,
            PumpConfigurationGroup.Device);

        // IbfVersion {ibfMajor=0, ibfMinor=1, ibfPatch=7, engMajor=0,
        // engMinor=1,
        // engPatch=7, vendorId='Insulet', productId='OmniPod', length=32' }
    }
}
