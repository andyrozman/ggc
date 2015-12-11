package ggc.plugin.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.device.PlugInBaseException;

/**
 * Created by andy on 08.06.15.
 */
public abstract class SerialCommunicationAbstract implements SerialCommunicationInterface
{

    private static final Logger LOG = LoggerFactory.getLogger(SerialCommunicationAbstract.class);


    public boolean connectAndInitDevice() throws PlugInBaseException
    {
        LOG.warn("connectAndInitDevice not implemented.");
        return false;
    }


    public void disconnectDevice()
    {
        LOG.warn("disconnectDevice() not implemented.");
    }


    public boolean isDataAvailable()
    {
        LOG.warn("isDataAvailable() not implemented.");
        return false;
    }


    public int available() throws PlugInBaseException
    {
        LOG.warn("available() not implemented.");
        return -1;
    }


    public int read() throws PlugInBaseException
    {
        LOG.warn("read() not implemented.");
        return -1;
    }


    public int read(byte[] buffer) throws PlugInBaseException
    {
        LOG.warn("read(byte[]) not implemented.");
        return -1;
    }


    public int read(int[] buffer) throws PlugInBaseException
    {
        LOG.warn("read(int[]) not implemented.");
        return -1;
    }


    public int read(byte[] b, int off, int len) throws PlugInBaseException
    {
        LOG.warn("read(byte[],int,int) not implemented.");
        return -1;
    }


    public void write(byte toWrite) throws PlugInBaseException
    {
        LOG.warn("write(byte) not implemented.");
    }


    public void write(int toWrite) throws PlugInBaseException
    {
        LOG.warn("write(int) not implemented.");
    }


    public void write(byte[] buffer) throws PlugInBaseException
    {
        LOG.warn("write(byte[]) not implemented.");
    }


    public void write(byte[] b, int off, int len) throws PlugInBaseException
    {
        LOG.warn("write(byte[],int,int) not implemented.");
    }


    // reading data (new methods)

    public byte[] readLineBytes() throws PlugInBaseException
    {
        LOG.warn("readLineBytes() not implemented.");
        return null;
    }


    public String readLine() throws PlugInBaseException
    {
        LOG.warn("readLine() not implemented.");
        return null;
    }


    public int readByteTimed() throws PlugInBaseException
    {
        LOG.warn("readByteTimed() not implemented.");
        return -1;
    }


    public void setDelayForTimedReading(int ms)
    {
        LOG.warn("setDelayForTimedReading() not implemented.");
    }


    public void write(int[] cmd) throws PlugInBaseException
    {
        LOG.warn("write(int[]) not implemented.");
    }


    public int getReceiveTimeout()
    {
        LOG.warn("getReceiveTimeout() not implemented.");
        return -1;
    }


    public void setReceiveTimeout(int timeout) throws PlugInBaseException
    {
        LOG.warn("setReceiveTimeout() not implemented.");
    }


    public void setSerialSettings(SerialSettings settings)
    {
        LOG.warn("setSerialSettings() not implemented.");
    }


    public SerialSettings createDefaultSerialSettings()
    {
        LOG.warn("createDefaultSerialSettings() not implemented.");
        return null;
    }


    public boolean isDeviceConnected()
    {
        LOG.warn("isDeviceConnected() not implemented.");
        return false;
    }


    public int[] convertByteArrayToIntArray(byte[] inputArray)
    {
        int[] dataOut = new int[inputArray.length];

        for (int i = 0; i < inputArray.length; i++)
        {
            int o = inputArray[i];

            if (o < 0)
                o += 256;

            dataOut[i] = o;
        }

        return dataOut;
    }


    public int[] convertByteArrayToIntArray(byte[] inputArray, int[] outputArray)
    {
        // int[] dataOut = new int[inputArray.length];

        for (int i = 0; i < inputArray.length; i++)
        {
            int o = inputArray[i];

            if (o < 0)
                o += 256;

            outputArray[i] = o;
        }

        return outputArray;
    }

}
