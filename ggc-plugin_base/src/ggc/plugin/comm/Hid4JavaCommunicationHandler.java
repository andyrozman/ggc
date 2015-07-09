package ggc.plugin.comm;

import ggc.plugin.device.PlugInBaseException;

/**
 * Created by andy on 08.06.15.
 */
public class Hid4JavaCommunicationHandler implements CommunicationInterface
{

    public boolean connectAndInitDevice() throws PlugInBaseException
    {
        return false;
    }


    public void disconnectDevice()
    {

    }


    public boolean isDataAvailable()
    {
        return false;
    }


    public int available() throws PlugInBaseException
    {
        return 0;
    }


    public int read() throws PlugInBaseException
    {
        return 0;
    }


    public int read(byte[] buffer) throws PlugInBaseException
    {
        return 0;
    }


    public int read(byte[] b, int off, int len) throws PlugInBaseException
    {
        return 0;
    }


    public void write(int toWrite) throws PlugInBaseException
    {

    }


    public void write(byte[] buffer) throws PlugInBaseException
    {

    }


    public void write(byte[] b, int off, int len) throws PlugInBaseException
    {

    }


    public byte[] readLineBytes() throws PlugInBaseException
    {
        return new byte[0];
    }


    public String readLine() throws PlugInBaseException
    {
        return null;
    }


    public int readByteTimed() throws PlugInBaseException
    {
        return 0;
    }


    public void setDelayForTimedReading(int ms)
    {

    }


    public void write(int[] cmd) throws PlugInBaseException
    {

    }


    public int getReceiveTimeout()
    {
        return 0;
    }


    public void setReceiveTimeout(int timeout) throws PlugInBaseException
    {

    }
}
