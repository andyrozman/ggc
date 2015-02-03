package ggc.cgms.data.defs;

import com.atech.utils.data.CodeEnum;

/**
 * Created by andy on 16.01.15.
 */
public enum CGMSObject implements CodeEnum
{
    Base(1), //
    SubEntry(2), //
    ;

    int code;

    private CGMSObject(int code)
    {
        this.code = code;
    }


    public int getCode()
    {
        return this.code;
    }

}
