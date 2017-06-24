package ggc.meter.device.arkray.impl;

import ggc.meter.device.arkray.ArkrayMeterDataReader;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;

/**
 * GT-1650. Implementation of this is not complete. It just reads serialNumber.
 */

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
 *  Filename:     ArkrayGlucoCardMemoryPC
 *  Description:  Data/device reader for Arkray GlucoCard Memory PC (GT-1650) (not complete)
 *
 *  Author: Andy {andy@atech-software.com}
 */

@Deprecated
public class ArkrayGlucoCardMemoryPC extends ArkrayGlucoCard
{

    String line = "";


    public ArkrayGlucoCardMemoryPC(ArkrayMeterDataReader dataReader)
    {
        super(dataReader);
    }

    int command = 0;


    public String getSerialNumber() throws PlugInBaseException
    {
        this.line = "";
        this.command = 1;

        return this.line;
    }


    public void setSerialNumber(String serial) throws PlugInBaseException
    {
        this.command = 0;
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        this.command = 1;
        readData();
    }


    public void readData() throws PlugInBaseException
    {
        int command_f = this.command;
        int serial_f = 0;

        // final String serial_f = serial;

        // if (this.sendNak())
        // {
        try
        {
            this.setReceiveTimeout(3000);

            ArkrayMeterMode mode = ArkrayMeterMode.ActionRecieve;
            boolean exit = false;
            char c;

            while ((c = this.readChar()) != '\025')
            {
                switch (c)
                {
                    case ACK:
                        {
                            // System.out.println("ACK");
                            if (mode == ArkrayMeterMode.ActionRecieve)
                            {
                                this.writeMessage((command_f == 1 ? "R" : "W") + "|");
                                mode = ArkrayMeterMode.AddressTokenRecieve;
                            }
                            else if (mode == ArkrayMeterMode.AddressTokenRecieve)
                            {
                                this.writeMessage("S|");
                                mode = ArkrayMeterMode.DataRecieve;
                            }
                            else if (mode == ArkrayMeterMode.DataRecieve)
                            {
                                if (command_f == 1)
                                {
                                    this.write(24);
                                }
                                else
                                {
                                    this.writeMessage(serial_f + "|" + '\r' + '\n');
                                    mode = ArkrayMeterMode.WriteCompleted;
                                }
                            }
                            else if (mode == ArkrayMeterMode.WriteCompleted)
                            {
                                this.write(24);
                                exit = true;
                            }
                        }
                        break;

                    case NAK:
                    case CR:
                        break;

                    case LF:
                        // System.out.println("LF");
                        if (command_f == 1)
                        {
                            this.write(24);
                        }
                        exit = true;
                        break;

                    default:
                        this.line += c;
                }

                if (exit)
                    break;
            }

            if (c == NAK)
            {
                // System.out.println("NAK");
            }

            if ((command_f == 1) && (this.line.contains("|")))
            {
                this.line = this.line.split("\\|")[1];
            }
            // System.out.println("line->" + this.line);
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
        // }

    }

}
