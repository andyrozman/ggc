package ggc.meter.device.arkray.impl;

import ggc.meter.device.arkray.ArkrayMeterDataReader;
import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.comm.cfg.SerialSettingsType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:     ArkrayGlucoXMeter
 *  Description:  Data/device reader for Arkray GlucoCard X Meter (GT-1910)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class ArkrayGlucoXMeter extends ArkrayGlucoCard
{

    public ArkrayGlucoXMeter(ArkrayMeterDataReader dataReader)
    {
        super(dataReader);
    }


    public SerialSettings getSerialSettings()
    {
        return new SerialSettings(19200, SerialSettingsType.DataBits8, SerialSettingsType.StopBits2,
                SerialSettingsType.ParityNone);
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        try
        {
            StringBuffer lineData = new StringBuffer();
            ArkrayMeterMode mode = ArkrayMeterMode.ActionRecieve;

            int numPatient = 0;
            boolean remoteCommand = false;
            short jump = 1;

            char c;
            while ((c = this.readChar()) != NAK)
            {
                switch (c)
                {
                    case STX:
                        jump = 1;
                        break;

                    case ETX:
                    case ETB:
                        jump = 2;
                        break;

                    case ACK:
                        if (remoteCommand)
                        {
                            if (mode == ArkrayMeterMode.ActionRecieve)
                            {
                                this.writeMessage("R|");
                                mode = ArkrayMeterMode.AddressTokenRecieve;
                            }
                            else if (mode == ArkrayMeterMode.AddressTokenRecieve)
                            {
                                this.writeMessage("N|");
                                mode = ArkrayMeterMode.DataRecieve;
                            }
                            else // if (mode == ArkrayMeterMode.DataRecieve)
                            {
                                this.writeMessage(
                                    right("000" + Integer.toHexString(numPatient).toUpperCase(), 3) + "|");
                                mode = ArkrayMeterMode.ActionRecieve;
                                numPatient++;
                            }
                        }
                        break;

                    case CR:
                        break;

                    case LF:
                        this.decode(lineData.toString());

                        lineData.setLength(0);

                        if (!remoteCommand)
                        {
                            this.write(146);
                            remoteCommand = true;
                        }

                        checkIfSerialNumberWasRead();

                        break;

                    case EOT:
                        this.write(5);
                        this.readUntilACKReceived();
                        this.glucoseUnitType = this.getGlucoseUnitType();

                        break;

                    default:
                        if (jump == 0)
                            lineData.append(c);
                        else
                        {
                            jump = (short) (jump - 1);
                        }
                        break;
                }
            }

        }
        catch (PlugInBaseException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.ErrorWithDeviceCommunication,
                    new Object[] { ex.getMessage() }, ex);
        }
        finally
        {
            this.disconnectDevice();
        }
    }

    // protected boolean sendAck(CommPortIdentifier id)
    // {
    // boolean b = false;
    // try
    // {
    // init("", "S", "", id.getName(), this.baudRate, 8, 2, 0, "");
    // System.out.println("Check Port->" + id.getName());
    // openComm(false, true);
    // this.writeToDevice(2);
    // this.enableReceiveTimeout(3000);
    // try
    // {
    // char c;
    // while ((c = readChar()) != '\005')
    // ;
    // if (c == '\005')
    // {
    // this.writeToDevice(6);
    //
    // b = true;
    // }
    // else
    // {
    // closeComm();
    // }
    // }
    // catch (Throwable t)
    // {
    // closeComm();
    // t.printStackTrace();
    // }
    // }
    // catch (Throwable t)
    // {
    // t.printStackTrace();
    // }
    // return b;
    // }

    // it might work with bytes method
    // protected void _writeMessage(String message) throws IOException
    // {
    // char[] messageChar = message.toCharArray();
    // for (int i = 0; i < messageChar.length; i++)
    // {
    // this.outputStream.write(messageChar[i]);
    // }
    // this.outputStream.flush();
    // }

}
