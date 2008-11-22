package ggc.cgm.data.defs;



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
 *  Filename:     CGMEvents
 *  Description:  CGMS device events
 * 
 *  Author: Andy {andy@atech-software.com}
 */

//IMPORTANT NOTICE: 
//This class is not implemented yet, all existing methods should be rechecked (they were copied from similar 
//class, with different type of data.


public class CGMEvents
{

    
    // start / end
    /**
     * 
     */
    public static final int CGM_EVENT_POWER_DOWN = 22;
    /**
     * 
     */
    public static final int CGM_EVENT_POWER_UP = 23;
    
    
    // date/time
    /**
     * 
     */
    public static final int CGM_EVENT_DATETIME_SET = 40;
    /**
     * 
     */
    public static final int CGM_EVENT_DATETIME_CORRECT = 41;
    /**
     * 
     */
    public static final int CGM_EVENT_DATETIME_CORRECT_TIME_SHIFT_BACK = 42;
    /**
     * 
     */
    public static final int CGM_EVENT_DATETIME_CORRECT_TIME_SHIFT_FORWARD = 43;
    
    
    
    

    
    
    
    

}
