package ggc.core.data.defs;

import com.atech.graphics.observe.ObservableType;

/**
 * Created by andy on 04.01.17.
 */
public enum GGCObservableType implements ObservableType
{
    InfoPanels, //
    Status, //
    Database, //
    ;

    public String getKey()
    {
        return this.name();
    }

    /**
     * Observable: Panels
     */
    public static final int OBSERVABLE_PANELS = 1;

    /**
     * Observable: Status
     */
    public static final int OBSERVABLE_STATUS = 2;

    /**
     * Observable: Db
     */
    public static final int OBSERVABLE_DB = 3;

}
