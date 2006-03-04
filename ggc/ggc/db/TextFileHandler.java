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

package ggc.db;


import ggc.datamodels.DailyValues;
import ggc.datamodels.DailyValuesRow;
import ggc.datamodels.HbA1cValues;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class TextFileHandler extends DataBaseHandler
{
    private BufferedReader br;
    private BufferedWriter bw;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private File dataFile;

    private static TextFileHandler singleton = null;

    private TextFileHandler()
    {
	dbType = "TextFile:";
	
	File fl = new File(props.getTextFilePath());
	if  (fl.exists())
            dbName = fl.getName();
	else
	    dbName = m_ic.getMessage("NO_FILE_OPENED");

    }


    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = new TextFileHandler();
        return singleton;
    }

    public void disconnectDb()
    {
        connected = false;
	setStatus();
        //StatusBar.getInstance().setDataSourceText("TextFile: " + m_ic.getMessage("NO_CONNECTION"));
    }
/*
    public void closeDataBase()
    {
        connected = false;
	setStatus();
        //StatusBar.getInstance().setDataSourceText("TextFile: " + m_ic.getMessage("NO_FILE_OPENED"));
    }
  */
    public void connectDb()
    {
        
	if (!isInitialized())
	{
	    initDb();
	}
	
	connected = true;
	setStatus();
        //StatusBar.getInstance().setDataSourceText("TextFile: " + m_ic.getMessage("CONNECTED"));
        if (props.getTextFileOpenDefaultFile())
            openDataBase(false);
    }

    public void initDb()
    {
        dataFile = new File(props.getTextFilePath());
        try {
            if (!dataFile.exists())
                dataFile.createNewFile();
            connected = true;
	    setStatus();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean isInitialized()
    {
        File df = new File(props.getTextFilePath());
        return df.exists();
    }


    public boolean dateTimeExists(Date date)
    {
        if (!connected)
            return false;

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
        if (!connected)
            return null;

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

    public HbA1cValues getHbA1c(Date endDay)
    {
        if (!connected)
            return null;

        HbA1cValues hbVal = null;

        try {
            hbVal = new HbA1cValues();
            Date startDay = new Date(endDay.getTime() - 1000L * 60L * 60L * 24L * 90L);
            sdf.applyPattern("yyyy-MM-dd");

            br = new BufferedReader(new FileReader(dataFile));

            String s = br.readLine();

            while (s != null) {
                if (s.startsWith("<day")) {
                    String readDate = s.substring(4, 14);

                    System.out.println(readDate);

                    Date readDay = sdf.parse(readDate);

                    System.out.println(readDay);

                    if (readDay.after(startDay) && readDay.before(endDay)) {
                        float sumbg = 0;
                        int countbg = 0;
                        while (!(s = br.readLine()).equals("</day>")) {

                            int i = s.indexOf(';', 1);

                            float bg = new Float(s.substring(++i, i = s.indexOf(';', i))).floatValue();

                            System.out.println(bg + "");

                            if (bg > 0) {
                                sumbg += bg;
                                countbg++;
                            }
                        }
                        hbVal.addDay(sumbg / countbg, countbg);

                    }
                }
                s = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }


        return hbVal;
    }

    public void openDataBase(boolean ask)
    {
        File tmpFile = null;

        if (ask) {
            JFileChooser chooser = new JFileChooser();

            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION)
                tmpFile = chooser.getSelectedFile();
        } else
            tmpFile = new File(props.getTextFilePath());

        if (tmpFile != null) {
            if (!tmpFile.exists()) {
                int res = JOptionPane.showConfirmDialog(null, tmpFile.getAbsolutePath() + " " + m_ic.getMessage("DOES_NOT_EXIST_WANT_CREATE"), m_ic.getMessage("FILE_DOES_NOT_EXIST"), JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION)
                    try {
                        tmpFile.createNewFile();
                    } catch (IOException e) {
                        System.out.println(e);
                        connected = false;
                        return;
                    }
                else {
                    connected = false;
                    return;
                }
            }
            dataFile = tmpFile;
            connected = true;
            //StatusBar.getInstance().setDataSourceText("TextFile: " + dataFile.getName() + " " + m_ic.getMessage("OPENED"));
	    setStatus();
        } else
            connected = false;
    }

    public void saveDayStats(DailyValues dV)
    {
        if (!connected)
            return;

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
