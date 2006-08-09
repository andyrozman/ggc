/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.imports;


import ggc.data.DailyValuesRow;
import ggc.data.event.ImportEvent;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.comm.NoSuchPortException;
import javax.comm.SerialPortEvent;
import javax.swing.ImageIcon;

import ggc.util.DataAccess;

/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class EuroFlashImport extends SerialMeterImport
{

    //private I18nControl m_ic = I18nControl.getInstance();

    private int counter = 0;

    private DailyValuesRow[] importedData = new DailyValuesRow[0];

    public static final int CONTROL_FIRST = 0x40;

    public static final int CONTROL_SECOND = 0x3F;

    /**
     * Constructor for EuroFlashImport.
     */
    public EuroFlashImport()
    {
        super();

        setImage(new ImageIcon(getClass().getResource("/icons/euroflash.gif")));
        setUseInfoMessage("<html>" + m_ic.getMessage("CONNECT_YOUR_CABLE_WITH_METER") +".<br>" + m_ic.getMessage("TURN_OFF_METER_PRESS_IMPORT") + "</html>");
        setName("EuroFlash");
    }

    /**
     * @see data.imports.DataImport#importData()
     */
    public void importData() throws ImportException
    {
        super.importData();

        try
        {
            portOutputStream.write('D');
            portOutputStream.write('M');
            portOutputStream.write('@');
        }
        catch (IOException e)
        {
        }
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

        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {

            try
            {
                StringBuffer inputBuffer = createBufferFromStream(portInputStream);

                char ident = inputBuffer.charAt(0);

                if (ident == '@')
                {
                    portOutputStream.write('D');
                    portOutputStream.write('M');
                    portOutputStream.write('?');
                    timeOut += 15000;
                    System.out.println("second char written.");
                }

                if (ident == '?')
                {
                    portOutputStream.write('D');
                    portOutputStream.write('M');
                    portOutputStream.write('P');
                    timeOut += 30000;
                    System.out.println("third char written.");
                }

                if (ident == 'P')
                {
                    timeOut += 15000;
                    parseDataString(new String(inputBuffer));
                    stopImport();

                }

            }
            catch (Exception exc)
            {
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

        while (newData != -1)
        {
            try
            {
                newData = stream.read();
                if (newData == -1)
                {
                    break;
                }
                if ('\r' == (char)newData)
                {
                    inputBuffer.append('\n');
                }
                else
                {
                    //System.out.println("byte : " + newData);
                    inputBuffer.append((char)newData);
                }
            }
            catch (IOException ex)
            {
                System.err.println(ex);
                return inputBuffer;
            }
        }

        return inputBuffer;
    }

    private void parseDataString(String dataStr) throws ImportException
    {
        String value = null;
        Vector<DailyValuesRow> importDataVector = new Vector<DailyValuesRow>();

        try
        {
            int currentCount = 0;
            int valueCount = 0;
            String measure = "";

            StringTokenizer stk = new StringTokenizer(dataStr, "\n");

            String tmp = null;
            StringTokenizer vstk = null;

            ////////////////////////////////////////
            value = stk.nextToken();
            vstk = new StringTokenizer(value, ",");

            // first value is information
            // ex.: P 150,"NNS210DPN","MG/DL " 05BD
            //String subValue = vstk.nextToken();

            // P 150
            String subValue = vstk.nextToken();
            valueCount = Integer.decode(subValue.substring(2)).intValue();

            // "NNS210DPN"; not used
            vstk.nextToken();

            // "MG/DL " 05BD
            vstk.nextToken();
            subValue = subValue.substring(1);
            int pos = subValue.indexOf('"');
            if (pos != -1)
            {
                //Log.getLogger().info("subValue : " + subValue);
                measure = subValue.substring(0, pos);
            }
            ////////////////////////////////////////

            // value string: P "SAT"," 6/02/01"," 1:21:30   ","  228 ", 00 07F6
            //DateFormat dFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("en", "US"))
            SimpleDateFormat dFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
            while (stk.hasMoreTokens())
            {
                value = stk.nextToken();

                vstk = new StringTokenizer(value, ",");

                // P "SAT"; I don't need it
                if (!vstk.hasMoreTokens())
                    continue;
                subValue = vstk.nextToken();

                // " 6/02/01"," 1:21:30   " ; the day mm/dd/yy
                if (!vstk.hasMoreTokens())
                    continue;
                subValue = vstk.nextToken();
                String dayValue = subValue.substring(1, subValue.length() - 2).trim();

                if (!vstk.hasMoreTokens())
                    continue;
                subValue = vstk.nextToken();
                String timeValue = subValue.substring(1, subValue.length() - 2).trim();

                Date date = dFormat.parse(dayValue + " " + timeValue, new ParsePosition(0));
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.roll(Calendar.YEAR, 2000);
                date = cal.getTime();

                // "  228 " bzValue
                if (!vstk.hasMoreTokens())
                    continue;
                subValue = vstk.nextToken();
                subValue = subValue.substring(1, subValue.length() - 2).trim();
                float bzValue = Float.parseFloat(subValue);

                //
                //Value dataValue = new Value(date, bzValue);

                //DataAccess.getInstance().getDateTimeFromDateObject(Date dt)
                DailyValuesRow dataValue = new DailyValuesRow(DataAccess.getInstance().getDateTimeFromDateObject(date), bzValue, 0, 0, 0, 0, "");
                importDataVector.addElement(dataValue);
                //Log.getLogger().info("new value : " + dataValue);

                currentCount++;
                fireImportChanged(new ImportEvent(this, ImportEvent.PROGRESS, (currentCount * 100 / valueCount)));
            }
        }
        catch (Exception exc)
        {
            //System.out.println("parsing failed (" + value + ") : " + exc);
            //exc.printStackTrace();
            throw new ImportException(exc);
        }

        importedData = new DailyValuesRow[importDataVector.size()];
        importDataVector.copyInto(importedData);

        //Log.getLogger().info("parse count="+valueCount+", measure="+measure);
        //System.out.println("parsing finished; found : " + getImportedData().length);
    }

    public static void main(String[] args)
    {
        EuroFlashImport importer = new EuroFlashImport();

        try
        {

            importer.setPort("COM2");
            importer.open();
            importer.importData();

        }
        catch (NoSuchPortException e)
        {
            System.out.println("exc : " + e);
        }
        catch (ImportException e)
        {
            System.out.println("exc : " + e);
        }

        //importer.close();
    }
}
