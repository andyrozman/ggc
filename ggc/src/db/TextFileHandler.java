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
 *  Filename: TextFileHandler.java
 *  Purpose:  Use a TextFile as DataSource
 *
 *  Author:   schultd
 */

package db;


import datamodels.DailyValues;
import datamodels.DailyValuesRow;
import datamodels.HbA1cValues;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TextFileHandler extends DataBaseHandler
{
    private BufferedReader br;
    private BufferedWriter bw;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private File dataFile;

    private static TextFileHandler singleton = null;

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = new TextFileHandler();
        return singleton;
    }

    public void closeConnection()
    {
        connected = false;
    }

    public void closeDataBase()
    {
        connectedToDB = false;
    }

    public void connect()
    {
        connected = true;
    }

    public void createNewDataBase(String name)
    {
        dataFile = new File(name);
        try {
            if (!dataFile.exists())
                dataFile.createNewFile();
            connectedToDB = true;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean dateTimeExists(Date date)
    {

        try {
            BufferedReader br = new BufferedReader(new FileReader("test.tmp"));
            String datetime = date.getTime() + "";

            String s = br.readLine();
            while (s != null) {
                if (s.startsWith(datetime)) {
                    br.close();
                    return true;
                }
                s = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        return false;
    }

    public DailyValues getDayStats(Date day)
    {
        DailyValues dV = new DailyValues();

        try {
            sdf.applyPattern("yyyy-MM-dd");
            String sday = "<day" + sdf.format(day) + ">";
            br = new BufferedReader(new FileReader(dataFile));
            String s = br.readLine();

            while (s != null) {
                if (s.equals(sday)) {
                    while (!(s = br.readLine()).equals("</day>")) {

                        int i = s.indexOf(';', 1);

                        sdf.applyPattern("dd.MM.yyyy HH:mm");

                        String datetime = sdf.format(new Date(new Long(s.substring(0, i)).longValue()));
                        String bg = s.substring(++i, i = s.indexOf(';', i));
                        String ins1 = s.substring(++i, i = s.indexOf(';', i));
                        String ins2 = s.substring(++i, i = s.indexOf(';', i));
                        String bu = s.substring(++i, i = s.indexOf(';', i));
                        String act = s.substring(++i, i = s.indexOf(';', i));
                        String com = s.substring(++i, i = s.indexOf(';', i));

                        dV.setNewRow(new DailyValuesRow(datetime, bg, ins1, ins2, bu, act, com));
                    }
                    break;
                }
                s = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        dV.setDate(day);
        return dV;
    }

    public HbA1cValues getHbA1c(Date day)
    {
        return null;
    }

    public void openDataBase()
    {
        JFileChooser chooser = new JFileChooser();

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dataFile = chooser.getSelectedFile();
            connectedToDB = true;
            return;
        }

        connectedToDB = false;
    }

    public void saveDayStats(DailyValues dV)
    {
        try {
            //write new day
            File tmpFile = new File("test2.tmp");

            BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));
            sdf.applyPattern("yyyy-MM-dd");

            String newHeader = "<day" + sdf.format(dV.getDate()) + ">";
            bw.write(newHeader + "\n");

            for (int i = 0; i < dV.getRowCount(); i++)
                bw.write(dV.getRowAt(i) + "\n");

            bw.write("</day>\n");
            bw.flush();

            //write other days
            br = new BufferedReader(new FileReader(dataFile));

            String s = br.readLine();

            while (s != null) {
                if (!s.equals(newHeader))
                    bw.write(s + "\n");
                else
                    while (!(s = br.readLine()).equals("</day>")) {
                    }

                s = br.readLine();
            }

            bw.flush();
            bw.close();

            tmpFile.renameTo(dataFile);

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
