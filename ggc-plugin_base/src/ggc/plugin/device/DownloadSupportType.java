package ggc.plugin.device;

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
 *  Filename: DownloadSupportType
 *  Description:  This class contains definitions for Download support of device  
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DownloadSupportType
{
    
    /**
     * DownloadSupportType: Download possible
     */
    public static final int DOWNLOAD_YES = 0;
    
    /**
     * DownloadSupportType: Download not possible
     */
    public static final int DOWNLOAD_SUPPORT_NO = 1;
    
    /**
     * DownloadSupportType: Download not supported by device
     */
    public static final int DOWNLOAD_SUPPORT_NA_DEVICE = 2;


    /**
     * DownloadSupportType: Download not supported by device
     */
    public static final int DOWNLOAD_SUPPORT_NA_GENERIC_DEVICE = 3;


}
