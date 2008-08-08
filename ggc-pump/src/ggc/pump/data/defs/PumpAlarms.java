package ggc.pump.device;

import ggc.pump.manager.company.AbstractPumpCompany;


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


public class PumpAlarms
{

    public static final int PUMP_ERROR_CARTRIDGE_EMPTY  = 1; //__________________________151
    public static final int PUMP_ERROR_BATTERY_DEPLETED =2;//__________________________152
    public static final int PUMP_ERROR_AUTOMATIC_OFF =3; //_____________________________152
    public static final int PUMP_ERROR_OCCLUSION = 4;  // minimed 'No Delivery'=4, roche 'Occlusion'=4
    public static final int PUMP_ERROR_END_OF_OPERATION = 5; //__________________________154
    public static final int PUMP_ERROR_MECHANICAL_ERROR = 6; //_________________________155
    public static final int PUMP_ERROR_ELECTRONIC_ERROR = 7; //_________________________156
    public static final int PUMP_ERROR_POWER_INTERRUPT = 8; //__________________________157
    public static final int PUMP_ERROR_CARTRIDGE_ERROR = 10; // _________________________158
    public static final int PUMP_ERROR_SET_NOTz_PRIMED = 11; //___________________________158
    public static final int PUMP_ERROR_DATA_INTERRUPTED = 12; //_________________________159
    public static final int PUMP_ERROR_LANGUAGE_ERROR = 13; //__________________________160
    public static final int PUMP_ERROR_INSULIN_CHANGED = 14; //__________________________    
    
    
    
/*  
 * 
 *   Minimed

 No Delivery (4)

 
 
    Sensor Alert: High Glucose (101)
    Sensor Alert: Low Glucose (102)
    Sensor Alert: Meter BG Now (104)
    Sensor Alarm (105)
    Sensor Alert: Calibration Error (106)
    Sensor Alert: Sensor End (107)
    Sensor Alert: Change Sensor (108)
    Sensor Alert: Sensor Error (109)
    Sensor Alert: Weak Signal (112)
    Sensor Alert: Lost Sensor (113)
    Sensor Alert: High Glucose Predicted (114)
    Sensor Alert: Low Glucose Predicted (115)
*/
    

}
