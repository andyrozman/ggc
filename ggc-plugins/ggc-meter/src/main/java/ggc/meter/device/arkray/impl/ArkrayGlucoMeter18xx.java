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
 *  Filename:     ArkrayGlucoMeter18xx
 *  Description:  Data/device reader for Arkray GlucoCard 18xx
 *
 *  Author: Andy {andy@atech-software.com}
 */
public class ArkrayGlucoMeter18xx extends ArkrayGlucoCard
{

    public ArkrayGlucoMeter18xx(ArkrayMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        try
        {
            StringBuffer lineData = new StringBuffer();

            char c;
            while ((c = this.readChar()) != LF)
            {
                // System.out.print(c);
            }

            this.write(21);

            while ((c = this.readChar()) != LF)
            {
                lineData.append(c);
            }

            this.decode(lineData.toString().substring(2));
            lineData.setLength(0);

            this.write(146);
            readUntilCharacterReceived(EOT);

            checkIfSerialNumberWasRead();

            this.write(5);
            readUntilACKReceived();

            this.writeMessage("W|");
            readUntilACKReceived();

            this.writeMessage("V|");
            readUntilACKReceived();

            this.writeMessage("1|");
            readUntilACKReceived();

            this.glucoseUnitType = this.getGlucoseUnitType();
            readUntilACKReceived();

            int n = 0;

            while (true)
            {
                this.writeMessage("R|");
                readUntilACKReceived();

                this.writeMessage("N|");
                readUntilACKReceived();

                this.writeMessage(right("000" + Integer.toHexString(n).toUpperCase(), 3) + "|");

                c = this.readChar();

                if (c == NAK)
                {
                    break;
                }

                lineData.append(c);

                while ((c = this.readChar()) != LF)
                {
                    lineData.append(c);
                }

                readUntilACKReceived();

                this.decode(lineData.toString());

                lineData.setLength(0);

                n++;
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
