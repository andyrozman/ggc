package ggc.cgms.data;

import ggc.plugin.output.OutputWriterConfigData;

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
 *  Filename:     CGMSValueConfig
 *  Description:  Class for Output Writer Config Data
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValueConfig implements OutputWriterConfigData
{

    String key;
    String value;
    String valueRaw;

    public CGMSValueConfig(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getDataKey() {
        return key;
    }

    public String getDataValue() {
        return this.value;
    }

    public String getDataValueRaw() {
        return this.value;
    }

    public boolean isDataValueBG() {
        return false;
    }
}
