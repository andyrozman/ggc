package ggc.meter.device.abbott.neo.data;

import ggc.plugin.device.impl.abbott.hid.AbbottHidRecordDto;

/**
 * Created by andy on 23/11/17.
 */
public class InsulinRecordDto extends AbbottHidRecordDto
{
    // RecordsType = 10

    protected int unknown1;
    protected int value;
    protected int unknown2;
    protected int unknown3;

    // 1:10  25.11  = morning long acting



//    1. type = "10"
//    2. id
//    3-7    date
//    8.     unknown
//    9. Carries an enumeration of the type of insulin used: insulin-type = morning-long-acting / breakfast-short-acting / l
//       unch-short-acting / evening-long-acting / dinner-short-acting morning-long-acting = "0"
//       breakfast-short-acting = "1" lunch-short-acting = "2" evening-long-acting = "3" dinner-short-acting = "4"
//    10. value = 1*DIGIT
//        unknown
//    unknown
//    13. unknown



    public InsulinRecordDto(String[] valuesIn)
    {
        this.values = valuesIn;

        this.recordType = getInt(0); // 10
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