package ggc.connect.data.retrieval;

import java.util.List;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.defs.ConnectHandler;
import ggc.connect.gui.ConnectDisplayConfigDialog;
import ggc.connect.gui.ConnectDisplayDataDialog;
import ggc.connect.util.DataAccessConnect;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.*;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.plugin.util.LogEntryType;

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
 *  Filename:     DeviceReaderRunner
 *  Description:  This is separate thread class to get current data from database in order to 
 *                compare it later.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// Try to assess possibility of super-classing

public class ConnectDataRetriever extends Thread implements OutputWriter
{

    private static final Logger LOG = LoggerFactory.getLogger(ConnectDataRetriever.class);

    DataAccessPlugInBase dataAccess;

    ConnectDisplayDataDialog dialogData;
    ConnectDisplayConfigDialog dialogConfig;
    ConnectHandlerParameters parameters;
    ConnectHandler connectHandler;

    boolean running = true;
    boolean special_status = false;

    // DeviceDataHandler deviceDataHandler;

    String pluginName;

    boolean dataTransfer = false;


    /**
     * Constructor
     */
    public ConnectDataRetriever(ConnectDisplayDataDialog dialog, ConnectHandlerParameters parameters,
            ConnectHandler connectHandler)
    {

        this.dataAccess = DataAccessConnect.getInstance();
        // this.configured_device = this.deviceDataHandler.getConfiguredDevice();

        dataTransfer = true;
        this.parameters = parameters;
        this.connectHandler = connectHandler;

        this.dialogData = dialog;

    }


    /**
     * Constructor
     */
    public ConnectDataRetriever(ConnectDisplayConfigDialog dialog, ConnectHandlerParameters parameters,
            ConnectHandler connectHandler)
    {
        // super(da, _ddh);

        this.dataAccess = DataAccessConnect.getInstance();
        // this.deviceDataHandler = _ddh;
        // this.configured_device = this.deviceDataHandler.getConfiguredDevice();
        this.parameters = parameters;
        this.connectHandler = connectHandler;

        this.dialogConfig = dialog;
    }


    public void readData()
    {
        // FIXME

        String lg = "";
        try
        {
            // readOldData();

            // DeviceInstanceWithHandler di = this.deviceDataHandler.getDeviceInterfaceV2();

            // this.setDeviceComment(di.getDeviceSpecialComment());
            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOADING);

            lg = "Device instance (v2) prepared for reading";
            LOG.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);

            this.special_status = false; // (di.getDeviceProgressStatus() ==
                                         // DeviceProgressStatus.Special);

            // if (di.getDeviceProgressStatus() == DeviceProgressStatus.Indeterminate)
            {
                setIndeterminateProgress();
            }

            // // check if device online (open succesful)
            // if (this.deviceDataHandler.getTransferType() !=
            // DeviceDataHandler.TRANSFER_READ_DATA_FILE
            // && !this.m_mi.isDeviceCommunicating())
            // {
            // this.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
            //
            // JOptionPane.showMessageDialog(this.getDialog(),
            // dataAccess.getI18nControlInstance().getMessage("ERROR_CONTACTING_DEVICE"),
            // dataAccess
            // .getI18nControlInstance().getMessage("ERROR"),
            // JOptionPane.ERROR_MESSAGE);
            //
            // return;
            // }

            // System.out.println("dataTransfer: " + this.deviceDataHandler.isDataTransfer());
            // System.out.println("TransferType: " + this.deviceDataHandler.getTransferType());
            // System.out.println("dataTransfer: " +
            // this.deviceDataHandler.isDataTransfer());

            if (dataTransfer)
            {
                lg = "Start reading of data";
                LOG.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);
                connectHandler.downloadData(parameters, this.dialogData);
            }
            else
            {
                lg = "Start reading of configuration";
                LOG.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);
                connectHandler.downloadData(parameters, this.dialogConfig);
            }

            running = false;

            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);
            this.setSpecialProgress(100);
            this.endOutput();

            lg = "Reading finished";
            LOG.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);

        }
        catch (PlugInBaseException ex)
        {
            this.setStatus(AbstractOutputWriter.STATUS_READER_ERROR);

            if (ex.getExceptionType() != PlugInExceptionType.DownloadCanceledByUser)
            {
                lg = "DeviceReaderRunner:Exception:" + ex;
                LOG.error(lg, ex);
                writeLog(LogEntryType.ERROR, lg, ex);
            }

            running = false;
        }
        catch (Exception ex)
        {
            this.setStatus(AbstractOutputWriter.STATUS_READER_ERROR);
            lg = "DeviceReaderRunner:Exception:" + ex;

            LOG.error(lg, ex);
            writeLog(LogEntryType.ERROR, lg, ex);
            running = false;
        }
    }


    /** 
     * Thread running method
     */
    @Override
    public void run()
    {
        while (running)
        {
            dataAccess.sleepMS(2000);

            readData();

        }
    }


    /**
     * Get Dialog 
     * 
     * @return
     */
    public JDialog getDialog()
    {
        if (dataTransfer)
            return this.dialogData;
        else
            return this.dialogConfig;
    }


    /**
     * Write Data to OutputWriter
     * 
     * @param data
     */
    public void writeData(OutputWriterData data)
    {
        // if (!this.special_status)
        // {
        // count++;
        //
        // float f = count * 1.0f / getOutputUtil().getMaxMemoryRecords() * 100.0f;
        //
        // // int i = (int)((count/500) * 100);
        // // System.out.println("Progress: " + f + " " + count + " max: " +
        // // this.dialog.outputUtil.getMaxMemoryRecords());
        //
        // if (this.deviceDataHandler.isDataTransfer())
        // {
        // this.dialogData.progress.setValue((int) f);
        // }
        // else
        // {
        // // FIXME we need to remove this, we have new method for writing
        // // configuration
        // this.dialogConfig.progress.setValue((int) f);
        // }
        // }

        getOutputWriter().writeData(data);
    }


    public void writeConfigurationData(OutputWriterConfigData configData)
    {
        if (this.dialogConfig == null)
        {
            LOG.warn("We can't write configuration data when no dialogConfig is set.");
        }

        // if (!this.special_status)
        // {
        // count++;
        //
        // float f = count * 1.0f / getOutputUtil().getMaxMemoryRecords() * 100.0f;
        //
        // // int i = (int)((count/500) * 100);
        // // System.out.println("Progress: " + f + " " + count + " max: " +
        // // this.dialog.outputUtil.getMaxMemoryRecords());
        //
        // this.dialogConfig.progress.setValue((int) f);
        // }

        getOutputWriter().writeConfigurationData(configData);

    }


    public void setPluginName(String pluginName)
    {
        this.pluginName = pluginName;
    }


    public String getPluginName()
    {
        return pluginName;
    }


    /**
     * Write LOG entry
     * 
     * @param entryType
     * @param message
     */
    public void writeLog(LogEntryType entryType, String message)
    {
        getOutputWriter().writeLog(entryType, message);
    }


    /**
     * Write LOG entry
     * 
     * @param entryType
     * @param message
     * @param ex
     */
    public void writeLog(LogEntryType entryType, String message, Exception ex)
    {
        getOutputWriter().writeLog(entryType, message, ex);
    }


    /** 
     * endOutput
     */
    public void endOutput()
    {
        getOutputWriter().endOutput();
    }


    /** 
     * getDeviceIdentification
     */
    public DeviceIdentification getDeviceIdentification()
    {
        // FIXME
        if (dataTransfer)
            return this.dialogData.getDeviceIdentification(); // .deviceIdentification;
        else
            return this.dialogConfig.getDeviceIdentification(); // .deviceIdentification;
    }


    /** 
     * getOutputUtil
     */
    public OutputUtil getOutputUtil()
    {
        return getOutputWriter().getOutputUtil();
    }


    /** 
     * interruptCommunication
     */
    public void interruptCommunication()
    {
    }


    /** 
     * setDeviceIdentification
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        getOutputWriter().setDeviceIdentification(di);
    }

    int count = 0;


    /**
     * If we have special status progress defined, by device, we need to set progress, by ourselves, this is 
     * done with this method.
     * @param value
     */
    public void setSpecialProgress(int value)
    {
        // System.out.println("Runner: Special progres: " + value);
        getOutputWriter().setSpecialProgress(value);
    }


    /** 
     * Set Sub Status
     */
    public void setSubStatus(String sub_status)
    {
        // System.out.println("Runner: Sub Status: " + subStatus);
        getOutputWriter().setSubStatus(sub_status);
    }


    /** 
     * Get Sub Status
     */
    public String getSubStatus()
    {
        return getOutputWriter().getSubStatus();
    }


    /**
     * User can stop readings from his side (if supported)
     */
    public void setReadingStop()
    {
        getOutputWriter().setReadingStop();
    }


    /**
     * This should be queried by device implementation, to see if it must stop reading
     */
    public boolean isReadingStopped()
    {
        return getOutputWriter().isReadingStopped();
    }


    /**
     * This is status of device and also of GUI that is reading device (if we have one)
     * This is to set that status to see where we are. Allowed statuses are: 1-Ready, 2-Downloading,
     * 3-Stopped by device, 4-Stoped by user,5-Download finished,...
     */
    public void setStatus(int status)
    {
        getOutputWriter().setStatus(status);
    }


    /** 
     * Get Status
     */
    public int getStatus()
    {
        return getOutputWriter().getStatus();
    }


    /** 
     * writeDeviceIdentification
     */
    public void writeDeviceIdentification()
    {
        getOutputWriter().writeDeviceIdentification();
    }


    /** 
     * writeHeader
     */
    public void writeHeader()
    {
    }


    public void setBGOutputType(int bg_type)
    {
    }


    /** 
     * writeRawData
     * 
     * @param input 
     * @param is_bg_data 
     */
    public void writeRawData(String input, boolean is_bg_data)
    {
    }


    /**
     * Set old data reading progress
     * 
     * @param value
     */
    public void setOldDataReadingProgress(int value)
    {
        getOutputWriter().setOldDataReadingProgress(value);
    }


    /**
     * Can old data reading be initiated (if module in current running mode supports this, this is
     * intended mostly for usage outside GGC)
     * 
     * @param value
     */
    public void canOldDataReadingBeInitiated(boolean value)
    {
        getOutputWriter().canOldDataReadingBeInitiated(value);
    }

    String device_source;


    /**
     * Set Device Source
     * 
     * @param dev
     */
    public void setDeviceSource(String dev)
    {
        this.device_source = dev;
    }


    /**
     * Set Device Source
     * 
     * @return 
     */
    public String getDeviceSource()
    {
        return this.device_source;
    }


    /**
     * Get OutputWriter
     * 
     * @return
     */
    public OutputWriter getOutputWriter()
    {
        return (OutputWriter) getDialog();
    }


    public void setIndeterminateProgress()
    {
        getOutputWriter().setIndeterminateProgress();
    }


    public void addErrorMessage(ErrorMessageDto msg)
    {
        getOutputWriter().addErrorMessage(msg);
    }


    public int getErrorMessageCount()
    {
        return getOutputWriter().getErrorMessageCount();
    }


    public List<ErrorMessageDto> getErrorMessages()
    {
        return getOutputWriter().getErrorMessages();
    }

    public void setSpecialNote(int noteType, String note) {
        getOutputWriter().setSpecialNote(noteType, note);
    }

}
