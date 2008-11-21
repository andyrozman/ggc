package ggc.plugin.protocol;


import ggc.plugin.util.DataAccessPlugInBase;


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


// STUB ONLY. Not implemented
// Will be used by Animas (Pump) in future

public abstract class DatabaseProtocol 
{

    protected DataAccessPlugInBase m_da = null; 



    public DatabaseProtocol()
    {
    }

    public DatabaseProtocol(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }


    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_FILE_IMPORT;
    }
    


}
