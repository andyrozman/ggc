package ggc.plugin.protocol.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressData;
import ggc.plugin.data.progress.ProgressReportInterface;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     AbstractDeviceReader
 *  Description:  This is abstract reader. It makes additional check that selected
 *        device is present.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AbstractDeviceReader implements ProgressReportInterface
{

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDeviceReader.class);

    protected ProgressData progressData;
    protected OutputWriter outputWriter;
    protected boolean downloadCanceled = false;


    public AbstractDeviceReader(OutputWriter outputWriter, boolean init) throws PlugInBaseException
    {
        this.outputWriter = outputWriter;
        this.progressData = new ProgressData();

        if (init)
        {
            doInit();
        }
    }


    public void doInit() throws PlugInBaseException
    {
        customInitAndChecks();
        configureProgressReporter();
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


    public ProgressData getProgressDataInstance()
    {
        return this.progressData;
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


    public void addToProgressAndCheckIfCanceled(ProgressType progressType, int progressAdd) throws PlugInBaseException
    {
        this.progressData.addToProgressAndCheckIfCanceled(progressType, progressAdd);

        // LOG.debug("Progress: " + this.progressData.calculateProgress());

        if (this.outputWriter != null)
        {
            this.outputWriter.setSpecialProgress(this.progressData.getCurrentProgress());
        }

        if (this.isDownloadCanceled())
        {
            throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByUser);
        }
    }


    /**
     * Read data
     * 
     * @throws PlugInBaseException
     */
    public abstract void readData() throws PlugInBaseException;


    /**
     * Read configuration
     * 
     * @throws PlugInBaseException
     */
    public abstract void readConfiguration() throws PlugInBaseException;


    /**
     * Close device
     * 
     * @throws PlugInBaseException
     */
    public abstract void closeDevice() throws PlugInBaseException;


    /**
     * This are custom checks if everything there is (or everything set is). When not exception must be thrown.
     *
     * @throws ggc.plugin.device.PlugInBaseException
     */
    public abstract void customInitAndChecks() throws PlugInBaseException;


    /**
     * Here we configure progress reporter. We can either call method configureProgressReporter() or
     * we can directly change progressData instance.
     */
    public abstract void configureProgressReporter();

}
