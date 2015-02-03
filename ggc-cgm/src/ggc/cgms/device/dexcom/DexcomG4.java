package ggc.cgms.device.dexcom;

import ggc.cgms.device.AbstractSerialCGMS;
import ggc.cgms.device.dexcom.receivers.DexcomDevice;
import ggc.cgms.device.dexcom.receivers.DexcomDeviceReader;
import ggc.cgms.device.dexcom.receivers.data.output.GGCOutputParser;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.manager.CGMSDevicesIds;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for Pump devices)
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
 *  Filename:     FRC_MinimedCarelink
 *  Description:  Minimed Carelink File Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     Dexcom 7  
 *  Description:  Dexcom 7 implementation (just settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DexcomG4 extends AbstractSerialCGMS
{

    /**
     * Constructor 
     */
    public DexcomG4()
    {
        super();
    }

    /**
     * Constructor 
     * 
     * @param communicationParameters 
     * @param writer 
     */
    public DexcomG4(String communicationParameters, OutputWriter writer)
    {
        super(communicationParameters, writer, DataAccessCGMS.getInstance());
    }

    /**
     * Constructor
     * 
     * @param communicationParameters
     * @param writer
     * @param da
     */
    public DexcomG4(String communicationParameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(communicationParameters, writer, da);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public DexcomG4(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    // ************************************************
    // *** Device Identification Methods ***
    // ************************************************


    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "Dexcom G4";
    }


    /**
     * {@inheritDoc}
     */
    public String getIconName()
    {
        return "dx_dexcomG4.jpg";
    }


    /**
     * {@inheritDoc}
     */
    public int getDeviceId()
    {
        return CGMSDevicesIds.CGMS_DEXCOM_G4;
    }


    /**
     * {@inheritDoc}
     */
    public String getComment()
    {
        return "";
    }


    /**
     * {@inheritDoc}
     */
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_DONE;
    }


    /**
     * {@inheritDoc}
     */
    public String getDeviceClassName()
    {
        return this.getClass().getSimpleName();
    }


    /**
     * {@inheritDoc}
     */
    public int getMaxMemoryRecords()
    {
        return 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_CONFIG_FROM_DEVICE | DownloadSupportType.DOWNLOAD_FROM_DEVICE;
        // DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE;
    }


    /**
     * {@inheritDoc}
     */
    public int howManyMonthsOfDataStored()
    {
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readConfiguration() throws PlugInBaseException
    {

        DexcomDeviceReader ddr = null;

        try
        {
            prepareBaseDeviceIdentification();

            ddr = new DexcomDeviceReader(this.connectionParameters, DexcomDevice.Dexcom_G4);

            GGCOutputParser gop = new GGCOutputParser(this.outputWriter, DexcomDevice.Dexcom_G4);
            ddr.setOutputWriter(outputWriter);
            ddr.setDataOutputParser(gop);

            ddr.downloadSettings();
        }
        catch (DexcomException ex)
        {
            throw new PlugInBaseException("Error reading Dexcom G4 device. Exception: " + ex, ex);
        }
        finally
        {
            if (ddr != null)
            {
                ddr.dispose();
            }
        }

    }

    private void prepareBaseDeviceIdentification()
    {
        DeviceIdentification di = this.outputWriter.getDeviceIdentification();
        // di.is_file_import = true;
        // di.fi_file_name = new File(filename).getName();
        di.company = this.dataAccess.getSelectedDeviceInstance().getDeviceCompany().getName();
        di.device_selected = this.dataAccess.getSelectedDeviceInstance().getName();
        // di.device_serial_number = "";

        this.outputWriter.setDeviceIdentification(di);
    }

    /**
     * {@inheritDoc}
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        DexcomDeviceReader ddr = null;
        try
        {
            prepareBaseDeviceIdentification();

            ddr = new DexcomDeviceReader(this.connectionParameters, DexcomDevice.Dexcom_G4);

            GGCOutputParser gop = new GGCOutputParser(this.outputWriter, DexcomDevice.Dexcom_G4);
            ddr.setOutputWriter(outputWriter);
            ddr.setDataOutputParser(gop);

            ddr.downloadData();
        }
        catch (DexcomException ex)
        {
            throw new PlugInBaseException("Error reading Dexcom G4 device. Exception: " + ex, ex);
        }
        finally
        {
            if (ddr != null)
            {
                ddr.dispose();
            }

            this.outputWriter.endOutput();
        }

    }

    /**
     * hasIndeterminateProgressStatus - if status can't be determined then JProgressBar will go from 
     *     left to right side, without displaying progress.
     * @return
     */
    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }

    /**
     * getConnectionProtocol - returns id of connection protocol
     *
     * @return id of connection protocol
     */
    @Override
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE;
    }

    public String getInstructions()
    {
        // FIXME
        return "DEXCOM_INSTRUCTIONS_DL_SERIAL_USB";
    }

    @Override
    public boolean isDeviceCommunicating()
    {
        return true;
    }

}
