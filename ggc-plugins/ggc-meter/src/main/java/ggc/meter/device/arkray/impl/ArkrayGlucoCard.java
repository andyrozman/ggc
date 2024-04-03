package ggc.meter.device.arkray.impl;

import ggc.meter.device.arkray.ArkrayMeterDataReader;
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
 *  Filename:     ArkrayGlucoCard
 *  Description:  Data/device reader for Arkray GlucoCard
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class ArkrayGlucoCard extends ArkraySerialMeterAbstract
{

    boolean isRunning;


    public ArkrayGlucoCard(ArkrayMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    protected void preInitDevice() throws PlugInBaseException
    {
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        isRunning = true;

        while (this.isRunning)
        {
            try
            {
                String line = "";

                short jump = 1;

                char c;
                while ((c = this.readChar()) != EOT)
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
                        case CR:
                            break;
                        case LF:
                            this.write(6);

                            this.decode(line);

                            line = "";
                            break;
                        default:
                            if (jump == 0)
                                line = line + c;
                            else
                            {
                                jump = (short) (jump - 1);
                            }
                            break;
                    }
                }

                this.isRunning = false;
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
        } // is running

    }

}
