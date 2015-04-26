package ggc.plugin.device.impl.animas.data;

import java.util.ArrayList;
import java.util.List;

import com.atech.utils.data.ATechDate;

import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;

/**
 * Application:   GGC - GNU Gluco Control
 * Plug-in:       GGC PlugIn Base (base class for all plugins)
 * <p/>
 * See AUTHORS for copyright information.
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * <p/>
 * Filename:     AnimasDevicePacket
 * Description:  Animas Device Packet
 * <p/>
 * Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasDevicePacket
{

    public AnimasDataType dataTypeObject;
    public int downloadedQuantity = 0;
    public char[] commandToSend;
    public boolean allDataReceived = false;

    public List<Short> dataReceived = new ArrayList<Short>();

    public int dataReceivedLength;
    public int historyRecordCount;
    public boolean findHistoryRecordCount;
    List<AnimasPreparedDataEntry> preparedData = new ArrayList<AnimasPreparedDataEntry>();
    public int lastDownloadRunQuantity = -1;
    public int retryCount = 0;
    public int recordCount;
    public int startingRecord;


    public short getReceivedDataBit(int bit)
    {
        return this.dataReceived.get(bit);
    }


    public boolean isReceivedDataBitSetTo(int bit, int expectedValue)
    {
        return (this.dataReceived.get(bit) == expectedValue);
    }


    public int getDataCheck()
    {
        int dataCheck = 0;
        for (int i = 6; i < this.dataReceivedLength; i++)
        {
            dataCheck += this.dataReceived.get(i);
        }
        return dataCheck;
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


    public void clearDataReceived()
    {
        this.dataReceived.clear();
    }


    public void addDataReceivedBit(short s)
    {
        this.dataReceived.add(s);
    }


    public void addPreparedData(String key, ATechDate dateTime)
    {
        AnimasUtils.getAnimasData().writeData(key, dateTime, null);
    }


    public void addPreparedData(String key, ATechDate dateTime, String value)
    {
        AnimasUtils.getAnimasData().writeData(key, dateTime, value);
    }


    public AnimasDeviceReplyPacket getReplyPacket()
    {
        AnimasDeviceReplyPacket replyPacket = new AnimasDeviceReplyPacket();

        replyPacket.dataTypeObject = this.dataTypeObject;
        replyPacket.dataReceived.addAll(this.dataReceived);
        replyPacket.dataReceivedLength = this.dataReceivedLength;

        return replyPacket;
    }
}
