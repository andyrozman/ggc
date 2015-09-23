package ggc.meter.device.ascensia.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.BitUtils;

import ggc.meter.device.ascensia.AscensiaUsbMeterHandler;
import ggc.plugin.comm.Hid4JavaCommunicationHandler;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 21.09.15.
 */
public class AscensiaContourUsbReader
{

    private static Log LOG = LogFactory.getLog(AscensiaContourUsbReader.class);

    private Hid4JavaCommunicationHandler communicationHandler;
    private boolean debug = false;
    private BitUtils bitUtils = new BitUtils();
    private boolean communicationStopped = false;
    private AscensiaDecoder decoder;
    private AscensiaUsbMeterHandler handler;


    public AscensiaContourUsbReader(AscensiaUsbMeterHandler handler, OutputWriter outputWriter)
    {
        decoder = new AscensiaDecoder(outputWriter);
        communicationHandler = new Hid4JavaCommunicationHandler();
        communicationHandler.setAllowedDevices(handler.getAllowedDevicesList());
        communicationHandler.setDelayForTimedReading(1000);
        this.handler = handler;
    }

    public int countNAK = 0;


    public void readFromDevice() throws PlugInBaseException
    {
        this.communicationHandler.connectAndInitDevice();

        byte whatToSend = 0x04;

        while (true)
        {
            sendDataToDevice(whatToSend);
            List<Byte> data = readDataFromDevice();

            if (communicationStopped)
            {
                break;
            }

            byte lastData;

            if (data.size() == 0)
            {
                lastData = 0x05;
            }
            else
            {
                lastData = data.get(data.size() - 1);
            }

            if (lastData == 0x15)
            {
                if (debug)
                    LOG.debug("Last device response: NAK (0x15)");
                // got a <NAK>, send <EOT>
                whatToSend = (byte) countNAK;
                countNAK++;
                if (countNAK == 256)
                    countNAK = 0;
            }
            else if (lastData == 0x05)
            {
                if (debug)
                    LOG.debug("Last device response: ENQ (0x05)");
                whatToSend = 0x06;
            }
            else if (lastData == 0x04)
            {
                if (debug)
                    LOG.debug("Last device response: EOT (0x04) - Exiting");
                break;
            }
        }

    }


    private void sendDataToDevice(byte whatToSend) throws PlugInBaseException
    {
        sleep(50);

        if (debug)
            LOG.debug("Sending: " + whatToSend);

        byte[] packet = createPacket(whatToSend);

        int ret = this.communicationHandler.writeWithReturn(packet);

        if (ret == 0)
        {
            LOG.error("Error on write to meter.");
            throw new PlugInBaseException(PlugInExceptionType.DeviceCouldNotBeContacted);
        }
        else
        {
            if (debug)
                LOG.debug("Write successful.");
        }
    }


    private byte[] createPacket(byte whatToSend)
    {
        byte[] msgData = new byte[64];

        msgData[0] = 0;
        msgData[1] = 0;
        msgData[2] = 0;
        msgData[3] = 1; // size
        msgData[4] = (byte) whatToSend;

        return msgData;
    }


    private void sleep(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {}
    }


    private boolean containsSTX(byte[] data)
    {
        for (byte b : data)
        {
            if (b == 0x02)
            {
                return true;
            }
        }

        return false;
    }


    private boolean containsRecordTermination(byte[] data)
    {
        boolean data0d = false;

        for (byte b : data)
        {
            if (b == 0x0d)
            {
                data0d = true;
            }
            else if ((data0d) && (b == 0x0a))
            {
                return true;
            }

        }

        return false;
    }


    private String getText(byte[] data)
    {
        boolean text = false;
        StringBuffer sb = new StringBuffer();

        for (byte b : data)
        {
            if (b == 0x02)
            {
                text = true;
            }

            if (b == 0x0d)
            {
                text = false;
            }

            if (text)
            {
                sb.append((char) b);
            }
        }

        return sb.toString();
    }


    private String getRawText(byte[] data)
    {
        StringBuffer sb = new StringBuffer();

        for (byte b : data)
        {
            sb.append((char) b);
        }

        return sb.toString();
    }


    private List<Byte> readDataFromDevice() throws PlugInBaseException
    {
        List<Byte> dataOut = new ArrayList<Byte>();

        while (true)
        {
            byte[] dataFramed = readDataFromDeviceInternal();

            if (dataFramed == null)
            {
                // return null;
                break;
            }

            byte[] data = bitUtils.getByteSubArray(dataFramed, 4, dataFramed[3]);

            if (containsRecordTermination(data))
            {
                if (dataOut.size() == 0)
                {
                    processLine(data);
                }
                else
                {
                    bitUtils.addByteArrayToList(dataOut, data);
                    processLine(bitUtils.getByteArrayFromList(dataOut));
                    dataOut.clear();
                }
            }
            else
            {
                dataOut.addAll(bitUtils.getListFromByteArray(data));
            }

            if (data.length <= 36) // 36
                break;
        }

        return dataOut;
    }


    public void processLine(byte[] data)
    {
        if (containsSTX(data))
        {
            String text = getText(data);
            String code = decoder.decode(text);

            if (code.equals("T"))
                communicationStopped = true;
        }
        else
        {
            System.out.println("Raw: " + getRawText(data));
        }

    }


    private byte[] readDataFromDeviceInternal() throws PlugInBaseException
    {
        int ret = 0;

        byte[] buf = new byte[64];

        ret = communicationHandler.read(buf);

        if (debug)
            System.out.println("Read (returned " + ret + ")");

        if (ret <= 0)
            return null;

        return buf;
    }


    public void closeDevice()
    {
        this.communicationHandler.disconnectDevice();
    }

}
