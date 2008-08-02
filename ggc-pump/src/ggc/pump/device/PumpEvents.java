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


public class PumpEvents
{

    public final static int PUMP_ALARM_CARTRIDGE_LOW = 1;
    
    public final static int PUMP_ALARM_BATTERY_LOW = 2;
    public final static int PUMP_ALARM_REVIEW_DATETIME = 3;
    public final static int PUMP_ALARM_ALARM_CLOCK = 4;
    public final static int PUMP_ALARM_PUMP_TIMER = 5;
    public final static int PUMP_ALARM_TBR_CANCELED = 6;
    public final static int PUMP_ALARM_TBR_OVER = 7;
    public final static int PUMP_ALARM_BOLUS_CANCELED = 8;
    
    
    

}
