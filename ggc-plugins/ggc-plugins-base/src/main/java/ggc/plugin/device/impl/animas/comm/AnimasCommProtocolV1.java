package ggc.plugin.device.impl.animas.comm;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;

import java.io.IOException;


import javax.swing.JLabel;
import javax.swing.JProgressBar;

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
 *  Filename:     AnimasCommProtocolV1
 *  Description:  Class for communication with Animas 1000.
 *       THIS CLASS IS NOT IMPLEMENTED YET, AND IT MIGHT NEVER BE.
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasCommProtocolV1 extends AnimasCommProtocolAbstract
{
    // supports 1000, 1200, 1200p

    private boolean debg = true;
    protected String[] inputstr = new String[256];

    private String p_end_test_mode = "t";
    private String p_start_test_mode = "[M0 00 00 00 00]";

    public boolean timer3Enabled = false;
    public boolean fClose = false;
    public boolean connected = false;
    public boolean cancelDownload = false;
    protected String workingTxt = "";

    public int connectionAddr;
    public boolean connectionState;
    public boolean ctlMode;

    public int disconnectTime;
    public int downloadStatus;
    public int fcSum1;
    public int fcSum2;
    public int foundSerialNumber;
    public String[] foundSerialNumberStr;
    public int pingTime;
    public String pumpModel;
    public int readBufferLength;
    public char[] sendBuffer;
    protected String downloadError;


    public AnimasCommProtocolV1(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader, OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
//        this.timeDelta = 0;
    }

    protected void initarray()
    {
        for (int i = 0; i < 256; i++)
        {
            this.inputstr[i] = "";
        }
    }

    protected boolean checkCRC(String txt, int nr, int rl)
    {
        int i = 0;
        int j = 0;
        int cksm = 0;
        int compcksm = 0;
        int compcksm1 = 0;
        // String itxt = "";
        boolean crc_error = false;
        if (rl != 4)
        {
            for (i = 0; i < nr; i++)
            {
                for (j = 0; j < 9; j++)
                {
                    try
                    {
                        compcksm += Integer.parseInt(
                            "" + this.inputstr[i].charAt((j * 2) + 1) + this.inputstr[i].charAt((j * 2) + 2), 16);
                    }
                    catch (Exception e)
                    {
                        crc_error = true;
                    }
                    if (compcksm >= 255)
                    {
                        compcksm -= 255;
                    }
                    compcksm1 += compcksm;
                    if (compcksm1 >= 255)
                    {
                        compcksm1 -= 255;
                    }
                }
            }
        }
        for (i = 0; i < nr; i++)
        {
            for (j = 1; j < 4; j++)
            {
                try
                {
                    compcksm += Integer.parseInt(
                        "" + this.inputstr[i].charAt((j * 2) + 1) + this.inputstr[i].charAt((j * 2) + 2), 16);
                }
                catch (Exception e)
                {
                    crc_error = true;
                }
                if (compcksm >= 255)
                {
                    compcksm -= 255;
                }
                compcksm1 += compcksm;
                if (compcksm1 >= 255)
                {
                    compcksm1 -= 255;
                }
            }
        }
        try
        {
            cksm = Integer.parseInt(txt, 16);
        }
        catch (Exception e)
        {
            crc_error = true;
        }
        if (this.debg)
        {
            System.out.println("[" + txt + "]");
            System.out.println("compcksm1=" + String.valueOf(compcksm1));
            System.out.println("compcksm=" + String.valueOf(compcksm));
            System.out.println("cksm=" + String.valueOf(cksm));
        }
        if ((cksm == compcksm1) && (!crc_error))
        {
            return true;
        }
        return false;
    }

    protected String newLine()
    {
        int i = this.workingTxt.indexOf("\r\n");
        if (this.debg)
        {
            System.out.println("Pos of #13#10 is :" + String.valueOf(i));
        }
        String ends;
        if ((i == -1) || (i == (this.workingTxt.length() - 2)))
        {
            ends = this.workingTxt;
            this.workingTxt = "";
        }
        else
        {
            ends = this.workingTxt.substring(0, i + 2);
            this.workingTxt = this.workingTxt.substring(i + 2, this.workingTxt.length());
        }
        return ends.trim();
    }

    protected boolean comWrite(String txt)
    {
        int i = 0;
        try
        {
            sendCharacterToDevice('\r');
        }
        catch (Exception e)
        {
            return false;
        }
        AnimasUtils.sleepInMs(40);
        try
        {
            sendCharacterToDevice('\n');
        }
        catch (Exception e)
        {
            return false;
        }
        AnimasUtils.sleepInMs(40);
        for (i = 0; i < txt.length(); i++)
        {
            try
            {
                sendCharacterToDevice(txt.charAt(i));
            }
            catch (Exception e)
            {
                return false;
            }
            AnimasUtils.sleepInMs(40);
        }
        return true;
    }


    protected void setLabelText(JLabel actionLabel, String msg)
    {
        System.out.println("Label: " + msg);
    }



    public void clearSendBuf(int j)
    {
        for (int i = 0; i < j; i++)
        {
            this.sendBuffer[i] = 'ÿ';
        }
    }

    public void setProgressBar(JProgressBar pg, int i)
    {
        if (pg != null)
        {
            pg.setValue(i);
            pg.repaint();
        }
    }

//    public void incProgressBar(JProgressBar pg)
//    {
//        if (pg != null)
//        {
//            if (pg.getValue() == pg.getMaximum())
//            {
//                pg.setValue(0);
//            }
//            pg.setValue(pg.getValue() + 1);
//            pg.repaint();
//        }
//    }


    public void freePort()
    {
        try
        {
            this.disconnectDevice();
            // this.myComm.close();
        }
        catch (Exception e)
        {}
    }

    public void cancelDownload()
    {
        this.timer3Enabled = false;

        this.fClose = true;
        try
        {
            closeConnection();
        }
        catch (Exception e)
        {}
        freePort();

        this.cancelDownload = true;
    }

    public void closeConnection()
    {
        if (this.fClose)
        {
            for (int i = 0; i < 100; i++)
            {
                AnimasUtils.sleepInMs(5);
            }

            this.timer3Enabled = false;

            for (int i = 0; i < 200; i++)
            {
                AnimasUtils.sleepInMs(5);
            }
            //setLabelText(this.msgLabel, "PumpDownload_Disconnected");

            freePort();
            //setProgressBar(this.myProgBar, 0);
        }
    }

    private boolean testmode(boolean stat)
    {
        String temps = "";
        String cmds = "";
        int i = 0;
        int j = 0;
        int ck1 = 0;
        int ck2 = 0;
        int n = 0;
        int cr = 0;
        try
        {
            if (stat)
            {
                cmds = this.p_start_test_mode;

                AnimasUtils.sleepInMs(300);
            }
            else
            {
                cmds = this.p_end_test_mode;

                AnimasUtils.sleepInMs(300);
            }
            temps = IR1000ReadComm();
            temps = "";
            i = 0;
            while (i < 5)
            {
                if (!comWrite(cmds))
                {
                    return false;
                }
                AnimasUtils.sleepInMs(400);
                temps = IR1000ReadComm();
                if ((!temps.equals("")) && (temps != null) && (temps.charAt(0) == '+'))
                {
                    i = 10;
                }
                else
                {
                    i += 1;
                }
            }
            if (this.debg)
            {
                System.out.println("test mode response: |" + temps + "|");
            }
            if ((i != 10) || (temps.indexOf("(") == 0) || (temps.indexOf(")") == 0)
                    || (temps.indexOf(")") < temps.indexOf("(")))
            {
                return false;
            }
            i = temps.indexOf("(") + 1;
            ck1 = 0;
            ck2 = 0;
            j = 0;
            n = 0;
            while (i < (temps.indexOf(")") - 2))
            {
                if (((temps.charAt(i) >= '0') && (temps.charAt(i) <= '9'))
                        || ((temps.charAt(i) >= 'A') && (temps.charAt(i) <= 'F')))
                {
                    j += 1;
                    n = (n * 16) + Integer.parseInt("" + temps.charAt(i), 16);
                    if (j == 2)
                    {
                        ck1 += n;
                        if (ck1 >= 255)
                        {
                            ck1 -= 255;
                        }
                        ck2 += ck1;
                        if (ck2 >= 255)
                        {
                            ck2 -= 255;
                        }
                        n = 0;
                        j = 0;
                    }
                }
                i += 1;
            }
            if (((temps.charAt(i) >= '0') && (temps.charAt(i) <= '9'))
                    || ((temps.charAt(i) >= 'A') && (temps.charAt(i) <= 'F') && (((temps.charAt(i + 1) >= '0') && (temps
                            .charAt(i + 1) <= '9')) || ((temps.charAt(i + 1) >= 'A') && (temps.charAt(i + 1) <= 'F')))))
            {
                cr = Integer.parseInt("" + temps.charAt(i) + temps.charAt(i + 1), 16);
            }
            else
            {
                return false;
            }
            if (this.debg)
            {
                System.out.println(" cr=" + String.valueOf(cr) + " ck2=" + String.valueOf(ck2));
            }
            if (cr == ck2)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return false;
    }

    public void openConnectionIR1000() throws PlugInBaseException
    {

        boolean bolConnected = false;
        this.timer3Enabled = true;
        new Thread(new Timer3(this)).start();
        while (!this.fClose)
        {
            // for (int porti = startP; porti < endP; porti++)
            // {
            this.ctlMode = false;
            this.connectionState = false;
            this.connectionAddr = 0;
            this.downloadStatus = 0;
            this.pingTime = 0;
            this.disconnectTime = 0;
            //this.pumpi = -1;
            this.downloadError = "";
            this.fClose = false;
            //setProgressBar(this.myProgBar, 0);
            // setLabelText(this.commPortLabel,
            // this.myPortList.getItemAt(porti).toString());

            //setLabelText(this.msgLabel, "PumpDownload_Search");

            AnimasUtils.sleepInMs(10);

            if (initSerialDevice())
            {
                this.ctlMode = false;
                this.connectionState = false;
                this.connectionAddr = 0;
                // this.findingModel = false;
                this.downloadStatus = 0;
                this.pingTime = 0;
                this.disconnectTime = 0;
                this.readBufferLength = 0;
                this.fcSum1 = 0;
                this.fcSum2 = 0;
                this.downloadError = "";
                this.foundSerialNumber = 0;
                this.foundSerialNumberStr = new String[10];

                AnimasUtils.sleepInMs(1000);


                bolConnected = false;
                int intAttempt = 1;
                while ((!bolConnected) && (intAttempt < 2))
                {
                    bolConnected = testmode(true);
                    intAttempt++;
                }
                if (bolConnected)
                {
                    break;
                }
                if (this.fClose)
                {
                    closeConnection();

                    return;
                }
                this.connected = true;
                //incProgressBar(this.myProgBar);
            }
            // }
            if (!bolConnected)
            {
                closeConnection();
                this.fClose = true;
                throw new PlugInBaseException(PlugInExceptionType.DeviceNotFoundOnConfiguredPort);
                // int n =
                // this.mw.showYesNoMessage("PumpDownload_No_Pump_Found"), 0);
                // if (n == 1)

            }
            if (bolConnected)
            {
                break;
            }
        }

        if (this.fClose)
        {
            closeConnection();

            return;
        }

        this.pumpModel = "PumpModel_IR1000";
        //setLabelText(this.msgLabel, "PumpDownload_Connected");
        //setProgressBar(this.myProgBar, 10);
        this.connected = true;
    }


    public static class Timer3 implements Runnable
    {
        AnimasCommProtocolV1 pc1000;

        public Timer3(AnimasCommProtocolV1 pc1000)
        {
            this.pc1000 = pc1000;
        }

        public void run()
        {
            while ((this.pc1000.timer3Enabled) && (!this.pc1000.fClose))
            {
                if ((this.pc1000.downloadStatus > 0) && (!this.pc1000.fClose))
                {
                    this.pc1000.timer3Enabled = false;
                    this.pc1000.fClose = true;
                    this.pc1000.closeConnection();
                }
                try
                {
                    Thread.sleep(3000L);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public AnimasImplementationType getImplementationType()
    {
        return AnimasImplementationType.AnimasImplementationV1;
    }


    protected String IR1000ReadComm() throws PlugInBaseException
    {
        byte[] buffer = new byte[1024];
        short[] newReadTemp = new short[20000];
        int newReadTempLength = 0;
        // String ms = "";
        int len = -1;
        try
        {
            while (this.commHandler.available() > 0)
            {
                len = this.commHandler.read(buffer);
                for (int i = 0; i < len; i++)
                {
                    if (buffer[i] < 0)
                    {
                        newReadTemp[(newReadTempLength + i)] = ((short) (256 + buffer[i]));
                    }
                    else
                    {
                        newReadTemp[(newReadTempLength + i)] = (buffer[i]);
                    }
                }
                newReadTempLength += len;
            }
        }
        catch (PlugInBaseException e)
        {
            e.printStackTrace();
            System.out.println("ERROR");

            return "";
        }
        String theReturn = "";
        for (int i = 0; i < newReadTempLength; i++)
        {
            theReturn = theReturn + (char) newReadTemp[i];
        }
        return theReturn;
    }


} // 6000 1415 605