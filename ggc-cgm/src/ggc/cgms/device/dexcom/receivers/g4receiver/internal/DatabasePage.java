package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
public class DatabasePage
{

    public DatabasePageHeader pageHeader = new DatabasePageHeader(); // 28
    public short[] pageData = new short[0]; // 500
    public short[] pageHeaderRaw = new short[0];


    public DatabasePage()
    {
    }


    public DatabasePageHeader getPageHeader()
    {
        return pageHeader;
    }


    public void setPageHeader(DatabasePageHeader pageHeader)
    {
        this.pageHeader = pageHeader;
    }


    public short[] getPageData()
    {
        return pageData;
    }


    public void setPageData(short[] pageData)
    {
        this.pageData = pageData;
    }

}
