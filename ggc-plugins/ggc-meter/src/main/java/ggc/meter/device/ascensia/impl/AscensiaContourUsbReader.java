package ggc.meter.device.ascensia.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.meter.device.ascensia.AscensiaUsbMeterHandler;
import ggc.plugin.comm.Hid4JavaCommunicationHandler;
import ggc.plugin.data.enums.ASCII;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.AbstractDeviceReader;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:     AscensiaContourUsbReader
 *  Description:  Ascensia Contour Usb Reader
 *
 *  Author: Andy {andy@atech-software.com}
 */
public class AscensiaContourUsbReader extends AbstractDeviceReader
{

    private static final Logger LOG = LoggerFactory.getLogger(AscensiaContourUsbReader.class);

    private Hid4JavaCommunicationHandler communicationHandler;
    private boolean debug = false;
    private BitUtils bitUtils = new BitUtils();
    private boolean communicationStopped = false;
    private AscensiaDecoder decoder;
    private AscensiaUsbMeterHandler handler;
    private OutputWriter outputWriter;
    int responseCount = 0;


    public AscensiaContourUsbReader(AscensiaUsbMeterHandler handler, String selectedDevice, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        super(outputWriter, false);
        decoder = new AscensiaDecoder(outputWriter, handler.getMeterDefinition(), this);
        communicationHandler = new Hid4JavaCommunicationHandler();
        communicationHandler.setTargetDevice(selectedDevice);
        communicationHandler.setAllowedDevices(handler.getAllowedDevicesList());
        communicationHandler.setDelayForTimedReading(100);
        this.outputWriter = outputWriter;
        this.handler = handler;

        doInit();
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

            if (communicationStopped || outputWriter.isReadingStopped())
            {
                break;
            }

            byte lastData;

            if (debug)
                System.out.println("Data size: " + data.size() + ", Data: " + data);

            if (data.size() == 0)
            {
                lastData = 0x05;

                if (debug)
                    System.out.println("Last response, zero, data:  " + lastData);
            }
            else
            {
                lastData = data.get(data.size() - 1);

                if (debug)
                    System.out.println("Last response, data:  " + lastData);
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

        if (ret <= 0)
        {
            String errorMessage = this.communicationHandler.getLastErrorMessage();
            LOG.error("Error on write to meter. Error message: " + errorMessage);
            if (responseCount == 0)
                throw new PlugInBaseException(PlugInExceptionType.DeviceCouldNotBeContacted);
            else
                throw new PlugInBaseException(PlugInExceptionType.LostCommunicationWithDevice);
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
        return contains(ASCII.STX, data);
    }


    private boolean containsETX(byte[] data)
    {
        return contains(ASCII.ETX, data);
    }


    private boolean contains(ASCII asciiChar, byte[] data)
    {
        for (byte b : data)
        {
            if (b == asciiChar.getValue())
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
            if (b == ASCII.CR.getValue())
            {
                data0d = true;
            }
            else if ((data0d) && (b == ASCII.LF.getValue()))
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

            if (b == ASCII.STX.getValue())
            {
                text = true;
            }

            if (b == ASCII.CR.getValue())
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
                    // System.out.println("before ProcessLine: "
                    // + bitUtils.toString(dataOut, OutputMode.AsHex,
                    // OutputMode.DelimitedSpaceInBrackets));
                    processLine(data);
                }
                else
                {
                    // System.out.println("before ProcessLine(concat): "
                    // + bitUtils.toString(dataOut, OutputMode.AsHex,
                    // OutputMode.DelimitedSpaceInBrackets));
                    bitUtils.addByteArrayToList(dataOut, data);
                    processLine(bitUtils.getByteArrayFromList(dataOut));
                    dataOut.clear();
                }
            }
            else
            {
                // System.out.println(
                // "before add: " + bitUtils.toString(dataOut, OutputMode.AsHex,
                // OutputMode.DelimitedSpaceInBrackets));
                dataOut.addAll(bitUtils.getListFromByteArray(data));
                // System.out.println(
                // "after add: " + bitUtils.toString(dataOut, OutputMode.AsHex,
                // OutputMode.DelimitedSpaceInBrackets));

            }

            // if (containsETX(data))
            // {
            // System.out.println("!!!!!!!!!!!!!!! ETX FOUND
            // !!!!!!!!!!!!!!!!!!!!");
            // communicationStopped = true;
            // }

            if (data.length <= 36) // 36
                break;
        }

        return dataOut;
    }


    public void processLine(byte[] data) throws PlugInBaseException
    {
        if (containsSTX(data))
        {
            String text = getText(data);
            String code = decoder.decode(text);

            responseCount++;

            addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            if (code.equals("L"))
            {
                communicationStopped = true;
            }
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


    @Override
    public void readData() throws PlugInBaseException
    {

    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {

    }


    public void closeDevice()
    {
        this.communicationHandler.disconnectDevice();
    }


    @Override
    public void customInitAndChecks() throws PlugInBaseException
    {

    }


    @Override
    public void configureProgressReporter()
    {
        configureProgressReporter(ProgressType.Dynamic, 0, 0, this.handler.getMeterDefinition().getMaxRecords());
    }

}
