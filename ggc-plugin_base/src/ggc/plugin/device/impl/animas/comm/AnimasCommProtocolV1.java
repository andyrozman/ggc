package ggc.plugin.device.impl.animas.comm;

import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.util.AnimasException;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    static String[] TIME_STRINGS = { "12:00 AM", "12:30 AM", "01:00 AM", "01:30 AM", "02:00 AM", "02:30 AM",
                                    "03:00 AM", "03:30 AM", "04:00 AM", "04:30 AM", "05:00 AM", "05:30 AM", "06:00 AM",
                                    "06:30 AM", "07:00 AM", "07:30 AM", "08:00 AM", "08:30 AM", "09:00 AM", "09:30 AM",
                                    "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "01:00 PM",
                                    "01:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM",
                                    "05:00 PM", "05:30 PM", "06:00 PM", "06:30 PM", "07:00 PM", "07:30 PM", "08:00 PM",
                                    "08:30 PM", "09:00 PM", "09:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM",
                                    "" };
    // private static boolean DEBUG = false;

    public String dbDateFormat = "yyyy-MM-dd";
    public String dbTimeFormat = "hh:mm a";
    public int[] dst = new int[4];

    private String[] inputstr = new String[256];

    private String p_dld_alarm_log = "[|0 01 00 FF FF]";
    private String p_dld_bolus_log = "[|0 02 00 FF FF]";
    private String p_dld_pump_info = "[>0 02 00 FF FF]";
    private String p_dld_total_log = "[|0 00 00 FF FF]";
    private String p_end_test_mode = "t";
    private String p_start_test_mode = "[M0 00 00 00 00]";

    public short[] readBuffer = new short['È'];
    public short[] readTemp = new short['È'];
    public int readTempLength = 0;
    public int timeDelta = 0;
    public boolean timer1Enabled = false;
    public boolean timer2Enabled = false;
    public boolean timer3Enabled = false;
    public boolean timer4Enabled = false;
    public boolean isIR1000 = true;
    public boolean inDataProcessing = false;
    public boolean fClose = false;
    public boolean downloadCompleted = false;
    private boolean dld_tout = false;
    private boolean dld_done = false;
    public boolean dldProgBarIncrement = false;
    private boolean debg = false;
    public boolean connected = false;
    public boolean cancelDownload = false;
    private String workingTxt = "";
    long userId = 1L;

    public String IR1200DL_N;
    public int ackNeeded;
    public JLabel actionLabel;
    public int autoconnectStateIndex;
    public int basalProgramNum;
    public String checkWriteData;
    public boolean checkWriteStatus;
    public JLabel commPortLabel;
    public int connectionAddr;
    public boolean connectionState;
    public boolean ctlMode;
    public boolean dataProcessing;

    public int dataType;
    public int disconnectTime;
    public JProgressBar dldProgBar;
    public int dldQuantity;
    public int dldType;
    // public String downloadError;
    public boolean downloadReceived;
    public int downloadStatus;
    public int fcSum1;
    public int fcSum2;
    public int foundSerialNumber;
    public String[] foundSerialNumberStr;
    public String inBuf;
    public String inData;
    public boolean inDataReady;
    public JLabel msgLabel;
    public JProgressBar myProgBar;
    public int nr;
    public int ns;
    public int pingTime;
    public String pumpModel;
    public int pumpi;
    public int readBufferLength;
    public boolean receiveEnabled;
    public String regAddr;
    public boolean rrNeeded;
    public char[] sendBuffer;
    public String serialNumber;
    public JLabel serialNumberLabel;
    private Map<String, String> settingsMap;
    public String softwareCode;

    private String downloadError;

    public AnimasCommProtocolV1(String portName, AnimasDeviceType deviceType, AnimasTransferType transferType,
            AnimasDeviceReader deviceReader, OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
        this.timeDelta = 0; // mw.timeDelta;AnimasDeviceReader deviceReader
        this.dldType = 0;
    }

    private void initarray()
    {
        for (int i = 0; i < 256; i++)
        {
            this.inputstr[i] = "";
        }
    }

    private boolean IR1000CheckCRC(String txt, int nr, int rl)
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

    private String newLine()
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

    private boolean comWrite(String txt)
    {
        int i = 0;
        try
        {
            sendchar('\r');
        }
        catch (Exception e)
        {
            return false;
        }
        AnimasUtils.sleepInMs(40);
        try
        {
            sendchar('\n');
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
                sendchar(txt.charAt(i));
            }
            catch (Exception e)
            {
                return false;
            }
            AnimasUtils.sleepInMs(40);
        }
        return true;
    }

    private boolean downloadIR1000Data(String dldstr, int dldtype, int reclen, int nrrec, String errcode, String msg)
            throws AnimasException
    {
        int i = 0;
        int j = 0;
        // int pp = 0;

        initarray();
        while (i < 3)
        {
            j = downloadIR1000Data3(dldstr, dldtype, reclen, nrrec, errcode, msg);
            if (j == nrrec)
            {
                i = 5;
            }
            else
            {
                i++;
            }
        }
        if (j != nrrec)
        {
            try
            {
                for (i = 1; i <= 3; i++)
                {
                    comWrite(this.p_end_test_mode);
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

            return false;
        }
        return true;
    }


    private String IR1000ReadComm()
    {
        byte[] buffer = new byte[1024];
        short[] newReadTemp = new short[20000];
        int newReadTempLength = 0;
        // String ms = "";
        int len = -1;
        try
        {
            while (this.inStream.available() > 0)
            {
                len = this.inStream.read(buffer);
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
        catch (IOException e)
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

    private int downloadIR1000Data3(String dldstr, int dldtype, int reclen, int nrrec, String errcode, String msg)
    {
        // int j = 0;
        int i = 0;
        // int iprocessg = 0;
        int tout = 0;
        String temps = "";
        String rtemps = "";
        String CKSM = "";
        // String text = "";
        try
        {
            if (!comWrite(dldstr))
            {
                return -2;
            }
            AnimasUtils.sleepInMs(1000);
            setLabelText(this.actionLabel, msg);

            setProgressBar(this.dldProgBar, (dldtype - 1) * 256 * 2);
            temps = "";
            if (this.debg)
            {
                System.out.println("Downloading " + String.valueOf(dldtype) + ": |");
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
                    incProgressBar(this.dldProgBar);
                    tout = 0;
                }
                if (rtemps.indexOf(")") >= 0)
                {
                    this.dld_done = true;
                }
            }
            incProgressBar(this.dldProgBar);
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
            if (!IR1000CheckCRC(CKSM, nrrec, dldtype))
            {
                if (this.debg)
                {
                    System.out.println("CKSM error: [" + CKSM + "] does not match");
                }
                return 5;
            }
            setProgressBar(this.dldProgBar, ((dldtype * 2) - 1) * 256);

            return nrrec;
        }
        catch (Exception e)
        {}
        return -1;
    }

    private void setLabelText(JLabel actionLabel, String msg)
    {
        System.out.println("Label: " + msg);
    }

    private boolean downloadIR1000() throws AnimasException
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

        this.dldProgBar.setMaximum(2048);
        setProgressBar(this.dldProgBar, 0);
        if (!this.connected)
        {
            this.downloadError = "IR1000_Not_Found";
            this.downloadStatus = 2;

            return false;
        }
        if (!this.fClose)
        {
            if (this.debg)
            {
                System.out.println("Downloading RAM!");
            }
            if (!downloadIR1000Data(this.p_dld_pump_info, 4, 9, 224, "R", "IR1000_Download_PumpSettings"))
            {
                return false;
            }
            RAMdownloadIR1000();
        }
        if (!this.fClose)
        {
            if (this.debg)
            {
                System.out.println("Downloading daily totals!");
            }
            if (!downloadIR1000Data(this.p_dld_total_log, 1, 19, 256, "D", "IR1000_Download_TDD"))
            {
                return false;
            }
            dailytotalsdownloadIR1000(); // process
        }
        if (!this.fClose)
        {
            if (this.debg)
            {
                System.out.println("Downloading alarm history!");
            }
            if (!downloadIR1000Data(this.p_dld_alarm_log, 2, 19, 256, "H", "IR1000_Download_Alarm"))
            {
                return false;
            }
            historydownloadIR1000(); // process
        }
        if (!this.fClose)
        {
            if (this.debg)
            {
                System.out.println("Downloading bolus history!");
            }
            if (!downloadIR1000Data(this.p_dld_bolus_log, 3, 19, 256, "B", "IR1000_Download_Bolus"))
            {
                return false;
            }
            bolusdownloadIR1000();
        }
        setProgressBar(this.dldProgBar, 2048);
        try
        {
            for (i = 1; i <= 3; i++)
            {
                comWrite(this.p_end_test_mode);
            }
            closeConnection();
        }
        catch (Exception e)
        {}
        this.downloadCompleted = true;

        return true;
    }

    private void dailytotalsdownloadIR1000()
    {
        int year = 0;
        int month = 0;
        int day = 0;
        int data1 = 0;
        int data2 = 0;
        int data3 = 0;
        int data4 = 0;
        double btotal = 0.0D;
        double dtotal = 0.0D;
        for (int i = 0; i < 256; i++)
        {
            if (this.inputstr[i].length() > 8)
            {
                year = Integer.parseInt("" + this.inputstr[i].charAt(3) + this.inputstr[i].charAt(4), 16) + 2000;
                month = Integer.parseInt("" + this.inputstr[i].charAt(5) + this.inputstr[i].charAt(6), 16);
                day = Integer.parseInt("" + this.inputstr[i].charAt(7) + this.inputstr[i].charAt(8), 16);
                data1 = Integer.parseInt("" + this.inputstr[i].charAt(9) + this.inputstr[i].charAt(10), 16);
                data2 = Integer.parseInt("" + this.inputstr[i].charAt(11) + this.inputstr[i].charAt(12), 16);
                data3 = Integer.parseInt("" + this.inputstr[i].charAt(13) + this.inputstr[i].charAt(14), 16);
                data4 = Integer.parseInt("" + this.inputstr[i].charAt(15) + this.inputstr[i].charAt(16), 16);

                btotal = (0.005D * data1) + data2;
                dtotal = (0.005D * data3) + data4;

                String mrds = "";
                // String mrts = "";
                Calendar c = Calendar.getInstance();

                c.set(year, month - 1, day, 0, 0);

                DateFormat df1 = new SimpleDateFormat(this.dbDateFormat);
                // DateFormat df2 = new SimpleDateFormat(this.dbTimeFormat);

                // mrts = df2.format(c.getTime());
                mrds = df1.format(c.getTime());

                // Calendar td = Calendar.getInstance();
                if ((year != 1) || (month != (2 + 1)) || (day != 5))
                {
                    String BgInsSql = " insert into tddlog (id,bolust,basalt,rec_date,flag,userid)  values ((case when (select max(ID) from TDDLOG) IS NULL then 1 else (select max(ID) from TDDLOG)+1 end), "
                            + String.valueOf(Math.round((dtotal - btotal) * 1000.0D))
                            + " ,"
                            + String.valueOf(Math.round(btotal * 1000.0D))
                            + ","
                            + " '"
                            + mrds
                            + "',"
                            + "0"
                            + ","
                            + String.valueOf(userId) + ")";

                    writeDb(BgInsSql, "tddlog");
                }
            }
            incProgressBar(this.dldProgBar);
        }
    }

    private void writeDb(String sql, String table)
    {
        System.out.println("Sql [" + table + "]: " + sql);
    }

    private void historydownloadIR1000()
    {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int data3 = 0;
        // int data4 = 0;
        // double btotal = 0.0D;
        // double dtotal = 0.0D;
        for (int i = 0; i < 256; i++)
        {
            if (this.inputstr[i].length() > 8)
            {
                year = Integer.parseInt("" + this.inputstr[i].charAt(3) + this.inputstr[i].charAt(4), 16) + 2000;
                month = Integer.parseInt("" + this.inputstr[i].charAt(5) + this.inputstr[i].charAt(6), 16);
                day = Integer.parseInt("" + this.inputstr[i].charAt(7) + this.inputstr[i].charAt(8), 16);
                hour = Integer.parseInt("" + this.inputstr[i].charAt(9) + this.inputstr[i].charAt(10), 16);
                minute = Integer.parseInt("" + this.inputstr[i].charAt(11) + this.inputstr[i].charAt(12), 16);
                data3 = Integer.parseInt("" + this.inputstr[i].charAt(13) + this.inputstr[i].charAt(14), 16);
                String text;
                if (data3 < 10)
                {
                    text = "00" + String.valueOf(data3);
                }
                else
                {
                    // String text;
                    if (data3 < 10)
                    {
                        text = "0" + String.valueOf(data3);
                    }
                    else
                    {
                        text = String.valueOf(data3);
                    }
                }
                text = "IR1000_Alarm_Code" + text;

                String mrds = "";
                String mrts = "";
                Calendar c = Calendar.getInstance();

                c.set(year, month - 1, day, hour, minute);

                DateFormat df1 = new SimpleDateFormat(this.dbDateFormat);
                DateFormat df2 = new SimpleDateFormat(this.dbTimeFormat);

                mrts = df2.format(c.getTime());
                mrds = df1.format(c.getTime());

                String BgInsSql = " insert into ALARMLOG (id,alarm,pumpalarm,rec_date,rec_time,userid,is_delete,is_modify,is_archived,is_backup,uniqueid)  values ((case when (select max(ID) from ALARMLOG) IS NULL then 1 else (select max(ID) from ALARMLOG)+1 end), '"
                        + text
                        + "',"
                        + String.valueOf(Math.round(data3))
                        + ","
                        + " '"
                        + mrds
                        + "','"
                        + mrts
                        + "',"
                        + String.valueOf(userId) + ",0,1,0,0,0)";

                writeDb(BgInsSql, "ALARMLOG");

            }
            incProgressBar(this.dldProgBar);
        }
    }

    private void bolusdownloadIR1000()
    {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int data3 = 0;
        int data4 = 0;
        // double btotal = 0.0D;
        // double dtotal = 0.0D;
        for (int i = 0; i < 256; i++)
        {
            if (this.inputstr[i].length() > 8)
            {
                year = Integer.parseInt("" + this.inputstr[i].charAt(3) + this.inputstr[i].charAt(4), 16) + 2000;
                month = Integer.parseInt("" + this.inputstr[i].charAt(5) + this.inputstr[i].charAt(6), 16);
                day = Integer.parseInt("" + this.inputstr[i].charAt(7) + this.inputstr[i].charAt(8), 16);
                hour = Integer.parseInt("" + this.inputstr[i].charAt(9) + this.inputstr[i].charAt(10), 16);
                minute = Integer.parseInt("" + this.inputstr[i].charAt(11) + this.inputstr[i].charAt(12), 16);
                data3 = Integer.parseInt("" + this.inputstr[i].charAt(13) + this.inputstr[i].charAt(14), 16);
                data4 = Integer.parseInt("" + this.inputstr[i].charAt(15) + this.inputstr[i].charAt(16), 16);

                if ((year != 2000) || (month != 0) || (day != 0))
                {
                    String mrds = "";
                    String mrts = "";
                    Calendar c = Calendar.getInstance();

                    c.set(year, month - 1, day, hour, minute);

                    DateFormat df1 = new SimpleDateFormat(this.dbDateFormat);
                    DateFormat df2 = new SimpleDateFormat(this.dbTimeFormat);

                    mrts = df2.format(c.getTime());
                    mrds = df1.format(c.getTime());

                    int bolusType = data4;

                    String BgInsSql = " insert into boluslog (id,bolus,bolus_type,bolus_duration,rec_date,rec_time,userid, is_delete,is_modify,is_archived,is_backup,uniqueid,source)  values ((case when (select max(ID) from BOLUSLOG) IS NULL then 1 else (select max(ID) from BOLUSLOG)+1 end), "
                            + String.valueOf(Math.round(data3 * 10))
                            + " ,"
                            + bolusType
                            + ","
                            + String.valueOf(0)
                            + ", '" + mrds + "'," + "'" + mrts + "'," + String.valueOf(userId) + ",0,1,0,0,0,1)";

                    writeDb(BgInsSql, "boluslog");

                }
            }
            incProgressBar(this.dldProgBar);
        }
    }

    private void RAMdownloadIR1000()
    {
        int sn1 = 0;
        int sn2 = 0;
        int data2 = 0;
        int basal_start_offset = 0;
        int hour = 0;
        int minute = 0;
        double btotal = 0.0D;
        int[] mem0 = new int['á'];
        int[] mem1 = new int['á'];
        int[] mem3 = new int['á'];
        for (int i = 0; i < 224; i++)
        {
            mem0[(i + 1)] = Integer.parseInt("" + this.inputstr[i].charAt(3) + this.inputstr[i].charAt(4), 16);

            mem1[(i + 1)] = Integer.parseInt("" + this.inputstr[i].charAt(5) + this.inputstr[i].charAt(6), 16);

            mem3[(i + 1)] = Integer.parseInt("" + this.inputstr[i].charAt(7) + this.inputstr[i].charAt(8), 16);

            incProgressBar(this.dldProgBar);
        }
        sn1 = mem3[(Integer.parseInt("20", 16) - 31)];
        sn2 = mem3[(Integer.parseInt("21", 16) - 31)];
        if (sn1 >= 128)
        {
            sn1 -= 128;
        }

        serialNumber = String.valueOf((sn1 * 256) + sn2);
        this.settingsMap.put("PUMP_SERIAL_NO", serialNumber);
        setLabelText(this.serialNumberLabel, serialNumber);
        this.settingsMap.put("ADVMAXBASAL", String.valueOf(mem0[119] * 0.05D));
        this.settingsMap.put("ADVMAXBOLUS", String.valueOf(mem0[120] * 0.1D));
        this.settingsMap.put("ADVMAXTDD", String.valueOf(mem0[118] * 5));
        if (mem0[97] == 1)
        {
            this.settingsMap.put("ADVAUTOOFFENABLED", "1");
        }
        else
        {
            this.settingsMap.put("ADVAUTOOFFENABLED", "0");
        }
        this.settingsMap.put("ADVAUTOOFFTIME", String.valueOf(mem0[98]));
        if (mem0[110] == 1)
        {
            this.settingsMap.put("ADVAUDIOBOLENABLED", "1");
        }
        else
        {
            this.settingsMap.put("ADVAUDIOBOLENABLED", "0");
        }
        this.settingsMap.put("ADVAUDIOBOLSTEP", String.valueOf(0.5D + (0.5D * mem0[109])));
        if (mem0[111] == 1)
        {
            this.settingsMap.put("ADVADVANCEDBOLUS", "1");
        }
        else
        {
            this.settingsMap.put("ADVADVANCEDBOLUS", "0");
        }
        if (mem0[99] == 1)
        {
            this.settingsMap.put("ADVCARTWARNLVL", "1");
        }
        else
        {
            this.settingsMap.put("ADVCARTWARNLVL", "0");
        }
        if (mem0[121] == 1)
        {
            this.settingsMap.put("ADVOCCLLIMITS", "1");
        }
        else
        {
            this.settingsMap.put("ADVOCCLLIMITS", "0");
        }
        if (mem0[103] == 1)
        {
            this.settingsMap.put("CLKMODE", "1");
        }
        else
        {
            this.settingsMap.put("CLKMODE", "0");
        }
        if (mem0[108] == 1)
        {
            this.settingsMap.put("SNDTEMPBASAL", "1");
        }
        else
        {
            this.settingsMap.put("SNDTEMPBASAL", "0");
        }
        if (mem0[114] == 1)
        {
            this.settingsMap.put("SNDNORMALBOLUS", "1");
        }
        else
        {
            this.settingsMap.put("SNDNORMALBOLUS", "0");
        }
        for (int i = 0; i < 4; i++)
        {
            this.settingsMap.put("BAS_" + String.valueOf(i) + "_NAME",
                "IR1000_Basal_Program_Name" + " " + String.valueOf(i + 1));
        }
        basal_start_offset = 0;
        data2 = mem1[(basal_start_offset + 2)];
        for (int i = 1; i <= data2; i++)
        {
            hour = (int) Math.round((mem1[(basal_start_offset + (i * 2) + 1)] / 2) - 0.5D);

            minute = (mem1[(basal_start_offset + (i * 2) + 1)] - (2 * hour)) * 30;
            btotal = mem1[(basal_start_offset + ((i + 1) * 2))] * 0.05D;

            Calendar segc = Calendar.getInstance();

            segc.set(2000, 0, 1, hour, minute);

            DateFormat dft = new SimpleDateFormat(this.dbTimeFormat);

            this.settingsMap.put("BAS_0_" + String.valueOf(i - 1) + "_TIME", dft.format(segc.getTime()));

            this.settingsMap.put("BAS_0_" + String.valueOf(i - 1) + "_VAL", String.valueOf(btotal));
        }
        basal_start_offset = 25;
        data2 = mem1[(basal_start_offset + 2)];
        for (int i = 1; i <= data2; i++)
        {
            hour = (int) Math.round((mem1[(basal_start_offset + (i * 2) + 1)] / 2) - 0.5D);

            minute = (mem1[(basal_start_offset + (i * 2) + 1)] - (2 * hour)) * 30;
            btotal = mem1[(basal_start_offset + ((i + 1) * 2))] * 0.05D;

            Calendar segc = Calendar.getInstance();

            segc.set(2000, 0, 1, hour, minute);

            DateFormat dft = new SimpleDateFormat(this.dbTimeFormat);

            this.settingsMap.put("BAS_1_" + String.valueOf(i - 1) + "_TIME", dft.format(segc.getTime()));

            this.settingsMap.put("BAS_1_" + String.valueOf(i - 1) + "_VAL", String.valueOf(btotal));
        }
        basal_start_offset = 50;
        data2 = mem1[(basal_start_offset + 2)];
        for (int i = 1; i <= data2; i++)
        {
            hour = (int) Math.round((mem1[(basal_start_offset + (i * 2) + 1)] / 2) - 0.5D);

            minute = (mem1[(basal_start_offset + (i * 2) + 1)] - (2 * hour)) * 30;
            btotal = mem1[(basal_start_offset + ((i + 1) * 2))] * 0.05D;

            Calendar segc = Calendar.getInstance();

            segc.set(2000, 0, 1, hour, minute);

            DateFormat dft = new SimpleDateFormat(this.dbTimeFormat);

            this.settingsMap.put("BAS_2_" + String.valueOf(i - 1) + "_TIME", dft.format(segc.getTime()));

            this.settingsMap.put("BAS_2_" + String.valueOf(i - 1) + "_VAL", String.valueOf(btotal));
        }
        basal_start_offset = 75;
        data2 = mem1[(basal_start_offset + 2)];
        for (int i = 1; i <= data2; i++)
        {
            hour = (int) Math.round((mem1[(basal_start_offset + (i * 2) + 1)] / 2) - 0.5D);

            minute = (mem1[(basal_start_offset + (i * 2) + 1)] - (2 * hour)) * 30;
            btotal = mem1[(basal_start_offset + ((i + 1) * 2))] * 0.05D;

            Calendar segc = Calendar.getInstance();

            segc.set(2000, 0, 1, hour, minute);

            DateFormat dft = new SimpleDateFormat(this.dbTimeFormat);

            this.settingsMap.put("BAS_3_" + String.valueOf(i - 1) + "_TIME", dft.format(segc.getTime()));

            this.settingsMap.put("BAS_3_" + String.valueOf(i - 1) + "_VAL", String.valueOf(btotal));
        }

        // saveSettingsRecord();
    }


    public void startOperation(AnimasTransferType transferType)
    {

        this.setLabelText(null, "MESSAGE_BEFORE_DOWNLOAD_STARTS 1");

        this.regAddr = "";
        if (this.isIR1000)
        {
            openConnectionIR1000();
        }

        if ((this.dldType == 0) && (this.isIR1000))
        {
            if (this.downloadStatus == 0)
            {
                this.settingsMap = new HashMap<String, String>(213);
                if (!this.fClose)
                {
                    this.settingsMap.put("PUMP_MODEL", "PumpModel_IR1000");
                }
                try
                {
                    downloadIR1000();
                }
                catch (AnimasException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                this.fClose = true;
                while (!this.fClose)
                {
                    try
                    {
                        Thread.sleep(2000L);
                    }
                    catch (Exception e)
                    {}
                }
            }
            // this.mw.pumpReturnFromDownload();

            return;
        }
        else
        {
            LOG.error("This operation not supported for models 1000 and 1200.");
        }
        // if (this.dldType == 1)
        // {
        // // Settings for 1000 ??
        // if (!this.fClose)
        // {
        // // downloadSettings();
        // }
        // this.fClose = true;
        // while (!this.fClose)
        // {
        // this.sleepInMs(100L);
        // }
        // // this.ps.pumpReturnFromDownload();
        // }
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

    public void incProgressBar(JProgressBar pg)
    {
        if (pg != null)
        {
            if (pg.getValue() == pg.getMaximum())
            {
                pg.setValue(0);
            }
            pg.setValue(pg.getValue() + 1);
            pg.repaint();
        }
    }

    public String FillString(String mstr, int slen)
    {
        for (int i = mstr.length(); i < slen; i++)
        {
            mstr = mstr + '\000';
        }
        int lsum1 = 0;
        int lsum2 = 0;
        for (int i = 0; i < mstr.length(); i++)
        {
            lsum1 += (short) mstr.charAt(i);
            if (lsum1 >= 255)
            {
                lsum1 -= 255;
            }
            lsum2 += lsum1;
            if (lsum2 >= 255)
            {
                lsum2 -= 255;
            }
        }
        int check1 = 255 - (lsum1 + lsum2);
        if (check1 < 1)
        {
            check1 += 255;
        }
        int check2 = 255 - (lsum1 + check1);
        if (check2 < 1)
        {
            check2 += 255;
        }
        return mstr + (char) check1 + (char) check2;
    }

    public boolean checkCRC(String mstr)
    {
        // boolean CRC = false;
        int lsum1 = 0;
        int lsum2 = 0;
        for (int i = 0; i < (mstr.length() - 2); i++)
        {
            lsum1 += (short) mstr.charAt(i);
            if (lsum1 >= 255)
            {
                lsum1 -= 255;
            }
            lsum2 += lsum1;
            if (lsum2 >= 255)
            {
                lsum2 -= 255;
            }
        }
        int check1 = 255 - (lsum1 + lsum2);
        if (check1 < 1)
        {
            check1 += 255;
        }
        int check2 = 255 - (lsum1 + check1);
        if (check2 < 1)
        {
            check2 += 255;
        }
        if ((mstr.charAt(mstr.length() - 2) == (char) check1) && (mstr.charAt(mstr.length() - 1) == (char) check2))
        {
            return true;
        }
        return false;
    }

    public void fletcher(int c)
    {
        this.fcSum1 += c;
        if (this.fcSum1 >= 255)
        {
            this.fcSum1 -= 255;
        }
        this.fcSum2 += this.fcSum1;
        if (this.fcSum2 >= 255)
        {
            this.fcSum2 -= 255;
        }
    }

    public boolean check_fletcher(String mstr)
    {
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i < (mstr.length() - 2); i++)
        {
            sum1 += (short) mstr.charAt(i);
            if (sum1 >= 255)
            {
                sum1 -= 255;
            }
            sum2 += sum1;
            if (sum2 >= 255)
            {
                sum2 -= 255;
            }
        }
        if (((char) sum1 == mstr.charAt(mstr.length() - 2)) && ((char) sum2 == mstr.charAt(mstr.length() - 1)))
        {
            return true;
        }
        return false;
    }

    public String num2hex(short c)
    {
        String ms = String.format("%2x", new Object[] { Short.valueOf(c) });
        if (ms.charAt(0) == ' ')
        {
            char[] data = { '0', ms.charAt(1) };

            ms = new String(data);
        }
        return ms;
    }

    public String num2dec(short c, int len)
    {
        String ms = String.format("%" + String.valueOf(len) + "d", new Object[] { Short.valueOf(c) });

        return ms;
    }

    public void irtx(char c)
    {
        short myc = (short) c;
        if (myc < 0)
        {
            myc = (short) (myc + 256);
        }
        // short nyc = myc;

        fletcher(myc);
        if ((myc == 125) || (myc == 192) || (myc == 193))
        {
            c = (char) (myc ^ 0x20);
            sendchar('}');
        }
        sendchar(c);
    }

    public void sendchar(char c)
    {
        try
        {
            this.outStream.write(c);
        }
        catch (Exception e)
        {}
    }

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
                try
                {
                    Thread.sleep(5L);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }

            this.timer3Enabled = false;

            for (int i = 0; i < 200; i++)
            {
                try
                {
                    Thread.sleep(5L);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
            setLabelText(this.msgLabel, "PumpDownload_Disconnected");

            freePort();
            setProgressBar(this.myProgBar, 0);
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

    public void openConnectionIR1000()
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
            this.pumpi = -1;
            this.downloadError = "";
            this.fClose = false;
            setProgressBar(this.myProgBar, 0);
            // setLabelText(this.commPortLabel,
            // this.myPortList.getItemAt(porti).toString());

            setLabelText(this.msgLabel, "PumpDownload_Search");
            try
            {
                Thread.sleep(10L);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
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
                try
                {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
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
                incProgressBar(this.myProgBar);
            }
            // }
            if (!bolConnected)
            {
                // int n =
                // this.mw.showYesNoMessage("PumpDownload_No_Pump_Found"), 0);
                // if (n == 1)
                {
                    this.downloadStatus = 0;
                    this.downloadError = "";
                    this.fClose = true;
                }
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
        setLabelText(this.msgLabel, "PumpDownload_Connected");
        setProgressBar(this.myProgBar, 10);
        this.connected = true;
    }

    public void downloadwait(int qty)
    {
        while ((this.dldQuantity < qty) && (this.downloadStatus == 0) && (!this.fClose))
        {
            try
            {
                Thread.sleep(1L);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        if ((this.downloadStatus > 0) && (!this.fClose))
        {
            this.fClose = true;
            closeConnection();
            // this.mw.pumpReturnFromDownload();

            return;
        }
        if (this.fClose)
        {}
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
        // TODO Auto-generated method stub
        return AnimasImplementationType.AnimasImplementationV1;
    }

} // 6000
