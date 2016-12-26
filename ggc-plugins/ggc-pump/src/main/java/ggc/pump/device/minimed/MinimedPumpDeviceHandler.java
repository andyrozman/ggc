package ggc.pump.device.minimed;

import java.util.List;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDeviceHandlerInterface;
import ggc.plugin.device.impl.minimed.MinimedDeviceReader;
import ggc.plugin.device.impl.minimed.cfg.MinimedSpecialConfig;
import ggc.plugin.device.impl.minimed.enums.MinimedConverterType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.defs.device.PumpDeviceHandler;
import ggc.pump.device.minimed.data.converter.Minimed511PumpDataConverter;
import ggc.pump.device.minimed.data.converter.Minimed512PumpDataConverter;
import ggc.pump.device.minimed.data.converter.Minimed515PumpDataConverter;
import ggc.pump.device.minimed.data.converter.Minimed523PumpDataConverter;
import ggc.pump.device.minimed.data.decoder.MinimedPumpHistoryDecoder;
import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     MinimedPumpDeviceHandler
 *  Description:  This is class that is called from Pump Plugin.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedPumpDeviceHandler extends PumpDeviceHandler implements MinimedDeviceHandlerInterface
{

    private static MinimedSpecialConfig specialConfigPanel;
    public DataAccessPump dataAccess;


    public MinimedPumpDeviceHandler(DataAccessPump dataAccessPump)
    {
        this.dataAccess = dataAccessPump;
        this.registerConverters();
    }


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.MinimedPumpHandler;
    }


    public void readDeviceData(DeviceDefinition definition, //
            Object connectionParameters, //
            OutputWriter outputWriter) throws PlugInBaseException
    {
        MinimedDeviceReader reader = new MinimedDeviceReader( //
                this, (String) connectionParameters, //
                this.getMinimedDeviceType(definition), //
                outputWriter);
        reader.readData();
    }


    public void readConfiguration(DeviceDefinition definition, //
            Object connectionParameters, //
            OutputWriter outputWriter) throws PlugInBaseException
    {
        MinimedDeviceReader reader = new MinimedDeviceReader( //
                this, (String) connectionParameters, //
                this.getMinimedDeviceType(definition), //
                outputWriter);
        reader.readConfiguration();
    }


    // TODO add Carelink Import
    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }


    public MinimedDeviceType getMinimedDeviceType(DeviceDefinition definition)
    {
        Object dd = getDeviceDefinition(definition).getInternalDefintion();

        return (MinimedDeviceType) dd;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSpecialConfig()
    {
        this.specialConfigKey = "MinimedConfig";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DeviceSpecialConfigPanelAbstract getSpecialConfigPanel(DeviceInstanceWithHandler deviceInstanceWithHandler)
    {
        if (specialConfigPanel == null)
        {
            specialConfigPanel = new MinimedSpecialConfig(DataAccessPump.getInstance(), deviceInstanceWithHandler);
        }

        return specialConfigPanel;
    }


    /**
     * {@inheritDoc}
     */
    public void registerConverters()
    {
        MinimedTargetType targetType = getDeviceTargetType();

        if (!MinimedUtil.isTargetRegistered(targetType))
        {
            MinimedUtil.registerConverter(targetType, MinimedConverterType.Pump511Converter,
                new Minimed511PumpDataConverter(dataAccess));
            MinimedUtil.registerConverter(targetType, MinimedConverterType.Pump512Converter,
                new Minimed512PumpDataConverter(dataAccess));
            MinimedUtil.registerConverter(targetType, MinimedConverterType.Pump515Converter,
                new Minimed515PumpDataConverter(dataAccess));
            MinimedUtil.registerConverter(targetType, MinimedConverterType.Pump523Converter,
                new Minimed523PumpDataConverter(dataAccess));

            MinimedUtil.registerDecoder(MinimedTargetType.Pump, new MinimedPumpHistoryDecoder());
        }
    }


    /**
     * {@inheritDoc}
     */
    public MinimedTargetType getDeviceTargetType()
    {
        return MinimedTargetType.Pump;
    }


    public DataAccessPlugInBase getDataAccessInstance()
    {
        return DataAccessPump.getInstance();
    }

}
