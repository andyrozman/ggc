/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.imports;


import ggc.datamodels.DailyValuesRow;

import javax.comm.SerialPortEvent;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Vector;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class FreeStyleImport extends SerialMeterImport
{

    private DailyValuesRow[] importedData = new DailyValuesRow[0];

    private Charset charset = null;

    private int recieved = 0;

    /**
     * Constructor for FreeStyleImport
     */
    public FreeStyleImport()
    {
        super();

        setImage(new ImageIcon(getClass().getResource("/icons/freestyle.png")));
        setUseInfoMessage("<html>" + "Please connect your cable with your meter.<br>" + "Turn your meter on and press \"Import\"" + "</html>");
        setName("FreeStyle");
    }


    /**
     * @see data.imports.DataImport#importData()
     */
    public void importData() throws ImportException
    {
        super.importData();

        charset = Charset.forName("US-ASCII");
        System.out.println("Charset : " + charset);

        try {
            portOutputStream.write((int)'m');
            portOutputStream.write((int)'e');
            portOutputStream.write((int)'m');
            //portOutputStream.write(((int) 'm')+((int) 'e')+((int) 'm'));
        } catch (IOException e) {
        }

        timeOut = 15000;
    }


    /**
     * @see data.imports.DataImport#getImportedData()
     */
    public DailyValuesRow[] getImportedData()
    {
        return importedData;
    }


    /**
     * @see javax.comm.SerialPortEventListener#serialEvent(SerialPortEvent)
     */
    public void serialEvent(SerialPortEvent event)
    {
        super.serialEvent(event);

        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {
                byte[] inputBuffer = createByteBufferFromStream(portInputStream);

                char ident = (char)inputBuffer[0];

                //System.out.println("ident : " + ident);

                // Append received data to messageAreaIn.
                //System.out.println("datalength : " + inputBuffer.length());
                //System.out.println(new String(inputBuffer, "UTF-16BE"));

                //portOutputStream.write((int) ident);
                portOutputStream.write(150);
                recieved++;
                System.out.println("recieved : " + recieved);

            } catch (Exception exc) {
                System.out.println("Exception while parsing new data. " + exc);
            }
        }
    }

    /**
     *
     */
    private StringBuffer createBufferFromStream(InputStream stream)
    {
        StringBuffer inputBuffer = new StringBuffer();
        int newData = 0;

        while (newData != -1) {
            try {
                newData = stream.read();
                if (newData == -1) {
                    break;
                }
                if ('\r' == (char)newData) {
                    inputBuffer.append('\n');
                } else {
                    System.out.println("byte : " + newData);
                    inputBuffer.append((char)newData);
                }
            } catch (IOException ex) {
                System.err.println(ex);
                return inputBuffer;
            }
        }

        return inputBuffer;
    }


    /**
     *
     */
    private byte[] createByteBufferFromStream(InputStream stream)
    {
        Vector bytes = new Vector();
        int newData = 0;

        while (newData != -1) {
            try {
                newData = stream.read();
                if (newData == -1) {
                    break;
                }
                System.out.println("byte : " + newData);
                bytes.addElement(new Byte((byte)newData));

            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

        byte[] allBytes = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            allBytes[i] = ((Byte)bytes.elementAt(i)).byteValue();
        }

        return allBytes;
    }

}

