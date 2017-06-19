package ggc.core.plugins;

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
 *  Filename:     GGCPluginType
 *  Description:  GGC Plugin Type enum
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public enum GGCPluginType
{
    MeterToolPlugin("Meter Tool Plugin", true), //
    PumpToolPlugin("Pumps Plugin", true), //
    NutritionToolPlugin("Nutrition Plugin", true), //
    CGMSToolPlugin("CGMS Plugin", true), //
    ConnectToolPlugin("Connect Plugin", true, true), //

    // this two are not plugins themselves, but we need it for translation
    // context, so we define them here
    PluginBase("PluginBase", false), //
    Core("GGCCore", false) //
    ;

    private String key;
    private String description;
    private boolean isPlugin = false;
    private boolean enabled = true;


    GGCPluginType(String description, boolean plugin)
    {
        this(description, plugin, true);
    }


    GGCPluginType(String description, boolean plugin, boolean enabled)
    {
        this.key = this.name();
        this.description = description;
        this.isPlugin = plugin;
        this.enabled = enabled;
    }


    public String getKey()
    {
        return key;
    }


    public void setKey(String key)
    {
        this.key = key;
    }


    public boolean isPlugin()
    {
        return isPlugin;
    }


    public String getDescription()
    {
        return description;
    }
}
