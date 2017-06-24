package ggc.meter.device.arkray.impl;

import ggc.meter.device.arkray.ArkrayMeterDataReader;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;

// GlucoMenGMDriver

/**
 * GT-1960
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
 *  Filename:     ArkrayGlucocardMX
 *  Description:  Data/device reader for Arkray GlucoCard MX (GT-1960)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class ArkrayGlucocardMX extends ArkraySerialMeterAbstract
{

    public ArkrayGlucocardMX(ArkrayMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    public void preInitDevice() throws PlugInBaseException
    {
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

            while ((c = readChar()) != '\025')
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
                        readUntilACKReceived();

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

}
