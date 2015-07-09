package ggc.pump.device.insulet.data.enums;

/**
 * Created by andy on 21.05.15.
 */
public enum OmnipodDataType
{

    None(false),

    // DATA
    LogRecord(true), //
    HistoryRecord(true), //

    // CONFIG
    BasalProgramNames(), //
    EEpromSettings(), //
    IbfVersion(), //
    LogDescriptions, //
    ManufacturingData, //
    PdmVersion, //
    Profile, //
    SubRecord, //
    UnknownRecord //
    ;

    boolean isData = false;


    OmnipodDataType()
    {
        this(false);
    }


    OmnipodDataType(boolean isData)
    {
        this.isData = isData;
    }


    public boolean isData()
    {
        return isData;
    }

}
