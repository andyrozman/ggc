package ggc.core.enums;

/**
 * Created by andy on 01.12.15.
 */
public enum DbLoadingStatus
{
    NotStarted(0), //
    Initialized(1), //
    BasicDataLoaded(2), //
    PlugInDataLoaded(3), //
    Finished(4),

    ;

    DbLoadingStatus(int code)
    {
    }

    /**
     * Db Status: Not started
     */
    public static final int DB_NOT_STARTED = 0;
    /**
     * Db Status: Init done
     */
    public static final int DB_INIT_DONE = 1;
    /**
     * Db Status: Base data loaded
     */
    public static final int DB_DATA_BASE = 2;

    // public boolean part_start = true;
    /**
     * Db Status: Data from plugins loaded
     */
    public static final int DB_DATA_PLUGINS = 3;

    // public boolean debug = false;
    /**
     * Db Status: Db Initialization done - Load completed
     */
    public static final int DB_INIT_FINISHED = 4;

}
