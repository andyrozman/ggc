package ggc.meter.device.menarini.impl;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.device.menarini.MeanriniMeterDataReader;
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
 *  Filename:     AscensiaBreeze2
 *  Description:  Data/device reader for Arkray GlucoCard
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MenariniGlucoMenVisio extends MenariniSerialMeterAbstract
{

    public MenariniGlucoMenVisio(MeanriniMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    protected void preInitDevice() throws PlugInBaseException
    {
        String data = "";
        try
        {
            this.communicationHandler.setReceiveTimeout(5000);

            try
            {
                this.write(new byte[] { 1, 2, 3, 4, 6, 13 });

                Thread.sleep(500L);

                this.write(new byte[] { 83, 49, 13 });

                data = "";
                char c = readChar();
                while ((c = readChar()) != '\r')
                    data = data + c;
                // System.out.println("linea->" + data);
            }
            catch (Exception ex)
            {
                communicationHandler.disconnectDevice();
                throw new PlugInBaseException(PlugInExceptionType.ErrorWithDeviceCommunication,
                        new Object[] { ex.getMessage() }, ex);
            }
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.ErrorWithDeviceCommunication,
                    new Object[] { ex.getMessage() }, ex);
        }

        if (data.length() == 0)
        {
            throw new PlugInBaseException(PlugInExceptionType.ErrorWithDeviceCommunication,
                    new Object[] { "Device returned no response." });
        }
    }


    @Override
    public SerialSettings getSerialSettings()
    {
        return new SerialSettings(9600, SerialSettingsType.DataBits8, SerialSettingsType.StopBits1,
                SerialSettingsType.ParityNone);
    }


    @Override
    protected String getDateTimeDefinition()
    {

        return null;
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        try
        {
            this.decode(dataLine, 0);

            writeDeviceIdentification();

            this.write(new byte[] { 83, 51, 13 });

            dataLine = "";

            this.readChar();

            try
            {
                while (true)
                {
                    dataLine += this.readChar();
                }
            }
            catch (PlugInBaseException ex)
            {
                throw ex;
            }
            finally
            {
                dataLine = dataLine.substring(0, dataLine.lastIndexOf('\r'));
                this.decode(dataLine, 1);
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


    private void decode(String line, int messageType) throws PlugInBaseException
    {
        char[] arr_s = line.toCharArray();

        switch (messageType)
        {
            case 0:
                long serial_str = arr_s[4] << 24 | arr_s[3] << 16 | arr_s[2] << 8 | arr_s[1];
                this.serialNumber = "" + serial_str;
                setDeviceInformation(this.serialNumber);
                break;

            case 1:
                line = line.substring(1);
                this.setGlucoseUnitType(GlucoseUnitType.mg_dL);

                while ((line.length() > 0) && ((arr_s = line.substring(0, 13).toCharArray()).length != 0))
                {
                    long bgResult = arr_s[1] << 8 | arr_s[0];

                    int hour = arr_s[2];
                    int minute = arr_s[3];
                    int day = arr_s[4];
                    int month = arr_s[5];
                    int year = arr_s[6];
                    int status = arr_s[11];

                    if (status < 64)
                    {
                        writeBGData(bgResult, 2000 + year, month, day, hour, minute);
                        this.entryCount++;
                    }

                    line = line.substring(13);
                }
                break;
        }

        addToProgress();

    }

}
