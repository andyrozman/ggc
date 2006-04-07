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


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import ggc.datamodels.DailyValues;
import ggc.datamodels.WeekValues;
import ggc.datamodels.DailyValuesRow;
import ggc.datamodels.HbA1cValues;
import ggc.db.datalayer.GGCDb;
import ggc.util.DataAccess;


public class HibernateHandler extends DataBaseHandler
{
    //Connection con = null;
    //private static HSQLHandler singleton = null;
    //private GGCProperties props = GGCProperties.getInstance();

    private GGCDb m_db = null;


    public HibernateHandler()
    {
        super();
	dbType = "Hibernate:";
	dbName = " [PgSQL]";

//	connected = true;
    }

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = new HibernateHandler();
        return singleton;
    }


    public boolean isInitialized()
    {
        return true;
    }


    public void connectDb()
    {
    }


    public boolean getDbInstance()
    {
	if (m_db==null)
	    m_db = DataAccess.getInstance().getDb();

	if (m_db==null)
	    return false;
	else
	    return true;
    }



    public HbA1cValues getHbA1c(GregorianCalendar day)
    {
	if (!getDbInstance())
            return null;

        System.out.println("Db::getHbA1c");

	return m_db.getHbA1c(day);
    }


    public DailyValues getDayStats(GregorianCalendar day)
    {
	if (!getDbInstance())
	    return null;

        System.out.println("Db::getDayStats");

	return m_db.getDayStats(day);
    }

    public WeekValues getDayStatsRange(GregorianCalendar sDay, GregorianCalendar eDay)
    {
	if (!getDbInstance())
	    return null;

        System.out.println("Db::getDayStatsRange");

	return m_db.getDayStatsRange(sDay, eDay);
    }



    public void saveDayStats(DailyValues dV)
    {
	if (!getDbInstance())
	    return;

	m_db.saveDayStats(dV);
    }



    public void initDb()
    {
    }


    public void disconnectDb()
    {
    }


    public boolean testDb() //throws ClassNotFoundException, SQLException
    {
	return true;
    }

    public boolean dateTimeExists(java.util.Date dd) 
    {
	return false;
    }

}
