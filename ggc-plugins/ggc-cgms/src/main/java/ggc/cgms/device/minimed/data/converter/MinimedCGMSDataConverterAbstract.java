package ggc.cgms.device.minimed.data.converter;

import ggc.cgms.data.defs.CGMSConfigurationGroup;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.SendMessageType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverterAbstract;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;

/**
 * Created by andy on 15.07.17.
 */
public abstract class MinimedCGMSDataConverterAbstract extends MinimedDataConverterAbstract
{

    public MinimedCGMSDataConverterAbstract(DataAccessCGMS dataAccess)
    {
        super(dataAccess);
        this.bitUtils = MinimedUtil.getBitUtils();
        this.outputWriter = MinimedUtil.getOutputWriter();
    }


    public void convertData(MinimedCommandReply minimedReply)
    {
        if (minimedReply.getCommandType() instanceof MinimedCommandType)
        {
            convertData(minimedReply, (MinimedCommandType) minimedReply.getCommandType());
        }
        else
        {
            convertData(minimedReply, (SendMessageType) minimedReply.getCommandType());
        }
    }


    public void convertData(MinimedCommandReply minimedReply, MinimedCommandType commandType)
    {

    }


    public void convertData(MinimedCommandReply minimedReply, SendMessageType commandType)
    {

    }


    public void refreshOutputWriter()
    {
        this.outputWriter = MinimedUtil.getOutputWriter();
    }


    protected void decodeEnableSetting(String key, MinimedCommandReply minimedReply, CGMSConfigurationGroup pcg)
    {
        decodeEnableSetting(key, minimedReply, 0, pcg);
    }


    protected void decodeEnableSetting(String key, int value, CGMSConfigurationGroup pcg)
    {
        writeSetting(key, parseResultEnable(value), pcg);
    }


    protected void decodeEnableSetting(String key, MinimedCommandReply minimedReply, int bit,
            CGMSConfigurationGroup pcg)
    {
        writeSetting(key, parseResultEnable(minimedReply.getRawDataAsInt(bit)), pcg);
    }


    protected void writeSetting(String key, String value, Object rawValue, CGMSConfigurationGroup group)
    {
        if (rawValue != null)
        {
            outputWriter.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));
        }
    }


    protected void writeSetting(String key, String value, CGMSConfigurationGroup group)
    {
        outputWriter.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));
    }

}
