package ggc.plugin.comm.cfg;

/**
 * Created by andy on 11.03.15.
 */
public class SerialSettings
{

    public Integer baudRate = 9600;
    public SerialSettingsType dataBits = SerialSettingsType.DataBits8;
    public SerialSettingsType stopBits = SerialSettingsType.StopBits1;
    public SerialSettingsType parity = SerialSettingsType.ParityNone;
    public SerialSettingsType flowControl = SerialSettingsType.FlowControlNone;

    public Boolean dtr = false;
    public Boolean rts = true;


    public SerialSettings()
    {
    }


    public SerialSettings(int baudRate, SerialSettingsType dataBits, SerialSettingsType stopBits,
            SerialSettingsType parity)
    {
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }


    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer("SerialSettings [");
        sb.append("baudRate=" + baudRate + ",");
        sb.append("dataBits=" + dataBits.name() + ",");
        sb.append("stopBits=" + stopBits + ",");
        sb.append("parity=" + parity + ",");
        sb.append("flowControl=" + flowControl + ",");
        sb.append("DTR=" + dtr + ",");
        sb.append("RTS=" + rts + "]");

        return sb.toString();

    }

}
