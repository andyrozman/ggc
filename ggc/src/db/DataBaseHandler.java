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

package db;


import datamodels.DailyValues;
import util.GGCProperties;

import javax.swing.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;


public class DataBaseHandler
{
    static boolean connectedToDB = false;
    private static DataBaseHandler singleton = null;
    String dbName = null;

    public DataBaseHandler()
    {
        GGCProperties props = GGCProperties.getInstance();
        dbName = props.getDBName();
    }

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = MySQLHandler.getInstance();
        return singleton;
    }

    public ResultSet getDayStats(String day)
    {
        return null;
    }

    public void saveDayStats(DailyValues dV)
    {
    }

    //is overwritten
    public boolean dateTimeExists(Date date, Time time)
    {
        return true;
    }

    public void createNewDataBase(String name)
    {
    }

    public boolean isConnectedToDB()
    {
        return connectedToDB;
    }

    public void setDBName(String name)
    {
        if (name != null && !name.equals(""))
            dbName = name;
        else
            JOptionPane.showMessageDialog(null, "Invalid Name for a Database", "GGC Error - Invalid Name", JOptionPane.ERROR_MESSAGE);
    }

    public void connect()
    {
    }

    public void closeConnection()
    {
    }
}