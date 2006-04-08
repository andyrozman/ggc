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
 *  Purpose:  A Default Handler that does nothing.
 *
 *  Author:   schultd
 */

package ggc.db;

import ggc.datamodels.DailyValues;
import ggc.datamodels.HbA1cValues;
import ggc.datamodels.WeekValues;

import java.util.Date;
import java.util.GregorianCalendar;

public class DummyHandler extends DataBaseHandler {
    public HbA1cValues getHbA1c(GregorianCalendar day) {
        return (new HbA1cValues());
    }

    public DailyValues getDayStats(GregorianCalendar day) {
        return (new DailyValues());
    }

    public WeekValues getDayStatsRange(GregorianCalendar sDay,
            GregorianCalendar eDay) {
        return (new WeekValues());
    }

    public static DummyHandler singleton = null;

    public static DataBaseHandler getInstance() {
        if (singleton == null)
            singleton = new DummyHandler();
        return singleton;
    }

    private DummyHandler() {
        // dbStatus = "Dummy Handler []";
        dbName = "";
        dbType = "Dummy Db:";
    }

    public void connectDb() {
        connected = false;
        setStatus();
    }

    public boolean testDb() {
        return true;
    }

    public void initDb() {
    }

    public void disconnectDb() {
        connected = false;
        setStatus();
    }

    public boolean isInitialized() {
        return false;
    }

    public boolean dateTimeExists(Date date) {
        return false;
    }

    public DailyValues getDayStats(Date day) {
        return null;
    }

    public HbA1cValues getHbA1c(Date day) {
        return null;
    }

    public void saveDayStats(DailyValues dV) {
    }

}
