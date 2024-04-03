package ggc.cgms.device.abbott.libre.enums;

/**
 * Created by andy on 28/08/17.
 */
public enum LibreError
{

    OK(0, "OK: No Error."), //
    BAD_PARAMS(1, "Bad parameter values/out of range!"), //
    WRONG_PARAM_NUM(2, "Wrong number of parameters!"), //
    CMD_FAILED(3, "Command failed!"), //
    CRC_ERROR(4, "Wrong checksum!"), //
    INVALID_ID(5, "Wrong table ID!"), //
    ;

    int code;
    String errorDescription;


    LibreError(int code, String errorDescription)
    {
        this.code = code;
        this.errorDescription = errorDescription;
    }


    int getCode()
    {
        return this.code;
    }

}
