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
 *  Filename: GlucoReader.java
 *  Purpose:  Class for reading data from the GlucoCard Meter.
 *
 *  Author:   schultd
 */

package meter;


import datamodels.DailyValuesRow;
import datamodels.GlucoValues;
import gui.ReadMeterFrame;

import javax.comm.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TooManyListenersException;


public class GlucoReader implements Runnable, SerialPortEventListener
{
    InputStream iStream = null;
    OutputStream oStream = null;
    Thread gThread;
    CommPortIdentifier portId = null;
    SerialPort sPort = null;
    static int counter = 0;
    static int h = 0;
    static byte[] readb = new byte[100];
    ReadMeterFrame rmf = null;
    GlucoValues gV = null;

    public GlucoReader()
    {
        gV = ReadMeterFrame.getInstance().getGlucoValues();
        rmf = ReadMeterFrame.getInstance();
        try {
            portId = CommPortIdentifier.getPortIdentifier("/dev/ttyS1");
            rmf.addLogText("Port: /dev/ttyS1");
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
        //System.out.println("Start new thread");
        gThread.start();
        try {
            oStream.write(0x00);
            oStream.write(0x02);
            rmf.addLogText("Write: 0x00 0x02");
        } catch (IOException e) {
        }
    }

    public void run()
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void serialEvent(SerialPortEvent event)
    {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            byte[] rBuffer = new byte[40];
            try {
                int numBytes = iStream.read(rBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (rBuffer[0] == 4 && counter == 0) {
                try {
                    rmf.addLogText("Get:   0x04");
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    rmf.addLogText("Write: 0x02 0x02 0x02");
                } catch (IOException e) {
                }
                counter++;
            }
            if (rBuffer[0] == 5 && counter == 1) {
                try {
                    rmf.addLogText("Get:   0x04");
                    oStream.write(0x02);
                    oStream.write(0x02);
                    rmf.addLogText("Write: 0x02 0x02");
                } catch (IOException e) {
                }
                counter++;
            }
            if (rBuffer[0] == 4 && rBuffer[1] == 0 && counter == 2) {
                try {
                    rmf.addLogText("Get:   0x04");
                    oStream.write(0x11);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    oStream.write(0x02);
                    rmf.addLogText("Write: 11222222222222222");
                } catch (IOException e) {
                }
                counter++;
            }
            if (rBuffer[0] == 5 && counter == 3) {
                try {
                    rmf.addLogText("Get:   0x05");
                    oStream.write(0x06);
                    rmf.addLogText("Write: 0x06");
                    counter++;
                } catch (IOException e) {
                }
            }
            if (counter > 3) {
                try {
                    int j = 0;
                    while (j < rBuffer.length && rBuffer[j] != 0)
                        readb[h++] = rBuffer[j++];

                    if (readb[h - 1] == 0x0A) {
                        String sAusgabe = (new String(readb)).trim();
                        //System.out.println("Char @ 2: " + readb[2]);
                        if (sAusgabe.charAt(1) == 'H') {
                            System.out.println("Got a Header");
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

                            System.out.println("Got value: " + value + " for timestamp: " + date);

                            if (date != null) {
                                DailyValuesRow dVR = new DailyValuesRow(date, bgvalue, 0, 0, 0, 0, "");
                                gV.setNewRow(dVR);
                            }

                        }


                        rmf.addLogText("Get: " + sAusgabe);

                        for (int z = 0; z < 100; z++)
                            readb[z] = 0;
                        h = 0;
                        oStream.write(0x06);
                        rmf.addLogText("Write: 0x06");
                        if (sAusgabe.charAt(1) == 'L') {
                            sPort.close();
                            rmf.addLogText("Thread destroyed");
                            gThread.destroy();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            }
        }
    }
}
