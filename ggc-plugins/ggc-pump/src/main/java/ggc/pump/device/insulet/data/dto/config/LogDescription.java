package ggc.pump.device.insulet.data.dto.config;

import ggc.pump.device.insulet.data.dto.AbstractRecord;
import ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */

public class LogDescription extends AbstractRecord
{

    Short log_index;
    Short backup;
    Short location;
    Short has_variable;
    Short record_size;

    Integer first_index;
    Integer last_index;

    // 18


    // log_description: { format: '5S2N', fields: [
    // 'log_index', 'backup', 'location', 'has_variable', 'record_size',
    // 'first_index', 'last_index'
    // ]},

    public LogDescription()
    {
        super(false);
    }


    // @Override
    // public int process(List<Integer> data, int offset)
    // {
    //
    // log_index = getShort(data, offset + 2);
    // backup = getShort(data, offset + 4);
    // location = getShort(data, offset + 6);
    // has_variable = getShort(data, offset + 8);
    // record_size = getShort(data, offset + 10);
    //
    // first_index = getInt(data, offset + 12);
    // last_index = getInt(data, offset + 16);
    //
    // return this.length;
    // }

    public void process(int[] data, int offset)
    {
        log_index = getShort(data, offset);
        backup = getShort(data, offset + 2);
        location = getShort(data, offset + 4);
        has_variable = getShort(data, offset + 6);
        record_size = getShort(data, offset + 8);
        //
        first_index = getInt(data, offset + 10);
        last_index = getInt(data, offset + 14);
    }


    @Override
    public void customProcess(int[] data)
    {

        // var logDescriptions = getFixedRecord('log_hdr', offset);
        // logDescriptions.descs = [];
        // addTimestamp(logDescriptions);
        // for (var i = 0; i < logDescriptions.numLogDescriptions; ++i) {
        // var descOffset = 15 + i * 18;
        // var desc = struct.unpack(logDescriptions.rawdata, descOffset,
        // fixedRecords.log_description.format,
        // fixedRecords.log_description.fields);
        // logDescriptions.descs.push(desc);
        // }
        // return logDescriptions;

        // System.out.println("Not implemented : " +
        // this.getClass().getSimpleName());

    }


    @Override
    public String toString()
    {
        return "LogDescription [" + "log_index=" + log_index + ", backup=" + backup + ", location=" + location
                + ", has_variable=" + has_variable + ", record_size=" + record_size + ", first_index=" + first_index
                + ", last_index=" + last_index + "]\n";
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.SubRecord;
    }

}
