package ggc.meter.device.abbott.neo.data;

import com.atech.utils.data.ATechDate;

import ggc.plugin.device.impl.abbott.hid.AbbottHidRecordDto;

/**
 * Created by andy on 23/11/17.
 */
public class DateTimeChangeDto extends AbbottHidRecordDto
{
    // RecordsType = 6

    // 6,  7,  11,23,17,20,8,  1,  11,23,17,20,10

    
    ATechDate dateTimeNew;
    int unknown1;
    ATechDate dateTimeOld;



    public DateTimeChangeDto(String[] valuesIn)
    {
        this.values = valuesIn;

        this.recordType = getInt(0); // 6
        this.recordId = getInt(1);

        this.dateTimeNew = getDateTime(3, 2, 4, 5, 6, 7);
        this.unknown1 = getInt(8);
        this.dateTimeOld = getDateTime(10, 9, 11, 12, 13, 14);
    }


    @Override
    public String toString()
    {
        return "DateTimeChangeDto [" + //
                "recordId=" + recordId + //
                ", recordType=" + recordType + //
                ", dateTimeNew=" + dateTimeNew + //
                ", unknown1=" + unknown1 + //
                ", dateTimeOld=" + dateTimeOld + //
                ']';
    }
} 