package ggc.plugin.device.impl.minimed.comm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.atech.utils.data.BitUtils;

import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedCommandPacket;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
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
 *  Filename:     MinimedCommunicationAbstract
 *  Description:  Abstract Communication class
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class MinimedCommunicationAbstract implements MinimedCommunicationInterface
{

    private static Logger LOG = LoggerFactory.getLogger(MinimedCommunicationAbstract.class);

    protected BitUtils bitUtils = MedtronicUtil.getBitUtils();
    protected DataAccessPlugInBase dataAccess;
    protected I18nControlAbstract i18nControl;
    protected MinimedDataHandler dataHandler;

    protected boolean lowLevelDebug = true;
    protected boolean debug = true;

    protected int[] serialBCD;

    protected String time;


    /**
     * Constructor
     *
     * @param minimedDataHandler data Handler
     */
    public MinimedCommunicationAbstract(DataAccessPlugInBase dataAccess, MinimedDataHandler minimedDataHandler)
    {
        this.dataAccess = dataAccess;
        this.i18nControl = dataAccess.getI18nControlInstance();
        this.dataHandler = minimedDataHandler;
        preInitInterface();
    }


    /**
     * Pre-init interface. Here goes interface implementations specific initalization.
     */
    public abstract void preInitInterface();


    /**
     * Read data from Interface (this is main method, that calls all relevant commands
     * and after that converts them into GGC readable data).
     *
     * @throws PlugInBaseException
     */
    public void readDataFromInterface() throws PlugInBaseException
    {
        // read all data commands for this specific device (512) and this
        // specific mode (pump, cgms)

        // with all commands (there should be 1 (pump) or perhaps two or three
        // (in cgms))

        this.time = "" + ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDateType.DateAndTimeSec);

        List<MinimedCommandType> commands = MinimedCommandType.getCommands(this.dataHandler.getDeviceType(),
            this.dataHandler.getDeviceTargetType() == MinimedTargetType.CGMS ? MinimedTargetType.CGMSData
                    : MinimedTargetType.PumpData);

        for (MinimedCommandType minimedCommandType : commands)
        {
            if (lowLevelDebug)
                LOG.debug("Getting Data for command: {}", minimedCommandType.name());

            executeHistoryCommandWithRetry(minimedCommandType);
        }
    }


    /**
     * Read configuration from Interface (this is main method, that calls all relevant commands
     * and after that converts them into GGC readable data).
     * 
     * @throws PlugInBaseException
     */
    public void readConfigurationFromInterface() throws PlugInBaseException
    {
        // read all settings commands for this specific device (512) and this
        // specific mode (pump, cgms)

        this.time = "" + ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDateType.DateAndTimeSec);

        List<MinimedCommandType> commands = MinimedCommandType.getCommands(this.dataHandler.getDeviceType(),
            this.dataHandler.getDeviceTargetType() == MinimedTargetType.CGMS ? MinimedTargetType.CGMSConfiguration
                    : MinimedTargetType.PumpConfiguration);

        for (MinimedCommandType minimedCommandType : commands)
        {
            MedtronicUtil.getOutputWriter().writeLog(LogEntryType.DEBUG,
                "Downloading Configuration - " + minimedCommandType.name());

            if (lowLevelDebug)
                LOG.debug("Getting Configuration for command: {}", minimedCommandType.name());

            MinimedCommandReply minimedCommandReply = executeCommandWithRetry(minimedCommandType);
            this.dataHandler.convertDeviceReply(minimedCommandReply);

            dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);
        }
    }


    /**
     * This is for execution of History commands (all with subpages). After execution each command
     * you need to call decoder for data.
     *
     * @param commandType commandType instance
     * @throws PlugInBaseException
     */
    protected abstract void executeHistoryCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException;


    /**
     * Create command packet with MinimedCommandType.
     * 
     * @param commandType commandType instance
     * @return MinimedCommandPacket instance
     */
    public MinimedCommandPacket createCommandPacket(MinimedCommandType commandType)
    {
        return new MinimedCommandPacket(commandType);
    }


    protected void writeToFile(MinimedCommandPacket commandPacket, MinimedDataPage dataPage)
    {
        String fileName = MedtronicUtil.getSerialNumber() + "_" + this.time + "_" + //
                commandPacket.commandType.name() + "_" + //
                commandPacket.commandParameters[0] + ".bin";

        writeToFile(fileName, dataPage.rawDataPage);
    }


    protected void writeToFile(String fileName, byte[] data)
    {
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream( //
                    new FileOutputStream("../data/log/minimed/" + fileName));

            outputStream.write(data);

            outputStream.flush();
            outputStream.close();
        }
        catch (Exception ex)
        {
            LOG.debug("Problem writing to file: {}, Ex.: {}", fileName, ex.getMessage(), ex);
        }

    }


    private void checkIfDirectoryExists()
    {
        File f = new File("../data/log/minimed");
        if (!f.exists())
        {
            f.mkdirs();
        }
    }


    protected void sleepMs(long timeout)
    {
        if (lowLevelDebug)
            LOG.debug("Sleeping for {} ms.", timeout);
        MedtronicUtil.sleepMs(timeout);
    }

}
