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
import datamodels.HbA1cValues;
import util.GGCProperties;

import javax.swing.*;
import java.util.Date;


public abstract class DataBaseHandler
{
    static boolean connectedToDB = false;
    static boolean connected = false;

    private static DataBaseHandler singleton = null;
    GGCProperties props = GGCProperties.getInstance();
    String dbName;

    public DataBaseHandler()
    {
        dbName = props.getDBName();
    }

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = MySQLHandler.getInstance();
        return singleton;
    }

    public boolean isConnected()
    {
        return connected;
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

    public abstract DailyValues getDayStats(Date day);

    public abstract HbA1cValues getHbA1c(Date day);

    public abstract void saveDayStats(DailyValues dV);

    public abstract boolean dateTimeExists(java.util.Date date);

    public abstract void createNewDataBase(String name);

    public abstract void connect();

    public abstract void closeConnection();

    public abstract void openDataBase();

    public abstract void closeDataBase();
}