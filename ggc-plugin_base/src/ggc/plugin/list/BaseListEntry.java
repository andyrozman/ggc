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
 *  Filename: GGCTreeRoot
 *  Purpose:  Used for holding tree information for nutrition and meals
 *
 *  Author:   andyrozman
 */

package ggc.plugin.list;




public class BaseListEntry  
{
    
    public static final int STATUS_NONE = 0;
    public static final int STATUS_DONE = 1;
    public static final int STATUS_PLANNED = 2;
    public static final int STATUS_NOTPLANNED = 3;
    
    
    public String name;
    public String page;
    public int status;
    

    public BaseListEntry()
    {
    }
    

    public BaseListEntry(String name, String page, int status)
    {
        this.name = name;
        this.page = page;
        this.status = status;
    }
    
    
    
}
