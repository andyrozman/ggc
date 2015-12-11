package ggc.cgms.device.minimed.data;

import java.util.List;

import ggc.cgms.device.minimed.data.enums.CGMSHistoryEntryType;
import ggc.plugin.device.impl.minimed.data.MinimedHistoryEntry;

/**
 * Created by andy on 27.03.15.
 */
public class CGMSHistoryEntry extends MinimedHistoryEntry
{

    private CGMSHistoryEntryType entryType;
    private Integer opCode; // this is set only when we have unknown entry...


    public CGMSHistoryEntryType getEntryType()
    {
        return entryType;
    }


    public void setEntryType(CGMSHistoryEntryType entryType)
    {
        this.entryType = entryType;

        this.sizes[0] = entryType.getHeadLength();
        this.sizes[1] = entryType.getDateLength();
        this.sizes[2] = entryType.getBodyLength();
    }


    public void setData(List<Byte> listRawData, boolean doNotProcess)
    {
        if (this.entryType.schemaSet)
        {
            super.setData(listRawData, doNotProcess);
        }
        else
        {
            this.rawData = listRawData;
        }
    }


    @Override
    public int getOpCode()
    {
        if (opCode == null)
            return entryType.getOpCode();
        else
            return opCode;
    }


    @Override
    public String getToStringStart()
    {
        return "CGMSHistoryEntry [type=" + entryType.name() + " [" + getOpCode() + ", 0x"
                + bitUtils.getCorrectHexValue(getOpCode()) + "]";
    }


    public void setOpCode(Integer opCode)
    {
        this.opCode = opCode;
    }

}
