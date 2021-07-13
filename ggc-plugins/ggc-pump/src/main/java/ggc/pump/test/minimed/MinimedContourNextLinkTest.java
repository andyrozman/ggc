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
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.pump.device.minimed.MinimedPumpDeviceHandler;
import ggc.pump.test.AbstractPumpTest;

/**
 * Created by andy on 29.10.15.
 */
public class MinimedContourNextLinkTest extends AbstractPumpTest
{

    BitUtils bitUtils = new BitUtils();
    String connectionString = null;


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

        this.connectionString = sb.toString();

        MinimedConnectionParametersDTO parametersDTO = new MinimedConnectionParametersDTO(this.connectionString);

        // 316551
        MedtronicUtil.setOutputWriter(new ConsoleOutputWriter());
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

    // 33 / 21
    // OPEN:  00 00 00 21 51 01 33 31 36 35 35 31 00 00 00 00 00 00 00 00 00 00 10 01 1E 00 00 00 00 00 00 00 00 00 00 00 B6
    //                    51 01 33 31 36 35 35 31 00 00 00 00 00 00 00 00 00 00 10 01 1E 00 00 00 00 00 00 00 00 00 00 00 B6
    //        41 42 43 01 15 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00

    // SETT:  00 00 00 28 51 01 33 31 36 35 35 31 00 00 00 00 00 00 00 00 00 00 12 21 05 00 00 00 00 00 00 00 07 00 00 00 66 A7 31 65 51 C0 00 52
    //                    51 01 33 31 36 35 35 31 00 00 00 00 00 00 00 00 00 00 12 21 05 00 00 00 00 00 00 00 07 00 00 00 4A A7 31 65 51 91 00 65
    //        00 00 00 28 51 01 33 31 36 35 35 31 00 00 00 00 00 00 00 00 00 00 12 21 05 00 00 00 00 00 00 00 07 00 00 00 4A A7 31 65 51 91 00 65 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    public static void main(String[] args)
    {
        MinimedContourNextLinkTest test = new MinimedContourNextLinkTest();
        test.testCommunicateWithPump();
    }


    private void testCommunicateWithPumpReal()
    {

        MinimedPumpDeviceHandler handler = new MinimedPumpDeviceHandler(dataAccessPump);

        //handler.readConfiguration();


    }



    private void testCommunicateWithPump()
    {
        //MinimedPumpDeviceHandler handler = new MinimedPumpDeviceHandler(dataAccessPump);

        MinimedCommunicationContourNext2 handler = new MinimedCommunicationContourNext2(dataAccessPump, null);

        try
        {
            handler.initializeCommunicationInterface();
            handler.initDevice();

            MinimedCommandReply minimedCommandReply = handler.executeCommandWithRetry(MinimedCommandType.Settings_512);

            MinimedDataConverter dataConverter = MedtronicUtil.getDataConverter(MinimedDeviceType.Minimed_512_712,
                    MinimedTargetType.Pump);

            dataConverter.refreshOutputWriter();
            dataConverter.convertData(minimedCommandReply);

        }
        catch (PlugInBaseException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                handler.closeDevice();
            } catch (PlugInBaseException e) {
                System.out.println("Error disconnecting from device: " + e);
            }
        }

    }

}
