package ggc.cgms.device.dexcom.receivers.data;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for Pump devices)
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
 *  Filename:     FRC_MinimedCarelink
 *  Description:  Minimed Carelink File Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class ReceiverDownloadDataConfig
{
    private String key;
    private String value;
    private boolean canBeTranslated;

    public ReceiverDownloadDataConfig(String key, String value, boolean canBeTranslated)
    {
        this.key = key;
        this.value = value;
        this.canBeTranslated = canBeTranslated;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.key + " = " + this.value;
    }

    public boolean isCanBeTranslated()
    {
        return canBeTranslated;
    }

    public void setCanBeTranslated(boolean canBeTranslated)
    {
        this.canBeTranslated = canBeTranslated;
    }

}
