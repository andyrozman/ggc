package ggc.plugin.util;

import java.util.Comparator;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class TimeZoneComparator implements Comparator<String>
{
 
    
    /*
     * Compare two TimeZones.
     * 
     * GMT- (12->1) < GMT < GMT+ (1->12)
     * 
     * @return +1 if less, 0 if same, -1 if greater
     */
    public final int compare(String pFirst, String pSecond)
    {

        // System.out.println(pFirst + " " + pSecond);

        if (areSameType(pFirst, pSecond))
        {
            return ((pFirst.compareTo(pSecond)) * typeChanger(pFirst, pSecond));
        }
        else
        {
            if ((pFirst.startsWith("(GMT-"))) // && (second.contains("(GMT)")))
            {
                return -1;
            }
            else if ((pFirst.startsWith("(GMT)"))) // &&
                                                   // (second.contains("(GMT)"
                                                   // )))
            {
                if (pSecond.startsWith("(GMT-"))
                    return 1;
                else
                    return -1;
            }
            else
            {
                return 1;
            }

        }
    } // end compare

    public int typeChanger(String first, String second)
    {
        if ((first.startsWith("(GMT-")) && (second.startsWith("(GMT-")))
            return -1;
        else
            return 1;
    }

    public boolean areSameType(String first, String second)
    {
        if (((first.startsWith("(GMT+")) && (second.startsWith("(GMT+")))
                || ((first.startsWith("(GMT)")) && (second.startsWith("(GMT)")))
                || ((first.startsWith("(GMT-")) && (second.startsWith("(GMT-"))))
        {
            return true;
        }
        else
            return false;

    }

}
