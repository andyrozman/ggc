/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.imports;


import ggc.meter.data.DailyValuesRow;
import gnu.io.SerialPortEvent;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.ImageIcon;



/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class FreeStyleImport extends SerialMeterImport
{

    //private I18nControl m_ic = I18nControl.getInstance();

    private DailyValuesRow[] importedData = new DailyValuesRow[0];

    private Charset charset = null;

    private int recieved = 0;

    /**
     * Constructor for FreeStyleImport
     */
    public FreeStyleImport()
    {
        super();

        setImage(new ImageIcon(getClass().getResource("/icons/freestyle.gif")));
        setUseInfoMessage("<html>" + m_ic.getMessage("CONNECT_YOUR_CABLE_WITH_METER") +".<br>" + m_ic.getMessage("TURN_OFF_METER_PRESS_IMPORT") + "</html>");
        setName("FreeStyle");
    }


    /**

     * @see data.imports.DataImport#importData()

     */
    @Override
    public void importData() throws ImportException
    {
        super.importData();

        charset = Charset.forName("US-ASCII");
        System.out.println("Charset : " + charset);

        try
        {
            portOutputStream.write('m');
            portOutputStream.write('e');
            portOutputStream.write('m');

            //portOutputStream.write(((int) 'm')+((int) 'e')+((int) 'm'));
        }
        catch (IOException e)
        {
        }

        timeOut = 15000;
    }


    /**

     * @see data.imports.DataImport#getImportedData()

     */
    //@Override
    public DailyValuesRow[] getImportedData()
    {
        return importedData;
    }


    /**

     * @see gnu.io.SerialPortEventListener#serialEvent(SerialPortEvent)

     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {
        super.serialEvent(event);

        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {

            try
            {
                //xbyte[] inputBuffer = createByteBufferFromStream(portInputStream);

                /*char ident = (char)inputBuffer[0]; */



                //System.out.println("ident : " + ident);



                // Append received data to messageAreaIn.

                //System.out.println("datalength : " + inputBuffer.length());

                //System.out.println(new String(inputBuffer, "UTF-16BE"));



                //portOutputStream.write((int) ident);

                portOutputStream.write(150);
                recieved++;
                System.out.println("recieved : " + recieved);

            }
            catch (Exception exc)
            {
                System.out.println("Exception while parsing new data. " + exc);
            }
        }
    }


}



