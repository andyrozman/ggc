package ggc.cgms.device.dexcom.receivers.g4receiver.util;

public class DexcomException extends Exception
{

    private static final long serialVersionUID = -544500956349213288L;
    private DexcomExceptionType exceptionType;

    @Deprecated
    public DexcomException(String errorMessage, Exception e)
    {
        super(errorMessage, e);
    }

    @Deprecated
    public DexcomException(String errorMessage)
    {
        super(errorMessage);
        // TODO Auto-generated constructor stub
    }

    public DexcomException(DexcomExceptionType exceptionType)
    {
        super(DexcomException.createMessage(exceptionType, null));
        this.setExceptionType(exceptionType);
    }

    public DexcomException(DexcomExceptionType exceptionType, Exception ex)
    {
        super(DexcomException.createMessage(exceptionType, null), ex);
        this.setExceptionType(exceptionType);
    }

    public DexcomException(DexcomExceptionType exceptionType, Object[] parameters)
    {
        super(DexcomException.createMessage(exceptionType, parameters));
        this.setExceptionType(exceptionType);
    }

    public DexcomException(DexcomExceptionType exceptionType, Object[] parameters, Exception ex)
    {
        super(DexcomException.createMessage(exceptionType, parameters));
        this.setExceptionType(exceptionType);
    }

    public static String createMessage(DexcomExceptionType exceptionType, Object[] parameters)
    {

        if (parameters != null)
            return String.format(exceptionType.errorMessage, parameters);
        else
            return exceptionType.errorMessage;
    }

    public DexcomExceptionType getExceptionType()
    {
        return exceptionType;
    }

    public void setExceptionType(DexcomExceptionType exceptionType)
    {
        this.exceptionType = exceptionType;
    }

}
