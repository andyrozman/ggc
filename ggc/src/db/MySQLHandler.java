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
import datamodels.DailyValuesRow;
import gui.StatusBar;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;


public class MySQLHandler extends DataBaseHandler
{
    Connection con = null;
    private static MySQLHandler singleton = null;

    private MySQLHandler()
    {
        super();
        connect();
    }

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = new MySQLHandler();
        return singleton;
    }

    public void connect()
    {
        try {
            if (con != null)
                con.close();
            Class.forName("org.gjt.mm.mysql.Driver");
            String sourceURL = "jdbc:mysql://" + props.getMySQLHost() + ":" + props.getMySQLPort() + "/?user=" + props.getMySQLUser() + "&password=" + props.getMySQLPass();
            con = DriverManager.getConnection(sourceURL);
            connected = true;
            StatusBar.getInstance().setDataSourceText("MySQL [" + props.getMySQLHost() + ":" + props.getMySQLPort() + "] no DataBase");
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    public void openDataBase()
    {
        try {
            con.createStatement().execute("use " + dbName);
            connectedToDB = true;
            StatusBar.getInstance().setDataSourceText("MySQL [" + props.getMySQLHost() + ":" + props.getMySQLPort() + "] DB:" + dbName);
        } catch (SQLException e) {
            System.out.println(e);
            connectedToDB = false;
        }
    }

    public void closeDataBase()
    {
        connectedToDB = false;
        StatusBar.getInstance().setDataSourceText("MySQL [" + props.getMySQLHost() + ":" + props.getMySQLPort() + "] no DataBase");
    }

    public DailyValues getDayStats(java.util.Date day)
    {
        ResultSet results = null;
        DailyValues dV = new DailyValues();
        float sumBG = 0;
        float sumIns1 = 0;
        float sumIns2 = 0;
        float sumBE = 0;

        int counterBG = 0;
        int counterBE = 0;
        int counterIns1 = 0;
        int counterIns2 = 0;

        float highestBG = 0;
        float lowestBG = Float.MAX_VALUE;
        float stdDev = 0;

        if (con != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sDay = sdf.format(day);
                String query = "SELECT * FROM DayValues WHERE date_format(datetime, '%Y-%m-%d') = '" + sDay + "' ORDER BY datetime";
                Statement statement = con.createStatement();
                results = statement.executeQuery(query);


                ResultSetMetaData metadata = results.getMetaData();
                int columns = metadata.getColumnCount();

                String[] columnNames = new String[columns];
                for (int i = 0; i < columns; i++)
                    columnNames[i] = metadata.getColumnLabel(i + 1);

                dV.setColumnNames(columnNames);

                Vector BGdata = new Vector();

                while (results.next()) {
                    sdf.applyPattern("yyyy-MM-dd HH:mm");
                    java.util.Date rDate = null;
                    try {
                        rDate = sdf.parse(results.getString(1));
                    } catch (ParseException e) {
                    } catch (SQLException e) {
                    }

                    //bg
                    float rBG = results.getFloat(2);
                    if (rBG != 0) {
                        sumBG += rBG;
                        BGdata.addElement(new Float(rBG));
                        counterBG++;
                        if (highestBG < rBG)
                            highestBG = rBG;
                        if (lowestBG > rBG)
                            lowestBG = rBG;
                    }
                    //ins1
                    float rIns1 = results.getFloat(3);
                    if (rIns1 != 0) {
                        sumIns1 += rIns1;
                        counterIns1++;
                    }

                    //ins2
                    float rIns2 = results.getFloat(4);
                    if (rIns2 != 0) {
                        sumIns2 += rIns2;
                        counterIns2++;
                    }

                    //be
                    float rBE = results.getFloat(5);
                    if (rBE != 0) {
                        sumBE += rBE;
                        counterBE++;
                    }

                    int rAct = results.getInt(6);
                    String rComment = results.getString(7);

                    DailyValuesRow dVR = new DailyValuesRow(rDate, rBG, rIns1, rIns2, rBE, rAct, rComment);
                    dV.setNewRow(dVR);
                }
                float avgBG = 0;
                if (counterBG != 0)
                    avgBG = sumBG / counterBG;

                float tmp = 0;
                for (int i = 0; i < BGdata.size(); i++)
                    tmp += Math.pow(((Float)(BGdata.get(i))).floatValue() - avgBG, 2.0);

                if (BGdata.size() > 1)
                    stdDev = (float)Math.sqrt(tmp / (BGdata.size() - 1));
                else
                    stdDev = 0;
            } catch (Exception e) {
                System.err.println(e);
            }
            dV.setDate(day);

            dV.setCounterBE(counterBE);
            dV.setCounterBG(counterBG);
            dV.setCounterIns1(counterIns1);
            dV.setCounterIns2(counterIns2);

            dV.setHighestBG(highestBG);
            dV.setLowestBG(lowestBG);
            dV.setStdDev(stdDev);
            dV.setSumBE(sumBE);
            dV.setSumBG(sumBG);
            dV.setSumIns1(sumIns1);
            dV.setSumIns2(sumIns2);
        }
        return dV;
    }

    public void saveDayStats(DailyValues dV)
    {
        Statement statement;
        try {
            if (!con.isClosed() && dV.hasChanged()) {
                statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                if (!dV.onlyInsert())
                    statement.execute("DELETE FROM DayValues where date_format(datetime, '%Y-%m-%d') = '" + dV.getDate().toString() + "'");

                ResultSet rs = statement.executeQuery("SELECT * from DayValues limit 1");
                for (int i = 0; i < dV.getRowCount(); i++) {
                    rs.moveToInsertRow();
                    rs.updateString(1, dV.getDateTimeAsStringAt(i));
                    rs.updateFloat(2, dV.getBGAt(i));
                    rs.updateFloat(3, dV.getIns1At(i));
                    rs.updateFloat(4, dV.getIns2At(i));
                    rs.updateFloat(5, dV.getBUAt(i));
                    rs.updateInt(6, dV.getActAt(i));
                    rs.updateString(7, dV.getCommentAt(i));
                    rs.insertRow();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public boolean dateTimeExists(java.util.Date date)
    {
        Statement statement;
        try {
            if (!con.isClosed()) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String sDate = sf.format(date);
                statement = con.createStatement();
                String tmp = new String("SELECT * FROM DayValues where datetime = '" + sDate + "'");
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
            String query2 = "CREATE TABLE DayValues( " + "datetime datetime NOT NULL default '0000-00-00 00:00:00', bg decimal(7,2) unsigned NOT NULL default '0.00', ins1 decimal(6,2) unsigned NOT NULL default '0.00', ins2 decimal(6,2) unsigned NOT NULL default '0.00', bu decimal(6,2) unsigned NOT NULL default '0.00', act tinyint(3) unsigned NOT NULL default '0', comment varchar(255) NOT NULL default '', PRIMARY KEY (datetime)";
            statement.execute(query2);
            dbName = name;
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    public void closeConnection()
    {
        try {
            con.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        } finally {
            connected = false;
            connectedToDB = false;
            StatusBar.getInstance().setDataSourceText("MySQL [no Connection]");
        }
    }
}
