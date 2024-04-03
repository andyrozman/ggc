package ggc.plugin.protocol.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.comm.Hid4JavaCommunicationHandler;
import ggc.plugin.data.enums.ASCII;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.handler.UsbHidDeviceHandler;
import ggc.plugin.output.OutputWriter;

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
public abstract class UsbHidDeviceReader extends AbstractDeviceReader
{

    private static final Logger LOG = LoggerFactory.getLogger(UsbHidDeviceReader.class);

    protected Hid4JavaCommunicationHandler communicationHandler;
    private boolean debug = false;
    protected BitUtils bitUtils = new BitUtils();
    protected boolean communicationStopped = false;

    protected OutputWriter outputWriter;
    protected UsbHidDeviceHandler handler;


    public UsbHidDeviceReader(UsbHidDeviceHandler handler, String selectedDevice, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        super(outputWriter, false);
        // decoder = new AscensiaDecoder(outputWriter, handler.getMeterDefinition());
        communicationHandler = new Hid4JavaCommunicationHandler();
        communicationHandler.setTargetDevice(selectedDevice);
        communicationHandler.setAllowedDevices(handler.getAllowedDevicesList());
        communicationHandler.setDelayForTimedReading(100);
        this.outputWriter = outputWriter;
        this.handler = handler;
    }


    public abstract void initHandler();

    public int countNAK = 0;


    protected void connectAndInitDevice() throws PlugInBaseException
    {
        this.communicationHandler.connectAndInitDevice();
    }


    // public void readFromDevice() throws PlugInBaseException
    // {
    // this.communicationHandler.connectAndInitDevice();
    //
    // byte whatToSend = 0x04;
    //
    // while (true)
    // {
    // sendDataToDevice(whatToSend);
    // List<Byte> data = readDataFromDevice();
    //
    // if (communicationStopped || outputWriter.isReadingStopped())
    // {
    // break;
    // }
    //
    // byte lastData;
    //
    // if (debug)
    // System.out.println("Data size: " + data.size() + ", Data: " + data);
    //
    // if (data.size() == 0)
    // {
    // lastData = 0x05;
    //
    // if (debug)
    // System.out.println("Last response, zero, data: " + lastData);
    // }
    // else
    // {
    // lastData = data.get(data.size() - 1);
    //
    // if (debug)
    // System.out.println("Last response, data: " + lastData);
    // }
    //
    // if (lastData == 0x15)
    // {
    // if (debug)
    // LOG.debug("Last device response: NAK (0x15)");
    // // got a <NAK>, send <EOT>
    // whatToSend = (byte) countNAK;
    // countNAK++;
    // if (countNAK == 256)
    // countNAK = 0;
    // }
    // else if (lastData == 0x05)
    // {
    // if (debug)
    // LOG.debug("Last device response: ENQ (0x05)");
    // whatToSend = 0x06;
    // }
    // else if (lastData == 0x04)
    // {
    // if (debug)
    // LOG.debug("Last device response: EOT (0x04) - Exiting");
    // break;
    // }
    // }
    //
    // }

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
