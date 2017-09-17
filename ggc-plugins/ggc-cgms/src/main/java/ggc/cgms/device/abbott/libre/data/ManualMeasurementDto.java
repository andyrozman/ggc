package ggc.cgms.device.abbott.libre.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.cgms.data.defs.CGMSTrendArrow;
import ggc.cgms.device.abbott.libre.enums.ResultValueType;

public class ManualMeasurementDto extends LibreRecordDto
{

    private static final Logger LOG = LoggerFactory.getLogger(ManualMeasurementDto.class);

    int recordId;
    int recordType;
    ATechDate dateTime;
    int unknown1;
    ResultValueType readingType;
    // blood-glucose = "0"
    // blood-ketone = "1"
    // sensor-glucose = "2"

    int unknown2;
    byte errorPresent; // error or lo
    float value; // in mg/dL, when blood-ketone ceil(value/2)/10
    int readFrom; // 0 = blood strip, 1 = from sensor

    CGMSTrendArrow trendArrow;

    byte sportsFlag;
    byte medicationFlag;
    byte rapidActingInsulinFlag; //
    byte longActingInsulinFlag; //
    int customCommentsBitfield; // Custom comments 1-6 flags. To be interpreted as a bitfield, LSB
                                // first.

    int unknown3;
    int unknown4;
    int unknown5; // "0" / "1" / "2" / "3" / "4" / "5"

    Float valueLongActingInsulin; // Value of long Acting insulin in 0.5 IE. If you want proper IE,
                                  // divide by 2
    int unknown6;
    byte foodFlag;
    Integer foodCarbohydratesGrams;
    int unknown7;
    int errorBitField;

    boolean errorPresentBitField;
    int errorCode;

    String comment1;
    String comment2;
    String comment3;
    String comment4;
    String comment5;
    String comment6;

    int unknown8;
    ATechDate dateTime2;
    int unknown9;
    Float valueRapidActingInsulin;

    boolean extendedSet = false;


    public ManualMeasurementDto(String[] valuesIn)
    {
        this.values = valuesIn;

        this.recordId = getInt(0);
        this.recordType = getInt(1);
        this.dateTime = getDateTime(3, 2, 4, 5, 6, 7);
        this.unknown1 = getInt(8);

        this.readingType = ResultValueType.getByCode(getByte(9));

        this.unknown2 = getInt(10);
        this.errorPresent = getByte(11);

        if (this.readingType == ResultValueType.Ketone)
        {
            float v1 = (float) Math.ceil((getInt(12) / 2f));
            this.value = v1 / 10f;
        }
        else
        {
            this.value = getInt(12);
        }

        this.readFrom = getInt(13);

        this.trendArrow = getTrendArrow(getByte(14));

        this.sportsFlag = getByte(15);
        this.medicationFlag = getByte(16);
        this.rapidActingInsulinFlag = getByte(17);
        this.longActingInsulinFlag = getByte(18);
        this.customCommentsBitfield = getInt(19);

        this.unknown3 = getByte(20);
        this.unknown4 = getByte(21);
        this.unknown5 = getByte(22);

        this.valueLongActingInsulin = getInt(23) / 2f;

        this.unknown6 = getByte(24);
        this.foodFlag = getByte(25);
        this.foodCarbohydratesGrams = getInt(26);
        this.unknown7 = getByte(27);

        this.errorBitField = getInt(28);
        this.errorPresentBitField = (errorBitField & 0x8000) > 1;
        this.errorCode = this.errorBitField;

        this.comment1 = getString(29);
        this.comment2 = getString(30);
        this.comment3 = getString(31);
        this.comment4 = getString(32);
        this.comment5 = getString(33);
        this.comment6 = getString(34);

        if (values.length > 35)
        {
            extendedSet = true;
            this.unknown8 = getInt(35);
            this.dateTime2 = getDateTime(37, 36, 38, 39, 40, 41);
            this.unknown9 = getInt(42);
            this.valueRapidActingInsulin = getInt(43) / 2f;
        }
    }


    public CGMSTrendArrow getTrendArrow(byte direction)
    {
        switch (direction)
        {
            case 0:
                return CGMSTrendArrow.None;

            case 1:
                return CGMSTrendArrow.DoubleDown;

            case 2:
                return CGMSTrendArrow.FortyFiveDown; // Down

            case 3:
                return CGMSTrendArrow.Flat;

            case 4:
                return CGMSTrendArrow.FortyFiveUp;

            case 5:
                return CGMSTrendArrow.DoubleUp;

            default:
                LOG.error("Unknown Trend: " + direction);
                return CGMSTrendArrow.None;

        }
        // none = "0"
        // down-fast = "1"
        // down = "2"
        // steady = "3"
        // up = "4"
        // up-fast = "5"

    }


    @Override
    public String toString()
    {
        String valueString = "ManualMeasurementDto [" + //
                "recordId=" + recordId + //
                ", recordType=" + recordType + //
                ", dateTime=" + dateTime + //
                ", unknown1=" + unknown1 + //
                ", readingType=" + readingType + //
                ", unknown2=" + unknown2 + //
                ", errorPresent=" + errorPresent + //
                ", value=" + value + //
                ", readFrom=" + readFrom + //
                ", trendArrow=" + trendArrow + //
                ", sportsFlag=" + sportsFlag + //
                ", medicationFlag=" + medicationFlag + //
                ", rapidActingInsulinFlag=" + rapidActingInsulinFlag + //
                ", longActingInsulinFlag=" + longActingInsulinFlag + //
                ", customCommentsBitfield=" + customCommentsBitfield + //
                ", unknown3=" + unknown3 + //
                ", unknown4=" + unknown4 + //
                ", unknown5=" + unknown5 + //
                ", valueLongActingInsulin=" + valueLongActingInsulin + //
                ", unknown6=" + unknown6 + //
                ", foodFlag=" + foodFlag + //
                ", foodCarbohydratesGrams=" + foodCarbohydratesGrams + //
                ", unknown7=" + unknown7 + //
                ", errorPresentBitField=" + errorPresentBitField + //
                ", errorCode=" + errorCode + //
                ", comment1=" + comment1 + //
                ", comment2=" + comment2 + //
                ", comment3=" + comment3 + //
                ", comment4=" + comment4 + //
                ", comment5=" + comment5 + //
                ", comment6=" + comment6;

        if (extendedSet)
        {
            valueString += ", unknown8=" + unknown8 + //
                    ", dateTime2=" + dateTime2 + //
                    ", unknown9=" + unknown9 + //
                    ", valueRapidActingInsulin=" + valueRapidActingInsulin;
        }

        valueString += "]";

        return valueString;

    }
    /*
     * TEST:
     * 17.9. 12:01 Long acting 4U
     * 12:49 Rapid acting 2E
     * 15:54 : ch 80
     */

}
