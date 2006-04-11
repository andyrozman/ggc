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
 *  Filename: DataBaseHandler.java
 *  Purpose:  Abstract class containing methods that all DataSourceHandlers
 *            must implement.
 *
 *  Author:   schultd
 */

package ggc.db;


import ggc.datamodels.DailyValues;
import ggc.datamodels.WeekValues;
import ggc.datamodels.HbA1cValues;
import ggc.gui.StatusBar;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;

import java.util.Date;
import java.util.GregorianCalendar;


public abstract class DataBaseHandler
{
    
    protected I18nControl m_ic = I18nControl.getInstance();    
    //protected GGCProperties props = GGCProperties.getInstance();
    
    //public static boolean connectedToDB = false;
    public boolean connected = false;

    public static DataBaseHandler singleton = null;
    //static GGCProperties props = GGCProperties.getInstance();
    
    protected String dbName;
    protected String dbType;

    //protected String dbStatus = "";

    protected DataBaseHandler()
    {
        //dbName = props.getDBName();
    }

    public static DataBaseHandler getInstance()
    {

	if (singleton == null) 
	{

	    singleton = HibernateHandler.getInstance();
/*
	    String s = GGCProperties.getInstance().getDataSource();

	    //System.out.println("DataSource: " + s);

            if (s.equals("HSQL"))
                singleton = HSQLHandler.getInstance();
	    else if (s.equals("MySQL"))
                singleton = MySQLHandler.getInstance();
            else if (s.equals("Textfile"))
                singleton = TextFileHandler.getInstance();
            else
                singleton = DummyHandler.getInstance();
		*/
        }
        return singleton;
    }

/*
    public static boolean hasInstance()
    {
        return singleton != null;
    }
*/
    public static void killHandler()
    {
        singleton = null;
    }

    public boolean isConnected()
    {
        return connected;
    }
/*
    public boolean isConnectedToDB()
    {
        return connectedToDB;
    }
*/

    public abstract boolean isInitialized();

/*
    public void setDBName(String name)
    {
        if (name != null && !name.equals(""))
            dbName = name;
        else
            JOptionPane.showMessageDialog(null, m_ic.getMessage("INVALID_NAME_FOR_DB"), "GGC " + m_ic.getMessage("ERROR")+ " - " + m_ic.getMessage("INVALID_NAME"), JOptionPane.ERROR_MESSAGE);
    }
*/
    public abstract DailyValues getDayStats(GregorianCalendar day);

    public abstract WeekValues getDayStatsRange(GregorianCalendar sDay, GregorianCalendar eDay);

    public abstract HbA1cValues getHbA1c(GregorianCalendar day);

    public abstract void saveDayStats(DailyValues dV);

    public abstract boolean dateTimeExists(java.util.Date date);

    //public abstract void createNewDataBase(String name);

    public abstract void connectDb();

    public abstract void disconnectDb();

    public abstract void initDb();

    //public abstract boolean testDb();

    //public abstract boolean testDb(String Host, String Port, String DB, String User, String Pass) throws ClassNotFoundException, SQLException;

    //public abstract void closeConnection();

    //public abstract void openDataBase(boolean ask);

    //public abstract void closeDataBase();


    public String getStatus()
    {
	
	String st = "";
	
	System.out.println(connected);

	if (connected)
	    st = m_ic.getMessage("CONNECTED");
	else
	{
	    if (!this.isInitialized())
	    {
                st = m_ic.getMessage("NOT_INIT");
	    }
	    else
	    {
		st = m_ic.getMessage("NOT_CONNECTED");
	    }
	    
	}

	return dbType + dbName + " [" + st + "]";
	
    }

    public void setStatus()
    {
	StatusBar.getInstance().setDataSourceText(getStatus());
    }
    
}