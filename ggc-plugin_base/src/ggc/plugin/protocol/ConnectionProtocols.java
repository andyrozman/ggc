package ggc.plugin.protocol;

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


public class ConnectionProtocols
{
    
    public static final int PROTOCOL_NONE = 0;
    public static final int PROTOCOL_SERIAL_USBBRIDGE = 1;
    public static final int PROTOCOL_USB = 2;
    public static final int PROTOCOL_MASS_STORAGE_XML = 3;
    public static final int PROTOCOL_BLUE_TOOTH = 4;
    public static final int PROTOCOL_FILE_IMPORT = 5;
    
    public static String[] connectionProtocolDescription = { 
                             "PROT_NONE",
                             "PROT_SERIAL_BRIDGE",
                             "PROT_SERIAL_USB",
                             "PROT_MASS_STORAGE_XML",
                             "PROT_BLUE_TOOTH",
                             "PROT_FILE_IMPORT"
                           };
    

   
    
    
}

