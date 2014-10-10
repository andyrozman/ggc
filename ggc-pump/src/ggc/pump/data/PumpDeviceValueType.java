package ggc.pump.data;

import ggc.plugin.data.DeviceValueTypeInterface;

public enum PumpDeviceValueType implements DeviceValueTypeInterface
{
    BASAL, //
    BOLUS, //
    BG("PUMP_ADD_DATA_BG"), //
    CH, //
    COMMENT, ;

    private String additionalKey;

    private PumpDeviceValueType()
    {
    }

    private PumpDeviceValueType(String additionalKey)
    {
        this.additionalKey = additionalKey;
    }

    public String getAdditionalKey()
    {
        return additionalKey;
    }

    public void setAdditionalKey(String additionalKey)
    {
        this.additionalKey = additionalKey;
    }

}
