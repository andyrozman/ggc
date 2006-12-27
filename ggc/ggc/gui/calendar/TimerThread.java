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
 *  Filename: GGCDb
 *  Purpose:  This is main datalayer file. It contains all methods for initialization of
 *      Hibernate framework, for adding/updating/deleting data from database (hibernate).
 *      It also contains all methods for mass readings of data from hibernate. 
 *
 *  Author:   andyrozman
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// 
// Methods are added to this class, whenever we make changes to our existing database,
// either add methods for handling data or adding new tables.
// 
// andyrozman


package ggc.gui.calendar;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JLabel;

import ggc.GGC;
import ggc.gui.MainFrame;
import ggc.util.DataAccess;


public class TimerThread extends Thread
{

    DataAccess m_da = null;
    public int m_difference = 0;
    JLabel time_1, time_2;
    private boolean running = true;
    

    public TimerThread(DataAccess da, JLabel time_1, JLabel time_2, int time_difference)
    {
        m_da = da;
        m_difference = time_difference;
        this.time_1 = time_1;
        this.time_2 = time_2;
    }


    
    
    public void run()
    {

        while(running)
        {
            
            long time1 = System.currentTimeMillis();
            
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(System.currentTimeMillis());
            this.time_1.setText(getParameter(gc, GregorianCalendar.HOUR_OF_DAY) + ":" + getParameter(gc, GregorianCalendar.MINUTE) + ":" + getParameter(gc, GregorianCalendar.SECOND));

            gc.setTimeInMillis(System.currentTimeMillis() + m_difference);
            this.time_2.setText(getParameter(gc, GregorianCalendar.HOUR_OF_DAY) + ":" + getParameter(gc, GregorianCalendar.MINUTE) + ":" + getParameter(gc, GregorianCalendar.SECOND));
            
        }


    }

    private String getParameter(GregorianCalendar gc, int parameter)
    {
        int num = gc.get(parameter);

        if (num>9)
        {
            return "" + num;
        }
        else //if (num>0)
        {
            return "0" + num;
        }
    }
    

}


