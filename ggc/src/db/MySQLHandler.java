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
 *  Filename: MySQLHandler.java
 *  Purpose:  Handler to access a MySQL DataBase.
 *
 *  Author:   schultd
 */

package db;


import datamodels.DailyValues;

import java.sql.*;
import java.text.SimpleDateFormat;


public class MySQLHandler extends DataBaseHandler
{
    Connection con = null;
    private static MySQLHandler singleton = null;

    private MySQLHandler()
    {
        super();
        connect();
    }

    public void connect()
    {
        try {
            if (con != null)
                con.close();
            Class.forName("org.gjt.mm.mysql.Driver");
            String sourceURL = "jdbc:mysql://localhost/" + dbName + "?user=testapp&password=gluco";
            con = DriverManager.getConnection(sourceURL);
            if (!dbName.equals(""))
                connectedToDB = true;
            else
                connectedToDB = false;
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = new MySQLHandler();
        return singleton;
    }

    public ResultSet getDayStats(String day)
    {
        ResultSet results = null;
        try {
            String query = "SELECT * FROM DayValues WHERE date = '" + day + "' ORDER BY date, time";
            Statement statement = con.createStatement();
            results = statement.executeQuery(query);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
        return results;
    }

    public void saveDayStats(DailyValues dV)
    {
        Statement statement;
        try {
            if (!con.isClosed() && dV.hasChanged()) {
                statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                //System.out.println("lala: " + dV.getDate().toString());
                if (!dV.onlyInsert())
                    statement.execute("DELETE FROM DayValues where date = '" + dV.getDate().toString() + "'");

                ResultSet rs = statement.executeQuery("SELECT * from DayValues limit 1");
                for (int i = 0; i < dV.getRowCount(); i++) {
                    rs.moveToInsertRow();
                    rs.updateDate(1, dV.getDate());
                    rs.updateTime(2, dV.getTimeAt(i));
                    rs.updateFloat(3, dV.getBGAt(i));
                    rs.updateFloat(4, dV.getIns1At(i));
                    rs.updateFloat(5, dV.getIns2At(i));
                    rs.updateFloat(6, dV.getBUAt(i));
                    rs.updateInt(7, dV.getActAt(i));
                    rs.updateString(8, dV.getCommentAt(i));
                    rs.insertRow();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public boolean dateTimeExists(Date date, Time time)
    {
        Statement statement;
        try {
            if (!con.isClosed()) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                String sDate = sf.format(date);
                sf.applyPattern("HH:mm");
                String sTime = sf.format(time);
                statement = con.createStatement();
                String tmp = new String("SELECT * FROM DayValues where date = '" + sDate + "' and time = '" + sTime + "'");
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

    public void createNewDataBase(String name)
    {
        try {
            Statement statement;
            if (!con.isClosed()) {
                String query1 = "CREATE DATABASE IF NOT EXISTS " + name;
                statement = con.createStatement();
                statement.execute(query1);
                con.close();
            }
            String sourceURL = "jdbc:mysql://localhost/" + name + "?user=testapp&password=gluco";
            con = DriverManager.getConnection(sourceURL);
            statement = con.createStatement();
            String query2 = "CREATE TABLE DayValues( " + "date date NOT NULL default '0000-00-00', " + "time time NOT NULL default '00:00:00', " + "bg decimal(7,2) unsigned NOT NULL default '0.00', " + "ins1 decimal(6,2) unsigned NOT NULL default '0.00', " + "ins2 decimal(6,2) unsigned NOT NULL default '0.00', " + "be decimal(6,2) unsigned NOT NULL default '0.00', " + "act tinyint(3) unsigned NOT NULL default '0', " + "comment varchar(255) NOT NULL default '', " + "PRIMARY KEY (date,time), " + "KEY date (date))";
            statement.execute(query2);
            dbName = name;
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    public void closeConnection()
    {
        dbName = "";
        try {
            con.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        } finally {
            connectedToDB = false;
        }
    }
}
