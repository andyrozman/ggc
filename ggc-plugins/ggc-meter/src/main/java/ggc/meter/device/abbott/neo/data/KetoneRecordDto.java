package ggc.meter.device.abbott.neo.data;

import ggc.plugin.device.impl.abbott.hid.AbbottHidRecordDto;

/**
 * Created by andy on 23/11/17.
 */
public class KetoneRecordDto extends AbbottHidRecordDto
{
    // RecordsType = 9

    protected int unknown1;
    protected int value;
    protected int unknown2;
    protected int unknown3;

    public KetoneRecordDto(String[] valuesIn)
    {
        this.values = valuesIn;

        this.recordType = getInt(0); // 9
        this.recordId = getInt(1);

        this.dateTime = getDateTime(3, 2, 4, 5, 6, 7);
        this.unknown1 = getInt(8);
        this.value = getInt(9);

        this.unknown2 = getInt(10);
        this.unknown3 = getInt(11);
    }




    @Override
    public String toString()
    {
        return "KetoneRecordDto [" + //
                "recordId=" + recordId + //
                ", recordType=" + recordType + //
                ", dateTimeNew=" + dateTime + //
                ", unknown1=" + unknown1 + //
                ", value=" + value + //
                ", unknown2=" + unknown2 + //
                ", unknown3=" + unknown3 + //
                ']';
    }

} 