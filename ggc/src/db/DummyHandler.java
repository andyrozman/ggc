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
 *  Filename: DummyHandler
 *  Purpose:  A Default Handler who does nothing.
 *
 *  Author:   schultd
 */

package db;


import datamodels.DailyValues;
import datamodels.HbA1cValues;

import java.util.Date;


public class DummyHandler extends DataBaseHandler
{
    public static DummyHandler singleton = null;

    public static DataBaseHandler getInstance()
    {
        if (singleton == null)
            singleton = new DummyHandler();
        return singleton;
    }

    public void closeConnection()
    {
        connected = false;
    }

    public void closeDataBase()
    {
        connectedToDB = false;
    }

    public void connect()
    {
        connected = false;
    }

    public void createNewDataBase(String name)
    {
    }

    public boolean dateTimeExists(Date date)
    {
        return false;
    }

    public DailyValues getDayStats(Date day)
    {
        return null;
    }

    public HbA1cValues getHbA1c(Date day)
    {
        return null;
    }

    public void openDataBase(boolean ask)
    {
    }

    public void saveDayStats(DailyValues dV)
    {
    }
}
