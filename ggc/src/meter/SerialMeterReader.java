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
 *  Filename: SerialMeterReader.java
 *  Purpose:  Abstract superclass for all SerialReaders.
 *
 *  Author:   schultd
 */

package meter;


import datamodels.GlucoValues;
import gui.ReadMeterFrame;
import util.GGCProperties;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;


public abstract class SerialMeterReader implements Runnable, SerialPortEventListener
{
    InputStream iStream = null;
    OutputStream oStream = null;
    CommPortIdentifier portId = null;
    SerialPort sPort = null;
    ReadMeterFrame rmf = null;
    GlucoValues gV = null;
    GGCProperties props = GGCProperties.getInstance();

    public static final int CONTROL_NUL = 0x00;
    public static final int CONTROL_STX = 0x02;
    public static final int CONTROL_EOT = 0x04;
    public static final int CONTROL_ENQ = 0x05;
    public static final int CONTROL_ACK = 0x06;
    public static final int CONTROL_LF = 0x0A;
    public static final int CONTROL_DC1 = 0x11;

    public void run()
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Vector getAvaibleSerialPorts()
    {
        Vector retVal = new Vector();
        int counter = 0;

        Enumeration enum = CommPortIdentifier.getPortIdentifiers();
        while (enum.hasMoreElements()) {
            CommPortIdentifier portID = (CommPortIdentifier)enum.nextElement();
            if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL)
                retVal.add(portID.getName());
        }
        return retVal;
    }

    public abstract void serialEvent(SerialPortEvent event);
}
