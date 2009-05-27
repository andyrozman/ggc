/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: DailyValuesRow.java Purpose: One row in the DailyValues Data Model.
 * 
 * Author: schultd Author: andyrozman {andy@atech-software.com}
 */

using System;
using GGCMobileNET.Data.Db.Objects;
using GGCMobileNET.Data.Utils;
using System.Collections.Generic;
using System.Globalization;
using System.Text;
namespace GGCMobileNET.Data
{

public class DailyValuesRow : DayValueDAO, IComparable<DailyValuesRow>
    //implements Serializable, Comparable<DailyValuesRow>
{

    private string activity;
    private string urine;


    // DayValueH m_dv = null;
    DataAccessMobile m_da = DataAccessMobile.Instance;
    // GGCProperties props = m_da.getSettings();

    // ArrayList<String> meals_ids;

    bool debug = false;

    Single float_instance = 0.0f;

    public DailyValuesRow()
    {
        this.DtInfo = 0L;
        this.Bg = 0;
        this.Ins1 = 0;
        this.Ins2 = 0;
        this.Ch = 0.0f;
        this.Extended = null;
        this.Comment = "";

        loadExtended();
    }

    
    public DailyValuesRow(DayValueDAO dvd)
    {
        this.Id = dvd.Id;
        this.DtInfo = dvd.DtInfo;
        this.Bg = dvd.Bg;
        this.Ins1 = dvd.Ins1;
        this.Ins2 = dvd.Ins2;
        this.Ch = dvd.Ch;
        this.Extended = dvd.Extended;
        this.Comment = dvd.Comment;
        this.MealsIds = dvd.MealsIds;
        this.PersonId = dvd.PersonId;
        
        this.Changed = dvd.Changed;

        loadExtended();
    }
    
    
    
    public DailyValuesRow(long datetime, int bg, int ins1, int ins2, float ch, String extended, String comment)
    {

        this.DtInfo = datetime;
        this.Bg = bg;
        this.Ins1 = ins1;
        this.Ins2 = ins2;
        this.Ch = ch;
        this.Extended = extended;
        this.Comment = comment;

        loadExtended();
    }

    // active
    public DailyValuesRow(long id, long datetime, int bg, int ins1, int ins2, String meals, float ch, String extended,
            long person_id, String comment)
    {
        this.Id = id;
        this.DtInfo = datetime;
        this.Bg = bg;
        this.Ins1 = ins1;
        this.Ins2 = ins2;
        this.Ch = ch;
        this.MealsIds = meals;
        this.PersonId = (int)person_id;
        this.Extended = extended;
        this.Comment = comment;

        loadExtended();
    }

    public DailyValuesRow(long datetime, int bg, int ins1, int ins2, float ch, String activity, String urine,
            String comment)
    {

        this.DtInfo = datetime;
        this.Bg = bg;
        this.Ins1 = ins1;
        this.Ins2 = ins2;
        this.Ch = ch;
        this.Comment = comment;
        this.activity = activity;
        this.urine = urine;
        this.Comment = comment;

        this.createExtended();
    }



    public DailyValuesRow(long datetime, String BG, String Ins1, String Ins2, String BU, String Act, String urine,
            String Comment, List<String> lst_meals)
    {
        // X
        this.DtInfo = datetime;

        setValueAt(BG, 1);
        setValueAt(Ins1, 2);
        setValueAt(Ins2, 3);
        setValueAt(BU, 4);
        setValueAt(Act, 5);
        setValueAt(urine, 6);
        setValueAt(Comment, 7);
    }

    public void loadExtended()
    {
        if ((this.Extended == null) || (this.Extended.Trim().Length == 0))
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
        StringBuilder sb = new StringBuilder();

        if (this.isExtendedValueSet(DailyValuesRow.EXTENDED_URINE))
        {
            sb.Append("URINE=" + getExtendedValue(DailyValuesRow.EXTENDED_URINE) + ";");
        }

        if (this.isExtendedValueSet(DailyValuesRow.EXTENDED_ACTIVITY))
        {
            sb.Append("ACTIVITY=" + getExtendedValue(DailyValuesRow.EXTENDED_ACTIVITY) + ";");
        }

        return sb.ToString();
    }

    public const int EXTENDED_ACTIVITY = 1;
    public const int EXTENDED_URINE = 2;

    private bool isExtendedValueSet(int type)
    {
        String val = getExtendedValue(type);

        if ((val == null) || (val.Trim().Length == 0))
            return false;
        else
            return true;
    }

    public String getExtendedValue(int type)
    {
        switch (type)
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
        if (this.Extended.Contains(key + "="))
        {
            String val = this.Extended.Substring(this.Extended.IndexOf(key + "=") + key.Length + 1);
            if (val.Contains(";"))
            {
                val = val.Substring(0, val.IndexOf(";"));
            }

            return val;
        }
        else
            return "";

    }


    public bool areMealsSet()
    {
        if ((this.MealsIds == null) || (this.MealsIds.Trim().Length == 0))
            return false;
        else
            return true;
    }


    public long getDateTimeLong(String date, String time)
    {
        // dd.MM.yyyy
        String[] datex = date.Split(".".ToCharArray());
        String[] timex = time.Split(":".ToCharArray());

        //StringTokenizer strtok = new StringTokenizer(date, ".");
        String[] dt = new String[5];
        dt[2] = datex[0]; // strtok.nextToken(); // day
        dt[1] = datex[1]; // strtok.nextToken(); // month
        dt[0] = datex[2]; // strtok.nextToken(); // year

        //strtok = new StringTokenizer(time, ":");
        dt[3] = timex[0]; // strtok.nextToken(); // hour
        dt[4] = timex[1]; // strtok.nextToken(); // minute

        String dt_out = dt[0] + m_da.getLeadingZero(dt[1], 2) + m_da.getLeadingZero(dt[2], 2)
                + m_da.getLeadingZero(dt[3], 2) + m_da.getLeadingZero(dt[4], 2);

        return Convert.ToInt64(dt_out);

    }


    /*
    public String[] getRowString()
    {
        // to-do
        String[] tmp = new String[8];
        tmp[0] = "" + this.DtInfo;
        tmp[1] = "" + bg;
        tmp[2] = "" + ins1;
        tmp[3] = "" + ins2;
        tmp[4] = "" + ch;
        tmp[5] = "" + activity;
        tmp[6] = "" + urine;
        tmp[7] = comment;

        return tmp;
    }*/

    public String toString()
    {
        /*
         * this.id = id; this.datetime = datetime; this.bg = bg; this.ins1 =
         * ins1; this.ins2 = ins2; this.ch = ch; this.meals = meals;
         * this.extended = extended; this.person_id = person_id; this.comment =
         * comment;
         */

        String ins = "" + this.Ins1;

        if (this.Ins2 > 0)
        {
            ins += "/" + this.Ins2;
        }

        String m = "";

        if (this.areMealsSet())
        {
            m = " [F]";
        }

        return this.getTimeAsString() + "      " + ins + "     " + this.getBGAsString() + "/" + this.Ch + m;
    }

    public long getDateTime()
    {
        return this.DtInfo;
    }

    public void setDateTime(long dt)
    {
        this.DtInfo = dt;
    }

    public DateTime getDateTimeAsDate()
    {
        return m_da.getDateTimeAsDateObject(this.DtInfo);
    }

    public long getDate()
    {
        return (this.DtInfo / 10000);
    }

    public String getDateAsString()
    {
        return m_da.getDateTimeAsDateString(this.DtInfo);
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // return sdf.format(datetime);
        // dssreturn m_da.getDateTimeAsDateString(datetime);
    }

    public String getTimeAsString()
    {
        // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // return sdf.format(datetime);
        return m_da.getDateTimeAsTimeString(this.DtInfo);
    }

    public String getDateTimeAsTime()
    {
        long i = m_da.getDateTimeLong(this.DtInfo, DataAccessMobile.DT_TIME);
        // System.out.println("X: " + i);
        return "" + i;
    }

    public void setDateTime(String date, String time)
    {
        this.DtInfo = getDateTimeLong(date, time);
    }

    public float getBG()
    {
        if (debug)
        {
            // System.out.println("getBg [type=" + props.getBG_unit() + "]");
            Console.WriteLine("getBg [internal_value=" + this.Bg + "]");
        }

        if (m_da.getBGMeasurmentType() == DataAccessMobile.BG_MMOL) // 2
        {
            float v = m_da.getBGValueByType(DataAccessMobile.BG_MMOL, this.Bg);

            if (debug)
                Console.WriteLine("getBg [type=2,return=" + v + "]");

            return v;

            // return m_da.getBGValueByType(DataAccess.BG_MMOL, bg);
        }
        else
        {
            if (debug)
                Console.WriteLine("getBg [type=1,return=" + this.Bg + "]");

            return this.Bg;
        }

    }

    public String getBGAsString()
    {
        if (debug)
        {
            // System.out.println("getBgAsString [type=" + props.getBG_unit() +
            // "]");
            Console.WriteLine("getBgAsString [internal_value=" + this.Bg + "]");
        }

        if (m_da.getBGMeasurmentType() == DataAccessMobile.BG_MMOL) // 2
        {
            float v = m_da.getBGValueByType(DataAccessMobile.BG_MMOL, this.Bg);

            

            if (debug)
                Console.WriteLine("getBgAsString [type=2,return=" + v + "]");

            String ss = DataAccessMobile.GetFloatAsString(v, 1);
            ss = ss.Replace(",", ".");
            return ss;

            // return m_da.getBGValueByType(DataAccess.BG_MMOL, bg);
        }
        else
        {
            if (debug)
                Console.WriteLine("getBgAsString [type=1,return=" + this.Bg + "]");

            return "" + this.Bg;
        }

    }

    public float getBGRaw()
    {
        return this.Bg;
    }

    public float getBG(int type)
    {

        if (debug)
            Console.WriteLine("Internal value: " + this.Bg);

        if (type == DataAccessMobile.BG_MGDL)
            return this.Bg;
        else
        {
            return m_da.getBGValueByType(DataAccessMobile.BG_MMOL, this.Bg);
        }

    }

    /*
     * public void setBG(String val) { setBG(m_da.getIntValue(val)); }
     */

    public static int BG_MMOLL = 2;
    public static int BG_MGDL = 1;

    public void setBG(int type, float val)
    {
        // FIX THIS

        if (debug)
            Console.WriteLine("SET BG: type=" + type + ",value=" + val);

        if (type == BG_MMOLL)
        {
            if (debug)
            {
                Console.WriteLine("SET BG :: MMOL");
                Console.WriteLine("     " + (int)m_da.getBGValueDifferent(type, val));
            }

            this.Bg = (int) m_da.getBGValueDifferent(type, val);
        }
        else
        {
            this.Bg = (int) val;
        }

        /*
         * 4
         * 
         * int unit = getBG_unit();
         * 
         * if (unit==1) return "mg/dl"; else if (unit==2) return "mmol/l"; else
         * return m_da.getI18nInstance().getMessage("UNKNOWN");
         */

    }

    public void setBG(int type, String val)
    {
        if (m_da.isEmptyOrUnset(val))
        {
            this.Bg = 0;
            return;
        }

        float f;
        try
        {
            f = Convert.ToSingle(val);
            //f = Float.parseFloat(val);
        }
        catch (Exception e)
        {
            f = 0;
            Console.WriteLine("ERROR: unparsable float string: " + val);
        }

        setBG(type, f);

    }

    public String getIns1AsString()
    {
        return "" + this.Ins1;
    }

    public void setIns1(String val)
    {
        if (!m_da.isEmptyOrUnset(val))
            this.Ins1 = Convert.ToInt32(val);
    }

    public String getIns2AsString()
    {
        return "" + Ins2;
    }

    public void setIns2(String val)
    {
        if (!m_da.isEmptyOrUnset(val))
        {
            this.Ins2 = Convert.ToInt32(val);
        }
        else
        {
            this.Ins2 = 0;
        }
    }

    public float getCH()
    {
        return this.Ch;
    }

    public String getCHAsString()
    {
        return this.getFloatAsString(this.Ch);
    }

    public void setCH(String val)
    {
        if (!m_da.isEmptyOrUnset(val))
        {
            this.Ch = Convert.ToSingle(val);
        }
        else
        {
            this.Ch = 0;
        }
    }

    public void setCH(float val)
    {
        this.Ch = val;
    }

    public String getActivity()
    {
        return activity;
    }

    /*
     * public void setAct(String val) { act = val; }
     */
    public void setActivity(String val)
    {
        if ((activity == null) || (activity != val))
        {
            activity = val;
            this.Extended = this.createExtended();
        }
    }

    public String getUrine()
    {
        return urine;
    }

    /*
     * public void setAct(String val) { act = val; }
     */
    public void setUrine(String val)
    {
        if ((urine == null) || (urine != val))
        {
            urine = val;
            this.Extended = this.createExtended();
        }
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
            return DataAccessMobile.GetFloatAsString(fl, 1);
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
            return DataAccessMobile.GetFloatAsString(fl, 2);
//            return DataAccessMobile.Decimal2Format.format(fl);
        }
    }

    public int getFloatAsInt(float f)
    {
        //Single f_i = new Float(f);
        return Convert.ToInt32(f); //.intValue();
    }

    // stupid but TableModel needs Objects...
    public Object getValueAt(int column)
    {
        // return "X";

        switch (column)
        {
        case 0:
            return this.DtInfo; // m_da.getDateTimeAsTimeString(
                                           // datetime);
        case 1:
            if (this.getBG() == 0.0f)
                // if (getBGAsString().equals("0"))
                return "";
            return this.getBGAsString();
        case 2:
            return this.getIns1AsString();
        case 3:
            return this.getIns2AsString();
        case 4:
            return this.getFloat2AsString(this.Ch);
        case 5:
            return activity;
        case 6:
            return urine;
        case 7:
            return this.Comment;
        default:
            {
                // log.warn("Shouldn't have reached by here: " + column);
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
                if (aValue.GetType()==typeof(String)) // instanceof string)
                    this.DtInfo = Convert.ToInt64(aValue);
                else if (aValue.GetType()==typeof(Int32))
                    this.DtInfo = Convert.ToInt32(aValue);
                else if (aValue.GetType()==typeof(Int64))
                    this.DtInfo = Convert.ToInt64(aValue);
            }
            break;
        case 1:
            {
                this.Bg = Convert.ToInt32(aValue);
            }
            break;
        case 2:
            {
                this.Ins1 = Convert.ToInt32(aValue);
            }
            break;
        case 3:
            {
                this.Ins2 = Convert.ToInt32(aValue);
                //this.setIns2(m_da.getIntValue(aValue));
            }
            break;
        case 4:
            {
                this.Ch = Convert.ToSingle(aValue);
            }
            break;
        case 5:
            {
                this.setActivity((String)aValue);
                //this.activity = (String)aValue; // m_da.getIntValue(
                //this.setExtended(this.createExtended());
            }
            break;
        case 6:
            {
                this.setUrine((String)aValue);
                //this.urine = (String)aValue;
                //this.setExtended(this.createExtended());
            }
            break;
        case 7:
            {
                this.Comment = (String)aValue;
            }
            break;
        }
    }

    public int getDateD()
    {
        return Convert.ToInt32(m_da.getDateTimeAsDateString(this.DtInfo));
        // return -1;
        /*
         * String dat = "";
         * 
         * dat += datetime.getYear() + 1900; dat +=
         * getLeadingZero(datetime.getMonth() + 1, 2); dat +=
         * getLeadingZero(datetime.getDate(), 2);
         * 
         * return Integer.parseInt(dat);
         */
    }

    public int getDateT()
    {
        return Convert.ToInt32(m_da.getDateTimeAsTimeString(this.DtInfo));
        // return -1;
        // return datetime.getHours()*100 + datetime.getMinutes();
    }

    public void SetDateTime(DateTime date, DateTime time)
    {
        string dt = date.Year + m_da.getLeadingZero(date.Month, 2) +
            m_da.getLeadingZero(date.Day, 2) + m_da.getLeadingZero(time.Hour, 2) +
            m_da.getLeadingZero(time.Minute, 2);

        this.DtInfo = Convert.ToInt64(dt);
    }



    public void SetComment(string comm)
    {
        this.Comment = comm;
    }

    public void SetChanged(long changed)
    {
        this.Changed = changed;
    }




    #region IComparable<DailyValuesRow> Members

    public int CompareTo(DailyValuesRow other)
    {
        return (int)(this.DtInfo - other.DtInfo); 
    }

    #endregion
}
}