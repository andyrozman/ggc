/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: GlucoCardReader.java
 *  Purpose:  Class for reading data from the GlucoCard Meter.
 *
 *  Author:   schultd
 */

package ggc.data.imports;


import ggc.datamodels.DailyValuesRow;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.comm.SerialPortEvent;
import javax.swing.ImageIcon;


public class GlucoCardImport extends SerialMeterImport
{
    
    //private I18nControl m_ic = I18nControl.getInstance();
    
    Thread gThread;

    static int counter = 0;

    static int h = 0;

    static byte[] readb = new byte[100];


    public static final int CONTROL_NUL = 0x00;
    public static final int CONTROL_STX = 0x02;
    public static final int CONTROL_EOT = 0x04;
    public static final int CONTROL_ENQ = 0x05;
    public static final int CONTROL_ACK = 0x06;
    public static final int CONTROL_LF = 0x0A;
    public static final int CONTROL_DC1 = 0x11;

    private Vector importedData = null;


    public GlucoCardImport()
    {
        super();

        setImage(new ImageIcon(getClass().getResource("/icons/glucocard.gif")));
        setUseInfoMessage("<html>" + m_ic.getMessage("CONNECT_YOUR_CABLE_WITH_METER") +".<br>" + m_ic.getMessage("TURN_OFF_METER_PRESS_IMPORT") + "</html>");
        setName("GlucoCard");
    }

    public void serialEvent(SerialPortEvent event)
    {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {
                byte[] rBuffer = new byte[40];

                int numBytes = portInputStream.read(rBuffer);
                //rmf.addLogText("Getting data buffer[0]=" + rBuffer[0]);

                if (rBuffer[0] == CONTROL_EOT && counter == 0) {
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    counter++;
                }
                if (rBuffer[0] == CONTROL_ENQ && counter == 1) {
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    counter++;
                }
                if (rBuffer[0] == CONTROL_EOT && rBuffer[1] == 0 && counter == 2) {
                    portOutputStream.write(CONTROL_DC1);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    portOutputStream.write(CONTROL_STX);
                    counter++;
                }
                if (rBuffer[0] == CONTROL_ENQ && counter == 3) {
                    portOutputStream.write(CONTROL_ACK);
                    counter++;
                }
                if (counter > 3) {

                    int j = 0;
                    while (j < rBuffer.length && rBuffer[j] != 0)
                        readb[h++] = rBuffer[j++];

                    if (readb[h - 1] == CONTROL_LF) {
                        String sAusgabe = (new String(readb)).trim();
                        if (sAusgabe.charAt(1) == 'H') {

                        }
                        if (sAusgabe.charAt(1) == 'R') {
                            int b = 0;
                            for (int i = 0; i < 3; i++)
                                b = sAusgabe.indexOf("|", b + 1);

                            String value = sAusgabe.substring(b + 1, sAusgabe.indexOf("|", b + 1));
                            String datetime = sAusgabe.substring(sAusgabe.lastIndexOf("|") + 1, sAusgabe.length() - 4);

                            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
                            Date date = null;
                            float bgvalue = 0;
                            try {
                                date = new Date(sf.parse(datetime).getTime());
                                date.setTime(date.getTime() - (date.getTime() % 60000L));

                                bgvalue = (new Float(value).floatValue());
                            } catch (ParseException e) {
                                System.out.println(e);
                            }

                            //rmf.addLogText("Got value: " + value + " for timestamp: " + date);

                            if (date != null) {
                                DailyValuesRow dVR = new DailyValuesRow(date, bgvalue, 0, 0, 0, 0, "");
                                importedData.addElement(dVR);
                            }

                        }

                        for (int z = 0; z < 100; z++)
                            readb[z] = 0;
                        h = 0;
                        portOutputStream.write(CONTROL_ACK);
                    }
                    counter++;
                }

            } catch (IOException e) {
                System.out.println(e);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
    }


    /**
     * @see data.imports.DataImport#getImportedData()
     */
    public DailyValuesRow[] getImportedData()
    {
        DailyValuesRow[] values = new DailyValuesRow[importedData.size()];
        importedData.toArray(values);
        return values;
    }

    /**
     * @see data.imports.DataImport#importData()
     */
    public void importData() throws ImportException
    {
        importedData = new Vector();
        super.importData();

        try {
            portOutputStream.write(CONTROL_NUL);
            portOutputStream.write(CONTROL_STX);
        } catch (IOException e) {
        }
    }
}