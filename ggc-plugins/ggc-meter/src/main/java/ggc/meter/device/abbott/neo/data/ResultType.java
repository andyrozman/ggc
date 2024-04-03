package ggc.meter.device.abbott.neo.data;

import java.util.Hashtable;

import com.atech.utils.data.CodeEnum;

/**
 * Created by andy on 25/11/17.
 */
public enum ResultType implements CodeEnum
{
    Unknown(0), //
    DateTimeChange(6), //
    BloodGlucose(7), //
    Ketone(9), //
    Insulin(10), //
    ;

    static Hashtable<Integer, ResultType> codeMapping = new Hashtable<Integer, ResultType>();

    static
    {
        for (ResultType pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }



    private int code;


    ResultType(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public static ResultType getByCode(int type)
    {
        if (codeMapping.containsKey(type))
        {
            return codeMapping.get(type);
        }
        else
            return ResultType.Unknown;

    }
}