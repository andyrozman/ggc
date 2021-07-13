package ggc.cgms.device.minimed;

import java.util.List;

import ggc.cgms.defs.device.CGMSDeviceHandler;
import ggc.cgms.device.minimed.data.converter.Minimed522CGMSDataConverter;
import ggc.cgms.device.minimed.data.converter.Minimed523CGMSDataConverter;
import ggc.cgms.util.DataAccessCGMS;
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
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     MinimedCGMSDeviceHandler
 *  Description:  This is class that is called from CGMS Plugin.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCGMSDeviceHandler extends CGMSDeviceHandler implements MinimedDeviceHandlerInterface
{

    private static MinimedSpecialConfig specialConfigPanel;

    public DataAccessCGMS dataAccess;


    public MinimedCGMSDeviceHandler(DataAccessCGMS dataAccess)
    {
        this.dataAccess = dataAccess;
        this.registerConverters();
    }


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.MinimedCGMSHandler;
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


    public void closeDevice() throws PlugInBaseException
    {
        // not used - close is handled internally
    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }


    public MinimedDeviceType getMinimedDeviceType(DeviceDefinition definition)
    {
        return (MinimedDeviceType) getDeviceDefinition(definition).getInternalDefintion();
    }


    public void registerConverters()
    {
        if (!MedtronicUtil.isTargetRegistered(MinimedTargetType.CGMS))
        {
            MedtronicUtil.registerConverter(MinimedTargetType.CGMS, MinimedConverterType.CGMS522Converter,
                    new Minimed522CGMSDataConverter(dataAccess));
            MedtronicUtil.registerConverter(MinimedTargetType.CGMS, MinimedConverterType.CGMS523Converter,
                    new Minimed523CGMSDataConverter(dataAccess));
        }
    }


    public MinimedTargetType getDeviceTargetType()
    {
        return MinimedTargetType.CGMS;
    }


    public DataAccessPlugInBase getDataAccessInstance()
    {
        return DataAccessCGMS.getInstance();
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
            specialConfigPanel = new MinimedSpecialConfig(DataAccessCGMS.getInstance(), deviceInstanceWithHandler);
        }

        return specialConfigPanel;
    }
}
