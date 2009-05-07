package ggc.pump.device.minimed;

import ggc.plugin.protocol.DatabaseProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.ATechDate;

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
 *  Filename:     MinimedCareLink
 *  Description:  Minimed CareLink "device". This is for importing data from CareLink
 *                export file
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class MinimedSPM extends DatabaseProtocol
{
    DataAccessPlugInBase m_da = null; //DataAccessPump.getInstance();
    int count_unk = 0; 
    private static Log log = LogFactory.getLog(MinimedSPM.class);

    /**
     * Constructor
     */
    public MinimedSPM(String filename, DataAccessPlugInBase da)
    {
        //this.setJDBCConnection(DatabaseProtocol.DB_CLASS_MS_ACCESS_MDB_TOOLS, 
        //    "jdbc:mdbtools:" + filename, null, "wolfGang");

        this.m_da = da;
        String url = DatabaseProtocol.URL_MS_ACCESS_JDBC_ODBC_BRIDGE.replace("%FILENAME%", filename) + ";PWD=wolfGang";
        
        //String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=f:\\Rozman_A_Plus_20090423.mdb;PWD=wolfGang";
        System.out.println("Url: " + url);
        
        
        
        this.setJDBCConnection(DatabaseProtocol.DB_CLASS_MS_ACCESS_JDBC_ODBC_BRIDGE,
            url);
            //DatabaseProtocol.URL_MS_ACCESS_JDBC_ODBC_BRIDGE.replace("%FILENAME", filename) + ";PWD=wolfGang", null, null);
        
        //DB_CLASS_MS_ACCESS_JDBC_ODBC_BRIDGE
        
    }
    
    
    public void readData()
    {
        //this.readDailyTotals();
        //this.readPrimes();
        //readEvents();
        //readAlarms();
        readBoluses();
    }
    
    
    public void readDailyTotals()
    {
        try
        {
            System.out.println("=======   DAILY TOTALS   ========");
            
            ResultSet rs = this.executeQuery("select THDate, THAmount from tblDailyTotals " + 
                                             " where THAmount > 0 ");
            
            while (rs.next())
            {

                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_DOUBLE);
                
                data.datetime = getDateTime(rs.getDate("THDate"), new Time(23, 59, 59));
                data.base_type = 6; // PUMP_DATA_REPORT
                data.sub_type = 4; // PUMP_REPORT_INSULIN_TOTAL_DAY
                data.value_dbl = rs.getDouble("THAmount");

                this.processDataEntry(data);
                /*
                Date d = rs.getDate(1);
                Double val = rs.getDouble(2);
                
                System.out.println("Date=" + d + ";Value=" + val);
                */
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    
    public void readPrimes()
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
                data.sub_type = 1; //PUMP_EVENT_PRIME_INFUSION_SET 
                data.value_dbl = rs.getDouble("PHAmount");

                this.processDataEntry(data);
                
                
                /*
                Date date = rs.getDate("PHDate");
                Time time = rs.getTime("PHTime");
                Double val = rs.getDouble("PHAmount");
                
                System.out.println("Date=" + date + ";Time=" + time + ";Value=" + val);
                */
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    public void readAlarms()
    {
        try
        {
            System.out.println("=======   ALARMS   ========");
            
            ResultSet rs = this.executeQuery("select AHDate, AHTime, AHCode from tblAlarms ");
            
            while (rs.next())
            {
                
                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_INT);
                
                data.datetime = getDateTime(rs.getDate("AHDate"), rs.getTime("AHTime"));
                data.base_type = 4; //PUMP_DATA_ALARM  
                data.sub_type = 0; 
                data.value_int = rs.getInt("AHCode");

                this.processDataEntry(data);
                
                /*
                Date date = rs.getDate("AHDate");
                Time time = rs.getTime("AHTime");
                int val = rs.getInt("AHCode");
                
                System.out.println("Date=" + date + ";Time=" + time + ";Alarm Code=" + val);
                */  
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    public void readBoluses()
    {
        // TODO
        try
        {
            System.out.println("=======   BOLUSES   ========");
            
            ResultSet rs = this.executeQuery("select BHDate, BHTime, BHType, BHAmount, BHDuration, BHDescription, BHDateStatus, BHAmountProg " +
                 "from tblBolusHistory order by BHDate, BHTime");

            
            while (rs.next())
            {
                
                int type = rs.getInt("BHType");
                double amount = rs.getDouble("BHAmount");
                double duration = rs.getDouble("BHDuration");
                
                String type_desc="";
                boolean set = false;
                

                MinimedSPMData data = new MinimedSPMData(MinimedSPMData.SOURCE_PUMP, MinimedSPMData.VALUE_STRING);
                
                data.datetime = getDateTime(rs.getDate("BHDate"), rs.getTime("BHTime"));
                data.base_type = 2; //PUMP_DATA_BOLUS  
                //data.sub_type = 0; 
                //data.value_int = rs.getInt("AHCode");

                this.processDataEntry(data);
                
                
                
                
                if ((type==1) || (type==2))
                {
                    data.sub_type = 1; //PUMP_BOLUS_STANDARD = 1;
                    data.value_str = "" + amount;
                    //type_desc = "Normal;Amount=" + amount;
                    set = true;
                }
                else if (type==3)
                {
                    data.sub_type = 5; // PUMP_BOLUS_SQUARE
                    data.value_str = "VALUE=" + amount + ";DURATION=" + duration;

                    
                    //type_desc = "Square;Amount=" + amount + "Duration=" + duration + " [min]";
                    set = true;
                }
                else if (type==4)
                {
                    data.sub_type = 7; //PUMP_BOLUS_DUAL_NORMAL
                    data.value_str = "" + amount;
                    //type_desc = "Dual/Normal;Amount=" + amount;
                    set = true;
                }
                else if (type==5)
                {
                    data.sub_type = 8; //PUMP_BOLUS_DUAL_SQUARE
                    data.value_str = "VALUE=" + amount + ";DURATION=" + duration;

                    //type_desc = "Dual/Square;Amount=" + amount + "Duration=" + duration + " [min]";
                    set = true;
                }
                else
                {
                    type_desc = "Unknown Bolus Type (" + type + ")";
                    log.error("Bolus: " + type_desc);
                }
                //else
                    
                if (set)
                    this.processDataEntry(data);

                
                //if (!set)
                //    System.out.println("Date=" + date + ";Time=" + time + ";Type=" + type_desc + ";Amount=" + amount);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    public void readEvents()
    {
        try
        {
            System.out.println("=======   EVENTS   ========");
            
            ResultSet rs = this.executeQuery("select EVX, EHDate, EHTime, EHCode, EHNewTime, EHByte1, EHInteger1, EHSingle1 " +
                " from tblEvents order by EHDate, EHTime");

            
            while (rs.next())
            {
                Date date = rs.getDate("EHDate");
                Time time = rs.getTime("EHTime");
                int code = rs.getInt("EHCode");
                Time val_time = rs.getTime("EHNewTime");
                byte val_byte = rs.getByte("EHByte1");
                int val_int = rs.getInt("EHInteger1");
                double val_dbl = rs.getDouble("EHSingle1");
                
                String desc="";
                boolean set = false;
                switch (code)
                {
                    case 1:
                        desc="Event=Time Change;Time=" + val_time ;
                        set = true;
                        break;
                
                    case 3:
                        desc="Event=Set Max Bolus;Value=" + val_dbl ;
                        set = true;
                        break;

                    case 4:
                        desc="Event=Set Max Basal;Value=" + val_dbl ;
                        set = true;
                        break;

                    case 6:
                        desc="Event=Pump Suspend";
                        set = true;
                        break;
                        
                    case 7:
                        desc="Event=Pump Resume";
                        set = true;
                        break;
                        

                    case 8:
                        desc="Event=Basal Profile Changed (" + getProfileName(val_int) + ")";
                        set = true;
                        break;
                        
                    case 9:
                        desc="Event=Temporary Basal Rate;Value=" + val_dbl + ";Duration=" + val_int + " (min)";
                        set = true;
                        break;

                    case 10:
                        desc="Event=Baterry Removed";
                        set = true;
                        break;
                        
                    case 11:
                        desc="Event=Baterry Replaced";
                        set = true;
                        break;
                        
                    case 13:
                        desc="Event=Basal Pattern Set (" + getProfileName(val_int) + ")";
                        set = true;
                        break;

                    case 16:
                        desc="Event=Low Battery";
                        set = true;
                        break;

                    case 17:
                        desc="Event=Low Reservoir";
                        set = true;
                        break;

                    case 18:
                        desc="Event=Rewind";
                        set = true;
                        break;

                    case 20:
                        desc="Event=Self Test";
                        set = true;
                        break;

                    case 23:
                        desc="Event=Download";
                        set = true;
                        break;

                    case 47:
                        desc="Event=Low Reservoir (remaining=" + val_dbl + ")";
                        set = true;
                        break;

                    case 49:
                        desc="Event=BG Sent from Meter (" + m_da.getBGValueByType(DataAccessPlugInBase.BG_MGDL, m_da.getBGMeasurmentType(), (float)val_dbl) + ")";
                        set = true;
                        break;
                        
                    case 59:
                        desc="Event=Temp Basal Type Set (" + val_int + " [1=%, x=U])";
                        set = true;
                        break;
                        
                    case 61:
                        desc="Event=Time Change;Time=" + val_time ;
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
                        desc="Unknown event (" + code + ")";
                        log.error(desc);
                    }
                
                }
                
                
                //if (!set)
                    System.out.println("Date=" + date + ";Time=" + time + ";" + desc);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    private String getProfileName(int type)
    {
        if (type==0)
            return "Standard";
        else if (type==1)
            return  "Pattern A";
        else 
            return  "Pattern B";
    }
    

    @SuppressWarnings("deprecation")
    private long getDateTime(Date date, Time time)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S);
        
        atd.day_of_month = date.getDate();
        atd.month = date.getMonth();
        atd.year = date.getYear();
        
        atd.hour_of_day = time.getHours();
        atd.minute = time.getMinutes();
        atd.second = time.getSeconds();
        
        return atd.getATDateTimeAsLong();
    }
    
    
    
    
    
    
    public abstract void processDataEntry(MinimedSPMData data);
   
    
    
    
    
    
    
    
    

}