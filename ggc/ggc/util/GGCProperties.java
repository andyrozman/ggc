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

package ggc.util;


import java.awt.*;


public class GGCProperties extends GGCPropertiesHelper
{
    private static GGCProperties singleton = null;

    private GGCProperties()
    {
        super.read();
        //setDefault("DBName", "glucodb");
        setDefault("UserName", "Unnamed User");
        setDefault("Ins1Name", "Insulin 1");
        setDefault("Ins2Name", "Insulin 2");
        setDefault("Ins1Abbr", "Ins1");
        setDefault("Ins2Abbr", "Ins2");
        setDefault("HighBG", "200");
        setDefault("LowBG", "60");
        setDefault("TargetHighBG", "120");
        setDefault("TargetLowBG", "80");
        setDefault("DataSource", "Textfile");
        setDefault("MySQLHost", "localhost");
        setDefault("MySQLPort", "3306");
        setDefault("MySQLUser", "ggc");
        setDefault("MySQLPass", "gluco");
        setDefault("MySQLDBName", "glucodb");
        setDefault("MySQLOpenDefaultDB", "false");
        setDefault("TextFilePath", System.getProperty("user.home") + "/MyValues.gcv");
        setDefault("TextFileOpenDefaultFile", "false");
        setDefault("AutoConnect", "false");
        setDefault("Rendering", "0");
        setDefault("AntiAliasing", "0");
        setDefault("ColorRendering", "0");
        setDefault("Dithering", "0");
        setDefault("FractionalMetrics", "0");
        setDefault("Interpolation", "0");
        setDefault("TextAntiAliasing", "0");
        setDefault("ColorTargetBG", "-1184275");
        setDefault("ColorBG", "-65485");
        setDefault("ColorAvgBG", "-6750208");
        setDefault("ColorHighBG", "-81409");
        setDefault("ColorLowBG", "-163654");
        setDefault("ColorBU", "-16724941");
        setDefault("ColorIns1", "-6710785");
        setDefault("ColorIns2", "-16776961");
        setDefault("ColorIns", "-16724788");
        setDefault("ColorInsPerBU", "-6711040");
        setDefault("MeterType", "GlucoCard");
        setDefault("MeterPort", "none");
        setDefault("Meter", "GlucoCard;EuroFlash;FreeStyle");
        setDefault("Meter.GlucoCard.class", "data.imports.GlucoCardImport");
        setDefault("Meter.EuroFlash.class", "data.imports.EuroFlashImport");
        setDefault("Meter.FreeStyle.class", "data.imports.FreeStyleImport");
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

    public String getMySQLDBName()
    {
        return get("MySQLDBName");
    }

    public boolean getMySQLOpenDefaultDB()
    {
        return getBoolean("MySQLOpenDefaultDB");
    }

    public String getTextFilePath()
    {
        return get("TextFilePath");
    }

    public boolean getTextFileOpenDefaultFile()
    {
        return getBoolean("TextFileOpenDefaultFile");
    }

    public boolean getAutoConnect()
    {
        return getBoolean("AutoConnect");
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

    public Color getColorTargetBG()
    {
        return getColor("ColorTargetBG");
    }

    public Color getColorBG()
    {
        return getColor("ColorBG");
    }

    public Color getColorByName(String identifier)
    {
        return getColor(identifier);
    }

    public Color getColorHighBG()
    {
        return getColor("ColorHighBG");
    }

    public Color getColorLowBG()
    {
        return getColor("ColorLowBG");
    }

    public Color getColorAvgBG()
    {
        return getColor("ColorAvgBG");
    }

    public Color getColorBU()
    {
        return getColor("ColorBU");
    }

    public Color getColorIns1()
    {
        return getColor("ColorIns1");
    }

    public Color getColorIns2()
    {
        return getColor("ColorIns2");
    }

    public Color getColorIns()
    {
        return getColor("ColorIns");
    }

    public Color getColorInsPerBU()
    {
        return getColor("ColorInsPerBU");
    }

    public String getMeterType()
    {
        return get("MeterType");
    }

    public String getMeterPort()
    {
        return get("MeterPort");
    }
}
