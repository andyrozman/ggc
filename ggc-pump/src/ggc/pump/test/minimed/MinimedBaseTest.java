package ggc.pump.test.minimed;

import com.atech.utils.data.BitUtils;

import ggc.plugin.device.impl.minimed.comm.serial.MinimedCommunicationCareLinkUSB;
import ggc.plugin.device.impl.minimed.data.MinimedCommandPacket;
import ggc.plugin.device.impl.minimed.data.dto.MinimedConnectionParametersDTO;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.pump.test.AbstractPumpTest;

/**
 * Created by andy on 29.10.15.
 */
public class MinimedBaseTest extends AbstractPumpTest
{

    BitUtils bitUtils = new BitUtils();


    public MinimedBaseTest()
    {
        this.initDb = false;
        prepareContext();

        StringBuilder sb = new StringBuilder();

        sb.append(MinimedCommInterfaceType.CareLinkUSB.name());
        sb.append(";");
        sb.append("/dev/ttyUSB3");
        sb.append(";");
        sb.append("316551"); // serial number

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
        MinimedBaseTest test = new MinimedBaseTest();
        test.testCreateTransmitpacket();
    }

}
