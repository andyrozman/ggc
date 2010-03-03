package ggc.pump.device.animas;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DatabaseProtocol;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.util.DataAccessPump;

import java.sql.ResultSet;

import javax.swing.JDialog;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.ATechDate;
import com.atech.utils.file.FileReaderContext;

// EZManagerPlugin.cs
// User: innominate at 9:02 PM 8/23/2008
//
//

/**
 * @author andy
 *
 */
public class FRC_EZManager_v2 extends DatabaseProtocol implements FileReaderContext 
{

    private static Log log = LogFactory.getLog(FRC_EZManager_v2.class);
    
    OutputWriter output_writer = null;

    
    
    /**
     * Constructor
     * 
     * @param ow
     */
    public FRC_EZManager_v2(OutputWriter ow)
    {
        super();
        output_writer = ow;
        m_da = DataAccessPump.getInstance();
    }
    
		
		/*
		public PluginInfo GetPluginInfo()
		{
			
			boolean hasPrefs = false;
			hasPrefs = true;
			
						
			return new PluginInfo(new AboutInfo(
			  "EZ Manager Plus Database Reader",
			  "2.0",
			  "This reads data from the Access Database file (.mdb) used by Animas' Easy Manager Plus software.",
			  "Copyright 2009",
			  "",
			  new string[]{"Nate Barish <natenate@gmail.com>"},
			  "GNU GLP"),
			  "This reads data from the Access Database file (.mdb) used by Animas' Easy Manager Plus software. " + 
			  "You may have to unlock the database before using it.  To do this open the .mdb file in Access " +
			  "these instructions are for 2007 other version are probally similar:  Goto DatabaseTools -> " +
			  "Users and Permissions -> User and Group Permissions and check all Permissions for all Users " +
			  "and Groups.  You should only have to do this once \n\n" +
			  "In the Preference window you can choose what you want to import.",
			  hasPrefs);
			
		}*/

	
    private void callBack(int progress)
    {
    }
    

    public void readFile(String filename)
    {
        this.setJDBCConnection(DatabaseProtocol.DATABASE_MS_ACCESS_MDBTOOL, filename);
        
        //this.setJDBCConnection(DatabaseProtocol.DB_CLASS_MS_ACCESS_MDB_TOOLS, "");
        
        
        // read
        // exact sqls
        
        /*
         *     activityDb
         *     activityLog
         *     basalprograms
         * X   bgLog
         * X   dailytotalslog
         *     errorcodes
         *     foodLog
         *     insulinLog
         *     insulinLog2
         *     notesLog
         * X   pumpbasallog
         * 
         */

		//ArrayList<PumpValuesEntry> entries = new ArrayList<PumpValuesEntry>();

		callBack(0);

		
		// Blood Glucose
        ResultSet rs = this.executeQuery("select day, month, year, hours, minutes, bg, userid from bgLog"); 
        
        String type = "bgLog";

        if (rs!=null)
        {
            try
            {
                
                while(rs.next())
                {
                    
                    try
                    {
        			    // day, month, year, hour, minute
        			    ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), rs.getString("hours"), rs.getString("minutes"));
        			    
                        PumpValuesEntry pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        
                        pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                        pve.setSubType(PumpAdditionalDataType.PUMP_ADD_DATA_BG);
                        pve.setValue(rs.getString("bg"));
                        
                        this.output_writer.writeData(pve);
                    }
                    catch(Exception ex)
                    {
                        log.error("Error reading row [" + type + ":" + ex, ex);
                        return;
                    }       
                }

                rs.close();
			}
			catch(Exception ex)
			{
			    log.error("Error reading [" + type + ":" + ex, ex);
			    return;
			}		
    
        }
			

        callBack(15);

        
        
        
        // Daily Totals
        rs = this.executeQuery("select day, month, year, btotal, dtotal, userid from dailytotalslog"); 
        
        type = "dailytotalslog";

        if (rs!=null)
        {
            try
            {
                
                while(rs.next())
                {
                    
                    try
                    {
                        // day, month, year, hour, minute
                        ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), "23", "59");
                        
                        float basal = getFloat(rs.getString("btotal"));
                        float total = getFloat(rs.getString("dtotal"));
                        

                        PumpValuesEntry pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_REPORT);
                        pve.setSubType(PumpReport.PUMP_REPORT_BASAL_TOTAL_DAY);
                        pve.setValue(DataAccessPump.Decimal3Format.format(basal));
                        this.output_writer.writeData(pve);
                        
                        pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_REPORT);
                        pve.setSubType(PumpReport.PUMP_REPORT_BOLUS_TOTAL_DAY);
                        pve.setValue(DataAccessPump.Decimal3Format.format(total-basal));
                        this.output_writer.writeData(pve);                        
                        
                        pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_REPORT);
                        pve.setSubType(PumpReport.PUMP_REPORT_INSULIN_TOTAL_DAY);
                        pve.setValue(DataAccessPump.Decimal3Format.format(total));
                        this.output_writer.writeData(pve);                        
                        
                    }
                    catch(Exception ex)
                    {
                        log.error("Error reading row [" + type + ":" + ex, ex);
                    }       
                }

                rs.close();
                
            }
            catch(Exception ex)
            {
                log.error("Error reading [" + type + ":" + ex, ex);
            }       
    
        }
            

        callBack(15);
        
        
        
        
        
        
        
        
        
        
		/*		    
		OleDbCommand com = new OleDbCommand("select * from insulinLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */

        //rs = this.executeQuery("select * from insulinLog"); 
        rs = null;
        
        type = "insulinLog";

        // FIXME
        if (rs!=null)
        {
        
        try
        {
        
            while(rs.next())
            {

                // day, month, year, hour, minute
//                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                @SuppressWarnings("unused")
                ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), rs.getString("hours"), rs.getString("minutes"));
//                ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));
                int units1 = rs.getInt(5); //Integer.parseInt(reader.GetValue(5).ToString());
                int units2 = rs.getInt(16); //Integer.parseInt(reader.GetValue(16).ToString());
                
				double units = 0;
				units += (double)units1 / 10.0;
				units += (double)units2 / 100.0;
				
				
				// FIXME
				//DateTime dt = new DateTime(year,month,day,hour,min,0,0);
				
				/*
				InsulinEntry entry = new InsulinEntry();
				entry.Device = Device.Pump;
				entry.Time = dt;
				entry.Amount = units;
				entry.Insulin = Insulin.Unknown;					
				
				entries.Add(entry);					
				*/
    		}
            rs.close();

        
        }
        catch (Exception ex)
        {
            log.error("Error reading row [" + type + "]:" + ex, ex);
        }                   
        }
        
        callBack(30);
			
		/*
        OleDbCommand com = new OleDbCommand("select * from notesLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */
        
        
        // Events, Basals, Alarams, Comments
        
        rs = this.executeQuery("select day, month, year, hours, minutes, note, pumpalarm, userid from notesLog"); 

        type = "notesLog";

        if (rs!=null)
        {

            try
            {
            
                while(rs.next())
                {
    
                    // day, month, year, hour, minute
    //                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                    ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), rs.getString("hours"), rs.getString("minutes"));
    //                ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));
    
                    String note = rs.getString("note");
                    int error_code = getInt(rs.getString("pumpalarm"));
    
                    PumpValuesEntry pve = new PumpValuesEntry();
                    pve.setDateTimeObject(atd);
                    
                    pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                    pve.setSubType(PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT);
                    pve.setValue(note);
                    
                    
                    if (error_code > 0)
                    {
                        
                        
                        
                    }
                    else
                    {
                        if (note.startsWith("Pump primed"))
                        {
                            
                        }
                        else if (note.startsWith("Cannula filled"))
                        {
                        }    
                        else if (note.startsWith("Pump suspended.  Resume time:"))
                        {
                        }    
                        else
                            System.out.println("Note: " + note);
                        
                    }
    
                    
                    
                    // FIXME
                    /*                
    				DateTime dt = new DateTime(year,month,day,hour,min,0,0);
    				
    				OtherEntry entry = new OtherEntry();
    				entry.Device = Device.Pump;
    				entry.Time = dt;
    				entry.Text = note;					
    				
    				entries.Add(entry);
    				*/
    			}
                
                rs.close();
    
                return;
                
    		}
            catch (Exception ex)
            {
                log.error("Error reading row [" + type + "]:" + ex, ex);
            }                   
        }
        
        callBack(45);
			
        /*				
		OleDbCommand com = new OleDbCommand("select * from foodLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */
        
        rs = this.executeQuery("select * from foodLog"); 
        type = "foodLog";

        if (rs!=null)
        {
        
        try
        {
        
        
            while(rs.next())
            {


                // day, month, year, hour, minute
//                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(5), rs.getInt(6));
                ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));
                
                double multiplier = rs.getDouble(7); //double.Parse(reader.GetValue(7).ToString());

                double carbs = rs.getDouble(8);
                //double.TryParse(reader.GetValue(8).ToString(), out carbs);						
				carbs = carbs / multiplier;

                double fiber = rs.getDouble(9);
                //double.TryParse(reader.GetValue(9).ToString(), out fiber);
				fiber = fiber / multiplier;


                @SuppressWarnings("unused")
                double calories = rs.getDouble(10);
                //double.TryParse(reader.GetValue(10).ToString(), out calories);	
				//calories = calories / multiplier;
				//calories is not using the multiplier

                double protien = rs.getDouble(11);
                //double.TryParse(reader.GetValue(11).ToString(), out protien);						
				protien = protien / multiplier;

                double fat = rs.getDouble(12);
                //double.TryParse(reader.GetValue(12).ToString(), out fat);
				fat = fat / multiplier;


                @SuppressWarnings("unused")
                String name = rs.getString(13); //reader.GetValue(13).ToString();
				
                
                // FIXME
                
                
                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(atd);
                
                pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                
                
                /*                

                
                
				DateTime dt = new DateTime(year,month,day,hour,min,0,0);

				
				
				FoodEntry entry = new FoodEntry();
				entry.Device = Device.Pump;
				entry.Time = dt;
				entry.Name = name;
				entry.Nutrition.TotalCarb = carbs;
				entry.Nutrition.Fiber = fiber;
				entry.Nutrition.Calories = calories;
				entry.Nutrition.Protien = protien;
				entry.Nutrition.TotalFat = fat;					
				
				entries.Add(entry); */					
			
    		}
            rs.close();
        }
        catch (Exception ex)
        {
            log.error("Error reading row [" + type + "]:" + ex, ex);
        }
        }
				
        callBack(60);
			
		/*		
		OleDbCommand com = new OleDbCommand("select * from activityLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */
        
        rs = this.executeQuery("select * from activityLog"); 
        type = "activityLog";

        if (rs!=null)
        {
        
        try
        {
        
            while(rs.next())
            {

                // day, month, year, hour, minute
//                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));


                @SuppressWarnings("unused")
                int duration = rs.getInt(5); //Integer.parseInt(reader.GetValue(5).ToString());

                @SuppressWarnings("unused")
                String name = rs.getString(13); //)reader.GetValue(13).ToString();

                
                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(atd);
                
                pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                
                // FIXME
                
                /*
				DateTime dt = new DateTime(year,month,day,hour,min,0,0);
				
				ExerciseEntry entry = new ExerciseEntry();
				entry.Device = Device.Pump;
				entry.Time = dt;
				entry.Name = name;
				entry.Duration = duration;			
				
				entries.Add(entry); */
    		}
            rs.close();
        }
        catch (Exception ex)
        {
            log.error("Error reading row [" + type + "]:" + ex, ex);
        }                   
        }				
			
        callBack(75);			
			

        // Pump Basal Rates
        rs = this.executeQuery("select day, month, year, hours, minutes, rate, userid from pumpbasallog"); 
        type = "pumpbasallog";

        if (rs!=null)
        {

            try
            {
            
                while(rs.next())
                {
                    try
                    {
                        // day, month, year, hour, minute
                        ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));
                    
                        PumpValuesEntry pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        
                        pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
                        pve.setSubType(PumpBasalSubType.PUMP_BASAL_VALUE);
                        
                        double rate = rs.getInt("rate") / 1000.0d; //units per hour
                        
                        pve.setValue(DataAccessPump.Decimal3Format.format(rate));
                    }
                    catch(Exception ex)
                    {
                        log.error("Error reading row [" + type + "]:" + ex, ex);
                    }                   
                    
        		}
                
                rs.close();	
            }
            catch(Exception ex)
            {
                log.error("Error reading [" + type + "]:" + ex, ex);
            }                   
        }

        //con.Close();

			
        callBack(90);

        /*
			entries.Sort(new EntryComparer());

			double totalNum = entries.Count;
			double numberOn = 0;
			
			//determine lenght inbetween basal changes
			GradualInsulinEntry lastEntry = null;			
			foreach (Entry e in entries)
			{
				if (e is GradualInsulinEntry)
				{
					if (lastEntry != null)
					{
						TimeSpan span = e.Time - lastEntry.Time;
						double mins = span.TotalMinutes;						
						lastEntry.Length = mins;
					}			
					lastEntry = (GradualInsulinEntry)e;
				}
				
				numberOn += 1;
				callBack((int)(90.0 + (numberOn / totalNum * 10.0)));
			}
			if (lastEntry == null)
			{
				entries.Remove(lastEntry);
			}
*/
			callBack(100);			
						
			//return entries;
    }


    private int getInt(String val)
    {
        return m_da.getIntValueFromString(val, 0);
    }

    
    private float getFloat(String val)
    {
        return m_da.getFloatValueFromString(val, 0.0f);
    }
    
    
    /**
     * Get ATech Date from int's 
     * 
     * @param day
     * @param month
     * @param year
     * @param hour
     * @param minute
     * @return
     */
    public ATechDate getAtechDate(int day, int month, int year, int hour, int minute)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S);
        atd.day_of_month = day;
        atd.month = month;
        atd.year = year;
        atd.hour_of_day = hour;
        atd.minute = minute;
        atd.second = 0;
        
        return atd;
    }

    
    /**
     * Get ATech Date from int's 
     * 
     * @param day
     * @param month
     * @param year
     * @param hour
     * @param minute
     * @return
     */
    public ATechDate getAtechDate(String day, String month, String year, String hour, String minute)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S);
        atd.day_of_month = getInt(day);
        atd.month = getInt(month);
        atd.year = getInt(year);
        atd.hour_of_day = getInt(hour);
        atd.minute = getInt(minute);
        atd.second = 0;
        
        return atd;
    }

    
    

        public String getFileDescription()
        {
            // TODO Auto-generated method stub
            return null;
        }



        public String getFileExtension()
        {
            // TODO Auto-generated method stub
            return null;
        }



        public FileFilter getFileFilter()
        {
            // TODO Auto-generated method stub
            return null;
        }



    public String getFullFileDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public void goToNextDialog(JDialog currentDialog)
    {
    }



    public boolean hasSpecialSelectorDialog()
    {
        return false;
    }





}

