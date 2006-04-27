/******************************************************
 * File: DailyValues.java
 * created Mar 29, 2002 1:09:43 PM by tscherno
 */

package ggc.data;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

//import ggc.db.DataBaseHandler;
import ggc.util.I18nControl;

import ggc.data.DailyValues;
import ggc.data.DailyValuesRow;


public class MonthlyValues extends WeeklyValues
{
//    private I18nControl m_ic = I18nControl.getInstance();

    public Hashtable byDay = new Hashtable();

    private int m_month = 0;

    public MonthlyValues(int month)
    {
        super();
        m_month = month;
    }


    public void process()
    {

    }


    // type of this data is as follows
    //  table[4][3]:
    //     4 = B(reakfast), L(unch), D(inner), N(ight)
    //     5 = BG (avg), Insulin 1 (Sum), Insulin 2 (Sum), CH, Comment
    //


    public void getDayValues(int day)
    {

    }

    // arraylist of table[5]:
    //   5 = BG, Insulin 1, Insulin 2, CH, Comment
    public void getDayValuesExtended()
    {
    }





}
