package ggc.plugin.comm.cfg;

/**
 * Created by andy on 14.05.15.
 */
public enum SerialSettingsType
{
    StopBits1, //
    StopBits1_5, //
    StopBits2, //

    ParityNone, //
    ParityOdd, //
    ParityEven, //
    ParityMark, //
    ParitySpace, //

    DataBits5, //
    DataBits6, //
    DataBits7, //
    DataBits8, //

    FlowControlNone, //
    FlowControlRtsCtsIn, //
    FlowControlRtsCtsOut, //
    FlowControlXonXoffIn, //
    FlowControlXonXoffOut, //

    ;

}
