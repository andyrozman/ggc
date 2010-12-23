package ggc.cgms.data.defs;



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
 *  Filename:     CGMDataType  
 *  Description:  CGMS Data types, as used in database (undefined at this time)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

//IMPORTANT NOTICE: 
//This class is not implemented yet, all existing methods should be rechecked (they were copied from similar 
//class, with different type of data.


public class CGMSDataType
{

    /**
     * 
     */
    public static final int CGMS_BG_READING    = 1; 
    

    
    public static final int CGMS_METER_CALIBRATION   = 2; 
    
    /**
     * 
     */
    public static final int CGMS_DATA_EVENT    = 3; 
    /**
     * 
     */
    public static final int CGMS_DATA_ALARM    = 4;
    /**
     * 
     */
    public static final int CGMS_DATA_ERROR    = 5;
    

}
