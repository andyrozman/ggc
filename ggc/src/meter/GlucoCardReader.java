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

package meter;


import datamodels.DailyValuesRow;
import gui.ReadMeterFrame;

import javax.comm.*;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TooManyListenersException;


public class GlucoCardReader extends SerialMeterReader
{
    Thread gThread;
    static int counter = 0;
    static int h = 0;
    static byte[] readb = new byte[100];

    public GlucoCardReader()
    {
        gV = ReadMeterFrame.getInstance().getGlucoValues();
        rmf = ReadMeterFrame.getInstance();
        try {
            portId = CommPortIdentifier.getPortIdentifier(props.getMeterPort());
            rmf.addLogText("Port: " + props.getMeterPort());
        } catch (NoSuchPortException e) {
        }

        try {
            sPort = (SerialPort)portId.open("ggc", 2000);
        } catch (PortInUseException e) {
        }

        try {
            sPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
            sPort.enableReceiveTimeout(30);
        } catch (UnsupportedCommOperationException e) {
        }

        try {
            oStream = sPort.getOutputStream();
            iStream = sPort.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        sPort.notifyOnDataAvailable(true);
        gThread = new Thread(this);
        gThread.start();
        try {
            oStream.write(CONTROL_NUL);
            oStream.write(CONTROL_STX);
        } catch (IOException e) {
        }
    }

    public void serialEvent(SerialPortEvent event)
    {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {
                byte[] rBuffer = new byte[40];

                int numBytes = iStream.read(rBuffer);

                if (rBuffer[0] == CONTROL_EOT && counter == 0) {
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    counter++;
                }
                if (rBuffer[0] == CONTROL_ENQ && counter == 1) {
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    counter++;
                }
                if (rBuffer[0] == CONTROL_EOT && rBuffer[1] == 0 && counter == 2) {
                    oStream.write(CONTROL_DC1);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    oStream.write(CONTROL_STX);
                    counter++;
                }
                if (rBuffer[0] == CONTROL_ENQ && counter == 3) {
                    oStream.write(CONTROL_ACK);
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

                                rmf.addLogText("Got value: " + value + " for timestamp: " + date);

                                if (date != null) {
                                    DailyValuesRow dVR = new DailyValuesRow(date, bgvalue, 0, 0, 0, 0, "");
                                    gV.setNewRow(dVR);
                                }

                            }

                            for (int z = 0; z < 100; z++)
                                readb[z] = 0;
                            h = 0;
                            oStream.write(CONTROL_ACK);

                            if (sAusgabe.charAt(1) == 'L') {
                                sPort.close();
                                rmf.addLogText("Thread destroyed");
                                gThread.destroy();
                            }
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
}
