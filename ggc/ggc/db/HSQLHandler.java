/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: HSQLHandler.java
 *  Purpose:  Handler to access a HSQL (Hypersonic) Database.
 *
 *  Author:   andyrozman
 */

package ggc.db;


import ggc.datamodels.DailyValues;
import ggc.datamodels.DailyValuesRow;
import ggc.datamodels.HbA1cValues;
import ggc.gui.StatusBar;
import ggc.util.GGCProperties;

import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


public class HSQLHandler extends DataBaseHandler
{
    Connection con = null;
    //private static HSQLHandler singleton = null;
    //private GGCProperties props = GGCProperties.getInstance();



    public HSQLHandler()
    {
        super();
	dbType = "HSQL:";
	dbName = "Internal Db";
        //connect();

	connected = false;
	//DataBaseHandler.connectedToDB = true;

    }

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = new HSQLHandler();
        return singleton;
    }


    public boolean isInitialized()
    {
	File fl = new File("../data/ggc_db.properties");
	return fl.exists();
    }


    public void connectDb()
    {
	System.out.println("connect");

        try 
	{
            /*if (con != null)
	    {
		//con.isClosed(
		return;
	    } */
		
                
	    if (!isInitialized())
	    {
		initDb();
	    }


	    con = null;


            Class.forName("org.hsqldb.jdbcDriver");
            String sourceURL = "jdbc:hsqldb:file:../data/ggc_db";
            con = DriverManager.getConnection(sourceURL);
            connected = true;
            //StatusBar.getInstance().setDataSourceText("HSQL Internal Db");

	    //checkDatabase();

	    connected = true;
	    //DataBaseHandler.connectedToDB = true;

	    //dbStatus = "HSQL [" + m_ic.getMessage("CONNECTED") + "]";
	    setStatus();
	    //StatusBar.getInstance().setStatus();


        } 
	catch (ClassNotFoundException cnfe) 
	{
            System.err.println(cnfe);
        } 
	catch (SQLException sqle) 
	{
            System.err.println(sqle);
        }
        
	//if (props.getMySQLOpenDefaultDB())
        //    openDataBase(false);
    }




    public HbA1cValues getHbA1c(java.util.Date day)
    {
	//connectDb();
	
	if (!connected)
            return null;

        ResultSet results = null;
        HbA1cValues hbVal = new HbA1cValues();

        if (con != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String eDay = sdf.format(day);
                String sDay = sdf.format(new Date(day.getTime() - 1000L * 60L * 60L * 24L * 90L));


		String query = "SELECT avg(bg), count(bg) from DayValues WHERE bg <> 0 AND dt_date >=  " + sDay + " AND dt_date <= " + eDay + " GROUP BY dt_date, dt_time;";

                //String query = "SELECT avg(bg), count(bg) from DayValues WHERE bg <> 0 and dt_date(dt, '%Y-%m-%d') >= '" + sDay + "' and date_format(dt, '%Y-%m-%d') <= '" + eDay + "' group by date_format(dt, '%Y-%m-%d');";
                Statement statement = con.createStatement();
                results = statement.executeQuery(query);
                while (results.next()) {
                    hbVal.addDay(results.getFloat(1), results.getInt(2));
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        return hbVal;
    }


    public DailyValues getDayStats(java.util.Date day)
    {
	//connectDb();
	
	if (!connected)
            return null;

        ResultSet results = null;
        DailyValues dV = new DailyValues();

        if (con != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String sDay = sdf.format(day);
                //String query = "SELECT * FROM DayValues WHERE date_format(dt, '%Y-%m-%d') = '" + sDay + "' ORDER BY dt";
                
		String query = "SELECT * FROM DayValues WHERE dt_date = " + sDay + " ORDER BY dt_date, dt_time";
		Statement statement = con.createStatement();
                results = statement.executeQuery(query);

                while (results.next()) 
		{
                    
		    //sdf.applyPattern("yyyy-MM-dd HH:mm");
                    java.util.Date rDate = getDate(results.getInt("DT_DATE"), results.getInt("DT_TIME"));

		    /*
                    try {
                        rDate = sdf.parse(results.getString(1));
                    } catch (ParseException e) {
                    } catch (SQLException e) {
                    } */

                    float rBG = results.getFloat(4);
                    float rIns1 = results.getFloat(5);
                    float rIns2 = results.getFloat(6);
                    float rBE = results.getFloat(7);
                    int rAct = results.getInt(8);
                    String rComment = results.getString(9);

                    DailyValuesRow dVR = new DailyValuesRow(rDate, rBG, rIns1, rIns2, rBE, rAct, rComment);
                    dV.setNewRow(dVR);
                }

            } 
	    catch (Exception e) 
	    {
                System.err.println(e);
            }
            dV.setDate(day);
        }
        return dV;
    }

    public void saveDayStats(DailyValues dV)
    {
        
	connectDb();
	
	if (!connected)
            return;

        Statement statement;
        try {
            if (!con.isClosed() && dV.hasChanged()) 
	    {
                statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                System.out.println(dV.getDateAsString());
                if (!dV.onlyInsert())
                    statement.execute("DELETE FROM DayValues where dt_date=" + dV.getDateD());

                //ResultSet rs = statement.executeQuery("SELECT * from DayValues limit 1");
                
		PreparedStatement ps_add = con.prepareStatement("INSERT INTO DayValues (dt_date, dt_time, bg, ins1, ins2, bu, act, comment) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

		PreparedStatement ps_upd = con.prepareStatement("UPDATE DayValues SET dt_date=?, dt_time=?, bg=?, ins1=?, ins2=?, bu=?, act=?, comment=? WHERE id=?");

		for (int i = 0; i < dV.getRowCount(); i++) 
		{
		    
		    long idx = dateTimeExists(dV.getDateD(), dV.getDateT(i));

		    if (idx==-1)
		    {

			ps_add.setInt(1, dV.getDateD());
			ps_add.setInt(2, dV.getDateT(i));
			ps_add.setFloat(3, dV.getBGAt(i));
			ps_add.setFloat(4, dV.getIns1At(i));
			ps_add.setFloat(5, dV.getIns2At(i));
			ps_add.setFloat(6, dV.getBUAt(i));
			ps_add.setInt(7, dV.getActAt(i));
			ps_add.setString(8, dV.getCommentAt(i));

			ps_add.executeUpdate();

		    }
		    else
		    {
			ps_upd.setInt(1, dV.getDateD());
			ps_upd.setInt(2, dV.getDateT(i));
			ps_upd.setFloat(3, dV.getBGAt(i));
			ps_upd.setFloat(4, dV.getIns1At(i));
			ps_upd.setFloat(5, dV.getIns2At(i));
			ps_upd.setFloat(6, dV.getBUAt(i));
			ps_upd.setInt(7, dV.getActAt(i));
			ps_upd.setString(8, dV.getCommentAt(i));
			ps_upd.setLong(9, idx);

			ps_upd.executeUpdate();

		    }

                }
            }
        } catch (Exception e) 
	{
            System.err.println(e);
            e.printStackTrace();
        }
    }


    public int dateTimeExists(int date, int time)
    {
	connectDb();
	
	if (!connected)
            return -1;
        
        Statement statement;
        try 
	{
            if (!con.isClosed()) 
	    {

                statement = con.createStatement();
                String tmp = new String("SELECT id FROM DayValues where dt_date= " + date + " AND dt_time=" + time);
                
		//System.out.println(tmp);
                ResultSet rs = statement.executeQuery(tmp);
                if (rs.next())
                    return rs.getInt(1); //true;
                else
                    return -1;
            }
        } 
	catch (Exception e)  { }

        return -1;
    }


    public boolean dateTimeExists(java.util.Date date)
    {
        
	connectDb();
	
	if (!connected)
            return false;

        Statement statement;
        try {
            if (!con.isClosed()) 
	    {
                SimpleDateFormat sf_date = new SimpleDateFormat("yyyyMMdd");
		//SimpleDateFormat sf_time = new SimpleDateFormat("HHmm");
                
		//String sDate = sf.format(date);


		int time = (date.getHours()*100) + date.getMinutes();


                statement = con.createStatement();
                String tmp = new String("SELECT * FROM DayValues where dt_date= " + sf_date.format(date) + " AND dt_time=" + time);
                
		//System.out.println(tmp);
                ResultSet rs = statement.executeQuery(tmp);
                if (rs.next())
                    return true;
                else
                    return false;
            }
        } catch (Exception e) {
        }
        return false;
    }



    public void initDb()
    {
        
	System.out.println("create");

	try 
	{

	    connectDb();
            
	    Statement statement;


            statement = con.createStatement();
	    statement.execute("DROP TABLE DayValues");



            statement = con.createStatement();
            String query2 = "CREATE TABLE DayValues( " + 
			    "id bigint NOT NULL IDENTITY PRIMARY KEY," + 
			    "dt_date int NOT NULL, "+
		            "dt_time int NOT NULL, "+
			    "bg decimal(7,2) NOT NULL, "+
			    "ins1 decimal(6,2) NOT NULL, "+
			    "ins2 decimal(6,2) NOT NULL, "+
			    "bu decimal(6,2) NOT NULL, "+
			    "act tinyint NOT NULL, "+
			    "comment varchar(255) NOT NULL)";
/*
            String query2 = "CREATE TABLE DayValues( " + 
			    "dt DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00', "+
			    "bg decimal(7,2) unsigned NOT NULL default '0.00', "+
			    "ins1 decimal(6,2) unsigned NOT NULL default '0.00', "+
			    "ins2 decimal(6,2) unsigned NOT NULL default '0.00', "+
			    "bu decimal(6,2) unsigned NOT NULL default '0.00', "+
			    "act tinyint(3) unsigned NOT NULL default '0', "+
			    "comment varchar(255) NOT NULL default '', "+
			    "PRIMARY KEY (datetime)";
  */

            statement.execute(query2);
            //dbName = name;

	    setStatus();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }




    public void disconnectDb()
    {
        try {
            con.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        } finally {
            connected = false;
            //connectedToDB = false;

	    //dbStatus = "HSQL [" + m_ic.getMessage("NO_CONNECTION") + "]";
	    //StatusBar.getInstance().setStatus();

	    setStatus();

            //StatusBar.getInstance().setDataSourceText("HSQL [" + m_ic.getMessage("NO_CONNECTION") + "]");
        }
    }


    public boolean testDb() //throws ClassNotFoundException, SQLException
    {
        try {
            Connection tmpcon;

            Class.forName("org.hsqldb.jdbcDriver");
            String sourceURL = "jdbc:hsqldb:file:../data/ggc_db";
            tmpcon = DriverManager.getConnection(sourceURL);
            tmpcon.close();
            return true;

        } 
        catch (ClassNotFoundException e) 
        {
            //throw e;
        } 
        catch (SQLException e) 
        {
            //throw e;
        }

	return false;

    }


    public static java.util.Date getDate(int date, int time)
    {

	//20050530


        int year = date/10000;
        int month = date - (year*10000);

        month = month/100;

        int days = date - (year*10000) - (month*100);


        int hours = time/100;
        int mins = time - (hours*100);


/*
	String dt = "" + date;
	String tm = "" + time;

	while (tm.length()<4)
	{
	    tm = "0" + tm;
	}


	String year = dt.substring(0, 4);
	String month = dt.substring(4, 6);
	String day = dt.substring(6, 8);

	String hours = tm.substring(0,2);
	String mins =  tm.substring(2,4);



	String hours = tm.substring(0,2);
	String mins =  tm.substring(2,4);
*/


	java.util.Date dat = new java.util.Date();

        dat.setDate(days);
	dat.setMonth(month-1);
	dat.setYear(year-1900);

	dat.setHours(hours);
	dat.setMinutes(mins);


/*	
        dat.setDate(Integer.parseInt(day));
	dat.setMonth(Integer.parseInt(month)-1);
	dat.setYear(Integer.parseInt(year)-1900);

	dat.setHours(Integer.parseInt(hours));
	dat.setMinutes(Integer.parseInt(mins));
*/

	return dat;


	//System.out.println("Date: " + day + "." + month + "." + year+ "  Time: " + hours + ":" + mins);




	//return null;



    }


    public static void main(String args[])
    {
	HSQLHandler.getDate(20040505, 110);
    }


}
