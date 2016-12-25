package main.java.ggc.pump.device.insulet.data.enums;

/**
 * Created by andy on 22.05.15.
 */
public enum BGFlags
{

    MANUAL_FLAG(0x01), //
    TEMPERATURE_FLAG(0x02), //
    BELOW_TARGET_FLAG(0x04), //
    ABOVE_TARGET_FLAG(0x08), //
    RANGE_ERROR_LOW_FLAG(0x10), //
    RANGE_ERROR_HIGH_FLAG(0x20), //
    OTHER_ERROR_FLAG(0x40), ;

    private Byte code;


    BGFlags(Integer code)
    {
        this.code = code.byteValue();
    }


    public Byte getCode()
    {
        return code;
    }
}
