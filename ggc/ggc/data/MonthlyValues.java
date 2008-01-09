/******************************************************
 * File: DailyValues.java
 * created Mar 29, 2002 1:09:43 PM by tscherno
 */

package ggc.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ggc.data.DailyValues;


public class MonthlyValues extends WeeklyValues
{
//    private I18nControl m_ic = I18nControl.getInstance();

    //public Hashtable byDay = new Hashtable();

    //DataAccess m_da = DataAccess.getInstance();

    private int m_month = 0;
    private int m_year = 0;
    private int max_days = 0;


    String empty_value = "";
    int times[] = { 1100, 1800, 2200 };



    public MonthlyValues(int year, int month)
    {
        super();
        m_month = month;
    	m_year = year;
    
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR, year);
        gc.set(Calendar.MONTH, (month-1));
    
    	max_days = gc.getActualMaximum(Calendar.DAY_OF_MONTH);

        //max_days = gc.getM


    }


    public void loadConfiguration()
    {
	this.empty_value = this.m_da.getSettings().getPrintEmptyValue();
	this.times[0] = this.m_da.getSettings().getPrintLunchStartTime();
	this.times[1] = this.m_da.getSettings().getPrintDinnerStartTime();
	this.times[2] = this.m_da.getSettings().getPrintNightStartTime();
    }

/*
    public void getTime(String time, int default_value)
    {

    }
*/



    public int getMonth()
    {
        return this.m_month;
    }

    public int getYear()
    {
        return this.m_year;
    }

    public int getDaysInMonth()
    {
        return max_days;
    }


    public void process()
    {

    }




    public int whichGroup(String timeStr)
    {

        int time = Integer.parseInt(timeStr);

	// hard coded times

        if ((time>0) & (time<times[0]))
            return 0;
        else if (time<times[1])
            return 1;
        else if (time<times[2])
            return 2;
        else
            return 3;

    }


    // type of this data is as follows
    //  table[4][3]:
    //     4 = B(reakfast), L(unch), D(inner), N(ight)
    //     3 = BG (avg), Ins (1+1), CH(sum)
    //
    public String[][] getDayValuesSimple(int day)
    {
        DailyValues dv = this.getDayValues(m_year, m_month, day);
        String[][] dataStr = new String[4][3];

        if (dv==null)
        {
            System.out.println(day + " = null");
            dataStr[0][0] = this.empty_value;
            dataStr[0][1] = this.empty_value;
            dataStr[0][2] = this.empty_value;

            dataStr[1][0] = this.empty_value;
            dataStr[1][1] = this.empty_value;
            dataStr[1][2] = this.empty_value;

            dataStr[2][0] = this.empty_value;
            dataStr[2][1] = this.empty_value;
            dataStr[2][2] = this.empty_value;

            dataStr[3][0] = this.empty_value;
            dataStr[3][1] = this.empty_value;
            dataStr[3][2] = this.empty_value;

            return dataStr;
        }

        //float[][] data = new float[4][5];
        float[][] data = initTable(new float[4][5]);
        //     5 = BG (avg), BG (count), Insulin 1 (Sum), Insulin 2 (Sum), CH(sum)

        for (int i=0; i<dv.getRowCount(); i++)
        {
            //System.out.println(" Day: " + day + " entry: " + i + " of " + dv.getRowCount());

            int grp = whichGroup(dv.getDateTimeAsTimeStringAt(i));


            float d= dv.getBGAt(i);
	    //     5 = BG (avg), BG (count), Insulin 1 (Sum), Insulin 2 (Sum), CH(sum)

            if (d>0.0)
            {
                data[grp][0] += d;
                data[grp][1]++;
            }
	    //else
	    //data[grp][0]

            data[grp][2] += dv.getIns1At(i);
            data[grp][3] += dv.getIns2At(i);
            data[grp][4] += dv.getCHAt(i);
        }

	//     4 = B(reakfast), L(unch), D(inner), N(ight)
	//     3 = BG (avg), Ins (1+1), CH(sum)


        for (int i=0; i<4; i++)
        {
	    // BG
            if (data[i][1]>=1)
            {
                if (m_da.getSettings().getBG_unit()==1)
                    dataStr[i][0] = "" + (int)(data[i][0]/data[i][1]); // mg/dl
                else
                    dataStr[i][0] = "" + data[i][0]/data[i][1];  // mmol/l
            }
            else
                dataStr[i][0] = this.empty_value;

	    // insulin
            if ((data[i][2]>0) && (data[i][3]>0))
            {
                // both insulins present
                dataStr[i][1] = "" + (int)data[i][2] + "+" + (int)data[i][3];
            }
            else if ((data[i][2]>0) && (data[i][3]==0))
            {
                // first insulin present
                dataStr[i][1] = "" + (int)data[i][2];
            }
            else if (data[i][3]>0)
            {
                // second insulin present
                dataStr[i][1] = "0+" + (int)data[i][3];
            }
            else
                dataStr[i][1] = this.empty_value;

	    // CH
	    if (data[i][4]>0)
		dataStr[i][2] = "" + (int)data[i][4];
	    else
                dataStr[i][2] = this.empty_value;

        }

        return dataStr;

    }

    public float[][] initTable(float[][] data)
    {
        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
            {
                data[i][j] = 0.0f;
            }

	    return data;
    }

    // arraylist of table[5]:
    //   5 = BG, Insulin 1, Insulin 2, CH, Comment
    public void getDayValuesExtended()
    {
    }


}
