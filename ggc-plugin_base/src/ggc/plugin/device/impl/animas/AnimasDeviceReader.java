package ggc.plugin.device.impl.animas;

import ggc.plugin.data.progress.ProgressData;
import ggc.plugin.data.progress.ProgressReportInterface;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;
import ggc.plugin.device.impl.animas.util.AnimasException;
import ggc.plugin.device.impl.animas.util.AnimasExceptionType;
import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     AnimasDeviceReader
 *  Description:  This in main class for reading data from device. Each plugin must extend
 *       this class, to call correct methods.
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasDeviceReader implements ProgressReportInterface
{
    public static final Log LOG = LogFactory.getLog(AnimasDeviceReader.class);

    protected String portName;
    protected ProgressData progressData = new ProgressData();
    protected OutputWriter outputWriter;
    protected AnimasDeviceType animasDevice;
    protected boolean downloadCanceled = false;

    public AnimasDeviceReader(String portName, AnimasDeviceType animasDevice, OutputWriter outputWriter) throws AnimasException
    {
        this.portName = portName;
        this.animasDevice = animasDevice;

        @SuppressWarnings("rawtypes")
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        StringBuilder sb = new StringBuilder();

        boolean deviceFound = false;

        while (ports.hasMoreElements())
        // for(Object cpi : portList)
        {
            CommPortIdentifier comp = (CommPortIdentifier) ports.nextElement();

            if (comp.getName().equals(this.portName))
            {
                deviceFound = true;
            }

            sb.append(comp.getName() + " ");
        }

        LOG.debug(String.format("Serial Ports found: %s, configured port (%s) found: %s", sb.toString(), portName,
            deviceFound));

        if (!deviceFound)
        {
            throw new AnimasException(AnimasExceptionType.DeviceNotFoundOnConfiguredPort,
                    new Object[] { this.portName });
        }

        if (animasDevice.getImplementationType() != AnimasImplementationType.AnimasImplementationV2)
        {
            throw new AnimasException(AnimasExceptionType.UnsupportedDevice);
        }
    }


    public void addToProgress(ProgressType progressType, int progressAdd)
    {
    }

    public void configureProgressReporter(ProgressType baseProgressType, int staticProgressPercentage,
            int staticMaxElements, int dynamicMaxElements)
    {
        this.progressData.configureProgressReporter(baseProgressType, staticProgressPercentage, staticMaxElements,
            dynamicMaxElements);
    }

    public void setDownloadCancel(boolean downloadCanceled)
    {
        this.downloadCanceled = downloadCanceled;
    }

    public boolean isDownloadCanceled()
    {
        if (this.outputWriter != null)
            return this.outputWriter.isReadingStopped() || downloadCanceled;
        else
            return downloadCanceled;
    }

    public void addToProgressAndCheckIfCanceled(ProgressType progressType, int progressAdd) throws AnimasException
    {
        this.progressData.addToProgressAndCheckIfCanceled(progressType, progressAdd);

        // log.debug("Progress: " + this.progressData.calculateProgress());

        if (this.outputWriter != null)
        {
            this.outputWriter.setSpecialProgress(this.progressData.getCurrentProgress());
        }

        if (this.isDownloadCanceled())
        {
            throw new AnimasException(AnimasExceptionType.DownloadCanceledByUser);
        }
    }

}
