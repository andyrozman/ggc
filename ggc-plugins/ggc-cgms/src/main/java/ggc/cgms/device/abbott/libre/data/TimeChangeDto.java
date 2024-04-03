package ggc.cgms.device.abbott.libre.data;

import com.atech.utils.data.ATechDate;

import ggc.plugin.device.impl.abbott.hid.AbbottHidRecordDto;

public class TimeChangeDto extends AbbottHidRecordDto
{

    ATechDate dateTimeNew;
    int unknown1;
    ATechDate dateTimeOld;
    int unknown2;
    int unknown3;
    int unknown4;
    int unknown5;
    int unknown6;


    public TimeChangeDto(String[] valuesIn)
    {
        this.values = valuesIn;

        this.recordId = getInt(0);
        this.recordType = getInt(1); // 5
        this.dateTimeNew = getDateTime(3, 2, 4, 5, 6, 7);
        this.unknown1 = getInt(8);
        this.dateTimeOld = getDateTime(10, 9, 11, 12, 13, 14);
        this.unknown2 = getInt(15);
        this.unknown3 = getInt(16);
        this.unknown4 = getInt(17);
        this.unknown5 = getInt(18);
        this.unknown6 = getInt(19);
    }


    @Override
    public String toString()
    {
        return "TimeChangeDto [" + //
                "recordId=" + recordId + //
                ", recordType=" + recordType + //
                ", dateTimeNew=" + dateTimeNew + //
                ", unknown1=" + unknown1 + //
                ", dateTimeOld=" + dateTimeOld + //
                ", unknown2=" + unknown2 + //
                ", unknown3=" + unknown3 + //
                ", unknown4=" + unknown4 + //
                ", unknown5=" + unknown5 + //
                ", unknown6=" + unknown6 + //
                ']';
    }
}
