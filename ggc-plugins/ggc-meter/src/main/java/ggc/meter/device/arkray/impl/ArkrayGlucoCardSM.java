package ggc.meter.device.arkray.impl;

import java.util.ArrayList;
import java.util.List;

import com.atech.utils.data.ATechDate;

import ggc.meter.data.GlucoseMeterMarkerDto;
import ggc.meter.defs.GlucoseMeterMarker;
import ggc.meter.device.arkray.ArkrayMeterDataReader;
import ggc.meter.device.arkray.ArkrayUtil;
import ggc.plugin.comm.cfg.USBDevice;
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
 *  Filename:     ArkrayGlucoCardSM
 *  Description:  Data/device reader for Arkray GlucoCard SM (USB)
 *
 *  Author: Andy {andy@atech-software.com}
 */
public class ArkrayGlucoCardSM extends ArkrayUSBMeterAbstract
{

    public ArkrayGlucoCardSM(ArkrayMeterDataReader dataReader)
    {
        super(dataReader);
    }


    @Override
    public void readDeviceData() throws PlugInBaseException
    {
        try
        {
            // String line = "";

            byte[] data = new byte[64];
            byte[] message = new byte[64];
            message[0] = 5;

            setReceiveTimeout(500);

            write(message);
            read(data);

            // this.deviceSM.write(message, 64, (byte) 0);
            // this.deviceSM.read(data, 500);

            write("COMMAND");
            read(data);

            // this.deviceSM.write("COMMAND".getBytes(), 64, (byte) 0);
            // this.deviceSM.read(data, 500);

            write("R003|");
            read(data);

            // this.deviceSM.write("R003|".getBytes(), 64, (byte) 0);
            // this.deviceSM.read(data, 500);

            String serial_read = new String(data);
            serial_read = serial_read.substring(0, serial_read.indexOf("|")).trim();

            this.serialNumber = serial_read;

            checkIfSerialNumberWasRead();

            write("R100|");
            read(data);

            // this.deviceSM.write("R100|".getBytes(), 64, (byte) 0);
            // this.deviceSM.read(data, 500);

            String response = new String(data);
            int num = Integer.parseInt(response.substring(0, response.indexOf("|")));

            for (int i = 0; i < num; i++)
            {
                String index = "R102" + right("00" + i, 3) + "|";

                write(index);
                read(data);

                // this.deviceSM.write(index.getBytes(), 64, (byte) 0);
                // this.deviceSM.read(data, 500);
                // String line = new String(data);
                this.decode(new String(data));
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
            // this.deviceSM.close();
        }
    }


    protected void decode(String line) throws PlugInBaseException
    {
        // System.out.println("line->" + line);

        float result = ArkrayUtil.getParsedResult(line.substring(0, 4));
        String date = 20 + line.substring(4, 14);
        String flag = line.substring(14, 16);

        if ((checkIsControlEntry(flag)) || (checkDeleted(flag)))
        {
            addToProgress();
            return;
        }

        List<GlucoseMeterMarkerDto> glucoseMarkers = checkAllFlags(flag);

        GlucoseMeterMarkerDto loHighMarker = checkRange(result);

        if (loHighMarker != null)
        {
            glucoseMarkers.add(loHighMarker);
        }

        ATechDate aTechDate = parseATechDate(date);

        writeBGData(result, glucoseUnitType, aTechDate, glucoseMarkers);
        addToProgress();

    }


    private GlucoseMeterMarkerDto checkRange(float result)
    {
        if (result < 20.0f)
            return GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.LowGlucose);
        else if (result > 600.0f)
            return GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.HighGlucose);
        else
            return null;
    }


    protected List<GlucoseMeterMarkerDto> checkAllFlags(String flag)
    {
        List<GlucoseMeterMarkerDto> markers = new ArrayList<GlucoseMeterMarkerDto>();

        String binaryFlag = Integer.toBinaryString(Integer.parseInt(flag, 16));

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 3))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PreMeal));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 4))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 5))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.Hypoglycemia));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 7))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.BedTime));
        }

        return markers;
    }


    @Override
    protected void createAllowedDevicesList()
    {
        this.allowedDevicesList = new ArrayList<USBDevice>();
        this.allowedDevicesList.add(new USBDevice("GlucoCard SM", 0x1a79, 0x6002));
    }


    @Override
    public void preInitDevice() throws PlugInBaseException
    {
    }

}
