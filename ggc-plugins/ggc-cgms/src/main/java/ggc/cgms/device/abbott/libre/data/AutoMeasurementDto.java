package ggc.cgms.device.abbott.libre.data;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.atech.utils.data.ATechDate;

public class AutoMeasurementDto extends LibreRecordDto
{

    int recordId;
    byte unknownValue1;
    ATechDate dateTime;

    byte unknownValue2;
    byte unknownValue3;
    byte unknownValue4;
    byte unknownValue5;
    byte firstReading;

    int value;
    int sensorTime;
    int errorBitfield;

    boolean errorPresentBitField;
    int errorCode;


    public AutoMeasurementDto(String value)
    {
        this.values = value.split(",");

        this.recordId = getInt(0);
        this.unknownValue1 = getByte(1);

        this.dateTime = getDateTime(3, 2, 4, 5, 6, 7);

        this.unknownValue2 = getByte(8);
        this.unknownValue3 = getByte(9);
        this.unknownValue4 = getByte(10);
        this.unknownValue5 = getByte(11);

        this.firstReading = getByte(12);

        this.value = getInt(13);
        this.sensorTime = getInt(14);
        this.errorBitfield = getInt(15);

        this.errorPresentBitField = (errorBitfield & 0x8000) > 1;
        this.errorCode = this.errorBitfield;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        AutoMeasurementDto that = (AutoMeasurementDto) o;

        return new EqualsBuilder() //
                .append(recordId, that.recordId) //
                .append(unknownValue1, that.unknownValue1) //
                .append(unknownValue2, that.unknownValue2) //
                .append(unknownValue3, that.unknownValue3) //
                .append(unknownValue4, that.unknownValue4) //
                .append(unknownValue5, that.unknownValue5) //
                .append(firstReading, that.firstReading) //
                .append(value, that.value) //
                .append(sensorTime, that.sensorTime) //
                .append(errorBitfield, that.errorBitfield) //
                .append(dateTime, that.dateTime) //
                .isEquals();
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37) //
                .append(recordId) //
                .append(unknownValue1) //
                .append(dateTime) //
                .append(unknownValue2) //
                .append(unknownValue3) //
                .append(unknownValue4) //
                .append(unknownValue5) //
                .append(firstReading) //
                .append(value) //
                .append(sensorTime) //
                .append(errorBitfield) //
                .toHashCode();
    }


    @Override
    public String toString()
    {
        return "AutoMeasurementDto [" + //
                "recordId=" + recordId + //
                ", unknownValue1=" + unknownValue1 + //
                ", dateTime=" + dateTime + //
                ", unknownValue2=" + unknownValue2 + //
                ", unknownValue3=" + unknownValue3 + //
                ", unknownValue4=" + unknownValue4 + //
                ", unknownValue5=" + unknownValue5 + //
                ", firstReading=" + firstReading + //
                ", value=" + value + //
                ", sensorTime=" + sensorTime + //
                ", errorBitfield=" + errorBitfield + ']';
    }

}
