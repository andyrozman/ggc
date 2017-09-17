package ggc.pump.test.minimed;

import com.atech.utils.data.BitUtils;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.serial.MinimedCommunicationCareLinkUSB;
import ggc.plugin.device.impl.minimed.comm.usb.MinimedCommunicationContourNext2;
import ggc.plugin.device.impl.minimed.data.MinimedCommandPacket;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverter;
import ggc.plugin.device.impl.minimed.data.dto.MinimedConnectionParametersDTO;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.pump.test.AbstractPumpTest;

/**
 * Created by andy on 29.10.15.
 */
public class MinimedContourNextLinkTest extends AbstractPumpTest
{

    BitUtils bitUtils = new BitUtils();


    public MinimedContourNextLinkTest()
    {
        this.initDb = false;
        prepareContext();

        StringBuilder sb = new StringBuilder();

        sb.append("1a79:6300");
        sb.append("#;#");
        sb.append("MINIMED_INTERFACE!=" + MinimedCommInterfaceType.ContourNextLink2.name());
        sb.append("#;#");
        sb.append(";");
        sb.append("MINIMED_SERIAL!=316551"); // serial number

        MinimedConnectionParametersDTO parametersDTO = new MinimedConnectionParametersDTO(sb.toString());

        // 316551
    }


    public void testCreateTransmitpacket()
    {

        MinimedCommunicationCareLinkUSB handler = new MinimedCommunicationCareLinkUSB(dataAccessPump, null);

        MinimedCommandPacket mcp = new MinimedCommandPacket(MinimedCommandType.HistoryData);
        mcp.commandParameters = new byte[] { 0 };

        byte[] packetMine = handler.createTransmitPacketForCommand(mcp);

        System.out.println("Packet Mine: " + bitUtils.getHex(packetMine));

        // byte[] packetNS = handler.createTransmitPacketRequest(mcp);

        // System.out.println("Packet NS: " + bitUtils.getHex(packetNS));

    }


    public static void main(String[] args)
    {
        MinimedContourNextLinkTest test = new MinimedContourNextLinkTest();
        test.testCommunicateWithPump();
    }


    private void testCommunicateWithPump()
    {
        MinimedCommunicationContourNext2 handler = new MinimedCommunicationContourNext2(dataAccessPump, null);

        try
        {
            handler.initializeCommunicationInterface();
            handler.initDevice();

            MinimedCommandReply minimedCommandReply = handler.executeCommandWithRetry(MinimedCommandType.Settings_512);

            MinimedDataConverter dataConverter = MinimedUtil.getDataConverter(MinimedDeviceType.Minimed_512_712,
                MinimedTargetType.Pump);

            dataConverter.convertData(minimedCommandReply);

        }
        catch (PlugInBaseException e)
        {
            e.printStackTrace();
        }

    }

}
