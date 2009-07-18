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
 *  Author:   andyrozman  {andy@atech-software.com}
 */

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
    //private String activity;      // extended
    //private String urine;         // extended 
    //private String food_desc;     // extended
    //private String food_desc_ch;  // extended
    //private String decimal_part_bolus;  // extended
    //private String decimal_part_basal;  // extended
    
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
     * Extended Parameter: Decimal Part Bolus
     */
    public static final int EXTENDED_DECIMAL_PART_BOLUS = 4;
    
    /**
     * Extended Parameter: Decimal Part Bolus
     */
    public static final int EXTENDED_DECIMAL_PART_BASAL = 5;
    
    private static final int EXTENDED_MAX = 5;
    
    private String extended_desc[] = { 
                                    "URINE",
                                    "ACTIVITY",
                                    "FOOD_DESCRIPTION",
                                    "FOOD_DESC_CH",
                                    "DECIMAL_BOLUS",
                                    "DECIMAL_BASAL"
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
            /*
            this.activity = "";
            this.urine = "";
            this.food_desc = "";
            this.food_desc_ch = "";
            this.decimal_part = ""; */
        }
        else
        {
            this.extended_arr = new String[EXTENDED_MAX+1];
            
            for(int i=0; i<=EXTENDED_MAX; i++)
            {
                this.extended_arr[i] = this.getExtendedEntry(i);
            }
            
            /*
            this.urine = this.getExtendedEntry(DailyValuesRow.EXTENDED_URINE);
            this.activity = this.getExtendedEntry(DailyValuesRow.EXTENDED_ACTIVITY);
            this.food_desc = this.getExtendedEntry(DailyValuesRow.EXTENDED_FOOD_DESCRIPTION);
            this.food_desc_ch = this.getExtendedEntry(DailyValuesRow.EXTENDED_FOOD_CH);
            this.decimal_part_bolus = this.getExtendedEntry(DailyValuesRow.EXTENDED_DECIMAL_PART);*/
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

        /*
        if (this.isExtendedValueSet(DailyValuesRow.EXTENDED_URINE))
        {
            sb.append("URINE=" + getExtendedValue(DailyValuesRow.EXTENDED_URINE) + extended_divider);
        }

        if (this.isExtendedValueSet(DailyValuesRow.EXTENDED_ACTIVITY))
        {
            sb.append("ACTIVITY=" + getExtendedValue(DailyValuesRow.EXTENDED_ACTIVITY) + extended_divider);
        }

        this.meal_desc = this.getExtendedEntry("FOOD_DESCRIPTION");
        this.meal_desc_ch = this.getExtendedEntry("FOOD_DESCRIPTION_CH");
        this.decimal_part = this.getExtendedEntry("DECIMAL_PART");
        
        if (this.isExtendedValueSet(DailyValuesRow.EXTENDED_FOOD_DESCRIPTION))
        {
            sb.append("ACTIVITY=" + getExtendedValue(DailyValuesRow.EXTENDED_ACTIVITY) + extended_divider);
        } */
        
        return sb.toString();
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

/*
    private void loadMealsIds()
    {
        // to-do
        if ((this.meals!=null) && (this.meals.trim().length()>0))
        {
            meals_ids = new ArrayList<String>();


        }
    }

    private String createMealsIds()
    {
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
*/
    /*
    public ArrayList<String> getMealIdsList()
    {
        return this.meals_ids;
    }

    public void setMealIdsList(ArrayList<String> lst)
    {
        this.meals_ids = lst;
    }*/



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
        /*
        if ((this.meals==null) || (this.meals.trim().length()==0))
            return false;
        else
            return true; */
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
        /*
        if ((this.meals==null) || (this.meals.trim().length()==0))
            return false;
        else
            return true; */
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

/*
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
*/
    
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

    /*
    public Date getDateTimeAsDate()
    {
        return m_da.getDateTimeAsDateObject(datetime);
    }*/


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
//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //return sdf.format(datetime);
	//dssreturn m_da.getDateTimeAsDateString(datetime);
    }

    /**
     * Get Time As String
     * 
     * @return
     */
    public String getTimeAsString()
    {
        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //return sdf.format(datetime);
        return m_da.getDateTimeAsTimeString(datetime);
    }

    /**
     * Get DateTime As Time
     * 
     * @return
     */
    public String getDateTimeAsTime()
    {
        //long i = m_da.getDateTimeLong(datetime, DataAccess.DT_TIME);
        //System.out.println("X: " + i);
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

            //String ss = DataAccess.Decimal1Format.format(v);
            //ss = ss.replace(",", ".");
            //return ss;

            return DataAccess.getFloatAsString(v, 1);
            
            //return m_da.getBGValueByType(DataAccess.BG_MMOL, bg);
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





/*
    public void setBG(String val)
    {
        setBG(m_da.getIntValue(val));
    }
*/

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

    

    
    /**
     * Set BG
     * 
     * @param type
     * @param val
     */
    public void setBG(int type, String val)
    {
        if (m_da.isEmptyOrUnset(val))
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
        return getDecimalValue(this.ins1, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_BOLUS));
    }


    /**
     * Get Ins 2 As Decimal String
     * @return
     */
    public String getIns2AsStringDecimal()
    {
        return getDecimalValue(this.ins2, this.getExtendedValue(DailyValuesRow.EXTENDED_DECIMAL_PART_BASAL));
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
    
    
    

    
    
    /**
     * Set Ins 1
     * 
     * @param val
     */
    public void setIns1(String val)
    {
        if (!m_da.isEmptyOrUnset(val))
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
        if (! m_da.isEmptyOrUnset(val))
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
                return this.getIns1AsStringDecimal();
            case 3:
                return this.getIns2AsStringDecimal();
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
     * Set Value At
     * 
     * @param aValue
     * @param column
     */
/*    public void setValueAt(Object aValue, int column)
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
*/


    
    



    /**
     * Get DateD
     * @return
     */
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
