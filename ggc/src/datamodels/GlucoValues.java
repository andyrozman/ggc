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
 *  Filename: GlucoValues.java
 *  Purpose:  The data read from the meter and translated into a more general
 *            form.
 *
 *  Author:   schultd
 */

package datamodels;


import db.DataBaseHandler;

import java.util.Vector;


public class GlucoValues extends DailyValues
{
    Vector dayValues = null;

    public GlucoValues()
    {
        dayValues = new Vector();
    }

    public void addRow(DailyValuesRow dRow)
    {
        for (int i = 0; i < dayValues.size(); i++) {
            DailyValues dV = (DailyValues)dayValues.elementAt(i);
            //System.out.println("date1:" + dV.getDate());
            //System.out.println("date2:" + dRow.getDate());
            if (dV.getDate().equals(dRow.getDate())) {
                dV.setNewRow(dRow);
                return;
            }
        }

        if (dRow.getDate() != null && dRow.getTime() != null) {
            DailyValues dV = new DailyValues();

            dV.setNewRow(dRow);
            dV.setDate(dRow.getDate().getTime());
            dV.setIsNew(true);

            dayValues.add(dV);
        }
    }

    public void setNewRow(DailyValuesRow dRow)
    {
        DataBaseHandler dbH = DataBaseHandler.getInstance();
        if (!dbH.dateTimeExists(dRow.getDate(), dRow.getTime())) {
            addRow(dRow);
        }
    }

    public void saveValues()
    {
        for (int i = 0; i < dayValues.size(); i++)
            ((DailyValues)dayValues.elementAt(i)).saveDay();
    }

    public int getRowCount()
    {
        int c = 0;
        for (int i = 0; i < dayValues.size(); i++)
            c += ((DailyValues)dayValues.elementAt(i)).getRowCount();
        return c;
    }

    public void deleteRow(int row)
    {
        if (row != -1) {
            int c = 0;
            Object o = null;
            for (int i = 0; i < dayValues.size(); i++) {
                int old = c;
                DailyValues dV = (DailyValues)dayValues.elementAt(i);
                c += dV.getRowCount();
                if (old <= row && row < c)
                    dV.deleteRow(row - old);
            }
        }
    }

    public Object getValueAt(int row, int column)
    {
        int c = 0;
        Object o = null;
        for (int i = 0; i < dayValues.size(); i++) {
            int old = c;
            DailyValues dV = (DailyValues)dayValues.elementAt(i);
            c += dV.getRowCount();
            if (old <= row && row < c) {
                o = dV.getValueAt(row - old, column);
                //System.out.println(o.toString());
            }
        }
        return o;
    }

    public void setValueAt(Object aValue, int row, int column)
    {
        int c = 0;
        Object o = null;
        for (int i = 0; i < dayValues.size(); i++) {
            int old = c;
            DailyValues dV = (DailyValues)dayValues.elementAt(i);
            c += dV.getRowCount();
            if (old <= row && row < c) {
                dV.setValueAt(aValue, row - old, column);
            }
        }
    }
}
