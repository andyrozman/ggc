package ggc.meter.device.menarini.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.GlucoseMeterMarkerDto;
import ggc.meter.defs.GlucoseMeterMarker;
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

public class MenariniGlucoMenGM extends MenariniSerialMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MenariniGlucoMenGM.class);

    private GlucoseUnitType unit;


    public MenariniGlucoMenGM(MeanriniMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    protected void preInitDevice() throws PlugInBaseException
    {

    }


    public SerialSettings getSerialSettings()
    {
        return new SerialSettings(19200, SerialSettingsType.DataBits8, SerialSettingsType.StopBits1,
                SerialSettingsType.ParityNone);
    }


    @Override
    protected String getDateTimeDefinition()
    {
        return null;
    }


    public void readDeviceData() throws PlugInBaseException
    {

        try
        {
            String line = "";
            int mode = 0;

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
                            if (mode == 0)
                            {
                                this.writeMessage("R|");
                                mode = 1;
                            }
                            else if (mode == 1)
                            {
                                this.writeMessage("N|");
                                mode = 2;
                            }
                            else if (mode == 2)
                            {
                                this.writeMessage(
                                    right("000" + Integer.toHexString(numPatient).toUpperCase(), 3) + "|");
                                mode = 0;
                                numPatient++;
                            }
                        }
                        break;

                    case CR:
                        break;

                    case LF:
                        {
                            this.decode(line);

                            line = "";

                            if (!remoteCommand)
                            {
                                this.write(146);
                                remoteCommand = true;
                            }
                        }
                        break;

                    case EOT:
                        {
                            this.write(5);

                            readUntilACKReceived();

                            this.unit = this.getUnit();
                            LOG.debug("Glucose Unit: " + unit);
                        }
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


    private GlucoseUnitType getUnit() throws Exception
    {
        String config = "";

        writeMessage("R|");
        char c;

        readUntilACKReceived();

        writeMessage("C|");

        while ((c = readChar()) != '\n')
        {
            config = config + c;
        }

        config = config.substring(2, 4);

        return checkUnit(config);
    }


    protected boolean decode(String line) throws PlugInBaseException
    {
        // System.out.println("linea->" + line);

        String[] data = line.split("\\|");

        if (data[0].equals("H"))
        {
            String[] subData = data[4].split("\\^");

            serialNumber = subData[2].substring(subData[2].indexOf("-") + 1);

            if (serialNumber.startsWith("SN "))
            {
                serialNumber = serialNumber.substring(3);
            }

            setDeviceInformation(serialNumber);
        }
        else if (data[0].equals("D"))
        {
            String measureLower = data[1].substring(0, 2);
            String measureUpper = data[1].substring(2, 4);

            String minute = data[1].substring(4, 6);
            String hour = data[1].substring(6, 8);
            String day = data[1].substring(8, 10);
            String month = data[1].substring(10, 12);
            String year = data[1].substring(12, 14);
            String flag = data[1].substring(14, 16);

            String dateString = "20" + year + month + day + hour + minute;

            int result = Integer.parseInt(new StringBuilder().append(measureUpper).append(measureLower).toString(), 16);

            ATechDate atechDate = parseATechDate(dateString);

            List<GlucoseMeterMarkerDto> glucoseMarkers = new ArrayList<GlucoseMeterMarkerDto>();

            if (checkPostMeal(flag))
            {
                glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
            }

            if ((checkFlag(flag) == 1) && (!measureUpper.equals("FF")) && (!checkDeleted(flag)))
            {
                writeBGData(result, GlucoseUnitType.mg_dL, atechDate, glucoseMarkers);
            }
        }

        addToProgress();

        return false;
    }


    private int checkFlag(String flag)
    {
        int i = 1;

        String b_flag = Integer.toBinaryString(Integer.parseInt(flag, 16));
        if ((b_flag.length() > 2) && (b_flag.substring(b_flag.length() - 3, b_flag.length() - 2).equals("1")))
        {
            i = 0;
        }
        return i;
    }


    private boolean checkDeleted(String flag)
    {
        String b_flag = Integer.toBinaryString(Integer.parseInt(flag, 16));

        if ((b_flag.length() > 3) && (b_flag.substring(b_flag.length() - 4, b_flag.length() - 3).equals("1")))
        {
            return true;
        }

        return false;
    }


    private boolean checkPostMeal(String flag)
    {
        String b_flag = Integer.toBinaryString(Integer.parseInt(flag, 16));

        if ((b_flag.length() > 4) && (b_flag.substring(b_flag.length() - 5, b_flag.length() - 4).equals("1")))
        {
            return true;
        }

        return false;
    }


    private GlucoseUnitType checkUnit(String flag)
    {
        String b_flag = Integer.toBinaryString(Integer.parseInt(flag, 16));

        if ((b_flag.length() > 2) && (b_flag.substring(b_flag.length() - 3, b_flag.length() - 2).equals("1")))
        {
            return GlucoseUnitType.mmol_L;
        }

        return GlucoseUnitType.mg_dL;
    }
}
