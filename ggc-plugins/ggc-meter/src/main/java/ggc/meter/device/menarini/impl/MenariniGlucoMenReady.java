package ggc.meter.device.menarini.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

// Same as Mendor
public class MenariniGlucoMenReady extends MenariniSerialMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MenariniGlucoMenReady.class);


    public MenariniGlucoMenReady(MeanriniMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    protected void preInitDevice() throws PlugInBaseException
    {
        try
        {
            this.communicationHandler.setReceiveTimeout(5000);

            try
            {
                write(new int[] { 128 });
                int c = unsignedByteToInt(readByte());

                if (c == 128)
                {}

                readByte(); // 16
                readByte(); // 32

                // return true;
            }
            catch (Exception ex)
            {
                this.communicationHandler.disconnectDevice();
                throw new PlugInBaseException(PlugInExceptionType.ErrorWithDeviceCommunication,
                        new Object[] { ex.getMessage() }, ex);
            }
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.ErrorWithDeviceCommunication,
                    new Object[] { ex.getMessage() }, ex);
        }

    }


    @Override
    public SerialSettings getSerialSettings()
    {
        return new SerialSettings(9600, SerialSettingsType.DataBits8, SerialSettingsType.StopBits1,
                SerialSettingsType.ParityNone);
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        try
        {
            // Serial Number
            serialNumber = this.readSerial();
            setDeviceInformation(this.serialNumber);

            // Readings count
            this.write(new int[] { 139, 17, 32, 24, 38, 16, 33 });

            String countLowByte = readPartOfNumber();

            this.write(new int[] { 139, 17, 32, 24, 39, 16, 33 });

            String countHighByte = readPartOfNumber();

            int count = Integer.parseInt(countHighByte + countLowByte, 16);
            // System.out.println("readings ->" + count);

            int address = 57856;
            int c = 0;
            String year = "";

            this.setGlucoseUnitType(GlucoseUnitType.mg_dL);

            do
            {
                String hexAddress = Integer.toHexString(address);
                int a1 = Integer.parseInt("1" + hexAddress.substring(0, 1), 16);
                int a2 = Integer.parseInt("2" + hexAddress.substring(1, 2), 16);
                int a3 = Integer.parseInt("1" + hexAddress.substring(2, 3), 16);
                int a4 = Integer.parseInt("2" + hexAddress.substring(3, 4), 16);

                this.write(new int[] { 139, a1, a2, a3, a4, 16, 40 });

                year = readRecord();

                c++;
                address += 8;

            } while ((!year.equals("0255")) && (c < count));

            this.active = false;
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


    @Override
    protected String getDateTimeDefinition()
    {
        return null;
    }


    private String readRecord() throws PlugInBaseException
    {
        String year = readPartOfNumber();
        year = right("20" + Integer.parseInt(year, 16), 4);

        String month = readPartOfNumber();
        String day = readPartOfNumber();
        String hour = readPartOfNumber();
        String minutes = readPartOfNumber();
        readPartOfNumber(); // seconds

        String lowByteResult = readPartOfNumber();
        String highByteResult = readPartOfNumber();

        int bgResult = Integer.parseInt(highByteResult + lowByteResult, 16);

        // System.out.println("Result -> " + bgResult);

        if ((!year.equals("0255")) && (bgResult < 10000))
        {
            writeBGData(bgResult, //
                Integer.parseInt(year), //
                Integer.parseInt(month, 16), //
                Integer.parseInt(day, 16), //
                Integer.parseInt(hour, 16), //
                Integer.parseInt(minutes, 16));

            entryCount += 1;
        }

        addToProgress();

        return year;
    }


    protected String readSerial() throws Exception
    {
        write(new int[] { 139, 17, 32, 19, 36, 16, 40 });

        String lsb1 = readPartOfNumber();
        String msb1 = readPartOfNumber();
        String lsb2 = readPartOfNumber();
        String msb2 = readPartOfNumber();
        String lsb3 = readPartOfNumber();
        String msb3 = readPartOfNumber();
        String lsb4 = readPartOfNumber();
        String msb4 = readPartOfNumber();

        String serial = new String(new char[] { (char) Integer.parseInt(msb1 + lsb1, 16) })
                + right("00" + Integer.parseInt(msb2 + lsb2, 16), 4)
                + right("00" + Integer.parseInt(msb3 + lsb3, 16), 4)
                + right("00" + Integer.parseInt(msb4 + lsb4, 16), 4);

        LOG.debug("Serial Number: " + serial);

        return serial;
    }


    private String readPartOfNumber() throws PlugInBaseException
    {
        readByte();

        return right(Integer.toHexString(unsignedByteToInt(readByte())), 1)
                + right(Integer.toHexString(unsignedByteToInt(readByte())), 1);
    }

}
