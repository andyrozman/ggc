package ggc.core.data.defs;

public enum DatabaseStatusType
{
    Stopped(0), // 0 - NU
    InitDone(1), // 1
    BaseDone(2), // 2
    ExtendedDone(3), // 3 - NU
    Loaded(3), // 3

    ;

    int code;


    DatabaseStatusType(int code)
    {
        this.code = code;
    }


    public int getCode()
    {
        return this.code;
    }

    /**
     * Db Status: Stopped
     */
    public static final int DB_STOPPED = 0;

    /**
     * Db Status: Init done
     */
    public static final int DB_INIT_DONE = 1;

    /**
     * Db Status: Base done
     */
    public static final int DB_BASE_DONE = 2;

    /**
     * Db Status: Extended done
     */
    public static final int DB_EXTENDED_DONE = 3;

    /**
     * Db Status: Loaded
     */
    public static final int DB_LOADED = 3;
}
