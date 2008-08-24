package ggc.cgm.data.defs;



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
 *  Filename: MeterInterface
 *  Purpose:  This is interface class, used for meters. It should be primary implemented by protocol class, and 
 *       protocol class should be used as super class for meter definitions. Each meter family "should" 
 *       have it's own super class and one class for each meter.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 */


public class CGMAlarms
{
     
    public static final int CGM_ALARM_HIGH_GLUCOSE = 101; //igh Glucose (101)
    public static final int CGM_ALARM_LOW_GLUCOSE = 102; //Low Glucose (102)
    public static final int CGM_ALARM_METER_BG_NOW = 104; //Meter BG Now (104)
    public static final int CGM_ALARM_BASE = 105; // (105)
    public static final int CGM_ALARM_CALIBRATION_ERROR = 106; //Calibration Error (106)
    public static final int CGM_ALARM_SENSOR_END = 107; //Sensor End (107)
    public static final int CGM_ALARM_CHNAGE_SENSOR = 108; //Change Sensor (108)
    public static final int CGM_ALARM_SENSOR_ERRO = 109; //Sensor Error (109)
    public static final int CGM_ALARM_WEAK_SIGNAL = 112; //Weak Signal (112)
    public static final int CGM_ALARM_LOST_SENSOR = 113; //Lost Sensor (113)
    public static final int CGM_ALARM_HIGH_GLUCOSE_PREDICTED = 114; //High Glucose Predicted (114)
    public static final int CGM_ALARM_LOW_GLUCOSE_PREDICTED = 115; //Low Glucose Predicted (115)

}