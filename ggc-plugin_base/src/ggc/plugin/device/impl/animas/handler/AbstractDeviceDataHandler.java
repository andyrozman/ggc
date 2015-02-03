package ggc.plugin.device.impl.animas.handler;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.comm.AnimasCommProtocolV2;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.util.AnimasException;
import ggc.plugin.device.impl.animas.util.AnimasExceptionType;

import java.util.List;

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
 *  Filename:     AbstractDeviceDataHandler
 *  Description:  Abstract Device Data Handler
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AbstractDeviceDataHandler extends AnimasCommProtocolV2
{

    //protected OutputWriter outputWriter;

    public AbstractDeviceDataHandler(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader, OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
        initLocal();
    }

    public void setDebugMode(boolean contextDebug, boolean communicationDebug)
    {
        this.DEBUG = contextDebug;
        this.debugCommunication = communicationDebug;
    }

    public abstract void initLocal();


    public abstract List<AnimasTransferType> getSupportedActions();

    public void checkIfActionAllowed(AnimasTransferType transferType) throws AnimasException
    {
        if (this.deviceType.getImplementationType() != this.getImplementationType())
        {
            throw new AnimasException(AnimasExceptionType.WrongPumpConfigurationSelected);
        }

        if (!this.getSupportedActions().contains(transferType))
        {
            LOG.error(String.format("Operation %s not supported for handler '%s'", this.getClass().getSimpleName()));

            throw new AnimasException(AnimasExceptionType.OperationNotSupportedForThisHandler);
        }

    }


    public abstract void startAction(AnimasTransferType transferType) throws AnimasException;

}
