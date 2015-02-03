package ggc.plugin.device.impl.animas.comm;

import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;
import ggc.plugin.device.impl.animas.util.AnimasException;
import ggc.plugin.device.impl.animas.util.AnimasExceptionType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;
import gnu.io.NRSerialPort;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     AnimasCommProtocolAbstract
 *  Description:  This is abstract communication class. It contains just basic commands for
 *       serial communication, all other communication classes must extends this one.
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AnimasCommProtocolAbstract
{
    public static final Log LOG = LogFactory.getLog(AnimasCommProtocolAbstract.class);

    protected NRSerialPort serialDevice;
    protected String portName;
    //public boolean cancelDownload = false;
    //public boolean downloadCompleted = false;
    protected InputStream inStream;
    protected OutputStream outStream;
    protected AnimasDeviceType deviceType;

    protected boolean DEBUG = true;
    protected boolean debugCommunication = false;


    protected AnimasException downloadProblem;

    protected AnimasDeviceData baseData;
    protected AnimasDeviceReader deviceReader;
    private boolean deviceConnected;
    protected OutputWriter outputWriter;


    public AnimasCommProtocolAbstract(String portName, AnimasDeviceType deviceType,
            AnimasDeviceReader deviceReader, OutputWriter outputWriter)
    {
        this.portName = portName;
        this.deviceType = deviceType;
        // this.transferType = transferType;
        this.deviceReader = deviceReader;
        this.outputWriter = outputWriter;

        //data.setPumpCommunicationInterface(this);
    }


    public boolean initSerialDevice()
    {
        if ((this.serialDevice != null) && (this.serialDevice.isConnected()))
        {
            return true;
        }

        this.serialDevice = new NRSerialPort(portName, 9600); // 9600

        this.deviceConnected = this.serialDevice.connect();

        LOG.debug("Connect: " + this.deviceConnected);

        if (!this.deviceConnected)
        {
            LOG.debug("Could not connect to port " + portName);

            return false;
        }


        try
        {
            SerialPort sport = this.serialDevice.getSerialPortInstance();
            sport.setDTR(false);
            sport.setRTS(true);

            inStream = this.serialDevice.getInputStream();
            outStream = this.serialDevice.getOutputStream();
        }
        catch (Exception ex)
        {
            LOG.error("Error setting streams: " + ex, ex);
            this.serialDevice = null;
            return false;
        }
        return true;
    }


    public void disconnectDevice()
    {
        if (serialDevice != null)
        {
            if (this.serialDevice.isConnected())
            {
                this.serialDevice.disconnect();
                this.serialDevice = null;
            }
        }

        this.deviceConnected = false;
    }





    public abstract AnimasImplementationType getImplementationType();

//    public boolean isDownloadCanceled()
//    {
//        return cancelDownload;
//    }
//
//    public boolean isDownloadCompleted()
//    {
//        return downloadCompleted;
//    }

    public boolean hasDownloadErrors()
    {
        return downloadProblem != null;
    }

    public AnimasDeviceData getData()
    {
        return baseData;
    }

    public void setBaseData(AnimasDeviceData data)
    {
        this.baseData = data;
    }


    protected List<Short> readDataFromDeviceInternal() throws AnimasException
    {
        List<Short> tempData = new ArrayList<Short>();

        byte[] buffer = new byte[1024];

        int len = -1;
        try
        {

            while (this.inStream.available() > 0)
            {
                len = this.inStream.read(buffer);
                for (int i = 0; i < len; i++)
                {
                    tempData.add(AnimasUtils.getUnsignedShort(buffer[i]));
                }
            }

            return tempData;
        }
        catch (Exception e)
        {
            LOG.error("Error reading from device. Exception: " + e, e);
            throw new AnimasException(AnimasExceptionType.ErrorCommunciationgWithDevice);
        }
    }


    protected boolean isDataAvailable()
    {
        try
        {
            return (this.inStream.available() > 0);
        }
        catch (IOException e)
        {
            return false;
        }
    }


}
