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
 *  Filename:     CGMEvents
 *  Description:  CGMS device events
 * 
 *  Author: Andy {andy@atech-software.com}
 */

//IMPORTANT NOTICE: 
//This class is not implemented yet, all existing methods should be rechecked (they were copied from similar 
//class, with different type of data.


public class CGMSEvents
{

    
    // start / end
    /**
     * 
     */
    public static final int CGMS_EVENT_CONTROLER_POWER_DOWN = 1;
    /**
     * 
     */
    public static final int CGMS_EVENT_CONTROLER_POWER_UP = 2;
    
    
    // date/time
    /**
     * 
     */
    public static final int CGMS_EVENT_DATETIME_SET = 3;
    /**
     * 
     */
    public static final int CGMS_EVENT_DATETIME_CHANGED = 4;
    
    
    
    
    // SENSOR - basic functions

    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_POWER_DOWN = 40;

    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_POWER_UP = 41;
    
    
    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_LOST = 42;
    
    
    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_CALIBRATION_METER_BG_NOW = 50;
    

    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_CALIBRATION_WAITING = 51;
    
    
    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_CALIBRATION_FACTOR = 52;
    
    
    // sensor init/operation

    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_PRE_INIT = 60;
    
    
    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_INIT = 61;


    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_BURST = 62;
    

    /**
     * 
     */
    public static final int CGMS_EVENT_SENSOR_WEAK_SIGNAL = 63;
    
    
    // Data 
    /**
     * 
     */
    public static final int CGMS_EVENT_DATA_LOW_BG = 64; 
    
}
