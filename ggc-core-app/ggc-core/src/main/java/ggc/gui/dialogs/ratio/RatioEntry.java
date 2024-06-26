package ggc.gui.dialogs.ratio;

import java.util.StringTokenizer;

import com.atech.utils.data.ATechDate;

import ggc.core.util.DataAccess;

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

    DataAccess dataAccess = DataAccess.getInstance();
    public int from;
    public int to;

    public float ch_insulin = 0.0f;
    public float bg_insulin = 0.0f;
    public float ch_bg = 5.0f;

    int procent = 100;


    public RatioEntry()
    {
    }


    public RatioEntry(String entry)
    {

        StringTokenizer strtok = new StringTokenizer(entry, ";");

        int i = 0;

        while (strtok.hasMoreTokens())
        {
            this.setColumnValue(i, strtok.nextToken());
            i++;
        }

    }


    public void setColumnValue(int column, String val)
    {

        // EXTENDED_RATIO_1=From;To;Ch/Ins;Bg/Ins;Ch/BG;Procent

        // dta.put("EXTENDED_RATIO_COUNT", "2");
        // dta.put("EXTENDED_RATIO_1", "0000;1159;6.67f;1.33f;5f;100");

        switch (column)
        {
            case 0:
                this.from = dataAccess.getIntValueFromString(val.replace(":", ""), 0);
                break;

            case 1:
                this.to = dataAccess.getIntValueFromString(val.replace(":", ""), 0);
                break;

            case 2:
                this.ch_insulin = dataAccess.getFloatValueFromString(val, 0.0f);
                break;

            case 3:
                this.bg_insulin = dataAccess.getFloatValueFromString(val, 0.0f);
                break;

            case 4:
                this.ch_bg = dataAccess.getFloatValueFromString(val, 0.0f);
                break;

            case 5:
                this.procent = dataAccess.getIntValueFromString(val, 0);
                break;

            default:
                break; // return "";
        }

    }


    public int getMinuteFrom()
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, this.from);
        return atd.hourOfDay * 60 + atd.minute;
    }


    public int getMinuteTo()
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, this.to);
        return atd.hourOfDay * 60 + atd.minute;
    }


    /**
     * Get Column Value
     *
     * @param column
     * @return
     */
    public String getColumnValue(int column)
    {
        switch (column)
        {
            case 0:
                return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, this.from);

            case 1:
                return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, this.to);

            case 2:
                return dataAccess.getFloatAsString(this.ch_insulin, 2);

            case 3:
                return dataAccess.getFloatAsString(this.bg_insulin, 2);

            case 4:
                return dataAccess.getFloatAsString(this.ch_bg, 2);

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
        return 6;
    }


    /**
     * Get Column Name
    
     * @param index
     * @return
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int index)
    {
        switch (index)
        {
            case 0:
                return "TIME_FROM";

            case 1:
                return "TIME_TILL";

            case 2:
                return "CH_INSULIN_RATIO_SHORT";

            case 3:
                return "BG_INSULIN_RATIO_SHORT";

            case 4:
                return "BG_CH_RATIO_SHORT";

            case 5:
                return "PROCENT_OF_BASE_SHORT";

            default:
                return "";
        }

    }


    /**
     * Get Save Data
     *
     * @return
     */
    public String getSaveData()
    {
        // dta.put("EXTENDED_RATIO_1", "0000;1159;6.67f;1.33f;5f;100");
        return getColumnValue(0) + ";" + getColumnValue(1) + ";" + getColumnValue(2) + ";" + getColumnValue(3) + ";"
                + getColumnValue(4) + ";" + getColumnValue(5);
    }

}
