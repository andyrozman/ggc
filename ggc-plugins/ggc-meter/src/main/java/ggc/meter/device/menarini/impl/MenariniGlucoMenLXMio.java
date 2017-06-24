package ggc.meter.device.menarini.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

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

public class MenariniGlucoMenLXMio extends MenariniSerialMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MenariniGlucoMenLXMio.class);


    public MenariniGlucoMenLXMio(MeanriniMeterDataReader dataReader)
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
        return new SerialSettings(9600, SerialSettingsType.DataBits8, SerialSettingsType.StopBits1,
                SerialSettingsType.ParityOdd);
    }


    @Override
    protected String getDateTimeDefinition()
    {
        return "yyyyMMddHHmm";
    }


    public void readDeviceData() throws PlugInBaseException
    {
        try
        {
            this.readChar();
            this.readChar();
            boolean readResult = false;
            String line = "";

            this.communicationHandler.write(162);
            this.communicationHandler.flush();

            char c;
            while (((c = this.readChar()) != ']') || (!readResult))
            {
                switch (c)
                {
                    case ']':
                        readResult = true;

                        this.communicationHandler.write(128);
                        this.communicationHandler.flush();
                        break;

                    case CR:
                        break;

                    case LF:
                        if (line.length() >= 30)
                        {
                            this.decode(line);
                        }
                        else
                        {
                            LOG.debug("Line discarded (length<30): " + line);
                        }
                        line = "";

                        break;

                    default:
                        line = line + c;
                }

            }

            this.readChar();
            this.readChar();
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


    private void decode(String line) throws PlugInBaseException
    {
        String[] data = line.split("\\,");

        if (data.length == 5)
        {
            this.serialNumber = data[3];
            setDeviceInformation(this.serialNumber);
        }
        else if (data.length == 6)
        {
            String result = data[1].replace(',', '.');

            GlucoseUnitType unitType = data[2].toLowerCase().contains("mg") ? GlucoseUnitType.mg_dL
                    : GlucoseUnitType.mmol_L;

            boolean controlEntry = data[0].equals("Ctl");

            ATechDate date = parseATechDate("20" + data[4] + data[5]);

            if (!controlEntry)
            {
                writeBGData(Float.parseFloat(result), unitType, date);
            }
        }

        addToProgress();

    }
}
