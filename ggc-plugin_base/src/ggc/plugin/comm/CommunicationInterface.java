package ggc.plugin.comm;

import ggc.plugin.device.PlugInBaseException;

/**
 * Created by andy on 08.06.15.
 */
public interface CommunicationInterface
{

    boolean connectAndInitDevice() throws PlugInBaseException;


    void disconnectDevice();


    // reading data (base methods)

    boolean isDataAvailable();


    int available() throws PlugInBaseException;


    int read() throws PlugInBaseException;


    int read(byte[] buffer) throws PlugInBaseException;


    int read(byte[] b, int off, int len) throws PlugInBaseException;


    void write(byte toWrite) throws PlugInBaseException;


    void write(int toWrite) throws PlugInBaseException;


    void write(byte[] buffer) throws PlugInBaseException;


    void write(byte[] b, int off, int len) throws PlugInBaseException;


    // reading data (new methods)

    byte[] readLineBytes() throws PlugInBaseException;


    String readLine() throws PlugInBaseException;


    int readByteTimed() throws PlugInBaseException;


    void setDelayForTimedReading(int ms);


    /**
     * @deprecated
     */
    void write(int[] cmd) throws PlugInBaseException;


    int getReceiveTimeout();


    void setReceiveTimeout(int timeout) throws PlugInBaseException;


    void flush() throws PlugInBaseException;
}
