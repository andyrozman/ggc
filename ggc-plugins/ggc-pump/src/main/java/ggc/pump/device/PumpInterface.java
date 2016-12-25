package main.java.ggc.pump.device;

import java.util.Hashtable;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import main.java.ggc.pump.data.defs.*;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     PumpInterface
 *  Description:  This is interface class, used for pumps. It should be primary implemented by abstract 
 *                protocol class. Each pump family "should" have it's own super class and one class for 
 *                each pump type instance.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public interface PumpInterface extends DeviceInterface
{

    // ************************************************
    // *** Meter Identification Methods ***
    // ************************************************

    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory
     * 
     * @return number
     */
    public int getMaxMemoryRecords();


    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues();


    /**
     * Get Alarm Mappings - Map pump specific alarms to Pump Tool specific 
     *     alarm codes
     * @return
     */
    public Hashtable<String, PumpAlarms> getAlarmMappings();


    /**
     * Get Event Mappings - Map pump specific events to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, PumpEventType> getEventMappings();


    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, PumpErrors> getErrorMappings();


    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, PumpBolusType> getBolusMappings();


    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, PumpReport> getReportMappings();


    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    public DeviceIdentification getDeviceInfo();


    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     * 
     * @return
     */
    public String getTemporaryBasalTypeDefinition();


    /**
     * Get Bolus Step (precission)
     * 
     * @return
     */
    public float getBolusStep();


    /**
     * Get Basal Step (precission)
     * 
     * @return
     */
    public float getBasalStep();


    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     * 
     * @return
     */
    public boolean arePumpSettingsSet();


    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    public int howManyMonthsOfDataStored();

}
