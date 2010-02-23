package ggc.pump.device.animas;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DatabaseProtocol;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;

import java.sql.ResultSet;
import java.util.ArrayList;

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

public class FRC_EZManager_v2 extends DatabaseProtocol implements FileReaderContext 
{

    private static Log log = LogFactory.getLog(FRC_EZManager_v2.class);
    
    OutputWriter output_writer = null;

    
    
    public FRC_EZManager_v2(OutputWriter ow)
    {
        super();
        output_writer = ow;
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
        
        this.setJDBCConnection(DatabaseProtocol.DB_CLASS_MS_ACCESS_MDB_TOOLS, "");
        
        
        // read
        // exact sqls
        


		ArrayList<PumpValuesEntry> entries = new ArrayList<PumpValuesEntry>();

		callBack(0);

		
		/*
        OleDbConnection con = new OleDbConnection("Provider=Microsoft.Jet.OLEDB.4.0;Data Source=" + m_localPref.FileName);
        con.Open();
		
			
	    OleDbCommand com = new OleDbCommand("select * from bgLog", con);                                    
        OleDbDataReader reader = com.ExecuteReader();                 
			*/		
		
		
        ResultSet rs = this.executeQuery("select * from bgLog"); 
        
        String type = "bgLog";

        if (rs!=null)
        {
            try
            {

                while(rs.next())
                {					
    			
    			    
    			    // day, month, year, hour, minute
    			    ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
    			    
                    double bg = rs.getDouble(5);

                    
                    PumpValuesEntry pve = new PumpValuesEntry();
                    pve.setDateTimeObject(atd);
                    
                    pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                    pve.setSubType(PumpAdditionalDataType.PUMP_ADD_DATA_BG);
                    
                    
                    /*
    				GregorianCalendar dt = new GregorianCalendar(year,month,day,hour,min,0,0);
    				
    				GlucoseEntry entry = new GlucoseEntry();
    				entry.Device = Device.Pump;
    				entry.Time = dt;
    				entry.Value = bg;
    				*/
    				
                    // FIXME
    				//entries.Add(entry);					
                }

                rs.close();
                
			}
			catch(Exception ex)
			{
			    log.error("Error reading row [" + type + ":" + ex, ex);
				//Console.WriteLine("Bad Row");
			}		
    
        }
			

        callBack(15);
			
		/*		    
		OleDbCommand com = new OleDbCommand("select * from insulinLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */

        rs = this.executeQuery("select * from insulinLog"); 
        
        type = "insulinLog";

        
        try
        {
        
            while(rs.next())
            {

                // day, month, year, hour, minute
                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));

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
        
        
        callBack(30);
			
		/*
        OleDbCommand com = new OleDbCommand("select * from notesLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */
        
        rs = this.executeQuery("select * from notesLog"); 

        type = "notesLog";

        
        try
        {
        
            while(rs.next())
            {

                // day, month, year, hour, minute
                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));

                String note = rs.getString(5); 

                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(atd);
                
                pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                pve.setSubType(PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT);
                pve.setValue(note);
                
                
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
            
		}
        catch (Exception ex)
        {
            log.error("Error reading row [" + type + "]:" + ex, ex);
        }                   
               
			
        callBack(45);
			
        /*				
		OleDbCommand com = new OleDbCommand("select * from foodLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */
        
        rs = this.executeQuery("select * from foodLog"); 
        type = "foodLog";

        
        try
        {
        
        
            while(rs.next())
            {


                // day, month, year, hour, minute
                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(5), rs.getInt(6));
                
                double multiplier = rs.getDouble(7); //double.Parse(reader.GetValue(7).ToString());

                double carbs = rs.getDouble(8);
                //double.TryParse(reader.GetValue(8).ToString(), out carbs);						
				carbs = carbs / multiplier;

                double fiber = rs.getDouble(9);
                //double.TryParse(reader.GetValue(9).ToString(), out fiber);
				fiber = fiber / multiplier;


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
				
        callBack(60);
			
		/*		
		OleDbCommand com = new OleDbCommand("select * from activityLog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */
        
        rs = this.executeQuery("select * from activityLog"); 
        type = "activityLog";

        try
        {
        
            while(rs.next())
            {

                // day, month, year, hour, minute
                ATechDate atd = getAtechDate(rs.getInt(0), rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));


                int duration = rs.getInt(5); //Integer.parseInt(reader.GetValue(5).ToString());

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
				
			
        callBack(75);			
			
        /*				
		OleDbCommand com = new OleDbCommand("select * from pumpbasallog", con);
        OleDbDataReader reader = com.ExecuteReader();
        */

        rs = this.executeQuery("select * from pumpbasallog"); 
        type = "pumpbasallog";


        try
        {
        
            while(rs.next())
            {

                try
                {

                // day, month, year, hour, minute
                ATechDate atd = getAtechDate(rs.getInt(3), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
            
                // FIXME
                
                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(atd);
                
                pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                
                
/*                
                int year = Integer.parseInt(reader.GetValue(1).ToString());
                int month = Integer.parseInt(reader.GetValue(2).ToString());
                int day = Integer.parseInt(reader.GetValue(3).ToString());
                int hour = Integer.parseInt(reader.GetValue(4).ToString());
                int min = Integer.parseInt(reader.GetValue(5).ToString());

                
                int rate1 = Integer.parseInt(reader.GetValue(7).ToString());
									
				double rate = rate1 / 1000.0; //units per hour
				
				
				
				
				
	*/								
				
                
                
				//DateTime dt = new DateTime(year,month,day,hour,min,0,0);

                // FIXME
/*				
				GradualInsulinEntry entry = new GradualInsulinEntry();
				entry.Device = Device.Pump;
				entry.Time = dt;
				entry.Rate = rate;
				entry.Insulin = Insulin.Unknown;	
				
				//we dont get the lenght but we can calculate it based
				//on the time between it and the next basal entry
				entry.Length = 0;
									
				entries.Add(entry); */
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
            log.error("Error reading row [" + type + "]:" + ex, ex);
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


    public ATechDate getAtechDate(int day, int month, int year, int hour, int minute)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S);
        atd.day_of_month = day;
        atd.month = month;
        atd.year = year;
        atd.hour_of_day = hour;
        atd.minute = minute;
        atd.second = 0;
        
        return null;
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

