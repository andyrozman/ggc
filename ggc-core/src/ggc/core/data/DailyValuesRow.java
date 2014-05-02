package ggc.core.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.db.ext.ExtendedCapable;
import com.atech.db.ext.ExtendedHandler;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     DailyValuesRow
 *  Description:  One row in the DailyValues Data Model.
 * 
 *  @author schultd
 *  @author Andy {andy@atech-software.com}  
 */

// WARNING: This is one of old classes, which we still use, and it needs serious rewrite. Andy
// FIXME
public class DailyValuesRow implements Serializable, Comparable<DailyValuesRow>, ExtendedCapable
{

    private static final long serialVersionUID = 3047797993365876861L;
    private static Log log = LogFactory.getLog(DailyValuesRow.class); 


    private long datetime;
    private int bg;
    private int ins1;
    private int ins2;
    private float ch;
    private String comment;
    private String extended;
    
    //private String[] extended_arr;
    private String meals;

    private boolean changed = false;
    DayValueH m_dv = null;
    DataAccess m_da = DataAccess.getInstance();
    GGCProperties props = m_da.getSettings();
    boolean debug = false;
    Hashtable<String,String> ht_extended = new Hashtable<String,String>();
    
    
    

    public DailyValuesRow()
    {
    	this.datetime = 0L;
    	this.bg = 0;
    	this.ins1 = 0;
    	this.ins2 = 0;
    	this.ch = 0.0f;
    	this.extended = null;
    	this.comment = "";

        this.ht_extended = this.getExtendedHandler().loadExtended(extended);
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

        this.ht_extended = this.getExtendedHandler().loadExtended(extended);
    }


    public DailyValuesRow(long datetime, int bg, int ins1, int ins2, float ch, String activity, String urine, String comment)
    {
        //loadExtended();
        this.datetime = datetime;
        this.bg = bg; 
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.setExtendedValue(ExtendedDailyValue.EXTENDED_ACTIVITY, activity, false);
        this.setExtendedValue(ExtendedDailyValue.EXTENDED_URINE, urine, false);
        //this.activity = activity;
        //this.urine = urine;
        this.comment = comment;
    }



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
        this.ht_extended = this.getExtendedHandler().loadExtended(extended);
    }


    public DailyValuesRow(long datetime, String BG, String Ins1, String Ins2, String CH, String act, String urine, String Comment, ArrayList<String> lst_meals)
    {
    	this.datetime = datetime;
        this.bg = m_da.getIntValueFromString(BG, 0);
        this.ins1 = m_da.getIntValueFromString(Ins1, 0);
        this.ins2 = m_da.getIntValueFromString(Ins2, 0);
        this.ch = m_da.getIntValueFromString(CH, 0);
        
        this.setExtendedValue(ExtendedDailyValue.EXTENDED_ACTIVITY, act, false);
        this.setExtendedValue(ExtendedDailyValue.EXTENDED_URINE, urine, false);

        //this.meals = dv.getMeals_ids();
        //this.extended = dv.getExtended();
        this.comment = Comment;
    	
    }

    
    

    public boolean areMealsSet()
    {
        if ((m_da.isValueSet(this.meals)) || (this.isExtendedValueSet(ExtendedDailyValue.EXTENDED_FOOD_DESCRIPTION)))
            return true;
        else
            return false;
    }

    
    /**
     * Are Meals Set
     * 
     * @return true if meals set (either from db or by description (in this case CH must be set)) 
     */
    public int areMealsSetType()
    {
        if (m_da.isValueSet(this.meals))
            return 1;
        else if (this.isExtendedValueSet(ExtendedDailyValue.EXTENDED_FOOD_DESCRIPTION))
            return 2;
        else
            return 0;
    }
    
    
    public String getMealsIds()
    {
        return this.meals;
    }
    

    public void setMealsIds(String val)
    {
        this.meals = val;
    }
    

    /**
     * Get DateTime Long
     * 
     * @param date
     * @param time
     * @return
     */
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


    
    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
//        return "DT="+datetime.getTime() + ";BG=" + BG + ";Ins1=" + Ins1 + ";Ins2=" + Ins2 + ";BE=" + BE + ";Act=" + Act + ";Comment=" + Comment + ";";
        return "DailyValuesRow [dt=" + getDateTime() + ";bg=" + bg + ";ins1=" + ins1 + ";ins2=" + ins2 + ";CH=" + ch + ";activity=" + getExtendedValue(ExtendedDailyValue.EXTENDED_ACTIVITY) + ";urine=" + getExtendedValue(ExtendedDailyValue.EXTENDED_URINE) + ";comment=" + comment + "]";
    }


    /**
     * Get DateTime
     * 
     * @return
     */
    public long getDateTime()
    {
	//System.out.println(datetime);
        return datetime;
    }


    /**
     * Set DateTime
     * 
     * @param dt
     */
    public void setDateTime(long dt)
    {
    	if (this.datetime!=dt)
    	{
    	    this.datetime = dt;
    	    this.changed=true;
    	}
    }


    /**
     * Get Date
     * 
     * @return
     */
    public long getDate()
    {
        return (datetime/10000);
    }



    /**
     * Get Date As String
     * 
     * @return
     */
    public String getDateAsString()
    {
        return ATDataAccessAbstract.getDateTimeAsDateString(datetime);
    }

    /**
     * Get Time As String
     * 
     * @return
     */
    public String getTimeAsString()
    {
        return ATDataAccessAbstract.getDateTimeAsTimeString(datetime);
    }

    /**
     * Get DateTime As Time
     * 
     * @return
     */
    public String getDateTimeAsTime()
    {
        return "" + ATechDate.convertATDate(datetime, ATechDate.FORMAT_DATE_AND_TIME_MIN, ATechDate.FORMAT_TIME_ONLY_MIN);
    }

    /**
     * Get DateTime As ATDate
     * 
     * @return
     */
    public ATechDate getDateTimeAsATDate()
    {
        return new ATechDate(datetime);
    }

    
    /**
     * Get DateTime As Date
     * 
     * @return
     */
    public Date getDateTimeAsDate()
    {
        /*
        ATechDate at = new ATechDate(datetime);
        
        System.out.println("getDateTimeAsDate()::getGregorianCalendar: gc      " + at.getGregorianCalendar().get(Calendar.HOUR_OF_DAY) + ":" + at.getGregorianCalendar().get(Calendar.MINUTE));
        
        
        Date d = at.getGregorianCalendar().getTime();
       
        System.out.println("getDateTimeAsDate()::getGregorianCalendar: date    " + d.getHours() + ":" + d.getMinutes());
        
        return at.getGregorianCalendar().getTime(); */
        
        return new ATechDate(datetime).getGregorianCalendar().getTime();
        
    }
    
    
    
    /**
     * Set DateTime
     * 
     * @param date
     * @param time
     */
    public void setDateTime(String date, String time)
    {
        datetime = getDateTimeLong(date, time);
    } 


    /**
     * Get BG
     * 
     * @return
     */
    public float getBG()
    {
        if (debug)
        {
            System.out.println("getBg [type=" + props.getBG_unit() + "]");
            System.out.println("getBg [internal_value=" + this.bg + "]");
        }

        if (props.getBG_unit()==2)
        {
            float v = m_da.getBGValueByType(DataAccess.BG_MMOL, bg);

            if (debug)
                System.out.println("getBg [type=2,return=" + v + "]");

            return v;

            //return m_da.getBGValueByType(DataAccess.BG_MMOL, bg);
        }
        else
        {
            if (debug)
                System.out.println("getBg [type=1,return=" + this.bg + "]");

            return bg;
        }

    }


    /**
     * Get BG As String
     * 
     * @return
     */
    public String getBGAsString()
    {
        if (debug)
        {
            System.out.println("getBgAsString [type=" + props.getBG_unit() + "]");
            System.out.println("getBgAsString [internal_value=" + this.bg + "]");
        }

        if (props.getBG_unit()==2)
        {
            float v = m_da.getBGValueByType(DataAccess.BG_MMOL, bg);

            if (debug)
                System.out.println("getBgAsString [type=2,return=" + v + "]");

            return DataAccess.getFloatAsString(v, 1);
        }
        else
        {
            if (debug)
                System.out.println("getBgAsString [type=1,return=" + this.bg + "]");

            return "" + bg;
        }

    }



    /**
     * Get BG Raw
     * 
     * @return
     */
    public float getBGRaw()
    {
        return bg;
    }


    /**
     * Get BG
     * 
     * @param type
     * @return
     */
    public float getBG(int type)
    {

        if (debug)
            System.out.println("Internal value: " + this.bg);

        if (type==DataAccess.BG_MGDL)
            return bg;
        else
        {
            return m_da.getBGValueByType(DataAccess.BG_MMOL, bg);
        }

    }



    /**
     * BG: mmol/L 
     */
    public static int BG_MMOLL = 2;

    /**
     * BG: mg/dL 
     */
    public static int BG_MGDL = 1;

    /**
     * Set BG
     * 
     * @param type
     * @param val
     */
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

    }

    

    
    /**
     * Set BG
     * 
     * @param type
     * @param val
     */
    public void setBG(int type, String val)
    {
        if (ATDataAccessAbstract.isEmptyOrUnset(val))
        {
            setBG(0);
            return;
        }
        
        float f;
        try
        {
            f = Float.parseFloat(val);
        }
        catch (NumberFormatException e)
        {
            f = 0;
            System.err.println("ERROR: unparsable folat string: " + val);
        }

        setBG(type,f);

    }



    /**
     * Set BG
     * 
     * @param val
     */
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


    /**
     * Get Ins 1
     * @return
     */
    public float getIns1()
    {
        return ins1;
    }

    /**
     * Get Ins 1 As String
     * @return
     */
    public String getIns1AsString()
    {
        return getFloatAsIntString(ins1);
    }

    /**
     * Get Ins 1 As Decimal String
     * @return
     */
    public String getIns1AsStringDecimal()
    {
        return getDecimalValue(this.ins1, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS1));
    }


    /**
     * Get Ins 2 As Decimal String
     * @return
     */
    public String getIns2AsStringDecimal()
    {
        return getDecimalValue(this.ins2, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS2));
    }
    
    
    
    private String getDecimalValue(int val, String decimal)
    {
        int dec = 0;
        
        if ((decimal==null) || (decimal.length()==0) || (decimal.equals("0")))
        {
            dec = 0;
        }
        else
        {
            dec = Integer.parseInt(decimal);
        }
        
        if (dec==0)
        {
            return "" + val;
        }
        else
            return DataAccess.getFloatAsString((val + (0.1f * dec)), 1);
        
    }
    
    
    private float getDecimalValueAsFloat(int value, String decimal_part)
    {
        float val = 0.0f;
        
        val += value;
        val += (0.1f * m_da.getFloatValueFromString(decimal_part, 0.0f));
        
        return val;
    }

    
    
    /**
     * Set Ins 1
     * 
     * @param val
     */
    public void setIns1(String val)
    {
        if (!ATDataAccessAbstract.isEmptyOrUnset(val))
            setIns1(m_da.getIntValue(val));
    }

    
    /**
     * Set Ins 1
     * 
     * @param val
     */
    public void setIns1(int val)
    {
        if (ins1!=val) 
        {
            ins1 = val;
            changed = true;
        }
    }


    /**
     * Get Ins 2
     * @return
     */
    public float getIns2()
    {
        return ins2;
    }

    /**
     * Get Ins 1 As String
     * @return
     */
    public String getIns2AsString()
    {
        return getFloatAsIntString(ins2);
    }


    /**
     * Set Ins 2
     * 
     * @param val
     */
    public void setIns2(String val)
    {
        if (! ATDataAccessAbstract.isEmptyOrUnset(val))
        {
            setIns2(m_da.getIntValue(val));
        }
        else
        {
            setIns2(0);
        }
    }

    
    /**
     * Set Ins 2
     * 
     * @param val
     */
    public void setIns2(int val)
    {
        if (ins2!=val) 
        {
            ins2 = val;
            changed = true;
        }
    }


    /**
     * Get CH
     * 
     * @return
     */
    public float getCH()
    {
        return ch;
    }


    /**
     * Get CH As String
     * 
     * @return
     */
    public String getCHAsString()
    {
        return this.getFloatAsString(ch);
    }


    /**
     * Set CH
     * 
     * @param val
     */
    public void setCH(String val)
    {
        setCH(m_da.getFloatValue(val));
    }

    /**
     * Set CH
     * 
     * @param val
     */
    public void setCH(float val)
    {
        if (ch!=val) 
        {
            ch = val;
            changed = true;
        }
    }


    /**
     * Get Activity
     * 
     * @return
     */
    public String getActivity()
    {
        return this.getExtendedValue(ExtendedDailyValue.EXTENDED_ACTIVITY);
    }

    
    /**
     * Get Insulin 3
     * 
     * @return
     */
    public float getIns3()
    {
        return m_da.getFloatValueFromString(this.getExtendedValue(ExtendedDailyValue.EXTENDED_INSULIN_3), 0.0f);
    }
    

    /**
     * Set Insulin 3
     * 
     * @param val
     */
    public void setIns3(float val)
    {
        this.setExtendedValue(ExtendedDailyValue.EXTENDED_INSULIN_3, "" + val, true);
    }
    

    /**
     * Set Insulin 3
     * 
     * @param val
     */
    public void setIns3(String val)
    {
        this.setExtendedValue(ExtendedDailyValue.EXTENDED_INSULIN_3, val, true);
    }
    

    public float getBasalInsulin()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        float sum = 0.0f;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BASAL)
            sum += getDecimalValueAsFloat(this.ins1, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS1));
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BASAL)
            sum += getDecimalValueAsFloat(this.ins2, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS2));

        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BASAL)
            sum += this.getIns3();
        
        return sum;
    }
    

    public float getBolusInsulin()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        float sum = 0.0f;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BOLUS)
            sum += getDecimalValueAsFloat(this.ins1, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS1));
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BOLUS)
            sum += getDecimalValueAsFloat(this.ins2, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS2));

        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BOLUS)
            sum += this.getIns3();
        
        return sum;
    }

    
    public int getBasalInsulinCount()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        int count = 0;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BASAL)
            if (getDecimalValueAsFloat(this.ins1, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS1))>0)
                count++;
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BASAL)
            if (getDecimalValueAsFloat(this.ins2, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS2))>0)
                count++;
        
        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BASAL)
            if (this.getIns3()>0)
                count++;
        
        return count;
    }
    

    public int getBolusInsulinCount()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        int count= 0;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BOLUS)
            if (getDecimalValueAsFloat(this.ins1, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS1))>0)
                count++; 
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BOLUS)
            if (getDecimalValueAsFloat(this.ins2, this.getExtendedValue(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS2))>0)
                count++; 

        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BOLUS)
            if (this.getIns3()>0)
                count++; 
        
        return count;
    }
    
    
    public void setActivity(String val)
    {
        setExtendedValue(ExtendedDailyValue.EXTENDED_ACTIVITY, val, true);
    }


    public String getFoodDescription()
    {
        return this.getExtendedValue(ExtendedDailyValue.EXTENDED_FOOD_DESCRIPTION);
    }

    
    public void setFoodDescription(String val)
    {
        setExtendedValue(ExtendedDailyValue.EXTENDED_FOOD_DESCRIPTION, val, true);
    }

    
    public String getFoodDescriptionCH()
    {
        return this.getExtendedValue(ExtendedDailyValue.EXTENDED_FOOD_CH);
    }
    
    
    public void setFoodDescriptionCH(String val)
    {
        setExtendedValue(ExtendedDailyValue.EXTENDED_FOOD_CH, val, true);
    }
    
    
    public String getUrine()
    {
        return this.getExtendedValue(ExtendedDailyValue.EXTENDED_URINE);
    }
    
    
    public void setUrine(String val)
    {
        setExtendedValue(ExtendedDailyValue.EXTENDED_URINE, val, true);
    }


    public void setExtended(String ext)
    {
        if (!this.extended.equals(ext))
        {
            this.extended = ext;
            this.changed = true;
        }
    }
    
    

    public String getComment()
    {
        return comment;
    }

    
    public void setComment(String val)
    {
    	if (comment==null)
    	{
    	    comment = val;
                changed = true;
    	}
    	else if (!comment.equals(val)) 
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
        return (m_dv==null);
    }


    public DayValueH getHibernateObject()
    {
        if (m_dv==null)
        {
            m_dv = new DayValueH();
            m_dv.setBg(bg);
            m_dv.setCh(ch);
            m_dv.setComment(comment);
            m_dv.setDt_info(datetime);
            m_dv.setIns1(ins1);
            m_dv.setIns2(ins2);
            m_dv.setExtended(this.getExtendedHandler().saveExtended(ht_extended));
            m_dv.setPerson_id((int)m_da.getCurrentUserId());
            m_dv.setMeals_ids(this.meals);
        }
        else
        {
            m_dv.setBg(bg);
            m_dv.setCh(ch);
            m_dv.setComment(comment);
            m_dv.setDt_info(datetime);
            m_dv.setIns1(ins1);
            m_dv.setIns2(ins2);
            m_dv.setExtended(this.getExtendedHandler().saveExtended(ht_extended));
            m_dv.setPerson_id((int)m_da.getCurrentUserId());
            m_dv.setMeals_ids(this.meals);
        }

        return m_dv;

    }

    public boolean hasHibernateObject()
    {
        return (m_dv!=null);
    }


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
            return DataAccess.Decimal1Format.format(fl);
        }
    }


    public String getFloat2AsString(float fl)
    {
        if (fl == 0.0)
        {
            return "";
        }
        else
        {
            return DataAccess.Decimal2Format.format(fl);
        }
    }
    
    
    /**
     * Get Float As Int
     * 
     * @param f
     * @return
     */
    public int getFloatAsInt(float f)
    {
        Float f_i = new Float(f);
        return f_i.intValue();
    }



    //stupid but TableModel needs Objects...
    /**
     * Get Value At
     * 
     * @param column
     * @return
     */
    public Object getValueAt(int column)
    {
        //return "X";

        switch (column) 
        {
            case 0:
                return new Long(datetime); //m_da.getDateTimeAsTimeString(datetime);
            case 1:
        	if (this.getBG()==0.0f)
                //if (getBGAsString().equals("0"))
                    return "";
                return this.getBGAsString();
            case 2:
                return DataAccess.getFloatAsString(this.getBolusInsulin(), 1);
            case 3:
                return DataAccess.getFloatAsString(this.getBasalInsulin(), 1);
            case 4:
                return this.getFloat2AsString(ch);
            case 5:
                return this.getActivity();
            case 6:
                return this.getUrine();
            case 7:
                return comment;
            default:
                {
                    log.warn("Shouldn't have reached by here: " + column);
                    return "";
                } 
        }
    }


    /**
     * Get DateD
     * @return
     */
    public int getDateD()
    {
        return Integer.parseInt(ATDataAccessAbstract.getDateTimeAsDateString(datetime));
    }


    /**
     * Get DateDString
     * @return
     */
    public String getDateDString()
    {
        return ATDataAccessAbstract.getDateTimeAsDateString(datetime);
    }
    
    
    /**
     * Get DateT
     * @return
     */
    public int getDateT()
    {
        return (new ATechDate(datetime)).getTime();
    }

    
    /**
     * Get DateTime Ms
     * @return
     */
    public long getDateTimeMs()
    {
        /*
        System.out.println("DWR: " + datetime);
        ATechDate atd = new ATechDate(datetime);
        
        GregorianCalendar gc = atd.getGregorianCalendar();
        System.out.println("getGregorianCalendar: gc      " + gc.get(Calendar.HOUR_OF_DAY) + ":" + gc.get(Calendar.MINUTE));
        */
        return (new ATechDate(datetime)).getGregorianCalendar().getTimeInMillis();
        
    }
    
    
    ///
    /// Comparable<DailyValuesRow>
    ///

    /** 
     * compareTo
     */
    public int compareTo(DailyValuesRow dvr)
    {
        return (int)(this.getDateTime()-dvr.getDateTime());
    }



    public ExtendedHandler getExtendedHandler()
    {
        return m_da.getExtendedHandler("DailyValuesRow");
    }

    
    /**
     * Get Extended Value
     * 
     * @param type
     * @return
     */
    public String getExtendedValue(int type)
    {
        return this.getExtendedHandler().getExtendedValue(type, this.ht_extended);
    }
    

    /**
     * Set Extended Value
     * 
     * @param type
     * @param value
     * @param set_checked
     */
    public void setExtendedValue(int type, String value, boolean set_checked)
    {
        boolean set = this.getExtendedHandler().setExtendedValue(type, value, this.ht_extended);
        
        if ((set_checked) && (set))
            this.changed = true;
    }
    
    /**
     * Is Extended Value Set 
     * 
     * @param type
     * @return
     */
    public boolean isExtendedValueSet(int type)
    {
        return this.getExtendedHandler().isExtendedValueSet(type, this.ht_extended);
    }
    
    
    /**
     * Create Extended
     */
    public void createExtended()
    {
        this.extended = this.getExtendedHandler().saveExtended(this.ht_extended);
    }
    
    
}
