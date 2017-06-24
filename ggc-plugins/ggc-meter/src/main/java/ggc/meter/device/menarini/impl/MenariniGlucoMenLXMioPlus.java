package ggc.meter.device.menarini.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.GlucoseMeterMarkerDto;
import ggc.meter.defs.GlucoseMeterMarker;
import ggc.meter.defs.device.MeterDeviceDefinition;
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
 *  Filename:     MenariniGlucoMenLXMioPlus
 *  Description:  Data/device reader for Menarini (GlucoMen) LX Mio+
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MenariniGlucoMenLXMioPlus extends MenariniSerialMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MenariniGlucoMenLXMioPlus.class);

    boolean readGlucose = false;
    boolean readKetones = false;


    public MenariniGlucoMenLXMioPlus(MeanriniMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    protected String getDateTimeDefinition()
    {
        return "yyyyMMddHHmm";
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
    public void readDeviceData() throws PlugInBaseException
    {
        try
        {
            this.readChar();
            this.readChar();
            String line = "";

            this.readGlucose = false;
            this.readKetones = false;

            this.communicationHandler.write(162);
            this.communicationHandler.flush();
            boolean exit = false;

            while (!exit)
            {
                char c = this.readChar();
                switch (c)
                {
                    case ']':
                        this.readChar();
                        this.readChar();

                        if (!this.readGlucose)
                        {
                            this.communicationHandler.write(128);
                            this.communicationHandler.flush();
                            this.readGlucose = true;
                        }
                        else if ((!this.readKetones) && (deviceSupportsKetones()))
                        {
                            this.communicationHandler.write(168);
                            this.communicationHandler.flush();
                            this.readKetones = true;
                        }
                        else
                        {
                            exit = true;
                        }
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
                            LOG.debug("Line discarded (length < 30): " + line);
                        }
                        line = "";

                        break;
                    default:
                        line = line + c;
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


    private void decode(String line) throws PlugInBaseException
    {
        String[] data = line.split("\\,");

        if (data.length == 5)
        {
            this.serialNumber = data[3].trim();
            setDeviceInformation(this.serialNumber);
        }
        else if (data.length == 6)
        {
            boolean ignore = false;
            String result = data[1].replace(',', '.');

            GlucoseUnitType unitType = data[2].toLowerCase().contains("mg") ? GlucoseUnitType.mg_dL
                    : GlucoseUnitType.mmol_L;

            boolean controlEntry = data[0].equals("Ctl");

            ATechDate date = parseATechDate("20" + data[4] + data[5]);

            List<GlucoseMeterMarkerDto> glucoseMarkers = new ArrayList<GlucoseMeterMarkerDto>();

            if (!isAreoDevice())
            {
                if (data[3].endsWith("4"))
                    glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
                else if (data[3].endsWith("3"))
                    glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PreMeal));
                else if (data[3].endsWith("5"))
                    glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.Activity));
                else if (data[3].endsWith("1"))
                    ignore = true;
            }
            else
            {
                String mask = right("0000" + Integer.toBinaryString(Integer.parseInt(data[3])), 4);

                if (mask.subSequence(0, 1).equals("1")) // activity
                {
                    glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.Activity));
                }

                if (mask.subSequence(1, 2).equals("1")) // post meal
                {
                    glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
                }

                if (mask.subSequence(2, 3).equals("1")) // pre meal
                {
                    glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PreMeal));
                }

                // e.check = mask.subSequence(3, 4).equals("1");
            }

            if ((!ignore) && (!controlEntry))
            {
                if (!this.readKetones)
                {
                    writeBGData(Float.parseFloat(result), unitType, date, glucoseMarkers);
                }
                else
                {
                    writeUrineData(Float.parseFloat(result), unitType, date);
                }

            }
        }

        addToProgress();

    }


    public boolean isAreoDevice()
    {
        return ((this.deviceDefinition == MeterDeviceDefinition.MenariniGlucoMenAreo)
                || (this.deviceDefinition == MeterDeviceDefinition.MenariniGlucoMenAreo2k));

    }


    private boolean deviceSupportsKetones()
    {
        return ((this.deviceDefinition == MeterDeviceDefinition.MenariniGlucoFixMioPlus)
                || (this.deviceDefinition == MeterDeviceDefinition.MenariniGlucoMenAreo2k));
    }

}
