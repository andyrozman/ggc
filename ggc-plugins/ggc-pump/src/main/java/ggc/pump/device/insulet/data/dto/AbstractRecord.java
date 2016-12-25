package main.java.ggc.pump.device.insulet.data.dto;

import java.util.List;

import com.atech.utils.data.BitUtils;

import main.java.ggc.pump.device.insulet.data.enums.OmnipodDataType;
import main.java.ggc.pump.device.insulet.util.InsuletUtil;
import main.java.ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 19.05.15.
 */
public abstract class AbstractRecord
{

    protected BitUtils bitUtils;
    protected int length;
    protected int crc;
    private boolean isImplemented = false;
    public int[] rawData;


    // public AbstractRecord(int length)
    // {
    // this.length = length;
    // bitUtils = InsuletUtil.getBitUtils();
    // }

    public AbstractRecord(boolean isImplemented)
    {
        bitUtils = InsuletUtil.getBitUtils();
        this.isImplemented = isImplemented;
    }


    @Deprecated
    public int process(List<Integer> data, int offset)
    {
        return this.getLength();
    }


    public int process(int[] data)
    {
        length = getShort(data, 0) + 2;
        rawData = data;
        this.customProcess(data);

        crc = getShort(data, this.length - 2);

        return this.length;
    }


    public Short getShort(int offset)
    {
        return getShort(this.rawData, offset);
    }


    public Short getShort(int[] data, int offset)
    {
        Integer ii = bitUtils.toInt(data[offset], data[offset + 1]);
        return ii.shortValue();
    }


    public Short getShortInverted(int[] data, int offset)
    {
        Integer ii = bitUtils.toInt(data[offset], data[offset + 1], BitUtils.BitConversion.LITTLE_ENDIAN);
        return ii.shortValue();
    }


    public Integer getShort2Inverted(int[] data, int offset)
    {
        Integer ii = bitUtils.toInt(data[offset], data[offset + 1], data[offset + 2],
            BitUtils.BitConversion.LITTLE_ENDIAN);
        return ii.intValue();
    }


    public Short getShortInverted(int offset)
    {
        Integer ii = bitUtils.toInt(rawData[offset], rawData[offset + 1], BitUtils.BitConversion.LITTLE_ENDIAN);
        return ii.shortValue();
    }


    public Integer getInt(List<Integer> data, int offset)
    {
        return bitUtils.toInt(data.get(offset), data.get(offset + 1), data.get(offset + 2), data.get(offset + 3));
    }


    public Integer getInt(int[] data, int offset)
    {
        return bitUtils.toInt(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]);
    }


    public Integer getInt(int offset)
    {
        return getInt(this.rawData, offset);
    }


    public Byte getByte(int[] data, int offset)
    {
        Integer ii = data[offset];
        return ii.byteValue();
    }


    public String getString(List<Integer> data, int offset, int length)
    {
        return bitUtils.getString(data, offset, length).trim();
    }


    public String getString(int offset, int length)
    {
        return getString(rawData, offset, length);
    }


    public String getString(int[] data, int offset, int length)
    {
        return bitUtils.getString(data, offset, length).trim();
    }


    public String createReadableString(String data)
    {
        String outString = "";
        for (int i = 0; i < data.length(); i++)
        {
            Character c = data.charAt(i);

            int j = (int) c;

            if (j > 31 && j < 128)
            {
                outString += c;
            }
        }

        outString = outString.replace("@", "");
        outString = outString.replace("%", "");

        return outString.trim();

    }


    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " - NOT IMPLEMENTED {length=" + length + "}";
    }


    public int getLength()
    {
        return this.length;
    }


    public boolean isImplemented()
    {
        return isImplemented;
    }


    public abstract void customProcess(int[] data);


    public abstract OmnipodDataType getOmnipodDataType();


    public Integer getIntInverted(int[] data, int offset)
    {
        return bitUtils.toInt(data[offset], data[offset + 1], data[offset + 2], data[offset + 3],
            BitUtils.BitConversion.LITTLE_ENDIAN);
    }


    public Integer getIntInverted(int offset)
    {
        return getIntInverted(rawData, offset);
    }


    public Byte getByte(int offset)
    {
        return getByte(rawData, offset);
    }


    public double toDecimal(int num)
    {
        double d = num / 100.0d;
        return d;
    }


    protected String getStringInverted(int offset, int length)
    {
        return bitUtils.getStringInverted(rawData, offset, length).trim();
    }


    protected String getDecimalString(Double value, int decimalPlaces)
    {
        return DataAccessPump.DecimalFormaters[decimalPlaces].format(value);
    }

}
