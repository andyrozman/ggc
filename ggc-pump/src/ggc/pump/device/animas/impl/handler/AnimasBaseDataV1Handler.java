package ggc.pump.device.animas.impl.handler;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.comm.AnimasCommProtocolV1;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;

import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;
import ggc.pump.device.animas.impl.converter.AnimasBaseDataV1Converter;
import ggc.pump.device.animas.impl.data.AnimasPumpDeviceData;
import ggc.pump.device.animas.impl.handler.v1.AnimasV1CommandType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.List;

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
 *  Filename:     AnimasBaseDataV1Handler
 *  Description:  Animas Base Data V1 Handler (for Implementation V1)
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasBaseDataV1Handler extends AnimasCommProtocolV1
{

    public static final Log LOG = LogFactory.getLog(AnimasBaseDataV2Handler.class);

    AnimasPumpDeviceData data;
    AnimasBaseDataV1Converter baseDataConverter;

    private boolean dld_tout = false;
    private boolean dld_done = false;
    private boolean debg = true;



    public AnimasBaseDataV1Handler(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader, OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
        initLocal();
    }


    public void initLocal()
    {
        this.data = new AnimasPumpDeviceData(new AnimasPumpDataWriter(this.outputWriter));
        this.setBaseData(data);
        baseDataConverter = new AnimasBaseDataV1Converter(deviceReader, data);
        //this.dataConverter = this.baseDataConverter;
    }


    public List<AnimasTransferType> getSupportedActions()
    {
        return Arrays.asList(AnimasTransferType.DownloadPumpData, //
                AnimasTransferType.DownloadPumpSettings);
    }

    protected void determineMaxProgressForTransferType(AnimasTransferType transferType)
    {
        int maxProgress = 5; // opening is 10

        if (this.data.getTransferType() == AnimasTransferType.DownloadPumpData)
        {
            maxProgress += 768;
        }
        else if (this.data.getTransferType() == AnimasTransferType.DownloadPumpSettings)
        {
            maxProgress += 224;
        }

        this.deviceReader.configureProgressReporter(ProgressType.Static, 100, maxProgress, 0);

    }


    public void checkIfActionAllowed(AnimasTransferType transferType) throws PlugInBaseException
    {
        if (this.deviceType.getImplementationType() != this.getImplementationType())
        {
            throw new PlugInBaseException(PlugInExceptionType.WrongDeviceConfigurationSelected,
                    new Object[] {this.deviceType.getImplementationType(), this.getImplementationType() });
        }

        if (!this.getSupportedActions().contains(transferType))
        {
            LOG.error(String.format("Operation %s not supported for handler '%s'", transferType.name(), this.getClass().getSimpleName()));

            throw new PlugInBaseException(PlugInExceptionType.OperationNotSupportedForThisHandler,
                    new Object[] { transferType.name(), this.getClass().getSimpleName() });
        }

    }





    private int commandRetries = 3;

    private boolean downloadFromDevice(AnimasV1CommandType cmdType) //, int dldtype, int reclen, int nrrec, String errcode, String msg)
            throws PlugInBaseException
    {
        // refactor

        int i = 0;
        int j = 0;
        // int pp = 0;

        initarray();
        PlugInBaseException exception;

        //while (i < 3)
        do
        {
            try
            {
                j = downloadFromDeviceInternal(cmdType); //.getCommandString(), dldtype, reclen, nrrec, errcode, msg);

                if (j == cmdType.getRecordCount())
                {
                    i = 5;
                }
                else
                {
                    i++;
                }
            }
            catch(PlugInBaseException ex)
            {

            }



        } while (i < commandRetries);

        if (j != cmdType.getRecordCount())
        {
            try
            {
                for (i = 1; i <= 3; i++)
                {
                    comWrite(AnimasV1CommandType.EndTestMode.getCommandString());
                }
                try
                {
                    closeConnection();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                freePort();
            }
            catch (Exception e)
            {}

            this.downloadError = "PumpDownolad_Read_Timeout";
            this.downloadStatus = 5;
            // throw new
            // AnimasException(AnimasExceptionType.PumpDownloadTimeout);

            throw new PlugInBaseException(PlugInExceptionType.TimeoutReadingData);


        }
        return true;
    }




    private int downloadFromDeviceInternal(AnimasV1CommandType cmdType) throws PlugInBaseException //String dldstr, int dldtype, int reclen, int nrrec, String errcode, String msg) throws PlugInBaseException
    {
        // int j = 0;
        int i = 0;
        // int iprocessg = 0;
        int tout = 0;
        String temps = "";
        String rtemps = "";
        String CKSM = "";

        // reafactor: Exceptions, this variables
        int dldtype = cmdType.getCommandCode();
        int reclen = cmdType.getRecordLength();
        int nrrec = cmdType.getRecordCount();
        String errcode = cmdType.getErrorCode();
        String msg = cmdType.getDescription();



        // String text = "";
        try
        {
            if (!comWrite(cmdType.getCommandString()))
            {
                return -2;
            }
            AnimasUtils.sleepInMs(1000);
            //setLabelText(this.actionLabel, msg);

            //setProgressBar(this.dldProgBar, (dldtype - 1) * 256 * 2);
            temps = "";

            if (this.debg)
            {
                System.out.println("Downloading " + String.valueOf(cmdType.getCommandCode()) + ": |");
            }
            this.dld_done = false;
            this.dld_tout = false;
            i = 0;
            tout = 0;
            while ((!this.dld_done) && (!this.dld_tout))
            {
                AnimasUtils.sleepInMs(15);

                rtemps = IR1000ReadComm();
                if (this.debg)
                {
                    System.out.println(rtemps);
                }
                temps = temps + rtemps;
                tout += 1;
                if (tout > 300)
                {
                    this.dld_tout = true;
                }
                if ((rtemps.indexOf("#") >= 0) || (rtemps.indexOf(" ") >= 0))
                {
                    i += 1;
                    //incProgressBar(this.dldProgBar);
                    tout = 0;
                }
                if (rtemps.indexOf(")") >= 0)
                {
                    this.dld_done = true;
                }
            }
            //incProgressBar(this.dldProgBar);
            if (this.dld_tout)
            {
                return 0;
            }
            if (this.debg)
            {
                System.out.println("|");
            }
            rtemps = "";
            this.workingTxt = temps;
            while ((rtemps.equals("")) && (!this.workingTxt.equals("")))
            {
                rtemps = newLine();
            }
            if (!rtemps.equals("+"))
            {
                if (this.debg)
                {
                    System.out.println("download did not start with +");
                    System.out.println("[" + rtemps + "]");
                }
                return 1;
            }
            rtemps = "";
            while ((rtemps.equals("")) && (!this.workingTxt.equals("")))
            {
                rtemps = newLine();
            }
            if (!rtemps.equals("("))
            {
                if (this.debg)
                {
                    System.out.println("second line did not start with (");
                    System.out.println("[" + rtemps + "]");
                }
                return 2;
            }
            for (i = 0; i < nrrec; i++)
            {
                rtemps = "";
                while ((rtemps.equals("")) && (!this.workingTxt.equals("")))
                {
                    rtemps = newLine();
                }
                if (this.debg)
                {
                    System.out.println(String.valueOf(i) + "'=|" + this.inputstr[i] + "| L="
                            + String.valueOf(this.inputstr[i].length()));
                }
                if ((rtemps.length() != reclen) || ((rtemps.charAt(0) != '#') && (dldtype != 4)))
                {
                    if (this.debg)
                    {
                        System.out.println("i=" + String.valueOf(i) + '\r' + '\n' + "rtemps=|" + rtemps + "|" + '\r'
                                + '\n' + "temps=|" + temps + "|");
                    }
                    return 3;
                }
                this.inputstr[i] = rtemps;
            }
            CKSM = newLine().substring(0, 2);
            if ((CKSM.length() != 2)
                    || (((CKSM.charAt(0) >= '0') && (CKSM.charAt(0) <= '9')) || ((CKSM.charAt(0) < 'A')
                    || (CKSM.charAt(0) > 'F') || (((CKSM.charAt(1) < '0') || (CKSM.charAt(1) > '9')) && ((CKSM
                    .charAt(1) < 'A') || (CKSM.charAt(1) > 'F'))))))
            {
                if (this.debg)
                {
                    System.out.println("CKSM error: [" + CKSM + "] incorrect");
                }
                return 4;
            }
            if (!checkCRC(CKSM, nrrec, dldtype))
            {
                if (this.debg)
                {
                    System.out.println("CKSM error: [" + CKSM + "] does not match");
                }
                return 5;
            }

            deviceReader.addToProgress(ProgressType.Static, 1);

            //setProgressBar(this.dldProgBar, ((dldtype * 2) - 1) * 256);

            return nrrec;
        }
        catch (Exception e)
        {
            throw new PlugInBaseException("", e);
        }
        //return -1;
    }



    private boolean downloadSettings() throws PlugInBaseException
    {
        if (!prepareDownload(508))
        {
            return false;
        }

        if (!this.fClose)
        {
            if (this.debg)
            {
                System.out.println("Downloading RAM!");
            }
            if (!downloadFromDevice(AnimasV1CommandType.PumpInfo))
            {
                return false;
            }

            baseDataConverter.processRawData(AnimasV1CommandType.PumpInfo, this.inputstr);
        }

        finishDownload(508);

        return true;
    }

    private boolean prepareDownload(int maxDownloadProgress) throws PlugInBaseException
    {
        //this.dldProgBar.setMaximum(508);

        //setProgressBar(this.dldProgBar, 0);
        if (!this.connected)
        {
            this.downloadError = "IR1000_Not_Found";
            this.downloadStatus = 2;

            throw new PlugInBaseException(PlugInExceptionType.DeviceCouldNotBeContacted);
        }

        return true;
    }


    private void finishDownload(int maxDownloadProgress)
    {
        //setProgressBar(this.dldProgBar, maxDownloadProgress); // 2048
        try
        {
            for (int i = 1; i <= 3; i++)
            {
                comWrite(AnimasV1CommandType.EndTestMode.getCommandString());
            }
            closeConnection();
        }
        catch (Exception e)
        {}

        //this.downloadCompleted = true;
    }


    private boolean downloadData() throws PlugInBaseException
    {
        // int j = 0;
        int i = 0;
        // int cport = 0;
        // int sn1 = 0;
        // int sn2 = 0;
        // int tout = 0;
        // String temps = "";
        // String rtemps = "";
        // String CKSM = "";
        // boolean found = false;

        if (!prepareDownload(1604)) // 2048
        {
            return false;
        }


        AnimasV1CommandType[] allowedCommandTypes = { //
                AnimasV1CommandType.DailyTotalsLog, //
                AnimasV1CommandType.AlarmLog, //
                AnimasV1CommandType.BolusLog, //
        } ;

        for(AnimasV1CommandType cmd : allowedCommandTypes)
        {
            LOG.debug("Downloading " + cmd.getDescription());

            downloadFromDevice(cmd);

            baseDataConverter.processRawData(cmd, this.inputstr);
        }


        finishDownload(768);

        return true;
    }


    public void startOperation(AnimasTransferType transferType) throws PlugInBaseException
    {

        data.setTransferType(transferType);

        try
        {
            LOG.debug("Running Animas Base Data Handler (v1)");

            this.checkIfActionAllowed(transferType);

            determineMaxProgressForTransferType(transferType);

            openConnectionIR1000();

            if (this.data.getTransferType() == AnimasTransferType.DownloadPumpData)
            {
                downloadData();
            }
            else if (this.data.getTransferType() == AnimasTransferType.DownloadPumpSettings)
            {
                downloadSettings();
            }

        }
        catch (PlugInBaseException ex)
        {
            if (AnimasUtils.checkIfUserRelevantExceptionIsThrown(ex, true))
            {
                LOG.error("Exception: " + ex, ex);
                throw ex;
            }
        }
        finally
        {
            try
            {
                this.closeConnection();
            }
            catch (Exception ex2)
            {}
        }



//        this.setLabelText(null, "MESSAGE_BEFORE_DOWNLOAD_STARTS 1");
//
//        openConnectionIR1000();
//
//        if (transferType  ==  AnimasTransferType.DownloadPumpData)
//        {
//            if (this.downloadStatus == 0)
//            {
//                //this.settingsMap = new HashMap<String, String>(213);
//                if (!this.fClose)
//                {
//                    //9this.settingsMap.put("PUMP_MODEL", "PumpModel_IR1000");
//                }
//                try
//                {
//                    downloadData();
//                }
//                catch (PlugInBaseException e1)
//                {
//
//                    e1.printStackTrace();
//                }
//                this.fClose = true;
//                while (!this.fClose)
//                {
//                    try
//                    {
//                        Thread.sleep(2000L);
//                    }
//                    catch (Exception e)
//                    {}
//                }
//            }
//            // this.mw.pumpReturnFromDownload();
//
//            return;
//        }
//        else
//        {
//            LOG.error("This operation not supported for models 1000 and 1200.");
//        }
//        // if (this.dldType == 1)
//        // {
//        // // Settings for 1000 ??
//        // if (!this.fClose)
//        // {
//        // // downloadSettings();
//        // }
//        // this.fClose = true;
//        // while (!this.fClose)
//        // {
//        // this.sleepInMs(100L);
//        // }
//        // // this.ps.pumpReturnFromDownload();
//        // }
    }


}
