package ggc.pump.data;

import ggc.plugin.data.DeviceValueTypeInterface;

public enum PumpDeviceValueType implements DeviceValueTypeInterface
{
    BASAL("BASAL_COLOR"), //
    BOLUS("BOLUS_COLOR"), //
    BG("BG_COLOR"), //
    CH("CH_COLOR"), //
    PUMP_ADDITIONAL_DATA("PUMP_ADD_DATA_COLOR"), //
    COMMENT("");

    private String colorKey;

    private PumpDeviceValueType()
    {
    }

    private PumpDeviceValueType(String colorKey)
    {
        this.colorKey = colorKey;
    }

    public String getColorKey()
    {
        return colorKey;
    }

    public void setColorKey(String colorKey)
    {
        this.colorKey = colorKey;
    }

}
