package ggc.meter.device.menarini.impl;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

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

public class MenariniGlucoMenPC extends MenariniSerialMeterAbstract
{

    public MenariniGlucoMenPC(MeanriniMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    protected void preInitDevice() throws PlugInBaseException
    {

    }


    @Override
    public SerialSettings getSerialSettings()
    {
        return new SerialSettings(4800, SerialSettingsType.DataBits8, SerialSettingsType.StopBits1,
                SerialSettingsType.ParityEven);
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        String data = "";

        this.communicationHandler.setReceiveTimeout(5000);
        try
        {
            char c;
            while ((c = readChar()) != '\007')
                data += c;
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

        this.decode(data);

    }


    public String getDateTimeDefinition()
    {
        return "yyyyMMddHHmm";
    }


    private void decode(String line) throws PlugInBaseException
    {
        // System.out.println("line->" + line);
        char[] arr_s = line.toCharArray();

        long serial_str = arr_s[5] << 16 | arr_s[4] << 8 | arr_s[3] << 0;
        this.serialNumber = "" + serial_str;

        setDeviceInformation(this.serialNumber);

        ATechDate previousDate = new ATechDate(ATechDateType.DateAndTimeMin, new GregorianCalendar());

        Set<String> entries = new HashSet<String>();

        while ((line.length() > 0) && ((arr_s = line.substring(0, 7).toCharArray()).length != 0))
        {
            String entry = line.substring(0, 7);

            if (entries.contains(entry))
            {
                break;
            }

            entries.add(entry);

            long recall = arr_s[5] << 24 | arr_s[4] << 16 | arr_s[3] << 8 | arr_s[2] << 0;

            int result = (int) (recall & 0x3FF);
            int delete = (int) (recall & 0x400) >> 10;
            int controlEntry = (int) (recall & 0x800) >> 11;
            int month = (int) (recall & 0xF000) >> 12;
            int minute = (int) (recall & 0x3F0000) >> 16;
            int day = (int) (recall & 0x7C00000) >> 22;
            int hour = (int) (recall & 0xF8000000) >> 27;

            line = line.substring(7);

            if ((result == 0) || (delete == 1) || (controlEntry == 1))
            {
                continue;
            }
            else
            {
                int year = previousDate.getYear();

                ATechDate aTechDate = getATechDate(year, month, day, hour, minute);

                if (previousDate.before(aTechDate))
                {
                    aTechDate = getATechDate(year - 1, month, day, hour, minute);
                }

                writeBGData(result, GlucoseUnitType.mg_dL, aTechDate);

                previousDate = aTechDate;
            }

            addToProgress();
        }
    }
}
