package ggc.core.data.defs;

/**
 * Created by andy on 04.01.17.
 */
public enum RefreshInfoType
{
    DevicesConfiguration(1), // PANEL_GROUP_PLUGINS_DEVICES
    DeviceDataAll(2), // PANEL_GROUP_ALL_DATA
    GeneralInfo(3), // PANEL_GROUP_GENERAL_INFO
    PluginsAll(4), // PANEL_GROUP_PLUGINS_ALL
    DeviceDataMeter(2), //
    DeviceDataCGMS(2), //
    DeviceDataPump(2), //
    Appointments(5), //
    Inventory(6);

    int code;


    RefreshInfoType(int code)
    {
        this.code = code;
    }


    public int getCode()
    {
        return this.code;
    }

    /**
     * Panel Group - Plugins Devices
     */
    public static final int PANEL_GROUP_PLUGINS_DEVICES = 1;

    /**
     * Panel Group - All Data (HbA1c and Statistics)
     */
    public static final int PANEL_GROUP_ALL_DATA = 2;

    /**
     * Panel Group - General Info
     */
    public static final int PANEL_GROUP_GENERAL_INFO = 3;

    /**
     * Panel Group - Plugins All
     */
    public static final int PANEL_GROUP_PLUGINS_ALL = 4;

}
