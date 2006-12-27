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

package ggc.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.*;

import ggc.db.hibernate.DayValueH;
//import ggc.errors.DateTimeError;
import ggc.util.DataAccess;
import ggc.util.I18nControl;


public class DailyValuesRow implements Serializable
{

    private I18nControl m_ic = I18nControl.getInstance();

    /*
    private Date datetime;
    private Float BG;
    private Float Ins1;
    private Float Ins2;
    private Float BE;
    private Integer Act;
    private String Comment;
    */

    private long datetime;
    private float bg;
    private float ins1;
    private float ins2;
    private float ch;
    private int act;
    private String comment;

    private boolean changed = false;

    DayValueH m_dv = null;
    DataAccess m_da = DataAccess.getInstance();



    public DailyValuesRow(long datetime, float bg, float ins1, float ins2, float ch, int act, String comment)
    {
        this.datetime = datetime;
        this.bg = bg; 
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.act = act;
        this.comment = comment;
    }

    public DailyValuesRow(DayValueH dv)
    {
    	this.datetime = dv.getDt_info();
    	this.bg = dv.getBg();
    	this.ins1 = dv.getIns1();
    	this.ins2 = dv.getIns2();
    	this.ch = dv.getCh();
    	this.act = dv.getAct();
    	this.comment = dv.getComment();
    
    	m_dv = dv;
    }





    public DailyValuesRow(String date, String time, String BG, String Ins1, String Ins2, String BU, String Act, String Comment)
    {
    	datetime = getDateTimeLong(date, time);
    
    	setValueAt(BG, 1);
    	setValueAt(Ins1, 2);
    	setValueAt(Ins2, 3);
    	setValueAt(BU, 4);
    	setValueAt(Act, 5);
    	setValueAt(Comment, 6);
    }


    public long getDateTimeLong(String date, String time)
    {
        StringTokenizer strtok = new StringTokenizer(date, ".");
        String dt[] = new String[5];
        dt[2] = strtok.nextToken();  // day
        dt[1] = strtok.nextToken();  // month
        dt[0] = strtok.nextToken();  // year

        strtok = new StringTokenizer(time, ":");
        dt[3] = strtok.nextToken();  // hour
        dt[4] = strtok.nextToken();  // minute

        String dt_out = dt[0] + m_da.getLeadingZero(dt[1], 2) + m_da.getLeadingZero(dt[2], 2) + m_da.getLeadingZero(dt[3], 2) + m_da.getLeadingZero(dt[4], 2);

        return Long.parseLong(dt_out);

    }

/*
    public DailyValuesRow(String date, String time, String BG, String Ins1, String Ins2, String BU, String Act, String Comment)
    {

	/*
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            try {
                df.parse(date);
            } 
            catch (ParseException e) {
                JOptionPane.showMessageDialog(null, m_ic.getMessage("DATE_MUST_BE_IN_FORMAT") + ": 'dd.MM.yyyy'", m_ic.getMessage("ERROR_PARSING_DATE"), JOptionPane.ERROR_MESSAGE);
                throw new DateTimeError("Incorrectly formatted date!");
            }
                
            try {
                df.applyPattern("dd.MM.yyyy HH:mm");
                this.datetime = new Date(df.parse(date+" "+time).getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, m_ic.getMessage("TIME_MUST_BE_IN_FORMAT") + ": 'HH:mm'", m_ic.getMessage("ERROR_PARSING_DATE"), JOptionPane.ERROR_MESSAGE);
                throw new DateTimeError("Incorrectly formatted time!");
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

	    System.out.println(this.toString());

        } catch (Exception e) {
            System.err.println(e);
        }
	*/
//    }

    public DailyValuesRow()
    {
    	this.datetime = 0L;
    	this.bg = 0.0f;
    	this.ins1 = 0.0f;
    	this.ins2 = 0.0f;
    	this.ch = 0.0f;
    	this.act = 0;
    	this.comment = "";
    }

    public String[] getRowString()
    {
        String[] tmp = new String[7];
        tmp[0] = "" + datetime;
        tmp[1] = "" + bg;
        tmp[2] = "" + ins1;
        tmp[3] = "" + ins2;
        tmp[4] = "" + ch;
        tmp[5] = "" + act;
        tmp[6] = comment;

        return tmp;
    }

    @Override
    public String toString()
    {
//        return "DT="+datetime.getTime() + ";BG=" + BG + ";Ins1=" + Ins1 + ";Ins2=" + Ins2 + ";BE=" + BE + ";Act=" + Act + ";Comment=" + Comment + ";";
        return getDateTime() + ";" + bg + ";" + ins1 + ";" + ins2 + ";" + ch + ";" + act + ";" + comment + ";";
    }


    public long getDateTime()
    {
	//System.out.println(datetime);
        return datetime;
    }


    public void setDateTime(long dt)
    {
	this.datetime = dt;
    }

    public Date getDateTimeAsDate()
    {
        return m_da.getDateTimeAsDateObject(datetime);
    }


    public long getDate()
    {
        return (datetime/10000);
    }



    public String getDateAsString()
    {
        return m_da.getDateTimeAsDateString(datetime);
//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //return sdf.format(datetime);
	//dssreturn m_da.getDateTimeAsDateString(datetime);
    }

    public String getTimeAsString()
    {
        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //return sdf.format(datetime);
        return m_da.getDateTimeAsTimeString(datetime);
    }

    public String getDateTimeAsTime()
    {
        long i = m_da.getDateTimeLong(datetime, DataAccess.DT_TIME);
        //System.out.println("X: " + i);
        return "" + i;
    }

    public void setDateTime(String date, String time)
    {
        datetime = getDateTimeLong(date, time);
    }


    public float getBG()
    {
        return bg;
    }

    public void setBG(String val)
    {
        setBG(m_da.getFloatValue(val));
    }


    public static int BG_MMOLL = 1;
    public static int BG_MGDL = 2;

    public void setBG(int type, float val)
    {
	// FIX THIS

    }

    public void setBG(int type, String val)
    {
	// FIX THIS

    }



    public void setBG(float val)
    {
        if (bg!=val) 
        {
            bg = val;
            changed = true;
        }
    }


    public float getIns1()
    {
        return ins1;
    }

    public void setIns1(String val)
    {
        setIns1(m_da.getFloatValue(val));
    }

    public void setIns1(float val)
    {
        if (ins1!=val) 
        {
            ins1 = val;
            changed = true;
        }
    }


    public float getIns2()
    {
        return ins2;
    }

    public void setIns2(String val)
    {
        setIns2(m_da.getFloatValue(val));
    }

    public void setIns2(float val)
    {
        if (ins2!=val) 
        {
            ins2 = val;
            changed = true;
        }
    }


    public float getCH()
    {
        return ch;
    }


    public void setCH(String val)
    {
        setCH(m_da.getFloatValue(val));
    }

    public void setCH(float val)
    {
        if (ch!=val) 
        {
            ch = val;
            changed = true;
        }
    }


    public int getAct()
    {
        return act;
    }

    public void setAct(String val)
    {
        act = m_da.getIntValue(val);
    }

    public void setAct(int val)
    {
        if (act!=val) 
        {
            act = val;
            changed = true;
        }
    }



    public String getComment()
    {
        return comment;
    }

    public void setComment(String val)
    {
        if (!comment.equals(val)) 
        {
            comment = val;
            changed = true;
        }
    }




    public boolean hasChanged()
    {
        return changed;
    }

    public boolean isNew()
    {
        if (m_dv==null)
            return true;
        else
            return false;
    }

    public DayValueH getHibernateObject()
    {
        if (m_dv==null)
        {
            m_dv = new DayValueH();
            m_dv.setAct(act);
            m_dv.setBg(bg);
            m_dv.setCh(ch);
            m_dv.setComment(comment);
            m_dv.setDt_info(datetime);
            m_dv.setIns1(ins1);
            m_dv.setIns2(ins2);
//	    m_dv.setMeals_ids("");
        }
        else
        {
            m_dv.setAct(act);
            m_dv.setBg(bg);
            m_dv.setCh(ch);
            m_dv.setComment(comment);
            m_dv.setDt_info(datetime);
            m_dv.setIns1(ins1);
            m_dv.setIns2(ins2);
        }

        return m_dv;

    }

    public boolean hasHibernateObject()
    {
        return (m_dv!=null);
    }



    //stupid but TableModel needs Objects...
    public Object getValueAt(int column)
    {
        switch (column) 
        {
            case 0:
                return ""+datetime; //m_da.getDateTimeAsTimeString(datetime);
            case 1:
                return ""+bg;
            case 2:
                return ""+ins1;
            case 3:
                return ""+ins2;
            case 4:
                return ""+ch;
            case 5:
                return ""+act;
            case 6:
                return comment;
            default:
                return null;
        }
    }


    public void setValueAt(Object aValue, int column)
    {
        switch (column) 
        {
    	    case 0:
    		{
    		    if (aValue instanceof String)
    			datetime = Long.parseLong((String)aValue);
    		    else if (aValue instanceof Integer)
    			datetime = ((Integer)aValue).intValue();
    		    else if (aValue instanceof Long)
    			datetime = ((Long)aValue).longValue();
    		    changed = true;
    		} break;
    	    case 1:
    		{
    		    bg = m_da.getFloatValue(aValue);
    		    if (bg!=0.0f)
    			changed = true;
    		} break;
            case 2:
    		{
    		    ins1 = m_da.getFloatValue(aValue);
    		    if (ins1!=0.0f)
    			changed = true;
    		} break;
            case 3:
    		{
    		    ins2 = m_da.getFloatValue(aValue);
    		    if (ins2!=0.0f)
    			changed = true;
    		} break;
            case 4:
    		{
    		    ch = m_da.getFloatValue(aValue);
    		    if (ch!=0.0f)
    			changed = true;
    		    changed = true;
    		} break;
            case 5:
    		{
    		    act = m_da.getIntValue(aValue);
    		    if (act!=0)
    			changed = true;
    		} break;
            case 6:
    		{
    		    if (aValue instanceof String)
    			comment = (String)aValue;
    		    if (comment.length()!=0)
    			changed = true;
    		} break;
        }
    }






    public int getDateD()
    {
        return Integer.parseInt(m_da.getDateTimeAsDateString(datetime));
	//return -1;
/*
	String dat = "";

	dat += datetime.getYear() + 1900;
	dat += getLeadingZero(datetime.getMonth() + 1, 2);
	dat += getLeadingZero(datetime.getDate(), 2);

	return Integer.parseInt(dat);
*/
    }


    public int getDateT()
    {
        return Integer.parseInt(m_da.getDateTimeAsTimeString(datetime));
	//return -1;
	//return datetime.getHours()*100 + datetime.getMinutes();
    }


}
