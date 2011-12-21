package ggc.shared.ratio;

import ggc.core.util.DataAccess;

import java.util.StringTokenizer;

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
 *  Filename:     RatioEntry  
 *  Description:  
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class RatioEntry
{
    DataAccess m_da = DataAccess.getInstance();
    int from;
    int to;
    
    float ch_insulin = 0.0f;
    float bg_insulin = 0.0f;
    float ch_bg = 5.0f;
    
    float procent = 100.0f;
    
    public RatioEntry()
    {
    }
    
    public RatioEntry(String entry)
    {
        
        StringTokenizer strtok = new StringTokenizer(entry, ";");
        
        int i=0;
        
        while(strtok.hasMoreTokens())
        {
            this.setColumnValue(i, strtok.nextToken());
            i++;
        }
        
    }
    
    public void setColumnValue(int column, String val)
    {
        
        // EXTENDED_RATIO_1=From;To;Ch/Ins;Bg/Ins;Ch/BG;Procent
        
        
//        dta.put("EXTENDED_RATIO_COUNT", "2");
//        dta.put("EXTENDED_RATIO_1", "0000;1159;6.67f;1.33f;5f;100");
        
        switch(column)
        {
            case 0:
                this.from = m_da.getIntValueFromString(val, 0);
                break;
            case 1:
                this.to = m_da.getIntValueFromString(val, 0);
                break;
                
            case 2:
                this.ch_insulin = m_da.getFloatValueFromString(val, 0.0f);
                break;
                
            case 3:
                this.bg_insulin = m_da.getFloatValueFromString(val, 0.0f);
                break;
                
            
                //this.ch_bg = m_da.getFloatValueFromString(val, 0.0f);
                //break;
                
            case 5:
                this.procent = m_da.getIntValueFromString(val, 0);
                break;
            
            case 4:
            default:
                break; //return "";
        }
        
    }
    
    
    public int getMinuteFrom()
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, this.from);
        return (atd.hour_of_day * 60) + atd.minute;
    }
    

    public int getMinuteTo()
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, this.to);
        return (atd.hour_of_day * 60) + atd.minute;
    }
    
    
    /**
     * Get Column Value
     * 
     * @param column
     * @return
     */
    public String getColumnValue(int column)
    {
        switch(column)
        {
            case 0:
                return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, this.from);
                
            case 1:
                return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, this.to);
                
            case 2:
                return DataAccess.getFloatAsString(this.ch_insulin, 2);
                
            case 3:
                return DataAccess.getFloatAsString(this.bg_insulin, 2);
                
            case 4:
                return DataAccess.getFloatAsString(this.ch_bg, 2);
                
            case 5:
                return "" + this.procent;
                
            default:
                return "";
        }
        
        
    }
    
    
    /**
     * Get Columns
     * 
     * @return
     */
    public int getColumns()
    {
        return 5;
    }
    
    
    public String getSaveData()
    {
        return null;
    }
    
    
    
    
}
