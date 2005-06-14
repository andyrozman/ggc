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

package ggc.datamodels;

import ggc.util.I18nControl;


import javax.swing.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DailyValuesRow implements Serializable
{

    private I18nControl m_ic = I18nControl.getInstance();

    private Date datetime;
    private Float BG;
    private Float Ins1;
    private Float Ins2;
    private Float BE;
    private Integer Act;
    private String Comment;

    public DailyValuesRow(Date datetime, float BG, float Ins1, float Ins2, float BE, int Act, String Comment)
    {
        this.datetime = datetime;
        this.BG = new Float(BG);
        this.Ins1 = new Float(Ins1);
        this.Ins2 = new Float(Ins2);
        this.BE = new Float(BE);
        this.Act = new Integer(Act);
        this.Comment = Comment;
    }

    public DailyValuesRow(String datetime, String BG, String Ins1, String Ins2, String BU, String Act, String Comment)
    {

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            try {
                this.datetime = new Date(df.parse(datetime).getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, m_ic.getMessage("DATE_MUST_BE_IN_FORMAT") + ": 'dd.MM.yyyy'", m_ic.getMessage("ERROR_PARSING_DATE"), JOptionPane.ERROR_MESSAGE);
                this.datetime = null;
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
        this.datetime = null;
        this.BG = null;
        this.Ins1 = null;
        this.Ins2 = null;
        this.BE = null;
        this.Act = null;
        this.Comment = null;
    }

    public String[] getRowString()
    {
        String[] tmp = new String[7];
        tmp[0] = datetime.toString();
        tmp[1] = BG.toString();
        tmp[2] = Ins1.toString();
        tmp[3] = Ins2.toString();
        tmp[4] = BE.toString();
        tmp[5] = Act.toString();
        tmp[6] = Comment;

        return tmp;
    }

    public String toString()
    {
        return datetime.getTime() + ";" + BG + ";" + Ins1 + ";" + Ins2 + ";" + BE + ";" + Act + ";" + Comment + ";";
    }

    public Date getDateTime()
    {
        return datetime;
    }


    public String getDateAsString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(datetime);
    }

    public String getTimeAsString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(datetime);
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
                return datetime;
            case 1:
                return BG;
            case 2:
                return Ins1;
            case 3:
                return Ins2;
            case 4:
                return BE;
            case 5:
                return Act;
            case 6:
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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:MM");
                    long tmp = sdf.parse(aValue.toString()).getTime();
                    datetime = new Date(tmp);
                } catch (Exception e) {
                    System.err.println(e);
                }
                break;
            case 1:
                BG = (Float)aValue;
                break;
            case 2:
                Ins1 = (Float)aValue;
                break;
            case 3:
                Ins2 = (Float)aValue;
                break;
            case 4:
                BE = (Float)aValue;
                break;
            case 5:
                Act = (Integer)aValue;
                break;
            case 6:
                Comment = (String)aValue;
                break;
        }
    }



    public int getDateD()
    {
	
	String dat = "";

	dat += datetime.getYear() + 1900;

	dat += getLeadingZero(datetime.getMonth() + 1, 2);

	dat += getLeadingZero(datetime.getDate(), 2);


	return Integer.parseInt(dat);

    }


    public int getDateT()
    {
	
	return datetime.getHours()*100 + datetime.getMinutes();
	
    }



    public String getLeadingZero(int value, int num_places)
    {

	String tmp = "" + value;

	while (tmp.length()<num_places)
	{
	    tmp = "0" + tmp; 
	}

	return tmp;

    }


}
