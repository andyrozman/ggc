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
 *  Filename: GGCProperties.java
 *  Purpose:  Store all Properties for the GGC app.
 *
 *  Author:   schultd
 */

package util;


public class GGCProperties extends GGCPropertiesHelper
{
    private static GGCProperties singleton = null;

    private GGCProperties()
    {
        super.read();
        setDefault("DBName", "glucodb");
        setDefault("UserName", "Unnamed User");
        setDefault("Ins1Name", "Insulin 1");
        setDefault("Ins2Name", "Insulin 2");
        setDefault("Ins1Abbr", "Ins1");
        setDefault("Ins2Abbr", "Ins2");
        setDefault("HighBG", "200");
        setDefault("LowBG", "60");
        setDefault("TargetHighBG", "120");
        setDefault("TargetLowBG", "80");
        setDefault("DataSource", "MySQL");
        setDefault("MySQLHost", "localhost");
        setDefault("MySQLPort", "3306");
        setDefault("MySQLUser", "testapp");
        setDefault("MySQLPass", "gluco");
        setDefault("Rendering", "0");
        setDefault("AntiAliasing", "0");
        setDefault("ColorRendering", "0");
        setDefault("Dithering", "0");
        setDefault("FractionalMetrics", "0");
        setDefault("Interpolation", "0");
        setDefault("TextAntiAliasing", "0");
    }

    public static GGCProperties getInstance()
    {
        if (singleton == null)
            singleton = new GGCProperties();
        return singleton;
    }

    public String getDBName()
    {
        return get("DBName");
    }

    public String getUserName()
    {
        return get("UserName");
    }

    public String getIns1Name()
    {
        return get("Ins1Name");
    }

    public String getIns2Name()
    {
        return get("Ins2Name");
    }

    public String getIns1Abbr()
    {
        return get("Ins1Abbr");
    }

    public String getIns2Abbr()
    {
        return get("Ins2Abbr");
    }

    public float getHighBG()
    {
        return getFloat("HighBG");
    }

    public String getHighBGAsString()
    {
        return get("HighBG");
    }

    public float getLowBG()
    {
        return getFloat("LowBG");
    }

    public String getLowBGAsString()
    {
        return get("LowBG");
    }

    public float getTargetHighBG()
    {
        return getFloat("TargetHighBG");
    }

    public String getTargetHighBGAsString()
    {
        return get("TargetHighBG");
    }

    public float getTargetLowBG()
    {
        return getFloat("TargetLowBG");
    }

    public String getTargetLowBGAsString()
    {
        return get("TargetLowBG");
    }

    public String getDataSource()
    {
        return get("DataSource");
    }

    public String getMySQLHost()
    {
        return get("MySQLHost");
    }

    public String getMySQLPort()
    {
        return get("MySQLPort");
    }

    public String getMySQLUser()
    {
        return get("MySQLUser");
    }

    public String getMySQLPass()
    {
        return get("MySQLPass");
    }

    public int getRendering()
    {
        return getInt("Rendering");
    }

    public int getAntiAliasing()
    {
        return getInt("AntiAliasing");
    }

    public int getColorRendering()
    {
        return getInt("ColorRendering");
    }

    public int getDithering()
    {
        return getInt("Dithering");
    }

    public int getFractionalMetrics()
    {
        return getInt("FractionalMetrics");
    }

    public int getInterpolation()
    {
        return getInt("Interpolation");
    }

    public int getTextAntiAliasing()
    {
        return getInt("TextAntiAliasing");
    }
}
