package ggc.core.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.ATechDate;

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

public class DailyValuesRow implements Serializable, Comparable<DailyValuesRow>
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
    
    private String[] extended_arr;
    private String meals;

    private boolean changed = false;
    DayValueH m_dv = null;
    DataAccess m_da = DataAccess.getInstance();
    GGCProperties props = m_da.getSettings();
    boolean debug = false;

    
    /**
     * Extended Parameter: Activity
     */
    public static final int EXTENDED_ACTIVITY = 0;

    /**
     * Extended Parameter: Urine
     */
    public static final int EXTENDED_URINE = 1;

    /**
     * Extended Parameter: Food Description
     */
    public static final int EXTENDED_FOOD_DESCRIPTION = 2;
    
    /**
     * Extended Parameter: Food CH
     */
    public static final int EXTENDED_FOOD_CH = 3;
    
    /**
     * Extended Parameter: Decimal Part Ins1
     */
    public static final int EXTENDED_DECIMAL_PART_INS1 = 4;
    
    /**
     * Extended Parameter: Decimal Part Ins2
     */
    public static final int EXTENDED_DECIMAL_PART_INS2 = 5;
    

    /**
     * Extended Parameter: Insulin 3
     */
    public static final int EXTENDED_INSULIN_3 = 6;
    
    
    private static final int EXTENDED_MAX = 6;
    
    private String extended_desc[] = { 
                                    "URINE",
                                    "ACTIVITY",
                                    "FOOD_DESCRIPTION",
                                    "FOOD_DESC_CH",
                                    "DECIMAL_INS1",
                                    "DECIMAL_INS2",
                                    "INSULIN_3"
    };
                                    
    
    

    /**
     * Constructor
     */
    public DailyValuesRow()
    {
    	this.datetime = 0L;
    	this.bg = 0;
    	this.ins1 = 0;
    	this.ins2 = 0;
    	this.ch = 0.0f;
    	this.extended = null;
    	this.comment = "";

        loadExtended();
    }



    /**
     * Constructor
     * 
     * @param datetime
     * @param bg
     * @param ins1
     * @param ins2
     * @param ch
     * @param extended
     * @param comment
     */
    public DailyValuesRow(long datetime, int bg, int ins1, int ins2, float ch, String extended, String comment)
    {
        this.datetime = datetime;
        this.bg = bg; 
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.extended = extended;
        this.comment = comment;

        loadExtended();
    }


    /**
     * Constructor
     * 
     * @param datetime
     * @param bg
     * @param ins1
     * @param ins2
     * @param ch
     * @param activity
     * @param urine
     * @param comment
     */
    public DailyValuesRow(long datetime, int bg, int ins1, int ins2, float ch, String activity, String urine, String comment)
    {
        loadExtended();
        this.datetime = datetime;
        this.bg = bg; 
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.extended_arr[DailyValuesRow.EXTENDED_ACTIVITY] = activity;
        this.extended_arr[DailyValuesRow.EXTENDED_URINE] = urine;
        //this.activity = activity;
        //this.urine = urine;
        this.comment = comment;
    }



    /**
     * Constructor
     * 
     * @param dv
     */
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
    	loadExtended();
    }

    /*
    public DailyValuesRow(long datetime, String BG, String Ins1, String Ins2, String BU, String Activity, String urine, String Comment, ArrayList<String> lst_meals)
    {
        // XX
        this(date,time,BG,Ins1,Ins2,BU,Act,null,Comment,false);
    }*/



    /**
     * Constructor
     * 
     * @param datetime
     * @param BG
     * @param Ins1
     * @param Ins2
     * @param CH 
     * @param Act
     * @param urine
     * @param Comment
     * @param lst_meals
     */
    public DailyValuesRow(long datetime, String BG, String Ins1, String Ins2, String CH, String Act, String urine, String Comment, ArrayList<String> lst_meals)
    {
    	this.datetime = datetime;
        this.bg = m_da.getIntValueFromString(BG, 0);
        this.ins1 = m_da.getIntValueFromString(Ins1, 0);
        this.ins2 = m_da.getIntValueFromString(Ins2, 0);
        this.ch = m_da.getIntValueFromString(CH, 0);
        
        setActivity(Act);
        setUrine(urine);
        
        //this.meals = dv.getMeals_ids();
        //this.extended = dv.getExtended();
        this.comment = Comment;
    	
    }

    /**
     * Load Extended
     */
    public void loadExtended()
    {
        if ((this.extended==null) || (this.extended.trim().length()==0))
        {
            this.extended_arr = new String[EXTENDED_MAX+1];
        }
        else
        {
            this.extended_arr = new String[EXTENDED_MAX+1];
            
            for(int i=0; i<=EXTENDED_MAX; i++)
            {
                this.extended_arr[i] = this.getExtendedEntry(i);
            }
        }
    }
    
    private String extended_divider = ";##;";

    
    /**
     * Create Extended Entry for database
     * 
     * @return String value containing all extended
     */
    public String createExtended()
    {
        StringBuffer sb = new StringBuffer();
        
        for(int type=1; type<=DailyValuesRow.EXTENDED_MAX; type++)
        {
            if (this.isExtendedValueSet(type))
            {
                sb.append(this.extended_desc[type] + "=" + getExtendedValue(type) + extended_divider);
            }
        }
        
        this.setExtended(sb.toString());
        
        return this.extended;
    }

    


    private boolean isExtendedValueSet(int type)
    {
        String val = getExtendedValue(type);

        return isValueSet(val);
    }

    private boolean isValueSet(String val)
    {
        if ((val==null) || (val.trim().length()==0))
            return false;
        else
            return true;
    }
    
    

    /**
     * Get Extended Value (after resolved)
     * 
     * @param type
     * @return
     */
    public String getExtendedValue(int type)
    {
        return this.extended_arr[type];
    }

    
    /**
     * Set Extended Value (after resolved)
     * 
     * @param type
     * @param val 
     */
    public void setExtendedValue(int type, String val)
    {
        this.extended_arr[type] = val;
    }
    

    /**
     * Set Extended Value (after resolved)
     * 
     * @param type
     * @param val 
     */
    public void setExtendedValueChecked(int type, String val)
    {
        if (isCheckOk(val, this.extended_arr[type]))
        {
            this.extended_arr[type] = val;
            changed = true;
        }
    }
    
    
    
    
    
    
    

    /**
     * Get Extended Entry
     * 
     * @param type
     * @return
     */
    public String getExtendedEntry(int type)
    {
        return getExtendedEntry(this.extended_desc[type]);
    }

    /**
     * Get Extended Entry
     * 
     * @param key
     * @return
     */
    public String getExtendedEntry(String key)
    {
        if (this.extended.contains(key + "="))
        {
            String val = this.extended.substring(this.extended.indexOf(key +"=") + key.length() +1);
            if (val.contains(this.extended_divider))
            {
                val = val.substring(0, val.indexOf(this.extended_divider));
            }

            return val;
        }
        else
            return "";

    }


    /**
     * Are Meals Set
     * 
     * @return true if meals set (either from db or by description (in this case CH must be set)) 
     */
    public boolean areMealsSet()
    {
        if ((isValueSet(this.meals)) || (isValueSet(this.extended_arr[DailyValuesRow.EXTENDED_FOOD_DESCRIPTION])))
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
        if (isValueSet(this.meals))
            return 1;
        else if (isValueSet(this.extended_arr[DailyValuesRow.EXTENDED_FOOD_DESCRIPTION]))
            return 2;
        else
            return 0;
    }
    
    
    /**
     * Get Meals Ids
     * 
     * @return
     */
    public String getMealsIds()
    {
        return this.meals;
    }
    
    /**
     * Set Meals Ids
     * 
     * @param val 
     */
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
        return "DailyValuesRow [dt=" + getDateTime() + ";bg=" + bg + ";ins1=" + ins1 + ";ins2=" + ins2 + ";CH=" + ch + ";activity=" + this.extended_arr[DailyValuesRow.EXTENDED_ACTIVITY] + ";urine=" + this.extended_arr[DailyValuesRow.EXTENDED_URINE] + ";comment=" + comment + "]";
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
        return m_da.getDateTimeAsDateString(datetime);
    }

    /**
     * Get Time As String
     * 
     * @return
     */
    public String getTimeAsString()
    {
        return m_da.getDateTimeAsTimeString(datetime);
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
        ATechDate at = new ATechDate(datetime);
        return at.getGregorianCalendar().getTime();
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
        if (DataAccess.isEmptyOrUnset(val))
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
        return getDecimalValue(this.ins1, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS1));
    }


    /**
     * Get Ins 2 As Decimal String
     * @return
     */
    public String getIns2AsStringDecimal()
    {
        return getDecimalValue(this.ins2, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS2));
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
        if (!DataAccess.isEmptyOrUnset(val))
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
        if (! DataAccess.isEmptyOrUnset(val))
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
        return this.extended_arr[DailyValuesRow.EXTENDED_ACTIVITY];
    }

    
    /**
     * Get Insulin 3
     * 
     * @return
     */
    public float getIns3()
    {
        return m_da.getFloatValueFromString(this.extended_arr[DailyValuesRow.EXTENDED_INSULIN_3], 0.0f);
    }
    

    /**
     * Set Insulin 3
     * 
     * @param val
     */
    public void setIns3(float val)
    {
        this.extended_arr[DailyValuesRow.EXTENDED_INSULIN_3] = "" + val;
    }
    

    /**
     * Set Insulin 3
     * 
     * @param val
     */
    public void setIns3(String val)
    {
        this.extended_arr[DailyValuesRow.EXTENDED_INSULIN_3] = val;
    }
    
    /**
     * Get Basal Insulin
     * 
     * @return
     */
    public float getBasalInsulin()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        float sum = 0.0f;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BASAL)
            sum += getDecimalValueAsFloat(this.ins1, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS1));
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BASAL)
            sum += getDecimalValueAsFloat(this.ins2, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS2));

        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BASAL)
            sum += this.getIns3();
        
        return sum;
    }
    

    /**
     * Get Bolus Insulin
     * 
     * @return
     */
    public float getBolusInsulin()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        float sum = 0.0f;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BOLUS)
            sum += getDecimalValueAsFloat(this.ins1, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS1));
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BOLUS)
            sum += getDecimalValueAsFloat(this.ins2, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS2));

        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BOLUS)
            sum += this.getIns3();
        
        return sum;
    }

    
    /**
     * Get Basal Insulin Count
     * 
     * @return
     */
    public int getBasalInsulinCount()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        int count = 0;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BASAL)
            if (getDecimalValueAsFloat(this.ins1, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS1))>0)
                count++;
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BASAL)
            if (getDecimalValueAsFloat(this.ins2, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS2))>0)
                count++;
        
        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BASAL)
            if (this.getIns3()>0)
                count++;
        
        return count;
    }
    

    /**
     * Get Bolus Insulin
     * 
     * @return
     */
    public int getBolusInsulinCount()
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        int count= 0;
        
        if (gp.getIns1Type()==DataAccess.INSULIN_DOSE_BOLUS)
            if (getDecimalValueAsFloat(this.ins1, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS1))>0)
                count++; 
        
        if (gp.getIns2Type()==DataAccess.INSULIN_DOSE_BOLUS)
            if (getDecimalValueAsFloat(this.ins2, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_INS2))>0)
                count++; 

        if (gp.getIns3Type()==DataAccess.INSULIN_DOSE_BOLUS)
            if (this.getIns3()>0)
                count++; 
        
        return count;
    }
    
    
    /**
     * Set Activity
     * 
     * @param val
     */
    public void setActivity(String val)
    {
        setExtendedValueChecked(DailyValuesRow.EXTENDED_ACTIVITY, val);
    }


    /**
     * Get Food Description
     * 
     * @return 
     */
    public String getFoodDescription()
    {
        return this.extended_arr[DailyValuesRow.EXTENDED_FOOD_DESCRIPTION];
    }

    
    /**
     * Set Food Description
     * 
     * @param val
     */
    public void setFoodDescription(String val)
    {
        setExtendedValueChecked(DailyValuesRow.EXTENDED_FOOD_DESCRIPTION, val);
    }

    
    /**
     * Get Food Description CH
     * 
     * @return 
     */
    public String getFoodDescriptionCH()
    {
        return this.extended_arr[DailyValuesRow.EXTENDED_FOOD_CH];
    }
    
    
    /**
     * Set Food Description CH
     * 
     * @param val
     */
    public void setFoodDescriptionCH(String val)
    {
        setExtendedValueChecked(DailyValuesRow.EXTENDED_FOOD_CH, val);
    }
    
    

    private boolean isCheckOk(String current, String old)
    {
        if ((current==null) || (current.trim().length()==0))
        {
            return false;
        }
        else
        {
            if ((old==null) || (!old.equals(current)))
                return true;
            else
                return false;
        }
        
    }

    
    /**
     * Get Urine
     * 
     * @return
     */
    public String getUrine()
    {
        return this.extended_arr[DailyValuesRow.EXTENDED_URINE];
    }
    
    
    /**
     * Set Urine
     * 
     * @param val
     */
    public void setUrine(String val)
    {
        setExtendedValueChecked(DailyValuesRow.EXTENDED_URINE, val);
    }


    /**
     * Set Extended
     * 
     * @param ext
     */
    public void setExtended(String ext)
    {
        if (!this.extended.equals(ext))
        {
            this.extended = ext;
            this.changed = true;
        }
        
    }
    
    

    /**
     * Get Comment
     * 
     * @return
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Set Comment
     * 
     * @param val
     */
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


    /**
     * Has Changed
     * 
     * @return
     */
    public boolean hasChanged()
    {
        return changed;
    }

    /**
     * Is New
     * 
     * @return
     */
    public boolean isNew()
    {
        if (m_dv==null)
            return true;
        else
            return false;
    }

    /**
     * Get Hibernate Object
     * 
     * @return
     */
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
            m_dv.setExtended(createExtended());
            m_dv.setPerson_id(m_da.getCurrentPersonId());
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
            m_dv.setExtended(createExtended());
            m_dv.setPerson_id(m_da.getCurrentPersonId());
            m_dv.setMeals_ids(this.meals);
        }

        return m_dv;

    }

    /**
     * Has Hibernate Object
     * 
     * @return
     */
    public boolean hasHibernateObject()
    {
        return (m_dv!=null);
    }


    /**
     * Get Float As Int String
     * 
     * @param fl
     * @return
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

    /**
     * Get Float As String
     * 
     * @param fl
     * @return
     */
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


    /**
     * Get Float As String
     * 
     * @param fl
     * @return
     */
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
        return Integer.parseInt(m_da.getDateTimeAsDateString(datetime));
    }


    /**
     * Get DateDString
     * @return
     */
    public String getDateDString()
    {
        return m_da.getDateTimeAsDateString(datetime);
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
    

}
