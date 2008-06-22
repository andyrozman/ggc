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

package ggc.meter.data;

//import ggc.db.hibernate.DayValueH;
import ggc.meter.util.DataAccessMeter;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DailyValuesRow //implements Serializable
{

    private static Log log = LogFactory.getLog(DailyValuesRow.class); 

    //private I18nControl m_ic = I18nControl.getInstance();
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
    private int bg;
    private int ins1;
    private int ins2;
    private float ch;
    private String activity;
    private String comment;
    private String extended;
    private String urine;
    private String meals;

    private boolean changed = false;

//x    DayValueH m_dv = null;
    DataAccessMeter m_da = DataAccessMeter.getInstance();
 //x   GGCProperties props = m_da.getSettings();

    ArrayList<String> meals_ids;

    boolean debug = false;

    Float float_instance = new Float(0.0);


    public DailyValuesRow()
    {
    	this.datetime = 0L;
    	this.bg = 0;
    	this.ins1 = 0;
    	this.ins2 = 0;
    	this.ch = 0.0f;
    	this.extended = null;
    	this.comment = "";
    	this.meals = "";

        loadExtended();
    }



    public DailyValuesRow(long datetime, int bg, int ins1, int ins2, float ch, String extended, String comment)
    {
        this.datetime = datetime;
        this.bg = bg; 
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.extended = extended;
        this.comment = comment;
    	this.meals = "";

        loadExtended();
    }


    public DailyValuesRow(long datetime, int bg, int ins1, int ins2, float ch, String activity, String urine, String comment)
    {
        this.datetime = datetime;
        this.bg = bg; 
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.activity = activity;
        this.urine = urine;
        this.comment = comment;
    }


/*
    public DailyValuesRow(DayValueH dv)
    {
    	this.datetime = dv.getDt_info();
    	this.bg = dv.getBg();
    	this.ins1 = dv.getIns1();
    	this.ins2 = dv.getIns2();
    	this.ch = dv.getCh();
        this.meals = dv.getMeals_ids();
        this.extended = dv.getExtended();
    	this.comment = dv.getComment();
    
    	m_dv = dv;
    }
*/
    
    /*
    public DailyValuesRow(long datetime, String BG, String Ins1, String Ins2, String BU, String Activity, String urine, String Comment, ArrayList<String> lst_meals)
    {
        // XX
        this(date,time,BG,Ins1,Ins2,BU,Act,null,Comment,false);
    }*/



    public DailyValuesRow(long datetime, String BG, String Ins1, String Ins2, String BU, String Act, String urine, String Comment, ArrayList<String> lst_meals)
    {
        //X
    	this.datetime = datetime;
    
    	setValueAt(BG, 1);
    	setValueAt(Ins1, 2);
    	setValueAt(Ins2, 3);
    	setValueAt(BU, 4);
    	setValueAt(Act, 5);
        setValueAt(urine, 6);
    	setValueAt(Comment, 7);

    	this.meals_ids = lst_meals;
    	//this.meals = "";

    }

    public void loadExtended()
    {
        if ((this.extended==null) || (this.extended.trim().length()==0))
        {
            this.activity = "";
            this.urine = "";
        }
        else
        {
            this.urine = this.getExtendedEntry("URINE");
            this.activity = this.getExtendedEntry("ACTIVITY");
        }
    }


    public String createExtended()
    {
        StringBuffer sb = new StringBuffer();

        if (this.isExtendedValueSet(DailyValuesRow.EXTENDED_URINE))
        {
            sb.append("URINE=" + getExtendedValue(DailyValuesRow.EXTENDED_URINE) + ";");
        }

        if (this.isExtendedValueSet(DailyValuesRow.EXTENDED_ACTIVITY))
        {
            sb.append("ACTIVITY=" + getExtendedValue(DailyValuesRow.EXTENDED_ACTIVITY) + ";");
        }

        return sb.toString();
    }

    public static final int EXTENDED_ACTIVITY = 1;
    public static final int EXTENDED_URINE = 2;


    private boolean isExtendedValueSet(int type)
    {
        String val = getExtendedValue(type);

        if ((val==null) || (val.trim().length()==0))
            return false;
        else
            return true;
    }


    public String getExtendedValue(int type)
    {
        switch(type)
        {
            case DailyValuesRow.EXTENDED_URINE:
                {
                    return this.urine;
                } 

            default:
            case DailyValuesRow.EXTENDED_ACTIVITY:
                {
                    return this.activity;
                } 
        }
    }



    public String getExtendedEntry(String key)
    {
        if (this.extended.contains(key + "="))
        {
            String val = this.extended.substring(this.extended.indexOf(key +"=") + key.length() +1);
            if (val.contains(";"))
            {
                val = val.substring(0, val.indexOf(";"));
            }

            return val;
        }
        else
            return "";

    }


    public void loadMealsIds()
    {
    	// private
        // to-do
        if ((this.meals!=null) && (this.meals.trim().length()>0))
        {
            meals_ids = new ArrayList<String>();


        }
    }

    public String createMealsIds()
    {
    	// ptivate
        if ((this.meals_ids==null) || (this.meals_ids.size()==0))
        {
            return "";
        }
        else
        {
            // TO-DO
            return "";
        }
    }

    public ArrayList<String> getMealIdsList()
    {
        return this.meals_ids;
    }

    public void setMealIdsList(ArrayList<String> lst)
    {
        this.meals_ids = lst;
    }



    public boolean areMealsSet()
    {
        if ((this.meals_ids==null) || (this.meals_ids.size()==0))
            return false;
        else
            return true;
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


    public String[] getRowString()
    {
        // to-do
        String[] tmp = new String[8];
        tmp[0] = "" + datetime;
        tmp[1] = "" + bg;
        tmp[2] = "" + ins1;
        tmp[3] = "" + ins2;
        tmp[4] = "" + ch;
        tmp[5] = "" + activity;
        tmp[6] = "" + urine;
        tmp[7] = comment;

        return tmp;
    }

    @Override
    public String toString()
    {
//        return "DT="+datetime.getTime() + ";BG=" + BG + ";Ins1=" + Ins1 + ";Ins2=" + Ins2 + ";BE=" + BE + ";Act=" + Act + ";Comment=" + Comment + ";";
        return "DailyValuesRow [dt=" + getDateTime() + ";bg=" + bg + ";ins1=" + ins1 + ";ins2=" + ins2 + ";CH=" + ch + ";activity=" + activity + ";urine=" + urine + ";comment=" + comment + "]";
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
        long i = m_da.getDateTimeLong(datetime, DataAccessMeter.DT_TIME);
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
    	/*
        if (debug)
        {
            System.out.println("getBg [type=" + props.getBG_unit() + "]");
            System.out.println("getBg [internal_value=" + this.bg + "]");
        }

        if (props.getBG_unit()==2)
        {
            float v = m_da.getBGValueByType(DataAccessMeter.BG_MMOL, bg);

            if (debug)
                System.out.println("getBg [typle=2,return=" + v + "]");

            return v;

            //return m_da.getBGValueByType(DataAccessMeter.BG_MMOL, bg);
        }
        else
        {
            if (debug)
                System.out.println("getBg [type=1,return=" + this.bg + "]");

            return bg;
        }
*/
    }


    public String getBGAsString()
    {
    	return null;
  /*
    	if (debug)
        {
            System.out.println("getBgAsString [type=" + props.getBG_unit() + "]");
            System.out.println("getBgAsString [internal_value=" + this.bg + "]");
        }

        if (props.getBG_unit()==2)
        {
            float v = m_da.getBGValueByType(DataAccessMeter.BG_MMOL, bg);

            if (debug)
                System.out.println("getBgAsString [type=2,return=" + v + "]");

            String ss = DataAccessMeter.MmolDecimalFormat.format(v);
            ss = ss.replace(",", ".");
            return ss;

            //return m_da.getBGValueByType(DataAccessMeter.BG_MMOL, bg);
        }
        else
        {
            if (debug)
                System.out.println("getBgAsString [type=1,return=" + this.bg + "]");

            return "" + bg;
            
        }*/

    }



    public float getBRaw()
    {
        return bg;

    }


    public float getBG(int type)
    {

        if (debug)
            System.out.println("Intenal value: " + this.bg);

        if (type==DataAccessMeter.BG_MGDL)
            return bg;
        else
        {
            return m_da.getBGValueByType(DataAccessMeter.BG_MMOL, bg);
        }

    }





/*
    public void setBG(String val)
    {
        setBG(m_da.getIntValue(val));
    }
*/

    public static int BG_MMOLL = 2;
    public static int BG_MGDL = 1;

    public void setBG(int type, float val)
    {
        // FIX THIS

        if (debug)
            System.out.println("SET BG: type=" + type + ",value=" + val);


        if (type==BG_MMOLL)
        {
            if (debug)
            {
                System.out.println("SET BG :: MMOL");
                System.out.println("     " + (int)m_da.getBGValueDifferent(type, val));
            }

            this.setBG((int)m_da.getBGValueDifferent(type, val));
        }
        else
        {
            this.setBG((int)val);
        }
        

/*

        int unit = getBG_unit();

        if (unit==1) 
            return "mg/dl";
        else if (unit==2) 
            return "mmol/l";
        else
            return m_da.getI18nInstance().getMessage("UNKNOWN");
*/

    }

    public void setBG(int type, String val)
    {
	// FIX THIS
        float f = Float.parseFloat(val);

        setBG(type,f);

    }



    public void setBG(int val)
    {
        if (bg!=val) 
        {
            bg = val;
            changed = true;
        }

        if (debug)
            System.out.println("Bg Raw: " + bg);
    }


    public float getIns1()
    {
        return ins1;
    }

    public String getIns1AsString()
    {
        return this.getFloatAsString(ins1);
    }



    public void setIns1(String val)
    {
        if (val.length()!=0)
            setIns1(m_da.getIntValue(val));
    }

    public void setIns1(int val)
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

    public String getIns2AsString()
    {
        return this.getFloatAsString(ins2);
    }


    public void setIns2(String val)
    {
        setIns2(m_da.getIntValue(val));
    }

    public void setIns2(int val)
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


    public String getCHAsString()
    {
        return this.getFloatAsString(ch);
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


    public String getActivity()
    {
        return activity;
    }
/*
    public void setAct(String val)
    {
        act = val;
    }
*/
    public void setActivity(String val)
    {
        if ((activity==null) || (!activity.equals(val)))
        {
            activity = val;
            if (activity.trim().length()!=0)
                changed = true;
        }
    }



    public String getUrine()
    {
        return urine;
    }
/*
    public void setAct(String val)
    {
        act = val;
    }
*/
    public void setUrine(String val)
    {
        if ((urine==null) || (!urine.equals(val)) )
        {
            urine = val;
            if (urine.trim().length()!=0)
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
    	return true;
    	/*
        if (m_dv==null)
            return true;
        else
            return false;
            */
    }

    /*
    public DayValueH getHibernateObject()
    {
        if (m_dv==null)
        {
            m_dv = new DayValueH();
            //m_dv.setAct(act);
            m_dv.setBg(bg);
            m_dv.setCh(ch);
            m_dv.setComment(comment);
            m_dv.setDt_info(datetime);
            m_dv.setIns1(ins1);
            m_dv.setIns2(ins2);
            m_dv.setExtended(createExtended());
//	    m_dv.setMeals_ids("");
        }
        else
        {
            //m_dv.setAct(act);
            m_dv.setBg(bg);
            m_dv.setCh(ch);
            m_dv.setComment(comment);
            m_dv.setDt_info(datetime);
            m_dv.setIns1(ins1);
            m_dv.setIns2(ins2);
            m_dv.setExtended(createExtended());
        }

        return m_dv;

    }

    public boolean hasHibernateObject()
    {
        return (m_dv!=null);
    }
*/

    public String getFloatAsIntString(float fl)
    {
        if (fl == 0.0)
        {
            return "";
        }
        else
        {
            int i = this.getFloatAsInt(fl);
            return "" + i;
        }
    }

    public String getFloatAsString(float fl)
    {
        if (fl == 0.0)
        {
            return "";
        }
        else
        {
            return DataAccessMeter.MmolDecimalFormat.format(fl);
        }
    }


    public int getFloatAsInt(float f)
    {
        Float f_i = new Float(f);
        return f_i.intValue();
    }



    //stupid but TableModel needs Objects...
    public Object getValueAt(int column)
    {
        //return "X";

        switch (column) 
        {
            case 0:
                return new Long(datetime); //m_da.getDateTimeAsTimeString(datetime);
            case 1:
                return this.getBGAsString();
            case 2:
                return this.getIns1AsString();
            case 3:
                return this.getIns2AsString();
            case 4:
                return this.getFloatAsString(ch);
            case 5:
                return activity;
            case 6:
                return urine;
            case 7:
                return comment;
            default:
                {
                    log.warn("Shouldn't have reached by here: " + column);
                    return "";
                } 
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
    		    bg = m_da.getIntValue(aValue);
    		    if (bg>0)
    			changed = true;
    		} break;
            case 2:
    		{
    		    ins1 = m_da.getIntValue(aValue);
    		    if (ins1!=0.0f)
    			changed = true;
    		} break;
            case 3:
    		{
    		    ins2 = m_da.getIntValue(aValue);
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
    		    this.activity = (String)aValue; // m_da.getIntValue(
    		    //if (act!=0)
                if ((activity!=null) && (activity.length()!=0))
                    changed = true;
    		} break;
            case 6:
            {
                this.urine = (String)aValue; 

                if ((urine!=null) && (urine.length()!=0))
                    changed = true;
            } break;
            case 7:
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
