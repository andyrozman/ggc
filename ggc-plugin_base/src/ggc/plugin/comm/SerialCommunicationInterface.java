package ggc.plugin.comm;

import ggc.plugin.device.PlugInBaseException;

import java.io.IOException;

/**
 * Created by andy on 11.03.15.
 */
public interface SerialCommunicationInterface
{

    // connect and settings

    //public boolean open() throws PlugInBaseException;

    void setSerialSettings(SerialSettings settings);

    SerialSettings createDefaultSerialSettings();

    boolean connectAndInitDevice() throws PlugInBaseException;

    void disconnectDevice();


    // reading data (base methods)

    boolean isDataAvailable();

    int available() throws PlugInBaseException;

    int read() throws PlugInBaseException;

    int read(byte[] buffer) throws PlugInBaseException;

    int read(byte[] b, int off, int len) throws PlugInBaseException;

    void write(int toWrite) throws PlugInBaseException;

    void write(byte[] buffer) throws PlugInBaseException;

    void write(byte[] b, int off, int len) throws PlugInBaseException;

    // reading data (new methods)

    byte[] readLineBytes() throws PlugInBaseException;

    String readLine() throws PlugInBaseException;

    int readByteTimed() throws PlugInBaseException;

    void setDelayForTimedReading(int ms);

}
