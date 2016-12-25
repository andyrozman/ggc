package main.java.ggc.pump.device.insulet.data.dto;

import java.util.List;

import main.java.ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 20.05.15.
 */
public class UnknownRecord extends AbstractRecord
{

    public UnknownRecord()
    {
        super(false);
    }


    @Override
    public int process(List<Integer> data, int offset)
    {
        return 0;
    }


    @Override
    public void customProcess(int[] data)
    {

    }


    @Override
    public String toString()
    {
        return "UnknownRecord {length=" + this.length + "}";
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.UnknownRecord;
    }

}
