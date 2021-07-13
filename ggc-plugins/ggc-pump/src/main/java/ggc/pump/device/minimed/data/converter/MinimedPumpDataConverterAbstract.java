package ggc.pump.device.minimed.data.converter;

import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.SendMessageType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverterAbstract;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 15.07.17.
 */
public abstract class MinimedPumpDataConverterAbstract extends MinimedDataConverterAbstract
{

    public MinimedPumpDataConverterAbstract(DataAccessPump dataAccess)
    {
        super(dataAccess);
        this.bitUtils = MedtronicUtil.getBitUtils();
        this.outputWriter = MedtronicUtil.getOutputWriter();
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
        this.outputWriter = MedtronicUtil.getOutputWriter();
    }

}
