package ggc.plugin.device.impl.animas.data;

import java.util.ArrayList;
import java.util.List;

import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;

/**
 * Created by andy on 06.04.15.
 */
public class AnimasDeviceReplyPacket
{

    public AnimasDataType dataTypeObject;

    public List<Short> dataReceived = new ArrayList<Short>();

    public int dataReceivedLength;


    // List<AnimasPreparedDataEntry> preparedData = new
    // ArrayList<AnimasPreparedDataEntry>();

    public short getReceivedDataBit(int bit)
    {
        return this.dataReceived.get(bit);
    }


    public boolean isReceivedDataBitSetTo(int bit, int expectedValue)
    {
        return (this.dataReceived.get(bit) == expectedValue);
    }


    public String getDataReceivedAsString(int startIndex)
    {
        String outString = "";
        for (int i = startIndex; i < dataReceivedLength; i++)
        {
            short data = AnimasUtils.getUnsignedShort(this.dataReceived.get(i));
            outString += AnimasUtils.short2hex(data);
        }
        return outString;
    }


    public short[] getDataReceivedAsArray()
    {
        short[] arr = new short[this.dataReceivedLength];

        for (int i = 0; i < this.dataReceivedLength; i++)
        {
            arr[i] = this.dataReceived.get(i);
        }

        return arr;
    }

}
