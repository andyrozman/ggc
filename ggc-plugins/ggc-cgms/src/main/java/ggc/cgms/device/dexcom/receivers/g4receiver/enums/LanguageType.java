package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

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
 *  Filename:     Dexcom 7
 *  Description:  Dexcom 7 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */
public enum LanguageType
{
    Czech(0x405, "LANGUAGE_CZECH"), //
    Dutch(0x413, "LANGUAGE_DUTCH"), //
    English(0x409, "LANGUAGE_ENGLISH"), //
    Finnish(0x40b, "LANGUAGE_FINNISH"), //
    French(0x40c, "LANGUAGE_FRENCH"), //
    French_Canada(0xc0c, "LANGUAGE_FRENCH_CANADA"), //
    German(0x407, "LANGUAGE_GERMAN"), //
    Italian(0x410, "LANGUAGE_ITALIAN"), //
    None(0, "NONE"), //
    Polish(0x415, "LANGUAGE_POLISH"), //
    Portugese_Brazil(0x416, "LANGUAGE_PORTUGESE_BRAZIL"), //
    Spanish(0x40a, "LANGUAGE_SPANISH"), //
    Swedish(0x41d, "LANGUAGE_SWEDISH"); //

    private int value;
    private String description;
    private static HashMap<Integer, LanguageType> map = new HashMap<Integer, LanguageType>();

    static
    {
        for (LanguageType el : values())
        {
            map.put(el.getValue(), el);
        }
    }


    LanguageType(int value, String description)
    {
        this.value = value;
        this.description = description;
    }


    public int getValue()
    {
        return value;
    }


    public void setValue(int value)
    {
        this.value = value;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public static LanguageType getEnum(int value)
    {
        return map.get(value);
    }
}
