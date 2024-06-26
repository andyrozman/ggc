package ggc.plugin.gui;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;

import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
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

public class DeviceReaderRunner extends Thread implements OutputWriter
{

    private static final Logger LOG = LoggerFactory.getLogger(DeviceReaderRunner.class);

    DeviceInterface m_mi = null;
    boolean special_status = false;

    DeviceConfigEntry configured_device;

    DeviceDisplayDataDialog dialog_data;
    DeviceDisplayConfigDialog dialog_config;

    boolean running = true;
    DataAccessPlugInBase m_da;
    boolean reading_started = false;

    DeviceDataHandler m_ddh;

    String pluginName;


    /**
     * Constructor
     *
     * @param da
     * @param _ddh
     */
    public DeviceReaderRunner(DataAccessPlugInBase da, DeviceDataHandler _ddh)
    {
        this.m_da = da;
        this.m_ddh = _ddh;
        this.configured_device = this.m_ddh.getConfiguredDevice();

        if (this.m_ddh.isDataTransfer())
        {
            this.dialog_data = this.m_ddh.dialog_data;
        }
        else
        {
            this.dialog_config = this.m_ddh.dialog_config;
        }

    }


    public void readDataFromDeviceV1()
    {
        String lg = "";
        try
        {
            readOldData();

            lg = "Creating instance [name=" + this.configured_device.name + ",company="
                    + this.configured_device.device_company + ",device=" + this.configured_device.device_device
                    + ",comm_port=" + this.configured_device.communication_port + "]";
            LOG.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);

            String className = m_da.getManager().getDeviceClassName(this.configured_device.device_company,
                this.configured_device.device_device);

            Class<?> c = Class.forName(className);

            Constructor<?> cnst = c
                    .getDeclaredConstructor(String.class, OutputWriter.class, DataAccessPlugInBase.class);
            this.m_mi = (DeviceInterface) cnst.newInstance(this.configured_device.communication_port_raw, this, m_da);
            this.setDeviceComment(this.m_mi.getDeviceSpecialComment());
            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOADING);

            lg = "Device instance created and initied";
            LOG.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);

            this.special_status = this.m_mi.hasSpecialProgressStatus();

            if (this.m_mi.hasIndeterminateProgressStatus())
            {
                setIndeterminateProgress();
            }

            // check if device online (open succesful)
            if (this.m_ddh.getTransferType() != DeviceDataHandler.TRANSFER_READ_DATA_FILE && //
                    this.m_ddh.getTransferType() != DeviceDataHandler.TRANSFER_READ_CONFIGURATION_FILE && //
                    !this.m_mi.isDeviceCommunicating())
            {
                this.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);

                JOptionPane.showMessageDialog(this.getDialog(),
                    m_da.getI18nControlInstance().getMessage("ERROR_CONTACTING_DEVICE"), m_da.getI18nControlInstance()
                            .getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);

                return;
            }

            if (this.m_ddh.isDataTransfer())
            {
                lg = "Start reading of data";
                LOG.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);

                if (this.m_ddh.getTransferType() == DeviceDataHandler.TRANSFER_READ_DATA)
                {
                    this.m_mi.readDeviceDataFull();
                }
                else
                {
                    this.m_ddh.selected_file_context.setOutputWriter(this);
                    this.m_ddh.selected_file_context.readFile(m_ddh.selected_file);
                }

            }
            else
            {
                lg = "Start reading of configuration";
                LOG.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);

                if (this.m_ddh.getTransferType() == DeviceDataHandler.TRANSFER_READ_CONFIGURATION)
                {
                    this.m_mi.readConfiguration();
                }
                else
                {
                    this.m_ddh.selected_file_context.setOutputWriter(this);
                    this.m_ddh.selected_file_context.readFile(m_ddh.selected_file);
                }
            }

            running = false;

            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);
            this.setSpecialProgress(100);
            this.endOutput();

            lg = "Reading finished";
            LOG.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);

        }
        catch (Exception ex)
        {
            this.setStatus(AbstractOutputWriter.STATUS_READER_ERROR);
            lg = "DeviceReaderRunner:Exception:" + ex;
            LOG.error(lg, ex);
            writeLog(LogEntryType.ERROR, lg, ex);
            running = false;

            if (m_da.checkUnsatisfiedLink(ex))
            {
                I18nControlAbstract ic = this.m_da.getI18nControlInstance();

                String[] dta = m_da.getUnsatisfiedLinkData(ex);

                JOptionPane.showMessageDialog(this.getDialog(),
                    String.format(ic.getMessage("NO_BINARY_PART_FOUND"), dta[0], dta[2], dta[1]),
                    ic.getMessage("ERROR") + ": " + dta[0], JOptionPane.ERROR_MESSAGE, null);
            }
        }
        finally
        {
            if (this.m_mi != null)
            {
                this.m_mi.dispose();
            }
        }

    }


    private void readOldData()
    {
        String lg;
        if (this.m_ddh.isDataTransfer())
        {
            lg = "Trying to reading old data from GGC...";
            LOG.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);

            OldDataReaderAbstract odra = m_da.getOldDataReader();

            if (odra != null)
            {

                odra.setDeviceReadRunner(this);
                m_da.getDeviceDataHandler().setDeviceData(odra.readOldEntries());
                lg = "Reading of old data finished !";
                LOG.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);
            }
            else
            {
                lg = "Reading unsucessful !";
                this.canOldDataReadingBeInitiated(false);
                LOG.warn(lg);
                writeLog(LogEntryType.WARNING, lg);
            }
        }
    }


    public void readDataFromDeviceV2()
    {
        // FIXME

        String lg = "";
        try
        {
            readOldData();

            DeviceInstanceWithHandler di = this.m_ddh.getDeviceInterfaceV2();

            this.setDeviceComment(di.getDeviceSpecialComment());
            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOADING);

            lg = "Device instance (v2) prepared for reading";
            LOG.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);

            this.special_status = (di.getDeviceProgressStatus() == DeviceProgressStatus.Special);

            if (di.getDeviceProgressStatus() == DeviceProgressStatus.Indeterminate)
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

            if (this.m_ddh.isDataTransfer())
            {
                lg = "Start reading of data";
                LOG.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);

                if (this.m_ddh.getTransferType() == DeviceDataHandler.TRANSFER_READ_DATA)
                {
                    di.readDeviceData(this.configured_device.communication_port_raw, this);
                }
                else
                {
                    // FIXME
                    // di.getFileDownloadContext(DownloadSupportType.DownloadDataFile)

                    // fixme
                    this.m_ddh.selected_file_context.setOutputWriter(this);
                    this.m_ddh.selected_file_context.readFile(m_ddh.selected_file);
                }
                lg = "End reading of data";
            }
            else
            {
                lg = "Start reading of configuration";
                LOG.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);

                if (this.m_ddh.getTransferType() == DeviceDataHandler.TRANSFER_READ_CONFIGURATION)
                {
                    di.readConfiguration(this.configured_device.communication_port_raw, this);
                }
                else
                {
                    this.m_ddh.selected_file_context.setOutputWriter(this);
                    this.m_ddh.selected_file_context.readFile(m_ddh.selected_file);
                }
                lg = "End reading of configuration";
            }

            running = false;

            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);
            this.setSpecialProgress(100);
            this.endOutput();

            // lg = "End reading of data/configuration.";
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

            if (m_da.checkUnsatisfiedLink(ex))
            {
                I18nControlAbstract ic = this.m_da.getI18nControlInstance();

                String[] dta = m_da.getUnsatisfiedLinkData(ex);

                JOptionPane.showMessageDialog(this.getDialog(),
                    String.format(ic.getMessage("NO_BINARY_PART_FOUND"), dta[0], dta[2], dta[1]),
                    ic.getMessage("ERROR") + ": " + dta[0], JOptionPane.ERROR_MESSAGE, null);
            }
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
            m_da.sleepMS(2000);

            if (this.m_ddh.getDeviceInterfaceV2() != null)
            {
                readDataFromDeviceV2();
            }
            else if (this.m_ddh.getDeviceInterfaceV1() != null)
            {
                readDataFromDeviceV1();
            }
        }
    }


    /**
     * Get Dialog 
     * 
     * @return
     */
    public JDialog getDialog()
    {
        if (this.m_ddh.isDataTransfer())
            return this.dialog_data;
        else
            return this.dialog_config;
    }


    /**
     * Write Data to OutputWriter
     * 
     * @param data
     */
    public void writeData(OutputWriterData data)
    {
        if (!this.special_status)
        {
            count++;

            float f = count * 1.0f / getOutputUtil().getMaxMemoryRecords() * 100.0f;

            // int i = (int)((count/500) * 100);
            // System.out.println("Progress: " + f + " " + count + " max: " +
            // this.dialog.outputUtil.getMaxMemoryRecords());

            if (this.m_ddh.isDataTransfer())
            {
                this.dialog_data.progress.setValue((int) f);
            }
            else
            {
                // FIXME we need to remove this, we have new method for writing
                // configuration
                this.dialog_config.progress.setValue((int) f);
            }
        }

        getOutputWriter().writeData(data);
    }


    public void writeConfigurationData(OutputWriterConfigData configData)
    {
        if (this.dialog_config == null)
        {
            LOG.warn("We can't write configuration data when no dialogConfig is set.");
        }

        if (!this.special_status)
        {
            count++;

            float f = count * 1.0f / getOutputUtil().getMaxMemoryRecords() * 100.0f;

            // int i = (int)((count/500) * 100);
            // System.out.println("Progress: " + f + " " + count + " max: " +
            // this.dialog.outputUtil.getMaxMemoryRecords());

            this.dialog_config.progress.setValue((int) f);
        }

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
        if (this.m_ddh.isDataTransfer())
            return this.dialog_data.getDeviceIdentification(); // .deviceIdentification;
        else
            return this.dialog_config.getDeviceIdentification(); // .deviceIdentification;
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
     * setBGOutputType
     */
    public void setBGOutputType(int bg_type)
    {
        // getOutputUtil().setOutputBGType(bg_type);
    }


    /** 
     * setDeviceIdentification
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        getOutputWriter().setDeviceIdentification(di);
    }


    /**
     * Set Device Comment
     * 
     * @param com
     */
    public void setDeviceComment(String com)
    {
        if (this.m_ddh.isDataTransfer())
        {
            this.dialog_data.setDeviceComment(com);
        }
        else
        {
            this.dialog_config.setDeviceComment(com);
        }
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


    public void setSpecialNote(int noteType, String note)
    {
        getOutputWriter().setSpecialNote(noteType, note);
    }

}
