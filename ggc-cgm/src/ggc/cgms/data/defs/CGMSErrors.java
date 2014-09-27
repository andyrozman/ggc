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
 *  Filename:     CGMErrors  
 *  Description:  Errors produced by CGMS device
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// IMPORTANT NOTICE:
// This class is not implemented yet, all existing methods should be rechecked
// (they were copied from similar
// class, with different type of data.

public class CGMSErrors
{
    /**
     * 
     */
    public static final int CGM_ERROR_UNKNOWN_ERROR = -1; // __________________________151

    /**
     * 
     */
    public static final int CGM_ERROR_BATTERY_DEPLETED = 2;// __________________________152
    /**
     * 
     */
    public static final int CGM_ERROR_AUTOMATIC_OFF = 3; // _____________________________152
    /**
     * 
     */
    public static final int CGM_ERROR_END_OF_OPERATION = 5; // __________________________154
    /**
     * 
     */
    public static final int CGM_ERROR_MECHANICAL_ERROR = 6; // _________________________155
    /**
     * 
     */
    public static final int CGM_ERROR_ELECTRONIC_ERROR = 7; // _________________________156
    /**
     * 
     */
    public static final int CGM_ERROR_POWER_INTERRUPT = 8; // __________________________157
    /**
     * 
     */
    public static final int CGM_ERROR_DATA_INTERRUPTED = 12; // _________________________159
    /**
     * 
     */
    public static final int CGM_ERROR_LANGUAGE_ERROR = 13; // __________________________160

    // sensor

    /**
     * 
     */
    public static final int CGMS_ERROR_SENSOR_CALIBRATION_ERROR = 40; // ___________________160

    /**
     * 
     */
    public static final int CGMS_ERROR_SENSOR_END_OF_LIFE = 41; // ___________________160

    /**
     * 
     */
    public static final int CGMS_ERROR_SENSOR_BAD = 42; // ___________________160

    /*
     * Minimed
     * No Delivery (4)
     * Sensor Alert: High Glucose (101)
     * Sensor Alert: Low Glucose (102)
     * Sensor Alert: Meter BG Now (104)
     * Sensor Alarm (105)
     * Sensor Alert: Calibration Error (106)
     * Sensor Alert: Sensor End (107)
     * Sensor Alert: Change Sensor (108)
     * Sensor Alert: Sensor Error (109)
     * Sensor Alert: Weak Signal (112)
     * Sensor Alert: Lost Sensor (113)
     * Sensor Alert: High Glucose Predicted (114)
     * Sensor Alert: Low Glucose Predicted (115)
     */

}
