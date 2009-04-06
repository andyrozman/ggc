package ggc.pump.device.dana;

import java.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceRecord.
 */
public class DeviceRecord
{
    
    
    
    /**
     * The Constant RECORD_CMD_ALARM.
     */
    public static final byte RECORD_CMD_ALARM = 5;
    
    /**
     * The Constant RECORD_CMD_BOLUS.
     */
    public static final byte RECORD_CMD_BOLUS = 1;
    
    /**
     * The Constant RECORD_CMD_CARBO.
     */
    public static final byte RECORD_CMD_CARBO = 7;
    
    /**
     * The Constant RECORD_CMD_DAILY.
     */
    public static final byte RECORD_CMD_DAILY = 2;
    
    /**
     * The Constant RECORD_CMD_ERROR.
     */
    public static final byte RECORD_CMD_ERROR = 6;
    
    /**
     * The Constant RECORD_CMD_GLUCOSE.
     */
    public static final byte RECORD_CMD_GLUCOSE = 4;
    
    /**
     * The Constant RECORD_CMD_PRIME.
     */
    public static final byte RECORD_CMD_PRIME = 3;
    
    /**
     * The Constant RECORD_HOUR_AFTERNOON.
     */
    public static final byte RECORD_HOUR_AFTERNOON = 4;
    
    /**
     * The Constant RECORD_HOUR_BREAKFAST.
     */
    public static final byte RECORD_HOUR_BREAKFAST = 1;
    
    /**
     * The Constant RECORD_HOUR_DAWN.
     */
    public static final byte RECORD_HOUR_DAWN = 0;
    
    /**
     * The Constant RECORD_HOUR_DINNER.
     */
    public static final byte RECORD_HOUR_DINNER = 5;
    
    /**
     * The Constant RECORD_HOUR_LUNCH.
     */
    public static final byte RECORD_HOUR_LUNCH = 3;
    
    /**
     * The Constant RECORD_HOUR_MORNING.
     */
    public static final byte RECORD_HOUR_MORNING = 2;
    
    /**
     * The Constant RECORD_HOUR_NIGHT.
     */
    public static final byte RECORD_HOUR_NIGHT = 6;
    
    /**
     * The Constant RECORD_TYPE_ALARM.
     */
    public static final byte RECORD_TYPE_ALARM = 5;
    
    /**
     * The Constant RECORD_TYPE_BOLUS.
     */
    public static final byte RECORD_TYPE_BOLUS = 1;
    
    /**
     * The Constant RECORD_TYPE_CARBO.
     */
    public static final byte RECORD_TYPE_CARBO = 8;
    
    /**
     * The Constant RECORD_TYPE_DAILY.
     */
    public static final byte RECORD_TYPE_DAILY = 2;
    
    /**
     * The Constant RECORD_TYPE_ERROR.
     */
    public static final byte RECORD_TYPE_ERROR = 4;
    
    /**
     * The Constant RECORD_TYPE_GLUCOSE.
     */
    public static final byte RECORD_TYPE_GLUCOSE = 6;
    
    /**
     * The Constant RECORD_TYPE_PRIME.
     */
    public static final byte RECORD_TYPE_PRIME = 3;
    
    /**
     * The record code.
     */
    public byte recordCode = 0;
    
    /**
     * The record date.
     */
    public GregorianCalendar recordDate = null;
    
    /**
     * The record type.
     */
    public byte recordType = 0;
    
    /**
     * The record value.
     */
    public int recordValue = 0;
    
    /**
     * The serial no.
     */
    public String serialNo = "0000000000";

    /**
     * Gets the hour code.
     * 
     * @return the hour code
     */
    public byte getHourCode()
    {
        Settings s = new Settings();
        
        if (this.recordDate.get(GregorianCalendar.HOUR_OF_DAY) < s.prefBreakfastFrom)
        {
            return 0;
        }
        if (this.recordDate.get(GregorianCalendar.HOUR_OF_DAY) <= s.prefBreakfastTo)
        {
            return 1;
        }
        if (this.recordDate.get(GregorianCalendar.HOUR_OF_DAY) < s.prefLunchFrom)
        {
            return 2;
        }
        if (this.recordDate.get(GregorianCalendar.HOUR_OF_DAY) <= s.prefLunchTo)
        {
            return 3;
        }
        if (this.recordDate.get(GregorianCalendar.HOUR_OF_DAY) < s.prefDinnerFrom)
        {
            return 4;
        }
        if (this.recordDate.get(GregorianCalendar.HOUR_OF_DAY) <= s.prefDinnerTo)
        {
            return 5;
        }
        return 6;
    }
}
