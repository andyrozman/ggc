package ggc.pump.device.minimed;

import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.plugin.protocol.DatabaseProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.PumpBaseType;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     MinimedCareLink
 *  Description:  Minimed CareLink "device". This is for importing data from CareLink
 *                export file
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MinimedSPMPump extends MinimedSPM
{
    DataAccessPlugInBase m_da = null; //DataAccessPump.getInstance();
    int count_unk = 0; 
    private static Log log = LogFactory.getLog(MinimedSPM.class);

    /**
     * Constructor
     */
    public MinimedSPMPump(String filename, DataAccessPlugInBase da)
    {
        super(filename, da);
    }
    
    
    public void readData()
    {
        //this.readDailyTotals();
        //this.readPrimes();
        //readEvents();
        //readAlarms();
        readBoluses();
    }
    
    
    
    
    public void processDataEntry(MinimedSPMData data)
    {
        if (data.base_type==PumpBaseType.PUMP_DATA_ADDITIONAL_DATA)
        {
            
            
        }
        else
        {
            PumpDataH pdh = new PumpDataH();
            pdh.setDt_info(data.datetime);
            pdh.setBase_type(data.base_type);
            pdh.setSub_type(data.sub_type);

            pdh.setValue("" + data.getValue());
            
        }
        
        
        /*
        if ((data.base_type==PumpBaseType.PUMP_DATA_REPORT) || // PUMP_DATA_REPORT
            (data.base_type==PumpBaseType.PUMP_DATA_EVENT))  // primes
        {
            
            PumpDataH pdh = new PumpDataH();
            pdh.setDt_info(data.datetime);
            pdh.setBase_type(data.base_type);
            pdh.setSub_type(data.sub_type);
            
            pdh.setValue("" + data.value_dbl);
        }
        else if (data.base_type==PumpBaseType.PUMP_DATA_EVENT)  // primes
        {
            
            //if (sub_type == )
            
            
            
        }
            
        
        else if (data.base_type==4)
        {
            
        }*/
    }
    
    
    
    
    
    
    

}