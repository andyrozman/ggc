package ggc.meter.device.abbott.neo.data;

/**
 * Created by andy on 23/11/17.
 */
public class BloodGlucoseRecordDto extends KetoneRecordDto
{
    // RecordsType = 7

    protected int unknown4;
    protected int unknown5;
    protected int unknown6;
    protected int unknown7;
    protected int unknown8;
    protected int unknown9;
    protected int unknown10;
    protected int unknown11;

    public BloodGlucoseRecordDto(String[] valuesIn)
    {
        super(valuesIn);

        this.unknown4 = getInt(12);
        this.unknown5 = getInt(13);
        this.unknown6 = getInt(14);
        this.unknown7 = getInt(15);
        this.unknown8 = getInt(16);
        this.unknown9 = getInt(17);
        this.unknown10 = getInt(18);
        this.unknown11 = getInt(19);
    }


    @Override
    public String toString()
    {
        return "BloodGlucoseRecordDto [" + //
                "recordId=" + recordId + //
                ", recordType=" + recordType + //
                ", dateTimeNew=" + dateTime + //
                ", unknown1=" + unknown1 + //
                ", value=" + value + //
                ", unknown2=" + unknown2 + //
                ", unknown3=" + unknown3 + //
                ", unknown4=" + unknown4 + //
                ", unknown5=" + unknown5 + //
                ", unknown6=" + unknown6 + //
                ", unknown7=" + unknown7 + //
                ", unknown8=" + unknown8 + //
                ", unknown9=" + unknown9 + //
                ", unknown10=" + unknown10 + //
                ", unknown11=" + unknown11 + //
                ']';
    }

} 