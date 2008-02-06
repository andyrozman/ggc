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
 *  Filename: TimerThread
 *  Purpose:  This is timer thread. 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

package ggc.gui.calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JLabel;

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


    
    
    @Override
    public void run()
    {

        while(running)
        {
            
//x            long time1 = System.currentTimeMillis();
            
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(System.currentTimeMillis());
            this.time_1.setText(getParameter(gc, Calendar.HOUR_OF_DAY) + ":" + getParameter(gc, Calendar.MINUTE) + ":" + getParameter(gc, Calendar.SECOND));

            gc.setTimeInMillis(System.currentTimeMillis() + m_difference);
            this.time_2.setText(getParameter(gc, Calendar.HOUR_OF_DAY) + ":" + getParameter(gc, Calendar.MINUTE) + ":" + getParameter(gc, Calendar.SECOND));
            
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


