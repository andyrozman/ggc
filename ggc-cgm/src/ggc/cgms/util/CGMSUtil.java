package ggc.cgms.util;

import java.util.ArrayList;
import java.util.List;

import com.atech.db.ext.ExtendedHandler;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCI18nControl;
import ggc.plugin.data.DeviceValuesEntry;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     PluginDb
 *  Description:  This is master class for using Db instance within plug-in. In most cases, we
 *                would want data to be handled by outside authority (GGC), but in some cases
 *                we wouldn't want that.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSUtil
{

    private static DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();

    private static GGCI18nControl i18nControl = dataAccess.getI18nControlInstance();

    private static GlucoseUnitType BGUnit = DataAccess.getInstance().getGlucoseUnitType();


    public static GGCI18nControl getI18Control()
    {
        return i18nControl;
    }


    public static String getTranslatedString(String key)
    {
        return i18nControl.getMessage(key);
    }


    public static int getCurrentUserId()
    {
        return (int) dataAccess.getCurrentUserId();
    }


    public static ExtendedHandler getExtendedHandler(String key)
    {
        return dataAccess.getExtendedHandler(key);
    }


    public static ArrayList<CGMSValuesSubEntry> getDataList(List<DeviceValuesEntry> list)
    {
        ArrayList<CGMSValuesSubEntry> lst = new ArrayList<CGMSValuesSubEntry>();

        for (int i = 0; i < list.size(); i++)
        {
            lst.add((CGMSValuesSubEntry) list.get(i));
        }

        return lst;
    }


    public static float getBG(int type, float value)
    {
        if (type == DataAccess.BG_MGDL)
            return value;
        else
            return dataAccess.getBGValueByType(DataAccess.BG_MMOL, value);
    }


    public static float getBG(int type, int value)
    {
        if (type == DataAccess.BG_MGDL)
            return value;
        else
            return dataAccess.getBGValueByType(DataAccess.BG_MMOL, value);
    }


    public static float getBGInCorrectFormat(int value)
    {
        if (BGUnit == GlucoseUnitType.mg_dL)
            return value;
        else
            return dataAccess.getBGValueByType(DataAccess.BG_MMOL, value);
    }


    public static float getBGInCorrectFormat(float value)
    {
        if (BGUnit == GlucoseUnitType.mg_dL)
            return value;
        else
            return dataAccess.getBGValueByType(DataAccess.BG_MMOL, value);
    }

}
