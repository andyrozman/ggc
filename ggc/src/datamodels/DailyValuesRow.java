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
 *  Filename: DailyValuesRow.java
 *  Purpose:  One row in the DailyValues Data Model.
 *
 *  Author:   schultd
 */

package datamodels;


import javax.swing.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DailyValuesRow
{
    private Date date;
    private Time time;
    private Float BG;
    private Float Ins1;
    private Float Ins2;
    private Float BE;
    private Integer Act;
    private String Comment;

    public DailyValuesRow(Date date, Time time, float BG, float Ins1, float Ins2, float BE, int Act, String Comment)
    {
        this.date = date;
        this.time = time;
        this.BG = new Float(BG);
        this.Ins1 = new Float(Ins1);
        this.Ins2 = new Float(Ins2);
        this.BE = new Float(BE);
        this.Act = new Integer(Act);
        this.Comment = Comment;
    }

    public DailyValuesRow(String date, String time, String BG, String Ins1, String Ins2, String BU, String Act, String Comment)
    {

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            try {
                this.date = new Date(df.parse(date).getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Date must be in form: 'dd.MM.yyyy'", "Error Parsing Date", JOptionPane.ERROR_MESSAGE);
                this.date = null;
            }
            df = new SimpleDateFormat("HH:mm");
            try {
                this.time = new Time(df.parse(time).getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Time must be in form: 'HH:mm'", "Error Parsing Time", JOptionPane.ERROR_MESSAGE);
                this.time = null;
            }
            if (BG.trim().equals(""))
                this.BG = new Float(0);
            else
                this.BG = new Float(BG);

            if (Ins1.trim().equals(""))
                this.Ins1 = new Float(0);
            else
                this.Ins1 = new Float(Ins1);

            if (Ins2.trim().equals(""))
                this.Ins2 = new Float(0);
            else
                this.Ins2 = new Float(Ins2);

            if (BU.trim().equals(""))
                this.BE = new Float(0);
            else
                this.BE = new Float(BU);

            if (Act.trim().equals(""))
                this.Act = new Integer(0);
            else
                this.Act = new Integer(Act);

            this.Comment = Comment;

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public DailyValuesRow()
    {
        this.time = null;
        this.BG = null;
        this.Ins1 = null;
        this.Ins2 = null;
        this.BE = null;
        this.Act = null;
        this.Comment = null;
    }

    public String[] getRowString()
    {
        String[] tmp = new String[8];
        tmp[0] = date.toString();
        tmp[1] = time.toString();
        tmp[2] = BG.toString();
        tmp[3] = Ins1.toString();
        tmp[4] = Ins2.toString();
        tmp[5] = BE.toString();
        tmp[6] = Act.toString();
        tmp[7] = Comment;

        return tmp;
    }

    public Date getDate()
    {
        return date;
    }

    public Time getTime()
    {
        return time;
    }

    public float getBG()
    {
        return BG.floatValue();
    }

    public float getIns1()
    {
        return Ins1.floatValue();
    }

    public float getIns2()
    {
        return Ins2.floatValue();
    }

    public float getBE()
    {
        return BE.floatValue();
    }

    public int getAct()
    {
        return Act.intValue();
    }

    public String getComment()
    {
        return Comment;
    }


    //stupid but TableModel needs Objects...
    public Object getValueAt(int column)
    {
        switch (column) {
            case 0:
                return date;
            case 1:
                return time;
            case 2:
                return BG;
            case 3:
                return Ins1;
            case 4:
                return Ins2;
            case 5:
                return BE;
            case 6:
                return Act;
            case 7:
                return Comment;
            default:
                return null;
        }
    }

    public void setValueAt(Object aValue, int column)
    {
        switch (column) {
            case 0:
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    long tmp = sdf.parse(aValue.toString()).getTime();
                    date = new Date(tmp);
                } catch (Exception e) {
                    System.err.println(e);
                }
                break;
            case 1:
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    long tmp = sdf.parse(aValue.toString()).getTime();
                    time = new Time(tmp);
                } catch (Exception e) {
                    System.err.println(e);
                }
                break;
            case 2:
                BG = (Float)aValue;
                break;
            case 3:
                Ins1 = (Float)aValue;
                break;
            case 4:
                Ins2 = (Float)aValue;
                break;
            case 5:
                BE = (Float)aValue;
                break;
            case 6:
                Act = (Integer)aValue;
                break;
            case 7:
                Comment = (String)aValue;
                break;
        }
    }
}
