package main.java.ggc.pump.device.minimed.old;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.protocol.DatabaseProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     MinimedSPM
 *  Description:  Minimed 'Solution Pumps and Meters' "device". This is for importing data from SMP
 *                export file (access database with mmp extension)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class MinimedSPM extends DatabaseProtocol
{

    DataAccessPlugInBase m_da = null; // DataAccessPump.getInstance();
    int count_unk = 0;
    private static final Logger LOG = LoggerFactory.getLogger(MinimedSPM.class);
    String[] profile_names;


    /**
     * Constructor
     *
     * @param filename
     * @param da
     */
    public MinimedSPM(String filename, DataAccessPlugInBase da)
    {
        // this.setJDBCConnection(DatabaseProtocol.DB_CLASS_MS_ACCESS_MDB_TOOLS,
        // "jdbc:mdbtools:" + filename, null, "wolfGang");

        this.m_da = da;
        String url = ""; // DatabaseProtocol.URL_MS_ACCESS_JDBC_ODBC_BRIDGE.replace("%FILENAME%",
                         // filename) + ";PWD=wolfGang";

        // String url =
        // "jdbc:odbc:Driver={Microsoft Access Driver
        // (*.mdb)};DBQ=f:\\Rozman_A_Plus_20090423.mdb;PWD=wolfGang";
        System.out.println("Url: " + url);

        profile_names = new String[3];
        profile_names[0] = "Standard";
        profile_names[1] = "Pattern A";
        profile_names[2] = "Pattern B";

        this.setJDBCConnection(DatabaseProtocol.DATABASE_MS_ACCESS_ODBC_BRIDGE, "path", null, null, ";PWD=wolfGang");

        // DatabaseProtocol.URL_MS_ACCESS_JDBC_ODBC_BRIDGE.replace("%FILENAME",
        // filename) + ";PWD=wolfGang", null, null);

        // DB_CLASS_MS_ACCESS_JDBC_ODBC_BRIDGE

    }


    /**
     * Read Data
     */
    public abstract void readData();


    /*
     * {
     * //this.readDailyTotals();
     * //this.readPrimes();
     * //readEvents();
     * //readAlarms();
     * //readBoluses();
     * this.readBasals();
     * }
     */

    protected void readBasals()
    {
        readBasalHistory();
    }


    private void readBasalHistory()
    {
        try
        {
            System.out.println("=======   BASAL HISTORY   ========");

            ResultSet rs = this.executeQuery(
                " select EV.EHDate as EHDate, EV.EHTime as EHTime, PH.PHTime as PHTime, PH.PHAmount as PHAmount, PH.Pattern as Pattern  from tblProfileHistory PH "
                        + " inner join tblEvents EV on PH.EVX = EV.EVX " + " order by EV.EVX, PH.PHTime ");

            long current_dt = -1L;
            int current_profile = -1;
            MinimedSPMData data = null;

            while (rs.next())
            {
                long dt = getDateTime(rs.getDate("EHDate"), rs.getTime("EHTime"), DATETIME_MIN);
                int profile = rs.getInt("Pattern");

                if (dt != current_dt || profile != current_profile)
                {
                    if (current_dt != -1)
                    {
                        this.processDataEntry(data);
                    }

                    data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_PROFILE);
                    data.datetime = dt;

                    current_dt = dt;
                    current_profile = profile;

                    data.value_str = this.getProfileName(profile);
                }

                data.addProfile(rs.getShort("PHTime"), rs.getFloat("PHAmount"));

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    @SuppressWarnings("deprecation")
    protected void readDailyTotals()
    {
        try
        {
            System.out.println("=======   DAILY TOTALS   ========");

            ResultSet rs = this.executeQuery("select THDate, THAmount from tblDailyTotals " + " where THAmount > 0 ");

            while (rs.next())
            {

                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_DOUBLE);

                data.datetime = getDateTime(rs.getDate("THDate"), new Time(23, 59, 59));
                data.base_type = 6; // PUMP_DATA_REPORT
                data.sub_type = 4; // PUMP_REPORT_INSULIN_TOTAL_DAY
                data.value_dbl = rs.getDouble("THAmount");

                this.processDataEntry(data);
                /*
                 * Date d = rs.getDate(1);
                 * Double val = rs.getDouble(2);
                 * System.out.println("Date=" + d + ";Value=" + val);
                 */
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    protected void readPrimes()
    {
        try
        {
            System.out.println("=======   PRIMES   ========");

            ResultSet rs = this.executeQuery("select PHDate, PHTime, PHAmount from tblPrimes ");

            while (rs.next())
            {

                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_DOUBLE);

                data.datetime = getDateTime(rs.getDate("PHDate"), rs.getTime("PHTime"));
                data.base_type = 3; // PUMP_DATA_EVENT
                data.sub_type = 1; // PUMP_EVENT_PRIME_INFUSION_SET
                data.value_dbl = rs.getDouble("PHAmount");

                this.processDataEntry(data);

                /*
                 * Date date = rs.getDate("PHDate");
                 * Time time = rs.getTime("PHTime");
                 * Double val = rs.getDouble("PHAmount");
                 * System.out.println("Date=" + date + ";Time=" + time +
                 * ";Value=" + val);
                 */
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    protected void readAlarms()
    {
        try
        {
            System.out.println("=======   ALARMS   ========");

            ResultSet rs = this.executeQuery("select AHDate, AHTime, AHCode from tblAlarms ");

            while (rs.next())
            {

                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_INT);

                data.datetime = getDateTime(rs.getDate("AHDate"), rs.getTime("AHTime"));
                data.base_type = 4; // PUMP_DATA_ALARM
                data.sub_type = 0;
                data.value_int = rs.getInt("AHCode");

                this.processDataEntry(data);

                /*
                 * Date date = rs.getDate("AHDate");
                 * Time time = rs.getTime("AHTime");
                 * int val = rs.getInt("AHCode");
                 * System.out.println("Date=" + date + ";Time=" + time +
                 * ";Alarm Code=" + val);
                 */
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    protected void readBoluses()
    {
        try
        {
            System.out.println("=======   BOLUSES   ========");

            ResultSet rs = this.executeQuery(
                "select BHDate, BHTime, BHType, BHAmount, BHDuration, BHDescription, BHDateStatus, BHAmountProg "
                        + "from tblBolusHistory order by BHDate, BHTime");

            while (rs.next())
            {
                int type = rs.getInt("BHType");
                double amount = rs.getDouble("BHAmount");
                double duration = rs.getDouble("BHDuration");

                String type_desc = "";
                boolean set = false;

                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_STRING);

                data.datetime = getDateTime(rs.getDate("BHDate"), rs.getTime("BHTime"));
                data.base_type = 2; // PUMP_DATA_BOLUS
                // data.sub_type = 0;
                // data.value_int = rs.getInt("AHCode");

                this.processDataEntry(data);

                if (type == 1 || type == 2)
                {
                    data.sub_type = 1; // PUMP_BOLUS_STANDARD = 1;
                    data.value_str = "" + amount;
                    // type_desc = "Normal;Amount=" + amount;
                    set = true;
                }
                else if (type == 3)
                {
                    data.sub_type = 5; // PUMP_BOLUS_SQUARE
                    data.value_str = "VALUE=" + amount + ";DURATION=" + duration;

                    // type_desc = "Square;Amount=" + amount + "Duration=" +
                    // duration + " [min]";
                    set = true;
                }
                else if (type == 4)
                {
                    data.sub_type = 7; // PUMP_BOLUS_DUAL_NORMAL
                    data.value_str = "" + amount;
                    // type_desc = "Dual/Normal;Amount=" + amount;
                    set = true;
                }
                else if (type == 5)
                {
                    data.sub_type = 8; // PUMP_BOLUS_DUAL_SQUARE
                    data.value_str = "VALUE=" + amount + ";DURATION=" + duration;

                    // type_desc = "Dual/Square;Amount=" + amount + "Duration="
                    // + duration + " [min]";
                    set = true;
                }
                else
                {
                    type_desc = "Unknown Bolus Type (" + type + ")";
                    LOG.error("Bolus: " + type_desc);
                }
                // else

                if (set)
                {
                    this.processDataEntry(data);
                }

                // if (!set)
                // System.out.println("Date=" + date + ";Time=" + time +
                // ";Type=" + type_desc + ";Amount=" + amount);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("deprecation")
    protected void readEvents()
    {
        try
        {
            System.out.println("=======   EVENTS   ========");

            ResultSet rs = this
                    .executeQuery("select EVX, EHDate, EHTime, EHCode, EHNewTime, EHByte1, EHInteger1, EHSingle1 "
                            + " from tblEvents order by EHDate, EHTime");

            while (rs.next())
            {

                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_STRING);

                data.datetime = getDateTime(rs.getDate("EHDate"), rs.getTime("EHTime"));
                data.base_type = 3; // PUMP_DATA_EVENT

                // Date date = rs.getDate("EHDate");
                // Time time = rs.getTime("EHTime");
                int code = rs.getInt("EHCode");
                Time val_time = rs.getTime("EHNewTime");
                // byte val_byte = rs.getByte("EHByte1");
                int val_int = rs.getInt("EHInteger1");
                double val_dbl = rs.getDouble("EHSingle1");

                String desc = "";
                boolean set = false;
                switch (code)
                {
                    case 1:
                    case 61:
                        desc = "Event=Time Change;Time=" + val_time;
                        data.sub_type = 41; // PUMP_EVENT_DATETIME_CORRECT
                        data.value_str = "" + (val_time.getHours() * 100 + val_time.getMinutes());
                        set = true;
                        break;

                    case 3:
                        desc = "Event=Set Max Bolus;Value=" + val_dbl;
                        data.sub_type = 51; // PUMP_EVENT_SET_MAX_BOLUS
                        data.value_str = "" + val_dbl;
                        set = true;
                        break;

                    case 4:
                        desc = "Event=Set Max Basal;Value=" + val_dbl;
                        data.sub_type = 50; // PUMP_EVENT_SET_MAX_BASAL
                        data.value_str = "" + val_dbl;
                        set = true;
                        break;

                    case 6:
                        desc = "Event=Pump Suspend";
                        data.sub_type = 21; // PUMP_EVENT_BASAL_STOP
                        set = true;
                        break;

                    case 7:
                        desc = "Event=Pump Resume";
                        data.sub_type = 20; // PUMP_EVENT_BASAL_RUN
                        set = true;
                        break;

                    case 8:
                        desc = "Event=Basal Profile Changed (" + getProfileName(val_int) + ")";
                        data.base_type = 1; // PUMP_DATA_BASAL
                        data.sub_type = 2; // PUMP_BASAL_PROFILE
                        data.value_str = this.getProfileName(val_int);
                        set = true;
                        break;

                    case 9:
                        desc = "Event=Temporary Basal Rate;Value=" + val_dbl + ";Duration=" + val_int + " (min)";
                        data.base_type = 1; // PUMP_DATA_BASAL
                        data.sub_type = 3; // PUMP_BASAL_TEMPORARY_BASAL_RATE

                        int hour = val_int / 60;
                        int time = hour * 100 + val_int - hour * 60;
                        data.value_str = "VALUE=" + val_dbl + ";" + "DURATION=" + time;
                        set = true;
                        break;

                    case 10:
                        desc = "Event=Baterry Removed";
                        data.sub_type = 55; // PUMP_EVENT_BATERRY_REMOVED
                        set = true;
                        break;

                    case 11:
                        desc = "Event=Baterry Replaced";
                        data.sub_type = 56; // PUMP_EVENT_BATERRY_REPLACED
                        set = true;
                        break;

                    case 13:
                        desc = "Event=Basal Pattern Set (" + getProfileName(val_int) + ")";
                        data.sub_type = 15; // PUMP_EVENT_SET_BASAL_PATTERN
                        data.value_str = getProfileName(val_int);
                        desc = "Event=Basal Pattern Set (" + data.value_str + ")";

                        set = true;
                        break;

                    case 16:
                        desc = "Event=Low Battery";
                        data.sub_type = 57; // PUMP_EVENT_BATERRY_LOW
                        set = true;
                        break;

                    case 17:
                        desc = "Event=Low Reservoir";
                        data.sub_type = 4; // PUMP_EVENT_RESERVOIR_LOW
                        set = true;
                        break;

                    case 18:
                        desc = "Event=Rewind";
                        data.sub_type = 3; // PUMP_EVENT_CARTRIDGE_REWIND = 3;
                        set = true;
                        break;

                    case 20:
                        desc = "Event=Self Test";
                        data.sub_type = 30; // PUMP_EVENT_SELF_TEST
                        set = true;
                        break;

                    case 23:
                        desc = "Event=Download";
                        data.sub_type = 31; // PUMP_EVENT_DOWNLOAD
                        set = true;
                        break;

                    case 47:
                        desc = "Event=Low Reservoir (remaining=" + val_dbl + ")";
                        data.sub_type = 5; // PUMP_EVENT_RESERVOIR_LOW_DESC
                        data.value_str = DataAccessPlugInBase.Decimal1Format.format(val_dbl);
                        set = true;
                        break;

                    case 49:
                        float val = m_da.getBGValueFromDefault(m_da.getGlucoseUnitType(), (float) val_dbl);
                        // desc="Event=BG Sent from Meter (" + val + ")";
                        data.sub_type = 70; // PUMP_EVENT_BG_FROM_METER

                        if (m_da.getGlucoseUnitType() == GlucoseUnitType.mg_dL)
                        {
                            data.value_str = DataAccessPlugInBase.Decimal0Format.format(val);
                        }
                        else
                        {
                            data.value_str = DataAccessPlugInBase.Decimal1Format.format(val);
                        }

                        desc = "Event=BG Sent from Meter (" + data.value_str + ")";
                        set = true;
                        break;

                    case 59:
                        desc = "Event=Temp Basal Type Set (" + val_int + " [1=%, x=U])";
                        data.sub_type = 10; // PUMP_EVENT_SET_TEMPORARY_BASAL_RATE_TYPE
                        data.value_str = "" + val_int;
                        set = true;
                        break;

                    case 45: // Bolus Delivery
                    case 48: // Check BG reminder
                    case 56: // easy bolus option set
                    case 57: // BG reminder option set
                    case 60: // alarm type set
                        set = true;
                        break;

                    default:
                        {
                            desc = "Unknown event (" + code + ")";
                            LOG.error(desc);
                        }

                }

                if (set)
                {
                    this.processDataEntry(data);
                }
                else
                {
                    System.out.println(desc);
                }

                // if (!set)
                // System.out.println("Date=" + date + ";Time=" + time + ";" +
                // desc);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private String getProfileName(int type)
    {
        if (type == 0)
            return "Standard";
        else if (type == 1)
            return "Pattern A";
        else
            return "Pattern B";
    }


    private long getDateTime(Date date, Time time)
    {
        return getDateTime(date, time, 1);
    }

    /**
     * Datetime in seconds
     */
    public static final int DATETIME_S = 1;

    /**
     * Datetime in minutes
     */
    public static final int DATETIME_MIN = 2;


    @SuppressWarnings("deprecation")
    private long getDateTime(Date date, Time time, int type)
    {
        int typex = 0;
        if (type == DATETIME_S)
        {
            typex = ATechDate.FORMAT_DATE_AND_TIME_S;
        }
        else
        {
            typex = ATechDate.FORMAT_DATE_AND_TIME_MIN;
        }

        ATechDate atd = new ATechDate(typex);

        atd.setDayOfMonth(date.getDate());
        atd.month = date.getMonth() + 1;
        atd.year = 1900 + date.getYear();

        atd.setHourOfDay(time.getHours());
        atd.minute = time.getMinutes();
        atd.second = time.getSeconds();

        // System.out.println("Date: " + date + " Time: " + time);
        // System.out.println("Day: " + atd.day_of_month + " Month: " +
        // atd.month + " Year: " + atd.year + " " + atd.hour_of_day + ":" +
        // atd.minute + ":" + atd.second);

        return atd.getATDateTimeAsLong();
    }


    /**
     * Process Data Entry
     *
     * @param data
     */
    public abstract void processDataEntry(MinimedSPMData data);

}
